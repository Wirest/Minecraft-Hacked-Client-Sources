package net.minecraft.util;

import com.google.common.base.Objects;

public class Vec3i implements Comparable
{
    /** The Null vector constant (0, 0, 0) */
    public static final Vec3i NULL_VECTOR = new Vec3i(0, 0, 0);

    /** X coordinate */
    private final int x;

    /** Y coordinate */
    private final int y;

    /** Z coordinate */
    private final int z;

    public Vec3i(int xIn, int yIn, int zIn)
    {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
    }

    public Vec3i(double xIn, double yIn, double zIn)
    {
        this(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
    }

    @Override
	public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof Vec3i))
        {
            return false;
        }
        else
        {
            Vec3i var2 = (Vec3i)p_equals_1_;
            return this.getX() != var2.getX() ? false : (this.getY() != var2.getY() ? false : this.getZ() == var2.getZ());
        }
    }

    @Override
	public int hashCode()
    {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    public int compareTo(Vec3i vec)
    {
        return this.getY() == vec.getY() ? (this.getZ() == vec.getZ() ? this.getX() - vec.getX() : this.getZ() - vec.getZ()) : this.getY() - vec.getY();
    }

    /**
     * Get the X coordinate
     */
    public int getX()
    {
        return this.x;
    }

    /**
     * Get the Y coordinate
     */
    public int getY()
    {
        return this.y;
    }

    /**
     * Get the Z coordinate
     */
    public int getZ()
    {
        return this.z;
    }

    /**
     * Calculate the cross product of this and the given Vector
     */
    public Vec3i crossProduct(Vec3i vec)
    {
        return new Vec3i(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }

    /**
     * Calculate squared distance to the given coordinates
     *  
     * @param toX X Coordinate
     * @param toY Y Coordinate
     * @param toZ Z Coordinate
     */
    public double distanceSq(double toX, double toY, double toZ)
    {
        double var7 = this.getX() - toX;
        double var9 = this.getY() - toY;
        double var11 = this.getZ() - toZ;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    /**
     * Compute square of distance from point x, y, z to center of this Block
     */
    public double distanceSqToCenter(double xIn, double yIn, double zIn)
    {
        double var7 = this.getX() + 0.5D - xIn;
        double var9 = this.getY() + 0.5D - yIn;
        double var11 = this.getZ() + 0.5D - zIn;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    /**
     * Calculate squared distance to the given Vector
     */
    public double distanceSq(Vec3i to)
    {
        return this.distanceSq(to.getX(), to.getY(), to.getZ());
    }

    @Override
	public String toString()
    {
        return Objects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }

    @Override
	public int compareTo(Object p_compareTo_1_)
    {
        return this.compareTo((Vec3i)p_compareTo_1_);
    }
}
