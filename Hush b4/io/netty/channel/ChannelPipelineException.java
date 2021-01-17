// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

public class ChannelPipelineException extends ChannelException
{
    private static final long serialVersionUID = 3379174210419885980L;
    
    public ChannelPipelineException() {
    }
    
    public ChannelPipelineException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ChannelPipelineException(final String message) {
        super(message);
    }
    
    public ChannelPipelineException(final Throwable cause) {
        super(cause);
    }
}
