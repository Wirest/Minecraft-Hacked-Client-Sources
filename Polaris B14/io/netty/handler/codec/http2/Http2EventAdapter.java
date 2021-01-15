/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
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
/*    */ public class Http2EventAdapter
/*    */   implements Http2Connection.Listener, Http2FrameListener
/*    */ {
/*    */   public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream)
/*    */     throws Http2Exception
/*    */   {
/* 28 */     return data.readableBytes() + padding;
/*    */   }
/*    */   
/*    */   public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream)
/*    */     throws Http2Exception
/*    */   {}
/*    */   
/*    */   public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream)
/*    */     throws Http2Exception
/*    */   {}
/*    */   
/*    */   public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive)
/*    */     throws Http2Exception
/*    */   {}
/*    */   
/*    */   public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode)
/*    */     throws Http2Exception
/*    */   {}
/*    */   
/*    */   public void onSettingsAckRead(ChannelHandlerContext ctx)
/*    */     throws Http2Exception
/*    */   {}
/*    */   
/*    */   public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings)
/*    */     throws Http2Exception
/*    */   {}
/*    */   
/*    */   public void onPingRead(ChannelHandlerContext ctx, ByteBuf data)
/*    */     throws Http2Exception
/*    */   {}
/*    */   
/*    */   public void onPingAckRead(ChannelHandlerContext ctx, ByteBuf data)
/*    */     throws Http2Exception
/*    */   {}
/*    */   
/*    */   public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding)
/*    */     throws Http2Exception
/*    */   {}
/*    */   
/*    */   public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData)
/*    */     throws Http2Exception
/*    */   {}
/*    */   
/*    */   public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement)
/*    */     throws Http2Exception
/*    */   {}
/*    */   
/*    */   public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload) {}
/*    */   
/*    */   public void streamAdded(Http2Stream stream) {}
/*    */   
/*    */   public void streamActive(Http2Stream stream) {}
/*    */   
/*    */   public void streamHalfClosed(Http2Stream stream) {}
/*    */   
/*    */   public void streamInactive(Http2Stream stream) {}
/*    */   
/*    */   public void streamRemoved(Http2Stream stream) {}
/*    */   
/*    */   public void priorityTreeParentChanged(Http2Stream stream, Http2Stream oldParent) {}
/*    */   
/*    */   public void priorityTreeParentChanging(Http2Stream stream, Http2Stream newParent) {}
/*    */   
/*    */   public void onWeightChanged(Http2Stream stream, short oldWeight) {}
/*    */   
/*    */   public void goingAway() {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2EventAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */