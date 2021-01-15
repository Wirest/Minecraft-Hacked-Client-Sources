/*     */ package ch.qos.logback.classic;
/*     */ 
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.classic.spi.LoggingEvent;
/*     */ import ch.qos.logback.classic.util.LoggerNameUtil;
/*     */ import ch.qos.logback.core.Appender;
/*     */ import ch.qos.logback.core.spi.AppenderAttachable;
/*     */ import ch.qos.logback.core.spi.AppenderAttachableImpl;
/*     */ import ch.qos.logback.core.spi.FilterReply;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.spi.LocationAwareLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Logger
/*     */   implements org.slf4j.Logger, LocationAwareLogger, AppenderAttachable<ILoggingEvent>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5454405123156820674L;
/*  45 */   public static final String FQCN = Logger.class.getName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String name;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private transient Level level;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private transient int effectiveLevelInt;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private transient Logger parent;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private transient List<Logger> childrenList;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private transient AppenderAttachableImpl<ILoggingEvent> aai;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 100 */   private transient boolean additive = true;
/*     */   final transient LoggerContext loggerContext;
/*     */   private static final int DEFAULT_CHILD_ARRAY_SIZE = 5;
/*     */   
/*     */   Logger(String name, Logger parent, LoggerContext loggerContext) {
/* 105 */     this.name = name;
/* 106 */     this.parent = parent;
/* 107 */     this.loggerContext = loggerContext;
/*     */   }
/*     */   
/*     */   public Level getEffectiveLevel() {
/* 111 */     return Level.toLevel(this.effectiveLevelInt);
/*     */   }
/*     */   
/*     */   int getEffectiveLevelInt() {
/* 115 */     return this.effectiveLevelInt;
/*     */   }
/*     */   
/*     */   public Level getLevel() {
/* 119 */     return this.level;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 123 */     return this.name;
/*     */   }
/*     */   
/*     */   private boolean isRootLogger()
/*     */   {
/* 128 */     return this.parent == null;
/*     */   }
/*     */   
/*     */   Logger getChildByName(String childName) {
/* 132 */     if (this.childrenList == null) {
/* 133 */       return null;
/*     */     }
/* 135 */     int len = this.childrenList.size();
/* 136 */     for (int i = 0; i < len; i++) {
/* 137 */       Logger childLogger_i = (Logger)this.childrenList.get(i);
/* 138 */       String childName_i = childLogger_i.getName();
/*     */       
/* 140 */       if (childName.equals(childName_i)) {
/* 141 */         return childLogger_i;
/*     */       }
/*     */     }
/*     */     
/* 145 */     return null;
/*     */   }
/*     */   
/*     */   public synchronized void setLevel(Level newLevel)
/*     */   {
/* 150 */     if (this.level == newLevel)
/*     */     {
/* 152 */       return;
/*     */     }
/* 154 */     if ((newLevel == null) && (isRootLogger())) {
/* 155 */       throw new IllegalArgumentException("The level of the root logger cannot be set to null");
/*     */     }
/*     */     
/*     */ 
/* 159 */     this.level = newLevel;
/* 160 */     if (newLevel == null) {
/* 161 */       this.effectiveLevelInt = this.parent.effectiveLevelInt;
/* 162 */       newLevel = this.parent.getEffectiveLevel();
/*     */     } else {
/* 164 */       this.effectiveLevelInt = newLevel.levelInt;
/*     */     }
/*     */     
/* 167 */     if (this.childrenList != null) {
/* 168 */       int len = this.childrenList.size();
/* 169 */       for (int i = 0; i < len; i++) {
/* 170 */         Logger child = (Logger)this.childrenList.get(i);
/*     */         
/* 172 */         child.handleParentLevelChange(this.effectiveLevelInt);
/*     */       }
/*     */     }
/*     */     
/* 176 */     this.loggerContext.fireOnLevelChange(this, newLevel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized void handleParentLevelChange(int newParentLevelInt)
/*     */   {
/* 188 */     if (this.level == null) {
/* 189 */       this.effectiveLevelInt = newParentLevelInt;
/*     */       
/*     */ 
/* 192 */       if (this.childrenList != null) {
/* 193 */         int len = this.childrenList.size();
/* 194 */         for (int i = 0; i < len; i++) {
/* 195 */           Logger child = (Logger)this.childrenList.get(i);
/* 196 */           child.handleParentLevelChange(newParentLevelInt);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void detachAndStopAllAppenders()
/*     */   {
/* 208 */     if (this.aai != null) {
/* 209 */       this.aai.detachAndStopAllAppenders();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean detachAppender(String name) {
/* 214 */     if (this.aai == null) {
/* 215 */       return false;
/*     */     }
/* 217 */     return this.aai.detachAppender(name);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void addAppender(Appender<ILoggingEvent> newAppender)
/*     */   {
/* 223 */     if (this.aai == null) {
/* 224 */       this.aai = new AppenderAttachableImpl();
/*     */     }
/* 226 */     this.aai.addAppender(newAppender);
/*     */   }
/*     */   
/*     */   public boolean isAttached(Appender<ILoggingEvent> appender) {
/* 230 */     if (this.aai == null) {
/* 231 */       return false;
/*     */     }
/* 233 */     return this.aai.isAttached(appender);
/*     */   }
/*     */   
/*     */   public Iterator<Appender<ILoggingEvent>> iteratorForAppenders()
/*     */   {
/* 238 */     if (this.aai == null) {
/* 239 */       return Collections.EMPTY_LIST.iterator();
/*     */     }
/* 241 */     return this.aai.iteratorForAppenders();
/*     */   }
/*     */   
/*     */   public Appender<ILoggingEvent> getAppender(String name) {
/* 245 */     if (this.aai == null) {
/* 246 */       return null;
/*     */     }
/* 248 */     return this.aai.getAppender(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void callAppenders(ILoggingEvent event)
/*     */   {
/* 258 */     int writes = 0;
/* 259 */     for (Logger l = this; l != null; l = l.parent) {
/* 260 */       writes += l.appendLoopOnAppenders(event);
/* 261 */       if (!l.additive) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/* 266 */     if (writes == 0) {
/* 267 */       this.loggerContext.noAppenderDefinedWarning(this);
/*     */     }
/*     */   }
/*     */   
/*     */   private int appendLoopOnAppenders(ILoggingEvent event) {
/* 272 */     if (this.aai != null) {
/* 273 */       return this.aai.appendLoopOnAppenders(event);
/*     */     }
/* 275 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean detachAppender(Appender<ILoggingEvent> appender)
/*     */   {
/* 283 */     if (this.aai == null) {
/* 284 */       return false;
/*     */     }
/* 286 */     return this.aai.detachAppender(appender);
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
/*     */   Logger createChildByLastNamePart(String lastPart)
/*     */   {
/* 307 */     int i_index = LoggerNameUtil.getFirstSeparatorIndexOf(lastPart);
/* 308 */     if (i_index != -1) {
/* 309 */       throw new IllegalArgumentException("Child name [" + lastPart + " passed as parameter, may not include [" + '.' + "]");
/*     */     }
/*     */     
/*     */ 
/* 313 */     if (this.childrenList == null)
/* 314 */       this.childrenList = new ArrayList();
/*     */     Logger childLogger;
/*     */     Logger childLogger;
/* 317 */     if (isRootLogger()) {
/* 318 */       childLogger = new Logger(lastPart, this, this.loggerContext);
/*     */     } else {
/* 320 */       childLogger = new Logger(this.name + '.' + lastPart, this, this.loggerContext);
/*     */     }
/*     */     
/* 323 */     this.childrenList.add(childLogger);
/* 324 */     childLogger.effectiveLevelInt = this.effectiveLevelInt;
/* 325 */     return childLogger;
/*     */   }
/*     */   
/*     */   private void localLevelReset() {
/* 329 */     this.effectiveLevelInt = 10000;
/* 330 */     if (isRootLogger()) {
/* 331 */       this.level = Level.DEBUG;
/*     */     } else {
/* 333 */       this.level = null;
/*     */     }
/*     */   }
/*     */   
/*     */   void recursiveReset() {
/* 338 */     detachAndStopAllAppenders();
/* 339 */     localLevelReset();
/* 340 */     this.additive = true;
/* 341 */     if (this.childrenList == null) {
/* 342 */       return;
/*     */     }
/* 344 */     for (Logger childLogger : this.childrenList) {
/* 345 */       childLogger.recursiveReset();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Logger createChildByName(String childName)
/*     */   {
/* 356 */     int i_index = LoggerNameUtil.getSeparatorIndexOf(childName, this.name.length() + 1);
/* 357 */     if (i_index != -1) {
/* 358 */       throw new IllegalArgumentException("For logger [" + this.name + "] child name [" + childName + " passed as parameter, may not include '.' after index" + (this.name.length() + 1));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 364 */     if (this.childrenList == null) {
/* 365 */       this.childrenList = new ArrayList(5);
/*     */     }
/*     */     
/* 368 */     Logger childLogger = new Logger(childName, this, this.loggerContext);
/* 369 */     this.childrenList.add(childLogger);
/* 370 */     childLogger.effectiveLevelInt = this.effectiveLevelInt;
/* 371 */     return childLogger;
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
/*     */   private void filterAndLog_0_Or3Plus(String localFQCN, Marker marker, Level level, String msg, Object[] params, Throwable t)
/*     */   {
/* 384 */     FilterReply decision = this.loggerContext.getTurboFilterChainDecision_0_3OrMore(marker, this, level, msg, params, t);
/*     */     
/*     */ 
/*     */ 
/* 388 */     if (decision == FilterReply.NEUTRAL) {
/* 389 */       if (this.effectiveLevelInt <= level.levelInt) {}
/*     */ 
/*     */     }
/* 392 */     else if (decision == FilterReply.DENY) {
/* 393 */       return;
/*     */     }
/*     */     
/* 396 */     buildLoggingEventAndAppend(localFQCN, marker, level, msg, params, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void filterAndLog_1(String localFQCN, Marker marker, Level level, String msg, Object param, Throwable t)
/*     */   {
/* 403 */     FilterReply decision = this.loggerContext.getTurboFilterChainDecision_1(marker, this, level, msg, param, t);
/*     */     
/*     */ 
/* 406 */     if (decision == FilterReply.NEUTRAL) {
/* 407 */       if (this.effectiveLevelInt <= level.levelInt) {}
/*     */ 
/*     */     }
/* 410 */     else if (decision == FilterReply.DENY) {
/* 411 */       return;
/*     */     }
/*     */     
/* 414 */     buildLoggingEventAndAppend(localFQCN, marker, level, msg, new Object[] { param }, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void filterAndLog_2(String localFQCN, Marker marker, Level level, String msg, Object param1, Object param2, Throwable t)
/*     */   {
/* 422 */     FilterReply decision = this.loggerContext.getTurboFilterChainDecision_2(marker, this, level, msg, param1, param2, t);
/*     */     
/*     */ 
/* 425 */     if (decision == FilterReply.NEUTRAL) {
/* 426 */       if (this.effectiveLevelInt <= level.levelInt) {}
/*     */ 
/*     */     }
/* 429 */     else if (decision == FilterReply.DENY) {
/* 430 */       return;
/*     */     }
/*     */     
/* 433 */     buildLoggingEventAndAppend(localFQCN, marker, level, msg, new Object[] { param1, param2 }, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void buildLoggingEventAndAppend(String localFQCN, Marker marker, Level level, String msg, Object[] params, Throwable t)
/*     */   {
/* 440 */     LoggingEvent le = new LoggingEvent(localFQCN, this, level, msg, t, params);
/* 441 */     le.setMarker(marker);
/* 442 */     callAppenders(le);
/*     */   }
/*     */   
/*     */   public void trace(String msg) {
/* 446 */     filterAndLog_0_Or3Plus(FQCN, null, Level.TRACE, msg, null, null);
/*     */   }
/*     */   
/*     */   public void trace(String format, Object arg) {
/* 450 */     filterAndLog_1(FQCN, null, Level.TRACE, format, arg, null);
/*     */   }
/*     */   
/*     */   public void trace(String format, Object arg1, Object arg2) {
/* 454 */     filterAndLog_2(FQCN, null, Level.TRACE, format, arg1, arg2, null);
/*     */   }
/*     */   
/*     */   public void trace(String format, Object[] argArray) {
/* 458 */     filterAndLog_0_Or3Plus(FQCN, null, Level.TRACE, format, argArray, null);
/*     */   }
/*     */   
/*     */   public void trace(String msg, Throwable t) {
/* 462 */     filterAndLog_0_Or3Plus(FQCN, null, Level.TRACE, msg, null, t);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String msg) {
/* 466 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.TRACE, msg, null, null);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String format, Object arg) {
/* 470 */     filterAndLog_1(FQCN, marker, Level.TRACE, format, arg, null);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String format, Object arg1, Object arg2) {
/* 474 */     filterAndLog_2(FQCN, marker, Level.TRACE, format, arg1, arg2, null);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String format, Object[] argArray) {
/* 478 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.TRACE, format, argArray, null);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String msg, Throwable t) {
/* 482 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.TRACE, msg, null, t);
/*     */   }
/*     */   
/*     */   public boolean isDebugEnabled() {
/* 486 */     return isDebugEnabled(null);
/*     */   }
/*     */   
/*     */   public boolean isDebugEnabled(Marker marker) {
/* 490 */     FilterReply decision = callTurboFilters(marker, Level.DEBUG);
/* 491 */     if (decision == FilterReply.NEUTRAL)
/* 492 */       return this.effectiveLevelInt <= 10000;
/* 493 */     if (decision == FilterReply.DENY)
/* 494 */       return false;
/* 495 */     if (decision == FilterReply.ACCEPT) {
/* 496 */       return true;
/*     */     }
/* 498 */     throw new IllegalStateException("Unknown FilterReply value: " + decision);
/*     */   }
/*     */   
/*     */   public void debug(String msg)
/*     */   {
/* 503 */     filterAndLog_0_Or3Plus(FQCN, null, Level.DEBUG, msg, null, null);
/*     */   }
/*     */   
/*     */   public void debug(String format, Object arg) {
/* 507 */     filterAndLog_1(FQCN, null, Level.DEBUG, format, arg, null);
/*     */   }
/*     */   
/*     */   public void debug(String format, Object arg1, Object arg2) {
/* 511 */     filterAndLog_2(FQCN, null, Level.DEBUG, format, arg1, arg2, null);
/*     */   }
/*     */   
/*     */   public void debug(String format, Object[] argArray) {
/* 515 */     filterAndLog_0_Or3Plus(FQCN, null, Level.DEBUG, format, argArray, null);
/*     */   }
/*     */   
/*     */   public void debug(String msg, Throwable t) {
/* 519 */     filterAndLog_0_Or3Plus(FQCN, null, Level.DEBUG, msg, null, t);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String msg) {
/* 523 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.DEBUG, msg, null, null);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String format, Object arg) {
/* 527 */     filterAndLog_1(FQCN, marker, Level.DEBUG, format, arg, null);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String format, Object arg1, Object arg2) {
/* 531 */     filterAndLog_2(FQCN, marker, Level.DEBUG, format, arg1, arg2, null);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String format, Object[] argArray) {
/* 535 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.DEBUG, format, argArray, null);
/*     */   }
/*     */   
/*     */   public void debug(Marker marker, String msg, Throwable t) {
/* 539 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.DEBUG, msg, null, t);
/*     */   }
/*     */   
/*     */   public void error(String msg) {
/* 543 */     filterAndLog_0_Or3Plus(FQCN, null, Level.ERROR, msg, null, null);
/*     */   }
/*     */   
/*     */   public void error(String format, Object arg) {
/* 547 */     filterAndLog_1(FQCN, null, Level.ERROR, format, arg, null);
/*     */   }
/*     */   
/*     */   public void error(String format, Object arg1, Object arg2) {
/* 551 */     filterAndLog_2(FQCN, null, Level.ERROR, format, arg1, arg2, null);
/*     */   }
/*     */   
/*     */   public void error(String format, Object[] argArray) {
/* 555 */     filterAndLog_0_Or3Plus(FQCN, null, Level.ERROR, format, argArray, null);
/*     */   }
/*     */   
/*     */   public void error(String msg, Throwable t) {
/* 559 */     filterAndLog_0_Or3Plus(FQCN, null, Level.ERROR, msg, null, t);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String msg) {
/* 563 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.ERROR, msg, null, null);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String format, Object arg) {
/* 567 */     filterAndLog_1(FQCN, marker, Level.ERROR, format, arg, null);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String format, Object arg1, Object arg2) {
/* 571 */     filterAndLog_2(FQCN, marker, Level.ERROR, format, arg1, arg2, null);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String format, Object[] argArray) {
/* 575 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.ERROR, format, argArray, null);
/*     */   }
/*     */   
/*     */   public void error(Marker marker, String msg, Throwable t) {
/* 579 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.ERROR, msg, null, t);
/*     */   }
/*     */   
/*     */   public boolean isInfoEnabled() {
/* 583 */     return isInfoEnabled(null);
/*     */   }
/*     */   
/*     */   public boolean isInfoEnabled(Marker marker) {
/* 587 */     FilterReply decision = callTurboFilters(marker, Level.INFO);
/* 588 */     if (decision == FilterReply.NEUTRAL)
/* 589 */       return this.effectiveLevelInt <= 20000;
/* 590 */     if (decision == FilterReply.DENY)
/* 591 */       return false;
/* 592 */     if (decision == FilterReply.ACCEPT) {
/* 593 */       return true;
/*     */     }
/* 595 */     throw new IllegalStateException("Unknown FilterReply value: " + decision);
/*     */   }
/*     */   
/*     */   public void info(String msg)
/*     */   {
/* 600 */     filterAndLog_0_Or3Plus(FQCN, null, Level.INFO, msg, null, null);
/*     */   }
/*     */   
/*     */   public void info(String format, Object arg) {
/* 604 */     filterAndLog_1(FQCN, null, Level.INFO, format, arg, null);
/*     */   }
/*     */   
/*     */   public void info(String format, Object arg1, Object arg2) {
/* 608 */     filterAndLog_2(FQCN, null, Level.INFO, format, arg1, arg2, null);
/*     */   }
/*     */   
/*     */   public void info(String format, Object[] argArray) {
/* 612 */     filterAndLog_0_Or3Plus(FQCN, null, Level.INFO, format, argArray, null);
/*     */   }
/*     */   
/*     */   public void info(String msg, Throwable t) {
/* 616 */     filterAndLog_0_Or3Plus(FQCN, null, Level.INFO, msg, null, t);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String msg) {
/* 620 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.INFO, msg, null, null);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String format, Object arg) {
/* 624 */     filterAndLog_1(FQCN, marker, Level.INFO, format, arg, null);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String format, Object arg1, Object arg2) {
/* 628 */     filterAndLog_2(FQCN, marker, Level.INFO, format, arg1, arg2, null);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String format, Object[] argArray) {
/* 632 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.INFO, format, argArray, null);
/*     */   }
/*     */   
/*     */   public void info(Marker marker, String msg, Throwable t) {
/* 636 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.INFO, msg, null, t);
/*     */   }
/*     */   
/*     */   public boolean isTraceEnabled() {
/* 640 */     return isTraceEnabled(null);
/*     */   }
/*     */   
/*     */   public boolean isTraceEnabled(Marker marker) {
/* 644 */     FilterReply decision = callTurboFilters(marker, Level.TRACE);
/* 645 */     if (decision == FilterReply.NEUTRAL)
/* 646 */       return this.effectiveLevelInt <= 5000;
/* 647 */     if (decision == FilterReply.DENY)
/* 648 */       return false;
/* 649 */     if (decision == FilterReply.ACCEPT) {
/* 650 */       return true;
/*     */     }
/* 652 */     throw new IllegalStateException("Unknown FilterReply value: " + decision);
/*     */   }
/*     */   
/*     */   public boolean isErrorEnabled()
/*     */   {
/* 657 */     return isErrorEnabled(null);
/*     */   }
/*     */   
/*     */   public boolean isErrorEnabled(Marker marker) {
/* 661 */     FilterReply decision = callTurboFilters(marker, Level.ERROR);
/* 662 */     if (decision == FilterReply.NEUTRAL)
/* 663 */       return this.effectiveLevelInt <= 40000;
/* 664 */     if (decision == FilterReply.DENY)
/* 665 */       return false;
/* 666 */     if (decision == FilterReply.ACCEPT) {
/* 667 */       return true;
/*     */     }
/* 669 */     throw new IllegalStateException("Unknown FilterReply value: " + decision);
/*     */   }
/*     */   
/*     */   public boolean isWarnEnabled()
/*     */   {
/* 674 */     return isWarnEnabled(null);
/*     */   }
/*     */   
/*     */   public boolean isWarnEnabled(Marker marker) {
/* 678 */     FilterReply decision = callTurboFilters(marker, Level.WARN);
/* 679 */     if (decision == FilterReply.NEUTRAL)
/* 680 */       return this.effectiveLevelInt <= 30000;
/* 681 */     if (decision == FilterReply.DENY)
/* 682 */       return false;
/* 683 */     if (decision == FilterReply.ACCEPT) {
/* 684 */       return true;
/*     */     }
/* 686 */     throw new IllegalStateException("Unknown FilterReply value: " + decision);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEnabledFor(Marker marker, Level level)
/*     */   {
/* 692 */     FilterReply decision = callTurboFilters(marker, level);
/* 693 */     if (decision == FilterReply.NEUTRAL)
/* 694 */       return this.effectiveLevelInt <= level.levelInt;
/* 695 */     if (decision == FilterReply.DENY)
/* 696 */       return false;
/* 697 */     if (decision == FilterReply.ACCEPT) {
/* 698 */       return true;
/*     */     }
/* 700 */     throw new IllegalStateException("Unknown FilterReply value: " + decision);
/*     */   }
/*     */   
/*     */   public boolean isEnabledFor(Level level)
/*     */   {
/* 705 */     return isEnabledFor(null, level);
/*     */   }
/*     */   
/*     */   public void warn(String msg) {
/* 709 */     filterAndLog_0_Or3Plus(FQCN, null, Level.WARN, msg, null, null);
/*     */   }
/*     */   
/*     */   public void warn(String msg, Throwable t) {
/* 713 */     filterAndLog_0_Or3Plus(FQCN, null, Level.WARN, msg, null, t);
/*     */   }
/*     */   
/*     */   public void warn(String format, Object arg) {
/* 717 */     filterAndLog_1(FQCN, null, Level.WARN, format, arg, null);
/*     */   }
/*     */   
/*     */   public void warn(String format, Object arg1, Object arg2) {
/* 721 */     filterAndLog_2(FQCN, null, Level.WARN, format, arg1, arg2, null);
/*     */   }
/*     */   
/*     */   public void warn(String format, Object[] argArray) {
/* 725 */     filterAndLog_0_Or3Plus(FQCN, null, Level.WARN, format, argArray, null);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String msg) {
/* 729 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.WARN, msg, null, null);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String format, Object arg) {
/* 733 */     filterAndLog_1(FQCN, marker, Level.WARN, format, arg, null);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String format, Object[] argArray) {
/* 737 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.WARN, format, argArray, null);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String format, Object arg1, Object arg2) {
/* 741 */     filterAndLog_2(FQCN, marker, Level.WARN, format, arg1, arg2, null);
/*     */   }
/*     */   
/*     */   public void warn(Marker marker, String msg, Throwable t) {
/* 745 */     filterAndLog_0_Or3Plus(FQCN, marker, Level.WARN, msg, null, t);
/*     */   }
/*     */   
/*     */   public boolean isAdditive() {
/* 749 */     return this.additive;
/*     */   }
/*     */   
/*     */   public void setAdditive(boolean additive) {
/* 753 */     this.additive = additive;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 757 */     return "Logger[" + this.name + "]";
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
/*     */   private FilterReply callTurboFilters(Marker marker, Level level)
/*     */   {
/* 772 */     return this.loggerContext.getTurboFilterChainDecision_0_3OrMore(marker, this, level, null, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LoggerContext getLoggerContext()
/*     */   {
/* 782 */     return this.loggerContext;
/*     */   }
/*     */   
/*     */   public void log(Marker marker, String fqcn, int levelInt, String message, Object[] argArray, Throwable t)
/*     */   {
/* 787 */     Level level = Level.fromLocationAwareLoggerInteger(levelInt);
/* 788 */     filterAndLog_0_Or3Plus(fqcn, marker, level, message, argArray, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object readResolve()
/*     */     throws ObjectStreamException
/*     */   {
/* 800 */     return LoggerFactory.getLogger(getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\Logger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */