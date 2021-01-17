// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.locale;

public class Extension
{
    private char _key;
    protected String _value;
    
    protected Extension(final char key) {
        this._key = key;
    }
    
    Extension(final char key, final String value) {
        this._key = key;
        this._value = value;
    }
    
    public char getKey() {
        return this._key;
    }
    
    public String getValue() {
        return this._value;
    }
    
    public String getID() {
        return this._key + "-" + this._value;
    }
    
    @Override
    public String toString() {
        return this.getID();
    }
}
