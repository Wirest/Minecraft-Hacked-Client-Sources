// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.sctp.oio;

import io.netty.channel.sctp.DefaultSctpServerChannelConfig;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import com.sun.nio.sctp.SctpChannel;
import java.nio.channels.SelectionKey;
import java.util.List;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Iterator;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import io.netty.channel.Channel;
import java.io.IOException;
import io.netty.channel.ChannelException;
import java.nio.channels.Selector;
import io.netty.channel.sctp.SctpServerChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.sctp.SctpServerChannel;
import io.netty.channel.oio.AbstractOioMessageChannel;

public class OioSctpServerChannel extends AbstractOioMessageChannel implements SctpServerChannel
{
    private static final InternalLogger logger;
    private static final ChannelMetadata METADATA;
    private final com.sun.nio.sctp.SctpServerChannel sch;
    private final SctpServerChannelConfig config;
    private final Selector selector;
    
    private static com.sun.nio.sctp.SctpServerChannel newServerSocket() {
        try {
            return com.sun.nio.sctp.SctpServerChannel.open();
        }
        catch (IOException e) {
            throw new ChannelException("failed to create a sctp server channel", e);
        }
    }
    
    public OioSctpServerChannel() {
        this(newServerSocket());
    }
    
    public OioSctpServerChannel(final com.sun.nio.sctp.SctpServerChannel sch) {
        super(null);
        if (sch == null) {
            throw new NullPointerException("sctp server channel");
        }
        this.sch = sch;
        boolean success = false;
        try {
            sch.configureBlocking(false);
            sch.register(this.selector = Selector.open(), 16);
            this.config = new OioSctpServerChannelConfig(this, sch);
            success = true;
        }
        catch (Exception e) {
            throw new ChannelException("failed to initialize a sctp server channel", e);
        }
        finally {
            if (!success) {
                try {
                    sch.close();
                }
                catch (IOException e2) {
                    OioSctpServerChannel.logger.warn("Failed to close a sctp server channel.", e2);
                }
            }
        }
    }
    
    @Override
    public ChannelMetadata metadata() {
        return OioSctpServerChannel.METADATA;
    }
    
    @Override
    public SctpServerChannelConfig config() {
        return this.config;
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
    public boolean isOpen() {
        return this.sch.isOpen();
    }
    
    @Override
    protected SocketAddress localAddress0() {
        try {
            final Iterator<SocketAddress> i = this.sch.getAllLocalAddresses().iterator();
            if (i.hasNext()) {
                return i.next();
            }
        }
        catch (IOException ex) {}
        return null;
    }
    
    @Override
    public Set<InetSocketAddress> allLocalAddresses() {
        try {
            final Set<SocketAddress> allLocalAddresses = this.sch.getAllLocalAddresses();
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
    public boolean isActive() {
        return this.isOpen() && this.localAddress0() != null;
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.sch.bind(localAddress, this.config.getBacklog());
    }
    
    @Override
    protected void doClose() throws Exception {
        try {
            this.selector.close();
        }
        catch (IOException e) {
            OioSctpServerChannel.logger.warn("Failed to close a selector.", e);
        }
        this.sch.close();
    }
    
    @Override
    protected int doReadMessages(final List<Object> buf) throws Exception {
        if (!this.isActive()) {
            return -1;
        }
        SctpChannel s = null;
        int acceptedChannels = 0;
        try {
            final int selectedKeys = this.selector.select(1000L);
            if (selectedKeys > 0) {
                final Iterator<SelectionKey> selectionKeys = this.selector.selectedKeys().iterator();
                do {
                    final SelectionKey key = selectionKeys.next();
                    selectionKeys.remove();
                    if (key.isAcceptable()) {
                        s = this.sch.accept();
                        if (s != null) {
                            buf.add(new OioSctpChannel(this, s));
                            ++acceptedChannels;
                        }
                    }
                } while (selectionKeys.hasNext());
                return acceptedChannels;
            }
        }
        catch (Throwable t) {
            OioSctpServerChannel.logger.warn("Failed to create a new channel from an accepted sctp channel.", t);
            if (s != null) {
                try {
                    s.close();
                }
                catch (Throwable t2) {
                    OioSctpServerChannel.logger.warn("Failed to close a sctp channel.", t2);
                }
            }
        }
        return acceptedChannels;
    }
    
    @Override
    public ChannelFuture bindAddress(final InetAddress localAddress) {
        return this.bindAddress(localAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture bindAddress(final InetAddress localAddress, final ChannelPromise promise) {
        if (this.eventLoop().inEventLoop()) {
            try {
                this.sch.bindAddress(localAddress);
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
                    OioSctpServerChannel.this.bindAddress(localAddress, promise);
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
                this.sch.unbindAddress(localAddress);
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
                    OioSctpServerChannel.this.unbindAddress(localAddress, promise);
                }
            });
        }
        return promise;
    }
    
    @Override
    protected void doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
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
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected Object filterOutboundMessage(final Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(OioSctpServerChannel.class);
        METADATA = new ChannelMetadata(false);
    }
    
    private final class OioSctpServerChannelConfig extends DefaultSctpServerChannelConfig
    {
        private OioSctpServerChannelConfig(final OioSctpServerChannel channel, final com.sun.nio.sctp.SctpServerChannel javaChannel) {
            super(channel, javaChannel);
        }
        
        @Override
        protected void autoReadCleared() {
            AbstractOioChannel.this.setReadPending(false);
        }
    }
}
