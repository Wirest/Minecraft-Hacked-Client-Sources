// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.serialization;

import java.io.ObjectInputStream;
import java.io.InputStream;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ObjectDecoder extends LengthFieldBasedFrameDecoder
{
    private final ClassResolver classResolver;
    
    public ObjectDecoder(final ClassResolver classResolver) {
        this(1048576, classResolver);
    }
    
    public ObjectDecoder(final int maxObjectSize, final ClassResolver classResolver) {
        super(maxObjectSize, 0, 4, 0, 4);
        this.classResolver = classResolver;
    }
    
    @Override
    protected Object decode(final ChannelHandlerContext ctx, final ByteBuf in) throws Exception {
        final ByteBuf frame = (ByteBuf)super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        final ObjectInputStream is = new CompactObjectInputStream(new ByteBufInputStream(frame), this.classResolver);
        final Object result = is.readObject();
        is.close();
        return result;
    }
    
    @Override
    protected ByteBuf extractFrame(final ChannelHandlerContext ctx, final ByteBuf buffer, final int index, final int length) {
        return buffer.slice(index, length);
    }
}
