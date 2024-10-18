package esoterum.world.blocks.signal;

import arc.Core;
import arc.func.Boolf;
import arc.graphics.g2d.TextureRegion;
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

        outputSignalRegions = new TextureRegion[size * 4];
        for (int i = 0; i < outputs.length; i++)
        {
            if (outputs[i] == 1)
                outputSignalRegions[i] = Core.atlas.find(name + "-output-" + i, "eso-default-gate-output");
        }
    }

    public class SignalGateBuild extends SignalBuild
    {
        @Override
        public void updateSignal()
        {
            super.updateSignal();
            SignalGraph.graph.setVertexAugmentation(v[0], function.get(this) ? 1 : 0);
        }
    }
}
