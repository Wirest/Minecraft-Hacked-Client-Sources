// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.embedded;

import java.util.ArrayList;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.EventLoop;
import java.nio.channels.ClosedChannelException;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.ChannelFuture;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.channel.ChannelPipeline;
import java.util.ArrayDeque;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import java.util.Queue;
import java.net.SocketAddress;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.AbstractChannel;

public class EmbeddedChannel extends AbstractChannel
{
    private static final InternalLogger logger;
    private static final ChannelMetadata METADATA;
    private final EmbeddedEventLoop loop;
    private final ChannelConfig config;
    private final SocketAddress localAddress;
    private final SocketAddress remoteAddress;
    private final Queue<Object> inboundMessages;
    private final Queue<Object> outboundMessages;
    private Throwable lastException;
    private int state;
    
    public EmbeddedChannel(final ChannelHandler... handlers) {
        super(null);
        this.loop = new EmbeddedEventLoop();
        this.config = new DefaultChannelConfig(this);
        this.localAddress = new EmbeddedSocketAddress();
        this.remoteAddress = new EmbeddedSocketAddress();
        this.inboundMessages = new ArrayDeque<Object>();
        this.outboundMessages = new ArrayDeque<Object>();
        if (handlers == null) {
            throw new NullPointerException("handlers");
        }
        int nHandlers = 0;
        final ChannelPipeline p = this.pipeline();
        for (final ChannelHandler h : handlers) {
            if (h == null) {
                break;
            }
            ++nHandlers;
            p.addLast(h);
        }
        if (nHandlers == 0) {
            throw new IllegalArgumentException("handlers is empty.");
        }
        this.loop.register(this);
        p.addLast(new LastInboundHandler());
    }
    
    @Override
    public ChannelMetadata metadata() {
        return EmbeddedChannel.METADATA;
    }
    
    @Override
    public ChannelConfig config() {
        return this.config;
    }
    
    @Override
    public boolean isOpen() {
        return this.state < 2;
    }
    
    @Override
    public boolean isActive() {
        return this.state == 1;
    }
    
    public Queue<Object> inboundMessages() {
        return this.inboundMessages;
    }
    
    @Deprecated
    public Queue<Object> lastInboundBuffer() {
        return this.inboundMessages();
    }
    
    public Queue<Object> outboundMessages() {
        return this.outboundMessages;
    }
    
    @Deprecated
    public Queue<Object> lastOutboundBuffer() {
        return this.outboundMessages();
    }
    
    public Object readInbound() {
        return this.inboundMessages.poll();
    }
    
    public Object readOutbound() {
        return this.outboundMessages.poll();
    }
    
    public boolean writeInbound(final Object... msgs) {
        this.ensureOpen();
        if (msgs.length == 0) {
            return !this.inboundMessages.isEmpty();
        }
        final ChannelPipeline p = this.pipeline();
        for (final Object m : msgs) {
            p.fireChannelRead(m);
        }
        p.fireChannelReadComplete();
        this.runPendingTasks();
        this.checkException();
        return !this.inboundMessages.isEmpty();
    }
    
    public boolean writeOutbound(final Object... msgs) {
        this.ensureOpen();
        if (msgs.length == 0) {
            return !this.outboundMessages.isEmpty();
        }
        final RecyclableArrayList futures = RecyclableArrayList.newInstance(msgs.length);
        try {
            for (final Object m : msgs) {
                if (m == null) {
                    break;
                }
                futures.add(this.write(m));
            }
            this.flush();
            for (int size = futures.size(), i = 0; i < size; ++i) {
                final ChannelFuture future = ((ArrayList<ChannelFuture>)futures).get(i);
                assert future.isDone();
                if (future.cause() != null) {
                    this.recordException(future.cause());
                }
            }
            this.runPendingTasks();
            this.checkException();
            return !this.outboundMessages.isEmpty();
        }
        finally {
            futures.recycle();
        }
    }
    
    public boolean finish() {
        this.close();
        this.runPendingTasks();
        this.checkException();
        return !this.inboundMessages.isEmpty() || !this.outboundMessages.isEmpty();
    }
    
    public void runPendingTasks() {
        try {
            this.loop.runTasks();
        }
        catch (Exception e) {
            this.recordException(e);
        }
    }
    
    private void recordException(final Throwable cause) {
        if (this.lastException == null) {
            this.lastException = cause;
        }
        else {
            EmbeddedChannel.logger.warn("More than one exception was raised. Will report only the first one and log others.", cause);
        }
    }
    
    public void checkException() {
        final Throwable t = this.lastException;
        if (t == null) {
            return;
        }
        this.lastException = null;
        PlatformDependent.throwException(t);
    }
    
    protected final void ensureOpen() {
        if (!this.isOpen()) {
            this.recordException(new ClosedChannelException());
            this.checkException();
        }
    }
    
    @Override
    protected boolean isCompatible(final EventLoop loop) {
        return loop instanceof EmbeddedEventLoop;
    }
    
    @Override
    protected SocketAddress localAddress0() {
        return this.isActive() ? this.localAddress : null;
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return this.isActive() ? this.remoteAddress : null;
    }
    
    @Override
    protected void doRegister() throws Exception {
        this.state = 1;
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }
    
    @Override
    protected void doClose() throws Exception {
        this.state = 2;
    }
    
    @Override
    protected void doBeginRead() throws Exception {
    }
    
    @Override
    protected AbstractUnsafe newUnsafe() {
        return new DefaultUnsafe();
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        while (true) {
            final Object msg = in.current();
            if (msg == null) {
                break;
            }
            ReferenceCountUtil.retain(msg);
            this.outboundMessages.add(msg);
            in.remove();
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(EmbeddedChannel.class);
        METADATA = new ChannelMetadata(false);
    }
    
    private class DefaultUnsafe extends AbstractUnsafe
    {
        @Override
        public void connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
            this.safeSetSuccess(promise);
        }
    }
    
    private final class LastInboundHandler extends ChannelInboundHandlerAdapter
    {
        @Override
        public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
            EmbeddedChannel.this.inboundMessages.add(msg);
        }
        
        @Override
        public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
            EmbeddedChannel.this.recordException(cause);
        }
    }
}
