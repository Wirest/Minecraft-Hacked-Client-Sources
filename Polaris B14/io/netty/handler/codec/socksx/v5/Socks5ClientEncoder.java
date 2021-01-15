/*     */ package io.netty.handler.codec.socksx.v5;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import io.netty.handler.codec.MessageToByteEncoder;
/*     */ import io.netty.handler.codec.socksx.SocksVersion;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ChannelHandler.Sharable
/*     */ public class Socks5ClientEncoder
/*     */   extends MessageToByteEncoder<Socks5Message>
/*     */ {
/*  36 */   public static final Socks5ClientEncoder DEFAULT = new Socks5ClientEncoder();
/*     */   
/*     */ 
/*     */   private final Socks5AddressEncoder addressEncoder;
/*     */   
/*     */ 
/*     */   protected Socks5ClientEncoder()
/*     */   {
/*  44 */     this(Socks5AddressEncoder.DEFAULT);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Socks5ClientEncoder(Socks5AddressEncoder addressEncoder)
/*     */   {
/*  51 */     if (addressEncoder == null) {
/*  52 */       throw new NullPointerException("addressEncoder");
/*     */     }
/*     */     
/*  55 */     this.addressEncoder = addressEncoder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final Socks5AddressEncoder addressEncoder()
/*     */   {
/*  62 */     return this.addressEncoder;
/*     */   }
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, Socks5Message msg, ByteBuf out) throws Exception
/*     */   {
/*  67 */     if ((msg instanceof Socks5InitialRequest)) {
/*  68 */       encodeAuthMethodRequest((Socks5InitialRequest)msg, out);
/*  69 */     } else if ((msg instanceof Socks5PasswordAuthRequest)) {
/*  70 */       encodePasswordAuthRequest((Socks5PasswordAuthRequest)msg, out);
/*  71 */     } else if ((msg instanceof Socks5CommandRequest)) {
/*  72 */       encodeCommandRequest((Socks5CommandRequest)msg, out);
/*     */     } else {
/*  74 */       throw new EncoderException("unsupported message type: " + StringUtil.simpleClassName(msg));
/*     */     }
/*     */   }
/*     */   
/*     */   private static void encodeAuthMethodRequest(Socks5InitialRequest msg, ByteBuf out) {
/*  79 */     out.writeByte(msg.version().byteValue());
/*     */     
/*  81 */     List<Socks5AuthMethod> authMethods = msg.authMethods();
/*  82 */     int numAuthMethods = authMethods.size();
/*  83 */     out.writeByte(numAuthMethods);
/*     */     
/*  85 */     if ((authMethods instanceof RandomAccess)) {
/*  86 */       for (int i = 0; i < numAuthMethods; i++) {
/*  87 */         out.writeByte(((Socks5AuthMethod)authMethods.get(i)).byteValue());
/*     */       }
/*     */     } else {
/*  90 */       for (Socks5AuthMethod a : authMethods) {
/*  91 */         out.writeByte(a.byteValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void encodePasswordAuthRequest(Socks5PasswordAuthRequest msg, ByteBuf out) {
/*  97 */     out.writeByte(1);
/*     */     
/*  99 */     String username = msg.username();
/* 100 */     out.writeByte(username.length());
/* 101 */     ByteBufUtil.writeAscii(out, username);
/*     */     
/* 103 */     String password = msg.password();
/* 104 */     out.writeByte(password.length());
/* 105 */     ByteBufUtil.writeAscii(out, password);
/*     */   }
/*     */   
/*     */   private void encodeCommandRequest(Socks5CommandRequest msg, ByteBuf out) throws Exception {
/* 109 */     out.writeByte(msg.version().byteValue());
/* 110 */     out.writeByte(msg.type().byteValue());
/* 111 */     out.writeByte(0);
/*     */     
/* 113 */     Socks5AddressType dstAddrType = msg.dstAddrType();
/* 114 */     out.writeByte(dstAddrType.byteValue());
/* 115 */     this.addressEncoder.encodeAddress(dstAddrType, msg.dstAddr(), out);
/* 116 */     out.writeShort(msg.dstPort());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5ClientEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */