package net.minecraft.pathfinding;

public class Path {
    /**
     * Contains the points in this path
     */
    private PathPoint[] pathPoints = new PathPoint[1024];

    /**
     * The number of points in this path
     */
    private int count;
    private static final String __OBFID = "CL_00000573";

    /**
     * Adds a point to the path
     */
    public PathPoint addPoint(PathPoint point) {
        if (point.index >= 0) {
            throw new IllegalStateException("OW KNOWS!");
        } else {
            if (count == pathPoints.length) {
                PathPoint[] var2 = new PathPoint[count << 1];
                System.arraycopy(pathPoints, 0, var2, 0, count);
                pathPoints = var2;
            }

            pathPoints[count] = point;
            point.index = count;
            sortBack(count++);
            return point;
        }
    }

    /**
     * Clears the path
     */
    public void clearPath() {
        count = 0;
    }

    /**
     * Returns and removes the first point in the path
     */
    public PathPoint dequeue() {
        PathPoint var1 = pathPoints[0];
        pathPoints[0] = pathPoints[--count];
        pathPoints[count] = null;

        if (count > 0) {
            sortForward(0);
        }

        var1.index = -1;
        return var1;
    }

    /**
     * Changes the provided point's distance to target
     */
    public void changeDistance(PathPoint p_75850_1_, float p_75850_2_) {
        float var3 = p_75850_1_.distanceToTarget;
        p_75850_1_.distanceToTarget = p_75850_2_;

        if (p_75850_2_ < var3) {
            sortBack(p_75850_1_.index);
        } else {
            sortForward(p_75850_1_.index);
        }
    }

    /**
     * Sorts a point to the left
     */
    private void sortBack(int p_75847_1_) {
        PathPoint var2 = pathPoints[p_75847_1_];
        int var4;

        for (float var3 = var2.distanceToTarget; p_75847_1_ > 0; p_75847_1_ = var4) {
            var4 = p_75847_1_ - 1 >> 1;
            PathPoint var5 = pathPoints[var4];

            if (var3 >= var5.distanceToTarget) {
                break;
            }

            pathPoints[p_75847_1_] = var5;
            var5.index = p_75847_1_;
        }

        pathPoints[p_75847_1_] = var2;
        var2.index = p_75847_1_;
    }

    /**
     * Sorts a point to the right
     */
    private void sortForward(int p_75846_1_) {
        PathPoint var2 = pathPoints[p_75846_1_];
        float var3 = var2.distanceToTarget;

        while (true) {
            int var4 = 1 + (p_75846_1_ << 1);
            int var5 = var4 + 1;

            if (var4 >= count) {
                break;
            }

            PathPoint var6 = pathPoints[var4];
            float var7 = var6.distanceToTarget;
            PathPoint var8;
            float var9;

            if (var5 >= count) {
                var8 = null;
                var9 = Float.POSITIVE_INFINITY;
            } else {
                var8 = pathPoints[var5];
                var9 = var8.distanceToTarget;
            }

            if (var7 < var9) {
                if (var7 >= var3) {
                    break;
                }

                pathPoints[p_75846_1_] = var6;
                var6.index = p_75846_1_;
                p_75846_1_ = var4;
            } else {
                if (var9 >= var3) {
                    break;
                }

                pathPoints[p_75846_1_] = var8;
                var8.index = p_75846_1_;
                p_75846_1_ = var5;
            }
        }

        pathPoints[p_75846_1_] = var2;
        var2.index = p_75846_1_;
    }

    /**
     * Returns true if this path contains no points
     */
    public boolean isPathEmpty() {
        return count == 0;
    }
}
