// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.protobuf;

import io.netty.buffer.Unpooled;
import com.google.protobuf.MessageLite;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import com.google.protobuf.MessageLiteOrBuilder;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public class ProtobufEncoder extends MessageToMessageEncoder<MessageLiteOrBuilder>
{
    @Override
    protected void encode(final ChannelHandlerContext ctx, final MessageLiteOrBuilder msg, final List<Object> out) throws Exception {
        if (msg instanceof MessageLite) {
            out.add(Unpooled.wrappedBuffer(((MessageLite)msg).toByteArray()));
            return;
        }
        if (msg instanceof MessageLite.Builder) {
            out.add(Unpooled.wrappedBuffer(((MessageLite.Builder)msg).build().toByteArray()));
        }
    }
}
