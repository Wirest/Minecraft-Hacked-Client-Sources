/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import com.jcraft.jzlib.Deflater;
/*     */ import com.jcraft.jzlib.JZlib;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.ChannelPromiseNotifier;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JZlibEncoder
/*     */   extends ZlibEncoder
/*     */ {
/*     */   private final int wrapperOverhead;
/*  38 */   private final Deflater z = new Deflater();
/*     */   
/*     */ 
/*     */   private volatile boolean finished;
/*     */   
/*     */ 
/*     */   private volatile ChannelHandlerContext ctx;
/*     */   
/*     */ 
/*     */ 
/*     */   public JZlibEncoder()
/*     */   {
/*  50 */     this(6);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JZlibEncoder(int compressionLevel)
/*     */   {
/*  66 */     this(ZlibWrapper.ZLIB, compressionLevel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JZlibEncoder(ZlibWrapper wrapper)
/*     */   {
/*  77 */     this(wrapper, 6);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JZlibEncoder(ZlibWrapper wrapper, int compressionLevel)
/*     */   {
/*  93 */     this(wrapper, compressionLevel, 15, 8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JZlibEncoder(ZlibWrapper wrapper, int compressionLevel, int windowBits, int memLevel)
/*     */   {
/* 120 */     if ((compressionLevel < 0) || (compressionLevel > 9)) {
/* 121 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*     */     }
/*     */     
/*     */ 
/* 125 */     if ((windowBits < 9) || (windowBits > 15)) {
/* 126 */       throw new IllegalArgumentException("windowBits: " + windowBits + " (expected: 9-15)");
/*     */     }
/*     */     
/* 129 */     if ((memLevel < 1) || (memLevel > 9)) {
/* 130 */       throw new IllegalArgumentException("memLevel: " + memLevel + " (expected: 1-9)");
/*     */     }
/*     */     
/* 133 */     if (wrapper == null) {
/* 134 */       throw new NullPointerException("wrapper");
/*     */     }
/* 136 */     if (wrapper == ZlibWrapper.ZLIB_OR_NONE) {
/* 137 */       throw new IllegalArgumentException("wrapper '" + ZlibWrapper.ZLIB_OR_NONE + "' is not " + "allowed for compression.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 142 */     int resultCode = this.z.init(compressionLevel, windowBits, memLevel, ZlibUtil.convertWrapperType(wrapper));
/*     */     
/*     */ 
/* 145 */     if (resultCode != 0) {
/* 146 */       ZlibUtil.fail(this.z, "initialization failure", resultCode);
/*     */     }
/*     */     
/* 149 */     this.wrapperOverhead = ZlibUtil.wrapperOverhead(wrapper);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JZlibEncoder(byte[] dictionary)
/*     */   {
/* 164 */     this(6, dictionary);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JZlibEncoder(int compressionLevel, byte[] dictionary)
/*     */   {
/* 183 */     this(compressionLevel, 15, 8, dictionary);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JZlibEncoder(int compressionLevel, int windowBits, int memLevel, byte[] dictionary)
/*     */   {
/* 212 */     if ((compressionLevel < 0) || (compressionLevel > 9)) {
/* 213 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*     */     }
/* 215 */     if ((windowBits < 9) || (windowBits > 15)) {
/* 216 */       throw new IllegalArgumentException("windowBits: " + windowBits + " (expected: 9-15)");
/*     */     }
/*     */     
/* 219 */     if ((memLevel < 1) || (memLevel > 9)) {
/* 220 */       throw new IllegalArgumentException("memLevel: " + memLevel + " (expected: 1-9)");
/*     */     }
/*     */     
/* 223 */     if (dictionary == null) {
/* 224 */       throw new NullPointerException("dictionary");
/*     */     }
/*     */     
/* 227 */     int resultCode = this.z.deflateInit(compressionLevel, windowBits, memLevel, JZlib.W_ZLIB);
/*     */     
/*     */ 
/* 230 */     if (resultCode != 0) {
/* 231 */       ZlibUtil.fail(this.z, "initialization failure", resultCode);
/*     */     } else {
/* 233 */       resultCode = this.z.deflateSetDictionary(dictionary, dictionary.length);
/* 234 */       if (resultCode != 0) {
/* 235 */         ZlibUtil.fail(this.z, "failed to set the dictionary", resultCode);
/*     */       }
/*     */     }
/*     */     
/* 239 */     this.wrapperOverhead = ZlibUtil.wrapperOverhead(ZlibWrapper.ZLIB);
/*     */   }
/*     */   
/*     */   public ChannelFuture close()
/*     */   {
/* 244 */     return close(ctx().channel().newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture close(final ChannelPromise promise)
/*     */   {
/* 249 */     ChannelHandlerContext ctx = ctx();
/* 250 */     EventExecutor executor = ctx.executor();
/* 251 */     if (executor.inEventLoop()) {
/* 252 */       return finishEncode(ctx, promise);
/*     */     }
/* 254 */     final ChannelPromise p = ctx.newPromise();
/* 255 */     executor.execute(new Runnable()
/*     */     {
/*     */       public void run() {
/* 258 */         ChannelFuture f = JZlibEncoder.this.finishEncode(JZlibEncoder.access$000(JZlibEncoder.this), p);
/* 259 */         f.addListener(new ChannelPromiseNotifier(new ChannelPromise[] { promise }));
/*     */       }
/* 261 */     });
/* 262 */     return p;
/*     */   }
/*     */   
/*     */   private ChannelHandlerContext ctx()
/*     */   {
/* 267 */     ChannelHandlerContext ctx = this.ctx;
/* 268 */     if (ctx == null) {
/* 269 */       throw new IllegalStateException("not added to a pipeline");
/*     */     }
/* 271 */     return ctx;
/*     */   }
/*     */   
/*     */   public boolean isClosed()
/*     */   {
/* 276 */     return this.finished;
/*     */   }
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception
/*     */   {
/* 281 */     if (this.finished) {
/* 282 */       out.writeBytes(in);
/* 283 */       return;
/*     */     }
/*     */     
/* 286 */     int inputLength = in.readableBytes();
/* 287 */     if (inputLength == 0) {
/* 288 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 293 */       boolean inHasArray = in.hasArray();
/* 294 */       this.z.avail_in = inputLength;
/* 295 */       if (inHasArray) {
/* 296 */         this.z.next_in = in.array();
/* 297 */         this.z.next_in_index = (in.arrayOffset() + in.readerIndex());
/*     */       } else {
/* 299 */         byte[] array = new byte[inputLength];
/* 300 */         in.getBytes(in.readerIndex(), array);
/* 301 */         this.z.next_in = array;
/* 302 */         this.z.next_in_index = 0;
/*     */       }
/* 304 */       int oldNextInIndex = this.z.next_in_index;
/*     */       
/*     */ 
/* 307 */       int maxOutputLength = (int)Math.ceil(inputLength * 1.001D) + 12 + this.wrapperOverhead;
/* 308 */       out.ensureWritable(maxOutputLength);
/* 309 */       this.z.avail_out = maxOutputLength;
/* 310 */       this.z.next_out = out.array();
/* 311 */       this.z.next_out_index = (out.arrayOffset() + out.writerIndex());
/* 312 */       int oldNextOutIndex = this.z.next_out_index;
/*     */       
/*     */       int resultCode;
/*     */       try
/*     */       {
/* 317 */         resultCode = this.z.deflate(2);
/*     */       } finally {
/* 319 */         in.skipBytes(this.z.next_in_index - oldNextInIndex);
/*     */       }
/*     */       
/* 322 */       if (resultCode != 0) {
/* 323 */         ZlibUtil.fail(this.z, "compression failure", resultCode);
/*     */       }
/*     */       
/* 326 */       int outputLength = this.z.next_out_index - oldNextOutIndex;
/* 327 */       if (outputLength > 0) {
/* 328 */         out.writerIndex(out.writerIndex() + outputLength);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 335 */       this.z.next_in = null;
/* 336 */       this.z.next_out = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close(final ChannelHandlerContext ctx, final ChannelPromise promise)
/*     */   {
/* 344 */     ChannelFuture f = finishEncode(ctx, ctx.newPromise());
/* 345 */     f.addListener(new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture f) throws Exception {
/* 348 */         ctx.close(promise);
/*     */       }
/*     */     });
/*     */     
/* 352 */     if (!f.isDone())
/*     */     {
/* 354 */       ctx.executor().schedule(new Runnable()
/*     */       {
/*     */ 
/* 357 */         public void run() { ctx.close(promise); } }, 10L, TimeUnit.SECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private ChannelFuture finishEncode(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */   {
/* 364 */     if (this.finished) {
/* 365 */       promise.setSuccess();
/* 366 */       return promise;
/*     */     }
/* 368 */     this.finished = true;
/*     */     
/*     */     ByteBuf footer;
/*     */     try
/*     */     {
/* 373 */       this.z.next_in = EmptyArrays.EMPTY_BYTES;
/* 374 */       this.z.next_in_index = 0;
/* 375 */       this.z.avail_in = 0;
/*     */       
/*     */ 
/* 378 */       byte[] out = new byte[32];
/* 379 */       this.z.next_out = out;
/* 380 */       this.z.next_out_index = 0;
/* 381 */       this.z.avail_out = out.length;
/*     */       
/*     */ 
/* 384 */       int resultCode = this.z.deflate(4);
/* 385 */       if ((resultCode != 0) && (resultCode != 1)) {
/* 386 */         promise.setFailure(ZlibUtil.deflaterException(this.z, "compression failure", resultCode));
/* 387 */         return promise; }
/* 388 */       ByteBuf footer; if (this.z.next_out_index != 0) {
/* 389 */         footer = Unpooled.wrappedBuffer(out, 0, this.z.next_out_index);
/*     */       } else {
/* 391 */         footer = Unpooled.EMPTY_BUFFER;
/*     */       }
/*     */     } finally {
/* 394 */       this.z.deflateEnd();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 400 */       this.z.next_in = null;
/* 401 */       this.z.next_out = null;
/*     */     }
/* 403 */     return ctx.writeAndFlush(footer, promise);
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 408 */     this.ctx = ctx;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\JZlibEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */