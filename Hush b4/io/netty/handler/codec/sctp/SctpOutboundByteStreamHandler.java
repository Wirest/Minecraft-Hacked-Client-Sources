// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.sctp;

import io.netty.channel.sctp.SctpMessage;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageEncoder;

public class SctpOutboundByteStreamHandler extends MessageToMessageEncoder<ByteBuf>
{
    private final int streamIdentifier;
    private final int protocolIdentifier;
    
    public SctpOutboundByteStreamHandler(final int streamIdentifier, final int protocolIdentifier) {
        this.streamIdentifier = streamIdentifier;
        this.protocolIdentifier = protocolIdentifier;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws Exception {
        out.add(new SctpMessage(this.streamIdentifier, this.protocolIdentifier, msg.retain()));
    }
}
