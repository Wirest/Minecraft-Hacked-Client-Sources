// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

public class PrematureChannelClosureException extends CodecException
{
    private static final long serialVersionUID = 4907642202594703094L;
    
    public PrematureChannelClosureException() {
    }
    
    public PrematureChannelClosureException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public PrematureChannelClosureException(final String message) {
        super(message);
    }
    
    public PrematureChannelClosureException(final Throwable cause) {
        super(cause);
    }
}
