
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple3f;

public class TexCoord3f
        extends Tuple3f
        implements Serializable {
    public TexCoord3f(float x, float y2, float z) {
        super(x, y2, z);
    }

    public TexCoord3f(float[] v) {
        super(v);
    }

    public TexCoord3f(TexCoord3f v1) {
        super(v1);
    }

    public TexCoord3f() {
    }
}

