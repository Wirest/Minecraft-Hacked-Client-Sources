/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.Signal;
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
/*     */ public abstract class ReplayingDecoder<S>
/*     */   extends ByteToMessageDecoder
/*     */ {
/* 270 */   static final Signal REPLAY = Signal.valueOf(ReplayingDecoder.class, "REPLAY");
/*     */   
/* 272 */   private final ReplayingDecoderBuffer replayable = new ReplayingDecoderBuffer();
/*     */   private S state;
/* 274 */   private int checkpoint = -1;
/*     */   
/*     */ 
/*     */ 
/*     */   protected ReplayingDecoder()
/*     */   {
/* 280 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ReplayingDecoder(S initialState)
/*     */   {
/* 287 */     this.state = initialState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void checkpoint()
/*     */   {
/* 294 */     this.checkpoint = internalBuffer().readerIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void checkpoint(S state)
/*     */   {
/* 302 */     checkpoint();
/* 303 */     state(state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected S state()
/*     */   {
/* 311 */     return (S)this.state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected S state(S newState)
/*     */   {
/* 319 */     S oldState = this.state;
/* 320 */     this.state = newState;
/* 321 */     return oldState;
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 326 */     RecyclableArrayList out = RecyclableArrayList.newInstance();
/*     */     try {
/* 328 */       this.replayable.terminate();
/* 329 */       callDecode(ctx, internalBuffer(), out);
/* 330 */       decodeLast(ctx, this.replayable, out);
/*     */     } catch (Signal replay) { int size;
/*     */       int i;
/* 333 */       replay.expect(REPLAY); } catch (DecoderException e) { int size;
/*     */       int i;
/* 335 */       throw e;
/*     */     } catch (Exception e) {
/* 337 */       throw new DecoderException(e);
/*     */     } finally {
/*     */       try {
/* 340 */         if (this.cumulation != null) {
/* 341 */           this.cumulation.release();
/* 342 */           this.cumulation = null;
/*     */         }
/* 344 */         int size = out.size();
/* 345 */         for (int i = 0; i < size; i++) {
/* 346 */           ctx.fireChannelRead(out.get(i));
/*     */         }
/* 348 */         if (size > 0)
/*     */         {
/* 350 */           ctx.fireChannelReadComplete();
/*     */         }
/* 352 */         ctx.fireChannelInactive();
/*     */       }
/*     */       finally {
/* 355 */         out.recycle();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */   {
/* 362 */     this.replayable.setCumulation(in);
/*     */     try {
/* 364 */       while (in.isReadable()) {
/* 365 */         int oldReaderIndex = this.checkpoint = in.readerIndex();
/* 366 */         int outSize = out.size();
/* 367 */         S oldState = this.state;
/* 368 */         int oldInputLength = in.readableBytes();
/*     */         try {
/* 370 */           decode(ctx, this.replayable, out);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 376 */           if (ctx.isRemoved()) {
/*     */             break;
/*     */           }
/*     */           
/* 380 */           if (outSize == out.size()) {
/* 381 */             if ((oldInputLength == in.readableBytes()) && (oldState == this.state)) {
/* 382 */               throw new DecoderException(StringUtil.simpleClassName(getClass()) + ".decode() must consume the inbound " + "data or change its state if it did not decode anything.");
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 388 */             continue;
/*     */           }
/*     */         }
/*     */         catch (Signal replay) {
/* 392 */           replay.expect(REPLAY);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 398 */           if (!ctx.isRemoved()) break label163; }
/* 399 */         break;
/*     */         
/*     */         label163:
/*     */         
/* 403 */         int checkpoint = this.checkpoint;
/* 404 */         if (checkpoint >= 0) {
/* 405 */           in.readerIndex(checkpoint);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 410 */         break;
/*     */         
/*     */ 
/* 413 */         if ((oldReaderIndex == in.readerIndex()) && (oldState == this.state)) {
/* 414 */           throw new DecoderException(StringUtil.simpleClassName(getClass()) + ".decode() method must consume the inbound data " + "or change its state if it decoded something.");
/*     */         }
/*     */         
/*     */ 
/* 418 */         if (isSingleDecode()) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     } catch (DecoderException e) {
/* 423 */       throw e;
/*     */     } catch (Throwable cause) {
/* 425 */       throw new DecoderException(cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\ReplayingDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */