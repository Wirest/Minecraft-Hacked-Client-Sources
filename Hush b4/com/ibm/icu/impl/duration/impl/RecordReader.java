// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration.impl;

interface RecordReader
{
    boolean open(final String p0);
    
    boolean close();
    
    boolean bool(final String p0);
    
    boolean[] boolArray(final String p0);
    
    char character(final String p0);
    
    char[] characterArray(final String p0);
    
    byte namedIndex(final String p0, final String[] p1);
    
    byte[] namedIndexArray(final String p0, final String[] p1);
    
    String string(final String p0);
    
    String[] stringArray(final String p0);
    
    String[][] stringTable(final String p0);
}
