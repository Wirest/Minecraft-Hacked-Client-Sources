// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple3b implements Serializable, Cloneable
{
    static final long serialVersionUID = -483782685323607044L;
    public byte x;
    public byte y;
    public byte z;
    
    public Tuple3b(final byte x, final byte y, final byte z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Tuple3b(final byte[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
    }
    
    public Tuple3b(final Tuple3b tuple3b) {
        this.x = tuple3b.x;
        this.y = tuple3b.y;
        this.z = tuple3b.z;
    }
    
    public Tuple3b() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    
    public String toString() {
        return "(" + (this.x & 0xFF) + ", " + (this.y & 0xFF) + ", " + (this.z & 0xFF) + ")";
    }
    
    public final void get(final byte[] array) {
        array[0] = this.x;
        array[1] = this.y;
        array[2] = this.z;
    }
    
    public final void get(final Tuple3b tuple3b) {
        tuple3b.x = this.x;
        tuple3b.y = this.y;
        tuple3b.z = this.z;
    }
    
    public final void set(final Tuple3b tuple3b) {
        this.x = tuple3b.x;
        this.y = tuple3b.y;
        this.z = tuple3b.z;
    }
    
    public final void set(final byte[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
    }
    
    public boolean equals(final Tuple3b tuple3b) {
        try {
            return this.x == tuple3b.x && this.y == tuple3b.y && this.z == tuple3b.z;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o) {
        try {
            final Tuple3b tuple3b = (Tuple3b)o;
            return this.x == tuple3b.x && this.y == tuple3b.y && this.z == tuple3b.z;
        }
        catch (NullPointerException ex) {
            return false;
        }
        catch (ClassCastException ex2) {
            return false;
        }
    }
    
    public int hashCode() {
        return (this.x & 0xFF) << 0 | (this.y & 0xFF) << 8 | (this.z & 0xFF) << 16;
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
