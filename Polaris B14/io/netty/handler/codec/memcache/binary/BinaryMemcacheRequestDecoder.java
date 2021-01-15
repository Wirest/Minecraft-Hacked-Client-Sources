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
/*    */ public class BinaryMemcacheRequestDecoder
/*    */   extends AbstractBinaryMemcacheDecoder<BinaryMemcacheRequest>
/*    */ {
/*    */   public BinaryMemcacheRequestDecoder()
/*    */   {
/* 28 */     this(8192);
/*    */   }
/*    */   
/*    */   public BinaryMemcacheRequestDecoder(int chunkSize) {
/* 32 */     super(chunkSize);
/*    */   }
/*    */   
/*    */   protected BinaryMemcacheRequest decodeHeader(ByteBuf in)
/*    */   {
/* 37 */     BinaryMemcacheRequest header = new DefaultBinaryMemcacheRequest();
/* 38 */     header.setMagic(in.readByte());
/* 39 */     header.setOpcode(in.readByte());
/* 40 */     header.setKeyLength(in.readShort());
/* 41 */     header.setExtrasLength(in.readByte());
/* 42 */     header.setDataType(in.readByte());
/* 43 */     header.setReserved(in.readShort());
/* 44 */     header.setTotalBodyLength(in.readInt());
/* 45 */     header.setOpaque(in.readInt());
/* 46 */     header.setCas(in.readLong());
/* 47 */     return header;
/*    */   }
/*    */   
/*    */   protected BinaryMemcacheRequest buildInvalidMessage()
/*    */   {
/* 52 */     return new DefaultBinaryMemcacheRequest("", Unpooled.EMPTY_BUFFER);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */