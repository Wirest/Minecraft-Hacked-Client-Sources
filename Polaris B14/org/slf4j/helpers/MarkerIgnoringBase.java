/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.Marker;
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
/*     */ public abstract class MarkerIgnoringBase
/*     */   extends NamedLoggerBase
/*     */   implements Logger
/*     */ {
/*     */   private static final long serialVersionUID = 9044267456635152283L;
/*     */   
/*     */   public boolean isTraceEnabled(Marker marker)
/*     */   {
/*  44 */     return isTraceEnabled();
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String msg) {
/*  48 */     trace(msg);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String format, Object arg) {
/*  52 */     trace(format, arg);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String format, Object arg1, Object arg2) {
/*  56 */     trace(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String format, Object... arguments) {
/*  60 */     trace(format, arguments);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String msg, Throwable t) {
/*  64 */     trace(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isDebugEnabled(Marker marker) {
/*  68 */     return isDebugEnabled();
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String msg) {
/*  72 */     debug(msg);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String format, Object arg) {
/*  76 */     debug(format, arg);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String format, Object arg1, Object arg2) {
/*  80 */     debug(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String format, Object... arguments) {
/*  84 */     debug(format, arguments);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String msg, Throwable t) {
/*  88 */     debug(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isInfoEnabled(Marker marker) {
/*  92 */     return isInfoEnabled();
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String msg) {
/*  96 */     info(msg);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String format, Object arg) {
/* 100 */     info(format, arg);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String format, Object arg1, Object arg2) {
/* 104 */     info(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String format, Object... arguments) {
/* 108 */     info(format, arguments);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String msg, Throwable t) {
/* 112 */     info(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isWarnEnabled(Marker marker) {
/* 116 */     return isWarnEnabled();
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String msg) {
/* 120 */     warn(msg);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String format, Object arg) {
/* 124 */     warn(format, arg);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String format, Object arg1, Object arg2) {
/* 128 */     warn(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String format, Object... arguments) {
/* 132 */     warn(format, arguments);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String msg, Throwable t) {
/* 136 */     warn(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isErrorEnabled(Marker marker)
/*     */   {
/* 141 */     return isErrorEnabled();
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String msg) {
/* 145 */     error(msg);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String format, Object arg) {
/* 149 */     error(format, arg);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String format, Object arg1, Object arg2) {
/* 153 */     error(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String format, Object... arguments) {
/* 157 */     error(format, arguments);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String msg, Throwable t) {
/* 161 */     error(msg, t);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 165 */     return getClass().getName() + "(" + getName() + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\helpers\MarkerIgnoringBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */