// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class LineBasedFrameDecoder extends ByteToMessageDecoder
{
    private final int maxLength;
    private final boolean failFast;
    private final boolean stripDelimiter;
    private boolean discarding;
    private int discardedBytes;
    
    public LineBasedFrameDecoder(final int maxLength) {
        this(maxLength, true, false);
    }
    
    public LineBasedFrameDecoder(final int maxLength, final boolean stripDelimiter, final boolean failFast) {
        this.maxLength = maxLength;
        this.failFast = failFast;
        this.stripDelimiter = stripDelimiter;
    }
    
    @Override
    protected final void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        final Object decoded = this.decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }
    
    protected Object decode(final ChannelHandlerContext ctx, final ByteBuf buffer) throws Exception {
        final int eol = findEndOfLine(buffer);
        if (this.discarding) {
            if (eol >= 0) {
                final int length = this.discardedBytes + eol - buffer.readerIndex();
                final int delimLength = (buffer.getByte(eol) == 13) ? 2 : 1;
                buffer.readerIndex(eol + delimLength);
                this.discardedBytes = 0;
                this.discarding = false;
                if (!this.failFast) {
                    this.fail(ctx, length);
                }
            }
            else {
                this.discardedBytes = buffer.readableBytes();
                buffer.readerIndex(buffer.writerIndex());
            }
            return null;
        }
        if (eol < 0) {
            final int length = buffer.readableBytes();
            if (length > this.maxLength) {
                this.discardedBytes = length;
                buffer.readerIndex(buffer.writerIndex());
                this.discarding = true;
                if (this.failFast) {
                    this.fail(ctx, "over " + this.discardedBytes);
                }
            }
            return null;
        }
        final int length2 = eol - buffer.readerIndex();
        final int delimLength2 = (buffer.getByte(eol) == 13) ? 2 : 1;
        if (length2 > this.maxLength) {
            buffer.readerIndex(eol + delimLength2);
            this.fail(ctx, length2);
            return null;
        }
        ByteBuf frame;
        if (this.stripDelimiter) {
            frame = buffer.readSlice(length2);
            buffer.skipBytes(delimLength2);
        }
        else {
            frame = buffer.readSlice(length2 + delimLength2);
        }
        return frame.retain();
    }
    
    private void fail(final ChannelHandlerContext ctx, final int length) {
        this.fail(ctx, String.valueOf(length));
    }
    
    private void fail(final ChannelHandlerContext ctx, final String length) {
        ctx.fireExceptionCaught(new TooLongFrameException("frame length (" + length + ") exceeds the allowed maximum (" + this.maxLength + ')'));
    }
    
    private static int findEndOfLine(final ByteBuf buffer) {
        for (int n = buffer.writerIndex(), i = buffer.readerIndex(); i < n; ++i) {
            final byte b = buffer.getByte(i);
            if (b == 10) {
                return i;
            }
            if (b == 13 && i < n - 1 && buffer.getByte(i + 1) == 10) {
                return i;
            }
        }
        return -1;
    }
}
