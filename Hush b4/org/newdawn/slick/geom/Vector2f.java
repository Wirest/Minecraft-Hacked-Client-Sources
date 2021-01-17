// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

import org.newdawn.slick.util.FastTrig;
import java.io.Serializable;

public strictfp class Vector2f implements Serializable
{
    private static final long serialVersionUID = 1339934L;
    public float x;
    public float y;
    
    public Vector2f() {
    }
    
    public Vector2f(final float[] coords) {
        this.x = coords[0];
        this.y = coords[1];
    }
    
    public Vector2f(final double theta) {
        this.x = 1.0f;
        this.y = 0.0f;
        this.setTheta(theta);
    }
    
    public strictfp void setTheta(double theta) {
        if (theta < -360.0 || theta > 360.0) {
            theta %= 360.0;
        }
        if (theta < 0.0) {
            theta += 360.0;
        }
        double oldTheta = this.getTheta();
        if (theta < -360.0 || theta > 360.0) {
            oldTheta %= 360.0;
        }
        if (theta < 0.0) {
            oldTheta += 360.0;
        }
        final float len = this.length();
        this.x = len * (float)FastTrig.cos(StrictMath.toRadians(theta));
        this.y = len * (float)FastTrig.sin(StrictMath.toRadians(theta));
    }
    
    public strictfp Vector2f add(final double theta) {
        this.setTheta(this.getTheta() + theta);
        return this;
    }
    
    public strictfp Vector2f sub(final double theta) {
        this.setTheta(this.getTheta() - theta);
        return this;
    }
    
    public strictfp double getTheta() {
        double theta = StrictMath.toDegrees(StrictMath.atan2(this.y, this.x));
        if (theta < -360.0 || theta > 360.0) {
            theta %= 360.0;
        }
        if (theta < 0.0) {
            theta += 360.0;
        }
        return theta;
    }
    
    public strictfp float getX() {
        return this.x;
    }
    
    public strictfp float getY() {
        return this.y;
    }
    
    public Vector2f(final Vector2f other) {
        this(other.getX(), other.getY());
    }
    
    public Vector2f(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public strictfp void set(final Vector2f other) {
        this.set(other.getX(), other.getY());
    }
    
    public strictfp float dot(final Vector2f other) {
        return this.x * other.getX() + this.y * other.getY();
    }
    
    public strictfp Vector2f set(final float x, final float y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public strictfp Vector2f getPerpendicular() {
        return new Vector2f(-this.y, this.x);
    }
    
    public strictfp Vector2f set(final float[] pt) {
        return this.set(pt[0], pt[1]);
    }
    
    public strictfp Vector2f negate() {
        return new Vector2f(-this.x, -this.y);
    }
    
    public strictfp Vector2f negateLocal() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }
    
    public strictfp Vector2f add(final Vector2f v) {
        this.x += v.getX();
        this.y += v.getY();
        return this;
    }
    
    public strictfp Vector2f sub(final Vector2f v) {
        this.x -= v.getX();
        this.y -= v.getY();
        return this;
    }
    
    public strictfp Vector2f scale(final float a) {
        this.x *= a;
        this.y *= a;
        return this;
    }
    
    public strictfp Vector2f normalise() {
        final float l = this.length();
        if (l == 0.0f) {
            return this;
        }
        this.x /= l;
        this.y /= l;
        return this;
    }
    
    public strictfp Vector2f getNormal() {
        final Vector2f cp = this.copy();
        cp.normalise();
        return cp;
    }
    
    public strictfp float lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }
    
    public strictfp float length() {
        return (float)Math.sqrt(this.lengthSquared());
    }
    
    public strictfp void projectOntoUnit(final Vector2f b, final Vector2f result) {
        final float dp = b.dot(this);
        result.x = dp * b.getX();
        result.y = dp * b.getY();
    }
    
    public strictfp Vector2f copy() {
        return new Vector2f(this.x, this.y);
    }
    
    @Override
    public strictfp String toString() {
        return "[Vector2f " + this.x + "," + this.y + " (" + this.length() + ")]";
    }
    
    public strictfp float distance(final Vector2f other) {
        return (float)Math.sqrt(this.distanceSquared(other));
    }
    
    public strictfp float distanceSquared(final Vector2f other) {
        final float dx = other.getX() - this.getX();
        final float dy = other.getY() - this.getY();
        return dx * dx + dy * dy;
    }
    
    @Override
    public strictfp int hashCode() {
        return 997 * (int)this.x ^ 991 * (int)this.y;
    }
    
    @Override
    public strictfp boolean equals(final Object other) {
        if (other instanceof Vector2f) {
            final Vector2f o = (Vector2f)other;
            return o.x == this.x && o.y == this.y;
        }
        return false;
    }
}
