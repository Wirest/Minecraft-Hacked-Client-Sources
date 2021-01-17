// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Point2f extends Tuple2f implements Serializable
{
    static final long serialVersionUID = -4801347926528714435L;
    
    public Point2f(final float n, final float n2) {
        super(n, n2);
    }
    
    public Point2f(final float[] array) {
        super(array);
    }
    
    public Point2f(final Point2f point2f) {
        super(point2f);
    }
    
    public Point2f(final Point2d point2d) {
        super(point2d);
    }
    
    public Point2f(final Tuple2d tuple2d) {
        super(tuple2d);
    }
    
    public Point2f(final Tuple2f tuple2f) {
        super(tuple2f);
    }
    
    public Point2f() {
    }
    
    public final float distanceSquared(final Point2f point2f) {
        final float n = this.x - point2f.x;
        final float n2 = this.y - point2f.y;
        return n * n + n2 * n2;
    }
    
    public final float distance(final Point2f point2f) {
        final float n = this.x - point2f.x;
        final float n2 = this.y - point2f.y;
        return (float)Math.sqrt(n * n + n2 * n2);
    }
    
    public final float distanceL1(final Point2f point2f) {
        return Math.abs(this.x - point2f.x) + Math.abs(this.y - point2f.y);
    }
    
    public final float distanceLinf(final Point2f point2f) {
        return Math.max(Math.abs(this.x - point2f.x), Math.abs(this.y - point2f.y));
    }
}
