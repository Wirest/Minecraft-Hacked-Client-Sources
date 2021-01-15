/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class LoggingPrintStream extends PrintStream
/*    */ {
/* 10 */   private static final Logger LOGGER = ;
/*    */   private final String domain;
/*    */   
/*    */   public LoggingPrintStream(String domainIn, OutputStream outStream)
/*    */   {
/* 15 */     super(outStream);
/* 16 */     this.domain = domainIn;
/*    */   }
/*    */   
/*    */   public void println(String p_println_1_)
/*    */   {
/* 21 */     logString(p_println_1_);
/*    */   }
/*    */   
/*    */   public void println(Object p_println_1_)
/*    */   {
/* 26 */     logString(String.valueOf(p_println_1_));
/*    */   }
/*    */   
/*    */   private void logString(String string)
/*    */   {
/* 31 */     StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
/* 32 */     StackTraceElement stacktraceelement = astacktraceelement[Math.min(3, astacktraceelement.length)];
/* 33 */     LOGGER.info("[{}]@.({}:{}): {}", new Object[] { this.domain, stacktraceelement.getFileName(), Integer.valueOf(stacktraceelement.getLineNumber()), string });
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\LoggingPrintStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */