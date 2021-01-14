
package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple3i
        implements Serializable {
    public int x;
    public int y;
    public int z;

    public Tuple3i(int x, int y2, int z) {
        this.x = x;
        this.y = y2;
        this.z = z;
    }

    public Tuple3i(int[] t) {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
    }

    public Tuple3i(Tuple3i t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
    }

    public Tuple3i() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public final void set(int x, int y2, int z) {
        this.x = x;
        this.y = y2;
        this.z = z;
    }

    public final void set(int[] t) {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
    }

    public final void set(Tuple3i t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
    }

    public final void get(int[] t) {
        t[0] = this.x;
        t[1] = this.y;
        t[2] = this.z;
    }

    public final void get(Tuple3i t) {
        t.x = this.x;
        t.y = this.y;
        t.z = this.z;
    }

    public final void add(Tuple3i t1, Tuple3i t2) {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
        this.z = t1.z + t2.z;
    }

    public final void add(Tuple3i t1) {
        this.x += t1.x;
        this.y += t1.y;
        this.z += t1.z;
    }

    public final void sub(Tuple3i t1, Tuple3i t2) {
        this.x = t1.x - t2.x;
        this.y = t1.y - t2.y;
        this.z = t1.z - t2.z;
    }

    public final void sub(Tuple3i t1) {
        this.x -= t1.x;
        this.y -= t1.y;
        this.z -= t1.z;
    }

    public final void negate(Tuple3i t1) {
        this.x = -t1.x;
        this.y = -t1.y;
        this.z = -t1.z;
    }

    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public final void scale(int s, Tuple3i t1) {
        this.x = s * t1.x;
        this.y = s * t1.y;
        this.z = s * t1.z;
    }

    public final void scale(int s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
    }

    public final void scaleAdd(int s, Tuple3i t1, Tuple3i t2) {
        this.x = s * t1.x + t2.x;
        this.y = s * t1.y + t2.y;
        this.z = s * t1.z + t2.z;
    }

    public final void scaleAdd(int s, Tuple3i t1) {
        this.x = s * this.x + t1.x;
        this.y = s * this.y + t1.y;
        this.z = s * this.z + t1.z;
    }

    public int hashCode() {
        return this.x ^ this.y ^ this.z;
    }

    private boolean equals(Tuple3i t1) {
        return t1 != null && this.x == t1.x && this.y == t1.y && this.z == t1.z;
    }

    public boolean equals(Object o1) {
        return o1 != null && o1 instanceof Tuple3i && this.equals((Tuple3i) o1);
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public final void clamp(int min, int max, Tuple3i t) {
        this.set(t);
        this.clamp(min, max);
    }

    public final void clampMin(int min, Tuple3i t) {
        this.set(t);
        this.clampMin(min);
    }

    public final void clampMax(int max, Tuple3i t) {
        this.set(t);
        this.clampMax(max);
    }

    public final void absolute(Tuple3i t) {
        this.set(t);
        this.absolute();
    }

    public final void clamp(int min, int max) {
        this.clampMin(min);
        this.clampMax(max);
    }

    public final void clampMin(int min) {
        if (this.x < min) {
            this.x = min;
        }
        if (this.y < min) {
            this.y = min;
        }
        if (this.z < min) {
            this.z = min;
        }
    }

    public final void clampMax(int max) {
        if (this.x > max) {
            this.x = max;
        }
        if (this.y > max) {
            this.y = max;
        }
        if (this.z > max) {
            this.z = max;
        }
    }

    public final void absolute() {
        if ((double) this.x < 0.0) {
            this.x = -this.x;
        }
        if ((double) this.y < 0.0) {
            this.y = -this.y;
        }
        if ((double) this.z < 0.0) {
            this.z = -this.z;
        }
    }
}

