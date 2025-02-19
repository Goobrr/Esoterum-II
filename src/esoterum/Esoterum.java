package esoterum;

import arc.Events;
import esoterum.graph.SignalGraph;
import esoterum.overlay.SignalOverlay;
import esoterum.ui.*;
import esoterum.world.blocks.signal.*;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.game.EventType;
import mindustry.game.EventType.*;
import mindustry.gen.Building;
import mindustry.mod.Mod;
import mindustry.type.*;
import mindustry.world.meta.BuildVisibility;

public class Esoterum extends Mod
{

    public static boolean debug = false;

    public Esoterum()
    {
        Events.on(ClientLoadEvent.class, event -> {
            EsoUI.init();
        });

        Events.on(WorldLoadBeginEvent.class, event -> {
            SignalGraph.graph.clear();
        });

        Events.run(EventType.Trigger.drawOver, () -> {
            if (!Vars.state.isGame()) return;

            SignalOverlay.draw();
        });
    }

    @Override
    public void loadContent()
    {
        new SignalWire("signal-wire")
        {{
            rotate = true;
            vertexCount = 4;
            setConns(0, 1, 2, 3);
            setInputs(0, 1, 1, 1);
            setOutputs(1, 0, 0, 0);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalJunction("signal-junction")
        {{
            rotate = true;
            rotateDraw = false;
            drawArrow = false;
            vertexCount = 2;
            setConns(0, 1, 0, 1);
            setInputs(1, 1, 1, 1);
            setOutputs(1, 1, 1, 1);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalJunction("signal-cjunction")
        {{
            rotate = true;

            vertexCount = 2;
            setConns(0, 1, 1, 0);
            setInputs(1, 1, 1, 1);
            setOutputs(1, 1, 1, 1);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalBlock("true-signal-router")
        {{
            rotate = true;
            rotateDraw = false;
            drawArrow = false;
            vertexCount = 1;
            hasGraph = false;
            setConns(0, 0, 0, 0);
            setInputs(1, 1, 1, 1);
            setOutputs(1, 1, 1, 1);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalRouter("signal-router")
        {{
            rotate = true;
            vertexCount = 4;
            setConns(0, 1, 2, 3);
            setInputs(0, 0, 1, 0);
            setOutputs(1, 1, 0, 1);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalBridge("signal-bridge")
        {{
            rotate = true;
            rotateDraw = false;
            drawArrow = false;
            vertexCount = 1;
            setConns(0, 0, 0, 0);
            setInputs(1, 1, 1, 1);
            setOutputs(1, 1, 1, 1);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalSwitch("signal-switch")
        {{
            rotate = false;
            vertexCount = 1;
            setConns(0, 0, 0, 0);
            setInputs(0, 0, 0, 0);
            setOutputs(1, 1, 1, 1);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("item-sensor")
        {{
            vertexCount = 1;
            setConns(0, 0, 0, 0);
            setInputs(0, 0, 0, 0);
            setOutputs(0, 1, 1, 1);
            function = gate -> {
                Building front = gate.front();
                if (front != null && front.items() != null)
                {
                    return !front.items().empty();
                }

                return false;
            };
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalDisplay("display")
        {{
            rotate = true;

            vertexCount = 2;
            hasGraph = false;
            setConns(0, 0, 0, 0);
            setInputs(1, 0, 1, 0);
            setOutputs(1, 0, 1, 0);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("signal-diode")
        {{
            vertexCount = 2;
            setConns(0, 0, 1, 0);
            setInputs(0, 0, 1, 0);
            setOutputs(1, 0, 0, 0);
            function = gate -> (gate.signal[1] == 1);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("and-gate")
        {{
            vertexCount = 4;
            setConns(0, 1, 2, 3);
            setInputs(0, 1, 1, 1);
            setOutputs(1, 0, 0, 0);
            function = gate -> {
                int a = gate.signal[1];
                int b = gate.signal[2];
                int c = gate.signal[3];
                return a + b + c > 1;
            };
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("or-gate")
        {{
            vertexCount = 4;
            setConns(0, 1, 2, 3);
            setInputs(0, 1, 1, 1);
            setOutputs(1, 0, 0, 0);
            function = gate -> (gate.signal[1] | gate.signal[2] | gate.signal[3]) == 1; // functionally a diode
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("not-gate")
        {{
            vertexCount = 4;
            setConns(0, 1, 2, 3);
            setInputs(0, 1, 1, 1);
            setOutputs(1, 0, 0, 0);
            function = gate -> (gate.signal[1] | gate.signal[2] | gate.signal[3]) != 1; // functionally NOR
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("xor-gate")
        {{
            vertexCount = 4;
            setConns(0, 1, 2, 3);
            setInputs(0, 1, 1, 1);
            setOutputs(1, 0, 0, 0);
            function = gate -> {
                int a = gate.signal[1];
                int b = gate.signal[2];
                int c = gate.signal[3];
                return a + b + c == 1;
            };
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        // new SignalAdder("full-adder")
        // {{
        //     vertexCount = 5;
        //     setConns(0, 0, 0, 1, 2, 3, 4, 0);
        //     setInputs(0, 0, 0, 0, 1, 1, 1, 0);
        //     setOutputs(1, 0, 0, 1, 0, 0, 0, 0);
        // }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalMem("memory")
        {{
            vertexCount = 26;
            setConns(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 24, 24, 24, 25, 25, 25, 25);
            setInputs(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
            setOutputs(1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));
    }

    @Override
    public void init()
    {
        new SettingsDialog(); // Initialize the settings dialog
    }
}
