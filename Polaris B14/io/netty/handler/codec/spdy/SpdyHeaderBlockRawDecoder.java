/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpdyHeaderBlockRawDecoder
/*     */   extends SpdyHeaderBlockDecoder
/*     */ {
/*     */   private static final int LENGTH_FIELD_SIZE = 4;
/*     */   private final int maxHeaderSize;
/*     */   private State state;
/*     */   private ByteBuf cumulation;
/*     */   private int headerSize;
/*     */   private int numHeaders;
/*     */   private int length;
/*     */   private String name;
/*     */   
/*     */   private static enum State
/*     */   {
/*  39 */     READ_NUM_HEADERS, 
/*  40 */     READ_NAME_LENGTH, 
/*  41 */     READ_NAME, 
/*  42 */     SKIP_NAME, 
/*  43 */     READ_VALUE_LENGTH, 
/*  44 */     READ_VALUE, 
/*  45 */     SKIP_VALUE, 
/*  46 */     END_HEADER_BLOCK, 
/*  47 */     ERROR;
/*     */     
/*     */     private State() {} }
/*     */   
/*  51 */   public SpdyHeaderBlockRawDecoder(SpdyVersion spdyVersion, int maxHeaderSize) { if (spdyVersion == null) {
/*  52 */       throw new NullPointerException("spdyVersion");
/*     */     }
/*  54 */     this.maxHeaderSize = maxHeaderSize;
/*  55 */     this.state = State.READ_NUM_HEADERS;
/*     */   }
/*     */   
/*     */   private static int readLengthField(ByteBuf buffer) {
/*  59 */     int length = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex());
/*  60 */     buffer.skipBytes(4);
/*  61 */     return length;
/*     */   }
/*     */   
/*     */   void decode(ByteBufAllocator alloc, ByteBuf headerBlock, SpdyHeadersFrame frame) throws Exception
/*     */   {
/*  66 */     if (headerBlock == null) {
/*  67 */       throw new NullPointerException("headerBlock");
/*     */     }
/*  69 */     if (frame == null) {
/*  70 */       throw new NullPointerException("frame");
/*     */     }
/*     */     
/*  73 */     if (this.cumulation == null) {
/*  74 */       decodeHeaderBlock(headerBlock, frame);
/*  75 */       if (headerBlock.isReadable()) {
/*  76 */         this.cumulation = alloc.buffer(headerBlock.readableBytes());
/*  77 */         this.cumulation.writeBytes(headerBlock);
/*     */       }
/*     */     } else {
/*  80 */       this.cumulation.writeBytes(headerBlock);
/*  81 */       decodeHeaderBlock(this.cumulation, frame);
/*  82 */       if (this.cumulation.isReadable()) {
/*  83 */         this.cumulation.discardReadBytes();
/*     */       } else {
/*  85 */         releaseBuffer();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void decodeHeaderBlock(ByteBuf headerBlock, SpdyHeadersFrame frame) throws Exception
/*     */   {
/*  92 */     while (headerBlock.isReadable()) { int skipLength;
/*  93 */       switch (this.state) {
/*     */       case READ_NUM_HEADERS: 
/*  95 */         if (headerBlock.readableBytes() < 4) {
/*  96 */           return;
/*     */         }
/*     */         
/*  99 */         this.numHeaders = readLengthField(headerBlock);
/*     */         
/* 101 */         if (this.numHeaders < 0) {
/* 102 */           this.state = State.ERROR;
/* 103 */           frame.setInvalid();
/* 104 */         } else if (this.numHeaders == 0) {
/* 105 */           this.state = State.END_HEADER_BLOCK;
/*     */         } else {
/* 107 */           this.state = State.READ_NAME_LENGTH;
/*     */         }
/* 109 */         break;
/*     */       
/*     */       case READ_NAME_LENGTH: 
/* 112 */         if (headerBlock.readableBytes() < 4) {
/* 113 */           return;
/*     */         }
/*     */         
/* 116 */         this.length = readLengthField(headerBlock);
/*     */         
/*     */ 
/* 119 */         if (this.length <= 0) {
/* 120 */           this.state = State.ERROR;
/* 121 */           frame.setInvalid();
/* 122 */         } else if ((this.length > this.maxHeaderSize) || (this.headerSize > this.maxHeaderSize - this.length)) {
/* 123 */           this.headerSize = (this.maxHeaderSize + 1);
/* 124 */           this.state = State.SKIP_NAME;
/* 125 */           frame.setTruncated();
/*     */         } else {
/* 127 */           this.headerSize += this.length;
/* 128 */           this.state = State.READ_NAME;
/*     */         }
/* 130 */         break;
/*     */       
/*     */       case READ_NAME: 
/* 133 */         if (headerBlock.readableBytes() < this.length) {
/* 134 */           return;
/*     */         }
/*     */         
/* 137 */         byte[] nameBytes = new byte[this.length];
/* 138 */         headerBlock.readBytes(nameBytes);
/* 139 */         this.name = new String(nameBytes, "UTF-8");
/*     */         
/*     */ 
/* 142 */         if (frame.headers().contains(this.name)) {
/* 143 */           this.state = State.ERROR;
/* 144 */           frame.setInvalid();
/*     */         } else {
/* 146 */           this.state = State.READ_VALUE_LENGTH;
/*     */         }
/* 148 */         break;
/*     */       
/*     */       case SKIP_NAME: 
/* 151 */         skipLength = Math.min(headerBlock.readableBytes(), this.length);
/* 152 */         headerBlock.skipBytes(skipLength);
/* 153 */         this.length -= skipLength;
/*     */         
/* 155 */         if (this.length == 0) {
/* 156 */           this.state = State.READ_VALUE_LENGTH;
/*     */         }
/*     */         
/*     */         break;
/*     */       case READ_VALUE_LENGTH: 
/* 161 */         if (headerBlock.readableBytes() < 4) {
/* 162 */           return;
/*     */         }
/*     */         
/* 165 */         this.length = readLengthField(headerBlock);
/*     */         
/*     */ 
/* 168 */         if (this.length < 0) {
/* 169 */           this.state = State.ERROR;
/* 170 */           frame.setInvalid();
/* 171 */         } else if (this.length == 0) {
/* 172 */           if (!frame.isTruncated())
/*     */           {
/* 174 */             frame.headers().add(this.name, "");
/*     */           }
/*     */           
/* 177 */           this.name = null;
/* 178 */           if (--this.numHeaders == 0) {
/* 179 */             this.state = State.END_HEADER_BLOCK;
/*     */           } else {
/* 181 */             this.state = State.READ_NAME_LENGTH;
/*     */           }
/*     */         }
/* 184 */         else if ((this.length > this.maxHeaderSize) || (this.headerSize > this.maxHeaderSize - this.length)) {
/* 185 */           this.headerSize = (this.maxHeaderSize + 1);
/* 186 */           this.name = null;
/* 187 */           this.state = State.SKIP_VALUE;
/* 188 */           frame.setTruncated();
/*     */         } else {
/* 190 */           this.headerSize += this.length;
/* 191 */           this.state = State.READ_VALUE;
/*     */         }
/* 193 */         break;
/*     */       
/*     */       case READ_VALUE: 
/* 196 */         if (headerBlock.readableBytes() < this.length) {
/* 197 */           return;
/*     */         }
/*     */         
/* 200 */         byte[] valueBytes = new byte[this.length];
/* 201 */         headerBlock.readBytes(valueBytes);
/*     */         
/*     */ 
/* 204 */         int index = 0;
/* 205 */         int offset = 0;
/*     */         
/*     */ 
/* 208 */         if (valueBytes[0] == 0) {
/* 209 */           this.state = State.ERROR;
/* 210 */           frame.setInvalid();
/*     */         }
/*     */         else
/*     */         {
/* 214 */           while (index < this.length) {
/* 215 */             while ((index < valueBytes.length) && (valueBytes[index] != 0)) {
/* 216 */               index++;
/*     */             }
/* 218 */             if (index < valueBytes.length)
/*     */             {
/* 220 */               if ((index + 1 == valueBytes.length) || (valueBytes[(index + 1)] == 0))
/*     */               {
/*     */ 
/*     */ 
/* 224 */                 this.state = State.ERROR;
/* 225 */                 frame.setInvalid();
/* 226 */                 break;
/*     */               }
/*     */             }
/* 229 */             String value = new String(valueBytes, offset, index - offset, "UTF-8");
/*     */             try
/*     */             {
/* 232 */               frame.headers().add(this.name, value);
/*     */             }
/*     */             catch (IllegalArgumentException e) {
/* 235 */               this.state = State.ERROR;
/* 236 */               frame.setInvalid();
/* 237 */               break;
/*     */             }
/* 239 */             index++;
/* 240 */             offset = index;
/*     */           }
/*     */           
/* 243 */           this.name = null;
/*     */           
/*     */ 
/* 246 */           if (this.state != State.ERROR)
/*     */           {
/*     */ 
/*     */ 
/* 250 */             if (--this.numHeaders == 0) {
/* 251 */               this.state = State.END_HEADER_BLOCK;
/*     */             } else
/* 253 */               this.state = State.READ_NAME_LENGTH; }
/*     */         }
/* 255 */         break;
/*     */       
/*     */       case SKIP_VALUE: 
/* 258 */         skipLength = Math.min(headerBlock.readableBytes(), this.length);
/* 259 */         headerBlock.skipBytes(skipLength);
/* 260 */         this.length -= skipLength;
/*     */         
/* 262 */         if (this.length == 0) {
/* 263 */           if (--this.numHeaders == 0) {
/* 264 */             this.state = State.END_HEADER_BLOCK;
/*     */           } else {
/* 266 */             this.state = State.READ_NAME_LENGTH;
/*     */           }
/*     */         }
/*     */         
/*     */         break;
/*     */       case END_HEADER_BLOCK: 
/* 272 */         this.state = State.ERROR;
/* 273 */         frame.setInvalid();
/* 274 */         break;
/*     */       
/*     */       case ERROR: 
/* 277 */         headerBlock.skipBytes(headerBlock.readableBytes());
/* 278 */         return;
/*     */       
/*     */       default: 
/* 281 */         throw new Error("Shouldn't reach here.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void endHeaderBlock(SpdyHeadersFrame frame) throws Exception
/*     */   {
/* 288 */     if (this.state != State.END_HEADER_BLOCK) {
/* 289 */       frame.setInvalid();
/*     */     }
/*     */     
/* 292 */     releaseBuffer();
/*     */     
/*     */ 
/* 295 */     this.headerSize = 0;
/* 296 */     this.name = null;
/* 297 */     this.state = State.READ_NUM_HEADERS;
/*     */   }
/*     */   
/*     */   void end()
/*     */   {
/* 302 */     releaseBuffer();
/*     */   }
/*     */   
/*     */   private void releaseBuffer() {
/* 306 */     if (this.cumulation != null) {
/* 307 */       this.cumulation.release();
/* 308 */       this.cumulation = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyHeaderBlockRawDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */