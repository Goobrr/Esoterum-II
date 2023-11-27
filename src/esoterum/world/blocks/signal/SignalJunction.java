package esoterum.world.blocks.signal;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.graphics.*;

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

            for(int index : outputs()){
                boolean signal = signalAtOutput(index);
                for(Building b : proximity()){
                    if(b instanceof SignalBuild s){
                        int i = getInputTo(index, s);
                        if(i == -1) continue;
                        s.acceptSignal(this, signal);
                    }
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

        @Override
        public boolean signalAtOutput(int index){
            return signalAtInput(IOMap.get(index));
        }
    }
}
