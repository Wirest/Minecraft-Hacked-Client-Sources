// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

public interface Replaceable
{
    int length();
    
    char charAt(final int p0);
    
    int char32At(final int p0);
    
    void getChars(final int p0, final int p1, final char[] p2, final int p3);
    
    void replace(final int p0, final int p1, final String p2);
    
    void replace(final int p0, final int p1, final char[] p2, final int p3, final int p4);
    
    void copy(final int p0, final int p1, final int p2);
    
    boolean hasMetaData();
}
