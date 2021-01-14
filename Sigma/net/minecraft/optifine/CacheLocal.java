package net.minecraft.optifine;

public class CacheLocal {
    private int maxX = 18;
    private int maxY = 128;
    private int maxZ = 18;
    private int offsetX = 0;
    private int offsetY = 0;
    private int offsetZ = 0;
    private int[][][] cache = null;
    private int[] lastZs = null;
    private int lastDz = 0;

    public CacheLocal(int maxX, int maxY, int maxZ) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        cache = new int[maxX][maxY][maxZ];
        resetCache();
    }

    public void resetCache() {
        for (int x = 0; x < maxX; ++x) {
            int[][] ys = cache[x];

            for (int y = 0; y < maxY; ++y) {
                int[] zs = ys[y];

                for (int z = 0; z < maxZ; ++z) {
                    zs[z] = -1;
                }
            }
        }
    }

    public void setOffset(int x, int y, int z) {
        offsetX = x;
        offsetY = y;
        offsetZ = z;
        resetCache();
    }

    public int get(int x, int y, int z) {
        try {
            lastZs = cache[x - offsetX][y - offsetY];
            lastDz = z - offsetZ;
            return lastZs[lastDz];
        } catch (ArrayIndexOutOfBoundsException var5) {
            var5.printStackTrace();
            return -1;
        }
    }

    public void setLast(int val) {
        try {
            lastZs[lastDz] = val;
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }
}
