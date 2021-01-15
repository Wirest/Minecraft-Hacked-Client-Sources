/*    */ package ch.qos.logback.core.rolling;
/*    */ 
/*    */ import ch.qos.logback.core.FileAppender;
/*    */ import ch.qos.logback.core.rolling.helper.CompressionMode;
/*    */ import ch.qos.logback.core.rolling.helper.FileNamePattern;
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
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
/*    */ public abstract class RollingPolicyBase
/*    */   extends ContextAwareBase
/*    */   implements RollingPolicy
/*    */ {
/* 29 */   protected CompressionMode compressionMode = CompressionMode.NONE;
/*    */   
/*    */ 
/*    */   FileNamePattern fileNamePattern;
/*    */   
/*    */ 
/*    */   protected String fileNamePatternStr;
/*    */   
/*    */ 
/*    */   private FileAppender parent;
/*    */   
/*    */ 
/*    */   FileNamePattern zipEntryFileNamePattern;
/*    */   
/*    */ 
/*    */   private boolean started;
/*    */   
/*    */ 
/*    */   protected void determineCompressionMode()
/*    */   {
/* 49 */     if (this.fileNamePatternStr.endsWith(".gz")) {
/* 50 */       addInfo("Will use gz compression");
/* 51 */       this.compressionMode = CompressionMode.GZ;
/* 52 */     } else if (this.fileNamePatternStr.endsWith(".zip")) {
/* 53 */       addInfo("Will use zip compression");
/* 54 */       this.compressionMode = CompressionMode.ZIP;
/*    */     } else {
/* 56 */       addInfo("No compression will be used");
/* 57 */       this.compressionMode = CompressionMode.NONE;
/*    */     }
/*    */   }
/*    */   
/*    */   public void setFileNamePattern(String fnp) {
/* 62 */     this.fileNamePatternStr = fnp;
/*    */   }
/*    */   
/*    */   public String getFileNamePattern() {
/* 66 */     return this.fileNamePatternStr;
/*    */   }
/*    */   
/*    */   public CompressionMode getCompressionMode() {
/* 70 */     return this.compressionMode;
/*    */   }
/*    */   
/*    */   public boolean isStarted() {
/* 74 */     return this.started;
/*    */   }
/*    */   
/*    */   public void start() {
/* 78 */     this.started = true;
/*    */   }
/*    */   
/*    */   public void stop() {
/* 82 */     this.started = false;
/*    */   }
/*    */   
/*    */   public void setParent(FileAppender appender) {
/* 86 */     this.parent = appender;
/*    */   }
/*    */   
/*    */   public boolean isParentPrudent() {
/* 90 */     return this.parent.isPrudent();
/*    */   }
/*    */   
/*    */   public String getParentsRawFileProperty() {
/* 94 */     return this.parent.rawFileProperty();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\RollingPolicyBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */