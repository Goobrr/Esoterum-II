package esoterum.world.blocks.signal;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.IntSeq;
import arc.util.Log;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import esoterum.graph.SignalGraph;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.world.Tile;

public class SignalBridge extends SignalBlock {

    int range = 100;

    public SignalBridge(String name){
        super(name);

        configurable = true;

        config(Point2.class, (SignalBridgeBuild tile, Point2 p) -> {
            int i = p.pack();
            SignalBridgeBuild other = (SignalBridgeBuild)Vars.world.build(i);
            if (other == null) return;
            if(tile.link.contains(i)){
                tile.link.removeValue(i);
                other.link.removeValue(tile.pos());
                SignalGraph.removeEdge(tile.v[0], other.v[0]);
            } else if (i != tile.pos()){
                tile.link.add(i);
                other.link.add(tile.pos());
                SignalGraph.addEdge(tile.v[0], other.v[0]);
            }
        });

        config(Integer.class, (SignalBridgeBuild tile, Integer i) -> {
            SignalBridgeBuild other = (SignalBridgeBuild)Vars.world.build(i);
            if(tile.link.contains(i)){
                tile.link.removeValue(i);
                other.link.removeValue(tile.pos());
                SignalGraph.removeEdge(tile.v[0], other.v[0]);
            } else if (i != tile.pos()){
                tile.link.add(i);
                other.link.add(tile.pos());
                SignalGraph.addEdge(tile.v[0], other.v[0]);
            }
        });

        config(Point2[].class, (SignalBridgeBuild tile, Point2[] l) -> {
            for(Point2 i : l){
                Tile other = Vars.world.tile(Point2.unpack(tile.pos()).add(i).pack());
                if(linkValid(tile.tile, other)) tile.configure(other.pos());
            }
        });

        configClear((SignalBridgeBuild tile) -> tile.link.clear());
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        Drawf.circles(x * 8 + offset, y * 8 + offset, range * 8, Color.white);
        Draw.reset();
    }

    public boolean linkValid(Tile tile, Tile other){
        if(other == null || tile == null || other.dst(tile) > range * 8) return false;
        if(other.build == null || !(other.build instanceof SignalBridgeBuild)) return false;

        return other.block() == tile.block() && tile.team() == other.team();
    }

    public class SignalBridgeBuild extends SignalBuild {
        public IntSeq link = new IntSeq();

        @Override
        public void updateEdges(){
            super.updateEdges();
            for(int i=0;i<link.size;i++){
                if (Vars.world.build(link.get(i)) instanceof SignalBridgeBuild b) SignalGraph.addEdge(v[0], b.v[0]);
            }
        }

        @Override
        public void onRemoved() {
            while(!link.isEmpty()){
                if(linkValid(tile, Vars.world.tile(link.get(0)))) configure(link.get(0));
            }
            super.onRemoved();
        }

        @Override
        public void draw(){
            super.draw();

            Draw.z(Layer.power);
            Lines.stroke(1f, signal[0] == 1? team.color : Color.white);
            for(int i=0;i<link.size;i++){
                Point2 p = Point2.unpack(link.get(i));
                Lines.line(
                    x, y,
                    p.x * 8, p.y * 8,
                    false
                );
            }
            Draw.reset();
        }

        @Override
        public void drawConfigure(){
            Tmp.c1.set(Color.white).lerp(team.color, signal[0]);

            Drawf.circles(x, y, size * 8 / 2f + 1f + Mathf.absin(Time.time, 4f, 1f), Tmp.c1);
            Drawf.circles(x, y, range * 8, Tmp.c1);

            for(int i=0;i<link.size;i++){
                Building b = Vars.world.build(link.get(i));
                if(b == null) continue;
                Drawf.square(b.x, b.y, b.block.size * 8 / 2f + 1f, Tmp.c1);
            }
        }

        @Override
        public boolean onConfigureBuildTapped(Building other){
            if(linkValid(tile, other.tile)){
                configure(other.pos());
                return false;
            }
            return true;
        }

        @Override
        public Point2[] config(){
            Point2[] p = new Point2[link.size];
            for(int i=0;i<link.size;i++){
                p[i] = Point2.unpack(link.get(i)).sub(Point2.unpack(pos()));
            }
            return p;
        }

        @Override
        public void write(Writes write){
            write.i(link.size);
            for(int i=0;i<link.size;i++){
                write.i(link.get(i));
            }
        }

        @Override
        public void read(Reads read, byte revision){
            int s = read.i(), l;
            for(int i=0;i<s;i++){
                l = read.i();
                if(linkValid(tile, Vars.world.tile(l))) configure(l);
            }
        }
    }
}
