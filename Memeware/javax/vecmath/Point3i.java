
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple3i;

public class Point3i
        extends Tuple3i
        implements Serializable {
    public Point3i(int x, int y2, int z) {
        super(x, y2, z);
    }

    public Point3i(int[] t) {
        super(t);
    }

    public Point3i(Point3i t1) {
        super(t1);
    }

    public Point3i() {
    }
}

