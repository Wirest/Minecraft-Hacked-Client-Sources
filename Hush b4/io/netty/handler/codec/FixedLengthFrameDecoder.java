// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class FixedLengthFrameDecoder extends ByteToMessageDecoder
{
    private final int frameLength;
    
    public FixedLengthFrameDecoder(final int frameLength) {
        if (frameLength <= 0) {
            throw new IllegalArgumentException("frameLength must be a positive integer: " + frameLength);
        }
        this.frameLength = frameLength;
    }
    
    @Override
    protected final void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        final Object decoded = this.decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }
    
    protected Object decode(final ChannelHandlerContext ctx, final ByteBuf in) throws Exception {
        if (in.readableBytes() < this.frameLength) {
            return null;
        }
        return in.readSlice(this.frameLength).retain();
    }
}
