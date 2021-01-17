// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.Recycler;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;

public final class PendingWriteQueue
{
    private static final InternalLogger logger;
    private final ChannelHandlerContext ctx;
    private final ChannelOutboundBuffer buffer;
    private final MessageSizeEstimator.Handle estimatorHandle;
    private PendingWrite head;
    private PendingWrite tail;
    private int size;
    
    public PendingWriteQueue(final ChannelHandlerContext ctx) {
        if (ctx == null) {
            throw new NullPointerException("ctx");
        }
        this.ctx = ctx;
        this.buffer = ctx.channel().unsafe().outboundBuffer();
        this.estimatorHandle = ctx.channel().config().getMessageSizeEstimator().newHandle();
    }
    
    public boolean isEmpty() {
        assert this.ctx.executor().inEventLoop();
        return this.head == null;
    }
    
    public int size() {
        assert this.ctx.executor().inEventLoop();
        return this.size;
    }
    
    public void add(final Object msg, final ChannelPromise promise) {
        assert this.ctx.executor().inEventLoop();
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        if (promise == null) {
            throw new NullPointerException("promise");
        }
        int messageSize = this.estimatorHandle.size(msg);
        if (messageSize < 0) {
            messageSize = 0;
        }
        final PendingWrite write = PendingWrite.newInstance(msg, messageSize, promise);
        final PendingWrite currentTail = this.tail;
        if (currentTail == null) {
            final PendingWrite pendingWrite = write;
            this.head = pendingWrite;
            this.tail = pendingWrite;
        }
        else {
            currentTail.next = write;
            this.tail = write;
        }
        ++this.size;
        this.buffer.incrementPendingOutboundBytes(write.size);
    }
    
    public void removeAndFailAll(final Throwable cause) {
        assert this.ctx.executor().inEventLoop();
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        PendingWrite next;
        for (PendingWrite write = this.head; write != null; write = next) {
            next = write.next;
            ReferenceCountUtil.safeRelease(write.msg);
            final ChannelPromise promise = write.promise;
            this.recycle(write);
            safeFail(promise, cause);
        }
        this.assertEmpty();
    }
    
    public void removeAndFail(final Throwable cause) {
        assert this.ctx.executor().inEventLoop();
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        final PendingWrite write = this.head;
        if (write == null) {
            return;
        }
        ReferenceCountUtil.safeRelease(write.msg);
        final ChannelPromise promise = write.promise;
        safeFail(promise, cause);
        this.recycle(write);
    }
    
    public ChannelFuture removeAndWriteAll() {
        assert this.ctx.executor().inEventLoop();
        PendingWrite write = this.head;
        if (write == null) {
            return null;
        }
        if (this.size == 1) {
            return this.removeAndWrite();
        }
        final ChannelPromise p = this.ctx.newPromise();
        final ChannelPromiseAggregator aggregator = new ChannelPromiseAggregator(p);
        while (write != null) {
            final PendingWrite next = write.next;
            final Object msg = write.msg;
            final ChannelPromise promise = write.promise;
            this.recycle(write);
            this.ctx.write(msg, promise);
            aggregator.add(promise);
            write = next;
        }
        this.assertEmpty();
        return p;
    }
    
    private void assertEmpty() {
        assert this.tail == null && this.head == null && this.size == 0;
    }
    
    public ChannelFuture removeAndWrite() {
        assert this.ctx.executor().inEventLoop();
        final PendingWrite write = this.head;
        if (write == null) {
            return null;
        }
        final Object msg = write.msg;
        final ChannelPromise promise = write.promise;
        this.recycle(write);
        return this.ctx.write(msg, promise);
    }
    
    public ChannelPromise remove() {
        assert this.ctx.executor().inEventLoop();
        final PendingWrite write = this.head;
        if (write == null) {
            return null;
        }
        final ChannelPromise promise = write.promise;
        ReferenceCountUtil.safeRelease(write.msg);
        this.recycle(write);
        return promise;
    }
    
    public Object current() {
        assert this.ctx.executor().inEventLoop();
        final PendingWrite write = this.head;
        if (write == null) {
            return null;
        }
        return write.msg;
    }
    
    private void recycle(final PendingWrite write) {
        final PendingWrite next = write.next;
        this.buffer.decrementPendingOutboundBytes(write.size);
        write.recycle();
        --this.size;
        if (next == null) {
            final PendingWrite pendingWrite = null;
            this.tail = pendingWrite;
            this.head = pendingWrite;
            assert this.size == 0;
        }
        else {
            this.head = next;
            assert this.size > 0;
        }
    }
    
    private static void safeFail(final ChannelPromise promise, final Throwable cause) {
        if (!(promise instanceof VoidChannelPromise) && !promise.tryFailure(cause)) {
            PendingWriteQueue.logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(PendingWriteQueue.class);
    }
    
    static final class PendingWrite
    {
        private static final Recycler<PendingWrite> RECYCLER;
        private final Recycler.Handle handle;
        private PendingWrite next;
        private long size;
        private ChannelPromise promise;
        private Object msg;
        
        private PendingWrite(final Recycler.Handle handle) {
            this.handle = handle;
        }
        
        static PendingWrite newInstance(final Object msg, final int size, final ChannelPromise promise) {
            final PendingWrite write = PendingWrite.RECYCLER.get();
            write.size = size;
            write.msg = msg;
            write.promise = promise;
            return write;
        }
        
        private void recycle() {
            this.size = 0L;
            this.next = null;
            this.msg = null;
            this.promise = null;
            PendingWrite.RECYCLER.recycle(this, this.handle);
        }
        
        static {
            RECYCLER = new Recycler<PendingWrite>() {
                @Override
                protected PendingWrite newObject(final Handle handle) {
                    return new PendingWrite(handle);
                }
            };
        }
    }
}
