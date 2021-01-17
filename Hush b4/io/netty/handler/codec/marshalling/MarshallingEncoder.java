// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.marshalling;

import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.ByteOutput;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class MarshallingEncoder extends MessageToByteEncoder<Object>
{
    private static final byte[] LENGTH_PLACEHOLDER;
    private final MarshallerProvider provider;
    
    public MarshallingEncoder(final MarshallerProvider provider) {
        this.provider = provider;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Object msg, final ByteBuf out) throws Exception {
        final Marshaller marshaller = this.provider.getMarshaller(ctx);
        final int lengthPos = out.writerIndex();
        out.writeBytes(MarshallingEncoder.LENGTH_PLACEHOLDER);
        final ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
        marshaller.start((ByteOutput)output);
        marshaller.writeObject(msg);
        marshaller.finish();
        marshaller.close();
        out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);
    }
    
    static {
        LENGTH_PLACEHOLDER = new byte[4];
    }
}
