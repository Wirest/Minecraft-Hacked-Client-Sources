/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import io.netty.util.internal.logging.InternalLogger;
/*    */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*    */ import java.lang.reflect.Field;
/*    */ import java.nio.ByteBuffer;
/*    */ import sun.misc.Cleaner;
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
/*    */ final class Cleaner0
/*    */ {
/*    */   private static final long CLEANER_FIELD_OFFSET;
/* 34 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Cleaner0.class);
/*    */   
/*    */   static {
/* 37 */     ByteBuffer direct = ByteBuffer.allocateDirect(1);
/*    */     
/* 39 */     long fieldOffset = -1L;
/* 40 */     if (PlatformDependent0.hasUnsafe()) {
/*    */       try {
/* 42 */         Field cleanerField = direct.getClass().getDeclaredField("cleaner");
/* 43 */         cleanerField.setAccessible(true);
/* 44 */         Cleaner cleaner = (Cleaner)cleanerField.get(direct);
/* 45 */         cleaner.clean();
/* 46 */         fieldOffset = PlatformDependent0.objectFieldOffset(cleanerField);
/*    */       }
/*    */       catch (Throwable t) {
/* 49 */         fieldOffset = -1L;
/*    */       }
/*    */     }
/* 52 */     logger.debug("java.nio.ByteBuffer.cleaner(): {}", fieldOffset != -1L ? "available" : "unavailable");
/* 53 */     CLEANER_FIELD_OFFSET = fieldOffset;
/*    */     
/*    */ 
/* 56 */     freeDirectBuffer(direct);
/*    */   }
/*    */   
/*    */   static void freeDirectBuffer(ByteBuffer buffer) {
/* 60 */     if ((CLEANER_FIELD_OFFSET == -1L) || (!buffer.isDirect())) {
/* 61 */       return;
/*    */     }
/*    */     try {
/* 64 */       Cleaner cleaner = (Cleaner)PlatformDependent0.getObject(buffer, CLEANER_FIELD_OFFSET);
/* 65 */       if (cleaner != null) {
/* 66 */         cleaner.clean();
/*    */       }
/*    */     }
/*    */     catch (Throwable t) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\Cleaner0.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */