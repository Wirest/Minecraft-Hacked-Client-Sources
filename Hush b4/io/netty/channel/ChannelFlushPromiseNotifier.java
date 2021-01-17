// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import java.util.Iterator;
import java.util.ArrayDeque;
import java.util.Queue;

public final class ChannelFlushPromiseNotifier
{
    private long writeCounter;
    private final Queue<FlushCheckpoint> flushCheckpoints;
    private final boolean tryNotify;
    
    public ChannelFlushPromiseNotifier(final boolean tryNotify) {
        this.flushCheckpoints = new ArrayDeque<FlushCheckpoint>();
        this.tryNotify = tryNotify;
    }
    
    public ChannelFlushPromiseNotifier() {
        this(false);
    }
    
    @Deprecated
    public ChannelFlushPromiseNotifier add(final ChannelPromise promise, final int pendingDataSize) {
        return this.add(promise, (long)pendingDataSize);
    }
    
    public ChannelFlushPromiseNotifier add(final ChannelPromise promise, final long pendingDataSize) {
        if (promise == null) {
            throw new NullPointerException("promise");
        }
        if (pendingDataSize < 0L) {
            throw new IllegalArgumentException("pendingDataSize must be >= 0 but was " + pendingDataSize);
        }
        final long checkpoint = this.writeCounter + pendingDataSize;
        if (promise instanceof FlushCheckpoint) {
            final FlushCheckpoint cp = (FlushCheckpoint)promise;
            cp.flushCheckpoint(checkpoint);
            this.flushCheckpoints.add(cp);
        }
        else {
            this.flushCheckpoints.add(new DefaultFlushCheckpoint(checkpoint, promise));
        }
        return this;
    }
    
    public ChannelFlushPromiseNotifier increaseWriteCounter(final long delta) {
        if (delta < 0L) {
            throw new IllegalArgumentException("delta must be >= 0 but was " + delta);
        }
        this.writeCounter += delta;
        return this;
    }
    
    public long writeCounter() {
        return this.writeCounter;
    }
    
    public ChannelFlushPromiseNotifier notifyPromises() {
        this.notifyPromises0(null);
        return this;
    }
    
    @Deprecated
    public ChannelFlushPromiseNotifier notifyFlushFutures() {
        return this.notifyPromises();
    }
    
    public ChannelFlushPromiseNotifier notifyPromises(final Throwable cause) {
        this.notifyPromises();
        while (true) {
            final FlushCheckpoint cp = this.flushCheckpoints.poll();
            if (cp == null) {
                break;
            }
            if (this.tryNotify) {
                cp.promise().tryFailure(cause);
            }
            else {
                cp.promise().setFailure(cause);
            }
        }
        return this;
    }
    
    @Deprecated
    public ChannelFlushPromiseNotifier notifyFlushFutures(final Throwable cause) {
        return this.notifyPromises(cause);
    }
    
    public ChannelFlushPromiseNotifier notifyPromises(final Throwable cause1, final Throwable cause2) {
        this.notifyPromises0(cause1);
        while (true) {
            final FlushCheckpoint cp = this.flushCheckpoints.poll();
            if (cp == null) {
                break;
            }
            if (this.tryNotify) {
                cp.promise().tryFailure(cause2);
            }
            else {
                cp.promise().setFailure(cause2);
            }
        }
        return this;
    }
    
    @Deprecated
    public ChannelFlushPromiseNotifier notifyFlushFutures(final Throwable cause1, final Throwable cause2) {
        return this.notifyPromises(cause1, cause2);
    }
    
    private void notifyPromises0(final Throwable cause) {
        if (this.flushCheckpoints.isEmpty()) {
            this.writeCounter = 0L;
            return;
        }
        final long writeCounter = this.writeCounter;
        while (true) {
            final FlushCheckpoint cp = this.flushCheckpoints.peek();
            if (cp == null) {
                this.writeCounter = 0L;
                break;
            }
            if (cp.flushCheckpoint() > writeCounter) {
                if (writeCounter > 0L && this.flushCheckpoints.size() == 1) {
                    this.writeCounter = 0L;
                    cp.flushCheckpoint(cp.flushCheckpoint() - writeCounter);
                    break;
                }
                break;
            }
            else {
                this.flushCheckpoints.remove();
                final ChannelPromise promise = cp.promise();
                if (cause == null) {
                    if (this.tryNotify) {
                        promise.trySuccess();
                    }
                    else {
                        promise.setSuccess();
                    }
                }
                else if (this.tryNotify) {
                    promise.tryFailure(cause);
                }
                else {
                    promise.setFailure(cause);
                }
            }
        }
        final long newWriteCounter = this.writeCounter;
        if (newWriteCounter >= 549755813888L) {
            this.writeCounter = 0L;
            for (final FlushCheckpoint cp2 : this.flushCheckpoints) {
                cp2.flushCheckpoint(cp2.flushCheckpoint() - newWriteCounter);
            }
        }
    }
    
    private static class DefaultFlushCheckpoint implements FlushCheckpoint
    {
        private long checkpoint;
        private final ChannelPromise future;
        
        DefaultFlushCheckpoint(final long checkpoint, final ChannelPromise future) {
            this.checkpoint = checkpoint;
            this.future = future;
        }
        
        @Override
        public long flushCheckpoint() {
            return this.checkpoint;
        }
        
        @Override
        public void flushCheckpoint(final long checkpoint) {
            this.checkpoint = checkpoint;
        }
        
        @Override
        public ChannelPromise promise() {
            return this.future;
        }
    }
    
    interface FlushCheckpoint
    {
        long flushCheckpoint();
        
        void flushCheckpoint(final long p0);
        
        ChannelPromise promise();
    }
}
