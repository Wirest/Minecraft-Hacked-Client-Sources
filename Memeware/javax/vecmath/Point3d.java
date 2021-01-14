
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Point3f;
import javax.vecmath.Point4d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

public class Point3d
        extends Tuple3d
        implements Serializable {
    public Point3d(double x, double y2, double z) {
        super(x, y2, z);
    }

    public Point3d(double[] p2) {
        super(p2);
    }

    public Point3d(Point3d p1) {
        super(p1);
    }

    public Point3d(Point3f p1) {
        super(p1);
    }

    public Point3d(Tuple3d t1) {
        super(t1);
    }

    public Point3d(Tuple3f t1) {
        super(t1);
    }

    public Point3d() {
    }

    public final double distanceSquared(Point3d p1) {
        double dx = this.x - p1.x;
        double dy = this.y - p1.y;
        double dz = this.z - p1.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public final double distance(Point3d p1) {
        return Math.sqrt(this.distanceSquared(p1));
    }

    public final double distanceL1(Point3d p1) {
        return Math.abs(this.x - p1.x) + Math.abs(this.y - p1.y) + Math.abs(this.z - p1.z);
    }

    public final double distanceLinf(Point3d p1) {
        return Math.max(Math.max(Math.abs(this.x - p1.x), Math.abs(this.y - p1.y)), Math.abs(this.z - p1.z));
    }

    public final void project(Point4d p1) {
        this.x = p1.x / p1.w;
        this.y = p1.y / p1.w;
        this.z = p1.z / p1.w;
    }
}

