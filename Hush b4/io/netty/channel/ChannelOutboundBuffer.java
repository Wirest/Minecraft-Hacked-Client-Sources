// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.buffer.Unpooled;
import io.netty.util.Recycler;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.channels.ClosedChannelException;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.nio.ByteBuffer;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.logging.InternalLogger;

public final class ChannelOutboundBuffer
{
    private static final InternalLogger logger;
    private static final FastThreadLocal<ByteBuffer[]> NIO_BUFFERS;
    private final Channel channel;
    private Entry flushedEntry;
    private Entry unflushedEntry;
    private Entry tailEntry;
    private int flushed;
    private int nioBufferCount;
    private long nioBufferSize;
    private boolean inFail;
    private static final AtomicLongFieldUpdater<ChannelOutboundBuffer> TOTAL_PENDING_SIZE_UPDATER;
    private volatile long totalPendingSize;
    private static final AtomicIntegerFieldUpdater<ChannelOutboundBuffer> WRITABLE_UPDATER;
    private volatile int writable;
    
    ChannelOutboundBuffer(final AbstractChannel channel) {
        this.writable = 1;
        this.channel = channel;
    }
    
    public void addMessage(final Object msg, final int size, final ChannelPromise promise) {
        final Entry entry = Entry.newInstance(msg, size, total(msg), promise);
        if (this.tailEntry == null) {
            this.flushedEntry = null;
            this.tailEntry = entry;
        }
        else {
            final Entry tail = this.tailEntry;
            tail.next = entry;
            this.tailEntry = entry;
        }
        if (this.unflushedEntry == null) {
            this.unflushedEntry = entry;
        }
        this.incrementPendingOutboundBytes(size);
    }
    
    public void addFlush() {
        Entry entry = this.unflushedEntry;
        if (entry != null) {
            if (this.flushedEntry == null) {
                this.flushedEntry = entry;
            }
            do {
                ++this.flushed;
                if (!entry.promise.setUncancellable()) {
                    final int pending = entry.cancel();
                    this.decrementPendingOutboundBytes(pending);
                }
                entry = entry.next;
            } while (entry != null);
            this.unflushedEntry = null;
        }
    }
    
    void incrementPendingOutboundBytes(final long size) {
        if (size == 0L) {
            return;
        }
        final long newWriteBufferSize = ChannelOutboundBuffer.TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, size);
        if (newWriteBufferSize > this.channel.config().getWriteBufferHighWaterMark() && ChannelOutboundBuffer.WRITABLE_UPDATER.compareAndSet(this, 1, 0)) {
            this.channel.pipeline().fireChannelWritabilityChanged();
        }
    }
    
    void decrementPendingOutboundBytes(final long size) {
        if (size == 0L) {
            return;
        }
        final long newWriteBufferSize = ChannelOutboundBuffer.TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, -size);
        if ((newWriteBufferSize == 0L || newWriteBufferSize < this.channel.config().getWriteBufferLowWaterMark()) && ChannelOutboundBuffer.WRITABLE_UPDATER.compareAndSet(this, 0, 1)) {
            this.channel.pipeline().fireChannelWritabilityChanged();
        }
    }
    
    private static long total(final Object msg) {
        if (msg instanceof ByteBuf) {
            return ((ByteBuf)msg).readableBytes();
        }
        if (msg instanceof FileRegion) {
            return ((FileRegion)msg).count();
        }
        if (msg instanceof ByteBufHolder) {
            return ((ByteBufHolder)msg).content().readableBytes();
        }
        return -1L;
    }
    
    public Object current() {
        final Entry entry = this.flushedEntry;
        if (entry == null) {
            return null;
        }
        return entry.msg;
    }
    
    public void progress(final long amount) {
        final Entry e = this.flushedEntry;
        assert e != null;
        final ChannelPromise p = e.promise;
        if (p instanceof ChannelProgressivePromise) {
            final long progress = e.progress + amount;
            e.progress = progress;
            ((ChannelProgressivePromise)p).tryProgress(progress, e.total);
        }
    }
    
    public boolean remove() {
        final Entry e = this.flushedEntry;
        if (e == null) {
            return false;
        }
        final Object msg = e.msg;
        final ChannelPromise promise = e.promise;
        final int size = e.pendingSize;
        this.removeEntry(e);
        if (!e.cancelled) {
            ReferenceCountUtil.safeRelease(msg);
            safeSuccess(promise);
            this.decrementPendingOutboundBytes(size);
        }
        e.recycle();
        return true;
    }
    
    public boolean remove(final Throwable cause) {
        final Entry e = this.flushedEntry;
        if (e == null) {
            return false;
        }
        final Object msg = e.msg;
        final ChannelPromise promise = e.promise;
        final int size = e.pendingSize;
        this.removeEntry(e);
        if (!e.cancelled) {
            ReferenceCountUtil.safeRelease(msg);
            safeFail(promise, cause);
            this.decrementPendingOutboundBytes(size);
        }
        e.recycle();
        return true;
    }
    
    private void removeEntry(final Entry e) {
        final int flushed = this.flushed - 1;
        this.flushed = flushed;
        if (flushed == 0) {
            this.flushedEntry = null;
            if (e == this.tailEntry) {
                this.tailEntry = null;
                this.unflushedEntry = null;
            }
        }
        else {
            this.flushedEntry = e.next;
        }
    }
    
    public void removeBytes(long writtenBytes) {
        while (true) {
            final Object msg = this.current();
            if (!(msg instanceof ByteBuf)) {
                assert writtenBytes == 0L;
                break;
            }
            else {
                final ByteBuf buf = (ByteBuf)msg;
                final int readerIndex = buf.readerIndex();
                final int readableBytes = buf.writerIndex() - readerIndex;
                if (readableBytes <= writtenBytes) {
                    if (writtenBytes != 0L) {
                        this.progress(readableBytes);
                        writtenBytes -= readableBytes;
                    }
                    this.remove();
                }
                else {
                    if (writtenBytes != 0L) {
                        buf.readerIndex(readerIndex + (int)writtenBytes);
                        this.progress(writtenBytes);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    public ByteBuffer[] nioBuffers() {
        long nioBufferSize = 0L;
        int nioBufferCount = 0;
        final InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
        ByteBuffer[] nioBuffers = ChannelOutboundBuffer.NIO_BUFFERS.get(threadLocalMap);
        for (Entry entry = this.flushedEntry; this.isFlushedEntry(entry) && entry.msg instanceof ByteBuf; entry = entry.next) {
            if (!entry.cancelled) {
                final ByteBuf buf = (ByteBuf)entry.msg;
                final int readerIndex = buf.readerIndex();
                final int readableBytes = buf.writerIndex() - readerIndex;
                if (readableBytes > 0) {
                    nioBufferSize += readableBytes;
                    int count = entry.count;
                    if (count == -1) {
                        count = (entry.count = buf.nioBufferCount());
                    }
                    final int neededSpace = nioBufferCount + count;
                    if (neededSpace > nioBuffers.length) {
                        nioBuffers = expandNioBufferArray(nioBuffers, neededSpace, nioBufferCount);
                        ChannelOutboundBuffer.NIO_BUFFERS.set(threadLocalMap, nioBuffers);
                    }
                    if (count == 1) {
                        ByteBuffer nioBuf = entry.buf;
                        if (nioBuf == null) {
                            nioBuf = (entry.buf = buf.internalNioBuffer(readerIndex, readableBytes));
                        }
                        nioBuffers[nioBufferCount++] = nioBuf;
                    }
                    else {
                        ByteBuffer[] nioBufs = entry.bufs;
                        if (nioBufs == null) {
                            nioBufs = (entry.bufs = buf.nioBuffers());
                        }
                        nioBufferCount = fillBufferArray(nioBufs, nioBuffers, nioBufferCount);
                    }
                }
            }
        }
        this.nioBufferCount = nioBufferCount;
        this.nioBufferSize = nioBufferSize;
        return nioBuffers;
    }
    
    private static int fillBufferArray(final ByteBuffer[] nioBufs, final ByteBuffer[] nioBuffers, int nioBufferCount) {
        for (final ByteBuffer nioBuf : nioBufs) {
            if (nioBuf == null) {
                break;
            }
            nioBuffers[nioBufferCount++] = nioBuf;
        }
        return nioBufferCount;
    }
    
    private static ByteBuffer[] expandNioBufferArray(final ByteBuffer[] array, final int neededSpace, final int size) {
        int newCapacity = array.length;
        do {
            newCapacity <<= 1;
            if (newCapacity < 0) {
                throw new IllegalStateException();
            }
        } while (neededSpace > newCapacity);
        final ByteBuffer[] newArray = new ByteBuffer[newCapacity];
        System.arraycopy(array, 0, newArray, 0, size);
        return newArray;
    }
    
    public int nioBufferCount() {
        return this.nioBufferCount;
    }
    
    public long nioBufferSize() {
        return this.nioBufferSize;
    }
    
    boolean isWritable() {
        return this.writable != 0;
    }
    
    public int size() {
        return this.flushed;
    }
    
    public boolean isEmpty() {
        return this.flushed == 0;
    }
    
    void failFlushed(final Throwable cause) {
        if (this.inFail) {
            return;
        }
        try {
            this.inFail = true;
            while (this.remove(cause)) {}
        }
        finally {
            this.inFail = false;
        }
    }
    
    void close(final ClosedChannelException cause) {
        if (this.inFail) {
            this.channel.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    ChannelOutboundBuffer.this.close(cause);
                }
            });
            return;
        }
        this.inFail = true;
        if (this.channel.isOpen()) {
            throw new IllegalStateException("close() must be invoked after the channel is closed.");
        }
        if (!this.isEmpty()) {
            throw new IllegalStateException("close() must be invoked after all flushed writes are handled.");
        }
        try {
            for (Entry e = this.unflushedEntry; e != null; e = e.recycleAndGetNext()) {
                final int size = e.pendingSize;
                ChannelOutboundBuffer.TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, -size);
                if (!e.cancelled) {
                    ReferenceCountUtil.safeRelease(e.msg);
                    safeFail(e.promise, cause);
                }
            }
        }
        finally {
            this.inFail = false;
        }
    }
    
    private static void safeSuccess(final ChannelPromise promise) {
        if (!(promise instanceof VoidChannelPromise) && !promise.trySuccess()) {
            ChannelOutboundBuffer.logger.warn("Failed to mark a promise as success because it is done already: {}", promise);
        }
    }
    
    private static void safeFail(final ChannelPromise promise, final Throwable cause) {
        if (!(promise instanceof VoidChannelPromise) && !promise.tryFailure(cause)) {
            ChannelOutboundBuffer.logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause);
        }
    }
    
    @Deprecated
    public void recycle() {
    }
    
    public long totalPendingWriteBytes() {
        return this.totalPendingSize;
    }
    
    public void forEachFlushedMessage(final MessageProcessor processor) throws Exception {
        if (processor == null) {
            throw new NullPointerException("processor");
        }
        Entry entry = this.flushedEntry;
        if (entry == null) {
            return;
        }
        while (entry.cancelled || processor.processMessage(entry.msg)) {
            entry = entry.next;
            if (!this.isFlushedEntry(entry)) {
                return;
            }
        }
    }
    
    private boolean isFlushedEntry(final Entry e) {
        return e != null && e != this.unflushedEntry;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ChannelOutboundBuffer.class);
        NIO_BUFFERS = new FastThreadLocal<ByteBuffer[]>() {
            @Override
            protected ByteBuffer[] initialValue() throws Exception {
                return new ByteBuffer[1024];
            }
        };
        AtomicIntegerFieldUpdater<ChannelOutboundBuffer> writableUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(ChannelOutboundBuffer.class, "writable");
        if (writableUpdater == null) {
            writableUpdater = AtomicIntegerFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "writable");
        }
        WRITABLE_UPDATER = writableUpdater;
        AtomicLongFieldUpdater<ChannelOutboundBuffer> pendingSizeUpdater = PlatformDependent.newAtomicLongFieldUpdater(ChannelOutboundBuffer.class, "totalPendingSize");
        if (pendingSizeUpdater == null) {
            pendingSizeUpdater = AtomicLongFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "totalPendingSize");
        }
        TOTAL_PENDING_SIZE_UPDATER = pendingSizeUpdater;
    }
    
    static final class Entry
    {
        private static final Recycler<Entry> RECYCLER;
        private final Recycler.Handle handle;
        Entry next;
        Object msg;
        ByteBuffer[] bufs;
        ByteBuffer buf;
        ChannelPromise promise;
        long progress;
        long total;
        int pendingSize;
        int count;
        boolean cancelled;
        
        private Entry(final Recycler.Handle handle) {
            this.count = -1;
            this.handle = handle;
        }
        
        static Entry newInstance(final Object msg, final int size, final long total, final ChannelPromise promise) {
            final Entry entry = Entry.RECYCLER.get();
            entry.msg = msg;
            entry.pendingSize = size;
            entry.total = total;
            entry.promise = promise;
            return entry;
        }
        
        int cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                final int pSize = this.pendingSize;
                ReferenceCountUtil.safeRelease(this.msg);
                this.msg = Unpooled.EMPTY_BUFFER;
                this.pendingSize = 0;
                this.total = 0L;
                this.progress = 0L;
                this.bufs = null;
                this.buf = null;
                return pSize;
            }
            return 0;
        }
        
        void recycle() {
            this.next = null;
            this.bufs = null;
            this.buf = null;
            this.msg = null;
            this.promise = null;
            this.progress = 0L;
            this.total = 0L;
            this.pendingSize = 0;
            this.count = -1;
            this.cancelled = false;
            Entry.RECYCLER.recycle(this, this.handle);
        }
        
        Entry recycleAndGetNext() {
            final Entry next = this.next;
            this.recycle();
            return next;
        }
        
        static {
            RECYCLER = new Recycler<Entry>() {
                @Override
                protected Entry newObject(final Handle handle) {
                    return new Entry(handle);
                }
            };
        }
    }
    
    public interface MessageProcessor
    {
        boolean processMessage(final Object p0) throws Exception;
    }
}
