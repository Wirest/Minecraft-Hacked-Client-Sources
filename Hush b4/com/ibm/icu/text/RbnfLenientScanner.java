// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

public interface RbnfLenientScanner
{
    boolean allIgnorable(final String p0);
    
    int prefixLength(final String p0, final String p1);
    
    int[] findText(final String p0, final String p1, final int p2);
}
