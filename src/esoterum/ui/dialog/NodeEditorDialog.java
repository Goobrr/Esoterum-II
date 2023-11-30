package esoterum.ui.dialog;

import arc.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;

// TODO
// actually make the thing
public class NodeEditorDialog extends BaseDialog{
    public NodeEditorDialog(){
        super("Eso UI Editor");

        onResize(this::rebuild);

        titleTable.clear();
    }

    @Override
    public Dialog show(){
        rebuild();
        return super.show();
    }

    public void rebuild(){
        cont.clear();

        float scaleUnit = Math.min(Core.graphics.getHeight(), Core.graphics.getWidth()) / Scl.scl(5f);
        cont.table(Tex.whiteui, t -> {
            t.left();
            t.setColor(Pal.darkerGray);
            t.label(() -> "ESOCORP(r) Chip Programmer v0.1 Beta").left().padLeft(15f).get().setAlignment(Align.left);
        }).height(40f).width((scaleUnit * 7) + 70);
        cont.row();
        cont.table(Tex.whiteui, t -> {
            t.setColor(Pal.darkestGray);
            t.margin(5f);
            t.defaults().pad(10f);
            t.table(Tex.button, t1 -> {

            }).size(scaleUnit * 1.5f, scaleUnit * 4);;
            t.table(Tex.button, t2 -> {

            }).size(scaleUnit * 4);
            t.table(Tex.button, t3 -> {

            }).size(scaleUnit * 1.5f, scaleUnit * 4);
        });
        cont.row();
        cont.table(t -> {
            t.left().top();
            t.defaults().pad(10f).padTop(0f);
            t.button(Icon.exit, this::hide).size(50f).padRight(4f).tooltip("Save & Exit");
            t.button(Icon.cancel, () -> {}).size(50f).padRight(4f).tooltip("Exit without saving");
            t.button(Icon.refresh, () -> {}).size(50f).padRight(4f).tooltip("Reset");
        }).left().top();
    }
}
