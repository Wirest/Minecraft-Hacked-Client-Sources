// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple4f implements Serializable, Cloneable
{
    static final long serialVersionUID = 7068460319248845763L;
    public float x;
    public float y;
    public float z;
    public float w;
    
    public Tuple4f(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Tuple4f(final float[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
        this.w = array[3];
    }
    
    public Tuple4f(final Tuple4f tuple4f) {
        this.x = tuple4f.x;
        this.y = tuple4f.y;
        this.z = tuple4f.z;
        this.w = tuple4f.w;
    }
    
    public Tuple4f(final Tuple4d tuple4d) {
        this.x = (float)tuple4d.x;
        this.y = (float)tuple4d.y;
        this.z = (float)tuple4d.z;
        this.w = (float)tuple4d.w;
    }
    
    public Tuple4f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 0.0f;
    }
    
    public final void set(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public final void set(final float[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
        this.w = array[3];
    }
    
    public final void set(final Tuple4f tuple4f) {
        this.x = tuple4f.x;
        this.y = tuple4f.y;
        this.z = tuple4f.z;
        this.w = tuple4f.w;
    }
    
    public final void set(final Tuple4d tuple4d) {
        this.x = (float)tuple4d.x;
        this.y = (float)tuple4d.y;
        this.z = (float)tuple4d.z;
        this.w = (float)tuple4d.w;
    }
    
    public final void get(final float[] array) {
        array[0] = this.x;
        array[1] = this.y;
        array[2] = this.z;
        array[3] = this.w;
    }
    
    public final void get(final Tuple4f tuple4f) {
        tuple4f.x = this.x;
        tuple4f.y = this.y;
        tuple4f.z = this.z;
        tuple4f.w = this.w;
    }
    
    public final void add(final Tuple4f tuple4f, final Tuple4f tuple4f2) {
        this.x = tuple4f.x + tuple4f2.x;
        this.y = tuple4f.y + tuple4f2.y;
        this.z = tuple4f.z + tuple4f2.z;
        this.w = tuple4f.w + tuple4f2.w;
    }
    
    public final void add(final Tuple4f tuple4f) {
        this.x += tuple4f.x;
        this.y += tuple4f.y;
        this.z += tuple4f.z;
        this.w += tuple4f.w;
    }
    
    public final void sub(final Tuple4f tuple4f, final Tuple4f tuple4f2) {
        this.x = tuple4f.x - tuple4f2.x;
        this.y = tuple4f.y - tuple4f2.y;
        this.z = tuple4f.z - tuple4f2.z;
        this.w = tuple4f.w - tuple4f2.w;
    }
    
    public final void sub(final Tuple4f tuple4f) {
        this.x -= tuple4f.x;
        this.y -= tuple4f.y;
        this.z -= tuple4f.z;
        this.w -= tuple4f.w;
    }
    
    public final void negate(final Tuple4f tuple4f) {
        this.x = -tuple4f.x;
        this.y = -tuple4f.y;
        this.z = -tuple4f.z;
        this.w = -tuple4f.w;
    }
    
    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
    }
    
    public final void scale(final float n, final Tuple4f tuple4f) {
        this.x = n * tuple4f.x;
        this.y = n * tuple4f.y;
        this.z = n * tuple4f.z;
        this.w = n * tuple4f.w;
    }
    
    public final void scale(final float n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
        this.w *= n;
    }
    
    public final void scaleAdd(final float n, final Tuple4f tuple4f, final Tuple4f tuple4f2) {
        this.x = n * tuple4f.x + tuple4f2.x;
        this.y = n * tuple4f.y + tuple4f2.y;
        this.z = n * tuple4f.z + tuple4f2.z;
        this.w = n * tuple4f.w + tuple4f2.w;
    }
    
    public final void scaleAdd(final float n, final Tuple4f tuple4f) {
        this.x = n * this.x + tuple4f.x;
        this.y = n * this.y + tuple4f.y;
        this.z = n * this.z + tuple4f.z;
        this.w = n * this.w + tuple4f.w;
    }
    
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
    }
    
    public boolean equals(final Tuple4f tuple4f) {
        try {
            return this.x == tuple4f.x && this.y == tuple4f.y && this.z == tuple4f.z && this.w == tuple4f.w;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o) {
        try {
            final Tuple4f tuple4f = (Tuple4f)o;
            return this.x == tuple4f.x && this.y == tuple4f.y && this.z == tuple4f.z && this.w == tuple4f.w;
        }
        catch (NullPointerException ex) {
            return false;
        }
        catch (ClassCastException ex2) {
            return false;
        }
    }
    
    public boolean epsilonEquals(final Tuple4f tuple4f, final float n) {
        final float n2 = this.x - tuple4f.x;
        if (((n2 < 0.0f) ? (-n2) : n2) > n) {
            return false;
        }
        final float n3 = this.y - tuple4f.y;
        if (((n3 < 0.0f) ? (-n3) : n3) > n) {
            return false;
        }
        final float n4 = this.z - tuple4f.z;
        if (((n4 < 0.0f) ? (-n4) : n4) > n) {
            return false;
        }
        final float n5 = this.w - tuple4f.w;
        return ((n5 < 0.0f) ? (-n5) : n5) <= n;
    }
    
    public int hashCode() {
        final long n = 31L * (31L * (31L * (31L * 1L + VecMathUtil.floatToIntBits(this.x)) + VecMathUtil.floatToIntBits(this.y)) + VecMathUtil.floatToIntBits(this.z)) + VecMathUtil.floatToIntBits(this.w);
        return (int)(n ^ n >> 32);
    }
    
    public final void clamp(final float n, final float n2, final Tuple4f tuple4f) {
        if (tuple4f.x > n2) {
            this.x = n2;
        }
        else if (tuple4f.x < n) {
            this.x = n;
        }
        else {
            this.x = tuple4f.x;
        }
        if (tuple4f.y > n2) {
            this.y = n2;
        }
        else if (tuple4f.y < n) {
            this.y = n;
        }
        else {
            this.y = tuple4f.y;
        }
        if (tuple4f.z > n2) {
            this.z = n2;
        }
        else if (tuple4f.z < n) {
            this.z = n;
        }
        else {
            this.z = tuple4f.z;
        }
        if (tuple4f.w > n2) {
            this.w = n2;
        }
        else if (tuple4f.w < n) {
            this.w = n;
        }
        else {
            this.w = tuple4f.w;
        }
    }
    
    public final void clampMin(final float n, final Tuple4f tuple4f) {
        if (tuple4f.x < n) {
            this.x = n;
        }
        else {
            this.x = tuple4f.x;
        }
        if (tuple4f.y < n) {
            this.y = n;
        }
        else {
            this.y = tuple4f.y;
        }
        if (tuple4f.z < n) {
            this.z = n;
        }
        else {
            this.z = tuple4f.z;
        }
        if (tuple4f.w < n) {
            this.w = n;
        }
        else {
            this.w = tuple4f.w;
        }
    }
    
    public final void clampMax(final float n, final Tuple4f tuple4f) {
        if (tuple4f.x > n) {
            this.x = n;
        }
        else {
            this.x = tuple4f.x;
        }
        if (tuple4f.y > n) {
            this.y = n;
        }
        else {
            this.y = tuple4f.y;
        }
        if (tuple4f.z > n) {
            this.z = n;
        }
        else {
            this.z = tuple4f.z;
        }
        if (tuple4f.w > n) {
            this.w = n;
        }
        else {
            this.w = tuple4f.z;
        }
    }
    
    public final void absolute(final Tuple4f tuple4f) {
        this.x = Math.abs(tuple4f.x);
        this.y = Math.abs(tuple4f.y);
        this.z = Math.abs(tuple4f.z);
        this.w = Math.abs(tuple4f.w);
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
        if (this.z > n2) {
            this.z = n2;
        }
        else if (this.z < n) {
            this.z = n;
        }
        if (this.w > n2) {
            this.w = n2;
        }
        else if (this.w < n) {
            this.w = n;
        }
    }
    
    public final void clampMin(final float n) {
        if (this.x < n) {
            this.x = n;
        }
        if (this.y < n) {
            this.y = n;
        }
        if (this.z < n) {
            this.z = n;
        }
        if (this.w < n) {
            this.w = n;
        }
    }
    
    public final void clampMax(final float n) {
        if (this.x > n) {
            this.x = n;
        }
        if (this.y > n) {
            this.y = n;
        }
        if (this.z > n) {
            this.z = n;
        }
        if (this.w > n) {
            this.w = n;
        }
    }
    
    public final void absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        this.w = Math.abs(this.w);
    }
    
    public void interpolate(final Tuple4f tuple4f, final Tuple4f tuple4f2, final float n) {
        this.x = (1.0f - n) * tuple4f.x + n * tuple4f2.x;
        this.y = (1.0f - n) * tuple4f.y + n * tuple4f2.y;
        this.z = (1.0f - n) * tuple4f.z + n * tuple4f2.z;
        this.w = (1.0f - n) * tuple4f.w + n * tuple4f2.w;
    }
    
    public void interpolate(final Tuple4f tuple4f, final float n) {
        this.x = (1.0f - n) * this.x + n * tuple4f.x;
        this.y = (1.0f - n) * this.y + n * tuple4f.y;
        this.z = (1.0f - n) * this.z + n * tuple4f.z;
        this.w = (1.0f - n) * this.w + n * tuple4f.w;
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
