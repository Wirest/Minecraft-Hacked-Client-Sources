// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

import io.netty.util.internal.StringUtil;
import io.netty.util.ReferenceCountUtil;
import java.util.List;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.TypeParameterMatcher;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public abstract class MessageToMessageEncoder<I> extends ChannelOutboundHandlerAdapter
{
    private final TypeParameterMatcher matcher;
    
    protected MessageToMessageEncoder() {
        this.matcher = TypeParameterMatcher.find(this, MessageToMessageEncoder.class, "I");
    }
    
    protected MessageToMessageEncoder(final Class<? extends I> outboundMessageType) {
        this.matcher = TypeParameterMatcher.get(outboundMessageType);
    }
    
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return this.matcher.match(msg);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        RecyclableArrayList out = null;
        try {
            if (this.acceptOutboundMessage(msg)) {
                out = RecyclableArrayList.newInstance();
                final I cast = (I)msg;
                try {
                    this.encode(ctx, cast, out);
                }
                finally {
                    ReferenceCountUtil.release(cast);
                }
                if (out.isEmpty()) {
                    out.recycle();
                    out = null;
                    throw new EncoderException(StringUtil.simpleClassName(this) + " must produce at least one message.");
                }
            }
            else {
                ctx.write(msg, promise);
            }
        }
        catch (EncoderException e) {
            throw e;
        }
        catch (Throwable t) {
            throw new EncoderException(t);
        }
        finally {
            if (out != null) {
                final int sizeMinusOne = out.size() - 1;
                if (sizeMinusOne == 0) {
                    ctx.write(out.get(0), promise);
                }
                else if (sizeMinusOne > 0) {
                    final ChannelPromise voidPromise = ctx.voidPromise();
                    final boolean isVoidPromise = promise == voidPromise;
                    for (int i = 0; i < sizeMinusOne; ++i) {
                        ChannelPromise p;
                        if (isVoidPromise) {
                            p = voidPromise;
                        }
                        else {
                            p = ctx.newPromise();
                        }
                        ctx.write(out.get(i), p);
                    }
                    ctx.write(out.get(sizeMinusOne), promise);
                }
                out.recycle();
            }
        }
    }
    
    protected abstract void encode(final ChannelHandlerContext p0, final I p1, final List<Object> p2) throws Exception;
}
