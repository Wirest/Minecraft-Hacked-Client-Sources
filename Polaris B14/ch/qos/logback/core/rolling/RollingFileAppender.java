/*     */ package ch.qos.logback.core.rolling;
/*     */ 
/*     */ import ch.qos.logback.core.FileAppender;
/*     */ import ch.qos.logback.core.rolling.helper.CompressionMode;
/*     */ import ch.qos.logback.core.rolling.helper.FileNamePattern;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RollingFileAppender<E>
/*     */   extends FileAppender<E>
/*     */ {
/*     */   File currentlyActiveFile;
/*     */   TriggeringPolicy<E> triggeringPolicy;
/*     */   RollingPolicy rollingPolicy;
/*  41 */   private static String RFA_NO_TP_URL = "http://logback.qos.ch/codes.html#rfa_no_tp";
/*  42 */   private static String RFA_NO_RP_URL = "http://logback.qos.ch/codes.html#rfa_no_rp";
/*  43 */   private static String COLLISION_URL = "http://logback.qos.ch/codes.html#rfa_collision";
/*     */   
/*     */   public void start() {
/*  46 */     if (this.triggeringPolicy == null) {
/*  47 */       addWarn("No TriggeringPolicy was set for the RollingFileAppender named " + getName());
/*     */       
/*  49 */       addWarn("For more information, please visit " + RFA_NO_TP_URL);
/*  50 */       return;
/*     */     }
/*     */     
/*     */ 
/*  54 */     if (!this.append) {
/*  55 */       addWarn("Append mode is mandatory for RollingFileAppender");
/*  56 */       this.append = true;
/*     */     }
/*     */     
/*  59 */     if (this.rollingPolicy == null) {
/*  60 */       addError("No RollingPolicy was set for the RollingFileAppender named " + getName());
/*     */       
/*  62 */       addError("For more information, please visit " + RFA_NO_RP_URL);
/*  63 */       return;
/*     */     }
/*     */     
/*     */ 
/*  67 */     if (fileAndPatternCollide()) {
/*  68 */       addError("File property collides with fileNamePattern. Aborting.");
/*  69 */       addError("For more information, please visit " + COLLISION_URL);
/*  70 */       return;
/*     */     }
/*     */     
/*  73 */     if (isPrudent()) {
/*  74 */       if (rawFileProperty() != null) {
/*  75 */         addWarn("Setting \"File\" property to null on account of prudent mode");
/*  76 */         setFile(null);
/*     */       }
/*  78 */       if (this.rollingPolicy.getCompressionMode() != CompressionMode.NONE) {
/*  79 */         addError("Compression is not supported in prudent mode. Aborting");
/*  80 */         return;
/*     */       }
/*     */     }
/*     */     
/*  84 */     this.currentlyActiveFile = new File(getFile());
/*  85 */     addInfo("Active log file name: " + getFile());
/*  86 */     super.start();
/*     */   }
/*     */   
/*     */   private boolean fileAndPatternCollide() {
/*  90 */     if ((this.triggeringPolicy instanceof RollingPolicyBase)) {
/*  91 */       RollingPolicyBase base = (RollingPolicyBase)this.triggeringPolicy;
/*  92 */       FileNamePattern fileNamePattern = base.fileNamePattern;
/*     */       
/*  94 */       if ((fileNamePattern != null) && (this.fileName != null)) {
/*  95 */         String regex = fileNamePattern.toRegex();
/*  96 */         return this.fileName.matches(regex);
/*     */       }
/*     */     }
/*  99 */     return false;
/*     */   }
/*     */   
/*     */   public void stop()
/*     */   {
/* 104 */     if (this.rollingPolicy != null) this.rollingPolicy.stop();
/* 105 */     if (this.triggeringPolicy != null) this.triggeringPolicy.stop();
/* 106 */     super.stop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setFile(String file)
/*     */   {
/* 113 */     if ((file != null) && ((this.triggeringPolicy != null) || (this.rollingPolicy != null))) {
/* 114 */       addError("File property must be set before any triggeringPolicy or rollingPolicy properties");
/* 115 */       addError("Visit http://logback.qos.ch/codes.html#rfa_file_after for more information");
/*     */     }
/* 117 */     super.setFile(file);
/*     */   }
/*     */   
/*     */   public String getFile()
/*     */   {
/* 122 */     return this.rollingPolicy.getActiveFileName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void rollover()
/*     */   {
/* 129 */     this.lock.lock();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 136 */       closeOutputStream();
/* 137 */       attemptRollover();
/* 138 */       attemptOpenFile();
/*     */     } finally {
/* 140 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   private void attemptOpenFile()
/*     */   {
/*     */     try {
/* 147 */       this.currentlyActiveFile = new File(this.rollingPolicy.getActiveFileName());
/*     */       
/*     */ 
/* 150 */       openFile(this.rollingPolicy.getActiveFileName());
/*     */     } catch (IOException e) {
/* 152 */       addError("setFile(" + this.fileName + ", false) call failed.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void attemptRollover() {
/*     */     try {
/* 158 */       this.rollingPolicy.rollover();
/*     */     } catch (RolloverFailure rf) {
/* 160 */       addWarn("RolloverFailure occurred. Deferring roll-over.");
/*     */       
/* 162 */       this.append = true;
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
/*     */   protected void subAppend(E event)
/*     */   {
/* 176 */     synchronized (this.triggeringPolicy) {
/* 177 */       if (this.triggeringPolicy.isTriggeringEvent(this.currentlyActiveFile, event)) {
/* 178 */         rollover();
/*     */       }
/*     */     }
/*     */     
/* 182 */     super.subAppend(event);
/*     */   }
/*     */   
/*     */   public RollingPolicy getRollingPolicy() {
/* 186 */     return this.rollingPolicy;
/*     */   }
/*     */   
/*     */   public TriggeringPolicy<E> getTriggeringPolicy() {
/* 190 */     return this.triggeringPolicy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRollingPolicy(RollingPolicy policy)
/*     */   {
/* 202 */     this.rollingPolicy = policy;
/* 203 */     if ((this.rollingPolicy instanceof TriggeringPolicy)) {
/* 204 */       this.triggeringPolicy = ((TriggeringPolicy)policy);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTriggeringPolicy(TriggeringPolicy<E> policy)
/*     */   {
/* 210 */     this.triggeringPolicy = policy;
/* 211 */     if ((policy instanceof RollingPolicy)) {
/* 212 */       this.rollingPolicy = ((RollingPolicy)policy);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\RollingFileAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */