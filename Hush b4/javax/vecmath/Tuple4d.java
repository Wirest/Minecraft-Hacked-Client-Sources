// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple4d implements Serializable, Cloneable
{
    static final long serialVersionUID = -4748953690425311052L;
    public double x;
    public double y;
    public double z;
    public double w;
    
    public Tuple4d(final double x, final double y, final double z, final double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Tuple4d(final double[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
        this.w = array[3];
    }
    
    public Tuple4d(final Tuple4d tuple4d) {
        this.x = tuple4d.x;
        this.y = tuple4d.y;
        this.z = tuple4d.z;
        this.w = tuple4d.w;
    }
    
    public Tuple4d(final Tuple4f tuple4f) {
        this.x = tuple4f.x;
        this.y = tuple4f.y;
        this.z = tuple4f.z;
        this.w = tuple4f.w;
    }
    
    public Tuple4d() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        this.w = 0.0;
    }
    
    public final void set(final double x, final double y, final double z, final double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public final void set(final double[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
        this.w = array[3];
    }
    
    public final void set(final Tuple4d tuple4d) {
        this.x = tuple4d.x;
        this.y = tuple4d.y;
        this.z = tuple4d.z;
        this.w = tuple4d.w;
    }
    
    public final void set(final Tuple4f tuple4f) {
        this.x = tuple4f.x;
        this.y = tuple4f.y;
        this.z = tuple4f.z;
        this.w = tuple4f.w;
    }
    
    public final void get(final double[] array) {
        array[0] = this.x;
        array[1] = this.y;
        array[2] = this.z;
        array[3] = this.w;
    }
    
    public final void get(final Tuple4d tuple4d) {
        tuple4d.x = this.x;
        tuple4d.y = this.y;
        tuple4d.z = this.z;
        tuple4d.w = this.w;
    }
    
    public final void add(final Tuple4d tuple4d, final Tuple4d tuple4d2) {
        this.x = tuple4d.x + tuple4d2.x;
        this.y = tuple4d.y + tuple4d2.y;
        this.z = tuple4d.z + tuple4d2.z;
        this.w = tuple4d.w + tuple4d2.w;
    }
    
    public final void add(final Tuple4d tuple4d) {
        this.x += tuple4d.x;
        this.y += tuple4d.y;
        this.z += tuple4d.z;
        this.w += tuple4d.w;
    }
    
    public final void sub(final Tuple4d tuple4d, final Tuple4d tuple4d2) {
        this.x = tuple4d.x - tuple4d2.x;
        this.y = tuple4d.y - tuple4d2.y;
        this.z = tuple4d.z - tuple4d2.z;
        this.w = tuple4d.w - tuple4d2.w;
    }
    
    public final void sub(final Tuple4d tuple4d) {
        this.x -= tuple4d.x;
        this.y -= tuple4d.y;
        this.z -= tuple4d.z;
        this.w -= tuple4d.w;
    }
    
    public final void negate(final Tuple4d tuple4d) {
        this.x = -tuple4d.x;
        this.y = -tuple4d.y;
        this.z = -tuple4d.z;
        this.w = -tuple4d.w;
    }
    
    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
    }
    
    public final void scale(final double n, final Tuple4d tuple4d) {
        this.x = n * tuple4d.x;
        this.y = n * tuple4d.y;
        this.z = n * tuple4d.z;
        this.w = n * tuple4d.w;
    }
    
    public final void scale(final double n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
        this.w *= n;
    }
    
    public final void scaleAdd(final double n, final Tuple4d tuple4d, final Tuple4d tuple4d2) {
        this.x = n * tuple4d.x + tuple4d2.x;
        this.y = n * tuple4d.y + tuple4d2.y;
        this.z = n * tuple4d.z + tuple4d2.z;
        this.w = n * tuple4d.w + tuple4d2.w;
    }
    
    public final void scaleAdd(final float n, final Tuple4d tuple4d) {
        this.scaleAdd((double)n, tuple4d);
    }
    
    public final void scaleAdd(final double n, final Tuple4d tuple4d) {
        this.x = n * this.x + tuple4d.x;
        this.y = n * this.y + tuple4d.y;
        this.z = n * this.z + tuple4d.z;
        this.w = n * this.w + tuple4d.w;
    }
    
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
    }
    
    public boolean equals(final Tuple4d tuple4d) {
        try {
            return this.x == tuple4d.x && this.y == tuple4d.y && this.z == tuple4d.z && this.w == tuple4d.w;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o) {
        try {
            final Tuple4d tuple4d = (Tuple4d)o;
            return this.x == tuple4d.x && this.y == tuple4d.y && this.z == tuple4d.z && this.w == tuple4d.w;
        }
        catch (NullPointerException ex) {
            return false;
        }
        catch (ClassCastException ex2) {
            return false;
        }
    }
    
    public boolean epsilonEquals(final Tuple4d tuple4d, final double n) {
        final double n2 = this.x - tuple4d.x;
        if (((n2 < 0.0) ? (-n2) : n2) > n) {
            return false;
        }
        final double n3 = this.y - tuple4d.y;
        if (((n3 < 0.0) ? (-n3) : n3) > n) {
            return false;
        }
        final double n4 = this.z - tuple4d.z;
        if (((n4 < 0.0) ? (-n4) : n4) > n) {
            return false;
        }
        final double n5 = this.w - tuple4d.w;
        return ((n5 < 0.0) ? (-n5) : n5) <= n;
    }
    
    public int hashCode() {
        final long n = 31L * (31L * (31L * (31L * 1L + VecMathUtil.doubleToLongBits(this.x)) + VecMathUtil.doubleToLongBits(this.y)) + VecMathUtil.doubleToLongBits(this.z)) + VecMathUtil.doubleToLongBits(this.w);
        return (int)(n ^ n >> 32);
    }
    
    public final void clamp(final float n, final float n2, final Tuple4d tuple4d) {
        this.clamp(n, (double)n2, tuple4d);
    }
    
    public final void clamp(final double n, final double n2, final Tuple4d tuple4d) {
        if (tuple4d.x > n2) {
            this.x = n2;
        }
        else if (tuple4d.x < n) {
            this.x = n;
        }
        else {
            this.x = tuple4d.x;
        }
        if (tuple4d.y > n2) {
            this.y = n2;
        }
        else if (tuple4d.y < n) {
            this.y = n;
        }
        else {
            this.y = tuple4d.y;
        }
        if (tuple4d.z > n2) {
            this.z = n2;
        }
        else if (tuple4d.z < n) {
            this.z = n;
        }
        else {
            this.z = tuple4d.z;
        }
        if (tuple4d.w > n2) {
            this.w = n2;
        }
        else if (tuple4d.w < n) {
            this.w = n;
        }
        else {
            this.w = tuple4d.w;
        }
    }
    
    public final void clampMin(final float n, final Tuple4d tuple4d) {
        this.clampMin((double)n, tuple4d);
    }
    
    public final void clampMin(final double n, final Tuple4d tuple4d) {
        if (tuple4d.x < n) {
            this.x = n;
        }
        else {
            this.x = tuple4d.x;
        }
        if (tuple4d.y < n) {
            this.y = n;
        }
        else {
            this.y = tuple4d.y;
        }
        if (tuple4d.z < n) {
            this.z = n;
        }
        else {
            this.z = tuple4d.z;
        }
        if (tuple4d.w < n) {
            this.w = n;
        }
        else {
            this.w = tuple4d.w;
        }
    }
    
    public final void clampMax(final float n, final Tuple4d tuple4d) {
        this.clampMax((double)n, tuple4d);
    }
    
    public final void clampMax(final double n, final Tuple4d tuple4d) {
        if (tuple4d.x > n) {
            this.x = n;
        }
        else {
            this.x = tuple4d.x;
        }
        if (tuple4d.y > n) {
            this.y = n;
        }
        else {
            this.y = tuple4d.y;
        }
        if (tuple4d.z > n) {
            this.z = n;
        }
        else {
            this.z = tuple4d.z;
        }
        if (tuple4d.w > n) {
            this.w = n;
        }
        else {
            this.w = tuple4d.z;
        }
    }
    
    public final void absolute(final Tuple4d tuple4d) {
        this.x = Math.abs(tuple4d.x);
        this.y = Math.abs(tuple4d.y);
        this.z = Math.abs(tuple4d.z);
        this.w = Math.abs(tuple4d.w);
    }
    
    public final void clamp(final float n, final float n2) {
        this.clamp(n, (double)n2);
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
        this.clampMin((double)n);
    }
    
    public final void clampMin(final double n) {
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
        this.clampMax((double)n);
    }
    
    public final void clampMax(final double n) {
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
    
    public void interpolate(final Tuple4d tuple4d, final Tuple4d tuple4d2, final float n) {
        this.interpolate(tuple4d, tuple4d2, (double)n);
    }
    
    public void interpolate(final Tuple4d tuple4d, final Tuple4d tuple4d2, final double n) {
        this.x = (1.0 - n) * tuple4d.x + n * tuple4d2.x;
        this.y = (1.0 - n) * tuple4d.y + n * tuple4d2.y;
        this.z = (1.0 - n) * tuple4d.z + n * tuple4d2.z;
        this.w = (1.0 - n) * tuple4d.w + n * tuple4d2.w;
    }
    
    public void interpolate(final Tuple4d tuple4d, final float n) {
        this.interpolate(tuple4d, (double)n);
    }
    
    public void interpolate(final Tuple4d tuple4d, final double n) {
        this.x = (1.0 - n) * this.x + n * tuple4d.x;
        this.y = (1.0 - n) * this.y + n * tuple4d.y;
        this.z = (1.0 - n) * this.z + n * tuple4d.z;
        this.w = (1.0 - n) * this.w + n * tuple4d.w;
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
