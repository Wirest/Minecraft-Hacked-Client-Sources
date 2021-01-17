// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import java.util.concurrent.atomic.AtomicInteger;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;
import io.netty.util.internal.logging.InternalLogger;

public class PooledByteBufAllocator extends AbstractByteBufAllocator
{
    private static final InternalLogger logger;
    private static final int DEFAULT_NUM_HEAP_ARENA;
    private static final int DEFAULT_NUM_DIRECT_ARENA;
    private static final int DEFAULT_PAGE_SIZE;
    private static final int DEFAULT_MAX_ORDER;
    private static final int DEFAULT_TINY_CACHE_SIZE;
    private static final int DEFAULT_SMALL_CACHE_SIZE;
    private static final int DEFAULT_NORMAL_CACHE_SIZE;
    private static final int DEFAULT_MAX_CACHED_BUFFER_CAPACITY;
    private static final int DEFAULT_CACHE_TRIM_INTERVAL;
    private static final int MIN_PAGE_SIZE = 4096;
    private static final int MAX_CHUNK_SIZE = 1073741824;
    public static final PooledByteBufAllocator DEFAULT;
    private final PoolArena<byte[]>[] heapArenas;
    private final PoolArena<ByteBuffer>[] directArenas;
    private final int tinyCacheSize;
    private final int smallCacheSize;
    private final int normalCacheSize;
    final PoolThreadLocalCache threadCache;
    
    public PooledByteBufAllocator() {
        this(false);
    }
    
    public PooledByteBufAllocator(final boolean preferDirect) {
        this(preferDirect, PooledByteBufAllocator.DEFAULT_NUM_HEAP_ARENA, PooledByteBufAllocator.DEFAULT_NUM_DIRECT_ARENA, PooledByteBufAllocator.DEFAULT_PAGE_SIZE, PooledByteBufAllocator.DEFAULT_MAX_ORDER);
    }
    
    public PooledByteBufAllocator(final int nHeapArena, final int nDirectArena, final int pageSize, final int maxOrder) {
        this(false, nHeapArena, nDirectArena, pageSize, maxOrder);
    }
    
    public PooledByteBufAllocator(final boolean preferDirect, final int nHeapArena, final int nDirectArena, final int pageSize, final int maxOrder) {
        this(preferDirect, nHeapArena, nDirectArena, pageSize, maxOrder, PooledByteBufAllocator.DEFAULT_TINY_CACHE_SIZE, PooledByteBufAllocator.DEFAULT_SMALL_CACHE_SIZE, PooledByteBufAllocator.DEFAULT_NORMAL_CACHE_SIZE);
    }
    
    public PooledByteBufAllocator(final boolean preferDirect, final int nHeapArena, final int nDirectArena, final int pageSize, final int maxOrder, final int tinyCacheSize, final int smallCacheSize, final int normalCacheSize) {
        super(preferDirect);
        this.threadCache = new PoolThreadLocalCache();
        this.tinyCacheSize = tinyCacheSize;
        this.smallCacheSize = smallCacheSize;
        this.normalCacheSize = normalCacheSize;
        final int chunkSize = validateAndCalculateChunkSize(pageSize, maxOrder);
        if (nHeapArena < 0) {
            throw new IllegalArgumentException("nHeapArena: " + nHeapArena + " (expected: >= 0)");
        }
        if (nDirectArena < 0) {
            throw new IllegalArgumentException("nDirectArea: " + nDirectArena + " (expected: >= 0)");
        }
        final int pageShifts = validateAndCalculatePageShifts(pageSize);
        if (nHeapArena > 0) {
            this.heapArenas = newArenaArray(nHeapArena);
            for (int i = 0; i < this.heapArenas.length; ++i) {
                this.heapArenas[i] = new PoolArena.HeapArena(this, pageSize, maxOrder, pageShifts, chunkSize);
            }
        }
        else {
            this.heapArenas = null;
        }
        if (nDirectArena > 0) {
            this.directArenas = newArenaArray(nDirectArena);
            for (int i = 0; i < this.directArenas.length; ++i) {
                this.directArenas[i] = new PoolArena.DirectArena(this, pageSize, maxOrder, pageShifts, chunkSize);
            }
        }
        else {
            this.directArenas = null;
        }
    }
    
    @Deprecated
    public PooledByteBufAllocator(final boolean preferDirect, final int nHeapArena, final int nDirectArena, final int pageSize, final int maxOrder, final int tinyCacheSize, final int smallCacheSize, final int normalCacheSize, final long cacheThreadAliveCheckInterval) {
        this(preferDirect, nHeapArena, nDirectArena, pageSize, maxOrder, tinyCacheSize, smallCacheSize, normalCacheSize);
    }
    
    private static <T> PoolArena<T>[] newArenaArray(final int size) {
        return (PoolArena<T>[])new PoolArena[size];
    }
    
    private static int validateAndCalculatePageShifts(final int pageSize) {
        if (pageSize < 4096) {
            throw new IllegalArgumentException("pageSize: " + pageSize + " (expected: " + 4096 + "+)");
        }
        if ((pageSize & pageSize - 1) != 0x0) {
            throw new IllegalArgumentException("pageSize: " + pageSize + " (expected: power of 2)");
        }
        return 31 - Integer.numberOfLeadingZeros(pageSize);
    }
    
    private static int validateAndCalculateChunkSize(final int pageSize, final int maxOrder) {
        if (maxOrder > 14) {
            throw new IllegalArgumentException("maxOrder: " + maxOrder + " (expected: 0-14)");
        }
        int chunkSize = pageSize;
        for (int i = maxOrder; i > 0; --i) {
            if (chunkSize > 536870912) {
                throw new IllegalArgumentException(String.format("pageSize (%d) << maxOrder (%d) must not exceed %d", pageSize, maxOrder, 1073741824));
            }
            chunkSize <<= 1;
        }
        return chunkSize;
    }
    
    @Override
    protected ByteBuf newHeapBuffer(final int initialCapacity, final int maxCapacity) {
        final PoolThreadCache cache = this.threadCache.get();
        final PoolArena<byte[]> heapArena = cache.heapArena;
        ByteBuf buf;
        if (heapArena != null) {
            buf = heapArena.allocate(cache, initialCapacity, maxCapacity);
        }
        else {
            buf = new UnpooledHeapByteBuf(this, initialCapacity, maxCapacity);
        }
        return AbstractByteBufAllocator.toLeakAwareBuffer(buf);
    }
    
    @Override
    protected ByteBuf newDirectBuffer(final int initialCapacity, final int maxCapacity) {
        final PoolThreadCache cache = this.threadCache.get();
        final PoolArena<ByteBuffer> directArena = cache.directArena;
        ByteBuf buf;
        if (directArena != null) {
            buf = directArena.allocate(cache, initialCapacity, maxCapacity);
        }
        else if (PlatformDependent.hasUnsafe()) {
            buf = new UnpooledUnsafeDirectByteBuf(this, initialCapacity, maxCapacity);
        }
        else {
            buf = new UnpooledDirectByteBuf(this, initialCapacity, maxCapacity);
        }
        return AbstractByteBufAllocator.toLeakAwareBuffer(buf);
    }
    
    @Override
    public boolean isDirectBufferPooled() {
        return this.directArenas != null;
    }
    
    @Deprecated
    public boolean hasThreadLocalCache() {
        return this.threadCache.isSet();
    }
    
    @Deprecated
    public void freeThreadLocalCache() {
        this.threadCache.remove();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(PooledByteBufAllocator.class);
        int defaultPageSize = SystemPropertyUtil.getInt("io.netty.allocator.pageSize", 8192);
        Throwable pageSizeFallbackCause = null;
        try {
            validateAndCalculatePageShifts(defaultPageSize);
        }
        catch (Throwable t) {
            pageSizeFallbackCause = t;
            defaultPageSize = 8192;
        }
        DEFAULT_PAGE_SIZE = defaultPageSize;
        int defaultMaxOrder = SystemPropertyUtil.getInt("io.netty.allocator.maxOrder", 11);
        Throwable maxOrderFallbackCause = null;
        try {
            validateAndCalculateChunkSize(PooledByteBufAllocator.DEFAULT_PAGE_SIZE, defaultMaxOrder);
        }
        catch (Throwable t2) {
            maxOrderFallbackCause = t2;
            defaultMaxOrder = 11;
        }
        DEFAULT_MAX_ORDER = defaultMaxOrder;
        final Runtime runtime = Runtime.getRuntime();
        final int defaultChunkSize = PooledByteBufAllocator.DEFAULT_PAGE_SIZE << PooledByteBufAllocator.DEFAULT_MAX_ORDER;
        DEFAULT_NUM_HEAP_ARENA = Math.max(0, SystemPropertyUtil.getInt("io.netty.allocator.numHeapArenas", (int)Math.min(runtime.availableProcessors(), Runtime.getRuntime().maxMemory() / defaultChunkSize / 2L / 3L)));
        DEFAULT_NUM_DIRECT_ARENA = Math.max(0, SystemPropertyUtil.getInt("io.netty.allocator.numDirectArenas", (int)Math.min(runtime.availableProcessors(), PlatformDependent.maxDirectMemory() / defaultChunkSize / 2L / 3L)));
        DEFAULT_TINY_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.tinyCacheSize", 512);
        DEFAULT_SMALL_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.smallCacheSize", 256);
        DEFAULT_NORMAL_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.normalCacheSize", 64);
        DEFAULT_MAX_CACHED_BUFFER_CAPACITY = SystemPropertyUtil.getInt("io.netty.allocator.maxCachedBufferCapacity", 32768);
        DEFAULT_CACHE_TRIM_INTERVAL = SystemPropertyUtil.getInt("io.netty.allocator.cacheTrimInterval", 8192);
        if (PooledByteBufAllocator.logger.isDebugEnabled()) {
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.numHeapArenas: {}", (Object)PooledByteBufAllocator.DEFAULT_NUM_HEAP_ARENA);
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.numDirectArenas: {}", (Object)PooledByteBufAllocator.DEFAULT_NUM_DIRECT_ARENA);
            if (pageSizeFallbackCause == null) {
                PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.pageSize: {}", (Object)PooledByteBufAllocator.DEFAULT_PAGE_SIZE);
            }
            else {
                PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.pageSize: {}", (Object)PooledByteBufAllocator.DEFAULT_PAGE_SIZE, pageSizeFallbackCause);
            }
            if (maxOrderFallbackCause == null) {
                PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.maxOrder: {}", (Object)PooledByteBufAllocator.DEFAULT_MAX_ORDER);
            }
            else {
                PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.maxOrder: {}", (Object)PooledByteBufAllocator.DEFAULT_MAX_ORDER, maxOrderFallbackCause);
            }
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.chunkSize: {}", (Object)(PooledByteBufAllocator.DEFAULT_PAGE_SIZE << PooledByteBufAllocator.DEFAULT_MAX_ORDER));
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.tinyCacheSize: {}", (Object)PooledByteBufAllocator.DEFAULT_TINY_CACHE_SIZE);
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.smallCacheSize: {}", (Object)PooledByteBufAllocator.DEFAULT_SMALL_CACHE_SIZE);
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.normalCacheSize: {}", (Object)PooledByteBufAllocator.DEFAULT_NORMAL_CACHE_SIZE);
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.maxCachedBufferCapacity: {}", (Object)PooledByteBufAllocator.DEFAULT_MAX_CACHED_BUFFER_CAPACITY);
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.cacheTrimInterval: {}", (Object)PooledByteBufAllocator.DEFAULT_CACHE_TRIM_INTERVAL);
        }
        DEFAULT = new PooledByteBufAllocator(PlatformDependent.directBufferPreferred());
    }
    
    final class PoolThreadLocalCache extends FastThreadLocal<PoolThreadCache>
    {
        private final AtomicInteger index;
        
        PoolThreadLocalCache() {
            this.index = new AtomicInteger();
        }
        
        @Override
        protected PoolThreadCache initialValue() {
            final int idx = this.index.getAndIncrement();
            PoolArena<byte[]> heapArena;
            if (PooledByteBufAllocator.this.heapArenas != null) {
                heapArena = PooledByteBufAllocator.this.heapArenas[Math.abs(idx % PooledByteBufAllocator.this.heapArenas.length)];
            }
            else {
                heapArena = null;
            }
            PoolArena<ByteBuffer> directArena;
            if (PooledByteBufAllocator.this.directArenas != null) {
                directArena = PooledByteBufAllocator.this.directArenas[Math.abs(idx % PooledByteBufAllocator.this.directArenas.length)];
            }
            else {
                directArena = null;
            }
            return new PoolThreadCache(heapArena, directArena, PooledByteBufAllocator.this.tinyCacheSize, PooledByteBufAllocator.this.smallCacheSize, PooledByteBufAllocator.this.normalCacheSize, PooledByteBufAllocator.DEFAULT_MAX_CACHED_BUFFER_CAPACITY, PooledByteBufAllocator.DEFAULT_CACHE_TRIM_INTERVAL);
        }
        
        @Override
        protected void onRemoval(final PoolThreadCache value) {
            value.free();
        }
    }
}
