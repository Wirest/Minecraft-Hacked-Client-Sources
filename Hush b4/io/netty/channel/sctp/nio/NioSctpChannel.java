// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.sctp.nio;

import io.netty.channel.sctp.DefaultSctpChannelConfig;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOutboundBuffer;
import com.sun.nio.sctp.MessageInfo;
import java.nio.ByteBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.sctp.SctpMessage;
import java.util.List;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;
import java.net.SocketAddress;
import java.util.LinkedHashSet;
import java.util.Set;
import com.sun.nio.sctp.Association;
import io.netty.channel.sctp.SctpServerChannel;
import java.net.InetSocketAddress;
import io.netty.channel.sctp.SctpNotificationHandler;
import java.nio.channels.SelectableChannel;
import io.netty.channel.Channel;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.channel.RecvByteBufAllocator;
import com.sun.nio.sctp.NotificationHandler;
import io.netty.channel.sctp.SctpChannelConfig;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.sctp.SctpChannel;
import io.netty.channel.nio.AbstractNioMessageChannel;

public class NioSctpChannel extends AbstractNioMessageChannel implements SctpChannel
{
    private static final ChannelMetadata METADATA;
    private static final InternalLogger logger;
    private final SctpChannelConfig config;
    private final NotificationHandler<?> notificationHandler;
    private RecvByteBufAllocator.Handle allocHandle;
    
    private static com.sun.nio.sctp.SctpChannel newSctpChannel() {
        try {
            return com.sun.nio.sctp.SctpChannel.open();
        }
        catch (IOException e) {
            throw new ChannelException("Failed to open a sctp channel.", e);
        }
    }
    
    public NioSctpChannel() {
        this(newSctpChannel());
    }
    
    public NioSctpChannel(final com.sun.nio.sctp.SctpChannel sctpChannel) {
        this(null, sctpChannel);
    }
    
    public NioSctpChannel(final Channel parent, final com.sun.nio.sctp.SctpChannel sctpChannel) {
        super(parent, sctpChannel, 1);
        try {
            sctpChannel.configureBlocking(false);
            this.config = new NioSctpChannelConfig(this, sctpChannel);
            this.notificationHandler = new SctpNotificationHandler(this);
        }
        catch (IOException e3) {
            try {
                sctpChannel.close();
            }
            catch (IOException e2) {
                if (NioSctpChannel.logger.isWarnEnabled()) {
                    NioSctpChannel.logger.warn("Failed to close a partially initialized sctp channel.", e2);
                }
            }
            throw new ChannelException("Failed to enter non-blocking mode.", e3);
        }
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }
    
    @Override
    public SctpServerChannel parent() {
        return (SctpServerChannel)super.parent();
    }
    
    @Override
    public ChannelMetadata metadata() {
        return NioSctpChannel.METADATA;
    }
    
    @Override
    public Association association() {
        try {
            return this.javaChannel().association();
        }
        catch (IOException ignored) {
            return null;
        }
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
    public SctpChannelConfig config() {
        return this.config;
    }
    
    @Override
    public Set<InetSocketAddress> allRemoteAddresses() {
        try {
            final Set<SocketAddress> allLocalAddresses = this.javaChannel().getRemoteAddresses();
            final Set<InetSocketAddress> addresses = new HashSet<InetSocketAddress>(allLocalAddresses.size());
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
    protected com.sun.nio.sctp.SctpChannel javaChannel() {
        return (com.sun.nio.sctp.SctpChannel)super.javaChannel();
    }
    
    @Override
    public boolean isActive() {
        final com.sun.nio.sctp.SctpChannel ch = this.javaChannel();
        return ch.isOpen() && this.association() != null;
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
    protected SocketAddress remoteAddress0() {
        try {
            final Iterator<SocketAddress> i = this.javaChannel().getRemoteAddresses().iterator();
            if (i.hasNext()) {
                return i.next();
            }
        }
        catch (IOException ex) {}
        return null;
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.javaChannel().bind(localAddress);
    }
    
    @Override
    protected boolean doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            this.javaChannel().bind(localAddress);
        }
        boolean success = false;
        try {
            final boolean connected = this.javaChannel().connect(remoteAddress);
            if (!connected) {
                this.selectionKey().interestOps(8);
            }
            success = true;
            return connected;
        }
        finally {
            if (!success) {
                this.doClose();
            }
        }
    }
    
    @Override
    protected void doFinishConnect() throws Exception {
        if (!this.javaChannel().finishConnect()) {
            throw new Error();
        }
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }
    
    @Override
    protected void doClose() throws Exception {
        this.javaChannel().close();
    }
    
    @Override
    protected int doReadMessages(final List<Object> buf) throws Exception {
        final com.sun.nio.sctp.SctpChannel ch = this.javaChannel();
        RecvByteBufAllocator.Handle allocHandle = this.allocHandle;
        if (allocHandle == null) {
            allocHandle = (this.allocHandle = this.config().getRecvByteBufAllocator().newHandle());
        }
        final ByteBuf buffer = allocHandle.allocate(this.config().getAllocator());
        boolean free = true;
        try {
            final ByteBuffer data = buffer.internalNioBuffer(buffer.writerIndex(), buffer.writableBytes());
            final int pos = data.position();
            final MessageInfo messageInfo = ch.receive(data, null, this.notificationHandler);
            if (messageInfo == null) {
                return 0;
            }
            buf.add(new SctpMessage(messageInfo, buffer.writerIndex(buffer.writerIndex() + data.position() - pos)));
            free = false;
            return 1;
        }
        catch (Throwable cause) {
            PlatformDependent.throwException(cause);
            return -1;
        }
        finally {
            final int bytesRead = buffer.readableBytes();
            allocHandle.record(bytesRead);
            if (free) {
                buffer.release();
            }
        }
    }
    
    @Override
    protected boolean doWriteMessage(final Object msg, final ChannelOutboundBuffer in) throws Exception {
        final SctpMessage packet = (SctpMessage)msg;
        ByteBuf data = packet.content();
        final int dataLen = data.readableBytes();
        if (dataLen == 0) {
            return true;
        }
        final ByteBufAllocator alloc = this.alloc();
        boolean needsCopy = data.nioBufferCount() != 1;
        if (!needsCopy && !data.isDirect() && alloc.isDirectBufferPooled()) {
            needsCopy = true;
        }
        ByteBuffer nioData;
        if (!needsCopy) {
            nioData = data.nioBuffer();
        }
        else {
            data = alloc.directBuffer(dataLen).writeBytes(data);
            nioData = data.nioBuffer();
        }
        final MessageInfo mi = MessageInfo.createOutgoing(this.association(), null, packet.streamIdentifier());
        mi.payloadProtocolID(packet.protocolIdentifier());
        mi.streamNumber(packet.streamIdentifier());
        final int writtenBytes = this.javaChannel().send(nioData, mi);
        return writtenBytes > 0;
    }
    
    @Override
    protected final Object filterOutboundMessage(final Object msg) throws Exception {
        if (!(msg instanceof SctpMessage)) {
            throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + " (expected: " + StringUtil.simpleClassName(SctpMessage.class));
        }
        final SctpMessage m = (SctpMessage)msg;
        final ByteBuf buf = m.content();
        if (buf.isDirect() && buf.nioBufferCount() == 1) {
            return m;
        }
        return new SctpMessage(m.protocolIdentifier(), m.streamIdentifier(), this.newDirectBuffer(m, buf));
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
                    NioSctpChannel.this.bindAddress(localAddress, promise);
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
                    NioSctpChannel.this.unbindAddress(localAddress, promise);
                }
            });
        }
        return promise;
    }
    
    static {
        METADATA = new ChannelMetadata(false);
        logger = InternalLoggerFactory.getInstance(NioSctpChannel.class);
    }
    
    private final class NioSctpChannelConfig extends DefaultSctpChannelConfig
    {
        private NioSctpChannelConfig(final NioSctpChannel channel, final com.sun.nio.sctp.SctpChannel javaChannel) {
            super(channel, javaChannel);
        }
        
        @Override
        protected void autoReadCleared() {
            AbstractNioChannel.this.setReadPending(false);
        }
    }
}
