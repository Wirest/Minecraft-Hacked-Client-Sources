// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.udt.nio;

import com.barchart.udt.SocketUDT;
import com.barchart.udt.nio.RendezvousChannelUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import java.io.IOException;
import io.netty.channel.ChannelException;
import com.barchart.udt.nio.SelectorProviderUDT;
import com.barchart.udt.nio.ServerSocketChannelUDT;
import com.barchart.udt.nio.ChannelUDT;
import io.netty.channel.Channel;
import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.KindUDT;
import java.nio.channels.spi.SelectorProvider;
import io.netty.channel.udt.UdtServerChannel;
import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.udt.UdtChannel;

public final class NioUdtProvider<T extends UdtChannel> implements ChannelFactory<T>
{
    public static final ChannelFactory<UdtServerChannel> BYTE_ACCEPTOR;
    public static final ChannelFactory<UdtChannel> BYTE_CONNECTOR;
    public static final SelectorProvider BYTE_PROVIDER;
    public static final ChannelFactory<UdtChannel> BYTE_RENDEZVOUS;
    public static final ChannelFactory<UdtServerChannel> MESSAGE_ACCEPTOR;
    public static final ChannelFactory<UdtChannel> MESSAGE_CONNECTOR;
    public static final SelectorProvider MESSAGE_PROVIDER;
    public static final ChannelFactory<UdtChannel> MESSAGE_RENDEZVOUS;
    private final KindUDT kind;
    private final TypeUDT type;
    
    public static ChannelUDT channelUDT(final Channel channel) {
        if (channel instanceof NioUdtByteAcceptorChannel) {
            return (ChannelUDT)((NioUdtByteAcceptorChannel)channel).javaChannel();
        }
        if (channel instanceof NioUdtByteConnectorChannel) {
            return (ChannelUDT)((NioUdtByteConnectorChannel)channel).javaChannel();
        }
        if (channel instanceof NioUdtByteRendezvousChannel) {
            return (ChannelUDT)((NioUdtByteRendezvousChannel)channel).javaChannel();
        }
        if (channel instanceof NioUdtMessageAcceptorChannel) {
            return (ChannelUDT)((NioUdtMessageAcceptorChannel)channel).javaChannel();
        }
        if (channel instanceof NioUdtMessageConnectorChannel) {
            return (ChannelUDT)((NioUdtMessageConnectorChannel)channel).javaChannel();
        }
        if (channel instanceof NioUdtMessageRendezvousChannel) {
            return (ChannelUDT)((NioUdtMessageRendezvousChannel)channel).javaChannel();
        }
        return null;
    }
    
    static ServerSocketChannelUDT newAcceptorChannelUDT(final TypeUDT type) {
        try {
            return SelectorProviderUDT.from(type).openServerSocketChannel();
        }
        catch (IOException e) {
            throw new ChannelException("failed to open a server socket channel", e);
        }
    }
    
    static SocketChannelUDT newConnectorChannelUDT(final TypeUDT type) {
        try {
            return SelectorProviderUDT.from(type).openSocketChannel();
        }
        catch (IOException e) {
            throw new ChannelException("failed to open a socket channel", e);
        }
    }
    
    static RendezvousChannelUDT newRendezvousChannelUDT(final TypeUDT type) {
        try {
            return SelectorProviderUDT.from(type).openRendezvousChannel();
        }
        catch (IOException e) {
            throw new ChannelException("failed to open a rendezvous channel", e);
        }
    }
    
    public static SocketUDT socketUDT(final Channel channel) {
        final ChannelUDT channelUDT = channelUDT(channel);
        if (channelUDT == null) {
            return null;
        }
        return channelUDT.socketUDT();
    }
    
    private NioUdtProvider(final TypeUDT type, final KindUDT kind) {
        this.type = type;
        this.kind = kind;
    }
    
    public KindUDT kind() {
        return this.kind;
    }
    
    @Override
    public T newChannel() {
        switch (this.kind) {
            case ACCEPTOR: {
                switch (this.type) {
                    case DATAGRAM: {
                        return (T)new NioUdtMessageAcceptorChannel();
                    }
                    case STREAM: {
                        return (T)new NioUdtByteAcceptorChannel();
                    }
                    default: {
                        throw new IllegalStateException("wrong type=" + this.type);
                    }
                }
                break;
            }
            case CONNECTOR: {
                switch (this.type) {
                    case DATAGRAM: {
                        return (T)new NioUdtMessageConnectorChannel();
                    }
                    case STREAM: {
                        return (T)new NioUdtByteConnectorChannel();
                    }
                    default: {
                        throw new IllegalStateException("wrong type=" + this.type);
                    }
                }
                break;
            }
            case RENDEZVOUS: {
                switch (this.type) {
                    case DATAGRAM: {
                        return (T)new NioUdtMessageRendezvousChannel();
                    }
                    case STREAM: {
                        return (T)new NioUdtByteRendezvousChannel();
                    }
                    default: {
                        throw new IllegalStateException("wrong type=" + this.type);
                    }
                }
                break;
            }
            default: {
                throw new IllegalStateException("wrong kind=" + this.kind);
            }
        }
    }
    
    public TypeUDT type() {
        return this.type;
    }
    
    static {
        BYTE_ACCEPTOR = new NioUdtProvider<UdtServerChannel>(TypeUDT.STREAM, KindUDT.ACCEPTOR);
        BYTE_CONNECTOR = new NioUdtProvider<UdtChannel>(TypeUDT.STREAM, KindUDT.CONNECTOR);
        BYTE_PROVIDER = (SelectorProvider)SelectorProviderUDT.STREAM;
        BYTE_RENDEZVOUS = new NioUdtProvider<UdtChannel>(TypeUDT.STREAM, KindUDT.RENDEZVOUS);
        MESSAGE_ACCEPTOR = new NioUdtProvider<UdtServerChannel>(TypeUDT.DATAGRAM, KindUDT.ACCEPTOR);
        MESSAGE_CONNECTOR = new NioUdtProvider<UdtChannel>(TypeUDT.DATAGRAM, KindUDT.CONNECTOR);
        MESSAGE_PROVIDER = (SelectorProvider)SelectorProviderUDT.DATAGRAM;
        MESSAGE_RENDEZVOUS = new NioUdtProvider<UdtChannel>(TypeUDT.DATAGRAM, KindUDT.RENDEZVOUS);
    }
}
