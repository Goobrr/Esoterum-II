package esoterum.ui;

import esoterum.ui.dialog.*;

public class EsoUI{
    public static NodeEditorDialog nodeEditor;

    public static void init(){
        nodeEditor = new NodeEditorDialog();
    }
}
