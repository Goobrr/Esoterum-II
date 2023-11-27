package esoterum.world.blocks.signal;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import esoterum.*;
import esoterum.graph.SignalGraph;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.graphics.*;
import mindustry.type.Category;
import mindustry.world.*;

public class SignalBlock extends Block {

    public TextureRegion bottomRegion, baseRegion, signalRegion;

    public TextureRegion[] inputSignalRegions, outputSignalRegions;

    public boolean debugDraw = false;

    public int[] inputs = new int[0];
    public int[] outputs = new int[0];
    public boolean hasGraph = true;
    public boolean canFloodfill = true;
    public SignalBlock(String name) {
        super(name);

        update = true;
        solid = true;
        size = 1;
        health = 60;

        category = Category.logic;
    }

    public void setInputs(int... indices){
        inputs = indices;
    }

    public void setOutputs(int... indices){
        outputs = indices;
    }

    @Override
    public void load(){
        super.load();

        bottomRegion = Core.atlas.find(name + "-bottom", "eso-none");

        baseRegion = Core.atlas.find(name + "-base", "eso-base-square");

        signalRegion = Core.atlas.find(name + "-signal", "eso-none");

        inputSignalRegions = new TextureRegion[size * 4];
        for(int i : inputs){
            inputSignalRegions[i] = Core.atlas.find(name + "-input-" + i, "eso-none");
        }

        outputSignalRegions = new TextureRegion[size * 4];
        for(int i : outputs){
            outputSignalRegions[i] = Core.atlas.find(name + "-output-" + i, "eso-none");
        }
    }

    @Override
    public void init(){
        super.init();

        if(inputs == null){
            inputs = new int[size * 4];
            for(int i = 0; i < size * 4; i++){
                inputs[i] = i;
            }
        }
        if(outputs == null){
            outputs = new int[size * 4];
            for(int i = 0; i < size * 4; i++){
                outputs[i] = i;
            }
        }
    }


    // TODO
    // 1. Probably turn some of this into an interface instead.
    // 2. Optimize O(n^2) methods.
    // 3. Stop the flickering

    public class SignalBuild extends Building {
        public SignalGraph signalGraph;

        public boolean hasGraph(){
            return hasGraph;
        }

        public int[] inputs(){
            return inputs;
        }

        public int[] outputs(){
            return outputs;
        }

        public boolean canFloodfill(){
            return canFloodfill;
        }

        @Override
        public void created(){
            super.created();
            if(!this.block.rotate) rotation(0);

            if(hasGraph && signalGraph == null){
                signalGraph = new SignalGraph();
                signalGraph.add(this);

                for(Point2 p : getEdges()){
                    if(Vars.world.build(tile.x + p.x, tile.y + p.y) instanceof SignalBuild b){
                        if(b.hasGraph()){
                            if(canConnect(b) | b.canConnect(this)){
                                if(b.signalGraph != null){
                                    b.signalGraph.merge(signalGraph);
                                }else{
                                    signalGraph.add(b);
                                }
                            }
                        }
                    }
                }
            }
        }

        // O(n^2) but I don't give a shit.
        public boolean canConnect(SignalBuild other){
            for(int index : outputs){
                Vec2 a = getConnector(index);
                for(int otherIndex : other.inputs()){
                    Vec2 b = other.getConnector(otherIndex);

                    if(a.x == b.x && a.y == b.y){
                        return true;
                    }
                }
            }

            return false;
        }

        public Vec2 getConnector(int index){
            Tmp.v1.set(EdgeUtils.getEdgeOffset(size, index, rotation));
            Tmp.v1.add(tile.x, tile.y);

            return new Vec2(Tmp.v1.x * 8, Tmp.v1.y * 8);
        }

        public int getOutputFrom(int index, SignalBuild other){
            Vec2 a = getConnector(index);
            for(int otherIndex : other.outputs()){
                Vec2 b = other.getConnector(otherIndex);

                if(a.x == b.x && a.y == b.y){
                    return otherIndex;
                }
            }

            return -1;
        };

        public int getInputTo(int index, SignalBuild other){
            Vec2 a = getConnector(index);
            for(int otherIndex : other.inputs()){
                Vec2 b = other.getConnector(otherIndex);

                if(a.x == b.x && a.y == b.y){
                    return otherIndex;
                }
            }

            return -1;
        };

        public void floodfill(Building source){
            for(Point2 p : getEdges()){
                if(Vars.world.build(tile.x + p.x, tile.y + p.y) instanceof SignalBuild b && b != source){
                    if(b.canFloodfill() && (canConnect(b) | b.canConnect(this))){
                        if(b.signalGraph != null){
                            b.signalGraph.merge(signalGraph);
                        }else{
                            signalGraph.add(b);
                            b.floodfill(source);
                        }
                    }
                }
            }
        }

        @Override
        public void onRemoved(){
            for(Point2 p : Edges.getEdges(size)){
                if(Vars.world.build(tile.x + p.x, tile.y + p.y) instanceof SignalBuild b && b.signalGraph != null){
                    if(canConnect(b) | b.canConnect(this)){
                        b.signalGraph.lastSignal = false;
                        b.signalGraph.nextSignal = false;
                    }
                }
            }

            if(signalGraph != null && hasGraph){
                signalGraph.delete();
                for(Point2 p : getEdges()){
                    if(Vars.world.build(tile.x + p.x, tile.y + p.y) instanceof SignalBuild b){
                        if(canConnect(b) | b.canConnect(this)){

                            b.signalGraph = new SignalGraph();
                            b.signalGraph.add(b);

                            b.floodfill(this);
                        }
                    }
                }
            }
            super.onRemoved();
        }

        // Signaling Logic

        public boolean signalAtOutput(int index){
            return true;
        }

        public boolean signalAtInput(int index){
            for(Building b : proximity()){
                if(b instanceof SignalBuild s){
                    int i = getOutputFrom(index, s);
                    if(i == -1) continue;
                    return s.signalAtOutput(i);
                }
            }

            return false;
        };

        public void sendSignal(int index, boolean signal){
            for(Building b : proximity()){
                if(b instanceof SignalBuild s){
                    int i = getInputTo(index, s);
                    if(i == -1) continue;
                    s.acceptSignal(this, signal);
                }
            }
        }

        public boolean acceptSignal(SignalBuild source, boolean signal){
            if(hasGraph && signalGraph != null && source.canConnect(this)){
                signalGraph.signal(signal);
                return true;
            }
            return false;
        }

        @Override
        public void displayBars(Table table){
            super.displayBars(table);

            table.row();
            table.table(t -> {
                t.left();
                t.label(() -> "Graph #" + (signalGraph != null ? signalGraph.getID() : -1));
                t.row();
                t.label(() -> "Signal: " + (signalGraph != null && signalGraph.lastSignal ? "1" : "0"));
                t.row();
                t.label(() -> "LastSignal: " + (signalGraph != null && signalGraph.lastSignal ? "1" : "0"));
                t.row();
                t.label(() -> "NextSignal: " + (signalGraph != null && signalGraph.nextSignal ? "1" : "0"));
                t.row();
                t.fill();
            }).grow();
        }

        @Override
        public void draw() {

            if(Esoterum.debug || debugDraw){
                debugDraw();
            }else{
                Draw.rect(bottomRegion, x, y);
                Draw.rect(baseRegion, x, y);

                drawSignalRegions();
            }
        }

        public void drawSignalRegions(){

        }

        public void debugDraw(){
            Draw.blend(Blending.additive);
            Draw.alpha(0.5f);
            if(hasGraph && signalGraph != null && signalGraph.lastSignal){
                Draw.color(Tmp.c1.set(Color.red).shiftHue(signalGraph.getID() * 15));
            }else{
                Draw.color(Pal.gray);
            }
            Fill.square(x, y, size * 4);
            Draw.blend();
            Draw.alpha(1);

            for(int index : outputs){
                Vec2 p = EdgeUtils.getEdgeOffset(size, index, rotation);

                DrawUtils.text((tile.x + p.x) * 8, (tile.y + p.y ) * 8, Pal.heal, String.valueOf(index));
            }

            for(int index : inputs){
                Vec2 p = EdgeUtils.getEdgeOffset(size, index, rotation);

                DrawUtils.text((tile.x + p.x) * 8, (tile.y + p.y ) * 8, Pal.accent, String.valueOf(index));
            }
        }

        @Override
        public byte version(){
            return 1;
        }

        @Override
        public void write(Writes write){
            super.write(write);

            if(hasGraph){
                Log.info("[" + pos() + "] Writing graph ID #" + signalGraph.getID());
                write.i(signalGraph.getID());
                write.bool(signalGraph.lastSignal);
                write.bool(signalGraph.nextSignal);
            }
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);

            if(revision >= 1 && hasGraph){
                int id = read.i();
                Log.info("[" + pos() + "] Read graph ID #" + id);
                SignalGraph graph = SignalGraph.getGraphByID(id);

                if(graph == null){
                    graph = new SignalGraph();
                    SignalGraph.mapID(id, graph.getID());

                    Log.info("[" + pos() + "] Initialized graph ID #" + graph.getID() + " mapped to ID #" + id);
                }

                signalGraph = graph;
                signalGraph.add(this);

                signalGraph.lastSignal = read.bool();
                signalGraph.nextSignal = read.bool();
            }
        }
    }
}
