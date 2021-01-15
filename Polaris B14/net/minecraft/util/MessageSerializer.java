/*    */ package net.minecraft.util;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import io.netty.util.Attribute;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.EnumConnectionState;
/*    */ import net.minecraft.network.EnumPacketDirection;
/*    */ import net.minecraft.network.NetworkManager;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.MarkerManager;
/*    */ 
/*    */ public class MessageSerializer extends MessageToByteEncoder<Packet>
/*    */ {
/* 20 */   private static final Logger logger = ;
/* 21 */   private static final org.apache.logging.log4j.Marker RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_SENT", NetworkManager.logMarkerPackets);
/*    */   private final EnumPacketDirection direction;
/*    */   
/*    */   public MessageSerializer(EnumPacketDirection direction)
/*    */   {
/* 26 */     this.direction = direction;
/*    */   }
/*    */   
/*    */   protected void encode(ChannelHandlerContext p_encode_1_, Packet p_encode_2_, ByteBuf p_encode_3_) throws IOException, Exception
/*    */   {
/* 31 */     Integer integer = ((EnumConnectionState)p_encode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get()).getPacketId(this.direction, p_encode_2_);
/*    */     
/* 33 */     if (logger.isDebugEnabled())
/*    */     {
/* 35 */       logger.debug(RECEIVED_PACKET_MARKER, "OUT: [{}:{}] {}", new Object[] { p_encode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get(), integer, p_encode_2_.getClass().getName() });
/*    */     }
/*    */     
/* 38 */     if (integer == null)
/*    */     {
/* 40 */       throw new IOException("Can't serialize unregistered packet");
/*    */     }
/*    */     
/*    */ 
/* 44 */     PacketBuffer packetbuffer = new PacketBuffer(p_encode_3_);
/* 45 */     packetbuffer.writeVarIntToBuffer(integer.intValue());
/*    */     
/*    */     try
/*    */     {
/* 49 */       p_encode_2_.writePacketData(packetbuffer);
/*    */     }
/*    */     catch (Throwable throwable)
/*    */     {
/* 53 */       logger.error(throwable);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\MessageSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */