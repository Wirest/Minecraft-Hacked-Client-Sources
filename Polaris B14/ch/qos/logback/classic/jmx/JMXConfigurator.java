/*     */ package ch.qos.logback.classic.jmx;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.joran.JoranConfigurator;
/*     */ import ch.qos.logback.classic.spi.LoggerContextListener;
/*     */ import ch.qos.logback.classic.util.ContextInitializer;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import ch.qos.logback.core.status.StatusListener;
/*     */ import ch.qos.logback.core.status.StatusListenerAsList;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import ch.qos.logback.core.util.StatusPrinter;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanRegistrationException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JMXConfigurator
/*     */   extends ContextAwareBase
/*     */   implements JMXConfiguratorMBean, LoggerContextListener
/*     */ {
/*  57 */   private static String EMPTY = "";
/*     */   
/*     */   LoggerContext loggerContext;
/*     */   
/*     */   MBeanServer mbs;
/*     */   
/*     */   ObjectName objectName;
/*     */   
/*     */   String objectNameAsString;
/*  66 */   boolean debug = true;
/*     */   
/*     */   boolean started;
/*     */   
/*     */   public JMXConfigurator(LoggerContext loggerContext, MBeanServer mbs, ObjectName objectName)
/*     */   {
/*  72 */     this.started = true;
/*  73 */     this.context = loggerContext;
/*  74 */     this.loggerContext = loggerContext;
/*  75 */     this.mbs = mbs;
/*  76 */     this.objectName = objectName;
/*  77 */     this.objectNameAsString = objectName.toString();
/*  78 */     if (previouslyRegisteredListenerWithSameObjectName()) {
/*  79 */       addError("Previously registered JMXConfigurator named [" + this.objectNameAsString + "] in the logger context named [" + loggerContext.getName() + "]");
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*  84 */       loggerContext.addListener(this);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean previouslyRegisteredListenerWithSameObjectName() {
/*  89 */     List<LoggerContextListener> lcll = this.loggerContext.getCopyOfListenerList();
/*  90 */     for (LoggerContextListener lcl : lcll) {
/*  91 */       if ((lcl instanceof JMXConfigurator)) {
/*  92 */         JMXConfigurator jmxConfigurator = (JMXConfigurator)lcl;
/*  93 */         if (this.objectName.equals(jmxConfigurator.objectName)) {
/*  94 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*  98 */     return false;
/*     */   }
/*     */   
/*     */   public void reloadDefaultConfiguration() throws JoranException {
/* 102 */     ContextInitializer ci = new ContextInitializer(this.loggerContext);
/* 103 */     URL url = ci.findURLOfDefaultConfigurationFile(true);
/* 104 */     reloadByURL(url);
/*     */   }
/*     */   
/*     */   public void reloadByFileName(String fileName) throws JoranException, FileNotFoundException
/*     */   {
/* 109 */     File f = new File(fileName);
/* 110 */     if ((f.exists()) && (f.isFile()))
/*     */     {
/*     */       try {
/* 113 */         URL url = f.toURI().toURL();
/* 114 */         reloadByURL(url);
/*     */       } catch (MalformedURLException e) {
/* 116 */         throw new RuntimeException("Unexpected MalformedURLException occured. See nexted cause.", e);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 121 */       String errMsg = "Could not find [" + fileName + "]";
/* 122 */       addInfo(errMsg);
/* 123 */       throw new FileNotFoundException(errMsg);
/*     */     }
/*     */   }
/*     */   
/*     */   void addStatusListener(StatusListener statusListener) {
/* 128 */     StatusManager sm = this.loggerContext.getStatusManager();
/* 129 */     sm.add(statusListener);
/*     */   }
/*     */   
/*     */   void removeStatusListener(StatusListener statusListener) {
/* 133 */     StatusManager sm = this.loggerContext.getStatusManager();
/* 134 */     sm.remove(statusListener);
/*     */   }
/*     */   
/*     */   public void reloadByURL(URL url) throws JoranException {
/* 138 */     StatusListenerAsList statusListenerAsList = new StatusListenerAsList();
/*     */     
/* 140 */     addStatusListener(statusListenerAsList);
/* 141 */     addInfo("Resetting context: " + this.loggerContext.getName());
/* 142 */     this.loggerContext.reset();
/*     */     
/* 144 */     addStatusListener(statusListenerAsList);
/*     */     try
/*     */     {
/* 147 */       JoranConfigurator configurator = new JoranConfigurator();
/* 148 */       configurator.setContext(this.loggerContext);
/* 149 */       configurator.doConfigure(url);
/* 150 */       addInfo("Context: " + this.loggerContext.getName() + " reloaded.");
/*     */     } finally {
/* 152 */       removeStatusListener(statusListenerAsList);
/* 153 */       if (this.debug) {
/* 154 */         StatusPrinter.print(statusListenerAsList.getStatusList());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setLoggerLevel(String loggerName, String levelStr) {
/* 160 */     if (loggerName == null) {
/* 161 */       return;
/*     */     }
/* 163 */     if (levelStr == null) {
/* 164 */       return;
/*     */     }
/* 166 */     loggerName = loggerName.trim();
/* 167 */     levelStr = levelStr.trim();
/*     */     
/* 169 */     addInfo("Trying to set level " + levelStr + " to logger " + loggerName);
/* 170 */     LoggerContext lc = (LoggerContext)this.context;
/*     */     
/* 172 */     Logger logger = lc.getLogger(loggerName);
/* 173 */     if ("null".equalsIgnoreCase(levelStr)) {
/* 174 */       logger.setLevel(null);
/*     */     } else {
/* 176 */       Level level = Level.toLevel(levelStr, null);
/* 177 */       if (level != null) {
/* 178 */         logger.setLevel(level);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getLoggerLevel(String loggerName) {
/* 184 */     if (loggerName == null) {
/* 185 */       return EMPTY;
/*     */     }
/*     */     
/* 188 */     loggerName = loggerName.trim();
/*     */     
/* 190 */     LoggerContext lc = (LoggerContext)this.context;
/* 191 */     Logger logger = lc.exists(loggerName);
/* 192 */     if ((logger != null) && (logger.getLevel() != null)) {
/* 193 */       return logger.getLevel().toString();
/*     */     }
/* 195 */     return EMPTY;
/*     */   }
/*     */   
/*     */   public String getLoggerEffectiveLevel(String loggerName)
/*     */   {
/* 200 */     if (loggerName == null) {
/* 201 */       return EMPTY;
/*     */     }
/*     */     
/* 204 */     loggerName = loggerName.trim();
/*     */     
/* 206 */     LoggerContext lc = (LoggerContext)this.context;
/* 207 */     Logger logger = lc.exists(loggerName);
/* 208 */     if (logger != null) {
/* 209 */       return logger.getEffectiveLevel().toString();
/*     */     }
/* 211 */     return EMPTY;
/*     */   }
/*     */   
/*     */   public List<String> getLoggerList()
/*     */   {
/* 216 */     LoggerContext lc = (LoggerContext)this.context;
/* 217 */     List<String> strList = new ArrayList();
/* 218 */     Iterator<Logger> it = lc.getLoggerList().iterator();
/* 219 */     while (it.hasNext()) {
/* 220 */       Logger log = (Logger)it.next();
/* 221 */       strList.add(log.getName());
/*     */     }
/* 223 */     return strList;
/*     */   }
/*     */   
/*     */   public List<String> getStatuses() {
/* 227 */     List<String> list = new ArrayList();
/* 228 */     Iterator<Status> it = this.context.getStatusManager().getCopyOfStatusList().iterator();
/*     */     
/* 230 */     while (it.hasNext()) {
/* 231 */       list.add(((Status)it.next()).toString());
/*     */     }
/* 233 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onStop(LoggerContext context)
/*     */   {
/* 241 */     if (!this.started) {
/* 242 */       addInfo("onStop() method called on a stopped JMXActivator [" + this.objectNameAsString + "]");
/*     */       
/* 244 */       return;
/*     */     }
/* 246 */     if (this.mbs.isRegistered(this.objectName)) {
/*     */       try {
/* 248 */         addInfo("Unregistering mbean [" + this.objectNameAsString + "]");
/* 249 */         this.mbs.unregisterMBean(this.objectName);
/*     */       }
/*     */       catch (InstanceNotFoundException e) {
/* 252 */         addError("Unable to find a verifiably registered mbean [" + this.objectNameAsString + "]", e);
/*     */       }
/*     */       catch (MBeanRegistrationException e) {
/* 255 */         addError("Failed to unregister [" + this.objectNameAsString + "]", e);
/*     */       }
/*     */     } else {
/* 258 */       addInfo("mbean [" + this.objectNameAsString + "] was not in the mbean registry. This is OK.");
/*     */     }
/*     */     
/* 261 */     stop();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onLevelChange(Logger logger, Level level) {}
/*     */   
/*     */   public void onReset(LoggerContext context)
/*     */   {
/* 269 */     addInfo("onReset() method called JMXActivator [" + this.objectNameAsString + "]");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isResetResistant()
/*     */   {
/* 278 */     return true;
/*     */   }
/*     */   
/*     */   private void clearFields() {
/* 282 */     this.mbs = null;
/* 283 */     this.objectName = null;
/* 284 */     this.loggerContext = null;
/*     */   }
/*     */   
/*     */   private void stop() {
/* 288 */     this.started = false;
/* 289 */     clearFields();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onStart(LoggerContext context) {}
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 298 */     return getClass().getName() + "(" + this.context.getName() + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\jmx\JMXConfigurator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */