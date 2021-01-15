/*    */ package io.netty.handler.codec.memcache;
/*    */ 
/*    */ import io.netty.handler.codec.MessageAggregator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractMemcacheObjectAggregator<H extends MemcacheMessage>
/*    */   extends MessageAggregator<MemcacheObject, H, MemcacheContent, FullMemcacheMessage>
/*    */ {
/*    */   protected AbstractMemcacheObjectAggregator(int maxContentLength)
/*    */   {
/* 48 */     super(maxContentLength);
/*    */   }
/*    */   
/*    */   protected boolean isContentMessage(MemcacheObject msg) throws Exception
/*    */   {
/* 53 */     return msg instanceof MemcacheContent;
/*    */   }
/*    */   
/*    */   protected boolean isLastContentMessage(MemcacheContent msg) throws Exception
/*    */   {
/* 58 */     return msg instanceof LastMemcacheContent;
/*    */   }
/*    */   
/*    */   protected boolean isAggregated(MemcacheObject msg) throws Exception
/*    */   {
/* 63 */     return msg instanceof FullMemcacheMessage;
/*    */   }
/*    */   
/*    */   protected boolean hasContentLength(H start) throws Exception
/*    */   {
/* 68 */     return false;
/*    */   }
/*    */   
/*    */   protected long contentLength(H start) throws Exception
/*    */   {
/* 73 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   protected Object newContinueResponse(H start) throws Exception
/*    */   {
/* 78 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\AbstractMemcacheObjectAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */