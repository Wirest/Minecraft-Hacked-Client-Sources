// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.epoll;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.ChannelConfig;
import io.netty.channel.AbstractChannel;
import io.netty.util.internal.StringUtil;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.channel.socket.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.AddressedEnvelope;
import java.io.IOException;
import io.netty.channel.ChannelOutboundBuffer;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.NetworkInterface;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import io.netty.channel.ChannelOption;
import java.net.InetSocketAddress;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.socket.DatagramChannel;

public final class EpollDatagramChannel extends AbstractEpollChannel implements DatagramChannel
{
    private static final ChannelMetadata METADATA;
    private static final String EXPECTED_TYPES;
    private volatile InetSocketAddress local;
    private volatile InetSocketAddress remote;
    private volatile boolean connected;
    private final EpollDatagramChannelConfig config;
    
    public EpollDatagramChannel() {
        super(Native.socketDgramFd(), 1);
        this.config = new EpollDatagramChannelConfig(this);
    }
    
    @Override
    public ChannelMetadata metadata() {
        return EpollDatagramChannel.METADATA;
    }
    
    @Override
    public boolean isActive() {
        return this.fd != -1 && ((this.config.getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) && this.isRegistered()) || this.active);
    }
    
    @Override
    public boolean isConnected() {
        return this.connected;
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
        if (multicastAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        promise.setFailure((Throwable)new UnsupportedOperationException("Multicast not supported"));
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
        if (multicastAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        promise.setFailure((Throwable)new UnsupportedOperationException("Multicast not supported"));
        return promise;
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress sourceToBlock) {
        return this.block(multicastAddress, networkInterface, sourceToBlock, this.newPromise());
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress sourceToBlock, final ChannelPromise promise) {
        if (multicastAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (sourceToBlock == null) {
            throw new NullPointerException("sourceToBlock");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        promise.setFailure((Throwable)new UnsupportedOperationException("Multicast not supported"));
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
        catch (Throwable e) {
            promise.setFailure(e);
            return promise;
        }
    }
    
    @Override
    protected AbstractEpollUnsafe newUnsafe() {
        return new EpollDatagramChannelUnsafe();
    }
    
    @Override
    protected InetSocketAddress localAddress0() {
        return this.local;
    }
    
    @Override
    protected InetSocketAddress remoteAddress0() {
        return this.remote;
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        final InetSocketAddress addr = (InetSocketAddress)localAddress;
        AbstractEpollChannel.checkResolvable(addr);
        Native.bind(this.fd, addr.getAddress(), addr.getPort());
        this.local = Native.localAddress(this.fd);
        this.active = true;
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        while (true) {
            final Object msg = in.current();
            if (msg == null) {
                this.clearEpollOut();
                break;
            }
            try {
                boolean done = false;
                for (int i = this.config().getWriteSpinCount() - 1; i >= 0; --i) {
                    if (this.doWriteMessage(msg)) {
                        done = true;
                        break;
                    }
                }
                if (!done) {
                    this.setEpollOut();
                    break;
                }
                in.remove();
            }
            catch (IOException e) {
                in.remove(e);
            }
        }
    }
    
    private boolean doWriteMessage(final Object msg) throws IOException {
        ByteBuf data;
        InetSocketAddress remoteAddress;
        if (msg instanceof AddressedEnvelope) {
            final AddressedEnvelope<ByteBuf, InetSocketAddress> envelope = (AddressedEnvelope<ByteBuf, InetSocketAddress>)msg;
            data = envelope.content();
            remoteAddress = envelope.recipient();
        }
        else {
            data = (ByteBuf)msg;
            remoteAddress = null;
        }
        final int dataLen = data.readableBytes();
        if (dataLen == 0) {
            return true;
        }
        if (remoteAddress == null) {
            remoteAddress = this.remote;
            if (remoteAddress == null) {
                throw new NotYetConnectedException();
            }
        }
        int writtenBytes;
        if (data.hasMemoryAddress()) {
            final long memoryAddress = data.memoryAddress();
            writtenBytes = Native.sendToAddress(this.fd, memoryAddress, data.readerIndex(), data.writerIndex(), remoteAddress.getAddress(), remoteAddress.getPort());
        }
        else {
            final ByteBuffer nioData = data.internalNioBuffer(data.readerIndex(), data.readableBytes());
            writtenBytes = Native.sendTo(this.fd, nioData, nioData.position(), nioData.limit(), remoteAddress.getAddress(), remoteAddress.getPort());
        }
        return writtenBytes > 0;
    }
    
    @Override
    protected Object filterOutboundMessage(final Object msg) {
        if (msg instanceof DatagramPacket) {
            final DatagramPacket packet = (DatagramPacket)msg;
            final ByteBuf content = ((DefaultAddressedEnvelope<ByteBuf, A>)packet).content();
            if (content.hasMemoryAddress()) {
                return msg;
            }
            return new DatagramPacket(this.newDirectBuffer(packet, content), ((DefaultAddressedEnvelope<M, InetSocketAddress>)packet).recipient());
        }
        else {
            if (!(msg instanceof ByteBuf)) {
                if (msg instanceof AddressedEnvelope) {
                    final AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope<Object, SocketAddress>)msg;
                    if (e.content() instanceof ByteBuf && (e.recipient() == null || e.recipient() instanceof InetSocketAddress)) {
                        final ByteBuf content = e.content();
                        if (content.hasMemoryAddress()) {
                            return e;
                        }
                        return new DefaultAddressedEnvelope(this.newDirectBuffer(e, content), e.recipient());
                    }
                }
                throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EpollDatagramChannel.EXPECTED_TYPES);
            }
            final ByteBuf buf = (ByteBuf)msg;
            if (buf.hasMemoryAddress()) {
                return msg;
            }
            return this.newDirectBuffer(buf);
        }
    }
    
    @Override
    public EpollDatagramChannelConfig config() {
        return this.config;
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        this.connected = false;
    }
    
    static {
        METADATA = new ChannelMetadata(true);
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(DatagramPacket.class) + ", " + StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(InetSocketAddress.class) + ">, " + StringUtil.simpleClassName(ByteBuf.class) + ')';
    }
    
    final class EpollDatagramChannelUnsafe extends AbstractEpollUnsafe
    {
        private RecvByteBufAllocator.Handle allocHandle;
        
        @Override
        public void connect(final SocketAddress remote, final SocketAddress local, final ChannelPromise channelPromise) {
            boolean success = false;
            try {
                try {
                    final InetSocketAddress remoteAddress = (InetSocketAddress)remote;
                    if (local != null) {
                        final InetSocketAddress localAddress = (InetSocketAddress)local;
                        EpollDatagramChannel.this.doBind(localAddress);
                    }
                    AbstractEpollChannel.checkResolvable(remoteAddress);
                    EpollDatagramChannel.this.remote = remoteAddress;
                    EpollDatagramChannel.this.local = Native.localAddress(EpollDatagramChannel.this.fd);
                    success = true;
                }
                finally {
                    if (!success) {
                        EpollDatagramChannel.this.doClose();
                    }
                    else {
                        channelPromise.setSuccess();
                        EpollDatagramChannel.this.connected = true;
                    }
                }
            }
            catch (Throwable cause) {
                channelPromise.setFailure(cause);
            }
        }
        
        @Override
        void epollInReady() {
            final DatagramChannelConfig config = EpollDatagramChannel.this.config();
            RecvByteBufAllocator.Handle allocHandle = this.allocHandle;
            if (allocHandle == null) {
                allocHandle = (this.allocHandle = config.getRecvByteBufAllocator().newHandle());
            }
            assert EpollDatagramChannel.this.eventLoop().inEventLoop();
            final ChannelPipeline pipeline = EpollDatagramChannel.this.pipeline();
            try {
                while (true) {
                    ByteBuf data = null;
                    try {
                        data = allocHandle.allocate(config.getAllocator());
                        final int writerIndex = data.writerIndex();
                        DatagramSocketAddress remoteAddress;
                        if (data.hasMemoryAddress()) {
                            remoteAddress = Native.recvFromAddress(EpollDatagramChannel.this.fd, data.memoryAddress(), writerIndex, data.capacity());
                        }
                        else {
                            final ByteBuffer nioData = data.internalNioBuffer(writerIndex, data.writableBytes());
                            remoteAddress = Native.recvFrom(EpollDatagramChannel.this.fd, nioData, nioData.position(), nioData.limit());
                        }
                        if (remoteAddress == null) {
                            break;
                        }
                        final int readBytes = remoteAddress.receivedAmount;
                        data.writerIndex(data.writerIndex() + readBytes);
                        allocHandle.record(readBytes);
                        this.readPending = false;
                        pipeline.fireChannelRead(new DatagramPacket(data, (InetSocketAddress)this.localAddress(), remoteAddress));
                        data = null;
                    }
                    catch (Throwable t) {
                        pipeline.fireChannelReadComplete();
                        pipeline.fireExceptionCaught(t);
                    }
                    finally {
                        if (data != null) {
                            data.release();
                        }
                    }
                }
            }
            finally {
                if (!EpollDatagramChannel.this.config().isAutoRead() && !this.readPending) {
                    EpollDatagramChannel.this.clearEpollIn();
                }
            }
        }
    }
    
    static final class DatagramSocketAddress extends InetSocketAddress
    {
        private static final long serialVersionUID = 1348596211215015739L;
        final int receivedAmount;
        
        DatagramSocketAddress(final String addr, final int port, final int receivedAmount) {
            super(addr, port);
            this.receivedAmount = receivedAmount;
        }
    }
}
