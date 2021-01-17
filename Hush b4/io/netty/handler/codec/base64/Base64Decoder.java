// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.base64;

import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageDecoder;

@ChannelHandler.Sharable
public class Base64Decoder extends MessageToMessageDecoder<ByteBuf>
{
    private final Base64Dialect dialect;
    
    public Base64Decoder() {
        this(Base64Dialect.STANDARD);
    }
    
    public Base64Decoder(final Base64Dialect dialect) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        this.dialect = dialect;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws Exception {
        out.add(Base64.decode(msg, msg.readerIndex(), msg.readableBytes(), this.dialect));
    }
}
