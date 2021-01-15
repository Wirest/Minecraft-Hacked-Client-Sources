/*    */ package optfine;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.minecraft.crash.CrashReport;
/*    */ 
/*    */ public class CrashReporter
/*    */ {
/*    */   public static void onCrashReport(CrashReport p_onCrashReport_0_)
/*    */   {
/*    */     try
/*    */     {
/* 14 */       GameSettings gamesettings = Config.getGameSettings();
/*    */       
/* 16 */       if (gamesettings == null)
/*    */       {
/* 18 */         return;
/*    */       }
/*    */       
/* 21 */       if (!gamesettings.snooperEnabled)
/*    */       {
/* 23 */         return;
/*    */       }
/*    */       
/* 26 */       String s = "http://optifine.net/crashReport";
/* 27 */       String s1 = makeReport(p_onCrashReport_0_);
/* 28 */       byte[] abyte = s1.getBytes("ASCII");
/* 29 */       IFileUploadListener ifileuploadlistener = new IFileUploadListener()
/*    */       {
/*    */ 
/*    */         public void fileUploadFinished(String p_fileUploadFinished_1_, byte[] p_fileUploadFinished_2_, Throwable p_fileUploadFinished_3_) {}
/*    */ 
/* 34 */       };
/* 35 */       Map map = new HashMap();
/* 36 */       map.put("OF-Version", Config.getVersion());
/* 37 */       map.put("OF-Summary", makeSummary(p_onCrashReport_0_));
/* 38 */       FileUploadThread fileuploadthread = new FileUploadThread(s, map, abyte, ifileuploadlistener);
/* 39 */       fileuploadthread.setPriority(10);
/* 40 */       fileuploadthread.start();
/* 41 */       Thread.sleep(1000L);
/*    */     }
/*    */     catch (Exception exception)
/*    */     {
/* 45 */       Config.dbg(exception.getClass().getName() + ": " + exception.getMessage());
/*    */     }
/*    */   }
/*    */   
/*    */   private static String makeReport(CrashReport p_makeReport_0_)
/*    */   {
/* 51 */     StringBuffer stringbuffer = new StringBuffer();
/* 52 */     stringbuffer.append("OptiFineVersion: " + Config.getVersion() + "\n");
/* 53 */     stringbuffer.append("Summary: " + makeSummary(p_makeReport_0_) + "\n");
/* 54 */     stringbuffer.append("\n");
/* 55 */     stringbuffer.append(p_makeReport_0_.getCompleteReport());
/* 56 */     stringbuffer.append("\n");
/* 57 */     stringbuffer.append("OpenGlVersion: " + Config.openGlVersion + "\n");
/* 58 */     stringbuffer.append("OpenGlRenderer: " + Config.openGlRenderer + "\n");
/* 59 */     stringbuffer.append("OpenGlVendor: " + Config.openGlVendor + "\n");
/* 60 */     stringbuffer.append("CpuCount: " + Config.getAvailableProcessors() + "\n");
/* 61 */     return stringbuffer.toString();
/*    */   }
/*    */   
/*    */   private static String makeSummary(CrashReport p_makeSummary_0_)
/*    */   {
/* 66 */     Throwable throwable = p_makeSummary_0_.getCrashCause();
/*    */     
/* 68 */     if (throwable == null)
/*    */     {
/* 70 */       return "Unknown";
/*    */     }
/*    */     
/*    */ 
/* 74 */     StackTraceElement[] astacktraceelement = throwable.getStackTrace();
/* 75 */     String s = "unknown";
/*    */     
/* 77 */     if (astacktraceelement.length > 0)
/*    */     {
/* 79 */       s = astacktraceelement[0].toString().trim();
/*    */     }
/*    */     
/* 82 */     String s1 = throwable.getClass().getName() + ": " + throwable.getMessage() + " (" + p_makeSummary_0_.getDescription() + ")" + " [" + s + "]";
/* 83 */     return s1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\CrashReporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */