/*    */ package ch.qos.logback.core.rolling.helper;
/*    */ 
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
/*    */ public class SizeAndTimeBasedArchiveRemover
/*    */   extends DefaultArchiveRemover
/*    */ {
/*    */   public SizeAndTimeBasedArchiveRemover(FileNamePattern fileNamePattern, RollingCalendar rc)
/*    */   {
/* 23 */     super(fileNamePattern, rc);
/*    */   }
/*    */   
/*    */   public void cleanByPeriodOffset(Date now, int periodOffset) {
/* 27 */     Date dateOfPeriodToClean = this.rc.getRelativeDate(now, periodOffset);
/*    */     
/* 29 */     String regex = this.fileNamePattern.toRegexForFixedDate(dateOfPeriodToClean);
/* 30 */     String stemRegex = FileFilterUtil.afterLastSlash(regex);
/* 31 */     File archive0 = new File(this.fileNamePattern.convertMultipleArguments(new Object[] { dateOfPeriodToClean, Integer.valueOf(0) }));
/*    */     
/*    */ 
/*    */ 
/* 35 */     archive0 = archive0.getAbsoluteFile();
/*    */     
/* 37 */     File parentDir = archive0.getAbsoluteFile().getParentFile();
/* 38 */     File[] matchingFileArray = FileFilterUtil.filesInFolderMatchingStemRegex(parentDir, stemRegex);
/*    */     
/*    */ 
/* 41 */     for (File f : matchingFileArray) {
/* 42 */       Date fileLastModified = this.rc.getRelativeDate(new Date(f.lastModified()), -1);
/*    */       
/* 44 */       if (fileLastModified.compareTo(dateOfPeriodToClean) <= 0) {
/* 45 */         addInfo("deleting " + f);
/* 46 */         f.delete();
/*    */       }
/*    */     }
/*    */     
/* 50 */     if (this.parentClean) {
/* 51 */       removeFolderIfEmpty(parentDir);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\SizeAndTimeBasedArchiveRemover.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */