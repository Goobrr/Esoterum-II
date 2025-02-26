package esoterum.ui;

import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import mindustry.ui.*;

public class EsoStyles
{
    public static TextButton.TextButtonStyle esoflatt, memflatt;
    public static ImageButton.ImageButtonStyle esoflati;

    public static void init()
    {
        esoflatt = new TextButton.TextButtonStyle()
        {{
            over = Styles.black5;
            font = Fonts.def;
            fontColor = Styles.flatt.fontColor;
            disabledFontColor = Styles.flatt.disabledFontColor;
            down = Styles.black9;
            up = Styles.black5;
        }};

        memflatt = new TextButton.TextButtonStyle()
        {{
            over = Styles.accentDrawable;
            checked = Styles.accentDrawable;
            font = Fonts.def;
            fontColor = Styles.flatt.fontColor;
            disabledFontColor = Styles.flatt.disabledFontColor;
            down = Styles.black9;
            up = Styles.black5;
        }};

        esoflati = new ImageButton.ImageButtonStyle(Styles.flati)
        {{
            over = Styles.black5;
            down = Styles.black9;
            up = Styles.black5;
        }};
    }
}
