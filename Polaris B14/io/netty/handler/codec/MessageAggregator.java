/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.ReferenceCountUtil;
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
/*     */ public abstract class MessageAggregator<I, S, C extends ByteBufHolder, O extends ByteBufHolder>
/*     */   extends MessageToMessageDecoder<I>
/*     */ {
/*     */   private static final int DEFAULT_MAX_COMPOSITEBUFFER_COMPONENTS = 1024;
/*     */   private final int maxContentLength;
/*     */   private O currentMessage;
/*     */   private boolean handlingOversizedMessage;
/*  58 */   private int maxCumulationBufferComponents = 1024;
/*     */   
/*     */ 
/*     */ 
/*     */   private ChannelHandlerContext ctx;
/*     */   
/*     */ 
/*     */   private ChannelFutureListener continueResponseWriteListener;
/*     */   
/*     */ 
/*     */ 
/*     */   protected MessageAggregator(int maxContentLength)
/*     */   {
/*  71 */     validateMaxContentLength(maxContentLength);
/*  72 */     this.maxContentLength = maxContentLength;
/*     */   }
/*     */   
/*     */   protected MessageAggregator(int maxContentLength, Class<? extends I> inboundMessageType) {
/*  76 */     super(inboundMessageType);
/*  77 */     validateMaxContentLength(maxContentLength);
/*  78 */     this.maxContentLength = maxContentLength;
/*     */   }
/*     */   
/*     */   private static void validateMaxContentLength(int maxContentLength) {
/*  82 */     if (maxContentLength <= 0) {
/*  83 */       throw new IllegalArgumentException("maxContentLength must be a positive integer: " + maxContentLength);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean acceptInboundMessage(Object msg)
/*     */     throws Exception
/*     */   {
/*  90 */     if (!super.acceptInboundMessage(msg)) {
/*  91 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  95 */     I in = (I)msg;
/*     */     
/*  97 */     return ((isContentMessage(in)) || (isStartMessage(in))) && (!isAggregated(in));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean isStartMessage(I paramI)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean isContentMessage(I paramI)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean isLastContentMessage(C paramC)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean isAggregated(I paramI)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int maxContentLength()
/*     */   {
/* 141 */     return this.maxContentLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int maxCumulationBufferComponents()
/*     */   {
/* 151 */     return this.maxCumulationBufferComponents;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setMaxCumulationBufferComponents(int maxCumulationBufferComponents)
/*     */   {
/* 162 */     if (maxCumulationBufferComponents < 2) {
/* 163 */       throw new IllegalArgumentException("maxCumulationBufferComponents: " + maxCumulationBufferComponents + " (expected: >= 2)");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 168 */     if (this.ctx == null) {
/* 169 */       this.maxCumulationBufferComponents = maxCumulationBufferComponents;
/*     */     } else {
/* 171 */       throw new IllegalStateException("decoder properties cannot be changed once the decoder is added to a pipeline.");
/*     */     }
/*     */   }
/*     */   
/*     */   public final boolean isHandlingOversizedMessage()
/*     */   {
/* 177 */     return this.handlingOversizedMessage;
/*     */   }
/*     */   
/*     */   protected final ChannelHandlerContext ctx() {
/* 181 */     if (this.ctx == null) {
/* 182 */       throw new IllegalStateException("not added to a pipeline yet");
/*     */     }
/* 184 */     return this.ctx;
/*     */   }
/*     */   
/*     */   protected void decode(final ChannelHandlerContext ctx, I msg, List<Object> out) throws Exception
/*     */   {
/* 189 */     O currentMessage = this.currentMessage;
/*     */     
/* 191 */     if (isStartMessage(msg)) {
/* 192 */       this.handlingOversizedMessage = false;
/* 193 */       if (currentMessage != null) {
/* 194 */         throw new MessageAggregationException();
/*     */       }
/*     */       
/*     */ 
/* 198 */       S m = msg;
/*     */       
/*     */ 
/* 201 */       if ((hasContentLength(m)) && 
/* 202 */         (contentLength(m) > this.maxContentLength))
/*     */       {
/* 204 */         invokeHandleOversizedMessage(ctx, m);
/* 205 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 210 */       Object continueResponse = newContinueResponse(m);
/* 211 */       if (continueResponse != null)
/*     */       {
/* 213 */         ChannelFutureListener listener = this.continueResponseWriteListener;
/* 214 */         if (listener == null) {
/* 215 */           this.continueResponseWriteListener = ( = new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 218 */               if (!future.isSuccess()) {
/* 219 */                 ctx.fireExceptionCaught(future.cause());
/*     */               }
/*     */             }
/*     */           });
/*     */         }
/* 224 */         ctx.writeAndFlush(continueResponse).addListener(listener);
/*     */       }
/*     */       
/* 227 */       if (((m instanceof DecoderResultProvider)) && (!((DecoderResultProvider)m).decoderResult().isSuccess())) { O aggregated;
/*     */         O aggregated;
/* 229 */         if (((m instanceof ByteBufHolder)) && (((ByteBufHolder)m).content().isReadable())) {
/* 230 */           aggregated = beginAggregation(m, ((ByteBufHolder)m).content().retain());
/*     */         } else {
/* 232 */           aggregated = beginAggregation(m, Unpooled.EMPTY_BUFFER);
/*     */         }
/* 234 */         finishAggregation(aggregated);
/* 235 */         out.add(aggregated);
/* 236 */         this.currentMessage = null;
/* 237 */         return;
/*     */       }
/*     */       
/*     */ 
/* 241 */       CompositeByteBuf content = ctx.alloc().compositeBuffer(this.maxCumulationBufferComponents);
/* 242 */       if ((m instanceof ByteBufHolder)) {
/* 243 */         appendPartialContent(content, ((ByteBufHolder)m).content());
/*     */       }
/* 245 */       this.currentMessage = beginAggregation(m, content);
/*     */     }
/* 247 */     else if (isContentMessage(msg))
/*     */     {
/* 249 */       C m = (ByteBufHolder)msg;
/* 250 */       ByteBuf partialContent = ((ByteBufHolder)msg).content();
/* 251 */       boolean isLastContentMessage = isLastContentMessage(m);
/* 252 */       if (this.handlingOversizedMessage) {
/* 253 */         if (isLastContentMessage) {
/* 254 */           this.currentMessage = null;
/*     */         }
/*     */         
/* 257 */         return;
/*     */       }
/*     */       
/* 260 */       if (currentMessage == null) {
/* 261 */         throw new MessageAggregationException();
/*     */       }
/*     */       
/*     */ 
/* 265 */       CompositeByteBuf content = (CompositeByteBuf)currentMessage.content();
/*     */       
/*     */ 
/* 268 */       if (content.readableBytes() > this.maxContentLength - partialContent.readableBytes())
/*     */       {
/*     */ 
/* 271 */         S s = currentMessage;
/* 272 */         invokeHandleOversizedMessage(ctx, s);
/* 273 */         return;
/*     */       }
/*     */       
/*     */ 
/* 277 */       appendPartialContent(content, partialContent);
/*     */       
/*     */ 
/* 280 */       aggregate(currentMessage, m);
/*     */       boolean last;
/*     */       boolean last;
/* 283 */       if ((m instanceof DecoderResultProvider)) {
/* 284 */         DecoderResult decoderResult = ((DecoderResultProvider)m).decoderResult();
/* 285 */         boolean last; if (!decoderResult.isSuccess()) {
/* 286 */           if ((currentMessage instanceof DecoderResultProvider)) {
/* 287 */             ((DecoderResultProvider)currentMessage).setDecoderResult(DecoderResult.failure(decoderResult.cause()));
/*     */           }
/*     */           
/* 290 */           last = true;
/*     */         } else {
/* 292 */           last = isLastContentMessage;
/*     */         }
/*     */       } else {
/* 295 */         last = isLastContentMessage;
/*     */       }
/*     */       
/* 298 */       if (last) {
/* 299 */         finishAggregation(currentMessage);
/*     */         
/*     */ 
/* 302 */         out.add(currentMessage);
/* 303 */         this.currentMessage = null;
/*     */       }
/*     */     } else {
/* 306 */       throw new MessageAggregationException();
/*     */     }
/*     */   }
/*     */   
/*     */   private static void appendPartialContent(CompositeByteBuf content, ByteBuf partialContent) {
/* 311 */     if (partialContent.isReadable()) {
/* 312 */       partialContent.retain();
/* 313 */       content.addComponent(partialContent);
/* 314 */       content.writerIndex(content.writerIndex() + partialContent.readableBytes());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean hasContentLength(S paramS)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract long contentLength(S paramS)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract Object newContinueResponse(S paramS)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract O beginAggregation(S paramS, ByteBuf paramByteBuf)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   protected void aggregate(O aggregated, C content)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected void finishAggregation(O aggregated)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */   private void invokeHandleOversizedMessage(ChannelHandlerContext ctx, S oversized)
/*     */     throws Exception
/*     */   {
/* 359 */     this.handlingOversizedMessage = true;
/* 360 */     this.currentMessage = null;
/*     */     try {
/* 362 */       handleOversizedMessage(ctx, oversized);
/*     */     }
/*     */     finally {
/* 365 */       ReferenceCountUtil.release(oversized);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleOversizedMessage(ChannelHandlerContext ctx, S oversized)
/*     */     throws Exception
/*     */   {
/* 377 */     ctx.fireExceptionCaught(new TooLongFrameException("content length exceeded " + maxContentLength() + " bytes."));
/*     */   }
/*     */   
/*     */ 
/*     */   public void channelInactive(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 384 */     if (this.currentMessage != null) {
/* 385 */       this.currentMessage.release();
/* 386 */       this.currentMessage = null;
/*     */     }
/*     */     
/* 389 */     super.channelInactive(ctx);
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 394 */     this.ctx = ctx;
/*     */   }
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 399 */     super.handlerRemoved(ctx);
/*     */     
/*     */ 
/*     */ 
/* 403 */     if (this.currentMessage != null) {
/* 404 */       this.currentMessage.release();
/* 405 */       this.currentMessage = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\MessageAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */