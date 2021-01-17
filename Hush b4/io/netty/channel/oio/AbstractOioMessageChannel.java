// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.oio;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelConfig;
import java.io.IOException;
import java.util.ArrayList;
import io.netty.channel.Channel;
import java.util.List;

public abstract class AbstractOioMessageChannel extends AbstractOioChannel
{
    private final List<Object> readBuf;
    
    protected AbstractOioMessageChannel(final Channel parent) {
        super(parent);
        this.readBuf = new ArrayList<Object>();
    }
    
    @Override
    protected void doRead() {
        final ChannelConfig config = this.config();
        final ChannelPipeline pipeline = this.pipeline();
        boolean closed = false;
        final int maxMessagesPerRead = config.getMaxMessagesPerRead();
        Throwable exception = null;
        int localRead = 0;
        try {
            do {
                localRead = this.doReadMessages(this.readBuf);
                if (localRead == 0) {
                    break;
                }
                if (localRead < 0) {
                    closed = true;
                    break;
                }
            } while (this.readBuf.size() < maxMessagesPerRead && config.isAutoRead());
        }
        catch (Throwable t) {
            exception = t;
        }
        for (int size = this.readBuf.size(), i = 0; i < size; ++i) {
            pipeline.fireChannelRead(this.readBuf.get(i));
        }
        this.readBuf.clear();
        pipeline.fireChannelReadComplete();
        if (exception != null) {
            if (exception instanceof IOException) {
                closed = true;
            }
            this.pipeline().fireExceptionCaught(exception);
        }
        if (closed) {
            if (this.isOpen()) {
                this.unsafe().close(this.unsafe().voidPromise());
            }
        }
        else if (localRead == 0 && this.isActive()) {
            this.read();
        }
    }
    
    protected abstract int doReadMessages(final List<Object> p0) throws Exception;
}
