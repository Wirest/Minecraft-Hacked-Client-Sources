// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

public interface RangeValueIterator
{
    boolean next(final Element p0);
    
    void reset();
    
    public static class Element
    {
        public int start;
        public int limit;
        public int value;
    }
}
