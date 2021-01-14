
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple4b;

public class Color4b
        extends Tuple4b
        implements Serializable {
    public Color4b(byte c1, byte c2, byte c3, byte c4) {
        super(c1, c2, c3, c4);
    }

    public Color4b(byte[] c2) {
        super(c2);
    }

    public Color4b(Color4b c1) {
        super(c1);
    }

    public Color4b(Tuple4b t1) {
        super(t1);
    }

    public Color4b() {
    }
}

