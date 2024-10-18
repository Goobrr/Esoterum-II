package esoterum.graph;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import esoterum.world.blocks.signal.SignalBlock;
import esoterum.world.blocks.signal.SignalBlock.SignalBuild;
import mindustry.Vars;
import mindustry.world.Tile;

public class SignalGraph
{
    public static int n;
    public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public static Runnable updater = () -> SignalGraph.updateTiles();
    public static ScheduledFuture<?> updateHandle;
    
    private static final Augmentation AUGMENTATION = new Augmentation()
    {
        @Override
        public Object combine(Object value1, Object value2)
        {
            return (int) value1 | (int) value2;
        }
    };

    public static ConnGraph graph = new ConnGraph(AUGMENTATION);

    public static void addVertex(SignalBlock.SignalBuild b, int k)
    {
        b.v[k] = new ConnVertex();
        graph.setVertexAugmentation(b.v[k], 0);
        //Log.info("add");
    }

    public static void addEdge(ConnVertex u, ConnVertex v)
    {
        if (u != null && v != null) graph.addEdge(u, v);
    }

    public static void removeVertex(SignalBlock.SignalBuild b, int k)
    {
        if (b == null || b.v[k] == null) return;
        for (ConnVertex v : graph.vertexInfo.get(b.v[k]).edges.keySet())
        {
            graph.removeEdge(b.v[k], v);
        }
        graph.removeVertexAugmentation(b.v[k]);
        b.v[k] = null;
        //Log.info("rm");
    }

    public static void clearVertices()
    {
        graph = new ConnGraph(AUGMENTATION);
    }

    public static void removeEdge(ConnVertex u, ConnVertex v)
    {
        if (u != null && v != null) graph.removeEdge(u, v);
    }

    public static void clear()
    {
        graph = new ConnGraph(AUGMENTATION);
    }

    public static void clearEdges(ConnVertex v)
    {
        if (v == null || graph.vertexInfo.get(v) == null) return;
        for (ConnVertex u : graph.vertexInfo.get(v).edges.keySet())
        {
            graph.removeEdge(v, u);
        }
    }

    public static void updateTiles(){
        int c = 0;
        for (Tile t : Vars.world.tiles){
            if(t.build instanceof SignalBuild b){
                c++;
                b.updateSignal();
            }
            if(c == n) break;
        }
    }

    public static void run(boolean b){
        if (b) updateHandle = scheduler.scheduleAtFixedRate(updater, 0, 1, TimeUnit.NANOSECONDS);
        else if (updateHandle != null) updateHandle.cancel(false);
    }

    public static int getAugment(ConnVertex v){
        return (int)graph.getVertexAugmentation(v);
    }
}
