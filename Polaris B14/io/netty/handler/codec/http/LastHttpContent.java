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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface LastHttpContent
/*    */   extends HttpContent
/*    */ {
/* 30 */   public static final LastHttpContent EMPTY_LAST_CONTENT = new LastHttpContent()
/*    */   {
/*    */     public ByteBuf content()
/*    */     {
/* 34 */       return Unpooled.EMPTY_BUFFER;
/*    */     }
/*    */     
/*    */     public LastHttpContent copy()
/*    */     {
/* 39 */       return EMPTY_LAST_CONTENT;
/*    */     }
/*    */     
/*    */     public LastHttpContent duplicate()
/*    */     {
/* 44 */       return this;
/*    */     }
/*    */     
/*    */     public HttpHeaders trailingHeaders()
/*    */     {
/* 49 */       return EmptyHttpHeaders.INSTANCE;
/*    */     }
/*    */     
/*    */     public DecoderResult decoderResult()
/*    */     {
/* 54 */       return DecoderResult.SUCCESS;
/*    */     }
/*    */     
/*    */     public void setDecoderResult(DecoderResult result)
/*    */     {
/* 59 */       throw new UnsupportedOperationException("read only");
/*    */     }
/*    */     
/*    */     public int refCnt()
/*    */     {
/* 64 */       return 1;
/*    */     }
/*    */     
/*    */     public LastHttpContent retain()
/*    */     {
/* 69 */       return this;
/*    */     }
/*    */     
/*    */     public LastHttpContent retain(int increment)
/*    */     {
/* 74 */       return this;
/*    */     }
/*    */     
/*    */     public LastHttpContent touch()
/*    */     {
/* 79 */       return this;
/*    */     }
/*    */     
/*    */     public LastHttpContent touch(Object hint)
/*    */     {
/* 84 */       return this;
/*    */     }
/*    */     
/*    */     public boolean release()
/*    */     {
/* 89 */       return false;
/*    */     }
/*    */     
/*    */     public boolean release(int decrement)
/*    */     {
/* 94 */       return false;
/*    */     }
/*    */     
/*    */     public String toString()
/*    */     {
/* 99 */       return "EmptyLastHttpContent";
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract HttpHeaders trailingHeaders();
/*    */   
/*    */   public abstract LastHttpContent copy();
/*    */   
/*    */   public abstract LastHttpContent retain(int paramInt);
/*    */   
/*    */   public abstract LastHttpContent retain();
/*    */   
/*    */   public abstract LastHttpContent touch();
/*    */   
/*    */   public abstract LastHttpContent touch(Object paramObject);
/*    */   
/*    */   public abstract LastHttpContent duplicate();
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\LastHttpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */