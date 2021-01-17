// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Point4d extends Tuple4d implements Serializable
{
    static final long serialVersionUID = 1733471895962736949L;
    
    public Point4d(final double n, final double n2, final double n3, final double n4) {
        super(n, n2, n3, n4);
    }
    
    public Point4d(final double[] array) {
        super(array);
    }
    
    public Point4d(final Point4d point4d) {
        super(point4d);
    }
    
    public Point4d(final Point4f point4f) {
        super(point4f);
    }
    
    public Point4d(final Tuple4f tuple4f) {
        super(tuple4f);
    }
    
    public Point4d(final Tuple4d tuple4d) {
        super(tuple4d);
    }
    
    public Point4d(final Tuple3d tuple3d) {
        super(tuple3d.x, tuple3d.y, tuple3d.z, 1.0);
    }
    
    public Point4d() {
    }
    
    public final void set(final Tuple3d tuple3d) {
        this.x = tuple3d.x;
        this.y = tuple3d.y;
        this.z = tuple3d.z;
        this.w = 1.0;
    }
    
    public final double distanceSquared(final Point4d point4d) {
        final double n = this.x - point4d.x;
        final double n2 = this.y - point4d.y;
        final double n3 = this.z - point4d.z;
        final double n4 = this.w - point4d.w;
        return n * n + n2 * n2 + n3 * n3 + n4 * n4;
    }
    
    public final double distance(final Point4d point4d) {
        final double n = this.x - point4d.x;
        final double n2 = this.y - point4d.y;
        final double n3 = this.z - point4d.z;
        final double n4 = this.w - point4d.w;
        return Math.sqrt(n * n + n2 * n2 + n3 * n3 + n4 * n4);
    }
    
    public final double distanceL1(final Point4d point4d) {
        return Math.abs(this.x - point4d.x) + Math.abs(this.y - point4d.y) + Math.abs(this.z - point4d.z) + Math.abs(this.w - point4d.w);
    }
    
    public final double distanceLinf(final Point4d point4d) {
        return Math.max(Math.max(Math.abs(this.x - point4d.x), Math.abs(this.y - point4d.y)), Math.max(Math.abs(this.z - point4d.z), Math.abs(this.w - point4d.w)));
    }
    
    public final void project(final Point4d point4d) {
        final double n = 1.0 / point4d.w;
        this.x = point4d.x * n;
        this.y = point4d.y * n;
        this.z = point4d.z * n;
        this.w = 1.0;
    }
}
