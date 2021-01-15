/*    */ package ch.qos.logback.core.rolling;
/*    */ 
/*    */ import ch.qos.logback.core.rolling.helper.ArchiveRemover;
/*    */ import ch.qos.logback.core.rolling.helper.FileNamePattern;
/*    */ import ch.qos.logback.core.rolling.helper.TimeBasedArchiveRemover;
/*    */ import java.io.File;
/*    */ import java.util.Date;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultTimeBasedFileNamingAndTriggeringPolicy<E>
/*    */   extends TimeBasedFileNamingAndTriggeringPolicyBase<E>
/*    */ {
/*    */   public void start()
/*    */   {
/* 32 */     super.start();
/* 33 */     this.archiveRemover = new TimeBasedArchiveRemover(this.tbrp.fileNamePattern, this.rc);
/* 34 */     this.archiveRemover.setContext(this.context);
/* 35 */     this.started = true;
/*    */   }
/*    */   
/*    */   public boolean isTriggeringEvent(File activeFile, E event) {
/* 39 */     long time = getCurrentTime();
/* 40 */     if (time >= this.nextCheck) {
/* 41 */       Date dateOfElapsedPeriod = this.dateInCurrentPeriod;
/* 42 */       addInfo("Elapsed period: " + dateOfElapsedPeriod);
/* 43 */       this.elapsedPeriodsFileName = this.tbrp.fileNamePatternWCS.convert(dateOfElapsedPeriod);
/*    */       
/* 45 */       setDateInCurrentPeriod(time);
/* 46 */       computeNextCheck();
/* 47 */       return true;
/*    */     }
/* 49 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 55 */     return "c.q.l.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\DefaultTimeBasedFileNamingAndTriggeringPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */