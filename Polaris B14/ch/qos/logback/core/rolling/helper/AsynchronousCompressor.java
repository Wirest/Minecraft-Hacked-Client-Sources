/*    */ package ch.qos.logback.core.rolling.helper;
/*    */ 
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.Future;
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
/*    */ public class AsynchronousCompressor
/*    */ {
/*    */   Compressor compressor;
/*    */   
/*    */   public AsynchronousCompressor(Compressor compressor)
/*    */   {
/* 24 */     this.compressor = compressor;
/*    */   }
/*    */   
/*    */   public Future<?> compressAsynchronously(String nameOfFile2Compress, String nameOfCompressedFile, String innerEntryName)
/*    */   {
/* 29 */     ExecutorService executor = Executors.newScheduledThreadPool(1);
/* 30 */     Future<?> future = executor.submit(new CompressionRunnable(this.compressor, nameOfFile2Compress, nameOfCompressedFile, innerEntryName));
/*    */     
/* 32 */     executor.shutdown();
/* 33 */     return future;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\AsynchronousCompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */