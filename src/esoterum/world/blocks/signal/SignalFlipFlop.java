package esoterum.world.blocks.signal;

import esoterum.graph.SignalGraph;

public class SignalFlipFlop extends SignalGate
{
    public SignalFlipFlop(String name)
    {
        super(name);
    }

    public class SignalFlipFlopBuild extends SignalGateBuild
    {
        public int lastSignal[];
        
        @Override
        public void updateSignal(boolean update) {
            lastSignal = signal.clone();
            if (update) for (int i = 0; i < vertexCount; i++)
                r[i] = SignalGraph.graph.vertexInfo.get(v[i]).vertex.arbitraryVisit.root();
            for (int i = 0; i < vertexCount; i++) if (r[i] != null) signal[i] = (int) r[i].augmentation;
            if (signal[1] == 1 && lastSignal[1] == 0) SignalGraph.graph.setVertexAugmentation(v[0], 1 - signal[0]);
            else if (signal[2] == 1 && lastSignal[2] == 0) SignalGraph.graph.setVertexAugmentation(v[0], 1 - signal[0]);
            else if (signal[3] == 1 && lastSignal[3] == 0) SignalGraph.graph.setVertexAugmentation(v[0], 1 - signal[0]);
        }
    }
}
