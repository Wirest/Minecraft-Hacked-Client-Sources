// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.stream.ChunkedInput;

public class HttpChunkedInput implements ChunkedInput<HttpContent>
{
    private final ChunkedInput<ByteBuf> input;
    private final LastHttpContent lastHttpContent;
    private boolean sentLastChunk;
    
    public HttpChunkedInput(final ChunkedInput<ByteBuf> input) {
        this.input = input;
        this.lastHttpContent = LastHttpContent.EMPTY_LAST_CONTENT;
    }
    
    public HttpChunkedInput(final ChunkedInput<ByteBuf> input, final LastHttpContent lastHttpContent) {
        this.input = input;
        this.lastHttpContent = lastHttpContent;
    }
    
    @Override
    public boolean isEndOfInput() throws Exception {
        return this.input.isEndOfInput() && this.sentLastChunk;
    }
    
    @Override
    public void close() throws Exception {
        this.input.close();
    }
    
    @Override
    public HttpContent readChunk(final ChannelHandlerContext ctx) throws Exception {
        if (!this.input.isEndOfInput()) {
            final ByteBuf buf = this.input.readChunk(ctx);
            return new DefaultHttpContent(buf);
        }
        if (this.sentLastChunk) {
            return null;
        }
        this.sentLastChunk = true;
        return this.lastHttpContent;
    }
}
