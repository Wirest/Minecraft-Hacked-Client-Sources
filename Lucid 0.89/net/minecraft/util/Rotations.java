package net.minecraft.util;

import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;

public class Rotations
{
    /** Rotation on the X axis */
    protected final float x;

    /** Rotation on the Y axis */
    protected final float y;

    /** Rotation on the Z axis */
    protected final float z;

    public Rotations(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Rotations(NBTTagList nbt)
    {
        this.x = nbt.getFloatAt(0);
        this.y = nbt.getFloatAt(1);
        this.z = nbt.getFloatAt(2);
    }

    public NBTTagList writeToNBT()
    {
        NBTTagList var1 = new NBTTagList();
        var1.appendTag(new NBTTagFloat(this.x));
        var1.appendTag(new NBTTagFloat(this.y));
        var1.appendTag(new NBTTagFloat(this.z));
        return var1;
    }

    @Override
	public boolean equals(Object p_equals_1_)
    {
        if (!(p_equals_1_ instanceof Rotations))
        {
            return false;
        }
        else
        {
            Rotations var2 = (Rotations)p_equals_1_;
            return this.x == var2.x && this.y == var2.y && this.z == var2.z;
        }
    }

    /**
     * Gets the X axis rotation
     */
    public float getX()
    {
        return this.x;
    }

    /**
     * Gets the Y axis rotation
     */
    public float getY()
    {
        return this.y;
    }

    /**
     * Gets the Z axis rotation
     */
    public float getZ()
    {
        return this.z;
    }
}
