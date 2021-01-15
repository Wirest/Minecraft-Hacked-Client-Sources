/*     */ package io.netty.handler.proxy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.HttpClientCodec;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.handler.codec.http.LastHttpContent;
/*     */ import io.netty.util.CharsetUtil;
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
/*     */ public final class HttpProxyHandler
/*     */   extends ProxyHandler
/*     */ {
/*     */   private static final String PROTOCOL = "http";
/*     */   private static final String AUTH_BASIC = "basic";
/*  44 */   private final HttpClientCodec codec = new HttpClientCodec();
/*     */   private final String username;
/*     */   private final String password;
/*     */   private final CharSequence authorization;
/*     */   private HttpResponseStatus status;
/*     */   
/*     */   public HttpProxyHandler(SocketAddress proxyAddress) {
/*  51 */     super(proxyAddress);
/*  52 */     this.username = null;
/*  53 */     this.password = null;
/*  54 */     this.authorization = null;
/*     */   }
/*     */   
/*     */   public HttpProxyHandler(SocketAddress proxyAddress, String username, String password) {
/*  58 */     super(proxyAddress);
/*  59 */     if (username == null) {
/*  60 */       throw new NullPointerException("username");
/*     */     }
/*  62 */     if (password == null) {
/*  63 */       throw new NullPointerException("password");
/*     */     }
/*  65 */     this.username = username;
/*  66 */     this.password = password;
/*     */     
/*  68 */     ByteBuf authz = Unpooled.copiedBuffer(username + ':' + password, CharsetUtil.UTF_8);
/*  69 */     ByteBuf authzBase64 = Base64.encode(authz, false);
/*     */     
/*  71 */     this.authorization = new AsciiString("Basic " + authzBase64.toString(CharsetUtil.US_ASCII));
/*     */     
/*  73 */     authz.release();
/*  74 */     authzBase64.release();
/*     */   }
/*     */   
/*     */   public String protocol()
/*     */   {
/*  79 */     return "http";
/*     */   }
/*     */   
/*     */   public String authScheme()
/*     */   {
/*  84 */     return this.authorization != null ? "basic" : "none";
/*     */   }
/*     */   
/*     */   public String username() {
/*  88 */     return this.username;
/*     */   }
/*     */   
/*     */   public String password() {
/*  92 */     return this.password;
/*     */   }
/*     */   
/*     */   protected void addCodec(ChannelHandlerContext ctx) throws Exception
/*     */   {
/*  97 */     ChannelPipeline p = ctx.pipeline();
/*  98 */     String name = ctx.name();
/*  99 */     p.addBefore(name, null, this.codec);
/*     */   }
/*     */   
/*     */   protected void removeEncoder(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 104 */     ctx.pipeline().remove(this.codec.encoder());
/*     */   }
/*     */   
/*     */   protected void removeDecoder(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 109 */     ctx.pipeline().remove(this.codec.decoder());
/*     */   }
/*     */   
/*     */   protected Object newInitialMessage(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 114 */     InetSocketAddress raddr = (InetSocketAddress)destinationAddress();
/*     */     String rhost;
/* 116 */     String rhost; if (raddr.isUnresolved()) {
/* 117 */       rhost = raddr.getHostString();
/*     */     } else {
/* 119 */       rhost = raddr.getAddress().getHostAddress();
/*     */     }
/*     */     
/* 122 */     FullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.CONNECT, rhost + ':' + raddr.getPort(), Unpooled.EMPTY_BUFFER, false);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 127 */     SocketAddress proxyAddress = proxyAddress();
/* 128 */     if ((proxyAddress instanceof InetSocketAddress)) {
/* 129 */       InetSocketAddress hostAddr = (InetSocketAddress)proxyAddress;
/* 130 */       req.headers().set(HttpHeaderNames.HOST, hostAddr.getHostString() + ':' + hostAddr.getPort());
/*     */     }
/*     */     
/* 133 */     if (this.authorization != null) {
/* 134 */       req.headers().set(HttpHeaderNames.PROXY_AUTHORIZATION, this.authorization);
/*     */     }
/*     */     
/* 137 */     return req;
/*     */   }
/*     */   
/*     */   protected boolean handleResponse(ChannelHandlerContext ctx, Object response) throws Exception
/*     */   {
/* 142 */     if ((response instanceof HttpResponse)) {
/* 143 */       if (this.status != null) {
/* 144 */         throw new ProxyConnectException(exceptionMessage("too many responses"));
/*     */       }
/* 146 */       this.status = ((HttpResponse)response).status();
/*     */     }
/*     */     
/* 149 */     boolean finished = response instanceof LastHttpContent;
/* 150 */     if (finished) {
/* 151 */       if (this.status == null) {
/* 152 */         throw new ProxyConnectException(exceptionMessage("missing response"));
/*     */       }
/* 154 */       if (this.status.code() != 200) {
/* 155 */         throw new ProxyConnectException(exceptionMessage("status: " + this.status));
/*     */       }
/*     */     }
/*     */     
/* 159 */     return finished;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\proxy\HttpProxyHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */