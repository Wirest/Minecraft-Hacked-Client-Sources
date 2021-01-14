
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Point4f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple4d;
import javax.vecmath.Tuple4f;

public class Point4d
        extends Tuple4d
        implements Serializable {
    public Point4d(double x, double y2, double z, double w) {
        super(x, y2, z, w);
    }

    public Point4d(double[] p2) {
        super(p2);
    }

    public Point4d(Point4f p1) {
        super(p1);
    }

    public Point4d(Point4d p1) {
        super(p1);
    }

    public Point4d(Tuple4d t1) {
        super(t1);
    }

    public Point4d(Tuple4f t1) {
        super(t1);
    }

    public Point4d() {
    }

    public Point4d(Tuple3d t1) {
        super(t1.x, t1.y, t1.z, 1.0);
    }

    public final void set(Tuple3d t1) {
        this.set(t1.x, t1.y, t1.z, 1.0);
    }

    public final double distanceSquared(Point4d p1) {
        double dx = this.x - p1.x;
        double dy = this.y - p1.y;
        double dz = this.z - p1.z;
        double dw = this.w - p1.w;
        return (float) (dx * dx + dy * dy + dz * dz + dw * dw);
    }

    public final double distance(Point4d p1) {
        return Math.sqrt(this.distanceSquared(p1));
    }

    public final double distanceL1(Point4d p1) {
        return Math.abs(this.x - p1.x) + Math.abs(this.y - p1.y) + Math.abs(this.z - p1.z) + Math.abs(this.w - p1.w);
    }

    public final double distanceLinf(Point4d p1) {
        return Math.max(Math.max(Math.abs(this.x - p1.x), Math.abs(this.y - p1.y)), Math.max(Math.abs(this.z - p1.z), Math.abs(this.w - p1.w)));
    }

    public final void project(Point4d p1) {
        this.x = p1.x / p1.w;
        this.y = p1.y / p1.w;
        this.z = p1.z / p1.w;
        this.w = 1.0;
    }
}

