package esoterum.world.blocks.signal;

import arc.Core;
import arc.func.Intf;
import arc.graphics.g2d.*;
import arc.math.geom.Rect;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import esoterum.graph.SignalGraph;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

public class SignalGate extends SignalBlock
{
    public Intf<SignalBuild> function;

    public SignalGate(String name)
    {
        super(name);

        rotate = true;
        hasGraph = false;

        config(Byte.class, (SignalGateBuild tile, Byte i) -> {
            tile.color = i;
        });

        config(Object[].class, (SignalGateBuild tile, Object[] o) -> {
            if (o[0] instanceof Long l) tile.shielding = l;
            if (o.length > 1 && o[1] instanceof Byte b) tile.color = b;
        });
    }

    @Override
    public void load()
    {
        super.load();

        baseRegion = Core.atlas.find(name + "-base", "eso-default-gate-base");

        outputSignalRegions = new TextureRegion[1];
        outputSignalRegions[0] = Core.atlas.find(name + "-output", "eso-default-gate-output");
    }

    public class SignalGateBuild extends SignalBuild
    {
        public int outputSignal;
        public byte color = 8;
        
        @Override
        public void updateSignal()
        {
            int r = color == 8 ? function.get(this) : (function.get(this) & 0xFFFF) | (color << 16);
            if (r != outputSignal) SignalGraph.graph.setNodeAugmentation(e[0], signal[0] = outputSignal = r);
        }

        @Override
        public void drawSignalRegions(Rect camera)
        {
            Draw.color(getWireColor(0));
            if (active[0]) Draw.rect(outputSignalRegions[0], x, y, rotation * 90);
            else Draw.rect(signalRegion, x, y, rotation * 90);
        }

        @Override
        public void buildConfiguration(Table table)
        {
            table.table().size(120f).growX();
            table.row();
            super.buildConfiguration(table.table().get());
            table.row();
            Table t = table.table().growX().get();
            t.setBackground(Styles.black5);
            ImageButton b[] = new ImageButton[9];
            for (int i = 0; i < 9; i++)
            {
                byte j = (byte) i;
                b[i] = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
                b[i].margin(4f);
                b[i].getImageCell().grow();
                b[i].getStyle().imageUpColor = colors[i];
                b[i].clicked(() -> color = j);
                b[i].update(() -> b[j].setChecked(color == j));
            }
            t.add(b[6]).size(40f);
            t.add(b[7]).size(40f);
            t.add(b[0]).size(40f);
            t.row().add(b[5]).size(40f);
            t.add(b[8]).size(40f);
            t.add(b[1]).size(40f);
            t.row().add(b[4]).size(40f);
            t.add(b[3]).size(40f);
            t.add(b[2]).size(40f);
        }

        @Override
        public Object[] config()
        {
            return new Object[]{shielding, color};
        }

        @Override
        public byte version()
        {
            return 5;
        }

        @Override
        public void write(Writes write)
        {
            super.write(write);
            write.b(color);
        }

        @Override
        public void read(Reads read, byte revision)
        {
            super.read(read, revision);
            if (revision >= 5) color = read.b();
        }
    }
}
