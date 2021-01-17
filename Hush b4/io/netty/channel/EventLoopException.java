// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

public class EventLoopException extends ChannelException
{
    private static final long serialVersionUID = -8969100344583703616L;
    
    public EventLoopException() {
    }
    
    public EventLoopException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public EventLoopException(final String message) {
        super(message);
    }
    
    public EventLoopException(final Throwable cause) {
        super(cause);
    }
}
