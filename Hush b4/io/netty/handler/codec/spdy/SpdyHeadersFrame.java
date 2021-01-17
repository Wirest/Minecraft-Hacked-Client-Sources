// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

public interface SpdyHeadersFrame extends SpdyStreamFrame
{
    boolean isInvalid();
    
    SpdyHeadersFrame setInvalid();
    
    boolean isTruncated();
    
    SpdyHeadersFrame setTruncated();
    
    SpdyHeaders headers();
    
    SpdyHeadersFrame setStreamId(final int p0);
    
    SpdyHeadersFrame setLast(final boolean p0);
}
