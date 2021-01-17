// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import java.util.zip.DataFormatException;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.zip.Inflater;
import io.netty.handler.codec.ByteToMessageDecoder;

public class NettyCompressionDecoder extends ByteToMessageDecoder
{
    private final Inflater inflater;
    private int treshold;
    
    public NettyCompressionDecoder(final int treshold) {
        this.treshold = treshold;
        this.inflater = new Inflater();
    }
    
    @Override
    protected void decode(final ChannelHandlerContext p_decode_1_, final ByteBuf p_decode_2_, final List<Object> p_decode_3_) throws DataFormatException, Exception {
        if (p_decode_2_.readableBytes() != 0) {
            final PacketBuffer packetbuffer = new PacketBuffer(p_decode_2_);
            final int i = packetbuffer.readVarIntFromBuffer();
            if (i == 0) {
                p_decode_3_.add(packetbuffer.readBytes(packetbuffer.readableBytes()));
            }
            else {
                if (i < this.treshold) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is below server threshold of " + this.treshold);
                }
                if (i > 2097152) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is larger than protocol maximum of " + 2097152);
                }
                final byte[] abyte = new byte[packetbuffer.readableBytes()];
                packetbuffer.readBytes(abyte);
                this.inflater.setInput(abyte);
                final byte[] abyte2 = new byte[i];
                this.inflater.inflate(abyte2);
                p_decode_3_.add(Unpooled.wrappedBuffer(abyte2));
                this.inflater.reset();
            }
        }
    }
    
    public void setCompressionTreshold(final int treshold) {
        this.treshold = treshold;
    }
}
