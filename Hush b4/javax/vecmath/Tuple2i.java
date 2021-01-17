// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple2i implements Serializable, Cloneable
{
    static final long serialVersionUID = -3555701650170169638L;
    public int x;
    public int y;
    
    public Tuple2i(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public Tuple2i(final int[] array) {
        this.x = array[0];
        this.y = array[1];
    }
    
    public Tuple2i(final Tuple2i tuple2i) {
        this.x = tuple2i.x;
        this.y = tuple2i.y;
    }
    
    public Tuple2i() {
        this.x = 0;
        this.y = 0;
    }
    
    public final void set(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public final void set(final int[] array) {
        this.x = array[0];
        this.y = array[1];
    }
    
    public final void set(final Tuple2i tuple2i) {
        this.x = tuple2i.x;
        this.y = tuple2i.y;
    }
    
    public final void get(final int[] array) {
        array[0] = this.x;
        array[1] = this.y;
    }
    
    public final void get(final Tuple2i tuple2i) {
        tuple2i.x = this.x;
        tuple2i.y = this.y;
    }
    
    public final void add(final Tuple2i tuple2i, final Tuple2i tuple2i2) {
        this.x = tuple2i.x + tuple2i2.x;
        this.y = tuple2i.y + tuple2i2.y;
    }
    
    public final void add(final Tuple2i tuple2i) {
        this.x += tuple2i.x;
        this.y += tuple2i.y;
    }
    
    public final void sub(final Tuple2i tuple2i, final Tuple2i tuple2i2) {
        this.x = tuple2i.x - tuple2i2.x;
        this.y = tuple2i.y - tuple2i2.y;
    }
    
    public final void sub(final Tuple2i tuple2i) {
        this.x -= tuple2i.x;
        this.y -= tuple2i.y;
    }
    
    public final void negate(final Tuple2i tuple2i) {
        this.x = -tuple2i.x;
        this.y = -tuple2i.y;
    }
    
    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }
    
    public final void scale(final int n, final Tuple2i tuple2i) {
        this.x = n * tuple2i.x;
        this.y = n * tuple2i.y;
    }
    
    public final void scale(final int n) {
        this.x *= n;
        this.y *= n;
    }
    
    public final void scaleAdd(final int n, final Tuple2i tuple2i, final Tuple2i tuple2i2) {
        this.x = n * tuple2i.x + tuple2i2.x;
        this.y = n * tuple2i.y + tuple2i2.y;
    }
    
    public final void scaleAdd(final int n, final Tuple2i tuple2i) {
        this.x = n * this.x + tuple2i.x;
        this.y = n * this.y + tuple2i.y;
    }
    
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
    
    public boolean equals(final Object o) {
        try {
            final Tuple2i tuple2i = (Tuple2i)o;
            return this.x == tuple2i.x && this.y == tuple2i.y;
        }
        catch (NullPointerException ex) {
            return false;
        }
        catch (ClassCastException ex2) {
            return false;
        }
    }
    
    public int hashCode() {
        final long n = 31L * (31L * 1L + this.x) + this.y;
        return (int)(n ^ n >> 32);
    }
    
    public final void clamp(final int n, final int n2, final Tuple2i tuple2i) {
        if (tuple2i.x > n2) {
            this.x = n2;
        }
        else if (tuple2i.x < n) {
            this.x = n;
        }
        else {
            this.x = tuple2i.x;
        }
        if (tuple2i.y > n2) {
            this.y = n2;
        }
        else if (tuple2i.y < n) {
            this.y = n;
        }
        else {
            this.y = tuple2i.y;
        }
    }
    
    public final void clampMin(final int n, final Tuple2i tuple2i) {
        if (tuple2i.x < n) {
            this.x = n;
        }
        else {
            this.x = tuple2i.x;
        }
        if (tuple2i.y < n) {
            this.y = n;
        }
        else {
            this.y = tuple2i.y;
        }
    }
    
    public final void clampMax(final int n, final Tuple2i tuple2i) {
        if (tuple2i.x > n) {
            this.x = n;
        }
        else {
            this.x = tuple2i.x;
        }
        if (tuple2i.y > n) {
            this.y = n;
        }
        else {
            this.y = tuple2i.y;
        }
    }
    
    public final void absolute(final Tuple2i tuple2i) {
        this.x = Math.abs(tuple2i.x);
        this.y = Math.abs(tuple2i.y);
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
    }
    
    public final void clampMin(final int n) {
        if (this.x < n) {
            this.x = n;
        }
        if (this.y < n) {
            this.y = n;
        }
    }
    
    public final void clampMax(final int n) {
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
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }
}
