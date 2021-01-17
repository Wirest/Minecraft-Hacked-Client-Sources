// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util;

public interface WritableRectangle extends WritablePoint, WritableDimension
{
    void setBounds(final int p0, final int p1, final int p2, final int p3);
    
    void setBounds(final ReadablePoint p0, final ReadableDimension p1);
    
    void setBounds(final ReadableRectangle p0);
}
