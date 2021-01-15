/*     */ package ch.qos.logback.core.rolling;
/*     */ 
/*     */ import ch.qos.logback.core.joran.spi.NoAutoStart;
/*     */ import ch.qos.logback.core.rolling.helper.ArchiveRemover;
/*     */ import ch.qos.logback.core.rolling.helper.CompressionMode;
/*     */ import ch.qos.logback.core.rolling.helper.FileFilterUtil;
/*     */ import ch.qos.logback.core.rolling.helper.FileNamePattern;
/*     */ import ch.qos.logback.core.rolling.helper.SizeAndTimeBasedArchiveRemover;
/*     */ import ch.qos.logback.core.util.FileSize;
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
/*     */ @NoAutoStart
/*     */ public class SizeAndTimeBasedFNATP<E>
/*     */   extends TimeBasedFileNamingAndTriggeringPolicyBase<E>
/*     */ {
/*  30 */   int currentPeriodsCounter = 0;
/*     */   
/*     */   FileSize maxFileSize;
/*     */   String maxFileSizeAsString;
/*     */   private int invocationCounter;
/*     */   
/*     */   public void start()
/*     */   {
/*  38 */     super.start();
/*     */     
/*  40 */     this.archiveRemover = createArchiveRemover();
/*  41 */     this.archiveRemover.setContext(this.context);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  46 */     String regex = this.tbrp.fileNamePattern.toRegexForFixedDate(this.dateInCurrentPeriod);
/*  47 */     String stemRegex = FileFilterUtil.afterLastSlash(regex);
/*     */     
/*     */ 
/*  50 */     computeCurrentPeriodsHighestCounterValue(stemRegex);
/*     */     
/*  52 */     this.started = true;
/*     */   }
/*     */   
/*     */   protected ArchiveRemover createArchiveRemover() {
/*  56 */     return new SizeAndTimeBasedArchiveRemover(this.tbrp.fileNamePattern, this.rc);
/*     */   }
/*     */   
/*     */   void computeCurrentPeriodsHighestCounterValue(String stemRegex) {
/*  60 */     File file = new File(getCurrentPeriodsFileNameWithoutCompressionSuffix());
/*  61 */     File parentDir = file.getParentFile();
/*     */     
/*  63 */     File[] matchingFileArray = FileFilterUtil.filesInFolderMatchingStemRegex(parentDir, stemRegex);
/*     */     
/*     */ 
/*  66 */     if ((matchingFileArray == null) || (matchingFileArray.length == 0)) {
/*  67 */       this.currentPeriodsCounter = 0;
/*  68 */       return;
/*     */     }
/*  70 */     this.currentPeriodsCounter = FileFilterUtil.findHighestCounter(matchingFileArray, stemRegex);
/*     */     
/*     */ 
/*     */ 
/*  74 */     if ((this.tbrp.getParentsRawFileProperty() != null) || (this.tbrp.compressionMode != CompressionMode.NONE))
/*     */     {
/*  76 */       this.currentPeriodsCounter += 1;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */   private int invocationMask = 1;
/*     */   
/*     */   public boolean isTriggeringEvent(File activeFile, E event)
/*     */   {
/*  89 */     long time = getCurrentTime();
/*  90 */     if (time >= this.nextCheck) {
/*  91 */       Date dateInElapsedPeriod = this.dateInCurrentPeriod;
/*  92 */       this.elapsedPeriodsFileName = this.tbrp.fileNamePatternWCS.convertMultipleArguments(new Object[] { dateInElapsedPeriod, Integer.valueOf(this.currentPeriodsCounter) });
/*     */       
/*  94 */       this.currentPeriodsCounter = 0;
/*  95 */       setDateInCurrentPeriod(time);
/*  96 */       computeNextCheck();
/*  97 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 101 */     if ((++this.invocationCounter & this.invocationMask) != this.invocationMask) {
/* 102 */       return false;
/*     */     }
/* 104 */     if (this.invocationMask < 15) {
/* 105 */       this.invocationMask = ((this.invocationMask << 1) + 1);
/*     */     }
/*     */     
/* 108 */     if (activeFile.length() >= this.maxFileSize.getSize()) {
/* 109 */       this.elapsedPeriodsFileName = this.tbrp.fileNamePatternWCS.convertMultipleArguments(new Object[] { this.dateInCurrentPeriod, Integer.valueOf(this.currentPeriodsCounter) });
/*     */       
/* 111 */       this.currentPeriodsCounter += 1;
/* 112 */       return true;
/*     */     }
/*     */     
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   private String getFileNameIncludingCompressionSuffix(Date date, int counter) {
/* 119 */     return this.tbrp.fileNamePattern.convertMultipleArguments(new Object[] { this.dateInCurrentPeriod, Integer.valueOf(counter) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getCurrentPeriodsFileNameWithoutCompressionSuffix()
/*     */   {
/* 126 */     return this.tbrp.fileNamePatternWCS.convertMultipleArguments(new Object[] { this.dateInCurrentPeriod, Integer.valueOf(this.currentPeriodsCounter) });
/*     */   }
/*     */   
/*     */   public String getMaxFileSize()
/*     */   {
/* 131 */     return this.maxFileSizeAsString;
/*     */   }
/*     */   
/*     */   public void setMaxFileSize(String maxFileSize) {
/* 135 */     this.maxFileSizeAsString = maxFileSize;
/* 136 */     this.maxFileSize = FileSize.valueOf(maxFileSize);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\SizeAndTimeBasedFNATP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */