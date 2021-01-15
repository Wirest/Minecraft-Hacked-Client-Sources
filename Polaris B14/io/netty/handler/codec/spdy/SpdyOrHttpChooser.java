/*     */ package io.netty.handler.codec.spdy;
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
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLSession;
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
/*     */ public abstract class SpdyOrHttpChooser
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private final int maxSpdyContentLength;
/*     */   private final int maxHttpContentLength;
/*     */   
/*     */   public static enum SelectedProtocol
/*     */   {
/*  41 */     SPDY_3_1("spdy/3.1"), 
/*  42 */     HTTP_1_1("http/1.1"), 
/*  43 */     HTTP_1_0("http/1.0"), 
/*  44 */     UNKNOWN("Unknown");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private SelectedProtocol(String defaultName) {
/*  49 */       this.name = defaultName;
/*     */     }
/*     */     
/*     */     public String protocolName() {
/*  53 */       return this.name;
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
/*  64 */       for (SelectedProtocol protocol : ) {
/*  65 */         if (protocol.protocolName().equals(name)) {
/*  66 */           return protocol;
/*     */         }
/*     */       }
/*  69 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected SpdyOrHttpChooser(int maxSpdyContentLength, int maxHttpContentLength)
/*     */   {
/*  77 */     this.maxSpdyContentLength = maxSpdyContentLength;
/*  78 */     this.maxHttpContentLength = maxHttpContentLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SelectedProtocol getProtocol(SSLEngine engine)
/*     */   {
/*  87 */     String[] protocol = StringUtil.split(engine.getSession().getProtocol(), ':');
/*  88 */     if (protocol.length < 2)
/*     */     {
/*  90 */       return SelectedProtocol.HTTP_1_1;
/*     */     }
/*  92 */     SelectedProtocol selectedProtocol = SelectedProtocol.protocol(protocol[1]);
/*  93 */     return selectedProtocol;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*  98 */     if (initPipeline(ctx))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 103 */       ctx.pipeline().remove(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean initPipeline(ChannelHandlerContext ctx)
/*     */   {
/* 110 */     SslHandler handler = (SslHandler)ctx.pipeline().get(SslHandler.class);
/* 111 */     if (handler == null)
/*     */     {
/* 113 */       throw new IllegalStateException("SslHandler is needed for SPDY");
/*     */     }
/*     */     
/* 116 */     SelectedProtocol protocol = getProtocol(handler.engine());
/* 117 */     switch (protocol)
/*     */     {
/*     */     case UNKNOWN: 
/* 120 */       return false;
/*     */     case SPDY_3_1: 
/* 122 */       addSpdyHandlers(ctx, SpdyVersion.SPDY_3_1);
/* 123 */       break;
/*     */     case HTTP_1_0: 
/*     */     case HTTP_1_1: 
/* 126 */       addHttpHandlers(ctx);
/* 127 */       break;
/*     */     default: 
/* 129 */       throw new IllegalStateException("Unknown SelectedProtocol");
/*     */     }
/* 131 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void addSpdyHandlers(ChannelHandlerContext ctx, SpdyVersion version)
/*     */   {
/* 138 */     ChannelPipeline pipeline = ctx.pipeline();
/* 139 */     pipeline.addLast("spdyFrameCodec", new SpdyFrameCodec(version));
/* 140 */     pipeline.addLast("spdySessionHandler", new SpdySessionHandler(version, true));
/* 141 */     pipeline.addLast("spdyHttpEncoder", new SpdyHttpEncoder(version));
/* 142 */     pipeline.addLast("spdyHttpDecoder", new SpdyHttpDecoder(version, this.maxSpdyContentLength));
/* 143 */     pipeline.addLast("spdyStreamIdHandler", new SpdyHttpResponseStreamIdHandler());
/* 144 */     pipeline.addLast("httpRequestHandler", createHttpRequestHandlerForSpdy());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void addHttpHandlers(ChannelHandlerContext ctx)
/*     */   {
/* 151 */     ChannelPipeline pipeline = ctx.pipeline();
/* 152 */     pipeline.addLast("httpRequestDecoder", new HttpRequestDecoder());
/* 153 */     pipeline.addLast("httpResponseEncoder", new HttpResponseEncoder());
/* 154 */     pipeline.addLast("httpChunkAggregator", new HttpObjectAggregator(this.maxHttpContentLength));
/* 155 */     pipeline.addLast("httpRequestHandler", createHttpRequestHandlerForHttp());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract ChannelHandler createHttpRequestHandlerForHttp();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ChannelHandler createHttpRequestHandlerForSpdy()
/*     */   {
/* 172 */     return createHttpRequestHandlerForHttp();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyOrHttpChooser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */