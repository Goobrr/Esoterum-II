package esoterum.world.blocks.signal;

import arc.scene.ui.layout.*;
import esoterum.ui.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.*;

public class SignalChipBlock extends SignalBlock{
    public SignalChipBlock(String name){
        super(name);

        rotate = true;
        hasGraph = false;
        canFloodfill = false;
        configurable = true;

        logicConfigurable = false;
    }

    public class SignalChipBuild extends SignalBuild{

        boolean[] signals;

        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation){
            signals = new boolean[outputs.length];

            return super.init(tile, team, shouldAdd, rotation);
        }

        @Override
        public void update(){
            super.update();

            for(int i : outputs()){
                sendSignal(i, signalAtOutput(i));
            }
        }

        @Override
        public boolean signalAtOutput(int index){
            for(int i : outputs()){
                if(index == i) return signals[i];
            }
            return false;
        }

        @Override
        public void buildConfiguration(Table table){
            table.button(Icon.pencil, Styles.cleari, () -> {
                EsoUI.nodeEditor.show();
            }).size(40);
        }
    }
}
