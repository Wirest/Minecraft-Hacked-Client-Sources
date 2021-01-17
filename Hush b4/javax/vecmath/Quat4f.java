// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Quat4f extends Tuple4f implements Serializable
{
    static final long serialVersionUID = 2675933778405442383L;
    static final double EPS = 1.0E-6;
    static final double EPS2 = 1.0E-30;
    static final double PIO2 = 1.57079632679;
    
    public Quat4f(final float n, final float n2, final float n3, final float n4) {
        final float n5 = (float)(1.0 / Math.sqrt(n * n + n2 * n2 + n3 * n3 + n4 * n4));
        this.x = n * n5;
        this.y = n2 * n5;
        this.z = n3 * n5;
        this.w = n4 * n5;
    }
    
    public Quat4f(final float[] array) {
        final float n = (float)(1.0 / Math.sqrt(array[0] * array[0] + array[1] * array[1] + array[2] * array[2] + array[3] * array[3]));
        this.x = array[0] * n;
        this.y = array[1] * n;
        this.z = array[2] * n;
        this.w = array[3] * n;
    }
    
    public Quat4f(final Quat4f quat4f) {
        super(quat4f);
    }
    
    public Quat4f(final Quat4d quat4d) {
        super(quat4d);
    }
    
    public Quat4f(final Tuple4f tuple4f) {
        final float n = (float)(1.0 / Math.sqrt(tuple4f.x * tuple4f.x + tuple4f.y * tuple4f.y + tuple4f.z * tuple4f.z + tuple4f.w * tuple4f.w));
        this.x = tuple4f.x * n;
        this.y = tuple4f.y * n;
        this.z = tuple4f.z * n;
        this.w = tuple4f.w * n;
    }
    
    public Quat4f(final Tuple4d tuple4d) {
        final double n = 1.0 / Math.sqrt(tuple4d.x * tuple4d.x + tuple4d.y * tuple4d.y + tuple4d.z * tuple4d.z + tuple4d.w * tuple4d.w);
        this.x = (float)(tuple4d.x * n);
        this.y = (float)(tuple4d.y * n);
        this.z = (float)(tuple4d.z * n);
        this.w = (float)(tuple4d.w * n);
    }
    
    public Quat4f() {
    }
    
    public final void conjugate(final Quat4f quat4f) {
        this.x = -quat4f.x;
        this.y = -quat4f.y;
        this.z = -quat4f.z;
        this.w = quat4f.w;
    }
    
    public final void conjugate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }
    
    public final void mul(final Quat4f quat4f, final Quat4f quat4f2) {
        if (this != quat4f && this != quat4f2) {
            this.w = quat4f.w * quat4f2.w - quat4f.x * quat4f2.x - quat4f.y * quat4f2.y - quat4f.z * quat4f2.z;
            this.x = quat4f.w * quat4f2.x + quat4f2.w * quat4f.x + quat4f.y * quat4f2.z - quat4f.z * quat4f2.y;
            this.y = quat4f.w * quat4f2.y + quat4f2.w * quat4f.y - quat4f.x * quat4f2.z + quat4f.z * quat4f2.x;
            this.z = quat4f.w * quat4f2.z + quat4f2.w * quat4f.z + quat4f.x * quat4f2.y - quat4f.y * quat4f2.x;
        }
        else {
            final float w = quat4f.w * quat4f2.w - quat4f.x * quat4f2.x - quat4f.y * quat4f2.y - quat4f.z * quat4f2.z;
            final float x = quat4f.w * quat4f2.x + quat4f2.w * quat4f.x + quat4f.y * quat4f2.z - quat4f.z * quat4f2.y;
            final float y = quat4f.w * quat4f2.y + quat4f2.w * quat4f.y - quat4f.x * quat4f2.z + quat4f.z * quat4f2.x;
            this.z = quat4f.w * quat4f2.z + quat4f2.w * quat4f.z + quat4f.x * quat4f2.y - quat4f.y * quat4f2.x;
            this.w = w;
            this.x = x;
            this.y = y;
        }
    }
    
    public final void mul(final Quat4f quat4f) {
        final float w = this.w * quat4f.w - this.x * quat4f.x - this.y * quat4f.y - this.z * quat4f.z;
        final float x = this.w * quat4f.x + quat4f.w * this.x + this.y * quat4f.z - this.z * quat4f.y;
        final float y = this.w * quat4f.y + quat4f.w * this.y - this.x * quat4f.z + this.z * quat4f.x;
        this.z = this.w * quat4f.z + quat4f.w * this.z + this.x * quat4f.y - this.y * quat4f.x;
        this.w = w;
        this.x = x;
        this.y = y;
    }
    
    public final void mulInverse(final Quat4f quat4f, final Quat4f quat4f2) {
        final Quat4f quat4f3 = new Quat4f(quat4f2);
        quat4f3.inverse();
        this.mul(quat4f, quat4f3);
    }
    
    public final void mulInverse(final Quat4f quat4f) {
        final Quat4f quat4f2 = new Quat4f(quat4f);
        quat4f2.inverse();
        this.mul(quat4f2);
    }
    
    public final void inverse(final Quat4f quat4f) {
        final float n = 1.0f / (quat4f.w * quat4f.w + quat4f.x * quat4f.x + quat4f.y * quat4f.y + quat4f.z * quat4f.z);
        this.w = n * quat4f.w;
        this.x = -n * quat4f.x;
        this.y = -n * quat4f.y;
        this.z = -n * quat4f.z;
    }
    
    public final void inverse() {
        final float n = 1.0f / (this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z);
        this.w *= n;
        this.x *= -n;
        this.y *= -n;
        this.z *= -n;
    }
    
    public final void normalize(final Quat4f quat4f) {
        final float n = quat4f.x * quat4f.x + quat4f.y * quat4f.y + quat4f.z * quat4f.z + quat4f.w * quat4f.w;
        if (n > 0.0f) {
            final float n2 = 1.0f / (float)Math.sqrt(n);
            this.x = n2 * quat4f.x;
            this.y = n2 * quat4f.y;
            this.z = n2 * quat4f.z;
            this.w = n2 * quat4f.w;
        }
        else {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 0.0f;
            this.w = 0.0f;
        }
    }
    
    public final void normalize() {
        final float n = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
        if (n > 0.0f) {
            final float n2 = 1.0f / (float)Math.sqrt(n);
            this.x *= n2;
            this.y *= n2;
            this.z *= n2;
            this.w *= n2;
        }
        else {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 0.0f;
            this.w = 0.0f;
        }
    }
    
    public final void set(final Matrix4f matrix4f) {
        final float n = 0.25f * (matrix4f.m00 + matrix4f.m11 + matrix4f.m22 + matrix4f.m33);
        if (n < 0.0f) {
            this.w = 0.0f;
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        if (n >= 1.0E-30) {
            this.w = (float)Math.sqrt(n);
            final float n2 = 0.25f / this.w;
            this.x = (matrix4f.m21 - matrix4f.m12) * n2;
            this.y = (matrix4f.m02 - matrix4f.m20) * n2;
            this.z = (matrix4f.m10 - matrix4f.m01) * n2;
            return;
        }
        this.w = 0.0f;
        final float n3 = -0.5f * (matrix4f.m11 + matrix4f.m22);
        if (n3 < 0.0f) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        if (n3 >= 1.0E-30) {
            this.x = (float)Math.sqrt(n3);
            final float n4 = 1.0f / (2.0f * this.x);
            this.y = matrix4f.m10 * n4;
            this.z = matrix4f.m20 * n4;
            return;
        }
        this.x = 0.0f;
        final float n5 = 0.5f * (1.0f - matrix4f.m22);
        if (n5 >= 1.0E-30) {
            this.y = (float)Math.sqrt(n5);
            this.z = matrix4f.m21 / (2.0f * this.y);
            return;
        }
        this.y = 0.0f;
        this.z = 1.0f;
    }
    
    public final void set(final Matrix4d matrix4d) {
        final double a = 0.25 * (matrix4d.m00 + matrix4d.m11 + matrix4d.m22 + matrix4d.m33);
        if (a < 0.0) {
            this.w = 0.0f;
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        if (a >= 1.0E-30) {
            this.w = (float)Math.sqrt(a);
            final double n = 0.25 / this.w;
            this.x = (float)((matrix4d.m21 - matrix4d.m12) * n);
            this.y = (float)((matrix4d.m02 - matrix4d.m20) * n);
            this.z = (float)((matrix4d.m10 - matrix4d.m01) * n);
            return;
        }
        this.w = 0.0f;
        final double a2 = -0.5 * (matrix4d.m11 + matrix4d.m22);
        if (a2 < 0.0) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        if (a2 >= 1.0E-30) {
            this.x = (float)Math.sqrt(a2);
            final double n2 = 0.5 / this.x;
            this.y = (float)(matrix4d.m10 * n2);
            this.z = (float)(matrix4d.m20 * n2);
            return;
        }
        this.x = 0.0f;
        final double a3 = 0.5 * (1.0 - matrix4d.m22);
        if (a3 >= 1.0E-30) {
            this.y = (float)Math.sqrt(a3);
            this.z = (float)(matrix4d.m21 / (2.0 * this.y));
            return;
        }
        this.y = 0.0f;
        this.z = 1.0f;
    }
    
    public final void set(final Matrix3f matrix3f) {
        final float n = 0.25f * (matrix3f.m00 + matrix3f.m11 + matrix3f.m22 + 1.0f);
        if (n < 0.0f) {
            this.w = 0.0f;
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        if (n >= 1.0E-30) {
            this.w = (float)Math.sqrt(n);
            final float n2 = 0.25f / this.w;
            this.x = (matrix3f.m21 - matrix3f.m12) * n2;
            this.y = (matrix3f.m02 - matrix3f.m20) * n2;
            this.z = (matrix3f.m10 - matrix3f.m01) * n2;
            return;
        }
        this.w = 0.0f;
        final float n3 = -0.5f * (matrix3f.m11 + matrix3f.m22);
        if (n3 < 0.0f) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        if (n3 >= 1.0E-30) {
            this.x = (float)Math.sqrt(n3);
            final float n4 = 0.5f / this.x;
            this.y = matrix3f.m10 * n4;
            this.z = matrix3f.m20 * n4;
            return;
        }
        this.x = 0.0f;
        final float n5 = 0.5f * (1.0f - matrix3f.m22);
        if (n5 >= 1.0E-30) {
            this.y = (float)Math.sqrt(n5);
            this.z = matrix3f.m21 / (2.0f * this.y);
            return;
        }
        this.y = 0.0f;
        this.z = 1.0f;
    }
    
    public final void set(final Matrix3d matrix3d) {
        final double a = 0.25 * (matrix3d.m00 + matrix3d.m11 + matrix3d.m22 + 1.0);
        if (a < 0.0) {
            this.w = 0.0f;
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        if (a >= 1.0E-30) {
            this.w = (float)Math.sqrt(a);
            final double n = 0.25 / this.w;
            this.x = (float)((matrix3d.m21 - matrix3d.m12) * n);
            this.y = (float)((matrix3d.m02 - matrix3d.m20) * n);
            this.z = (float)((matrix3d.m10 - matrix3d.m01) * n);
            return;
        }
        this.w = 0.0f;
        final double a2 = -0.5 * (matrix3d.m11 + matrix3d.m22);
        if (a2 < 0.0) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        if (a2 >= 1.0E-30) {
            this.x = (float)Math.sqrt(a2);
            final double n2 = 0.5 / this.x;
            this.y = (float)(matrix3d.m10 * n2);
            this.z = (float)(matrix3d.m20 * n2);
            return;
        }
        this.x = 0.0f;
        final double a3 = 0.5 * (1.0 - matrix3d.m22);
        if (a3 >= 1.0E-30) {
            this.y = (float)Math.sqrt(a3);
            this.z = (float)(matrix3d.m21 / (2.0 * this.y));
            return;
        }
        this.y = 0.0f;
        this.z = 1.0f;
    }
    
    public final void set(final AxisAngle4f axisAngle4f) {
        final float n = (float)Math.sqrt(axisAngle4f.x * axisAngle4f.x + axisAngle4f.y * axisAngle4f.y + axisAngle4f.z * axisAngle4f.z);
        if (n < 1.0E-6) {
            this.w = 0.0f;
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 0.0f;
        }
        else {
            final float n2 = 1.0f / n;
            final float n3 = (float)Math.sin(axisAngle4f.angle / 2.0);
            this.w = (float)Math.cos(axisAngle4f.angle / 2.0);
            this.x = axisAngle4f.x * n2 * n3;
            this.y = axisAngle4f.y * n2 * n3;
            this.z = axisAngle4f.z * n2 * n3;
        }
    }
    
    public final void set(final AxisAngle4d axisAngle4d) {
        final float n = (float)(1.0 / Math.sqrt(axisAngle4d.x * axisAngle4d.x + axisAngle4d.y * axisAngle4d.y + axisAngle4d.z * axisAngle4d.z));
        if (n < 1.0E-6) {
            this.w = 0.0f;
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 0.0f;
        }
        else {
            final float n2 = 1.0f / n;
            final float n3 = (float)Math.sin(axisAngle4d.angle / 2.0);
            this.w = (float)Math.cos(axisAngle4d.angle / 2.0);
            this.x = (float)axisAngle4d.x * n2 * n3;
            this.y = (float)axisAngle4d.y * n2 * n3;
            this.z = (float)axisAngle4d.z * n2 * n3;
        }
    }
    
    public final void interpolate(final Quat4f quat4f, final float n) {
        double a = this.x * quat4f.x + this.y * quat4f.y + this.z * quat4f.z + this.w * quat4f.w;
        if (a < 0.0) {
            quat4f.x = -quat4f.x;
            quat4f.y = -quat4f.y;
            quat4f.z = -quat4f.z;
            quat4f.w = -quat4f.w;
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
        this.w = (float)(n2 * this.w + n3 * quat4f.w);
        this.x = (float)(n2 * this.x + n3 * quat4f.x);
        this.y = (float)(n2 * this.y + n3 * quat4f.y);
        this.z = (float)(n2 * this.z + n3 * quat4f.z);
    }
    
    public final void interpolate(final Quat4f quat4f, final Quat4f quat4f2, final float n) {
        double a = quat4f2.x * quat4f.x + quat4f2.y * quat4f.y + quat4f2.z * quat4f.z + quat4f2.w * quat4f.w;
        if (a < 0.0) {
            quat4f.x = -quat4f.x;
            quat4f.y = -quat4f.y;
            quat4f.z = -quat4f.z;
            quat4f.w = -quat4f.w;
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
        this.w = (float)(n2 * quat4f.w + n3 * quat4f2.w);
        this.x = (float)(n2 * quat4f.x + n3 * quat4f2.x);
        this.y = (float)(n2 * quat4f.y + n3 * quat4f2.y);
        this.z = (float)(n2 * quat4f.z + n3 * quat4f2.z);
    }
}
