/*     */ package ch.qos.logback.core.rolling.helper;
/*     */ 
/*     */ import ch.qos.logback.core.pattern.Converter;
/*     */ import ch.qos.logback.core.pattern.LiteralConverter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DefaultArchiveRemover
/*     */   extends ContextAwareBase
/*     */   implements ArchiveRemover
/*     */ {
/*     */   protected static final long UNINITIALIZED = -1L;
/*     */   protected static final long INACTIVITY_TOLERANCE_IN_MILLIS = 5529600000L;
/*     */   static final int MAX_VALUE_FOR_INACTIVITY_PERIODS = 336;
/*     */   final FileNamePattern fileNamePattern;
/*     */   final RollingCalendar rc;
/*     */   int periodOffsetForDeletionTarget;
/*     */   final boolean parentClean;
/*  36 */   long lastHeartBeat = -1L;
/*     */   
/*     */   public DefaultArchiveRemover(FileNamePattern fileNamePattern, RollingCalendar rc)
/*     */   {
/*  40 */     this.fileNamePattern = fileNamePattern;
/*  41 */     this.rc = rc;
/*  42 */     this.parentClean = computeParentCleaningFlag(fileNamePattern);
/*     */   }
/*     */   
/*     */   int computeElapsedPeriodsSinceLastClean(long nowInMillis)
/*     */   {
/*  47 */     long periodsElapsed = 0L;
/*  48 */     if (this.lastHeartBeat == -1L) {
/*  49 */       addInfo("first clean up after appender initialization");
/*  50 */       periodsElapsed = this.rc.periodsElapsed(nowInMillis, nowInMillis + 5529600000L);
/*  51 */       if (periodsElapsed > 336L)
/*  52 */         periodsElapsed = 336L;
/*     */     } else {
/*  54 */       periodsElapsed = this.rc.periodsElapsed(this.lastHeartBeat, nowInMillis);
/*  55 */       if (periodsElapsed < 1L) {
/*  56 */         addWarn("Unexpected periodsElapsed value " + periodsElapsed);
/*  57 */         periodsElapsed = 1L;
/*     */       }
/*     */     }
/*  60 */     return (int)periodsElapsed;
/*     */   }
/*     */   
/*     */   public void clean(Date now) {
/*  64 */     long nowInMillis = now.getTime();
/*  65 */     int periodsElapsed = computeElapsedPeriodsSinceLastClean(nowInMillis);
/*  66 */     this.lastHeartBeat = nowInMillis;
/*  67 */     if (periodsElapsed > 1) {
/*  68 */       addInfo("periodsElapsed = " + periodsElapsed);
/*     */     }
/*  70 */     for (int i = 0; i < periodsElapsed; i++) {
/*  71 */       cleanByPeriodOffset(now, this.periodOffsetForDeletionTarget - i);
/*     */     }
/*     */   }
/*     */   
/*     */   abstract void cleanByPeriodOffset(Date paramDate, int paramInt);
/*     */   
/*     */   boolean computeParentCleaningFlag(FileNamePattern fileNamePattern) {
/*  78 */     DateTokenConverter dtc = fileNamePattern.getPrimaryDateTokenConverter();
/*     */     
/*  80 */     if (dtc.getDatePattern().indexOf('/') != -1) {
/*  81 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  86 */     Converter<Object> p = fileNamePattern.headTokenConverter;
/*     */     
/*     */ 
/*  89 */     while ((p != null) && 
/*  90 */       (!(p instanceof DateTokenConverter)))
/*     */     {
/*     */ 
/*  93 */       p = p.getNext();
/*     */     }
/*     */     
/*  96 */     while (p != null) {
/*  97 */       if ((p instanceof LiteralConverter)) {
/*  98 */         String s = p.convert(null);
/*  99 */         if (s.indexOf('/') != -1) {
/* 100 */           return true;
/*     */         }
/*     */       }
/* 103 */       p = p.getNext();
/*     */     }
/*     */     
/*     */ 
/* 107 */     return false;
/*     */   }
/*     */   
/*     */   void removeFolderIfEmpty(File dir) {
/* 111 */     removeFolderIfEmpty(dir, 0);
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
/*     */   private void removeFolderIfEmpty(File dir, int depth)
/*     */   {
/* 124 */     if (depth >= 3) {
/* 125 */       return;
/*     */     }
/* 127 */     if ((dir.isDirectory()) && (FileFilterUtil.isEmptyDirectory(dir))) {
/* 128 */       addInfo("deleting folder [" + dir + "]");
/* 129 */       dir.delete();
/* 130 */       removeFolderIfEmpty(dir.getParentFile(), depth + 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setMaxHistory(int maxHistory) {
/* 135 */     this.periodOffsetForDeletionTarget = (-maxHistory - 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\DefaultArchiveRemover.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */