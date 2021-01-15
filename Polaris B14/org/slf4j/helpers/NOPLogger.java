/*     */ package org.slf4j.helpers;
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
/*     */ public class NOPLogger
/*     */   extends MarkerIgnoringBase
/*     */ {
/*     */   private static final long serialVersionUID = -517220405410904473L;
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
/*  43 */   public static final NOPLogger NOP_LOGGER = new NOPLogger();
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
/*     */   public String getName()
/*     */   {
/*  56 */     return "NOP";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isTraceEnabled()
/*     */   {
/*  64 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void trace(String msg) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void trace(String format, Object arg) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void trace(String format, Object arg1, Object arg2) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void trace(String format, Object... argArray) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void trace(String msg, Throwable t) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isDebugEnabled()
/*     */   {
/*  97 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void debug(String msg) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void debug(String format, Object arg) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void debug(String format, Object arg1, Object arg2) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void debug(String format, Object... argArray) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void debug(String msg, Throwable t) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isInfoEnabled()
/*     */   {
/* 133 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void info(String msg) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void info(String format, Object arg1) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void info(String format, Object arg1, Object arg2) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void info(String format, Object... argArray) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void info(String msg, Throwable t) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isWarnEnabled()
/*     */   {
/* 169 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void warn(String msg) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void warn(String format, Object arg1) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void warn(String format, Object arg1, Object arg2) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void warn(String format, Object... argArray) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void warn(String msg, Throwable t) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean isErrorEnabled()
/*     */   {
/* 201 */     return false;
/*     */   }
/*     */   
/*     */   public final void error(String msg) {}
/*     */   
/*     */   public final void error(String format, Object arg1) {}
/*     */   
/*     */   public final void error(String format, Object arg1, Object arg2) {}
/*     */   
/*     */   public final void error(String format, Object... argArray) {}
/*     */   
/*     */   public final void error(String msg, Throwable t) {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\helpers\NOPLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */