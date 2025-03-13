package esoterum;

import arc.Events;
import arc.util.*;
import esoterum.graph.*;
import esoterum.overlay.SignalOverlay;
import esoterum.ui.*;
import esoterum.world.blocks.decor.*;
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
    public static Thread t;
    private static boolean running = true;
    public static float latency = 0;

    public static Runnable r = () -> {
        try
        {
            boolean update;
            GraphEvent.eventType e;
            while (running)
            {
                if (Vars.state.isGame() && !Vars.state.isPaused())
                {
                    Time.mark();
                    update = !SignalGraph.events.isEmpty();
                    while ((e = SignalGraph.events.poll()) != null) e.run();
                    SignalGraph.updateBuilds(update);
                    latency = Time.elapsed();
                }
                else Thread.sleep(100);
            }
        } catch (Throwable e)
        {
            Log.err(e);
        }
    };

    public Esoterum()
    {
        Events.on(ClientLoadEvent.class, event -> {
            EsoStyles.init();
            EsoUI.init();
            DebugFrag.build();
        });

        Events.on(WorldLoadBeginEvent.class, event -> {
            SignalGraph.events.add(new GraphEvent.clearEvent());
            if (t == null || !t.isAlive()) (t = new Thread(r)).start();
        });

        Events.run(EventType.Trigger.drawOver, () -> {
            if (!Vars.state.isGame()) return;

            SignalOverlay.draw();
        });

        Events.run(DisposeEvent.class, () -> {
            running = false;
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
            dark = true;
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

        new SignalHyperClock("hyper-clock")
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

        new SignalGate("toggler")
        {{
            vertexCount = 4;
            setConns(0, 1, 2, 3);
            setInputs(0, 1, 1, 1);
            setOutputs(0, 0, 0, 0);
            function = gate -> {
                Building front = gate.front();
                if (front != null)
                {
                    front.enabled = ((gate.r[1].augmentation | gate.r[2].augmentation | gate.r[3].augmentation) >= 1);
                    return front.enabled;
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

        new SignalMatrix("matrix")
        {{
            vertexCount = 24;
            setConns(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 7, 6, 5, 4, 3, 2, 1, 0, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25);
            setInputs(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
            setOutputs(1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("signal-diode")
        {{
            vertexCount = 2;
            setConns(0, 0, 1, 0);
            setInputs(0, 0, 1, 0);
            setOutputs(1, 0, 0, 0);
            function = gate -> (gate.r[1].augmentation == 1);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("and-gate")
        {{
            vertexCount = 4;
            setConns(0, 1, 2, 3);
            setInputs(0, 1, 1, 1);
            setOutputs(1, 0, 0, 0);
            function = gate -> {
                return gate.r[1].augmentation == (gate.active[1] ? 1 : 0)
                    && gate.r[2].augmentation == (gate.active[2] ? 1 : 0)
                    && gate.r[3].augmentation == (gate.active[3] ? 1 : 0);
            };
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("bi-and-gate")
        {{
            vertexCount = 3;
            setConns(0, 1, 2, 1);
            setInputs(0, 1, 1, 1);
            setOutputs(1, 1, 0, 1);
            function = gate -> {
                return (gate.r[1].augmentation & gate.r[2].augmentation) == 1;
            };
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("or-gate")
        {{
            vertexCount = 4;
            setConns(0, 1, 2, 3);
            setInputs(0, 1, 1, 1);
            setOutputs(1, 0, 0, 0);
            function = gate -> (gate.r[1].augmentation | gate.r[2].augmentation | gate.r[3].augmentation) == 1; // functionally a diode
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("not-gate")
        {{
            vertexCount = 4;
            setConns(0, 1, 2, 3);
            setInputs(0, 1, 1, 1);
            setOutputs(1, 0, 0, 0);
            function = gate -> (gate.r[1].augmentation | gate.r[2].augmentation | gate.r[3].augmentation) != 1; // functionally NOR
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("xor-gate")
        {{
            vertexCount = 4;
            setConns(0, 1, 2, 3);
            setInputs(0, 1, 1, 1);
            setOutputs(1, 0, 0, 0);
            function = gate -> {
                return (gate.r[1].augmentation ^ gate.r[2].augmentation ^ gate.r[3].augmentation) == 1;
            };
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalGate("bi-xor-gate")
        {{
            vertexCount = 3;
            setConns(0, 1, 2, 1);
            setInputs(0, 1, 1, 1);
            setOutputs(1, 1, 0, 1);
            function = gate -> {
                return (gate.r[1].augmentation ^ gate.r[2].augmentation) == 1;
            };
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalMem("memory")
        {{
            vertexCount = 26;
            setConns(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 24, 24, 24, 25, 25, 25, 25);
            setInputs(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
            setOutputs(1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalDrawer("drawer")
        {{
            vertexCount = 1;
            setConns(0, 0, 0, 0);
            setInputs(1, 1, 1, 1);
            setOutputs(0, 0, 0, 0);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new SignalFlipFlop("t-flip-flop")
        {{
            vertexCount = 4;
            setConns(0, 1, 2, 3);
            setInputs(0, 1, 1, 1);
            setOutputs(1, 0, 0, 0);
        }}.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 1));

        new LabelBlock("label")
        {{
            size = 1;
        }}.requirements(Category.effect, BuildVisibility.shown, ItemStack.with(Items.copper, 1));
    }

    @Override
    public void init()
    {
        new SettingsDialog(); // Initialize the settings dialog
    }
}
