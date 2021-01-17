// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

public class UnsupportedHttpVersionException extends ProtocolException
{
    private static final long serialVersionUID = -1348448090193107031L;
    
    public UnsupportedHttpVersionException() {
    }
    
    public UnsupportedHttpVersionException(final String message) {
        super(message);
    }
}
