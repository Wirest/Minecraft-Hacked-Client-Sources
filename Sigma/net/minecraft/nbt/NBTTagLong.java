package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong extends NBTBase.NBTPrimitive {
    /**
     * The long value for the tag.
     */
    private long data;
    private static final String __OBFID = "CL_00001225";

    NBTTagLong() {
    }

    public NBTTagLong(long data) {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension
     * classes
     */
    @Override
    void write(DataOutput output) throws IOException {
        output.writeLong(data);
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(64L);
        data = input.readLong();
    }

    /**
     * Gets the type byte for the tag.
     */
    @Override
    public byte getId() {
        return (byte) 4;
    }

    @Override
    public String toString() {
        return "" + data + "L";
    }

    /**
     * Creates a clone of the tag.
     */
    @Override
    public NBTBase copy() {
        return new NBTTagLong(data);
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            NBTTagLong var2 = (NBTTagLong) p_equals_1_;
            return data == var2.data;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ (int) (data ^ data >>> 32);
    }

    @Override
    public long getLong() {
        return data;
    }

    @Override
    public int getInt() {
        return (int) (data & -1L);
    }

    @Override
    public short getShort() {
        return (short) ((int) (data & 65535L));
    }

    @Override
    public byte getByte() {
        return (byte) ((int) (data & 255L));
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
