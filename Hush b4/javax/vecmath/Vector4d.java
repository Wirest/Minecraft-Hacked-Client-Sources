// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Vector4d extends Tuple4d implements Serializable
{
    static final long serialVersionUID = 3938123424117448700L;
    
    public Vector4d(final double n, final double n2, final double n3, final double n4) {
        super(n, n2, n3, n4);
    }
    
    public Vector4d(final double[] array) {
        super(array);
    }
    
    public Vector4d(final Vector4d vector4d) {
        super(vector4d);
    }
    
    public Vector4d(final Vector4f vector4f) {
        super(vector4f);
    }
    
    public Vector4d(final Tuple4f tuple4f) {
        super(tuple4f);
    }
    
    public Vector4d(final Tuple4d tuple4d) {
        super(tuple4d);
    }
    
    public Vector4d(final Tuple3d tuple3d) {
        super(tuple3d.x, tuple3d.y, tuple3d.z, 0.0);
    }
    
    public Vector4d() {
    }
    
    public final void set(final Tuple3d tuple3d) {
        this.x = tuple3d.x;
        this.y = tuple3d.y;
        this.z = tuple3d.z;
        this.w = 0.0;
    }
    
    public final double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
    }
    
    public final double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
    }
    
    public final double dot(final Vector4d vector4d) {
        return this.x * vector4d.x + this.y * vector4d.y + this.z * vector4d.z + this.w * vector4d.w;
    }
    
    public final void normalize(final Vector4d vector4d) {
        final double n = 1.0 / Math.sqrt(vector4d.x * vector4d.x + vector4d.y * vector4d.y + vector4d.z * vector4d.z + vector4d.w * vector4d.w);
        this.x = vector4d.x * n;
        this.y = vector4d.y * n;
        this.z = vector4d.z * n;
        this.w = vector4d.w * n;
    }
    
    public final void normalize() {
        final double n = 1.0 / Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
        this.x *= n;
        this.y *= n;
        this.z *= n;
        this.w *= n;
    }
    
    public final double angle(final Vector4d vector4d) {
        double a = this.dot(vector4d) / (this.length() * vector4d.length());
        if (a < -1.0) {
            a = -1.0;
        }
        if (a > 1.0) {
            a = 1.0;
        }
        return Math.acos(a);
    }
}
