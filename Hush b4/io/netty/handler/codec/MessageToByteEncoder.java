// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.TypeParameterMatcher;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public abstract class MessageToByteEncoder<I> extends ChannelOutboundHandlerAdapter
{
    private final TypeParameterMatcher matcher;
    private final boolean preferDirect;
    
    protected MessageToByteEncoder() {
        this(true);
    }
    
    protected MessageToByteEncoder(final Class<? extends I> outboundMessageType) {
        this(outboundMessageType, true);
    }
    
    protected MessageToByteEncoder(final boolean preferDirect) {
        this.matcher = TypeParameterMatcher.find(this, MessageToByteEncoder.class, "I");
        this.preferDirect = preferDirect;
    }
    
    protected MessageToByteEncoder(final Class<? extends I> outboundMessageType, final boolean preferDirect) {
        this.matcher = TypeParameterMatcher.get(outboundMessageType);
        this.preferDirect = preferDirect;
    }
    
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return this.matcher.match(msg);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        ByteBuf buf = null;
        try {
            if (this.acceptOutboundMessage(msg)) {
                final I cast = (I)msg;
                buf = this.allocateBuffer(ctx, cast, this.preferDirect);
                try {
                    this.encode(ctx, cast, buf);
                }
                finally {
                    ReferenceCountUtil.release(cast);
                }
                if (buf.isReadable()) {
                    ctx.write(buf, promise);
                }
                else {
                    buf.release();
                    ctx.write(Unpooled.EMPTY_BUFFER, promise);
                }
                buf = null;
            }
            else {
                ctx.write(msg, promise);
            }
        }
        catch (EncoderException e) {
            throw e;
        }
        catch (Throwable e2) {
            throw new EncoderException(e2);
        }
        finally {
            if (buf != null) {
                buf.release();
            }
        }
    }
    
    protected ByteBuf allocateBuffer(final ChannelHandlerContext ctx, final I msg, final boolean preferDirect) throws Exception {
        if (preferDirect) {
            return ctx.alloc().ioBuffer();
        }
        return ctx.alloc().heapBuffer();
    }
    
    protected abstract void encode(final ChannelHandlerContext p0, final I p1, final ByteBuf p2) throws Exception;
}
