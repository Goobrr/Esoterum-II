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
        public void updateSignal() {
            lastSignal = signal.clone();
            for (int i = 1; i < vertexCount; i++) if (r[i] != null) signal[i] = r[i].augmentation;
            if ((signal[1] & 0xFFFF) != 0 && (lastSignal[1] & 0xFFFF) == 0) SignalGraph.graph.setNodeAugmentation(e[0], signal[0] = outputSignal = (color == 8 ? (signal[1] | signal[2] | signal[3]) & 0x70000 : (color << 16)) | (~outputSignal & 0xFFFF));
            else if ((signal[2] & 0xFFFF) != 0 && (lastSignal[2] & 0xFFFF) == 0) SignalGraph.graph.setNodeAugmentation(e[0], signal[0] = outputSignal = (color == 8 ? (signal[1] | signal[2] | signal[3]) & 0x70000 : (color << 16)) | (~outputSignal & 0xFFFF));
            else if ((signal[3] & 0xFFFF) != 0 && (lastSignal[3] & 0xFFFF) == 0) SignalGraph.graph.setNodeAugmentation(e[0], signal[0] = outputSignal = (color == 8 ? (signal[1] | signal[2] | signal[3]) & 0x70000 : (color << 16)) | (~outputSignal & 0xFFFF));
        }
    }
}
