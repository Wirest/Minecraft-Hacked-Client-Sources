/*    */ package io.netty.channel.epoll;
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
/*    */ public final class Epoll
/*    */ {
/*    */   private static final Throwable UNAVAILABILITY_CAUSE;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static
/*    */   {
/* 26 */     Throwable cause = null;
/* 27 */     int epollFd = -1;
/* 28 */     int eventFd = -1;
/*    */     try {
/* 30 */       epollFd = Native.epollCreate();
/* 31 */       eventFd = Native.eventFd();
/*    */       
/*    */ 
/*    */ 
/* 35 */       if (epollFd != -1) {
/*    */         try {
/* 37 */           Native.close(epollFd);
/*    */         }
/*    */         catch (Exception ignore) {}
/*    */       }
/*    */       
/* 42 */       if (eventFd != -1) {
/*    */         try {
/* 44 */           Native.close(eventFd);
/*    */         }
/*    */         catch (Exception ignore) {}
/*    */       }
/*    */       
/*    */ 
/*    */ 
/* 51 */       if (cause == null) {
/*    */         break label119;
/*    */       }
/*    */     }
/*    */     catch (Throwable t)
/*    */     {
/* 33 */       cause = t;
/*    */     } finally {
/* 35 */       if (epollFd != -1) {
/*    */         try {
/* 37 */           Native.close(epollFd);
/*    */         }
/*    */         catch (Exception ignore) {}
/*    */       }
/*    */       
/* 42 */       if (eventFd != -1) {
/*    */         try {
/* 44 */           Native.close(eventFd);
/*    */         }
/*    */         catch (Exception ignore) {}
/*    */       }
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 52 */     UNAVAILABILITY_CAUSE = cause; return;
/*    */     label119:
/* 54 */     UNAVAILABILITY_CAUSE = null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isAvailable()
/*    */   {
/* 63 */     return UNAVAILABILITY_CAUSE == null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void ensureAvailability()
/*    */   {
/* 73 */     if (UNAVAILABILITY_CAUSE != null) {
/* 74 */       throw ((Error)new UnsatisfiedLinkError("failed to load the required native library").initCause(UNAVAILABILITY_CAUSE));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Throwable unavailabilityCause()
/*    */   {
/* 86 */     return UNAVAILABILITY_CAUSE;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\Epoll.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */