// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.locale;

public class ParseStatus
{
    int _parseLength;
    int _errorIndex;
    String _errorMsg;
    
    public ParseStatus() {
        this._parseLength = 0;
        this._errorIndex = -1;
        this._errorMsg = null;
    }
    
    public void reset() {
        this._parseLength = 0;
        this._errorIndex = -1;
        this._errorMsg = null;
    }
    
    public boolean isError() {
        return this._errorIndex >= 0;
    }
    
    public int getErrorIndex() {
        return this._errorIndex;
    }
    
    public int getParseLength() {
        return this._parseLength;
    }
    
    public String getErrorMessage() {
        return this._errorMsg;
    }
}
