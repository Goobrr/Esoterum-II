package esoterum.world.blocks.signal;

import esoterum.graph.SignalGraph;

public class SignalMem extends SignalBlock {
    public SignalMem(String name)
    {
        super(name);

        rotate = true;
        size = 8;
        hasGraph = false;
    }

    public class SignalMemBuild extends SignalBuild
    {
        int[] mem = new int[256*8];
        
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
                SignalGraph.graph.setVertexAugmentation(v[7], mem[(addr << 3)]);
                SignalGraph.graph.setVertexAugmentation(v[6], mem[(addr << 3)+1]);
                SignalGraph.graph.setVertexAugmentation(v[5], mem[(addr << 3)+2]);
                SignalGraph.graph.setVertexAugmentation(v[4], mem[(addr << 3)+3]);
                SignalGraph.graph.setVertexAugmentation(v[3], mem[(addr << 3)+4]);
                SignalGraph.graph.setVertexAugmentation(v[2], mem[(addr << 3)+5]);
                SignalGraph.graph.setVertexAugmentation(v[1], mem[(addr << 3)+6]);
                SignalGraph.graph.setVertexAugmentation(v[0], mem[(addr << 3)+7]);
            }
        }
    }
}