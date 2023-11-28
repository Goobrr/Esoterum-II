package esoterum.world.blocks.signal;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.world.*;

// TODO
// 1. un-hack signal propagation
// 2. >1 size compatibility

public class SignalJunction extends SignalBlock{

    public IntIntMap IOMap;
    public TextureRegion signalRegion1, signalRegion2;
    public SignalJunction(String name){
        super(name);

        canFloodfill = false;
        hasGraph = false;
        rotate = false;
    }

    public void mapIO(int... indices){
        IOMap = new IntIntMap();

        for(int i = 0; i < indices.length; i += 2){
            IOMap.put(indices[i], indices[i + 1]);
            IOMap.put(indices[i + 1], indices[i]);
        }
    };

    @Override
    public void load(){
        super.load();

        signalRegion1 = Core.atlas.find(name + "-signal-1");
        signalRegion2 = Core.atlas.find(name + "-signal-2");
    }

    public class SignalJunctionBuild extends SignalBuild {

        @Override
        public void update(){
            super.update();

            // something really wacky is going on and this is the only way i can seem to fix it.
            boolean[] tmp = new boolean[4];
            for(int i = 0; i < 4; i++){
                tmp[i] = signalAtInput(i);
            }

            for(int i = 0; i < 4; i++){
                Point2 p = getEdges()[i];
                int op_i = new int[]{2, 3, 0, 1}[i];
                if(Vars.world.build(tile.x + p.x, tile.y + p.y) instanceof SignalBuild b){
                    b.acceptSignal(this, tmp[op_i]);
                }
            }


        }

        @Override
        public void drawSignalRegions(){
            super.drawSignalRegions();

            Draw.color(signalAtInput(0) || signalAtInput(2) ? Pal.accent : Color.white);
            Draw.rect(signalRegion1, x, y);

            Draw.color(signalAtInput(1) || signalAtInput(3) ? Pal.accent : Color.white);
            Draw.rect(signalRegion2, x, y);
        }

        // hack to prevent floodfilling
        @Override
        public void onRemoved(){
            for(Point2 p : Edges.getEdges(size)){
                if(Vars.world.build(tile.x + p.x, tile.y + p.y) instanceof SignalBuild b && b.signalGraph != null){
                    if(b.hasGraph() && (canConnect(b) | b.canConnect(this))){
                        b.acceptSignal(this, false);
                    }
                }
            }
        }

        @Override
        public boolean signalAtOutput(int index){
            boolean signal = signalAtInput(IOMap.get(index));
            return signal;
        }
    }
}
