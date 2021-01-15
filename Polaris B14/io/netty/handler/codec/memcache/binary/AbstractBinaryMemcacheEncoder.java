/*    */ package io.netty.handler.codec.memcache.binary;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufAllocator;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.memcache.AbstractMemcacheObjectEncoder;
/*    */ import io.netty.util.CharsetUtil;
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
/*    */ public abstract class AbstractBinaryMemcacheEncoder<M extends BinaryMemcacheMessage>
/*    */   extends AbstractMemcacheObjectEncoder<M>
/*    */ {
/*    */   private static final int DEFAULT_BUFFER_SIZE = 24;
/*    */   
/*    */   protected ByteBuf encodeMessage(ChannelHandlerContext ctx, M msg)
/*    */   {
/* 37 */     ByteBuf buf = ctx.alloc().buffer(24);
/*    */     
/* 39 */     encodeHeader(buf, msg);
/* 40 */     encodeExtras(buf, msg.extras());
/* 41 */     encodeKey(buf, msg.key());
/*    */     
/* 43 */     return buf;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private static void encodeExtras(ByteBuf buf, ByteBuf extras)
/*    */   {
/* 53 */     if ((extras == null) || (!extras.isReadable())) {
/* 54 */       return;
/*    */     }
/*    */     
/* 57 */     buf.writeBytes(extras);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private static void encodeKey(ByteBuf buf, String key)
/*    */   {
/* 67 */     if ((key == null) || (key.isEmpty())) {
/* 68 */       return;
/*    */     }
/*    */     
/* 71 */     buf.writeBytes(key.getBytes(CharsetUtil.UTF_8));
/*    */   }
/*    */   
/*    */   protected abstract void encodeHeader(ByteBuf paramByteBuf, M paramM);
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\AbstractBinaryMemcacheEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */