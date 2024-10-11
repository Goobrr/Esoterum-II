package esoterum.ui;

import esoterum.EsoVars;
import mindustry.gen.Icon;

import static arc.Core.*;
import static mindustry.Vars.*;

public class SettingsDialog
{
    public SettingsDialog()
    {
        ui.settings.hidden(this::run);
        ui.settings.addCategory("@category.eso.name", Icon.settings, table -> {
            table.checkPref("manhattannode", true);
        });

        run();
    }

    public void run()
    {
        EsoVars.drawNodesAsManhattan = settings.getBool("manhattannode");
    }
}
