package esoterum.world.blocks.signal;

import arc.Core;
import arc.graphics.g2d.*;
import arc.math.geom.Rect;
import arc.util.io.*;
import esoterum.graph.*;
import mindustry.world.Block;

public class SignalSwitch extends SignalBlock
{
    public TextureRegion switchOnRegion, switchOffRegion;

    public SignalSwitch(String name)
    {
        super(name);

        rotate = false;
        configurable = true;
        hasGraph = false;

        alwaysReplace = true;
        replaceable = true;

        config(Boolean.class, (SignalSwitchBuild b, Boolean s) -> {
            b.enabled = s;
        });

        config(Object[].class, (SignalBuild tile, Object[] p) -> {
            if (p[0] instanceof Long l)
            {
                tile.shielding = l;
                SignalGraph.events.add(new GraphEvent.updateEvent(tile));
            }

            if (p.length > 1 && p[1] instanceof Boolean b) tile.enabled = b;
        });
    }

    @Override
    public boolean canReplace(Block other)
    {
        return super.canReplace(other) || other instanceof SignalSwitch;
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
        public void updateSignal()
        {
            if ((enabled ? 0 : -1) != signal[0])
                SignalGraph.graph.setVertexAugmentation(this.v[0], signal[0] = enabled ? 0 : -1);
        }

        @Override
        public Object[] config()
        {
            return new Object[]{shielding, enabled};
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
        public void drawSignalRegions(Rect camera)
        {
        }

        @Override
        public void drawShieldRegions()
        {
        }

        @Override
        public void read(Reads read, byte revision)
        {
            super.read(read, revision);
            enabled = read.bool();
            signal[0] = 0;
            SignalGraph.events.add(new GraphEvent.updateEvent(this));
        }
    }
}
