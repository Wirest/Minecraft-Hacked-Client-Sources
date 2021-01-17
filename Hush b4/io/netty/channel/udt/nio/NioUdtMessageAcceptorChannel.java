// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.udt.nio;

import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.channel.Channel;
import java.util.List;
import com.barchart.udt.TypeUDT;
import io.netty.channel.ChannelMetadata;

public class NioUdtMessageAcceptorChannel extends NioUdtAcceptorChannel
{
    private static final ChannelMetadata METADATA;
    
    public NioUdtMessageAcceptorChannel() {
        super(TypeUDT.DATAGRAM);
    }
    
    @Override
    protected int doReadMessages(final List<Object> buf) throws Exception {
        final SocketChannelUDT channelUDT = this.javaChannel().accept();
        if (channelUDT == null) {
            return 0;
        }
        buf.add(new NioUdtMessageConnectorChannel(this, channelUDT));
        return 1;
    }
    
    @Override
    public ChannelMetadata metadata() {
        return NioUdtMessageAcceptorChannel.METADATA;
    }
    
    static {
        METADATA = new ChannelMetadata(false);
    }
}
