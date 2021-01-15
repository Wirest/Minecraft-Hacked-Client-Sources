/*    */ package io.netty.buffer;
/*    */ 
/*    */ import io.netty.util.ResourceLeak;
/*    */ import java.nio.ByteOrder;
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
/*    */ final class SimpleLeakAwareByteBuf
/*    */   extends WrappedByteBuf
/*    */ {
/*    */   private final ResourceLeak leak;
/*    */   
/*    */   SimpleLeakAwareByteBuf(ByteBuf buf, ResourceLeak leak)
/*    */   {
/* 28 */     super(buf);
/* 29 */     this.leak = leak;
/*    */   }
/*    */   
/*    */   public ByteBuf touch()
/*    */   {
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   public ByteBuf touch(Object hint)
/*    */   {
/* 39 */     return this;
/*    */   }
/*    */   
/*    */   public boolean release()
/*    */   {
/* 44 */     boolean deallocated = super.release();
/* 45 */     if (deallocated) {
/* 46 */       this.leak.close();
/*    */     }
/* 48 */     return deallocated;
/*    */   }
/*    */   
/*    */   public boolean release(int decrement)
/*    */   {
/* 53 */     boolean deallocated = super.release(decrement);
/* 54 */     if (deallocated) {
/* 55 */       this.leak.close();
/*    */     }
/* 57 */     return deallocated;
/*    */   }
/*    */   
/*    */   public ByteBuf order(ByteOrder endianness)
/*    */   {
/* 62 */     this.leak.record();
/* 63 */     if (order() == endianness) {
/* 64 */       return this;
/*    */     }
/* 66 */     return new SimpleLeakAwareByteBuf(super.order(endianness), this.leak);
/*    */   }
/*    */   
/*    */ 
/*    */   public ByteBuf slice()
/*    */   {
/* 72 */     return new SimpleLeakAwareByteBuf(super.slice(), this.leak);
/*    */   }
/*    */   
/*    */   public ByteBuf slice(int index, int length)
/*    */   {
/* 77 */     return new SimpleLeakAwareByteBuf(super.slice(index, length), this.leak);
/*    */   }
/*    */   
/*    */   public ByteBuf duplicate()
/*    */   {
/* 82 */     return new SimpleLeakAwareByteBuf(super.duplicate(), this.leak);
/*    */   }
/*    */   
/*    */   public ByteBuf readSlice(int length)
/*    */   {
/* 87 */     return new SimpleLeakAwareByteBuf(super.readSlice(length), this.leak);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\SimpleLeakAwareByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */