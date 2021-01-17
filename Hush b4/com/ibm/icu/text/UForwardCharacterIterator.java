// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

public interface UForwardCharacterIterator
{
    public static final int DONE = -1;
    
    int next();
    
    int nextCodePoint();
}
