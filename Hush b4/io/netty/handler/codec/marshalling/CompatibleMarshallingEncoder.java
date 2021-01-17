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
public class CompatibleMarshallingEncoder extends MessageToByteEncoder<Object>
{
    private final MarshallerProvider provider;
    
    public CompatibleMarshallingEncoder(final MarshallerProvider provider) {
        this.provider = provider;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Object msg, final ByteBuf out) throws Exception {
        final Marshaller marshaller = this.provider.getMarshaller(ctx);
        marshaller.start((ByteOutput)new ChannelBufferByteOutput(out));
        marshaller.writeObject(msg);
        marshaller.finish();
        marshaller.close();
    }
}
