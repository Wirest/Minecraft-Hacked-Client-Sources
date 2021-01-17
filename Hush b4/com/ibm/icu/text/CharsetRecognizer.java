// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

abstract class CharsetRecognizer
{
    abstract String getName();
    
    public String getLanguage() {
        return null;
    }
    
    abstract CharsetMatch match(final CharsetDetector p0);
}
