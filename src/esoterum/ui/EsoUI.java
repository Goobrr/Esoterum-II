package esoterum.ui;

import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import esoterum.ui.dialog.NodeEditorDialog;
import mindustry.gen.*;
import mindustry.ui.*;

public class EsoUI
{
    private static final StringBuilder buffer = new StringBuilder();
    public static NodeEditorDialog nodeEditor;

    public static IntSeq hAligns = new IntSeq(new int[]{Align.left, Align.center, Align.right});
    public static IntSeq vAligns = new IntSeq(new int[]{Align.top, Align.center, Align.bottom});

    public static void init()
    {
        nodeEditor = new NodeEditorDialog();
    }

    // Temporary
    // Remove once UI::formatIcons is in release
    public static String formatIcons(String s){
        if(!s.contains(":")) return s;

        buffer.setLength(0);
        boolean changed = false;

        boolean checkIcon = false;
        String[] tokens = s.split(":");
        for(String token : tokens){
            if(checkIcon){
                if(Iconc.codes.containsKey(token)){
                    buffer.append((char)Iconc.codes.get(token));
                    changed = true;
                    checkIcon = false;
                }else if(Fonts.hasUnicodeStr(token)){
                    buffer.append(Fonts.getUnicodeStr(token));
                    changed = true;
                    checkIcon = false;
                }else{
                    buffer.append(":").append(token);
                }
            }else{
                buffer.append(token);
                checkIcon = true;
            }
        }

        return changed ? buffer.toString() : s;
    }
}
