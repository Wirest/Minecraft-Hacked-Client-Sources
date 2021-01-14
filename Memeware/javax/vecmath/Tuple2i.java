
package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple2i
        implements Serializable,
        Cloneable {
    static final long serialVersionUID = -3555701650170169638L;
    public int x;
    public int y;

    public Tuple2i(int x, int y2) {
        this.x = x;
        this.y = y2;
    }

    public Tuple2i(int[] t) {
        this.x = t[0];
        this.y = t[1];
    }

    public Tuple2i(Tuple2i t1) {
        this.x = t1.x;
        this.y = t1.y;
    }

    public Tuple2i() {
        this.x = 0;
        this.y = 0;
    }

    public final void set(int x, int y2) {
        this.x = x;
        this.y = y2;
    }

    public final void set(int[] t) {
        this.x = t[0];
        this.y = t[1];
    }

    public final void set(Tuple2i t1) {
        this.x = t1.x;
        this.y = t1.y;
    }

    public final void get(int[] t) {
        t[0] = this.x;
        t[1] = this.y;
    }

    public final void get(Tuple2i t) {
        t.x = this.x;
        t.y = this.y;
    }

    public final void add(Tuple2i t1, Tuple2i t2) {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
    }

    public final void add(Tuple2i t1) {
        this.x += t1.x;
        this.y += t1.y;
    }

    public final void sub(Tuple2i t1, Tuple2i t2) {
        this.x = t1.x - t2.x;
        this.y = t1.y - t2.y;
    }

    public final void sub(Tuple2i t1) {
        this.x -= t1.x;
        this.y -= t1.y;
    }

    public final void negate(Tuple2i t1) {
        this.x = -t1.x;
        this.y = -t1.y;
    }

    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }

    public final void scale(int s, Tuple2i t1) {
        this.x = s * t1.x;
        this.y = s * t1.y;
    }

    public final void scale(int s) {
        this.x *= s;
        this.y *= s;
    }

    public final void scaleAdd(int s, Tuple2i t1, Tuple2i t2) {
        this.x = s * t1.x + t2.x;
        this.y = s * t1.y + t2.y;
    }

    public final void scaleAdd(int s, Tuple2i t1) {
        this.x = s * this.x + t1.x;
        this.y = s * this.y + t1.y;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public boolean equals(Object t1) {
        try {
            Tuple2i t2 = (Tuple2i) t1;
            return this.x == t2.x && this.y == t2.y;
        } catch (NullPointerException e2) {
            return false;
        } catch (ClassCastException e3) {
            return false;
        }
    }

    public int hashCode() {
        long bits = 1L;
        bits = 31L * bits + (long) this.x;
        bits = 31L * bits + (long) this.y;
        return (int) (bits ^ bits >> 32);
    }

    public final void clamp(int min, int max, Tuple2i t) {
        this.x = t.x > max ? max : (t.x < min ? min : t.x);
        this.y = t.y > max ? max : (t.y < min ? min : t.y);
    }

    public final void clampMin(int min, Tuple2i t) {
        this.x = t.x < min ? min : t.x;
        this.y = t.y < min ? min : t.y;
    }

    public final void clampMax(int max, Tuple2i t) {
        this.x = t.x > max ? max : t.x;
        this.y = t.y > max ? max : t.y;
    }

    public final void absolute(Tuple2i t) {
        this.x = Math.abs(t.x);
        this.y = Math.abs(t.y);
    }

    public final void clamp(int min, int max) {
        if (this.x > max) {
            this.x = max;
        } else if (this.x < min) {
            this.x = min;
        }
        if (this.y > max) {
            this.y = max;
        } else if (this.y < min) {
            this.y = min;
        }
    }

    public final void clampMin(int min) {
        if (this.x < min) {
            this.x = min;
        }
        if (this.y < min) {
            this.y = min;
        }
    }

    public final void clampMax(int max) {
        if (this.x > max) {
            this.x = max;
        }
        if (this.y > max) {
            this.y = max;
        }
    }

    public final void absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public final int getX() {
        return this.x;
    }

    public final void setX(int x) {
        this.x = x;
    }

    public final int getY() {
        return this.y;
    }

    public final void setY(int y2) {
        this.y = y2;
    }
}

