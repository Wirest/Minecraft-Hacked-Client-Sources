// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.vector;

import java.nio.FloatBuffer;

public class Quaternion extends Vector implements ReadableVector4f
{
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    public float z;
    public float w;
    
    public Quaternion() {
        this.setIdentity();
    }
    
    public Quaternion(final ReadableVector4f src) {
        this.set(src);
    }
    
    public Quaternion(final float x, final float y, final float z, final float w) {
        this.set(x, y, z, w);
    }
    
    public void set(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public void set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void set(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Quaternion set(final ReadableVector4f src) {
        this.x = src.getX();
        this.y = src.getY();
        this.z = src.getZ();
        this.w = src.getW();
        return this;
    }
    
    public Quaternion setIdentity() {
        return setIdentity(this);
    }
    
    public static Quaternion setIdentity(final Quaternion q) {
        q.x = 0.0f;
        q.y = 0.0f;
        q.z = 0.0f;
        q.w = 1.0f;
        return q;
    }
    
    @Override
    public float lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
    }
    
    public static Quaternion normalise(final Quaternion src, Quaternion dest) {
        final float inv_l = 1.0f / src.length();
        if (dest == null) {
            dest = new Quaternion();
        }
        dest.set(src.x * inv_l, src.y * inv_l, src.z * inv_l, src.w * inv_l);
        return dest;
    }
    
    public Quaternion normalise(final Quaternion dest) {
        return normalise(this, dest);
    }
    
    public static float dot(final Quaternion left, final Quaternion right) {
        return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
    }
    
    public Quaternion negate(final Quaternion dest) {
        return negate(this, dest);
    }
    
    public static Quaternion negate(final Quaternion src, Quaternion dest) {
        if (dest == null) {
            dest = new Quaternion();
        }
        dest.x = -src.x;
        dest.y = -src.y;
        dest.z = -src.z;
        dest.w = src.w;
        return dest;
    }
    
    @Override
    public Vector negate() {
        return negate(this, this);
    }
    
    @Override
    public Vector load(final FloatBuffer buf) {
        this.x = buf.get();
        this.y = buf.get();
        this.z = buf.get();
        this.w = buf.get();
        return this;
    }
    
    @Override
    public Vector scale(final float scale) {
        return scale(scale, this, this);
    }
    
    public static Quaternion scale(final float scale, final Quaternion src, Quaternion dest) {
        if (dest == null) {
            dest = new Quaternion();
        }
        dest.x = src.x * scale;
        dest.y = src.y * scale;
        dest.z = src.z * scale;
        dest.w = src.w * scale;
        return dest;
    }
    
    @Override
    public Vector store(final FloatBuffer buf) {
        buf.put(this.x);
        buf.put(this.y);
        buf.put(this.z);
        buf.put(this.w);
        return this;
    }
    
    public final float getX() {
        return this.x;
    }
    
    public final float getY() {
        return this.y;
    }
    
    public final void setX(final float x) {
        this.x = x;
    }
    
    public final void setY(final float y) {
        this.y = y;
    }
    
    public void setZ(final float z) {
        this.z = z;
    }
    
    public float getZ() {
        return this.z;
    }
    
    public void setW(final float w) {
        this.w = w;
    }
    
    public float getW() {
        return this.w;
    }
    
    @Override
    public String toString() {
        return "Quaternion: " + this.x + " " + this.y + " " + this.z + " " + this.w;
    }
    
    public static Quaternion mul(final Quaternion left, final Quaternion right, Quaternion dest) {
        if (dest == null) {
            dest = new Quaternion();
        }
        dest.set(left.x * right.w + left.w * right.x + left.y * right.z - left.z * right.y, left.y * right.w + left.w * right.y + left.z * right.x - left.x * right.z, left.z * right.w + left.w * right.z + left.x * right.y - left.y * right.x, left.w * right.w - left.x * right.x - left.y * right.y - left.z * right.z);
        return dest;
    }
    
    public static Quaternion mulInverse(final Quaternion left, final Quaternion right, Quaternion dest) {
        float n = right.lengthSquared();
        n = ((n == 0.0) ? n : (1.0f / n));
        if (dest == null) {
            dest = new Quaternion();
        }
        dest.set((left.x * right.w - left.w * right.x - left.y * right.z + left.z * right.y) * n, (left.y * right.w - left.w * right.y - left.z * right.x + left.x * right.z) * n, (left.z * right.w - left.w * right.z - left.x * right.y + left.y * right.x) * n, (left.w * right.w + left.x * right.x + left.y * right.y + left.z * right.z) * n);
        return dest;
    }
    
    public final void setFromAxisAngle(final Vector4f a1) {
        this.x = a1.x;
        this.y = a1.y;
        this.z = a1.z;
        final float n = (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        final float s = (float)(Math.sin(0.5 * a1.w) / n);
        this.x *= s;
        this.y *= s;
        this.z *= s;
        this.w = (float)Math.cos(0.5 * a1.w);
    }
    
    public final Quaternion setFromMatrix(final Matrix4f m) {
        return setFromMatrix(m, this);
    }
    
    public static Quaternion setFromMatrix(final Matrix4f m, final Quaternion q) {
        return q.setFromMat(m.m00, m.m01, m.m02, m.m10, m.m11, m.m12, m.m20, m.m21, m.m22);
    }
    
    public final Quaternion setFromMatrix(final Matrix3f m) {
        return setFromMatrix(m, this);
    }
    
    public static Quaternion setFromMatrix(final Matrix3f m, final Quaternion q) {
        return q.setFromMat(m.m00, m.m01, m.m02, m.m10, m.m11, m.m12, m.m20, m.m21, m.m22);
    }
    
    private Quaternion setFromMat(final float m00, final float m01, final float m02, final float m10, final float m11, final float m12, final float m20, final float m21, final float m22) {
        final float tr = m00 + m11 + m22;
        if (tr >= 0.0) {
            float s = (float)Math.sqrt(tr + 1.0);
            this.w = s * 0.5f;
            s = 0.5f / s;
            this.x = (m21 - m12) * s;
            this.y = (m02 - m20) * s;
            this.z = (m10 - m01) * s;
        }
        else {
            final float max = Math.max(Math.max(m00, m11), m22);
            if (max == m00) {
                float s = (float)Math.sqrt(m00 - (m11 + m22) + 1.0);
                this.x = s * 0.5f;
                s = 0.5f / s;
                this.y = (m01 + m10) * s;
                this.z = (m20 + m02) * s;
                this.w = (m21 - m12) * s;
            }
            else if (max == m11) {
                float s = (float)Math.sqrt(m11 - (m22 + m00) + 1.0);
                this.y = s * 0.5f;
                s = 0.5f / s;
                this.z = (m12 + m21) * s;
                this.x = (m01 + m10) * s;
                this.w = (m02 - m20) * s;
            }
            else {
                float s = (float)Math.sqrt(m22 - (m00 + m11) + 1.0);
                this.z = s * 0.5f;
                s = 0.5f / s;
                this.x = (m20 + m02) * s;
                this.y = (m12 + m21) * s;
                this.w = (m10 - m01) * s;
            }
        }
        return this;
    }
}
