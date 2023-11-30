package esoterum;

import arc.*;
import arc.util.*;
import esoterum.ui.*;
import esoterum.world.blocks.signal.*;
import mindustry.content.Items;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.meta.BuildVisibility;

public class Esoterum extends Mod{

    public static boolean debug = false;
    public Esoterum(){
        Events.on(ClientLoadEvent.class, event -> {
            Log.info("Waagh");
            EsoUI.init();
        });
    }

    @Override
    public void loadContent(){

        new SignalWire("signal-wire"){{
            rotate = true;
            setOutputs(0);
            setInputs(1, 2, 3);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalJunction("signal-junction"){{
            setInputs(0, 1, 2, 3);
            setOutputs(0, 1, 2, 3);
            mapIO(0, 2, 1, 3);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalWire("signal-router"){{
            rotate = true;
            setOutputs(0, 1, 3);
            setInputs(2);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalSwitch("signal-switch"){{
            rotate = false;
            setOutputs(0, 1, 2, 3);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalChipBlock("test-chip"){{
            size = 2;
            setInputs(4, 5);
            setOutputs(0, 1);

            debugDraw = true;
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("signal-diode"){{
            setInputs(2);
            setOutputs(0);
            function = gate -> gate.signalAtInput(2);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("and-gate"){{
            setInputs(1, 2, 3);
            setOutputs(0);
            function = gate -> {
                int a = gate.signalAtInput(1) ? 1 : 0;
                int b = gate.signalAtInput(2) ? 1 : 0;
                int c = gate.signalAtInput(3) ? 1 : 0;
                return a + b + c > 1;
            };
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("or-gate"){{
            setInputs(1, 2, 3);
            setOutputs(0);
            function = gate -> gate.signalAtInput(1) | gate.signalAtInput(2) | gate.signalAtInput(3); // functionally a diode
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("not-gate"){{
            setInputs(1, 2, 3);
            setOutputs(0);
            function = gate -> !(gate.signalAtInput(1) | gate.signalAtInput(2) | gate.signalAtInput(3)); // functionally NOR
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("xor-gate"){{
            setInputs(1, 2, 3);
            setOutputs(0);
            function = gate -> {
                int a = gate.signalAtInput(1) ? 1 : 0;
                int b = gate.signalAtInput(2) ? 1 : 0;
                int c = gate.signalAtInput(3) ? 1 : 0;
                return a + b + c == 1;
            };
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));
    }

}
