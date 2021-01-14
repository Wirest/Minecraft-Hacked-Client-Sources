package org.lwjgl.opengl;

public final class DisplayMode {
    private final int width;
    private final int height;
    private final int bpp;
    private final int freq;
    private final boolean fullscreen;

    public DisplayMode(int paramInt1, int paramInt2) {
        this(paramInt1, paramInt2, 0, 0, false);
    }

    DisplayMode(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        this(paramInt1, paramInt2, paramInt3, paramInt4, true);
    }

    private DisplayMode(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
        this.width = paramInt1;
        this.height = paramInt2;
        this.bpp = paramInt3;
        this.freq = paramInt4;
        this.fullscreen = paramBoolean;
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

    public boolean equals(Object paramObject) {
        if ((paramObject == null) || (!(paramObject instanceof DisplayMode))) {
            return false;
        }
        DisplayMode localDisplayMode = (DisplayMode) paramObject;
        return (localDisplayMode.width == this.width) && (localDisplayMode.height == this.height) && (localDisplayMode.bpp == this.bpp) && (localDisplayMode.freq == this.freq);
    }

    public int hashCode() {
        return this.width + this.height + this.freq + this.bpp;
    }

    public String toString() {
        StringBuilder localStringBuilder = new StringBuilder(32);
        localStringBuilder.append(this.width);
        localStringBuilder.append(" x ");
        localStringBuilder.append(this.height);
        localStringBuilder.append(" x ");
        localStringBuilder.append(this.bpp);
        localStringBuilder.append(" @");
        localStringBuilder.append(this.freq);
        localStringBuilder.append("Hz");
        return localStringBuilder.toString();
    }
}




