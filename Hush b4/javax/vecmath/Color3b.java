// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.awt.Color;
import java.io.Serializable;

public class Color3b extends Tuple3b implements Serializable
{
    static final long serialVersionUID = 6632576088353444794L;
    
    public Color3b(final byte b, final byte b2, final byte b3) {
        super(b, b2, b3);
    }
    
    public Color3b(final byte[] array) {
        super(array);
    }
    
    public Color3b(final Color3b color3b) {
        super(color3b);
    }
    
    public Color3b(final Tuple3b tuple3b) {
        super(tuple3b);
    }
    
    public Color3b(final Color color) {
        super((byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue());
    }
    
    public Color3b() {
    }
    
    public final void set(final Color color) {
        this.x = (byte)color.getRed();
        this.y = (byte)color.getGreen();
        this.z = (byte)color.getBlue();
    }
    
    public final Color get() {
        return new Color(this.x & 0xFF, this.y & 0xFF, this.z & 0xFF);
    }
}
