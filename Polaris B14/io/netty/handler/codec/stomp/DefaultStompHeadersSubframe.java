/*    */ package io.netty.handler.codec.stomp;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderResult;
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
/*    */ public class DefaultStompHeadersSubframe
/*    */   implements StompHeadersSubframe
/*    */ {
/*    */   protected final StompCommand command;
/* 26 */   protected DecoderResult decoderResult = DecoderResult.SUCCESS;
/* 27 */   protected final StompHeaders headers = new DefaultStompHeaders();
/*    */   
/*    */   public DefaultStompHeadersSubframe(StompCommand command) {
/* 30 */     if (command == null) {
/* 31 */       throw new NullPointerException("command");
/*    */     }
/* 33 */     this.command = command;
/*    */   }
/*    */   
/*    */   public StompCommand command()
/*    */   {
/* 38 */     return this.command;
/*    */   }
/*    */   
/*    */   public StompHeaders headers()
/*    */   {
/* 43 */     return this.headers;
/*    */   }
/*    */   
/*    */   public DecoderResult decoderResult()
/*    */   {
/* 48 */     return this.decoderResult;
/*    */   }
/*    */   
/*    */   public void setDecoderResult(DecoderResult decoderResult)
/*    */   {
/* 53 */     this.decoderResult = decoderResult;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 58 */     return "StompFrame{command=" + this.command + ", headers=" + this.headers + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\stomp\DefaultStompHeadersSubframe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */