/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.ChannelPromiseNotifier;
/*     */ import io.netty.handler.codec.MessageToByteEncoder;
/*     */ import io.netty.util.concurrent.EventExecutor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Bzip2Encoder
/*     */   extends MessageToByteEncoder<ByteBuf>
/*     */ {
/*     */   private static enum State
/*     */   {
/*  42 */     INIT, 
/*  43 */     INIT_BLOCK, 
/*  44 */     WRITE_DATA, 
/*  45 */     CLOSE_BLOCK;
/*     */     
/*     */     private State() {} }
/*  48 */   private State currentState = State.INIT;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  53 */   private final Bzip2BitWriter writer = new Bzip2BitWriter();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int streamBlockSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int streamCRC;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Bzip2BlockCompressor blockCompressor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private volatile boolean finished;
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile ChannelHandlerContext ctx;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Bzip2Encoder()
/*     */   {
/*  84 */     this(9);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Bzip2Encoder(int blockSizeMultiplier)
/*     */   {
/*  95 */     if ((blockSizeMultiplier < 1) || (blockSizeMultiplier > 9)) {
/*  96 */       throw new IllegalArgumentException("blockSizeMultiplier: " + blockSizeMultiplier + " (expected: 1-9)");
/*     */     }
/*     */     
/*  99 */     this.streamBlockSize = (blockSizeMultiplier * 100000);
/*     */   }
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception
/*     */   {
/* 104 */     if (this.finished) {
/* 105 */       out.writeBytes(in);
/*     */     }
/*     */     else {
/*     */       for (;;)
/*     */       {
/* 110 */         switch (this.currentState) {
/*     */         case INIT: 
/* 112 */           out.ensureWritable(4);
/* 113 */           out.writeMedium(4348520);
/* 114 */           out.writeByte(48 + this.streamBlockSize / 100000);
/* 115 */           this.currentState = State.INIT_BLOCK;
/*     */         case INIT_BLOCK: 
/* 117 */           this.blockCompressor = new Bzip2BlockCompressor(this.writer, this.streamBlockSize);
/* 118 */           this.currentState = State.WRITE_DATA;
/*     */         case WRITE_DATA: 
/* 120 */           if (!in.isReadable()) {
/* 121 */             return;
/*     */           }
/* 123 */           Bzip2BlockCompressor blockCompressor = this.blockCompressor;
/* 124 */           int length = in.readableBytes() < blockCompressor.availableSize() ? in.readableBytes() : blockCompressor.availableSize();
/*     */           int offset;
/*     */           byte[] array;
/*     */           int offset;
/* 128 */           if (in.hasArray()) {
/* 129 */             byte[] array = in.array();
/* 130 */             offset = in.arrayOffset() + in.readerIndex();
/*     */           } else {
/* 132 */             array = new byte[length];
/* 133 */             in.getBytes(in.readerIndex(), array);
/* 134 */             offset = 0;
/*     */           }
/* 136 */           int bytesWritten = blockCompressor.write(array, offset, length);
/* 137 */           in.skipBytes(bytesWritten);
/* 138 */           if (!blockCompressor.isFull()) {
/* 139 */             if (in.isReadable()) {
/*     */               break;
/*     */             }
/*     */             
/*     */           }
/*     */           else
/* 145 */             this.currentState = State.CLOSE_BLOCK;
/*     */           break;
/* 147 */         case CLOSE_BLOCK:  closeBlock(out);
/* 148 */           this.currentState = State.INIT_BLOCK;
/* 149 */           break;
/*     */         default: 
/* 151 */           throw new IllegalStateException();
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void closeBlock(ByteBuf out)
/*     */   {
/* 160 */     Bzip2BlockCompressor blockCompressor = this.blockCompressor;
/* 161 */     if (!blockCompressor.isEmpty()) {
/* 162 */       blockCompressor.close(out);
/* 163 */       int blockCRC = blockCompressor.crc();
/* 164 */       this.streamCRC = ((this.streamCRC << 1 | this.streamCRC >>> 31) ^ blockCRC);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 172 */     return this.finished;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture close()
/*     */   {
/* 181 */     return close(ctx().newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture close(final ChannelPromise promise)
/*     */   {
/* 190 */     ChannelHandlerContext ctx = ctx();
/* 191 */     EventExecutor executor = ctx.executor();
/* 192 */     if (executor.inEventLoop()) {
/* 193 */       return finishEncode(ctx, promise);
/*     */     }
/* 195 */     executor.execute(new Runnable()
/*     */     {
/*     */       public void run() {
/* 198 */         ChannelFuture f = Bzip2Encoder.this.finishEncode(Bzip2Encoder.access$000(Bzip2Encoder.this), promise);
/* 199 */         f.addListener(new ChannelPromiseNotifier(new ChannelPromise[] { promise }));
/*     */       }
/* 201 */     });
/* 202 */     return promise;
/*     */   }
/*     */   
/*     */   public void close(final ChannelHandlerContext ctx, final ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 208 */     ChannelFuture f = finishEncode(ctx, ctx.newPromise());
/* 209 */     f.addListener(new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture f) throws Exception {
/* 212 */         ctx.close(promise);
/*     */       }
/*     */     });
/*     */     
/* 216 */     if (!f.isDone())
/*     */     {
/* 218 */       ctx.executor().schedule(new Runnable()
/*     */       {
/*     */ 
/* 221 */         public void run() { ctx.close(promise); } }, 10L, TimeUnit.SECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private ChannelFuture finishEncode(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */   {
/* 228 */     if (this.finished) {
/* 229 */       promise.setSuccess();
/* 230 */       return promise;
/*     */     }
/* 232 */     this.finished = true;
/*     */     
/* 234 */     ByteBuf footer = ctx.alloc().buffer();
/* 235 */     closeBlock(footer);
/*     */     
/* 237 */     int streamCRC = this.streamCRC;
/* 238 */     Bzip2BitWriter writer = this.writer;
/*     */     try {
/* 240 */       writer.writeBits(footer, 24, 1536581L);
/* 241 */       writer.writeBits(footer, 24, 3690640L);
/* 242 */       writer.writeInt(footer, streamCRC);
/* 243 */       writer.flush(footer);
/*     */     } finally {
/* 245 */       this.blockCompressor = null;
/*     */     }
/* 247 */     return ctx.writeAndFlush(footer, promise);
/*     */   }
/*     */   
/*     */   private ChannelHandlerContext ctx() {
/* 251 */     ChannelHandlerContext ctx = this.ctx;
/* 252 */     if (ctx == null) {
/* 253 */       throw new IllegalStateException("not added to a pipeline");
/*     */     }
/* 255 */     return ctx;
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 260 */     this.ctx = ctx;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Bzip2Encoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */