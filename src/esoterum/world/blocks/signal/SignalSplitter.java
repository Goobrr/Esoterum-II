package esoterum.world.blocks.signal;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Rect;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import esoterum.graph.GraphEvent;
import esoterum.graph.SignalGraph;
import esoterum.ui.EsoStyles;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

public class SignalSplitter extends SignalBlock
{
    public TextureRegion[] signalRegions;

    public SignalSplitter(String name)
    {
        super(name);

        config(Integer.class, (SignalSplitterBuild tile, Integer i) -> {
            tile.index = i;
        });

        config(Object[].class, (SignalSplitterBuild tile, Object[] o) -> {
            if (o[0] instanceof Long l) tile.shielding = l;
            if (o[1] instanceof Integer i) tile.index = i;
            if (o[2] instanceof Byte b) tile.color = b;
        });
    }

    public void load()
    {
        super.load();

        signalRegions = new TextureRegion[16];
        for (int i = 0; i < 16; i++) signalRegions[i] = Core.atlas.find(name + "-signal-" + i);
    }

    public class SignalSplitterBuild extends SignalBuild
    {
        public int index = 0, outputSignal;
        public byte color = 8;

        @Override
        public void updateSignal()
        {
            if (r[0] != null) signal[0] = r[0].augmentation;
            if (r[1] != null) signal[1] = r[1].augmentation;
            int r = (color == 8 ? signal[1] & 0x70000 : (color << 16)) | (((signal[1] >> index) & 1) == 1 ? 0xFFFF : 0);
            if (r != outputSignal) SignalGraph.graph.setNodeAugmentation(e[0], outputSignal = r);
        }

        @Override
        public void drawSignalRegions(Rect camera)
        {
            Draw.color(getWireColor(0));
            Draw.rect(signalRegions[index], x, y, rotation * 90);
            if (active[0]) Draw.rect(outputSignalRegions[0], x, y, rotation * 90);
            Draw.color(getWireColor(1));
            if (active[2]) Draw.rect(inputSignalRegions[2], x, y, rotation * 90);
        }

        @Override
        public void buildConfiguration(Table table)
        {
            table.table().size(120f).growX();
            table.row();
            Table t0 = table.table().grow().get();
            t0.table().size(40f);
            for (int i = size * 6 - rotation * size - 1; i >= size * 5 - rotation * size; i--)
            {
                final int t = i % (size * 4);
                TextButton b = t0.button((shielding & (1L << t)) > 0 ? "" + t : "X", () -> {
                    configure(1L << t);
                }).size(40f).tooltip("Toggle Shielding").get();
                b.setStyle(EsoStyles.esoflatt);
                b.update(() -> {
                    b.setText((shielding & (1L << t)) > 0 ? "" + t : "X");
                });
            }
            t0.row();
            for (int i = 0; i < size; i++)
            {
                final int t1 = (6 * size + i - rotation * size) % (size * 4);
                TextButton b1 = t0.button((shielding & (1L << t1)) > 0 ? "" + t1 : "X", () -> {
                    configure(1L << t1);
                }).size(40f).tooltip("Toggle Shielding").get();
                b1.setStyle(EsoStyles.esoflatt);
                b1.update(() -> {
                    b1.setText((shielding & (1L << t1)) > 0 ? "" + t1 : "X");
                });
                for (int j = 0; j < size; j++)
                {
                    TextButton bm = t0.button(String.format("%X", index & 15), () -> {
                        configure((index + 1) % 16);
                    }).size(40f).get();
                    bm.setStyle(EsoStyles.esoflatt);
                    bm.update(() -> {
                        bm.setText(String.format("%X", index & 15));
                    });
                }
                final int t2 = (size * 5 - i - 1 - rotation * size) % (size * 4);
                TextButton b2 = t0.button((shielding & (1L << t2)) > 0 ? "" + t2 : "X", () -> {
                    configure(1L << t2);
                }).size(40f).tooltip("Toggle Shielding").get();
                b2.setStyle(EsoStyles.esoflatt);
                b2.update(() -> {
                    b2.setText((shielding & (1L << t2)) > 0 ? "" + t2 : "X");
                });
                t0.row();
            }
            t0.table().size(40f);
            for (int i = size * 7 - rotation * size; i < size * 8 - rotation * size; i++)
            {
                final int t = i % (size * 4);
                TextButton b = t0.button((shielding & (1L << t)) > 0 ? "" + t : "X", () -> {
                    configure(1L << t);
                }).size(40f).tooltip("Toggle Shielding").get();
                b.setStyle(EsoStyles.esoflatt);
                b.update(() -> {
                    b.setText((shielding & (1L << t)) > 0 ? "" + t : "X");
                });
            }
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
            return new Object[]{shielding, index, color};
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
            write.b(index);
            write.b(color);
        }

        @Override
        public void read(Reads read, byte revision)
        {
            super.read(read, revision);
            index = read.b();
            if (revision >= 5) color = read.b();
        }
    }
}
