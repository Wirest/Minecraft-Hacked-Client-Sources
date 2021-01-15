/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.ChannelPromiseAggregator;
/*     */ import io.netty.channel.embedded.EmbeddedChannel;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.compression.ZlibCodecFactory;
/*     */ import io.netty.handler.codec.compression.ZlibWrapper;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompressorHttp2ConnectionEncoder
/*     */   extends DefaultHttp2ConnectionEncoder
/*     */ {
/*  41 */   private static final Http2ConnectionAdapter CLEAN_UP_LISTENER = new Http2ConnectionAdapter()
/*     */   {
/*     */     public void streamRemoved(Http2Stream stream) {
/*  44 */       EmbeddedChannel compressor = (EmbeddedChannel)stream.getProperty(CompressorHttp2ConnectionEncoder.class);
/*  45 */       if (compressor != null) {
/*  46 */         CompressorHttp2ConnectionEncoder.cleanup(stream, compressor);
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */   private final int compressionLevel;
/*     */   
/*     */   private final int windowBits;
/*     */   private final int memLevel;
/*     */   
/*     */   public static class Builder
/*     */     extends DefaultHttp2ConnectionEncoder.Builder
/*     */   {
/*  59 */     protected int compressionLevel = 6;
/*  60 */     protected int windowBits = 15;
/*  61 */     protected int memLevel = 8;
/*     */     
/*     */     public Builder compressionLevel(int compressionLevel) {
/*  64 */       this.compressionLevel = compressionLevel;
/*  65 */       return this;
/*     */     }
/*     */     
/*     */     public Builder windowBits(int windowBits) {
/*  69 */       this.windowBits = windowBits;
/*  70 */       return this;
/*     */     }
/*     */     
/*     */     public Builder memLevel(int memLevel) {
/*  74 */       this.memLevel = memLevel;
/*  75 */       return this;
/*     */     }
/*     */     
/*     */     public CompressorHttp2ConnectionEncoder build()
/*     */     {
/*  80 */       return new CompressorHttp2ConnectionEncoder(this);
/*     */     }
/*     */   }
/*     */   
/*     */   protected CompressorHttp2ConnectionEncoder(Builder builder) {
/*  85 */     super(builder);
/*  86 */     if ((builder.compressionLevel < 0) || (builder.compressionLevel > 9)) {
/*  87 */       throw new IllegalArgumentException("compressionLevel: " + builder.compressionLevel + " (expected: 0-9)");
/*     */     }
/*  89 */     if ((builder.windowBits < 9) || (builder.windowBits > 15)) {
/*  90 */       throw new IllegalArgumentException("windowBits: " + builder.windowBits + " (expected: 9-15)");
/*     */     }
/*  92 */     if ((builder.memLevel < 1) || (builder.memLevel > 9)) {
/*  93 */       throw new IllegalArgumentException("memLevel: " + builder.memLevel + " (expected: 1-9)");
/*     */     }
/*  95 */     this.compressionLevel = builder.compressionLevel;
/*  96 */     this.windowBits = builder.windowBits;
/*  97 */     this.memLevel = builder.memLevel;
/*     */     
/*  99 */     connection().addListener(CLEAN_UP_LISTENER);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream, ChannelPromise promise)
/*     */   {
/* 105 */     Http2Stream stream = connection().stream(streamId);
/* 106 */     EmbeddedChannel channel = stream == null ? null : (EmbeddedChannel)stream.getProperty(CompressorHttp2ConnectionEncoder.class);
/*     */     
/* 108 */     if (channel == null)
/*     */     {
/* 110 */       return super.writeData(ctx, streamId, data, padding, endOfStream, promise);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 115 */       channel.writeOutbound(new Object[] { data });
/* 116 */       ByteBuf buf = nextReadableBuf(channel);
/* 117 */       if (buf == null) { Object localObject1;
/* 118 */         if (endOfStream) {
/* 119 */           if (channel.finish()) {
/* 120 */             buf = nextReadableBuf(channel);
/*     */           }
/* 122 */           return super.writeData(ctx, streamId, buf == null ? Unpooled.EMPTY_BUFFER : buf, padding, true, promise);
/*     */         }
/*     */         
/*     */ 
/* 126 */         promise.setSuccess();
/* 127 */         return promise;
/*     */       }
/*     */       
/* 130 */       ChannelPromiseAggregator aggregator = new ChannelPromiseAggregator(promise);
/* 131 */       ChannelPromise bufPromise = ctx.newPromise();
/* 132 */       aggregator.add(new Promise[] { bufPromise });
/*     */       ByteBuf nextBuf;
/* 134 */       for (;;) { nextBuf = nextReadableBuf(channel);
/* 135 */         boolean compressedEndOfStream = (nextBuf == null) && (endOfStream);
/* 136 */         if ((compressedEndOfStream) && (channel.finish())) {
/* 137 */           nextBuf = nextReadableBuf(channel);
/* 138 */           compressedEndOfStream = nextBuf == null;
/*     */         }
/*     */         
/*     */         ChannelPromise nextPromise;
/* 142 */         if (nextBuf != null)
/*     */         {
/*     */ 
/* 145 */           ChannelPromise nextPromise = ctx.newPromise();
/* 146 */           aggregator.add(new Promise[] { nextPromise });
/*     */         } else {
/* 148 */           nextPromise = null;
/*     */         }
/*     */         
/* 151 */         super.writeData(ctx, streamId, buf, padding, compressedEndOfStream, bufPromise);
/* 152 */         if (nextBuf == null) {
/*     */           break;
/*     */         }
/*     */         
/* 156 */         padding = 0;
/* 157 */         buf = nextBuf;
/* 158 */         bufPromise = nextPromise;
/*     */       }
/* 160 */       return promise;
/*     */     } finally {
/* 162 */       if (endOfStream) {
/* 163 */         cleanup(stream, channel);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise)
/*     */   {
/* 171 */     initCompressor(streamId, headers, endStream);
/* 172 */     return super.writeHeaders(ctx, streamId, headers, padding, endStream, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise)
/*     */   {
/* 179 */     initCompressor(streamId, headers, endOfStream);
/* 180 */     return super.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream, promise);
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
/*     */   protected EmbeddedChannel newContentCompressor(AsciiString contentEncoding)
/*     */     throws Http2Exception
/*     */   {
/* 194 */     if ((HttpHeaderValues.GZIP.equalsIgnoreCase(contentEncoding)) || (HttpHeaderValues.X_GZIP.equalsIgnoreCase(contentEncoding))) {
/* 195 */       return newCompressionChannel(ZlibWrapper.GZIP);
/*     */     }
/* 197 */     if ((HttpHeaderValues.DEFLATE.equalsIgnoreCase(contentEncoding)) || (HttpHeaderValues.X_DEFLATE.equalsIgnoreCase(contentEncoding))) {
/* 198 */       return newCompressionChannel(ZlibWrapper.ZLIB);
/*     */     }
/*     */     
/* 201 */     return null;
/*     */   }
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
/* 213 */     return contentEncoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private EmbeddedChannel newCompressionChannel(ZlibWrapper wrapper)
/*     */   {
/* 221 */     return new EmbeddedChannel(new ChannelHandler[] { ZlibCodecFactory.newZlibEncoder(wrapper, this.compressionLevel, this.windowBits, this.memLevel) });
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
/*     */   private void initCompressor(int streamId, Http2Headers headers, boolean endOfStream)
/*     */   {
/* 234 */     Http2Stream stream = connection().stream(streamId);
/* 235 */     if (stream == null) {
/* 236 */       return;
/*     */     }
/*     */     
/* 239 */     EmbeddedChannel compressor = (EmbeddedChannel)stream.getProperty(CompressorHttp2ConnectionEncoder.class);
/* 240 */     if (compressor == null) {
/* 241 */       if (!endOfStream) {
/* 242 */         AsciiString encoding = (AsciiString)headers.get(HttpHeaderNames.CONTENT_ENCODING);
/* 243 */         if (encoding == null) {
/* 244 */           encoding = HttpHeaderValues.IDENTITY;
/*     */         }
/*     */         try {
/* 247 */           compressor = newContentCompressor(encoding);
/* 248 */           if (compressor != null) {
/* 249 */             stream.setProperty(CompressorHttp2ConnectionEncoder.class, compressor);
/* 250 */             AsciiString targetContentEncoding = getTargetContentEncoding(encoding);
/* 251 */             if (HttpHeaderValues.IDENTITY.equalsIgnoreCase(targetContentEncoding)) {
/* 252 */               headers.remove(HttpHeaderNames.CONTENT_ENCODING);
/*     */             } else {
/* 254 */               headers.set(HttpHeaderNames.CONTENT_ENCODING, targetContentEncoding);
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (Throwable ignored) {}
/*     */       }
/*     */     }
/* 261 */     else if (endOfStream) {
/* 262 */       cleanup(stream, compressor);
/*     */     }
/*     */     
/* 265 */     if (compressor != null)
/*     */     {
/*     */ 
/*     */ 
/* 269 */       headers.remove(HttpHeaderNames.CONTENT_LENGTH);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void cleanup(Http2Stream stream, EmbeddedChannel compressor)
/*     */   {
/* 280 */     if (compressor.finish()) {
/*     */       for (;;) {
/* 282 */         ByteBuf buf = (ByteBuf)compressor.readOutbound();
/* 283 */         if (buf == null) {
/*     */           break;
/*     */         }
/*     */         
/* 287 */         buf.release();
/*     */       }
/*     */     }
/* 290 */     stream.removeProperty(CompressorHttp2ConnectionEncoder.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static ByteBuf nextReadableBuf(EmbeddedChannel compressor)
/*     */   {
/*     */     ByteBuf buf;
/*     */     
/*     */     for (;;)
/*     */     {
/* 301 */       buf = (ByteBuf)compressor.readOutbound();
/* 302 */       if (buf == null) {
/* 303 */         return null;
/*     */       }
/* 305 */       if (buf.isReadable()) break;
/* 306 */       buf.release();
/*     */     }
/*     */     
/* 309 */     return buf;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\CompressorHttp2ConnectionEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */