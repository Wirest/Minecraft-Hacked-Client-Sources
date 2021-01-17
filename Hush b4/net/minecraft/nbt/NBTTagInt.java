// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

public class NBTTagInt extends NBTPrimitive
{
    private int data;
    
    NBTTagInt() {
    }
    
    public NBTTagInt(final int data) {
        this.data = data;
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        output.writeInt(this.data);
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(96L);
        this.data = input.readInt();
    }
    
    @Override
    public byte getId() {
        return 3;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append(this.data).toString();
    }
    
    @Override
    public NBTBase copy() {
        return new NBTTagInt(this.data);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            final NBTTagInt nbttagint = (NBTTagInt)p_equals_1_;
            return this.data == nbttagint.data;
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
        return (short)(this.data & 0xFFFF);
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
        return (float)this.data;
    }
}
