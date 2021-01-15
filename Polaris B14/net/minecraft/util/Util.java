/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.FutureTask;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class Util
/*    */ {
/*    */   public static EnumOS getOSType()
/*    */   {
/* 11 */     String s = System.getProperty("os.name").toLowerCase();
/* 12 */     return s.contains("unix") ? EnumOS.LINUX : s.contains("linux") ? EnumOS.LINUX : s.contains("sunos") ? EnumOS.SOLARIS : s.contains("solaris") ? EnumOS.SOLARIS : s.contains("mac") ? EnumOS.OSX : s.contains("win") ? EnumOS.WINDOWS : EnumOS.UNKNOWN;
/*    */   }
/*    */   
/*    */   public static <V> V func_181617_a(FutureTask<V> p_181617_0_, Logger p_181617_1_)
/*    */   {
/*    */     try
/*    */     {
/* 19 */       p_181617_0_.run();
/* 20 */       return (V)p_181617_0_.get();
/*    */     }
/*    */     catch (ExecutionException executionexception)
/*    */     {
/* 24 */       p_181617_1_.fatal("Error executing task", executionexception);
/*    */     }
/*    */     catch (InterruptedException interruptedexception)
/*    */     {
/* 28 */       p_181617_1_.fatal("Error executing task", interruptedexception);
/*    */     }
/*    */     
/* 31 */     return null;
/*    */   }
/*    */   
/*    */   public static enum EnumOS
/*    */   {
/* 36 */     LINUX, 
/* 37 */     SOLARIS, 
/* 38 */     WINDOWS, 
/* 39 */     OSX, 
/* 40 */     UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */