/*    */ package io.netty.handler.codec.spdy;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufAllocator;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.handler.codec.AsciiString;
/*    */ import io.netty.util.CharsetUtil;
/*    */ import java.util.Set;
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
/*    */ public class SpdyHeaderBlockRawEncoder
/*    */   extends SpdyHeaderBlockEncoder
/*    */ {
/*    */   private final int version;
/*    */   
/*    */   public SpdyHeaderBlockRawEncoder(SpdyVersion version)
/*    */   {
/* 32 */     if (version == null) {
/* 33 */       throw new NullPointerException("version");
/*    */     }
/* 35 */     this.version = version.getVersion();
/*    */   }
/*    */   
/*    */   private static void setLengthField(ByteBuf buffer, int writerIndex, int length) {
/* 39 */     buffer.setInt(writerIndex, length);
/*    */   }
/*    */   
/*    */   private static void writeLengthField(ByteBuf buffer, int length) {
/* 43 */     buffer.writeInt(length);
/*    */   }
/*    */   
/*    */   public ByteBuf encode(ByteBufAllocator alloc, SpdyHeadersFrame frame) throws Exception
/*    */   {
/* 48 */     Set<CharSequence> names = frame.headers().names();
/* 49 */     int numHeaders = names.size();
/* 50 */     if (numHeaders == 0) {
/* 51 */       return Unpooled.EMPTY_BUFFER;
/*    */     }
/* 53 */     if (numHeaders > 65535) {
/* 54 */       throw new IllegalArgumentException("header block contains too many headers");
/*    */     }
/*    */     
/* 57 */     ByteBuf headerBlock = alloc.heapBuffer();
/* 58 */     writeLengthField(headerBlock, numHeaders);
/* 59 */     for (CharSequence name : names) {
/* 60 */       byte[] nameBytes = AsciiString.getBytes(name, CharsetUtil.UTF_8);
/* 61 */       writeLengthField(headerBlock, nameBytes.length);
/* 62 */       headerBlock.writeBytes(nameBytes);
/* 63 */       int savedIndex = headerBlock.writerIndex();
/* 64 */       int valueLength = 0;
/* 65 */       writeLengthField(headerBlock, valueLength);
/* 66 */       for (CharSequence value : frame.headers().getAll(name)) {
/* 67 */         byte[] valueBytes = AsciiString.getBytes(value, CharsetUtil.UTF_8);
/* 68 */         if (valueBytes.length > 0) {
/* 69 */           headerBlock.writeBytes(valueBytes);
/* 70 */           headerBlock.writeByte(0);
/* 71 */           valueLength += valueBytes.length + 1;
/*    */         }
/*    */       }
/* 74 */       if (valueLength != 0) {
/* 75 */         valueLength--;
/*    */       }
/* 77 */       if (valueLength > 65535) {
/* 78 */         throw new IllegalArgumentException("header exceeds allowable length: " + name);
/*    */       }
/*    */       
/* 81 */       if (valueLength > 0) {
/* 82 */         setLengthField(headerBlock, savedIndex, valueLength);
/* 83 */         headerBlock.writerIndex(headerBlock.writerIndex() - 1);
/*    */       }
/*    */     }
/* 86 */     return headerBlock;
/*    */   }
/*    */   
/*    */   void end() {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyHeaderBlockRawEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */