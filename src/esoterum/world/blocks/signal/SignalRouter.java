package esoterum.world.blocks.signal;

import esoterum.graph.SignalGraph;

public class SignalRouter extends SignalBlock
{
    public SignalRouter(String name)
    {
        super(name);
    }

    public class SignalRouterBuild extends SignalBuild
    {
        @Override
        public void updateSignal(boolean update)
        {
            super.updateSignal(update);
            for (int i = 0; i < size * 4; i++)
                if (outputs[i] == 1)
                {
                    if (signal[2] != signal[i]) SignalGraph.graph.setVertexAugmentation(v[i], signal[2]);
                }
        }
    }
}
