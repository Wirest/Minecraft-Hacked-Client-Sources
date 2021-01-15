/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class PoolArena<T>
/*     */ {
/*     */   static final int numTinySubpagePools = 32;
/*     */   final PooledByteBufAllocator parent;
/*     */   private final int maxOrder;
/*     */   final int pageSize;
/*     */   final int pageShifts;
/*     */   final int chunkSize;
/*     */   final int subpageOverflowMask;
/*     */   final int numSmallSubpagePools;
/*     */   private final PoolSubpage<T>[] tinySubpagePools;
/*     */   private final PoolSubpage<T>[] smallSubpagePools;
/*     */   private final PoolChunkList<T> q050;
/*     */   private final PoolChunkList<T> q025;
/*     */   private final PoolChunkList<T> q000;
/*     */   private final PoolChunkList<T> qInit;
/*     */   private final PoolChunkList<T> q075;
/*     */   private final PoolChunkList<T> q100;
/*     */   
/*     */   protected PoolArena(PooledByteBufAllocator parent, int pageSize, int maxOrder, int pageShifts, int chunkSize)
/*     */   {
/*  50 */     this.parent = parent;
/*  51 */     this.pageSize = pageSize;
/*  52 */     this.maxOrder = maxOrder;
/*  53 */     this.pageShifts = pageShifts;
/*  54 */     this.chunkSize = chunkSize;
/*  55 */     this.subpageOverflowMask = (pageSize - 1 ^ 0xFFFFFFFF);
/*  56 */     this.tinySubpagePools = newSubpagePoolArray(32);
/*  57 */     for (int i = 0; i < this.tinySubpagePools.length; i++) {
/*  58 */       this.tinySubpagePools[i] = newSubpagePoolHead(pageSize);
/*     */     }
/*     */     
/*  61 */     this.numSmallSubpagePools = (pageShifts - 9);
/*  62 */     this.smallSubpagePools = newSubpagePoolArray(this.numSmallSubpagePools);
/*  63 */     for (int i = 0; i < this.smallSubpagePools.length; i++) {
/*  64 */       this.smallSubpagePools[i] = newSubpagePoolHead(pageSize);
/*     */     }
/*     */     
/*  67 */     this.q100 = new PoolChunkList(this, null, 100, Integer.MAX_VALUE);
/*  68 */     this.q075 = new PoolChunkList(this, this.q100, 75, 100);
/*  69 */     this.q050 = new PoolChunkList(this, this.q075, 50, 100);
/*  70 */     this.q025 = new PoolChunkList(this, this.q050, 25, 75);
/*  71 */     this.q000 = new PoolChunkList(this, this.q025, 1, 50);
/*  72 */     this.qInit = new PoolChunkList(this, this.q000, Integer.MIN_VALUE, 25);
/*     */     
/*  74 */     this.q100.prevList = this.q075;
/*  75 */     this.q075.prevList = this.q050;
/*  76 */     this.q050.prevList = this.q025;
/*  77 */     this.q025.prevList = this.q000;
/*  78 */     this.q000.prevList = null;
/*  79 */     this.qInit.prevList = this.qInit;
/*     */   }
/*     */   
/*     */   private PoolSubpage<T> newSubpagePoolHead(int pageSize) {
/*  83 */     PoolSubpage<T> head = new PoolSubpage(pageSize);
/*  84 */     head.prev = head;
/*  85 */     head.next = head;
/*  86 */     return head;
/*     */   }
/*     */   
/*     */   private PoolSubpage<T>[] newSubpagePoolArray(int size)
/*     */   {
/*  91 */     return new PoolSubpage[size];
/*     */   }
/*     */   
/*     */   abstract boolean isDirect();
/*     */   
/*     */   PooledByteBuf<T> allocate(PoolThreadCache cache, int reqCapacity, int maxCapacity) {
/*  97 */     PooledByteBuf<T> buf = newByteBuf(maxCapacity);
/*  98 */     allocate(cache, buf, reqCapacity);
/*  99 */     return buf;
/*     */   }
/*     */   
/*     */   static int tinyIdx(int normCapacity) {
/* 103 */     return normCapacity >>> 4;
/*     */   }
/*     */   
/*     */   static int smallIdx(int normCapacity) {
/* 107 */     int tableIdx = 0;
/* 108 */     int i = normCapacity >>> 10;
/* 109 */     while (i != 0) {
/* 110 */       i >>>= 1;
/* 111 */       tableIdx++;
/*     */     }
/* 113 */     return tableIdx;
/*     */   }
/*     */   
/*     */   boolean isTinyOrSmall(int normCapacity)
/*     */   {
/* 118 */     return (normCapacity & this.subpageOverflowMask) == 0;
/*     */   }
/*     */   
/*     */   static boolean isTiny(int normCapacity)
/*     */   {
/* 123 */     return (normCapacity & 0xFE00) == 0;
/*     */   }
/*     */   
/*     */   private void allocate(PoolThreadCache cache, PooledByteBuf<T> buf, int reqCapacity) {
/* 127 */     int normCapacity = normalizeCapacity(reqCapacity);
/* 128 */     if (isTinyOrSmall(normCapacity)) { PoolSubpage<T>[] table;
/*     */       int tableIdx;
/*     */       PoolSubpage<T>[] table;
/* 131 */       if (isTiny(normCapacity)) {
/* 132 */         if (cache.allocateTiny(this, buf, reqCapacity, normCapacity))
/*     */         {
/* 134 */           return;
/*     */         }
/* 136 */         int tableIdx = tinyIdx(normCapacity);
/* 137 */         table = this.tinySubpagePools;
/*     */       } else {
/* 139 */         if (cache.allocateSmall(this, buf, reqCapacity, normCapacity))
/*     */         {
/* 141 */           return;
/*     */         }
/* 143 */         tableIdx = smallIdx(normCapacity);
/* 144 */         table = this.smallSubpagePools;
/*     */       }
/*     */       
/* 147 */       synchronized (this) {
/* 148 */         PoolSubpage<T> head = table[tableIdx];
/* 149 */         PoolSubpage<T> s = head.next;
/* 150 */         if (s != head) {
/* 151 */           assert ((s.doNotDestroy) && (s.elemSize == normCapacity));
/* 152 */           long handle = s.allocate();
/* 153 */           assert (handle >= 0L);
/* 154 */           s.chunk.initBufWithSubpage(buf, handle, reqCapacity);
/* 155 */           return;
/*     */         }
/*     */       }
/* 158 */     } else if (normCapacity <= this.chunkSize) {
/* 159 */       if (!cache.allocateNormal(this, buf, reqCapacity, normCapacity)) {}
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 165 */       allocateHuge(buf, reqCapacity);
/* 166 */       return;
/*     */     }
/* 168 */     allocateNormal(buf, reqCapacity, normCapacity);
/*     */   }
/*     */   
/*     */   private synchronized void allocateNormal(PooledByteBuf<T> buf, int reqCapacity, int normCapacity) {
/* 172 */     if ((this.q050.allocate(buf, reqCapacity, normCapacity)) || (this.q025.allocate(buf, reqCapacity, normCapacity)) || (this.q000.allocate(buf, reqCapacity, normCapacity)) || (this.qInit.allocate(buf, reqCapacity, normCapacity)) || (this.q075.allocate(buf, reqCapacity, normCapacity)) || (this.q100.allocate(buf, reqCapacity, normCapacity)))
/*     */     {
/*     */ 
/* 175 */       return;
/*     */     }
/*     */     
/*     */ 
/* 179 */     PoolChunk<T> c = newChunk(this.pageSize, this.maxOrder, this.pageShifts, this.chunkSize);
/* 180 */     long handle = c.allocate(normCapacity);
/* 181 */     assert (handle > 0L);
/* 182 */     c.initBuf(buf, handle, reqCapacity);
/* 183 */     this.qInit.add(c);
/*     */   }
/*     */   
/*     */   private void allocateHuge(PooledByteBuf<T> buf, int reqCapacity) {
/* 187 */     buf.initUnpooled(newUnpooledChunk(reqCapacity), reqCapacity);
/*     */   }
/*     */   
/*     */   void free(PoolChunk<T> chunk, long handle, int normCapacity, boolean sameThreads) {
/* 191 */     if (chunk.unpooled) {
/* 192 */       destroyChunk(chunk);
/*     */     } else {
/* 194 */       if (sameThreads) {
/* 195 */         PoolThreadCache cache = (PoolThreadCache)this.parent.threadCache.get();
/* 196 */         if (cache.add(this, chunk, handle, normCapacity))
/*     */         {
/* 198 */           return;
/*     */         }
/*     */       }
/*     */       
/* 202 */       synchronized (this) {
/* 203 */         chunk.parent.free(chunk, handle);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   PoolSubpage<T> findSubpagePoolHead(int elemSize) { PoolSubpage<T>[] table;
/*     */     int tableIdx;
/*     */     PoolSubpage<T>[] table;
/* 211 */     if (isTiny(elemSize)) {
/* 212 */       int tableIdx = elemSize >>> 4;
/* 213 */       table = this.tinySubpagePools;
/*     */     } else {
/* 215 */       tableIdx = 0;
/* 216 */       elemSize >>>= 10;
/* 217 */       while (elemSize != 0) {
/* 218 */         elemSize >>>= 1;
/* 219 */         tableIdx++;
/*     */       }
/* 221 */       table = this.smallSubpagePools;
/*     */     }
/*     */     
/* 224 */     return table[tableIdx];
/*     */   }
/*     */   
/*     */   int normalizeCapacity(int reqCapacity) {
/* 228 */     if (reqCapacity < 0) {
/* 229 */       throw new IllegalArgumentException("capacity: " + reqCapacity + " (expected: 0+)");
/*     */     }
/* 231 */     if (reqCapacity >= this.chunkSize) {
/* 232 */       return reqCapacity;
/*     */     }
/*     */     
/* 235 */     if (!isTiny(reqCapacity))
/*     */     {
/*     */ 
/* 238 */       int normalizedCapacity = reqCapacity;
/* 239 */       normalizedCapacity--;
/* 240 */       normalizedCapacity |= normalizedCapacity >>> 1;
/* 241 */       normalizedCapacity |= normalizedCapacity >>> 2;
/* 242 */       normalizedCapacity |= normalizedCapacity >>> 4;
/* 243 */       normalizedCapacity |= normalizedCapacity >>> 8;
/* 244 */       normalizedCapacity |= normalizedCapacity >>> 16;
/* 245 */       normalizedCapacity++;
/*     */       
/* 247 */       if (normalizedCapacity < 0) {
/* 248 */         normalizedCapacity >>>= 1;
/*     */       }
/*     */       
/* 251 */       return normalizedCapacity;
/*     */     }
/*     */     
/*     */ 
/* 255 */     if ((reqCapacity & 0xF) == 0) {
/* 256 */       return reqCapacity;
/*     */     }
/*     */     
/* 259 */     return (reqCapacity & 0xFFFFFFF0) + 16;
/*     */   }
/*     */   
/*     */   void reallocate(PooledByteBuf<T> buf, int newCapacity, boolean freeOldMemory) {
/* 263 */     if ((newCapacity < 0) || (newCapacity > buf.maxCapacity())) {
/* 264 */       throw new IllegalArgumentException("newCapacity: " + newCapacity);
/*     */     }
/*     */     
/* 267 */     int oldCapacity = buf.length;
/* 268 */     if (oldCapacity == newCapacity) {
/* 269 */       return;
/*     */     }
/*     */     
/* 272 */     PoolChunk<T> oldChunk = buf.chunk;
/* 273 */     long oldHandle = buf.handle;
/* 274 */     T oldMemory = buf.memory;
/* 275 */     int oldOffset = buf.offset;
/* 276 */     int oldMaxLength = buf.maxLength;
/* 277 */     int readerIndex = buf.readerIndex();
/* 278 */     int writerIndex = buf.writerIndex();
/*     */     
/* 280 */     allocate((PoolThreadCache)this.parent.threadCache.get(), buf, newCapacity);
/* 281 */     if (newCapacity > oldCapacity) {
/* 282 */       memoryCopy(oldMemory, oldOffset, buf.memory, buf.offset, oldCapacity);
/*     */ 
/*     */     }
/* 285 */     else if (newCapacity < oldCapacity) {
/* 286 */       if (readerIndex < newCapacity) {
/* 287 */         if (writerIndex > newCapacity) {
/* 288 */           writerIndex = newCapacity;
/*     */         }
/* 290 */         memoryCopy(oldMemory, oldOffset + readerIndex, buf.memory, buf.offset + readerIndex, writerIndex - readerIndex);
/*     */       }
/*     */       else
/*     */       {
/* 294 */         readerIndex = writerIndex = newCapacity;
/*     */       }
/*     */     }
/*     */     
/* 298 */     buf.setIndex(readerIndex, writerIndex);
/*     */     
/* 300 */     if (freeOldMemory)
/* 301 */       free(oldChunk, oldHandle, oldMaxLength, buf.initThread == Thread.currentThread()); }
/*     */   
/*     */   protected abstract PoolChunk<T> newChunk(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */   
/*     */   protected abstract PoolChunk<T> newUnpooledChunk(int paramInt);
/*     */   
/*     */   protected abstract PooledByteBuf<T> newByteBuf(int paramInt);
/*     */   
/*     */   protected abstract void memoryCopy(T paramT1, int paramInt1, T paramT2, int paramInt2, int paramInt3);
/*     */   
/*     */   protected abstract void destroyChunk(PoolChunk<T> paramPoolChunk);
/* 312 */   public synchronized String toString() { StringBuilder buf = new StringBuilder().append("Chunk(s) at 0~25%:").append(StringUtil.NEWLINE).append(this.qInit).append(StringUtil.NEWLINE).append("Chunk(s) at 0~50%:").append(StringUtil.NEWLINE).append(this.q000).append(StringUtil.NEWLINE).append("Chunk(s) at 25~75%:").append(StringUtil.NEWLINE).append(this.q025).append(StringUtil.NEWLINE).append("Chunk(s) at 50~100%:").append(StringUtil.NEWLINE).append(this.q050).append(StringUtil.NEWLINE).append("Chunk(s) at 75~100%:").append(StringUtil.NEWLINE).append(this.q075).append(StringUtil.NEWLINE).append("Chunk(s) at 100%:").append(StringUtil.NEWLINE).append(this.q100).append(StringUtil.NEWLINE).append("tiny subpages:");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 338 */     for (int i = 1; i < this.tinySubpagePools.length; i++) {
/* 339 */       PoolSubpage<T> head = this.tinySubpagePools[i];
/* 340 */       if (head.next != head)
/*     */       {
/*     */ 
/*     */ 
/* 344 */         buf.append(StringUtil.NEWLINE).append(i).append(": ");
/*     */         
/*     */ 
/* 347 */         PoolSubpage<T> s = head.next;
/*     */         for (;;) {
/* 349 */           buf.append(s);
/* 350 */           s = s.next;
/* 351 */           if (s == head)
/*     */             break;
/*     */         }
/*     */       }
/*     */     }
/* 356 */     buf.append(StringUtil.NEWLINE).append("small subpages:");
/*     */     
/* 358 */     for (int i = 1; i < this.smallSubpagePools.length; i++) {
/* 359 */       PoolSubpage<T> head = this.smallSubpagePools[i];
/* 360 */       if (head.next != head)
/*     */       {
/*     */ 
/*     */ 
/* 364 */         buf.append(StringUtil.NEWLINE).append(i).append(": ");
/*     */         
/*     */ 
/* 367 */         PoolSubpage<T> s = head.next;
/*     */         for (;;) {
/* 369 */           buf.append(s);
/* 370 */           s = s.next;
/* 371 */           if (s == head)
/*     */             break;
/*     */         }
/*     */       }
/*     */     }
/* 376 */     buf.append(StringUtil.NEWLINE);
/*     */     
/* 378 */     return buf.toString();
/*     */   }
/*     */   
/*     */   static final class HeapArena extends PoolArena<byte[]>
/*     */   {
/*     */     HeapArena(PooledByteBufAllocator parent, int pageSize, int maxOrder, int pageShifts, int chunkSize) {
/* 384 */       super(pageSize, maxOrder, pageShifts, chunkSize);
/*     */     }
/*     */     
/*     */     boolean isDirect()
/*     */     {
/* 389 */       return false;
/*     */     }
/*     */     
/*     */     protected PoolChunk<byte[]> newChunk(int pageSize, int maxOrder, int pageShifts, int chunkSize)
/*     */     {
/* 394 */       return new PoolChunk(this, new byte[chunkSize], pageSize, maxOrder, pageShifts, chunkSize);
/*     */     }
/*     */     
/*     */     protected PoolChunk<byte[]> newUnpooledChunk(int capacity)
/*     */     {
/* 399 */       return new PoolChunk(this, new byte[capacity], capacity);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected void destroyChunk(PoolChunk<byte[]> chunk) {}
/*     */     
/*     */ 
/*     */     protected PooledByteBuf<byte[]> newByteBuf(int maxCapacity)
/*     */     {
/* 409 */       return PooledHeapByteBuf.newInstance(maxCapacity);
/*     */     }
/*     */     
/*     */     protected void memoryCopy(byte[] src, int srcOffset, byte[] dst, int dstOffset, int length)
/*     */     {
/* 414 */       if (length == 0) {
/* 415 */         return;
/*     */       }
/*     */       
/* 418 */       System.arraycopy(src, srcOffset, dst, dstOffset, length);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class DirectArena extends PoolArena<ByteBuffer>
/*     */   {
/* 424 */     private static final boolean HAS_UNSAFE = ;
/*     */     
/*     */     DirectArena(PooledByteBufAllocator parent, int pageSize, int maxOrder, int pageShifts, int chunkSize) {
/* 427 */       super(pageSize, maxOrder, pageShifts, chunkSize);
/*     */     }
/*     */     
/*     */     boolean isDirect()
/*     */     {
/* 432 */       return true;
/*     */     }
/*     */     
/*     */     protected PoolChunk<ByteBuffer> newChunk(int pageSize, int maxOrder, int pageShifts, int chunkSize)
/*     */     {
/* 437 */       return new PoolChunk(this, ByteBuffer.allocateDirect(chunkSize), pageSize, maxOrder, pageShifts, chunkSize);
/*     */     }
/*     */     
/*     */ 
/*     */     protected PoolChunk<ByteBuffer> newUnpooledChunk(int capacity)
/*     */     {
/* 443 */       return new PoolChunk(this, ByteBuffer.allocateDirect(capacity), capacity);
/*     */     }
/*     */     
/*     */     protected void destroyChunk(PoolChunk<ByteBuffer> chunk)
/*     */     {
/* 448 */       PlatformDependent.freeDirectBuffer((ByteBuffer)chunk.memory);
/*     */     }
/*     */     
/*     */     protected PooledByteBuf<ByteBuffer> newByteBuf(int maxCapacity)
/*     */     {
/* 453 */       if (HAS_UNSAFE) {
/* 454 */         return PooledUnsafeDirectByteBuf.newInstance(maxCapacity);
/*     */       }
/* 456 */       return PooledDirectByteBuf.newInstance(maxCapacity);
/*     */     }
/*     */     
/*     */ 
/*     */     protected void memoryCopy(ByteBuffer src, int srcOffset, ByteBuffer dst, int dstOffset, int length)
/*     */     {
/* 462 */       if (length == 0) {
/* 463 */         return;
/*     */       }
/*     */       
/* 466 */       if (HAS_UNSAFE) {
/* 467 */         PlatformDependent.copyMemory(PlatformDependent.directBufferAddress(src) + srcOffset, PlatformDependent.directBufferAddress(dst) + dstOffset, length);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 472 */         src = src.duplicate();
/* 473 */         dst = dst.duplicate();
/* 474 */         src.position(srcOffset).limit(srcOffset + length);
/* 475 */         dst.position(dstOffset);
/* 476 */         dst.put(src);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\PoolArena.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */