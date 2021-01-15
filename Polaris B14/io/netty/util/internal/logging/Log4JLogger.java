/*     */ package io.netty.util.internal.logging;
/*     */ 
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
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
/*     */ 
/*     */ 
/*     */ class Log4JLogger
/*     */   extends AbstractInternalLogger
/*     */ {
/*     */   private static final long serialVersionUID = 2851357342488183058L;
/*     */   final transient Logger logger;
/*  59 */   static final String FQCN = Log4JLogger.class.getName();
/*     */   
/*     */   final boolean traceCapable;
/*     */   
/*     */ 
/*     */   Log4JLogger(Logger logger)
/*     */   {
/*  66 */     super(logger.getName());
/*  67 */     this.logger = logger;
/*  68 */     this.traceCapable = isTraceCapable();
/*     */   }
/*     */   
/*     */   private boolean isTraceCapable() {
/*     */     try {
/*  73 */       this.logger.isTraceEnabled();
/*  74 */       return true;
/*     */     } catch (NoSuchMethodError ignored) {}
/*  76 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTraceEnabled()
/*     */   {
/*  87 */     if (this.traceCapable) {
/*  88 */       return this.logger.isTraceEnabled();
/*     */     }
/*  90 */     return this.logger.isDebugEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void trace(String msg)
/*     */   {
/* 102 */     this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, msg, null);
/*     */   }
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
/*     */   public void trace(String format, Object arg)
/*     */   {
/* 121 */     if (isTraceEnabled()) {
/* 122 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 123 */       this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void trace(String format, Object argA, Object argB)
/*     */   {
/* 146 */     if (isTraceEnabled()) {
/* 147 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 148 */       this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void trace(String format, Object... arguments)
/*     */   {
/* 169 */     if (isTraceEnabled()) {
/* 170 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 171 */       this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void trace(String msg, Throwable t)
/*     */   {
/* 186 */     this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, msg, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDebugEnabled()
/*     */   {
/* 196 */     return this.logger.isDebugEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void debug(String msg)
/*     */   {
/* 207 */     this.logger.log(FQCN, Level.DEBUG, msg, null);
/*     */   }
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
/*     */   public void debug(String format, Object arg)
/*     */   {
/* 226 */     if (this.logger.isDebugEnabled()) {
/* 227 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 228 */       this.logger.log(FQCN, Level.DEBUG, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void debug(String format, Object argA, Object argB)
/*     */   {
/* 250 */     if (this.logger.isDebugEnabled()) {
/* 251 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 252 */       this.logger.log(FQCN, Level.DEBUG, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void debug(String format, Object... arguments)
/*     */   {
/* 271 */     if (this.logger.isDebugEnabled()) {
/* 272 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 273 */       this.logger.log(FQCN, Level.DEBUG, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void debug(String msg, Throwable t)
/*     */   {
/* 287 */     this.logger.log(FQCN, Level.DEBUG, msg, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInfoEnabled()
/*     */   {
/* 297 */     return this.logger.isInfoEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void info(String msg)
/*     */   {
/* 308 */     this.logger.log(FQCN, Level.INFO, msg, null);
/*     */   }
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
/*     */   public void info(String format, Object arg)
/*     */   {
/* 326 */     if (this.logger.isInfoEnabled()) {
/* 327 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 328 */       this.logger.log(FQCN, Level.INFO, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void info(String format, Object argA, Object argB)
/*     */   {
/* 350 */     if (this.logger.isInfoEnabled()) {
/* 351 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 352 */       this.logger.log(FQCN, Level.INFO, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void info(String format, Object... argArray)
/*     */   {
/* 372 */     if (this.logger.isInfoEnabled()) {
/* 373 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
/* 374 */       this.logger.log(FQCN, Level.INFO, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void info(String msg, Throwable t)
/*     */   {
/* 389 */     this.logger.log(FQCN, Level.INFO, msg, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWarnEnabled()
/*     */   {
/* 399 */     return this.logger.isEnabledFor(Level.WARN);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void warn(String msg)
/*     */   {
/* 410 */     this.logger.log(FQCN, Level.WARN, msg, null);
/*     */   }
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
/*     */   public void warn(String format, Object arg)
/*     */   {
/* 429 */     if (this.logger.isEnabledFor(Level.WARN)) {
/* 430 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 431 */       this.logger.log(FQCN, Level.WARN, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void warn(String format, Object argA, Object argB)
/*     */   {
/* 453 */     if (this.logger.isEnabledFor(Level.WARN)) {
/* 454 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 455 */       this.logger.log(FQCN, Level.WARN, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void warn(String format, Object... argArray)
/*     */   {
/* 475 */     if (this.logger.isEnabledFor(Level.WARN)) {
/* 476 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
/* 477 */       this.logger.log(FQCN, Level.WARN, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void warn(String msg, Throwable t)
/*     */   {
/* 492 */     this.logger.log(FQCN, Level.WARN, msg, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isErrorEnabled()
/*     */   {
/* 502 */     return this.logger.isEnabledFor(Level.ERROR);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void error(String msg)
/*     */   {
/* 513 */     this.logger.log(FQCN, Level.ERROR, msg, null);
/*     */   }
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
/*     */   public void error(String format, Object arg)
/*     */   {
/* 532 */     if (this.logger.isEnabledFor(Level.ERROR)) {
/* 533 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 534 */       this.logger.log(FQCN, Level.ERROR, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void error(String format, Object argA, Object argB)
/*     */   {
/* 556 */     if (this.logger.isEnabledFor(Level.ERROR)) {
/* 557 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 558 */       this.logger.log(FQCN, Level.ERROR, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void error(String format, Object... argArray)
/*     */   {
/* 578 */     if (this.logger.isEnabledFor(Level.ERROR)) {
/* 579 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
/* 580 */       this.logger.log(FQCN, Level.ERROR, ft.getMessage(), ft.getThrowable());
/*     */     }
/*     */   }
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
/*     */   public void error(String msg, Throwable t)
/*     */   {
/* 595 */     this.logger.log(FQCN, Level.ERROR, msg, t);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\logging\Log4JLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */