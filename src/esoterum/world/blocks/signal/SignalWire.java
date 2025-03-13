package esoterum.world.blocks.signal;


import arc.math.geom.Vec2;
import esoterum.EdgeUtils;
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
                if (bypass) SignalGraph.graph.setNodeAugmentation(e[0], 0);
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
            if (!bypass) if ((signal[0] = r[1].augmentation | r[2].augmentation | r[3].augmentation) != outputSignal)
                SignalGraph.graph.setNodeAugmentation(e[0], outputSignal = signal[0]);
            else if (r[0] != null) signal[0] = r[0].augmentation;
        }
    }
}
