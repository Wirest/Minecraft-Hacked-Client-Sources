/*    */ package io.netty.handler.ssl;
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
/*    */ public final class SslHandshakeCompletionEvent
/*    */ {
/* 25 */   public static final SslHandshakeCompletionEvent SUCCESS = new SslHandshakeCompletionEvent();
/*    */   
/*    */ 
/*    */   private final Throwable cause;
/*    */   
/*    */ 
/*    */   private SslHandshakeCompletionEvent()
/*    */   {
/* 33 */     this.cause = null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public SslHandshakeCompletionEvent(Throwable cause)
/*    */   {
/* 41 */     if (cause == null) {
/* 42 */       throw new NullPointerException("cause");
/*    */     }
/* 44 */     this.cause = cause;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isSuccess()
/*    */   {
/* 51 */     return this.cause == null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Throwable cause()
/*    */   {
/* 59 */     return this.cause;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\SslHandshakeCompletionEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */