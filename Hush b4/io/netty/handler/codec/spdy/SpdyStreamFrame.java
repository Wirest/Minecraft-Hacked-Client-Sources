// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

public interface SpdyStreamFrame extends SpdyFrame
{
    int streamId();
    
    SpdyStreamFrame setStreamId(final int p0);
    
    boolean isLast();
    
    SpdyStreamFrame setLast(final boolean p0);
}
