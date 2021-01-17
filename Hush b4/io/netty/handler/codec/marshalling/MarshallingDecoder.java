// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.marshalling;

import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class MarshallingDecoder extends LengthFieldBasedFrameDecoder
{
    private final UnmarshallerProvider provider;
    
    public MarshallingDecoder(final UnmarshallerProvider provider) {
        this(provider, 1048576);
    }
    
    public MarshallingDecoder(final UnmarshallerProvider provider, final int maxObjectSize) {
        super(maxObjectSize, 0, 4, 0, 4);
        this.provider = provider;
    }
    
    @Override
    protected Object decode(final ChannelHandlerContext ctx, final ByteBuf in) throws Exception {
        final ByteBuf frame = (ByteBuf)super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        final Unmarshaller unmarshaller = this.provider.getUnmarshaller(ctx);
        final ByteInput input = (ByteInput)new ChannelBufferByteInput(frame);
        try {
            unmarshaller.start(input);
            final Object obj = unmarshaller.readObject();
            unmarshaller.finish();
            return obj;
        }
        finally {
            unmarshaller.close();
        }
    }
    
    @Override
    protected ByteBuf extractFrame(final ChannelHandlerContext ctx, final ByteBuf buffer, final int index, final int length) {
        return buffer.slice(index, length);
    }
}
