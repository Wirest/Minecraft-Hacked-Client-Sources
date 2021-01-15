/*     */ package io.netty.handler.proxy;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ProxyConnectionEvent
/*     */ {
/*     */   private final String protocol;
/*     */   private final String authScheme;
/*     */   private final SocketAddress proxyAddress;
/*     */   private final SocketAddress destinationAddress;
/*     */   private String strVal;
/*     */   
/*     */   public ProxyConnectionEvent(String protocol, String authScheme, SocketAddress proxyAddress, SocketAddress destinationAddress)
/*     */   {
/*  36 */     if (protocol == null) {
/*  37 */       throw new NullPointerException("protocol");
/*     */     }
/*  39 */     if (authScheme == null) {
/*  40 */       throw new NullPointerException("authScheme");
/*     */     }
/*  42 */     if (proxyAddress == null) {
/*  43 */       throw new NullPointerException("proxyAddress");
/*     */     }
/*  45 */     if (destinationAddress == null) {
/*  46 */       throw new NullPointerException("destinationAddress");
/*     */     }
/*     */     
/*  49 */     this.protocol = protocol;
/*  50 */     this.authScheme = authScheme;
/*  51 */     this.proxyAddress = proxyAddress;
/*  52 */     this.destinationAddress = destinationAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String protocol()
/*     */   {
/*  59 */     return this.protocol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String authScheme()
/*     */   {
/*  66 */     return this.authScheme;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends SocketAddress> T proxyAddress()
/*     */   {
/*  74 */     return this.proxyAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends SocketAddress> T destinationAddress()
/*     */   {
/*  82 */     return this.destinationAddress;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  87 */     if (this.strVal != null) {
/*  88 */       return this.strVal;
/*     */     }
/*     */     
/*  91 */     StringBuilder buf = new StringBuilder(128).append(StringUtil.simpleClassName(this)).append('(').append(this.protocol).append(", ").append(this.authScheme).append(", ").append(this.proxyAddress).append(" => ").append(this.destinationAddress).append(')');
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
/* 103 */     return this.strVal = buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\proxy\ProxyConnectionEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */