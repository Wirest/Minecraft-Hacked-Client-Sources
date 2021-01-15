/*    */ package ch.qos.logback.core.joran.spi;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
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
/*    */ public enum ConsoleTarget
/*    */ {
/* 29 */   SystemOut("System.out", new OutputStream()), 
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
/* 48 */   SystemErr("System.err", new OutputStream());
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final String name;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private final OutputStream stream;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ConsoleTarget findByName(String name)
/*    */   {
/* 68 */     for (ConsoleTarget target : ) {
/* 69 */       if (target.name.equalsIgnoreCase(name)) {
/* 70 */         return target;
/*    */       }
/*    */     }
/* 73 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private ConsoleTarget(String name, OutputStream stream)
/*    */   {
/* 80 */     this.name = name;
/* 81 */     this.stream = stream;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 85 */     return this.name;
/*    */   }
/*    */   
/*    */   public OutputStream getStream() {
/* 89 */     return this.stream;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\spi\ConsoleTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */