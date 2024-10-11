package esoterum.world.blocks.signal;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import esoterum.*;
import esoterum.graph.ConnVertex;
import esoterum.graph.SignalGraph;
import mindustry.Vars;
import mindustry.game.EventType.WorldLoadEvent;
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
    public int vertexCount = 1;
    public int[] conns = new int[0];
    public boolean hasGraph = true;
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

    public void setConns(int... vc){
        conns = vc;
    }

    @Override
    public void load(){
        super.load();

        uiIcon = fullIcon = Core.atlas.find(name + "-full");

        bottomRegion = Core.atlas.find(name + "-bottom", "eso-none");

        baseRegion = Core.atlas.find(name + "-base", "eso-base-square");

        signalRegion = Core.atlas.find(name + "-signal", "eso-none");

        inputSignalRegions = new TextureRegion[size * 4];
        outputSignalRegions = new TextureRegion[size * 4];
        for(int i=0;i<size*4;i++){
            if (inputs[i] > 0) inputSignalRegions[i] = Core.atlas.find(name + "-input-" + i, "eso-none");
            if (outputs[i] > 0) outputSignalRegions[i] = Core.atlas.find(name + "-output-" + i, "eso-none");
        }
    }

    @Override
    public void init(){
        super.init();

        if(inputs == null){
            inputs = new int[size * 4];
            for(int i = 0; i < size * 4; i++){
                inputs[i] = 1;
            }
        }
        if(outputs == null){
            outputs = new int[size * 4];
            for(int i = 0; i < size * 4; i++){
                outputs[i] = 1;
            }
        }
        if(conns == null){
            conns = new int[size * 4];
            for(int i = 0; i < size * 4; i++){
                conns[i] = 0;
            }
        }
    }

    public class SignalBuild extends Building {
        public ConnVertex[] v = new ConnVertex[vertexCount];
        public int[] signal = new int[vertexCount];
        public boolean[] active = new boolean[size*4];

        public int[] inputs(){
            return inputs;
        }

        public int[] outputs(){
            return outputs;
        }

        public int[] conns(){
            return conns;
        }

        public int size(){
            return size;
        }

        @Override
        public void created(){
            super.created();
            if(!this.block.rotate) rotation(0);

            for (int i=0;i<vertexCount;i++){
                if (v[i] == null) SignalGraph.addVertex(this, i);
            }

            updateEdges();
        }
        
        public void updateEdges(){
            for (int i=0;i<vertexCount;i++) SignalGraph.clearEdges(v[i]);
            for (int i=0;i<size*4;i++){
                active[i] = false;
                Vec2 offset = EdgeUtils.getEdgeOffset(size, i, rotation);
                Vec2 sideOffset = EdgeUtils.getEdgeOffset(1, i/size, rotation);
                if(Vars.world.build((int)(x/8 + offset.x + sideOffset.x), (int)(y/8 + offset.y + sideOffset.y)) instanceof SignalBuild b){
                    int index = EdgeUtils.getOffsetIndex(b.size(), x/8 + offset.x - b.x/8, y/8 + offset.y - b.y/8, b.rotation);
                    if ((b.inputs()[index] & outputs[i]) == 1 || (b.outputs()[index] & inputs[i]) == 1){
                        SignalGraph.addEdge(v[conns[i]], b.v[b.conns()[index]]);
                        active[i] = true;
                    }
                }
            }
        }

        @Override
        public void onRemoved(){
            for (int i=0;i<vertexCount;i++) SignalGraph.removeVertex(this, i);
            super.onRemoved();
        }

        @Override
        public void updateTile(){
            for (int i=0;i<vertexCount;i++) signal[i] = (int)SignalGraph.graph.getComponentAugmentation(v[i]);
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
            updateEdges();
        }

        @Override
        public void updateProximity(){
            super.updateProximity();
            updateEdges();
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
            Draw.color(signal[0] == 1 ? Pal.accent : Color.white);

            Draw.rect(signalRegion, x, y, rotation * 90);

            for(int i=0;i<size*4;i++){
                if (active[i]){
                    Draw.color(signal[conns[i]] == 1 ? Pal.accent : Color.white);
                    if(inputs[i] == 1) Draw.rect(inputSignalRegions[i], x, y, rotation * 90);
                    if(outputs[i] == 1) Draw.rect(outputSignalRegions[i], x, y, rotation * 90);
                }
            }
        }

        public void debugDraw(){
            Draw.blend(Blending.additive);
            Draw.alpha(0.5f);
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
            return 3;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            for(int i=0;i<vertexCount;i++) write.i(signal[i]);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            if(revision >= 3){
                for(int i=0;i<vertexCount;i++) signal[i] = read.i();
            } else if(revision >= 2){

            } else if(revision >= 1 && hasGraph){
                read.i();
                read.bool();
            }
        }
    }
}
