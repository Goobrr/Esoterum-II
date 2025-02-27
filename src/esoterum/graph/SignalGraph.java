package esoterum.graph;

import arc.struct.Seq;
import esoterum.EsoVars;
import esoterum.world.blocks.signal.SignalBlock;
import esoterum.world.blocks.signal.SignalBlock.SignalBuild;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SignalGraph
{
    private static final Augmentation AUGMENTATION = new Augmentation()
    {
        @Override
        public Object combine(Object value1, Object value2)
        {
            return (int) value1 | (int) value2;
        }
    };

    public static ConnGraph graph = new ConnGraph(AUGMENTATION);
    public static Seq<SignalBuild> builds = new Seq<>();
    public static Seq<SignalBuild> brights = new Seq<>();

    public static ConcurrentLinkedQueue<GraphEvent.eventType> events = new ConcurrentLinkedQueue<>();

    public static void addVertex(SignalBlock.SignalBuild b, int k)
    {
        b.v[k] = new ConnVertex();
        graph.setVertexAugmentation(b.v[k], 0);
    }

    public static void addEdge(ConnVertex u, ConnVertex v)
    {
        if (u != null && v != null) graph.addEdge(u, v);
    }

    public static void removeVertex(SignalBlock.SignalBuild b, int k)
    {
        if (b == null || b.v[k] == null) return;
        for (ConnVertex v : graph.vertexInfo.get(b.v[k]).edges.keys())
        {
            graph.removeEdge(b.v[k], v);
        }
        graph.removeVertexAugmentation(b.v[k]);
        b.v[k] = null;
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
        graph.clear();
        builds.clear();
        brights.clear();
        graph = new ConnGraph(AUGMENTATION);
    }

    public static void clearEdges(ConnVertex v)
    {
        if (v == null || graph.vertexInfo.get(v) == null) return;
        for (ConnVertex u : graph.vertexInfo.get(v).edges.keys())
        {
            graph.removeEdge(v, u);
        }
    }

    public static int addBuild(SignalBuild b)
    {
        builds.add(b);
        for (int i = 0; i < b.vertexCount(); i++) if (b.v[i] == null) addVertex(b, i);
        return builds.size - 1;
    }

    public static int addBright(SignalBuild b)
    {
        brights.add(b);
        return brights.size - 1;
    }

    public static void removeBuild(int id)
    {
        SignalBuild b = builds.pop();
        if (id == builds.size) return;
        b.id = id;
        builds.set(id, b);
    }
    
    public static void removeBright(int id)
    {
        SignalBuild b = brights.pop();
        if (id == brights.size) return;
        b.brightid = id;
        brights.set(id, b);
    }

    public static void updateBuilds(boolean update)
    {
        if (EsoVars.darkMode) brights.each(b -> {
            b.updateSignal(update);
        });
        else builds.each(b -> {
            b.updateSignal(update);
        });
    }
}
