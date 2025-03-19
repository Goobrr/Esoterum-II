package esoterum.world.blocks.signal;

import arc.graphics.g2d.Draw;
import arc.math.geom.Rect;
import esoterum.graph.SignalGraph;
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
    public boolean canReplace(Block other)
    {
        return super.canReplace(other) || other instanceof SignalSwitch;
    }

    public class SignalHyperClockBuild extends SignalBuild
    {
        public boolean on = true;

        @Override
        public void updateSignal()
        {
            SignalGraph.graph.setVertexAugmentation(this.v[0], signal[0] = on ? -1 : 0);
            on = !on;
        }

        @Override
        public void draw()
        {
            Draw.rect(baseRegion, x, y, rotation * 90);
        }

        @Override
        public void drawSignalRegions(Rect camera)
        {
        }
    }
}
