
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple3b;

public class Color3b
        extends Tuple3b
        implements Serializable {
    public Color3b(byte c1, byte c2, byte c3) {
        super(c1, c2, c3);
    }

    public Color3b(byte[] c2) {
        super(c2);
    }

    public Color3b(Color3b c1) {
        super(c1);
    }

    public Color3b(Tuple3b t1) {
        super(t1);
    }

    public Color3b() {
    }
}

