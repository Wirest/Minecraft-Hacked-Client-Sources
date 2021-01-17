// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.network.PacketBuffer;
import java.io.IOException;
import net.minecraft.network.EnumConnectionState;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.MarkerManager;
import net.minecraft.network.NetworkManager;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.EnumPacketDirection;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;
import net.minecraft.network.Packet;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageSerializer extends MessageToByteEncoder<Packet>
{
    private static final Logger logger;
    private static final Marker RECEIVED_PACKET_MARKER;
    private final EnumPacketDirection direction;
    
    static {
        logger = LogManager.getLogger();
        RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_SENT", NetworkManager.logMarkerPackets);
    }
    
    public MessageSerializer(final EnumPacketDirection direction) {
        this.direction = direction;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext p_encode_1_, final Packet p_encode_2_, final ByteBuf p_encode_3_) throws IOException, Exception {
        final Integer integer = p_encode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get().getPacketId(this.direction, p_encode_2_);
        if (MessageSerializer.logger.isDebugEnabled()) {
            MessageSerializer.logger.debug(MessageSerializer.RECEIVED_PACKET_MARKER, "OUT: [{}:{}] {}", p_encode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get(), integer, p_encode_2_.getClass().getName());
        }
        if (integer == null) {
            throw new IOException("Can't serialize unregistered packet");
        }
        final PacketBuffer packetbuffer = new PacketBuffer(p_encode_3_);
        packetbuffer.writeVarIntToBuffer(integer);
        try {
            p_encode_2_.writePacketData(packetbuffer);
        }
        catch (Throwable throwable) {
            MessageSerializer.logger.error(throwable);
        }
    }
}
