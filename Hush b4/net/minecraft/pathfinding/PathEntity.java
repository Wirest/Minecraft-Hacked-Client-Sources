// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;

public class PathEntity
{
    private final PathPoint[] points;
    private int currentPathIndex;
    private int pathLength;
    
    public PathEntity(final PathPoint[] pathpoints) {
        this.points = pathpoints;
        this.pathLength = pathpoints.length;
    }
    
    public void incrementPathIndex() {
        ++this.currentPathIndex;
    }
    
    public boolean isFinished() {
        return this.currentPathIndex >= this.pathLength;
    }
    
    public PathPoint getFinalPathPoint() {
        return (this.pathLength > 0) ? this.points[this.pathLength - 1] : null;
    }
    
    public PathPoint getPathPointFromIndex(final int index) {
        return this.points[index];
    }
    
    public int getCurrentPathLength() {
        return this.pathLength;
    }
    
    public void setCurrentPathLength(final int length) {
        this.pathLength = length;
    }
    
    public int getCurrentPathIndex() {
        return this.currentPathIndex;
    }
    
    public void setCurrentPathIndex(final int currentPathIndexIn) {
        this.currentPathIndex = currentPathIndexIn;
    }
    
    public Vec3 getVectorFromIndex(final Entity entityIn, final int index) {
        final double d0 = this.points[index].xCoord + (int)(entityIn.width + 1.0f) * 0.5;
        final double d2 = this.points[index].yCoord;
        final double d3 = this.points[index].zCoord + (int)(entityIn.width + 1.0f) * 0.5;
        return new Vec3(d0, d2, d3);
    }
    
    public Vec3 getPosition(final Entity entityIn) {
        return this.getVectorFromIndex(entityIn, this.currentPathIndex);
    }
    
    public boolean isSamePath(final PathEntity pathentityIn) {
        if (pathentityIn == null) {
            return false;
        }
        if (pathentityIn.points.length != this.points.length) {
            return false;
        }
        for (int i = 0; i < this.points.length; ++i) {
            if (this.points[i].xCoord != pathentityIn.points[i].xCoord || this.points[i].yCoord != pathentityIn.points[i].yCoord || this.points[i].zCoord != pathentityIn.points[i].zCoord) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isDestinationSame(final Vec3 vec) {
        final PathPoint pathpoint = this.getFinalPathPoint();
        return pathpoint != null && (pathpoint.xCoord == (int)vec.xCoord && pathpoint.zCoord == (int)vec.zCoord);
    }
}
