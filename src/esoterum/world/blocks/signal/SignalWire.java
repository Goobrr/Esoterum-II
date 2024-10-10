package esoterum.world.blocks.signal;


import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.Vec2;
import arc.util.Log;
import esoterum.EdgeUtils;
import esoterum.graph.SignalGraph;
import mindustry.Vars;
import mindustry.graphics.*;

public class SignalWire extends SignalBlock{
    public SignalWire(String name){
        super(name);
    }

    public class SignalWireBuild extends SignalBuild{
        public boolean bypass = false;
        @Override
        public void updateEdges(){
            for (int i=0;i<vertexCount;i++) SignalGraph.clearEdges(v[i]);

            int c = 0;
            int last = -1;
            //Log.info("start");
            bypass = false;
            for (int i=0;i<size*4;i++){
                active[i] = false;
                Vec2 offset = EdgeUtils.getEdgeOffset(size, i, rotation);
                Vec2 sideOffset = EdgeUtils.getEdgeOffset(1, i/size, rotation);
                if(Vars.world.build((int)(x/8 + offset.x + sideOffset.x), (int)(y/8 + offset.y + sideOffset.y)) instanceof SignalBuild b){
                    int index = EdgeUtils.getOffsetIndex(b.size(), x/8 + offset.x - b.x/8, y/8 + offset.y - b.y/8, b.rotation);
                    //Log.info("" + x/8 + " " + y/8);
                    //Log.info("" + (int)(x/8 + offset.x + sideOffset.x) + " " + (int)(y/8 + offset.y + sideOffset.y));
                    //Log.info(index);
                    if ((b.inputs()[index] & outputs[i]) == 1 || (b.outputs()[index] & inputs[i]) == 1){
                        c += inputs[i];
                        last = i;
                        SignalGraph.addEdge(v[conns[i]], b.v[b.conns()[index]]);
                        active[i] = true;
                    }
                }
            }
            if (c == 1){
                SignalGraph.addEdge(v[0], v[conns[last]]);
                bypass = true;
            }
        }

        @Override
        public void updateTile(){
            super.updateTile();
            if(!bypass) {
                signal[0] = signal[1] | signal[2] | signal[3];
                SignalGraph.graph.setVertexAugmentation(v[0], signal[0]);
            }
        }
    }
}
