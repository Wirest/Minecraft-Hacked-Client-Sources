/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.io.PrintStream;
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
/*     */ public class LogLog
/*     */ {
/*     */   public static final String DEBUG_KEY = "log4j.debug";
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final String CONFIG_DEBUG_KEY = "log4j.configDebug";
/*  58 */   protected static boolean debugEnabled = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private static boolean quietMode = false;
/*     */   
/*     */ 
/*     */   private static final String PREFIX = "log4j: ";
/*     */   
/*     */ 
/*     */   private static final String ERR_PREFIX = "log4j:ERROR ";
/*     */   
/*     */   private static final String WARN_PREFIX = "log4j:WARN ";
/*     */   
/*     */ 
/*     */   public static void setInternalDebugging(boolean enabled)
/*     */   {
/*  76 */     debugEnabled = enabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void debug(String msg)
/*     */   {
/*  84 */     if ((debugEnabled) && (!quietMode)) {
/*  85 */       System.out.println("log4j: " + msg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void debug(String msg, Throwable t)
/*     */   {
/*  94 */     if ((debugEnabled) && (!quietMode)) {
/*  95 */       System.out.println("log4j: " + msg);
/*  96 */       if (t != null) {
/*  97 */         t.printStackTrace(System.out);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void error(String msg)
/*     */   {
/* 107 */     if (quietMode)
/* 108 */       return;
/* 109 */     System.err.println("log4j:ERROR " + msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void error(String msg, Throwable t)
/*     */   {
/* 118 */     if (quietMode) {
/* 119 */       return;
/*     */     }
/* 121 */     System.err.println("log4j:ERROR " + msg);
/* 122 */     if (t != null) {
/* 123 */       t.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setQuietMode(boolean quietMode)
/*     */   {
/* 134 */     quietMode = quietMode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void warn(String msg)
/*     */   {
/* 142 */     if (quietMode) {
/* 143 */       return;
/*     */     }
/* 145 */     System.err.println("log4j:WARN " + msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void warn(String msg, Throwable t)
/*     */   {
/* 153 */     if (quietMode) {
/* 154 */       return;
/*     */     }
/* 156 */     System.err.println("log4j:WARN " + msg);
/* 157 */     if (t != null) {
/* 158 */       t.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\apache\log4j\helpers\LogLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */