
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Point3d;
import javax.vecmath.Point4f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

public class Point3f
        extends Tuple3f
        implements Serializable {
    public Point3f(float x, float y2, float z) {
        super(x, y2, z);
    }

    public Point3f(float[] p2) {
        super(p2);
    }

    public Point3f(Point3f p1) {
        super(p1);
    }

    public Point3f(Point3d p1) {
        super(p1);
    }

    public Point3f(Tuple3f t1) {
        super(t1);
    }

    public Point3f(Tuple3d t1) {
        super(t1);
    }

    public Point3f() {
    }

    public final float distanceSquared(Point3f p1) {
        double dx = this.x - p1.x;
        double dy = this.y - p1.y;
        double dz = this.z - p1.z;
        return (float) (dx * dx + dy * dy + dz * dz);
    }

    public final float distance(Point3f p1) {
        return (float) Math.sqrt(this.distanceSquared(p1));
    }

    public final float distanceL1(Point3f p1) {
        return Math.abs(this.x - p1.x) + Math.abs(this.y - p1.y) + Math.abs(this.z - p1.z);
    }

    public final float distanceLinf(Point3f p1) {
        return Math.max(Math.max(Math.abs(this.x - p1.x), Math.abs(this.y - p1.y)), Math.abs(this.z - p1.z));
    }

    public final void project(Point4f p1) {
        this.x = p1.x / p1.w;
        this.y = p1.y / p1.w;
        this.z = p1.z / p1.w;
    }
}

