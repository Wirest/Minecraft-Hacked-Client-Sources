/*     */ package ch.qos.logback.classic.turbo;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.gaffer.GafferUtil;
/*     */ import ch.qos.logback.classic.joran.JoranConfigurator;
/*     */ import ch.qos.logback.classic.util.EnvUtil;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.event.SaxEvent;
/*     */ import ch.qos.logback.core.joran.spi.ConfigurationWatchList;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
/*     */ import ch.qos.logback.core.spi.FilterReply;
/*     */ import ch.qos.logback.core.status.StatusUtil;
/*     */ import java.io.File;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ExecutorService;
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
/*     */ 
/*     */ public class ReconfigureOnChangeFilter
/*     */   extends TurboFilter
/*     */ {
/*     */   public static final long DEFAULT_REFRESH_PERIOD = 60000L;
/*     */   long refreshPeriod;
/*     */   URL mainConfigurationURL;
/*     */   protected volatile long nextCheck;
/*     */   ConfigurationWatchList configurationWatchList;
/*     */   private long invocationCounter;
/*     */   private volatile long mask;
/*     */   private volatile long lastMaskCheck;
/*     */   private static final int MAX_MASK = 65535;
/*     */   private static final long MASK_INCREASE_THRESHOLD = 100L;
/*     */   private static final long MASK_DECREASE_THRESHOLD = 800L;
/*     */   
/*     */   public void start()
/*     */   {
/*  59 */     this.configurationWatchList = ConfigurationWatchListUtil.getConfigurationWatchList(this.context);
/*  60 */     if (this.configurationWatchList != null) {
/*  61 */       this.mainConfigurationURL = this.configurationWatchList.getMainURL();
/*  62 */       if (this.mainConfigurationURL == null) {
/*  63 */         addWarn("Due to missing top level configuration file, automatic reconfiguration is impossible.");
/*  64 */         return;
/*     */       }
/*  66 */       List<File> watchList = this.configurationWatchList.getCopyOfFileWatchList();
/*  67 */       long inSeconds = this.refreshPeriod / 1000L;
/*  68 */       addInfo("Will scan for changes in [" + watchList + "] every " + inSeconds + " seconds. ");
/*     */       
/*  70 */       synchronized (this.configurationWatchList) {
/*  71 */         updateNextCheck(System.currentTimeMillis());
/*     */       }
/*  73 */       super.start();
/*     */     } else {
/*  75 */       addWarn("Empty ConfigurationWatchList in context");
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  81 */     return "ReconfigureOnChangeFilter{invocationCounter=" + this.invocationCounter + '}';
/*     */   }
/*     */   
/*     */   public ReconfigureOnChangeFilter()
/*     */   {
/*  51 */     this.refreshPeriod = 60000L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */     this.invocationCounter = 0L;
/*     */     
/*  94 */     this.mask = 15L;
/*  95 */     this.lastMaskCheck = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */ 
/*     */   public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t)
/*     */   {
/* 101 */     if (!isStarted()) {
/* 102 */       return FilterReply.NEUTRAL;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 108 */     if ((this.invocationCounter++ & this.mask) != this.mask) {
/* 109 */       return FilterReply.NEUTRAL;
/*     */     }
/*     */     
/* 112 */     long now = System.currentTimeMillis();
/*     */     
/* 114 */     synchronized (this.configurationWatchList) {
/* 115 */       updateMaskIfNecessary(now);
/* 116 */       if (changeDetected(now))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 121 */         disableSubsequentReconfiguration();
/* 122 */         detachReconfigurationToNewThread();
/*     */       }
/*     */     }
/*     */     
/* 126 */     return FilterReply.NEUTRAL;
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
/*     */   private void updateMaskIfNecessary(long now)
/*     */   {
/* 144 */     long timeElapsedSinceLastMaskUpdateCheck = now - this.lastMaskCheck;
/* 145 */     this.lastMaskCheck = now;
/* 146 */     if ((timeElapsedSinceLastMaskUpdateCheck < 100L) && (this.mask < 65535L)) {
/* 147 */       this.mask = (this.mask << 1 | 1L);
/* 148 */     } else if (timeElapsedSinceLastMaskUpdateCheck > 800L) {
/* 149 */       this.mask >>>= 2;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void detachReconfigurationToNewThread()
/*     */   {
/* 157 */     addInfo("Detected change in [" + this.configurationWatchList.getCopyOfFileWatchList() + "]");
/* 158 */     this.context.getExecutorService().submit(new ReconfiguringThread());
/*     */   }
/*     */   
/*     */   void updateNextCheck(long now) {
/* 162 */     this.nextCheck = (now + this.refreshPeriod);
/*     */   }
/*     */   
/*     */   protected boolean changeDetected(long now) {
/* 166 */     if (now >= this.nextCheck) {
/* 167 */       updateNextCheck(now);
/* 168 */       return this.configurationWatchList.changeDetected();
/*     */     }
/* 170 */     return false;
/*     */   }
/*     */   
/*     */   void disableSubsequentReconfiguration() {
/* 174 */     this.nextCheck = Long.MAX_VALUE;
/*     */   }
/*     */   
/*     */   public long getRefreshPeriod() {
/* 178 */     return this.refreshPeriod;
/*     */   }
/*     */   
/*     */ 
/* 182 */   public void setRefreshPeriod(long refreshPeriod) { this.refreshPeriod = refreshPeriod; }
/*     */   
/*     */   class ReconfiguringThread implements Runnable {
/*     */     ReconfiguringThread() {}
/*     */     
/* 187 */     public void run() { if (ReconfigureOnChangeFilter.this.mainConfigurationURL == null) {
/* 188 */         ReconfigureOnChangeFilter.this.addInfo("Due to missing top level configuration file, skipping reconfiguration");
/* 189 */         return;
/*     */       }
/* 191 */       LoggerContext lc = (LoggerContext)ReconfigureOnChangeFilter.this.context;
/* 192 */       ReconfigureOnChangeFilter.this.addInfo("Will reset and reconfigure context named [" + ReconfigureOnChangeFilter.this.context.getName() + "]");
/* 193 */       if (ReconfigureOnChangeFilter.this.mainConfigurationURL.toString().endsWith("xml")) {
/* 194 */         performXMLConfiguration(lc);
/* 195 */       } else if (ReconfigureOnChangeFilter.this.mainConfigurationURL.toString().endsWith("groovy")) {
/* 196 */         if (EnvUtil.isGroovyAvailable()) {
/* 197 */           lc.reset();
/*     */           
/*     */ 
/* 200 */           GafferUtil.runGafferConfiguratorOn(lc, this, ReconfigureOnChangeFilter.this.mainConfigurationURL);
/*     */         } else {
/* 202 */           ReconfigureOnChangeFilter.this.addError("Groovy classes are not available on the class path. ABORTING INITIALIZATION.");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void performXMLConfiguration(LoggerContext lc) {
/* 208 */       JoranConfigurator jc = new JoranConfigurator();
/* 209 */       jc.setContext(ReconfigureOnChangeFilter.this.context);
/* 210 */       StatusUtil statusUtil = new StatusUtil(ReconfigureOnChangeFilter.this.context);
/* 211 */       List<SaxEvent> eventList = jc.recallSafeConfiguration();
/* 212 */       URL mainURL = ConfigurationWatchListUtil.getMainWatchURL(ReconfigureOnChangeFilter.this.context);
/* 213 */       lc.reset();
/* 214 */       long threshold = System.currentTimeMillis();
/*     */       try {
/* 216 */         jc.doConfigure(ReconfigureOnChangeFilter.this.mainConfigurationURL);
/* 217 */         if (statusUtil.hasXMLParsingErrors(threshold)) {
/* 218 */           fallbackConfiguration(lc, eventList, mainURL);
/*     */         }
/*     */       } catch (JoranException e) {
/* 221 */         fallbackConfiguration(lc, eventList, mainURL);
/*     */       }
/*     */     }
/*     */     
/*     */     private void fallbackConfiguration(LoggerContext lc, List<SaxEvent> eventList, URL mainURL) {
/* 226 */       JoranConfigurator joranConfigurator = new JoranConfigurator();
/* 227 */       joranConfigurator.setContext(ReconfigureOnChangeFilter.this.context);
/* 228 */       if (eventList != null) {
/* 229 */         ReconfigureOnChangeFilter.this.addWarn("Falling back to previously registered safe configuration.");
/*     */         try {
/* 231 */           lc.reset();
/* 232 */           JoranConfigurator.informContextOfURLUsedForConfiguration(ReconfigureOnChangeFilter.this.context, mainURL);
/* 233 */           joranConfigurator.doConfigure(eventList);
/* 234 */           ReconfigureOnChangeFilter.this.addInfo("Re-registering previous fallback configuration once more as a fallback configuration point");
/* 235 */           joranConfigurator.registerSafeConfiguration();
/*     */         } catch (JoranException e) {
/* 237 */           ReconfigureOnChangeFilter.this.addError("Unexpected exception thrown by a configuration considered safe.", e);
/*     */         }
/*     */       } else {
/* 240 */         ReconfigureOnChangeFilter.this.addWarn("No previous configuration to fall back on.");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\turbo\ReconfigureOnChangeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */