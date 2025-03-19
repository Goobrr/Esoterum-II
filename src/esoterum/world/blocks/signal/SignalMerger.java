package esoterum.world.blocks.signal;

import arc.graphics.g2d.Draw;
import arc.math.geom.Rect;
import esoterum.graph.SignalGraph;

public class SignalMerger extends SignalSplitter
{
    public SignalMerger(String name)
    {
        super(name);
    }

    public class SignalMergerBuild extends SignalSplitterBuild
    {
        @Override
        public void updateSignal()
        {
            if (r[0] != null) signal[0] = r[0].augmentation;
            if (r[1] != null) signal[1] = r[1].augmentation;
            int r = signal[1] != 0 ? 1 << index : 0;
            if (r != outputSignal) SignalGraph.graph.setNodeAugmentation(e[0], outputSignal = r);
        }

        @Override
        public void drawSignalRegions(Rect camera)
        {
            Draw.color(getWireOffColor().cpy().lerp(getWireColor(), (float) (signal[0] & 0xFFFF) / 0xFFFF));
            if (active[0]) Draw.rect(outputSignalRegions[0], x, y, rotation * 90);
            Draw.color(getWireOffColor().cpy().lerp(getWireColor(), (float) (signal[1] & 0xFFFF) / 0xFFFF));
            Draw.rect(signalRegions[index], x, y, rotation * 90);
            if (active[2]) Draw.rect(inputSignalRegions[2], x, y, rotation * 90);
        }
    }
}
