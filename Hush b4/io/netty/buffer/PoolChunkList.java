// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.internal.StringUtil;

final class PoolChunkList<T>
{
    private final PoolArena<T> arena;
    private final PoolChunkList<T> nextList;
    PoolChunkList<T> prevList;
    private final int minUsage;
    private final int maxUsage;
    private PoolChunk<T> head;
    
    PoolChunkList(final PoolArena<T> arena, final PoolChunkList<T> nextList, final int minUsage, final int maxUsage) {
        this.arena = arena;
        this.nextList = nextList;
        this.minUsage = minUsage;
        this.maxUsage = maxUsage;
    }
    
    boolean allocate(final PooledByteBuf<T> buf, final int reqCapacity, final int normCapacity) {
        if (this.head == null) {
            return false;
        }
        PoolChunk<T> cur = this.head;
        while (true) {
            final long handle = cur.allocate(normCapacity);
            if (handle >= 0L) {
                cur.initBuf(buf, handle, reqCapacity);
                if (cur.usage() >= this.maxUsage) {
                    this.remove(cur);
                    this.nextList.add(cur);
                }
                return true;
            }
            cur = cur.next;
            if (cur == null) {
                return false;
            }
        }
    }
    
    void free(final PoolChunk<T> chunk, final long handle) {
        chunk.free(handle);
        if (chunk.usage() < this.minUsage) {
            this.remove(chunk);
            if (this.prevList == null) {
                assert chunk.usage() == 0;
                this.arena.destroyChunk(chunk);
            }
            else {
                this.prevList.add(chunk);
            }
        }
    }
    
    void add(final PoolChunk<T> chunk) {
        if (chunk.usage() >= this.maxUsage) {
            this.nextList.add(chunk);
            return;
        }
        chunk.parent = this;
        if (this.head == null) {
            this.head = chunk;
            chunk.prev = null;
            chunk.next = null;
        }
        else {
            chunk.prev = null;
            chunk.next = this.head;
            this.head.prev = chunk;
            this.head = chunk;
        }
    }
    
    private void remove(final PoolChunk<T> cur) {
        if (cur == this.head) {
            this.head = cur.next;
            if (this.head != null) {
                this.head.prev = null;
            }
        }
        else {
            final PoolChunk<T> next = cur.next;
            if ((cur.prev.next = next) != null) {
                next.prev = cur.prev;
            }
        }
    }
    
    @Override
    public String toString() {
        if (this.head == null) {
            return "none";
        }
        final StringBuilder buf = new StringBuilder();
        PoolChunk<T> cur = this.head;
        while (true) {
            buf.append(cur);
            cur = cur.next;
            if (cur == null) {
                break;
            }
            buf.append(StringUtil.NEWLINE);
        }
        return buf.toString();
    }
}
