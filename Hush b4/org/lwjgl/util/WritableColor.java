// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util;

import java.nio.ByteBuffer;

public interface WritableColor
{
    void set(final int p0, final int p1, final int p2, final int p3);
    
    void set(final byte p0, final byte p1, final byte p2, final byte p3);
    
    void set(final int p0, final int p1, final int p2);
    
    void set(final byte p0, final byte p1, final byte p2);
    
    void setRed(final int p0);
    
    void setGreen(final int p0);
    
    void setBlue(final int p0);
    
    void setAlpha(final int p0);
    
    void setRed(final byte p0);
    
    void setGreen(final byte p0);
    
    void setBlue(final byte p0);
    
    void setAlpha(final byte p0);
    
    void readRGBA(final ByteBuffer p0);
    
    void readRGB(final ByteBuffer p0);
    
    void readARGB(final ByteBuffer p0);
    
    void readBGRA(final ByteBuffer p0);
    
    void readBGR(final ByteBuffer p0);
    
    void readABGR(final ByteBuffer p0);
    
    void setColor(final ReadableColor p0);
}
