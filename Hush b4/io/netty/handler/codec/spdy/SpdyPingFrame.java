// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

public interface SpdyPingFrame extends SpdyFrame
{
    int id();
    
    SpdyPingFrame setId(final int p0);
}
