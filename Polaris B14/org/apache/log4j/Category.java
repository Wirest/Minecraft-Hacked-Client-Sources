/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import org.apache.log4j.helpers.NullEnumeration;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.MarkerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Category
/*     */ {
/*  46 */   private static final String CATEGORY_FQCN = Category.class.getName();
/*     */   
/*     */   private String name;
/*     */   
/*     */   protected Logger slf4jLogger;
/*     */   
/*     */   private LocationAwareLogger locationAwareLogger;
/*  53 */   private static Marker FATAL_MARKER = MarkerFactory.getMarker("FATAL");
/*     */   
/*     */   Category(String name) {
/*  56 */     this.name = name;
/*  57 */     this.slf4jLogger = LoggerFactory.getLogger(name);
/*  58 */     if ((this.slf4jLogger instanceof LocationAwareLogger)) {
/*  59 */       this.locationAwareLogger = ((LocationAwareLogger)this.slf4jLogger);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Category getInstance(Class clazz) {
/*  64 */     return Log4jLoggerFactory.getLogger(clazz.getName());
/*     */   }
/*     */   
/*     */   public static Category getInstance(String name) {
/*  68 */     return Log4jLoggerFactory.getLogger(name);
/*     */   }
/*     */   
/*     */   public final Category getParent() {
/*  72 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  81 */     return this.name;
/*     */   }
/*     */   
/*     */   public Appender getAppender(String name) {
/*  85 */     return null;
/*     */   }
/*     */   
/*     */   public Enumeration getAllAppenders() {
/*  89 */     return NullEnumeration.getInstance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Level getEffectiveLevel()
/*     */   {
/* 101 */     if (this.slf4jLogger.isTraceEnabled()) {
/* 102 */       return Level.TRACE;
/*     */     }
/* 104 */     if (this.slf4jLogger.isDebugEnabled()) {
/* 105 */       return Level.DEBUG;
/*     */     }
/* 107 */     if (this.slf4jLogger.isInfoEnabled()) {
/* 108 */       return Level.INFO;
/*     */     }
/* 110 */     if (this.slf4jLogger.isWarnEnabled()) {
/* 111 */       return Level.WARN;
/*     */     }
/* 113 */     return Level.ERROR;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Level getLevel()
/*     */   {
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public final Level getPriority() {
/* 130 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isDebugEnabled()
/*     */   {
/* 137 */     return this.slf4jLogger.isDebugEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isInfoEnabled()
/*     */   {
/* 144 */     return this.slf4jLogger.isInfoEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isWarnEnabled()
/*     */   {
/* 151 */     return this.slf4jLogger.isWarnEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isErrorEnabled()
/*     */   {
/* 158 */     return this.slf4jLogger.isErrorEnabled();
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
/*     */   public boolean isEnabledFor(Priority p)
/*     */   {
/* 172 */     switch (p.level) {
/*     */     case 5000: 
/* 174 */       return this.slf4jLogger.isTraceEnabled();
/*     */     case 10000: 
/* 176 */       return this.slf4jLogger.isDebugEnabled();
/*     */     case 20000: 
/* 178 */       return this.slf4jLogger.isInfoEnabled();
/*     */     case 30000: 
/* 180 */       return this.slf4jLogger.isWarnEnabled();
/*     */     case 40000: 
/* 182 */       return this.slf4jLogger.isErrorEnabled();
/*     */     case 50000: 
/* 184 */       return this.slf4jLogger.isErrorEnabled();
/*     */     }
/* 186 */     return false;
/*     */   }
/*     */   
/*     */   void differentiatedLog(Marker marker, String fqcn, int level, Object message, Throwable t)
/*     */   {
/* 191 */     String m = convertToString(message);
/* 192 */     if (this.locationAwareLogger != null) {
/* 193 */       switch (level) {
/*     */       case 0: 
/* 195 */         this.slf4jLogger.trace(marker, m);
/* 196 */         break;
/*     */       case 10: 
/* 198 */         this.slf4jLogger.debug(marker, m);
/* 199 */         break;
/*     */       case 20: 
/* 201 */         this.slf4jLogger.info(marker, m);
/* 202 */         break;
/*     */       case 30: 
/* 204 */         this.slf4jLogger.warn(marker, m);
/* 205 */         break;
/*     */       case 40: 
/* 207 */         this.slf4jLogger.error(marker, m);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void debug(Object message)
/*     */   {
/* 217 */     differentiatedLog(null, CATEGORY_FQCN, 10, message, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void debug(Object message, Throwable t)
/*     */   {
/* 225 */     differentiatedLog(null, CATEGORY_FQCN, 10, message, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void info(Object message)
/*     */   {
/* 232 */     differentiatedLog(null, CATEGORY_FQCN, 20, message, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void info(Object message, Throwable t)
/*     */   {
/* 240 */     differentiatedLog(null, CATEGORY_FQCN, 20, message, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void warn(Object message)
/*     */   {
/* 247 */     differentiatedLog(null, CATEGORY_FQCN, 30, message, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void warn(Object message, Throwable t)
/*     */   {
/* 255 */     differentiatedLog(null, CATEGORY_FQCN, 30, message, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void error(Object message)
/*     */   {
/* 262 */     differentiatedLog(null, CATEGORY_FQCN, 40, message, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void error(Object message, Throwable t)
/*     */   {
/* 270 */     differentiatedLog(null, CATEGORY_FQCN, 40, message, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void fatal(Object message)
/*     */   {
/* 277 */     differentiatedLog(FATAL_MARKER, CATEGORY_FQCN, 40, message, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fatal(Object message, Throwable t)
/*     */   {
/* 285 */     differentiatedLog(FATAL_MARKER, CATEGORY_FQCN, 40, message, t);
/*     */   }
/*     */   
/*     */   protected void forcedLog(String FQCN, Priority p, Object msg, Throwable t) {
/* 289 */     log(FQCN, p, msg, t);
/*     */   }
/*     */   
/*     */   public void log(String FQCN, Priority p, Object msg, Throwable t)
/*     */   {
/* 294 */     int levelInt = priorityToLevelInt(p);
/* 295 */     differentiatedLog(null, FQCN, levelInt, msg, t);
/*     */   }
/*     */   
/*     */   public void log(Priority p, Object message, Throwable t) {
/* 299 */     int levelInt = priorityToLevelInt(p);
/* 300 */     differentiatedLog(null, CATEGORY_FQCN, levelInt, message, t);
/*     */   }
/*     */   
/*     */   public void log(Priority p, Object message) {
/* 304 */     int levelInt = priorityToLevelInt(p);
/* 305 */     differentiatedLog(null, CATEGORY_FQCN, levelInt, message, null);
/*     */   }
/*     */   
/*     */   private int priorityToLevelInt(Priority p) {
/* 309 */     switch (p.level) {
/*     */     case 5000: 
/*     */     case 9900: 
/* 312 */       return 0;
/*     */     case 10000: 
/* 314 */       return 10;
/*     */     case 20000: 
/* 316 */       return 20;
/*     */     case 30000: 
/* 318 */       return 30;
/*     */     case 40000: 
/* 320 */       return 40;
/*     */     case 50000: 
/* 322 */       return 40;
/*     */     }
/* 324 */     throw new IllegalStateException("Unknown Priority " + p);
/*     */   }
/*     */   
/*     */   protected final String convertToString(Object message)
/*     */   {
/* 329 */     if (message == null) {
/* 330 */       return (String)message;
/*     */     }
/* 332 */     return message.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setAdditivity(boolean additive) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void addAppender(Appender newAppender) {}
/*     */   
/*     */ 
/*     */   public void setLevel(Level level) {}
/*     */   
/*     */ 
/*     */   public boolean getAdditivity()
/*     */   {
/* 349 */     return false;
/*     */   }
/*     */   
/*     */   public void assertLog(boolean assertion, String msg) {
/* 353 */     if (!assertion) {
/* 354 */       error(msg);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\apache\log4j\Category.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */