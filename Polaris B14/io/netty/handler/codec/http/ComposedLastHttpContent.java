/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
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
/*    */ final class ComposedLastHttpContent
/*    */   implements LastHttpContent
/*    */ {
/*    */   private final HttpHeaders trailingHeaders;
/*    */   private DecoderResult result;
/*    */   
/*    */   ComposedLastHttpContent(HttpHeaders trailingHeaders)
/*    */   {
/* 28 */     this.trailingHeaders = trailingHeaders;
/*    */   }
/*    */   
/*    */   public HttpHeaders trailingHeaders() {
/* 32 */     return this.trailingHeaders;
/*    */   }
/*    */   
/*    */   public LastHttpContent copy()
/*    */   {
/* 37 */     LastHttpContent content = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
/* 38 */     content.trailingHeaders().set(trailingHeaders());
/* 39 */     return content;
/*    */   }
/*    */   
/*    */   public LastHttpContent retain(int increment)
/*    */   {
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public LastHttpContent retain()
/*    */   {
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public LastHttpContent touch()
/*    */   {
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public LastHttpContent touch(Object hint)
/*    */   {
/* 59 */     return this;
/*    */   }
/*    */   
/*    */   public LastHttpContent duplicate()
/*    */   {
/* 64 */     return copy();
/*    */   }
/*    */   
/*    */   public ByteBuf content()
/*    */   {
/* 69 */     return Unpooled.EMPTY_BUFFER;
/*    */   }
/*    */   
/*    */   public DecoderResult decoderResult()
/*    */   {
/* 74 */     return this.result;
/*    */   }
/*    */   
/*    */   public void setDecoderResult(DecoderResult result)
/*    */   {
/* 79 */     this.result = result;
/*    */   }
/*    */   
/*    */   public int refCnt()
/*    */   {
/* 84 */     return 1;
/*    */   }
/*    */   
/*    */   public boolean release()
/*    */   {
/* 89 */     return false;
/*    */   }
/*    */   
/*    */   public boolean release(int decrement)
/*    */   {
/* 94 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\ComposedLastHttpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */