
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4d;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class AxisAngle4f
        implements Serializable {
    public float x;
    public float y;
    public float z;
    public float angle;

    public AxisAngle4f(float x, float y2, float z, float angle) {
        this.set(x, y2, z, angle);
    }

    public AxisAngle4f(float[] a2) {
        this.set(a2);
    }

    public AxisAngle4f(AxisAngle4f a1) {
        this.set(a1);
    }

    public AxisAngle4f(AxisAngle4d a1) {
        this.set(a1);
    }

    public AxisAngle4f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 1.0f;
        this.angle = 0.0f;
    }

    public AxisAngle4f(Vector3f axis, float angle) {
        this.x = axis.x;
        this.y = axis.y;
        this.z = axis.z;
        this.angle = angle;
    }

    public final void set(Vector3f axis, float angle) {
        this.x = axis.x;
        this.y = axis.y;
        this.z = axis.z;
        this.angle = angle;
    }

    public final void set(float x, float y2, float z, float angle) {
        this.x = x;
        this.y = y2;
        this.z = z;
        this.angle = angle;
    }

    public final void set(float[] a2) {
        this.x = a2[0];
        this.y = a2[1];
        this.z = a2[2];
        this.angle = a2[3];
    }

    public final void set(AxisAngle4f a1) {
        this.x = a1.x;
        this.y = a1.y;
        this.z = a1.z;
        this.angle = a1.angle;
    }

    public final void set(AxisAngle4d a1) {
        this.x = (float) a1.x;
        this.y = (float) a1.y;
        this.z = (float) a1.z;
        this.angle = (float) a1.angle;
    }

    public final void get(float[] a2) {
        a2[0] = this.x;
        a2[1] = this.y;
        a2[2] = this.z;
        a2[3] = this.angle;
    }

    public final void set(Matrix4f m1) {
        this.setFromMat(m1.m00, m1.m01, m1.m02, m1.m10, m1.m11, m1.m12, m1.m20, m1.m21, m1.m22);
    }

    public final void set(Matrix4d m1) {
        this.setFromMat(m1.m00, m1.m01, m1.m02, m1.m10, m1.m11, m1.m12, m1.m20, m1.m21, m1.m22);
    }

    public final void set(Matrix3f m1) {
        this.setFromMat(m1.m00, m1.m01, m1.m02, m1.m10, m1.m11, m1.m12, m1.m20, m1.m21, m1.m22);
    }

    public final void set(Matrix3d m1) {
        this.setFromMat(m1.m00, m1.m01, m1.m02, m1.m10, m1.m11, m1.m12, m1.m20, m1.m21, m1.m22);
    }

    public final void set(Quat4f q1) {
        this.setFromQuat(q1.x, q1.y, q1.z, q1.w);
    }

    public final void set(Quat4d q1) {
        this.setFromQuat(q1.x, q1.y, q1.z, q1.w);
    }

    private void setFromMat(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {
        double cos = (m00 + m11 + m22 - 1.0) * 0.5;
        this.x = (float) (m21 - m12);
        this.y = (float) (m02 - m20);
        this.z = (float) (m10 - m01);
        double sin = 0.5 * Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        this.angle = (float) Math.atan2(sin, cos);
    }

    private void setFromQuat(double x, double y2, double z, double w) {
        double sin_a2 = Math.sqrt(x * x + y2 * y2 + z * z);
        this.angle = (float) (2.0 * Math.atan2(sin_a2, w));
        this.x = (float) x;
        this.y = (float) y2;
        this.z = (float) z;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.angle + ")";
    }

    public boolean equals(AxisAngle4f a1) {
        return a1 != null && this.x == a1.x && this.y == a1.y && this.z == a1.z && this.angle == a1.angle;
    }

    public boolean equals(Object o1) {
        return o1 != null && o1 instanceof AxisAngle4f && this.equals((AxisAngle4f) o1);
    }

    public boolean epsilonEquals(AxisAngle4f a1, float epsilon) {
        return Math.abs(a1.x - this.x) <= epsilon && Math.abs(a1.y - this.y) <= epsilon && Math.abs(a1.z - this.z) <= epsilon && Math.abs(a1.angle - this.angle) <= epsilon;
    }

    public int hashCode() {
        return Float.floatToIntBits(this.x) ^ Float.floatToIntBits(this.y) ^ Float.floatToIntBits(this.z) ^ Float.floatToIntBits(this.angle);
    }
}

