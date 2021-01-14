package io.netty.bootstrap;

import io.netty.channel.*;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.StringUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractBootstrap<B extends AbstractBootstrap<B, C>, C extends Channel>
        implements Cloneable {
    private final Map<ChannelOption<?>, Object> options = new LinkedHashMap();
    private final Map<AttributeKey<?>, Object> attrs = new LinkedHashMap();
    private volatile EventLoopGroup group;
    private volatile ChannelFactory<? extends C> channelFactory;
    private volatile SocketAddress localAddress;
    private volatile ChannelHandler handler;

    AbstractBootstrap() {
    }

    AbstractBootstrap(AbstractBootstrap<B, C> paramAbstractBootstrap) {
        this.group = paramAbstractBootstrap.group;
        this.channelFactory = paramAbstractBootstrap.channelFactory;
        this.handler = paramAbstractBootstrap.handler;
        this.localAddress = paramAbstractBootstrap.localAddress;
        synchronized (paramAbstractBootstrap.options) {
            this.options.putAll(paramAbstractBootstrap.options);
        }
        synchronized (paramAbstractBootstrap.attrs) {
            this.attrs.putAll(paramAbstractBootstrap.attrs);
        }
    }

    private static void doBind0(ChannelFuture paramChannelFuture, final Channel paramChannel, final SocketAddress paramSocketAddress, final ChannelPromise paramChannelPromise) {
        paramChannel.eventLoop().execute(new Runnable() {
            public void run() {
                if (this.val$regFuture.isSuccess()) {
                    paramChannel.bind(paramSocketAddress, paramChannelPromise).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                } else {
                    paramChannelPromise.setFailure(this.val$regFuture.cause());
                }
            }
        });
    }

    public B group(EventLoopGroup paramEventLoopGroup) {
        if (paramEventLoopGroup == null) {
            throw new NullPointerException("group");
        }
        if (this.group != null) {
            throw new IllegalStateException("group set already");
        }
        this.group = paramEventLoopGroup;
        return this;
    }

    public B channel(Class<? extends C> paramClass) {
        if (paramClass == null) {
            throw new NullPointerException("channelClass");
        }
        return channelFactory(new BootstrapChannelFactory(paramClass));
    }

    public B channelFactory(ChannelFactory<? extends C> paramChannelFactory) {
        if (paramChannelFactory == null) {
            throw new NullPointerException("channelFactory");
        }
        if (this.channelFactory != null) {
            throw new IllegalStateException("channelFactory set already");
        }
        this.channelFactory = paramChannelFactory;
        return this;
    }

    public B localAddress(SocketAddress paramSocketAddress) {
        this.localAddress = paramSocketAddress;
        return this;
    }

    public B localAddress(int paramInt) {
        return localAddress(new InetSocketAddress(paramInt));
    }

    public B localAddress(String paramString, int paramInt) {
        return localAddress(new InetSocketAddress(paramString, paramInt));
    }

    public B localAddress(InetAddress paramInetAddress, int paramInt) {
        return localAddress(new InetSocketAddress(paramInetAddress, paramInt));
    }

    public <T> B option(ChannelOption<T> paramChannelOption, T paramT) {
        if (paramChannelOption == null) {
            throw new NullPointerException("option");
        }
        if (paramT == null) {
            synchronized (this.options) {
                this.options.remove(paramChannelOption);
            }
        } else {
            synchronized (this.options) {
                this.options.put(paramChannelOption, paramT);
            }
        }
        return this;
    }

    public <T> B attr(AttributeKey<T> paramAttributeKey, T paramT) {
        if (paramAttributeKey == null) {
            throw new NullPointerException("key");
        }
        if (paramT == null) {
            synchronized (this.attrs) {
                this.attrs.remove(paramAttributeKey);
            }
        } else {
            synchronized (this.attrs) {
                this.attrs.put(paramAttributeKey, paramT);
            }
        }
    ??? =this;
        return (B) ???;
    }

    public B validate() {
        if (this.group == null) {
            throw new IllegalStateException("group not set");
        }
        if (this.channelFactory == null) {
            throw new IllegalStateException("channel or channelFactory not set");
        }
        return this;
    }

    public abstract B clone();

    public ChannelFuture register() {
        validate();
        return initAndRegister();
    }

    public ChannelFuture bind() {
        validate();
        SocketAddress localSocketAddress = this.localAddress;
        if (localSocketAddress == null) {
            throw new IllegalStateException("localAddress not set");
        }
        return doBind(localSocketAddress);
    }

    public ChannelFuture bind(int paramInt) {
        return bind(new InetSocketAddress(paramInt));
    }

    public ChannelFuture bind(String paramString, int paramInt) {
        return bind(new InetSocketAddress(paramString, paramInt));
    }

    public ChannelFuture bind(InetAddress paramInetAddress, int paramInt) {
        return bind(new InetSocketAddress(paramInetAddress, paramInt));
    }

    public ChannelFuture bind(SocketAddress paramSocketAddress) {
        validate();
        if (paramSocketAddress == null) {
            throw new NullPointerException("localAddress");
        }
        return doBind(paramSocketAddress);
    }

    private ChannelFuture doBind(final SocketAddress paramSocketAddress) {
        final ChannelFuture localChannelFuture = initAndRegister();
        final Channel localChannel = localChannelFuture.channel();
        if (localChannelFuture.cause() != null) {
            return localChannelFuture;
        }
        final Object localObject;
        if (localChannelFuture.isDone()) {
            localObject = localChannel.newPromise();
            doBind0(localChannelFuture, localChannel, paramSocketAddress, (ChannelPromise) localObject);
        } else {
            localObject = new PendingRegistrationPromise(localChannel, null);
            localChannelFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture paramAnonymousChannelFuture)
                        throws Exception {
                    AbstractBootstrap.doBind0(localChannelFuture, localChannel, paramSocketAddress, localObject);
                }
            });
        }
        return (ChannelFuture) localObject;
    }

    final ChannelFuture initAndRegister() {
        Channel localChannel = channelFactory().newChannel();
        try {
            init(localChannel);
        } catch (Throwable localThrowable) {
            localChannel.unsafe().closeForcibly();
            return new DefaultChannelPromise(localChannel, GlobalEventExecutor.INSTANCE).setFailure(localThrowable);
        }
        ChannelFuture localChannelFuture = group().register(localChannel);
        if (localChannelFuture.cause() != null) {
            if (localChannel.isRegistered()) {
                localChannel.close();
            } else {
                localChannel.unsafe().closeForcibly();
            }
        }
        return localChannelFuture;
    }

    abstract void init(Channel paramChannel)
            throws Exception;

    public B handler(ChannelHandler paramChannelHandler) {
        if (paramChannelHandler == null) {
            throw new NullPointerException("handler");
        }
        this.handler = paramChannelHandler;
        return this;
    }

    final SocketAddress localAddress() {
        return this.localAddress;
    }

    final ChannelFactory<? extends C> channelFactory() {
        return this.channelFactory;
    }

    final ChannelHandler handler() {
        return this.handler;
    }

    public final EventLoopGroup group() {
        return this.group;
    }

    final Map<ChannelOption<?>, Object> options() {
        return this.options;
    }

    final Map<AttributeKey<?>, Object> attrs() {
        return this.attrs;
    }

    public String toString() {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(StringUtil.simpleClassName(this));
        localStringBuilder.append('(');
        if (this.group != null) {
            localStringBuilder.append("group: ");
            localStringBuilder.append(StringUtil.simpleClassName(this.group));
            localStringBuilder.append(", ");
        }
        if (this.channelFactory != null) {
            localStringBuilder.append("channelFactory: ");
            localStringBuilder.append(this.channelFactory);
            localStringBuilder.append(", ");
        }
        if (this.localAddress != null) {
            localStringBuilder.append("localAddress: ");
            localStringBuilder.append(this.localAddress);
            localStringBuilder.append(", ");
        }
        synchronized (this.options) {
            if (!this.options.isEmpty()) {
                localStringBuilder.append("options: ");
                localStringBuilder.append(this.options);
                localStringBuilder.append(", ");
            }
        }
        synchronized (this.attrs) {
            if (!this.attrs.isEmpty()) {
                localStringBuilder.append("attrs: ");
                localStringBuilder.append(this.attrs);
                localStringBuilder.append(", ");
            }
        }
        if (this.handler != null) {
            localStringBuilder.append("handler: ");
            localStringBuilder.append(this.handler);
            localStringBuilder.append(", ");
        }
        if (localStringBuilder.charAt(localStringBuilder.length() - 1) == '(') {
            localStringBuilder.append(')');
        } else {
            localStringBuilder.setCharAt(localStringBuilder.length() - 2, ')');
            localStringBuilder.setLength(localStringBuilder.length() - 1);
        }
        return localStringBuilder.toString();
    }

    private static final class PendingRegistrationPromise
            extends DefaultChannelPromise {
        private PendingRegistrationPromise(Channel paramChannel) {
            super();
        }

        protected EventExecutor executor() {
            if (channel().isRegistered()) {
                return super.executor();
            }
            return GlobalEventExecutor.INSTANCE;
        }
    }

    private static final class BootstrapChannelFactory<T extends Channel>
            implements ChannelFactory<T> {
        private final Class<? extends T> clazz;

        BootstrapChannelFactory(Class<? extends T> paramClass) {
            this.clazz = paramClass;
        }

        public T newChannel() {
            try {
                return (Channel) this.clazz.newInstance();
            } catch (Throwable localThrowable) {
                throw new ChannelException("Unable to create Channel from class " + this.clazz, localThrowable);
            }
        }

        public String toString() {
            return StringUtil.simpleClassName(this.clazz) + ".class";
        }
    }
}




