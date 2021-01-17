// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.protobuf;

import java.io.OutputStream;
import io.netty.buffer.ByteBufOutputStream;
import com.google.protobuf.CodedOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class ProtobufVarint32LengthFieldPrepender extends MessageToByteEncoder<ByteBuf>
{
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf msg, final ByteBuf out) throws Exception {
        final int bodyLen = msg.readableBytes();
        final int headerLen = CodedOutputStream.computeRawVarint32Size(bodyLen);
        out.ensureWritable(headerLen + bodyLen);
        final CodedOutputStream headerOut = CodedOutputStream.newInstance((OutputStream)new ByteBufOutputStream(out), headerLen);
        headerOut.writeRawVarint32(bodyLen);
        headerOut.flush();
        out.writeBytes(msg, msg.readerIndex(), bodyLen);
    }
}
