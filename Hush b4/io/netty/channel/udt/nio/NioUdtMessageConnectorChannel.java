// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.udt.nio;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelConfig;
import io.netty.util.internal.StringUtil;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.udt.UdtMessage;
import java.nio.channels.ScatteringByteChannel;
import java.util.List;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import io.netty.channel.ChannelException;
import com.barchart.udt.nio.ChannelUDT;
import io.netty.channel.udt.DefaultUdtChannelConfig;
import java.nio.channels.SelectableChannel;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.channel.Channel;
import com.barchart.udt.TypeUDT;
import io.netty.channel.udt.UdtChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.nio.AbstractNioMessageChannel;

public class NioUdtMessageConnectorChannel extends AbstractNioMessageChannel implements UdtChannel
{
    private static final InternalLogger logger;
    private static final ChannelMetadata METADATA;
    private static final String EXPECTED_TYPE;
    private final UdtChannelConfig config;
    
    public NioUdtMessageConnectorChannel() {
        this(TypeUDT.DATAGRAM);
    }
    
    public NioUdtMessageConnectorChannel(final Channel parent, final SocketChannelUDT channelUDT) {
        super(parent, (SelectableChannel)channelUDT, 1);
        try {
            channelUDT.configureBlocking(false);
            switch (channelUDT.socketUDT().status()) {
                case INIT:
                case OPENED: {
                    this.config = new DefaultUdtChannelConfig(this, (ChannelUDT)channelUDT, true);
                    break;
                }
                default: {
                    this.config = new DefaultUdtChannelConfig(this, (ChannelUDT)channelUDT, false);
                    break;
                }
            }
        }
        catch (Exception e3) {
            try {
                channelUDT.close();
            }
            catch (Exception e2) {
                if (NioUdtMessageConnectorChannel.logger.isWarnEnabled()) {
                    NioUdtMessageConnectorChannel.logger.warn("Failed to close channel.", e2);
                }
            }
            throw new ChannelException("Failed to configure channel.", e3);
        }
    }
    
    public NioUdtMessageConnectorChannel(final SocketChannelUDT channelUDT) {
        this(null, channelUDT);
    }
    
    public NioUdtMessageConnectorChannel(final TypeUDT type) {
        this(NioUdtProvider.newConnectorChannelUDT(type));
    }
    
    @Override
    public UdtChannelConfig config() {
        return this.config;
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.javaChannel().bind(localAddress);
    }
    
    @Override
    protected void doClose() throws Exception {
        this.javaChannel().close();
    }
    
    @Override
    protected boolean doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        this.doBind((localAddress != null) ? localAddress : new InetSocketAddress(0));
        boolean success = false;
        try {
            final boolean connected = this.javaChannel().connect(remoteAddress);
            if (!connected) {
                this.selectionKey().interestOps(this.selectionKey().interestOps() | 0x8);
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
    protected void doDisconnect() throws Exception {
        this.doClose();
    }
    
    @Override
    protected void doFinishConnect() throws Exception {
        if (this.javaChannel().finishConnect()) {
            this.selectionKey().interestOps(this.selectionKey().interestOps() & 0xFFFFFFF7);
            return;
        }
        throw new Error("Provider error: failed to finish connect. Provider library should be upgraded.");
    }
    
    @Override
    protected int doReadMessages(final List<Object> buf) throws Exception {
        final int maximumMessageSize = this.config.getReceiveBufferSize();
        final ByteBuf byteBuf = this.config.getAllocator().directBuffer(maximumMessageSize);
        final int receivedMessageSize = byteBuf.writeBytes((ScatteringByteChannel)this.javaChannel(), maximumMessageSize);
        if (receivedMessageSize <= 0) {
            byteBuf.release();
            return 0;
        }
        if (receivedMessageSize >= maximumMessageSize) {
            this.javaChannel().close();
            throw new ChannelException("Invalid config : increase receive buffer size to avoid message truncation");
        }
        buf.add(new UdtMessage(byteBuf));
        return 1;
    }
    
    @Override
    protected boolean doWriteMessage(final Object msg, final ChannelOutboundBuffer in) throws Exception {
        final UdtMessage message = (UdtMessage)msg;
        final ByteBuf byteBuf = message.content();
        final int messageSize = byteBuf.readableBytes();
        long writtenBytes;
        if (byteBuf.nioBufferCount() == 1) {
            writtenBytes = this.javaChannel().write(byteBuf.nioBuffer());
        }
        else {
            writtenBytes = this.javaChannel().write(byteBuf.nioBuffers());
        }
        if (writtenBytes <= 0L && messageSize > 0) {
            return false;
        }
        if (writtenBytes != messageSize) {
            throw new Error("Provider error: failed to write message. Provider library should be upgraded.");
        }
        return true;
    }
    
    @Override
    protected final Object filterOutboundMessage(final Object msg) throws Exception {
        if (msg instanceof UdtMessage) {
            return msg;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + NioUdtMessageConnectorChannel.EXPECTED_TYPE);
    }
    
    @Override
    public boolean isActive() {
        final SocketChannelUDT channelUDT = this.javaChannel();
        return channelUDT.isOpen() && channelUDT.isConnectFinished();
    }
    
    protected SocketChannelUDT javaChannel() {
        return (SocketChannelUDT)super.javaChannel();
    }
    
    @Override
    protected SocketAddress localAddress0() {
        return this.javaChannel().socket().getLocalSocketAddress();
    }
    
    @Override
    public ChannelMetadata metadata() {
        return NioUdtMessageConnectorChannel.METADATA;
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return this.javaChannel().socket().getRemoteSocketAddress();
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(NioUdtMessageConnectorChannel.class);
        METADATA = new ChannelMetadata(false);
        EXPECTED_TYPE = " (expected: " + StringUtil.simpleClassName(UdtMessage.class) + ')';
    }
}
