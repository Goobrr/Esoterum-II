package esoterum.graph;

import arc.*;
import arc.struct.*;
import arc.util.*;
import esoterum.world.blocks.signal.SignalBlock.*;
import mindustry.game.EventType.*;

public class SignalGraph {
    // Graph ID tracking, for saving and stuff.
    private static int max = 1;
    private static IntQueue free = new IntQueue();

    public static Seq<SignalGraph> graphs = new Seq<>(false, 16, SignalGraph.class);

    public static IntIntMap IDMap = new IntIntMap();

    // Graph tomfoolery
    public Seq<SignalBuild> buildings = new Seq<>(false, 16, SignalBuild.class);

    public boolean signal = false;

    private long lastUpdateFrame = -1;

    private final int graphID;

    static {
        Events.on(WorldLoadBeginEvent.class, event -> {
            Log.info("Cleared Graph ID Tracking");
            IDMap.clear();
            graphs.clear();

            free.clear();
            max = 1;
        });
    }

    // Graph Logic

    public SignalGraph(){
        graphID = free.size == 0 ? max++ : free.removeFirst();
        graphs.add(this);
    }

    public static void mapID(int id, int realID){
        IDMap.put(id, realID);
    }

    public static SignalGraph getGraphByID(int id){
        return graphs.find(g -> g.getID() == IDMap.get(id));
    };

    public int getID(){
        return graphID;
    }

    public void delete(){
        for(SignalBuild b : buildings){
            b.signalGraph = null;
        }
        buildings.clear();

        free.addLast(graphID);
    }

    public SignalGraph merge(SignalGraph graph){
        if(graph.getID() == graphID) return this;

        SignalGraph merged = graph.buildings.size <= this.buildings.size ? graph : this;
        SignalGraph merger = this.buildings.size >= graph.buildings.size ? this : graph;
        for(SignalBuild b : merged.buildings){
            b.signalGraph = merger;
            merger.add(b);
        }

        merger.signal |= merged.signal;

        graphs.remove(merged);
        free.addLast(merged.graphID);

        return merger;
    }

    public void add(SignalBuild b){
        b.signalGraph = this;

        buildings.add(b);
    }

    public void remove(SignalBuild b){
        b.signalGraph = null;

        buildings.remove(b);
        buildings.setSize(buildings.size - 1);
        graphs.remove(this);
    }

    public boolean signal(){
        return signal(false);
    }

    public boolean signal(Boolean signal){
        if(lastUpdateFrame != Core.graphics.getFrameId()){
            lastUpdateFrame = Core.graphics.getFrameId();
            this.signal = false;
        }

        this.signal |= signal;

        return this.signal;
    }
}
