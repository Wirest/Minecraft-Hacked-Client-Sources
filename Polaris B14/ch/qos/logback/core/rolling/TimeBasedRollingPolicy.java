/*     */ package ch.qos.logback.core.rolling;
/*     */ 
/*     */ import ch.qos.logback.core.rolling.helper.ArchiveRemover;
/*     */ import ch.qos.logback.core.rolling.helper.AsynchronousCompressor;
/*     */ import ch.qos.logback.core.rolling.helper.CompressionMode;
/*     */ import ch.qos.logback.core.rolling.helper.Compressor;
/*     */ import ch.qos.logback.core.rolling.helper.FileFilterUtil;
/*     */ import ch.qos.logback.core.rolling.helper.FileNamePattern;
/*     */ import ch.qos.logback.core.rolling.helper.RenameUtil;
/*     */ import java.io.File;
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TimeBasedRollingPolicy<E>
/*     */   extends RollingPolicyBase
/*     */   implements TriggeringPolicy<E>
/*     */ {
/*     */   static final String FNP_NOT_SET = "The FileNamePattern option must be set before using TimeBasedRollingPolicy. ";
/*     */   static final int INFINITE_HISTORY = 0;
/*     */   FileNamePattern fileNamePatternWCS;
/*     */   private Compressor compressor;
/*  45 */   private RenameUtil renameUtil = new RenameUtil();
/*     */   
/*     */   Future<?> future;
/*  48 */   private int maxHistory = 0;
/*     */   
/*     */   private ArchiveRemover archiveRemover;
/*     */   
/*     */   TimeBasedFileNamingAndTriggeringPolicy<E> timeBasedFileNamingAndTriggeringPolicy;
/*  53 */   boolean cleanHistoryOnStart = false;
/*     */   
/*     */   public void start()
/*     */   {
/*  57 */     this.renameUtil.setContext(this.context);
/*     */     
/*     */ 
/*  60 */     if (this.fileNamePatternStr != null) {
/*  61 */       this.fileNamePattern = new FileNamePattern(this.fileNamePatternStr, this.context);
/*  62 */       determineCompressionMode();
/*     */     } else {
/*  64 */       addWarn("The FileNamePattern option must be set before using TimeBasedRollingPolicy. ");
/*  65 */       addWarn("See also http://logback.qos.ch/codes.html#tbr_fnp_not_set");
/*  66 */       throw new IllegalStateException("The FileNamePattern option must be set before using TimeBasedRollingPolicy. See also http://logback.qos.ch/codes.html#tbr_fnp_not_set");
/*     */     }
/*     */     
/*     */ 
/*  70 */     this.compressor = new Compressor(this.compressionMode);
/*  71 */     this.compressor.setContext(this.context);
/*     */     
/*     */ 
/*  74 */     this.fileNamePatternWCS = new FileNamePattern(Compressor.computeFileNameStr_WCS(this.fileNamePatternStr, this.compressionMode), this.context);
/*     */     
/*     */ 
/*  77 */     addInfo("Will use the pattern " + this.fileNamePatternWCS + " for the active file");
/*     */     
/*     */ 
/*  80 */     if (this.compressionMode == CompressionMode.ZIP) {
/*  81 */       String zipEntryFileNamePatternStr = transformFileNamePattern2ZipEntry(this.fileNamePatternStr);
/*  82 */       this.zipEntryFileNamePattern = new FileNamePattern(zipEntryFileNamePatternStr, this.context);
/*     */     }
/*     */     
/*  85 */     if (this.timeBasedFileNamingAndTriggeringPolicy == null) {
/*  86 */       this.timeBasedFileNamingAndTriggeringPolicy = new DefaultTimeBasedFileNamingAndTriggeringPolicy();
/*     */     }
/*  88 */     this.timeBasedFileNamingAndTriggeringPolicy.setContext(this.context);
/*  89 */     this.timeBasedFileNamingAndTriggeringPolicy.setTimeBasedRollingPolicy(this);
/*  90 */     this.timeBasedFileNamingAndTriggeringPolicy.start();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  95 */     if (this.maxHistory != 0) {
/*  96 */       this.archiveRemover = this.timeBasedFileNamingAndTriggeringPolicy.getArchiveRemover();
/*  97 */       this.archiveRemover.setMaxHistory(this.maxHistory);
/*  98 */       if (this.cleanHistoryOnStart) {
/*  99 */         addInfo("Cleaning on start up");
/* 100 */         this.archiveRemover.clean(new Date(this.timeBasedFileNamingAndTriggeringPolicy.getCurrentTime()));
/*     */       }
/*     */     }
/*     */     
/* 104 */     super.start();
/*     */   }
/*     */   
/*     */   public void stop()
/*     */   {
/* 109 */     if (!isStarted())
/* 110 */       return;
/* 111 */     waitForAsynchronousJobToStop();
/* 112 */     super.stop();
/*     */   }
/*     */   
/*     */   private void waitForAsynchronousJobToStop()
/*     */   {
/* 117 */     if (this.future != null)
/*     */       try {
/* 119 */         this.future.get(30L, TimeUnit.SECONDS);
/*     */       } catch (TimeoutException e) {
/* 121 */         addError("Timeout while waiting for compression job to finish", e);
/*     */       } catch (Exception e) {
/* 123 */         addError("Unexpected exception while waiting for compression job to finish", e);
/*     */       }
/*     */   }
/*     */   
/*     */   private String transformFileNamePattern2ZipEntry(String fileNamePatternStr) {
/* 128 */     String slashified = FileFilterUtil.slashify(fileNamePatternStr);
/* 129 */     return FileFilterUtil.afterLastSlash(slashified);
/*     */   }
/*     */   
/*     */   public void setTimeBasedFileNamingAndTriggeringPolicy(TimeBasedFileNamingAndTriggeringPolicy<E> timeBasedTriggering)
/*     */   {
/* 134 */     this.timeBasedFileNamingAndTriggeringPolicy = timeBasedTriggering;
/*     */   }
/*     */   
/*     */   public TimeBasedFileNamingAndTriggeringPolicy<E> getTimeBasedFileNamingAndTriggeringPolicy() {
/* 138 */     return this.timeBasedFileNamingAndTriggeringPolicy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void rollover()
/*     */     throws RolloverFailure
/*     */   {
/* 146 */     String elapsedPeriodsFileName = this.timeBasedFileNamingAndTriggeringPolicy.getElapsedPeriodsFileName();
/*     */     
/*     */ 
/* 149 */     String elapsedPeriodStem = FileFilterUtil.afterLastSlash(elapsedPeriodsFileName);
/*     */     
/* 151 */     if (this.compressionMode == CompressionMode.NONE) {
/* 152 */       if (getParentsRawFileProperty() != null) {
/* 153 */         this.renameUtil.rename(getParentsRawFileProperty(), elapsedPeriodsFileName);
/*     */       }
/*     */     }
/* 156 */     else if (getParentsRawFileProperty() == null) {
/* 157 */       this.future = asyncCompress(elapsedPeriodsFileName, elapsedPeriodsFileName, elapsedPeriodStem);
/*     */     } else {
/* 159 */       this.future = renamedRawAndAsyncCompress(elapsedPeriodsFileName, elapsedPeriodStem);
/*     */     }
/*     */     
/*     */ 
/* 163 */     if (this.archiveRemover != null) {
/* 164 */       this.archiveRemover.clean(new Date(this.timeBasedFileNamingAndTriggeringPolicy.getCurrentTime()));
/*     */     }
/*     */   }
/*     */   
/*     */   Future asyncCompress(String nameOfFile2Compress, String nameOfCompressedFile, String innerEntryName) throws RolloverFailure
/*     */   {
/* 170 */     AsynchronousCompressor ac = new AsynchronousCompressor(this.compressor);
/* 171 */     return ac.compressAsynchronously(nameOfFile2Compress, nameOfCompressedFile, innerEntryName);
/*     */   }
/*     */   
/*     */   Future renamedRawAndAsyncCompress(String nameOfCompressedFile, String innerEntryName) throws RolloverFailure
/*     */   {
/* 176 */     String parentsRawFile = getParentsRawFileProperty();
/* 177 */     String tmpTarget = parentsRawFile + System.nanoTime() + ".tmp";
/* 178 */     this.renameUtil.rename(parentsRawFile, tmpTarget);
/* 179 */     return asyncCompress(tmpTarget, nameOfCompressedFile, innerEntryName);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getActiveFileName()
/*     */   {
/* 203 */     String parentsRawFileProperty = getParentsRawFileProperty();
/* 204 */     if (parentsRawFileProperty != null) {
/* 205 */       return parentsRawFileProperty;
/*     */     }
/* 207 */     return this.timeBasedFileNamingAndTriggeringPolicy.getCurrentPeriodsFileNameWithoutCompressionSuffix();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isTriggeringEvent(File activeFile, E event)
/*     */   {
/* 213 */     return this.timeBasedFileNamingAndTriggeringPolicy.isTriggeringEvent(activeFile, event);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxHistory()
/*     */   {
/* 222 */     return this.maxHistory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxHistory(int maxHistory)
/*     */   {
/* 232 */     this.maxHistory = maxHistory;
/*     */   }
/*     */   
/*     */   public boolean isCleanHistoryOnStart()
/*     */   {
/* 237 */     return this.cleanHistoryOnStart;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCleanHistoryOnStart(boolean cleanHistoryOnStart)
/*     */   {
/* 246 */     this.cleanHistoryOnStart = cleanHistoryOnStart;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 252 */     return "c.q.l.core.rolling.TimeBasedRollingPolicy";
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\TimeBasedRollingPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */