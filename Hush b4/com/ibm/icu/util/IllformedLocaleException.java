// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

public class IllformedLocaleException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private int _errIdx;
    
    public IllformedLocaleException() {
        this._errIdx = -1;
    }
    
    public IllformedLocaleException(final String message) {
        super(message);
        this._errIdx = -1;
    }
    
    public IllformedLocaleException(final String message, final int errorIndex) {
        super(message + ((errorIndex < 0) ? "" : (" [at index " + errorIndex + "]")));
        this._errIdx = -1;
        this._errIdx = errorIndex;
    }
    
    public int getErrorIndex() {
        return this._errIdx;
    }
}
