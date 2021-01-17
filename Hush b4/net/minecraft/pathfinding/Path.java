// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

public class Path
{
    private PathPoint[] pathPoints;
    private int count;
    
    public Path() {
        this.pathPoints = new PathPoint[1024];
    }
    
    public PathPoint addPoint(final PathPoint point) {
        if (point.index >= 0) {
            throw new IllegalStateException("OW KNOWS!");
        }
        if (this.count == this.pathPoints.length) {
            final PathPoint[] apathpoint = new PathPoint[this.count << 1];
            System.arraycopy(this.pathPoints, 0, apathpoint, 0, this.count);
            this.pathPoints = apathpoint;
        }
        this.pathPoints[this.count] = point;
        point.index = this.count;
        this.sortBack(this.count++);
        return point;
    }
    
    public void clearPath() {
        this.count = 0;
    }
    
    public PathPoint dequeue() {
        final PathPoint pathpoint = this.pathPoints[0];
        final PathPoint[] pathPoints = this.pathPoints;
        final int n = 0;
        final PathPoint[] pathPoints2 = this.pathPoints;
        final int count = this.count - 1;
        this.count = count;
        pathPoints[n] = pathPoints2[count];
        this.pathPoints[this.count] = null;
        if (this.count > 0) {
            this.sortForward(0);
        }
        pathpoint.index = -1;
        return pathpoint;
    }
    
    public void changeDistance(final PathPoint p_75850_1_, final float p_75850_2_) {
        final float f = p_75850_1_.distanceToTarget;
        p_75850_1_.distanceToTarget = p_75850_2_;
        if (p_75850_2_ < f) {
            this.sortBack(p_75850_1_.index);
        }
        else {
            this.sortForward(p_75850_1_.index);
        }
    }
    
    private void sortBack(int p_75847_1_) {
        final PathPoint pathpoint = this.pathPoints[p_75847_1_];
        final float f = pathpoint.distanceToTarget;
        while (p_75847_1_ > 0) {
            final int i = p_75847_1_ - 1 >> 1;
            final PathPoint pathpoint2 = this.pathPoints[i];
            if (f >= pathpoint2.distanceToTarget) {
                break;
            }
            this.pathPoints[p_75847_1_] = pathpoint2;
            pathpoint2.index = p_75847_1_;
            p_75847_1_ = i;
        }
        this.pathPoints[p_75847_1_] = pathpoint;
        pathpoint.index = p_75847_1_;
    }
    
    private void sortForward(int p_75846_1_) {
        final PathPoint pathpoint = this.pathPoints[p_75846_1_];
        final float f = pathpoint.distanceToTarget;
        while (true) {
            final int i = 1 + (p_75846_1_ << 1);
            final int j = i + 1;
            if (i >= this.count) {
                break;
            }
            final PathPoint pathpoint2 = this.pathPoints[i];
            final float f2 = pathpoint2.distanceToTarget;
            PathPoint pathpoint3;
            float f3;
            if (j >= this.count) {
                pathpoint3 = null;
                f3 = Float.POSITIVE_INFINITY;
            }
            else {
                pathpoint3 = this.pathPoints[j];
                f3 = pathpoint3.distanceToTarget;
            }
            if (f2 < f3) {
                if (f2 >= f) {
                    break;
                }
                this.pathPoints[p_75846_1_] = pathpoint2;
                pathpoint2.index = p_75846_1_;
                p_75846_1_ = i;
            }
            else {
                if (f3 >= f) {
                    break;
                }
                this.pathPoints[p_75846_1_] = pathpoint3;
                pathpoint3.index = p_75846_1_;
                p_75846_1_ = j;
            }
        }
        this.pathPoints[p_75846_1_] = pathpoint;
        pathpoint.index = p_75846_1_;
    }
    
    public boolean isPathEmpty() {
        return this.count == 0;
    }
}
