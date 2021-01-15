/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.TypeParameterMatcher;
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
/*     */ public abstract class MessageToByteEncoder<I>
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*     */   private final TypeParameterMatcher matcher;
/*     */   private final boolean preferDirect;
/*     */   
/*     */   protected MessageToByteEncoder()
/*     */   {
/*  55 */     this(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected MessageToByteEncoder(Class<? extends I> outboundMessageType)
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
/*     */   protected MessageToByteEncoder(boolean preferDirect)
/*     */   {
/*  73 */     this.matcher = TypeParameterMatcher.find(this, MessageToByteEncoder.class, "I");
/*  74 */     this.preferDirect = preferDirect;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MessageToByteEncoder(Class<? extends I> outboundMessageType, boolean preferDirect)
/*     */   {
/*  86 */     this.matcher = TypeParameterMatcher.get(outboundMessageType);
/*  87 */     this.preferDirect = preferDirect;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean acceptOutboundMessage(Object msg)
/*     */     throws Exception
/*     */   {
/*  95 */     return this.matcher.match(msg);
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 100 */     ByteBuf buf = null;
/*     */     try {
/* 102 */       if (acceptOutboundMessage(msg))
/*     */       {
/* 104 */         I cast = (I)msg;
/* 105 */         buf = allocateBuffer(ctx, cast, this.preferDirect);
/*     */         try {
/* 107 */           encode(ctx, cast, buf);
/*     */         } finally {
/* 109 */           ReferenceCountUtil.release(cast);
/*     */         }
/*     */         
/* 112 */         if (buf.isReadable()) {
/* 113 */           ctx.write(buf, promise);
/*     */         } else {
/* 115 */           buf.release();
/* 116 */           ctx.write(Unpooled.EMPTY_BUFFER, promise);
/*     */         }
/* 118 */         buf = null;
/*     */       } else {
/* 120 */         ctx.write(msg, promise);
/*     */       }
/*     */     } catch (EncoderException e) {
/* 123 */       throw e;
/*     */     } catch (Throwable e) {
/* 125 */       throw new EncoderException(e);
/*     */     } finally {
/* 127 */       if (buf != null) {
/* 128 */         buf.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, I msg, boolean preferDirect)
/*     */     throws Exception
/*     */   {
/* 139 */     if (preferDirect) {
/* 140 */       return ctx.alloc().ioBuffer();
/*     */     }
/* 142 */     return ctx.alloc().heapBuffer();
/*     */   }
/*     */   
/*     */   protected abstract void encode(ChannelHandlerContext paramChannelHandlerContext, I paramI, ByteBuf paramByteBuf)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\MessageToByteEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */