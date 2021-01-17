// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

final class PoolChunk<T>
{
    final PoolArena<T> arena;
    final T memory;
    final boolean unpooled;
    private final byte[] memoryMap;
    private final byte[] depthMap;
    private final PoolSubpage<T>[] subpages;
    private final int subpageOverflowMask;
    private final int pageSize;
    private final int pageShifts;
    private final int maxOrder;
    private final int chunkSize;
    private final int log2ChunkSize;
    private final int maxSubpageAllocs;
    private final byte unusable;
    private int freeBytes;
    PoolChunkList<T> parent;
    PoolChunk<T> prev;
    PoolChunk<T> next;
    
    PoolChunk(final PoolArena<T> arena, final T memory, final int pageSize, final int maxOrder, final int pageShifts, final int chunkSize) {
        this.unpooled = false;
        this.arena = arena;
        this.memory = memory;
        this.pageSize = pageSize;
        this.pageShifts = pageShifts;
        this.maxOrder = maxOrder;
        this.chunkSize = chunkSize;
        this.unusable = (byte)(maxOrder + 1);
        this.log2ChunkSize = log2(chunkSize);
        this.subpageOverflowMask = ~(pageSize - 1);
        this.freeBytes = chunkSize;
        assert maxOrder < 30 : "maxOrder should be < 30, but is: " + maxOrder;
        this.maxSubpageAllocs = 1 << maxOrder;
        this.memoryMap = new byte[this.maxSubpageAllocs << 1];
        this.depthMap = new byte[this.memoryMap.length];
        int memoryMapIndex = 1;
        for (int d = 0; d <= maxOrder; ++d) {
            for (int depth = 1 << d, p = 0; p < depth; ++p) {
                this.memoryMap[memoryMapIndex] = (byte)d;
                this.depthMap[memoryMapIndex] = (byte)d;
                ++memoryMapIndex;
            }
        }
        this.subpages = this.newSubpageArray(this.maxSubpageAllocs);
    }
    
    PoolChunk(final PoolArena<T> arena, final T memory, final int size) {
        this.unpooled = true;
        this.arena = arena;
        this.memory = memory;
        this.memoryMap = null;
        this.depthMap = null;
        this.subpages = null;
        this.subpageOverflowMask = 0;
        this.pageSize = 0;
        this.pageShifts = 0;
        this.maxOrder = 0;
        this.unusable = (byte)(this.maxOrder + 1);
        this.chunkSize = size;
        this.log2ChunkSize = log2(this.chunkSize);
        this.maxSubpageAllocs = 0;
    }
    
    private PoolSubpage<T>[] newSubpageArray(final int size) {
        return (PoolSubpage<T>[])new PoolSubpage[size];
    }
    
    int usage() {
        final int freeBytes = this.freeBytes;
        if (freeBytes == 0) {
            return 100;
        }
        final int freePercentage = (int)(freeBytes * 100L / this.chunkSize);
        if (freePercentage == 0) {
            return 99;
        }
        return 100 - freePercentage;
    }
    
    long allocate(final int normCapacity) {
        if ((normCapacity & this.subpageOverflowMask) != 0x0) {
            return this.allocateRun(normCapacity);
        }
        return this.allocateSubpage(normCapacity);
    }
    
    private void updateParentsAlloc(int id) {
        while (id > 1) {
            final int parentId = id >>> 1;
            final byte val1 = this.value(id);
            final byte val2 = this.value(id ^ 0x1);
            final byte val3 = (val1 < val2) ? val1 : val2;
            this.setValue(parentId, val3);
            id = parentId;
        }
    }
    
    private void updateParentsFree(int id) {
        int logChild = this.depth(id) + 1;
        while (id > 1) {
            final int parentId = id >>> 1;
            final byte val1 = this.value(id);
            final byte val2 = this.value(id ^ 0x1);
            --logChild;
            if (val1 == logChild && val2 == logChild) {
                this.setValue(parentId, (byte)(logChild - 1));
            }
            else {
                final byte val3 = (val1 < val2) ? val1 : val2;
                this.setValue(parentId, val3);
            }
            id = parentId;
        }
    }
    
    private int allocateNode(final int d) {
        int id = 1;
        final int initial = -(1 << d);
        byte val = this.value(id);
        if (val > d) {
            return -1;
        }
        while (val < d || (id & initial) == 0x0) {
            id <<= 1;
            val = this.value(id);
            if (val > d) {
                id ^= 0x1;
                val = this.value(id);
            }
        }
        final byte value = this.value(id);
        assert value == d && (id & initial) == 1 << d : String.format("val = %d, id & initial = %d, d = %d", value, id & initial, d);
        this.setValue(id, this.unusable);
        this.updateParentsAlloc(id);
        return id;
    }
    
    private long allocateRun(final int normCapacity) {
        final int d = this.maxOrder - (log2(normCapacity) - this.pageShifts);
        final int id = this.allocateNode(d);
        if (id < 0) {
            return id;
        }
        this.freeBytes -= this.runLength(id);
        return id;
    }
    
    private long allocateSubpage(final int normCapacity) {
        final int d = this.maxOrder;
        final int id = this.allocateNode(d);
        if (id < 0) {
            return id;
        }
        final PoolSubpage<T>[] subpages = this.subpages;
        final int pageSize = this.pageSize;
        this.freeBytes -= pageSize;
        final int subpageIdx = this.subpageIdx(id);
        PoolSubpage<T> subpage = subpages[subpageIdx];
        if (subpage == null) {
            subpage = new PoolSubpage<T>(this, id, this.runOffset(id), pageSize, normCapacity);
            subpages[subpageIdx] = subpage;
        }
        else {
            subpage.init(normCapacity);
        }
        return subpage.allocate();
    }
    
    void free(final long handle) {
        final int memoryMapIdx = (int)handle;
        final int bitmapIdx = (int)(handle >>> 32);
        if (bitmapIdx != 0) {
            final PoolSubpage<T> subpage = this.subpages[this.subpageIdx(memoryMapIdx)];
            assert subpage != null && subpage.doNotDestroy;
            if (subpage.free(bitmapIdx & 0x3FFFFFFF)) {
                return;
            }
        }
        this.freeBytes += this.runLength(memoryMapIdx);
        this.setValue(memoryMapIdx, this.depth(memoryMapIdx));
        this.updateParentsFree(memoryMapIdx);
    }
    
    void initBuf(final PooledByteBuf<T> buf, final long handle, final int reqCapacity) {
        final int memoryMapIdx = (int)handle;
        final int bitmapIdx = (int)(handle >>> 32);
        if (bitmapIdx == 0) {
            final byte val = this.value(memoryMapIdx);
            assert val == this.unusable : String.valueOf(val);
            buf.init(this, handle, this.runOffset(memoryMapIdx), reqCapacity, this.runLength(memoryMapIdx));
        }
        else {
            this.initBufWithSubpage(buf, handle, bitmapIdx, reqCapacity);
        }
    }
    
    void initBufWithSubpage(final PooledByteBuf<T> buf, final long handle, final int reqCapacity) {
        this.initBufWithSubpage(buf, handle, (int)(handle >>> 32), reqCapacity);
    }
    
    private void initBufWithSubpage(final PooledByteBuf<T> buf, final long handle, final int bitmapIdx, final int reqCapacity) {
        assert bitmapIdx != 0;
        final int memoryMapIdx = (int)handle;
        final PoolSubpage<T> subpage = this.subpages[this.subpageIdx(memoryMapIdx)];
        assert subpage.doNotDestroy;
        assert reqCapacity <= subpage.elemSize;
        buf.init(this, handle, this.runOffset(memoryMapIdx) + (bitmapIdx & 0x3FFFFFFF) * subpage.elemSize, reqCapacity, subpage.elemSize);
    }
    
    private byte value(final int id) {
        return this.memoryMap[id];
    }
    
    private void setValue(final int id, final byte val) {
        this.memoryMap[id] = val;
    }
    
    private byte depth(final int id) {
        return this.depthMap[id];
    }
    
    private static int log2(final int val) {
        return 31 - Integer.numberOfLeadingZeros(val);
    }
    
    private int runLength(final int id) {
        return 1 << this.log2ChunkSize - this.depth(id);
    }
    
    private int runOffset(final int id) {
        final int shift = id ^ 1 << this.depth(id);
        return shift * this.runLength(id);
    }
    
    private int subpageIdx(final int memoryMapIdx) {
        return memoryMapIdx ^ this.maxSubpageAllocs;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("Chunk(");
        buf.append(Integer.toHexString(System.identityHashCode(this)));
        buf.append(": ");
        buf.append(this.usage());
        buf.append("%, ");
        buf.append(this.chunkSize - this.freeBytes);
        buf.append('/');
        buf.append(this.chunkSize);
        buf.append(')');
        return buf.toString();
    }
}
