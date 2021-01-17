// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;

abstract class SpdyHeaderBlockDecoder
{
    static SpdyHeaderBlockDecoder newInstance(final SpdyVersion spdyVersion, final int maxHeaderSize) {
        return new SpdyHeaderBlockZlibDecoder(spdyVersion, maxHeaderSize);
    }
    
    abstract void decode(final ByteBuf p0, final SpdyHeadersFrame p1) throws Exception;
    
    abstract void endHeaderBlock(final SpdyHeadersFrame p0) throws Exception;
    
    abstract void end();
}
