/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.util.List;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.Inflater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JdkZlibDecoder
/*     */   extends ZlibDecoder
/*     */ {
/*     */   private static final int FHCRC = 2;
/*     */   private static final int FEXTRA = 4;
/*     */   private static final int FNAME = 8;
/*     */   private static final int FCOMMENT = 16;
/*     */   private static final int FRESERVED = 224;
/*     */   private Inflater inflater;
/*     */   private final byte[] dictionary;
/*     */   private final CRC32 crc;
/*     */   
/*     */   private static enum GzipState
/*     */   {
/*  44 */     HEADER_START, 
/*  45 */     HEADER_END, 
/*  46 */     FLG_READ, 
/*  47 */     XLEN_READ, 
/*  48 */     SKIP_FNAME, 
/*  49 */     SKIP_COMMENT, 
/*  50 */     PROCESS_FHCRC, 
/*  51 */     FOOTER_START;
/*     */     
/*     */     private GzipState() {} }
/*  54 */   private GzipState gzipState = GzipState.HEADER_START;
/*  55 */   private int flags = -1;
/*  56 */   private int xlen = -1;
/*     */   
/*     */ 
/*     */   private volatile boolean finished;
/*     */   
/*     */   private boolean decideZlibOrNone;
/*     */   
/*     */ 
/*     */   public JdkZlibDecoder()
/*     */   {
/*  66 */     this(ZlibWrapper.ZLIB, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkZlibDecoder(byte[] dictionary)
/*     */   {
/*  75 */     this(ZlibWrapper.ZLIB, dictionary);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkZlibDecoder(ZlibWrapper wrapper)
/*     */   {
/*  84 */     this(wrapper, null);
/*     */   }
/*     */   
/*     */   private JdkZlibDecoder(ZlibWrapper wrapper, byte[] dictionary) {
/*  88 */     if (wrapper == null) {
/*  89 */       throw new NullPointerException("wrapper");
/*     */     }
/*  91 */     switch (wrapper) {
/*     */     case GZIP: 
/*  93 */       this.inflater = new Inflater(true);
/*  94 */       this.crc = new CRC32();
/*  95 */       break;
/*     */     case NONE: 
/*  97 */       this.inflater = new Inflater(true);
/*  98 */       this.crc = null;
/*  99 */       break;
/*     */     case ZLIB: 
/* 101 */       this.inflater = new Inflater();
/* 102 */       this.crc = null;
/* 103 */       break;
/*     */     
/*     */     case ZLIB_OR_NONE: 
/* 106 */       this.decideZlibOrNone = true;
/* 107 */       this.crc = null;
/* 108 */       break;
/*     */     default: 
/* 110 */       throw new IllegalArgumentException("Only GZIP or ZLIB is supported, but you used " + wrapper);
/*     */     }
/* 112 */     this.dictionary = dictionary;
/*     */   }
/*     */   
/*     */   public boolean isClosed()
/*     */   {
/* 117 */     return this.finished;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/* 122 */     if (this.finished)
/*     */     {
/* 124 */       in.skipBytes(in.readableBytes());
/* 125 */       return;
/*     */     }
/*     */     
/* 128 */     int readableBytes = in.readableBytes();
/* 129 */     if (readableBytes == 0) {
/* 130 */       return;
/*     */     }
/*     */     
/* 133 */     if (this.decideZlibOrNone)
/*     */     {
/* 135 */       if (readableBytes < 2) {
/* 136 */         return;
/*     */       }
/*     */       
/* 139 */       boolean nowrap = !looksLikeZlib(in.getShort(in.readerIndex()));
/* 140 */       this.inflater = new Inflater(nowrap);
/* 141 */       this.decideZlibOrNone = false;
/*     */     }
/*     */     
/* 144 */     if (this.crc != null) {
/* 145 */       switch (this.gzipState) {
/*     */       case FOOTER_START: 
/* 147 */         if (readGZIPFooter(in)) {
/* 148 */           this.finished = true;
/*     */         }
/* 150 */         return;
/*     */       }
/* 152 */       if ((this.gzipState != GzipState.HEADER_END) && 
/* 153 */         (!readGZIPHeader(in))) {
/* 154 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 159 */       readableBytes = in.readableBytes();
/*     */     }
/*     */     
/* 162 */     if (in.hasArray()) {
/* 163 */       this.inflater.setInput(in.array(), in.arrayOffset() + in.readerIndex(), readableBytes);
/*     */     } else {
/* 165 */       byte[] array = new byte[readableBytes];
/* 166 */       in.getBytes(in.readerIndex(), array);
/* 167 */       this.inflater.setInput(array);
/*     */     }
/*     */     
/* 170 */     int maxOutputLength = this.inflater.getRemaining() << 1;
/* 171 */     ByteBuf decompressed = ctx.alloc().heapBuffer(maxOutputLength);
/*     */     try {
/* 173 */       boolean readFooter = false;
/* 174 */       byte[] outArray = decompressed.array();
/* 175 */       while (!this.inflater.needsInput()) {
/* 176 */         int writerIndex = decompressed.writerIndex();
/* 177 */         int outIndex = decompressed.arrayOffset() + writerIndex;
/* 178 */         int length = decompressed.writableBytes();
/*     */         
/* 180 */         if (length == 0)
/*     */         {
/* 182 */           out.add(decompressed);
/* 183 */           decompressed = ctx.alloc().heapBuffer(maxOutputLength);
/* 184 */           outArray = decompressed.array();
/*     */         }
/*     */         else
/*     */         {
/* 188 */           int outputLength = this.inflater.inflate(outArray, outIndex, length);
/* 189 */           if (outputLength > 0) {
/* 190 */             decompressed.writerIndex(writerIndex + outputLength);
/* 191 */             if (this.crc != null) {
/* 192 */               this.crc.update(outArray, outIndex, outputLength);
/*     */             }
/*     */           }
/* 195 */           else if (this.inflater.needsDictionary()) {
/* 196 */             if (this.dictionary == null) {
/* 197 */               throw new DecompressionException("decompression failure, unable to set dictionary as non was specified");
/*     */             }
/*     */             
/* 200 */             this.inflater.setDictionary(this.dictionary);
/*     */           }
/*     */           
/*     */ 
/* 204 */           if (this.inflater.finished()) {
/* 205 */             if (this.crc == null) {
/* 206 */               this.finished = true; break;
/*     */             }
/* 208 */             readFooter = true;
/*     */             
/* 210 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 214 */       in.skipBytes(readableBytes - this.inflater.getRemaining());
/*     */       
/* 216 */       if (readFooter) {
/* 217 */         this.gzipState = GzipState.FOOTER_START;
/* 218 */         if (readGZIPFooter(in)) {
/* 219 */           this.finished = true;
/*     */         }
/*     */       }
/*     */     } catch (DataFormatException e) {
/* 223 */       throw new DecompressionException("decompression failure", e);
/*     */     }
/*     */     finally {
/* 226 */       if (decompressed.isReadable()) {
/* 227 */         out.add(decompressed);
/*     */       } else {
/* 229 */         decompressed.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 236 */     super.handlerRemoved0(ctx);
/* 237 */     if (this.inflater != null) {
/* 238 */       this.inflater.end();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean readGZIPHeader(ByteBuf in) {
/* 243 */     switch (this.gzipState) {
/*     */     case HEADER_START: 
/* 245 */       if (in.readableBytes() < 10) {
/* 246 */         return false;
/*     */       }
/*     */       
/* 249 */       int magic0 = in.readByte();
/* 250 */       int magic1 = in.readByte();
/*     */       
/* 252 */       if (magic0 != 31) {
/* 253 */         throw new DecompressionException("Input is not in the GZIP format");
/*     */       }
/* 255 */       this.crc.update(magic0);
/* 256 */       this.crc.update(magic1);
/*     */       
/* 258 */       int method = in.readUnsignedByte();
/* 259 */       if (method != 8) {
/* 260 */         throw new DecompressionException("Unsupported compression method " + method + " in the GZIP header");
/*     */       }
/*     */       
/* 263 */       this.crc.update(method);
/*     */       
/* 265 */       this.flags = in.readUnsignedByte();
/* 266 */       this.crc.update(this.flags);
/*     */       
/* 268 */       if ((this.flags & 0xE0) != 0) {
/* 269 */         throw new DecompressionException("Reserved flags are set in the GZIP header");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 274 */       this.crc.update(in.readByte());
/* 275 */       this.crc.update(in.readByte());
/* 276 */       this.crc.update(in.readByte());
/* 277 */       this.crc.update(in.readByte());
/*     */       
/* 279 */       this.crc.update(in.readUnsignedByte());
/* 280 */       this.crc.update(in.readUnsignedByte());
/*     */       
/* 282 */       this.gzipState = GzipState.FLG_READ;
/*     */     case FLG_READ: 
/* 284 */       if ((this.flags & 0x4) != 0) {
/* 285 */         if (in.readableBytes() < 2) {
/* 286 */           return false;
/*     */         }
/* 288 */         int xlen1 = in.readUnsignedByte();
/* 289 */         int xlen2 = in.readUnsignedByte();
/* 290 */         this.crc.update(xlen1);
/* 291 */         this.crc.update(xlen2);
/*     */         
/* 293 */         this.xlen |= xlen1 << 8 | xlen2;
/*     */       }
/* 295 */       this.gzipState = GzipState.XLEN_READ;
/*     */     case XLEN_READ: 
/* 297 */       if (this.xlen != -1) {
/* 298 */         if (in.readableBytes() < this.xlen) {
/* 299 */           return false;
/*     */         }
/* 301 */         byte[] xtra = new byte[this.xlen];
/* 302 */         in.readBytes(xtra);
/* 303 */         this.crc.update(xtra);
/*     */       }
/* 305 */       this.gzipState = GzipState.SKIP_FNAME;
/*     */     case SKIP_FNAME: 
/* 307 */       if ((this.flags & 0x8) != 0) {
/* 308 */         if (!in.isReadable())
/* 309 */           return false;
/*     */         int b;
/*     */         do {
/* 312 */           b = in.readUnsignedByte();
/* 313 */           this.crc.update(b);
/* 314 */         } while ((b != 0) && 
/*     */         
/*     */ 
/* 317 */           (in.isReadable()));
/*     */       }
/* 319 */       this.gzipState = GzipState.SKIP_COMMENT;
/*     */     case SKIP_COMMENT: 
/* 321 */       if ((this.flags & 0x10) != 0) {
/* 322 */         if (!in.isReadable())
/* 323 */           return false;
/*     */         int b;
/*     */         do {
/* 326 */           b = in.readUnsignedByte();
/* 327 */           this.crc.update(b);
/* 328 */         } while ((b != 0) && 
/*     */         
/*     */ 
/* 331 */           (in.isReadable()));
/*     */       }
/* 333 */       this.gzipState = GzipState.PROCESS_FHCRC;
/*     */     case PROCESS_FHCRC: 
/* 335 */       if ((this.flags & 0x2) != 0) {
/* 336 */         if (in.readableBytes() < 4) {
/* 337 */           return false;
/*     */         }
/* 339 */         verifyCrc(in);
/*     */       }
/* 341 */       this.crc.reset();
/* 342 */       this.gzipState = GzipState.HEADER_END;
/*     */     case HEADER_END: 
/* 344 */       return true;
/*     */     }
/* 346 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   private boolean readGZIPFooter(ByteBuf buf)
/*     */   {
/* 351 */     if (buf.readableBytes() < 8) {
/* 352 */       return false;
/*     */     }
/*     */     
/* 355 */     verifyCrc(buf);
/*     */     
/*     */ 
/* 358 */     int dataLength = 0;
/* 359 */     for (int i = 0; i < 4; i++) {
/* 360 */       dataLength |= buf.readUnsignedByte() << i * 8;
/*     */     }
/* 362 */     int readLength = this.inflater.getTotalOut();
/* 363 */     if (dataLength != readLength) {
/* 364 */       throw new DecompressionException("Number of bytes mismatch. Expected: " + dataLength + ", Got: " + readLength);
/*     */     }
/*     */     
/* 367 */     return true;
/*     */   }
/*     */   
/*     */   private void verifyCrc(ByteBuf in) {
/* 371 */     long crcValue = 0L;
/* 372 */     for (int i = 0; i < 4; i++) {
/* 373 */       crcValue |= in.readUnsignedByte() << i * 8;
/*     */     }
/* 375 */     long readCrc = this.crc.getValue();
/* 376 */     if (crcValue != readCrc) {
/* 377 */       throw new DecompressionException("CRC value missmatch. Expected: " + crcValue + ", Got: " + readCrc);
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
/*     */   private static boolean looksLikeZlib(short cmf_flg)
/*     */   {
/* 390 */     return ((cmf_flg & 0x7800) == 30720) && (cmf_flg % 31 == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\JdkZlibDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */