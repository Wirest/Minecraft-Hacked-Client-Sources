// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

public class NBTTagLong extends NBTPrimitive
{
    private long data;
    
    NBTTagLong() {
    }
    
    public NBTTagLong(final long data) {
        this.data = data;
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        output.writeLong(this.data);
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(128L);
        this.data = input.readLong();
    }
    
    @Override
    public byte getId() {
        return 4;
    }
    
    @Override
    public String toString() {
        return this.data + "L";
    }
    
    @Override
    public NBTBase copy() {
        return new NBTTagLong(this.data);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            final NBTTagLong nbttaglong = (NBTTagLong)p_equals_1_;
            return this.data == nbttaglong.data;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ (int)(this.data ^ this.data >>> 32);
    }
    
    @Override
    public long getLong() {
        return this.data;
    }
    
    @Override
    public int getInt() {
        return (int)(this.data & -1L);
    }
    
    @Override
    public short getShort() {
        return (short)(this.data & 0xFFFFL);
    }
    
    @Override
    public byte getByte() {
        return (byte)(this.data & 0xFFL);
    }
    
    @Override
    public double getDouble() {
        return (double)this.data;
    }
    
    @Override
    public float getFloat() {
        return (float)this.data;
    }
}
