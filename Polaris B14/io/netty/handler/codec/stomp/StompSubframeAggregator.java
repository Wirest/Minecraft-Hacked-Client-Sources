/*    */ package io.netty.handler.codec.stomp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
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
/*    */ public class StompSubframeAggregator
/*    */   extends MessageAggregator<StompSubframe, StompHeadersSubframe, StompContentSubframe, StompFrame>
/*    */ {
/*    */   public StompSubframeAggregator(int maxContentLength)
/*    */   {
/* 42 */     super(maxContentLength);
/*    */   }
/*    */   
/*    */   protected boolean isStartMessage(StompSubframe msg) throws Exception
/*    */   {
/* 47 */     return msg instanceof StompHeadersSubframe;
/*    */   }
/*    */   
/*    */   protected boolean isContentMessage(StompSubframe msg) throws Exception
/*    */   {
/* 52 */     return msg instanceof StompContentSubframe;
/*    */   }
/*    */   
/*    */   protected boolean isLastContentMessage(StompContentSubframe msg) throws Exception
/*    */   {
/* 57 */     return msg instanceof LastStompContentSubframe;
/*    */   }
/*    */   
/*    */   protected boolean isAggregated(StompSubframe msg) throws Exception
/*    */   {
/* 62 */     return msg instanceof StompFrame;
/*    */   }
/*    */   
/*    */   protected boolean hasContentLength(StompHeadersSubframe start) throws Exception
/*    */   {
/* 67 */     return start.headers().contains(StompHeaders.CONTENT_LENGTH);
/*    */   }
/*    */   
/*    */   protected long contentLength(StompHeadersSubframe start) throws Exception
/*    */   {
/* 72 */     return start.headers().getLong(StompHeaders.CONTENT_LENGTH, 0L);
/*    */   }
/*    */   
/*    */   protected Object newContinueResponse(StompHeadersSubframe start) throws Exception
/*    */   {
/* 77 */     return null;
/*    */   }
/*    */   
/*    */   protected StompFrame beginAggregation(StompHeadersSubframe start, ByteBuf content) throws Exception
/*    */   {
/* 82 */     StompFrame ret = new DefaultStompFrame(start.command(), content);
/* 83 */     ret.headers().set(start.headers());
/* 84 */     return ret;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\stomp\StompSubframeAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */