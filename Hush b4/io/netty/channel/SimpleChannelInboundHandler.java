// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.TypeParameterMatcher;

public abstract class SimpleChannelInboundHandler<I> extends ChannelInboundHandlerAdapter
{
    private final TypeParameterMatcher matcher;
    private final boolean autoRelease;
    
    protected SimpleChannelInboundHandler() {
        this(true);
    }
    
    protected SimpleChannelInboundHandler(final boolean autoRelease) {
        this.matcher = TypeParameterMatcher.find(this, SimpleChannelInboundHandler.class, "I");
        this.autoRelease = autoRelease;
    }
    
    protected SimpleChannelInboundHandler(final Class<? extends I> inboundMessageType) {
        this(inboundMessageType, true);
    }
    
    protected SimpleChannelInboundHandler(final Class<? extends I> inboundMessageType, final boolean autoRelease) {
        this.matcher = TypeParameterMatcher.get(inboundMessageType);
        this.autoRelease = autoRelease;
    }
    
    public boolean acceptInboundMessage(final Object msg) throws Exception {
        return this.matcher.match(msg);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        boolean release = true;
        try {
            if (this.acceptInboundMessage(msg)) {
                final I imsg = (I)msg;
                this.channelRead0(ctx, imsg);
            }
            else {
                release = false;
                ctx.fireChannelRead(msg);
            }
        }
        finally {
            if (this.autoRelease && release) {
                ReferenceCountUtil.release(msg);
            }
        }
    }
    
    protected abstract void channelRead0(final ChannelHandlerContext p0, final I p1) throws Exception;
}
