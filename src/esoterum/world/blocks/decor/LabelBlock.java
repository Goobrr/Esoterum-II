package esoterum.world.blocks.decor;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import arc.util.pooling.*;
import esoterum.ui.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.world.*;

public class LabelBlock extends Block {
    private GlyphLayout layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
    public int maxNewlines = 15;
    public int drawDistance = 5;

    public LabelBlock(String name) {
        super(name);

        configurable = true;
        logicConfigurable = false;
        solid = false;
        update = true;

        category = Category.effect;
        clipSize = 500;

        config(String.class, (LabelBuild tile, String s) -> {
            tile.labelText = s;
        });

        config(int[].class, (LabelBuild tile, int[] i ) -> {
            tile.alignH = i[0];
            tile.alignV = i[1];
        });

        config(String[].class, (LabelBuild tile, String[] s) -> {
            tile.configure(new int[]{Integer.valueOf(s[0]), Integer.valueOf(s[1])});
            tile.configure(s[2]);
        });

        hasShadow = false;
    }

    public class LabelBuild extends Building {
        public String labelText = "Label";
        public int alignV = Align.top;
        public int alignH = Align.left;

        public float blockAlpha = 1;

        @Override
        public void buildConfiguration(Table table) {
            table.table(t -> {
                t.button(Icon.pencilSmall, () -> {
                    BaseDialog dialog = new BaseDialog("Edit Label");
                    dialog.setFillParent(false);
                    TextArea a = dialog.cont.add(new TextArea(labelText.replace("\r", "\n"))).size(380f, 160f).get();
                    a.setFilter((textField, c) -> {
                        if(c == '\n'){
                            int count = 0;
                            for(int i = 0; i < textField.getText().length(); i++){
                                if(textField.getText().charAt(i) == '\n'){
                                    count++;
                                }
                            }
                            return count < maxNewlines;
                        }
                        return true;
                    });
                    a.setMaxLength(250);
                    dialog.cont.row();
                    dialog.cont.label(() -> a.getText().length() + " / " + 250).color(Color.lightGray);
                    dialog.buttons.button("@ok", () -> {
                        if(!a.getText().equals(labelText)) configure(a.getText());
                        dialog.hide();
                    }).size(130f, 60f);
                    dialog.update(() -> {
                        if(tile.build != this){
                            dialog.hide();
                        }
                    });
                    dialog.closeOnBack();
                    dialog.show();
                }).size(40);

                t.button(b -> {
                    b.center();
                    b.image(() -> switch(alignH) {
                        case Align.left -> Core.atlas.find("eso-align-left");
                        case Align.right -> Core.atlas.find("eso-align-right");
                        default -> Core.atlas.find("eso-align-hcenter");
                    }).grow();
                }, () -> {
                    alignH = EsoUI.hAligns.get((EsoUI.hAligns.indexOf(alignH) + 1) % 3);
                    configure(new int[]{alignH, alignV});
                }).size(40);

                t.button(b -> {
                    b.center();
                    b.image(() -> switch(alignV) {
                        case Align.top -> Core.atlas.find("eso-align-top");
                        case Align.bottom -> Core.atlas.find("eso-align-bottom");
                        default -> Core.atlas.find("eso-align-vcenter");
                    }).grow();
                }, () -> {
                    alignV = EsoUI.vAligns.get((EsoUI.vAligns.indexOf(alignV) + 1) % 3);
                    configure(new int[]{alignH, alignV});
                }).size(40);
            });
        }

        @Override
        public void updateTableAlign(Table table) {
            Vec2 pos = Core.input.mouseScreen(x, y);
            table.setPosition(pos.x, pos.y, Align.center);
        }

        @Override
        public void draw() {
            // Block is drawn when player cursor is in proximity
            Vec2 mouse = Core.input.mouseWorld();
            float dist = Mathf.dst2(x, y, mouse.x, mouse.y);

            if(dist < (drawDistance * Vars.tilesize * drawDistance * Vars.tilesize) ){
                blockAlpha = Mathf.lerp(blockAlpha, 1, 0.2f / Time.delta);
            }else{
                blockAlpha = Mathf.lerp(blockAlpha, 0, 0.2f / Time.delta);
            }

            if(blockAlpha > 0.05) {
                Draw.alpha(blockAlpha);
                Draw.rect(region, x, y);
                Draw.alpha(1);
            }

            // Text draw
            Font font = Fonts.outline;
            float scale = 0.5f;
            String text = EsoUI.formatIcons(labelText);

            boolean ints = font.usesIntegerPositions();
            font.setUseIntegerPositions(false);
            font.getData().setScale(scale);
            layout.setText(font, text);

            font.setColor(Color.white);
            font.draw(text,
                    x + (((alignH & Align.left) != 0 ? block.size * Vars.tilesize / 2.0f : ((alignH & Align.right) != 0) ? block.size * Vars.tilesize * -0.5f : 0)),
                    y + (((alignV & Align.center) != 0 ? layout.height / 2f : (alignV & Align.bottom) != 0 ? layout.height : 0)),
            alignH);

            font.setUseIntegerPositions(ints);
            font.setColor(Color.white);
            font.getData().setScale(1f);
            Draw.reset();
            Pools.free(layout);
        }

        @Override
        public String[] config() {
            return new String[]{String.valueOf(alignH), String.valueOf(alignV), labelText};
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.str(labelText);
            write.i(alignH);
            write.i(alignV);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            labelText = read.str();
            alignH = read.i();
            alignV = read.i();
        }
    }
}
