// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.awt.Color;
import java.io.Serializable;

public class Color4b extends Tuple4b implements Serializable
{
    static final long serialVersionUID = -105080578052502155L;
    
    public Color4b(final byte b, final byte b2, final byte b3, final byte b4) {
        super(b, b2, b3, b4);
    }
    
    public Color4b(final byte[] array) {
        super(array);
    }
    
    public Color4b(final Color4b color4b) {
        super(color4b);
    }
    
    public Color4b(final Tuple4b tuple4b) {
        super(tuple4b);
    }
    
    public Color4b(final Color color) {
        super((byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue(), (byte)color.getAlpha());
    }
    
    public Color4b() {
    }
    
    public final void set(final Color color) {
        this.x = (byte)color.getRed();
        this.y = (byte)color.getGreen();
        this.z = (byte)color.getBlue();
        this.w = (byte)color.getAlpha();
    }
    
    public final Color get() {
        return new Color(this.x & 0xFF, this.y & 0xFF, this.z & 0xFF, this.w & 0xFF);
    }
}
