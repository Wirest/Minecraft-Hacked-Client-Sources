// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObjectEncoder;
import io.netty.util.CharsetUtil;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;

public class RtspRequestEncoder extends RtspObjectEncoder<HttpRequest>
{
    private static final byte[] CRLF;
    
    @Override
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return msg instanceof FullHttpRequest;
    }
    
    @Override
    protected void encodeInitialLine(final ByteBuf buf, final HttpRequest request) throws Exception {
        HttpHeaders.encodeAscii(request.getMethod().toString(), buf);
        buf.writeByte(32);
        buf.writeBytes(request.getUri().getBytes(CharsetUtil.UTF_8));
        buf.writeByte(32);
        HttpObjectEncoder.encodeAscii(request.getProtocolVersion().toString(), buf);
        buf.writeBytes(RtspRequestEncoder.CRLF);
    }
    
    static {
        CRLF = new byte[] { 13, 10 };
    }
}
