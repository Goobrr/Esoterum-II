package esoterum.world.blocks.signal;

import arc.Core;
import arc.graphics.g2d.*;
import arc.math.geom.Rect;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.Eachable;
import arc.util.io.*;
import esoterum.EsoVars;
import esoterum.graph.SignalGraph;
import esoterum.ui.EsoStyles;
import mindustry.entities.units.BuildPlan;
import mindustry.ui.Styles;

public class SignalMem extends SignalBlock
{
    public TextureRegion fullWireRegion, leftWireRegion, rightWireRegion, pinoutRegion;

    public SignalMem(String name)
    {
        super(name);

        rotate = true;
        size = 8;
        hasGraph = false;

        config(Object[].class, (SignalMemBuild tile, Object[] p) -> {
            if (p[0] instanceof int[] m) tile.mem = m.clone();
            if (p[1] instanceof Boolean b) tile.persist = b;
        });
    }

    @Override
    public void load()
    {
        super.load();
        fullWireRegion = Core.atlas.find("eso-memory-wire-full", "eso-none");
        leftWireRegion = Core.atlas.find("eso-memory-wire-left", "eso-none");
        rightWireRegion = Core.atlas.find("eso-memory-wire-right", "eso-none");
        pinoutRegion = Core.atlas.find("eso-memory-pinout", "eso-none");
    }

    @Override
    public TextureRegion getPlanRegion(BuildPlan plan, Eachable<BuildPlan> list)
    {
        return outputSignalRegions[0];
    }

    public class SignalMemBuild extends SignalBuild
    {
        int[] mem = new int[256];
        int bit = 0, mode = 1;
        boolean persist = false;

        @Override
        public void buildConfiguration(Table table)
        {
            table.table().size(40f);
            table.row();
            Table gtable = table.table().get();
            gtable.margin(5f);
            gtable.setBackground(Styles.black5);
            for (int i = 0; i < 16; i++)
            {
                for (int j = 0; j < 16; j++)
                {
                    int addr = (i << 4) | j;
                    TextButton b = gtable.button(String.format("%02X", mem[addr]), () -> {
                        mem[addr] = (mem[addr] + (1 << bit) * mode) & 255;
                    }).size(40f).pad(2f).get();
                    b.setStyle(EsoStyles.memflatt);
                    b.update(() -> {
                        int sel = signal[8] |
                                (signal[9] << 1) |
                                (signal[10] << 2) |
                                (signal[11] << 3) |
                                (signal[12] << 4) |
                                (signal[13] << 5) |
                                (signal[14] << 6) |
                                (signal[15] << 7);
                        b.setChecked(sel == addr);
                        b.setText(String.format("%02X", mem[addr]));
                    });
                }
                gtable.row();
            }
            table.row();
            Table btable = table.table().growX().get();
            btable.setBackground(Styles.black5);
            btable.margin(5f);
            TextButton b = btable.button("" + (bit + 1), () -> {
                bit = (bit + 1) % 8;
            }).size(40f).get();
            b.update(() -> {
                b.setText("" + (bit + 1));
            });
            TextButton m = btable.button(mode > 0 ? "+" : "-", () -> {
                mode *= -1;
            }).size(40f).get();
            m.update(() -> {
                m.setText(mode > 0 ? "+" : "-");
            });
        }

        @Override
        public void drawSignalRegions(Rect camera)
        {
            Draw.color((signal[0]
                    | signal[1]
                    | signal[2]
                    | signal[3]
                    | signal[4]
                    | signal[5]
                    | signal[6]
                    | signal[7]) > 0 ? getWireColor() : getWireOffColor());
            Draw.rect(fullWireRegion, x, y, rotation * 90);
            Draw.color((signal[8]
                    | signal[9]
                    | signal[10]
                    | signal[11]
                    | signal[12]
                    | signal[13]
                    | signal[14]
                    | signal[15]) > 0 ? getWireColor() : getWireOffColor());
            Draw.rect(fullWireRegion, x, y, rotation * 90 + 90);
            Draw.color((signal[16]
                    | signal[17]
                    | signal[18]
                    | signal[19]
                    | signal[20]
                    | signal[21]
                    | signal[22]
                    | signal[23]) > 0 ? getWireColor() : getWireOffColor());
            Draw.rect(fullWireRegion, x, y, rotation * 90 + 180);
            Draw.color(signal[24] > 0 ? getWireColor() : getWireOffColor());
            Draw.rect(leftWireRegion, x, y, rotation * 90);
            Draw.color(signal[25] > 0 ? getWireColor() : getWireOffColor());
            Draw.rect(rightWireRegion, x, y, rotation * 90);
        }

