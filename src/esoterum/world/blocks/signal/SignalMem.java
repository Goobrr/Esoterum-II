package esoterum.world.blocks.signal;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import esoterum.EsoVars;
import esoterum.graph.SignalGraph;

public class SignalMem extends SignalBlock {
    public TextureRegion fullWireRegion, leftWireRegion, rightWireRegion;
    public SignalMem(String name)
    {
        super(name);

        rotate = true;
        size = 8;
        hasGraph = false;
        configurable = false;
    }

    @Override
    public void load()
    {
        super.load();
        fullWireRegion = Core.atlas.find("eso-memory-wire-full", "eso-none");
        leftWireRegion = Core.atlas.find("eso-memory-wire-left", "eso-none");
        rightWireRegion = Core.atlas.find("eso-memory-wire-right", "eso-none");
    }

    public class SignalMemBuild extends SignalBuild
    {
        int[] mem = new int[256*8];

        @Override
        public void buildConfiguration(Table table)
        {
            // disable shielding for memory blocks
        }

        @Override
        public void drawSignalRegions()
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
            if (EsoVars.drawSignalRegions)
            {
                Draw.rect(outputSignalRegions[rotation], x, y);
                drawSignalRegions();
            }
            else
            {
                Draw.rect(uiIcon, x, y, rotation * 90);
            }
        }

        @Override
        public void updateTile()
        {
            super.updateTile();
            int addr = (signal[8] + 
                (signal[9] << 1) +
                (signal[10] << 2) +
                (signal[11] << 3) +
                (signal[12] << 4) +
                (signal[13] << 5) +
                (signal[14] << 6) +
                (signal[15] << 7));
            
            if(signal[24] == 1){
                mem[(addr << 3)] = signal[16];
                mem[(addr << 3)+1] = signal[17];
                mem[(addr << 3)+2] = signal[18];
                mem[(addr << 3)+3] = signal[19];
                mem[(addr << 3)+4] = signal[20];
                mem[(addr << 3)+5] = signal[21];
                mem[(addr << 3)+6] = signal[22];
                mem[(addr << 3)+7] = signal[23];
            }

            if(signal[25] == 1){
                if (mem[(addr << 3)] != signal[7]) SignalGraph.graph.setVertexAugmentation(v[7], mem[(addr << 3)]);
                if (mem[(addr << 3)+1] != signal[6]) SignalGraph.graph.setVertexAugmentation(v[6], mem[(addr << 3)+1]);
                if (mem[(addr << 3)+2] != signal[5]) SignalGraph.graph.setVertexAugmentation(v[5], mem[(addr << 3)+2]);
                if (mem[(addr << 3)+3] != signal[4]) SignalGraph.graph.setVertexAugmentation(v[4], mem[(addr << 3)+3]);
                if (mem[(addr << 3)+4] != signal[3]) SignalGraph.graph.setVertexAugmentation(v[3], mem[(addr << 3)+4]);
                if (mem[(addr << 3)+5] != signal[2]) SignalGraph.graph.setVertexAugmentation(v[2], mem[(addr << 3)+5]);
                if (mem[(addr << 3)+6] != signal[1]) SignalGraph.graph.setVertexAugmentation(v[1], mem[(addr << 3)+6]);
                if (mem[(addr << 3)+7] != signal[0]) SignalGraph.graph.setVertexAugmentation(v[0], mem[(addr << 3)+7]);
            }
        }
    }
}