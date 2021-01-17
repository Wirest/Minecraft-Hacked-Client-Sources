// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToMessageDecoder;

public abstract class HttpContentDecoder extends MessageToMessageDecoder<HttpObject>
{
    private EmbeddedChannel decoder;
    private HttpMessage message;
    private boolean decodeStarted;
    private boolean continueResponse;
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final HttpObject msg, final List<Object> out) throws Exception {
        if (msg instanceof HttpResponse && ((HttpResponse)msg).getStatus().code() == 100) {
            if (!(msg instanceof LastHttpContent)) {
                this.continueResponse = true;
            }
            out.add(ReferenceCountUtil.retain(msg));
            return;
        }
        if (this.continueResponse) {
            if (msg instanceof LastHttpContent) {
                this.continueResponse = false;
            }
            out.add(ReferenceCountUtil.retain(msg));
            return;
        }
        if (msg instanceof HttpMessage) {
            assert this.message == null;
            this.message = (HttpMessage)msg;
            this.decodeStarted = false;
            this.cleanup();
        }
        if (msg instanceof HttpContent) {
            final HttpContent c = (HttpContent)msg;
            if (!this.decodeStarted) {
                this.decodeStarted = true;
                final HttpMessage message = this.message;
                final HttpHeaders headers = message.headers();
                this.message = null;
                String contentEncoding = headers.get("Content-Encoding");
                if (contentEncoding != null) {
                    contentEncoding = contentEncoding.trim();
                }
                else {
                    contentEncoding = "identity";
                }
                final EmbeddedChannel contentDecoder = this.newContentDecoder(contentEncoding);
                this.decoder = contentDecoder;
                if (contentDecoder != null) {
                    final String targetContentEncoding = this.getTargetContentEncoding(contentEncoding);
                    if ("identity".equals(targetContentEncoding)) {
                        headers.remove("Content-Encoding");
                    }
                    else {
                        headers.set("Content-Encoding", targetContentEncoding);
                    }
                    out.add(message);
                    this.decodeContent(c, out);
                    if (headers.contains("Content-Length")) {
                        int contentLength = 0;
                        for (int size = out.size(), i = 0; i < size; ++i) {
                            final Object o = out.get(i);
                            if (o instanceof HttpContent) {
                                contentLength += ((HttpContent)o).content().readableBytes();
                            }
                        }
                        headers.set("Content-Length", Integer.toString(contentLength));
                    }
                    return;
                }
                if (c instanceof LastHttpContent) {
                    this.decodeStarted = false;
                }
                out.add(message);
                out.add(c.retain());
            }
            else if (this.decoder != null) {
                this.decodeContent(c, out);
            }
            else {
                if (c instanceof LastHttpContent) {
                    this.decodeStarted = false;
                }
                out.add(c.retain());
            }
        }
    }
    
    private void decodeContent(final HttpContent c, final List<Object> out) {
        final ByteBuf content = c.content();
        this.decode(content, out);
        if (c instanceof LastHttpContent) {
            this.finishDecode(out);
            final LastHttpContent last = (LastHttpContent)c;
            final HttpHeaders headers = last.trailingHeaders();
            if (headers.isEmpty()) {
                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
            }
            else {
                out.add(new ComposedLastHttpContent(headers));
            }
        }
    }
    
    protected abstract EmbeddedChannel newContentDecoder(final String p0) throws Exception;
    
    protected String getTargetContentEncoding(final String contentEncoding) throws Exception {
        return "identity";
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        this.cleanup();
        super.handlerRemoved(ctx);
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.cleanup();
        super.channelInactive(ctx);
    }
    
    private void cleanup() {
        if (this.decoder != null) {
            if (this.decoder.finish()) {
                while (true) {
                    final ByteBuf buf = (ByteBuf)this.decoder.readOutbound();
                    if (buf == null) {
                        break;
                    }
                    buf.release();
                }
            }
            this.decoder = null;
        }
    }
    
    private void decode(final ByteBuf in, final List<Object> out) {
        this.decoder.writeInbound(in.retain());
        this.fetchDecoderOutput(out);
    }
    
    private void finishDecode(final List<Object> out) {
        if (this.decoder.finish()) {
            this.fetchDecoderOutput(out);
        }
        this.decodeStarted = false;
        this.decoder = null;
    }
    
    private void fetchDecoderOutput(final List<Object> out) {
        while (true) {
            final ByteBuf buf = (ByteBuf)this.decoder.readInbound();
            if (buf == null) {
                break;
            }
            if (!buf.isReadable()) {
                buf.release();
            }
            else {
                out.add(new DefaultHttpContent(buf));
            }
        }
    }
}
