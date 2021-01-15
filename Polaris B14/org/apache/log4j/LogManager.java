/*    */ package org.apache.log4j;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.Vector;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogManager
/*    */ {
/*    */   public static Logger getRootLogger()
/*    */   {
/* 41 */     return Log4jLoggerFactory.getLogger("ROOT");
/*    */   }
/*    */   
/*    */   public static Logger getLogger(String name) {
/* 45 */     return Log4jLoggerFactory.getLogger(name);
/*    */   }
/*    */   
/*    */   public static Logger getLogger(Class clazz) {
/* 49 */     return Log4jLoggerFactory.getLogger(clazz.getName());
/*    */   }
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
/*    */   public static Logger getLogger(String name, LoggerFactory loggerFactory)
/*    */   {
/* 64 */     return loggerFactory.makeNewLoggerInstance(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Enumeration getCurrentLoggers()
/*    */   {
/* 73 */     return new Vector().elements();
/*    */   }
/*    */   
/*    */   public static void shutdown() {}
/*    */   
/*    */   public static void resetConfiguration() {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\apache\log4j\LogManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */