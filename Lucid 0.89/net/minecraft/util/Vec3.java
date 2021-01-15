package net.minecraft.util;

public class Vec3
{
    /** X coordinate of Vec3D */
    public final double xCoord;

    /** Y coordinate of Vec3D */
    public final double yCoord;

    /** Z coordinate of Vec3D */
    public final double zCoord;

    public Vec3(double x, double y, double z)
    {
        if (x == -0.0D)
        {
            x = 0.0D;
        }

        if (y == -0.0D)
        {
            y = 0.0D;
        }

        if (z == -0.0D)
        {
            z = 0.0D;
        }

        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    /**
     * Returns a new vector with the result of the specified vector minus this.
     */
    public Vec3 subtractReverse(Vec3 vec)
    {
        return new Vec3(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
    }

    /**
     * Normalizes the vector to a length of 1 (except if it is the zero vector)
     */
    public Vec3 normalize()
    {
        double var1 = MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return var1 < 1.0E-4D ? new Vec3(0.0D, 0.0D, 0.0D) : new Vec3(this.xCoord / var1, this.yCoord / var1, this.zCoord / var1);
    }

    public double dotProduct(Vec3 vec)
    {
        return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
    }

    /**
     * Returns a new vector with the result of this vector x the specified vector.
     */
    public Vec3 crossProduct(Vec3 vec)
    {
        return new Vec3(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
    }

    public Vec3 subtract(Vec3 vec)
    {
        return this.subtract(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public Vec3 subtract(double x, double y, double z)
    {
        return this.addVector(-x, -y, -z);
    }

    public Vec3 add(Vec3 vec)
    {
        return this.addVector(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    /**
     * Adds the specified x,y,z vector components to this vector and returns the resulting vector. Does not change this
     * vector.
     */
    public Vec3 addVector(double x, double y, double z)
    {
        return new Vec3(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    /**
     * Euclidean distance between this and the specified vector, returned as double.
     */
    public double distanceTo(Vec3 vec)
    {
        double var2 = vec.xCoord - this.xCoord;
        double var4 = vec.yCoord - this.yCoord;
        double var6 = vec.zCoord - this.zCoord;
        return MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
    }

    /**
     * The square of the Euclidean distance between this and the specified vector.
     */
    public double squareDistanceTo(Vec3 vec)
    {
        double var2 = vec.xCoord - this.xCoord;
        double var4 = vec.yCoord - this.yCoord;
        double var6 = vec.zCoord - this.zCoord;
        return var2 * var2 + var4 * var4 + var6 * var6;
    }

    /**
     * Returns the length of the vector.
     */
    public double lengthVector()
    {
        return MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    /**
     * Returns a new vector with x value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     */
    public Vec3 getIntermediateWithXValue(Vec3 vec, double x)
    {
        double var4 = vec.xCoord - this.xCoord;
        double var6 = vec.yCoord - this.yCoord;
        double var8 = vec.zCoord - this.zCoord;

        if (var4 * var4 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double var10 = (x - this.xCoord) / var4;
            return var10 >= 0.0D && var10 <= 1.0D ? new Vec3(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
        }
    }

    /**
     * Returns a new vector with y value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     */
    public Vec3 getIntermediateWithYValue(Vec3 vec, double y)
    {
        double var4 = vec.xCoord - this.xCoord;
        double var6 = vec.yCoord - this.yCoord;
        double var8 = vec.zCoord - this.zCoord;

        if (var6 * var6 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double var10 = (y - this.yCoord) / var6;
            return var10 >= 0.0D && var10 <= 1.0D ? new Vec3(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
        }
    }

    /**
     * Returns a new vector with z value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     */
    public Vec3 getIntermediateWithZValue(Vec3 vec, double z)
    {
        double var4 = vec.xCoord - this.xCoord;
        double var6 = vec.yCoord - this.yCoord;
        double var8 = vec.zCoord - this.zCoord;

        if (var8 * var8 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double var10 = (z - this.zCoord) / var8;
            return var10 >= 0.0D && var10 <= 1.0D ? new Vec3(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
        }
    }

    @Override
	public String toString()
    {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }

    public Vec3 rotatePitch(float pitch)
    {
        float var2 = MathHelper.cos(pitch);
        float var3 = MathHelper.sin(pitch);
        double var4 = this.xCoord;
        double var6 = this.yCoord * var2 + this.zCoord * var3;
        double var8 = this.zCoord * var2 - this.yCoord * var3;
        return new Vec3(var4, var6, var8);
    }

    public Vec3 rotateYaw(float yaw)
    {
        float var2 = MathHelper.cos(yaw);
        float var3 = MathHelper.sin(yaw);
        double var4 = this.xCoord * var2 + this.zCoord * var3;
        double var6 = this.yCoord;
        double var8 = this.zCoord * var2 - this.xCoord * var3;
        return new Vec3(var4, var6, var8);
    }
}
