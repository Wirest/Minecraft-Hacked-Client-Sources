// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

public interface SpdyGoAwayFrame extends SpdyFrame
{
    int lastGoodStreamId();
    
    SpdyGoAwayFrame setLastGoodStreamId(final int p0);
    
    SpdySessionStatus status();
    
    SpdyGoAwayFrame setStatus(final SpdySessionStatus p0);
}
