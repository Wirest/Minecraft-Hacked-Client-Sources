// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

import io.netty.channel.ChannelPromise;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.TypeParameterMatcher;
import io.netty.channel.ChannelDuplexHandler;

public abstract class MessageToMessageCodec<INBOUND_IN, OUTBOUND_IN> extends ChannelDuplexHandler
{
    private final MessageToMessageEncoder<Object> encoder;
    private final MessageToMessageDecoder<Object> decoder;
    private final TypeParameterMatcher inboundMsgMatcher;
    private final TypeParameterMatcher outboundMsgMatcher;
    
    protected MessageToMessageCodec() {
        this.encoder = new MessageToMessageEncoder<Object>() {
            @Override
            public boolean acceptOutboundMessage(final Object msg) throws Exception {
                return MessageToMessageCodec.this.acceptOutboundMessage(msg);
            }
            
            @Override
            protected void encode(final ChannelHandlerContext ctx, final Object msg, final List<Object> out) throws Exception {
                MessageToMessageCodec.this.encode(ctx, msg, out);
            }
        };
        this.decoder = new MessageToMessageDecoder<Object>() {
            @Override
            public boolean acceptInboundMessage(final Object msg) throws Exception {
                return MessageToMessageCodec.this.acceptInboundMessage(msg);
            }
            
            @Override
            protected void decode(final ChannelHandlerContext ctx, final Object msg, final List<Object> out) throws Exception {
                MessageToMessageCodec.this.decode(ctx, msg, out);
            }
        };
        this.inboundMsgMatcher = TypeParameterMatcher.find(this, MessageToMessageCodec.class, "INBOUND_IN");
        this.outboundMsgMatcher = TypeParameterMatcher.find(this, MessageToMessageCodec.class, "OUTBOUND_IN");
    }
    
    protected MessageToMessageCodec(final Class<? extends INBOUND_IN> inboundMessageType, final Class<? extends OUTBOUND_IN> outboundMessageType) {
        this.encoder = new MessageToMessageEncoder<Object>() {
            @Override
            public boolean acceptOutboundMessage(final Object msg) throws Exception {
                return MessageToMessageCodec.this.acceptOutboundMessage(msg);
            }
            
            @Override
            protected void encode(final ChannelHandlerContext ctx, final Object msg, final List<Object> out) throws Exception {
                MessageToMessageCodec.this.encode(ctx, msg, out);
            }
        };
        this.decoder = new MessageToMessageDecoder<Object>() {
            @Override
            public boolean acceptInboundMessage(final Object msg) throws Exception {
                return MessageToMessageCodec.this.acceptInboundMessage(msg);
            }
            
            @Override
            protected void decode(final ChannelHandlerContext ctx, final Object msg, final List<Object> out) throws Exception {
                MessageToMessageCodec.this.decode(ctx, msg, out);
            }
        };
        this.inboundMsgMatcher = TypeParameterMatcher.get(inboundMessageType);
        this.outboundMsgMatcher = TypeParameterMatcher.get(outboundMessageType);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        this.decoder.channelRead(ctx, msg);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        this.encoder.write(ctx, msg, promise);
    }
    
    public boolean acceptInboundMessage(final Object msg) throws Exception {
        return this.inboundMsgMatcher.match(msg);
    }
    
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return this.outboundMsgMatcher.match(msg);
    }
    
    protected abstract void encode(final ChannelHandlerContext p0, final OUTBOUND_IN p1, final List<Object> p2) throws Exception;
    
    protected abstract void decode(final ChannelHandlerContext p0, final INBOUND_IN p1, final List<Object> p2) throws Exception;
}
