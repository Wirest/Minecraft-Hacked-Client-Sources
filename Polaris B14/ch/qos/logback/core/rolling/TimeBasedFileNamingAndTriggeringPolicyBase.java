/*     */ package ch.qos.logback.core.rolling;
/*     */ 
/*     */ import ch.qos.logback.core.rolling.helper.ArchiveRemover;
/*     */ import ch.qos.logback.core.rolling.helper.DateTokenConverter;
/*     */ import ch.qos.logback.core.rolling.helper.FileNamePattern;
/*     */ import ch.qos.logback.core.rolling.helper.RollingCalendar;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import java.io.File;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TimeBasedFileNamingAndTriggeringPolicyBase<E>
/*     */   extends ContextAwareBase
/*     */   implements TimeBasedFileNamingAndTriggeringPolicy<E>
/*     */ {
/*     */   protected TimeBasedRollingPolicy<E> tbrp;
/*  29 */   protected ArchiveRemover archiveRemover = null;
/*     */   
/*     */   protected String elapsedPeriodsFileName;
/*     */   protected RollingCalendar rc;
/*  33 */   protected long artificialCurrentTime = -1L;
/*  34 */   protected Date dateInCurrentPeriod = null;
/*     */   
/*     */   protected long nextCheck;
/*  37 */   protected boolean started = false;
/*     */   
/*     */   public boolean isStarted() {
/*  40 */     return this.started;
/*     */   }
/*     */   
/*     */   public void start() {
/*  44 */     DateTokenConverter dtc = this.tbrp.fileNamePattern.getPrimaryDateTokenConverter();
/*  45 */     if (dtc == null) {
/*  46 */       throw new IllegalStateException("FileNamePattern [" + this.tbrp.fileNamePattern.getPattern() + "] does not contain a valid DateToken");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  51 */     this.rc = new RollingCalendar();
/*  52 */     this.rc.init(dtc.getDatePattern());
/*  53 */     addInfo("The date pattern is '" + dtc.getDatePattern() + "' from file name pattern '" + this.tbrp.fileNamePattern.getPattern() + "'.");
/*     */     
/*     */ 
/*  56 */     this.rc.printPeriodicity(this);
/*     */     
/*  58 */     setDateInCurrentPeriod(new Date(getCurrentTime()));
/*  59 */     if (this.tbrp.getParentsRawFileProperty() != null) {
/*  60 */       File currentFile = new File(this.tbrp.getParentsRawFileProperty());
/*  61 */       if ((currentFile.exists()) && (currentFile.canRead())) {
/*  62 */         setDateInCurrentPeriod(new Date(currentFile.lastModified()));
/*     */       }
/*     */     }
/*     */     
/*  66 */     addInfo("Setting initial period to " + this.dateInCurrentPeriod);
/*  67 */     computeNextCheck();
/*     */   }
/*     */   
/*     */   public void stop() {
/*  71 */     this.started = false;
/*     */   }
/*     */   
/*     */   protected void computeNextCheck() {
/*  75 */     this.nextCheck = this.rc.getNextTriggeringMillis(this.dateInCurrentPeriod);
/*     */   }
/*     */   
/*     */   protected void setDateInCurrentPeriod(long now) {
/*  79 */     this.dateInCurrentPeriod.setTime(now);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setDateInCurrentPeriod(Date _dateInCurrentPeriod)
/*     */   {
/*  85 */     this.dateInCurrentPeriod = _dateInCurrentPeriod;
/*     */   }
/*     */   
/*     */   public String getElapsedPeriodsFileName() {
/*  89 */     return this.elapsedPeriodsFileName;
/*     */   }
/*     */   
/*     */   public String getCurrentPeriodsFileNameWithoutCompressionSuffix() {
/*  93 */     return this.tbrp.fileNamePatternWCS.convert(this.dateInCurrentPeriod);
/*     */   }
/*     */   
/*     */   public void setCurrentTime(long timeInMillis) {
/*  97 */     this.artificialCurrentTime = timeInMillis;
/*     */   }
/*     */   
/*     */   public long getCurrentTime()
/*     */   {
/* 102 */     if (this.artificialCurrentTime >= 0L) {
/* 103 */       return this.artificialCurrentTime;
/*     */     }
/* 105 */     return System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   public void setTimeBasedRollingPolicy(TimeBasedRollingPolicy<E> _tbrp)
/*     */   {
/* 110 */     this.tbrp = _tbrp;
/*     */   }
/*     */   
/*     */   public ArchiveRemover getArchiveRemover()
/*     */   {
/* 115 */     return this.archiveRemover;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\TimeBasedFileNamingAndTriggeringPolicyBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */