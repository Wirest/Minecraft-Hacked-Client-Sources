/*    */ package io.netty.handler.codec.memcache;
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
/*    */ 
/*    */ public abstract interface LastMemcacheContent
/*    */   extends MemcacheContent
/*    */ {
/* 31 */   public static final LastMemcacheContent EMPTY_LAST_CONTENT = new LastMemcacheContent()
/*    */   {
/*    */     public LastMemcacheContent copy()
/*    */     {
/* 35 */       return EMPTY_LAST_CONTENT;
/*    */     }
/*    */     
/*    */     public LastMemcacheContent retain(int increment)
/*    */     {
/* 40 */       return this;
/*    */     }
/*    */     
/*    */     public LastMemcacheContent retain()
/*    */     {
/* 45 */       return this;
/*    */     }
/*    */     
/*    */     public LastMemcacheContent touch()
/*    */     {
/* 50 */       return this;
/*    */     }
/*    */     
/*    */     public LastMemcacheContent touch(Object hint)
/*    */     {
/* 55 */       return this;
/*    */     }
/*    */     
/*    */     public LastMemcacheContent duplicate()
/*    */     {
/* 60 */       return this;
/*    */     }
/*    */     
/*    */     public ByteBuf content()
/*    */     {
/* 65 */       return Unpooled.EMPTY_BUFFER;
/*    */     }
/*    */     
/*    */     public DecoderResult decoderResult()
/*    */     {
/* 70 */       return DecoderResult.SUCCESS;
/*    */     }
/*    */     
/*    */     public void setDecoderResult(DecoderResult result)
/*    */     {
/* 75 */       throw new UnsupportedOperationException("read only");
/*    */     }
/*    */     
/*    */     public int refCnt()
/*    */     {
/* 80 */       return 1;
/*    */     }
/*    */     
/*    */     public boolean release()
/*    */     {
/* 85 */       return false;
/*    */     }
/*    */     
/*    */     public boolean release(int decrement)
/*    */     {
/* 90 */       return false;
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract LastMemcacheContent copy();
/*    */   
/*    */   public abstract LastMemcacheContent retain(int paramInt);
/*    */   
/*    */   public abstract LastMemcacheContent retain();
/*    */   
/*    */   public abstract LastMemcacheContent touch();
/*    */   
/*    */   public abstract LastMemcacheContent touch(Object paramObject);
/*    */   
/*    */   public abstract LastMemcacheContent duplicate();
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\LastMemcacheContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */