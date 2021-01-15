/*    */ package io.netty.handler.codec.socksx.v5;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.EncoderException;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import io.netty.handler.codec.socksx.SocksVersion;
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public class Socks5ServerEncoder
/*    */   extends MessageToByteEncoder<Socks5Message>
/*    */ {
/* 32 */   public static final Socks5ServerEncoder DEFAULT = new Socks5ServerEncoder(Socks5AddressEncoder.DEFAULT);
/*    */   
/*    */ 
/*    */   private final Socks5AddressEncoder addressEncoder;
/*    */   
/*    */ 
/*    */   protected Socks5ServerEncoder()
/*    */   {
/* 40 */     this(Socks5AddressEncoder.DEFAULT);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Socks5ServerEncoder(Socks5AddressEncoder addressEncoder)
/*    */   {
/* 47 */     if (addressEncoder == null) {
/* 48 */       throw new NullPointerException("addressEncoder");
/*    */     }
/*    */     
/* 51 */     this.addressEncoder = addressEncoder;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected final Socks5AddressEncoder addressEncoder()
/*    */   {
/* 58 */     return this.addressEncoder;
/*    */   }
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, Socks5Message msg, ByteBuf out) throws Exception
/*    */   {
/* 63 */     if ((msg instanceof Socks5InitialResponse)) {
/* 64 */       encodeAuthMethodResponse((Socks5InitialResponse)msg, out);
/* 65 */     } else if ((msg instanceof Socks5PasswordAuthResponse)) {
/* 66 */       encodePasswordAuthResponse((Socks5PasswordAuthResponse)msg, out);
/* 67 */     } else if ((msg instanceof Socks5CommandResponse)) {
/* 68 */       encodeCommandResponse((Socks5CommandResponse)msg, out);
/*    */     } else {
/* 70 */       throw new EncoderException("unsupported message type: " + StringUtil.simpleClassName(msg));
/*    */     }
/*    */   }
/*    */   
/*    */   private static void encodeAuthMethodResponse(Socks5InitialResponse msg, ByteBuf out) {
/* 75 */     out.writeByte(msg.version().byteValue());
/* 76 */     out.writeByte(msg.authMethod().byteValue());
/*    */   }
/*    */   
/*    */   private static void encodePasswordAuthResponse(Socks5PasswordAuthResponse msg, ByteBuf out) {
/* 80 */     out.writeByte(1);
/* 81 */     out.writeByte(msg.status().byteValue());
/*    */   }
/*    */   
/*    */   private void encodeCommandResponse(Socks5CommandResponse msg, ByteBuf out) throws Exception {
/* 85 */     out.writeByte(msg.version().byteValue());
/* 86 */     out.writeByte(msg.status().byteValue());
/* 87 */     out.writeByte(0);
/*    */     
/* 89 */     Socks5AddressType bndAddrType = msg.bndAddrType();
/* 90 */     out.writeByte(bndAddrType.byteValue());
/* 91 */     this.addressEncoder.encodeAddress(bndAddrType, msg.bndAddr(), out);
/*    */     
/* 93 */     out.writeShort(msg.bndPort());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5ServerEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */