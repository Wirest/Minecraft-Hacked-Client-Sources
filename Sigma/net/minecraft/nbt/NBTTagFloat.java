package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.util.MathHelper;

public class NBTTagFloat extends NBTBase.NBTPrimitive {
    /**
     * The float value for the tag.
     */
    private float data;
    private static final String __OBFID = "CL_00001220";

    NBTTagFloat() {
    }

    public NBTTagFloat(float data) {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension
     * classes
     */
    @Override
    void write(DataOutput output) throws IOException {
        output.writeFloat(data);
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(32L);
        data = input.readFloat();
    }

    /**
     * Gets the type byte for the tag.
     */
    @Override
    public byte getId() {
        return (byte) 5;
    }

    @Override
    public String toString() {
        return "" + data + "f";
    }

    /**
     * Creates a clone of the tag.
     */
    @Override
    public NBTBase copy() {
        return new NBTTagFloat(data);
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            NBTTagFloat var2 = (NBTTagFloat) p_equals_1_;
            return data == var2.data;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ Float.floatToIntBits(data);
    }

    @Override
    public long getLong() {
        return (long) data;
    }

    @Override
    public int getInt() {
        return MathHelper.floor_float(data);
    }

    @Override
    public short getShort() {
        return (short) (MathHelper.floor_float(data) & 65535);
    }

    @Override
    public byte getByte() {
        return (byte) (MathHelper.floor_float(data) & 255);
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
