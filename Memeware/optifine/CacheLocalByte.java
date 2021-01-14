package optifine;

public class CacheLocalByte {
    private int maxX = 18;
    private int maxY = 128;
    private int maxZ = 18;
    private int offsetX = 0;
    private int offsetY = 0;
    private int offsetZ = 0;
    private byte[][][] cache = (byte[][][]) null;
    private byte[] lastZs = null;
    private int lastDz = 0;

    public CacheLocalByte(int maxX, int maxY, int maxZ) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.cache = new byte[maxX][maxY][maxZ];
        this.resetCache();
    }

    public void resetCache() {
        for (int x = 0; x < this.maxX; ++x) {
            byte[][] ys = this.cache[x];

            for (int y = 0; y < this.maxY; ++y) {
                byte[] zs = ys[y];

                for (int z = 0; z < this.maxZ; ++z) {
                    zs[z] = -1;
                }
            }
        }
    }

    public void setOffset(int x, int y, int z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        this.resetCache();
    }

    public byte get(int x, int y, int z) {
        try {
            this.lastZs = this.cache[x - this.offsetX][y - this.offsetY];
            this.lastDz = z - this.offsetZ;
            return this.lastZs[this.lastDz];
        } catch (ArrayIndexOutOfBoundsException var5) {
            var5.printStackTrace();
            return (byte) -1;
        }
    }

    public void setLast(byte val) {
        try {
            this.lastZs[this.lastDz] = val;
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }
}
