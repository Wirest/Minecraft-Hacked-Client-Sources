/*    */ package io.netty.buffer;
/*    */ 
/*    */ import io.netty.util.IllegalReferenceCountException;
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
/*    */ public class DefaultByteBufHolder
/*    */   implements ByteBufHolder
/*    */ {
/*    */   private final ByteBuf data;
/*    */   
/*    */   public DefaultByteBufHolder(ByteBuf data)
/*    */   {
/* 30 */     if (data == null) {
/* 31 */       throw new NullPointerException("data");
/*    */     }
/* 33 */     this.data = data;
/*    */   }
/*    */   
/*    */   public ByteBuf content()
/*    */   {
/* 38 */     if (this.data.refCnt() <= 0) {
/* 39 */       throw new IllegalReferenceCountException(this.data.refCnt());
/*    */     }
/* 41 */     return this.data;
/*    */   }
/*    */   
/*    */   public ByteBufHolder copy()
/*    */   {
/* 46 */     return new DefaultByteBufHolder(this.data.copy());
/*    */   }
/*    */   
/*    */   public ByteBufHolder duplicate()
/*    */   {
/* 51 */     return new DefaultByteBufHolder(this.data.duplicate());
/*    */   }
/*    */   
/*    */   public int refCnt()
/*    */   {
/* 56 */     return this.data.refCnt();
/*    */   }
/*    */   
/*    */   public ByteBufHolder retain()
/*    */   {
/* 61 */     this.data.retain();
/* 62 */     return this;
/*    */   }
/*    */   
/*    */   public ByteBufHolder retain(int increment)
/*    */   {
/* 67 */     this.data.retain(increment);
/* 68 */     return this;
/*    */   }
/*    */   
/*    */   public ByteBufHolder touch()
/*    */   {
/* 73 */     this.data.touch();
/* 74 */     return this;
/*    */   }
/*    */   
/*    */   public ByteBufHolder touch(Object hint)
/*    */   {
/* 79 */     this.data.touch(hint);
/* 80 */     return this;
/*    */   }
/*    */   
/*    */   public boolean release()
/*    */   {
/* 85 */     return this.data.release();
/*    */   }
/*    */   
/*    */   public boolean release(int decrement)
/*    */   {
/* 90 */     return this.data.release(decrement);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 95 */     return StringUtil.simpleClassName(this) + '(' + content().toString() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\DefaultByteBufHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */