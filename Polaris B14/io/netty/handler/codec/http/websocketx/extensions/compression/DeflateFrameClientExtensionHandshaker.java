/*     */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*     */ 
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtension;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandshaker;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
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
/*     */ public final class DeflateFrameClientExtensionHandshaker
/*     */   implements WebSocketClientExtensionHandshaker
/*     */ {
/*     */   private final int compressionLevel;
/*     */   private final boolean useWebkitExtensionName;
/*     */   
/*     */   public DeflateFrameClientExtensionHandshaker(boolean useWebkitExtensionName)
/*     */   {
/*  41 */     this(6, useWebkitExtensionName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeflateFrameClientExtensionHandshaker(int compressionLevel, boolean useWebkitExtensionName)
/*     */   {
/*  51 */     if ((compressionLevel < 0) || (compressionLevel > 9)) {
/*  52 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*     */     }
/*     */     
/*  55 */     this.compressionLevel = compressionLevel;
/*  56 */     this.useWebkitExtensionName = useWebkitExtensionName;
/*     */   }
/*     */   
/*     */   public WebSocketExtensionData newRequestData()
/*     */   {
/*  61 */     return new WebSocketExtensionData(this.useWebkitExtensionName ? "x-webkit-deflate-frame" : "deflate-frame", Collections.emptyMap());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public WebSocketClientExtension handshakeExtension(WebSocketExtensionData extensionData)
/*     */   {
/*  68 */     if ((!"x-webkit-deflate-frame".equals(extensionData.name())) && (!"deflate-frame".equals(extensionData.name())))
/*     */     {
/*  70 */       return null;
/*     */     }
/*     */     
/*  73 */     if (extensionData.parameters().isEmpty()) {
/*  74 */       return new DeflateFrameClientExtension(this.compressionLevel);
/*     */     }
/*  76 */     return null;
/*     */   }
/*     */   
/*     */   private static class DeflateFrameClientExtension implements WebSocketClientExtension
/*     */   {
/*     */     private final int compressionLevel;
/*     */     
/*     */     public DeflateFrameClientExtension(int compressionLevel)
/*     */     {
/*  85 */       this.compressionLevel = compressionLevel;
/*     */     }
/*     */     
/*     */     public int rsv()
/*     */     {
/*  90 */       return 4;
/*     */     }
/*     */     
/*     */     public WebSocketExtensionEncoder newExtensionEncoder()
/*     */     {
/*  95 */       return new PerFrameDeflateEncoder(this.compressionLevel, 15, false);
/*     */     }
/*     */     
/*     */     public WebSocketExtensionDecoder newExtensionDecoder()
/*     */     {
/* 100 */       return new PerFrameDeflateDecoder(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\DeflateFrameClientExtensionHandshaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */