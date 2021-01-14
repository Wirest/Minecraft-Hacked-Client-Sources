
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple2f;

public class TexCoord2f
        extends Tuple2f
        implements Serializable {
    public TexCoord2f(float x, float y2) {
        super(x, y2);
    }

    public TexCoord2f(float[] v) {
        super(v);
    }

    public TexCoord2f(TexCoord2f v1) {
        super(v1);
    }

    public TexCoord2f(Tuple2f t1) {
        super(t1);
    }

    public TexCoord2f() {
    }
}

