package esoterum.ui;

import esoterum.ui.dialog.NodeEditorDialog;

public class EsoUI
{
    public static NodeEditorDialog nodeEditor;

    public static void init()
    {
        nodeEditor = new NodeEditorDialog();
    }
}
