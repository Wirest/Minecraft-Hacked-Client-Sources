/*    */ package io.netty.handler.codec.spdy;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufAllocator;
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
/*    */ abstract class SpdyHeaderBlockDecoder
/*    */ {
/*    */   static SpdyHeaderBlockDecoder newInstance(SpdyVersion spdyVersion, int maxHeaderSize)
/*    */   {
/* 24 */     return new SpdyHeaderBlockZlibDecoder(spdyVersion, maxHeaderSize);
/*    */   }
/*    */   
/*    */   abstract void decode(ByteBufAllocator paramByteBufAllocator, ByteBuf paramByteBuf, SpdyHeadersFrame paramSpdyHeadersFrame)
/*    */     throws Exception;
/*    */   
/*    */   abstract void endHeaderBlock(SpdyHeadersFrame paramSpdyHeadersFrame)
/*    */     throws Exception;
/*    */   
/*    */   abstract void end();
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyHeaderBlockDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */