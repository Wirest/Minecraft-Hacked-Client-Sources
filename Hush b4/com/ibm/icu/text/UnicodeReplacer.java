// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

interface UnicodeReplacer
{
    int replace(final Replaceable p0, final int p1, final int p2, final int[] p3);
    
    String toReplacerPattern(final boolean p0);
    
    void addReplacementSetTo(final UnicodeSet p0);
}
