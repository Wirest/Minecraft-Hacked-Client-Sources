// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Vector2f extends Tuple2f implements Serializable
{
    static final long serialVersionUID = -2168194326883512320L;
    
    public Vector2f(final float n, final float n2) {
        super(n, n2);
    }
    
    public Vector2f(final float[] array) {
        super(array);
    }
    
    public Vector2f(final Vector2f vector2f) {
        super(vector2f);
    }
    
    public Vector2f(final Vector2d vector2d) {
        super(vector2d);
    }
    
    public Vector2f(final Tuple2f tuple2f) {
        super(tuple2f);
    }
    
    public Vector2f(final Tuple2d tuple2d) {
        super(tuple2d);
    }
    
    public Vector2f() {
    }
    
    public final float dot(final Vector2f vector2f) {
        return this.x * vector2f.x + this.y * vector2f.y;
    }
    
    public final float length() {
        return (float)Math.sqrt(this.x * this.x + this.y * this.y);
    }
    
    public final float lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }
    
    public final void normalize(final Vector2f vector2f) {
        final float n = (float)(1.0 / Math.sqrt(vector2f.x * vector2f.x + vector2f.y * vector2f.y));
        this.x = vector2f.x * n;
        this.y = vector2f.y * n;
    }
    
    public final void normalize() {
        final float n = (float)(1.0 / Math.sqrt(this.x * this.x + this.y * this.y));
        this.x *= n;
        this.y *= n;
    }
    
    public final float angle(final Vector2f vector2f) {
        double a = this.dot(vector2f) / (this.length() * vector2f.length());
        if (a < -1.0) {
            a = -1.0;
        }
        if (a > 1.0) {
            a = 1.0;
        }
        return (float)Math.acos(a);
    }
}
