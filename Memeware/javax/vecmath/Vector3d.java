
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;

public class Vector3d
        extends Tuple3d
        implements Serializable {
    public Vector3d(double x, double y2, double z) {
        super(x, y2, z);
    }

    public Vector3d(double[] v) {
        super(v);
    }

    public Vector3d(Vector3f v1) {
        super(v1);
    }

    public Vector3d(Vector3d v1) {
        super(v1);
    }

    public Vector3d(Tuple3d t1) {
        super(t1);
    }

    public Vector3d(Tuple3f t1) {
        super(t1);
    }

    public Vector3d() {
    }

    public Vector3d(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public Vector3d(Vec3i d) {
        this(d.getX(), d.getY(), d.getZ());
    }

    public final void cross(Vector3d v1, Vector3d v2) {
        this.set(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z, v1.x * v2.y - v1.y * v2.x);
    }

    public final void normalize(Vector3d v1) {
        this.set(v1);
        this.normalize();
    }

    public final void normalize() {
        double d = this.length();
        this.x /= d;
        this.y /= d;
        this.z /= d;
    }

    public final double dot(Vector3d v1) {
        return this.x * v1.x + this.y * v1.y + this.z * v1.z;
    }

    public final double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public final double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public final double angle(Vector3d v1) {
        double xx = this.y * v1.z - this.z * v1.y;
        double yy = this.z * v1.x - this.x * v1.z;
        double zz = this.x * v1.y - this.y * v1.x;
        double cross = Math.sqrt(xx * xx + yy * yy + zz * zz);
        return Math.abs(Math.atan2(cross, this.dot(v1)));
    }

    public Vector3d addVector(double d, double e, double f) {
        this.add(new Vector3d(d, e, f));
        return this;
    }

    public double squareDistanceTo(Vector3d vec) {
        double var2 = vec.x - this.x;
        double var3 = vec.y - this.y;
        double var4 = vec.z - this.z;
        return var2 * var2 + var3 * var3 + var4 * var4;
    }
}

