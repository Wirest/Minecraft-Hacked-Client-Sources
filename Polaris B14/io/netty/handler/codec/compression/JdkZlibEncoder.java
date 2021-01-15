/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.ChannelPromiseNotifier;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.Deflater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JdkZlibEncoder
/*     */   extends ZlibEncoder
/*     */ {
/*     */   private final ZlibWrapper wrapper;
/*     */   private final Deflater deflater;
/*     */   private volatile boolean finished;
/*     */   private volatile ChannelHandlerContext ctx;
/*  43 */   private final CRC32 crc = new CRC32();
/*  44 */   private static final byte[] gzipHeader = { 31, -117, 8, 0, 0, 0, 0, 0, 0, 0 };
/*  45 */   private boolean writeHeader = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkZlibEncoder()
/*     */   {
/*  54 */     this(6);
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
/*     */   public JdkZlibEncoder(int compressionLevel)
/*     */   {
/*  69 */     this(ZlibWrapper.ZLIB, compressionLevel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkZlibEncoder(ZlibWrapper wrapper)
/*     */   {
/*  79 */     this(wrapper, 6);
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
/*     */   public JdkZlibEncoder(ZlibWrapper wrapper, int compressionLevel)
/*     */   {
/*  94 */     if ((compressionLevel < 0) || (compressionLevel > 9)) {
/*  95 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*     */     }
/*     */     
/*  98 */     if (wrapper == null) {
/*  99 */       throw new NullPointerException("wrapper");
/*     */     }
/* 101 */     if (wrapper == ZlibWrapper.ZLIB_OR_NONE) {
/* 102 */       throw new IllegalArgumentException("wrapper '" + ZlibWrapper.ZLIB_OR_NONE + "' is not " + "allowed for compression.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 107 */     this.wrapper = wrapper;
/* 108 */     this.deflater = new Deflater(compressionLevel, wrapper != ZlibWrapper.ZLIB);
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
/*     */   public JdkZlibEncoder(byte[] dictionary)
/*     */   {
/* 122 */     this(6, dictionary);
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
/*     */   public JdkZlibEncoder(int compressionLevel, byte[] dictionary)
/*     */   {
/* 140 */     if ((compressionLevel < 0) || (compressionLevel > 9)) {
/* 141 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*     */     }
/*     */     
/* 144 */     if (dictionary == null) {
/* 145 */       throw new NullPointerException("dictionary");
/*     */     }
/*     */     
/* 148 */     this.wrapper = ZlibWrapper.ZLIB;
/* 149 */     this.deflater = new Deflater(compressionLevel);
/* 150 */     this.deflater.setDictionary(dictionary);
/*     */   }
/*     */   
/*     */   public ChannelFuture close()
/*     */   {
/* 155 */     return close(ctx().newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture close(final ChannelPromise promise)
/*     */   {
/* 160 */     ChannelHandlerContext ctx = ctx();
/* 161 */     EventExecutor executor = ctx.executor();
/* 162 */     if (executor.inEventLoop()) {
/* 163 */       return finishEncode(ctx, promise);
/*     */     }
/* 165 */     final ChannelPromise p = ctx.newPromise();
/* 166 */     executor.execute(new Runnable()
/*     */     {
/*     */       public void run() {
/* 169 */         ChannelFuture f = JdkZlibEncoder.this.finishEncode(JdkZlibEncoder.access$000(JdkZlibEncoder.this), p);
/* 170 */         f.addListener(new ChannelPromiseNotifier(new ChannelPromise[] { promise }));
/*     */       }
/* 172 */     });
/* 173 */     return p;
/*     */   }
/*     */   
/*     */   private ChannelHandlerContext ctx()
/*     */   {
/* 178 */     ChannelHandlerContext ctx = this.ctx;
/* 179 */     if (ctx == null) {
/* 180 */       throw new IllegalStateException("not added to a pipeline");
/*     */     }
/* 182 */     return ctx;
/*     */   }
/*     */   
/*     */   public boolean isClosed()
/*     */   {
/* 187 */     return this.finished;
/*     */   }
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, ByteBuf uncompressed, ByteBuf out) throws Exception
/*     */   {
/* 192 */     if (this.finished) {
/* 193 */       out.writeBytes(uncompressed);
/* 194 */       return;
/*     */     }
/*     */     
/* 197 */     int len = uncompressed.readableBytes();
/* 198 */     if (len == 0) {
/*     */       return;
/*     */     }
/*     */     
/*     */     byte[] inAry;
/*     */     int offset;
/* 204 */     if (uncompressed.hasArray())
/*     */     {
/* 206 */       byte[] inAry = uncompressed.array();
/* 207 */       int offset = uncompressed.arrayOffset() + uncompressed.readerIndex();
/*     */       
/* 209 */       uncompressed.skipBytes(len);
/*     */     } else {
/* 211 */       inAry = new byte[len];
/* 212 */       uncompressed.readBytes(inAry);
/* 213 */       offset = 0;
/*     */     }
/*     */     
/* 216 */     if (this.writeHeader) {
/* 217 */       this.writeHeader = false;
/* 218 */       if (this.wrapper == ZlibWrapper.GZIP) {
/* 219 */         out.writeBytes(gzipHeader);
/*     */       }
/*     */     }
/*     */     
/* 223 */     if (this.wrapper == ZlibWrapper.GZIP) {
/* 224 */       this.crc.update(inAry, offset, len);
/*     */     }
/*     */     
/* 227 */     this.deflater.setInput(inAry, offset, len);
/* 228 */     while (!this.deflater.needsInput()) {
/* 229 */       deflate(out);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf msg, boolean preferDirect)
/*     */     throws Exception
/*     */   {
/* 236 */     int sizeEstimate = (int)Math.ceil(msg.readableBytes() * 1.001D) + 12;
/* 237 */     if (this.writeHeader) {
/* 238 */       switch (this.wrapper) {
/*     */       case GZIP: 
/* 240 */         sizeEstimate += gzipHeader.length;
/* 241 */         break;
/*     */       case ZLIB: 
/* 243 */         sizeEstimate += 2;
/*     */       }
/*     */       
/*     */     }
/* 247 */     return ctx.alloc().heapBuffer(sizeEstimate);
/*     */   }
/*     */   
/*     */   public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception
/*     */   {
/* 252 */     ChannelFuture f = finishEncode(ctx, ctx.newPromise());
/* 253 */     f.addListener(new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture f) throws Exception {
/* 256 */         ctx.close(promise);
/*     */       }
/*     */     });
/*     */     
/* 260 */     if (!f.isDone())
/*     */     {
/* 262 */       ctx.executor().schedule(new Runnable()
/*     */       {
/*     */ 
/* 265 */         public void run() { ctx.close(promise); } }, 10L, TimeUnit.SECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private ChannelFuture finishEncode(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */   {
/* 272 */     if (this.finished) {
/* 273 */       promise.setSuccess();
/* 274 */       return promise;
/*     */     }
/*     */     
/* 277 */     this.finished = true;
/* 278 */     ByteBuf footer = ctx.alloc().heapBuffer();
/* 279 */     if ((this.writeHeader) && (this.wrapper == ZlibWrapper.GZIP))
/*     */     {
/* 281 */       this.writeHeader = false;
/* 282 */       footer.writeBytes(gzipHeader);
/*     */     }
/*     */     
/* 285 */     this.deflater.finish();
/*     */     
/* 287 */     while (!this.deflater.finished()) {
/* 288 */       deflate(footer);
/* 289 */       if (!footer.isWritable())
/*     */       {
/* 291 */         ctx.write(footer);
/* 292 */         footer = ctx.alloc().heapBuffer();
/*     */       }
/*     */     }
/* 295 */     if (this.wrapper == ZlibWrapper.GZIP) {
/* 296 */       int crcValue = (int)this.crc.getValue();
/* 297 */       int uncBytes = this.deflater.getTotalIn();
/* 298 */       footer.writeByte(crcValue);
/* 299 */       footer.writeByte(crcValue >>> 8);
/* 300 */       footer.writeByte(crcValue >>> 16);
/* 301 */       footer.writeByte(crcValue >>> 24);
/* 302 */       footer.writeByte(uncBytes);
/* 303 */       footer.writeByte(uncBytes >>> 8);
/* 304 */       footer.writeByte(uncBytes >>> 16);
/* 305 */       footer.writeByte(uncBytes >>> 24);
/*     */     }
/* 307 */     this.deflater.end();
/* 308 */     return ctx.writeAndFlush(footer, promise);
/*     */   }
/*     */   
/*     */   private void deflate(ByteBuf out) {
/*     */     int numBytes;
/*     */     do {
/* 314 */       int writerIndex = out.writerIndex();
/* 315 */       numBytes = this.deflater.deflate(out.array(), out.arrayOffset() + writerIndex, out.writableBytes(), 2);
/*     */       
/* 317 */       out.writerIndex(writerIndex + numBytes);
/* 318 */     } while (numBytes > 0);
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 323 */     this.ctx = ctx;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\JdkZlibEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */