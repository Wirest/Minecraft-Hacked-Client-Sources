// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk.storage;

public class NibbleArrayReader
{
    public final byte[] data;
    private final int depthBits;
    private final int depthBitsPlusFour;
    
    public NibbleArrayReader(final byte[] dataIn, final int depthBitsIn) {
        this.data = dataIn;
        this.depthBits = depthBitsIn;
        this.depthBitsPlusFour = depthBitsIn + 4;
    }
    
    public int get(final int p_76686_1_, final int p_76686_2_, final int p_76686_3_) {
        final int i = p_76686_1_ << this.depthBitsPlusFour | p_76686_3_ << this.depthBits | p_76686_2_;
        final int j = i >> 1;
        final int k = i & 0x1;
        return (k == 0) ? (this.data[j] & 0xF) : (this.data[j] >> 4 & 0xF);
    }
}
