
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

public class Color3f
        extends Tuple3f
        implements Serializable {
    public Color3f(float x, float y2, float z) {
        super(x, y2, z);
    }

    public Color3f(float[] c2) {
        super(c2);
    }

    public Color3f(Color3f c1) {
        super(c1);
    }

    public Color3f(Tuple3d t1) {
        super(t1);
    }

    public Color3f(Tuple3f t1) {
        super(t1);
    }

    public Color3f() {
    }
}

