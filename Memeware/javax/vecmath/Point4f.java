
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Point4d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4d;
import javax.vecmath.Tuple4f;

public class Point4f
        extends Tuple4f
        implements Serializable {
    public Point4f(float x, float y2, float z, float w) {
        super(x, y2, z, w);
    }

    public Point4f(float[] p2) {
        super(p2);
    }

    public Point4f(Point4f p1) {
        super(p1);
    }

    public Point4f(Point4d p1) {
        super(p1);
    }

    public Point4f(Tuple4d t1) {
        super(t1);
    }

    public Point4f(Tuple4f t1) {
        super(t1);
    }

    public Point4f() {
    }

    public Point4f(Tuple3f t1) {
        super(t1.x, t1.y, t1.z, 1.0f);
    }

    public final void set(Tuple3f t1) {
        this.set(t1.x, t1.y, t1.z, 1.0f);
    }

    public final float distanceSquared(Point4f p1) {
        double dx = this.x - p1.x;
        double dy = this.y - p1.y;
        double dz = this.z - p1.z;
        double dw = this.w - p1.w;
        return (float) (dx * dx + dy * dy + dz * dz + dw * dw);
    }

    public final float distance(Point4f p1) {
        return (float) Math.sqrt(this.distanceSquared(p1));
    }

    public final float distanceL1(Point4f p1) {
        return Math.abs(this.x - p1.x) + Math.abs(this.y - p1.y) + Math.abs(this.z - p1.z) + Math.abs(this.w - p1.w);
    }

    public final float distanceLinf(Point4f p1) {
        return Math.max(Math.max(Math.abs(this.x - p1.x), Math.abs(this.y - p1.y)), Math.max(Math.abs(this.z - p1.z), Math.abs(this.w - p1.w)));
    }

    public final void project(Point4f p1) {
        this.x = p1.x / p1.w;
        this.y = p1.y / p1.w;
        this.z = p1.z / p1.w;
        this.w = 1.0f;
    }
}

