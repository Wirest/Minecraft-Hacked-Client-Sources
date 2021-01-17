// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util;

import java.nio.ByteBuffer;

public interface ReadableColor
{
    public static final ReadableColor RED = new Color(255, 0, 0);
    public static final ReadableColor ORANGE = new Color(255, 128, 0);
    public static final ReadableColor YELLOW = new Color(255, 255, 0);
    public static final ReadableColor GREEN = new Color(0, 255, 0);
    public static final ReadableColor CYAN = new Color(0, 255, 255);
    public static final ReadableColor BLUE = new Color(0, 0, 255);
    public static final ReadableColor PURPLE = new Color(255, 0, 255);
    public static final ReadableColor WHITE = new Color(255, 255, 255);
    public static final ReadableColor BLACK = new Color(0, 0, 0);
    public static final ReadableColor LTGREY = new Color(192, 192, 192);
    public static final ReadableColor DKGREY = new Color(64, 64, 64);
    public static final ReadableColor GREY = new Color(128, 128, 128);
    
    int getRed();
    
    int getGreen();
    
    int getBlue();
    
    int getAlpha();
    
    byte getRedByte();
    
    byte getGreenByte();
    
    byte getBlueByte();
    
    byte getAlphaByte();
    
    void writeRGBA(final ByteBuffer p0);
    
    void writeRGB(final ByteBuffer p0);
    
    void writeABGR(final ByteBuffer p0);
    
    void writeBGR(final ByteBuffer p0);
    
    void writeBGRA(final ByteBuffer p0);
    
    void writeARGB(final ByteBuffer p0);
}
