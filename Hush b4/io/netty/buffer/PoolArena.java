// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;
import io.netty.util.internal.StringUtil;

abstract class PoolArena<T>
{
    static final int numTinySubpagePools = 32;
    final PooledByteBufAllocator parent;
    private final int maxOrder;
    final int pageSize;
    final int pageShifts;
    final int chunkSize;
    final int subpageOverflowMask;
    final int numSmallSubpagePools;
    private final PoolSubpage<T>[] tinySubpagePools;
    private final PoolSubpage<T>[] smallSubpagePools;
    private final PoolChunkList<T> q050;
    private final PoolChunkList<T> q025;
    private final PoolChunkList<T> q000;
    private final PoolChunkList<T> qInit;
    private final PoolChunkList<T> q075;
    private final PoolChunkList<T> q100;
    
    protected PoolArena(final PooledByteBufAllocator parent, final int pageSize, final int maxOrder, final int pageShifts, final int chunkSize) {
        this.parent = parent;
        this.pageSize = pageSize;
        this.maxOrder = maxOrder;
        this.pageShifts = pageShifts;
        this.chunkSize = chunkSize;
        this.subpageOverflowMask = ~(pageSize - 1);
        this.tinySubpagePools = this.newSubpagePoolArray(32);
        for (int i = 0; i < this.tinySubpagePools.length; ++i) {
            this.tinySubpagePools[i] = this.newSubpagePoolHead(pageSize);
        }
        this.numSmallSubpagePools = pageShifts - 9;
        this.smallSubpagePools = this.newSubpagePoolArray(this.numSmallSubpagePools);
        for (int i = 0; i < this.smallSubpagePools.length; ++i) {
            this.smallSubpagePools[i] = this.newSubpagePoolHead(pageSize);
        }
        this.q100 = new PoolChunkList<T>(this, null, 100, Integer.MAX_VALUE);
        this.q075 = new PoolChunkList<T>(this, this.q100, 75, 100);
        this.q050 = new PoolChunkList<T>(this, this.q075, 50, 100);
        this.q025 = new PoolChunkList<T>(this, this.q050, 25, 75);
        this.q000 = new PoolChunkList<T>(this, this.q025, 1, 50);
        this.qInit = new PoolChunkList<T>(this, this.q000, Integer.MIN_VALUE, 25);
        this.q100.prevList = this.q075;
        this.q075.prevList = this.q050;
        this.q050.prevList = this.q025;
        this.q025.prevList = this.q000;
        this.q000.prevList = null;
        this.qInit.prevList = this.qInit;
    }
    
    private PoolSubpage<T> newSubpagePoolHead(final int pageSize) {
        final PoolSubpage<T> head = new PoolSubpage<T>(pageSize);
        head.prev = head;
        return head.next = head;
    }
    
    private PoolSubpage<T>[] newSubpagePoolArray(final int size) {
        return (PoolSubpage<T>[])new PoolSubpage[size];
    }
    
    abstract boolean isDirect();
    
    PooledByteBuf<T> allocate(final PoolThreadCache cache, final int reqCapacity, final int maxCapacity) {
        final PooledByteBuf<T> buf = this.newByteBuf(maxCapacity);
        this.allocate(cache, buf, reqCapacity);
        return buf;
    }
    
    static int tinyIdx(final int normCapacity) {
        return normCapacity >>> 4;
    }
    
    static int smallIdx(final int normCapacity) {
        int tableIdx = 0;
        for (int i = normCapacity >>> 10; i != 0; i >>>= 1, ++tableIdx) {}
        return tableIdx;
    }
    
    boolean isTinyOrSmall(final int normCapacity) {
        return (normCapacity & this.subpageOverflowMask) == 0x0;
    }
    
    static boolean isTiny(final int normCapacity) {
        return (normCapacity & 0xFFFFFE00) == 0x0;
    }
    
    private void allocate(final PoolThreadCache cache, final PooledByteBuf<T> buf, final int reqCapacity) {
        final int normCapacity = this.normalizeCapacity(reqCapacity);
        if (this.isTinyOrSmall(normCapacity)) {
            int tableIdx;
            PoolSubpage<T>[] table;
            if (isTiny(normCapacity)) {
                if (cache.allocateTiny(this, buf, reqCapacity, normCapacity)) {
                    return;
                }
                tableIdx = tinyIdx(normCapacity);
                table = this.tinySubpagePools;
            }
            else {
                if (cache.allocateSmall(this, buf, reqCapacity, normCapacity)) {
                    return;
                }
                tableIdx = smallIdx(normCapacity);
                table = this.smallSubpagePools;
            }
            synchronized (this) {
                final PoolSubpage<T> head = table[tableIdx];
                final PoolSubpage<T> s = head.next;
                if (s != head) {
                    assert s.doNotDestroy && s.elemSize == normCapacity;
                    final long handle = s.allocate();
                    assert handle >= 0L;
                    s.chunk.initBufWithSubpage(buf, handle, reqCapacity);
                    return;
                }
            }
        }
        else {
            if (normCapacity > this.chunkSize) {
                this.allocateHuge(buf, reqCapacity);
                return;
            }
            if (cache.allocateNormal(this, buf, reqCapacity, normCapacity)) {
                return;
            }
        }
        this.allocateNormal(buf, reqCapacity, normCapacity);
    }
    
    private synchronized void allocateNormal(final PooledByteBuf<T> buf, final int reqCapacity, final int normCapacity) {
        if (this.q050.allocate(buf, reqCapacity, normCapacity) || this.q025.allocate(buf, reqCapacity, normCapacity) || this.q000.allocate(buf, reqCapacity, normCapacity) || this.qInit.allocate(buf, reqCapacity, normCapacity) || this.q075.allocate(buf, reqCapacity, normCapacity) || this.q100.allocate(buf, reqCapacity, normCapacity)) {
            return;
        }
        final PoolChunk<T> c = this.newChunk(this.pageSize, this.maxOrder, this.pageShifts, this.chunkSize);
        final long handle = c.allocate(normCapacity);
        assert handle > 0L;
        c.initBuf(buf, handle, reqCapacity);
        this.qInit.add(c);
    }
    
    private void allocateHuge(final PooledByteBuf<T> buf, final int reqCapacity) {
        buf.initUnpooled(this.newUnpooledChunk(reqCapacity), reqCapacity);
    }
    
    void free(final PoolChunk<T> chunk, final long handle, final int normCapacity) {
        if (chunk.unpooled) {
            this.destroyChunk(chunk);
        }
        else {
            final PoolThreadCache cache = this.parent.threadCache.get();
            if (cache.add(this, chunk, handle, normCapacity)) {
                return;
            }
            synchronized (this) {
                chunk.parent.free(chunk, handle);
            }
        }
    }
    
    PoolSubpage<T> findSubpagePoolHead(int elemSize) {
        int tableIdx;
        PoolSubpage<T>[] table;
        if (isTiny(elemSize)) {
            tableIdx = elemSize >>> 4;
            table = this.tinySubpagePools;
        }
        else {
            for (tableIdx = 0, elemSize >>>= 10; elemSize != 0; elemSize >>>= 1, ++tableIdx) {}
            table = this.smallSubpagePools;
        }
        return table[tableIdx];
    }
    
    int normalizeCapacity(final int reqCapacity) {
        if (reqCapacity < 0) {
            throw new IllegalArgumentException("capacity: " + reqCapacity + " (expected: 0+)");
        }
        if (reqCapacity >= this.chunkSize) {
            return reqCapacity;
        }
        if (!isTiny(reqCapacity)) {
            int normalizedCapacity = reqCapacity;
            normalizedCapacity = (--normalizedCapacity | normalizedCapacity >>> 1);
            normalizedCapacity |= normalizedCapacity >>> 2;
            normalizedCapacity |= normalizedCapacity >>> 4;
            normalizedCapacity |= normalizedCapacity >>> 8;
            normalizedCapacity |= normalizedCapacity >>> 16;
            if (++normalizedCapacity < 0) {
                normalizedCapacity >>>= 1;
            }
            return normalizedCapacity;
        }
        if ((reqCapacity & 0xF) == 0x0) {
            return reqCapacity;
        }
        return (reqCapacity & 0xFFFFFFF0) + 16;
    }
    
    void reallocate(final PooledByteBuf<T> buf, final int newCapacity, final boolean freeOldMemory) {
        if (newCapacity < 0 || newCapacity > buf.maxCapacity()) {
            throw new IllegalArgumentException("newCapacity: " + newCapacity);
        }
        final int oldCapacity = buf.length;
        if (oldCapacity == newCapacity) {
            return;
        }
        final PoolChunk<T> oldChunk = buf.chunk;
        final long oldHandle = buf.handle;
        final T oldMemory = buf.memory;
        final int oldOffset = buf.offset;
        final int oldMaxLength = buf.maxLength;
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        this.allocate(this.parent.threadCache.get(), buf, newCapacity);
        if (newCapacity > oldCapacity) {
            this.memoryCopy(oldMemory, oldOffset, buf.memory, buf.offset, oldCapacity);
        }
        else if (newCapacity < oldCapacity) {
            if (readerIndex < newCapacity) {
                if (writerIndex > newCapacity) {
                    writerIndex = newCapacity;
                }
                this.memoryCopy(oldMemory, oldOffset + readerIndex, buf.memory, buf.offset + readerIndex, writerIndex - readerIndex);
            }
            else {
                writerIndex = newCapacity;
                readerIndex = newCapacity;
            }
        }
        buf.setIndex(readerIndex, writerIndex);
        if (freeOldMemory) {
            this.free(oldChunk, oldHandle, oldMaxLength);
        }
    }
    
    protected abstract PoolChunk<T> newChunk(final int p0, final int p1, final int p2, final int p3);
    
    protected abstract PoolChunk<T> newUnpooledChunk(final int p0);
    
    protected abstract PooledByteBuf<T> newByteBuf(final int p0);
    
    protected abstract void memoryCopy(final T p0, final int p1, final T p2, final int p3, final int p4);
    
    protected abstract void destroyChunk(final PoolChunk<T> p0);
    
    @Override
    public synchronized String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("Chunk(s) at 0~25%:");
        buf.append(StringUtil.NEWLINE);
        buf.append(this.qInit);
        buf.append(StringUtil.NEWLINE);
        buf.append("Chunk(s) at 0~50%:");
        buf.append(StringUtil.NEWLINE);
        buf.append(this.q000);
        buf.append(StringUtil.NEWLINE);
        buf.append("Chunk(s) at 25~75%:");
        buf.append(StringUtil.NEWLINE);
        buf.append(this.q025);
        buf.append(StringUtil.NEWLINE);
        buf.append("Chunk(s) at 50~100%:");
        buf.append(StringUtil.NEWLINE);
        buf.append(this.q050);
        buf.append(StringUtil.NEWLINE);
        buf.append("Chunk(s) at 75~100%:");
        buf.append(StringUtil.NEWLINE);
        buf.append(this.q075);
        buf.append(StringUtil.NEWLINE);
        buf.append("Chunk(s) at 100%:");
        buf.append(StringUtil.NEWLINE);
        buf.append(this.q100);
        buf.append(StringUtil.NEWLINE);
        buf.append("tiny subpages:");
        for (int i = 1; i < this.tinySubpagePools.length; ++i) {
            final PoolSubpage<T> head = this.tinySubpagePools[i];
            if (head.next != head) {
                buf.append(StringUtil.NEWLINE);
                buf.append(i);
                buf.append(": ");
                PoolSubpage<T> s = head.next;
                do {
                    buf.append(s);
                    s = s.next;
                } while (s != head);
            }
        }
        buf.append(StringUtil.NEWLINE);
        buf.append("small subpages:");
        for (int i = 1; i < this.smallSubpagePools.length; ++i) {
            final PoolSubpage<T> head = this.smallSubpagePools[i];
            if (head.next != head) {
                buf.append(StringUtil.NEWLINE);
                buf.append(i);
                buf.append(": ");
                PoolSubpage<T> s = head.next;
                do {
                    buf.append(s);
                    s = s.next;
                } while (s != head);
            }
        }
        buf.append(StringUtil.NEWLINE);
        return buf.toString();
    }
    
    static final class HeapArena extends PoolArena<byte[]>
    {
        HeapArena(final PooledByteBufAllocator parent, final int pageSize, final int maxOrder, final int pageShifts, final int chunkSize) {
            super(parent, pageSize, maxOrder, pageShifts, chunkSize);
        }
        
        @Override
        boolean isDirect() {
            return false;
        }
        
        @Override
        protected PoolChunk<byte[]> newChunk(final int pageSize, final int maxOrder, final int pageShifts, final int chunkSize) {
            return new PoolChunk<byte[]>(this, new byte[chunkSize], pageSize, maxOrder, pageShifts, chunkSize);
        }
        
        @Override
        protected PoolChunk<byte[]> newUnpooledChunk(final int capacity) {
            return new PoolChunk<byte[]>(this, new byte[capacity], capacity);
        }
        
        @Override
        protected void destroyChunk(final PoolChunk<byte[]> chunk) {
        }
        
        @Override
        protected PooledByteBuf<byte[]> newByteBuf(final int maxCapacity) {
            return PooledHeapByteBuf.newInstance(maxCapacity);
        }
        
        @Override
        protected void memoryCopy(final byte[] src, final int srcOffset, final byte[] dst, final int dstOffset, final int length) {
            if (length == 0) {
                return;
            }
            System.arraycopy(src, srcOffset, dst, dstOffset, length);
        }
    }
    
    static final class DirectArena extends PoolArena<ByteBuffer>
    {
        private static final boolean HAS_UNSAFE;
        
        DirectArena(final PooledByteBufAllocator parent, final int pageSize, final int maxOrder, final int pageShifts, final int chunkSize) {
            super(parent, pageSize, maxOrder, pageShifts, chunkSize);
        }
        
        @Override
        boolean isDirect() {
            return true;
        }
        
        @Override
        protected PoolChunk<ByteBuffer> newChunk(final int pageSize, final int maxOrder, final int pageShifts, final int chunkSize) {
            return new PoolChunk<ByteBuffer>(this, ByteBuffer.allocateDirect(chunkSize), pageSize, maxOrder, pageShifts, chunkSize);
        }
        
        @Override
        protected PoolChunk<ByteBuffer> newUnpooledChunk(final int capacity) {
            return new PoolChunk<ByteBuffer>(this, ByteBuffer.allocateDirect(capacity), capacity);
        }
        
        @Override
        protected void destroyChunk(final PoolChunk<ByteBuffer> chunk) {
            PlatformDependent.freeDirectBuffer(chunk.memory);
        }
        
        @Override
        protected PooledByteBuf<ByteBuffer> newByteBuf(final int maxCapacity) {
            if (DirectArena.HAS_UNSAFE) {
                return PooledUnsafeDirectByteBuf.newInstance(maxCapacity);
            }
            return PooledDirectByteBuf.newInstance(maxCapacity);
        }
        
        @Override
        protected void memoryCopy(ByteBuffer src, final int srcOffset, ByteBuffer dst, final int dstOffset, final int length) {
            if (length == 0) {
                return;
            }
            if (DirectArena.HAS_UNSAFE) {
                PlatformDependent.copyMemory(PlatformDependent.directBufferAddress(src) + srcOffset, PlatformDependent.directBufferAddress(dst) + dstOffset, length);
            }
            else {
                src = src.duplicate();
                dst = dst.duplicate();
                src.position(srcOffset).limit(srcOffset + length);
                dst.position(dstOffset);
                dst.put(src);
            }
        }
        
        static {
            HAS_UNSAFE = PlatformDependent.hasUnsafe();
        }
    }
}
