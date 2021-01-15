/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.crash.CrashReport;
/*    */ 
/*    */ public class ReportedException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final CrashReport theReportedExceptionCrashReport;
/*    */   
/*    */   public ReportedException(CrashReport report)
/*    */   {
/* 12 */     this.theReportedExceptionCrashReport = report;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public CrashReport getCrashReport()
/*    */   {
/* 20 */     return this.theReportedExceptionCrashReport;
/*    */   }
/*    */   
/*    */   public Throwable getCause()
/*    */   {
/* 25 */     return this.theReportedExceptionCrashReport.getCrashCause();
/*    */   }
/*    */   
/*    */   public String getMessage()
/*    */   {
/* 30 */     return this.theReportedExceptionCrashReport.getDescription();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\ReportedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */