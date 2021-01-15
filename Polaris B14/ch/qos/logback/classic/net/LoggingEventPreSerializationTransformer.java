/*    */ package ch.qos.logback.classic.net;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.classic.spi.LoggingEvent;
/*    */ import ch.qos.logback.classic.spi.LoggingEventVO;
/*    */ import ch.qos.logback.core.spi.PreSerializationTransformer;
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
/*    */ public class LoggingEventPreSerializationTransformer
/*    */   implements PreSerializationTransformer<ILoggingEvent>
/*    */ {
/*    */   public Serializable transform(ILoggingEvent event)
/*    */   {
/* 27 */     if (event == null) {
/* 28 */       return null;
/*    */     }
/* 30 */     if ((event instanceof LoggingEvent))
/* 31 */       return LoggingEventVO.build(event);
/* 32 */     if ((event instanceof LoggingEventVO)) {
/* 33 */       return (LoggingEventVO)event;
/*    */     }
/* 35 */     throw new IllegalArgumentException("Unsupported type " + event.getClass().getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\LoggingEventPreSerializationTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */