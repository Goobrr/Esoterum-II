package esoterum;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import arc.util.pooling.*;
import mindustry.ui.*;

public class DrawUtils{
    private static final Font font = Fonts.outline;
    private static final GlyphLayout layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);

    // shamelessly stolen from PM
    public static float text(float x, float y, Color color, CharSequence text){
        return text(x, y, false, color, text, 0.25f);
    }

    public static float text(float x, float y, boolean underline, Color color, CharSequence text, float scale){
        boolean ints = font.usesIntegerPositions();
        font.setUseIntegerPositions(false);
        font.getData().setScale(scale);
        layout.setText(font, text);

        font.setColor(color);
        font.draw(text, x, y + (underline ? layout.height + 1 : layout.height / 2f), Align.center);
        if(underline){
            y -= 1f;
            Lines.stroke(2f, Color.darkGray);
            Lines.line(x - layout.width / 2f - 2f, y, x + layout.width / 2f + 1.5f, y);
            Lines.stroke(1f, color);
            Lines.line(x - layout.width / 2f - 2f, y, x + layout.width / 2f + 1.5f, y);
        }

        float width = layout.width;

        font.setUseIntegerPositions(ints);
        font.setColor(Color.white);
        font.getData().setScale(1f);
        Draw.reset();
        Pools.free(layout);

        return width;
    }
}
