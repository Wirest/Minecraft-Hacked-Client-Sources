
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4d;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector4d;

public class Vector4f
        extends Tuple4f
        implements Serializable {
    public Vector4f(float x, float y2, float z, float w) {
        super(x, y2, z, w);
    }

    public Vector4f(float[] v) {
        super(v);
    }

    public Vector4f(Vector4f v1) {
        super(v1);
    }

    public Vector4f(Vector4d v1) {
        super(v1);
    }

    public Vector4f(Tuple4d t1) {
        super(t1);
    }

    public Vector4f(Tuple4f t1) {
        super(t1);
    }

    public Vector4f() {
    }

    public Vector4f(Tuple3f t1) {
        super(t1.x, t1.y, t1.z, 0.0f);
    }

    public final void set(Tuple3f t1) {
        this.set(t1.x, t1.y, t1.z, 0.0f);
    }

    public final float lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
    }

    public final float length() {
        return (float) Math.sqrt(this.lengthSquared());
    }

    public final float dot(Vector4f v1) {
        return this.x * v1.x + this.y * v1.y + this.z * v1.z + this.w * v1.w;
    }

    public final void normalize(Vector4d v1) {
        this.set(v1);
        this.normalize();
    }

    public final void normalize() {
        double d = this.length();
        this.x /= (float) d;
        this.y /= (float) d;
        this.z /= (float) d;
        this.w /= (float) d;
    }

    public final float angle(Vector4f v1) {
        double d = this.dot(v1);
        double v1_length = v1.length();
        double v_length = this.length();
        return (float) Math.acos(d / v1_length / v_length);
    }
}

