/*    */ package ch.qos.logback.classic.db;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.classic.spi.LoggerContextVO;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ public class DBHelper
/*    */ {
/*    */   public static final short PROPERTIES_EXIST = 1;
/*    */   public static final short EXCEPTION_EXISTS = 2;
/*    */   
/*    */   public static short computeReferenceMask(ILoggingEvent event)
/*    */   {
/* 28 */     short mask = 0;
/*    */     
/* 30 */     int mdcPropSize = 0;
/* 31 */     if (event.getMDCPropertyMap() != null) {
/* 32 */       mdcPropSize = event.getMDCPropertyMap().keySet().size();
/*    */     }
/* 34 */     int contextPropSize = 0;
/* 35 */     if (event.getLoggerContextVO().getPropertyMap() != null) {
/* 36 */       contextPropSize = event.getLoggerContextVO().getPropertyMap().size();
/*    */     }
/*    */     
/* 39 */     if ((mdcPropSize > 0) || (contextPropSize > 0)) {
/* 40 */       mask = 1;
/*    */     }
/* 42 */     if (event.getThrowableProxy() != null) {
/* 43 */       mask = (short)(mask | 0x2);
/*    */     }
/* 45 */     return mask;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\db\DBHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */