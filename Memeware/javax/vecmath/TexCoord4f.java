
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple4d;
import javax.vecmath.Tuple4f;

public class TexCoord4f
        extends Tuple4f
        implements Serializable {
    static final long serialVersionUID = -3517736544731446513L;

    public TexCoord4f(float x, float y2, float z, float w) {
        super(x, y2, z, w);
    }

    public TexCoord4f(float[] v) {
        super(v);
    }

    public TexCoord4f(TexCoord4f v1) {
        super(v1);
    }

    public TexCoord4f(Tuple4f t1) {
        super(t1);
    }

    public TexCoord4f(Tuple4d t1) {
        super(t1);
    }

    public TexCoord4f() {
    }
}

