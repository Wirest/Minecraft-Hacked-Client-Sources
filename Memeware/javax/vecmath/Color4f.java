
package javax.vecmath;

import java.awt.Color;
import java.io.Serializable;
import javax.vecmath.Tuple4d;
import javax.vecmath.Tuple4f;

public class Color4f
        extends Tuple4f
        implements Serializable {
    public Color4f(float x, float y2, float z, float w) {
        super(x, y2, z, w);
    }

    public Color4f(float[] c2) {
        super(c2);
    }

    public Color4f(Color4f c1) {
        super(c1);
    }

    public Color4f(Tuple4d t1) {
        super(t1);
    }

    public Color4f(Tuple4f t1) {
        super(t1);
    }

    public Color4f() {
    }

    public final void set(Color color) {
        this.x = (float) color.getRed() / 255.0f;
        this.y = (float) color.getGreen() / 255.0f;
        this.z = (float) color.getBlue() / 255.0f;
        this.w = (float) color.getAlpha() / 255.0f;
    }

    public final Color get() {
        int r = Math.round(this.x * 255.0f);
        int g = Math.round(this.y * 255.0f);
        int b = Math.round(this.z * 255.0f);
        int a2 = Math.round(this.w * 255.0f);
        return new Color(r, g, b, a2);
    }
}

