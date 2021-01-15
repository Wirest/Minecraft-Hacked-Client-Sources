/*     */ package io.netty.handler.proxy;
/*     */ 
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.socksx.v4.DefaultSocks4CommandRequest;
/*     */ import io.netty.handler.codec.socksx.v4.Socks4ClientDecoder;
/*     */ import io.netty.handler.codec.socksx.v4.Socks4ClientEncoder;
/*     */ import io.netty.handler.codec.socksx.v4.Socks4CommandResponse;
/*     */ import io.netty.handler.codec.socksx.v4.Socks4CommandStatus;
/*     */ import io.netty.handler.codec.socksx.v4.Socks4CommandType;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
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
/*     */ public final class Socks4ProxyHandler
/*     */   extends ProxyHandler
/*     */ {
/*     */   private static final String PROTOCOL = "socks4";
/*     */   private static final String AUTH_USERNAME = "username";
/*     */   private final String username;
/*     */   private String decoderName;
/*     */   private String encoderName;
/*     */   
/*     */   public Socks4ProxyHandler(SocketAddress proxyAddress)
/*     */   {
/*  42 */     this(proxyAddress, null);
/*     */   }
/*     */   
/*     */   public Socks4ProxyHandler(SocketAddress proxyAddress, String username) {
/*  46 */     super(proxyAddress);
/*  47 */     if ((username != null) && (username.length() == 0)) {
/*  48 */       username = null;
/*     */     }
/*  50 */     this.username = username;
/*     */   }
/*     */   
/*     */   public String protocol()
/*     */   {
/*  55 */     return "socks4";
/*     */   }
/*     */   
/*     */   public String authScheme()
/*     */   {
/*  60 */     return this.username != null ? "username" : "none";
/*     */   }
/*     */   
/*     */   public String username() {
/*  64 */     return this.username;
/*     */   }
/*     */   
/*     */   protected void addCodec(ChannelHandlerContext ctx) throws Exception
/*     */   {
/*  69 */     ChannelPipeline p = ctx.pipeline();
/*  70 */     String name = ctx.name();
/*     */     
/*  72 */     Socks4ClientDecoder decoder = new Socks4ClientDecoder();
/*  73 */     p.addBefore(name, null, decoder);
/*     */     
/*  75 */     this.decoderName = p.context(decoder).name();
/*  76 */     this.encoderName = (this.decoderName + ".encoder");
/*     */     
/*  78 */     p.addBefore(name, this.encoderName, Socks4ClientEncoder.INSTANCE);
/*     */   }
/*     */   
/*     */   protected void removeEncoder(ChannelHandlerContext ctx) throws Exception
/*     */   {
/*  83 */     ChannelPipeline p = ctx.pipeline();
/*  84 */     p.remove(this.encoderName);
/*     */   }
/*     */   
/*     */   protected void removeDecoder(ChannelHandlerContext ctx) throws Exception
/*     */   {
/*  89 */     ChannelPipeline p = ctx.pipeline();
/*  90 */     p.remove(this.decoderName);
/*     */   }
/*     */   
/*     */   protected Object newInitialMessage(ChannelHandlerContext ctx) throws Exception
/*     */   {
/*  95 */     InetSocketAddress raddr = (InetSocketAddress)destinationAddress();
/*     */     String rhost;
/*  97 */     String rhost; if (raddr.isUnresolved()) {
/*  98 */       rhost = raddr.getHostString();
/*     */     } else {
/* 100 */       rhost = raddr.getAddress().getHostAddress();
/*     */     }
/* 102 */     return new DefaultSocks4CommandRequest(Socks4CommandType.CONNECT, rhost, raddr.getPort(), this.username != null ? this.username : "");
/*     */   }
/*     */   
/*     */   protected boolean handleResponse(ChannelHandlerContext ctx, Object response)
/*     */     throws Exception
/*     */   {
/* 108 */     Socks4CommandResponse res = (Socks4CommandResponse)response;
/* 109 */     Socks4CommandStatus status = res.status();
/* 110 */     if (status == Socks4CommandStatus.SUCCESS) {
/* 111 */       return true;
/*     */     }
/*     */     
/* 114 */     throw new ProxyConnectException(exceptionMessage("status: " + status));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\proxy\Socks4ProxyHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */