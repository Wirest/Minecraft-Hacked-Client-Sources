// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.socket.nio;

import io.netty.channel.ChannelConfig;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.SocketException;
import java.net.NetworkInterface;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.util.internal.StringUtil;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.util.ReferenceCounted;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelOutboundBuffer;
import java.nio.ByteBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import io.netty.channel.ChannelOption;
import java.nio.channels.SelectableChannel;
import io.netty.channel.Channel;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.socket.InternetProtocolFamily;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.channel.RecvByteBufAllocator;
import java.nio.channels.MembershipKey;
import java.util.List;
import java.net.InetAddress;
import java.util.Map;
import io.netty.channel.socket.DatagramChannelConfig;
import java.nio.channels.spi.SelectorProvider;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.nio.AbstractNioMessageChannel;

public final class NioDatagramChannel extends AbstractNioMessageChannel implements DatagramChannel
{
    private static final ChannelMetadata METADATA;
    private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER;
    private static final String EXPECTED_TYPES;
    private final DatagramChannelConfig config;
    private Map<InetAddress, List<MembershipKey>> memberships;
    private RecvByteBufAllocator.Handle allocHandle;
    
    private static java.nio.channels.DatagramChannel newSocket(final SelectorProvider provider) {
        try {
            return provider.openDatagramChannel();
        }
        catch (IOException e) {
            throw new ChannelException("Failed to open a socket.", e);
        }
    }
    
    private static java.nio.channels.DatagramChannel newSocket(final SelectorProvider provider, final InternetProtocolFamily ipFamily) {
        if (ipFamily == null) {
            return newSocket(provider);
        }
        checkJavaVersion();
        try {
            return provider.openDatagramChannel(ProtocolFamilyConverter.convert(ipFamily));
        }
        catch (IOException e) {
            throw new ChannelException("Failed to open a socket.", e);
        }
    }
    
    private static void checkJavaVersion() {
        if (PlatformDependent.javaVersion() < 7) {
            throw new UnsupportedOperationException("Only supported on java 7+.");
        }
    }
    
    public NioDatagramChannel() {
        this(newSocket(NioDatagramChannel.DEFAULT_SELECTOR_PROVIDER));
    }
    
    public NioDatagramChannel(final SelectorProvider provider) {
        this(newSocket(provider));
    }
    
    public NioDatagramChannel(final InternetProtocolFamily ipFamily) {
        this(newSocket(NioDatagramChannel.DEFAULT_SELECTOR_PROVIDER, ipFamily));
    }
    
    public NioDatagramChannel(final SelectorProvider provider, final InternetProtocolFamily ipFamily) {
        this(newSocket(provider, ipFamily));
    }
    
    public NioDatagramChannel(final java.nio.channels.DatagramChannel socket) {
        super(null, socket, 1);
        this.config = new NioDatagramChannelConfig(this, socket);
    }
    
    @Override
    public ChannelMetadata metadata() {
        return NioDatagramChannel.METADATA;
    }
    
    @Override
    public DatagramChannelConfig config() {
        return this.config;
    }
    
    @Override
    public boolean isActive() {
        final java.nio.channels.DatagramChannel ch = this.javaChannel();
        return ch.isOpen() && ((this.config.getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) && this.isRegistered()) || ch.socket().isBound());
    }
    
    @Override
    public boolean isConnected() {
        return this.javaChannel().isConnected();
    }
    
    @Override
    protected java.nio.channels.DatagramChannel javaChannel() {
        return (java.nio.channels.DatagramChannel)super.javaChannel();
    }
    
    @Override
    protected SocketAddress localAddress0() {
        return this.javaChannel().socket().getLocalSocketAddress();
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return this.javaChannel().socket().getRemoteSocketAddress();
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.javaChannel().socket().bind(localAddress);
    }
    
    @Override
    protected boolean doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            this.javaChannel().socket().bind(localAddress);
        }
        boolean success = false;
        try {
            this.javaChannel().connect(remoteAddress);
            success = true;
            return true;
        }
        finally {
            if (!success) {
                this.doClose();
            }
        }
    }
    
    @Override
    protected void doFinishConnect() throws Exception {
        throw new Error();
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        this.javaChannel().disconnect();
    }
    
    @Override
    protected void doClose() throws Exception {
        this.javaChannel().close();
    }
    
    @Override
    protected int doReadMessages(final List<Object> buf) throws Exception {
        final java.nio.channels.DatagramChannel ch = this.javaChannel();
        final DatagramChannelConfig config = this.config();
        RecvByteBufAllocator.Handle allocHandle = this.allocHandle;
        if (allocHandle == null) {
            allocHandle = (this.allocHandle = config.getRecvByteBufAllocator().newHandle());
        }
        final ByteBuf data = allocHandle.allocate(config.getAllocator());
        boolean free = true;
        try {
            final ByteBuffer nioData = data.internalNioBuffer(data.writerIndex(), data.writableBytes());
            final int pos = nioData.position();
            final InetSocketAddress remoteAddress = (InetSocketAddress)ch.receive(nioData);
            if (remoteAddress == null) {
                return 0;
            }
            final int readBytes = nioData.position() - pos;
            data.writerIndex(data.writerIndex() + readBytes);
            allocHandle.record(readBytes);
            buf.add(new DatagramPacket(data, this.localAddress(), remoteAddress));
            free = false;
            return 1;
        }
        catch (Throwable cause) {
            PlatformDependent.throwException(cause);
            return -1;
        }
        finally {
            if (free) {
                data.release();
            }
        }
    }
    
    @Override
    protected boolean doWriteMessage(final Object msg, final ChannelOutboundBuffer in) throws Exception {
        SocketAddress remoteAddress;
        ByteBuf data;
        if (msg instanceof AddressedEnvelope) {
            final AddressedEnvelope<ByteBuf, SocketAddress> envelope = (AddressedEnvelope<ByteBuf, SocketAddress>)msg;
            remoteAddress = envelope.recipient();
            data = envelope.content();
        }
        else {
            data = (ByteBuf)msg;
            remoteAddress = null;
        }
        final int dataLen = data.readableBytes();
        if (dataLen == 0) {
            return true;
        }
        final ByteBuffer nioData = data.internalNioBuffer(data.readerIndex(), dataLen);
        int writtenBytes;
        if (remoteAddress != null) {
            writtenBytes = this.javaChannel().send(nioData, remoteAddress);
        }
        else {
            writtenBytes = this.javaChannel().write(nioData);
        }
        return writtenBytes > 0;
    }
    
    @Override
    protected Object filterOutboundMessage(final Object msg) {
        if (msg instanceof DatagramPacket) {
            final DatagramPacket p = (DatagramPacket)msg;
            final ByteBuf content = ((DefaultAddressedEnvelope<ByteBuf, A>)p).content();
            if (isSingleDirectBuffer(content)) {
                return p;
            }
            return new DatagramPacket(this.newDirectBuffer(p, content), ((DefaultAddressedEnvelope<M, InetSocketAddress>)p).recipient());
        }
        else {
            if (!(msg instanceof ByteBuf)) {
                if (msg instanceof AddressedEnvelope) {
                    final AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope<Object, SocketAddress>)msg;
                    if (e.content() instanceof ByteBuf) {
                        final ByteBuf content = e.content();
                        if (isSingleDirectBuffer(content)) {
                            return e;
                        }
                        return new DefaultAddressedEnvelope(this.newDirectBuffer(e, content), e.recipient());
                    }
                }
                throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + NioDatagramChannel.EXPECTED_TYPES);
            }
            final ByteBuf buf = (ByteBuf)msg;
            if (isSingleDirectBuffer(buf)) {
                return buf;
            }
            return this.newDirectBuffer(buf);
        }
    }
    
    private static boolean isSingleDirectBuffer(final ByteBuf buf) {
        return buf.isDirect() && buf.nioBufferCount() == 1;
    }
    
    @Override
    protected boolean continueOnWriteError() {
        return true;
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
    public ChannelFuture joinGroup(final InetAddress multicastAddress) {
        return this.joinGroup(multicastAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress, final ChannelPromise promise) {
        try {
            return this.joinGroup(multicastAddress, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), null, promise);
        }
        catch (SocketException e) {
            promise.setFailure((Throwable)e);
            return promise;
        }
    }
    
    @Override
    public ChannelFuture joinGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface) {
        return this.joinGroup(multicastAddress, networkInterface, this.newPromise());
    }
    
    @Override
    public ChannelFuture joinGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface, final ChannelPromise promise) {
        return this.joinGroup(multicastAddress.getAddress(), networkInterface, null, promise);
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source) {
        return this.joinGroup(multicastAddress, networkInterface, source, this.newPromise());
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source, final ChannelPromise promise) {
        checkJavaVersion();
        if (multicastAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        try {
            MembershipKey key;
            if (source == null) {
                key = this.javaChannel().join(multicastAddress, networkInterface);
            }
            else {
                key = this.javaChannel().join(multicastAddress, networkInterface, source);
            }
            synchronized (this) {
                List<MembershipKey> keys = null;
                if (this.memberships == null) {
                    this.memberships = new HashMap<InetAddress, List<MembershipKey>>();
                }
                else {
                    keys = this.memberships.get(multicastAddress);
                }
                if (keys == null) {
                    keys = new ArrayList<MembershipKey>();
                    this.memberships.put(multicastAddress, keys);
                }
                keys.add(key);
            }
            promise.setSuccess();
        }
        catch (Throwable e) {
            promise.setFailure(e);
        }
        return promise;
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress) {
        return this.leaveGroup(multicastAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress, final ChannelPromise promise) {
        try {
            return this.leaveGroup(multicastAddress, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), null, promise);
        }
        catch (SocketException e) {
            promise.setFailure((Throwable)e);
            return promise;
        }
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface) {
        return this.leaveGroup(multicastAddress, networkInterface, this.newPromise());
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface, final ChannelPromise promise) {
        return this.leaveGroup(multicastAddress.getAddress(), networkInterface, null, promise);
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source) {
        return this.leaveGroup(multicastAddress, networkInterface, source, this.newPromise());
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source, final ChannelPromise promise) {
        checkJavaVersion();
        if (multicastAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        synchronized (this) {
            if (this.memberships != null) {
                final List<MembershipKey> keys = this.memberships.get(multicastAddress);
                if (keys != null) {
                    final Iterator<MembershipKey> keyIt = keys.iterator();
                    while (keyIt.hasNext()) {
                        final MembershipKey key = keyIt.next();
                        if (networkInterface.equals(key.networkInterface()) && ((source == null && key.sourceAddress() == null) || (source != null && source.equals(key.sourceAddress())))) {
                            key.drop();
                            keyIt.remove();
                        }
                    }
                    if (keys.isEmpty()) {
                        this.memberships.remove(multicastAddress);
                    }
                }
            }
        }
        promise.setSuccess();
        return promise;
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress sourceToBlock) {
        return this.block(multicastAddress, networkInterface, sourceToBlock, this.newPromise());
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress sourceToBlock, final ChannelPromise promise) {
        checkJavaVersion();
        if (multicastAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (sourceToBlock == null) {
            throw new NullPointerException("sourceToBlock");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        synchronized (this) {
            if (this.memberships != null) {
                final List<MembershipKey> keys = this.memberships.get(multicastAddress);
                for (final MembershipKey key : keys) {
                    if (networkInterface.equals(key.networkInterface())) {
                        try {
                            key.block(sourceToBlock);
                        }
                        catch (IOException e) {
                            promise.setFailure((Throwable)e);
                        }
                    }
                }
            }
        }
        promise.setSuccess();
        return promise;
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final InetAddress sourceToBlock) {
        return this.block(multicastAddress, sourceToBlock, this.newPromise());
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final InetAddress sourceToBlock, final ChannelPromise promise) {
        try {
            return this.block(multicastAddress, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), sourceToBlock, promise);
        }
        catch (SocketException e) {
            promise.setFailure((Throwable)e);
            return promise;
        }
    }
    
    @Override
    protected void setReadPending(final boolean readPending) {
        super.setReadPending(readPending);
    }
    
    static {
        METADATA = new ChannelMetadata(true);
        DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(DatagramPacket.class) + ", " + StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(SocketAddress.class) + ">, " + StringUtil.simpleClassName(ByteBuf.class) + ')';
    }
}
