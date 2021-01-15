/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.http.HttpObjectAggregator;
/*     */ import io.netty.handler.codec.http.HttpRequestDecoder;
/*     */ import io.netty.handler.codec.http.HttpResponseEncoder;
/*     */ import io.netty.handler.ssl.SslHandler;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLEngine;
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
/*     */ 
/*     */ 
/*     */ public abstract class Http2OrHttpChooser
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private final int maxHttpContentLength;
/*     */   
/*     */   public static enum SelectedProtocol
/*     */   {
/*  42 */     HTTP_2("h2-16"), 
/*  43 */     HTTP_1_1("http/1.1"), 
/*  44 */     HTTP_1_0("http/1.0"), 
/*  45 */     UNKNOWN("Unknown");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private SelectedProtocol(String defaultName) {
/*  50 */       this.name = defaultName;
/*     */     }
/*     */     
/*     */     public String protocolName() {
/*  54 */       return this.name;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static SelectedProtocol protocol(String name)
/*     */     {
/*  65 */       for (SelectedProtocol protocol : ) {
/*  66 */         if (protocol.protocolName().equals(name)) {
/*  67 */           return protocol;
/*     */         }
/*     */       }
/*  70 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected Http2OrHttpChooser(int maxHttpContentLength)
/*     */   {
/*  77 */     this.maxHttpContentLength = maxHttpContentLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract SelectedProtocol getProtocol(SSLEngine paramSSLEngine);
/*     */   
/*     */ 
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/*  88 */     if (initPipeline(ctx))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  93 */       ctx.pipeline().remove(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean initPipeline(ChannelHandlerContext ctx)
/*     */   {
/* 100 */     SslHandler handler = (SslHandler)ctx.pipeline().get(SslHandler.class);
/* 101 */     if (handler == null)
/*     */     {
/* 103 */       throw new IllegalStateException("SslHandler is needed for HTTP2");
/*     */     }
/*     */     
/* 106 */     SelectedProtocol protocol = getProtocol(handler.engine());
/* 107 */     switch (protocol)
/*     */     {
/*     */     case UNKNOWN: 
/* 110 */       return false;
/*     */     case HTTP_2: 
/* 112 */       addHttp2Handlers(ctx);
/* 113 */       break;
/*     */     case HTTP_1_0: 
/*     */     case HTTP_1_1: 
/* 116 */       addHttpHandlers(ctx);
/* 117 */       break;
/*     */     default: 
/* 119 */       throw new IllegalStateException("Unknown SelectedProtocol");
/*     */     }
/* 121 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void addHttp2Handlers(ChannelHandlerContext ctx)
/*     */   {
/* 128 */     ChannelPipeline pipeline = ctx.pipeline();
/* 129 */     pipeline.addLast("http2ConnectionHandler", createHttp2RequestHandler());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void addHttpHandlers(ChannelHandlerContext ctx)
/*     */   {
/* 136 */     ChannelPipeline pipeline = ctx.pipeline();
/* 137 */     pipeline.addLast("httpRequestDecoder", new HttpRequestDecoder());
/* 138 */     pipeline.addLast("httpResponseEncoder", new HttpResponseEncoder());
/* 139 */     pipeline.addLast("httpChunkAggregator", new HttpObjectAggregator(this.maxHttpContentLength));
/* 140 */     pipeline.addLast("httpRequestHandler", createHttp1RequestHandler());
/*     */   }
/*     */   
/*     */   protected abstract ChannelHandler createHttp1RequestHandler();
/*     */   
/*     */   protected abstract Http2ConnectionHandler createHttp2RequestHandler();
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2OrHttpChooser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */