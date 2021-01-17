// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.vector;

import java.nio.FloatBuffer;
import java.io.Serializable;

public class Vector3f extends Vector implements Serializable, ReadableVector3f, WritableVector3f
{
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    public float z;
    
    public Vector3f() {
    }
    
    public Vector3f(final ReadableVector3f src) {
        this.set(src);
    }
    
    public Vector3f(final float x, final float y, final float z) {
        this.set(x, y, z);
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
    
    public Vector3f set(final ReadableVector3f src) {
        this.x = src.getX();
        this.y = src.getY();
        this.z = src.getZ();
        return this;
    }
    
    @Override
    public float lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
    
    public Vector3f translate(final float x, final float y, final float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    public static Vector3f add(final Vector3f left, final Vector3f right, final Vector3f dest) {
        if (dest == null) {
            return new Vector3f(left.x + right.x, left.y + right.y, left.z + right.z);
        }
        dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
        return dest;
    }
    
    public static Vector3f sub(final Vector3f left, final Vector3f right, final Vector3f dest) {
        if (dest == null) {
            return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
        }
        dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
        return dest;
    }
    
    public static Vector3f cross(final Vector3f left, final Vector3f right, Vector3f dest) {
        if (dest == null) {
            dest = new Vector3f();
        }
        dest.set(left.y * right.z - left.z * right.y, right.x * left.z - right.z * left.x, left.x * right.y - left.y * right.x);
        return dest;
    }
    
    @Override
    public Vector negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }
    
    public Vector3f negate(Vector3f dest) {
        if (dest == null) {
            dest = new Vector3f();
        }
        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        return dest;
    }
    
    public Vector3f normalise(Vector3f dest) {
        final float l = this.length();
        if (dest == null) {
            dest = new Vector3f(this.x / l, this.y / l, this.z / l);
        }
        else {
            dest.set(this.x / l, this.y / l, this.z / l);
        }
        return dest;
    }
    
    public static float dot(final Vector3f left, final Vector3f right) {
        return left.x * right.x + left.y * right.y + left.z * right.z;
    }
    
    public static float angle(final Vector3f a, final Vector3f b) {
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
        return this;
    }
    
    @Override
    public Vector scale(final float scale) {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
        return this;
    }
    
    @Override
    public Vector store(final FloatBuffer buf) {
        buf.put(this.x);
        buf.put(this.y);
        buf.put(this.z);
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(64);
        sb.append("Vector3f[");
        sb.append(this.x);
        sb.append(", ");
        sb.append(this.y);
        sb.append(", ");
        sb.append(this.z);
        sb.append(']');
        return sb.toString();
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
        final Vector3f other = (Vector3f)obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }
}
