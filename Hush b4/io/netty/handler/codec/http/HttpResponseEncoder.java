// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;

public class HttpResponseEncoder extends HttpObjectEncoder<HttpResponse>
{
    private static final byte[] CRLF;
    
    @Override
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return super.acceptOutboundMessage(msg) && !(msg instanceof HttpRequest);
    }
    
    @Override
    protected void encodeInitialLine(final ByteBuf buf, final HttpResponse response) throws Exception {
        response.getProtocolVersion().encode(buf);
        buf.writeByte(32);
        response.getStatus().encode(buf);
        buf.writeBytes(HttpResponseEncoder.CRLF);
    }
    
    static {
        CRLF = new byte[] { 13, 10 };
    }
}
