/*     */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*     */ 
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtension;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandshaker;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
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
/*     */ 
/*     */ public final class PerMessageDeflateClientExtensionHandshaker
/*     */   implements WebSocketClientExtensionHandshaker
/*     */ {
/*     */   private final int compressionLevel;
/*     */   private final boolean allowClientWindowSize;
/*     */   private final int requestedServerWindowSize;
/*     */   private final boolean allowClientNoContext;
/*     */   private final boolean requestedServerNoContext;
/*     */   
/*     */   public PerMessageDeflateClientExtensionHandshaker()
/*     */   {
/*  47 */     this(6, false, 15, false, false);
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
/*     */   public PerMessageDeflateClientExtensionHandshaker(int compressionLevel, boolean allowClientWindowSize, int requestedServerWindowSize, boolean allowClientNoContext, boolean requestedServerNoContext)
/*     */   {
/*  70 */     if ((requestedServerWindowSize > 15) || (requestedServerWindowSize < 8)) {
/*  71 */       throw new IllegalArgumentException("requestedServerWindowSize: " + requestedServerWindowSize + " (expected: 8-15)");
/*     */     }
/*     */     
/*  74 */     if ((compressionLevel < 0) || (compressionLevel > 9)) {
/*  75 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*     */     }
/*     */     
/*  78 */     this.compressionLevel = compressionLevel;
/*  79 */     this.allowClientWindowSize = allowClientWindowSize;
/*  80 */     this.requestedServerWindowSize = requestedServerWindowSize;
/*  81 */     this.allowClientNoContext = allowClientNoContext;
/*  82 */     this.requestedServerNoContext = requestedServerNoContext;
/*     */   }
/*     */   
/*     */   public WebSocketExtensionData newRequestData()
/*     */   {
/*  87 */     HashMap<String, String> parameters = new HashMap(4);
/*  88 */     if (this.requestedServerWindowSize != 15) {
/*  89 */       parameters.put("server_no_context_takeover", null);
/*     */     }
/*  91 */     if (this.allowClientNoContext) {
/*  92 */       parameters.put("client_no_context_takeover", null);
/*     */     }
/*  94 */     if (this.requestedServerWindowSize != 15) {
/*  95 */       parameters.put("server_max_window_bits", Integer.toString(this.requestedServerWindowSize));
/*     */     }
/*  97 */     if (this.allowClientWindowSize) {
/*  98 */       parameters.put("client_max_window_bits", null);
/*     */     }
/* 100 */     return new WebSocketExtensionData("permessage-deflate", parameters);
/*     */   }
/*     */   
/*     */   public WebSocketClientExtension handshakeExtension(WebSocketExtensionData extensionData)
/*     */   {
/* 105 */     if (!"permessage-deflate".equals(extensionData.name())) {
/* 106 */       return null;
/*     */     }
/*     */     
/* 109 */     boolean succeed = true;
/* 110 */     int clientWindowSize = 15;
/* 111 */     int serverWindowSize = 15;
/* 112 */     boolean serverNoContext = false;
/* 113 */     boolean clientNoContext = false;
/*     */     
/* 115 */     Iterator<Map.Entry<String, String>> parametersIterator = extensionData.parameters().entrySet().iterator();
/*     */     
/* 117 */     while ((succeed) && (parametersIterator.hasNext())) {
/* 118 */       Map.Entry<String, String> parameter = (Map.Entry)parametersIterator.next();
/*     */       
/* 120 */       if ("client_max_window_bits".equalsIgnoreCase((String)parameter.getKey()))
/*     */       {
/* 122 */         if (this.allowClientWindowSize) {
/* 123 */           clientWindowSize = Integer.parseInt((String)parameter.getValue());
/*     */         } else {
/* 125 */           succeed = false;
/*     */         }
/* 127 */       } else if ("server_max_window_bits".equalsIgnoreCase((String)parameter.getKey()))
/*     */       {
/* 129 */         serverWindowSize = Integer.parseInt((String)parameter.getValue());
/* 130 */         if ((clientWindowSize > 15) || (clientWindowSize < 8)) {
/* 131 */           succeed = false;
/*     */         }
/* 133 */       } else if ("client_no_context_takeover".equalsIgnoreCase((String)parameter.getKey()))
/*     */       {
/* 135 */         if (this.allowClientNoContext) {
/* 136 */           clientNoContext = true;
/*     */         } else {
/* 138 */           succeed = false;
/*     */         }
/* 140 */       } else if ("server_no_context_takeover".equalsIgnoreCase((String)parameter.getKey()))
/*     */       {
/* 142 */         if (this.requestedServerNoContext) {
/* 143 */           serverNoContext = true;
/*     */         } else {
/* 145 */           succeed = false;
/*     */         }
/*     */       }
/*     */       else {
/* 149 */         succeed = false;
/*     */       }
/*     */     }
/*     */     
/* 153 */     if (((this.requestedServerNoContext) && (!serverNoContext)) || (this.requestedServerWindowSize != serverWindowSize))
/*     */     {
/* 155 */       succeed = false;
/*     */     }
/*     */     
/* 158 */     if (succeed) {
/* 159 */       return new PermessageDeflateExtension(serverNoContext, serverWindowSize, clientNoContext, clientWindowSize);
/*     */     }
/*     */     
/* 162 */     return null;
/*     */   }
/*     */   
/*     */   private final class PermessageDeflateExtension
/*     */     implements WebSocketClientExtension
/*     */   {
/*     */     private final boolean serverNoContext;
/*     */     private final int serverWindowSize;
/*     */     private final boolean clientNoContext;
/*     */     private final int clientWindowSize;
/*     */     
/*     */     public int rsv()
/*     */     {
/* 175 */       return 4;
/*     */     }
/*     */     
/*     */     public PermessageDeflateExtension(boolean serverNoContext, int serverWindowSize, boolean clientNoContext, int clientWindowSize)
/*     */     {
/* 180 */       this.serverNoContext = serverNoContext;
/* 181 */       this.serverWindowSize = serverWindowSize;
/* 182 */       this.clientNoContext = clientNoContext;
/* 183 */       this.clientWindowSize = clientWindowSize;
/*     */     }
/*     */     
/*     */     public WebSocketExtensionEncoder newExtensionEncoder()
/*     */     {
/* 188 */       return new PerMessageDeflateEncoder(PerMessageDeflateClientExtensionHandshaker.this.compressionLevel, this.serverWindowSize, this.serverNoContext);
/*     */     }
/*     */     
/*     */     public WebSocketExtensionDecoder newExtensionDecoder()
/*     */     {
/* 193 */       return new PerMessageDeflateDecoder(this.clientNoContext);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\PerMessageDeflateClientExtensionHandshaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */