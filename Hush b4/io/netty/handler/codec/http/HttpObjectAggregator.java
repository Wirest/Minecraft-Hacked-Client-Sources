// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class HttpObjectAggregator extends MessageToMessageDecoder<HttpObject>
{
    public static final int DEFAULT_MAX_COMPOSITEBUFFER_COMPONENTS = 1024;
    private static final FullHttpResponse CONTINUE;
    private final int maxContentLength;
    private AggregatedFullHttpMessage currentMessage;
    private boolean tooLongFrameFound;
    private int maxCumulationBufferComponents;
    private ChannelHandlerContext ctx;
    
    public HttpObjectAggregator(final int maxContentLength) {
        this.maxCumulationBufferComponents = 1024;
        if (maxContentLength <= 0) {
            throw new IllegalArgumentException("maxContentLength must be a positive integer: " + maxContentLength);
        }
        this.maxContentLength = maxContentLength;
    }
    
    public final int getMaxCumulationBufferComponents() {
        return this.maxCumulationBufferComponents;
    }
    
    public final void setMaxCumulationBufferComponents(final int maxCumulationBufferComponents) {
        if (maxCumulationBufferComponents < 2) {
            throw new IllegalArgumentException("maxCumulationBufferComponents: " + maxCumulationBufferComponents + " (expected: >= 2)");
        }
        if (this.ctx == null) {
            this.maxCumulationBufferComponents = maxCumulationBufferComponents;
            return;
        }
        throw new IllegalStateException("decoder properties cannot be changed once the decoder is added to a pipeline.");
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final HttpObject msg, final List<Object> out) throws Exception {
        AggregatedFullHttpMessage currentMessage = this.currentMessage;
        if (msg instanceof HttpMessage) {
            this.tooLongFrameFound = false;
            assert currentMessage == null;
            final HttpMessage m = (HttpMessage)msg;
            if (HttpHeaders.is100ContinueExpected(m)) {
                ctx.writeAndFlush(HttpObjectAggregator.CONTINUE).addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                    @Override
                    public void operationComplete(final ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            ctx.fireExceptionCaught(future.cause());
                        }
                    }
                });
            }
            if (!m.getDecoderResult().isSuccess()) {
                HttpHeaders.removeTransferEncodingChunked(m);
                out.add(toFullMessage(m));
                this.currentMessage = null;
                return;
            }
            if (msg instanceof HttpRequest) {
                final HttpRequest header = (HttpRequest)msg;
                currentMessage = (this.currentMessage = new AggregatedFullHttpRequest(header, (ByteBuf)ctx.alloc().compositeBuffer(this.maxCumulationBufferComponents), (HttpHeaders)null));
            }
            else {
                if (!(msg instanceof HttpResponse)) {
                    throw new Error();
                }
                final HttpResponse header2 = (HttpResponse)msg;
                currentMessage = (this.currentMessage = new AggregatedFullHttpResponse(header2, (ByteBuf)Unpooled.compositeBuffer(this.maxCumulationBufferComponents), (HttpHeaders)null));
            }
            HttpHeaders.removeTransferEncodingChunked(currentMessage);
        }
        else {
            if (!(msg instanceof HttpContent)) {
                throw new Error();
            }
            if (this.tooLongFrameFound) {
                if (msg instanceof LastHttpContent) {
                    this.currentMessage = null;
                }
                return;
            }
            assert currentMessage != null;
            final HttpContent chunk = (HttpContent)msg;
            final CompositeByteBuf content = (CompositeByteBuf)currentMessage.content();
            if (content.readableBytes() > this.maxContentLength - chunk.content().readableBytes()) {
                this.tooLongFrameFound = true;
                currentMessage.release();
                this.currentMessage = null;
                throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
            }
            if (chunk.content().isReadable()) {
                chunk.retain();
                content.addComponent(chunk.content());
                content.writerIndex(content.writerIndex() + chunk.content().readableBytes());
            }
            boolean last;
            if (!chunk.getDecoderResult().isSuccess()) {
                currentMessage.setDecoderResult(DecoderResult.failure(chunk.getDecoderResult().cause()));
                last = true;
            }
            else {
                last = (chunk instanceof LastHttpContent);
            }
            if (last) {
                this.currentMessage = null;
                if (chunk instanceof LastHttpContent) {
                    final LastHttpContent trailer = (LastHttpContent)chunk;
                    currentMessage.setTrailingHeaders(trailer.trailingHeaders());
                }
                else {
                    currentMessage.setTrailingHeaders(new DefaultHttpHeaders());
                }
                currentMessage.headers().set("Content-Length", String.valueOf(content.readableBytes()));
                out.add(currentMessage);
            }
        }
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (this.currentMessage != null) {
            this.currentMessage.release();
            this.currentMessage = null;
        }
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        if (this.currentMessage != null) {
            this.currentMessage.release();
            this.currentMessage = null;
        }
    }
    
    private static FullHttpMessage toFullMessage(final HttpMessage msg) {
        if (msg instanceof FullHttpMessage) {
            return ((FullHttpMessage)msg).retain();
        }
        FullHttpMessage fullMsg;
        if (msg instanceof HttpRequest) {
            fullMsg = new AggregatedFullHttpRequest((HttpRequest)msg, Unpooled.EMPTY_BUFFER, (HttpHeaders)new DefaultHttpHeaders());
        }
        else {
            if (!(msg instanceof HttpResponse)) {
                throw new IllegalStateException();
            }
            fullMsg = new AggregatedFullHttpResponse((HttpResponse)msg, Unpooled.EMPTY_BUFFER, (HttpHeaders)new DefaultHttpHeaders());
        }
        return fullMsg;
    }
    
    static {
        CONTINUE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER);
    }
    
    private abstract static class AggregatedFullHttpMessage extends DefaultByteBufHolder implements FullHttpMessage
    {
        protected final HttpMessage message;
        private HttpHeaders trailingHeaders;
        
        private AggregatedFullHttpMessage(final HttpMessage message, final ByteBuf content, final HttpHeaders trailingHeaders) {
            super(content);
            this.message = message;
            this.trailingHeaders = trailingHeaders;
        }
        
        @Override
        public HttpHeaders trailingHeaders() {
            return this.trailingHeaders;
        }
        
        public void setTrailingHeaders(final HttpHeaders trailingHeaders) {
            this.trailingHeaders = trailingHeaders;
        }
        
        @Override
        public HttpVersion getProtocolVersion() {
            return this.message.getProtocolVersion();
        }
        
        @Override
        public FullHttpMessage setProtocolVersion(final HttpVersion version) {
            this.message.setProtocolVersion(version);
            return this;
        }
        
        @Override
        public HttpHeaders headers() {
            return this.message.headers();
        }
        
        @Override
        public DecoderResult getDecoderResult() {
            return this.message.getDecoderResult();
        }
        
        @Override
        public void setDecoderResult(final DecoderResult result) {
            this.message.setDecoderResult(result);
        }
        
        @Override
        public FullHttpMessage retain(final int increment) {
            super.retain(increment);
            return this;
        }
        
        @Override
        public FullHttpMessage retain() {
            super.retain();
            return this;
        }
        
        @Override
        public abstract FullHttpMessage copy();
        
        @Override
        public abstract FullHttpMessage duplicate();
    }
    
    private static final class AggregatedFullHttpRequest extends AggregatedFullHttpMessage implements FullHttpRequest
    {
        private AggregatedFullHttpRequest(final HttpRequest request, final ByteBuf content, final HttpHeaders trailingHeaders) {
            super((HttpMessage)request, content, trailingHeaders);
        }
        
        @Override
        public FullHttpRequest copy() {
            final DefaultFullHttpRequest copy = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().copy());
            copy.headers().set(this.headers());
            copy.trailingHeaders().set(this.trailingHeaders());
            return copy;
        }
        
        @Override
        public FullHttpRequest duplicate() {
            final DefaultFullHttpRequest duplicate = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().duplicate());
            duplicate.headers().set(this.headers());
            duplicate.trailingHeaders().set(this.trailingHeaders());
            return duplicate;
        }
        
        @Override
        public FullHttpRequest retain(final int increment) {
            super.retain(increment);
            return this;
        }
        
        @Override
        public FullHttpRequest retain() {
            super.retain();
            return this;
        }
        
        @Override
        public FullHttpRequest setMethod(final HttpMethod method) {
            ((HttpRequest)this.message).setMethod(method);
            return this;
        }
        
        @Override
        public FullHttpRequest setUri(final String uri) {
            ((HttpRequest)this.message).setUri(uri);
            return this;
        }
        
        @Override
        public HttpMethod getMethod() {
            return ((HttpRequest)this.message).getMethod();
        }
        
        @Override
        public String getUri() {
            return ((HttpRequest)this.message).getUri();
        }
        
        @Override
        public FullHttpRequest setProtocolVersion(final HttpVersion version) {
            super.setProtocolVersion(version);
            return this;
        }
    }
    
    private static final class AggregatedFullHttpResponse extends AggregatedFullHttpMessage implements FullHttpResponse
    {
        private AggregatedFullHttpResponse(final HttpResponse message, final ByteBuf content, final HttpHeaders trailingHeaders) {
            super((HttpMessage)message, content, trailingHeaders);
        }
        
        @Override
        public FullHttpResponse copy() {
            final DefaultFullHttpResponse copy = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().copy());
            copy.headers().set(this.headers());
            copy.trailingHeaders().set(this.trailingHeaders());
            return copy;
        }
        
        @Override
        public FullHttpResponse duplicate() {
            final DefaultFullHttpResponse duplicate = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().duplicate());
            duplicate.headers().set(this.headers());
            duplicate.trailingHeaders().set(this.trailingHeaders());
            return duplicate;
        }
        
        @Override
        public FullHttpResponse setStatus(final HttpResponseStatus status) {
            ((HttpResponse)this.message).setStatus(status);
            return this;
        }
        
        @Override
        public HttpResponseStatus getStatus() {
            return ((HttpResponse)this.message).getStatus();
        }
        
        @Override
        public FullHttpResponse setProtocolVersion(final HttpVersion version) {
            super.setProtocolVersion(version);
            return this;
        }
        
        @Override
        public FullHttpResponse retain(final int increment) {
            super.retain(increment);
            return this;
        }
        
        @Override
        public FullHttpResponse retain() {
            super.retain();
            return this;
        }
    }
}
