package com.mentalfrostbyte.jello.ttf;

public interface FontRenderer
{
    int drawString(final FontData p0, final String p1, final int p2, final int p3, final int p4);
    
    int drawString(final String p0, final int p1, final int p2, final int p3);
    
    FontData getFontData();
}
