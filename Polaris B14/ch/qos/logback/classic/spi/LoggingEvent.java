/*     */ package ch.qos.logback.classic.spi;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.util.LogbackMDCAdapter;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.slf4j.MDC;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.helpers.FormattingTuple;
/*     */ import org.slf4j.helpers.MessageFormatter;
/*     */ import org.slf4j.spi.MDCAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoggingEvent
/*     */   implements ILoggingEvent
/*     */ {
/*     */   transient String fqnOfLoggerClass;
/*     */   private String threadName;
/*     */   private String loggerName;
/*     */   private LoggerContext loggerContext;
/*     */   private LoggerContextVO loggerContextVO;
/*     */   private transient Level level;
/*     */   private String message;
/*     */   transient String formattedMessage;
/*     */   private transient Object[] argumentArray;
/*     */   private ThrowableProxy throwableProxy;
/*     */   private StackTraceElement[] callerDataArray;
/*     */   private Marker marker;
/*     */   private Map<String, String> mdcPropertyMap;
/*  94 */   private static final Map<String, String> CACHED_NULL_MAP = new HashMap();
/*     */   
/*     */ 
/*     */   private long timeStamp;
/*     */   
/*     */ 
/*     */ 
/*     */   public LoggingEvent() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public LoggingEvent(String fqcn, Logger logger, Level level, String message, Throwable throwable, Object[] argArray)
/*     */   {
/* 107 */     this.fqnOfLoggerClass = fqcn;
/* 108 */     this.loggerName = logger.getName();
/* 109 */     this.loggerContext = logger.getLoggerContext();
/* 110 */     this.loggerContextVO = this.loggerContext.getLoggerContextRemoteView();
/* 111 */     this.level = level;
/*     */     
/* 113 */     this.message = message;
/* 114 */     this.argumentArray = argArray;
/*     */     
/* 116 */     if (throwable == null) {
/* 117 */       throwable = extractThrowableAnRearrangeArguments(argArray);
/*     */     }
/*     */     
/* 120 */     if (throwable != null) {
/* 121 */       this.throwableProxy = new ThrowableProxy(throwable);
/* 122 */       LoggerContext lc = logger.getLoggerContext();
/* 123 */       if (lc.isPackagingDataEnabled()) {
/* 124 */         this.throwableProxy.calculatePackagingData();
/*     */       }
/*     */     }
/*     */     
/* 128 */     this.timeStamp = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   private Throwable extractThrowableAnRearrangeArguments(Object[] argArray) {
/* 132 */     Throwable extractedThrowable = EventArgUtil.extractThrowable(argArray);
/* 133 */     if (EventArgUtil.successfulExtraction(extractedThrowable)) {
/* 134 */       this.argumentArray = EventArgUtil.trimmedCopy(argArray);
/*     */     }
/* 136 */     return extractedThrowable;
/*     */   }
/*     */   
/*     */   public void setArgumentArray(Object[] argArray) {
/* 140 */     if (this.argumentArray != null) {
/* 141 */       throw new IllegalStateException("argArray has been already set");
/*     */     }
/* 143 */     this.argumentArray = argArray;
/*     */   }
/*     */   
/*     */   public Object[] getArgumentArray() {
/* 147 */     return this.argumentArray;
/*     */   }
/*     */   
/*     */   public Level getLevel() {
/* 151 */     return this.level;
/*     */   }
/*     */   
/*     */   public String getLoggerName() {
/* 155 */     return this.loggerName;
/*     */   }
/*     */   
/*     */   public void setLoggerName(String loggerName) {
/* 159 */     this.loggerName = loggerName;
/*     */   }
/*     */   
/*     */   public String getThreadName() {
/* 163 */     if (this.threadName == null) {
/* 164 */       this.threadName = Thread.currentThread().getName();
/*     */     }
/* 166 */     return this.threadName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setThreadName(String threadName)
/*     */     throws IllegalStateException
/*     */   {
/* 174 */     if (this.threadName != null) {
/* 175 */       throw new IllegalStateException("threadName has been already set");
/*     */     }
/* 177 */     this.threadName = threadName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IThrowableProxy getThrowableProxy()
/*     */   {
/* 185 */     return this.throwableProxy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setThrowableProxy(ThrowableProxy tp)
/*     */   {
/* 192 */     if (this.throwableProxy != null) {
/* 193 */       throw new IllegalStateException("ThrowableProxy has been already set.");
/*     */     }
/* 195 */     this.throwableProxy = tp;
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
/*     */   public void prepareForDeferredProcessing()
/*     */   {
/* 208 */     getFormattedMessage();
/* 209 */     getThreadName();
/*     */     
/* 211 */     getMDCPropertyMap();
/*     */   }
/*     */   
/*     */   public LoggerContextVO getLoggerContextVO() {
/* 215 */     return this.loggerContextVO;
/*     */   }
/*     */   
/*     */   public void setLoggerContextRemoteView(LoggerContextVO loggerContextVO) {
/* 219 */     this.loggerContextVO = loggerContextVO;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/* 223 */     return this.message;
/*     */   }
/*     */   
/*     */   public void setMessage(String message) {
/* 227 */     if (this.message != null) {
/* 228 */       throw new IllegalStateException("The message for this event has been set already.");
/*     */     }
/*     */     
/* 231 */     this.message = message;
/*     */   }
/*     */   
/*     */   public long getTimeStamp() {
/* 235 */     return this.timeStamp;
/*     */   }
/*     */   
/*     */   public void setTimeStamp(long timeStamp) {
/* 239 */     this.timeStamp = timeStamp;
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/* 243 */     if (this.level != null) {
/* 244 */       throw new IllegalStateException("The level has been already set for this event.");
/*     */     }
/*     */     
/* 247 */     this.level = level;
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
/*     */   public StackTraceElement[] getCallerData()
/*     */   {
/* 261 */     if (this.callerDataArray == null) {
/* 262 */       this.callerDataArray = CallerData.extract(new Throwable(), this.fqnOfLoggerClass, this.loggerContext.getMaxCallerDataDepth(), this.loggerContext.getFrameworkPackages());
/*     */     }
/*     */     
/* 265 */     return this.callerDataArray;
/*     */   }
/*     */   
/*     */   public boolean hasCallerData() {
/* 269 */     return this.callerDataArray != null;
/*     */   }
/*     */   
/*     */   public void setCallerData(StackTraceElement[] callerDataArray) {
/* 273 */     this.callerDataArray = callerDataArray;
/*     */   }
/*     */   
/*     */   public Marker getMarker() {
/* 277 */     return this.marker;
/*     */   }
/*     */   
/*     */   public void setMarker(Marker marker) {
/* 281 */     if (this.marker != null) {
/* 282 */       throw new IllegalStateException("The marker has been already set for this event.");
/*     */     }
/*     */     
/* 285 */     this.marker = marker;
/*     */   }
/*     */   
/*     */   public long getContextBirthTime() {
/* 289 */     return this.loggerContextVO.getBirthTime();
/*     */   }
/*     */   
/*     */   public String getFormattedMessage()
/*     */   {
/* 294 */     if (this.formattedMessage != null) {
/* 295 */       return this.formattedMessage;
/*     */     }
/* 297 */     if (this.argumentArray != null) {
/* 298 */       this.formattedMessage = MessageFormatter.arrayFormat(this.message, this.argumentArray).getMessage();
/*     */     }
/*     */     else {
/* 301 */       this.formattedMessage = this.message;
/*     */     }
/*     */     
/* 304 */     return this.formattedMessage;
/*     */   }
/*     */   
/*     */   public Map<String, String> getMDCPropertyMap()
/*     */   {
/* 309 */     if (this.mdcPropertyMap == null) {
/* 310 */       MDCAdapter mdc = MDC.getMDCAdapter();
/* 311 */       if ((mdc instanceof LogbackMDCAdapter)) {
/* 312 */         this.mdcPropertyMap = ((LogbackMDCAdapter)mdc).getPropertyMap();
/*     */       } else {
/* 314 */         this.mdcPropertyMap = mdc.getCopyOfContextMap();
/*     */       }
/*     */     }
/* 317 */     if (this.mdcPropertyMap == null) {
/* 318 */       this.mdcPropertyMap = CACHED_NULL_MAP;
/*     */     }
/* 320 */     return this.mdcPropertyMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMDCPropertyMap(Map<String, String> map)
/*     */   {
/* 330 */     if (this.mdcPropertyMap != null) {
/* 331 */       throw new IllegalStateException("The MDCPropertyMap has been already set for this event.");
/*     */     }
/*     */     
/* 334 */     this.mdcPropertyMap = map;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public Map<String, String> getMdc()
/*     */   {
/* 344 */     return getMDCPropertyMap();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 349 */     StringBuilder sb = new StringBuilder();
/* 350 */     sb.append('[');
/* 351 */     sb.append(this.level).append("] ");
/* 352 */     sb.append(getFormattedMessage());
/* 353 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void writeObject(ObjectOutputStream out)
/*     */     throws IOException
/*     */   {
/* 363 */     throw new UnsupportedOperationException(getClass() + " does not support serialization. " + "Use LoggerEventVO instance instead. See also LoggerEventVO.build method.");
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\LoggingEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */