package cedo.util.fake;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;

import java.net.SocketAddress;

public class FakeNetworkManager extends NetworkManager {
    public FakeNetworkManager(final EnumPacketDirection packetDirection) {
        super(packetDirection);
    }

    public Channel channel() {
        return new Channel() {

            @Override
            public ChannelId id() {
                return null;
            }

            public EventLoop eventLoop() {
                return null;
            }

            public Channel parent() {
                return null;
            }

            public ChannelConfig config() {
                return null;
            }

            public boolean isOpen() {
                return false;
            }

            public boolean isRegistered() {
                return false;
            }

            public boolean isActive() {
                return false;
            }

            public ChannelMetadata metadata() {
                return null;
            }

            public SocketAddress localAddress() {
                return null;
            }

            public SocketAddress remoteAddress() {
                return null;
            }

            public ChannelFuture closeFuture() {
                return null;
            }

            public boolean isWritable() {
                return false;
            }

            public long bytesBeforeUnwritable() {
                return 0L;
            }

            public long bytesBeforeWritable() {
                return 0L;
            }

            public Unsafe unsafe() {
                return null;
            }

            public ChannelPipeline pipeline() {
                return null;
            }

            public ByteBufAllocator alloc() {
                return null;
            }

            public ChannelPromise newPromise() {
                return null;
            }

            public ChannelProgressivePromise newProgressivePromise() {
                return null;
            }

            public ChannelFuture newSucceededFuture() {
                return null;
            }

            public ChannelFuture newFailedFuture(final Throwable cause) {
                return null;
            }

            public ChannelPromise voidPromise() {
                return null;
            }

            public ChannelFuture bind(final SocketAddress localAddress) {
                return null;
            }

            public ChannelFuture connect(final SocketAddress remoteAddress) {
                return null;
            }

            public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
                return null;
            }

            public ChannelFuture disconnect() {
                return null;
            }

            public ChannelFuture close() {
                return null;
            }

            public ChannelFuture deregister() {
                return null;
            }

            public ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise) {
                return null;
            }

            public ChannelFuture connect(final SocketAddress remoteAddress, final ChannelPromise promise) {
                return null;
            }

            public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
                return null;
            }

            public ChannelFuture disconnect(final ChannelPromise promise) {
                return null;
            }

            public ChannelFuture close(final ChannelPromise promise) {
                return null;
            }

            public ChannelFuture deregister(final ChannelPromise promise) {
                return null;
            }

            public Channel read() {
                return null;
            }

            public ChannelFuture write(final Object msg) {
                return null;
            }

            public ChannelFuture write(final Object msg, final ChannelPromise promise) {
                return null;
            }

            public Channel flush() {
                return null;
            }

            public ChannelFuture writeAndFlush(final Object msg, final ChannelPromise promise) {
                return null;
            }

            public ChannelFuture writeAndFlush(final Object msg) {
                return null;
            }

            public <T> Attribute<T> attr(final AttributeKey<T> key) {
                return new Attribute<T>() {
                    public T setIfAbsent(final T value) {
                        return null;
                    }

                    public T getAndSet(final T value) {
                        return null;
                    }

                    public AttributeKey<T> key() {
                        return null;
                    }

                    public T getAndRemove() {
                        return null;
                    }

                    public void remove() {
                    }

                    public T get() {
                        return null;
                    }

                    public boolean compareAndSet(final T oldValue, final T newValue) {
                        return false;
                    }

                    public void set(final T value) {
                    }
                };
            }

            public <T> boolean hasAttr(final AttributeKey<T> key) {
                return false;
            }

            public int compareTo(final Channel o) {
                return 0;
            }
        };
    }
}
