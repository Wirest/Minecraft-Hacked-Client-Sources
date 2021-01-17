// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.stream;

import io.netty.channel.ChannelHandlerContext;
import java.io.InputStream;
import java.io.PushbackInputStream;
import io.netty.buffer.ByteBuf;

public class ChunkedStream implements ChunkedInput<ByteBuf>
{
    static final int DEFAULT_CHUNK_SIZE = 8192;
    private final PushbackInputStream in;
    private final int chunkSize;
    private long offset;
    
    public ChunkedStream(final InputStream in) {
        this(in, 8192);
    }
    
    public ChunkedStream(final InputStream in, final int chunkSize) {
        if (in == null) {
            throw new NullPointerException("in");
        }
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("chunkSize: " + chunkSize + " (expected: a positive integer)");
        }
        if (in instanceof PushbackInputStream) {
            this.in = (PushbackInputStream)in;
        }
        else {
            this.in = new PushbackInputStream(in);
        }
        this.chunkSize = chunkSize;
    }
    
    public long transferredBytes() {
        return this.offset;
    }
    
    @Override
    public boolean isEndOfInput() throws Exception {
        final int b = this.in.read();
        if (b < 0) {
            return true;
        }
        this.in.unread(b);
        return false;
    }
    
    @Override
    public void close() throws Exception {
        this.in.close();
    }
    
    @Override
    public ByteBuf readChunk(final ChannelHandlerContext ctx) throws Exception {
        if (this.isEndOfInput()) {
            return null;
        }
        final int availableBytes = this.in.available();
        int chunkSize;
        if (availableBytes <= 0) {
            chunkSize = this.chunkSize;
        }
        else {
            chunkSize = Math.min(this.chunkSize, this.in.available());
        }
        boolean release = true;
        final ByteBuf buffer = ctx.alloc().buffer(chunkSize);
        try {
            this.offset += buffer.writeBytes(this.in, chunkSize);
            release = false;
            return buffer;
        }
        finally {
            if (release) {
                buffer.release();
            }
        }
    }
}
