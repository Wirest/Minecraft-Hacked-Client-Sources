/*     */ package io.netty.util.internal.logging;
/*     */ 
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogRecord;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class JdkLogger
/*     */   extends AbstractInternalLogger
/*     */ {
/*     */   private static final long serialVersionUID = -1767272577989225979L;
/*     */   final transient Logger logger;
/*     */   
/*     */   JdkLogger(Logger logger)
/*     */   {
/*  57 */     super(logger.getName());
/*  58 */     this.logger = logger;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTraceEnabled()
/*     */   {
/*  68 */     return this.logger.isLoggable(Level.FINEST);
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
/*  79 */     if (this.logger.isLoggable(Level.FINEST)) {
/*  80 */       log(SELF, Level.FINEST, msg, null);
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
/*     */   public void trace(String format, Object arg)
/*     */   {
/* 100 */     if (this.logger.isLoggable(Level.FINEST)) {
/* 101 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 102 */       log(SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
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
/* 124 */     if (this.logger.isLoggable(Level.FINEST)) {
/* 125 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 126 */       log(SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
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
/*     */   public void trace(String format, Object... argArray)
/*     */   {
/* 146 */     if (this.logger.isLoggable(Level.FINEST)) {
/* 147 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
/* 148 */       log(SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
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
/*     */   public void trace(String msg, Throwable t)
/*     */   {
/* 162 */     if (this.logger.isLoggable(Level.FINEST)) {
/* 163 */       log(SELF, Level.FINEST, msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDebugEnabled()
/*     */   {
/* 174 */     return this.logger.isLoggable(Level.FINE);
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
/* 185 */     if (this.logger.isLoggable(Level.FINE)) {
/* 186 */       log(SELF, Level.FINE, msg, null);
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
/*     */   public void debug(String format, Object arg)
/*     */   {
/* 205 */     if (this.logger.isLoggable(Level.FINE)) {
/* 206 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 207 */       log(SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
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
/* 229 */     if (this.logger.isLoggable(Level.FINE)) {
/* 230 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 231 */       log(SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
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
/*     */   public void debug(String format, Object... argArray)
/*     */   {
/* 251 */     if (this.logger.isLoggable(Level.FINE)) {
/* 252 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
/* 253 */       log(SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
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
/* 267 */     if (this.logger.isLoggable(Level.FINE)) {
/* 268 */       log(SELF, Level.FINE, msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInfoEnabled()
/*     */   {
/* 279 */     return this.logger.isLoggable(Level.INFO);
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
/* 290 */     if (this.logger.isLoggable(Level.INFO)) {
/* 291 */       log(SELF, Level.INFO, msg, null);
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
/*     */   public void info(String format, Object arg)
/*     */   {
/* 310 */     if (this.logger.isLoggable(Level.INFO)) {
/* 311 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 312 */       log(SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
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
/* 334 */     if (this.logger.isLoggable(Level.INFO)) {
/* 335 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 336 */       log(SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
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
/* 356 */     if (this.logger.isLoggable(Level.INFO)) {
/* 357 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
/* 358 */       log(SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
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
/* 373 */     if (this.logger.isLoggable(Level.INFO)) {
/* 374 */       log(SELF, Level.INFO, msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWarnEnabled()
/*     */   {
/* 386 */     return this.logger.isLoggable(Level.WARNING);
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
/* 397 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 398 */       log(SELF, Level.WARNING, msg, null);
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
/*     */   public void warn(String format, Object arg)
/*     */   {
/* 418 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 419 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 420 */       log(SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
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
/* 442 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 443 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 444 */       log(SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
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
/* 464 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 465 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
/* 466 */       log(SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
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
/* 481 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 482 */       log(SELF, Level.WARNING, msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isErrorEnabled()
/*     */   {
/* 493 */     return this.logger.isLoggable(Level.SEVERE);
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
/* 504 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 505 */       log(SELF, Level.SEVERE, msg, null);
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
/*     */   public void error(String format, Object arg)
/*     */   {
/* 525 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 526 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 527 */       log(SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
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
/* 549 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 550 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 551 */       log(SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
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
/*     */   public void error(String format, Object... arguments)
/*     */   {
/* 571 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 572 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 573 */       log(SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
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
/* 588 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 589 */       log(SELF, Level.SEVERE, msg, t);
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
/*     */   private void log(String callerFQCN, Level level, String msg, Throwable t)
/*     */   {
/* 602 */     LogRecord record = new LogRecord(level, msg);
/* 603 */     record.setLoggerName(name());
/* 604 */     record.setThrown(t);
/* 605 */     fillCallerData(callerFQCN, record);
/* 606 */     this.logger.log(record);
/*     */   }
/*     */   
/* 609 */   static final String SELF = JdkLogger.class.getName();
/* 610 */   static final String SUPER = AbstractInternalLogger.class.getName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void fillCallerData(String callerFQCN, LogRecord record)
/*     */   {
/* 619 */     StackTraceElement[] steArray = new Throwable().getStackTrace();
/*     */     
/* 621 */     int selfIndex = -1;
/* 622 */     for (int i = 0; i < steArray.length; i++) {
/* 623 */       String className = steArray[i].getClassName();
/* 624 */       if ((className.equals(callerFQCN)) || (className.equals(SUPER))) {
/* 625 */         selfIndex = i;
/* 626 */         break;
/*     */       }
/*     */     }
/*     */     
/* 630 */     int found = -1;
/* 631 */     for (int i = selfIndex + 1; i < steArray.length; i++) {
/* 632 */       String className = steArray[i].getClassName();
/* 633 */       if ((!className.equals(callerFQCN)) && (!className.equals(SUPER))) {
/* 634 */         found = i;
/* 635 */         break;
/*     */       }
/*     */     }
/*     */     
/* 639 */     if (found != -1) {
/* 640 */       StackTraceElement ste = steArray[found];
/*     */       
/*     */ 
/* 643 */       record.setSourceClassName(ste.getClassName());
/* 644 */       record.setSourceMethodName(ste.getMethodName());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\logging\JdkLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */