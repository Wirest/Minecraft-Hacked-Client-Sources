// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Quat4d extends Tuple4d implements Serializable
{
    static final long serialVersionUID = 7577479888820201099L;
    static final double EPS = 1.0E-6;
    static final double EPS2 = 1.0E-30;
    static final double PIO2 = 1.57079632679;
    
    public Quat4d(final double n, final double n2, final double n3, final double n4) {
        final double n5 = 1.0 / Math.sqrt(n * n + n2 * n2 + n3 * n3 + n4 * n4);
        this.x = n * n5;
        this.y = n2 * n5;
        this.z = n3 * n5;
        this.w = n4 * n5;
    }
    
    public Quat4d(final double[] array) {
        final double n = 1.0 / Math.sqrt(array[0] * array[0] + array[1] * array[1] + array[2] * array[2] + array[3] * array[3]);
        this.x = array[0] * n;
        this.y = array[1] * n;
        this.z = array[2] * n;
        this.w = array[3] * n;
    }
    
    public Quat4d(final Quat4d quat4d) {
        super(quat4d);
    }
    
    public Quat4d(final Quat4f quat4f) {
        super(quat4f);
    }
    
    public Quat4d(final Tuple4f tuple4f) {
        final double n = 1.0 / Math.sqrt(tuple4f.x * tuple4f.x + tuple4f.y * tuple4f.y + tuple4f.z * tuple4f.z + tuple4f.w * tuple4f.w);
        this.x = tuple4f.x * n;
        this.y = tuple4f.y * n;
        this.z = tuple4f.z * n;
        this.w = tuple4f.w * n;
    }
    
    public Quat4d(final Tuple4d tuple4d) {
        final double n = 1.0 / Math.sqrt(tuple4d.x * tuple4d.x + tuple4d.y * tuple4d.y + tuple4d.z * tuple4d.z + tuple4d.w * tuple4d.w);
        this.x = tuple4d.x * n;
        this.y = tuple4d.y * n;
        this.z = tuple4d.z * n;
        this.w = tuple4d.w * n;
    }
    
    public Quat4d() {
    }
    
    public final void conjugate(final Quat4d quat4d) {
        this.x = -quat4d.x;
        this.y = -quat4d.y;
        this.z = -quat4d.z;
        this.w = quat4d.w;
    }
    
    public final void conjugate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }
    
    public final void mul(final Quat4d quat4d, final Quat4d quat4d2) {
        if (this != quat4d && this != quat4d2) {
            this.w = quat4d.w * quat4d2.w - quat4d.x * quat4d2.x - quat4d.y * quat4d2.y - quat4d.z * quat4d2.z;
            this.x = quat4d.w * quat4d2.x + quat4d2.w * quat4d.x + quat4d.y * quat4d2.z - quat4d.z * quat4d2.y;
            this.y = quat4d.w * quat4d2.y + quat4d2.w * quat4d.y - quat4d.x * quat4d2.z + quat4d.z * quat4d2.x;
            this.z = quat4d.w * quat4d2.z + quat4d2.w * quat4d.z + quat4d.x * quat4d2.y - quat4d.y * quat4d2.x;
        }
        else {
            final double w = quat4d.w * quat4d2.w - quat4d.x * quat4d2.x - quat4d.y * quat4d2.y - quat4d.z * quat4d2.z;
            final double x = quat4d.w * quat4d2.x + quat4d2.w * quat4d.x + quat4d.y * quat4d2.z - quat4d.z * quat4d2.y;
            final double y = quat4d.w * quat4d2.y + quat4d2.w * quat4d.y - quat4d.x * quat4d2.z + quat4d.z * quat4d2.x;
            this.z = quat4d.w * quat4d2.z + quat4d2.w * quat4d.z + quat4d.x * quat4d2.y - quat4d.y * quat4d2.x;
            this.w = w;
            this.x = x;
            this.y = y;
        }
    }
    
    public final void mul(final Quat4d quat4d) {
        final double w = this.w * quat4d.w - this.x * quat4d.x - this.y * quat4d.y - this.z * quat4d.z;
        final double x = this.w * quat4d.x + quat4d.w * this.x + this.y * quat4d.z - this.z * quat4d.y;
        final double y = this.w * quat4d.y + quat4d.w * this.y - this.x * quat4d.z + this.z * quat4d.x;
        this.z = this.w * quat4d.z + quat4d.w * this.z + this.x * quat4d.y - this.y * quat4d.x;
        this.w = w;
        this.x = x;
        this.y = y;
    }
    
    public final void mulInverse(final Quat4d quat4d, final Quat4d quat4d2) {
        final Quat4d quat4d3 = new Quat4d(quat4d2);
        quat4d3.inverse();
        this.mul(quat4d, quat4d3);
    }
    
    public final void mulInverse(final Quat4d quat4d) {
        final Quat4d quat4d2 = new Quat4d(quat4d);
        quat4d2.inverse();
        this.mul(quat4d2);
    }
    
    public final void inverse(final Quat4d quat4d) {
        final double n = 1.0 / (quat4d.w * quat4d.w + quat4d.x * quat4d.x + quat4d.y * quat4d.y + quat4d.z * quat4d.z);
        this.w = n * quat4d.w;
        this.x = -n * quat4d.x;
        this.y = -n * quat4d.y;
        this.z = -n * quat4d.z;
    }
    
    public final void inverse() {
        final double n = 1.0 / (this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z);
        this.w *= n;
        this.x *= -n;
        this.y *= -n;
        this.z *= -n;
    }
    
    public final void normalize(final Quat4d quat4d) {
        final double a = quat4d.x * quat4d.x + quat4d.y * quat4d.y + quat4d.z * quat4d.z + quat4d.w * quat4d.w;
        if (a > 0.0) {
            final double n = 1.0 / Math.sqrt(a);
            this.x = n * quat4d.x;
            this.y = n * quat4d.y;
            this.z = n * quat4d.z;
            this.w = n * quat4d.w;
        }
        else {
            this.x = 0.0;
            this.y = 0.0;
            this.z = 0.0;
            this.w = 0.0;
        }
    }
    
    public final void normalize() {
        final double a = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
        if (a > 0.0) {
            final double n = 1.0 / Math.sqrt(a);
            this.x *= n;
            this.y *= n;
            this.z *= n;
            this.w *= n;
        }
        else {
            this.x = 0.0;
            this.y = 0.0;
            this.z = 0.0;
            this.w = 0.0;
        }
    }
    
    public final void set(final Matrix4f matrix4f) {
        final double a = 0.25 * (matrix4f.m00 + matrix4f.m11 + matrix4f.m22 + matrix4f.m33);
        if (a < 0.0) {
            this.w = 0.0;
            this.x = 0.0;
            this.y = 0.0;
            this.z = 1.0;
            return;
        }
        if (a >= 1.0E-30) {
            this.w = Math.sqrt(a);
            final double n = 0.25 / this.w;
            this.x = (matrix4f.m21 - matrix4f.m12) * n;
            this.y = (matrix4f.m02 - matrix4f.m20) * n;
            this.z = (matrix4f.m10 - matrix4f.m01) * n;
            return;
        }
        this.w = 0.0;
        final double a2 = -0.5 * (matrix4f.m11 + matrix4f.m22);
        if (a2 < 0.0) {
            this.x = 0.0;
            this.y = 0.0;
            this.z = 1.0;
            return;
        }
        if (a2 >= 1.0E-30) {
            this.x = Math.sqrt(a2);
            final double n2 = 1.0 / (2.0 * this.x);
            this.y = matrix4f.m10 * n2;
            this.z = matrix4f.m20 * n2;
            return;
        }
        this.x = 0.0;
        final double a3 = 0.5 * (1.0 - matrix4f.m22);
        if (a3 >= 1.0E-30) {
            this.y = Math.sqrt(a3);
            this.z = matrix4f.m21 / (2.0 * this.y);
            return;
        }
        this.y = 0.0;
        this.z = 1.0;
    }
    
    public final void set(final Matrix4d matrix4d) {
        final double a = 0.25 * (matrix4d.m00 + matrix4d.m11 + matrix4d.m22 + matrix4d.m33);
        if (a < 0.0) {
            this.w = 0.0;
            this.x = 0.0;
            this.y = 0.0;
            this.z = 1.0;
            return;
        }
        if (a >= 1.0E-30) {
            this.w = Math.sqrt(a);
            final double n = 0.25 / this.w;
            this.x = (matrix4d.m21 - matrix4d.m12) * n;
            this.y = (matrix4d.m02 - matrix4d.m20) * n;
            this.z = (matrix4d.m10 - matrix4d.m01) * n;
            return;
        }
        this.w = 0.0;
        final double a2 = -0.5 * (matrix4d.m11 + matrix4d.m22);
        if (a2 < 0.0) {
            this.x = 0.0;
            this.y = 0.0;
            this.z = 1.0;
            return;
        }
        if (a2 >= 1.0E-30) {
            this.x = Math.sqrt(a2);
            final double n2 = 0.5 / this.x;
            this.y = matrix4d.m10 * n2;
            this.z = matrix4d.m20 * n2;
            return;
        }
        this.x = 0.0;
        final double a3 = 0.5 * (1.0 - matrix4d.m22);
        if (a3 >= 1.0E-30) {
            this.y = Math.sqrt(a3);
            this.z = matrix4d.m21 / (2.0 * this.y);
            return;
        }
        this.y = 0.0;
        this.z = 1.0;
    }
    
    public final void set(final Matrix3f matrix3f) {
        final double a = 0.25 * (matrix3f.m00 + matrix3f.m11 + matrix3f.m22 + 1.0);
        if (a < 0.0) {
            this.w = 0.0;
            this.x = 0.0;
            this.y = 0.0;
            this.z = 1.0;
            return;
        }
        if (a >= 1.0E-30) {
            this.w = Math.sqrt(a);
            final double n = 0.25 / this.w;
            this.x = (matrix3f.m21 - matrix3f.m12) * n;
            this.y = (matrix3f.m02 - matrix3f.m20) * n;
            this.z = (matrix3f.m10 - matrix3f.m01) * n;
            return;
        }
        this.w = 0.0;
        final double a2 = -0.5 * (matrix3f.m11 + matrix3f.m22);
        if (a2 < 0.0) {
            this.x = 0.0;
            this.y = 0.0;
            this.z = 1.0;
            return;
        }
        if (a2 >= 1.0E-30) {
            this.x = Math.sqrt(a2);
            final double n2 = 0.5 / this.x;
            this.y = matrix3f.m10 * n2;
            this.z = matrix3f.m20 * n2;
            return;
        }
        this.x = 0.0;
        final double a3 = 0.5 * (1.0 - matrix3f.m22);
        if (a3 >= 1.0E-30) {
            this.y = Math.sqrt(a3);
            this.z = matrix3f.m21 / (2.0 * this.y);
        }
        this.y = 0.0;
        this.z = 1.0;
    }
    
    public final void set(final Matrix3d matrix3d) {
        final double a = 0.25 * (matrix3d.m00 + matrix3d.m11 + matrix3d.m22 + 1.0);
        if (a < 0.0) {
            this.w = 0.0;
            this.x = 0.0;
            this.y = 0.0;
            this.z = 1.0;
            return;
        }
        if (a >= 1.0E-30) {
            this.w = Math.sqrt(a);
            final double n = 0.25 / this.w;
            this.x = (matrix3d.m21 - matrix3d.m12) * n;
            this.y = (matrix3d.m02 - matrix3d.m20) * n;
            this.z = (matrix3d.m10 - matrix3d.m01) * n;
            return;
        }
        this.w = 0.0;
        final double a2 = -0.5 * (matrix3d.m11 + matrix3d.m22);
        if (a2 < 0.0) {
            this.x = 0.0;
            this.y = 0.0;
            this.z = 1.0;
            return;
        }
        if (a2 >= 1.0E-30) {
            this.x = Math.sqrt(a2);
            final double n2 = 0.5 / this.x;
            this.y = matrix3d.m10 * n2;
            this.z = matrix3d.m20 * n2;
            return;
        }
        this.x = 0.0;
        final double a3 = 0.5 * (1.0 - matrix3d.m22);
        if (a3 >= 1.0E-30) {
            this.y = Math.sqrt(a3);
            this.z = matrix3d.m21 / (2.0 * this.y);
            return;
        }
        this.y = 0.0;
        this.z = 1.0;
    }
    
    public final void set(final AxisAngle4f axisAngle4f) {
        final double sqrt = Math.sqrt(axisAngle4f.x * axisAngle4f.x + axisAngle4f.y * axisAngle4f.y + axisAngle4f.z * axisAngle4f.z);
        if (sqrt < 1.0E-6) {
            this.w = 0.0;
            this.x = 0.0;
            this.y = 0.0;
            this.z = 0.0;
        }
        else {
            final double sin = Math.sin(axisAngle4f.angle / 2.0);
            final double n = 1.0 / sqrt;
            this.w = Math.cos(axisAngle4f.angle / 2.0);
            this.x = axisAngle4f.x * n * sin;
            this.y = axisAngle4f.y * n * sin;
            this.z = axisAngle4f.z * n * sin;
        }
    }
    
    public final void set(final AxisAngle4d axisAngle4d) {
        final double sqrt = Math.sqrt(axisAngle4d.x * axisAngle4d.x + axisAngle4d.y * axisAngle4d.y + axisAngle4d.z * axisAngle4d.z);
        if (sqrt < 1.0E-6) {
            this.w = 0.0;
            this.x = 0.0;
            this.y = 0.0;
            this.z = 0.0;
        }
        else {
            final double n = 1.0 / sqrt;
            final double sin = Math.sin(axisAngle4d.angle / 2.0);
            this.w = Math.cos(axisAngle4d.angle / 2.0);
            this.x = axisAngle4d.x * n * sin;
            this.y = axisAngle4d.y * n * sin;
            this.z = axisAngle4d.z * n * sin;
        }
    }
    
    public final void interpolate(final Quat4d quat4d, final double n) {
        double a = this.x * quat4d.x + this.y * quat4d.y + this.z * quat4d.z + this.w * quat4d.w;
        if (a < 0.0) {
            quat4d.x = -quat4d.x;
            quat4d.y = -quat4d.y;
            quat4d.z = -quat4d.z;
            quat4d.w = -quat4d.w;
            a = -a;
        }
        double n2;
        double n3;
        if (1.0 - a > 1.0E-6) {
            final double acos = Math.acos(a);
            final double sin = Math.sin(acos);
            n2 = Math.sin((1.0 - n) * acos) / sin;
            n3 = Math.sin(n * acos) / sin;
        }
        else {
            n2 = 1.0 - n;
            n3 = n;
        }
        this.w = n2 * this.w + n3 * quat4d.w;
        this.x = n2 * this.x + n3 * quat4d.x;
        this.y = n2 * this.y + n3 * quat4d.y;
        this.z = n2 * this.z + n3 * quat4d.z;
    }
    
    public final void interpolate(final Quat4d quat4d, final Quat4d quat4d2, final double n) {
        double a = quat4d2.x * quat4d.x + quat4d2.y * quat4d.y + quat4d2.z * quat4d.z + quat4d2.w * quat4d.w;
        if (a < 0.0) {
            quat4d.x = -quat4d.x;
            quat4d.y = -quat4d.y;
            quat4d.z = -quat4d.z;
            quat4d.w = -quat4d.w;
            a = -a;
        }
        double n2;
        double n3;
        if (1.0 - a > 1.0E-6) {
            final double acos = Math.acos(a);
            final double sin = Math.sin(acos);
            n2 = Math.sin((1.0 - n) * acos) / sin;
            n3 = Math.sin(n * acos) / sin;
        }
        else {
            n2 = 1.0 - n;
            n3 = n;
        }
        this.w = n2 * quat4d.w + n3 * quat4d2.w;
        this.x = n2 * quat4d.x + n3 * quat4d2.x;
        this.y = n2 * quat4d.y + n3 * quat4d2.y;
        this.z = n2 * quat4d.z + n3 * quat4d2.z;
    }
}
