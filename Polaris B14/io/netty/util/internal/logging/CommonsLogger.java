/*     */ package io.netty.util.internal.logging;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CommonsLogger
/*     */   extends AbstractInternalLogger
/*     */ {
/*     */   private static final long serialVersionUID = 8647838678388394885L;
/*     */   private final transient Log logger;
/*     */   
/*     */   CommonsLogger(Log logger, String name)
/*     */   {
/*  55 */     super(name);
/*  56 */     if (logger == null) {
/*  57 */       throw new NullPointerException("logger");
/*     */     }
/*  59 */     this.logger = logger;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTraceEnabled()
/*     */   {
/*  68 */     return this.logger.isTraceEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void trace(String msg)
/*     */   {
/*  79 */     this.logger.trace(msg);
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
/*  98 */     if (this.logger.isTraceEnabled()) {
/*  99 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 100 */       this.logger.trace(ft.getMessage(), ft.getThrowable());
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
/*     */   public void trace(String format, Object argA, Object argB)
/*     */   {
/* 122 */     if (this.logger.isTraceEnabled()) {
/* 123 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 124 */       this.logger.trace(ft.getMessage(), ft.getThrowable());
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
/*     */   public void trace(String format, Object... arguments)
/*     */   {
/* 142 */     if (this.logger.isTraceEnabled()) {
/* 143 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 144 */       this.logger.trace(ft.getMessage(), ft.getThrowable());
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
/* 159 */     this.logger.trace(msg, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDebugEnabled()
/*     */   {
/* 168 */     return this.logger.isDebugEnabled();
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
/*     */   public void debug(String msg)
/*     */   {
/* 181 */     this.logger.debug(msg);
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
/* 200 */     if (this.logger.isDebugEnabled()) {
/* 201 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 202 */       this.logger.debug(ft.getMessage(), ft.getThrowable());
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
/* 224 */     if (this.logger.isDebugEnabled()) {
/* 225 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 226 */       this.logger.debug(ft.getMessage(), ft.getThrowable());
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
/*     */   public void debug(String format, Object... arguments)
/*     */   {
/* 244 */     if (this.logger.isDebugEnabled()) {
/* 245 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 246 */       this.logger.debug(ft.getMessage(), ft.getThrowable());
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
/*     */   public void debug(String msg, Throwable t)
/*     */   {
/* 261 */     this.logger.debug(msg, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInfoEnabled()
/*     */   {
/* 270 */     return this.logger.isInfoEnabled();
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
/* 281 */     this.logger.info(msg);
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
/*     */   public void info(String format, Object arg)
/*     */   {
/* 301 */     if (this.logger.isInfoEnabled()) {
/* 302 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 303 */       this.logger.info(ft.getMessage(), ft.getThrowable());
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
/*     */   public void info(String format, Object argA, Object argB)
/*     */   {
/* 324 */     if (this.logger.isInfoEnabled()) {
/* 325 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 326 */       this.logger.info(ft.getMessage(), ft.getThrowable());
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
/*     */   public void info(String format, Object... arguments)
/*     */   {
/* 344 */     if (this.logger.isInfoEnabled()) {
/* 345 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 346 */       this.logger.info(ft.getMessage(), ft.getThrowable());
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
/* 361 */     this.logger.info(msg, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWarnEnabled()
/*     */   {
/* 370 */     return this.logger.isWarnEnabled();
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
/* 381 */     this.logger.warn(msg);
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
/* 400 */     if (this.logger.isWarnEnabled()) {
/* 401 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 402 */       this.logger.warn(ft.getMessage(), ft.getThrowable());
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
/* 424 */     if (this.logger.isWarnEnabled()) {
/* 425 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 426 */       this.logger.warn(ft.getMessage(), ft.getThrowable());
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
/*     */   public void warn(String format, Object... arguments)
/*     */   {
/* 444 */     if (this.logger.isWarnEnabled()) {
/* 445 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 446 */       this.logger.warn(ft.getMessage(), ft.getThrowable());
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
/*     */   public void warn(String msg, Throwable t)
/*     */   {
/* 462 */     this.logger.warn(msg, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isErrorEnabled()
/*     */   {
/* 471 */     return this.logger.isErrorEnabled();
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
/* 482 */     this.logger.error(msg);
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
/* 501 */     if (this.logger.isErrorEnabled()) {
/* 502 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 503 */       this.logger.error(ft.getMessage(), ft.getThrowable());
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
/* 525 */     if (this.logger.isErrorEnabled()) {
/* 526 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 527 */       this.logger.error(ft.getMessage(), ft.getThrowable());
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
/*     */   public void error(String format, Object... arguments)
/*     */   {
/* 545 */     if (this.logger.isErrorEnabled()) {
/* 546 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 547 */       this.logger.error(ft.getMessage(), ft.getThrowable());
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
/* 562 */     this.logger.error(msg, t);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\logging\CommonsLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */