/*    */ package ch.qos.logback.classic.spi;
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
/*    */ public class PlatformInfo
/*    */ {
/*    */   private static final int UNINITIALIZED = -1;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 25 */   private static int hasJMXObjectName = -1;
/*    */   
/*    */   public static boolean hasJMXObjectName() {
/* 28 */     if (hasJMXObjectName == -1) {
/*    */       try {
/* 30 */         Class.forName("javax.management.ObjectName");
/* 31 */         hasJMXObjectName = 1;
/*    */       } catch (Throwable e) {
/* 33 */         hasJMXObjectName = 0;
/*    */       }
/*    */     }
/* 36 */     return hasJMXObjectName == 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\PlatformInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */