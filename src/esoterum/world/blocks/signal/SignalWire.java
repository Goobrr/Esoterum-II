package esoterum.world.blocks.signal;


import arc.math.geom.Vec2;
import esoterum.EdgeUtils;
import esoterum.EsoVars;
import esoterum.Esoterum;
import esoterum.graph.GraphEvent;
import esoterum.graph.SignalGraph;
import mindustry.Vars;

public class SignalWire extends SignalBlock
{
    public SignalWire(String name)
    {
        super(name);

        dark = true;
    }

    public class SignalWireBuild extends SignalBuild
    {
        public boolean bypass = true;
        public int outputSignal = 0;

        @Override
        public boolean dark(){
            return bypass;
        }

        @Override
        public void updateEdges()
        {
            for (int i = 0; i < vertexCount; i++) SignalGraph.clearEdges(v[i]);

            int c = 0;
            int last = -1;
            for (int i = 0; i < size * 4; i++)
            {
                active[i] = false;
                Vec2 offset = EdgeUtils.getEdgeOffset(size, i, rotation);
                Vec2 sideOffset = EdgeUtils.getEdgeOffset(1, i / size, rotation);
                if (Vars.world.build((int) (x / 8 + offset.x + sideOffset.x), (int) (y / 8 + offset.y + sideOffset.y)) instanceof SignalBuild b)
                {
                    int index = EdgeUtils.getOffsetIndex(b.size(), x / 8 + offset.x - b.x / 8, y / 8 + offset.y - b.y / 8, b.rotation);
                    if (((b.inputs()[index] & outputs[i]) == 1 || (b.outputs()[index] & inputs[i]) == 1) && ((shielding & (1L << i)) == 0) && ((b.shielding & (1L << index)) == 0))
                    {
                        c += inputs[i];
                        last = i;
                        SignalGraph.addEdge(v[conns[i]], b.v[b.conns()[index]]);
                        active[i] = true;
                    }
                    if ((b.outputs()[index] & outputs[i]) == 1) c += 2;
                    b.active[index] = active[i];
                }
            }
            if (c == 1)
            {
                SignalGraph.addEdge(v[0], v[conns[last]]);
                e[0] = SignalGraph.graph.vertexInfo.get(v[0]).vertex.arbitraryVisit;
                if (bypass) SignalGraph.graph.setNodeAugmentation(e[0], signal[0] = 0);
                else
                {
                    bypass = true;
                    SignalGraph.removeBright(brightid);
                }

            }
            else if (bypass)
            {
                bypass = false;
                SignalGraph.addBright(this);
            }
        }

        @Override
        public void updateSignal()
        {
            if (r[1] != null) signal[1] = r[1].augmentation;
            if (r[2] != null) signal[2] = r[2].augmentation;
            if (r[3] != null) signal[3] = r[3].augmentation;
            if (!bypass) if ((signal[0] = signal[1] | signal[2] | signal[3]) != outputSignal)
                SignalGraph.graph.setNodeAugmentation(e[0], outputSignal = signal[0]);
            else; else if (r[0] != null) signal[0] = r[0].augmentation;
        }
    }
}
