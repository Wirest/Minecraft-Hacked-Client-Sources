/*     */ package io.netty.handler.stream;
/*     */ 
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelProgressivePromise;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkedWriteHandler
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*  69 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChunkedWriteHandler.class);
/*     */   
/*     */ 
/*  72 */   private final Queue<PendingWrite> queue = new ArrayDeque();
/*     */   
/*     */   private volatile ChannelHandlerContext ctx;
/*     */   
/*     */   private PendingWrite currentWrite;
/*     */   
/*     */ 
/*     */   public ChunkedWriteHandler() {}
/*     */   
/*     */   @Deprecated
/*     */   public ChunkedWriteHandler(int maxPendingWrites)
/*     */   {
/*  84 */     if (maxPendingWrites <= 0) {
/*  85 */       throw new IllegalArgumentException("maxPendingWrites: " + maxPendingWrites + " (expected: > 0)");
/*     */     }
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*  92 */     this.ctx = ctx;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resumeTransfer()
/*     */   {
/*  99 */     final ChannelHandlerContext ctx = this.ctx;
/* 100 */     if (ctx == null) {
/* 101 */       return;
/*     */     }
/* 103 */     if (ctx.executor().inEventLoop()) {
/*     */       try {
/* 105 */         doFlush(ctx);
/*     */       } catch (Exception e) {
/* 107 */         if (logger.isWarnEnabled()) {
/* 108 */           logger.warn("Unexpected exception while sending chunks.", e);
/*     */         }
/*     */         
/*     */       }
/*     */     } else {
/* 113 */       ctx.executor().execute(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/*     */           try {
/* 118 */             ChunkedWriteHandler.this.doFlush(ctx);
/*     */           } catch (Exception e) {
/* 120 */             if (ChunkedWriteHandler.logger.isWarnEnabled()) {
/* 121 */               ChunkedWriteHandler.logger.warn("Unexpected exception while sending chunks.", e);
/*     */             }
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 131 */     this.queue.add(new PendingWrite(msg, promise));
/*     */   }
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 136 */     if (!doFlush(ctx))
/*     */     {
/* 138 */       ctx.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 144 */     doFlush(ctx);
/* 145 */     ctx.fireChannelInactive();
/*     */   }
/*     */   
/*     */   public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 150 */     if (ctx.channel().isWritable())
/*     */     {
/* 152 */       doFlush(ctx);
/*     */     }
/* 154 */     ctx.fireChannelWritabilityChanged();
/*     */   }
/*     */   
/*     */   private void discard(Throwable cause) {
/*     */     for (;;) {
/* 159 */       PendingWrite currentWrite = this.currentWrite;
/*     */       
/* 161 */       if (this.currentWrite == null) {
/* 162 */         currentWrite = (PendingWrite)this.queue.poll();
/*     */       } else {
/* 164 */         this.currentWrite = null;
/*     */       }
/*     */       
/* 167 */       if (currentWrite == null) {
/*     */         break;
/*     */       }
/* 170 */       Object message = currentWrite.msg;
/* 171 */       if ((message instanceof ChunkedInput)) {
/* 172 */         ChunkedInput<?> in = (ChunkedInput)message;
/*     */         try {
/* 174 */           if (!in.isEndOfInput()) {
/* 175 */             if (cause == null) {
/* 176 */               cause = new ClosedChannelException();
/*     */             }
/* 178 */             currentWrite.fail(cause);
/*     */           } else {
/* 180 */             currentWrite.success(in.length());
/*     */           }
/* 182 */           closeInput(in);
/*     */         } catch (Exception e) {
/* 184 */           currentWrite.fail(e);
/* 185 */           logger.warn(ChunkedInput.class.getSimpleName() + ".isEndOfInput() failed", e);
/* 186 */           closeInput(in);
/*     */         }
/*     */       } else {
/* 189 */         if (cause == null) {
/* 190 */           cause = new ClosedChannelException();
/*     */         }
/* 192 */         currentWrite.fail(cause);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean doFlush(ChannelHandlerContext ctx) throws Exception {
/* 198 */     final Channel channel = ctx.channel();
/* 199 */     if (!channel.isActive()) {
/* 200 */       discard(null);
/* 201 */       return false;
/*     */     }
/*     */     
/* 204 */     boolean flushed = false;
/* 205 */     while (channel.isWritable()) {
/* 206 */       if (this.currentWrite == null) {
/* 207 */         this.currentWrite = ((PendingWrite)this.queue.poll());
/*     */       }
/*     */       
/* 210 */       if (this.currentWrite == null) {
/*     */         break;
/*     */       }
/* 213 */       final PendingWrite currentWrite = this.currentWrite;
/* 214 */       final Object pendingMessage = currentWrite.msg;
/*     */       
/* 216 */       if ((pendingMessage instanceof ChunkedInput)) {
/* 217 */         final ChunkedInput<?> chunks = (ChunkedInput)pendingMessage;
/*     */         
/*     */ 
/* 220 */         Object message = null;
/*     */         boolean endOfInput;
/* 222 */         boolean suspend; try { message = chunks.readChunk(ctx);
/* 223 */           endOfInput = chunks.isEndOfInput();
/*     */           boolean suspend;
/* 225 */           if (message == null)
/*     */           {
/* 227 */             suspend = !endOfInput;
/*     */           } else {
/* 229 */             suspend = false;
/*     */           }
/*     */         } catch (Throwable t) {
/* 232 */           this.currentWrite = null;
/*     */           
/* 234 */           if (message != null) {
/* 235 */             ReferenceCountUtil.release(message);
/*     */           }
/*     */           
/* 238 */           currentWrite.fail(t);
/* 239 */           closeInput(chunks);
/* 240 */           break;
/*     */         }
/*     */         
/* 243 */         if (suspend) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 250 */         if (message == null)
/*     */         {
/*     */ 
/* 253 */           message = Unpooled.EMPTY_BUFFER;
/*     */         }
/*     */         
/* 256 */         ChannelFuture f = ctx.write(message);
/* 257 */         if (endOfInput) {
/* 258 */           this.currentWrite = null;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 265 */           f.addListener(new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 268 */               currentWrite.progress(chunks.progress(), chunks.length());
/* 269 */               currentWrite.success(chunks.length());
/* 270 */               ChunkedWriteHandler.closeInput(chunks);
/*     */             }
/*     */           });
/* 273 */         } else if (channel.isWritable()) {
/* 274 */           f.addListener(new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 277 */               if (!future.isSuccess()) {
/* 278 */                 ChunkedWriteHandler.closeInput((ChunkedInput)pendingMessage);
/* 279 */                 currentWrite.fail(future.cause());
/*     */               } else {
/* 281 */                 currentWrite.progress(chunks.progress(), chunks.length());
/*     */               }
/*     */             }
/*     */           });
/*     */         } else {
/* 286 */           f.addListener(new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 289 */               if (!future.isSuccess()) {
/* 290 */                 ChunkedWriteHandler.closeInput((ChunkedInput)pendingMessage);
/* 291 */                 currentWrite.fail(future.cause());
/*     */               } else {
/* 293 */                 currentWrite.progress(chunks.progress(), chunks.length());
/* 294 */                 if (channel.isWritable()) {
/* 295 */                   ChunkedWriteHandler.this.resumeTransfer();
/*     */                 }
/*     */               }
/*     */             }
/*     */           });
/*     */         }
/*     */       } else {
/* 302 */         ctx.write(pendingMessage, currentWrite.promise);
/* 303 */         this.currentWrite = null;
/*     */       }
/*     */       
/*     */ 
/* 307 */       ctx.flush();
/* 308 */       flushed = true;
/*     */       
/* 310 */       if (!channel.isActive()) {
/* 311 */         discard(new ClosedChannelException());
/* 312 */         break;
/*     */       }
/*     */     }
/*     */     
/* 316 */     return flushed;
/*     */   }
/*     */   
/*     */   static void closeInput(ChunkedInput<?> chunks) {
/*     */     try {
/* 321 */       chunks.close();
/*     */     } catch (Throwable t) {
/* 323 */       if (logger.isWarnEnabled()) {
/* 324 */         logger.warn("Failed to close a chunked input.", t);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class PendingWrite {
/*     */     final Object msg;
/*     */     final ChannelPromise promise;
/*     */     
/*     */     PendingWrite(Object msg, ChannelPromise promise) {
/* 334 */       this.msg = msg;
/* 335 */       this.promise = promise;
/*     */     }
/*     */     
/*     */     void fail(Throwable cause) {
/* 339 */       ReferenceCountUtil.release(this.msg);
/* 340 */       this.promise.tryFailure(cause);
/*     */     }
/*     */     
/*     */     void success(long total) {
/* 344 */       if (this.promise.isDone())
/*     */       {
/* 346 */         return;
/*     */       }
/*     */       
/* 349 */       if ((this.promise instanceof ChannelProgressivePromise))
/*     */       {
/* 351 */         ((ChannelProgressivePromise)this.promise).tryProgress(total, total);
/*     */       }
/*     */       
/* 354 */       this.promise.trySuccess();
/*     */     }
/*     */     
/*     */     void progress(long progress, long total) {
/* 358 */       if ((this.promise instanceof ChannelProgressivePromise)) {
/* 359 */         ((ChannelProgressivePromise)this.promise).tryProgress(progress, total);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\stream\ChunkedWriteHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */