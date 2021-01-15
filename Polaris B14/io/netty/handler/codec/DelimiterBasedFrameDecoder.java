/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
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
/*     */ public class DelimiterBasedFrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private final ByteBuf[] delimiters;
/*     */   private final int maxFrameLength;
/*     */   private final boolean stripDelimiter;
/*     */   private final boolean failFast;
/*     */   private boolean discardingTooLongFrame;
/*     */   private int tooLongFrameLength;
/*     */   private final LineBasedFrameDecoder lineBasedDecoder;
/*     */   
/*     */   public DelimiterBasedFrameDecoder(int maxFrameLength, ByteBuf delimiter)
/*     */   {
/*  78 */     this(maxFrameLength, true, delimiter);
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
/*     */   public DelimiterBasedFrameDecoder(int maxFrameLength, boolean stripDelimiter, ByteBuf delimiter)
/*     */   {
/*  93 */     this(maxFrameLength, stripDelimiter, true, delimiter);
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
/*     */   public DelimiterBasedFrameDecoder(int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf delimiter)
/*     */   {
/* 116 */     this(maxFrameLength, stripDelimiter, failFast, new ByteBuf[] { delimiter.slice(delimiter.readerIndex(), delimiter.readableBytes()) });
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
/*     */   public DelimiterBasedFrameDecoder(int maxFrameLength, ByteBuf... delimiters)
/*     */   {
/* 129 */     this(maxFrameLength, true, delimiters);
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
/*     */   public DelimiterBasedFrameDecoder(int maxFrameLength, boolean stripDelimiter, ByteBuf... delimiters)
/*     */   {
/* 144 */     this(maxFrameLength, stripDelimiter, true, delimiters);
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
/*     */   public DelimiterBasedFrameDecoder(int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf... delimiters)
/*     */   {
/* 166 */     validateMaxFrameLength(maxFrameLength);
/* 167 */     if (delimiters == null) {
/* 168 */       throw new NullPointerException("delimiters");
/*     */     }
/* 170 */     if (delimiters.length == 0) {
/* 171 */       throw new IllegalArgumentException("empty delimiters");
/*     */     }
/*     */     
/* 174 */     if ((isLineBased(delimiters)) && (!isSubclass())) {
/* 175 */       this.lineBasedDecoder = new LineBasedFrameDecoder(maxFrameLength, stripDelimiter, failFast);
/* 176 */       this.delimiters = null;
/*     */     } else {
/* 178 */       this.delimiters = new ByteBuf[delimiters.length];
/* 179 */       for (int i = 0; i < delimiters.length; i++) {
/* 180 */         ByteBuf d = delimiters[i];
/* 181 */         validateDelimiter(d);
/* 182 */         this.delimiters[i] = d.slice(d.readerIndex(), d.readableBytes());
/*     */       }
/* 184 */       this.lineBasedDecoder = null;
/*     */     }
/* 186 */     this.maxFrameLength = maxFrameLength;
/* 187 */     this.stripDelimiter = stripDelimiter;
/* 188 */     this.failFast = failFast;
/*     */   }
/*     */   
/*     */   private static boolean isLineBased(ByteBuf[] delimiters)
/*     */   {
/* 193 */     if (delimiters.length != 2) {
/* 194 */       return false;
/*     */     }
/* 196 */     ByteBuf a = delimiters[0];
/* 197 */     ByteBuf b = delimiters[1];
/* 198 */     if (a.capacity() < b.capacity()) {
/* 199 */       a = delimiters[1];
/* 200 */       b = delimiters[0];
/*     */     }
/* 202 */     return (a.capacity() == 2) && (b.capacity() == 1) && (a.getByte(0) == 13) && (a.getByte(1) == 10) && (b.getByte(0) == 10);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isSubclass()
/*     */   {
/* 211 */     return getClass() != DelimiterBasedFrameDecoder.class;
/*     */   }
/*     */   
/*     */   protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/* 216 */     Object decoded = decode(ctx, in);
/* 217 */     if (decoded != null) {
/* 218 */       out.add(decoded);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer)
/*     */     throws Exception
/*     */   {
/* 231 */     if (this.lineBasedDecoder != null) {
/* 232 */       return this.lineBasedDecoder.decode(ctx, buffer);
/*     */     }
/*     */     
/* 235 */     int minFrameLength = Integer.MAX_VALUE;
/* 236 */     ByteBuf minDelim = null;
/* 237 */     for (ByteBuf delim : this.delimiters) {
/* 238 */       int frameLength = indexOf(buffer, delim);
/* 239 */       if ((frameLength >= 0) && (frameLength < minFrameLength)) {
/* 240 */         minFrameLength = frameLength;
/* 241 */         minDelim = delim;
/*     */       }
/*     */     }
/*     */     
/* 245 */     if (minDelim != null) {
/* 246 */       int minDelimLength = minDelim.capacity();
/*     */       
/*     */ 
/* 249 */       if (this.discardingTooLongFrame)
/*     */       {
/*     */ 
/* 252 */         this.discardingTooLongFrame = false;
/* 253 */         buffer.skipBytes(minFrameLength + minDelimLength);
/*     */         
/* 255 */         int tooLongFrameLength = this.tooLongFrameLength;
/* 256 */         this.tooLongFrameLength = 0;
/* 257 */         if (!this.failFast) {
/* 258 */           fail(tooLongFrameLength);
/*     */         }
/* 260 */         return null;
/*     */       }
/*     */       
/* 263 */       if (minFrameLength > this.maxFrameLength)
/*     */       {
/* 265 */         buffer.skipBytes(minFrameLength + minDelimLength);
/* 266 */         fail(minFrameLength);
/* 267 */         return null;
/*     */       }
/*     */       ByteBuf frame;
/* 270 */       if (this.stripDelimiter) {
/* 271 */         ByteBuf frame = buffer.readSlice(minFrameLength);
/* 272 */         buffer.skipBytes(minDelimLength);
/*     */       } else {
/* 274 */         frame = buffer.readSlice(minFrameLength + minDelimLength);
/*     */       }
/*     */       
/* 277 */       return frame.retain();
/*     */     }
/* 279 */     if (!this.discardingTooLongFrame) {
/* 280 */       if (buffer.readableBytes() > this.maxFrameLength)
/*     */       {
/* 282 */         this.tooLongFrameLength = buffer.readableBytes();
/* 283 */         buffer.skipBytes(buffer.readableBytes());
/* 284 */         this.discardingTooLongFrame = true;
/* 285 */         if (this.failFast) {
/* 286 */           fail(this.tooLongFrameLength);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 291 */       this.tooLongFrameLength += buffer.readableBytes();
/* 292 */       buffer.skipBytes(buffer.readableBytes());
/*     */     }
/* 294 */     return null;
/*     */   }
/*     */   
/*     */   private void fail(long frameLength)
/*     */   {
/* 299 */     if (frameLength > 0L) {
/* 300 */       throw new TooLongFrameException("frame length exceeds " + this.maxFrameLength + ": " + frameLength + " - discarded");
/*     */     }
/*     */     
/*     */ 
/* 304 */     throw new TooLongFrameException("frame length exceeds " + this.maxFrameLength + " - discarding");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int indexOf(ByteBuf haystack, ByteBuf needle)
/*     */   {
/* 316 */     for (int i = haystack.readerIndex(); i < haystack.writerIndex(); i++) {
/* 317 */       int haystackIndex = i;
/*     */       
/* 319 */       for (int needleIndex = 0; needleIndex < needle.capacity(); needleIndex++) {
/* 320 */         if (haystack.getByte(haystackIndex) != needle.getByte(needleIndex)) {
/*     */           break;
/*     */         }
/* 323 */         haystackIndex++;
/* 324 */         if ((haystackIndex == haystack.writerIndex()) && (needleIndex != needle.capacity() - 1))
/*     */         {
/* 326 */           return -1;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 331 */       if (needleIndex == needle.capacity())
/*     */       {
/* 333 */         return i - haystack.readerIndex();
/*     */       }
/*     */     }
/* 336 */     return -1;
/*     */   }
/*     */   
/*     */   private static void validateDelimiter(ByteBuf delimiter) {
/* 340 */     if (delimiter == null) {
/* 341 */       throw new NullPointerException("delimiter");
/*     */     }
/* 343 */     if (!delimiter.isReadable()) {
/* 344 */       throw new IllegalArgumentException("empty delimiter");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void validateMaxFrameLength(int maxFrameLength) {
/* 349 */     if (maxFrameLength <= 0) {
/* 350 */       throw new IllegalArgumentException("maxFrameLength must be a positive integer: " + maxFrameLength);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\DelimiterBasedFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */