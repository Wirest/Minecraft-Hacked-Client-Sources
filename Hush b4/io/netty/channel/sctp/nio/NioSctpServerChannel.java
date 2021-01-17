// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.sctp.nio;

import io.netty.channel.sctp.DefaultSctpServerChannelConfig;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import com.sun.nio.sctp.SctpChannel;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.net.SocketAddress;
import java.util.LinkedHashSet;
import java.net.InetSocketAddress;
import java.util.Set;
import java.nio.channels.SelectableChannel;
import io.netty.channel.Channel;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.channel.sctp.SctpServerChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.sctp.SctpServerChannel;
import io.netty.channel.nio.AbstractNioMessageChannel;

public class NioSctpServerChannel extends AbstractNioMessageChannel implements SctpServerChannel
{
    private static final ChannelMetadata METADATA;
    private final SctpServerChannelConfig config;
    
    private static com.sun.nio.sctp.SctpServerChannel newSocket() {
        try {
            return com.sun.nio.sctp.SctpServerChannel.open();
        }
        catch (IOException e) {
            throw new ChannelException("Failed to open a server socket.", e);
        }
    }
    
    public NioSctpServerChannel() {
        super(null, newSocket(), 16);
        this.config = new NioSctpServerChannelConfig(this, this.javaChannel());
    }
    
    @Override
    public ChannelMetadata metadata() {
        return NioSctpServerChannel.METADATA;
    }
    
    @Override
    public Set<InetSocketAddress> allLocalAddresses() {
        try {
            final Set<SocketAddress> allLocalAddresses = this.javaChannel().getAllLocalAddresses();
            final Set<InetSocketAddress> addresses = new LinkedHashSet<InetSocketAddress>(allLocalAddresses.size());
            for (final SocketAddress socketAddress : allLocalAddresses) {
                addresses.add((InetSocketAddress)socketAddress);
            }
            return addresses;
        }
        catch (Throwable ignored) {
            return Collections.emptySet();
        }
    }
    
    @Override
    public SctpServerChannelConfig config() {
        return this.config;
    }
    
    @Override
    public boolean isActive() {
        return this.isOpen() && !this.allLocalAddresses().isEmpty();
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return null;
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    @Override
    protected com.sun.nio.sctp.SctpServerChannel javaChannel() {
        return (com.sun.nio.sctp.SctpServerChannel)super.javaChannel();
    }
    
    @Override
    protected SocketAddress localAddress0() {
        try {
            final Iterator<SocketAddress> i = this.javaChannel().getAllLocalAddresses().iterator();
            if (i.hasNext()) {
                return i.next();
            }
        }
        catch (IOException ex) {}
        return null;
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.javaChannel().bind(localAddress, this.config.getBacklog());
    }
    
    @Override
    protected void doClose() throws Exception {
        this.javaChannel().close();
    }
    
    @Override
    protected int doReadMessages(final List<Object> buf) throws Exception {
        final SctpChannel ch = this.javaChannel().accept();
        if (ch == null) {
            return 0;
        }
        buf.add(new NioSctpChannel(this, ch));
        return 1;
    }
    
    @Override
    public ChannelFuture bindAddress(final InetAddress localAddress) {
        return this.bindAddress(localAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture bindAddress(final InetAddress localAddress, final ChannelPromise promise) {
        if (this.eventLoop().inEventLoop()) {
            try {
                this.javaChannel().bindAddress(localAddress);
                promise.setSuccess();
            }
            catch (Throwable t) {
                promise.setFailure(t);
            }
        }
        else {
            this.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    NioSctpServerChannel.this.bindAddress(localAddress, promise);
                }
            });
        }
        return promise;
    }
    
    @Override
    public ChannelFuture unbindAddress(final InetAddress localAddress) {
        return this.unbindAddress(localAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture unbindAddress(final InetAddress localAddress, final ChannelPromise promise) {
        if (this.eventLoop().inEventLoop()) {
            try {
                this.javaChannel().unbindAddress(localAddress);
                promise.setSuccess();
            }
            catch (Throwable t) {
                promise.setFailure(t);
            }
        }
        else {
            this.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    NioSctpServerChannel.this.unbindAddress(localAddress, promise);
                }
            });
        }
        return promise;
    }
    
    @Override
    protected boolean doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void doFinishConnect() throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return null;
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected boolean doWriteMessage(final Object msg, final ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected Object filterOutboundMessage(final Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    static {
        METADATA = new ChannelMetadata(false);
    }
    
    private final class NioSctpServerChannelConfig extends DefaultSctpServerChannelConfig
    {
        private NioSctpServerChannelConfig(final NioSctpServerChannel channel, final com.sun.nio.sctp.SctpServerChannel javaChannel) {
            super(channel, javaChannel);
        }
        
        @Override
        protected void autoReadCleared() {
            AbstractNioChannel.this.setReadPending(false);
        }
    }
}
