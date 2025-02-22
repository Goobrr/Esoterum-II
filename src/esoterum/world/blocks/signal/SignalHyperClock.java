package esoterum.world.blocks.signal;

import arc.Core;
import arc.graphics.g2d.*;
import arc.math.geom.Rect;
import esoterum.graph.*;
import mindustry.world.Block;

public class SignalHyperClock extends SignalBlock
{
    public SignalHyperClock(String name)
    {
        super(name);

        rotate = false;
        hasGraph = false;

        alwaysReplace = true;
        replaceable = true;
    }

    @Override
    public void load()
    {
        super.load();

        baseRegion = Core.atlas.find(name, "eso-default-gate-base");
    }

    @Override
    public boolean canReplace(Block other)
    {
        return super.canReplace(other) || other instanceof SignalSwitch;
    }

    public class SignalHyperClockBuild extends SignalBuild
    {
        private boolean on = true;

        @Override
        public void updateSignal(boolean update)
        {
            SignalGraph.graph.setVertexAugmentation(this.v[0], signal[0] = on ? 1 : 0);
            on = !on;
        }

        @Override
        public void draw()
        {
            Draw.rect(uiIcon, x, y, rotation * 90);
        }

        @Override
        public void drawSignalRegions(Rect camera)
        {
        }
    }
}
