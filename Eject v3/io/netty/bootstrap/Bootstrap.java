package io.netty.bootstrap;

import io.netty.channel.*;
import io.netty.util.AttributeKey;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class Bootstrap
        extends AbstractBootstrap<Bootstrap, Channel> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(Bootstrap.class);
    private volatile SocketAddress remoteAddress;

    public Bootstrap() {
    }

    private Bootstrap(Bootstrap paramBootstrap) {
        super(paramBootstrap);
        this.remoteAddress = paramBootstrap.remoteAddress;
    }

    private static void doConnect0(ChannelFuture paramChannelFuture, final Channel paramChannel, final SocketAddress paramSocketAddress1, final SocketAddress paramSocketAddress2, final ChannelPromise paramChannelPromise) {
        paramChannel.eventLoop().execute(new Runnable() {
            public void run() {
                if (this.val$regFuture.isSuccess()) {
                    if (paramSocketAddress2 == null) {
                        paramChannel.connect(paramSocketAddress1, paramChannelPromise);
                    } else {
                        paramChannel.connect(paramSocketAddress1, paramSocketAddress2, paramChannelPromise);
                    }
                    paramChannelPromise.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                } else {
                    paramChannelPromise.setFailure(this.val$regFuture.cause());
                }
            }
        });
    }

    public Bootstrap remoteAddress(SocketAddress paramSocketAddress) {
        this.remoteAddress = paramSocketAddress;
        return this;
    }

    public Bootstrap remoteAddress(String paramString, int paramInt) {
        this.remoteAddress = new InetSocketAddress(paramString, paramInt);
        return this;
    }

    public Bootstrap remoteAddress(InetAddress paramInetAddress, int paramInt) {
        this.remoteAddress = new InetSocketAddress(paramInetAddress, paramInt);
        return this;
    }

    public ChannelFuture connect() {
        validate();
        SocketAddress localSocketAddress = this.remoteAddress;
        if (localSocketAddress == null) {
            throw new IllegalStateException("remoteAddress not set");
        }
        return doConnect(localSocketAddress, localAddress());
    }

    public ChannelFuture connect(String paramString, int paramInt) {
        return connect(new InetSocketAddress(paramString, paramInt));
    }

    public ChannelFuture connect(InetAddress paramInetAddress, int paramInt) {
        return connect(new InetSocketAddress(paramInetAddress, paramInt));
    }

    public ChannelFuture connect(SocketAddress paramSocketAddress) {
        if (paramSocketAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        validate();
        return doConnect(paramSocketAddress, localAddress());
    }

    public ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2) {
        if (paramSocketAddress1 == null) {
            throw new NullPointerException("remoteAddress");
        }
        validate();
        return doConnect(paramSocketAddress1, paramSocketAddress2);
    }

    private ChannelFuture doConnect(final SocketAddress paramSocketAddress1, final SocketAddress paramSocketAddress2) {
        final ChannelFuture localChannelFuture = initAndRegister();
        final Channel localChannel = localChannelFuture.channel();
        if (localChannelFuture.cause() != null) {
            return localChannelFuture;
        }
        final ChannelPromise localChannelPromise = localChannel.newPromise();
        if (localChannelFuture.isDone()) {
            doConnect0(localChannelFuture, localChannel, paramSocketAddress1, paramSocketAddress2, localChannelPromise);
        } else {
            localChannelFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture paramAnonymousChannelFuture)
                        throws Exception {
                    Bootstrap.doConnect0(localChannelFuture, localChannel, paramSocketAddress1, paramSocketAddress2, localChannelPromise);
                }
            });
        }
        return localChannelPromise;
    }

    void init(Channel paramChannel)
            throws Exception {
        ChannelPipeline localChannelPipeline = paramChannel.pipeline();
        localChannelPipeline.addLast(new ChannelHandler[]{handler()});
        Map localMap = options();
        Object localObject1;
        synchronized (localMap) {
            Iterator localIterator = localMap.entrySet().iterator();
            while (localIterator.hasNext()) {
                localObject1 = (Map.Entry) localIterator.next();
                try {
                    if (!paramChannel.config().setOption((ChannelOption) ((Map.Entry) localObject1).getKey(), ((Map.Entry) localObject1).getValue())) {
                        logger.warn("Unknown channel option: " + localObject1);
                    }
                } catch (Throwable localThrowable) {
                    logger.warn("Failed to set a channel option: " + paramChannel, localThrowable);
                }
            }
        }
    ??? =attrs();
        synchronized (???)
        {
            localObject1 = ((Map) ? ??).entrySet().iterator();
            while (((Iterator) localObject1).hasNext()) {
                Map.Entry localEntry = (Map.Entry) ((Iterator) localObject1).next();
                paramChannel.attr((AttributeKey) localEntry.getKey()).set(localEntry.getValue());
            }
        }
    }

    public Bootstrap validate() {
        super.validate();
        if (handler() == null) {
            throw new IllegalStateException("handler not set");
        }
        return this;
    }

    public Bootstrap clone() {
        return new Bootstrap(this);
    }

    public String toString() {
        if (this.remoteAddress == null) {
            return super.toString();
        }
        StringBuilder localStringBuilder = new StringBuilder(super.toString());
        localStringBuilder.setLength(localStringBuilder.length() - 1);
        localStringBuilder.append(", remoteAddress: ");
        localStringBuilder.append(this.remoteAddress);
        localStringBuilder.append(')');
        return localStringBuilder.toString();
    }
}




