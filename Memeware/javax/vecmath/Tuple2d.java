
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple2f;

public abstract class Tuple2d
        implements Serializable {
    public double x;
    public double y;

    public Tuple2d(double x, double y2) {
        this.x = x;
        this.y = y2;
    }

    public Tuple2d(double[] t) {
        this.x = t[0];
        this.y = t[1];
    }

    public Tuple2d(Tuple2d t1) {
        this.x = t1.x;
        this.y = t1.y;
    }

    public Tuple2d(Tuple2f t1) {
        this.x = t1.x;
        this.y = t1.y;
    }

    public Tuple2d() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public final void set(double x, double y2) {
        this.x = x;
        this.y = y2;
    }

    public final void set(double[] t) {
        this.x = t[0];
        this.y = t[1];
    }

    public final void set(Tuple2d t1) {
        this.x = t1.x;
        this.y = t1.y;
    }

    public final void set(Tuple2f t1) {
        this.x = t1.x;
        this.y = t1.y;
    }

    public final void get(double[] t) {
        t[0] = this.x;
        t[1] = this.y;
    }

    public final void add(Tuple2d t1, Tuple2d t2) {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
    }

    public final void add(Tuple2d t1) {
        this.x += t1.x;
        this.y += t1.y;
    }

    public final void sub(Tuple2d t1, Tuple2d t2) {
        this.x = t1.x - t2.x;
        this.y = t1.y - t2.y;
    }

    public final void sub(Tuple2d t1) {
        this.x -= t1.x;
        this.y -= t1.y;
    }

    public final void negate(Tuple2d t1) {
        this.x = -t1.x;
        this.y = -t1.y;
    }

    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }

    public final void scale(double s, Tuple2d t1) {
        this.x = s * t1.x;
        this.y = s * t1.y;
    }

    public final void scale(double s) {
        this.x *= s;
        this.y *= s;
    }

    public final void scaleAdd(double s, Tuple2d t1, Tuple2d t2) {
        this.x = s * t1.x + t2.x;
        this.y = s * t1.y + t2.y;
    }

    public final void scaleAdd(double s, Tuple2d t1) {
        this.x = s * this.x + t1.x;
        this.y = s * this.y + t1.y;
    }

    public int hashCode() {
        long xbits = Double.doubleToLongBits(this.x);
        long ybits = Double.doubleToLongBits(this.y);
        return (int) (xbits ^ xbits >> 32 ^ ybits ^ ybits >> 32);
    }

    public boolean equals(Tuple2d t1) {
        return t1 != null && this.x == t1.x && this.y == t1.y;
    }

    public boolean equals(Object o1) {
        return o1 != null && o1 instanceof Tuple2d && this.equals((Tuple2d) o1);
    }

    public boolean epsilonEquals(Tuple2d t1, double epsilon) {
        return Math.abs(t1.x - this.x) <= epsilon && Math.abs(t1.y - this.y) <= epsilon;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public final void clamp(double min, double max, Tuple2d t) {
        this.set(t);
        this.clamp(min, max);
    }

    public final void clampMin(double min, Tuple2d t) {
        this.set(t);
        this.clampMin(min);
    }

    public final void clampMax(double max, Tuple2d t) {
        this.set(t);
        this.clampMax(max);
    }

    public final void absolute(Tuple2d t) {
        this.set(t);
        this.absolute();
    }

    public final void clamp(double min, double max) {
        this.clampMin(min);
        this.clampMax(max);
    }

    public final void clampMin(double min) {
        if (this.x < min) {
            this.x = min;
        }
        if (this.y < min) {
            this.y = min;
        }
    }

    public final void clampMax(double max) {
        if (this.x > max) {
            this.x = max;
        }
        if (this.y > max) {
            this.y = max;
        }
    }

    public final void absolute() {
        if (this.x < 0.0) {
            this.x = -this.x;
        }
        if (this.y < 0.0) {
            this.y = -this.y;
        }
    }

    public final void interpolate(Tuple2d t1, Tuple2d t2, double alpha) {
        this.set(t1);
        this.interpolate(t2, alpha);
    }

    public final void interpolate(Tuple2d t1, double alpha) {
        double beta = 1.0 - alpha;
        this.x = beta * this.x + alpha * t1.x;
        this.y = beta * this.y + alpha * t1.y;
    }
}

