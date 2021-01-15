/*     */ package io.netty.buffer;
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
/*     */ public abstract interface ByteBufProcessor
/*     */ {
/*  24 */   public static final ByteBufProcessor FIND_NUL = new ByteBufProcessor()
/*     */   {
/*     */     public boolean process(byte value) throws Exception {
/*  27 */       return value != 0;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  34 */   public static final ByteBufProcessor FIND_NON_NUL = new ByteBufProcessor()
/*     */   {
/*     */     public boolean process(byte value) throws Exception {
/*  37 */       return value == 0;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  44 */   public static final ByteBufProcessor FIND_CR = new ByteBufProcessor()
/*     */   {
/*     */     public boolean process(byte value) throws Exception {
/*  47 */       return value != 13;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  54 */   public static final ByteBufProcessor FIND_NON_CR = new ByteBufProcessor()
/*     */   {
/*     */     public boolean process(byte value) throws Exception {
/*  57 */       return value == 13;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  64 */   public static final ByteBufProcessor FIND_LF = new ByteBufProcessor()
/*     */   {
/*     */     public boolean process(byte value) throws Exception {
/*  67 */       return value != 10;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  74 */   public static final ByteBufProcessor FIND_NON_LF = new ByteBufProcessor()
/*     */   {
/*     */     public boolean process(byte value) throws Exception {
/*  77 */       return value == 10;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  84 */   public static final ByteBufProcessor FIND_CRLF = new ByteBufProcessor()
/*     */   {
/*     */     public boolean process(byte value) throws Exception {
/*  87 */       return (value != 13) && (value != 10);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  94 */   public static final ByteBufProcessor FIND_NON_CRLF = new ByteBufProcessor()
/*     */   {
/*     */     public boolean process(byte value) throws Exception {
/*  97 */       return (value == 13) || (value == 10);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 104 */   public static final ByteBufProcessor FIND_LINEAR_WHITESPACE = new ByteBufProcessor()
/*     */   {
/*     */     public boolean process(byte value) throws Exception {
/* 107 */       return (value != 32) && (value != 9);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 114 */   public static final ByteBufProcessor FIND_NON_LINEAR_WHITESPACE = new ByteBufProcessor()
/*     */   {
/*     */     public boolean process(byte value) throws Exception {
/* 117 */       return (value == 32) || (value == 9);
/*     */     }
/*     */   };
/*     */   
/*     */   public abstract boolean process(byte paramByte)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\ByteBufProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */