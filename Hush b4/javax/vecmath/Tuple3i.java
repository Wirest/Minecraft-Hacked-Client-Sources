// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple3i implements Serializable, Cloneable
{
    static final long serialVersionUID = -732740491767276200L;
    public int x;
    public int y;
    public int z;
    
    public Tuple3i(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Tuple3i(final int[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
    }
    
    public Tuple3i(final Tuple3i tuple3i) {
        this.x = tuple3i.x;
        this.y = tuple3i.y;
        this.z = tuple3i.z;
    }
    
    public Tuple3i() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    
    public final void set(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public final void set(final int[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
    }
    
    public final void set(final Tuple3i tuple3i) {
        this.x = tuple3i.x;
        this.y = tuple3i.y;
        this.z = tuple3i.z;
    }
    
    public final void get(final int[] array) {
        array[0] = this.x;
        array[1] = this.y;
        array[2] = this.z;
    }
    
    public final void get(final Tuple3i tuple3i) {
        tuple3i.x = this.x;
        tuple3i.y = this.y;
        tuple3i.z = this.z;
    }
    
    public final void add(final Tuple3i tuple3i, final Tuple3i tuple3i2) {
        this.x = tuple3i.x + tuple3i2.x;
        this.y = tuple3i.y + tuple3i2.y;
        this.z = tuple3i.z + tuple3i2.z;
    }
    
    public final void add(final Tuple3i tuple3i) {
        this.x += tuple3i.x;
        this.y += tuple3i.y;
        this.z += tuple3i.z;
    }
    
    public final void sub(final Tuple3i tuple3i, final Tuple3i tuple3i2) {
        this.x = tuple3i.x - tuple3i2.x;
        this.y = tuple3i.y - tuple3i2.y;
        this.z = tuple3i.z - tuple3i2.z;
    }
    
    public final void sub(final Tuple3i tuple3i) {
        this.x -= tuple3i.x;
        this.y -= tuple3i.y;
        this.z -= tuple3i.z;
    }
    
    public final void negate(final Tuple3i tuple3i) {
        this.x = -tuple3i.x;
        this.y = -tuple3i.y;
        this.z = -tuple3i.z;
    }
    
    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }
    
    public final void scale(final int n, final Tuple3i tuple3i) {
        this.x = n * tuple3i.x;
        this.y = n * tuple3i.y;
        this.z = n * tuple3i.z;
    }
    
    public final void scale(final int n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
    }
    
    public final void scaleAdd(final int n, final Tuple3i tuple3i, final Tuple3i tuple3i2) {
        this.x = n * tuple3i.x + tuple3i2.x;
        this.y = n * tuple3i.y + tuple3i2.y;
        this.z = n * tuple3i.z + tuple3i2.z;
    }
    
    public final void scaleAdd(final int n, final Tuple3i tuple3i) {
        this.x = n * this.x + tuple3i.x;
        this.y = n * this.y + tuple3i.y;
        this.z = n * this.z + tuple3i.z;
    }
    
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
    
    public boolean equals(final Object o) {
        try {
            final Tuple3i tuple3i = (Tuple3i)o;
            return this.x == tuple3i.x && this.y == tuple3i.y && this.z == tuple3i.z;
        }
        catch (NullPointerException ex) {
            return false;
        }
        catch (ClassCastException ex2) {
            return false;
        }
    }
    
    public int hashCode() {
        final long n = 31L * (31L * (31L * 1L + this.x) + this.y) + this.z;
        return (int)(n ^ n >> 32);
    }
    
    public final void clamp(final int z, final int z2, final Tuple3i tuple3i) {
        if (tuple3i.x > z2) {
            this.x = z2;
        }
        else if (tuple3i.x < z) {
            this.x = z;
        }
        else {
            this.x = tuple3i.x;
        }
        if (tuple3i.y > z2) {
            this.y = z2;
        }
        else if (tuple3i.y < z) {
            this.y = z;
        }
        else {
            this.y = tuple3i.y;
        }
        if (tuple3i.z > z2) {
            this.z = z2;
        }
        else if (tuple3i.z < z) {
            this.z = z;
        }
        else {
            this.z = tuple3i.z;
        }
    }
    
    public final void clampMin(final int z, final Tuple3i tuple3i) {
        if (tuple3i.x < z) {
            this.x = z;
        }
        else {
            this.x = tuple3i.x;
        }
        if (tuple3i.y < z) {
            this.y = z;
        }
        else {
            this.y = tuple3i.y;
        }
        if (tuple3i.z < z) {
            this.z = z;
        }
        else {
            this.z = tuple3i.z;
        }
    }
    
    public final void clampMax(final int z, final Tuple3i tuple3i) {
        if (tuple3i.x > z) {
            this.x = z;
        }
        else {
            this.x = tuple3i.x;
        }
        if (tuple3i.y > z) {
            this.y = z;
        }
        else {
            this.y = tuple3i.y;
        }
        if (tuple3i.z > z) {
            this.z = z;
        }
        else {
            this.z = tuple3i.z;
        }
    }
    
    public final void absolute(final Tuple3i tuple3i) {
        this.x = Math.abs(tuple3i.x);
        this.y = Math.abs(tuple3i.y);
        this.z = Math.abs(tuple3i.z);
    }
    
    public final void clamp(final int z, final int z2) {
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
    
    public final void clampMin(final int z) {
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
    
    public final void clampMax(final int z) {
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
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }
}
