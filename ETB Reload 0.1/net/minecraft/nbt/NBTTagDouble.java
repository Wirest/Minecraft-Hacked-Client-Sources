package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.util.MathHelper;

public class NBTTagDouble extends NBTBase.NBTPrimitive
{
    /** The double value for the tag. */
    private double data;

    NBTTagDouble()
    {
    }

    public NBTTagDouble(double data)
    {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    @Override
	void write(DataOutput output) throws IOException
    {
        output.writeDouble(this.data);
    }

    @Override
	void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(128L);
        this.data = input.readDouble();
    }

    /**
     * Gets the type byte for the tag.
     */
    @Override
	public byte getId()
    {
        return (byte)6;
    }

    @Override
	public String toString()
    {
        return "" + this.data + "d";
    }

    /**
     * Creates a clone of the tag.
     */
    @Override
	public NBTBase copy()
    {
        return new NBTTagDouble(this.data);
    }

    @Override
	public boolean equals(Object p_equals_1_)
    {
        if (super.equals(p_equals_1_))
        {
            NBTTagDouble nbttagdouble = (NBTTagDouble)p_equals_1_;
            return this.data == nbttagdouble.data;
        }
        else
        {
            return false;
        }
    }

    @Override
	public int hashCode()
    {
        long i = Double.doubleToLongBits(this.data);
        return super.hashCode() ^ (int)(i ^ i >>> 32);
    }

    @Override
	public long getLong()
    {
        return (long)Math.floor(this.data);
    }

    @Override
	public int getInt()
    {
        return MathHelper.floor_double(this.data);
    }

    @Override
	public short getShort()
    {
        return (short)(MathHelper.floor_double(this.data) & 65535);
    }

    @Override
	public byte getByte()
    {
        return (byte)(MathHelper.floor_double(this.data) & 255);
    }

    @Override
	public double getDouble()
    {
        return this.data;
    }

    @Override
	public float getFloat()
    {
        return (float)this.data;
    }
}
