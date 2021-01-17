// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.InternalLogger;

@ChannelHandler.Sharable
public abstract class ChannelInitializer<C extends Channel> extends ChannelInboundHandlerAdapter
{
    private static final InternalLogger logger;
    
    protected abstract void initChannel(final C p0) throws Exception;
    
    @Override
    public final void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        final ChannelPipeline pipeline = ctx.pipeline();
        boolean success = false;
        try {
            this.initChannel(ctx.channel());
            pipeline.remove(this);
            ctx.fireChannelRegistered();
            success = true;
        }
        catch (Throwable t) {
            ChannelInitializer.logger.warn("Failed to initialize a channel. Closing: " + ctx.channel(), t);
        }
        finally {
            if (pipeline.context(this) != null) {
                pipeline.remove(this);
            }
            if (!success) {
                ctx.close();
            }
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ChannelInitializer.class);
    }
}
