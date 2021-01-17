// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Point3f extends Tuple3f implements Serializable
{
    static final long serialVersionUID = -8689337816398030143L;
    
    public Point3f(final float n, final float n2, final float n3) {
        super(n, n2, n3);
    }
    
    public Point3f(final float[] array) {
        super(array);
    }
    
    public Point3f(final Point3f point3f) {
        super(point3f);
    }
    
    public Point3f(final Point3d point3d) {
        super(point3d);
    }
    
    public Point3f(final Tuple3f tuple3f) {
        super(tuple3f);
    }
    
    public Point3f(final Tuple3d tuple3d) {
        super(tuple3d);
    }
    
    public Point3f() {
    }
    
    public final float distanceSquared(final Point3f point3f) {
        final float n = this.x - point3f.x;
        final float n2 = this.y - point3f.y;
        final float n3 = this.z - point3f.z;
        return n * n + n2 * n2 + n3 * n3;
    }
    
    public final float distance(final Point3f point3f) {
        final float n = this.x - point3f.x;
        final float n2 = this.y - point3f.y;
        final float n3 = this.z - point3f.z;
        return (float)Math.sqrt(n * n + n2 * n2 + n3 * n3);
    }
    
    public final float distanceL1(final Point3f point3f) {
        return Math.abs(this.x - point3f.x) + Math.abs(this.y - point3f.y) + Math.abs(this.z - point3f.z);
    }
    
    public final float distanceLinf(final Point3f point3f) {
        return Math.max(Math.max(Math.abs(this.x - point3f.x), Math.abs(this.y - point3f.y)), Math.abs(this.z - point3f.z));
    }
    
    public final void project(final Point4f point4f) {
        final float n = 1.0f / point4f.w;
        this.x = point4f.x * n;
        this.y = point4f.y * n;
        this.z = point4f.z * n;
    }
}
