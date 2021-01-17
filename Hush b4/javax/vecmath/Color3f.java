// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.awt.Color;
import java.io.Serializable;

public class Color3f extends Tuple3f implements Serializable
{
    static final long serialVersionUID = -1861792981817493659L;
    
    public Color3f(final float n, final float n2, final float n3) {
        super(n, n2, n3);
    }
    
    public Color3f(final float[] array) {
        super(array);
    }
    
    public Color3f(final Color3f color3f) {
        super(color3f);
    }
    
    public Color3f(final Tuple3f tuple3f) {
        super(tuple3f);
    }
    
    public Color3f(final Tuple3d tuple3d) {
        super(tuple3d);
    }
    
    public Color3f(final Color color) {
        super(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
    }
    
    public Color3f() {
    }
    
    public final void set(final Color color) {
        this.x = color.getRed() / 255.0f;
        this.y = color.getGreen() / 255.0f;
        this.z = color.getBlue() / 255.0f;
    }
    
    public final Color get() {
        return new Color(Math.round(this.x * 255.0f), Math.round(this.y * 255.0f), Math.round(this.z * 255.0f));
    }
}
