// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class AxisAngle4f implements Serializable, Cloneable
{
    static final long serialVersionUID = -163246355858070601L;
    public float x;
    public float y;
    public float z;
    public float angle;
    static final double EPS = 1.0E-6;
    
    public AxisAngle4f(final float x, final float y, final float z, final float angle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
    }
    
    public AxisAngle4f(final float[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
        this.angle = array[3];
    }
    
    public AxisAngle4f(final AxisAngle4f axisAngle4f) {
        this.x = axisAngle4f.x;
        this.y = axisAngle4f.y;
        this.z = axisAngle4f.z;
        this.angle = axisAngle4f.angle;
    }
    
    public AxisAngle4f(final AxisAngle4d axisAngle4d) {
        this.x = (float)axisAngle4d.x;
        this.y = (float)axisAngle4d.y;
        this.z = (float)axisAngle4d.z;
        this.angle = (float)axisAngle4d.angle;
    }
    
    public AxisAngle4f(final Vector3f vector3f, final float angle) {
        this.x = vector3f.x;
        this.y = vector3f.y;
        this.z = vector3f.z;
        this.angle = angle;
    }
    
    public AxisAngle4f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 1.0f;
        this.angle = 0.0f;
    }
    
    public final void set(final float x, final float y, final float z, final float angle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
    }
    
    public final void set(final float[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
        this.angle = array[3];
    }
    
    public final void set(final AxisAngle4f axisAngle4f) {
        this.x = axisAngle4f.x;
        this.y = axisAngle4f.y;
        this.z = axisAngle4f.z;
        this.angle = axisAngle4f.angle;
    }
    
    public final void set(final AxisAngle4d axisAngle4d) {
        this.x = (float)axisAngle4d.x;
        this.y = (float)axisAngle4d.y;
        this.z = (float)axisAngle4d.z;
        this.angle = (float)axisAngle4d.angle;
    }
    
    public final void set(final Vector3f vector3f, final float angle) {
        this.x = vector3f.x;
        this.y = vector3f.y;
        this.z = vector3f.z;
        this.angle = angle;
    }
    
    public final void get(final float[] array) {
        array[0] = this.x;
        array[1] = this.y;
        array[2] = this.z;
        array[3] = this.angle;
    }
    
    public final void set(final Quat4f quat4f) {
        final double a = quat4f.x * quat4f.x + quat4f.y * quat4f.y + quat4f.z * quat4f.z;
        if (a > 1.0E-6) {
            final double sqrt = Math.sqrt(a);
            final double n = 1.0 / sqrt;
            this.x = (float)(quat4f.x * n);
            this.y = (float)(quat4f.y * n);
            this.z = (float)(quat4f.z * n);
            this.angle = (float)(2.0 * Math.atan2(sqrt, quat4f.w));
        }
        else {
            this.x = 0.0f;
            this.y = 1.0f;
            this.z = 0.0f;
            this.angle = 0.0f;
        }
    }
    
    public final void set(final Quat4d quat4d) {
        final double a = quat4d.x * quat4d.x + quat4d.y * quat4d.y + quat4d.z * quat4d.z;
        if (a > 1.0E-6) {
            final double sqrt = Math.sqrt(a);
            final double n = 1.0 / sqrt;
            this.x = (float)(quat4d.x * n);
            this.y = (float)(quat4d.y * n);
            this.z = (float)(quat4d.z * n);
            this.angle = (float)(2.0 * Math.atan2(sqrt, quat4d.w));
        }
        else {
            this.x = 0.0f;
            this.y = 1.0f;
            this.z = 0.0f;
            this.angle = 0.0f;
        }
    }
    
    public final void set(final Matrix4f matrix4f) {
        final Matrix3f matrix3f = new Matrix3f();
        matrix4f.get(matrix3f);
        this.x = matrix3f.m21 - matrix3f.m12;
        this.y = matrix3f.m02 - matrix3f.m20;
        this.z = matrix3f.m10 - matrix3f.m01;
        final double a = this.x * this.x + this.y * this.y + this.z * this.z;
        if (a > 1.0E-6) {
            final double sqrt = Math.sqrt(a);
            this.angle = (float)Math.atan2(0.5 * sqrt, 0.5 * (matrix3f.m00 + matrix3f.m11 + matrix3f.m22 - 1.0));
            final double n = 1.0 / sqrt;
            this.x *= (float)n;
            this.y *= (float)n;
            this.z *= (float)n;
        }
        else {
            this.x = 0.0f;
            this.y = 1.0f;
            this.z = 0.0f;
            this.angle = 0.0f;
        }
    }
    
    public final void set(final Matrix4d matrix4d) {
        final Matrix3d matrix3d = new Matrix3d();
        matrix4d.get(matrix3d);
        this.x = (float)(matrix3d.m21 - matrix3d.m12);
        this.y = (float)(matrix3d.m02 - matrix3d.m20);
        this.z = (float)(matrix3d.m10 - matrix3d.m01);
        final double a = this.x * this.x + this.y * this.y + this.z * this.z;
        if (a > 1.0E-6) {
            final double sqrt = Math.sqrt(a);
            this.angle = (float)Math.atan2(0.5 * sqrt, 0.5 * (matrix3d.m00 + matrix3d.m11 + matrix3d.m22 - 1.0));
            final double n = 1.0 / sqrt;
            this.x *= (float)n;
            this.y *= (float)n;
            this.z *= (float)n;
        }
        else {
            this.x = 0.0f;
            this.y = 1.0f;
            this.z = 0.0f;
            this.angle = 0.0f;
        }
    }
    
    public final void set(final Matrix3f matrix3f) {
        this.x = matrix3f.m21 - matrix3f.m12;
        this.y = matrix3f.m02 - matrix3f.m20;
        this.z = matrix3f.m10 - matrix3f.m01;
        final double a = this.x * this.x + this.y * this.y + this.z * this.z;
        if (a > 1.0E-6) {
            final double sqrt = Math.sqrt(a);
            this.angle = (float)Math.atan2(0.5 * sqrt, 0.5 * (matrix3f.m00 + matrix3f.m11 + matrix3f.m22 - 1.0));
            final double n = 1.0 / sqrt;
            this.x *= (float)n;
            this.y *= (float)n;
            this.z *= (float)n;
        }
        else {
            this.x = 0.0f;
            this.y = 1.0f;
            this.z = 0.0f;
            this.angle = 0.0f;
        }
    }
    
    public final void set(final Matrix3d matrix3d) {
        this.x = (float)(matrix3d.m21 - matrix3d.m12);
        this.y = (float)(matrix3d.m02 - matrix3d.m20);
        this.z = (float)(matrix3d.m10 - matrix3d.m01);
        final double a = this.x * this.x + this.y * this.y + this.z * this.z;
        if (a > 1.0E-6) {
            final double sqrt = Math.sqrt(a);
            this.angle = (float)Math.atan2(0.5 * sqrt, 0.5 * (matrix3d.m00 + matrix3d.m11 + matrix3d.m22 - 1.0));
            final double n = 1.0 / sqrt;
            this.x *= (float)n;
            this.y *= (float)n;
            this.z *= (float)n;
        }
        else {
            this.x = 0.0f;
            this.y = 1.0f;
            this.z = 0.0f;
            this.angle = 0.0f;
        }
    }
    
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.angle + ")";
    }
    
    public boolean equals(final AxisAngle4f axisAngle4f) {
        try {
            return this.x == axisAngle4f.x && this.y == axisAngle4f.y && this.z == axisAngle4f.z && this.angle == axisAngle4f.angle;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o) {
        try {
            final AxisAngle4f axisAngle4f = (AxisAngle4f)o;
            return this.x == axisAngle4f.x && this.y == axisAngle4f.y && this.z == axisAngle4f.z && this.angle == axisAngle4f.angle;
        }
        catch (NullPointerException ex) {
            return false;
        }
        catch (ClassCastException ex2) {
            return false;
        }
    }
    
    public boolean epsilonEquals(final AxisAngle4f axisAngle4f, final float n) {
        final float n2 = this.x - axisAngle4f.x;
        if (((n2 < 0.0f) ? (-n2) : n2) > n) {
            return false;
        }
        final float n3 = this.y - axisAngle4f.y;
        if (((n3 < 0.0f) ? (-n3) : n3) > n) {
            return false;
        }
        final float n4 = this.z - axisAngle4f.z;
        if (((n4 < 0.0f) ? (-n4) : n4) > n) {
            return false;
        }
        final float n5 = this.angle - axisAngle4f.angle;
        return ((n5 < 0.0f) ? (-n5) : n5) <= n;
    }
    
    public int hashCode() {
        final long n = 31L * (31L * (31L * (31L * 1L + VecMathUtil.floatToIntBits(this.x)) + VecMathUtil.floatToIntBits(this.y)) + VecMathUtil.floatToIntBits(this.z)) + VecMathUtil.floatToIntBits(this.angle);
        return (int)(n ^ n >> 32);
    }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }
}
