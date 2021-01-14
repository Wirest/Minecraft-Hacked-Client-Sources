
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple4i;

public class Point4i
        extends Tuple4i
        implements Serializable {
    public Point4i(int x, int y2, int z, int w) {
        super(x, y2, z, w);
    }

    public Point4i(int[] t) {
        super(t);
    }

    public Point4i(Point4i t1) {
        super(t1);
    }

    public Point4i() {
    }
}

