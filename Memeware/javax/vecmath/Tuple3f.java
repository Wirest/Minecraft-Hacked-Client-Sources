
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple3d;

public abstract class Tuple3f
        implements Serializable {
    public float x;
    public float y;
    public float z;

    public Tuple3f(float x, float y2, float z) {
        this.x = x;
        this.y = y2;
        this.z = z;
    }

    public Tuple3f(float[] t) {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
    }

    public Tuple3f(Tuple3f t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
    }

    public Tuple3f(Tuple3d t1) {
        this.x = (float) t1.x;
        this.y = (float) t1.y;
        this.z = (float) t1.z;
    }

    public Tuple3f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public final void set(float x, float y2, float z) {
        this.x = x;
        this.y = y2;
        this.z = z;
    }

    public final void set(float[] t) {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
    }

    public final void set(Tuple3f t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
    }

    public final void set(Tuple3d t1) {
        this.x = (float) t1.x;
        this.y = (float) t1.y;
        this.z = (float) t1.z;
    }

    public final void get(float[] t) {
        t[0] = this.x;
        t[1] = this.y;
        t[2] = this.z;
    }

    public final void get(Tuple3f t) {
        t.x = this.x;
        t.y = this.y;
        t.z = this.z;
    }

    public final void add(Tuple3f t1, Tuple3f t2) {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
        this.z = t1.z + t2.z;
    }

    public final void add(Tuple3f t1) {
        this.x += t1.x;
        this.y += t1.y;
        this.z += t1.z;
    }

    public final void sub(Tuple3f t1, Tuple3f t2) {
        this.x = t1.x - t2.x;
        this.y = t1.y - t2.y;
        this.z = t1.z - t2.z;
    }

    public final void sub(Tuple3f t1) {
        this.x -= t1.x;
        this.y -= t1.y;
        this.z -= t1.z;
    }

    public final void negate(Tuple3f t1) {
        this.x = -t1.x;
        this.y = -t1.y;
        this.z = -t1.z;
    }

    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public final void scale(float s, Tuple3f t1) {
        this.x = s * t1.x;
        this.y = s * t1.y;
        this.z = s * t1.z;
    }

    public final void scale(float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
    }

    public final void scaleAdd(float s, Tuple3f t1, Tuple3f t2) {
        this.x = s * t1.x + t2.x;
        this.y = s * t1.y + t2.y;
        this.z = s * t1.z + t2.z;
    }

    public final void scaleAdd(float s, Tuple3f t1) {
        this.x = s * this.x + t1.x;
        this.y = s * this.y + t1.y;
        this.z = s * this.z + t1.z;
    }

    public int hashCode() {
        int xbits = Float.floatToIntBits(this.x);
        int ybits = Float.floatToIntBits(this.y);
        int zbits = Float.floatToIntBits(this.z);
        return xbits ^ ybits ^ zbits;
    }

    public boolean equals(Tuple3f t1) {
        return t1 != null && this.x == t1.x && this.y == t1.y && this.z == t1.z;
    }

    public boolean epsilonEquals(Tuple3f t1, float epsilon) {
        return Math.abs(t1.x - this.x) <= epsilon && Math.abs(t1.y - this.y) <= epsilon && Math.abs(t1.z - this.z) <= epsilon;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public final void clamp(float min, float max, Tuple3f t) {
        this.set(t);
        this.clamp(min, max);
    }

    public final void clampMin(float min, Tuple3f t) {
        this.set(t);
        this.clampMin(min);
    }

    public final void clampMax(float max, Tuple3f t) {
        this.set(t);
        this.clampMax(max);
    }

    public final void absolute(Tuple3f t) {
        this.set(t);
        this.absolute();
    }

    public final void clamp(float min, float max) {
        this.clampMin(min);
        this.clampMax(max);
    }

    public final void clampMin(float min) {
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

    public final void clampMax(float max) {
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

    public final void interpolate(Tuple3f t1, Tuple3f t2, float alpha) {
        this.set(t1);
        this.interpolate(t2, alpha);
    }

    public final void interpolate(Tuple3f t1, float alpha) {
        float beta = 1.0f - alpha;
        this.x = beta * this.x + alpha * t1.x;
        this.y = beta * this.y + alpha * t1.y;
        this.z = beta * this.z + alpha * t1.z;
    }
}

