/*     */ package io.netty.util.internal.logging;
/*     */ 
/*     */ import org.slf4j.Logger;
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
/*     */ class Slf4JLogger
/*     */   extends AbstractInternalLogger
/*     */ {
/*     */   private static final long serialVersionUID = 108038972685130825L;
/*     */   private final transient Logger logger;
/*     */   
/*     */   Slf4JLogger(Logger logger)
/*     */   {
/*  30 */     super(logger.getName());
/*  31 */     this.logger = logger;
/*     */   }
/*     */   
/*     */   public boolean isTraceEnabled()
/*     */   {
/*  36 */     return this.logger.isTraceEnabled();
/*     */   }
/*     */   
/*     */   public void trace(String msg)
/*     */   {
/*  41 */     this.logger.trace(msg);
/*     */   }
/*     */   
/*     */   public void trace(String format, Object arg)
/*     */   {
/*  46 */     this.logger.trace(format, arg);
/*     */   }
/*     */   
/*     */   public void trace(String format, Object argA, Object argB)
/*     */   {
/*  51 */     this.logger.trace(format, argA, argB);
/*     */   }
/*     */   
/*     */   public void trace(String format, Object... argArray)
/*     */   {
/*  56 */     this.logger.trace(format, argArray);
/*     */   }
/*     */   
/*     */   public void trace(String msg, Throwable t)
/*     */   {
/*  61 */     this.logger.trace(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isDebugEnabled()
/*     */   {
/*  66 */     return this.logger.isDebugEnabled();
/*     */   }
/*     */   
/*     */   public void debug(String msg)
/*     */   {
/*  71 */     this.logger.debug(msg);
/*     */   }
/*     */   
/*     */   public void debug(String format, Object arg)
/*     */   {
/*  76 */     this.logger.debug(format, arg);
/*     */   }
/*     */   
/*     */   public void debug(String format, Object argA, Object argB)
/*     */   {
/*  81 */     this.logger.debug(format, argA, argB);
/*     */   }
/*     */   
/*     */   public void debug(String format, Object... argArray)
/*     */   {
/*  86 */     this.logger.debug(format, argArray);
/*     */   }
/*     */   
/*     */   public void debug(String msg, Throwable t)
/*     */   {
/*  91 */     this.logger.debug(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isInfoEnabled()
/*     */   {
/*  96 */     return this.logger.isInfoEnabled();
/*     */   }
/*     */   
/*     */   public void info(String msg)
/*     */   {
/* 101 */     this.logger.info(msg);
/*     */   }
/*     */   
/*     */   public void info(String format, Object arg)
/*     */   {
/* 106 */     this.logger.info(format, arg);
/*     */   }
/*     */   
/*     */   public void info(String format, Object argA, Object argB)
/*     */   {
/* 111 */     this.logger.info(format, argA, argB);
/*     */   }
/*     */   
/*     */   public void info(String format, Object... argArray)
/*     */   {
/* 116 */     this.logger.info(format, argArray);
/*     */   }
/*     */   
/*     */   public void info(String msg, Throwable t)
/*     */   {
/* 121 */     this.logger.info(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isWarnEnabled()
/*     */   {
/* 126 */     return this.logger.isWarnEnabled();
/*     */   }
/*     */   
/*     */   public void warn(String msg)
/*     */   {
/* 131 */     this.logger.warn(msg);
/*     */   }
/*     */   
/*     */   public void warn(String format, Object arg)
/*     */   {
/* 136 */     this.logger.warn(format, arg);
/*     */   }
/*     */   
/*     */   public void warn(String format, Object... argArray)
/*     */   {
/* 141 */     this.logger.warn(format, argArray);
/*     */   }
/*     */   
/*     */   public void warn(String format, Object argA, Object argB)
/*     */   {
/* 146 */     this.logger.warn(format, argA, argB);
/*     */   }
/*     */   
/*     */   public void warn(String msg, Throwable t)
/*     */   {
/* 151 */     this.logger.warn(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isErrorEnabled()
/*     */   {
/* 156 */     return this.logger.isErrorEnabled();
/*     */   }
/*     */   
/*     */   public void error(String msg)
/*     */   {
/* 161 */     this.logger.error(msg);
/*     */   }
/*     */   
/*     */   public void error(String format, Object arg)
/*     */   {
/* 166 */     this.logger.error(format, arg);
/*     */   }
/*     */   
/*     */   public void error(String format, Object argA, Object argB)
/*     */   {
/* 171 */     this.logger.error(format, argA, argB);
/*     */   }
/*     */   
/*     */   public void error(String format, Object... argArray)
/*     */   {
/* 176 */     this.logger.error(format, argArray);
/*     */   }
/*     */   
/*     */   public void error(String msg, Throwable t)
/*     */   {
/* 181 */     this.logger.error(msg, t);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\logging\Slf4JLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */