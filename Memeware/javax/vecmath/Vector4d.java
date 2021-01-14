
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple4d;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector4f;

public class Vector4d
        extends Tuple4d
        implements Serializable {
    public Vector4d(double x, double y2, double z, double w) {
        super(x, y2, z, w);
    }

    public Vector4d(double[] v) {
        super(v);
    }

    public Vector4d(Vector4f v1) {
        super(v1);
    }

    public Vector4d(Vector4d v1) {
        super(v1);
    }

    public Vector4d(Tuple4d t1) {
        super(t1);
    }

    public Vector4d(Tuple4f t1) {
        super(t1);
    }

    public Vector4d() {
    }

    public Vector4d(Tuple3d t1) {
        super(t1.x, t1.y, t1.z, 0.0);
    }

    public final void set(Tuple3d t1) {
        this.set(t1.x, t1.y, t1.z, 0.0);
    }

    public final double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
    }

    public final double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public final double dot(Vector4d v1) {
        return this.x * v1.x + this.y * v1.y + this.z * v1.z + this.w * v1.w;
    }

    public final void normalize(Vector4d v1) {
        this.set(v1);
        this.normalize();
    }

    public final void normalize() {
        double d = this.length();
        this.x /= d;
        this.y /= d;
        this.z /= d;
        this.w /= d;
    }

    public final double angle(Vector4d v1) {
        double d = this.dot(v1);
        double v1_length = v1.length();
        double v_length = this.length();
        return Math.acos(d / v1_length / v_length);
    }
}

