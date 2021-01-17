// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.PlatformDependent;

abstract class SpdyHeaderBlockEncoder
{
    static SpdyHeaderBlockEncoder newInstance(final SpdyVersion version, final int compressionLevel, final int windowBits, final int memLevel) {
        if (PlatformDependent.javaVersion() >= 7) {
            return new SpdyHeaderBlockZlibEncoder(version, compressionLevel);
        }
        return new SpdyHeaderBlockJZlibEncoder(version, compressionLevel, windowBits, memLevel);
    }
    
    abstract ByteBuf encode(final SpdyHeadersFrame p0) throws Exception;
    
    abstract void end();
}
