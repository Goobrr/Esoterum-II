package esoterum.world.blocks.signal;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import esoterum.graph.GraphEvent;
import esoterum.graph.SignalGraph;
import esoterum.world.blocks.signal.SignalMatrix.SignalMatrixBuild;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;

public class SignalDrawer extends SignalBlock {
    public SignalDrawer(String name)
    {
        super(name);

        config(Point2.class, (SignalDrawerBuild tile, Point2 p) -> {
            int i = p.pack();
            if (tile.frompos == i) tile.frompos = -1;
            else if (tile.topos == i) tile.topos = -1;
            else if (tile.frompos == -1) tile.frompos = i;
            else if (tile.topos == -1) tile.topos = i;
            SignalGraph.events.add(new GraphEvent.updateEvent(tile));
        });

        config(Object[].class, (SignalDrawerBuild tile, Object[] o) -> {
            if (o[0] instanceof Long l)
            {
                tile.shielding = l;
                SignalGraph.events.add(new GraphEvent.updateEvent(tile));
            }
            if (o[1] instanceof Point2 p) tile.frompos = p.pack();
            if (o[2] instanceof Point2 p) tile.topos = p.pack();
        });
    }

    public class SignalDrawerBuild extends SignalBuild {
        public int frompos = -1, topos = -1;
        public SignalMatrixBuild from, to;
        @Override
        public void updateSignal(boolean update){
            super.updateSignal(update);
            if (update)
            {
                if (Vars.world.build(frompos) instanceof SignalMatrixBuild b) from = b;
                else { frompos = -1; from = null; }
                if (Vars.world.build(topos) instanceof SignalMatrixBuild b) to = b;
                else { topos = -1; to = null; }
            }
            if (from != null && to != null && signal[0] > 0)
            {
                for (int x=0;x<256;x++){
                    for (int y=0;y<256;y++){
                        int p = from.img.getRaw(x, y);
                        if ((p & 0xFFFFFF00) != 0 && p != to.img.getRaw(x, y)) to.queuedOrders.add(to.new PaintOrder(x, y, p));
                    }
                }
            }
        }

        @Override
        public Object[] config()
        {
            return new Object[]{shielding, Point2.unpack(frompos), Point2.unpack(topos)};
        }

        @Override
        public void drawConfigure()
        {
            if (from != null) Drawf.square(from.x, from.y, from.block.size * 8 / 2f + 1f, Pal.place);
            if (to != null) Drawf.square(to.x, to.y, to.block.size * 8 / 2f + 1f, Pal.plastanium);
        }

        @Override
        public boolean onConfigureBuildTapped(Building other)
        {
            if (other instanceof SignalMatrixBuild)
            {
                configure(Point2.unpack(other.pos()));
                return false;
            }
            return true;
        }

        @Override
        public void write(Writes write)
        {
            super.write(write);
            write.i(frompos);
            write.i(topos);
        }

        @Override
        public void read(Reads read, byte revision)
        {
            super.read(read, revision);
            frompos = read.i();
            topos = read.i();
        }
    }
}
