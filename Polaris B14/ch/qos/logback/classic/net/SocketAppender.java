/*    */ package ch.qos.logback.classic.net;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.net.AbstractSocketAppender;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SocketAppender
/*    */   extends AbstractSocketAppender<ILoggingEvent>
/*    */ {
/* 36 */   private static final PreSerializationTransformer<ILoggingEvent> pst = new LoggingEventPreSerializationTransformer();
/*    */   
/*    */ 
/* 39 */   private boolean includeCallerData = false;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void postProcessEvent(ILoggingEvent event)
/*    */   {
/* 47 */     if (this.includeCallerData) {
/* 48 */       event.getCallerData();
/*    */     }
/*    */   }
/*    */   
/*    */   public void setIncludeCallerData(boolean includeCallerData) {
/* 53 */     this.includeCallerData = includeCallerData;
/*    */   }
/*    */   
/*    */   public PreSerializationTransformer<ILoggingEvent> getPST() {
/* 57 */     return pst;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\SocketAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */