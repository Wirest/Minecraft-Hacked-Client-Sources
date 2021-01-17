// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.vector;

import java.nio.FloatBuffer;
import java.io.Serializable;

public class Vector2f extends Vector implements Serializable, ReadableVector2f, WritableVector2f
{
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    
    public Vector2f() {
    }
    
    public Vector2f(final ReadableVector2f src) {
        this.set(src);
    }
    
    public Vector2f(final float x, final float y) {
        this.set(x, y);
    }
    
    public void set(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2f set(final ReadableVector2f src) {
        this.x = src.getX();
        this.y = src.getY();
        return this;
    }
    
    @Override
    public float lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }
    
    public Vector2f translate(final float x, final float y) {
        this.x += x;
        this.y += y;
        return this;
    }
    
    @Override
    public Vector negate() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }
    
    public Vector2f negate(Vector2f dest) {
        if (dest == null) {
            dest = new Vector2f();
        }
        dest.x = -this.x;
        dest.y = -this.y;
        return dest;
    }
    
    public Vector2f normalise(Vector2f dest) {
        final float l = this.length();
        if (dest == null) {
            dest = new Vector2f(this.x / l, this.y / l);
        }
        else {
            dest.set(this.x / l, this.y / l);
        }
        return dest;
    }
    
    public static float dot(final Vector2f left, final Vector2f right) {
        return left.x * right.x + left.y * right.y;
    }
    
    public static float angle(final Vector2f a, final Vector2f b) {
        float dls = dot(a, b) / (a.length() * b.length());
        if (dls < -1.0f) {
            dls = -1.0f;
        }
        else if (dls > 1.0f) {
            dls = 1.0f;
        }
        return (float)Math.acos(dls);
    }
    
    public static Vector2f add(final Vector2f left, final Vector2f right, final Vector2f dest) {
        if (dest == null) {
            return new Vector2f(left.x + right.x, left.y + right.y);
        }
        dest.set(left.x + right.x, left.y + right.y);
        return dest;
    }
    
    public static Vector2f sub(final Vector2f left, final Vector2f right, final Vector2f dest) {
        if (dest == null) {
            return new Vector2f(left.x - right.x, left.y - right.y);
        }
        dest.set(left.x - right.x, left.y - right.y);
        return dest;
    }
    
    @Override
    public Vector store(final FloatBuffer buf) {
        buf.put(this.x);
        buf.put(this.y);
        return this;
    }
    
    @Override
    public Vector load(final FloatBuffer buf) {
        this.x = buf.get();
        this.y = buf.get();
        return this;
    }
    
    @Override
    public Vector scale(final float scale) {
        this.x *= scale;
        this.y *= scale;
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(64);
        sb.append("Vector2f[");
        sb.append(this.x);
        sb.append(", ");
        sb.append(this.y);
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
        final Vector2f other = (Vector2f)obj;
        return this.x == other.x && this.y == other.y;
    }
}
