// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

public interface ValueIterator
{
    boolean next(final Element p0);
    
    void reset();
    
    void setRange(final int p0, final int p1);
    
    public static final class Element
    {
        public int integer;
        public Object value;
    }
}
