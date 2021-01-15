/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DnsResponseHeader
/*     */   extends DnsHeader
/*     */ {
/*     */   private boolean authoritativeAnswer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean truncated;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean recursionAvailable;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private DnsResponseCode responseCode;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsResponseHeader(DnsMessage parent, int id)
/*     */   {
/*  41 */     super(parent);
/*  42 */     setId(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAuthoritativeAnswer()
/*     */   {
/*  50 */     return this.authoritativeAnswer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTruncated()
/*     */   {
/*  58 */     return this.truncated;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isRecursionAvailable()
/*     */   {
/*  65 */     return this.recursionAvailable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DnsResponseCode responseCode()
/*     */   {
/*  72 */     return this.responseCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int type()
/*     */   {
/*  81 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsResponseHeader setAuthoritativeAnswer(boolean authoritativeAnswer)
/*     */   {
/*  92 */     this.authoritativeAnswer = authoritativeAnswer;
/*  93 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsResponseHeader setTruncated(boolean truncated)
/*     */   {
/* 104 */     this.truncated = truncated;
/* 105 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsResponseHeader setRecursionAvailable(boolean recursionAvailable)
/*     */   {
/* 115 */     this.recursionAvailable = recursionAvailable;
/* 116 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsResponseHeader setResponseCode(DnsResponseCode responseCode)
/*     */   {
/* 126 */     this.responseCode = responseCode;
/* 127 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsResponseHeader setType(int type)
/*     */   {
/* 139 */     if (type != 1) {
/* 140 */       throw new IllegalArgumentException("type cannot be anything but TYPE_RESPONSE (1) for a response header.");
/*     */     }
/* 142 */     super.setType(type);
/* 143 */     return this;
/*     */   }
/*     */   
/*     */   public DnsResponseHeader setId(int id)
/*     */   {
/* 148 */     super.setId(id);
/* 149 */     return this;
/*     */   }
/*     */   
/*     */   public DnsHeader setRecursionDesired(boolean recursionDesired)
/*     */   {
/* 154 */     return super.setRecursionDesired(recursionDesired);
/*     */   }
/*     */   
/*     */   public DnsResponseHeader setOpcode(int opcode)
/*     */   {
/* 159 */     super.setOpcode(opcode);
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public DnsResponseHeader setZ(int z)
/*     */   {
/* 165 */     super.setZ(z);
/* 166 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsResponseHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */