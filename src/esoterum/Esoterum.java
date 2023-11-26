package esoterum;

import esoterum.world.blocks.signal.*;
import mindustry.content.Items;
import mindustry.mod.*;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.meta.BuildVisibility;

public class Esoterum extends Mod{

    public static boolean debug = false;

    public Esoterum(){

    }

    @Override
    public void loadContent(){

        new SignalWire("signal-wire"){{
            rotate = true;
            setOutputs(0);
            setInputs(1, 2, 3);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalWire("signal-router"){{
            rotate = true;
            setOutputs(0, 1, 3);
            setInputs(2);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalSwitch("signal-switch"){{
            rotate = true;
            setOutputs(0, 1, 2, 3);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalBlock("test-3"){{
            rotate = true;
            size = 3;
            setInputs(6, 8);
            setOutputs(1);

            hasGraph = false;
            canFloodfill = false;

            debugDraw = true;
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
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));;
    }

}
