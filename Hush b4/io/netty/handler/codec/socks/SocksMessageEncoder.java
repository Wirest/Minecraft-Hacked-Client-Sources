// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class SocksMessageEncoder extends MessageToByteEncoder<SocksMessage>
{
    private static final String name = "SOCKS_MESSAGE_ENCODER";
    
    @Deprecated
    public static String getName() {
        return "SOCKS_MESSAGE_ENCODER";
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final SocksMessage msg, final ByteBuf out) throws Exception {
        msg.encodeAsByteBuf(out);
    }
}
