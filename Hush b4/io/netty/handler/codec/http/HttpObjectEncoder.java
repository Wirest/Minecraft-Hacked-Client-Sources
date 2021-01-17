// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.util.CharsetUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.FileRegion;
import io.netty.util.internal.StringUtil;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageEncoder;

public abstract class HttpObjectEncoder<H extends HttpMessage> extends MessageToMessageEncoder<Object>
{
    private static final byte[] CRLF;
    private static final byte[] ZERO_CRLF;
    private static final byte[] ZERO_CRLF_CRLF;
    private static final ByteBuf CRLF_BUF;
    private static final ByteBuf ZERO_CRLF_CRLF_BUF;
    private static final int ST_INIT = 0;
    private static final int ST_CONTENT_NON_CHUNK = 1;
    private static final int ST_CONTENT_CHUNK = 2;
    private int state;
    
    public HttpObjectEncoder() {
        this.state = 0;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Object msg, final List<Object> out) throws Exception {
        ByteBuf buf = null;
        if (msg instanceof HttpMessage) {
            if (this.state != 0) {
                throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
            }
            final H m = (H)msg;
            buf = ctx.alloc().buffer();
            this.encodeInitialLine(buf, m);
            HttpHeaders.encode(m.headers(), buf);
            buf.writeBytes(HttpObjectEncoder.CRLF);
            this.state = (HttpHeaders.isTransferEncodingChunked(m) ? 2 : 1);
        }
        if (msg instanceof HttpContent || msg instanceof ByteBuf || msg instanceof FileRegion) {
            if (this.state == 0) {
                throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
            }
            final long contentLength = contentLength(msg);
            if (this.state == 1) {
                if (contentLength > 0L) {
                    if (buf != null && buf.writableBytes() >= contentLength && msg instanceof HttpContent) {
                        buf.writeBytes(((HttpContent)msg).content());
                        out.add(buf);
                    }
                    else {
                        if (buf != null) {
                            out.add(buf);
                        }
                        out.add(encodeAndRetain(msg));
                    }
                }
                else if (buf != null) {
                    out.add(buf);
                }
                else {
                    out.add(Unpooled.EMPTY_BUFFER);
                }
                if (msg instanceof LastHttpContent) {
                    this.state = 0;
                }
            }
            else {
                if (this.state != 2) {
                    throw new Error();
                }
                if (buf != null) {
                    out.add(buf);
                }
                this.encodeChunkedContent(ctx, msg, contentLength, out);
            }
        }
        else if (buf != null) {
            out.add(buf);
        }
    }
    
    private void encodeChunkedContent(final ChannelHandlerContext ctx, final Object msg, final long contentLength, final List<Object> out) {
        if (contentLength > 0L) {
            final byte[] length = Long.toHexString(contentLength).getBytes(CharsetUtil.US_ASCII);
            final ByteBuf buf = ctx.alloc().buffer(length.length + 2);
            buf.writeBytes(length);
            buf.writeBytes(HttpObjectEncoder.CRLF);
            out.add(buf);
            out.add(encodeAndRetain(msg));
            out.add(HttpObjectEncoder.CRLF_BUF.duplicate());
        }
        if (msg instanceof LastHttpContent) {
            final HttpHeaders headers = ((LastHttpContent)msg).trailingHeaders();
            if (headers.isEmpty()) {
                out.add(HttpObjectEncoder.ZERO_CRLF_CRLF_BUF.duplicate());
            }
            else {
                final ByteBuf buf = ctx.alloc().buffer();
                buf.writeBytes(HttpObjectEncoder.ZERO_CRLF);
                HttpHeaders.encode(headers, buf);
                buf.writeBytes(HttpObjectEncoder.CRLF);
                out.add(buf);
            }
            this.state = 0;
        }
        else if (contentLength == 0L) {
            out.add(Unpooled.EMPTY_BUFFER);
        }
    }
    
    @Override
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return msg instanceof HttpObject || msg instanceof ByteBuf || msg instanceof FileRegion;
    }
    
    private static Object encodeAndRetain(final Object msg) {
        if (msg instanceof ByteBuf) {
            return ((ByteBuf)msg).retain();
        }
        if (msg instanceof HttpContent) {
            return ((HttpContent)msg).content().retain();
        }
        if (msg instanceof FileRegion) {
            return ((FileRegion)msg).retain();
        }
        throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
    }
    
    private static long contentLength(final Object msg) {
        if (msg instanceof HttpContent) {
            return ((HttpContent)msg).content().readableBytes();
        }
        if (msg instanceof ByteBuf) {
            return ((ByteBuf)msg).readableBytes();
        }
        if (msg instanceof FileRegion) {
            return ((FileRegion)msg).count();
        }
        throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
    }
    
    @Deprecated
    protected static void encodeAscii(final String s, final ByteBuf buf) {
        HttpHeaders.encodeAscii0(s, buf);
    }
    
    protected abstract void encodeInitialLine(final ByteBuf p0, final H p1) throws Exception;
    
    static {
        CRLF = new byte[] { 13, 10 };
        ZERO_CRLF = new byte[] { 48, 13, 10 };
        ZERO_CRLF_CRLF = new byte[] { 48, 13, 10, 13, 10 };
        CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(HttpObjectEncoder.CRLF.length).writeBytes(HttpObjectEncoder.CRLF));
        ZERO_CRLF_CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(HttpObjectEncoder.ZERO_CRLF_CRLF.length).writeBytes(HttpObjectEncoder.ZERO_CRLF_CRLF));
    }
}
