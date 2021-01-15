/*    */ package io.netty.handler.codec.marshalling;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
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
/*    */ class ChannelBufferByteInput
/*    */   implements ByteInput
/*    */ {
/*    */   private final ByteBuf buffer;
/*    */   
/*    */   ChannelBufferByteInput(ByteBuf buffer)
/*    */   {
/* 31 */     this.buffer = buffer;
/*    */   }
/*    */   
/*    */   public void close()
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public int available()
/*    */     throws IOException
/*    */   {
/* 41 */     return this.buffer.readableBytes();
/*    */   }
/*    */   
/*    */   public int read() throws IOException
/*    */   {
/* 46 */     if (this.buffer.isReadable()) {
/* 47 */       return this.buffer.readByte() & 0xFF;
/*    */     }
/* 49 */     return -1;
/*    */   }
/*    */   
/*    */   public int read(byte[] array) throws IOException
/*    */   {
/* 54 */     return read(array, 0, array.length);
/*    */   }
/*    */   
/*    */   public int read(byte[] dst, int dstIndex, int length) throws IOException
/*    */   {
/* 59 */     int available = available();
/* 60 */     if (available == 0) {
/* 61 */       return -1;
/*    */     }
/*    */     
/* 64 */     length = Math.min(available, length);
/* 65 */     this.buffer.readBytes(dst, dstIndex, length);
/* 66 */     return length;
/*    */   }
/*    */   
/*    */   public long skip(long bytes) throws IOException
/*    */   {
/* 71 */     int readable = this.buffer.readableBytes();
/* 72 */     if (readable < bytes) {
/* 73 */       bytes = readable;
/*    */     }
/* 75 */     this.buffer.readerIndex((int)(this.buffer.readerIndex() + bytes));
/* 76 */     return bytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\marshalling\ChannelBufferByteInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */