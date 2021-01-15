/*     */ package ch.qos.logback.classic.spi;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
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
/*     */ public class ThrowableProxyVO
/*     */   implements IThrowableProxy, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -773438177285807139L;
/*     */   private String className;
/*     */   private String message;
/*     */   private int commonFramesCount;
/*     */   private StackTraceElementProxy[] stackTraceElementProxyArray;
/*     */   private IThrowableProxy cause;
/*     */   private IThrowableProxy[] suppressed;
/*     */   
/*     */   public String getMessage()
/*     */   {
/*  32 */     return this.message;
/*     */   }
/*     */   
/*     */   public String getClassName() {
/*  36 */     return this.className;
/*     */   }
/*     */   
/*     */   public int getCommonFrames() {
/*  40 */     return this.commonFramesCount;
/*     */   }
/*     */   
/*     */   public IThrowableProxy getCause() {
/*  44 */     return this.cause;
/*     */   }
/*     */   
/*     */   public StackTraceElementProxy[] getStackTraceElementProxyArray() {
/*  48 */     return this.stackTraceElementProxyArray;
/*     */   }
/*     */   
/*     */   public IThrowableProxy[] getSuppressed() {
/*  52 */     return this.suppressed;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  57 */     int prime = 31;
/*  58 */     int result = 1;
/*  59 */     result = 31 * result + (this.className == null ? 0 : this.className.hashCode());
/*     */     
/*  61 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/*  66 */     if (this == obj)
/*  67 */       return true;
/*  68 */     if (obj == null)
/*  69 */       return false;
/*  70 */     if (getClass() != obj.getClass())
/*  71 */       return false;
/*  72 */     ThrowableProxyVO other = (ThrowableProxyVO)obj;
/*     */     
/*  74 */     if (this.className == null) {
/*  75 */       if (other.className != null)
/*  76 */         return false;
/*  77 */     } else if (!this.className.equals(other.className)) {
/*  78 */       return false;
/*     */     }
/*  80 */     if (!Arrays.equals(this.stackTraceElementProxyArray, other.stackTraceElementProxyArray)) {
/*  81 */       return false;
/*     */     }
/*  83 */     if (!Arrays.equals(this.suppressed, other.suppressed)) {
/*  84 */       return false;
/*     */     }
/*  86 */     if (this.cause == null) {
/*  87 */       if (other.cause != null)
/*  88 */         return false;
/*  89 */     } else if (!this.cause.equals(other.cause)) {
/*  90 */       return false;
/*     */     }
/*  92 */     return true;
/*     */   }
/*     */   
/*     */   public static ThrowableProxyVO build(IThrowableProxy throwableProxy) {
/*  96 */     if (throwableProxy == null) {
/*  97 */       return null;
/*     */     }
/*  99 */     ThrowableProxyVO tpvo = new ThrowableProxyVO();
/* 100 */     tpvo.className = throwableProxy.getClassName();
/* 101 */     tpvo.message = throwableProxy.getMessage();
/* 102 */     tpvo.commonFramesCount = throwableProxy.getCommonFrames();
/* 103 */     tpvo.stackTraceElementProxyArray = throwableProxy.getStackTraceElementProxyArray();
/* 104 */     IThrowableProxy cause = throwableProxy.getCause();
/* 105 */     if (cause != null) {
/* 106 */       tpvo.cause = build(cause);
/*     */     }
/* 108 */     IThrowableProxy[] suppressed = throwableProxy.getSuppressed();
/* 109 */     if (suppressed != null) {
/* 110 */       tpvo.suppressed = new IThrowableProxy[suppressed.length];
/* 111 */       for (int i = 0; i < suppressed.length; i++) {
/* 112 */         tpvo.suppressed[i] = build(suppressed[i]);
/*     */       }
/*     */     }
/* 115 */     return tpvo;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\ThrowableProxyVO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */