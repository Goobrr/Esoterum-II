package esoterum.overlay;

import static mindustry.Vars.player;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.Color;
import arc.math.geom.Rect;
import esoterum.EsoVars;
import esoterum.world.blocks.signal.SignalBlock.SignalBuild;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.graphics.Layer;

public class SignalOverlay {
    public static Rect camRect = new Rect(), tileRect = new Rect();
    public static void draw()
    {
        if (!EsoVars.drawSignalRegions) return;
        Core.camera.bounds(camRect);
        Vars.indexer.eachBlock(player.team(), camRect, build -> {return true;}, build -> {
            if (build instanceof SignalBuild b)
            {
                Draw.z(30.05f);
                Draw.color(Color.white);
                b.drawShieldRegions();
                b.drawSignalRegions(camRect);
            }
        });
        Draw.reset();
    }
}
