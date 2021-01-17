// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

public class ChannelException extends RuntimeException
{
    private static final long serialVersionUID = 2908618315971075004L;
    
    public ChannelException() {
    }
    
    public ChannelException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ChannelException(final String message) {
        super(message);
    }
    
    public ChannelException(final Throwable cause) {
        super(cause);
    }
}
