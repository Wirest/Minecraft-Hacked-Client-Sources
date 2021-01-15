/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.embedded.EmbeddedChannel;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.compression.ZlibCodecFactory;
/*     */ import io.netty.handler.codec.compression.ZlibWrapper;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DelegatingDecompressorFrameListener
/*     */   extends Http2FrameListenerDecorator
/*     */ {
/*  41 */   private static final Http2ConnectionAdapter CLEAN_UP_LISTENER = new Http2ConnectionAdapter()
/*     */   {
/*     */     public void streamRemoved(Http2Stream stream) {
/*  44 */       DelegatingDecompressorFrameListener.Http2Decompressor decompressor = DelegatingDecompressorFrameListener.decompressor(stream);
/*  45 */       if (decompressor != null) {
/*  46 */         DelegatingDecompressorFrameListener.cleanup(stream, decompressor);
/*     */       }
/*     */     }
/*     */   };
/*     */   private final Http2Connection connection;
/*     */   private final boolean strict;
/*     */   private boolean flowControllerInitialized;
/*     */   
/*     */   public DelegatingDecompressorFrameListener(Http2Connection connection, Http2FrameListener listener)
/*     */   {
/*  56 */     this(connection, listener, true);
/*     */   }
/*     */   
/*     */   public DelegatingDecompressorFrameListener(Http2Connection connection, Http2FrameListener listener, boolean strict)
/*     */   {
/*  61 */     super(listener);
/*  62 */     this.connection = connection;
/*  63 */     this.strict = strict;
/*     */     
/*  65 */     connection.addListener(CLEAN_UP_LISTENER);
/*     */   }
/*     */   
/*     */   public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream)
/*     */     throws Http2Exception
/*     */   {
/*  71 */     Http2Stream stream = this.connection.stream(streamId);
/*  72 */     Http2Decompressor decompressor = decompressor(stream);
/*  73 */     if (decompressor == null)
/*     */     {
/*  75 */       return this.listener.onDataRead(ctx, streamId, data, padding, endOfStream);
/*     */     }
/*     */     
/*  78 */     EmbeddedChannel channel = decompressor.decompressor();
/*  79 */     int compressedBytes = data.readableBytes() + padding;
/*  80 */     int processedBytes = 0;
/*  81 */     decompressor.incrementCompressedBytes(compressedBytes);
/*     */     try
/*     */     {
/*  84 */       channel.writeInbound(new Object[] { data.retain() });
/*  85 */       ByteBuf buf = nextReadableBuf(channel);
/*  86 */       if ((buf == null) && (endOfStream) && (channel.finish())) {
/*  87 */         buf = nextReadableBuf(channel);
/*     */       }
/*  89 */       if (buf == null) {
/*  90 */         if (endOfStream) {
/*  91 */           this.listener.onDataRead(ctx, streamId, Unpooled.EMPTY_BUFFER, padding, true);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*  97 */         decompressor.incrementDecompressedByes(compressedBytes);
/*  98 */         processedBytes = compressedBytes;
/*     */       } else {
/*     */         try {
/* 101 */           decompressor.incrementDecompressedByes(padding);
/*     */           for (;;) {
/* 103 */             ByteBuf nextBuf = nextReadableBuf(channel);
/* 104 */             boolean decompressedEndOfStream = (nextBuf == null) && (endOfStream);
/* 105 */             if ((decompressedEndOfStream) && (channel.finish())) {
/* 106 */               nextBuf = nextReadableBuf(channel);
/* 107 */               decompressedEndOfStream = nextBuf == null;
/*     */             }
/*     */             
/* 110 */             decompressor.incrementDecompressedByes(buf.readableBytes());
/* 111 */             processedBytes += this.listener.onDataRead(ctx, streamId, buf, padding, decompressedEndOfStream);
/* 112 */             if (nextBuf == null) {
/*     */               break;
/*     */             }
/*     */             
/* 116 */             padding = 0;
/* 117 */             buf.release();
/* 118 */             buf = nextBuf;
/*     */           }
/*     */         } finally {
/* 121 */           buf.release();
/*     */         }
/*     */       }
/* 124 */       decompressor.incrementProcessedBytes(processedBytes);
/*     */       
/* 126 */       return processedBytes;
/*     */     }
/*     */     catch (Http2Exception e) {
/* 129 */       decompressor.incrementProcessedBytes(compressedBytes);
/* 130 */       throw e;
/*     */     }
/*     */     catch (Throwable t) {
/* 133 */       decompressor.incrementProcessedBytes(compressedBytes);
/* 134 */       throw Http2Exception.streamError(stream.id(), Http2Error.INTERNAL_ERROR, t, "Decompressor error detected while delegating data read on streamId %d", new Object[] { Integer.valueOf(stream.id()) });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream)
/*     */     throws Http2Exception
/*     */   {
/* 142 */     initDecompressor(streamId, headers, endStream);
/* 143 */     this.listener.onHeadersRead(ctx, streamId, headers, padding, endStream);
/*     */   }
/*     */   
/*     */   public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream)
/*     */     throws Http2Exception
/*     */   {
/* 149 */     initDecompressor(streamId, headers, endStream);
/* 150 */     this.listener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected EmbeddedChannel newContentDecompressor(AsciiString contentEncoding)
/*     */     throws Http2Exception
/*     */   {
/* 163 */     if ((HttpHeaderValues.GZIP.equalsIgnoreCase(contentEncoding)) || (HttpHeaderValues.X_GZIP.equalsIgnoreCase(contentEncoding)))
/*     */     {
/* 165 */       return new EmbeddedChannel(new ChannelHandler[] { ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP) });
/*     */     }
/* 167 */     if ((HttpHeaderValues.DEFLATE.equalsIgnoreCase(contentEncoding)) || (HttpHeaderValues.X_DEFLATE.equalsIgnoreCase(contentEncoding)))
/*     */     {
/* 169 */       ZlibWrapper wrapper = this.strict ? ZlibWrapper.ZLIB : ZlibWrapper.ZLIB_OR_NONE;
/*     */       
/* 171 */       return new EmbeddedChannel(new ChannelHandler[] { ZlibCodecFactory.newZlibDecoder(wrapper) });
/*     */     }
/*     */     
/* 174 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AsciiString getTargetContentEncoding(AsciiString contentEncoding)
/*     */     throws Http2Exception
/*     */   {
/* 187 */     return HttpHeaderValues.IDENTITY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initDecompressor(int streamId, Http2Headers headers, boolean endOfStream)
/*     */     throws Http2Exception
/*     */   {
/* 200 */     Http2Stream stream = this.connection.stream(streamId);
/* 201 */     if (stream == null) {
/* 202 */       return;
/*     */     }
/*     */     
/* 205 */     Http2Decompressor decompressor = decompressor(stream);
/* 206 */     if ((decompressor == null) && (!endOfStream))
/*     */     {
/* 208 */       AsciiString contentEncoding = (AsciiString)headers.get(HttpHeaderNames.CONTENT_ENCODING);
/* 209 */       if (contentEncoding == null) {
/* 210 */         contentEncoding = HttpHeaderValues.IDENTITY;
/*     */       }
/* 212 */       EmbeddedChannel channel = newContentDecompressor(contentEncoding);
/* 213 */       if (channel != null) {
/* 214 */         decompressor = new Http2Decompressor(channel);
/* 215 */         stream.setProperty(Http2Decompressor.class, decompressor);
/*     */         
/*     */ 
/* 218 */         AsciiString targetContentEncoding = getTargetContentEncoding(contentEncoding);
/* 219 */         if (HttpHeaderValues.IDENTITY.equalsIgnoreCase(targetContentEncoding)) {
/* 220 */           headers.remove(HttpHeaderNames.CONTENT_ENCODING);
/*     */         } else {
/* 222 */           headers.set(HttpHeaderNames.CONTENT_ENCODING, targetContentEncoding);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 227 */     if (decompressor != null)
/*     */     {
/*     */ 
/*     */ 
/* 231 */       headers.remove(HttpHeaderNames.CONTENT_LENGTH);
/*     */       
/*     */ 
/*     */ 
/* 235 */       if (!this.flowControllerInitialized) {
/* 236 */         this.flowControllerInitialized = true;
/* 237 */         this.connection.local().flowController(new ConsumedBytesConverter((Http2LocalFlowController)this.connection.local().flowController()));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static Http2Decompressor decompressor(Http2Stream stream) {
/* 243 */     return (Http2Decompressor)(stream == null ? null : stream.getProperty(Http2Decompressor.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void cleanup(Http2Stream stream, Http2Decompressor decompressor)
/*     */   {
/* 254 */     EmbeddedChannel channel = decompressor.decompressor();
/* 255 */     if (channel.finish()) {
/*     */       for (;;) {
/* 257 */         ByteBuf buf = (ByteBuf)channel.readInbound();
/* 258 */         if (buf == null) {
/*     */           break;
/*     */         }
/* 261 */         buf.release();
/*     */       }
/*     */     }
/* 264 */     decompressor = (Http2Decompressor)stream.removeProperty(Http2Decompressor.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static ByteBuf nextReadableBuf(EmbeddedChannel decompressor)
/*     */   {
/*     */     ByteBuf buf;
/*     */     
/*     */ 
/*     */     for (;;)
/*     */     {
/* 276 */       buf = (ByteBuf)decompressor.readInbound();
/* 277 */       if (buf == null) {
/* 278 */         return null;
/*     */       }
/* 280 */       if (buf.isReadable()) break;
/* 281 */       buf.release();
/*     */     }
/*     */     
/* 284 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class ConsumedBytesConverter
/*     */     implements Http2LocalFlowController
/*     */   {
/*     */     private final Http2LocalFlowController flowController;
/*     */     
/*     */     ConsumedBytesConverter(Http2LocalFlowController flowController)
/*     */     {
/* 295 */       this.flowController = ((Http2LocalFlowController)ObjectUtil.checkNotNull(flowController, "flowController"));
/*     */     }
/*     */     
/*     */     public void initialWindowSize(int newWindowSize) throws Http2Exception
/*     */     {
/* 300 */       this.flowController.initialWindowSize(newWindowSize);
/*     */     }
/*     */     
/*     */     public int initialWindowSize()
/*     */     {
/* 305 */       return this.flowController.initialWindowSize();
/*     */     }
/*     */     
/*     */     public int windowSize(Http2Stream stream)
/*     */     {
/* 310 */       return this.flowController.windowSize(stream);
/*     */     }
/*     */     
/*     */     public void incrementWindowSize(ChannelHandlerContext ctx, Http2Stream stream, int delta)
/*     */       throws Http2Exception
/*     */     {
/* 316 */       this.flowController.incrementWindowSize(ctx, stream, delta);
/*     */     }
/*     */     
/*     */     public void receiveFlowControlledFrame(ChannelHandlerContext ctx, Http2Stream stream, ByteBuf data, int padding, boolean endOfStream)
/*     */       throws Http2Exception
/*     */     {
/* 322 */       this.flowController.receiveFlowControlledFrame(ctx, stream, data, padding, endOfStream);
/*     */     }
/*     */     
/*     */     public void consumeBytes(ChannelHandlerContext ctx, Http2Stream stream, int numBytes)
/*     */       throws Http2Exception
/*     */     {
/* 328 */       DelegatingDecompressorFrameListener.Http2Decompressor decompressor = DelegatingDecompressorFrameListener.decompressor(stream);
/* 329 */       DelegatingDecompressorFrameListener.Http2Decompressor copy = null;
/*     */       try {
/* 331 */         if (decompressor != null)
/*     */         {
/* 333 */           copy = new DelegatingDecompressorFrameListener.Http2Decompressor(decompressor);
/*     */           
/* 335 */           numBytes = decompressor.consumeProcessedBytes(numBytes);
/*     */         }
/* 337 */         this.flowController.consumeBytes(ctx, stream, numBytes);
/*     */       } catch (Http2Exception e) {
/* 339 */         if (copy != null) {
/* 340 */           stream.setProperty(DelegatingDecompressorFrameListener.Http2Decompressor.class, copy);
/*     */         }
/* 342 */         throw e;
/*     */       } catch (Throwable t) {
/* 344 */         if (copy != null) {
/* 345 */           stream.setProperty(DelegatingDecompressorFrameListener.Http2Decompressor.class, copy);
/*     */         }
/* 347 */         throw new Http2Exception(Http2Error.INTERNAL_ERROR, "Error while returning bytes to flow control window", t);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public int unconsumedBytes(Http2Stream stream)
/*     */     {
/* 354 */       return this.flowController.unconsumedBytes(stream);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class Http2Decompressor
/*     */   {
/*     */     private final EmbeddedChannel decompressor;
/*     */     private int processed;
/*     */     private int compressed;
/*     */     private int decompressed;
/*     */     
/*     */     Http2Decompressor(Http2Decompressor rhs)
/*     */     {
/* 368 */       this(rhs.decompressor);
/* 369 */       this.processed = rhs.processed;
/* 370 */       this.compressed = rhs.compressed;
/* 371 */       this.decompressed = rhs.decompressed;
/*     */     }
/*     */     
/*     */     Http2Decompressor(EmbeddedChannel decompressor) {
/* 375 */       this.decompressor = decompressor;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     EmbeddedChannel decompressor()
/*     */     {
/* 382 */       return this.decompressor;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     void incrementProcessedBytes(int delta)
/*     */     {
/* 389 */       if (this.processed + delta < 0) {
/* 390 */         throw new IllegalArgumentException("processed bytes cannot be negative");
/*     */       }
/* 392 */       this.processed += delta;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     void incrementCompressedBytes(int delta)
/*     */     {
/* 399 */       if (this.compressed + delta < 0) {
/* 400 */         throw new IllegalArgumentException("compressed bytes cannot be negative");
/*     */       }
/* 402 */       this.compressed += delta;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     void incrementDecompressedByes(int delta)
/*     */     {
/* 410 */       if (this.decompressed + delta < 0) {
/* 411 */         throw new IllegalArgumentException("decompressed bytes cannot be negative");
/*     */       }
/* 413 */       this.decompressed += delta;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     int consumeProcessedBytes(int processedBytes)
/*     */     {
/* 426 */       incrementProcessedBytes(-processedBytes);
/*     */       
/* 428 */       double consumedRatio = processedBytes / this.decompressed;
/* 429 */       int consumedCompressed = Math.min(this.compressed, (int)Math.ceil(this.compressed * consumedRatio));
/* 430 */       incrementDecompressedByes(-Math.min(this.decompressed, (int)Math.ceil(this.decompressed * consumedRatio)));
/* 431 */       incrementCompressedBytes(-consumedCompressed);
/*     */       
/* 433 */       return consumedCompressed;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\DelegatingDecompressorFrameListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */