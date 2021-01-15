/*     */ package ch.qos.logback.classic;
/*     */ 
/*     */ import ch.qos.logback.classic.spi.LoggerComparator;
/*     */ import ch.qos.logback.classic.spi.LoggerContextListener;
/*     */ import ch.qos.logback.classic.spi.LoggerContextVO;
/*     */ import ch.qos.logback.classic.spi.TurboFilterList;
/*     */ import ch.qos.logback.classic.turbo.TurboFilter;
/*     */ import ch.qos.logback.classic.util.LoggerNameUtil;
/*     */ import ch.qos.logback.core.ContextBase;
/*     */ import ch.qos.logback.core.spi.FilterReply;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import ch.qos.logback.core.status.StatusListener;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import ch.qos.logback.core.status.WarnStatus;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.slf4j.ILoggerFactory;
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
/*     */ public class LoggerContext
/*     */   extends ContextBase
/*     */   implements ILoggerFactory, LifeCycle
/*     */ {
/*     */   final Logger root;
/*     */   private int size;
/*  50 */   private int noAppenderWarning = 0;
/*  51 */   private final List<LoggerContextListener> loggerContextListenerList = new ArrayList();
/*     */   
/*     */   private Map<String, Logger> loggerCache;
/*     */   
/*     */   private LoggerContextVO loggerContextRemoteView;
/*  56 */   private final TurboFilterList turboFilterList = new TurboFilterList();
/*  57 */   private boolean packagingDataEnabled = true;
/*     */   
/*  59 */   private int maxCallerDataDepth = 8;
/*     */   
/*  61 */   int resetCount = 0;
/*     */   private List<String> frameworkPackages;
/*     */   
/*     */   public LoggerContext()
/*     */   {
/*  66 */     this.loggerCache = new ConcurrentHashMap();
/*     */     
/*  68 */     this.loggerContextRemoteView = new LoggerContextVO(this);
/*  69 */     this.root = new Logger("ROOT", null, this);
/*  70 */     this.root.setLevel(Level.DEBUG);
/*  71 */     this.loggerCache.put("ROOT", this.root);
/*  72 */     initEvaluatorMap();
/*  73 */     this.size = 1;
/*  74 */     this.frameworkPackages = new ArrayList();
/*     */   }
/*     */   
/*     */   void initEvaluatorMap() {
/*  78 */     putObject("EVALUATOR_MAP", new HashMap());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateLoggerContextVO()
/*     */   {
/*  86 */     this.loggerContextRemoteView = new LoggerContextVO(this);
/*     */   }
/*     */   
/*     */   public void putProperty(String key, String val)
/*     */   {
/*  91 */     super.putProperty(key, val);
/*  92 */     updateLoggerContextVO();
/*     */   }
/*     */   
/*     */   public void setName(String name)
/*     */   {
/*  97 */     super.setName(name);
/*  98 */     updateLoggerContextVO();
/*     */   }
/*     */   
/*     */   public final Logger getLogger(Class clazz) {
/* 102 */     return getLogger(clazz.getName());
/*     */   }
/*     */   
/*     */   public final Logger getLogger(String name)
/*     */   {
/* 107 */     if (name == null) {
/* 108 */       throw new IllegalArgumentException("name argument cannot be null");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 113 */     if ("ROOT".equalsIgnoreCase(name)) {
/* 114 */       return this.root;
/*     */     }
/*     */     
/* 117 */     int i = 0;
/* 118 */     Logger logger = this.root;
/*     */     
/*     */ 
/*     */ 
/* 122 */     Logger childLogger = (Logger)this.loggerCache.get(name);
/*     */     
/* 124 */     if (childLogger != null) {
/* 125 */       return childLogger;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     for (;;)
/*     */     {
/* 132 */       int h = LoggerNameUtil.getSeparatorIndexOf(name, i);
/* 133 */       String childName; String childName; if (h == -1) {
/* 134 */         childName = name;
/*     */       } else {
/* 136 */         childName = name.substring(0, h);
/*     */       }
/*     */       
/* 139 */       i = h + 1;
/* 140 */       synchronized (logger) {
/* 141 */         childLogger = logger.getChildByName(childName);
/* 142 */         if (childLogger == null) {
/* 143 */           childLogger = logger.createChildByName(childName);
/* 144 */           this.loggerCache.put(childName, childLogger);
/* 145 */           incSize();
/*     */         }
/*     */       }
/* 148 */       logger = childLogger;
/* 149 */       if (h == -1) {
/* 150 */         return childLogger;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void incSize() {
/* 156 */     this.size += 1;
/*     */   }
/*     */   
/*     */   int size() {
/* 160 */     return this.size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Logger exists(String name)
/*     */   {
/* 170 */     return (Logger)this.loggerCache.get(name);
/*     */   }
/*     */   
/*     */   final void noAppenderDefinedWarning(Logger logger) {
/* 174 */     if (this.noAppenderWarning++ == 0) {
/* 175 */       getStatusManager().add(new WarnStatus("No appenders present in context [" + getName() + "] for logger [" + logger.getName() + "].", logger));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public List<Logger> getLoggerList()
/*     */   {
/* 182 */     Collection<Logger> collection = this.loggerCache.values();
/* 183 */     List<Logger> loggerList = new ArrayList(collection);
/* 184 */     Collections.sort(loggerList, new LoggerComparator());
/* 185 */     return loggerList;
/*     */   }
/*     */   
/*     */   public LoggerContextVO getLoggerContextRemoteView() {
/* 189 */     return this.loggerContextRemoteView;
/*     */   }
/*     */   
/*     */   public void setPackagingDataEnabled(boolean packagingDataEnabled) {
/* 193 */     this.packagingDataEnabled = packagingDataEnabled;
/*     */   }
/*     */   
/*     */   public boolean isPackagingDataEnabled() {
/* 197 */     return this.packagingDataEnabled;
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
/*     */   public void reset()
/*     */   {
/* 210 */     this.resetCount += 1;
/* 211 */     super.reset();
/* 212 */     initEvaluatorMap();
/* 213 */     this.root.recursiveReset();
/* 214 */     resetTurboFilterList();
/* 215 */     fireOnReset();
/* 216 */     resetListenersExceptResetResistant();
/* 217 */     resetStatusListeners();
/*     */   }
/*     */   
/*     */   private void resetStatusListeners() {
/* 221 */     StatusManager sm = getStatusManager();
/* 222 */     for (StatusListener sl : sm.getCopyOfStatusListenerList()) {
/* 223 */       sm.remove(sl);
/*     */     }
/*     */   }
/*     */   
/*     */   public TurboFilterList getTurboFilterList() {
/* 228 */     return this.turboFilterList;
/*     */   }
/*     */   
/*     */   public void addTurboFilter(TurboFilter newFilter) {
/* 232 */     this.turboFilterList.add(newFilter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTurboFilterList()
/*     */   {
/* 240 */     for (TurboFilter tf : this.turboFilterList) {
/* 241 */       tf.stop();
/*     */     }
/* 243 */     this.turboFilterList.clear();
/*     */   }
/*     */   
/*     */ 
/*     */   final FilterReply getTurboFilterChainDecision_0_3OrMore(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t)
/*     */   {
/* 249 */     if (this.turboFilterList.size() == 0) {
/* 250 */       return FilterReply.NEUTRAL;
/*     */     }
/* 252 */     return this.turboFilterList.getTurboFilterChainDecision(marker, logger, level, format, params, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final FilterReply getTurboFilterChainDecision_1(Marker marker, Logger logger, Level level, String format, Object param, Throwable t)
/*     */   {
/* 259 */     if (this.turboFilterList.size() == 0) {
/* 260 */       return FilterReply.NEUTRAL;
/*     */     }
/* 262 */     return this.turboFilterList.getTurboFilterChainDecision(marker, logger, level, format, new Object[] { param }, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final FilterReply getTurboFilterChainDecision_2(Marker marker, Logger logger, Level level, String format, Object param1, Object param2, Throwable t)
/*     */   {
/* 269 */     if (this.turboFilterList.size() == 0) {
/* 270 */       return FilterReply.NEUTRAL;
/*     */     }
/* 272 */     return this.turboFilterList.getTurboFilterChainDecision(marker, logger, level, format, new Object[] { param1, param2 }, t);
/*     */   }
/*     */   
/*     */ 
/*     */   public void addListener(LoggerContextListener listener)
/*     */   {
/* 278 */     this.loggerContextListenerList.add(listener);
/*     */   }
/*     */   
/*     */   public void removeListener(LoggerContextListener listener) {
/* 282 */     this.loggerContextListenerList.remove(listener);
/*     */   }
/*     */   
/*     */   private void resetListenersExceptResetResistant() {
/* 286 */     List<LoggerContextListener> toRetain = new ArrayList();
/*     */     
/* 288 */     for (LoggerContextListener lcl : this.loggerContextListenerList) {
/* 289 */       if (lcl.isResetResistant()) {
/* 290 */         toRetain.add(lcl);
/*     */       }
/*     */     }
/* 293 */     this.loggerContextListenerList.retainAll(toRetain);
/*     */   }
/*     */   
/*     */   private void resetAllListeners() {
/* 297 */     this.loggerContextListenerList.clear();
/*     */   }
/*     */   
/*     */   public List<LoggerContextListener> getCopyOfListenerList() {
/* 301 */     return new ArrayList(this.loggerContextListenerList);
/*     */   }
/*     */   
/*     */   void fireOnLevelChange(Logger logger, Level level) {
/* 305 */     for (LoggerContextListener listener : this.loggerContextListenerList) {
/* 306 */       listener.onLevelChange(logger, level);
/*     */     }
/*     */   }
/*     */   
/*     */   private void fireOnReset() {
/* 311 */     for (LoggerContextListener listener : this.loggerContextListenerList) {
/* 312 */       listener.onReset(this);
/*     */     }
/*     */   }
/*     */   
/*     */   private void fireOnStart() {
/* 317 */     for (LoggerContextListener listener : this.loggerContextListenerList) {
/* 318 */       listener.onStart(this);
/*     */     }
/*     */   }
/*     */   
/*     */   private void fireOnStop() {
/* 323 */     for (LoggerContextListener listener : this.loggerContextListenerList) {
/* 324 */       listener.onStop(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void start()
/*     */   {
/* 331 */     super.start();
/* 332 */     fireOnStart();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 336 */     reset();
/* 337 */     fireOnStop();
/* 338 */     resetAllListeners();
/* 339 */     super.stop();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 344 */     return getClass().getName() + "[" + getName() + "]";
/*     */   }
/*     */   
/*     */   public int getMaxCallerDataDepth() {
/* 348 */     return this.maxCallerDataDepth;
/*     */   }
/*     */   
/*     */   public void setMaxCallerDataDepth(int maxCallerDataDepth) {
/* 352 */     this.maxCallerDataDepth = maxCallerDataDepth;
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
/*     */   public List<String> getFrameworkPackages()
/*     */   {
/* 365 */     return this.frameworkPackages;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\LoggerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */