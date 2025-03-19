package esoterum.world.blocks.signal;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Rect;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import esoterum.graph.GraphEvent;
import esoterum.graph.SignalGraph;
import esoterum.ui.EsoStyles;

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

        @Override
        public void updateSignal()
        {
            if (r[0] != null) signal[0] = r[0].augmentation;
            if (r[1] != null) signal[1] = r[1].augmentation;
            int r = ((signal[1] >> index) & 1) == 1 ? -1 : 0;
            if (r != outputSignal) SignalGraph.graph.setNodeAugmentation(e[0], outputSignal = r);
        }

        @Override
        public void drawSignalRegions(Rect camera)
        {
            Draw.color(getWireOffColor().cpy().lerp(getWireColor(), (float) (signal[0] & 0xFFFF) / 0xFFFF));
            Draw.rect(signalRegions[index], x, y, rotation * 90);
            if (active[0]) Draw.rect(outputSignalRegions[0], x, y, rotation * 90);
            Draw.color(getWireOffColor().cpy().lerp(getWireColor(), (float) (signal[1] & 0xFFFF) / 0xFFFF));
            if (active[2]) Draw.rect(inputSignalRegions[2], x, y, rotation * 90);
        }

        @Override
        public void buildConfiguration(Table table)
        {
            table.table().size(40f);
            for (int i = size * 6 - rotation * size - 1; i >= size * 5 - rotation * size; i--)
            {
                final int t = i % (size * 4);
                TextButton b = table.button((shielding & (1L << t)) > 0 ? "" + t : "X", () -> {
                    configure(1L << t);
                }).size(40f).tooltip("Toggle Shielding").get();
                b.setStyle(EsoStyles.esoflatt);
                b.update(() -> {
                    b.setText((shielding & (1L << t)) > 0 ? "" + t : "X");
                });
            }
            table.row();
            for (int i = 0; i < size; i++)
            {
                final int t1 = (6 * size + i - rotation * size) % (size * 4);
                TextButton b1 = table.button((shielding & (1L << t1)) > 0 ? "" + t1 : "X", () -> {
                    configure(1L << t1);
                }).size(40f).tooltip("Toggle Shielding").get();
                b1.setStyle(EsoStyles.esoflatt);
                b1.update(() -> {
                    b1.setText((shielding & (1L << t1)) > 0 ? "" + t1 : "X");
                });
                for (int j = 0; j < size; j++)
                {
                    TextButton bm = table.button(String.format("%X", index & 15), () -> {
                        configure((index + 1) % 16);
                    }).size(40f).get();
                    bm.setStyle(EsoStyles.esoflatt);
                    bm.update(() -> {
                        bm.setText(String.format("%X", index & 15));
                    });
                }
                final int t2 = (size * 5 - i - 1 - rotation * size) % (size * 4);
                TextButton b2 = table.button((shielding & (1L << t2)) > 0 ? "" + t2 : "X", () -> {
                    configure(1L << t2);
                }).size(40f).tooltip("Toggle Shielding").get();
                b2.setStyle(EsoStyles.esoflatt);
                b2.update(() -> {
                    b2.setText((shielding & (1L << t2)) > 0 ? "" + t2 : "X");
                });
                table.row();
            }
            table.table().size(40f);
            for (int i = size * 7 - rotation * size; i < size * 8 - rotation * size; i++)
            {
                final int t = i % (size * 4);
                TextButton b = table.button((shielding & (1L << t)) > 0 ? "" + t : "X", () -> {
                    configure(1L << t);
                }).size(40f).tooltip("Toggle Shielding").get();
                b.setStyle(EsoStyles.esoflatt);
                b.update(() -> {
                    b.setText((shielding & (1L << t)) > 0 ? "" + t : "X");
                });
            }
        }

        public Object[] config()
        {
            return new Object[]{shielding, index};
        }

        @Override
        public void write(Writes write)
        {
            super.write(write);
            write.b(index);
        }

        @Override
        public void read(Reads read, byte revision)
        {
            super.read(read, revision);
            index = read.b();
        }
    }
}
