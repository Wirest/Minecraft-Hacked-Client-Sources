/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Snappy
/*     */ {
/*     */   private static final int MAX_HT_SIZE = 16384;
/*     */   private static final int MIN_COMPRESSIBLE_BYTES = 15;
/*     */   private static final int PREAMBLE_NOT_FULL = -1;
/*     */   private static final int NOT_ENOUGH_INPUT = -1;
/*     */   private static final int LITERAL = 0;
/*     */   private static final int COPY_1_BYTE_OFFSET = 1;
/*     */   private static final int COPY_2_BYTE_OFFSET = 2;
/*     */   private static final int COPY_4_BYTE_OFFSET = 3;
/*     */   private State state;
/*     */   private byte tag;
/*     */   private int written;
/*     */   
/*     */   Snappy()
/*     */   {
/*  42 */     this.state = State.READY;
/*     */   }
/*     */   
/*     */   private static enum State
/*     */   {
/*  47 */     READY, 
/*  48 */     READING_PREAMBLE, 
/*  49 */     READING_TAG, 
/*  50 */     READING_LITERAL, 
/*  51 */     READING_COPY;
/*     */     
/*     */     private State() {} }
/*     */   
/*  55 */   public void reset() { this.state = State.READY;
/*  56 */     this.tag = 0;
/*  57 */     this.written = 0;
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf in, ByteBuf out, int length)
/*     */   {
/*  62 */     for (int i = 0;; i++) {
/*  63 */       int b = length >>> i * 7;
/*  64 */       if ((b & 0xFFFFFF80) != 0) {
/*  65 */         out.writeByte(b & 0x7F | 0x80);
/*     */       } else {
/*  67 */         out.writeByte(b);
/*  68 */         break;
/*     */       }
/*     */     }
/*     */     
/*  72 */     int inIndex = in.readerIndex();
/*  73 */     int baseIndex = inIndex;
/*     */     
/*  75 */     short[] table = getHashTable(length);
/*  76 */     int shift = 32 - (int)Math.floor(Math.log(table.length) / Math.log(2.0D));
/*     */     
/*  78 */     int nextEmit = inIndex;
/*     */     
/*  80 */     if (length - inIndex >= 15) {
/*  81 */       int nextHash = hash(in, ++inIndex, shift);
/*     */       for (;;) {
/*  83 */         int skip = 32;
/*     */         
/*     */ 
/*  86 */         int nextIndex = inIndex;
/*     */         int candidate;
/*  88 */         do { inIndex = nextIndex;
/*  89 */           int hash = nextHash;
/*  90 */           int bytesBetweenHashLookups = skip++ >> 5;
/*  91 */           nextIndex = inIndex + bytesBetweenHashLookups;
/*     */           
/*     */ 
/*  94 */           if (nextIndex > length - 4) {
/*     */             break;
/*     */           }
/*     */           
/*  98 */           nextHash = hash(in, nextIndex, shift);
/*     */           
/* 100 */           candidate = baseIndex + table[hash];
/*     */           
/* 102 */           table[hash] = ((short)(inIndex - baseIndex));
/*     */         }
/* 104 */         while (in.getInt(inIndex) != in.getInt(candidate));
/*     */         
/* 106 */         encodeLiteral(in, out, inIndex - nextEmit);
/*     */         int insertTail;
/*     */         do
/*     */         {
/* 110 */           int base = inIndex;
/* 111 */           int matched = 4 + findMatchingLength(in, candidate + 4, inIndex + 4, length);
/* 112 */           inIndex += matched;
/* 113 */           int offset = base - candidate;
/* 114 */           encodeCopy(out, offset, matched);
/* 115 */           in.readerIndex(in.readerIndex() + matched);
/* 116 */           insertTail = inIndex - 1;
/* 117 */           nextEmit = inIndex;
/* 118 */           if (inIndex >= length - 4) {
/*     */             break;
/*     */           }
/*     */           
/* 122 */           int prevHash = hash(in, insertTail, shift);
/* 123 */           table[prevHash] = ((short)(inIndex - baseIndex - 1));
/* 124 */           int currentHash = hash(in, insertTail + 1, shift);
/* 125 */           candidate = baseIndex + table[currentHash];
/* 126 */           table[currentHash] = ((short)(inIndex - baseIndex));
/*     */         }
/* 128 */         while (in.getInt(insertTail + 1) == in.getInt(candidate));
/*     */         
/* 130 */         nextHash = hash(in, insertTail + 2, shift);
/* 131 */         inIndex++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 136 */     if (nextEmit < length) {
/* 137 */       encodeLiteral(in, out, length - nextEmit);
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
/*     */ 
/*     */ 
/*     */   private static int hash(ByteBuf in, int index, int shift)
/*     */   {
/* 152 */     return in.getInt(index) + 506832829 >>> shift;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static short[] getHashTable(int inputSize)
/*     */   {
/* 162 */     int htSize = 256;
/* 163 */     while ((htSize < 16384) && (htSize < inputSize)) {
/* 164 */       htSize <<= 1;
/*     */     }
/*     */     short[] table;
/*     */     short[] table;
/* 168 */     if (htSize <= 256) {
/* 169 */       table = new short['Ā'];
/*     */     } else {
/* 171 */       table = new short['䀀'];
/*     */     }
/*     */     
/* 174 */     return table;
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
/*     */   private static int findMatchingLength(ByteBuf in, int minIndex, int inIndex, int maxIndex)
/*     */   {
/* 189 */     int matched = 0;
/*     */     
/* 191 */     while ((inIndex <= maxIndex - 4) && (in.getInt(inIndex) == in.getInt(minIndex + matched)))
/*     */     {
/* 193 */       inIndex += 4;
/* 194 */       matched += 4;
/*     */     }
/*     */     
/* 197 */     while ((inIndex < maxIndex) && (in.getByte(minIndex + matched) == in.getByte(inIndex))) {
/* 198 */       inIndex++;
/* 199 */       matched++;
/*     */     }
/*     */     
/* 202 */     return matched;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int bitsToEncode(int value)
/*     */   {
/* 214 */     int highestOneBit = Integer.highestOneBit(value);
/* 215 */     int bitLength = 0;
/* 216 */     while (highestOneBit >>= 1 != 0) {
/* 217 */       bitLength++;
/*     */     }
/*     */     
/* 220 */     return bitLength;
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
/*     */   private static void encodeLiteral(ByteBuf in, ByteBuf out, int length)
/*     */   {
/* 233 */     if (length < 61) {
/* 234 */       out.writeByte(length - 1 << 2);
/*     */     } else {
/* 236 */       int bitLength = bitsToEncode(length - 1);
/* 237 */       int bytesToEncode = 1 + bitLength / 8;
/* 238 */       out.writeByte(59 + bytesToEncode << 2);
/* 239 */       for (int i = 0; i < bytesToEncode; i++) {
/* 240 */         out.writeByte(length - 1 >> i * 8 & 0xFF);
/*     */       }
/*     */     }
/*     */     
/* 244 */     out.writeBytes(in, length);
/*     */   }
/*     */   
/*     */   private static void encodeCopyWithOffset(ByteBuf out, int offset, int length) {
/* 248 */     if ((length < 12) && (offset < 2048)) {
/* 249 */       out.writeByte(0x1 | length - 4 << 2 | offset >> 8 << 5);
/* 250 */       out.writeByte(offset & 0xFF);
/*     */     } else {
/* 252 */       out.writeByte(0x2 | length - 1 << 2);
/* 253 */       out.writeByte(offset & 0xFF);
/* 254 */       out.writeByte(offset >> 8 & 0xFF);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void encodeCopy(ByteBuf out, int offset, int length)
/*     */   {
/* 266 */     while (length >= 68) {
/* 267 */       encodeCopyWithOffset(out, offset, 64);
/* 268 */       length -= 64;
/*     */     }
/*     */     
/* 271 */     if (length > 64) {
/* 272 */       encodeCopyWithOffset(out, offset, 60);
/* 273 */       length -= 60;
/*     */     }
/*     */     
/* 276 */     encodeCopyWithOffset(out, offset, length);
/*     */   }
/*     */   
/*     */   public void decode(ByteBuf in, ByteBuf out) {
/* 280 */     while (in.isReadable()) {
/* 281 */       switch (this.state) {
/*     */       case READY: 
/* 283 */         this.state = State.READING_PREAMBLE;
/*     */       case READING_PREAMBLE: 
/* 285 */         int uncompressedLength = readPreamble(in);
/* 286 */         if (uncompressedLength == -1)
/*     */         {
/* 288 */           return;
/*     */         }
/* 290 */         if (uncompressedLength == 0)
/*     */         {
/* 292 */           this.state = State.READY;
/* 293 */           return;
/*     */         }
/* 295 */         out.ensureWritable(uncompressedLength);
/* 296 */         this.state = State.READING_TAG;
/*     */       case READING_TAG: 
/* 298 */         if (!in.isReadable()) {
/* 299 */           return;
/*     */         }
/* 301 */         this.tag = in.readByte();
/* 302 */         switch (this.tag & 0x3) {
/*     */         case 0: 
/* 304 */           this.state = State.READING_LITERAL;
/* 305 */           break;
/*     */         case 1: 
/*     */         case 2: 
/*     */         case 3: 
/* 309 */           this.state = State.READING_COPY;
/*     */         }
/*     */         
/* 312 */         break;
/*     */       case READING_LITERAL: 
/* 314 */         int literalWritten = decodeLiteral(this.tag, in, out);
/* 315 */         if (literalWritten != -1) {
/* 316 */           this.state = State.READING_TAG;
/* 317 */           this.written += literalWritten;
/*     */         }
/*     */         else {
/*     */           return;
/*     */         }
/*     */         break;
/*     */       case READING_COPY: 
/*     */         int decodeWritten;
/* 325 */         switch (this.tag & 0x3) {
/*     */         case 1: 
/* 327 */           decodeWritten = decodeCopyWith1ByteOffset(this.tag, in, out, this.written);
/* 328 */           if (decodeWritten != -1) {
/* 329 */             this.state = State.READING_TAG;
/* 330 */             this.written += decodeWritten;
/*     */           }
/*     */           else {
/*     */             return;
/*     */           }
/*     */           break;
/*     */         case 2: 
/* 337 */           decodeWritten = decodeCopyWith2ByteOffset(this.tag, in, out, this.written);
/* 338 */           if (decodeWritten != -1) {
/* 339 */             this.state = State.READING_TAG;
/* 340 */             this.written += decodeWritten;
/*     */           }
/*     */           else {
/*     */             return;
/*     */           }
/*     */           break;
/*     */         case 3: 
/* 347 */           decodeWritten = decodeCopyWith4ByteOffset(this.tag, in, out, this.written);
/* 348 */           if (decodeWritten != -1) {
/* 349 */             this.state = State.READING_TAG;
/* 350 */             this.written += decodeWritten;
/*     */           }
/*     */           else
/*     */           {
/*     */             return;
/*     */           }
/*     */           
/*     */ 
/*     */           break;
/*     */         }
/*     */         
/*     */         
/*     */         break;
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static int readPreamble(ByteBuf in)
/*     */   {
/* 371 */     int length = 0;
/* 372 */     int byteIndex = 0;
/* 373 */     while (in.isReadable()) {
/* 374 */       int current = in.readUnsignedByte();
/* 375 */       length |= (current & 0x7F) << byteIndex++ * 7;
/* 376 */       if ((current & 0x80) == 0) {
/* 377 */         return length;
/*     */       }
/*     */       
/* 380 */       if (byteIndex >= 4) {
/* 381 */         throw new DecompressionException("Preamble is greater than 4 bytes");
/*     */       }
/*     */     }
/*     */     
/* 385 */     return 0;
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
/*     */   private static int decodeLiteral(byte tag, ByteBuf in, ByteBuf out)
/*     */   {
/* 400 */     in.markReaderIndex();
/*     */     int length;
/* 402 */     switch (tag >> 2 & 0x3F) {
/*     */     case 60: 
/* 404 */       if (!in.isReadable()) {
/* 405 */         return -1;
/*     */       }
/* 407 */       length = in.readUnsignedByte();
/* 408 */       break;
/*     */     case 61: 
/* 410 */       if (in.readableBytes() < 2) {
/* 411 */         return -1;
/*     */       }
/* 413 */       length = ByteBufUtil.swapShort(in.readShort());
/* 414 */       break;
/*     */     case 62: 
/* 416 */       if (in.readableBytes() < 3) {
/* 417 */         return -1;
/*     */       }
/* 419 */       length = ByteBufUtil.swapMedium(in.readUnsignedMedium());
/* 420 */       break;
/*     */     case 64: 
/* 422 */       if (in.readableBytes() < 4) {
/* 423 */         return -1;
/*     */       }
/* 425 */       length = ByteBufUtil.swapInt(in.readInt());
/* 426 */       break;
/*     */     case 63: default: 
/* 428 */       length = tag >> 2 & 0x3F;
/*     */     }
/* 430 */     length++;
/*     */     
/* 432 */     if (in.readableBytes() < length) {
/* 433 */       in.resetReaderIndex();
/* 434 */       return -1;
/*     */     }
/*     */     
/* 437 */     out.writeBytes(in, length);
/* 438 */     return length;
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
/*     */   private static int decodeCopyWith1ByteOffset(byte tag, ByteBuf in, ByteBuf out, int writtenSoFar)
/*     */   {
/* 455 */     if (!in.isReadable()) {
/* 456 */       return -1;
/*     */     }
/*     */     
/* 459 */     int initialIndex = out.writerIndex();
/* 460 */     int length = 4 + ((tag & 0x1C) >> 2);
/* 461 */     int offset = (tag & 0xE0) << 8 >> 5 | in.readUnsignedByte();
/*     */     
/* 463 */     validateOffset(offset, writtenSoFar);
/*     */     
/* 465 */     out.markReaderIndex();
/* 466 */     if (offset < length) {
/* 467 */       for (int copies = length / offset; 
/* 468 */           copies > 0; copies--) {
/* 469 */         out.readerIndex(initialIndex - offset);
/* 470 */         out.readBytes(out, offset);
/*     */       }
/* 472 */       if (length % offset != 0) {
/* 473 */         out.readerIndex(initialIndex - offset);
/* 474 */         out.readBytes(out, length % offset);
/*     */       }
/*     */     } else {
/* 477 */       out.readerIndex(initialIndex - offset);
/* 478 */       out.readBytes(out, length);
/*     */     }
/* 480 */     out.resetReaderIndex();
/*     */     
/* 482 */     return length;
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
/*     */   private static int decodeCopyWith2ByteOffset(byte tag, ByteBuf in, ByteBuf out, int writtenSoFar)
/*     */   {
/* 499 */     if (in.readableBytes() < 2) {
/* 500 */       return -1;
/*     */     }
/*     */     
/* 503 */     int initialIndex = out.writerIndex();
/* 504 */     int length = 1 + (tag >> 2 & 0x3F);
/* 505 */     int offset = ByteBufUtil.swapShort(in.readShort());
/*     */     
/* 507 */     validateOffset(offset, writtenSoFar);
/*     */     
/* 509 */     out.markReaderIndex();
/* 510 */     if (offset < length) {
/* 511 */       for (int copies = length / offset; 
/* 512 */           copies > 0; copies--) {
/* 513 */         out.readerIndex(initialIndex - offset);
/* 514 */         out.readBytes(out, offset);
/*     */       }
/* 516 */       if (length % offset != 0) {
/* 517 */         out.readerIndex(initialIndex - offset);
/* 518 */         out.readBytes(out, length % offset);
/*     */       }
/*     */     } else {
/* 521 */       out.readerIndex(initialIndex - offset);
/* 522 */       out.readBytes(out, length);
/*     */     }
/* 524 */     out.resetReaderIndex();
/*     */     
/* 526 */     return length;
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
/*     */   private static int decodeCopyWith4ByteOffset(byte tag, ByteBuf in, ByteBuf out, int writtenSoFar)
/*     */   {
/* 543 */     if (in.readableBytes() < 4) {
/* 544 */       return -1;
/*     */     }
/*     */     
/* 547 */     int initialIndex = out.writerIndex();
/* 548 */     int length = 1 + (tag >> 2 & 0x3F);
/* 549 */     int offset = ByteBufUtil.swapInt(in.readInt());
/*     */     
/* 551 */     validateOffset(offset, writtenSoFar);
/*     */     
/* 553 */     out.markReaderIndex();
/* 554 */     if (offset < length) {
/* 555 */       for (int copies = length / offset; 
/* 556 */           copies > 0; copies--) {
/* 557 */         out.readerIndex(initialIndex - offset);
/* 558 */         out.readBytes(out, offset);
/*     */       }
/* 560 */       if (length % offset != 0) {
/* 561 */         out.readerIndex(initialIndex - offset);
/* 562 */         out.readBytes(out, length % offset);
/*     */       }
/*     */     } else {
/* 565 */       out.readerIndex(initialIndex - offset);
/* 566 */       out.readBytes(out, length);
/*     */     }
/* 568 */     out.resetReaderIndex();
/*     */     
/* 570 */     return length;
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
/*     */   private static void validateOffset(int offset, int chunkSizeSoFar)
/*     */   {
/* 583 */     if (offset > 32767) {
/* 584 */       throw new DecompressionException("Offset exceeds maximum permissible value");
/*     */     }
/*     */     
/* 587 */     if (offset <= 0) {
/* 588 */       throw new DecompressionException("Offset is less than minimum permissible value");
/*     */     }
/*     */     
/* 591 */     if (offset > chunkSizeSoFar) {
/* 592 */       throw new DecompressionException("Offset exceeds size of chunk");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int calculateChecksum(ByteBuf data)
/*     */   {
/* 603 */     return calculateChecksum(data, data.readerIndex(), data.readableBytes());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int calculateChecksum(ByteBuf data, int offset, int length)
/*     */   {
/* 613 */     Crc32c crc32 = new Crc32c();
/*     */     try { byte[] array;
/* 615 */       if (data.hasArray()) {
/* 616 */         crc32.update(data.array(), data.arrayOffset() + offset, length);
/*     */       } else {
/* 618 */         array = new byte[length];
/* 619 */         data.getBytes(offset, array);
/* 620 */         crc32.update(array, 0, length);
/*     */       }
/*     */       
/* 623 */       return maskChecksum((int)crc32.getValue());
/*     */     } finally {
/* 625 */       crc32.reset();
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
/*     */ 
/*     */   static void validateChecksum(int expectedChecksum, ByteBuf data)
/*     */   {
/* 639 */     validateChecksum(expectedChecksum, data, data.readerIndex(), data.readableBytes());
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
/*     */   static void validateChecksum(int expectedChecksum, ByteBuf data, int offset, int length)
/*     */   {
/* 652 */     int actualChecksum = calculateChecksum(data, offset, length);
/* 653 */     if (actualChecksum != expectedChecksum) {
/* 654 */       throw new DecompressionException("mismatching checksum: " + Integer.toHexString(actualChecksum) + " (expected: " + Integer.toHexString(expectedChecksum) + ')');
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int maskChecksum(int checksum)
/*     */   {
/* 672 */     return (checksum >> 15 | checksum << 17) + -1568478504;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Snappy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */