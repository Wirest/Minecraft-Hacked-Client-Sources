// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

public class NBTTagShort extends NBTPrimitive
{
    private short data;
    
    public NBTTagShort() {
    }
    
    public NBTTagShort(final short data) {
        this.data = data;
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        output.writeShort(this.data);
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(80L);
        this.data = input.readShort();
    }
    
    @Override
    public byte getId() {
        return 2;
    }
    
    @Override
    public String toString() {
        return this.data + "s";
    }
    
    @Override
    public NBTBase copy() {
        return new NBTTagShort(this.data);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            final NBTTagShort nbttagshort = (NBTTagShort)p_equals_1_;
            return this.data == nbttagshort.data;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ this.data;
    }
    
    @Override
    public long getLong() {
        return this.data;
    }
    
    @Override
    public int getInt() {
        return this.data;
    }
    
    @Override
    public short getShort() {
        return this.data;
    }
    
    @Override
    public byte getByte() {
        return (byte)(this.data & 0xFF);
    }
    
    @Override
    public double getDouble() {
        return this.data;
    }
    
    @Override
    public float getFloat() {
        return this.data;
    }
}
