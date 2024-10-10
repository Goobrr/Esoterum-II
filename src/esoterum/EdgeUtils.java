package esoterum;

import arc.math.geom.*;
import arc.util.Log;

public class EdgeUtils {
    // Yeah, I'm hardcoding this shit
    public static Vec2[][][] edges = {
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

    public static int[][][] indices = {{
        {-1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1,  2, -1, -1, -1},
        {-1, -1,  3, -1,  1, -1, -1},
        {-1, -1, -1,  0, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1}
    }, {
        {-1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1,  5, -1,  4, -1},
        {-1, -1,  6, -1, -1, -1,  3},
        {-1, -1, -1, -1, -1, -1, -1},
        {-1, -1,  7, -1, -1, -1,  2},
        {-1, -1, -1,  0, -1,  1, -1}
    }, {
        {-1,  8, -1,  7, -1,  6, -1},
        { 9, -1, -1, -1, -1, -1,  5},
        {-1, -1, -1, -1, -1, -1, -1},
        {10, -1, -1, -1, -1, -1,  4},
        {-1, -1, -1, -1, -1, -1, -1},
        {11, -1, -1, -1, -1, -1,  3},
        {-1,  0, -1,  1, -1,  2, -1}
    }};

    public static Vec2 getEdgeOffset(int size, int index, int rotation){
        Vec2[][] e = edges[size - 1];

        return e[((int)Math.floor((float)index / (float)size) + rotation) % 4][index % size];
    }

    public static int getOffsetIndex(int size, float x, float y, int rotation){
        int e = indices[size - 1][(int)(x*2+3)][(int)(y*2+3)];
        //Log.info("raw: "+e);
        return (e - rotation*size + size*4) % (size * 4);
    }
}
