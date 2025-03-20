package esoterum.world.blocks.signal;

import arc.Core;
import arc.graphics.g2d.*;
import arc.math.geom.Rect;

public class SignalJunction extends SignalBlock
{

    public TextureRegion signalRegion1, signalRegion2;

    public SignalJunction(String name)
    {
        super(name);

        hasGraph = false;
        rotate = true;

        dark = true;
    }

    @Override
    public void load()
    {
        super.load();

        signalRegion1 = Core.atlas.find(name + "-signal-1");
        signalRegion2 = Core.atlas.find(name + "-signal-2");
    }

    public class SignalJunctionBuild extends SignalBuild
    {
        @Override
        public void drawSignalRegions(Rect camera)
        {
            super.drawSignalRegions(camera);

            Draw.color(getWireColor(rotateDraw ? 0 : rotation % 2));
            Draw.rect(signalRegion1, x, y, rotateDraw ? rotdeg() : 0);

            Draw.color(getWireColor(rotateDraw ? 1 : 1 - rotation % 2));
            Draw.rect(signalRegion2, x, y, rotateDraw ? rotdeg() : 0);
        }
    }
}
