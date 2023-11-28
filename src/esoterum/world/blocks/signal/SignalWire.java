package esoterum.world.blocks.signal;


import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.graphics.*;

public class SignalWire extends SignalBlock{
    public SignalWire(String name){
        super(name);
    }

    public class SignalWireBuild extends SignalBuild{
        @Override
        public boolean signalAtOutput(int index){
            for(int i : outputs()){
                if(i == index & (signalGraph != null && signalGraph.signal)) return true;
            }
            return false;
        }

        public void drawSignalRegions(){
            Draw.color(signalGraph != null && signalGraph.signal ? Pal.accent : Color.white);

            Draw.rect(signalRegion, x, y, rotation * 90);

            for(int i : outputs()){
                for(Building b : proximity()){
                    if(b instanceof SignalBuild s){
                        int j = getInputTo(i, s);
                        if(j != -1){
                            Draw.rect(outputSignalRegions[i], x, y, rotation * 90);
                        }
                    }
                }
            }

            for(int i : inputs()){
                for(Building b : proximity()){
                    if(b instanceof SignalBuild s){
                        int j = getOutputFrom(i, s);
                        if(j != -1){
                            Draw.color(s.signalAtOutput(j) ? Pal.accent : Color.white);
                            Draw.rect(inputSignalRegions[i], x, y, rotation * 90);
                        }
                    }
                }
            }
        }
    }
}
