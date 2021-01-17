// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.bootstrap;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import io.netty.channel.ChannelPipeline;
import io.netty.util.AttributeKey;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.Channel;

public final class Bootstrap extends AbstractBootstrap<Bootstrap, Channel>
{
    private static final InternalLogger logger;
    private volatile SocketAddress remoteAddress;
    
    public Bootstrap() {
    }
    
    private Bootstrap(final Bootstrap bootstrap) {
        super(bootstrap);
        this.remoteAddress = bootstrap.remoteAddress;
    }
    
    public Bootstrap remoteAddress(final SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }
    
    public Bootstrap remoteAddress(final String inetHost, final int inetPort) {
        this.remoteAddress = new InetSocketAddress(inetHost, inetPort);
        return this;
    }
    
    public Bootstrap remoteAddress(final InetAddress inetHost, final int inetPort) {
        this.remoteAddress = new InetSocketAddress(inetHost, inetPort);
        return this;
    }
    
    public ChannelFuture connect() {
        this.validate();
        final SocketAddress remoteAddress = this.remoteAddress;
        if (remoteAddress == null) {
            throw new IllegalStateException("remoteAddress not set");
        }
        return this.doConnect(remoteAddress, this.localAddress());
    }
    
    public ChannelFuture connect(final String inetHost, final int inetPort) {
        return this.connect(new InetSocketAddress(inetHost, inetPort));
    }
    
    public ChannelFuture connect(final InetAddress inetHost, final int inetPort) {
        return this.connect(new InetSocketAddress(inetHost, inetPort));
    }
    
    public ChannelFuture connect(final SocketAddress remoteAddress) {
        if (remoteAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        this.validate();
        return this.doConnect(remoteAddress, this.localAddress());
    }
    
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        if (remoteAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        this.validate();
        return this.doConnect(remoteAddress, localAddress);
    }
    
    private ChannelFuture doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        final ChannelFuture regFuture = this.initAndRegister();
        final Channel channel = regFuture.channel();
        if (regFuture.cause() != null) {
            return regFuture;
        }
        final ChannelPromise promise = channel.newPromise();
        if (regFuture.isDone()) {
            doConnect0(regFuture, channel, remoteAddress, localAddress, promise);
        }
        else {
            regFuture.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture future) throws Exception {
                    doConnect0(regFuture, channel, remoteAddress, localAddress, promise);
                }
            });
        }
        return promise;
    }
    
    private static void doConnect0(final ChannelFuture regFuture, final Channel channel, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        channel.eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                if (regFuture.isSuccess()) {
                    if (localAddress == null) {
                        channel.connect(remoteAddress, promise);
                    }
                    else {
                        channel.connect(remoteAddress, localAddress, promise);
                    }
                    promise.addListener((GenericFutureListener<? extends Future<? super Void>>)ChannelFutureListener.CLOSE_ON_FAILURE);
                }
                else {
                    promise.setFailure(regFuture.cause());
                }
            }
        });
    }
    
    @Override
    void init(final Channel channel) throws Exception {
        final ChannelPipeline p = channel.pipeline();
        p.addLast(this.handler());
        final Map<ChannelOption<?>, Object> options = this.options();
        synchronized (options) {
            for (final Map.Entry<ChannelOption<?>, Object> e : options.entrySet()) {
                try {
                    if (channel.config().setOption(e.getKey(), e.getValue())) {
                        continue;
                    }
                    Bootstrap.logger.warn("Unknown channel option: " + e);
                }
                catch (Throwable t) {
                    Bootstrap.logger.warn("Failed to set a channel option: " + channel, t);
                }
            }
        }
        final Map<AttributeKey<?>, Object> attrs = this.attrs();
        synchronized (attrs) {
            for (final Map.Entry<AttributeKey<?>, Object> e2 : attrs.entrySet()) {
                channel.attr(e2.getKey()).set(e2.getValue());
            }
        }
    }
    
    @Override
    public Bootstrap validate() {
        super.validate();
        if (this.handler() == null) {
            throw new IllegalStateException("handler not set");
        }
        return this;
    }
    
    @Override
    public Bootstrap clone() {
        return new Bootstrap(this);
    }
    
    @Override
    public String toString() {
        if (this.remoteAddress == null) {
            return super.toString();
        }
        final StringBuilder buf = new StringBuilder(super.toString());
        buf.setLength(buf.length() - 1);
        buf.append(", remoteAddress: ");
        buf.append(this.remoteAddress);
        buf.append(')');
        return buf.toString();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(Bootstrap.class);
    }
}
