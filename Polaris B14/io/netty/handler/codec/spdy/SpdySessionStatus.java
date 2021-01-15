/*     */ package io.netty.handler.codec.spdy;
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
/*     */ public class SpdySessionStatus
/*     */   implements Comparable<SpdySessionStatus>
/*     */ {
/*  26 */   public static final SpdySessionStatus OK = new SpdySessionStatus(0, "OK");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  32 */   public static final SpdySessionStatus PROTOCOL_ERROR = new SpdySessionStatus(1, "PROTOCOL_ERROR");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  38 */   public static final SpdySessionStatus INTERNAL_ERROR = new SpdySessionStatus(2, "INTERNAL_ERROR");
/*     */   
/*     */   private final int code;
/*     */   
/*     */   private final String statusPhrase;
/*     */   
/*     */ 
/*     */   public static SpdySessionStatus valueOf(int code)
/*     */   {
/*  47 */     switch (code) {
/*     */     case 0: 
/*  49 */       return OK;
/*     */     case 1: 
/*  51 */       return PROTOCOL_ERROR;
/*     */     case 2: 
/*  53 */       return INTERNAL_ERROR;
/*     */     }
/*     */     
/*  56 */     return new SpdySessionStatus(code, "UNKNOWN (" + code + ')');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpdySessionStatus(int code, String statusPhrase)
/*     */   {
/*  68 */     if (statusPhrase == null) {
/*  69 */       throw new NullPointerException("statusPhrase");
/*     */     }
/*     */     
/*  72 */     this.code = code;
/*  73 */     this.statusPhrase = statusPhrase;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int code()
/*     */   {
/*  80 */     return this.code;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String statusPhrase()
/*     */   {
/*  87 */     return this.statusPhrase;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  92 */     return code();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/*  97 */     if (!(o instanceof SpdySessionStatus)) {
/*  98 */       return false;
/*     */     }
/*     */     
/* 101 */     return code() == ((SpdySessionStatus)o).code();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 106 */     return statusPhrase();
/*     */   }
/*     */   
/*     */   public int compareTo(SpdySessionStatus o)
/*     */   {
/* 111 */     return code() - o.code();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdySessionStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */