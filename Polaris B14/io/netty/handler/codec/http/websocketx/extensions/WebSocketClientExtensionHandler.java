/*     */ package io.netty.handler.codec.http.websocketx.extensions;
/*     */ 
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.CodecException;
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
/*     */ public class WebSocketClientExtensionHandler
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*     */   private final List<WebSocketClientExtensionHandshaker> extensionHandshakers;
/*     */   
/*     */   public WebSocketClientExtensionHandler(WebSocketClientExtensionHandshaker... extensionHandshakers)
/*     */   {
/*  53 */     if (extensionHandshakers == null) {
/*  54 */       throw new NullPointerException("extensionHandshakers");
/*     */     }
/*  56 */     if (extensionHandshakers.length == 0) {
/*  57 */       throw new IllegalArgumentException("extensionHandshakers must contains at least one handshaker");
/*     */     }
/*  59 */     this.extensionHandshakers = Arrays.asList(extensionHandshakers);
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/*  64 */     if (((msg instanceof HttpRequest)) && (WebSocketExtensionUtil.isWebsocketUpgrade((HttpRequest)msg))) {
/*  65 */       HttpRequest request = (HttpRequest)msg;
/*  66 */       String headerValue = (String)request.headers().getAndConvert(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
/*     */       
/*  68 */       for (WebSocketClientExtensionHandshaker extentionHandshaker : this.extensionHandshakers) {
/*  69 */         WebSocketExtensionData extensionData = extentionHandshaker.newRequestData();
/*  70 */         headerValue = WebSocketExtensionUtil.appendExtension(headerValue, extensionData.name(), extensionData.parameters());
/*     */       }
/*     */       
/*     */ 
/*  74 */       request.headers().set(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS, headerValue);
/*     */     }
/*     */     
/*  77 */     super.write(ctx, msg, promise);
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg)
/*     */     throws Exception
/*     */   {
/*  83 */     if ((msg instanceof HttpResponse)) {
/*  84 */       HttpResponse response = (HttpResponse)msg;
/*     */       
/*  86 */       if (WebSocketExtensionUtil.isWebsocketUpgrade(response)) {
/*  87 */         String extensionsHeader = (String)response.headers().getAndConvert(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
/*     */         
/*  89 */         if (extensionsHeader != null) {
/*  90 */           List<WebSocketExtensionData> extensions = WebSocketExtensionUtil.extractExtensions(extensionsHeader);
/*     */           
/*  92 */           List<WebSocketClientExtension> validExtensions = new ArrayList(extensions.size());
/*     */           
/*  94 */           int rsv = 0;
/*     */           
/*  96 */           for (WebSocketExtensionData extensionData : extensions) {
/*  97 */             Iterator<WebSocketClientExtensionHandshaker> extensionHandshakersIterator = this.extensionHandshakers.iterator();
/*     */             
/*  99 */             WebSocketClientExtension validExtension = null;
/*     */             
/* 101 */             while ((validExtension == null) && (extensionHandshakersIterator.hasNext())) {
/* 102 */               WebSocketClientExtensionHandshaker extensionHandshaker = (WebSocketClientExtensionHandshaker)extensionHandshakersIterator.next();
/*     */               
/* 104 */               validExtension = extensionHandshaker.handshakeExtension(extensionData);
/*     */             }
/*     */             
/* 107 */             if ((validExtension != null) && ((validExtension.rsv() & rsv) == 0)) {
/* 108 */               rsv |= validExtension.rsv();
/* 109 */               validExtensions.add(validExtension);
/*     */             } else {
/* 111 */               throw new CodecException("invalid WebSocket Extension handhshake for \"" + extensionsHeader + "\"");
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 116 */           for (WebSocketClientExtension validExtension : validExtensions) {
/* 117 */             WebSocketExtensionDecoder decoder = validExtension.newExtensionDecoder();
/* 118 */             WebSocketExtensionEncoder encoder = validExtension.newExtensionEncoder();
/* 119 */             ctx.pipeline().addAfter(ctx.name(), decoder.getClass().getName(), decoder);
/* 120 */             ctx.pipeline().addAfter(ctx.name(), encoder.getClass().getName(), encoder);
/*     */           }
/*     */         }
/*     */         
/* 124 */         ctx.pipeline().remove(ctx.name());
/*     */       }
/*     */     }
/*     */     
/* 128 */     super.channelRead(ctx, msg);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\WebSocketClientExtensionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */