// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple3d implements Serializable, Cloneable
{
    static final long serialVersionUID = 5542096614926168415L;
    public double x;
    public double y;
    public double z;
    
    public Tuple3d(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Tuple3d(final double[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
    }
    
    public Tuple3d(final Tuple3d tuple3d) {
        this.x = tuple3d.x;
        this.y = tuple3d.y;
        this.z = tuple3d.z;
    }
    
    public Tuple3d(final Tuple3f tuple3f) {
        this.x = tuple3f.x;
        this.y = tuple3f.y;
        this.z = tuple3f.z;
    }
    
    public Tuple3d() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }
    
    public final void set(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public final void set(final double[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
    }
    
    public final void set(final Tuple3d tuple3d) {
        this.x = tuple3d.x;
        this.y = tuple3d.y;
        this.z = tuple3d.z;
    }
    
    public final void set(final Tuple3f tuple3f) {
        this.x = tuple3f.x;
        this.y = tuple3f.y;
        this.z = tuple3f.z;
    }
    
    public final void get(final double[] array) {
        array[0] = this.x;
        array[1] = this.y;
        array[2] = this.z;
    }
    
    public final void get(final Tuple3d tuple3d) {
        tuple3d.x = this.x;
        tuple3d.y = this.y;
        tuple3d.z = this.z;
    }
    
    public final void add(final Tuple3d tuple3d, final Tuple3d tuple3d2) {
        this.x = tuple3d.x + tuple3d2.x;
        this.y = tuple3d.y + tuple3d2.y;
        this.z = tuple3d.z + tuple3d2.z;
    }
    
    public final void add(final Tuple3d tuple3d) {
        this.x += tuple3d.x;
        this.y += tuple3d.y;
        this.z += tuple3d.z;
    }
    
    public final void sub(final Tuple3d tuple3d, final Tuple3d tuple3d2) {
        this.x = tuple3d.x - tuple3d2.x;
        this.y = tuple3d.y - tuple3d2.y;
        this.z = tuple3d.z - tuple3d2.z;
    }
    
    public final void sub(final Tuple3d tuple3d) {
        this.x -= tuple3d.x;
        this.y -= tuple3d.y;
        this.z -= tuple3d.z;
    }
    
    public final void negate(final Tuple3d tuple3d) {
        this.x = -tuple3d.x;
        this.y = -tuple3d.y;
        this.z = -tuple3d.z;
    }
    
    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }
    
    public final void scale(final double n, final Tuple3d tuple3d) {
        this.x = n * tuple3d.x;
        this.y = n * tuple3d.y;
        this.z = n * tuple3d.z;
    }
    
    public final void scale(final double n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
    }
    
    public final void scaleAdd(final double n, final Tuple3d tuple3d, final Tuple3d tuple3d2) {
        this.x = n * tuple3d.x + tuple3d2.x;
        this.y = n * tuple3d.y + tuple3d2.y;
        this.z = n * tuple3d.z + tuple3d2.z;
    }
    
    public final void scaleAdd(final double n, final Tuple3f tuple3f) {
        this.scaleAdd(n, new Point3d(tuple3f));
    }
    
    public final void scaleAdd(final double n, final Tuple3d tuple3d) {
        this.x = n * this.x + tuple3d.x;
        this.y = n * this.y + tuple3d.y;
        this.z = n * this.z + tuple3d.z;
    }
    
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
    
    public int hashCode() {
        final long n = 31L * (31L * (31L * 1L + VecMathUtil.doubleToLongBits(this.x)) + VecMathUtil.doubleToLongBits(this.y)) + VecMathUtil.doubleToLongBits(this.z);
        return (int)(n ^ n >> 32);
    }
    
    public boolean equals(final Tuple3d tuple3d) {
        try {
            return this.x == tuple3d.x && this.y == tuple3d.y && this.z == tuple3d.z;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o) {
        try {
            final Tuple3d tuple3d = (Tuple3d)o;
            return this.x == tuple3d.x && this.y == tuple3d.y && this.z == tuple3d.z;
        }
        catch (ClassCastException ex) {
            return false;
        }
        catch (NullPointerException ex2) {
            return false;
        }
    }
    
    public boolean epsilonEquals(final Tuple3d tuple3d, final double n) {
        final double n2 = this.x - tuple3d.x;
        if (((n2 < 0.0) ? (-n2) : n2) > n) {
            return false;
        }
        final double n3 = this.y - tuple3d.y;
        if (((n3 < 0.0) ? (-n3) : n3) > n) {
            return false;
        }
        final double n4 = this.z - tuple3d.z;
        return ((n4 < 0.0) ? (-n4) : n4) <= n;
    }
    
    public final void clamp(final float n, final float n2, final Tuple3d tuple3d) {
        this.clamp(n, (double)n2, tuple3d);
    }
    
    public final void clamp(final double z, final double z2, final Tuple3d tuple3d) {
        if (tuple3d.x > z2) {
            this.x = z2;
        }
        else if (tuple3d.x < z) {
            this.x = z;
        }
        else {
            this.x = tuple3d.x;
        }
        if (tuple3d.y > z2) {
            this.y = z2;
        }
        else if (tuple3d.y < z) {
            this.y = z;
        }
        else {
            this.y = tuple3d.y;
        }
        if (tuple3d.z > z2) {
            this.z = z2;
        }
        else if (tuple3d.z < z) {
            this.z = z;
        }
        else {
            this.z = tuple3d.z;
        }
    }
    
    public final void clampMin(final float n, final Tuple3d tuple3d) {
        this.clampMin((double)n, tuple3d);
    }
    
    public final void clampMin(final double z, final Tuple3d tuple3d) {
        if (tuple3d.x < z) {
            this.x = z;
        }
        else {
            this.x = tuple3d.x;
        }
        if (tuple3d.y < z) {
            this.y = z;
        }
        else {
            this.y = tuple3d.y;
        }
        if (tuple3d.z < z) {
            this.z = z;
        }
        else {
            this.z = tuple3d.z;
        }
    }
    
    public final void clampMax(final float n, final Tuple3d tuple3d) {
        this.clampMax((double)n, tuple3d);
    }
    
    public final void clampMax(final double z, final Tuple3d tuple3d) {
        if (tuple3d.x > z) {
            this.x = z;
        }
        else {
            this.x = tuple3d.x;
        }
        if (tuple3d.y > z) {
            this.y = z;
        }
        else {
            this.y = tuple3d.y;
        }
        if (tuple3d.z > z) {
            this.z = z;
        }
        else {
            this.z = tuple3d.z;
        }
    }
    
    public final void absolute(final Tuple3d tuple3d) {
        this.x = Math.abs(tuple3d.x);
        this.y = Math.abs(tuple3d.y);
        this.z = Math.abs(tuple3d.z);
    }
    
    public final void clamp(final float n, final float n2) {
        this.clamp(n, (double)n2);
    }
    
    public final void clamp(final double z, final double z2) {
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
    
    public final void clampMin(final float n) {
        this.clampMin((double)n);
    }
    
    public final void clampMin(final double z) {
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
    
    public final void clampMax(final float n) {
        this.clampMax((double)n);
    }
    
    public final void clampMax(final double z) {
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
    
    public final void interpolate(final Tuple3d tuple3d, final Tuple3d tuple3d2, final float n) {
        this.interpolate(tuple3d, tuple3d2, (double)n);
    }
    
    public final void interpolate(final Tuple3d tuple3d, final Tuple3d tuple3d2, final double n) {
        this.x = (1.0 - n) * tuple3d.x + n * tuple3d2.x;
        this.y = (1.0 - n) * tuple3d.y + n * tuple3d2.y;
        this.z = (1.0 - n) * tuple3d.z + n * tuple3d2.z;
    }
    
    public final void interpolate(final Tuple3d tuple3d, final float n) {
        this.interpolate(tuple3d, (double)n);
    }
    
    public final void interpolate(final Tuple3d tuple3d, final double n) {
        this.x = (1.0 - n) * this.x + n * tuple3d.x;
        this.y = (1.0 - n) * this.y + n * tuple3d.y;
        this.z = (1.0 - n) * this.z + n * tuple3d.z;
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
