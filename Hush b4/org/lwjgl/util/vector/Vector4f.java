// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.vector;

import java.nio.FloatBuffer;
import java.io.Serializable;

public class Vector4f extends Vector implements Serializable, ReadableVector4f, WritableVector4f
{
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    public float z;
    public float w;
    
    public Vector4f() {
    }
    
    public Vector4f(final ReadableVector4f src) {
        this.set(src);
    }
    
    public Vector4f(final float x, final float y, final float z, final float w) {
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
    
    public Vector4f set(final ReadableVector4f src) {
        this.x = src.getX();
        this.y = src.getY();
        this.z = src.getZ();
        this.w = src.getW();
        return this;
    }
    
    @Override
    public float lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
    }
    
    public Vector4f translate(final float x, final float y, final float z, final float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }
    
    public static Vector4f add(final Vector4f left, final Vector4f right, final Vector4f dest) {
        if (dest == null) {
            return new Vector4f(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
        }
        dest.set(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
        return dest;
    }
    
    public static Vector4f sub(final Vector4f left, final Vector4f right, final Vector4f dest) {
        if (dest == null) {
            return new Vector4f(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
        }
        dest.set(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
        return dest;
    }
    
    @Override
    public Vector negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
        return this;
    }
    
    public Vector4f negate(Vector4f dest) {
        if (dest == null) {
            dest = new Vector4f();
        }
        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        dest.w = -this.w;
        return dest;
    }
    
    public Vector4f normalise(Vector4f dest) {
        final float l = this.length();
        if (dest == null) {
            dest = new Vector4f(this.x / l, this.y / l, this.z / l, this.w / l);
        }
        else {
            dest.set(this.x / l, this.y / l, this.z / l, this.w / l);
        }
        return dest;
    }
    
    public static float dot(final Vector4f left, final Vector4f right) {
        return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
    }
    
    public static float angle(final Vector4f a, final Vector4f b) {
        float dls = dot(a, b) / (a.length() * b.length());
        if (dls < -1.0f) {
            dls = -1.0f;
        }
        else if (dls > 1.0f) {
            dls = 1.0f;
        }
        return (float)Math.acos(dls);
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
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
        this.w *= scale;
        return this;
    }
    
    @Override
    public Vector store(final FloatBuffer buf) {
        buf.put(this.x);
        buf.put(this.y);
        buf.put(this.z);
        buf.put(this.w);
        return this;
    }
    
    @Override
    public String toString() {
        return "Vector4f: " + this.x + " " + this.y + " " + this.z + " " + this.w;
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
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Vector4f other = (Vector4f)obj;
        return this.x == other.x && this.y == other.y && this.z == other.z && this.w == other.w;
    }
}
