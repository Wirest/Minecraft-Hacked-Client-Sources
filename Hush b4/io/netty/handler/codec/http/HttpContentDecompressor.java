// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;

public class HttpContentDecompressor extends HttpContentDecoder
{
    private final boolean strict;
    
    public HttpContentDecompressor() {
        this(false);
    }
    
    public HttpContentDecompressor(final boolean strict) {
        this.strict = strict;
    }
    
    @Override
    protected EmbeddedChannel newContentDecoder(final String contentEncoding) throws Exception {
        if ("gzip".equalsIgnoreCase(contentEncoding) || "x-gzip".equalsIgnoreCase(contentEncoding)) {
            return new EmbeddedChannel(new ChannelHandler[] { ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP) });
        }
        if ("deflate".equalsIgnoreCase(contentEncoding) || "x-deflate".equalsIgnoreCase(contentEncoding)) {
            ZlibWrapper wrapper;
            if (this.strict) {
                wrapper = ZlibWrapper.ZLIB;
            }
            else {
                wrapper = ZlibWrapper.ZLIB_OR_NONE;
            }
            return new EmbeddedChannel(new ChannelHandler[] { ZlibCodecFactory.newZlibDecoder(wrapper) });
        }
        return null;
    }
}
