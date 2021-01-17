// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.udt.nio;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.udt.UdtChannelConfig;
import io.netty.channel.ChannelConfig;
import java.net.InetSocketAddress;
import io.netty.channel.ChannelOutboundBuffer;
import java.net.SocketAddress;
import com.barchart.udt.TypeUDT;
import io.netty.channel.ChannelException;
import com.barchart.udt.nio.ChannelUDT;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.DefaultUdtServerChannelConfig;
import java.nio.channels.SelectableChannel;
import io.netty.channel.Channel;
import com.barchart.udt.nio.ServerSocketChannelUDT;
import io.netty.channel.udt.UdtServerChannelConfig;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.udt.UdtServerChannel;
import io.netty.channel.nio.AbstractNioMessageChannel;

public abstract class NioUdtAcceptorChannel extends AbstractNioMessageChannel implements UdtServerChannel
{
    protected static final InternalLogger logger;
    private final UdtServerChannelConfig config;
    
    protected NioUdtAcceptorChannel(final ServerSocketChannelUDT channelUDT) {
        super(null, (SelectableChannel)channelUDT, 16);
        try {
            channelUDT.configureBlocking(false);
            this.config = new DefaultUdtServerChannelConfig(this, (ChannelUDT)channelUDT, true);
        }
        catch (Exception e3) {
            try {
                channelUDT.close();
            }
            catch (Exception e2) {
                if (NioUdtAcceptorChannel.logger.isWarnEnabled()) {
                    NioUdtAcceptorChannel.logger.warn("Failed to close channel.", e2);
                }
            }
            throw new ChannelException("Failed to configure channel.", e3);
        }
    }
    
    protected NioUdtAcceptorChannel(final TypeUDT type) {
        this(NioUdtProvider.newAcceptorChannelUDT(type));
    }
    
    @Override
    public UdtServerChannelConfig config() {
        return this.config;
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.javaChannel().socket().bind(localAddress, this.config.getBacklog());
    }
    
    @Override
    protected void doClose() throws Exception {
        this.javaChannel().close();
    }
    
    @Override
    protected boolean doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void doFinishConnect() throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected boolean doWriteMessage(final Object msg, final ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected final Object filterOutboundMessage(final Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isActive() {
        return this.javaChannel().socket().isBound();
    }
    
    protected ServerSocketChannelUDT javaChannel() {
        return (ServerSocketChannelUDT)super.javaChannel();
    }
    
    @Override
    protected SocketAddress localAddress0() {
        return this.javaChannel().socket().getLocalSocketAddress();
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return null;
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return null;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(NioUdtAcceptorChannel.class);
    }
}
