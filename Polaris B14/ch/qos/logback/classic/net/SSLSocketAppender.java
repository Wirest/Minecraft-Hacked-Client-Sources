/*    */ package ch.qos.logback.classic.net;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.net.AbstractSSLSocketAppender;
/*    */ import ch.qos.logback.core.spi.PreSerializationTransformer;
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
/*    */ public class SSLSocketAppender
/*    */   extends AbstractSSLSocketAppender<ILoggingEvent>
/*    */ {
/* 32 */   private final PreSerializationTransformer<ILoggingEvent> pst = new LoggingEventPreSerializationTransformer();
/*    */   
/*    */ 
/*    */ 
/*    */   private boolean includeCallerData;
/*    */   
/*    */ 
/*    */ 
/*    */   protected void postProcessEvent(ILoggingEvent event)
/*    */   {
/* 42 */     if (this.includeCallerData) {
/* 43 */       event.getCallerData();
/*    */     }
/*    */   }
/*    */   
/*    */   public void setIncludeCallerData(boolean includeCallerData) {
/* 48 */     this.includeCallerData = includeCallerData;
/*    */   }
/*    */   
/*    */   public PreSerializationTransformer<ILoggingEvent> getPST() {
/* 52 */     return this.pst;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\SSLSocketAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */