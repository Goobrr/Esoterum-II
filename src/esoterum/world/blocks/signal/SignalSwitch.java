package esoterum.world.blocks.signal;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.io.*;
import esoterum.graph.SignalGraph;

public class SignalSwitch extends SignalBlock{
    public TextureRegion switchOnRegion, switchOffRegion;
    public SignalSwitch(String name){
        super(name);

        rotate = false;
        configurable = true;
        hasGraph = false;

        config(Integer.class, (b, s) -> {
            ((SignalSwitchBuild) b).signal[0] = s;
        });
    }

    @Override
    public void load(){
        super.load();

        switchOnRegion = Core.atlas.find(name + "-on");
        switchOffRegion = Core.atlas.find(name + "-off");
    }

    public class SignalSwitchBuild extends SignalBuild {
        @Override
        public boolean configTapped(){
            configure(1-signal[0]);
            return false;
        }

        @Override
        public void updateTile(){
            SignalGraph.graph.setVertexAugmentation(v[0], signal[0]);
            super.updateTile();
        }
        
        @Override
        public void write(Writes write){
            super.write(write);
            write.bool(signal[0] == 1);
        }

        @Override
        public void draw(){
            Draw.rect(baseRegion, x, y);
            Draw.rect(signal[0] == 1 ? switchOnRegion : switchOffRegion, x, y);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            signal[0] = read.bool() ? 1 : 0;
        }
    }
}
