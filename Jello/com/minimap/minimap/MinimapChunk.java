package com.minimap.minimap;

import java.util.*;
import com.minimap.*;

public class MinimapChunk
{
    public static Random rand;
    public int[][] colors;
    public boolean chunkGrid;
    public boolean slimeChunk;
    public int[] lastHeights;
    
    public MinimapChunk(final int X, final int Z) {
        this.colors = new int[16][16];
        this.lastHeights = new int[16];
        this.chunkGrid = ((X & 0x1) == (Z & 0x1));
        this.slimeChunk = isSlimeChunk(X, Z);
    }
    
    public static boolean isSlimeChunk(final int xPosition, final int zPosition) {
        try {
            final long seed = XaeroMinimap.getSettings().getSlimeChunksSeed();
            if (seed == 0L) {
                return false;
            }
            final Random rnd = new Random(seed + xPosition * xPosition * 4987142 + xPosition * 5947611 + zPosition * zPosition * 4392871L + zPosition * 389711 ^ 0x3AD8025FL);
            return rnd.nextInt(10) == 0;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    static {
        MinimapChunk.rand = new Random();
    }
}
