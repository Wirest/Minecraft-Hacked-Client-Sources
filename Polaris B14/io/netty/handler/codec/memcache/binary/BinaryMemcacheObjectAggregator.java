/*    */ package io.netty.handler.codec.memcache.binary;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.handler.codec.memcache.AbstractMemcacheObjectAggregator;
/*    */ import io.netty.handler.codec.memcache.FullMemcacheMessage;
/*    */ import io.netty.handler.codec.memcache.MemcacheObject;
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
/*    */ public class BinaryMemcacheObjectAggregator
/*    */   extends AbstractMemcacheObjectAggregator<BinaryMemcacheMessage>
/*    */ {
/*    */   public BinaryMemcacheObjectAggregator(int maxContentLength)
/*    */   {
/* 33 */     super(maxContentLength);
/*    */   }
/*    */   
/*    */   protected boolean isStartMessage(MemcacheObject msg) throws Exception
/*    */   {
/* 38 */     return msg instanceof BinaryMemcacheMessage;
/*    */   }
/*    */   
/*    */   protected FullMemcacheMessage beginAggregation(BinaryMemcacheMessage start, ByteBuf content) throws Exception
/*    */   {
/* 43 */     if ((start instanceof BinaryMemcacheRequest)) {
/* 44 */       return toFullRequest((BinaryMemcacheRequest)start, content);
/*    */     }
/*    */     
/* 47 */     if ((start instanceof BinaryMemcacheResponse)) {
/* 48 */       return toFullResponse((BinaryMemcacheResponse)start, content);
/*    */     }
/*    */     
/*    */ 
/* 52 */     throw new Error();
/*    */   }
/*    */   
/*    */   private static FullBinaryMemcacheRequest toFullRequest(BinaryMemcacheRequest request, ByteBuf content) {
/* 56 */     FullBinaryMemcacheRequest fullRequest = new DefaultFullBinaryMemcacheRequest(request.key(), request.extras(), content);
/*    */     
/*    */ 
/* 59 */     fullRequest.setMagic(request.magic());
/* 60 */     fullRequest.setOpcode(request.opcode());
/* 61 */     fullRequest.setKeyLength(request.keyLength());
/* 62 */     fullRequest.setExtrasLength(request.extrasLength());
/* 63 */     fullRequest.setDataType(request.dataType());
/* 64 */     fullRequest.setTotalBodyLength(request.totalBodyLength());
/* 65 */     fullRequest.setOpaque(request.opaque());
/* 66 */     fullRequest.setCas(request.cas());
/* 67 */     fullRequest.setReserved(request.reserved());
/*    */     
/* 69 */     return fullRequest;
/*    */   }
/*    */   
/*    */   private static FullBinaryMemcacheResponse toFullResponse(BinaryMemcacheResponse response, ByteBuf content) {
/* 73 */     FullBinaryMemcacheResponse fullResponse = new DefaultFullBinaryMemcacheResponse(response.key(), response.extras(), content);
/*    */     
/*    */ 
/* 76 */     fullResponse.setMagic(response.magic());
/* 77 */     fullResponse.setOpcode(response.opcode());
/* 78 */     fullResponse.setKeyLength(response.keyLength());
/* 79 */     fullResponse.setExtrasLength(response.extrasLength());
/* 80 */     fullResponse.setDataType(response.dataType());
/* 81 */     fullResponse.setTotalBodyLength(response.totalBodyLength());
/* 82 */     fullResponse.setOpaque(response.opaque());
/* 83 */     fullResponse.setCas(response.cas());
/* 84 */     fullResponse.setStatus(response.status());
/*    */     
/* 86 */     return fullResponse;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheObjectAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */