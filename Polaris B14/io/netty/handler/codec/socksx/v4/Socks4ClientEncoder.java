/*    */ package io.netty.handler.codec.socksx.v4;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufUtil;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import io.netty.handler.codec.socksx.SocksVersion;
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
/*    */ 
/*    */ 
/*    */ @ChannelHandler.Sharable
/*    */ public final class Socks4ClientEncoder
/*    */   extends MessageToByteEncoder<Socks4CommandRequest>
/*    */ {
/* 35 */   public static final Socks4ClientEncoder INSTANCE = new Socks4ClientEncoder();
/*    */   
/* 37 */   private static final byte[] IPv4_DOMAIN_MARKER = { 0, 0, 0, 1 };
/*    */   
/*    */ 
/*    */   protected void encode(ChannelHandlerContext ctx, Socks4CommandRequest msg, ByteBuf out)
/*    */     throws Exception
/*    */   {
/* 43 */     out.writeByte(msg.version().byteValue());
/* 44 */     out.writeByte(msg.type().byteValue());
/* 45 */     out.writeShort(msg.dstPort());
/* 46 */     if (NetUtil.isValidIpV4Address(msg.dstAddr())) {
/* 47 */       out.writeBytes(NetUtil.createByteArrayFromIpAddressString(msg.dstAddr()));
/* 48 */       ByteBufUtil.writeAscii(out, msg.userId());
/* 49 */       out.writeByte(0);
/*    */     } else {
/* 51 */       out.writeBytes(IPv4_DOMAIN_MARKER);
/* 52 */       ByteBufUtil.writeAscii(out, msg.userId());
/* 53 */       out.writeByte(0);
/* 54 */       ByteBufUtil.writeAscii(out, msg.dstAddr());
/* 55 */       out.writeByte(0);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v4\Socks4ClientEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */