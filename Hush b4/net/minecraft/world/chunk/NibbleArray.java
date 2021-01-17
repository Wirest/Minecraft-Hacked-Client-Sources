// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk;

public class NibbleArray
{
    private final byte[] data;
    
    public NibbleArray() {
        this.data = new byte[2048];
    }
    
    public NibbleArray(final byte[] storageArray) {
        this.data = storageArray;
        if (storageArray.length != 2048) {
            throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + storageArray.length);
        }
    }
    
    public int get(final int x, final int y, final int z) {
        return this.getFromIndex(this.getCoordinateIndex(x, y, z));
    }
    
    public void set(final int x, final int y, final int z, final int value) {
        this.setIndex(this.getCoordinateIndex(x, y, z), value);
    }
    
    private int getCoordinateIndex(final int x, final int y, final int z) {
        return y << 8 | z << 4 | x;
    }
    
    public int getFromIndex(final int index) {
        final int i = this.getNibbleIndex(index);
        return this.isLowerNibble(index) ? (this.data[i] & 0xF) : (this.data[i] >> 4 & 0xF);
    }
    
    public void setIndex(final int index, final int value) {
        final int i = this.getNibbleIndex(index);
        if (this.isLowerNibble(index)) {
            this.data[i] = (byte)((this.data[i] & 0xF0) | (value & 0xF));
        }
        else {
            this.data[i] = (byte)((this.data[i] & 0xF) | (value & 0xF) << 4);
        }
    }
    
    private boolean isLowerNibble(final int index) {
        return (index & 0x1) == 0x0;
    }
    
    private int getNibbleIndex(final int index) {
        return index >> 1;
    }
    
    public byte[] getData() {
        return this.data;
    }
}
