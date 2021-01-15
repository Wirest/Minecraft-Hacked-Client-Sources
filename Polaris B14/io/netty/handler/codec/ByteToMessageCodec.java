/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
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
/*     */ public abstract class ByteToMessageCodec<I>
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*     */   private final TypeParameterMatcher outboundMsgMatcher;
/*     */   private final MessageToByteEncoder<I> encoder;
/*  39 */   private final ByteToMessageDecoder decoder = new ByteToMessageDecoder()
/*     */   {
/*     */     public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*  42 */       ByteToMessageCodec.this.decode(ctx, in, out);
/*     */     }
/*     */     
/*     */     protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */     {
/*  47 */       ByteToMessageCodec.this.decodeLast(ctx, in, out);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */   protected ByteToMessageCodec()
/*     */   {
/*  55 */     this(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ByteToMessageCodec(Class<? extends I> outboundMessageType)
/*     */   {
/*  62 */     this(outboundMessageType, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteToMessageCodec(boolean preferDirect)
/*     */   {
/*  73 */     CodecUtil.ensureNotSharable(this);
/*  74 */     this.outboundMsgMatcher = TypeParameterMatcher.find(this, ByteToMessageCodec.class, "I");
/*  75 */     this.encoder = new Encoder(preferDirect);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteToMessageCodec(Class<? extends I> outboundMessageType, boolean preferDirect)
/*     */   {
/*  87 */     CodecUtil.ensureNotSharable(this);
/*  88 */     this.outboundMsgMatcher = TypeParameterMatcher.get(outboundMessageType);
/*  89 */     this.encoder = new Encoder(preferDirect);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean acceptOutboundMessage(Object msg)
/*     */     throws Exception
/*     */   {
/*  98 */     return this.outboundMsgMatcher.match(msg);
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/* 103 */     this.decoder.channelRead(ctx, msg);
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 108 */     this.encoder.write(ctx, msg, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void encode(ChannelHandlerContext paramChannelHandlerContext, I paramI, ByteBuf paramByteBuf)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void decode(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */   protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 125 */     decode(ctx, in, out);
/*     */   }
/*     */   
/*     */   private final class Encoder extends MessageToByteEncoder<I> {
/*     */     Encoder(boolean preferDirect) {
/* 130 */       super();
/*     */     }
/*     */     
/*     */     public boolean acceptOutboundMessage(Object msg) throws Exception
/*     */     {
/* 135 */       return ByteToMessageCodec.this.acceptOutboundMessage(msg);
/*     */     }
/*     */     
/*     */     protected void encode(ChannelHandlerContext ctx, I msg, ByteBuf out) throws Exception
/*     */     {
/* 140 */       ByteToMessageCodec.this.encode(ctx, msg, out);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\ByteToMessageCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */