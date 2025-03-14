package esoterum.world.blocks.signal;

import arc.Core;
import arc.func.Boolf;
import arc.graphics.g2d.*;
import arc.math.geom.Rect;
import esoterum.graph.SignalGraph;

public class SignalGate extends SignalBlock
{
    public Boolf<SignalBuild> function;

    public SignalGate(String name)
    {
        super(name);

        rotate = true;
        hasGraph = false;
    }

    @Override
    public void load()
    {
        super.load();

        baseRegion = Core.atlas.find(name + "-base", "eso-default-gate-base");

        outputSignalRegions = new TextureRegion[1];
        outputSignalRegions[0] = Core.atlas.find(name + "-output", "eso-default-gate-output");
    }

    public class SignalGateBuild extends SignalBuild
    {
        public int outputSignal;
        
        @Override
        public void updateSignal()
        {
            int r = function.get(this) ? 1 : 0;
            if (r != outputSignal) SignalGraph.graph.setNodeAugmentation(e[0], signal[0] = outputSignal = r);
        }

        @Override
        public void drawSignalRegions(Rect camera)
        {
            Draw.color(signal[0] == 1 ? getWireColor() : getWireOffColor());
            if (active[0]) Draw.rect(outputSignalRegions[0], x, y, rotation * 90);
            else Draw.rect(signalRegion, x, y, rotation * 90);
        }
    }
}
