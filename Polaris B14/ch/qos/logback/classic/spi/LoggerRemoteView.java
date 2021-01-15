/*    */ package ch.qos.logback.classic.spi;
/*    */ 
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LoggerRemoteView
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 5028223666108713696L;
/*    */   final LoggerContextVO loggerContextView;
/*    */   final String name;
/*    */   
/*    */   public LoggerRemoteView(String name, LoggerContext lc)
/*    */   {
/* 37 */     this.name = name;
/* 38 */     assert (lc.getLoggerContextRemoteView() != null);
/* 39 */     this.loggerContextView = lc.getLoggerContextRemoteView();
/*    */   }
/*    */   
/*    */   public LoggerContextVO getLoggerContextView() {
/* 43 */     return this.loggerContextView;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 47 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\LoggerRemoteView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */