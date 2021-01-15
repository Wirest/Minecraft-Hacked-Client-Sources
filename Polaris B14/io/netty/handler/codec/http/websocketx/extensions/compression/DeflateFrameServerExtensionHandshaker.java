/*    */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*    */ 
/*    */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
/*    */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
/*    */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
/*    */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtension;
/*    */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandshaker;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ public final class DeflateFrameServerExtensionHandshaker
/*    */   implements WebSocketServerExtensionHandshaker
/*    */ {
/*    */   static final String X_WEBKIT_DEFLATE_FRAME_EXTENSION = "x-webkit-deflate-frame";
/*    */   static final String DEFLATE_FRAME_EXTENSION = "deflate-frame";
/*    */   private final int compressionLevel;
/*    */   
/*    */   public DeflateFrameServerExtensionHandshaker()
/*    */   {
/* 41 */     this(6);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DeflateFrameServerExtensionHandshaker(int compressionLevel)
/*    */   {
/* 51 */     if ((compressionLevel < 0) || (compressionLevel > 9)) {
/* 52 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*    */     }
/*    */     
/* 55 */     this.compressionLevel = compressionLevel;
/*    */   }
/*    */   
/*    */   public WebSocketServerExtension handshakeExtension(WebSocketExtensionData extensionData)
/*    */   {
/* 60 */     if ((!"x-webkit-deflate-frame".equals(extensionData.name())) && (!"deflate-frame".equals(extensionData.name())))
/*    */     {
/* 62 */       return null;
/*    */     }
/*    */     
/* 65 */     if (extensionData.parameters().isEmpty()) {
/* 66 */       return new DeflateFrameServerExtension(this.compressionLevel, extensionData.name());
/*    */     }
/* 68 */     return null;
/*    */   }
/*    */   
/*    */   private static class DeflateFrameServerExtension implements WebSocketServerExtension
/*    */   {
/*    */     private final String extensionName;
/*    */     private final int compressionLevel;
/*    */     
/*    */     public DeflateFrameServerExtension(int compressionLevel, String extensionName)
/*    */     {
/* 78 */       this.extensionName = extensionName;
/* 79 */       this.compressionLevel = compressionLevel;
/*    */     }
/*    */     
/*    */     public int rsv()
/*    */     {
/* 84 */       return 4;
/*    */     }
/*    */     
/*    */     public WebSocketExtensionEncoder newExtensionEncoder()
/*    */     {
/* 89 */       return new PerFrameDeflateEncoder(this.compressionLevel, 15, false);
/*    */     }
/*    */     
/*    */     public WebSocketExtensionDecoder newExtensionDecoder()
/*    */     {
/* 94 */       return new PerFrameDeflateDecoder(false);
/*    */     }
/*    */     
/*    */     public WebSocketExtensionData newReponseData()
/*    */     {
/* 99 */       return new WebSocketExtensionData(this.extensionName, Collections.emptyMap());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\DeflateFrameServerExtensionHandshaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */