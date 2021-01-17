// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple3f implements Serializable, Cloneable
{
    static final long serialVersionUID = 5019834619484343712L;
    public float x;
    public float y;
    public float z;
    
    public Tuple3f(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Tuple3f(final float[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
    }
    
    public Tuple3f(final Tuple3f tuple3f) {
        this.x = tuple3f.x;
        this.y = tuple3f.y;
        this.z = tuple3f.z;
    }
    
    public Tuple3f(final Tuple3d tuple3d) {
        this.x = (float)tuple3d.x;
        this.y = (float)tuple3d.y;
        this.z = (float)tuple3d.z;
    }
    
    public Tuple3f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }
    
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
    
    public final void set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public final void set(final float[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
    }
    
    public final void set(final Tuple3f tuple3f) {
        this.x = tuple3f.x;
        this.y = tuple3f.y;
        this.z = tuple3f.z;
    }
    
    public final void set(final Tuple3d tuple3d) {
        this.x = (float)tuple3d.x;
        this.y = (float)tuple3d.y;
        this.z = (float)tuple3d.z;
    }
    
    public final void get(final float[] array) {
        array[0] = this.x;
        array[1] = this.y;
        array[2] = this.z;
    }
    
    public final void get(final Tuple3f tuple3f) {
        tuple3f.x = this.x;
        tuple3f.y = this.y;
        tuple3f.z = this.z;
    }
    
    public final void add(final Tuple3f tuple3f, final Tuple3f tuple3f2) {
        this.x = tuple3f.x + tuple3f2.x;
        this.y = tuple3f.y + tuple3f2.y;
        this.z = tuple3f.z + tuple3f2.z;
    }
    
    public final void add(final Tuple3f tuple3f) {
        this.x += tuple3f.x;
        this.y += tuple3f.y;
        this.z += tuple3f.z;
    }
    
    public final void sub(final Tuple3f tuple3f, final Tuple3f tuple3f2) {
        this.x = tuple3f.x - tuple3f2.x;
        this.y = tuple3f.y - tuple3f2.y;
        this.z = tuple3f.z - tuple3f2.z;
    }
    
    public final void sub(final Tuple3f tuple3f) {
        this.x -= tuple3f.x;
        this.y -= tuple3f.y;
        this.z -= tuple3f.z;
    }
    
    public final void negate(final Tuple3f tuple3f) {
        this.x = -tuple3f.x;
        this.y = -tuple3f.y;
        this.z = -tuple3f.z;
    }
    
    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }
    
    public final void scale(final float n, final Tuple3f tuple3f) {
        this.x = n * tuple3f.x;
        this.y = n * tuple3f.y;
        this.z = n * tuple3f.z;
    }
    
    public final void scale(final float n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
    }
    
    public final void scaleAdd(final float n, final Tuple3f tuple3f, final Tuple3f tuple3f2) {
        this.x = n * tuple3f.x + tuple3f2.x;
        this.y = n * tuple3f.y + tuple3f2.y;
        this.z = n * tuple3f.z + tuple3f2.z;
    }
    
    public final void scaleAdd(final float n, final Tuple3f tuple3f) {
        this.x = n * this.x + tuple3f.x;
        this.y = n * this.y + tuple3f.y;
        this.z = n * this.z + tuple3f.z;
    }
    
    public boolean equals(final Tuple3f tuple3f) {
        try {
            return this.x == tuple3f.x && this.y == tuple3f.y && this.z == tuple3f.z;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o) {
        try {
            final Tuple3f tuple3f = (Tuple3f)o;
            return this.x == tuple3f.x && this.y == tuple3f.y && this.z == tuple3f.z;
        }
        catch (NullPointerException ex) {
            return false;
        }
        catch (ClassCastException ex2) {
            return false;
        }
    }
    
    public boolean epsilonEquals(final Tuple3f tuple3f, final float n) {
        final float n2 = this.x - tuple3f.x;
        if (((n2 < 0.0f) ? (-n2) : n2) > n) {
            return false;
        }
        final float n3 = this.y - tuple3f.y;
        if (((n3 < 0.0f) ? (-n3) : n3) > n) {
            return false;
        }
        final float n4 = this.z - tuple3f.z;
        return ((n4 < 0.0f) ? (-n4) : n4) <= n;
    }
    
    public int hashCode() {
        final long n = 31L * (31L * (31L * 1L + VecMathUtil.floatToIntBits(this.x)) + VecMathUtil.floatToIntBits(this.y)) + VecMathUtil.floatToIntBits(this.z);
        return (int)(n ^ n >> 32);
    }
    
    public final void clamp(final float z, final float z2, final Tuple3f tuple3f) {
        if (tuple3f.x > z2) {
            this.x = z2;
        }
        else if (tuple3f.x < z) {
            this.x = z;
        }
        else {
            this.x = tuple3f.x;
        }
        if (tuple3f.y > z2) {
            this.y = z2;
        }
        else if (tuple3f.y < z) {
            this.y = z;
        }
        else {
            this.y = tuple3f.y;
        }
        if (tuple3f.z > z2) {
            this.z = z2;
        }
        else if (tuple3f.z < z) {
            this.z = z;
        }
        else {
            this.z = tuple3f.z;
        }
    }
    
    public final void clampMin(final float z, final Tuple3f tuple3f) {
        if (tuple3f.x < z) {
            this.x = z;
        }
        else {
            this.x = tuple3f.x;
        }
        if (tuple3f.y < z) {
            this.y = z;
        }
        else {
            this.y = tuple3f.y;
        }
        if (tuple3f.z < z) {
            this.z = z;
        }
        else {
            this.z = tuple3f.z;
        }
    }
    
    public final void clampMax(final float z, final Tuple3f tuple3f) {
        if (tuple3f.x > z) {
            this.x = z;
        }
        else {
            this.x = tuple3f.x;
        }
        if (tuple3f.y > z) {
            this.y = z;
        }
        else {
            this.y = tuple3f.y;
        }
        if (tuple3f.z > z) {
            this.z = z;
        }
        else {
            this.z = tuple3f.z;
        }
    }
    
    public final void absolute(final Tuple3f tuple3f) {
        this.x = Math.abs(tuple3f.x);
        this.y = Math.abs(tuple3f.y);
        this.z = Math.abs(tuple3f.z);
    }
    
    public final void clamp(final float z, final float z2) {
        if (this.x > z2) {
            this.x = z2;
        }
        else if (this.x < z) {
            this.x = z;
        }
        if (this.y > z2) {
            this.y = z2;
        }
        else if (this.y < z) {
            this.y = z;
        }
        if (this.z > z2) {
            this.z = z2;
        }
        else if (this.z < z) {
            this.z = z;
        }
    }
    
    public final void clampMin(final float z) {
        if (this.x < z) {
            this.x = z;
        }
        if (this.y < z) {
            this.y = z;
        }
        if (this.z < z) {
            this.z = z;
        }
    }
    
    public final void clampMax(final float z) {
        if (this.x > z) {
            this.x = z;
        }
        if (this.y > z) {
            this.y = z;
        }
        if (this.z > z) {
            this.z = z;
        }
    }
    
    public final void absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
    }
    
    public final void interpolate(final Tuple3f tuple3f, final Tuple3f tuple3f2, final float n) {
        this.x = (1.0f - n) * tuple3f.x + n * tuple3f2.x;
        this.y = (1.0f - n) * tuple3f.y + n * tuple3f2.y;
        this.z = (1.0f - n) * tuple3f.z + n * tuple3f2.z;
    }
    
    public final void interpolate(final Tuple3f tuple3f, final float n) {
        this.x = (1.0f - n) * this.x + n * tuple3f.x;
        this.y = (1.0f - n) * this.y + n * tuple3f.y;
        this.z = (1.0f - n) * this.z + n * tuple3f.z;
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
