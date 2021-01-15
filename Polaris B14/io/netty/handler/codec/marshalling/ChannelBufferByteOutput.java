/*    */ package io.netty.handler.codec.marshalling;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.io.IOException;
/*    */ import org.jboss.marshalling.ByteOutput;
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
/*    */ 
/*    */ class ChannelBufferByteOutput
/*    */   implements ByteOutput
/*    */ {
/*    */   private final ByteBuf buffer;
/*    */   
/*    */   ChannelBufferByteOutput(ByteBuf buffer)
/*    */   {
/* 36 */     this.buffer = buffer;
/*    */   }
/*    */   
/*    */ 
/*    */   public void close()
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void flush()
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void write(int b)
/*    */     throws IOException
/*    */   {
/* 51 */     this.buffer.writeByte(b);
/*    */   }
/*    */   
/*    */   public void write(byte[] bytes) throws IOException
/*    */   {
/* 56 */     this.buffer.writeBytes(bytes);
/*    */   }
/*    */   
/*    */   public void write(byte[] bytes, int srcIndex, int length) throws IOException
/*    */   {
/* 61 */     this.buffer.writeBytes(bytes, srcIndex, length);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   ByteBuf getBuffer()
/*    */   {
/* 69 */     return this.buffer;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\marshalling\ChannelBufferByteOutput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */