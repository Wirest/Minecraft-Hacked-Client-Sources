
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Point2d;
import javax.vecmath.Tuple2d;
import javax.vecmath.Tuple2f;

public class Point2f
        extends Tuple2f
        implements Serializable {
    public Point2f(float x, float y2) {
        super(x, y2);
    }

    public Point2f(float[] p2) {
        super(p2);
    }

    public Point2f(Point2f p1) {
        super(p1);
    }

    public Point2f(Point2d p1) {
        super(p1);
    }

    public Point2f(Tuple2f t1) {
        super(t1);
    }

    public Point2f(Tuple2d t1) {
        super(t1);
    }

    public Point2f() {
    }

    public final float distanceSquared(Point2f p1) {
        double dx = this.x - p1.x;
        double dy = this.y - p1.y;
        return (float) (dx * dx + dy * dy);
    }

    public final float distance(Point2f p1) {
        return (float) Math.sqrt(this.distanceSquared(p1));
    }

    public final float distanceL1(Point2f p1) {
        return Math.abs(this.x - p1.x) + Math.abs(this.y - p1.y);
    }

    public final float distanceLinf(Point2f p1) {
        return Math.max(Math.abs(this.x - p1.x), Math.abs(this.y - p1.y));
    }
}

