// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple2f implements Serializable, Cloneable
{
    static final long serialVersionUID = 9011180388985266884L;
    public float x;
    public float y;
    
    public Tuple2f(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public Tuple2f(final float[] array) {
        this.x = array[0];
        this.y = array[1];
    }
    
    public Tuple2f(final Tuple2f tuple2f) {
        this.x = tuple2f.x;
        this.y = tuple2f.y;
    }
    
    public Tuple2f(final Tuple2d tuple2d) {
        this.x = (float)tuple2d.x;
        this.y = (float)tuple2d.y;
    }
    
    public Tuple2f() {
        this.x = 0.0f;
        this.y = 0.0f;
    }
    
    public final void set(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public final void set(final float[] array) {
        this.x = array[0];
        this.y = array[1];
    }
    
    public final void set(final Tuple2f tuple2f) {
        this.x = tuple2f.x;
        this.y = tuple2f.y;
    }
    
    public final void set(final Tuple2d tuple2d) {
        this.x = (float)tuple2d.x;
        this.y = (float)tuple2d.y;
    }
    
    public final void get(final float[] array) {
        array[0] = this.x;
        array[1] = this.y;
    }
    
    public final void add(final Tuple2f tuple2f, final Tuple2f tuple2f2) {
        this.x = tuple2f.x + tuple2f2.x;
        this.y = tuple2f.y + tuple2f2.y;
    }
    
    public final void add(final Tuple2f tuple2f) {
        this.x += tuple2f.x;
        this.y += tuple2f.y;
    }
    
    public final void sub(final Tuple2f tuple2f, final Tuple2f tuple2f2) {
        this.x = tuple2f.x - tuple2f2.x;
        this.y = tuple2f.y - tuple2f2.y;
    }
    
    public final void sub(final Tuple2f tuple2f) {
        this.x -= tuple2f.x;
        this.y -= tuple2f.y;
    }
    
    public final void negate(final Tuple2f tuple2f) {
        this.x = -tuple2f.x;
        this.y = -tuple2f.y;
    }
    
    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }
    
    public final void scale(final float n, final Tuple2f tuple2f) {
        this.x = n * tuple2f.x;
        this.y = n * tuple2f.y;
    }
    
    public final void scale(final float n) {
        this.x *= n;
        this.y *= n;
    }
    
    public final void scaleAdd(final float n, final Tuple2f tuple2f, final Tuple2f tuple2f2) {
        this.x = n * tuple2f.x + tuple2f2.x;
        this.y = n * tuple2f.y + tuple2f2.y;
    }
    
    public final void scaleAdd(final float n, final Tuple2f tuple2f) {
        this.x = n * this.x + tuple2f.x;
        this.y = n * this.y + tuple2f.y;
    }
    
    public int hashCode() {
        final long n = 31L * (31L * 1L + VecMathUtil.floatToIntBits(this.x)) + VecMathUtil.floatToIntBits(this.y);
        return (int)(n ^ n >> 32);
    }
    
    public boolean equals(final Tuple2f tuple2f) {
        try {
            return this.x == tuple2f.x && this.y == tuple2f.y;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o) {
        try {
            final Tuple2f tuple2f = (Tuple2f)o;
            return this.x == tuple2f.x && this.y == tuple2f.y;
        }
        catch (NullPointerException ex) {
            return false;
        }
        catch (ClassCastException ex2) {
            return false;
        }
    }
    
    public boolean epsilonEquals(final Tuple2f tuple2f, final float n) {
        final float n2 = this.x - tuple2f.x;
        if (((n2 < 0.0f) ? (-n2) : n2) > n) {
            return false;
        }
        final float n3 = this.y - tuple2f.y;
        return ((n3 < 0.0f) ? (-n3) : n3) <= n;
    }
    
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
    
    public final void clamp(final float n, final float n2, final Tuple2f tuple2f) {
        if (tuple2f.x > n2) {
            this.x = n2;
        }
        else if (tuple2f.x < n) {
            this.x = n;
        }
        else {
            this.x = tuple2f.x;
        }
        if (tuple2f.y > n2) {
            this.y = n2;
        }
        else if (tuple2f.y < n) {
            this.y = n;
        }
        else {
            this.y = tuple2f.y;
        }
    }
    
    public final void clampMin(final float n, final Tuple2f tuple2f) {
        if (tuple2f.x < n) {
            this.x = n;
        }
        else {
            this.x = tuple2f.x;
        }
        if (tuple2f.y < n) {
            this.y = n;
        }
        else {
            this.y = tuple2f.y;
        }
    }
    
    public final void clampMax(final float n, final Tuple2f tuple2f) {
        if (tuple2f.x > n) {
            this.x = n;
        }
        else {
            this.x = tuple2f.x;
        }
        if (tuple2f.y > n) {
            this.y = n;
        }
        else {
            this.y = tuple2f.y;
        }
    }
    
    public final void absolute(final Tuple2f tuple2f) {
        this.x = Math.abs(tuple2f.x);
        this.y = Math.abs(tuple2f.y);
    }
    
    public final void clamp(final float n, final float n2) {
        if (this.x > n2) {
            this.x = n2;
        }
        else if (this.x < n) {
            this.x = n;
        }
        if (this.y > n2) {
            this.y = n2;
        }
        else if (this.y < n) {
            this.y = n;
        }
    }
    
    public final void clampMin(final float n) {
        if (this.x < n) {
            this.x = n;
        }
        if (this.y < n) {
            this.y = n;
        }
    }
    
    public final void clampMax(final float n) {
        if (this.x > n) {
            this.x = n;
        }
        if (this.y > n) {
            this.y = n;
        }
    }
    
    public final void absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
    }
    
    public final void interpolate(final Tuple2f tuple2f, final Tuple2f tuple2f2, final float n) {
        this.x = (1.0f - n) * tuple2f.x + n * tuple2f2.x;
        this.y = (1.0f - n) * tuple2f.y + n * tuple2f2.y;
    }
    
    public final void interpolate(final Tuple2f tuple2f, final float n) {
        this.x = (1.0f - n) * this.x + n * tuple2f.x;
        this.y = (1.0f - n) * this.y + n * tuple2f.y;
    }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }
}
