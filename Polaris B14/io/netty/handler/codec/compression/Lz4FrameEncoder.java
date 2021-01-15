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
/*     */ import java.util.zip.Checksum;
/*     */ import net.jpountz.lz4.LZ4Compressor;
/*     */ import net.jpountz.lz4.LZ4Exception;
/*     */ import net.jpountz.lz4.LZ4Factory;
/*     */ import net.jpountz.xxhash.StreamingXXHash32;
/*     */ import net.jpountz.xxhash.XXHashFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Lz4FrameEncoder
/*     */   extends MessageToByteEncoder<ByteBuf>
/*     */ {
/*     */   private LZ4Compressor compressor;
/*     */   private Checksum checksum;
/*     */   private final int compressionLevel;
/*     */   private byte[] buffer;
/*     */   private int currentBlockLength;
/*     */   private final int compressedBlockSize;
/*     */   private volatile boolean finished;
/*     */   private volatile ChannelHandlerContext ctx;
/*     */   
/*     */   public Lz4FrameEncoder()
/*     */   {
/* 101 */     this(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Lz4FrameEncoder(boolean highCompressor)
/*     */   {
/* 113 */     this(LZ4Factory.fastestInstance(), highCompressor, 65536, XXHashFactory.fastestInstance().newStreamingHash32(-1756908916).asChecksum());
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
/*     */   public Lz4FrameEncoder(LZ4Factory factory, boolean highCompressor, int blockSize, Checksum checksum)
/*     */   {
/* 130 */     super(false);
/* 131 */     if (factory == null) {
/* 132 */       throw new NullPointerException("factory");
/*     */     }
/* 134 */     if (checksum == null) {
/* 135 */       throw new NullPointerException("checksum");
/*     */     }
/*     */     
/* 138 */     this.compressor = (highCompressor ? factory.highCompressor() : factory.fastCompressor());
/* 139 */     this.checksum = checksum;
/*     */     
/* 141 */     this.compressionLevel = compressionLevel(blockSize);
/* 142 */     this.buffer = new byte[blockSize];
/* 143 */     this.currentBlockLength = 0;
/* 144 */     this.compressedBlockSize = (21 + this.compressor.maxCompressedLength(blockSize));
/*     */     
/* 146 */     this.finished = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int compressionLevel(int blockSize)
/*     */   {
/* 153 */     if ((blockSize < 64) || (blockSize > 33554432)) {
/* 154 */       throw new IllegalArgumentException(String.format("blockSize: %d (expected: %d-%d)", new Object[] { Integer.valueOf(blockSize), Integer.valueOf(64), Integer.valueOf(33554432) }));
/*     */     }
/*     */     
/* 157 */     int compressionLevel = 32 - Integer.numberOfLeadingZeros(blockSize - 1);
/* 158 */     compressionLevel = Math.max(0, compressionLevel - 10);
/* 159 */     return compressionLevel;
/*     */   }
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception
/*     */   {
/* 164 */     if (this.finished) {
/* 165 */       out.writeBytes(in);
/* 166 */       return;
/*     */     }
/*     */     
/* 169 */     int length = in.readableBytes();
/*     */     
/* 171 */     byte[] buffer = this.buffer;
/* 172 */     int blockSize = buffer.length;
/* 173 */     while (this.currentBlockLength + length >= blockSize) {
/* 174 */       int tail = blockSize - this.currentBlockLength;
/* 175 */       in.getBytes(in.readerIndex(), buffer, this.currentBlockLength, tail);
/* 176 */       this.currentBlockLength = blockSize;
/* 177 */       flushBufferedData(out);
/* 178 */       in.skipBytes(tail);
/* 179 */       length -= tail;
/*     */     }
/* 181 */     in.readBytes(buffer, this.currentBlockLength, length);
/* 182 */     this.currentBlockLength += length;
/*     */   }
/*     */   
/*     */   private void flushBufferedData(ByteBuf out) {
/* 186 */     int currentBlockLength = this.currentBlockLength;
/* 187 */     if (currentBlockLength == 0) {
/* 188 */       return;
/*     */     }
/* 190 */     this.checksum.reset();
/* 191 */     this.checksum.update(this.buffer, 0, currentBlockLength);
/* 192 */     int check = (int)this.checksum.getValue();
/*     */     
/* 194 */     out.ensureWritable(this.compressedBlockSize);
/* 195 */     int idx = out.writerIndex();
/* 196 */     byte[] dest = out.array();
/* 197 */     int destOff = out.arrayOffset() + idx;
/*     */     int compressedLength;
/*     */     try {
/* 200 */       compressedLength = this.compressor.compress(this.buffer, 0, currentBlockLength, dest, destOff + 21);
/*     */     } catch (LZ4Exception e) {
/* 202 */       throw new CompressionException(e);
/*     */     }
/*     */     int blockType;
/* 205 */     if (compressedLength >= currentBlockLength) {
/* 206 */       int blockType = 16;
/* 207 */       compressedLength = currentBlockLength;
/* 208 */       System.arraycopy(this.buffer, 0, dest, destOff + 21, currentBlockLength);
/*     */     } else {
/* 210 */       blockType = 32;
/*     */     }
/*     */     
/* 213 */     out.setLong(idx, 5501767354678207339L);
/* 214 */     dest[(destOff + 8)] = ((byte)(blockType | this.compressionLevel));
/* 215 */     writeIntLE(compressedLength, dest, destOff + 9);
/* 216 */     writeIntLE(currentBlockLength, dest, destOff + 13);
/* 217 */     writeIntLE(check, dest, destOff + 17);
/* 218 */     out.writerIndex(idx + 21 + compressedLength);
/* 219 */     currentBlockLength = 0;
/*     */     
/* 221 */     this.currentBlockLength = currentBlockLength;
/*     */   }
/*     */   
/*     */   private ChannelFuture finishEncode(ChannelHandlerContext ctx, ChannelPromise promise) {
/* 225 */     if (this.finished) {
/* 226 */       promise.setSuccess();
/* 227 */       return promise;
/*     */     }
/* 229 */     this.finished = true;
/*     */     
/* 231 */     ByteBuf footer = ctx.alloc().heapBuffer(this.compressor.maxCompressedLength(this.currentBlockLength) + 21);
/*     */     
/* 233 */     flushBufferedData(footer);
/*     */     
/* 235 */     int idx = footer.writerIndex();
/* 236 */     byte[] dest = footer.array();
/* 237 */     int destOff = footer.arrayOffset() + idx;
/* 238 */     footer.setLong(idx, 5501767354678207339L);
/* 239 */     dest[(destOff + 8)] = ((byte)(0x10 | this.compressionLevel));
/* 240 */     writeIntLE(0, dest, destOff + 9);
/* 241 */     writeIntLE(0, dest, destOff + 13);
/* 242 */     writeIntLE(0, dest, destOff + 17);
/* 243 */     footer.writerIndex(idx + 21);
/*     */     
/* 245 */     this.compressor = null;
/* 246 */     this.checksum = null;
/* 247 */     this.buffer = null;
/*     */     
/* 249 */     return ctx.writeAndFlush(footer, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void writeIntLE(int i, byte[] buf, int off)
/*     */   {
/* 256 */     buf[(off++)] = ((byte)i);
/* 257 */     buf[(off++)] = ((byte)(i >>> 8));
/* 258 */     buf[(off++)] = ((byte)(i >>> 16));
/* 259 */     buf[off] = ((byte)(i >>> 24));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 266 */     return this.finished;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture close()
/*     */   {
/* 275 */     return close(ctx().newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture close(final ChannelPromise promise)
/*     */   {
/* 284 */     ChannelHandlerContext ctx = ctx();
/* 285 */     EventExecutor executor = ctx.executor();
/* 286 */     if (executor.inEventLoop()) {
/* 287 */       return finishEncode(ctx, promise);
/*     */     }
/* 289 */     executor.execute(new Runnable()
/*     */     {
/*     */       public void run() {
/* 292 */         ChannelFuture f = Lz4FrameEncoder.this.finishEncode(Lz4FrameEncoder.access$000(Lz4FrameEncoder.this), promise);
/* 293 */         f.addListener(new ChannelPromiseNotifier(new ChannelPromise[] { promise }));
/*     */       }
/* 295 */     });
/* 296 */     return promise;
/*     */   }
/*     */   
/*     */   public void close(final ChannelHandlerContext ctx, final ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 302 */     ChannelFuture f = finishEncode(ctx, ctx.newPromise());
/* 303 */     f.addListener(new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture f) throws Exception {
/* 306 */         ctx.close(promise);
/*     */       }
/*     */     });
/*     */     
/* 310 */     if (!f.isDone())
/*     */     {
/* 312 */       ctx.executor().schedule(new Runnable()
/*     */       {
/*     */ 
/* 315 */         public void run() { ctx.close(promise); } }, 10L, TimeUnit.SECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private ChannelHandlerContext ctx()
/*     */   {
/* 322 */     ChannelHandlerContext ctx = this.ctx;
/* 323 */     if (ctx == null) {
/* 324 */       throw new IllegalStateException("not added to a pipeline");
/*     */     }
/* 326 */     return ctx;
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 331 */     this.ctx = ctx;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Lz4FrameEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */