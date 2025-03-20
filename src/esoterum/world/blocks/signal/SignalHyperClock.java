package esoterum.world.blocks.signal;

import esoterum.graph.SignalGraph;
import mindustry.world.Block;

public class SignalHyperClock extends SignalGate
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

    public class SignalHyperClockBuild extends SignalGateBuild
    {
        public boolean on = true;

        @Override
        public void updateSignal()
        {
            SignalGraph.graph.setVertexAugmentation(this.v[0], signal[0] = (color == 8 ? 0 : (color << 16)) | (on ? 0xFFFF : 0));
            on = !on;
        }
    }
}
