package esoterum.graph;

import arc.struct.Seq;
import esoterum.EsoVars;
import esoterum.world.blocks.signal.SignalBlock;
import esoterum.world.blocks.signal.SignalDrawer;
import esoterum.world.blocks.signal.SignalBlock.SignalBuild;
import esoterum.world.blocks.signal.SignalDrawer.SignalDrawerBuild;
import esoterum.world.blocks.signal.SignalMatrix.SignalMatrixBuild;
import mindustry.Vars;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SignalGraph
{
    public static ConnGraph graph = new ConnGraph();
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
        graph = new ConnGraph();
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
        graph = new ConnGraph();
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
        if (update) builds.each(b -> {
            for (int i = 0; i < b.vertexCount(); i++)
            {
                b.e[i] = SignalGraph.graph.vertexInfo.get(b.v[i]).vertex.arbitraryVisit;
                b.r[i] = b.e[i].root();
            }
            if (b instanceof SignalDrawerBuild d)
            {
                if (Vars.world.build(d.frompos) instanceof SignalMatrixBuild m) d.from = m;
                else { d.frompos = -1; d.from = null; }
                if (Vars.world.build(d.topos) instanceof SignalMatrixBuild m) d.to = m;
                else { d.topos = -1; d.to = null; }
            }
        });
        if (EsoVars.darkMode) brights.each(b -> {
            b.updateSignal();
        });
        else builds.each(b -> {
            b.updateSignal();
        });
    }
}
