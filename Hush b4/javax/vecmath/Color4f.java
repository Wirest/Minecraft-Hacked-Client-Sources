// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.awt.Color;
import java.io.Serializable;

public class Color4f extends Tuple4f implements Serializable
{
    static final long serialVersionUID = 8577680141580006740L;
    
    public Color4f(final float n, final float n2, final float n3, final float n4) {
        super(n, n2, n3, n4);
    }
    
    public Color4f(final float[] array) {
        super(array);
    }
    
    public Color4f(final Color4f color4f) {
        super(color4f);
    }
    
    public Color4f(final Tuple4f tuple4f) {
        super(tuple4f);
    }
    
    public Color4f(final Tuple4d tuple4d) {
        super(tuple4d);
    }
    
    public Color4f(final Color color) {
        super(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public Color4f() {
    }
    
    public final void set(final Color color) {
        this.x = color.getRed() / 255.0f;
        this.y = color.getGreen() / 255.0f;
        this.z = color.getBlue() / 255.0f;
        this.w = color.getAlpha() / 255.0f;
    }
    
    public final Color get() {
        return new Color(Math.round(this.x * 255.0f), Math.round(this.y * 255.0f), Math.round(this.z * 255.0f), Math.round(this.w * 255.0f));
    }
}
