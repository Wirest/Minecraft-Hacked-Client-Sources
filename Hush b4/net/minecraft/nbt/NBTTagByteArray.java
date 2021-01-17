// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import java.util.Arrays;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

public class NBTTagByteArray extends NBTBase
{
    private byte[] data;
    
    NBTTagByteArray() {
    }
    
    public NBTTagByteArray(final byte[] data) {
        this.data = data;
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        output.writeInt(this.data.length);
        output.write(this.data);
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(192L);
        final int i = input.readInt();
        sizeTracker.read(8 * i);
        input.readFully(this.data = new byte[i]);
    }
    
    @Override
    public byte getId() {
        return 7;
    }
    
    @Override
    public String toString() {
        return "[" + this.data.length + " bytes]";
    }
    
    @Override
    public NBTBase copy() {
        final byte[] abyte = new byte[this.data.length];
        System.arraycopy(this.data, 0, abyte, 0, this.data.length);
        return new NBTTagByteArray(abyte);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return super.equals(p_equals_1_) && Arrays.equals(this.data, ((NBTTagByteArray)p_equals_1_).data);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.data);
    }
    
    public byte[] getByteArray() {
        return this.data;
    }
}
