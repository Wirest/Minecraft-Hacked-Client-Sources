/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.internal.RecyclableArrayList;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public abstract class ByteToMessageDecoder
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*  76 */   public static final Cumulator MERGE_CUMULATOR = new Cumulator() {
/*     */     public ByteBuf cumulate(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf in) {
/*     */       ByteBuf buffer;
/*     */       ByteBuf buffer;
/*  80 */       if ((cumulation.writerIndex() > cumulation.maxCapacity() - in.readableBytes()) || (cumulation.refCnt() > 1))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */         buffer = ByteToMessageDecoder.expandCumulation(alloc, cumulation, in.readableBytes());
/*     */       } else {
/*  91 */         buffer = cumulation;
/*     */       }
/*  93 */       buffer.writeBytes(in);
/*  94 */       in.release();
/*  95 */       return buffer;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 104 */   public static final Cumulator COMPOSITE_CUMULATOR = new Cumulator()
/*     */   {
/*     */     public ByteBuf cumulate(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf in) {
/*     */       ByteBuf buffer;
/* 108 */       if (cumulation.refCnt() > 1)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */         ByteBuf buffer = ByteToMessageDecoder.expandCumulation(alloc, cumulation, in.readableBytes());
/* 116 */         buffer.writeBytes(in);
/* 117 */         in.release();
/*     */       } else { CompositeByteBuf composite;
/*     */         CompositeByteBuf composite;
/* 120 */         if ((cumulation instanceof CompositeByteBuf)) {
/* 121 */           composite = (CompositeByteBuf)cumulation;
/*     */         } else {
/* 123 */           int readable = cumulation.readableBytes();
/* 124 */           composite = alloc.compositeBuffer();
/* 125 */           composite.addComponent(cumulation).writerIndex(readable);
/*     */         }
/* 127 */         composite.addComponent(in).writerIndex(composite.writerIndex() + in.readableBytes());
/* 128 */         buffer = composite;
/*     */       }
/* 130 */       return buffer;
/*     */     }
/*     */   };
/*     */   
/*     */   ByteBuf cumulation;
/* 135 */   private Cumulator cumulator = MERGE_CUMULATOR;
/*     */   private boolean singleDecode;
/*     */   private boolean first;
/*     */   
/*     */   protected ByteToMessageDecoder() {
/* 140 */     CodecUtil.ensureNotSharable(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSingleDecode(boolean singleDecode)
/*     */   {
/* 150 */     this.singleDecode = singleDecode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSingleDecode()
/*     */   {
/* 160 */     return this.singleDecode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCumulator(Cumulator cumulator)
/*     */   {
/* 167 */     if (cumulator == null) {
/* 168 */       throw new NullPointerException("cumulator");
/*     */     }
/* 170 */     this.cumulator = cumulator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int actualReadableBytes()
/*     */   {
/* 180 */     return internalBuffer().readableBytes();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteBuf internalBuffer()
/*     */   {
/* 189 */     if (this.cumulation != null) {
/* 190 */       return this.cumulation;
/*     */     }
/* 192 */     return Unpooled.EMPTY_BUFFER;
/*     */   }
/*     */   
/*     */   public final void handlerRemoved(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 198 */     ByteBuf buf = internalBuffer();
/* 199 */     int readable = buf.readableBytes();
/* 200 */     if (readable > 0) {
/* 201 */       ByteBuf bytes = buf.readBytes(readable);
/* 202 */       buf.release();
/* 203 */       ctx.fireChannelRead(bytes);
/* 204 */       ctx.fireChannelReadComplete();
/*     */     } else {
/* 206 */       buf.release();
/*     */     }
/* 208 */     this.cumulation = null;
/* 209 */     handlerRemoved0(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void handlerRemoved0(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg)
/*     */     throws Exception
/*     */   {
/* 220 */     if ((msg instanceof ByteBuf)) {
/* 221 */       RecyclableArrayList out = RecyclableArrayList.newInstance();
/*     */       try {
/* 223 */         ByteBuf data = (ByteBuf)msg;
/* 224 */         this.first = (this.cumulation == null);
/* 225 */         if (this.first) {
/* 226 */           this.cumulation = data;
/*     */         } else {
/* 228 */           this.cumulation = this.cumulator.cumulate(ctx.alloc(), this.cumulation, data);
/*     */         }
/* 230 */         callDecode(ctx, this.cumulation, out); } catch (DecoderException e) { int size;
/*     */         int i;
/* 232 */         throw e;
/*     */       } catch (Throwable t) {
/* 234 */         throw new DecoderException(t);
/*     */       } finally {
/* 236 */         if ((this.cumulation != null) && (!this.cumulation.isReadable())) {
/* 237 */           this.cumulation.release();
/* 238 */           this.cumulation = null;
/*     */         }
/* 240 */         int size = out.size();
/*     */         
/* 242 */         for (int i = 0; i < size; i++) {
/* 243 */           ctx.fireChannelRead(out.get(i));
/*     */         }
/* 245 */         out.recycle();
/*     */       }
/*     */     } else {
/* 248 */       ctx.fireChannelRead(msg);
/*     */     }
/*     */   }
/*     */   
/*     */   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 254 */     if ((this.cumulation != null) && (!this.first) && (this.cumulation.refCnt() == 1))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 262 */       this.cumulation.discardSomeReadBytes();
/*     */     }
/* 264 */     ctx.fireChannelReadComplete();
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 269 */     RecyclableArrayList out = RecyclableArrayList.newInstance();
/*     */     try {
/* 271 */       if (this.cumulation != null) {
/* 272 */         callDecode(ctx, this.cumulation, out);
/* 273 */         decodeLast(ctx, this.cumulation, out);
/*     */       } else {
/* 275 */         decodeLast(ctx, Unpooled.EMPTY_BUFFER, out);
/*     */       } } catch (DecoderException e) { int size;
/*     */       int i;
/* 278 */       throw e;
/*     */     } catch (Exception e) {
/* 280 */       throw new DecoderException(e);
/*     */     } finally {
/*     */       try {
/* 283 */         if (this.cumulation != null) {
/* 284 */           this.cumulation.release();
/* 285 */           this.cumulation = null;
/*     */         }
/* 287 */         int size = out.size();
/* 288 */         for (int i = 0; i < size; i++) {
/* 289 */           ctx.fireChannelRead(out.get(i));
/*     */         }
/* 291 */         if (size > 0)
/*     */         {
/* 293 */           ctx.fireChannelReadComplete();
/*     */         }
/* 295 */         ctx.fireChannelInactive();
/*     */       }
/*     */       finally {
/* 298 */         out.recycle();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */   {
/*     */     try
/*     */     {
/* 313 */       while (in.isReadable()) {
/* 314 */         int outSize = out.size();
/* 315 */         int oldInputLength = in.readableBytes();
/* 316 */         decode(ctx, in, out);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 322 */         if (ctx.isRemoved()) {
/*     */           break;
/*     */         }
/*     */         
/* 326 */         if (outSize == out.size()) {
/* 327 */           if (oldInputLength == in.readableBytes()) {
/*     */             break;
/*     */           }
/*     */           
/*     */         }
/*     */         else
/*     */         {
/* 334 */           if (oldInputLength == in.readableBytes()) {
/* 335 */             throw new DecoderException(StringUtil.simpleClassName(getClass()) + ".decode() did not read anything but decoded a message.");
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 340 */           if (isSingleDecode())
/*     */             break;
/*     */         }
/*     */       }
/*     */     } catch (DecoderException e) {
/* 345 */       throw e;
/*     */     } catch (Throwable cause) {
/* 347 */       throw new DecoderException(cause);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void decode(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 371 */     decode(ctx, in, out);
/*     */   }
/*     */   
/*     */   static ByteBuf expandCumulation(ByteBufAllocator alloc, ByteBuf cumulation, int readable) {
/* 375 */     ByteBuf oldCumulation = cumulation;
/* 376 */     cumulation = alloc.buffer(oldCumulation.readableBytes() + readable);
/* 377 */     cumulation.writeBytes(oldCumulation);
/* 378 */     oldCumulation.release();
/* 379 */     return cumulation;
/*     */   }
/*     */   
/*     */   public static abstract interface Cumulator
/*     */   {
/*     */     public abstract ByteBuf cumulate(ByteBufAllocator paramByteBufAllocator, ByteBuf paramByteBuf1, ByteBuf paramByteBuf2);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\ByteToMessageDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */