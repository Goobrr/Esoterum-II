package esoterum;

import arc.math.geom.*;

public class EdgeUtils {
    // Yeah, I'm hardcoding this shit
    public static final Vec2[][][] edges = {
        {
            {new Vec2(0.5f, 0f)},
            {new Vec2(0f, 0.5f)},
            {new Vec2(-0.5f, 0f)},
            {new Vec2(0f, -0.5f)}
        },
        {
            {new Vec2(1.5f, 0f), new Vec2(1.5f, 1f)},
            {new Vec2(1f, 1.5f), new Vec2(0f, 1.5f)},
            {new Vec2(-0.5f, 1f), new Vec2(-0.5f, 0f)},
            {new Vec2(0f, -0.5f), new Vec2(1f, -0.5f)}
        },
        {
            {new Vec2(1.5f, -1f), new Vec2(1.5f, 0f), new Vec2(1.5f, 1f)},
            {new Vec2(1f, 1.5f), new Vec2(0f, 1.5f), new Vec2(-1f, 1.5f)},
            {new Vec2(-1.5f, 1f), new Vec2(-1.5f, 0f), new Vec2(-1.5f, -1f)},
            {new Vec2(-1f, -1.5f), new Vec2(0f, -1.5f), new Vec2(1f, -1.5f)}
        },
    };

    public static Vec2 getEdgeOffset(int size, int index, int rotation){
        Vec2[][] e = edges[size - 1];

        int i = index % size;
        int f = ((int)Math.floor((float)index / (float)size) + rotation) % 4;

        return e[f][i];
    }
}
