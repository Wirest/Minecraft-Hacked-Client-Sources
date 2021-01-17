// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.string;

import io.netty.buffer.ByteBufUtil;
import java.nio.CharBuffer;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import java.nio.charset.Charset;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public class StringEncoder extends MessageToMessageEncoder<CharSequence>
{
    private final Charset charset;
    
    public StringEncoder() {
        this(Charset.defaultCharset());
    }
    
    public StringEncoder(final Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.charset = charset;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final CharSequence msg, final List<Object> out) throws Exception {
        if (msg.length() == 0) {
            return;
        }
        out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), this.charset));
    }
}
