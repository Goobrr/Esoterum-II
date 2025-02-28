package esoterum.ui;

import arc.Core;
import arc.func.*;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.scene.event.Touchable;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.Table;
import esoterum.EsoVars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.SettingsMenuDialog;

import static arc.Core.*;
import static mindustry.Vars.*;

public class SettingsDialog
{
    public SettingsDialog()
    {
        if (!headless)
        {
            ui.settings.hidden(this::run);
            ui.settings.addCategory("@category.eso.name", Icon.settings, table -> {
                table.pref(new LabelSetting("displayoffcolor"));

                settings.defaults("displayroff", 255);
                settings.defaults("displaygoff", 255);
                settings.defaults("displayboff", 255);

                Cons<Integer> offChanged = e -> EsoVars.displayOffColor = GetOffColor();

                ColorSliderSetting rOffSlider = new ColorSliderSetting(0, 255, 1, "displayroff", true, false, false, false, offChanged, this::GetOffColor);
                ColorSliderSetting gOffSlider = new ColorSliderSetting(0, 255, 1, "displaygoff", false, true, false, false, offChanged, this::GetOffColor);
                ColorSliderSetting bOffSlider = new ColorSliderSetting(0, 255, 1, "displayboff", false, false, true, true, offChanged, this::GetOffColor);

                table.pref(rOffSlider);
                table.pref(gOffSlider);
                table.pref(bOffSlider);

                table.pref(new LabelSetting("displaycolor"));

                settings.defaults("displayr", (int) (Pal.accent.r * 255f));
                settings.defaults("displayg", (int) (Pal.accent.g * 255f));
                settings.defaults("displayb", (int) (Pal.accent.b * 255f));

                Cons<Integer> onChanged = e -> EsoVars.displayColor = GetColor();

                ColorSliderSetting rSlider = new ColorSliderSetting(0, 255, 1, "displayr", true, false, false, false, onChanged, this::GetColor);
                ColorSliderSetting gSlider = new ColorSliderSetting(0, 255, 1, "displayg", false, true, false, false, onChanged, this::GetColor);
                ColorSliderSetting bSlider = new ColorSliderSetting(0, 255, 1, "displayb", false, false, true, true, onChanged, this::GetColor);

                table.pref(rSlider);
                table.pref(gSlider);
                table.pref(bSlider);

                table.checkPref("manhattannode", true);
                table.checkPref("drawsignals", true);
                table.checkPref("darkmode", false);
            });

            run();
        }
    }

    private Slider.SliderStyle CopyStyle(Slider.SliderStyle style)
    {
        Slider.SliderStyle newStyle = new Slider.SliderStyle();

        newStyle.knobOver = style.knobOver;
        newStyle.knobDown = style.knobDown;

        newStyle.background = style.background;
        newStyle.disabledBackground = style.disabledBackground;
        newStyle.knob = style.knob;
        newStyle.disabledKnob = style.disabledKnob;

        newStyle.knobBefore = style.knobBefore;
        newStyle.disabledKnobBefore = style.disabledKnobBefore;

        newStyle.knobAfter = style.knobAfter;
        newStyle.disabledKnobAfter = style.disabledKnobAfter;

        return newStyle;
    }

    private void CopyMeta(Drawable in, Drawable out)
    {
        out.setLeftWidth(in.getLeftWidth());
        out.setRightWidth(in.getRightWidth());
        out.setTopHeight(in.getTopHeight());
        out.setBottomHeight(in.getBottomHeight());

        out.setMinWidth(in.getMinWidth());
        out.setMinHeight(in.getMinHeight());
    }

    private Color GetOffColor()
    {
        return new Color(settings.getInt("displayroff") / 255f, settings.getInt("displaygoff") / 255f, settings.getInt("displayboff") / 255f);
    }

    private Color GetColor()
    {
        return new Color(settings.getInt("displayr") / 255f, settings.getInt("displayg") / 255f, settings.getInt("displayb") / 255f);
    }

    public void run()
    {
        EsoVars.drawNodesAsManhattan = settings.getBool("manhattannode");
        EsoVars.drawSignalRegions = settings.getBool("drawsignals");
        EsoVars.darkMode = settings.getBool("darkmode");
    }

    private class LabelSetting extends SettingsMenuDialog.SettingsTable.Setting
    {
        private final Label label;

        public LabelSetting(String name)
        {
            super(name);

            label = new Label("", Styles.outlineLabel);
        }

        @Override
        public void add(SettingsMenuDialog.SettingsTable table)
        {
            label.setText(bundle.getOrNull("setting." + name + ".name"));
            table.add(label).center().padBottom(10f);
            table.row();
        }
    }

    private class ColorSliderSetting extends SettingsMenuDialog.SettingsTable.Setting
    {
        private final Slider slider;
        private final Cons<Integer> changed;
        private final Prov<Color> getColor;
        private final boolean last; // This is VERY ugly

        public ColorSliderSetting(float min, float max, float stepSize, String name, boolean r, boolean g, boolean b, boolean last, Cons<Integer> changed, Prov<Color> getColor)
        {
            super(name);

            this.last = last;
            this.changed = changed;
            this.getColor = getColor;

            slider = new Slider(min, max, stepSize, false);

            Slider.SliderStyle og = slider.getStyle();
            Slider.SliderStyle copy = CopyStyle(og);
            copy.background = new BaseDrawable()
            {
                @Override
                public void draw(float x, float y, float width, float height)
                {
                    Color baseColor = getColor.get();

                    float sliceSize = width / 255f;

                    for (int i = 0; i < 255; i++)
                    {
                        float _i = i / 255f;
                        Color color = new Color(r ? _i : baseColor.r, g ? _i : baseColor.g, b ? _i : baseColor.b);

                        Draw.color(color);
                        Draw.rect("white", x + i * sliceSize + 1, y + height / 2f, sliceSize, height);
                    }
                }
            };

            CopyMeta(og.background, copy.background);

            copy.knob = new BaseDrawable()
            {
                @Override
                public void draw(float x, float y, float width, float height)
                {
                    Draw.color(Pal.gray);
                    Draw.rect("white", x + width / 2f, y + height / 2f, width, height);

                    Draw.color(getColor.get());
                    Draw.rect("white", x + (width - 10f) / 2f + 5f, y + (height - 10f) / 2f + 5f, width - 10f, height - 10f);
                }
            };

            CopyMeta(og.knob, copy.knob);

            copy.knobOver = copy.knob;
            copy.knobDown = copy.knob;

            slider.setStyle(copy);
        }

        @Override
        public void add(SettingsMenuDialog.SettingsTable table)
        {
            int colorSetting = settings.getInt(name);
            slider.setValue(colorSetting);

            Label value = new Label("", Styles.outlineLabel);
            Table content = new Table();

            content.add(title, Styles.outlineLabel).left().growX().wrap();
            content.add(value).padLeft(10f).right();
            content.margin(3f, 33f, 3f, 33f);

            content.touchable = Touchable.disabled;

            slider.changed(() -> {
                int _value = (int) slider.getValue();
                settings.put(name, _value);
                value.setText(_value == 69 ? "nice" : String.valueOf(_value));

                changed.get(_value);
            });

            slider.change();

            ui.addDescTooltip(table.stack(slider, content).width(Math.min(Core.graphics.getWidth() / 1.2f, 460f)).center().padTop(4f).padBottom(last ? 20f : 0f).get(), bundle.getOrNull("setting." + name + ".description"));
            table.row();
        }
    }
}
