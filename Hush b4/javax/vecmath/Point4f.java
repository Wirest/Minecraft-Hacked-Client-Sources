// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Point4f extends Tuple4f implements Serializable
{
    static final long serialVersionUID = 4643134103185764459L;
    
    public Point4f(final float n, final float n2, final float n3, final float n4) {
        super(n, n2, n3, n4);
    }
    
    public Point4f(final float[] array) {
        super(array);
    }
    
    public Point4f(final Point4f point4f) {
        super(point4f);
    }
    
    public Point4f(final Point4d point4d) {
        super(point4d);
    }
    
    public Point4f(final Tuple4f tuple4f) {
        super(tuple4f);
    }
    
    public Point4f(final Tuple4d tuple4d) {
        super(tuple4d);
    }
    
    public Point4f(final Tuple3f tuple3f) {
        super(tuple3f.x, tuple3f.y, tuple3f.z, 1.0f);
    }
    
    public Point4f() {
    }
    
    public final void set(final Tuple3f tuple3f) {
        this.x = tuple3f.x;
        this.y = tuple3f.y;
        this.z = tuple3f.z;
        this.w = 1.0f;
    }
    
    public final float distanceSquared(final Point4f point4f) {
        final float n = this.x - point4f.x;
        final float n2 = this.y - point4f.y;
        final float n3 = this.z - point4f.z;
        final float n4 = this.w - point4f.w;
        return n * n + n2 * n2 + n3 * n3 + n4 * n4;
    }
    
    public final float distance(final Point4f point4f) {
        final float n = this.x - point4f.x;
        final float n2 = this.y - point4f.y;
        final float n3 = this.z - point4f.z;
        final float n4 = this.w - point4f.w;
        return (float)Math.sqrt(n * n + n2 * n2 + n3 * n3 + n4 * n4);
    }
    
    public final float distanceL1(final Point4f point4f) {
        return Math.abs(this.x - point4f.x) + Math.abs(this.y - point4f.y) + Math.abs(this.z - point4f.z) + Math.abs(this.w - point4f.w);
    }
    
    public final float distanceLinf(final Point4f point4f) {
        return Math.max(Math.max(Math.abs(this.x - point4f.x), Math.abs(this.y - point4f.y)), Math.max(Math.abs(this.z - point4f.z), Math.abs(this.w - point4f.w)));
    }
    
    public final void project(final Point4f point4f) {
        final float n = 1.0f / point4f.w;
        this.x = point4f.x * n;
        this.y = point4f.y * n;
        this.z = point4f.z * n;
        this.w = 1.0f;
    }
}
