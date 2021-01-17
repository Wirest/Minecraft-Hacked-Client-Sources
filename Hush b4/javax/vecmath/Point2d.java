// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Point2d extends Tuple2d implements Serializable
{
    static final long serialVersionUID = 1133748791492571954L;
    
    public Point2d(final double n, final double n2) {
        super(n, n2);
    }
    
    public Point2d(final double[] array) {
        super(array);
    }
    
    public Point2d(final Point2d point2d) {
        super(point2d);
    }
    
    public Point2d(final Point2f point2f) {
        super(point2f);
    }
    
    public Point2d(final Tuple2d tuple2d) {
        super(tuple2d);
    }
    
    public Point2d(final Tuple2f tuple2f) {
        super(tuple2f);
    }
    
    public Point2d() {
    }
    
    public final double distanceSquared(final Point2d point2d) {
        final double n = this.x - point2d.x;
        final double n2 = this.y - point2d.y;
        return n * n + n2 * n2;
    }
    
    public final double distance(final Point2d point2d) {
        final double n = this.x - point2d.x;
        final double n2 = this.y - point2d.y;
        return Math.sqrt(n * n + n2 * n2);
    }
    
    public final double distanceL1(final Point2d point2d) {
        return Math.abs(this.x - point2d.x) + Math.abs(this.y - point2d.y);
    }
    
    public final double distanceLinf(final Point2d point2d) {
        return Math.max(Math.abs(this.x - point2d.x), Math.abs(this.y - point2d.y));
    }
}
