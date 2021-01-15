/*    */ package io.netty.handler.codec.memcache;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
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
/*    */ public class DefaultLastMemcacheContent
/*    */   extends DefaultMemcacheContent
/*    */   implements LastMemcacheContent
/*    */ {
/*    */   public DefaultLastMemcacheContent()
/*    */   {
/* 28 */     super(Unpooled.buffer());
/*    */   }
/*    */   
/*    */   public DefaultLastMemcacheContent(ByteBuf content) {
/* 32 */     super(content);
/*    */   }
/*    */   
/*    */   public LastMemcacheContent retain()
/*    */   {
/* 37 */     super.retain();
/* 38 */     return this;
/*    */   }
/*    */   
/*    */   public LastMemcacheContent retain(int increment)
/*    */   {
/* 43 */     super.retain(increment);
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public LastMemcacheContent touch()
/*    */   {
/* 49 */     super.touch();
/* 50 */     return this;
/*    */   }
/*    */   
/*    */   public LastMemcacheContent touch(Object hint)
/*    */   {
/* 55 */     super.touch(hint);
/* 56 */     return this;
/*    */   }
/*    */   
/*    */   public LastMemcacheContent copy()
/*    */   {
/* 61 */     return new DefaultLastMemcacheContent(content().copy());
/*    */   }
/*    */   
/*    */   public LastMemcacheContent duplicate()
/*    */   {
/* 66 */     return new DefaultLastMemcacheContent(content().duplicate());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\DefaultLastMemcacheContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */