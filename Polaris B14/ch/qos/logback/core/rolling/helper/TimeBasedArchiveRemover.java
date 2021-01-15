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
/*    */ public class TimeBasedArchiveRemover
/*    */   extends DefaultArchiveRemover
/*    */ {
/*    */   public TimeBasedArchiveRemover(FileNamePattern fileNamePattern, RollingCalendar rc)
/*    */   {
/* 23 */     super(fileNamePattern, rc);
/*    */   }
/*    */   
/*    */   protected void cleanByPeriodOffset(Date now, int periodOffset) {
/* 27 */     Date date2delete = this.rc.getRelativeDate(now, periodOffset);
/* 28 */     String filename = this.fileNamePattern.convert(date2delete);
/* 29 */     File file2Delete = new File(filename);
/* 30 */     if ((file2Delete.exists()) && (file2Delete.isFile())) {
/* 31 */       Date fileLastModified = this.rc.getRelativeDate(new Date(file2Delete.lastModified()), -1);
/*    */       
/* 33 */       if (fileLastModified.compareTo(date2delete) <= 0) {
/* 34 */         addInfo("deleting " + file2Delete);
/* 35 */         file2Delete.delete();
/*    */         
/* 37 */         if (this.parentClean) {
/* 38 */           removeFolderIfEmpty(file2Delete.getParentFile());
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 46 */     return "c.q.l.core.rolling.helper.TimeBasedArchiveRemover";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\TimeBasedArchiveRemover.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */