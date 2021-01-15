/*    */ package io.netty.buffer;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ final class UnreleasableByteBuf
/*    */   extends WrappedByteBuf
/*    */ {
/*    */   private SwappedByteBuf swappedBuf;
/*    */   
/*    */   UnreleasableByteBuf(ByteBuf buf)
/*    */   {
/* 29 */     super(buf);
/*    */   }
/*    */   
/*    */   public ByteBuf order(ByteOrder endianness)
/*    */   {
/* 34 */     if (endianness == null) {
/* 35 */       throw new NullPointerException("endianness");
/*    */     }
/* 37 */     if (endianness == order()) {
/* 38 */       return this;
/*    */     }
/*    */     
/* 41 */     SwappedByteBuf swappedBuf = this.swappedBuf;
/* 42 */     if (swappedBuf == null) {
/* 43 */       this.swappedBuf = (swappedBuf = new SwappedByteBuf(this));
/*    */     }
/* 45 */     return swappedBuf;
/*    */   }
/*    */   
/*    */   public ByteBuf readSlice(int length)
/*    */   {
/* 50 */     return new UnreleasableByteBuf(this.buf.readSlice(length));
/*    */   }
/*    */   
/*    */   public ByteBuf slice()
/*    */   {
/* 55 */     return new UnreleasableByteBuf(this.buf.slice());
/*    */   }
/*    */   
/*    */   public ByteBuf slice(int index, int length)
/*    */   {
/* 60 */     return new UnreleasableByteBuf(this.buf.slice(index, length));
/*    */   }
/*    */   
/*    */   public ByteBuf duplicate()
/*    */   {
/* 65 */     return new UnreleasableByteBuf(this.buf.duplicate());
/*    */   }
/*    */   
/*    */   public ByteBuf retain(int increment)
/*    */   {
/* 70 */     return this;
/*    */   }
/*    */   
/*    */   public ByteBuf retain()
/*    */   {
/* 75 */     return this;
/*    */   }
/*    */   
/*    */   public ByteBuf touch()
/*    */   {
/* 80 */     return this;
/*    */   }
/*    */   
/*    */   public ByteBuf touch(Object hint)
/*    */   {
/* 85 */     return this;
/*    */   }
/*    */   
/*    */   public boolean release()
/*    */   {
/* 90 */     return false;
/*    */   }
/*    */   
/*    */   public boolean release(int decrement)
/*    */   {
/* 95 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\UnreleasableByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */