// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

public class CacheLocalByte
{
    private int maxX;
    private int maxY;
    private int maxZ;
    private int offsetX;
    private int offsetY;
    private int offsetZ;
    private byte[][][] cache;
    private byte[] lastZs;
    private int lastDz;
    
    public CacheLocalByte(final int p_i26_1_, final int p_i26_2_, final int p_i26_3_) {
        this.maxX = 18;
        this.maxY = 128;
        this.maxZ = 18;
        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
        this.cache = null;
        this.lastZs = null;
        this.lastDz = 0;
        this.maxX = p_i26_1_;
        this.maxY = p_i26_2_;
        this.maxZ = p_i26_3_;
        this.cache = new byte[p_i26_1_][p_i26_2_][p_i26_3_];
        this.resetCache();
    }
    
    public void resetCache() {
        for (int i = 0; i < this.maxX; ++i) {
            final byte[][] abyte = this.cache[i];
            for (int j = 0; j < this.maxY; ++j) {
                final byte[] abyte2 = abyte[j];
                for (int k = 0; k < this.maxZ; ++k) {
                    abyte2[k] = -1;
                }
            }
        }
    }
    
    public void setOffset(final int p_setOffset_1_, final int p_setOffset_2_, final int p_setOffset_3_) {
        this.offsetX = p_setOffset_1_;
        this.offsetY = p_setOffset_2_;
        this.offsetZ = p_setOffset_3_;
        this.resetCache();
    }
    
    public byte get(final int p_get_1_, final int p_get_2_, final int p_get_3_) {
        try {
            this.lastZs = this.cache[p_get_1_ - this.offsetX][p_get_2_ - this.offsetY];
            this.lastDz = p_get_3_ - this.offsetZ;
            return this.lastZs[this.lastDz];
        }
        catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
            arrayindexoutofboundsexception.printStackTrace();
            return -1;
        }
    }
    
    public void setLast(final byte p_setLast_1_) {
        try {
            this.lastZs[this.lastDz] = p_setLast_1_;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
