// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson;

public final class JsonIOException extends JsonParseException
{
    private static final long serialVersionUID = 1L;
    
    public JsonIOException(final String msg) {
        super(msg);
    }
    
    public JsonIOException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
    
    public JsonIOException(final Throwable cause) {
        super(cause);
    }
}
