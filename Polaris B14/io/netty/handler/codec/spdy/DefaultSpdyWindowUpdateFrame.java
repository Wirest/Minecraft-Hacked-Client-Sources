/*    */ package io.netty.handler.codec.spdy;
/*    */ 
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public class DefaultSpdyWindowUpdateFrame
/*    */   implements SpdyWindowUpdateFrame
/*    */ {
/*    */   private int streamId;
/*    */   private int deltaWindowSize;
/*    */   
/*    */   public DefaultSpdyWindowUpdateFrame(int streamId, int deltaWindowSize)
/*    */   {
/* 35 */     setStreamId(streamId);
/* 36 */     setDeltaWindowSize(deltaWindowSize);
/*    */   }
/*    */   
/*    */   public int streamId()
/*    */   {
/* 41 */     return this.streamId;
/*    */   }
/*    */   
/*    */   public SpdyWindowUpdateFrame setStreamId(int streamId)
/*    */   {
/* 46 */     if (streamId < 0) {
/* 47 */       throw new IllegalArgumentException("Stream-ID cannot be negative: " + streamId);
/*    */     }
/*    */     
/* 50 */     this.streamId = streamId;
/* 51 */     return this;
/*    */   }
/*    */   
/*    */   public int deltaWindowSize()
/*    */   {
/* 56 */     return this.deltaWindowSize;
/*    */   }
/*    */   
/*    */   public SpdyWindowUpdateFrame setDeltaWindowSize(int deltaWindowSize)
/*    */   {
/* 61 */     if (deltaWindowSize <= 0) {
/* 62 */       throw new IllegalArgumentException("Delta-Window-Size must be positive: " + deltaWindowSize);
/*    */     }
/*    */     
/*    */ 
/* 66 */     this.deltaWindowSize = deltaWindowSize;
/* 67 */     return this;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 72 */     return StringUtil.simpleClassName(this) + StringUtil.NEWLINE + "--> Stream-ID = " + streamId() + StringUtil.NEWLINE + "--> Delta-Window-Size = " + deltaWindowSize();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\DefaultSpdyWindowUpdateFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */