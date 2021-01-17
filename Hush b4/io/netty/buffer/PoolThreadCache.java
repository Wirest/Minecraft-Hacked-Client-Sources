// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.ThreadDeathWatcher;
import java.nio.ByteBuffer;
import io.netty.util.internal.logging.InternalLogger;

final class PoolThreadCache
{
    private static final InternalLogger logger;
    final PoolArena<byte[]> heapArena;
    final PoolArena<ByteBuffer> directArena;
    private final MemoryRegionCache<byte[]>[] tinySubPageHeapCaches;
    private final MemoryRegionCache<byte[]>[] smallSubPageHeapCaches;
    private final MemoryRegionCache<ByteBuffer>[] tinySubPageDirectCaches;
    private final MemoryRegionCache<ByteBuffer>[] smallSubPageDirectCaches;
    private final MemoryRegionCache<byte[]>[] normalHeapCaches;
    private final MemoryRegionCache<ByteBuffer>[] normalDirectCaches;
    private final int numShiftsNormalDirect;
    private final int numShiftsNormalHeap;
    private final int freeSweepAllocationThreshold;
    private int allocations;
    private final Thread thread;
    private final Runnable freeTask;
    
    PoolThreadCache(final PoolArena<byte[]> heapArena, final PoolArena<ByteBuffer> directArena, final int tinyCacheSize, final int smallCacheSize, final int normalCacheSize, final int maxCachedBufferCapacity, final int freeSweepAllocationThreshold) {
        this.thread = Thread.currentThread();
        this.freeTask = new Runnable() {
            @Override
            public void run() {
                PoolThreadCache.this.free0();
            }
        };
        if (maxCachedBufferCapacity < 0) {
            throw new IllegalArgumentException("maxCachedBufferCapacity: " + maxCachedBufferCapacity + " (expected: >= 0)");
        }
        if (freeSweepAllocationThreshold < 1) {
            throw new IllegalArgumentException("freeSweepAllocationThreshold: " + maxCachedBufferCapacity + " (expected: > 0)");
        }
        this.freeSweepAllocationThreshold = freeSweepAllocationThreshold;
        this.heapArena = heapArena;
        if ((this.directArena = directArena) != null) {
            this.tinySubPageDirectCaches = (MemoryRegionCache<ByteBuffer>[])createSubPageCaches(tinyCacheSize, 32);
            this.smallSubPageDirectCaches = (MemoryRegionCache<ByteBuffer>[])createSubPageCaches(smallCacheSize, directArena.numSmallSubpagePools);
            this.numShiftsNormalDirect = log2(directArena.pageSize);
            this.normalDirectCaches = createNormalCaches(normalCacheSize, maxCachedBufferCapacity, directArena);
        }
        else {
            this.tinySubPageDirectCaches = null;
            this.smallSubPageDirectCaches = null;
            this.normalDirectCaches = null;
            this.numShiftsNormalDirect = -1;
        }
        if (heapArena != null) {
            this.tinySubPageHeapCaches = (MemoryRegionCache<byte[]>[])createSubPageCaches(tinyCacheSize, 32);
            this.smallSubPageHeapCaches = (MemoryRegionCache<byte[]>[])createSubPageCaches(smallCacheSize, heapArena.numSmallSubpagePools);
            this.numShiftsNormalHeap = log2(heapArena.pageSize);
            this.normalHeapCaches = createNormalCaches(normalCacheSize, maxCachedBufferCapacity, heapArena);
        }
        else {
            this.tinySubPageHeapCaches = null;
            this.smallSubPageHeapCaches = null;
            this.normalHeapCaches = null;
            this.numShiftsNormalHeap = -1;
        }
        ThreadDeathWatcher.watch(this.thread, this.freeTask);
    }
    
    private static <T> SubPageMemoryRegionCache<T>[] createSubPageCaches(final int cacheSize, final int numCaches) {
        if (cacheSize > 0) {
            final SubPageMemoryRegionCache<T>[] cache = (SubPageMemoryRegionCache<T>[])new SubPageMemoryRegionCache[numCaches];
            for (int i = 0; i < cache.length; ++i) {
                cache[i] = new SubPageMemoryRegionCache<T>(cacheSize);
            }
            return cache;
        }
        return null;
    }
    
    private static <T> NormalMemoryRegionCache<T>[] createNormalCaches(final int cacheSize, final int maxCachedBufferCapacity, final PoolArena<T> area) {
        if (cacheSize > 0) {
            final int max = Math.min(area.chunkSize, maxCachedBufferCapacity);
            final int arraySize = Math.max(1, max / area.pageSize);
            final NormalMemoryRegionCache<T>[] cache = (NormalMemoryRegionCache<T>[])new NormalMemoryRegionCache[arraySize];
            for (int i = 0; i < cache.length; ++i) {
                cache[i] = new NormalMemoryRegionCache<T>(cacheSize);
            }
            return cache;
        }
        return null;
    }
    
    private static int log2(int val) {
        int res;
        for (res = 0; val > 1; val >>= 1, ++res) {}
        return res;
    }
    
    boolean allocateTiny(final PoolArena<?> area, final PooledByteBuf<?> buf, final int reqCapacity, final int normCapacity) {
        return this.allocate(this.cacheForTiny(area, normCapacity), buf, reqCapacity);
    }
    
    boolean allocateSmall(final PoolArena<?> area, final PooledByteBuf<?> buf, final int reqCapacity, final int normCapacity) {
        return this.allocate(this.cacheForSmall(area, normCapacity), buf, reqCapacity);
    }
    
    boolean allocateNormal(final PoolArena<?> area, final PooledByteBuf<?> buf, final int reqCapacity, final int normCapacity) {
        return this.allocate(this.cacheForNormal(area, normCapacity), buf, reqCapacity);
    }
    
    private boolean allocate(final MemoryRegionCache<?> cache, final PooledByteBuf buf, final int reqCapacity) {
        if (cache == null) {
            return false;
        }
        final boolean allocated = cache.allocate(buf, reqCapacity);
        if (++this.allocations >= this.freeSweepAllocationThreshold) {
            this.allocations = 0;
            this.trim();
        }
        return allocated;
    }
    
    boolean add(final PoolArena<?> area, final PoolChunk chunk, final long handle, final int normCapacity) {
        MemoryRegionCache<?> cache;
        if (area.isTinyOrSmall(normCapacity)) {
            if (PoolArena.isTiny(normCapacity)) {
                cache = this.cacheForTiny(area, normCapacity);
            }
            else {
                cache = this.cacheForSmall(area, normCapacity);
            }
        }
        else {
            cache = this.cacheForNormal(area, normCapacity);
        }
        return cache != null && cache.add(chunk, handle);
    }
    
    void free() {
        ThreadDeathWatcher.unwatch(this.thread, this.freeTask);
        this.free0();
    }
    
    private void free0() {
        final int numFreed = free(this.tinySubPageDirectCaches) + free(this.smallSubPageDirectCaches) + free(this.normalDirectCaches) + free(this.tinySubPageHeapCaches) + free(this.smallSubPageHeapCaches) + free(this.normalHeapCaches);
        if (numFreed > 0 && PoolThreadCache.logger.isDebugEnabled()) {
            PoolThreadCache.logger.debug("Freed {} thread-local buffer(s) from thread: {}", (Object)numFreed, this.thread.getName());
        }
    }
    
    private static int free(final MemoryRegionCache<?>[] caches) {
        if (caches == null) {
            return 0;
        }
        int numFreed = 0;
        for (final MemoryRegionCache<?> c : caches) {
            numFreed += free(c);
        }
        return numFreed;
    }
    
    private static int free(final MemoryRegionCache<?> cache) {
        if (cache == null) {
            return 0;
        }
        return cache.free();
    }
    
    void trim() {
        trim(this.tinySubPageDirectCaches);
        trim(this.smallSubPageDirectCaches);
        trim(this.normalDirectCaches);
        trim(this.tinySubPageHeapCaches);
        trim(this.smallSubPageHeapCaches);
        trim(this.normalHeapCaches);
    }
    
    private static void trim(final MemoryRegionCache<?>[] caches) {
        if (caches == null) {
            return;
        }
        for (final MemoryRegionCache<?> c : caches) {
            trim(c);
        }
    }
    
    private static void trim(final MemoryRegionCache<?> cache) {
        if (cache == null) {
            return;
        }
        ((MemoryRegionCache<Object>)cache).trim();
    }
    
    private MemoryRegionCache<?> cacheForTiny(final PoolArena<?> area, final int normCapacity) {
        final int idx = PoolArena.tinyIdx(normCapacity);
        if (area.isDirect()) {
            return cache((MemoryRegionCache<?>[])this.tinySubPageDirectCaches, idx);
        }
        return cache((MemoryRegionCache<?>[])this.tinySubPageHeapCaches, idx);
    }
    
    private MemoryRegionCache<?> cacheForSmall(final PoolArena<?> area, final int normCapacity) {
        final int idx = PoolArena.smallIdx(normCapacity);
        if (area.isDirect()) {
            return cache((MemoryRegionCache<?>[])this.smallSubPageDirectCaches, idx);
        }
        return cache((MemoryRegionCache<?>[])this.smallSubPageHeapCaches, idx);
    }
    
    private MemoryRegionCache<?> cacheForNormal(final PoolArena<?> area, final int normCapacity) {
        if (area.isDirect()) {
            final int idx = log2(normCapacity >> this.numShiftsNormalDirect);
            return cache((MemoryRegionCache<?>[])this.normalDirectCaches, idx);
        }
        final int idx = log2(normCapacity >> this.numShiftsNormalHeap);
        return cache((MemoryRegionCache<?>[])this.normalHeapCaches, idx);
    }
    
    private static <T> MemoryRegionCache<T> cache(final MemoryRegionCache<T>[] cache, final int idx) {
        if (cache == null || idx > cache.length - 1) {
            return null;
        }
        return cache[idx];
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(PoolThreadCache.class);
    }
    
    private static final class SubPageMemoryRegionCache<T> extends MemoryRegionCache<T>
    {
        SubPageMemoryRegionCache(final int size) {
            super(size);
        }
        
        @Override
        protected void initBuf(final PoolChunk<T> chunk, final long handle, final PooledByteBuf<T> buf, final int reqCapacity) {
            chunk.initBufWithSubpage(buf, handle, reqCapacity);
        }
    }
    
    private static final class NormalMemoryRegionCache<T> extends MemoryRegionCache<T>
    {
        NormalMemoryRegionCache(final int size) {
            super(size);
        }
        
        @Override
        protected void initBuf(final PoolChunk<T> chunk, final long handle, final PooledByteBuf<T> buf, final int reqCapacity) {
            chunk.initBuf(buf, handle, reqCapacity);
        }
    }
    
    private abstract static class MemoryRegionCache<T>
    {
        private final Entry<T>[] entries;
        private final int maxUnusedCached;
        private int head;
        private int tail;
        private int maxEntriesInUse;
        private int entriesInUse;
        
        MemoryRegionCache(final int size) {
            this.entries = (Entry<T>[])new Entry[powerOfTwo(size)];
            for (int i = 0; i < this.entries.length; ++i) {
                this.entries[i] = new Entry<T>();
            }
            this.maxUnusedCached = size / 2;
        }
        
        private static int powerOfTwo(int res) {
            if (res <= 2) {
                return 2;
            }
            res = (--res | res >> 1);
            res |= res >> 2;
            res |= res >> 4;
            res |= res >> 8;
            res |= res >> 16;
            return ++res;
        }
        
        protected abstract void initBuf(final PoolChunk<T> p0, final long p1, final PooledByteBuf<T> p2, final int p3);
        
        public boolean add(final PoolChunk<T> chunk, final long handle) {
            final Entry<T> entry = this.entries[this.tail];
            if (entry.chunk != null) {
                return false;
            }
            --this.entriesInUse;
            entry.chunk = chunk;
            entry.handle = handle;
            this.tail = this.nextIdx(this.tail);
            return true;
        }
        
        public boolean allocate(final PooledByteBuf<T> buf, final int reqCapacity) {
            final Entry<T> entry = this.entries[this.head];
            if (entry.chunk == null) {
                return false;
            }
            ++this.entriesInUse;
            if (this.maxEntriesInUse < this.entriesInUse) {
                this.maxEntriesInUse = this.entriesInUse;
            }
            this.initBuf(entry.chunk, entry.handle, buf, reqCapacity);
            entry.chunk = null;
            this.head = this.nextIdx(this.head);
            return true;
        }
        
        public int free() {
            int numFreed = 0;
            this.entriesInUse = 0;
            this.maxEntriesInUse = 0;
            for (int i = this.head; freeEntry(this.entries[i]); i = this.nextIdx(i)) {
                ++numFreed;
            }
            return numFreed;
        }
        
        private void trim() {
            int free = this.size() - this.maxEntriesInUse;
            this.entriesInUse = 0;
            this.maxEntriesInUse = 0;
            if (free <= this.maxUnusedCached) {
                return;
            }
            int i = this.head;
            while (free > 0) {
                if (!freeEntry(this.entries[i])) {
                    return;
                }
                i = this.nextIdx(i);
                --free;
            }
        }
        
        private static boolean freeEntry(final Entry entry) {
            final PoolChunk chunk = entry.chunk;
            if (chunk == null) {
                return false;
            }
            synchronized (chunk.arena) {
                chunk.parent.free(chunk, entry.handle);
            }
            entry.chunk = null;
            return true;
        }
        
        private int size() {
            return this.tail - this.head & this.entries.length - 1;
        }
        
        private int nextIdx(final int index) {
            return index + 1 & this.entries.length - 1;
        }
        
        private static final class Entry<T>
        {
            PoolChunk<T> chunk;
            long handle;
        }
    }
}
