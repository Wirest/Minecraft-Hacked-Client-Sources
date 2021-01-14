package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class PathEntity {
    /**
     * The actual points in the path
     */
    private final PathPoint[] points;

    /**
     * PathEntity Array Index the Entity is currently targeting
     */
    private int currentPathIndex;

    /**
     * The total length of the path
     */
    private int pathLength;
    private static final String __OBFID = "CL_00000575";

    public PathEntity(PathPoint[] p_i2136_1_) {
        this.points = p_i2136_1_;
        this.pathLength = p_i2136_1_.length;
    }

    /**
     * Directs this path to the next point in its array
     */
    public void incrementPathIndex() {
        ++this.currentPathIndex;
    }

    /**
     * Returns true if this path has reached the end
     */
    public boolean isFinished() {
        return this.currentPathIndex >= this.pathLength;
    }

    /**
     * returns the last PathPoint of the Array
     */
    public PathPoint getFinalPathPoint() {
        return this.pathLength > 0 ? this.points[this.pathLength - 1] : null;
    }

    /**
     * return the PathPoint located at the specified PathIndex, usually the current one
     */
    public PathPoint getPathPointFromIndex(int p_75877_1_) {
        return this.points[p_75877_1_];
    }

    public int getCurrentPathLength() {
        return this.pathLength;
    }

    public void setCurrentPathLength(int p_75871_1_) {
        this.pathLength = p_75871_1_;
    }

    public int getCurrentPathIndex() {
        return this.currentPathIndex;
    }

    public void setCurrentPathIndex(int p_75872_1_) {
        this.currentPathIndex = p_75872_1_;
    }

    /**
     * Gets the vector of the PathPoint associated with the given index.
     */
    public Vec3 getVectorFromIndex(Entity p_75881_1_, int p_75881_2_) {
        double var3 = (double) this.points[p_75881_2_].xCoord + (double) ((int) (p_75881_1_.width + 1.0F)) * 0.5D;
        double var5 = (double) this.points[p_75881_2_].yCoord;
        double var7 = (double) this.points[p_75881_2_].zCoord + (double) ((int) (p_75881_1_.width + 1.0F)) * 0.5D;
        return new Vec3(var3, var5, var7);
    }

    /**
     * returns the current PathEntity target node as Vec3D
     */
    public Vec3 getPosition(Entity p_75878_1_) {
        return this.getVectorFromIndex(p_75878_1_, this.currentPathIndex);
    }

    /**
     * Returns true if the EntityPath are the same. Non instance related equals.
     */
    public boolean isSamePath(PathEntity p_75876_1_) {
        if (p_75876_1_ == null) {
            return false;
        } else if (p_75876_1_.points.length != this.points.length) {
            return false;
        } else {
            for (int var2 = 0; var2 < this.points.length; ++var2) {
                if (this.points[var2].xCoord != p_75876_1_.points[var2].xCoord || this.points[var2].yCoord != p_75876_1_.points[var2].yCoord || this.points[var2].zCoord != p_75876_1_.points[var2].zCoord) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * Returns true if the final PathPoint in the PathEntity is equal to Vec3D coords.
     */
    public boolean isDestinationSame(Vec3 p_75880_1_) {
        PathPoint var2 = this.getFinalPathPoint();
        return var2 == null ? false : var2.xCoord == (int) p_75880_1_.xCoord && var2.zCoord == (int) p_75880_1_.zCoord;
    }
}
