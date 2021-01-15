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
/*     */ 
/*     */ 
/*     */ public class SubstituteLogger
/*     */   implements Logger
/*     */ {
/*     */   private final String name;
/*     */   private volatile Logger _delegate;
/*     */   
/*     */   public SubstituteLogger(String name)
/*     */   {
/*  46 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  50 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isTraceEnabled() {
/*  54 */     return delegate().isTraceEnabled();
/*     */   }
/*     */   
/*     */   public void trace(String msg) {
/*  58 */     delegate().trace(msg);
/*     */   }
/*     */   
/*     */   public void trace(String format, Object arg) {
/*  62 */     delegate().trace(format, arg);
/*     */   }
/*     */   
/*     */   public void trace(String format, Object arg1, Object arg2) {
/*  66 */     delegate().trace(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void trace(String format, Object... arguments) {
/*  70 */     delegate().trace(format, arguments);
/*     */   }
/*     */   
/*     */   public void trace(String msg, Throwable t) {
/*  74 */     delegate().trace(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isTraceEnabled(Marker marker) {
/*  78 */     return delegate().isTraceEnabled(marker);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String msg) {
/*  82 */     delegate().trace(marker, msg);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String format, Object arg) {
/*  86 */     delegate().trace(marker, format, arg);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String format, Object arg1, Object arg2) {
/*  90 */     delegate().trace(marker, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String format, Object... arguments) {
/*  94 */     delegate().trace(marker, format, arguments);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String msg, Throwable t) {
/*  98 */     delegate().trace(marker, msg, t);
/*     */   }
/*     */   
/*     */   public boolean isDebugEnabled() {
/* 102 */     return delegate().isDebugEnabled();
/*     */   }
/*     */   
/*     */   public void debug(String msg) {
/* 106 */     delegate().debug(msg);
/*     */   }
/*     */   
/*     */   public void debug(String format, Object arg) {
/* 110 */     delegate().debug(format, arg);
/*     */   }
/*     */   
/*     */   public void debug(String format, Object arg1, Object arg2) {
/* 114 */     delegate().debug(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void debug(String format, Object... arguments) {
/* 118 */     delegate().debug(format, arguments);
/*     */   }
/*     */   
/*     */   public void debug(String msg, Throwable t) {
/* 122 */     delegate().debug(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isDebugEnabled(Marker marker) {
/* 126 */     return delegate().isDebugEnabled(marker);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String msg) {
/* 130 */     delegate().debug(marker, msg);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String format, Object arg) {
/* 134 */     delegate().debug(marker, format, arg);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String format, Object arg1, Object arg2) {
/* 138 */     delegate().debug(marker, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String format, Object... arguments) {
/* 142 */     delegate().debug(marker, format, arguments);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String msg, Throwable t) {
/* 146 */     delegate().debug(marker, msg, t);
/*     */   }
/*     */   
/*     */   public boolean isInfoEnabled() {
/* 150 */     return delegate().isInfoEnabled();
/*     */   }
/*     */   
/*     */   public void info(String msg) {
/* 154 */     delegate().info(msg);
/*     */   }
/*     */   
/*     */   public void info(String format, Object arg) {
/* 158 */     delegate().info(format, arg);
/*     */   }
/*     */   
/*     */   public void info(String format, Object arg1, Object arg2) {
/* 162 */     delegate().info(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void info(String format, Object... arguments) {
/* 166 */     delegate().info(format, arguments);
/*     */   }
/*     */   
/*     */   public void info(String msg, Throwable t) {
/* 170 */     delegate().info(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isInfoEnabled(Marker marker) {
/* 174 */     return delegate().isInfoEnabled(marker);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String msg) {
/* 178 */     delegate().info(marker, msg);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String format, Object arg) {
/* 182 */     delegate().info(marker, format, arg);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String format, Object arg1, Object arg2) {
/* 186 */     delegate().info(marker, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String format, Object... arguments) {
/* 190 */     delegate().info(marker, format, arguments);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String msg, Throwable t) {
/* 194 */     delegate().info(marker, msg, t);
/*     */   }
/*     */   
/*     */   public boolean isWarnEnabled() {
/* 198 */     return delegate().isWarnEnabled();
/*     */   }
/*     */   
/*     */   public void warn(String msg) {
/* 202 */     delegate().warn(msg);
/*     */   }
/*     */   
/*     */   public void warn(String format, Object arg) {
/* 206 */     delegate().warn(format, arg);
/*     */   }
/*     */   
/*     */   public void warn(String format, Object arg1, Object arg2) {
/* 210 */     delegate().warn(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void warn(String format, Object... arguments) {
/* 214 */     delegate().warn(format, arguments);
/*     */   }
/*     */   
/*     */   public void warn(String msg, Throwable t) {
/* 218 */     delegate().warn(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isWarnEnabled(Marker marker) {
/* 222 */     return delegate().isWarnEnabled(marker);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String msg) {
/* 226 */     delegate().warn(marker, msg);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String format, Object arg) {
/* 230 */     delegate().warn(marker, format, arg);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String format, Object arg1, Object arg2) {
/* 234 */     delegate().warn(marker, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String format, Object... arguments) {
/* 238 */     delegate().warn(marker, format, arguments);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String msg, Throwable t) {
/* 242 */     delegate().warn(marker, msg, t);
/*     */   }
/*     */   
/*     */   public boolean isErrorEnabled() {
/* 246 */     return delegate().isErrorEnabled();
/*     */   }
/*     */   
/*     */   public void error(String msg) {
/* 250 */     delegate().error(msg);
/*     */   }
/*     */   
/*     */   public void error(String format, Object arg) {
/* 254 */     delegate().error(format, arg);
/*     */   }
/*     */   
/*     */   public void error(String format, Object arg1, Object arg2) {
/* 258 */     delegate().error(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void error(String format, Object... arguments) {
/* 262 */     delegate().error(format, arguments);
/*     */   }
/*     */   
/*     */   public void error(String msg, Throwable t) {
/* 266 */     delegate().error(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isErrorEnabled(Marker marker) {
/* 270 */     return delegate().isErrorEnabled(marker);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String msg) {
/* 274 */     delegate().error(marker, msg);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String format, Object arg) {
/* 278 */     delegate().error(marker, format, arg);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String format, Object arg1, Object arg2) {
/* 282 */     delegate().error(marker, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String format, Object... arguments) {
/* 286 */     delegate().error(marker, format, arguments);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String msg, Throwable t) {
/* 290 */     delegate().error(marker, msg, t);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 295 */     if (this == o) return true;
/* 296 */     if ((o == null) || (getClass() != o.getClass())) { return false;
/*     */     }
/* 298 */     SubstituteLogger that = (SubstituteLogger)o;
/*     */     
/* 300 */     if (!this.name.equals(that.name)) { return false;
/*     */     }
/* 302 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 307 */     return this.name.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   Logger delegate()
/*     */   {
/* 315 */     return this._delegate != null ? this._delegate : NOPLogger.NOP_LOGGER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDelegate(Logger delegate)
/*     */   {
/* 323 */     this._delegate = delegate;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\helpers\SubstituteLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */