/*    */ package ch.qos.logback.core.rolling.helper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompressionRunnable
/*    */   implements Runnable
/*    */ {
/*    */   final Compressor compressor;
/*    */   
/*    */ 
/*    */ 
/*    */   final String nameOfFile2Compress;
/*    */   
/*    */ 
/*    */ 
/*    */   final String nameOfCompressedFile;
/*    */   
/*    */ 
/*    */   final String innerEntryName;
/*    */   
/*    */ 
/*    */ 
/*    */   public CompressionRunnable(Compressor compressor, String nameOfFile2Compress, String nameOfCompressedFile, String innerEntryName)
/*    */   {
/* 26 */     this.compressor = compressor;
/* 27 */     this.nameOfFile2Compress = nameOfFile2Compress;
/* 28 */     this.nameOfCompressedFile = nameOfCompressedFile;
/* 29 */     this.innerEntryName = innerEntryName;
/*    */   }
/*    */   
/*    */   public void run() {
/* 33 */     this.compressor.compress(this.nameOfFile2Compress, this.nameOfCompressedFile, this.innerEntryName);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\CompressionRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */