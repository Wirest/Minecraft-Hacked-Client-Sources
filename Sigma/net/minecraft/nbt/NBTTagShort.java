package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTBase.NBTPrimitive {
    /**
     * The short value for the tag.
     */
    private short data;
    private static final String __OBFID = "CL_00001227";

    public NBTTagShort() {
    }

    public NBTTagShort(short data) {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension
     * classes
     */
    @Override
    void write(DataOutput output) throws IOException {
        output.writeShort(data);
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(16L);
        data = input.readShort();
    }

    /**
     * Gets the type byte for the tag.
     */
    @Override
    public byte getId() {
        return (byte) 2;
    }

    @Override
    public String toString() {
        return "" + data + "s";
    }

    /**
     * Creates a clone of the tag.
     */
    @Override
    public NBTBase copy() {
        return new NBTTagShort(data);
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            NBTTagShort var2 = (NBTTagShort) p_equals_1_;
            return data == var2.data;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ data;
    }

    @Override
    public long getLong() {
        return data;
    }

    @Override
    public int getInt() {
        return data;
    }

    @Override
    public short getShort() {
        return data;
    }

    @Override
    public byte getByte() {
        return (byte) (data & 255);
    }

    @Override
    public double getDouble() {
        return data;
    }

    @Override
    public float getFloat() {
        return data;
    }
}
