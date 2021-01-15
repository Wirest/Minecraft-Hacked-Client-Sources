/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.internal.TypeParameterMatcher;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MessageToMessageCodec<INBOUND_IN, OUTBOUND_IN>
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*  57 */   private final MessageToMessageEncoder<Object> encoder = new MessageToMessageEncoder()
/*     */   {
/*     */     public boolean acceptOutboundMessage(Object msg) throws Exception
/*     */     {
/*  61 */       return MessageToMessageCodec.this.acceptOutboundMessage(msg);
/*     */     }
/*     */     
/*     */     protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out)
/*     */       throws Exception
/*     */     {
/*  67 */       MessageToMessageCodec.this.encode(ctx, msg, out);
/*     */     }
/*     */   };
/*     */   
/*  71 */   private final MessageToMessageDecoder<Object> decoder = new MessageToMessageDecoder()
/*     */   {
/*     */     public boolean acceptInboundMessage(Object msg) throws Exception
/*     */     {
/*  75 */       return MessageToMessageCodec.this.acceptInboundMessage(msg);
/*     */     }
/*     */     
/*     */     protected void decode(ChannelHandlerContext ctx, Object msg, List<Object> out)
/*     */       throws Exception
/*     */     {
/*  81 */       MessageToMessageCodec.this.decode(ctx, msg, out);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */   private final TypeParameterMatcher inboundMsgMatcher;
/*     */   
/*     */   private final TypeParameterMatcher outboundMsgMatcher;
/*     */   
/*     */ 
/*     */   protected MessageToMessageCodec()
/*     */   {
/*  93 */     this.inboundMsgMatcher = TypeParameterMatcher.find(this, MessageToMessageCodec.class, "INBOUND_IN");
/*  94 */     this.outboundMsgMatcher = TypeParameterMatcher.find(this, MessageToMessageCodec.class, "OUTBOUND_IN");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MessageToMessageCodec(Class<? extends INBOUND_IN> inboundMessageType, Class<? extends OUTBOUND_IN> outboundMessageType)
/*     */   {
/* 105 */     this.inboundMsgMatcher = TypeParameterMatcher.get(inboundMessageType);
/* 106 */     this.outboundMsgMatcher = TypeParameterMatcher.get(outboundMessageType);
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/* 111 */     this.decoder.channelRead(ctx, msg);
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 116 */     this.encoder.write(ctx, msg, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean acceptInboundMessage(Object msg)
/*     */     throws Exception
/*     */   {
/* 125 */     return this.inboundMsgMatcher.match(msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean acceptOutboundMessage(Object msg)
/*     */     throws Exception
/*     */   {
/* 134 */     return this.outboundMsgMatcher.match(msg);
/*     */   }
/*     */   
/*     */   protected abstract void encode(ChannelHandlerContext paramChannelHandlerContext, OUTBOUND_IN paramOUTBOUND_IN, List<Object> paramList)
/*     */     throws Exception;
/*     */   
/*     */   protected abstract void decode(ChannelHandlerContext paramChannelHandlerContext, INBOUND_IN paramINBOUND_IN, List<Object> paramList)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\MessageToMessageCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */