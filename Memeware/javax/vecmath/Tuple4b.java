
package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple4b
        implements Serializable {
    public byte x;
    public byte y;
    public byte z;
    public byte w;

    public Tuple4b(byte b1, byte b2, byte b3, byte b4) {
        this.x = b1;
        this.y = b2;
        this.z = b3;
        this.w = b4;
    }

    public Tuple4b(byte[] t) {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
        this.w = t[3];
    }

    public Tuple4b(Tuple4b t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
        this.w = t1.w;
    }

    public Tuple4b() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
    }

    public final void set(Tuple4b t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
        this.w = t1.w;
    }

    public final void set(byte[] t) {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
        this.w = t[3];
    }

    public final void get(byte[] t) {
        t[0] = this.x;
        t[1] = this.y;
        t[2] = this.z;
        t[3] = this.w;
    }

    public final void get(Tuple4b t) {
        t.x = this.x;
        t.y = this.y;
        t.z = this.z;
        t.w = this.w;
    }

    public int hashCode() {
        return this.x | this.y << 8 | this.z << 16 | this.w << 24;
    }

    public boolean equals(Tuple4b t1) {
        return t1 != null && this.x == t1.x && this.y == t1.y && this.z == t1.z && this.w == t1.w;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
    }
}

