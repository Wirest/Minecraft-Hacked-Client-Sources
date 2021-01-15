/*    */ package io.netty.handler.codec.memcache;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public class DefaultMemcacheContent
/*    */   extends AbstractMemcacheObject
/*    */   implements MemcacheContent
/*    */ {
/*    */   private final ByteBuf content;
/*    */   
/*    */   public DefaultMemcacheContent(ByteBuf content)
/*    */   {
/* 32 */     if (content == null) {
/* 33 */       throw new NullPointerException("Content cannot be null.");
/*    */     }
/* 35 */     this.content = content;
/*    */   }
/*    */   
/*    */   public ByteBuf content()
/*    */   {
/* 40 */     return this.content;
/*    */   }
/*    */   
/*    */   public MemcacheContent copy()
/*    */   {
/* 45 */     return new DefaultMemcacheContent(this.content.copy());
/*    */   }
/*    */   
/*    */   public MemcacheContent duplicate()
/*    */   {
/* 50 */     return new DefaultMemcacheContent(this.content.duplicate());
/*    */   }
/*    */   
/*    */   public int refCnt()
/*    */   {
/* 55 */     return this.content.refCnt();
/*    */   }
/*    */   
/*    */   public MemcacheContent retain()
/*    */   {
/* 60 */     this.content.retain();
/* 61 */     return this;
/*    */   }
/*    */   
/*    */   public MemcacheContent retain(int increment)
/*    */   {
/* 66 */     this.content.retain(increment);
/* 67 */     return this;
/*    */   }
/*    */   
/*    */   public MemcacheContent touch()
/*    */   {
/* 72 */     this.content.touch();
/* 73 */     return this;
/*    */   }
/*    */   
/*    */   public MemcacheContent touch(Object hint)
/*    */   {
/* 78 */     this.content.touch(hint);
/* 79 */     return this;
/*    */   }
/*    */   
/*    */   public boolean release()
/*    */   {
/* 84 */     return this.content.release();
/*    */   }
/*    */   
/*    */   public boolean release(int decrement)
/*    */   {
/* 89 */     return this.content.release(decrement);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 94 */     return StringUtil.simpleClassName(this) + "(data: " + content() + ", decoderResult: " + decoderResult() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\DefaultMemcacheContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */