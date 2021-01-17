// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple2d implements Serializable, Cloneable
{
    static final long serialVersionUID = 6205762482756093838L;
    public double x;
    public double y;
    
    public Tuple2d(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    public Tuple2d(final double[] array) {
        this.x = array[0];
        this.y = array[1];
    }
    
    public Tuple2d(final Tuple2d tuple2d) {
        this.x = tuple2d.x;
        this.y = tuple2d.y;
    }
    
    public Tuple2d(final Tuple2f tuple2f) {
        this.x = tuple2f.x;
        this.y = tuple2f.y;
    }
    
    public Tuple2d() {
        this.x = 0.0;
        this.y = 0.0;
    }
    
    public final void set(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    public final void set(final double[] array) {
        this.x = array[0];
        this.y = array[1];
    }
    
    public final void set(final Tuple2d tuple2d) {
        this.x = tuple2d.x;
        this.y = tuple2d.y;
    }
    
    public final void set(final Tuple2f tuple2f) {
        this.x = tuple2f.x;
        this.y = tuple2f.y;
    }
    
    public final void get(final double[] array) {
        array[0] = this.x;
        array[1] = this.y;
    }
    
    public final void add(final Tuple2d tuple2d, final Tuple2d tuple2d2) {
        this.x = tuple2d.x + tuple2d2.x;
        this.y = tuple2d.y + tuple2d2.y;
    }
    
    public final void add(final Tuple2d tuple2d) {
        this.x += tuple2d.x;
        this.y += tuple2d.y;
    }
    
    public final void sub(final Tuple2d tuple2d, final Tuple2d tuple2d2) {
        this.x = tuple2d.x - tuple2d2.x;
        this.y = tuple2d.y - tuple2d2.y;
    }
    
    public final void sub(final Tuple2d tuple2d) {
        this.x -= tuple2d.x;
        this.y -= tuple2d.y;
    }
    
    public final void negate(final Tuple2d tuple2d) {
        this.x = -tuple2d.x;
        this.y = -tuple2d.y;
    }
    
    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }
    
    public final void scale(final double n, final Tuple2d tuple2d) {
        this.x = n * tuple2d.x;
        this.y = n * tuple2d.y;
    }
    
    public final void scale(final double n) {
        this.x *= n;
        this.y *= n;
    }
    
    public final void scaleAdd(final double n, final Tuple2d tuple2d, final Tuple2d tuple2d2) {
        this.x = n * tuple2d.x + tuple2d2.x;
        this.y = n * tuple2d.y + tuple2d2.y;
    }
    
    public final void scaleAdd(final double n, final Tuple2d tuple2d) {
        this.x = n * this.x + tuple2d.x;
        this.y = n * this.y + tuple2d.y;
    }
    
    public int hashCode() {
        final long n = 31L * (31L * 1L + VecMathUtil.doubleToLongBits(this.x)) + VecMathUtil.doubleToLongBits(this.y);
        return (int)(n ^ n >> 32);
    }
    
    public boolean equals(final Tuple2d tuple2d) {
        try {
            return this.x == tuple2d.x && this.y == tuple2d.y;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o) {
        try {
            final Tuple2d tuple2d = (Tuple2d)o;
            return this.x == tuple2d.x && this.y == tuple2d.y;
        }
        catch (NullPointerException ex) {
            return false;
        }
        catch (ClassCastException ex2) {
            return false;
        }
    }
    
    public boolean epsilonEquals(final Tuple2d tuple2d, final double n) {
        final double n2 = this.x - tuple2d.x;
        if (((n2 < 0.0) ? (-n2) : n2) > n) {
            return false;
        }
        final double n3 = this.y - tuple2d.y;
        return ((n3 < 0.0) ? (-n3) : n3) <= n;
    }
    
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
    
    public final void clamp(final double n, final double n2, final Tuple2d tuple2d) {
        if (tuple2d.x > n2) {
            this.x = n2;
        }
        else if (tuple2d.x < n) {
            this.x = n;
        }
        else {
            this.x = tuple2d.x;
        }
        if (tuple2d.y > n2) {
            this.y = n2;
        }
        else if (tuple2d.y < n) {
            this.y = n;
        }
        else {
            this.y = tuple2d.y;
        }
    }
    
    public final void clampMin(final double n, final Tuple2d tuple2d) {
        if (tuple2d.x < n) {
            this.x = n;
        }
        else {
            this.x = tuple2d.x;
        }
        if (tuple2d.y < n) {
            this.y = n;
        }
        else {
            this.y = tuple2d.y;
        }
    }
    
    public final void clampMax(final double n, final Tuple2d tuple2d) {
        if (tuple2d.x > n) {
            this.x = n;
        }
        else {
            this.x = tuple2d.x;
        }
        if (tuple2d.y > n) {
            this.y = n;
        }
        else {
            this.y = tuple2d.y;
        }
    }
    
    public final void absolute(final Tuple2d tuple2d) {
        this.x = Math.abs(tuple2d.x);
        this.y = Math.abs(tuple2d.y);
    }
    
    public final void clamp(final double n, final double n2) {
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
    
    public final void clampMin(final double n) {
        if (this.x < n) {
            this.x = n;
        }
        if (this.y < n) {
            this.y = n;
        }
    }
    
    public final void clampMax(final double n) {
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
    
    public final void interpolate(final Tuple2d tuple2d, final Tuple2d tuple2d2, final double n) {
        this.x = (1.0 - n) * tuple2d.x + n * tuple2d2.x;
        this.y = (1.0 - n) * tuple2d.y + n * tuple2d2.y;
    }
    
    public final void interpolate(final Tuple2d tuple2d, final double n) {
        this.x = (1.0 - n) * this.x + n * tuple2d.x;
        this.y = (1.0 - n) * this.y + n * tuple2d.y;
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
