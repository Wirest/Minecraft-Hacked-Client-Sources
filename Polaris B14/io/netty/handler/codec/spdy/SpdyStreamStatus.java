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
/*     */ public class SpdyStreamStatus
/*     */   implements Comparable<SpdyStreamStatus>
/*     */ {
/*  26 */   public static final SpdyStreamStatus PROTOCOL_ERROR = new SpdyStreamStatus(1, "PROTOCOL_ERROR");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  32 */   public static final SpdyStreamStatus INVALID_STREAM = new SpdyStreamStatus(2, "INVALID_STREAM");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  38 */   public static final SpdyStreamStatus REFUSED_STREAM = new SpdyStreamStatus(3, "REFUSED_STREAM");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  44 */   public static final SpdyStreamStatus UNSUPPORTED_VERSION = new SpdyStreamStatus(4, "UNSUPPORTED_VERSION");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  50 */   public static final SpdyStreamStatus CANCEL = new SpdyStreamStatus(5, "CANCEL");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  56 */   public static final SpdyStreamStatus INTERNAL_ERROR = new SpdyStreamStatus(6, "INTERNAL_ERROR");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */   public static final SpdyStreamStatus FLOW_CONTROL_ERROR = new SpdyStreamStatus(7, "FLOW_CONTROL_ERROR");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */   public static final SpdyStreamStatus STREAM_IN_USE = new SpdyStreamStatus(8, "STREAM_IN_USE");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */   public static final SpdyStreamStatus STREAM_ALREADY_CLOSED = new SpdyStreamStatus(9, "STREAM_ALREADY_CLOSED");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */   public static final SpdyStreamStatus INVALID_CREDENTIALS = new SpdyStreamStatus(10, "INVALID_CREDENTIALS");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */   public static final SpdyStreamStatus FRAME_TOO_LARGE = new SpdyStreamStatus(11, "FRAME_TOO_LARGE");
/*     */   
/*     */   private final int code;
/*     */   
/*     */   private final String statusPhrase;
/*     */   
/*     */ 
/*     */   public static SpdyStreamStatus valueOf(int code)
/*     */   {
/*  95 */     if (code == 0) {
/*  96 */       throw new IllegalArgumentException("0 is not a valid status code for a RST_STREAM");
/*     */     }
/*     */     
/*     */ 
/* 100 */     switch (code) {
/*     */     case 1: 
/* 102 */       return PROTOCOL_ERROR;
/*     */     case 2: 
/* 104 */       return INVALID_STREAM;
/*     */     case 3: 
/* 106 */       return REFUSED_STREAM;
/*     */     case 4: 
/* 108 */       return UNSUPPORTED_VERSION;
/*     */     case 5: 
/* 110 */       return CANCEL;
/*     */     case 6: 
/* 112 */       return INTERNAL_ERROR;
/*     */     case 7: 
/* 114 */       return FLOW_CONTROL_ERROR;
/*     */     case 8: 
/* 116 */       return STREAM_IN_USE;
/*     */     case 9: 
/* 118 */       return STREAM_ALREADY_CLOSED;
/*     */     case 10: 
/* 120 */       return INVALID_CREDENTIALS;
/*     */     case 11: 
/* 122 */       return FRAME_TOO_LARGE;
/*     */     }
/*     */     
/* 125 */     return new SpdyStreamStatus(code, "UNKNOWN (" + code + ')');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpdyStreamStatus(int code, String statusPhrase)
/*     */   {
/* 137 */     if (code == 0) {
/* 138 */       throw new IllegalArgumentException("0 is not a valid status code for a RST_STREAM");
/*     */     }
/*     */     
/*     */ 
/* 142 */     if (statusPhrase == null) {
/* 143 */       throw new NullPointerException("statusPhrase");
/*     */     }
/*     */     
/* 146 */     this.code = code;
/* 147 */     this.statusPhrase = statusPhrase;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int code()
/*     */   {
/* 154 */     return this.code;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String statusPhrase()
/*     */   {
/* 161 */     return this.statusPhrase;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 166 */     return code();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 171 */     if (!(o instanceof SpdyStreamStatus)) {
/* 172 */       return false;
/*     */     }
/*     */     
/* 175 */     return code() == ((SpdyStreamStatus)o).code();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 180 */     return statusPhrase();
/*     */   }
/*     */   
/*     */   public int compareTo(SpdyStreamStatus o)
/*     */   {
/* 185 */     return code() - o.code();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyStreamStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */