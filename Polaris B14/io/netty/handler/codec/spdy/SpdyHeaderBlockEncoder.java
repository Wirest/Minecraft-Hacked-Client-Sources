/*    */ package io.netty.handler.codec.spdy;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufAllocator;
/*    */ import io.netty.util.internal.PlatformDependent;
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
/*    */ abstract class SpdyHeaderBlockEncoder
/*    */ {
/*    */   static SpdyHeaderBlockEncoder newInstance(SpdyVersion version, int compressionLevel, int windowBits, int memLevel)
/*    */   {
/* 27 */     if (PlatformDependent.javaVersion() >= 7) {
/* 28 */       return new SpdyHeaderBlockZlibEncoder(version, compressionLevel);
/*    */     }
/*    */     
/* 31 */     return new SpdyHeaderBlockJZlibEncoder(version, compressionLevel, windowBits, memLevel);
/*    */   }
/*    */   
/*    */   abstract ByteBuf encode(ByteBufAllocator paramByteBufAllocator, SpdyHeadersFrame paramSpdyHeadersFrame)
/*    */     throws Exception;
/*    */   
/*    */   abstract void end();
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyHeaderBlockEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */