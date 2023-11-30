package esoterum.ui;

import arc.*;
import arc.util.*;
import esoterum.ui.dialog.*;
import mindustry.game.EventType.*;

public class EsoUI{
    public static NodeEditorDialog nodeEditor;

    public static void init(){
        nodeEditor = new NodeEditorDialog();
    }
}
