// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.timeout;

import io.netty.channel.ChannelException;

public class TimeoutException extends ChannelException
{
    private static final long serialVersionUID = 4673641882869672533L;
    
    TimeoutException() {
    }
    
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
