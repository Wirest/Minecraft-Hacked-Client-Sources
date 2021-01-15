/*    */ package io.netty.handler.codec.memcache.binary;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerAppender;
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
/*    */ public class BinaryMemcacheServerCodec
/*    */   extends ChannelHandlerAppender
/*    */ {
/*    */   public BinaryMemcacheServerCodec()
/*    */   {
/* 30 */     this(8192);
/*    */   }
/*    */   
/*    */   public BinaryMemcacheServerCodec(int decodeChunkSize) {
/* 34 */     add(new BinaryMemcacheRequestDecoder(decodeChunkSize));
/* 35 */     add(new BinaryMemcacheResponseEncoder());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheServerCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */