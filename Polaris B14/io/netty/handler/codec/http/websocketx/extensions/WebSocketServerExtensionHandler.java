/*     */ package io.netty.handler.codec.http.websocketx.extensions;
/*     */ 
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class WebSocketServerExtensionHandler
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*     */   private final List<WebSocketServerExtensionHandshaker> extensionHandshakers;
/*     */   private List<WebSocketServerExtension> validExtensions;
/*     */   
/*     */   public WebSocketServerExtensionHandler(WebSocketServerExtensionHandshaker... extensionHandshakers)
/*     */   {
/*  56 */     if (extensionHandshakers == null) {
/*  57 */       throw new NullPointerException("extensionHandshakers");
/*     */     }
/*  59 */     if (extensionHandshakers.length == 0) {
/*  60 */       throw new IllegalArgumentException("extensionHandshakers must contains at least one handshaker");
/*     */     }
/*  62 */     this.extensionHandshakers = Arrays.asList(extensionHandshakers);
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/*     */     int rsv;
/*  68 */     if ((msg instanceof HttpRequest)) {
/*  69 */       HttpRequest request = (HttpRequest)msg;
/*     */       
/*  71 */       if (WebSocketExtensionUtil.isWebsocketUpgrade(request)) {
/*  72 */         String extensionsHeader = (String)request.headers().getAndConvert(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
/*     */         
/*  74 */         if (extensionsHeader != null) {
/*  75 */           List<WebSocketExtensionData> extensions = WebSocketExtensionUtil.extractExtensions(extensionsHeader);
/*     */           
/*  77 */           rsv = 0;
/*     */           
/*  79 */           for (WebSocketExtensionData extensionData : extensions) {
/*  80 */             Iterator<WebSocketServerExtensionHandshaker> extensionHandshakersIterator = this.extensionHandshakers.iterator();
/*     */             
/*  82 */             WebSocketServerExtension validExtension = null;
/*     */             
/*  84 */             while ((validExtension == null) && (extensionHandshakersIterator.hasNext())) {
/*  85 */               WebSocketServerExtensionHandshaker extensionHandshaker = (WebSocketServerExtensionHandshaker)extensionHandshakersIterator.next();
/*     */               
/*  87 */               validExtension = extensionHandshaker.handshakeExtension(extensionData);
/*     */             }
/*     */             
/*  90 */             if ((validExtension != null) && ((validExtension.rsv() & rsv) == 0)) {
/*  91 */               if (this.validExtensions == null) {
/*  92 */                 this.validExtensions = new ArrayList(1);
/*     */               }
/*  94 */               rsv |= validExtension.rsv();
/*  95 */               this.validExtensions.add(validExtension);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 102 */     super.channelRead(ctx, msg);
/*     */   }
/*     */   
/*     */   public void write(final ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 107 */     if (((msg instanceof HttpResponse)) && (WebSocketExtensionUtil.isWebsocketUpgrade((HttpResponse)msg)) && (this.validExtensions != null))
/*     */     {
/* 109 */       HttpResponse response = (HttpResponse)msg;
/* 110 */       String headerValue = (String)response.headers().getAndConvert(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
/*     */       
/* 112 */       for (WebSocketServerExtension extension : this.validExtensions) {
/* 113 */         WebSocketExtensionData extensionData = extension.newReponseData();
/* 114 */         headerValue = WebSocketExtensionUtil.appendExtension(headerValue, extensionData.name(), extensionData.parameters());
/*     */       }
/*     */       
/*     */ 
/* 118 */       promise.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture future) throws Exception {
/* 121 */           if (future.isSuccess()) {
/* 122 */             for (WebSocketServerExtension extension : WebSocketServerExtensionHandler.this.validExtensions) {
/* 123 */               WebSocketExtensionDecoder decoder = extension.newExtensionDecoder();
/* 124 */               WebSocketExtensionEncoder encoder = extension.newExtensionEncoder();
/* 125 */               ctx.pipeline().addAfter(ctx.name(), decoder.getClass().getName(), decoder);
/* 126 */               ctx.pipeline().addAfter(ctx.name(), encoder.getClass().getName(), encoder);
/*     */             }
/*     */           }
/*     */           
/* 130 */           ctx.pipeline().remove(ctx.name());
/*     */         }
/*     */       });
/*     */       
/* 134 */       if (headerValue != null) {
/* 135 */         response.headers().set(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS, headerValue);
/*     */       }
/*     */     }
/*     */     
/* 139 */     super.write(ctx, msg, promise);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\WebSocketServerExtensionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */