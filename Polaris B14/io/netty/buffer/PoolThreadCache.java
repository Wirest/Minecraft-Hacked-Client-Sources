/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.ThreadDeathWatcher;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PoolThreadCache
/*     */ {
/*  34 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PoolThreadCache.class);
/*     */   
/*     */   final PoolArena<byte[]> heapArena;
/*     */   
/*     */   final PoolArena<ByteBuffer> directArena;
/*     */   
/*     */   private final MemoryRegionCache<byte[]>[] tinySubPageHeapCaches;
/*     */   
/*     */   private final MemoryRegionCache<byte[]>[] smallSubPageHeapCaches;
/*     */   
/*     */   private final MemoryRegionCache<ByteBuffer>[] tinySubPageDirectCaches;
/*     */   
/*     */   private final MemoryRegionCache<ByteBuffer>[] smallSubPageDirectCaches;
/*     */   
/*     */   private final MemoryRegionCache<byte[]>[] normalHeapCaches;
/*     */   private final MemoryRegionCache<ByteBuffer>[] normalDirectCaches;
/*     */   private final int numShiftsNormalDirect;
/*     */   private final int numShiftsNormalHeap;
/*     */   private final int freeSweepAllocationThreshold;
/*     */   private int allocations;
/*  54 */   private final Thread thread = Thread.currentThread();
/*  55 */   private final Runnable freeTask = new Runnable()
/*     */   {
/*     */     public void run() {
/*  58 */       PoolThreadCache.this.free0();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   PoolThreadCache(PoolArena<byte[]> heapArena, PoolArena<ByteBuffer> directArena, int tinyCacheSize, int smallCacheSize, int normalCacheSize, int maxCachedBufferCapacity, int freeSweepAllocationThreshold)
/*     */   {
/*  68 */     if (maxCachedBufferCapacity < 0) {
/*  69 */       throw new IllegalArgumentException("maxCachedBufferCapacity: " + maxCachedBufferCapacity + " (expected: >= 0)");
/*     */     }
/*     */     
/*  72 */     if (freeSweepAllocationThreshold < 1) {
/*  73 */       throw new IllegalArgumentException("freeSweepAllocationThreshold: " + maxCachedBufferCapacity + " (expected: > 0)");
/*     */     }
/*     */     
/*  76 */     this.freeSweepAllocationThreshold = freeSweepAllocationThreshold;
/*  77 */     this.heapArena = heapArena;
/*  78 */     this.directArena = directArena;
/*  79 */     if (directArena != null) {
/*  80 */       this.tinySubPageDirectCaches = createSubPageCaches(tinyCacheSize, 32);
/*  81 */       this.smallSubPageDirectCaches = createSubPageCaches(smallCacheSize, directArena.numSmallSubpagePools);
/*     */       
/*  83 */       this.numShiftsNormalDirect = log2(directArena.pageSize);
/*  84 */       this.normalDirectCaches = createNormalCaches(normalCacheSize, maxCachedBufferCapacity, directArena);
/*     */     }
/*     */     else
/*     */     {
/*  88 */       this.tinySubPageDirectCaches = null;
/*  89 */       this.smallSubPageDirectCaches = null;
/*  90 */       this.normalDirectCaches = null;
/*  91 */       this.numShiftsNormalDirect = -1;
/*     */     }
/*  93 */     if (heapArena != null)
/*     */     {
/*  95 */       this.tinySubPageHeapCaches = createSubPageCaches(tinyCacheSize, 32);
/*  96 */       this.smallSubPageHeapCaches = createSubPageCaches(smallCacheSize, heapArena.numSmallSubpagePools);
/*     */       
/*  98 */       this.numShiftsNormalHeap = log2(heapArena.pageSize);
/*  99 */       this.normalHeapCaches = createNormalCaches(normalCacheSize, maxCachedBufferCapacity, heapArena);
/*     */     }
/*     */     else
/*     */     {
/* 103 */       this.tinySubPageHeapCaches = null;
/* 104 */       this.smallSubPageHeapCaches = null;
/* 105 */       this.normalHeapCaches = null;
/* 106 */       this.numShiftsNormalHeap = -1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 111 */     ThreadDeathWatcher.watch(this.thread, this.freeTask);
/*     */   }
/*     */   
/*     */   private static <T> SubPageMemoryRegionCache<T>[] createSubPageCaches(int cacheSize, int numCaches) {
/* 115 */     if (cacheSize > 0)
/*     */     {
/* 117 */       SubPageMemoryRegionCache<T>[] cache = new SubPageMemoryRegionCache[numCaches];
/* 118 */       for (int i = 0; i < cache.length; i++)
/*     */       {
/* 120 */         cache[i] = new SubPageMemoryRegionCache(cacheSize);
/*     */       }
/* 122 */       return cache;
/*     */     }
/* 124 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private static <T> NormalMemoryRegionCache<T>[] createNormalCaches(int cacheSize, int maxCachedBufferCapacity, PoolArena<T> area)
/*     */   {
/* 130 */     if (cacheSize > 0) {
/* 131 */       int max = Math.min(area.chunkSize, maxCachedBufferCapacity);
/* 132 */       int arraySize = Math.max(1, max / area.pageSize);
/*     */       
/*     */ 
/* 135 */       NormalMemoryRegionCache<T>[] cache = new NormalMemoryRegionCache[arraySize];
/* 136 */       for (int i = 0; i < cache.length; i++) {
/* 137 */         cache[i] = new NormalMemoryRegionCache(cacheSize);
/*     */       }
/* 139 */       return cache;
/*     */     }
/* 141 */     return null;
/*     */   }
/*     */   
/*     */   private static int log2(int val)
/*     */   {
/* 146 */     int res = 0;
/* 147 */     while (val > 1) {
/* 148 */       val >>= 1;
/* 149 */       res++;
/*     */     }
/* 151 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   boolean allocateTiny(PoolArena<?> area, PooledByteBuf<?> buf, int reqCapacity, int normCapacity)
/*     */   {
/* 158 */     return allocate(cacheForTiny(area, normCapacity), buf, reqCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   boolean allocateSmall(PoolArena<?> area, PooledByteBuf<?> buf, int reqCapacity, int normCapacity)
/*     */   {
/* 165 */     return allocate(cacheForSmall(area, normCapacity), buf, reqCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   boolean allocateNormal(PoolArena<?> area, PooledByteBuf<?> buf, int reqCapacity, int normCapacity)
/*     */   {
/* 172 */     return allocate(cacheForNormal(area, normCapacity), buf, reqCapacity);
/*     */   }
/*     */   
/*     */   private boolean allocate(MemoryRegionCache<?> cache, PooledByteBuf buf, int reqCapacity)
/*     */   {
/* 177 */     if (cache == null)
/*     */     {
/* 179 */       return false;
/*     */     }
/* 181 */     boolean allocated = cache.allocate(buf, reqCapacity);
/* 182 */     if (++this.allocations >= this.freeSweepAllocationThreshold) {
/* 183 */       this.allocations = 0;
/* 184 */       trim();
/*     */     }
/* 186 */     return allocated;
/*     */   }
/*     */   
/*     */ 
/*     */   boolean add(PoolArena<?> area, PoolChunk chunk, long handle, int normCapacity)
/*     */   {
/*     */     MemoryRegionCache<?> cache;
/*     */     
/*     */     MemoryRegionCache<?> cache;
/*     */     
/* 196 */     if (area.isTinyOrSmall(normCapacity)) { MemoryRegionCache<?> cache;
/* 197 */       if (PoolArena.isTiny(normCapacity)) {
/* 198 */         cache = cacheForTiny(area, normCapacity);
/*     */       } else {
/* 200 */         cache = cacheForSmall(area, normCapacity);
/*     */       }
/*     */     } else {
/* 203 */       cache = cacheForNormal(area, normCapacity);
/*     */     }
/* 205 */     if (cache == null) {
/* 206 */       return false;
/*     */     }
/* 208 */     return cache.add(chunk, handle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void free()
/*     */   {
/* 215 */     ThreadDeathWatcher.unwatch(this.thread, this.freeTask);
/* 216 */     free0();
/*     */   }
/*     */   
/*     */   private void free0() {
/* 220 */     int numFreed = free(this.tinySubPageDirectCaches) + free(this.smallSubPageDirectCaches) + free(this.normalDirectCaches) + free(this.tinySubPageHeapCaches) + free(this.smallSubPageHeapCaches) + free(this.normalHeapCaches);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 227 */     if ((numFreed > 0) && (logger.isDebugEnabled())) {
/* 228 */       logger.debug("Freed {} thread-local buffer(s) from thread: {}", Integer.valueOf(numFreed), this.thread.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   private static int free(MemoryRegionCache<?>[] caches) {
/* 233 */     if (caches == null) {
/* 234 */       return 0;
/*     */     }
/*     */     
/* 237 */     int numFreed = 0;
/* 238 */     for (MemoryRegionCache<?> c : caches) {
/* 239 */       numFreed += free(c);
/*     */     }
/* 241 */     return numFreed;
/*     */   }
/*     */   
/*     */   private static int free(MemoryRegionCache<?> cache) {
/* 245 */     if (cache == null) {
/* 246 */       return 0;
/*     */     }
/* 248 */     return cache.free();
/*     */   }
/*     */   
/*     */   void trim() {
/* 252 */     trim(this.tinySubPageDirectCaches);
/* 253 */     trim(this.smallSubPageDirectCaches);
/* 254 */     trim(this.normalDirectCaches);
/* 255 */     trim(this.tinySubPageHeapCaches);
/* 256 */     trim(this.smallSubPageHeapCaches);
/* 257 */     trim(this.normalHeapCaches);
/*     */   }
/*     */   
/*     */   private static void trim(MemoryRegionCache<?>[] caches) {
/* 261 */     if (caches == null) {
/* 262 */       return;
/*     */     }
/* 264 */     for (MemoryRegionCache<?> c : caches) {
/* 265 */       trim(c);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void trim(MemoryRegionCache<?> cache) {
/* 270 */     if (cache == null) {
/* 271 */       return;
/*     */     }
/* 273 */     cache.trim();
/*     */   }
/*     */   
/*     */   private MemoryRegionCache<?> cacheForTiny(PoolArena<?> area, int normCapacity) {
/* 277 */     int idx = PoolArena.tinyIdx(normCapacity);
/* 278 */     if (area.isDirect()) {
/* 279 */       return cache(this.tinySubPageDirectCaches, idx);
/*     */     }
/* 281 */     return cache(this.tinySubPageHeapCaches, idx);
/*     */   }
/*     */   
/*     */   private MemoryRegionCache<?> cacheForSmall(PoolArena<?> area, int normCapacity) {
/* 285 */     int idx = PoolArena.smallIdx(normCapacity);
/* 286 */     if (area.isDirect()) {
/* 287 */       return cache(this.smallSubPageDirectCaches, idx);
/*     */     }
/* 289 */     return cache(this.smallSubPageHeapCaches, idx);
/*     */   }
/*     */   
/*     */   private MemoryRegionCache<?> cacheForNormal(PoolArena<?> area, int normCapacity) {
/* 293 */     if (area.isDirect()) {
/* 294 */       int idx = log2(normCapacity >> this.numShiftsNormalDirect);
/* 295 */       return cache(this.normalDirectCaches, idx);
/*     */     }
/* 297 */     int idx = log2(normCapacity >> this.numShiftsNormalHeap);
/* 298 */     return cache(this.normalHeapCaches, idx);
/*     */   }
/*     */   
/*     */   private static <T> MemoryRegionCache<T> cache(MemoryRegionCache<T>[] cache, int idx) {
/* 302 */     if ((cache == null) || (idx > cache.length - 1)) {
/* 303 */       return null;
/*     */     }
/* 305 */     return cache[idx];
/*     */   }
/*     */   
/*     */   private static final class SubPageMemoryRegionCache<T>
/*     */     extends PoolThreadCache.MemoryRegionCache<T>
/*     */   {
/*     */     SubPageMemoryRegionCache(int size)
/*     */     {
/* 313 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */     protected void initBuf(PoolChunk<T> chunk, long handle, PooledByteBuf<T> buf, int reqCapacity)
/*     */     {
/* 319 */       chunk.initBufWithSubpage(buf, handle, reqCapacity);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NormalMemoryRegionCache<T>
/*     */     extends PoolThreadCache.MemoryRegionCache<T>
/*     */   {
/*     */     NormalMemoryRegionCache(int size)
/*     */     {
/* 328 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */     protected void initBuf(PoolChunk<T> chunk, long handle, PooledByteBuf<T> buf, int reqCapacity)
/*     */     {
/* 334 */       chunk.initBuf(buf, handle, reqCapacity);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static abstract class MemoryRegionCache<T>
/*     */   {
/*     */     private final Entry<T>[] entries;
/*     */     
/*     */     private final int maxUnusedCached;
/*     */     private int head;
/*     */     private int tail;
/*     */     private int maxEntriesInUse;
/*     */     private int entriesInUse;
/*     */     
/*     */     MemoryRegionCache(int size)
/*     */     {
/* 351 */       this.entries = new Entry[powerOfTwo(size)];
/* 352 */       for (int i = 0; i < this.entries.length; i++) {
/* 353 */         this.entries[i] = new Entry(null);
/*     */       }
/* 355 */       this.maxUnusedCached = (size / 2);
/*     */     }
/*     */     
/*     */     private static int powerOfTwo(int res) {
/* 359 */       if (res <= 2) {
/* 360 */         return 2;
/*     */       }
/* 362 */       res--;
/* 363 */       res |= res >> 1;
/* 364 */       res |= res >> 2;
/* 365 */       res |= res >> 4;
/* 366 */       res |= res >> 8;
/* 367 */       res |= res >> 16;
/* 368 */       res++;
/* 369 */       return res;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected abstract void initBuf(PoolChunk<T> paramPoolChunk, long paramLong, PooledByteBuf<T> paramPooledByteBuf, int paramInt);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean add(PoolChunk<T> chunk, long handle)
/*     */     {
/* 382 */       Entry<T> entry = this.entries[this.tail];
/* 383 */       if (entry.chunk != null)
/*     */       {
/* 385 */         return false;
/*     */       }
/* 387 */       this.entriesInUse -= 1;
/*     */       
/* 389 */       entry.chunk = chunk;
/* 390 */       entry.handle = handle;
/* 391 */       this.tail = nextIdx(this.tail);
/* 392 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean allocate(PooledByteBuf<T> buf, int reqCapacity)
/*     */     {
/* 399 */       Entry<T> entry = this.entries[this.head];
/* 400 */       if (entry.chunk == null) {
/* 401 */         return false;
/*     */       }
/*     */       
/* 404 */       this.entriesInUse += 1;
/* 405 */       if (this.maxEntriesInUse < this.entriesInUse) {
/* 406 */         this.maxEntriesInUse = this.entriesInUse;
/*     */       }
/* 408 */       initBuf(entry.chunk, entry.handle, buf, reqCapacity);
/*     */       
/* 410 */       entry.chunk = null;
/* 411 */       this.head = nextIdx(this.head);
/* 412 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public int free()
/*     */     {
/* 419 */       int numFreed = 0;
/* 420 */       this.entriesInUse = 0;
/* 421 */       this.maxEntriesInUse = 0;
/* 422 */       for (int i = this.head;; i = nextIdx(i)) {
/* 423 */         if (freeEntry(this.entries[i])) {
/* 424 */           numFreed++;
/*     */         }
/*     */         else {
/* 427 */           return numFreed;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void trim()
/*     */     {
/* 436 */       int free = size() - this.maxEntriesInUse;
/* 437 */       this.entriesInUse = 0;
/* 438 */       this.maxEntriesInUse = 0;
/*     */       
/* 440 */       if (free <= this.maxUnusedCached) {
/* 441 */         return;
/*     */       }
/*     */       
/* 444 */       int i = this.head;
/* 445 */       for (; free > 0; free--) {
/* 446 */         if (!freeEntry(this.entries[i])) {
/*     */           break;
/*     */         }
/*     */         
/* 450 */         i = nextIdx(i);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 455 */       this.head = i;
/*     */     }
/*     */     
/*     */     private static boolean freeEntry(Entry entry)
/*     */     {
/* 460 */       PoolChunk chunk = entry.chunk;
/* 461 */       if (chunk == null) {
/* 462 */         return false;
/*     */       }
/*     */       
/* 465 */       synchronized (chunk.arena) {
/* 466 */         chunk.parent.free(chunk, entry.handle);
/*     */       }
/* 468 */       entry.chunk = null;
/* 469 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private int size()
/*     */     {
/* 476 */       return this.tail - this.head & this.entries.length - 1;
/*     */     }
/*     */     
/*     */     private int nextIdx(int index)
/*     */     {
/* 481 */       return index + 1 & this.entries.length - 1;
/*     */     }
/*     */     
/*     */     private static final class Entry<T>
/*     */     {
/*     */       PoolChunk<T> chunk;
/*     */       long handle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\PoolThreadCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */