package esoterum.world.blocks.signal;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import esoterum.EsoVars;

public class SignalDisplay extends SignalBlock
{
    public SignalDisplay(String name)
    {
        super(name);
    }

    @Override
    public Color getWireOffColor()
    {
        return EsoVars.displayOffColor;
    }

    @Override
    public Color getWireColor()
    {
        return EsoVars.displayColor;
    }

    public class SignalDisplayBuild extends SignalBuild
    {
        @Override
        public void draw()
        {
            //drawSignalRegions();
            Draw.color(signal[0] == 1 ? getWireColor() : getWireOffColor());
            Draw.rect(signalRegion, x, y);
        }
    }
}
