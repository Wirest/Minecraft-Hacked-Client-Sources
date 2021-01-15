/*    */ package ch.qos.logback.classic;
/*    */ 
/*    */ import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.ConsoleAppender;
/*    */ import ch.qos.logback.core.status.InfoStatus;
/*    */ import ch.qos.logback.core.status.StatusManager;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public class BasicConfigurator
/*    */ {
/* 34 */   static final BasicConfigurator hiddenSingleton = new BasicConfigurator();
/*    */   
/*    */ 
/*    */ 
/*    */   public static void configure(LoggerContext lc)
/*    */   {
/* 40 */     StatusManager sm = lc.getStatusManager();
/* 41 */     if (sm != null) {
/* 42 */       sm.add(new InfoStatus("Setting up default configuration.", lc));
/*    */     }
/* 44 */     ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender();
/* 45 */     ca.setContext(lc);
/* 46 */     ca.setName("console");
/* 47 */     PatternLayoutEncoder pl = new PatternLayoutEncoder();
/* 48 */     pl.setContext(lc);
/* 49 */     pl.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
/* 50 */     pl.start();
/*    */     
/* 52 */     ca.setEncoder(pl);
/* 53 */     ca.start();
/* 54 */     Logger rootLogger = lc.getLogger("ROOT");
/* 55 */     rootLogger.addAppender(ca);
/*    */   }
/*    */   
/*    */   public static void configureDefaultContext() {
/* 59 */     LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
/* 60 */     configure(lc);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\BasicConfigurator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */