package esoterum;

import arc.math.geom.Vec2;

public class EdgeUtils
{
    // Yeah, I'm hardcoding this shit
    public static Vec2[][][] edges = {
            {
                    {new Vec2(0.5f, 0f)},
                    {new Vec2(0f, 0.5f)},
                    {new Vec2(-0.5f, 0f)},
                    {new Vec2(0f, -0.5f)}
            }, {
            {new Vec2(1f, -0.5f), new Vec2(1f, 0.5f)},
            {new Vec2(0.5f, 1f), new Vec2(-0.5f, 1f)},
            {new Vec2(-1f, 0.5f), new Vec2(-1f, -0.5f)},
            {new Vec2(-0.5f, -1f), new Vec2(0.5f, -1f)}
    }, {
            {new Vec2(1.5f, -1f), new Vec2(1.5f, 0f), new Vec2(1.5f, 1f)},
            {new Vec2(1f, 1.5f), new Vec2(0f, 1.5f), new Vec2(-1f, 1.5f)},
            {new Vec2(-1.5f, 1f), new Vec2(-1.5f, 0f), new Vec2(-1.5f, -1f)},
            {new Vec2(-1f, -1.5f), new Vec2(0f, -1.5f), new Vec2(1f, -1.5f)}
    }, {
            {new Vec2(2f, -1.5f), new Vec2(2f, -0.5f), new Vec2(2f, 0.5f), new Vec2(2f, 1.5f)},
            {new Vec2(1.5f, 2f), new Vec2(0.5f, 2f), new Vec2(-0.5f, 2f), new Vec2(-1.5f, 2f)},
            {new Vec2(-2f, 1.5f), new Vec2(-2f, 0.5f), new Vec2(-2f, -0.5f), new Vec2(-2f, -1.5f)},
            {new Vec2(-1.5f, -2f), new Vec2(-0.5f, -2f), new Vec2(0.5f, -2f), new Vec2(1.5f, -2f)}
    }, {
            {new Vec2(2.5f, -2f), new Vec2(2.5f, -1f), new Vec2(2.5f, 0f), new Vec2(2.5f, 1f), new Vec2(2.5f, 2f)},
            {new Vec2(2f, 2.5f), new Vec2(1f, 2.5f), new Vec2(0f, 2.5f), new Vec2(-1f, 2.5f), new Vec2(-2f, 2.5f)},
            {new Vec2(-2.5f, 2f), new Vec2(-2.5f, 1f), new Vec2(-2.5f, 0f), new Vec2(-2.5f, -1f), new Vec2(-2.5f, -2f)},
            {new Vec2(-2f, -2.5f), new Vec2(-1f, -2.5f), new Vec2(0f, -2.5f), new Vec2(1f, -2.5f), new Vec2(2f, -2.5f)}
    }, {
            {new Vec2(3f, -2.5f), new Vec2(3f, -1.5f), new Vec2(3f, -0.5f), new Vec2(3f, 0.5f), new Vec2(3f, 1.5f), new Vec2(3f, 2.5f)},
            {new Vec2(2.5f, 3f), new Vec2(1.5f, 3f), new Vec2(0.5f, 3f), new Vec2(-0.5f, 3f), new Vec2(-1.5f, 3f), new Vec2(-2.5f, 3f)},
            {new Vec2(-3f, 2.5f), new Vec2(-3f, 1.5f), new Vec2(-3f, 0.5f), new Vec2(-3f, -0.5f), new Vec2(-3f, -1.5f), new Vec2(-3f, -2.5f)},
            {new Vec2(-2.5f, -3f), new Vec2(-1.5f, -3f), new Vec2(-0.5f, -3f), new Vec2(0.5f, -3f), new Vec2(1.5f, -3f), new Vec2(2.5f, -3f)}
    }, {
            {new Vec2(3.5f, -3f), new Vec2(3.5f, -2f), new Vec2(3.5f, -1f), new Vec2(3.5f, 0f), new Vec2(3.5f, 1f), new Vec2(3.5f, 2f), new Vec2(3.5f, 3f)},
            {new Vec2(3f, 3.5f), new Vec2(2f, 3.5f), new Vec2(1f, 3.5f), new Vec2(0f, 3.5f), new Vec2(-1f, 3.5f), new Vec2(-2f, 3.5f), new Vec2(-3f, 3.5f)},
            {new Vec2(-3.5f, 3f), new Vec2(-3.5f, 2f), new Vec2(-3.5f, 1f), new Vec2(-3.5f, 0f), new Vec2(-3.5f, -1f), new Vec2(-3.5f, -2f), new Vec2(-3.5f, -3f)},
            {new Vec2(-3f, -3.5f), new Vec2(-2f, -3.5f), new Vec2(-1f, -3.5f), new Vec2(0f, -3.5f), new Vec2(1f, -3.5f), new Vec2(2f, -3.5f), new Vec2(3f, -3.5f)}
    }, {
            {new Vec2(4f, -3.5f), new Vec2(4f, -2.5f), new Vec2(4f, -1.5f), new Vec2(4f, -0.5f), new Vec2(4f, 0.5f), new Vec2(4f, 1.5f), new Vec2(4f, 2.5f), new Vec2(4f, 3.5f)},
            {new Vec2(3.5f, 4f), new Vec2(2.5f, 4f), new Vec2(1.5f, 4f), new Vec2(0.5f, 4f), new Vec2(-0.5f, 4f), new Vec2(-1.5f, 4f), new Vec2(-2.5f, 4f), new Vec2(-3.5f, 4f)},
            {new Vec2(-4f, 3.5f), new Vec2(-4f, 2.5f), new Vec2(-4f, 1.5f), new Vec2(-4f, 0.5f), new Vec2(-4f, -0.5f), new Vec2(-4f, -1.5f), new Vec2(-4f, -2.5f), new Vec2(-4f, -3.5f)},
            {new Vec2(-3.5f, -4f), new Vec2(-2.5f, -4f), new Vec2(-1.5f, -4f), new Vec2(-0.5f, -4f), new Vec2(0.5f, -4f), new Vec2(1.5f, -4f), new Vec2(2.5f, -4f), new Vec2(3.5f, -4f)}
    }
    };

    public static int[][] indices = {
            {-1, 23, -1, 22, -1, 21, -1, 20, -1, 19, -1, 18, -1, 17, -1, 16, -1},
            {24, -1, 20, -1, 19, -1, 18, -1, 17, -1, 16, -1, 15, -1, 14, -1, 15},
            {-1, 21, -1, 17, -1, 16, -1, 15, -1, 14, -1, 13, -1, 12, -1, 13, -1},
            {25, -1, 18, -1, 14, -1, 13, -1, 12, -1, 11, -1, 10, -1, 11, -1, 14},
            {-1, 22, -1, 15, -1, 11, -1, 10, -1, 9, -1, 8, -1, 9, -1, 12, -1},
            {26, -1, 19, -1, 12, -1, 8, -1, 7, -1, 6, -1, 7, -1, 10, -1, 13},
            {-1, 23, -1, 16, -1, 9, -1, 5, -1, 4, -1, 5, -1, 8, -1, 11, -1},
            {27, -1, 20, -1, 13, -1, 6, -1, 2, -1, 3, -1, 6, -1, 9, -1, 12},
            {-1, 24, -1, 17, -1, 10, -1, 3, -1, 1, -1, 4, -1, 7, -1, 10, -1},
            {28, -1, 21, -1, 14, -1, 7, -1, 0, -1, 2, -1, 5, -1, 8, -1, 11},
            {-1, 25, -1, 18, -1, 11, -1, 0, -1, 1, -1, 3, -1, 6, -1, 9, -1},
            {29, -1, 22, -1, 15, -1, 0, -1, 1, -1, 2, -1, 4, -1, 7, -1, 10},
            {-1, 26, -1, 19, -1, 0, -1, 1, -1, 2, -1, 3, -1, 5, -1, 8, -1},
            {30, -1, 23, -1, 0, -1, 1, -1, 2, -1, 3, -1, 4, -1, 6, -1, 9},
            {-1, 27, -1, 0, -1, 1, -1, 2, -1, 3, -1, 4, -1, 5, -1, 7, -1},
            {31, -1, 0, -1, 1, -1, 2, -1, 3, -1, 4, -1, 5, -1, 6, -1, 8},
            {-1, 0, -1, 1, -1, 2, -1, 3, -1, 4, -1, 5, -1, 6, -1, 7, -1}
    };

    public static Vec2 getEdgeOffset(int size, int index, int rotation)
    {
        Vec2[][] e = edges[size - 1];

        return e[((int) Math.floor((float) index / (float) size) + rotation) % 4][index % size];
    }

    public static int getOffsetIndex(int size, float x, float y, int rotation)
    {
        int e = indices[(int) (x * 2 + 8)][(int) (y * 2 + 8)];
        return (e - rotation * size + size * 4) % (size * 4);
    }
}
