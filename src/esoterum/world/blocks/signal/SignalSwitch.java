package esoterum.world.blocks.signal;

import arc.Core;
import arc.graphics.g2d.*;
import arc.util.io.*;
import esoterum.graph.SignalGraph;

public class SignalSwitch extends SignalBlock
{
    public TextureRegion switchOnRegion, switchOffRegion;

    public SignalSwitch(String name)
    {
        super(name);

        rotate = false;
        configurable = true;
        hasGraph = false;

        config(Boolean.class, (b, s) -> {
            ((SignalSwitchBuild) b).enabled = s;
            SignalGraph.graph.setVertexAugmentation(((SignalSwitchBuild) b).v[0], b.enabled ? 0 : 1);
        });
    }

    @Override
    public void load()
    {
        super.load();

        switchOnRegion = Core.atlas.find(name + "-on");
        switchOffRegion = Core.atlas.find(name + "-off");
    }

    public class SignalSwitchBuild extends SignalBuild
    {
        @Override
        public boolean configTapped()
        {
            configure(!enabled);
            return false;
        }

        @Override
        public void write(Writes write)
        {
            super.write(write);
            write.bool(enabled);
        }

        @Override
        public void draw()
        {
            Draw.rect(baseRegion, x, y);
            Draw.rect(enabled ? switchOffRegion : switchOnRegion, x, y);
        }

        @Override
        public void read(Reads read, byte revision)
        {
            super.read(read, revision);
            enabled = read.bool();
            SignalGraph.graph.setVertexAugmentation(v[0], enabled ? 0 : 1);
        }
    }
}
