/*    */ package io.netty.handler.codec.spdy;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageCodec;
/*    */ import io.netty.handler.codec.http.HttpHeaders;
/*    */ import io.netty.handler.codec.http.HttpMessage;
/*    */ import io.netty.util.ReferenceCountUtil;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.Queue;
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
/*    */ public class SpdyHttpResponseStreamIdHandler
/*    */   extends MessageToMessageCodec<Object, HttpMessage>
/*    */ {
/* 35 */   private static final Integer NO_ID = Integer.valueOf(-1);
/* 36 */   private final Queue<Integer> ids = new LinkedList();
/*    */   
/*    */   public boolean acceptInboundMessage(Object msg) throws Exception
/*    */   {
/* 40 */     return ((msg instanceof HttpMessage)) || ((msg instanceof SpdyRstStreamFrame));
/*    */   }
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, HttpMessage msg, List<Object> out) throws Exception
/*    */   {
/* 45 */     Integer id = (Integer)this.ids.poll();
/* 46 */     if ((id != null) && (id.intValue() != NO_ID.intValue()) && (!msg.headers().contains(SpdyHttpHeaders.Names.STREAM_ID))) {
/* 47 */       msg.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, id.intValue());
/*    */     }
/*    */     
/* 50 */     out.add(ReferenceCountUtil.retain(msg));
/*    */   }
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception
/*    */   {
/* 55 */     if ((msg instanceof HttpMessage)) {
/* 56 */       boolean contains = ((HttpMessage)msg).headers().contains(SpdyHttpHeaders.Names.STREAM_ID);
/* 57 */       if (!contains) {
/* 58 */         this.ids.add(NO_ID);
/*    */       } else {
/* 60 */         this.ids.add(((HttpMessage)msg).headers().getInt(SpdyHttpHeaders.Names.STREAM_ID));
/*    */       }
/* 62 */     } else if ((msg instanceof SpdyRstStreamFrame)) {
/* 63 */       this.ids.remove(Integer.valueOf(((SpdyRstStreamFrame)msg).streamId()));
/*    */     }
/*    */     
/* 66 */     out.add(ReferenceCountUtil.retain(msg));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyHttpResponseStreamIdHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */