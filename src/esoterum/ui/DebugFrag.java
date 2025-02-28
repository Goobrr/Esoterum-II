package esoterum.ui;

import arc.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import esoterum.*;
import esoterum.graph.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.ui.*;

import static mindustry.Vars.*;

public class DebugFrag {
    private static float latency = 0;

    public static void build(){
        Timer.schedule(() -> {
            latency = Esoterum.latency;
        }, 0, 0.25f);

        WidgetGroup hudGroup = Vars.ui.hudGroup;
        Table t = hudGroup.find("fps/ping");
        t.clearChildren();

        t.top().left();
        t.margin(15f);

        t.label(() -> "Game").expandX().expandY().top().left().style(Styles.outlineLabel).color(Pal.accent).colspan(2).width(200).get().setAlignment(Align.left);
        t.label(() -> "Graph").expandX().expandY().top().left().style(Styles.outlineLabel).color(Pal.accent).colspan(2).width(200).get().setAlignment(Align.left);
        t.row();

        t.label(() -> "FPS").expandX().expandY().top().left().style(Styles.outlineLabel).width(100).get().setAlignment(Align.left);
        t.label(() -> "" + Core.graphics.getFramesPerSecond()).expandX().expandY().top().left().style(Styles.outlineLabel).width(100).name("fps").get().setAlignment(Align.left);

        t.label(() -> "Latency").expandX().expandY().top().left().style(Styles.outlineLabel).width(100).get().setAlignment(Align.left);
        t.label(() -> SignalGraph.builds.size == 0 ? "-" : latency + "ms").expandX().expandY().top().left().style(Styles.outlineLabel).width(100).name("ups").get().setAlignment(Align.left);
        t.row();

        t.label(() -> "MEM").expandX().expandY().top().left().style(Styles.outlineLabel).width(100).get().setAlignment(Align.left);
        t.label(() -> "" + (Core.app.getJavaHeap() / 1024 / 1024) + "mb").left().expandX().expandY().top().left().style(Styles.outlineLabel).width(100).name("memory").get().setAlignment(Align.left);

        t.label(() -> "Size").expandX().expandY().top().left().style(Styles.outlineLabel).width(100).get().setAlignment(Align.left);
        t.label(() -> "" + (EsoVars.darkMode ? SignalGraph.brights.size : SignalGraph.builds.size)).expandX().expandY().top().left().style(Styles.outlineLabel).width(100).name("size").get().setAlignment(Align.left);
        t.row();

        t.row();

        t.label(() -> "Ping").visible(net::client).expandX().expandY().top().left().style(Styles.outlineLabel).width(100).get().setAlignment(Align.left);
        t.label(() -> "" + Vars.netClient.getPing() + "ms").visible(net::client).expandX().expandY().top().left().style(Styles.outlineLabel).width(100).name("ping").get().setAlignment(Align.left);
        t.row();
        t.label(() -> "TPS").visible(net::client).expandX().expandY().top().left().style(Styles.outlineLabel).width(100).get().setAlignment(Align.left);
        t.label(() -> "" + (Vars.state.serverTps == -1 ? 60 : Vars.state.serverTps)).visible(net::client).expandX().expandY().top().left().style(Styles.outlineLabel).width(150).name("tps").get().setAlignment(Align.left);
    }
}
