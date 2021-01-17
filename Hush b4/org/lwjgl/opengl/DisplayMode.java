// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

public final class DisplayMode
{
    private final int width;
    private final int height;
    private final int bpp;
    private final int freq;
    private final boolean fullscreen;
    
    public DisplayMode(final int width, final int height) {
        this(width, height, 0, 0, false);
    }
    
    DisplayMode(final int width, final int height, final int bpp, final int freq) {
        this(width, height, bpp, freq, true);
    }
    
    private DisplayMode(final int width, final int height, final int bpp, final int freq, final boolean fullscreen) {
        this.width = width;
        this.height = height;
        this.bpp = bpp;
        this.freq = freq;
        this.fullscreen = fullscreen;
    }
    
    public boolean isFullscreenCapable() {
        return this.fullscreen;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getBitsPerPixel() {
        return this.bpp;
    }
    
    public int getFrequency() {
        return this.freq;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof DisplayMode)) {
            return false;
        }
        final DisplayMode dm = (DisplayMode)obj;
        return dm.width == this.width && dm.height == this.height && dm.bpp == this.bpp && dm.freq == this.freq;
    }
    
    @Override
    public int hashCode() {
        return this.width ^ this.height ^ this.freq ^ this.bpp;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(32);
        sb.append(this.width);
        sb.append(" x ");
        sb.append(this.height);
        sb.append(" x ");
        sb.append(this.bpp);
        sb.append(" @");
        sb.append(this.freq);
        sb.append("Hz");
        return sb.toString();
    }
}
