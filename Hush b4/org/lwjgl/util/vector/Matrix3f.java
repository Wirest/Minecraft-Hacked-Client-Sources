// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.vector;

import java.nio.FloatBuffer;
import java.io.Serializable;

public class Matrix3f extends Matrix implements Serializable
{
    private static final long serialVersionUID = 1L;
    public float m00;
    public float m01;
    public float m02;
    public float m10;
    public float m11;
    public float m12;
    public float m20;
    public float m21;
    public float m22;
    
    public Matrix3f() {
        this.setIdentity();
    }
    
    public Matrix3f load(final Matrix3f src) {
        return load(src, this);
    }
    
    public static Matrix3f load(final Matrix3f src, Matrix3f dest) {
        if (dest == null) {
            dest = new Matrix3f();
        }
        dest.m00 = src.m00;
        dest.m10 = src.m10;
        dest.m20 = src.m20;
        dest.m01 = src.m01;
        dest.m11 = src.m11;
        dest.m21 = src.m21;
        dest.m02 = src.m02;
        dest.m12 = src.m12;
        dest.m22 = src.m22;
        return dest;
    }
    
    @Override
    public Matrix load(final FloatBuffer buf) {
        this.m00 = buf.get();
        this.m01 = buf.get();
        this.m02 = buf.get();
        this.m10 = buf.get();
        this.m11 = buf.get();
        this.m12 = buf.get();
        this.m20 = buf.get();
        this.m21 = buf.get();
        this.m22 = buf.get();
        return this;
    }
    
    @Override
    public Matrix loadTranspose(final FloatBuffer buf) {
        this.m00 = buf.get();
        this.m10 = buf.get();
        this.m20 = buf.get();
        this.m01 = buf.get();
        this.m11 = buf.get();
        this.m21 = buf.get();
        this.m02 = buf.get();
        this.m12 = buf.get();
        this.m22 = buf.get();
        return this;
    }
    
    @Override
    public Matrix store(final FloatBuffer buf) {
        buf.put(this.m00);
        buf.put(this.m01);
        buf.put(this.m02);
        buf.put(this.m10);
        buf.put(this.m11);
        buf.put(this.m12);
        buf.put(this.m20);
        buf.put(this.m21);
        buf.put(this.m22);
        return this;
    }
    
    @Override
    public Matrix storeTranspose(final FloatBuffer buf) {
        buf.put(this.m00);
        buf.put(this.m10);
        buf.put(this.m20);
        buf.put(this.m01);
        buf.put(this.m11);
        buf.put(this.m21);
        buf.put(this.m02);
        buf.put(this.m12);
        buf.put(this.m22);
        return this;
    }
    
    public static Matrix3f add(final Matrix3f left, final Matrix3f right, Matrix3f dest) {
        if (dest == null) {
            dest = new Matrix3f();
        }
        dest.m00 = left.m00 + right.m00;
        dest.m01 = left.m01 + right.m01;
        dest.m02 = left.m02 + right.m02;
        dest.m10 = left.m10 + right.m10;
        dest.m11 = left.m11 + right.m11;
        dest.m12 = left.m12 + right.m12;
        dest.m20 = left.m20 + right.m20;
        dest.m21 = left.m21 + right.m21;
        dest.m22 = left.m22 + right.m22;
        return dest;
    }
    
    public static Matrix3f sub(final Matrix3f left, final Matrix3f right, Matrix3f dest) {
        if (dest == null) {
            dest = new Matrix3f();
        }
        dest.m00 = left.m00 - right.m00;
        dest.m01 = left.m01 - right.m01;
        dest.m02 = left.m02 - right.m02;
        dest.m10 = left.m10 - right.m10;
        dest.m11 = left.m11 - right.m11;
        dest.m12 = left.m12 - right.m12;
        dest.m20 = left.m20 - right.m20;
        dest.m21 = left.m21 - right.m21;
        dest.m22 = left.m22 - right.m22;
        return dest;
    }
    
    public static Matrix3f mul(final Matrix3f left, final Matrix3f right, Matrix3f dest) {
        if (dest == null) {
            dest = new Matrix3f();
        }
        final float m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02;
        final float m2 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02;
        final float m3 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02;
        final float m4 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12;
        final float m5 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12;
        final float m6 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12;
        final float m7 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22;
        final float m8 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22;
        final float m9 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22;
        dest.m00 = m00;
        dest.m01 = m2;
        dest.m02 = m3;
        dest.m10 = m4;
        dest.m11 = m5;
        dest.m12 = m6;
        dest.m20 = m7;
        dest.m21 = m8;
        dest.m22 = m9;
        return dest;
    }
    
    public static Vector3f transform(final Matrix3f left, final Vector3f right, Vector3f dest) {
        if (dest == null) {
            dest = new Vector3f();
        }
        final float x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z;
        final float y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z;
        final float z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z;
        dest.x = x;
        dest.y = y;
        dest.z = z;
        return dest;
    }
    
    @Override
    public Matrix transpose() {
        return transpose(this, this);
    }
    
    public Matrix3f transpose(final Matrix3f dest) {
        return transpose(this, dest);
    }
    
    public static Matrix3f transpose(final Matrix3f src, Matrix3f dest) {
        if (dest == null) {
            dest = new Matrix3f();
        }
        final float m00 = src.m00;
        final float m2 = src.m10;
        final float m3 = src.m20;
        final float m4 = src.m01;
        final float m5 = src.m11;
        final float m6 = src.m21;
        final float m7 = src.m02;
        final float m8 = src.m12;
        final float m9 = src.m22;
        dest.m00 = m00;
        dest.m01 = m2;
        dest.m02 = m3;
        dest.m10 = m4;
        dest.m11 = m5;
        dest.m12 = m6;
        dest.m20 = m7;
        dest.m21 = m8;
        dest.m22 = m9;
        return dest;
    }
    
    @Override
    public float determinant() {
        final float f = this.m00 * (this.m11 * this.m22 - this.m12 * this.m21) + this.m01 * (this.m12 * this.m20 - this.m10 * this.m22) + this.m02 * (this.m10 * this.m21 - this.m11 * this.m20);
        return f;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(this.m00).append(' ').append(this.m10).append(' ').append(this.m20).append(' ').append('\n');
        buf.append(this.m01).append(' ').append(this.m11).append(' ').append(this.m21).append(' ').append('\n');
        buf.append(this.m02).append(' ').append(this.m12).append(' ').append(this.m22).append(' ').append('\n');
        return buf.toString();
    }
    
    @Override
    public Matrix invert() {
        return invert(this, this);
    }
    
    public static Matrix3f invert(final Matrix3f src, Matrix3f dest) {
        final float determinant = src.determinant();
        if (determinant != 0.0f) {
            if (dest == null) {
                dest = new Matrix3f();
            }
            final float determinant_inv = 1.0f / determinant;
            final float t00 = src.m11 * src.m22 - src.m12 * src.m21;
            final float t2 = -src.m10 * src.m22 + src.m12 * src.m20;
            final float t3 = src.m10 * src.m21 - src.m11 * src.m20;
            final float t4 = -src.m01 * src.m22 + src.m02 * src.m21;
            final float t5 = src.m00 * src.m22 - src.m02 * src.m20;
            final float t6 = -src.m00 * src.m21 + src.m01 * src.m20;
            final float t7 = src.m01 * src.m12 - src.m02 * src.m11;
            final float t8 = -src.m00 * src.m12 + src.m02 * src.m10;
            final float t9 = src.m00 * src.m11 - src.m01 * src.m10;
            dest.m00 = t00 * determinant_inv;
            dest.m11 = t5 * determinant_inv;
            dest.m22 = t9 * determinant_inv;
            dest.m01 = t4 * determinant_inv;
            dest.m10 = t2 * determinant_inv;
            dest.m20 = t3 * determinant_inv;
            dest.m02 = t7 * determinant_inv;
            dest.m12 = t8 * determinant_inv;
            dest.m21 = t6 * determinant_inv;
            return dest;
        }
        return null;
    }
    
    @Override
    public Matrix negate() {
        return this.negate(this);
    }
    
    public Matrix3f negate(final Matrix3f dest) {
        return negate(this, dest);
    }
    
    public static Matrix3f negate(final Matrix3f src, Matrix3f dest) {
        if (dest == null) {
            dest = new Matrix3f();
        }
        dest.m00 = -src.m00;
        dest.m01 = -src.m02;
        dest.m02 = -src.m01;
        dest.m10 = -src.m10;
        dest.m11 = -src.m12;
        dest.m12 = -src.m11;
        dest.m20 = -src.m20;
        dest.m21 = -src.m22;
        dest.m22 = -src.m21;
        return dest;
    }
    
    @Override
    public Matrix setIdentity() {
        return setIdentity(this);
    }
    
    public static Matrix3f setIdentity(final Matrix3f m) {
        m.m00 = 1.0f;
        m.m01 = 0.0f;
        m.m02 = 0.0f;
        m.m10 = 0.0f;
        m.m11 = 1.0f;
        m.m12 = 0.0f;
        m.m20 = 0.0f;
        m.m21 = 0.0f;
        m.m22 = 1.0f;
        return m;
    }
    
    @Override
    public Matrix setZero() {
        return setZero(this);
    }
    
    public static Matrix3f setZero(final Matrix3f m) {
        m.m00 = 0.0f;
        m.m01 = 0.0f;
        m.m02 = 0.0f;
        m.m10 = 0.0f;
        m.m11 = 0.0f;
        m.m12 = 0.0f;
        m.m20 = 0.0f;
        m.m21 = 0.0f;
        m.m22 = 0.0f;
        return m;
    }
}
