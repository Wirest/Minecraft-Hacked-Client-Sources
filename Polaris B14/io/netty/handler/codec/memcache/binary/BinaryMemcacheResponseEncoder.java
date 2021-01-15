/*    */ package io.netty.handler.codec.memcache.binary;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
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
/*    */ public class BinaryMemcacheResponseEncoder
/*    */   extends AbstractBinaryMemcacheEncoder<BinaryMemcacheResponse>
/*    */ {
/*    */   protected void encodeHeader(ByteBuf buf, BinaryMemcacheResponse msg)
/*    */   {
/* 28 */     buf.writeByte(msg.magic());
/* 29 */     buf.writeByte(msg.opcode());
/* 30 */     buf.writeShort(msg.keyLength());
/* 31 */     buf.writeByte(msg.extrasLength());
/* 32 */     buf.writeByte(msg.dataType());
/* 33 */     buf.writeShort(msg.status());
/* 34 */     buf.writeInt(msg.totalBodyLength());
/* 35 */     buf.writeInt(msg.opaque());
/* 36 */     buf.writeLong(msg.cas());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheResponseEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */