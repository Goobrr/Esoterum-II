package esoterum.world.blocks.signal;

import arc.graphics.Color;
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
}
