// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

public class SpdyProtocolException extends Exception
{
    private static final long serialVersionUID = 7870000537743847264L;
    
    public SpdyProtocolException() {
    }
    
    public SpdyProtocolException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public SpdyProtocolException(final String message) {
        super(message);
    }
    
    public SpdyProtocolException(final Throwable cause) {
        super(cause);
    }
}
