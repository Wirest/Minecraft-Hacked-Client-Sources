/*     */ package io.netty.handler.codec.dns;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DnsHeader
/*     */ {
/*     */   public static final int TYPE_QUERY = 0;
/*     */   public static final int TYPE_RESPONSE = 1;
/*     */   public static final int OPCODE_QUERY = 0;
/*     */   @Deprecated
/*     */   public static final int OPCODE_IQUERY = 1;
/*     */   private final DnsMessage parent;
/*     */   private boolean recursionDesired;
/*     */   private int opcode;
/*     */   private int id;
/*     */   private int type;
/*     */   private int z;
/*     */   
/*     */   DnsHeader(DnsMessage parent)
/*     */   {
/*  57 */     if (parent == null) {
/*  58 */       throw new NullPointerException("parent");
/*     */     }
/*  60 */     this.parent = parent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int questionCount()
/*     */   {
/*  67 */     return this.parent.questions().size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int answerCount()
/*     */   {
/*  74 */     return this.parent.answers().size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int authorityResourceCount()
/*     */   {
/*  82 */     return this.parent.authorityResources().size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int additionalResourceCount()
/*     */   {
/*  90 */     return this.parent.additionalResources().size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isRecursionDesired()
/*     */   {
/*  97 */     return this.recursionDesired;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int opcode()
/*     */   {
/* 107 */     return this.opcode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int type()
/*     */   {
/* 116 */     return this.type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int id()
/*     */   {
/* 124 */     return this.id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsHeader setOpcode(int opcode)
/*     */   {
/* 135 */     this.opcode = opcode;
/* 136 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsHeader setRecursionDesired(boolean recursionDesired)
/*     */   {
/* 148 */     this.recursionDesired = recursionDesired;
/* 149 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsHeader setType(int type)
/*     */   {
/* 160 */     this.type = type;
/* 161 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsHeader setId(int id)
/*     */   {
/* 172 */     this.id = id;
/* 173 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int z()
/*     */   {
/* 180 */     return this.z;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsHeader setZ(int z)
/*     */   {
/* 191 */     this.z = z;
/* 192 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */