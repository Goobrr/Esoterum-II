package esoterum.world.blocks.signal;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.Rect;
import arc.scene.ui.layout.Table;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.*;

public class SignalMatrix extends SignalBlock
{
    public TextureRegion placeRegion, pinoutRegion;

    public SignalMatrix(String name)
    {
        super(name);
        size = 8;
        rotate = true;
    }

    @Override
    public void load()
    {
        super.load();

        placeRegion = Core.atlas.find("eso-display-matrix-place", "eso-none");
        pinoutRegion = Core.atlas.find("eso-display-matrix-pinout", "eso-none");
    }

    @Override
    public TextureRegion getPlanRegion(BuildPlan plan, Eachable<BuildPlan> list)
    {
        return placeRegion;
    }

    public class SignalMatrixBuild extends SignalBuild
    {
        public Pixmap img = new Pixmap(256, 256);
        public Texture tex = new Texture(256, 256);
        public TextureRegion txr = new TextureRegion(tex);

        public PaintOrder currentOrder;
        public ConcurrentLinkedQueue<PaintOrder> queuedOrders = new ConcurrentLinkedQueue<>();

        public int[] colorMap = new int[256 * 256];
        public AtomicReference<int[]> colorMapRef = new AtomicReference<>(colorMap);
        public AtomicBoolean cleared = new AtomicBoolean(true);

        public int calculateColor(int colorA, int colorB)
        {
            int value = colorA << 1 | colorB;
            return value | value << 2 | value << 4 | value << 6;
        }

        @Override
        public void updateSignal(boolean update)
        {
            super.updateSignal(update);

            int x = (signal[0] |
                    (signal[1] << 1) |
                    (signal[2] << 2) |
                    (signal[3] << 3) |
                    (signal[4] << 4) |
                    (signal[5] << 5) |
                    (signal[6] << 6) |
                    (signal[7] << 7));
            int y = (signal[8] |
                    (signal[9] << 1) |
                    (signal[10] << 2) |
                    (signal[11] << 3) |
                    (signal[12] << 4) |
                    (signal[13] << 5) |
                    (signal[14] << 6) |
                    (signal[15] << 7));

            if (signal[23] > 0)
            {
                if (!cleared.get())
                {
                    queuedOrders.add(new ClearOrder());
                    colorMapRef.set(new int[256 * 256]);
                    cleared.set(true);
                }

            }
            else if (signal[22] > 0)
            {
                int color =
                        (calculateColor(signal[16], signal[17]) << 24) |
                                (calculateColor(signal[18], signal[19]) << 16) |
                                (calculateColor(signal[20], signal[21]) << 8) | 0xFF;

                int[] colorMap = colorMapRef.get();
                if (colorMap[x + y * 256] != color)
                {
                    queuedOrders.add(new PaintOrder(x, y, color));
                    colorMap[x + y * 256] = color;
                    colorMapRef.set(colorMap);
                    cleared.set(false);
                }
            }
        }

        @Override
        public void draw()
        {
            boolean update = false;
            while ((currentOrder = queuedOrders.poll()) != null)
            {
                update = true;
                if (currentOrder instanceof ClearOrder) img.fill(0xFF);
                else if (img.getRaw(currentOrder.x, currentOrder.y) != currentOrder.color)
                    img.setRaw(currentOrder.x, currentOrder.y, currentOrder.color);
            }

            if (update) tex.draw(img);

            Draw.z(30.05f);
            Draw.rect(txr, this.x, this.y, rotation * 90);
        }

        @Override
        public void drawSelect()
        {
            super.drawSelect();

            Draw.rect(pinoutRegion, x, y, rotation * 90);
        }

        @Override
        public void drawSignalRegions(Rect camera)
        {
        }

        @Override
        public void drawShieldRegions()
        {
        }

        @Override
        public void created()
        {
            super.created();
            img.fill(0xFF);
            tex.draw(img);
        }

        @Override
        public void onRemoved()
        {
            super.onRemoved();
            tex.dispose();
            img.dispose();
        }

        @Override
        public void buildConfiguration(Table table)
        {
            // disable shielding for memory blocks
        }

        public class PaintOrder
        {
            public int x, y, color;

            private PaintOrder()
            {

            }

            public PaintOrder(int x, int y, int color)
            {
                this.x = x;
                this.y = y;
                this.color = color;
            }
        }

        public class ClearOrder extends PaintOrder
        {
        }
    }
}
