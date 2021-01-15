/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class FastLz
/*     */ {
/*     */   private static final int MAX_DISTANCE = 8191;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int MAX_FARDISTANCE = 73725;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int HASH_LOG = 13;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int HASH_SIZE = 8192;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int HASH_MASK = 8191;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int MAX_COPY = 32;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int MAX_LEN = 264;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int MIN_RECOMENDED_LENGTH_FOR_LEVEL_2 = 65536;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int MAGIC_NUMBER = 4607066;
/*     */   
/*     */ 
/*     */ 
/*     */   static final byte BLOCK_TYPE_NON_COMPRESSED = 0;
/*     */   
/*     */ 
/*     */ 
/*     */   static final byte BLOCK_TYPE_COMPRESSED = 1;
/*     */   
/*     */ 
/*     */ 
/*     */   static final byte BLOCK_WITHOUT_CHECKSUM = 0;
/*     */   
/*     */ 
/*     */ 
/*     */   static final byte BLOCK_WITH_CHECKSUM = 16;
/*     */   
/*     */ 
/*     */   static final int OPTIONS_OFFSET = 3;
/*     */   
/*     */ 
/*     */   static final int CHECKSUM_OFFSET = 4;
/*     */   
/*     */ 
/*     */   static final int MAX_CHUNK_LENGTH = 65535;
/*     */   
/*     */ 
/*     */   static final int MIN_LENGTH_TO_COMPRESSION = 32;
/*     */   
/*     */ 
/*     */   static final int LEVEL_AUTO = 0;
/*     */   
/*     */ 
/*     */   static final int LEVEL_1 = 1;
/*     */   
/*     */ 
/*     */   static final int LEVEL_2 = 2;
/*     */   
/*     */ 
/*     */ 
/*     */   static int calculateOutputBufferLength(int inputLength)
/*     */   {
/*  83 */     int outputLength = (int)(inputLength * 1.06D);
/*  84 */     return Math.max(outputLength, 66);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static int compress(byte[] input, int inOffset, int inLength, byte[] output, int outOffset, int proposedLevel)
/*     */   {
/*     */     int level;
/*     */     
/*     */ 
/*     */     int level;
/*     */     
/*  96 */     if (proposedLevel == 0) {
/*  97 */       level = inLength < 65536 ? 1 : 2;
/*     */     } else {
/*  99 */       level = proposedLevel;
/*     */     }
/*     */     
/* 102 */     int ip = 0;
/* 103 */     int ipBound = ip + inLength - 2;
/* 104 */     int ipLimit = ip + inLength - 12;
/*     */     
/* 106 */     int op = 0;
/*     */     
/*     */ 
/* 109 */     int[] htab = new int[' '];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 120 */     if (inLength < 4) {
/* 121 */       if (inLength != 0)
/*     */       {
/* 123 */         output[(outOffset + op++)] = ((byte)(inLength - 1));
/* 124 */         ipBound++;
/* 125 */         while (ip <= ipBound) {
/* 126 */           output[(outOffset + op++)] = input[(inOffset + ip++)];
/*     */         }
/* 128 */         return inLength + 1;
/*     */       }
/*     */       
/* 131 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 136 */     for (int hslot = 0; hslot < 8192; hslot++)
/*     */     {
/* 138 */       htab[hslot] = ip;
/*     */     }
/*     */     
/*     */ 
/* 142 */     int copy = 2;
/* 143 */     output[(outOffset + op++)] = 31;
/* 144 */     output[(outOffset + op++)] = input[(inOffset + ip++)];
/* 145 */     output[(outOffset + op++)] = input[(inOffset + ip++)];
/*     */     
/*     */ 
/* 148 */     while (ip < ipLimit) {
/* 149 */       int ref = 0;
/*     */       
/* 151 */       long distance = 0L;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 156 */       int len = 3;
/*     */       
/*     */ 
/* 159 */       int anchor = ip;
/*     */       
/* 161 */       boolean matchLabel = false;
/*     */       
/*     */ 
/* 164 */       if (level == 2)
/*     */       {
/* 166 */         if ((input[(inOffset + ip)] == input[(inOffset + ip - 1)]) && (readU16(input, inOffset + ip - 1) == readU16(input, inOffset + ip + 1)))
/*     */         {
/* 168 */           distance = 1L;
/* 169 */           ip += 3;
/* 170 */           ref = anchor - 1 + 3;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 175 */           matchLabel = true;
/*     */         }
/*     */       }
/* 178 */       if (!matchLabel)
/*     */       {
/*     */ 
/* 181 */         int hval = hashFunction(input, inOffset + ip);
/*     */         
/* 183 */         hslot = hval;
/*     */         
/* 185 */         ref = htab[hval];
/*     */         
/*     */ 
/* 188 */         distance = anchor - ref;
/*     */         
/*     */ 
/*     */ 
/* 192 */         htab[hslot] = anchor;
/*     */         
/*     */ 
/* 195 */         if ((distance != 0L) && (level == 1 ? distance < 8191L : distance < 73725L)) { if ((input[(inOffset + ref++)] == input[(inOffset + ip++)]) && (input[(inOffset + ref++)] == input[(inOffset + ip++)]) && (input[(inOffset + ref++)] == input[(inOffset + ip++)])) {}
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/*     */ 
/* 203 */           output[(outOffset + op++)] = input[(inOffset + anchor++)];
/* 204 */           ip = anchor;
/* 205 */           copy++;
/* 206 */           if (copy != 32) continue;
/* 207 */           copy = 0;
/* 208 */           output[(outOffset + op++)] = 31; continue;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 213 */         if (level == 2)
/*     */         {
/* 215 */           if (distance >= 8191L) {
/* 216 */             if ((input[(inOffset + ip++)] != input[(inOffset + ref++)]) || (input[(inOffset + ip++)] != input[(inOffset + ref++)]))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 221 */               output[(outOffset + op++)] = input[(inOffset + anchor++)];
/* 222 */               ip = anchor;
/* 223 */               copy++;
/* 224 */               if (copy != 32) continue;
/* 225 */               copy = 0;
/* 226 */               output[(outOffset + op++)] = 31; continue;
/*     */             }
/*     */             
/*     */ 
/* 230 */             len += 2;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 238 */       ip = anchor + len;
/*     */       
/*     */ 
/* 241 */       distance -= 1L;
/*     */       
/* 243 */       if (distance == 0L)
/*     */       {
/*     */ 
/* 246 */         byte x = input[(inOffset + ip - 1)];
/* 247 */         while ((ip < ipBound) && 
/* 248 */           (input[(inOffset + ref++)] == x))
/*     */         {
/*     */ 
/* 251 */           ip++;
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 257 */       else if (input[(inOffset + ref++)] == input[(inOffset + ip++)])
/*     */       {
/*     */ 
/* 260 */         if (input[(inOffset + ref++)] == input[(inOffset + ip++)])
/*     */         {
/*     */ 
/* 263 */           if (input[(inOffset + ref++)] == input[(inOffset + ip++)])
/*     */           {
/*     */ 
/* 266 */             if (input[(inOffset + ref++)] == input[(inOffset + ip++)])
/*     */             {
/*     */ 
/* 269 */               if (input[(inOffset + ref++)] == input[(inOffset + ip++)])
/*     */               {
/*     */ 
/* 272 */                 if (input[(inOffset + ref++)] == input[(inOffset + ip++)])
/*     */                 {
/*     */ 
/* 275 */                   if (input[(inOffset + ref++)] == input[(inOffset + ip++)])
/*     */                   {
/*     */ 
/* 278 */                     if (input[(inOffset + ref++)] == input[(inOffset + ip++)])
/*     */                     {
/*     */ 
/* 281 */                       while (ip < ipBound)
/* 282 */                         if (input[(inOffset + ref++)] != input[(inOffset + ip++)])
/*     */                           break; }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 291 */       if (copy != 0)
/*     */       {
/*     */ 
/* 294 */         output[(outOffset + op - copy - 1)] = ((byte)(copy - 1));
/*     */       }
/*     */       else {
/* 297 */         op--;
/*     */       }
/*     */       
/*     */ 
/* 301 */       copy = 0;
/*     */       
/*     */ 
/* 304 */       ip -= 3;
/* 305 */       len = ip - anchor;
/*     */       
/*     */ 
/* 308 */       if (level == 2) {
/* 309 */         if (distance < 8191L) {
/* 310 */           if (len < 7) {
/* 311 */             output[(outOffset + op++)] = ((byte)(int)((len << 5) + (distance >>> 8)));
/* 312 */             output[(outOffset + op++)] = ((byte)(int)(distance & 0xFF));
/*     */           } else {
/* 314 */             output[(outOffset + op++)] = ((byte)(int)(224L + (distance >>> 8)));
/* 315 */             for (len -= 7; len >= 255; len -= 255) {
/* 316 */               output[(outOffset + op++)] = -1;
/*     */             }
/* 318 */             output[(outOffset + op++)] = ((byte)len);
/* 319 */             output[(outOffset + op++)] = ((byte)(int)(distance & 0xFF));
/*     */           }
/*     */           
/*     */         }
/* 323 */         else if (len < 7) {
/* 324 */           distance -= 8191L;
/* 325 */           output[(outOffset + op++)] = ((byte)((len << 5) + 31));
/* 326 */           output[(outOffset + op++)] = -1;
/* 327 */           output[(outOffset + op++)] = ((byte)(int)(distance >>> 8));
/* 328 */           output[(outOffset + op++)] = ((byte)(int)(distance & 0xFF));
/*     */         } else {
/* 330 */           distance -= 8191L;
/* 331 */           output[(outOffset + op++)] = -1;
/* 332 */           for (len -= 7; len >= 255; len -= 255) {
/* 333 */             output[(outOffset + op++)] = -1;
/*     */           }
/* 335 */           output[(outOffset + op++)] = ((byte)len);
/* 336 */           output[(outOffset + op++)] = -1;
/* 337 */           output[(outOffset + op++)] = ((byte)(int)(distance >>> 8));
/* 338 */           output[(outOffset + op++)] = ((byte)(int)(distance & 0xFF));
/*     */         }
/*     */       }
/*     */       else {
/* 342 */         if (len > 262) {
/* 343 */           while (len > 262) {
/* 344 */             output[(outOffset + op++)] = ((byte)(int)(224L + (distance >>> 8)));
/* 345 */             output[(outOffset + op++)] = -3;
/* 346 */             output[(outOffset + op++)] = ((byte)(int)(distance & 0xFF));
/* 347 */             len -= 262;
/*     */           }
/*     */         }
/*     */         
/* 351 */         if (len < 7) {
/* 352 */           output[(outOffset + op++)] = ((byte)(int)((len << 5) + (distance >>> 8)));
/* 353 */           output[(outOffset + op++)] = ((byte)(int)(distance & 0xFF));
/*     */         } else {
/* 355 */           output[(outOffset + op++)] = ((byte)(int)(224L + (distance >>> 8)));
/* 356 */           output[(outOffset + op++)] = ((byte)(len - 7));
/* 357 */           output[(outOffset + op++)] = ((byte)(int)(distance & 0xFF));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 363 */       int hval = hashFunction(input, inOffset + ip);
/* 364 */       htab[hval] = (ip++);
/*     */       
/*     */ 
/* 367 */       hval = hashFunction(input, inOffset + ip);
/* 368 */       htab[hval] = (ip++);
/*     */       
/*     */ 
/* 371 */       output[(outOffset + op++)] = 31;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 390 */     ipBound++;
/* 391 */     while (ip <= ipBound) {
/* 392 */       output[(outOffset + op++)] = input[(inOffset + ip++)];
/* 393 */       copy++;
/* 394 */       if (copy == 32) {
/* 395 */         copy = 0;
/* 396 */         output[(outOffset + op++)] = 31;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 401 */     if (copy != 0)
/*     */     {
/* 403 */       output[(outOffset + op - copy - 1)] = ((byte)(copy - 1));
/*     */     } else {
/* 405 */       op--;
/*     */     }
/*     */     
/* 408 */     if (level == 2)
/*     */     {
/* 410 */       int tmp1583_1581 = outOffset; byte[] tmp1583_1580 = output;tmp1583_1580[tmp1583_1581] = ((byte)(tmp1583_1580[tmp1583_1581] | 0x20));
/*     */     }
/*     */     
/* 413 */     return op;
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
/*     */   static int decompress(byte[] input, int inOffset, int inLength, byte[] output, int outOffset, int outLength)
/*     */   {
/* 427 */     int level = (input[inOffset] >> 5) + 1;
/* 428 */     if ((level != 1) && (level != 2)) {
/* 429 */       throw new DecompressionException(String.format("invalid level: %d (expected: %d or %d)", new Object[] { Integer.valueOf(level), Integer.valueOf(1), Integer.valueOf(2) }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 435 */     int ip = 0;
/*     */     
/* 437 */     int op = 0;
/*     */     
/* 439 */     long ctrl = input[(inOffset + ip++)] & 0x1F;
/*     */     
/* 441 */     int loop = 1;
/*     */     do
/*     */     {
/* 444 */       int ref = op;
/*     */       
/* 446 */       long len = ctrl >> 5;
/*     */       
/* 448 */       long ofs = (ctrl & 0x1F) << 8;
/*     */       
/* 450 */       if (ctrl >= 32L) {
/* 451 */         len -= 1L;
/*     */         
/* 453 */         ref = (int)(ref - ofs);
/*     */         
/*     */ 
/* 456 */         if (len == 6L) {
/* 457 */           if (level == 1)
/*     */           {
/* 459 */             len += (input[(inOffset + ip++)] & 0xFF);
/*     */           } else {
/*     */             int code;
/* 462 */             do { code = input[(inOffset + ip++)] & 0xFF;
/* 463 */               len += code;
/* 464 */             } while (code == 255);
/*     */           }
/*     */         }
/* 467 */         if (level == 1)
/*     */         {
/* 469 */           ref -= (input[(inOffset + ip++)] & 0xFF);
/*     */         } else {
/* 471 */           int code = input[(inOffset + ip++)] & 0xFF;
/* 472 */           ref -= code;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 477 */           if ((code == 255) && (ofs == 7936L)) {
/* 478 */             ofs = (input[(inOffset + ip++)] & 0xFF) << 8;
/* 479 */             ofs += (input[(inOffset + ip++)] & 0xFF);
/*     */             
/* 481 */             ref = (int)(op - ofs - 8191L);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 486 */         if (op + len + 3L > outLength) {
/* 487 */           return 0;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 493 */         if (ref - 1 < 0) {
/* 494 */           return 0;
/*     */         }
/*     */         
/* 497 */         if (ip < inLength) {
/* 498 */           ctrl = input[(inOffset + ip++)] & 0xFF;
/*     */         } else {
/* 500 */           loop = 0;
/*     */         }
/*     */         
/* 503 */         if (ref == op)
/*     */         {
/*     */ 
/* 506 */           byte b = output[(outOffset + ref - 1)];
/* 507 */           output[(outOffset + op++)] = b;
/* 508 */           output[(outOffset + op++)] = b;
/* 509 */           output[(outOffset + op++)] = b;
/* 510 */           while (len != 0L) {
/* 511 */             output[(outOffset + op++)] = b;
/* 512 */             len -= 1L;
/*     */           }
/*     */         }
/*     */         else {
/* 516 */           ref--;
/*     */           
/*     */ 
/* 519 */           output[(outOffset + op++)] = output[(outOffset + ref++)];
/* 520 */           output[(outOffset + op++)] = output[(outOffset + ref++)];
/* 521 */           output[(outOffset + op++)] = output[(outOffset + ref++)];
/*     */           
/* 523 */           while (len != 0L) {
/* 524 */             output[(outOffset + op++)] = output[(outOffset + ref++)];
/* 525 */             len -= 1L;
/*     */           }
/*     */         }
/*     */       } else {
/* 529 */         ctrl += 1L;
/*     */         
/* 531 */         if (op + ctrl > outLength) {
/* 532 */           return 0;
/*     */         }
/* 534 */         if (ip + ctrl > inLength) {
/* 535 */           return 0;
/*     */         }
/*     */         
/*     */ 
/* 539 */         output[(outOffset + op++)] = input[(inOffset + ip++)];
/*     */         
/* 541 */         for (ctrl -= 1L; ctrl != 0L; ctrl -= 1L)
/*     */         {
/* 543 */           output[(outOffset + op++)] = input[(inOffset + ip++)];
/*     */         }
/*     */         
/* 546 */         loop = ip < inLength ? 1 : 0;
/* 547 */         if (loop != 0)
/*     */         {
/* 549 */           ctrl = input[(inOffset + ip++)] & 0xFF;
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 554 */     } while (loop != 0);
/*     */     
/*     */ 
/* 557 */     return op;
/*     */   }
/*     */   
/*     */   private static int hashFunction(byte[] p, int offset) {
/* 561 */     int v = readU16(p, offset);
/* 562 */     v ^= readU16(p, offset + 1) ^ v >> 3;
/* 563 */     v &= 0x1FFF;
/* 564 */     return v;
/*     */   }
/*     */   
/*     */   private static int readU16(byte[] data, int offset) {
/* 568 */     if (offset + 1 >= data.length) {
/* 569 */       return data[offset] & 0xFF;
/*     */     }
/* 571 */     return (data[(offset + 1)] & 0xFF) << 8 | data[offset] & 0xFF;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\FastLz.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */