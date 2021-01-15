/*    */ package org.apache.log4j;
/*    */ 
/*    */ import org.apache.log4j.spi.LoggerFactory;
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
/*    */ public class Logger
/*    */   extends Category
/*    */ {
/* 34 */   private static final String LOGGER_FQCN = Logger.class.getName();
/*    */   
/*    */   protected Logger(String name) {
/* 37 */     super(name);
/*    */   }
/*    */   
/*    */   public static Logger getLogger(String name) {
/* 41 */     return Log4jLoggerFactory.getLogger(name);
/*    */   }
/*    */   
/*    */   public static Logger getLogger(String name, LoggerFactory loggerFactory) {
/* 45 */     return Log4jLoggerFactory.getLogger(name, loggerFactory);
/*    */   }
/*    */   
/*    */   public static Logger getLogger(Class clazz) {
/* 49 */     return getLogger(clazz.getName());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Logger getRootLogger()
/*    */   {
/* 58 */     return Log4jLoggerFactory.getLogger("ROOT");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isTraceEnabled()
/*    */   {
/* 66 */     return this.slf4jLogger.isTraceEnabled();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void trace(Object message)
/*    */   {
/* 73 */     differentiatedLog(null, LOGGER_FQCN, 0, message, null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void trace(Object message, Throwable t)
/*    */   {
/* 81 */     differentiatedLog(null, LOGGER_FQCN, 0, message, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\apache\log4j\Logger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */