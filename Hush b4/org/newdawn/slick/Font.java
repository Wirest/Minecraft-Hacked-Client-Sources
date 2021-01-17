// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

public interface Font
{
    int getWidth(final String p0);
    
    int getHeight(final String p0);
    
    int getLineHeight();
    
    void drawString(final float p0, final float p1, final String p2);
    
    void drawString(final float p0, final float p1, final String p2, final Color p3);
    
    void drawString(final float p0, final float p1, final String p2, final Color p3, final int p4, final int p5);
}