        @Override
        public void draw()
        {
            if (EsoVars.drawSignalRegions) Draw.rect(persist ? outputSignalRegions[rotation] : inputSignalRegions[rotation], x, y);
            else Draw.rect(uiIcon, x, y, rotation * 90);
        }

        @Override
        public void drawSelect()
        {
            super.drawSelect();

            Draw.rect(pinoutRegion, x, y, rotation * 90);
        }

        @Override
        public void drawShieldRegions()
        {
        }

        public Object[] config()
        {
            return new Object[]{mem, persist};
        }

        @Override
        public void updateSignal(boolean update)
        {
            super.updateSignal(update);
            int addr = signal[8] |
                    (signal[9] << 1) |
                    (signal[10] << 2) |
                    (signal[11] << 3) |
                    (signal[12] << 4) |
                    (signal[13] << 5) |
                    (signal[14] << 6) |
                    (signal[15] << 7);

            if (signal[24] == 1)
            {
                mem[addr] = signal[16] |
                        (signal[17] << 1) |
                        (signal[18] << 2) |
                        (signal[19] << 3) |
                        (signal[20] << 4) |
                        (signal[21] << 5) |
                        (signal[22] << 6) |
                        (signal[23] << 7);
            }

            if (signal[25] == 1)
            {
                if ((mem[addr] & 1) != signal[7]) SignalGraph.graph.setVertexAugmentation(v[7], mem[addr] & 1);
                if (((mem[addr] >> 1) & 1) != signal[6])
                    SignalGraph.graph.setVertexAugmentation(v[6], (mem[addr] >> 1) & 1);
                if (((mem[addr] >> 2) & 1) != signal[5])
                    SignalGraph.graph.setVertexAugmentation(v[5], (mem[addr] >> 2) & 1);
                if (((mem[addr] >> 3) & 1) != signal[4])
                    SignalGraph.graph.setVertexAugmentation(v[4], (mem[addr] >> 3) & 1);
                if (((mem[addr] >> 4) & 1) != signal[3])
                    SignalGraph.graph.setVertexAugmentation(v[3], (mem[addr] >> 4) & 1);
                if (((mem[addr] >> 5) & 1) != signal[2])
                    SignalGraph.graph.setVertexAugmentation(v[2], (mem[addr] >> 5) & 1);
                if (((mem[addr] >> 6) & 1) != signal[1])
                    SignalGraph.graph.setVertexAugmentation(v[1], (mem[addr] >> 6) & 1);
                if (((mem[addr] >> 7) & 1) != signal[0])
                    SignalGraph.graph.setVertexAugmentation(v[0], (mem[addr] >> 7) & 1);
            } else if (persist)
            {
                if(signal[7] != 0) SignalGraph.graph.setVertexAugmentation(v[7], 0);
                if(signal[6] != 0) SignalGraph.graph.setVertexAugmentation(v[6], 0);
                if(signal[5] != 0) SignalGraph.graph.setVertexAugmentation(v[5], 0);
                if(signal[4] != 0) SignalGraph.graph.setVertexAugmentation(v[4], 0);
                if(signal[3] != 0) SignalGraph.graph.setVertexAugmentation(v[3], 0);
                if(signal[2] != 0) SignalGraph.graph.setVertexAugmentation(v[2], 0);
                if(signal[1] != 0) SignalGraph.graph.setVertexAugmentation(v[1], 0);
                if(signal[0] != 0) SignalGraph.graph.setVertexAugmentation(v[0], 0);
            }
        }

        @Override
        public byte version()
        {
            return 6;
        }

        @Override
        public void write(Writes write)
        {
            super.write(write);
            for (int i = 0; i < 256; i++) write.b(mem[i]);
            write.bool(persist);
        }

        @Override
        public void read(Reads read, byte revision)
        {
            super.read(read, revision);
            if (revision >= 5) for (int i = 0; i < 256; i++) mem[i] = ((int) read.b()) & 0xFF;
            if (revision >= 6) persist = read.bool();
        }
    }
}