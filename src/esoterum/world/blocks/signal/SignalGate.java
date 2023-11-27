package esoterum.world.blocks.signal;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;

public class SignalGate extends SignalSource{
    public Boolf<SignalBuild> function;
    public SignalGate(String name){
        super(name);

        rotate = true;
    }

    @Override
    public void load(){
        super.load();

        baseRegion = Core.atlas.find(name + "-base", "eso-default-gate-base");

        outputSignalRegions = new TextureRegion[size * 4];
        for(int i : outputs){
            outputSignalRegions[i] = Core.atlas.find(name + "-output-" + i, "eso-default-gate-output");
        }
    }

    public class SignalSimpleLogicGateBuild extends SignalSourceBuild {
        @Override
        public boolean getSignal(){
            return function.get(this);
        }

        @Override
        public void drawSignalRegions(){
            Draw.color(signal ? Pal.accent : Color.white);

            Draw.rect(signalRegion, x, y, rotation * 90);
            for(int i : outputs()){
                Draw.rect(outputSignalRegions[i], x, y, rotation * 90);
            }

            for(int i : inputs()){
                Draw.color(signalAtInput(i) ? Pal.accent : Color.white);
                Draw.rect(inputSignalRegions[i], x, y, rotation * 90);
            }
        }
    }
}
