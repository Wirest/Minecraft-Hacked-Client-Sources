// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

public interface XMLHandler
{
    void startDocument();
    
    void endDocument();
    
    void startElement(final char[] p0, final int p1, final int p2);
    
    void endElement();
    
    void attributeName(final char[] p0, final int p1, final int p2);
    
    void attributeValue(final char[] p0, final int p1, final int p2);
    
    void charData(final char[] p0, final int p1, final int p2);
}
