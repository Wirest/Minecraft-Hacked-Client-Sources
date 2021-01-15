/*    */ package io.netty.handler.codec.socksx.v4;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import io.netty.util.NetUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ChannelHandler.Sharable
/*    */ public final class Socks4ServerEncoder
/*    */   extends MessageToByteEncoder<Socks4CommandResponse>
/*    */ {
/* 31 */   public static final Socks4ServerEncoder INSTANCE = new Socks4ServerEncoder();
/*    */   
/* 33 */   private static final byte[] IPv4_HOSTNAME_ZEROED = { 0, 0, 0, 0 };
/*    */   
/*    */ 
/*    */   protected void encode(ChannelHandlerContext ctx, Socks4CommandResponse msg, ByteBuf out)
/*    */     throws Exception
/*    */   {
/* 39 */     out.writeByte(0);
/* 40 */     out.writeByte(msg.status().byteValue());
/* 41 */     out.writeShort(msg.dstPort());
/* 42 */     out.writeBytes(msg.dstAddr() == null ? IPv4_HOSTNAME_ZEROED : NetUtil.createByteArrayFromIpAddressString(msg.dstAddr()));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v4\Socks4ServerEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */