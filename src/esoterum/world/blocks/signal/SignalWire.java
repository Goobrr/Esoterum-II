package esoterum.world.blocks.signal;


import arc.math.geom.Vec2;
import esoterum.EdgeUtils;
import esoterum.graph.SignalGraph;
import mindustry.Vars;

public class SignalWire extends SignalBlock
{
    public SignalWire(String name)
    {
        super(name);
    }

    public class SignalWireBuild extends SignalBuild
    {
        public boolean bypass = false;

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
                    if (((b.inputs()[index] & outputs[i]) == 1 || (b.outputs()[index] & inputs[i]) == 1) && ((shielding & (1l << i)) == 0) && ((b.shielding & (1l << index)) == 0))
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
                if (bypass) SignalGraph.graph.setVertexAugmentation(v[0], 0);
                else bypass = true;
            }
            else bypass = false;
        }

        @Override
        public void updateTile()
        {
            super.updateTile();
            if (!bypass)
            {
                SignalGraph.graph.setVertexAugmentation(v[0], signal[1] | signal[2] | signal[3]);
            }
        }
    }
}
