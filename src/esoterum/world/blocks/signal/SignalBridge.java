package esoterum.world.blocks.signal;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.math.geom.*;
import arc.struct.IntSeq;
import arc.util.*;
import arc.util.io.*;
import esoterum.EsoVars;
import esoterum.graph.*;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.graphics.*;
import mindustry.world.Tile;

public class SignalBridge extends SignalBlock
{
    public TextureRegion[] signalRegions;
    int range = 1000;

    public SignalBridge(String name)
    {
        super(name);

        configurable = true;
        // clipSize = range * 2 + 4f;
        // clipSize *= Vars.tilesize;

        dark = true;

        config(Point2.class, (SignalBridgeBuild tile, Point2 p) -> {
            int i = p.pack();
            SignalBridgeBuild other = (SignalBridgeBuild) Vars.world.build(i);
            if (other == null) return;
            if (tile.link.contains(i))
            {
                tile.link.removeValue(i);
                other.link.removeValue(tile.pos());
                SignalGraph.events.add(new GraphEvent.updateEvent(tile));
            }
            else if (i != tile.pos())
            {
                tile.link.add(i);
                other.link.add(tile.pos());
                SignalGraph.events.add(new GraphEvent.updateEvent(tile));
            }
        });

        config(Integer.class, (SignalBridgeBuild tile, Integer i) -> {
            SignalBridgeBuild other = (SignalBridgeBuild) Vars.world.build(i);
            if (other == null) return;
            if (tile.link.contains(i))
            {
                tile.link.removeValue(i);
                other.link.removeValue(tile.pos());
                SignalGraph.events.add(new GraphEvent.updateEvent(tile));
            }
            else if (i != tile.pos())
            {
                tile.link.add(i);
                other.link.add(tile.pos());
                SignalGraph.events.add(new GraphEvent.updateEvent(tile));
            }
        });

        config(Object[].class, (SignalBridgeBuild tile, Object[] p) -> {
            if (p[0] instanceof Long l)
            {
                tile.shielding = l;
                SignalGraph.events.add(new GraphEvent.updateEvent(tile));
            }
            for (int i = 1; i < p.length; i++)
            {
                Tile other = Vars.world.tile(Point2.unpack(tile.pos()).add((Point2) p[i]).pack());
                if (linkValid(tile.tile, other))
                {
                    if (other.pos() != tile.pos() && other.build instanceof SignalBridgeBuild b)
                    {
                        tile.link.add(b.pos());
                        b.link.add(tile.pos());
                        SignalGraph.events.add(new GraphEvent.updateEvent(tile));
                    }
                }
            }
        });

        configClear((SignalBridgeBuild tile) -> tile.link.clear());
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid)
    {
        Drawf.circles(x * 8 + offset, y * 8 + offset, range * 8, Color.white);
        Draw.reset();
    }

    public boolean linkValid(Tile tile, Tile other)
    {
        if (other == null || tile == null || other.dst(tile) > range * 8) return false;
        if (other.build == null || !(other.build instanceof SignalBridgeBuild)) return false;

        return other.block() == tile.block() && tile.team() == other.team();
    }

    @Override
    public void load()
    {
        super.load();
        signalRegions = new TextureRegion[16];
        for (int i = 0; i < 16; i++) signalRegions[i] = Core.atlas.find("eso-signal-bridge-" + i, "eso-none");
    }

    public class SignalBridgeBuild extends SignalBuild
    {
        public IntSeq link = new IntSeq();

        @Override
        public void updateEdges()
        {
            super.updateEdges();
            for (int i = 0; i < link.size; i++)
            {
                if (Vars.world.build(link.get(i)) instanceof SignalBridgeBuild b) SignalGraph.addEdge(v[0], b.v[0]);
            }
        }

        @Override
        public void onRemoved()
        {
            while (!link.isEmpty())
            {
                if (linkValid(tile, Vars.world.tile(link.get(0)))) configure(link.get(0));
            }
            super.onRemoved();
        }

        @Override
        public void drawSignalRegions(Rect camera)
        {
            Draw.color(signal[0] == 1 ? getWireColor() : getWireOffColor());
            Draw.rect(signalRegions[(active[0] ? 1 : 0) + ((active[1] ? 1 : 0) << 1) + ((active[2] ? 1 : 0) << 2) + ((active[3] ? 1 : 0) << 3)], x, y, rotation * 90);

            Draw.z(Layer.power);
            Lines.stroke(1f, signal[0] == 1 ? getWireColor() : getWireOffColor());
            for (int i = 0; i < link.size; i++)
            {
                Point2 p = Point2.unpack(link.get(i));
                p.x *= 8;
                p.y *= 8;
                if (!camera.overlaps(Math.min(p.x, x), Math.min(p.y, y), Math.abs(p.x - x) + 1, Math.abs(p.y - y) + 1) && (p.y > y || (p.y == y && p.x < x)))
                    continue;
                if (EsoVars.drawNodesAsManhattan)
                {
                    float halfwayX = (x + p.x) / 2;
                    Lines.line(
                            x, y + 2,
                            halfwayX, y + 2,
                            true
                    );

                    Lines.line(
                            halfwayX, y + 2,
                            halfwayX, p.y + 2,
                            true
                    );

                    Lines.line(
                            halfwayX, p.y + 2,
                            p.x, p.y + 2,
                            true
                    );

                    Lines.line(
                            x, y,
                            x, y + 2,
                            true
                    );

                    Lines.line(
                            p.x, p.y,
                            p.x, p.y + 2,
                            true
                    );
                }
                else
                {
                    Lines.line(
                            x, y,
                            p.x, p.y,
                            false
                    );
                }
            }
            Draw.reset();
        }

        @Override
        public void drawConfigure()
        {
            Tmp.c1.set(Color.white).lerp(team.color, signal[0]);

            Drawf.circles(x, y, size * 8 / 2f + 1f + Mathf.absin(Time.time, 4f, 1f), Tmp.c1);
            Drawf.circles(x, y, range * 8, Tmp.c1);

            for (int i = 0; i < link.size; i++)
            {
                Building b = Vars.world.build(link.get(i));
                if (b == null) continue;
                Drawf.square(b.x, b.y, b.block.size * 8 / 2f + 1f, Tmp.c1);
            }
        }

        @Override
        public boolean onConfigureBuildTapped(Building other)
        {
            if (linkValid(tile, other.tile))
            {
                configure(other.pos());
                return false;
            }
            return true;
        }

        @Override
        public Object[] config()
        {
            Object[] p = new Object[link.size + 1];
            p[0] = shielding;
            for (int i = 1; i < link.size + 1; i++)
            {
                p[i] = Point2.unpack(link.get(i - 1)).sub(Point2.unpack(pos()));
            }
            return p;
        }

        @Override
        public void write(Writes write)
        {
            super.write(write);
            write.i(link.size);
            for (int i = 0; i < link.size; i++)
            {
                write.i(link.get(i));
            }
        }

        @Override
        public void read(Reads read, byte revision)
        {
            if (revision >= 4) super.read(read, revision);
            int s = read.i(), l;
            link.clear();
            for (int i = 0; i < s; i++)
            {
                l = read.i();
                if (linkValid(tile, Vars.world.tile(l)))
                {
                    SignalBridgeBuild other = (SignalBridgeBuild) Vars.world.build(l);
                    if (other == null) return;
                    if (l != tile.pos())
                    {
                        link.add(l);
                        other.link.add(pos());
                    }
                }
            }
            SignalGraph.events.add(new GraphEvent.updateEvent(this));
        }
    }
}
