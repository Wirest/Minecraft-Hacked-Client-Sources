// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.channel.CombinedChannelDuplexHandler;

public final class HttpServerCodec extends CombinedChannelDuplexHandler<HttpRequestDecoder, HttpResponseEncoder>
{
    public HttpServerCodec() {
        this(4096, 8192, 8192);
    }
    
    public HttpServerCodec(final int maxInitialLineLength, final int maxHeaderSize, final int maxChunkSize) {
        super(new HttpRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize), new HttpResponseEncoder());
    }
    
    public HttpServerCodec(final int maxInitialLineLength, final int maxHeaderSize, final int maxChunkSize, final boolean validateHeaders) {
        super(new HttpRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders), new HttpResponseEncoder());
    }
}
