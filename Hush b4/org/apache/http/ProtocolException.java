// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

public class ProtocolException extends HttpException
{
    private static final long serialVersionUID = -2143571074341228994L;
    
    public ProtocolException() {
    }
    
    public ProtocolException(final String message) {
        super(message);
    }
    
    public ProtocolException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
