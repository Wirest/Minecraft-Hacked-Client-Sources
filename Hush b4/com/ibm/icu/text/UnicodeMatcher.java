// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

public interface UnicodeMatcher
{
    public static final int U_MISMATCH = 0;
    public static final int U_PARTIAL_MATCH = 1;
    public static final int U_MATCH = 2;
    public static final char ETHER = '\uffff';
    
    int matches(final Replaceable p0, final int[] p1, final int p2, final boolean p3);
    
    String toPattern(final boolean p0);
    
    boolean matchesIndexValue(final int p0);
    
    void addMatchSetTo(final UnicodeSet p0);
}
