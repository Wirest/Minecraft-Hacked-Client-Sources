/*     */ package ch.qos.logback.core;
/*     */ 
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import ch.qos.logback.core.spi.LogbackLock;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import ch.qos.logback.core.util.ExecutorServiceUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextBase
/*     */   implements Context, LifeCycle
/*     */ {
/*  29 */   private long birthTime = System.currentTimeMillis();
/*     */   
/*     */   private String name;
/*  32 */   private StatusManager sm = new BasicStatusManager();
/*     */   
/*     */ 
/*     */ 
/*  36 */   Map<String, String> propertyMap = new HashMap();
/*  37 */   Map<String, Object> objectMap = new HashMap();
/*     */   
/*  39 */   LogbackLock configurationLock = new LogbackLock();
/*     */   private volatile ExecutorService executorService;
/*     */   private LifeCycleManager lifeCycleManager;
/*     */   private boolean started;
/*     */   
/*     */   public StatusManager getStatusManager()
/*     */   {
/*  46 */     return this.sm;
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
/*     */   public void setStatusManager(StatusManager statusManager)
/*     */   {
/*  61 */     if (statusManager == null) {
/*  62 */       throw new IllegalArgumentException("null StatusManager not allowed");
/*     */     }
/*  64 */     this.sm = statusManager;
/*     */   }
/*     */   
/*     */   public Map<String, String> getCopyOfPropertyMap() {
/*  68 */     return new HashMap(this.propertyMap);
/*     */   }
/*     */   
/*     */   public void putProperty(String key, String val) {
/*  72 */     this.propertyMap.put(key, val);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProperty(String key)
/*     */   {
/*  83 */     if ("CONTEXT_NAME".equals(key)) {
/*  84 */       return getName();
/*     */     }
/*  86 */     return (String)this.propertyMap.get(key);
/*     */   }
/*     */   
/*     */   public Object getObject(String key) {
/*  90 */     return this.objectMap.get(key);
/*     */   }
/*     */   
/*     */   public void putObject(String key, Object value) {
/*  94 */     this.objectMap.put(key, value);
/*     */   }
/*     */   
/*     */   public void removeObject(String key) {
/*  98 */     this.objectMap.remove(key);
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 103 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/* 110 */     this.started = true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void stop()
/*     */   {
/* 116 */     stopExecutorService();
/* 117 */     this.started = false;
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/* 121 */     return this.started;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 129 */     removeShutdownHook();
/* 130 */     getLifeCycleManager().reset();
/* 131 */     this.propertyMap.clear();
/* 132 */     this.objectMap.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String name)
/*     */     throws IllegalStateException
/*     */   {
/* 143 */     if ((name != null) && (name.equals(this.name))) {
/* 144 */       return;
/*     */     }
/* 146 */     if ((this.name == null) || ("default".equals(this.name)))
/*     */     {
/* 148 */       this.name = name;
/*     */     } else {
/* 150 */       throw new IllegalStateException("Context has been already given a name");
/*     */     }
/*     */   }
/*     */   
/*     */   public long getBirthTime() {
/* 155 */     return this.birthTime;
/*     */   }
/*     */   
/*     */   public Object getConfigurationLock() {
/* 159 */     return this.configurationLock;
/*     */   }
/*     */   
/*     */   public ExecutorService getExecutorService() {
/* 163 */     if (this.executorService == null) {
/* 164 */       synchronized (this) {
/* 165 */         if (this.executorService == null) {
/* 166 */           this.executorService = ExecutorServiceUtil.newExecutorService();
/*     */         }
/*     */       }
/*     */     }
/* 170 */     return this.executorService;
/*     */   }
/*     */   
/*     */   private synchronized void stopExecutorService() {
/* 174 */     if (this.executorService != null) {
/* 175 */       ExecutorServiceUtil.shutdown(this.executorService);
/* 176 */       this.executorService = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeShutdownHook() {
/* 181 */     Thread hook = (Thread)getObject("SHUTDOWN_HOOK");
/* 182 */     if (hook != null) {
/* 183 */       removeObject("SHUTDOWN_HOOK");
/*     */       try {
/* 185 */         Runtime.getRuntime().removeShutdownHook(hook);
/*     */       }
/*     */       catch (IllegalStateException e) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void register(LifeCycle component)
/*     */   {
/* 194 */     getLifeCycleManager().register(component);
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
/*     */   synchronized LifeCycleManager getLifeCycleManager()
/*     */   {
/* 210 */     if (this.lifeCycleManager == null) {
/* 211 */       this.lifeCycleManager = new LifeCycleManager();
/*     */     }
/* 213 */     return this.lifeCycleManager;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 218 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\ContextBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */