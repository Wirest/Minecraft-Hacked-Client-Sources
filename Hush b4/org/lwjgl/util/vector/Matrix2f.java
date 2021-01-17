// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.vector;

import java.nio.FloatBuffer;
import java.io.Serializable;

public class Matrix2f extends Matrix implements Serializable
{
    private static final long serialVersionUID = 1L;
    public float m00;
    public float m01;
    public float m10;
    public float m11;
    
    public Matrix2f() {
        this.setIdentity();
    }
    
    public Matrix2f(final Matrix2f src) {
        this.load(src);
    }
    
    public Matrix2f load(final Matrix2f src) {
        return load(src, this);
    }
    
    public static Matrix2f load(final Matrix2f src, Matrix2f dest) {
        if (dest == null) {
            dest = new Matrix2f();
        }
        dest.m00 = src.m00;
        dest.m01 = src.m01;
        dest.m10 = src.m10;
        dest.m11 = src.m11;
        return dest;
    }
    
    @Override
    public Matrix load(final FloatBuffer buf) {
        this.m00 = buf.get();
        this.m01 = buf.get();
        this.m10 = buf.get();
        this.m11 = buf.get();
        return this;
    }
    
    @Override
    public Matrix loadTranspose(final FloatBuffer buf) {
        this.m00 = buf.get();
        this.m10 = buf.get();
        this.m01 = buf.get();
        this.m11 = buf.get();
        return this;
    }
    
    @Override
    public Matrix store(final FloatBuffer buf) {
        buf.put(this.m00);
        buf.put(this.m01);
        buf.put(this.m10);
        buf.put(this.m11);
        return this;
    }
    
    @Override
    public Matrix storeTranspose(final FloatBuffer buf) {
        buf.put(this.m00);
        buf.put(this.m10);
        buf.put(this.m01);
        buf.put(this.m11);
        return this;
    }
    
    public static Matrix2f add(final Matrix2f left, final Matrix2f right, Matrix2f dest) {
        if (dest == null) {
            dest = new Matrix2f();
        }
        dest.m00 = left.m00 + right.m00;
        dest.m01 = left.m01 + right.m01;
        dest.m10 = left.m10 + right.m10;
        dest.m11 = left.m11 + right.m11;
        return dest;
    }
    
    public static Matrix2f sub(final Matrix2f left, final Matrix2f right, Matrix2f dest) {
        if (dest == null) {
            dest = new Matrix2f();
        }
        dest.m00 = left.m00 - right.m00;
        dest.m01 = left.m01 - right.m01;
        dest.m10 = left.m10 - right.m10;
        dest.m11 = left.m11 - right.m11;
        return dest;
    }
    
    public static Matrix2f mul(final Matrix2f left, final Matrix2f right, Matrix2f dest) {
        if (dest == null) {
            dest = new Matrix2f();
        }
        final float m00 = left.m00 * right.m00 + left.m10 * right.m01;
        final float m2 = left.m01 * right.m00 + left.m11 * right.m01;
        final float m3 = left.m00 * right.m10 + left.m10 * right.m11;
        final float m4 = left.m01 * right.m10 + left.m11 * right.m11;
        dest.m00 = m00;
        dest.m01 = m2;
        dest.m10 = m3;
        dest.m11 = m4;
        return dest;
    }
    
    public static Vector2f transform(final Matrix2f left, final Vector2f right, Vector2f dest) {
        if (dest == null) {
            dest = new Vector2f();
        }
        final float x = left.m00 * right.x + left.m10 * right.y;
        final float y = left.m01 * right.x + left.m11 * right.y;
        dest.x = x;
        dest.y = y;
        return dest;
    }
    
    @Override
    public Matrix transpose() {
        return this.transpose(this);
    }
    
    public Matrix2f transpose(final Matrix2f dest) {
        return transpose(this, dest);
    }
    
    public static Matrix2f transpose(final Matrix2f src, Matrix2f dest) {
        if (dest == null) {
            dest = new Matrix2f();
        }
        final float m01 = src.m10;
        final float m2 = src.m01;
        dest.m01 = m01;
        dest.m10 = m2;
        return dest;
    }
    
    @Override
    public Matrix invert() {
        return invert(this, this);
    }
    
    public static Matrix2f invert(final Matrix2f src, Matrix2f dest) {
        final float determinant = src.determinant();
        if (determinant != 0.0f) {
            if (dest == null) {
                dest = new Matrix2f();
            }
            final float determinant_inv = 1.0f / determinant;
            final float t00 = src.m11 * determinant_inv;
            final float t2 = -src.m01 * determinant_inv;
            final float t3 = src.m00 * determinant_inv;
            final float t4 = -src.m10 * determinant_inv;
            dest.m00 = t00;
            dest.m01 = t2;
            dest.m10 = t4;
            dest.m11 = t3;
            return dest;
        }
        return null;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(this.m00).append(' ').append(this.m10).append(' ').append('\n');
        buf.append(this.m01).append(' ').append(this.m11).append(' ').append('\n');
        return buf.toString();
    }
    
    @Override
    public Matrix negate() {
        return this.negate(this);
    }
    
    public Matrix2f negate(final Matrix2f dest) {
        return negate(this, dest);
    }
    
    public static Matrix2f negate(final Matrix2f src, Matrix2f dest) {
        if (dest == null) {
            dest = new Matrix2f();
        }
        dest.m00 = -src.m00;
        dest.m01 = -src.m01;
        dest.m10 = -src.m10;
        dest.m11 = -src.m11;
        return dest;
    }
    
    @Override
    public Matrix setIdentity() {
        return setIdentity(this);
    }
    
    public static Matrix2f setIdentity(final Matrix2f src) {
        src.m00 = 1.0f;
        src.m01 = 0.0f;
        src.m10 = 0.0f;
        src.m11 = 1.0f;
        return src;
    }
    
    @Override
    public Matrix setZero() {
        return setZero(this);
    }
    
    public static Matrix2f setZero(final Matrix2f src) {
        src.m00 = 0.0f;
        src.m01 = 0.0f;
        src.m10 = 0.0f;
        src.m11 = 0.0f;
        return src;
    }
    
    @Override
    public float determinant() {
        return this.m00 * this.m11 - this.m01 * this.m10;
    }
}
