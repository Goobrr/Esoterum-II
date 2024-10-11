package esoterum.world.blocks.signal;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
import esoterum.graph.SignalGraph;

public class SignalGate extends SignalBlock{
    public Boolf<SignalBuild> function;
    public SignalGate(String name){
        super(name);

        rotate = true;
        hasGraph = false;
    }

    @Override
    public void load(){
        super.load();

        baseRegion = Core.atlas.find(name + "-base", "eso-default-gate-base");

        outputSignalRegions = new TextureRegion[size * 4];
        for(int i : outputs){
            outputSignalRegions[i] = Core.atlas.find(name + "-output-" + i, "eso-default-gate-output");
        }
    }

    public class SignalGateBuild extends SignalBuild {
        @Override
        public void updateTile(){
            super.updateTile();
            SignalGraph.graph.setVertexAugmentation(v[0], function.get(this) ? 1 : 0);
        }
    }
}
