// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple4i implements Serializable, Cloneable
{
    static final long serialVersionUID = 8064614250942616720L;
    public int x;
    public int y;
    public int z;
    public int w;
    
    public Tuple4i(final int x, final int y, final int z, final int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Tuple4i(final int[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
        this.w = array[3];
    }
    
    public Tuple4i(final Tuple4i tuple4i) {
        this.x = tuple4i.x;
        this.y = tuple4i.y;
        this.z = tuple4i.z;
        this.w = tuple4i.w;
    }
    
    public Tuple4i() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
    }
    
    public final void set(final int x, final int y, final int z, final int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public final void set(final int[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
        this.w = array[3];
    }
    
    public final void set(final Tuple4i tuple4i) {
        this.x = tuple4i.x;
        this.y = tuple4i.y;
        this.z = tuple4i.z;
        this.w = tuple4i.w;
    }
    
    public final void get(final int[] array) {
        array[0] = this.x;
        array[1] = this.y;
        array[2] = this.z;
        array[3] = this.w;
    }
    
    public final void get(final Tuple4i tuple4i) {
        tuple4i.x = this.x;
        tuple4i.y = this.y;
        tuple4i.z = this.z;
        tuple4i.w = this.w;
    }
    
    public final void add(final Tuple4i tuple4i, final Tuple4i tuple4i2) {
        this.x = tuple4i.x + tuple4i2.x;
        this.y = tuple4i.y + tuple4i2.y;
        this.z = tuple4i.z + tuple4i2.z;
        this.w = tuple4i.w + tuple4i2.w;
    }
    
    public final void add(final Tuple4i tuple4i) {
        this.x += tuple4i.x;
        this.y += tuple4i.y;
        this.z += tuple4i.z;
        this.w += tuple4i.w;
    }
    
    public final void sub(final Tuple4i tuple4i, final Tuple4i tuple4i2) {
        this.x = tuple4i.x - tuple4i2.x;
        this.y = tuple4i.y - tuple4i2.y;
        this.z = tuple4i.z - tuple4i2.z;
        this.w = tuple4i.w - tuple4i2.w;
    }
    
    public final void sub(final Tuple4i tuple4i) {
        this.x -= tuple4i.x;
        this.y -= tuple4i.y;
        this.z -= tuple4i.z;
        this.w -= tuple4i.w;
    }
    
    public final void negate(final Tuple4i tuple4i) {
        this.x = -tuple4i.x;
        this.y = -tuple4i.y;
        this.z = -tuple4i.z;
        this.w = -tuple4i.w;
    }
    
    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
    }
    
    public final void scale(final int n, final Tuple4i tuple4i) {
        this.x = n * tuple4i.x;
        this.y = n * tuple4i.y;
        this.z = n * tuple4i.z;
        this.w = n * tuple4i.w;
    }
    
    public final void scale(final int n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
        this.w *= n;
    }
    
    public final void scaleAdd(final int n, final Tuple4i tuple4i, final Tuple4i tuple4i2) {
        this.x = n * tuple4i.x + tuple4i2.x;
        this.y = n * tuple4i.y + tuple4i2.y;
        this.z = n * tuple4i.z + tuple4i2.z;
        this.w = n * tuple4i.w + tuple4i2.w;
    }
    
    public final void scaleAdd(final int n, final Tuple4i tuple4i) {
        this.x = n * this.x + tuple4i.x;
        this.y = n * this.y + tuple4i.y;
        this.z = n * this.z + tuple4i.z;
        this.w = n * this.w + tuple4i.w;
    }
    
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
    }
    
    public boolean equals(final Object o) {
        try {
            final Tuple4i tuple4i = (Tuple4i)o;
            return this.x == tuple4i.x && this.y == tuple4i.y && this.z == tuple4i.z && this.w == tuple4i.w;
        }
        catch (NullPointerException ex) {
            return false;
        }
        catch (ClassCastException ex2) {
            return false;
        }
    }
    
    public int hashCode() {
        final long n = 31L * (31L * (31L * (31L * 1L + this.x) + this.y) + this.z) + this.w;
        return (int)(n ^ n >> 32);
    }
    
    public final void clamp(final int n, final int n2, final Tuple4i tuple4i) {
        if (tuple4i.x > n2) {
            this.x = n2;
        }
        else if (tuple4i.x < n) {
            this.x = n;
        }
        else {
            this.x = tuple4i.x;
        }
        if (tuple4i.y > n2) {
            this.y = n2;
        }
        else if (tuple4i.y < n) {
            this.y = n;
        }
        else {
            this.y = tuple4i.y;
        }
        if (tuple4i.z > n2) {
            this.z = n2;
        }
        else if (tuple4i.z < n) {
            this.z = n;
        }
        else {
            this.z = tuple4i.z;
        }
        if (tuple4i.w > n2) {
            this.w = n2;
        }
        else if (tuple4i.w < n) {
            this.w = n;
        }
        else {
            this.w = tuple4i.w;
        }
    }
    
    public final void clampMin(final int n, final Tuple4i tuple4i) {
        if (tuple4i.x < n) {
            this.x = n;
        }
        else {
            this.x = tuple4i.x;
        }
        if (tuple4i.y < n) {
            this.y = n;
        }
        else {
            this.y = tuple4i.y;
        }
        if (tuple4i.z < n) {
            this.z = n;
        }
        else {
            this.z = tuple4i.z;
        }
        if (tuple4i.w < n) {
            this.w = n;
        }
        else {
            this.w = tuple4i.w;
        }
    }
    
    public final void clampMax(final int n, final Tuple4i tuple4i) {
        if (tuple4i.x > n) {
            this.x = n;
        }
        else {
            this.x = tuple4i.x;
        }
        if (tuple4i.y > n) {
            this.y = n;
        }
        else {
            this.y = tuple4i.y;
        }
        if (tuple4i.z > n) {
            this.z = n;
        }
        else {
            this.z = tuple4i.z;
        }
        if (tuple4i.w > n) {
            this.w = n;
        }
        else {
            this.w = tuple4i.z;
        }
    }
    
    public final void absolute(final Tuple4i tuple4i) {
        this.x = Math.abs(tuple4i.x);
        this.y = Math.abs(tuple4i.y);
        this.z = Math.abs(tuple4i.z);
        this.w = Math.abs(tuple4i.w);
    }
    
    public final void clamp(final int n, final int n2) {
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
    
    public final void clampMin(final int n) {
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
    
    public final void clampMax(final int n) {
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
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }
}
