package net.minecraft.pathfinding;

import net.minecraft.util.MathHelper;

public class PathPoint
{
    /** The x coordinate of this point */
    public final int xCoord;

    /** The y coordinate of this point */
    public final int yCoord;

    /** The z coordinate of this point */
    public final int zCoord;

    /** A hash of the coordinates used to identify this point */
    private final int hash;

    /** The index of this point in its assigned path */
    int index = -1;

    /** The distance along the path to this point */
    float totalPathDistance;

    /** The linear distance to the next point */
    float distanceToNext;

    /** The distance to the target */
    float distanceToTarget;

    /** The point preceding this in its assigned path */
    PathPoint previous;

    /** True if the pathfinder has already visited this point */
    public boolean visited;
    private static final String __OBFID = "CL_00000574";

    public PathPoint(int p_i2135_1_, int p_i2135_2_, int p_i2135_3_)
    {
        this.xCoord = p_i2135_1_;
        this.yCoord = p_i2135_2_;
        this.zCoord = p_i2135_3_;
        this.hash = makeHash(p_i2135_1_, p_i2135_2_, p_i2135_3_);
    }

    public static int makeHash(int p_75830_0_, int p_75830_1_, int p_75830_2_)
    {
        return p_75830_1_ & 255 | (p_75830_0_ & 32767) << 8 | (p_75830_2_ & 32767) << 24 | (p_75830_0_ < 0 ? Integer.MIN_VALUE : 0) | (p_75830_2_ < 0 ? 32768 : 0);
    }

    /**
     * Returns the linear distance to another path point
     */
    public float distanceTo(PathPoint p_75829_1_)
    {
        float var2 = (float)(p_75829_1_.xCoord - this.xCoord);
        float var3 = (float)(p_75829_1_.yCoord - this.yCoord);
        float var4 = (float)(p_75829_1_.zCoord - this.zCoord);
        return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
    }

    /**
     * Returns the squared distance to another path point
     */
    public float distanceToSquared(PathPoint p_75832_1_)
    {
        float var2 = (float)(p_75832_1_.xCoord - this.xCoord);
        float var3 = (float)(p_75832_1_.yCoord - this.yCoord);
        float var4 = (float)(p_75832_1_.zCoord - this.zCoord);
        return var2 * var2 + var3 * var3 + var4 * var4;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (!(p_equals_1_ instanceof PathPoint))
        {
            return false;
        }
        else
        {
            PathPoint var2 = (PathPoint)p_equals_1_;
            return this.hash == var2.hash && this.xCoord == var2.xCoord && this.yCoord == var2.yCoord && this.zCoord == var2.zCoord;
        }
    }

    public int hashCode()
    {
        return this.hash;
    }

    /**
     * Returns true if this point has already been assigned to a path
     */
    public boolean isAssigned()
    {
        return this.index >= 0;
    }

    public String toString()
    {
        return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
    }
}
