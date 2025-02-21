package esoterum.world.blocks.signal;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.Rect;
import arc.scene.ui.layout.Table;
import esoterum.graph.SignalGraph;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SignalMatrix extends SignalBlock
{
    public SignalMatrix(String name)
    {
        super(name);
        size = 8;
        rotate = true;
    }

    public class SignalMatrixBuild extends SignalBuild
    {
        public Pixmap img = new Pixmap(256, 256);
        public Texture tex = new Texture(256, 256);
        public TextureRegion txr = new TextureRegion(tex);

        private PaintOrder currentOrder;
        private ConcurrentLinkedQueue<PaintOrder> queuedOrders = new ConcurrentLinkedQueue<>();

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

            if (signal[22] > 0)
            {
                int color = ((signal[16] << 31) |
                        (signal[17] << 30) |
                        (signal[18] << 23) |
                        (signal[19] << 22) |
                        (signal[20] << 15) |
                        (signal[21] << 14) |
                        0xFF);
                queuedOrders.add(new PaintOrder(x, y, color));
            }
            else if (signal[23] > 0)
            {
                queuedOrders.add(new PaintOrder(x, y, 0xFF));
            }
        }

        @Override
        public void draw()
        {
            boolean update = false;
            while ((currentOrder = queuedOrders.poll()) != null)
            {
                update = true;
                if (img.getRaw(currentOrder.x, currentOrder.y) != currentOrder.color) img.setRaw(currentOrder.x, currentOrder.y, currentOrder.color);
            }

            if (update) tex.draw(img);

            Draw.z(30.05f);
            Draw.rect(txr, this.x, this.y, rotation * 90);
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

        private record PaintOrder(int x, int y, int color)
        {
        }
    }
}
