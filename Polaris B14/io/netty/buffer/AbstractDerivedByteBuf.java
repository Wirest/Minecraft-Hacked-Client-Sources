/*    */ package io.netty.buffer;
/*    */ 
/*    */ import java.nio.ByteBuffer;
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
/*    */ public abstract class AbstractDerivedByteBuf
/*    */   extends AbstractByteBuf
/*    */ {
/*    */   protected AbstractDerivedByteBuf(int maxCapacity)
/*    */   {
/* 28 */     super(maxCapacity);
/*    */   }
/*    */   
/*    */   public final int refCnt()
/*    */   {
/* 33 */     return unwrap().refCnt();
/*    */   }
/*    */   
/*    */   public final ByteBuf retain()
/*    */   {
/* 38 */     unwrap().retain();
/* 39 */     return this;
/*    */   }
/*    */   
/*    */   public final ByteBuf retain(int increment)
/*    */   {
/* 44 */     unwrap().retain(increment);
/* 45 */     return this;
/*    */   }
/*    */   
/*    */   public final ByteBuf touch()
/*    */   {
/* 50 */     unwrap().touch();
/* 51 */     return this;
/*    */   }
/*    */   
/*    */   public final ByteBuf touch(Object hint)
/*    */   {
/* 56 */     unwrap().touch(hint);
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public final boolean release()
/*    */   {
/* 62 */     return unwrap().release();
/*    */   }
/*    */   
/*    */   public final boolean release(int decrement)
/*    */   {
/* 67 */     return unwrap().release(decrement);
/*    */   }
/*    */   
/*    */   public ByteBuffer internalNioBuffer(int index, int length)
/*    */   {
/* 72 */     return nioBuffer(index, length);
/*    */   }
/*    */   
/*    */   public ByteBuffer nioBuffer(int index, int length)
/*    */   {
/* 77 */     return unwrap().nioBuffer(index, length);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\AbstractDerivedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */