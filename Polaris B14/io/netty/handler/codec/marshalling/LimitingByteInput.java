/*    */ package io.netty.handler.codec.marshalling;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.jboss.marshalling.ByteInput;
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
/*    */ class LimitingByteInput
/*    */   implements ByteInput
/*    */ {
/* 29 */   private static final TooBigObjectException EXCEPTION = new TooBigObjectException();
/*    */   private final ByteInput input;
/*    */   private final long limit;
/*    */   private long read;
/*    */   
/*    */   LimitingByteInput(ByteInput input, long limit)
/*    */   {
/* 36 */     if (limit <= 0L) {
/* 37 */       throw new IllegalArgumentException("The limit MUST be > 0");
/*    */     }
/* 39 */     this.input = input;
/* 40 */     this.limit = limit;
/*    */   }
/*    */   
/*    */   public void close()
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public int available()
/*    */     throws IOException
/*    */   {
/* 50 */     return readable(this.input.available());
/*    */   }
/*    */   
/*    */   public int read() throws IOException
/*    */   {
/* 55 */     int readable = readable(1);
/* 56 */     if (readable > 0) {
/* 57 */       int b = this.input.read();
/* 58 */       this.read += 1L;
/* 59 */       return b;
/*    */     }
/* 61 */     throw EXCEPTION;
/*    */   }
/*    */   
/*    */   public int read(byte[] array)
/*    */     throws IOException
/*    */   {
/* 67 */     return read(array, 0, array.length);
/*    */   }
/*    */   
/*    */   public int read(byte[] array, int offset, int length) throws IOException
/*    */   {
/* 72 */     int readable = readable(length);
/* 73 */     if (readable > 0) {
/* 74 */       int i = this.input.read(array, offset, readable);
/* 75 */       this.read += i;
/* 76 */       return i;
/*    */     }
/* 78 */     throw EXCEPTION;
/*    */   }
/*    */   
/*    */   public long skip(long bytes)
/*    */     throws IOException
/*    */   {
/* 84 */     int readable = readable((int)bytes);
/* 85 */     if (readable > 0) {
/* 86 */       long i = this.input.skip(readable);
/* 87 */       this.read += i;
/* 88 */       return i;
/*    */     }
/* 90 */     throw EXCEPTION;
/*    */   }
/*    */   
/*    */   private int readable(int length)
/*    */   {
/* 95 */     return (int)Math.min(length, this.limit - this.read);
/*    */   }
/*    */   
/*    */   static final class TooBigObjectException
/*    */     extends IOException
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\marshalling\LimitingByteInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */