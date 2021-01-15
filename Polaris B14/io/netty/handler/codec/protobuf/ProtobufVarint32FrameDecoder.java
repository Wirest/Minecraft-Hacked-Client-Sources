/*    */ package io.netty.handler.codec.protobuf;
/*    */ 
/*    */ import com.google.protobuf.CodedInputStream;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import io.netty.handler.codec.CorruptedFrameException;
/*    */ import java.util.List;
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
/*    */ public class ProtobufVarint32FrameDecoder
/*    */   extends ByteToMessageDecoder
/*    */ {
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*    */     throws Exception
/*    */   {
/* 49 */     in.markReaderIndex();
/* 50 */     byte[] buf = new byte[5];
/* 51 */     for (int i = 0; i < buf.length; i++) {
/* 52 */       if (!in.isReadable()) {
/* 53 */         in.resetReaderIndex();
/* 54 */         return;
/*    */       }
/*    */       
/* 57 */       buf[i] = in.readByte();
/* 58 */       if (buf[i] >= 0) {
/* 59 */         int length = CodedInputStream.newInstance(buf, 0, i + 1).readRawVarint32();
/* 60 */         if (length < 0) {
/* 61 */           throw new CorruptedFrameException("negative length: " + length);
/*    */         }
/*    */         
/* 64 */         if (in.readableBytes() < length) {
/* 65 */           in.resetReaderIndex();
/* 66 */           return;
/*    */         }
/* 68 */         out.add(in.readBytes(length));
/* 69 */         return;
/*    */       }
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 75 */     throw new CorruptedFrameException("length wider than 32-bit");
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\protobuf\ProtobufVarint32FrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */