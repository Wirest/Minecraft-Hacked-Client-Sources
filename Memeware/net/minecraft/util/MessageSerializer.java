package net.minecraft.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class MessageSerializer extends MessageToByteEncoder {
    private static final Logger logger = LogManager.getLogger();
    private static final Marker RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_SENT", NetworkManager.logMarkerPackets);
    private final EnumPacketDirection direction;
    private static final String __OBFID = "CL_00001253";

    public MessageSerializer(EnumPacketDirection direction) {
        this.direction = direction;
    }

    protected void encode(ChannelHandlerContext p_encode_1_, Packet p_encode_2_, ByteBuf p_encode_3_) throws IOException {
        Integer var4 = ((EnumConnectionState) p_encode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get()).getPacketId(this.direction, p_encode_2_);

        if (logger.isDebugEnabled()) {
            logger.debug(RECEIVED_PACKET_MARKER, "OUT: [{}:{}] {}", new Object[]{p_encode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get(), var4, p_encode_2_.getClass().getName()});
        }

        if (var4 == null) {
            throw new IOException("Can\'t serialize unregistered packet");
        } else {
            PacketBuffer var5 = new PacketBuffer(p_encode_3_);
            var5.writeVarIntToBuffer(var4.intValue());

            try {
                if (p_encode_2_ instanceof S0CPacketSpawnPlayer) {
                    p_encode_2_ = p_encode_2_;
                }

                p_encode_2_.writePacketData(var5);
            } catch (Throwable var7) {
                logger.error(var7);
            }
        }
    }

    protected void encode(ChannelHandlerContext p_encode_1_, Object p_encode_2_, ByteBuf p_encode_3_) throws IOException {
        this.encode(p_encode_1_, (Packet) p_encode_2_, p_encode_3_);
    }
}
