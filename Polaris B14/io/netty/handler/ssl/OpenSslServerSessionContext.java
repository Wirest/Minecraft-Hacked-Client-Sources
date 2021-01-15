/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import org.apache.tomcat.jni.SSLContext;
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
/*    */ public final class OpenSslServerSessionContext
/*    */   extends OpenSslSessionContext
/*    */ {
/*    */   OpenSslServerSessionContext(long context)
/*    */   {
/* 27 */     super(context);
/*    */   }
/*    */   
/*    */   public void setSessionTimeout(int seconds)
/*    */   {
/* 32 */     if (seconds < 0) {
/* 33 */       throw new IllegalArgumentException();
/*    */     }
/* 35 */     SSLContext.setSessionCacheTimeout(this.context, seconds);
/*    */   }
/*    */   
/*    */   public int getSessionTimeout()
/*    */   {
/* 40 */     return (int)SSLContext.getSessionCacheTimeout(this.context);
/*    */   }
/*    */   
/*    */   public void setSessionCacheSize(int size)
/*    */   {
/* 45 */     if (size < 0) {
/* 46 */       throw new IllegalArgumentException();
/*    */     }
/* 48 */     SSLContext.setSessionCacheSize(this.context, size);
/*    */   }
/*    */   
/*    */   public int getSessionCacheSize()
/*    */   {
/* 53 */     return (int)SSLContext.getSessionCacheSize(this.context);
/*    */   }
/*    */   
/*    */   public void setSessionCacheEnabled(boolean enabled)
/*    */   {
/* 58 */     long mode = enabled ? 2L : 0L;
/* 59 */     SSLContext.setSessionCacheMode(this.context, mode);
/*    */   }
/*    */   
/*    */   public boolean isSessionCacheEnabled()
/*    */   {
/* 64 */     return SSLContext.getSessionCacheMode(this.context) == 2L;
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
/*    */   public boolean setSessionIdContext(byte[] sidCtx)
/*    */   {
/* 77 */     return SSLContext.setSessionIdContext(this.context, sidCtx);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\OpenSslServerSessionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */