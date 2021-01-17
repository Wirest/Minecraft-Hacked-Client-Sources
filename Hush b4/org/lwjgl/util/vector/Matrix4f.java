// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.vector;

import java.nio.FloatBuffer;
import java.io.Serializable;

public class Matrix4f extends Matrix implements Serializable
{
    private static final long serialVersionUID = 1L;
    public float m00;
    public float m01;
    public float m02;
    public float m03;
    public float m10;
    public float m11;
    public float m12;
    public float m13;
    public float m20;
    public float m21;
    public float m22;
    public float m23;
    public float m30;
    public float m31;
    public float m32;
    public float m33;
    
    public Matrix4f() {
        this.setIdentity();
    }
    
    public Matrix4f(final Matrix4f src) {
        this.load(src);
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(this.m00).append(' ').append(this.m10).append(' ').append(this.m20).append(' ').append(this.m30).append('\n');
        buf.append(this.m01).append(' ').append(this.m11).append(' ').append(this.m21).append(' ').append(this.m31).append('\n');
        buf.append(this.m02).append(' ').append(this.m12).append(' ').append(this.m22).append(' ').append(this.m32).append('\n');
        buf.append(this.m03).append(' ').append(this.m13).append(' ').append(this.m23).append(' ').append(this.m33).append('\n');
        return buf.toString();
    }
    
    @Override
    public Matrix setIdentity() {
        return setIdentity(this);
    }
    
    public static Matrix4f setIdentity(final Matrix4f m) {
        m.m00 = 1.0f;
        m.m01 = 0.0f;
        m.m02 = 0.0f;
        m.m03 = 0.0f;
        m.m10 = 0.0f;
        m.m11 = 1.0f;
        m.m12 = 0.0f;
        m.m13 = 0.0f;
        m.m20 = 0.0f;
        m.m21 = 0.0f;
        m.m22 = 1.0f;
        m.m23 = 0.0f;
        m.m30 = 0.0f;
        m.m31 = 0.0f;
        m.m32 = 0.0f;
        m.m33 = 1.0f;
        return m;
    }
    
    @Override
    public Matrix setZero() {
        return setZero(this);
    }
    
    public static Matrix4f setZero(final Matrix4f m) {
        m.m00 = 0.0f;
        m.m01 = 0.0f;
        m.m02 = 0.0f;
        m.m03 = 0.0f;
        m.m10 = 0.0f;
        m.m11 = 0.0f;
        m.m12 = 0.0f;
        m.m13 = 0.0f;
        m.m20 = 0.0f;
        m.m21 = 0.0f;
        m.m22 = 0.0f;
        m.m23 = 0.0f;
        m.m30 = 0.0f;
        m.m31 = 0.0f;
        m.m32 = 0.0f;
        m.m33 = 0.0f;
        return m;
    }
    
    public Matrix4f load(final Matrix4f src) {
        return load(src, this);
    }
    
    public static Matrix4f load(final Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }
        dest.m00 = src.m00;
        dest.m01 = src.m01;
        dest.m02 = src.m02;
        dest.m03 = src.m03;
        dest.m10 = src.m10;
        dest.m11 = src.m11;
        dest.m12 = src.m12;
        dest.m13 = src.m13;
        dest.m20 = src.m20;
        dest.m21 = src.m21;
        dest.m22 = src.m22;
        dest.m23 = src.m23;
        dest.m30 = src.m30;
        dest.m31 = src.m31;
        dest.m32 = src.m32;
        dest.m33 = src.m33;
        return dest;
    }
    
    @Override
    public Matrix load(final FloatBuffer buf) {
        this.m00 = buf.get();
        this.m01 = buf.get();
        this.m02 = buf.get();
        this.m03 = buf.get();
        this.m10 = buf.get();
        this.m11 = buf.get();
        this.m12 = buf.get();
        this.m13 = buf.get();
        this.m20 = buf.get();
        this.m21 = buf.get();
        this.m22 = buf.get();
        this.m23 = buf.get();
        this.m30 = buf.get();
        this.m31 = buf.get();
        this.m32 = buf.get();
        this.m33 = buf.get();
        return this;
    }
    
    @Override
    public Matrix loadTranspose(final FloatBuffer buf) {
        this.m00 = buf.get();
        this.m10 = buf.get();
        this.m20 = buf.get();
        this.m30 = buf.get();
        this.m01 = buf.get();
        this.m11 = buf.get();
        this.m21 = buf.get();
        this.m31 = buf.get();
        this.m02 = buf.get();
        this.m12 = buf.get();
        this.m22 = buf.get();
        this.m32 = buf.get();
        this.m03 = buf.get();
        this.m13 = buf.get();
        this.m23 = buf.get();
        this.m33 = buf.get();
        return this;
    }
    
    @Override
    public Matrix store(final FloatBuffer buf) {
        buf.put(this.m00);
        buf.put(this.m01);
        buf.put(this.m02);
        buf.put(this.m03);
        buf.put(this.m10);
        buf.put(this.m11);
        buf.put(this.m12);
        buf.put(this.m13);
        buf.put(this.m20);
        buf.put(this.m21);
        buf.put(this.m22);
        buf.put(this.m23);
        buf.put(this.m30);
        buf.put(this.m31);
        buf.put(this.m32);
        buf.put(this.m33);
        return this;
    }
    
    @Override
    public Matrix storeTranspose(final FloatBuffer buf) {
        buf.put(this.m00);
        buf.put(this.m10);
        buf.put(this.m20);
        buf.put(this.m30);
        buf.put(this.m01);
        buf.put(this.m11);
        buf.put(this.m21);
        buf.put(this.m31);
        buf.put(this.m02);
        buf.put(this.m12);
        buf.put(this.m22);
        buf.put(this.m32);
        buf.put(this.m03);
        buf.put(this.m13);
        buf.put(this.m23);
        buf.put(this.m33);
        return this;
    }
    
    public Matrix store3f(final FloatBuffer buf) {
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
    
    public static Matrix4f add(final Matrix4f left, final Matrix4f right, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }
        dest.m00 = left.m00 + right.m00;
        dest.m01 = left.m01 + right.m01;
        dest.m02 = left.m02 + right.m02;
        dest.m03 = left.m03 + right.m03;
        dest.m10 = left.m10 + right.m10;
        dest.m11 = left.m11 + right.m11;
        dest.m12 = left.m12 + right.m12;
        dest.m13 = left.m13 + right.m13;
        dest.m20 = left.m20 + right.m20;
        dest.m21 = left.m21 + right.m21;
        dest.m22 = left.m22 + right.m22;
        dest.m23 = left.m23 + right.m23;
        dest.m30 = left.m30 + right.m30;
        dest.m31 = left.m31 + right.m31;
        dest.m32 = left.m32 + right.m32;
        dest.m33 = left.m33 + right.m33;
        return dest;
    }
    
    public static Matrix4f sub(final Matrix4f left, final Matrix4f right, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }
        dest.m00 = left.m00 - right.m00;
        dest.m01 = left.m01 - right.m01;
        dest.m02 = left.m02 - right.m02;
        dest.m03 = left.m03 - right.m03;
        dest.m10 = left.m10 - right.m10;
        dest.m11 = left.m11 - right.m11;
        dest.m12 = left.m12 - right.m12;
        dest.m13 = left.m13 - right.m13;
        dest.m20 = left.m20 - right.m20;
        dest.m21 = left.m21 - right.m21;
        dest.m22 = left.m22 - right.m22;
        dest.m23 = left.m23 - right.m23;
        dest.m30 = left.m30 - right.m30;
        dest.m31 = left.m31 - right.m31;
        dest.m32 = left.m32 - right.m32;
        dest.m33 = left.m33 - right.m33;
        return dest;
    }
    
    public static Matrix4f mul(final Matrix4f left, final Matrix4f right, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }
        final float m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02 + left.m30 * right.m03;
        final float m2 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02 + left.m31 * right.m03;
        final float m3 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02 + left.m32 * right.m03;
        final float m4 = left.m03 * right.m00 + left.m13 * right.m01 + left.m23 * right.m02 + left.m33 * right.m03;
        final float m5 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12 + left.m30 * right.m13;
        final float m6 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12 + left.m31 * right.m13;
        final float m7 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12 + left.m32 * right.m13;
        final float m8 = left.m03 * right.m10 + left.m13 * right.m11 + left.m23 * right.m12 + left.m33 * right.m13;
        final float m9 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22 + left.m30 * right.m23;
        final float m10 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22 + left.m31 * right.m23;
        final float m11 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22 + left.m32 * right.m23;
        final float m12 = left.m03 * right.m20 + left.m13 * right.m21 + left.m23 * right.m22 + left.m33 * right.m23;
        final float m13 = left.m00 * right.m30 + left.m10 * right.m31 + left.m20 * right.m32 + left.m30 * right.m33;
        final float m14 = left.m01 * right.m30 + left.m11 * right.m31 + left.m21 * right.m32 + left.m31 * right.m33;
        final float m15 = left.m02 * right.m30 + left.m12 * right.m31 + left.m22 * right.m32 + left.m32 * right.m33;
        final float m16 = left.m03 * right.m30 + left.m13 * right.m31 + left.m23 * right.m32 + left.m33 * right.m33;
        dest.m00 = m00;
        dest.m01 = m2;
        dest.m02 = m3;
        dest.m03 = m4;
        dest.m10 = m5;
        dest.m11 = m6;
        dest.m12 = m7;
        dest.m13 = m8;
        dest.m20 = m9;
        dest.m21 = m10;
        dest.m22 = m11;
        dest.m23 = m12;
        dest.m30 = m13;
        dest.m31 = m14;
        dest.m32 = m15;
        dest.m33 = m16;
        return dest;
    }
    
    public static Vector4f transform(final Matrix4f left, final Vector4f right, Vector4f dest) {
        if (dest == null) {
            dest = new Vector4f();
        }
        final float x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z + left.m30 * right.w;
        final float y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z + left.m31 * right.w;
        final float z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z + left.m32 * right.w;
        final float w = left.m03 * right.x + left.m13 * right.y + left.m23 * right.z + left.m33 * right.w;
        dest.x = x;
        dest.y = y;
        dest.z = z;
        dest.w = w;
        return dest;
    }
    
    @Override
    public Matrix transpose() {
        return this.transpose(this);
    }
    
    public Matrix4f translate(final Vector2f vec) {
        return this.translate(vec, this);
    }
    
    public Matrix4f translate(final Vector3f vec) {
        return this.translate(vec, this);
    }
    
    public Matrix4f scale(final Vector3f vec) {
        return scale(vec, this, this);
    }
    
    public static Matrix4f scale(final Vector3f vec, final Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }
        dest.m00 = src.m00 * vec.x;
        dest.m01 = src.m01 * vec.x;
        dest.m02 = src.m02 * vec.x;
        dest.m03 = src.m03 * vec.x;
        dest.m10 = src.m10 * vec.y;
        dest.m11 = src.m11 * vec.y;
        dest.m12 = src.m12 * vec.y;
        dest.m13 = src.m13 * vec.y;
        dest.m20 = src.m20 * vec.z;
        dest.m21 = src.m21 * vec.z;
        dest.m22 = src.m22 * vec.z;
        dest.m23 = src.m23 * vec.z;
        return dest;
    }
    
    public Matrix4f rotate(final float angle, final Vector3f axis) {
        return this.rotate(angle, axis, this);
    }
    
    public Matrix4f rotate(final float angle, final Vector3f axis, final Matrix4f dest) {
        return rotate(angle, axis, this, dest);
    }
    
    public static Matrix4f rotate(final float angle, final Vector3f axis, final Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }
        final float c = (float)Math.cos(angle);
        final float s = (float)Math.sin(angle);
        final float oneminusc = 1.0f - c;
        final float xy = axis.x * axis.y;
        final float yz = axis.y * axis.z;
        final float xz = axis.x * axis.z;
        final float xs = axis.x * s;
        final float ys = axis.y * s;
        final float zs = axis.z * s;
        final float f00 = axis.x * axis.x * oneminusc + c;
        final float f2 = xy * oneminusc + zs;
        final float f3 = xz * oneminusc - ys;
        final float f4 = xy * oneminusc - zs;
        final float f5 = axis.y * axis.y * oneminusc + c;
        final float f6 = yz * oneminusc + xs;
        final float f7 = xz * oneminusc + ys;
        final float f8 = yz * oneminusc - xs;
        final float f9 = axis.z * axis.z * oneminusc + c;
        final float t00 = src.m00 * f00 + src.m10 * f2 + src.m20 * f3;
        final float t2 = src.m01 * f00 + src.m11 * f2 + src.m21 * f3;
        final float t3 = src.m02 * f00 + src.m12 * f2 + src.m22 * f3;
        final float t4 = src.m03 * f00 + src.m13 * f2 + src.m23 * f3;
        final float t5 = src.m00 * f4 + src.m10 * f5 + src.m20 * f6;
        final float t6 = src.m01 * f4 + src.m11 * f5 + src.m21 * f6;
        final float t7 = src.m02 * f4 + src.m12 * f5 + src.m22 * f6;
        final float t8 = src.m03 * f4 + src.m13 * f5 + src.m23 * f6;
        dest.m20 = src.m00 * f7 + src.m10 * f8 + src.m20 * f9;
        dest.m21 = src.m01 * f7 + src.m11 * f8 + src.m21 * f9;
        dest.m22 = src.m02 * f7 + src.m12 * f8 + src.m22 * f9;
        dest.m23 = src.m03 * f7 + src.m13 * f8 + src.m23 * f9;
        dest.m00 = t00;
        dest.m01 = t2;
        dest.m02 = t3;
        dest.m03 = t4;
        dest.m10 = t5;
        dest.m11 = t6;
        dest.m12 = t7;
        dest.m13 = t8;
        return dest;
    }
    
    public Matrix4f translate(final Vector3f vec, final Matrix4f dest) {
        return translate(vec, this, dest);
    }
    
    public static Matrix4f translate(final Vector3f vec, final Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }
        final Matrix4f matrix4f = dest;
        matrix4f.m30 += src.m00 * vec.x + src.m10 * vec.y + src.m20 * vec.z;
        final Matrix4f matrix4f2 = dest;
        matrix4f2.m31 += src.m01 * vec.x + src.m11 * vec.y + src.m21 * vec.z;
        final Matrix4f matrix4f3 = dest;
        matrix4f3.m32 += src.m02 * vec.x + src.m12 * vec.y + src.m22 * vec.z;
        final Matrix4f matrix4f4 = dest;
        matrix4f4.m33 += src.m03 * vec.x + src.m13 * vec.y + src.m23 * vec.z;
        return dest;
    }
    
    public Matrix4f translate(final Vector2f vec, final Matrix4f dest) {
        return translate(vec, this, dest);
    }
    
    public static Matrix4f translate(final Vector2f vec, final Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }
        final Matrix4f matrix4f = dest;
        matrix4f.m30 += src.m00 * vec.x + src.m10 * vec.y;
        final Matrix4f matrix4f2 = dest;
        matrix4f2.m31 += src.m01 * vec.x + src.m11 * vec.y;
        final Matrix4f matrix4f3 = dest;
        matrix4f3.m32 += src.m02 * vec.x + src.m12 * vec.y;
        final Matrix4f matrix4f4 = dest;
        matrix4f4.m33 += src.m03 * vec.x + src.m13 * vec.y;
        return dest;
    }
    
    public Matrix4f transpose(final Matrix4f dest) {
        return transpose(this, dest);
    }
    
    public static Matrix4f transpose(final Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }
        final float m00 = src.m00;
        final float m2 = src.m10;
        final float m3 = src.m20;
        final float m4 = src.m30;
        final float m5 = src.m01;
        final float m6 = src.m11;
        final float m7 = src.m21;
        final float m8 = src.m31;
        final float m9 = src.m02;
        final float m10 = src.m12;
        final float m11 = src.m22;
        final float m12 = src.m32;
        final float m13 = src.m03;
        final float m14 = src.m13;
        final float m15 = src.m23;
        final float m16 = src.m33;
        dest.m00 = m00;
        dest.m01 = m2;
        dest.m02 = m3;
        dest.m03 = m4;
        dest.m10 = m5;
        dest.m11 = m6;
        dest.m12 = m7;
        dest.m13 = m8;
        dest.m20 = m9;
        dest.m21 = m10;
        dest.m22 = m11;
        dest.m23 = m12;
        dest.m30 = m13;
        dest.m31 = m14;
        dest.m32 = m15;
        dest.m33 = m16;
        return dest;
    }
    
    @Override
    public float determinant() {
        float f = this.m00 * (this.m11 * this.m22 * this.m33 + this.m12 * this.m23 * this.m31 + this.m13 * this.m21 * this.m32 - this.m13 * this.m22 * this.m31 - this.m11 * this.m23 * this.m32 - this.m12 * this.m21 * this.m33);
        f -= this.m01 * (this.m10 * this.m22 * this.m33 + this.m12 * this.m23 * this.m30 + this.m13 * this.m20 * this.m32 - this.m13 * this.m22 * this.m30 - this.m10 * this.m23 * this.m32 - this.m12 * this.m20 * this.m33);
        f += this.m02 * (this.m10 * this.m21 * this.m33 + this.m11 * this.m23 * this.m30 + this.m13 * this.m20 * this.m31 - this.m13 * this.m21 * this.m30 - this.m10 * this.m23 * this.m31 - this.m11 * this.m20 * this.m33);
        f -= this.m03 * (this.m10 * this.m21 * this.m32 + this.m11 * this.m22 * this.m30 + this.m12 * this.m20 * this.m31 - this.m12 * this.m21 * this.m30 - this.m10 * this.m22 * this.m31 - this.m11 * this.m20 * this.m32);
        return f;
    }
    
    private static float determinant3x3(final float t00, final float t01, final float t02, final float t10, final float t11, final float t12, final float t20, final float t21, final float t22) {
        return t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22) + t02 * (t10 * t21 - t11 * t20);
    }
    
    @Override
    public Matrix invert() {
        return invert(this, this);
    }
    
    public static Matrix4f invert(final Matrix4f src, Matrix4f dest) {
        final float determinant = src.determinant();
        if (determinant != 0.0f) {
            if (dest == null) {
                dest = new Matrix4f();
            }
            final float determinant_inv = 1.0f / determinant;
            final float t00 = determinant3x3(src.m11, src.m12, src.m13, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
            final float t2 = -determinant3x3(src.m10, src.m12, src.m13, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
            final float t3 = determinant3x3(src.m10, src.m11, src.m13, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
            final float t4 = -determinant3x3(src.m10, src.m11, src.m12, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
            final float t5 = -determinant3x3(src.m01, src.m02, src.m03, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
            final float t6 = determinant3x3(src.m00, src.m02, src.m03, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
            final float t7 = -determinant3x3(src.m00, src.m01, src.m03, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
            final float t8 = determinant3x3(src.m00, src.m01, src.m02, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
            final float t9 = determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m31, src.m32, src.m33);
            final float t10 = -determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m30, src.m32, src.m33);
            final float t11 = determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m30, src.m31, src.m33);
            final float t12 = -determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m30, src.m31, src.m32);
            final float t13 = -determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m21, src.m22, src.m23);
            final float t14 = determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m20, src.m22, src.m23);
            final float t15 = -determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m20, src.m21, src.m23);
            final float t16 = determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m20, src.m21, src.m22);
            dest.m00 = t00 * determinant_inv;
            dest.m11 = t6 * determinant_inv;
            dest.m22 = t11 * determinant_inv;
            dest.m33 = t16 * determinant_inv;
            dest.m01 = t5 * determinant_inv;
            dest.m10 = t2 * determinant_inv;
            dest.m20 = t3 * determinant_inv;
            dest.m02 = t9 * determinant_inv;
            dest.m12 = t10 * determinant_inv;
            dest.m21 = t7 * determinant_inv;
            dest.m03 = t13 * determinant_inv;
            dest.m30 = t4 * determinant_inv;
            dest.m13 = t14 * determinant_inv;
            dest.m31 = t8 * determinant_inv;
            dest.m32 = t12 * determinant_inv;
            dest.m23 = t15 * determinant_inv;
            return dest;
        }
        return null;
    }
    
    @Override
    public Matrix negate() {
        return this.negate(this);
    }
    
    public Matrix4f negate(final Matrix4f dest) {
        return negate(this, dest);
    }
    
    public static Matrix4f negate(final Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }
        dest.m00 = -src.m00;
        dest.m01 = -src.m01;
        dest.m02 = -src.m02;
        dest.m03 = -src.m03;
        dest.m10 = -src.m10;
        dest.m11 = -src.m11;
        dest.m12 = -src.m12;
        dest.m13 = -src.m13;
        dest.m20 = -src.m20;
        dest.m21 = -src.m21;
        dest.m22 = -src.m22;
        dest.m23 = -src.m23;
        dest.m30 = -src.m30;
        dest.m31 = -src.m31;
        dest.m32 = -src.m32;
        dest.m33 = -src.m33;
        return dest;
    }
}
