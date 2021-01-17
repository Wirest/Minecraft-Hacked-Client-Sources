// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.sctp;

import io.netty.handler.codec.CodecException;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.MessageToMessageDecoder;

public class SctpInboundByteStreamHandler extends MessageToMessageDecoder<SctpMessage>
{
    private final int protocolIdentifier;
    private final int streamIdentifier;
    
    public SctpInboundByteStreamHandler(final int protocolIdentifier, final int streamIdentifier) {
        this.protocolIdentifier = protocolIdentifier;
        this.streamIdentifier = streamIdentifier;
    }
    
    @Override
    public final boolean acceptInboundMessage(final Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && this.acceptInboundMessage((SctpMessage)msg);
    }
    
    protected boolean acceptInboundMessage(final SctpMessage msg) {
        return msg.protocolIdentifier() == this.protocolIdentifier && msg.streamIdentifier() == this.streamIdentifier;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final SctpMessage msg, final List<Object> out) throws Exception {
        if (!msg.isComplete()) {
            throw new CodecException(String.format("Received SctpMessage is not complete, please add %s in the pipeline before this handler", SctpMessageCompletionHandler.class.getSimpleName()));
        }
        out.add(msg.content().retain());
    }
}
