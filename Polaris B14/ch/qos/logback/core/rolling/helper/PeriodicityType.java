/*    */ package ch.qos.logback.core.rolling.helper;
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
/*    */ public enum PeriodicityType
/*    */ {
/* 18 */   ERRONEOUS,  TOP_OF_MILLISECOND,  TOP_OF_SECOND,  TOP_OF_MINUTE,  TOP_OF_HOUR,  HALF_DAY,  TOP_OF_DAY,  TOP_OF_WEEK,  TOP_OF_MONTH;
/*    */   
/*    */ 
/*    */ 
/* 22 */   static PeriodicityType[] VALID_ORDERED_LIST = { TOP_OF_MILLISECOND, TOP_OF_SECOND, TOP_OF_MINUTE, TOP_OF_HOUR, TOP_OF_DAY, TOP_OF_WEEK, TOP_OF_MONTH };
/*    */   
/*    */   private PeriodicityType() {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\PeriodicityType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */