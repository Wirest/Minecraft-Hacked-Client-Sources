/*    */ package ch.qos.logback.classic.jul;
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
/*    */ public class JULHelper
/*    */ {
/*    */   public static final boolean isRegularNonRootLogger(java.util.logging.Logger julLogger)
/*    */   {
/* 23 */     if (julLogger == null)
/* 24 */       return false;
/* 25 */     return !julLogger.getName().equals("");
/*    */   }
/*    */   
/*    */   public static final boolean isRoot(java.util.logging.Logger julLogger) {
/* 29 */     if (julLogger == null)
/* 30 */       return false;
/* 31 */     return julLogger.getName().equals("");
/*    */   }
/*    */   
/*    */   public static java.util.logging.Level asJULLevel(ch.qos.logback.classic.Level lbLevel) {
/* 35 */     if (lbLevel == null) {
/* 36 */       throw new IllegalArgumentException("Unexpected level [null]");
/*    */     }
/* 38 */     switch (lbLevel.levelInt) {
/*    */     case -2147483648: 
/* 40 */       return java.util.logging.Level.ALL;
/*    */     case 5000: 
/* 42 */       return java.util.logging.Level.FINEST;
/*    */     case 10000: 
/* 44 */       return java.util.logging.Level.FINE;
/*    */     case 20000: 
/* 46 */       return java.util.logging.Level.INFO;
/*    */     case 30000: 
/* 48 */       return java.util.logging.Level.WARNING;
/*    */     case 40000: 
/* 50 */       return java.util.logging.Level.SEVERE;
/*    */     case 2147483647: 
/* 52 */       return java.util.logging.Level.OFF;
/*    */     }
/* 54 */     throw new IllegalArgumentException("Unexpected level [" + lbLevel + "]");
/*    */   }
/*    */   
/*    */   public static String asJULLoggerName(String loggerName)
/*    */   {
/* 59 */     if ("ROOT".equals(loggerName)) {
/* 60 */       return "";
/*    */     }
/* 62 */     return loggerName;
/*    */   }
/*    */   
/*    */   public static java.util.logging.Logger asJULLogger(String loggerName) {
/* 66 */     String julLoggerName = asJULLoggerName(loggerName);
/* 67 */     return java.util.logging.Logger.getLogger(julLoggerName);
/*    */   }
/*    */   
/*    */   public static java.util.logging.Logger asJULLogger(ch.qos.logback.classic.Logger logger) {
/* 71 */     return asJULLogger(logger.getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\jul\JULHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */