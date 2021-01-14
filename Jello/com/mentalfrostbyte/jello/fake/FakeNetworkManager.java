package com.mentalfrostbyte.jello.fake;

import net.minecraft.network.*;
import java.net.*;
import io.netty.buffer.*;
import io.netty.util.*;
import io.netty.channel.*;

public class FakeNetworkManager extends NetworkManager
{
    public FakeNetworkManager(final EnumPacketDirection packetDirection) {
        super(packetDirection);
    }
    
    public Channel channel() {
        return (Channel)new Channel() {
           
            
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
            
            public Channel.Unsafe unsafe() {
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
                return (Attribute<T>)new Attribute<T>() {
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
