/*    */ package io.netty.util.internal.logging;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.slf4j.helpers.NOPLoggerFactory;
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
/*    */ public class Slf4JLoggerFactory
/*    */   extends InternalLoggerFactory
/*    */ {
/*    */   public Slf4JLoggerFactory() {}
/*    */   
/*    */   Slf4JLoggerFactory(boolean failIfNOP)
/*    */   {
/* 36 */     assert (failIfNOP);
/*    */     
/*    */ 
/*    */ 
/* 40 */     final StringBuffer buf = new StringBuffer();
/* 41 */     PrintStream err = System.err;
/*    */     try {
/* 43 */       System.setErr(new PrintStream(new OutputStream()
/*    */       {
/*    */ 
/* 46 */         public void write(int b) { buf.append((char)b); } }, true, "US-ASCII"));
/*    */     }
/*    */     catch (UnsupportedEncodingException e)
/*    */     {
/* 50 */       throw new Error(e);
/*    */     }
/*    */     try
/*    */     {
/* 54 */       if ((LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory)) {
/* 55 */         throw new NoClassDefFoundError(buf.toString());
/*    */       }
/* 57 */       err.print(buf);
/* 58 */       err.flush();
/*    */     }
/*    */     finally {
/* 61 */       System.setErr(err);
/*    */     }
/*    */   }
/*    */   
/*    */   public InternalLogger newInstance(String name)
/*    */   {
/* 67 */     return new Slf4JLogger(LoggerFactory.getLogger(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\logging\Slf4JLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */