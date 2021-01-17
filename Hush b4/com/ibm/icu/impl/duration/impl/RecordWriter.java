// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration.impl;

interface RecordWriter
{
    boolean open(final String p0);
    
    boolean close();
    
    void bool(final String p0, final boolean p1);
    
    void boolArray(final String p0, final boolean[] p1);
    
    void character(final String p0, final char p1);
    
    void characterArray(final String p0, final char[] p1);
    
    void namedIndex(final String p0, final String[] p1, final int p2);
    
    void namedIndexArray(final String p0, final String[] p1, final byte[] p2);
    
    void string(final String p0, final String p1);
    
    void stringArray(final String p0, final String[] p1);
    
    void stringTable(final String p0, final String[][] p1);
}
