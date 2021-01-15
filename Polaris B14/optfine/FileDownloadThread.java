/*    */ package optfine;
/*    */ 
/*    */ public class FileDownloadThread extends Thread
/*    */ {
/*  5 */   private String urlString = null;
/*  6 */   private IFileDownloadListener listener = null;
/*    */   
/*    */   public FileDownloadThread(String p_i32_1_, IFileDownloadListener p_i32_2_)
/*    */   {
/* 10 */     this.urlString = p_i32_1_;
/* 11 */     this.listener = p_i32_2_;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 18 */       byte[] abyte = HttpUtils.get(this.urlString);
/* 19 */       this.listener.fileDownloadFinished(this.urlString, abyte, null);
/*    */     }
/*    */     catch (Exception exception)
/*    */     {
/* 23 */       this.listener.fileDownloadFinished(this.urlString, null, exception);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getUrlString()
/*    */   {
/* 29 */     return this.urlString;
/*    */   }
/*    */   
/*    */   public IFileDownloadListener getListener()
/*    */   {
/* 34 */     return this.listener;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\FileDownloadThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */