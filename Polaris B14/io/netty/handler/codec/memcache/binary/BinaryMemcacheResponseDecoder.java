/*    */ package io.netty.handler.codec.memcache.binary;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
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
/*    */ public class BinaryMemcacheResponseDecoder
/*    */   extends AbstractBinaryMemcacheDecoder<BinaryMemcacheResponse>
/*    */ {
/*    */   public BinaryMemcacheResponseDecoder()
/*    */   {
/* 28 */     this(8192);
/*    */   }
/*    */   
/*    */   public BinaryMemcacheResponseDecoder(int chunkSize) {
/* 32 */     super(chunkSize);
/*    */   }
/*    */   
/*    */   protected BinaryMemcacheResponse decodeHeader(ByteBuf in)
/*    */   {
/* 37 */     BinaryMemcacheResponse header = new DefaultBinaryMemcacheResponse();
/* 38 */     header.setMagic(in.readByte());
/* 39 */     header.setOpcode(in.readByte());
/* 40 */     header.setKeyLength(in.readShort());
/* 41 */     header.setExtrasLength(in.readByte());
/* 42 */     header.setDataType(in.readByte());
/* 43 */     header.setStatus(in.readShort());
/* 44 */     header.setTotalBodyLength(in.readInt());
/* 45 */     header.setOpaque(in.readInt());
/* 46 */     header.setCas(in.readLong());
/* 47 */     return header;
/*    */   }
/*    */   
/*    */   protected BinaryMemcacheResponse buildInvalidMessage()
/*    */   {
/* 52 */     return new DefaultBinaryMemcacheResponse("", Unpooled.EMPTY_BUFFER);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */