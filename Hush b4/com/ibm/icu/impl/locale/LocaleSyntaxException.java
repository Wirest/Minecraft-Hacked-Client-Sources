// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.locale;

public class LocaleSyntaxException extends Exception
{
    private static final long serialVersionUID = 1L;
    private int _index;
    
    public LocaleSyntaxException(final String msg) {
        this(msg, 0);
    }
    
    public LocaleSyntaxException(final String msg, final int errorIndex) {
        super(msg);
        this._index = -1;
        this._index = errorIndex;
    }
    
    public int getErrorIndex() {
        return this._index;
    }
}
