/*    */ package ch.qos.logback.core;
/*    */ 
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
/*    */ public abstract class LayoutBase<E>
/*    */   extends ContextAwareBase
/*    */   implements Layout<E>
/*    */ {
/*    */   protected boolean started;
/*    */   String fileHeader;
/*    */   String fileFooter;
/*    */   String presentationHeader;
/*    */   String presentationFooter;
/*    */   
/*    */   public void setContext(Context context)
/*    */   {
/* 28 */     this.context = context;
/*    */   }
/*    */   
/*    */   public Context getContext() {
/* 32 */     return this.context;
/*    */   }
/*    */   
/*    */   public void start() {
/* 36 */     this.started = true;
/*    */   }
/*    */   
/*    */   public void stop() {
/* 40 */     this.started = false;
/*    */   }
/*    */   
/*    */   public boolean isStarted() {
/* 44 */     return this.started;
/*    */   }
/*    */   
/*    */   public String getFileHeader() {
/* 48 */     return this.fileHeader;
/*    */   }
/*    */   
/*    */   public String getPresentationHeader() {
/* 52 */     return this.presentationHeader;
/*    */   }
/*    */   
/*    */   public String getPresentationFooter() {
/* 56 */     return this.presentationFooter;
/*    */   }
/*    */   
/*    */   public String getFileFooter() {
/* 60 */     return this.fileFooter;
/*    */   }
/*    */   
/*    */   public String getContentType() {
/* 64 */     return "text/plain";
/*    */   }
/*    */   
/*    */   public void setFileHeader(String header) {
/* 68 */     this.fileHeader = header;
/*    */   }
/*    */   
/*    */   public void setFileFooter(String footer) {
/* 72 */     this.fileFooter = footer;
/*    */   }
/*    */   
/*    */   public void setPresentationHeader(String header) {
/* 76 */     this.presentationHeader = header;
/*    */   }
/*    */   
/*    */   public void setPresentationFooter(String footer) {
/* 80 */     this.presentationFooter = footer;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\LayoutBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */