// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

import io.netty.channel.ChannelPromise;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.TypeParameterMatcher;
import io.netty.channel.ChannelDuplexHandler;

public abstract class ByteToMessageCodec<I> extends ChannelDuplexHandler
{
    private final TypeParameterMatcher outboundMsgMatcher;
    private final MessageToByteEncoder<I> encoder;
    private final ByteToMessageDecoder decoder;
    
    protected ByteToMessageCodec() {
        this(true);
    }
    
    protected ByteToMessageCodec(final Class<? extends I> outboundMessageType) {
        this(outboundMessageType, true);
    }
    
    protected ByteToMessageCodec(final boolean preferDirect) {
        this.decoder = new ByteToMessageDecoder() {
            public void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
                ByteToMessageCodec.this.decode(ctx, in, out);
            }
            
            @Override
            protected void decodeLast(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
                ByteToMessageCodec.this.decodeLast(ctx, in, out);
            }
        };
        this.outboundMsgMatcher = TypeParameterMatcher.find(this, ByteToMessageCodec.class, "I");
        this.encoder = new Encoder(preferDirect);
    }
    
    protected ByteToMessageCodec(final Class<? extends I> outboundMessageType, final boolean preferDirect) {
        this.decoder = new ByteToMessageDecoder() {
            public void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
                ByteToMessageCodec.this.decode(ctx, in, out);
            }
            
            @Override
            protected void decodeLast(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
                ByteToMessageCodec.this.decodeLast(ctx, in, out);
            }
        };
        this.checkForSharableAnnotation();
        this.outboundMsgMatcher = TypeParameterMatcher.get(outboundMessageType);
        this.encoder = new Encoder(preferDirect);
    }
    
    private void checkForSharableAnnotation() {
        if (this.isSharable()) {
            throw new IllegalStateException("@Sharable annotation is not allowed");
        }
    }
    
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return this.outboundMsgMatcher.match(msg);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        this.decoder.channelRead(ctx, msg);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        this.encoder.write(ctx, msg, promise);
    }
    
    protected abstract void encode(final ChannelHandlerContext p0, final I p1, final ByteBuf p2) throws Exception;
    
    protected abstract void decode(final ChannelHandlerContext p0, final ByteBuf p1, final List<Object> p2) throws Exception;
    
    protected void decodeLast(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        this.decode(ctx, in, out);
    }
    
    private final class Encoder extends MessageToByteEncoder<I>
    {
        Encoder(final boolean preferDirect) {
            super(preferDirect);
        }
        
        @Override
        public boolean acceptOutboundMessage(final Object msg) throws Exception {
            return ByteToMessageCodec.this.acceptOutboundMessage(msg);
        }
        
        @Override
        protected void encode(final ChannelHandlerContext ctx, final I msg, final ByteBuf out) throws Exception {
            ByteToMessageCodec.this.encode(ctx, msg, out);
        }
    }
}
