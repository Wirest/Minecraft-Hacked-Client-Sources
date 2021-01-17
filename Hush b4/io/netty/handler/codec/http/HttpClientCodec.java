// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.handler.codec.PrematureChannelClosureException;
import io.netty.buffer.ByteBuf;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Queue;
import io.netty.channel.CombinedChannelDuplexHandler;

public final class HttpClientCodec extends CombinedChannelDuplexHandler<HttpResponseDecoder, HttpRequestEncoder>
{
    private final Queue<HttpMethod> queue;
    private boolean done;
    private final AtomicLong requestResponseCounter;
    private final boolean failOnMissingResponse;
    
    public HttpClientCodec() {
        this(4096, 8192, 8192, false);
    }
    
    public void setSingleDecode(final boolean singleDecode) {
        ((CombinedChannelDuplexHandler<HttpResponseDecoder, O>)this).inboundHandler().setSingleDecode(singleDecode);
    }
    
    public boolean isSingleDecode() {
        return ((CombinedChannelDuplexHandler<HttpResponseDecoder, O>)this).inboundHandler().isSingleDecode();
    }
    
    public HttpClientCodec(final int maxInitialLineLength, final int maxHeaderSize, final int maxChunkSize) {
        this(maxInitialLineLength, maxHeaderSize, maxChunkSize, false);
    }
    
    public HttpClientCodec(final int maxInitialLineLength, final int maxHeaderSize, final int maxChunkSize, final boolean failOnMissingResponse) {
        this(maxInitialLineLength, maxHeaderSize, maxChunkSize, failOnMissingResponse, true);
    }
    
    public HttpClientCodec(final int maxInitialLineLength, final int maxHeaderSize, final int maxChunkSize, final boolean failOnMissingResponse, final boolean validateHeaders) {
        this.queue = new ArrayDeque<HttpMethod>();
        this.requestResponseCounter = new AtomicLong();
        ((CombinedChannelDuplexHandler<Decoder, Encoder>)this).init(new Decoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders), new Encoder());
        this.failOnMissingResponse = failOnMissingResponse;
    }
    
    private final class Encoder extends HttpRequestEncoder
    {
        @Override
        protected void encode(final ChannelHandlerContext ctx, final Object msg, final List<Object> out) throws Exception {
            if (msg instanceof HttpRequest && !HttpClientCodec.this.done) {
                HttpClientCodec.this.queue.offer(((HttpRequest)msg).getMethod());
            }
            super.encode(ctx, msg, out);
            if (HttpClientCodec.this.failOnMissingResponse && msg instanceof LastHttpContent) {
                HttpClientCodec.this.requestResponseCounter.incrementAndGet();
            }
        }
    }
    
    private final class Decoder extends HttpResponseDecoder
    {
        Decoder(final int maxInitialLineLength, final int maxHeaderSize, final int maxChunkSize, final boolean validateHeaders) {
            super(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders);
        }
        
        @Override
        protected void decode(final ChannelHandlerContext ctx, final ByteBuf buffer, final List<Object> out) throws Exception {
            if (HttpClientCodec.this.done) {
                final int readable = this.actualReadableBytes();
                if (readable == 0) {
                    return;
                }
                out.add(buffer.readBytes(readable));
            }
            else {
                final int oldSize = out.size();
                super.decode(ctx, buffer, out);
                if (HttpClientCodec.this.failOnMissingResponse) {
                    for (int size = out.size(), i = oldSize; i < size; ++i) {
                        this.decrement(out.get(i));
                    }
                }
            }
        }
        
        private void decrement(final Object msg) {
            if (msg == null) {
                return;
            }
            if (msg instanceof LastHttpContent) {
                HttpClientCodec.this.requestResponseCounter.decrementAndGet();
            }
        }
        
        @Override
        protected boolean isContentAlwaysEmpty(final HttpMessage msg) {
            final int statusCode = ((HttpResponse)msg).getStatus().code();
            if (statusCode == 100) {
                return true;
            }
            final HttpMethod method = HttpClientCodec.this.queue.poll();
            final char firstChar = method.name().charAt(0);
            switch (firstChar) {
                case 'H': {
                    if (HttpMethod.HEAD.equals(method)) {
                        return true;
                    }
                    break;
                }
                case 'C': {
                    if (statusCode == 200 && HttpMethod.CONNECT.equals(method)) {
                        HttpClientCodec.this.done = true;
                        HttpClientCodec.this.queue.clear();
                        return true;
                    }
                    break;
                }
            }
            return super.isContentAlwaysEmpty(msg);
        }
        
        @Override
        public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            if (HttpClientCodec.this.failOnMissingResponse) {
                final long missingResponses = HttpClientCodec.this.requestResponseCounter.get();
                if (missingResponses > 0L) {
                    ctx.fireExceptionCaught(new PrematureChannelClosureException("channel gone inactive with " + missingResponses + " missing response(s)"));
                }
            }
        }
    }
}
