/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import org.apache.tomcat.jni.SSLContext;
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
/*     */ public final class OpenSslSessionStats
/*     */ {
/*     */   private final long context;
/*     */   
/*     */   OpenSslSessionStats(long context)
/*     */   {
/*  31 */     this.context = context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long number()
/*     */   {
/*  38 */     return SSLContext.sessionNumber(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long connect()
/*     */   {
/*  45 */     return SSLContext.sessionConnect(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long connectGood()
/*     */   {
/*  52 */     return SSLContext.sessionConnectGood(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long connectRenegotiate()
/*     */   {
/*  59 */     return SSLContext.sessionConnectRenegotiate(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long accept()
/*     */   {
/*  66 */     return SSLContext.sessionAccept(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long acceptGood()
/*     */   {
/*  73 */     return SSLContext.sessionAcceptGood(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long acceptRenegotiate()
/*     */   {
/*  80 */     return SSLContext.sessionAcceptRenegotiate(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long hits()
/*     */   {
/*  89 */     return SSLContext.sessionHits(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long cbHits()
/*     */   {
/*  96 */     return SSLContext.sessionCbHits(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long misses()
/*     */   {
/* 104 */     return SSLContext.sessionMisses(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long timeouts()
/*     */   {
/* 113 */     return SSLContext.sessionTimeouts(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long cacheFull()
/*     */   {
/* 120 */     return SSLContext.sessionCacheFull(this.context);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\OpenSslSessionStats.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */