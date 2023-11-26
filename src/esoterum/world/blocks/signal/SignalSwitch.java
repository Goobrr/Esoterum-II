package esoterum.world.blocks.signal;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.io.*;

public class SignalSwitch extends SignalSource{
    public TextureRegion switchOnRegion, switchOffRegion;
    public SignalSwitch(String name){
        super(name);

        rotate = false;
        configurable = true;

        config(Boolean.class, (b, s) -> {
            ((SignalSwitchBuild) b).signal = s;
        });
    }

    @Override
    public void load(){
        super.load();

        switchOnRegion = Core.atlas.find(name + "-on");
        switchOffRegion = Core.atlas.find(name + "-off");
    }

    public class SignalSwitchBuild extends SignalSourceBuild {
        @Override
        public boolean configTapped(){
            configure(!signal);
            return false;
        }

        @Override
        public boolean signalAtOutput(int index){
            return signal;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.bool(signal);
        }

        @Override
        public void draw(){
            Draw.rect(baseRegion, x, y);
            Draw.rect(signal ? switchOnRegion : switchOffRegion, x, y);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            signal = read.bool();
        }
    }
}
