// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBuf;

public class HttpRequestEncoder extends HttpObjectEncoder<HttpRequest>
{
    private static final char SLASH = '/';
    private static final char QUESTION_MARK = '?';
    private static final byte[] CRLF;
    
    @Override
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return super.acceptOutboundMessage(msg) && !(msg instanceof HttpResponse);
    }
    
    @Override
    protected void encodeInitialLine(final ByteBuf buf, final HttpRequest request) throws Exception {
        request.getMethod().encode(buf);
        buf.writeByte(32);
        String uri = request.getUri();
        if (uri.length() == 0) {
            uri += '/';
        }
        else {
            final int start = uri.indexOf("://");
            if (start != -1 && uri.charAt(0) != '/') {
                final int startIndex = start + 3;
                final int index = uri.indexOf(63, startIndex);
                if (index == -1) {
                    if (uri.lastIndexOf(47) <= startIndex) {
                        uri += '/';
                    }
                }
                else if (uri.lastIndexOf(47, index) <= startIndex) {
                    final int len = uri.length();
                    final StringBuilder sb = new StringBuilder(len + 1);
                    sb.append(uri, 0, index);
                    sb.append('/');
                    sb.append(uri, index, len);
                    uri = sb.toString();
                }
            }
        }
        buf.writeBytes(uri.getBytes(CharsetUtil.UTF_8));
        buf.writeByte(32);
        request.getProtocolVersion().encode(buf);
        buf.writeBytes(HttpRequestEncoder.CRLF);
    }
    
    static {
        CRLF = new byte[] { 13, 10 };
    }
}
