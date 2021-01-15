/*     */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*     */ 
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtension;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandshaker;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ public final class PerMessageDeflateServerExtensionHandshaker
/*     */   implements WebSocketServerExtensionHandshaker
/*     */ {
/*     */   public static final int MIN_WINDOW_SIZE = 8;
/*     */   public static final int MAX_WINDOW_SIZE = 15;
/*     */   static final String PERMESSAGE_DEFLATE_EXTENSION = "permessage-deflate";
/*     */   static final String CLIENT_MAX_WINDOW = "client_max_window_bits";
/*     */   static final String SERVER_MAX_WINDOW = "server_max_window_bits";
/*     */   static final String CLIENT_NO_CONTEXT = "client_no_context_takeover";
/*     */   static final String SERVER_NO_CONTEXT = "server_no_context_takeover";
/*     */   private final int compressionLevel;
/*     */   private final boolean allowServerWindowSize;
/*     */   private final int preferredClientWindowSize;
/*     */   private final boolean allowServerNoContext;
/*     */   private final boolean preferredClientNoContext;
/*     */   
/*     */   public PerMessageDeflateServerExtensionHandshaker()
/*     */   {
/*  53 */     this(6, false, 15, false, false);
/*     */   }
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
/*     */   public PerMessageDeflateServerExtensionHandshaker(int compressionLevel, boolean allowServerWindowSize, int preferredClientWindowSize, boolean allowServerNoContext, boolean preferredClientNoContext)
/*     */   {
/*  76 */     if ((preferredClientWindowSize > 15) || (preferredClientWindowSize < 8)) {
/*  77 */       throw new IllegalArgumentException("preferredServerWindowSize: " + preferredClientWindowSize + " (expected: 8-15)");
/*     */     }
/*     */     
/*  80 */     if ((compressionLevel < 0) || (compressionLevel > 9)) {
/*  81 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*     */     }
/*     */     
/*  84 */     this.compressionLevel = compressionLevel;
/*  85 */     this.allowServerWindowSize = allowServerWindowSize;
/*  86 */     this.preferredClientWindowSize = preferredClientWindowSize;
/*  87 */     this.allowServerNoContext = allowServerNoContext;
/*  88 */     this.preferredClientNoContext = preferredClientNoContext;
/*     */   }
/*     */   
/*     */   public WebSocketServerExtension handshakeExtension(WebSocketExtensionData extensionData)
/*     */   {
/*  93 */     if (!"permessage-deflate".equals(extensionData.name())) {
/*  94 */       return null;
/*     */     }
/*     */     
/*  97 */     boolean deflateEnabled = true;
/*  98 */     int clientWindowSize = 15;
/*  99 */     int serverWindowSize = 15;
/* 100 */     boolean serverNoContext = false;
/* 101 */     boolean clientNoContext = false;
/*     */     
/* 103 */     Iterator<Map.Entry<String, String>> parametersIterator = extensionData.parameters().entrySet().iterator();
/*     */     
/* 105 */     while ((deflateEnabled) && (parametersIterator.hasNext())) {
/* 106 */       Map.Entry<String, String> parameter = (Map.Entry)parametersIterator.next();
/*     */       
/* 108 */       if ("client_max_window_bits".equalsIgnoreCase((String)parameter.getKey()))
/*     */       {
/* 110 */         clientWindowSize = this.preferredClientWindowSize;
/* 111 */       } else if ("server_max_window_bits".equalsIgnoreCase((String)parameter.getKey()))
/*     */       {
/* 113 */         if (this.allowServerWindowSize) {
/* 114 */           serverWindowSize = Integer.parseInt((String)parameter.getValue());
/* 115 */           if ((serverWindowSize > 15) || (serverWindowSize < 8)) {
/* 116 */             deflateEnabled = false;
/*     */           }
/*     */         } else {
/* 119 */           deflateEnabled = false;
/*     */         }
/* 121 */       } else if ("client_no_context_takeover".equalsIgnoreCase((String)parameter.getKey()))
/*     */       {
/* 123 */         clientNoContext = this.preferredClientNoContext;
/* 124 */       } else if ("server_no_context_takeover".equalsIgnoreCase((String)parameter.getKey()))
/*     */       {
/* 126 */         if (this.allowServerNoContext) {
/* 127 */           serverNoContext = true;
/*     */         } else {
/* 129 */           deflateEnabled = false;
/*     */         }
/*     */       }
/*     */       else {
/* 133 */         deflateEnabled = false;
/*     */       }
/*     */     }
/*     */     
/* 137 */     if (deflateEnabled) {
/* 138 */       return new PermessageDeflateExtension(this.compressionLevel, serverNoContext, serverWindowSize, clientNoContext, clientWindowSize);
/*     */     }
/*     */     
/* 141 */     return null;
/*     */   }
/*     */   
/*     */   private static class PermessageDeflateExtension
/*     */     implements WebSocketServerExtension
/*     */   {
/*     */     private final int compressionLevel;
/*     */     private final boolean serverNoContext;
/*     */     private final int serverWindowSize;
/*     */     private final boolean clientNoContext;
/*     */     private final int clientWindowSize;
/*     */     
/*     */     public PermessageDeflateExtension(int compressionLevel, boolean serverNoContext, int serverWindowSize, boolean clientNoContext, int clientWindowSize)
/*     */     {
/* 155 */       this.compressionLevel = compressionLevel;
/* 156 */       this.serverNoContext = serverNoContext;
/* 157 */       this.serverWindowSize = serverWindowSize;
/* 158 */       this.clientNoContext = clientNoContext;
/* 159 */       this.clientWindowSize = clientWindowSize;
/*     */     }
/*     */     
/*     */     public int rsv()
/*     */     {
/* 164 */       return 4;
/*     */     }
/*     */     
/*     */     public WebSocketExtensionEncoder newExtensionEncoder()
/*     */     {
/* 169 */       return new PerMessageDeflateEncoder(this.compressionLevel, this.clientWindowSize, this.clientNoContext);
/*     */     }
/*     */     
/*     */     public WebSocketExtensionDecoder newExtensionDecoder()
/*     */     {
/* 174 */       return new PerMessageDeflateDecoder(this.serverNoContext);
/*     */     }
/*     */     
/*     */     public WebSocketExtensionData newReponseData()
/*     */     {
/* 179 */       HashMap<String, String> parameters = new HashMap(4);
/* 180 */       if (this.serverNoContext) {
/* 181 */         parameters.put("server_no_context_takeover", null);
/*     */       }
/* 183 */       if (this.clientNoContext) {
/* 184 */         parameters.put("client_no_context_takeover", null);
/*     */       }
/* 186 */       if (this.serverWindowSize != 15) {
/* 187 */         parameters.put("server_max_window_bits", Integer.toString(this.serverWindowSize));
/*     */       }
/* 189 */       if (this.clientWindowSize != 15) {
/* 190 */         parameters.put("client_max_window_bits", Integer.toString(this.clientWindowSize));
/*     */       }
/* 192 */       return new WebSocketExtensionData("permessage-deflate", parameters);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\PerMessageDeflateServerExtensionHandshaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */