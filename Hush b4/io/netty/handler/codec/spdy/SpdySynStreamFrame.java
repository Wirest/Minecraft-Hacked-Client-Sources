// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

public interface SpdySynStreamFrame extends SpdyHeadersFrame
{
    int associatedStreamId();
    
    SpdySynStreamFrame setAssociatedStreamId(final int p0);
    
    byte priority();
    
    SpdySynStreamFrame setPriority(final byte p0);
    
    boolean isUnidirectional();
    
    SpdySynStreamFrame setUnidirectional(final boolean p0);
    
    SpdySynStreamFrame setStreamId(final int p0);
    
    SpdySynStreamFrame setLast(final boolean p0);
    
    SpdySynStreamFrame setInvalid();
}
