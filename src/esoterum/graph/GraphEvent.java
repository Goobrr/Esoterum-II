package esoterum.graph;

import esoterum.world.blocks.signal.SignalBlock.SignalBuild;

public class GraphEvent
{

    public static class eventType
    {
        public SignalBuild b;

        public void run()
        {
        }
    }

    public static class createEvent extends eventType
    {
        public createEvent(SignalBuild b)
        {
            this.b = b;
        }

        public void run()
        {
            b.id = SignalGraph.addBuild(b);
            if (!b.dark()) b.brightid = SignalGraph.addBright(b);
            b.updateEdges();
        }
    }

    public static class brightenEvent extends eventType
    {
        public brightenEvent(SignalBuild b)
        {
            this.b = b;
        }

        public void run()
        {
            b.brightid = SignalGraph.addBright(b); 
        }
    }

    public static class destroyEvent extends eventType
    {
        public destroyEvent(SignalBuild b)
        {
            this.b = b;
        }

        public void run()
        {
            for (int i = 0; i < b.vertexCount(); i++) SignalGraph.removeVertex(b, i);
            SignalGraph.removeBuild(b.id);
            if (!b.dark()) SignalGraph.removeBright(b.brightid);
        }
    }

    public static class darkenEvent extends eventType
    {
        public darkenEvent(SignalBuild b)
        {
            this.b = b;
        }

        public void run()
        {
            SignalGraph.removeBright(b.brightid);
        }
    }

    public static class updateEvent extends eventType
    {
        public updateEvent(SignalBuild b)
        {
            this.b = b;
        }

        public void run()
        {
            b.updateEdges();
        }
    }

    public static class clearEvent extends eventType
    {
        public void run()
        {
            SignalGraph.clear();
        }
    }
}
