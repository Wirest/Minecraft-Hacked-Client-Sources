// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.Promise;
import java.util.concurrent.RejectedExecutionException;
import java.io.IOException;
import io.netty.util.ReferenceCountUtil;
import java.net.InetSocketAddress;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.OneTimeTask;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.internal.ThreadLocalRandom;
import java.net.SocketAddress;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.ClosedChannelException;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.DefaultAttributeMap;

public abstract class AbstractChannel extends DefaultAttributeMap implements Channel
{
    private static final InternalLogger logger;
    static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION;
    static final NotYetConnectedException NOT_YET_CONNECTED_EXCEPTION;
    private MessageSizeEstimator.Handle estimatorHandle;
    private final Channel parent;
    private final long hashCode;
    private final Unsafe unsafe;
    private final DefaultChannelPipeline pipeline;
    private final ChannelFuture succeededFuture;
    private final VoidChannelPromise voidPromise;
    private final VoidChannelPromise unsafeVoidPromise;
    private final CloseFuture closeFuture;
    private volatile SocketAddress localAddress;
    private volatile SocketAddress remoteAddress;
    private volatile EventLoop eventLoop;
    private volatile boolean registered;
    private boolean strValActive;
    private String strVal;
    
    protected AbstractChannel(final Channel parent) {
        this.hashCode = ThreadLocalRandom.current().nextLong();
        this.succeededFuture = new SucceededChannelFuture(this, null);
        this.voidPromise = new VoidChannelPromise(this, true);
        this.unsafeVoidPromise = new VoidChannelPromise(this, false);
        this.closeFuture = new CloseFuture(this);
        this.parent = parent;
        this.unsafe = this.newUnsafe();
        this.pipeline = new DefaultChannelPipeline(this);
    }
    
    @Override
    public boolean isWritable() {
        final ChannelOutboundBuffer buf = this.unsafe.outboundBuffer();
        return buf != null && buf.isWritable();
    }
    
    @Override
    public Channel parent() {
        return this.parent;
    }
    
    @Override
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.config().getAllocator();
    }
    
    @Override
    public EventLoop eventLoop() {
        final EventLoop eventLoop = this.eventLoop;
        if (eventLoop == null) {
            throw new IllegalStateException("channel not registered to an event loop");
        }
        return eventLoop;
    }
    
    @Override
    public SocketAddress localAddress() {
        SocketAddress localAddress = this.localAddress;
        if (localAddress == null) {
            try {
                localAddress = (this.localAddress = this.unsafe().localAddress());
            }
            catch (Throwable t) {
                return null;
            }
        }
        return localAddress;
    }
    
    protected void invalidateLocalAddress() {
        this.localAddress = null;
    }
    
    @Override
    public SocketAddress remoteAddress() {
        SocketAddress remoteAddress = this.remoteAddress;
        if (remoteAddress == null) {
            try {
                remoteAddress = (this.remoteAddress = this.unsafe().remoteAddress());
            }
            catch (Throwable t) {
                return null;
            }
        }
        return remoteAddress;
    }
    
    protected void invalidateRemoteAddress() {
        this.remoteAddress = null;
    }
    
    @Override
    public boolean isRegistered() {
        return this.registered;
    }
    
    @Override
    public ChannelFuture bind(final SocketAddress localAddress) {
        return this.pipeline.bind(localAddress);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress) {
        return this.pipeline.connect(remoteAddress);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        return this.pipeline.connect(remoteAddress, localAddress);
    }
    
    @Override
    public ChannelFuture disconnect() {
        return this.pipeline.disconnect();
    }
    
    @Override
    public ChannelFuture close() {
        return this.pipeline.close();
    }
    
    @Override
    public ChannelFuture deregister() {
        return this.pipeline.deregister();
    }
    
    @Override
    public Channel flush() {
        this.pipeline.flush();
        return this;
    }
    
    @Override
    public ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise) {
        return this.pipeline.bind(localAddress, promise);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final ChannelPromise promise) {
        return this.pipeline.connect(remoteAddress, promise);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        return this.pipeline.connect(remoteAddress, localAddress, promise);
    }
    
    @Override
    public ChannelFuture disconnect(final ChannelPromise promise) {
        return this.pipeline.disconnect(promise);
    }
    
    @Override
    public ChannelFuture close(final ChannelPromise promise) {
        return this.pipeline.close(promise);
    }
    
    @Override
    public ChannelFuture deregister(final ChannelPromise promise) {
        return this.pipeline.deregister(promise);
    }
    
    @Override
    public Channel read() {
        this.pipeline.read();
        return this;
    }
    
    @Override
    public ChannelFuture write(final Object msg) {
        return this.pipeline.write(msg);
    }
    
    @Override
    public ChannelFuture write(final Object msg, final ChannelPromise promise) {
        return this.pipeline.write(msg, promise);
    }
    
    @Override
    public ChannelFuture writeAndFlush(final Object msg) {
        return this.pipeline.writeAndFlush(msg);
    }
    
    @Override
    public ChannelFuture writeAndFlush(final Object msg, final ChannelPromise promise) {
        return this.pipeline.writeAndFlush(msg, promise);
    }
    
    @Override
    public ChannelPromise newPromise() {
        return new DefaultChannelPromise(this);
    }
    
    @Override
    public ChannelProgressivePromise newProgressivePromise() {
        return new DefaultChannelProgressivePromise(this);
    }
    
    @Override
    public ChannelFuture newSucceededFuture() {
        return this.succeededFuture;
    }
    
    @Override
    public ChannelFuture newFailedFuture(final Throwable cause) {
        return new FailedChannelFuture(this, null, cause);
    }
    
    @Override
    public ChannelFuture closeFuture() {
        return this.closeFuture;
    }
    
    @Override
    public Unsafe unsafe() {
        return this.unsafe;
    }
    
    protected abstract AbstractUnsafe newUnsafe();
    
    @Override
    public final int hashCode() {
        return (int)this.hashCode;
    }
    
    @Override
    public final boolean equals(final Object o) {
        return this == o;
    }
    
    @Override
    public final int compareTo(final Channel o) {
        if (this == o) {
            return 0;
        }
        long ret = this.hashCode - o.hashCode();
        if (ret > 0L) {
            return 1;
        }
        if (ret < 0L) {
            return -1;
        }
        ret = System.identityHashCode(this) - System.identityHashCode(o);
        if (ret != 0L) {
            return (int)ret;
        }
        throw new Error();
    }
    
    @Override
    public String toString() {
        final boolean active = this.isActive();
        if (this.strValActive == active && this.strVal != null) {
            return this.strVal;
        }
        final SocketAddress remoteAddr = this.remoteAddress();
        final SocketAddress localAddr = this.localAddress();
        if (remoteAddr != null) {
            SocketAddress srcAddr;
            SocketAddress dstAddr;
            if (this.parent == null) {
                srcAddr = localAddr;
                dstAddr = remoteAddr;
            }
            else {
                srcAddr = remoteAddr;
                dstAddr = localAddr;
            }
            this.strVal = String.format("[id: 0x%08x, %s %s %s]", (int)this.hashCode, srcAddr, active ? "=>" : ":>", dstAddr);
        }
        else if (localAddr != null) {
            this.strVal = String.format("[id: 0x%08x, %s]", (int)this.hashCode, localAddr);
        }
        else {
            this.strVal = String.format("[id: 0x%08x]", (int)this.hashCode);
        }
        this.strValActive = active;
        return this.strVal;
    }
    
    @Override
    public final ChannelPromise voidPromise() {
        return this.voidPromise;
    }
    
    final MessageSizeEstimator.Handle estimatorHandle() {
        if (this.estimatorHandle == null) {
            this.estimatorHandle = this.config().getMessageSizeEstimator().newHandle();
        }
        return this.estimatorHandle;
    }
    
    protected abstract boolean isCompatible(final EventLoop p0);
    
    protected abstract SocketAddress localAddress0();
    
    protected abstract SocketAddress remoteAddress0();
    
    protected void doRegister() throws Exception {
    }
    
    protected abstract void doBind(final SocketAddress p0) throws Exception;
    
    protected abstract void doDisconnect() throws Exception;
    
    protected abstract void doClose() throws Exception;
    
    protected void doDeregister() throws Exception {
    }
    
    protected abstract void doBeginRead() throws Exception;
    
    protected abstract void doWrite(final ChannelOutboundBuffer p0) throws Exception;
    
    protected Object filterOutboundMessage(final Object msg) throws Exception {
        return msg;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AbstractChannel.class);
        CLOSED_CHANNEL_EXCEPTION = new ClosedChannelException();
        NOT_YET_CONNECTED_EXCEPTION = new NotYetConnectedException();
        AbstractChannel.CLOSED_CHANNEL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        AbstractChannel.NOT_YET_CONNECTED_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    }
    
    protected abstract class AbstractUnsafe implements Unsafe
    {
        private ChannelOutboundBuffer outboundBuffer;
        private boolean inFlush0;
        
        protected AbstractUnsafe() {
            this.outboundBuffer = new ChannelOutboundBuffer(AbstractChannel.this);
        }
        
        @Override
        public final ChannelOutboundBuffer outboundBuffer() {
            return this.outboundBuffer;
        }
        
        @Override
        public final SocketAddress localAddress() {
            return AbstractChannel.this.localAddress0();
        }
        
        @Override
        public final SocketAddress remoteAddress() {
            return AbstractChannel.this.remoteAddress0();
        }
        
        @Override
        public final void register(final EventLoop eventLoop, final ChannelPromise promise) {
            if (eventLoop == null) {
                throw new NullPointerException("eventLoop");
            }
            if (AbstractChannel.this.isRegistered()) {
                promise.setFailure((Throwable)new IllegalStateException("registered to an event loop already"));
                return;
            }
            if (!AbstractChannel.this.isCompatible(eventLoop)) {
                promise.setFailure((Throwable)new IllegalStateException("incompatible event loop type: " + eventLoop.getClass().getName()));
                return;
            }
            AbstractChannel.this.eventLoop = eventLoop;
            if (eventLoop.inEventLoop()) {
                this.register0(promise);
            }
            else {
                try {
                    eventLoop.execute(new OneTimeTask() {
                        @Override
                        public void run() {
                            AbstractUnsafe.this.register0(promise);
                        }
                    });
                }
                catch (Throwable t) {
                    AbstractChannel.logger.warn("Force-closing a channel whose registration task was not accepted by an event loop: {}", AbstractChannel.this, t);
                    this.closeForcibly();
                    AbstractChannel.this.closeFuture.setClosed();
                    this.safeSetFailure(promise, t);
                }
            }
        }
        
        private void register0(final ChannelPromise promise) {
            try {
                if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
                    return;
                }
                AbstractChannel.this.doRegister();
                AbstractChannel.this.registered = true;
                this.safeSetSuccess(promise);
                AbstractChannel.this.pipeline.fireChannelRegistered();
                if (AbstractChannel.this.isActive()) {
                    AbstractChannel.this.pipeline.fireChannelActive();
                }
            }
            catch (Throwable t) {
                this.closeForcibly();
                AbstractChannel.this.closeFuture.setClosed();
                this.safeSetFailure(promise, t);
            }
        }
        
        @Override
        public final void bind(final SocketAddress localAddress, final ChannelPromise promise) {
            if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
                return;
            }
            if (!PlatformDependent.isWindows() && !PlatformDependent.isRoot() && Boolean.TRUE.equals(AbstractChannel.this.config().getOption(ChannelOption.SO_BROADCAST)) && localAddress instanceof InetSocketAddress && !((InetSocketAddress)localAddress).getAddress().isAnyLocalAddress()) {
                AbstractChannel.logger.warn("A non-root user can't receive a broadcast packet if the socket is not bound to a wildcard address; binding to a non-wildcard address (" + localAddress + ") anyway as requested.");
            }
            final boolean wasActive = AbstractChannel.this.isActive();
            try {
                AbstractChannel.this.doBind(localAddress);
            }
            catch (Throwable t) {
                this.safeSetFailure(promise, t);
                this.closeIfClosed();
                return;
            }
            if (!wasActive && AbstractChannel.this.isActive()) {
                this.invokeLater(new OneTimeTask() {
                    @Override
                    public void run() {
                        AbstractChannel.this.pipeline.fireChannelActive();
                    }
                });
            }
            this.safeSetSuccess(promise);
        }
        
        @Override
        public final void disconnect(final ChannelPromise promise) {
            if (!promise.setUncancellable()) {
                return;
            }
            final boolean wasActive = AbstractChannel.this.isActive();
            try {
                AbstractChannel.this.doDisconnect();
            }
            catch (Throwable t) {
                this.safeSetFailure(promise, t);
                this.closeIfClosed();
                return;
            }
            if (wasActive && !AbstractChannel.this.isActive()) {
                this.invokeLater(new OneTimeTask() {
                    @Override
                    public void run() {
                        AbstractChannel.this.pipeline.fireChannelInactive();
                    }
                });
            }
            this.safeSetSuccess(promise);
            this.closeIfClosed();
        }
        
        @Override
        public final void close(final ChannelPromise promise) {
            if (!promise.setUncancellable()) {
                return;
            }
            if (this.inFlush0) {
                this.invokeLater(new OneTimeTask() {
                    @Override
                    public void run() {
                        AbstractUnsafe.this.close(promise);
                    }
                });
                return;
            }
            if (AbstractChannel.this.closeFuture.isDone()) {
                this.safeSetSuccess(promise);
                return;
            }
            final boolean wasActive = AbstractChannel.this.isActive();
            final ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            this.outboundBuffer = null;
            try {
                AbstractChannel.this.doClose();
                AbstractChannel.this.closeFuture.setClosed();
                this.safeSetSuccess(promise);
            }
            catch (Throwable t) {
                AbstractChannel.this.closeFuture.setClosed();
                this.safeSetFailure(promise, t);
            }
            try {
                outboundBuffer.failFlushed(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
                outboundBuffer.close(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
            }
            finally {
                if (wasActive && !AbstractChannel.this.isActive()) {
                    this.invokeLater(new OneTimeTask() {
                        @Override
                        public void run() {
                            AbstractChannel.this.pipeline.fireChannelInactive();
                        }
                    });
                }
                this.deregister(this.voidPromise());
            }
        }
        
        @Override
        public final void closeForcibly() {
            try {
                AbstractChannel.this.doClose();
            }
            catch (Exception e) {
                AbstractChannel.logger.warn("Failed to close a channel.", e);
            }
        }
        
        @Override
        public final void deregister(final ChannelPromise promise) {
            if (!promise.setUncancellable()) {
                return;
            }
            if (!AbstractChannel.this.registered) {
                this.safeSetSuccess(promise);
                return;
            }
            try {
                AbstractChannel.this.doDeregister();
            }
            catch (Throwable t) {
                AbstractChannel.logger.warn("Unexpected exception occurred while deregistering a channel.", t);
            }
            finally {
                if (AbstractChannel.this.registered) {
                    AbstractChannel.this.registered = false;
                    this.invokeLater(new OneTimeTask() {
                        @Override
                        public void run() {
                            AbstractChannel.this.pipeline.fireChannelUnregistered();
                        }
                    });
                    this.safeSetSuccess(promise);
                }
                else {
                    this.safeSetSuccess(promise);
                }
            }
        }
        
        @Override
        public final void beginRead() {
            if (!AbstractChannel.this.isActive()) {
                return;
            }
            try {
                AbstractChannel.this.doBeginRead();
            }
            catch (Exception e) {
                this.invokeLater(new OneTimeTask() {
                    @Override
                    public void run() {
                        AbstractChannel.this.pipeline.fireExceptionCaught(e);
                    }
                });
                this.close(this.voidPromise());
            }
        }
        
        @Override
        public final void write(Object msg, final ChannelPromise promise) {
            final ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            if (outboundBuffer == null) {
                this.safeSetFailure(promise, AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
                ReferenceCountUtil.release(msg);
                return;
            }
            int size;
            try {
                msg = AbstractChannel.this.filterOutboundMessage(msg);
                size = AbstractChannel.this.estimatorHandle().size(msg);
                if (size < 0) {
                    size = 0;
                }
            }
            catch (Throwable t) {
                this.safeSetFailure(promise, t);
                ReferenceCountUtil.release(msg);
                return;
            }
            outboundBuffer.addMessage(msg, size, promise);
        }
        
        @Override
        public final void flush() {
            final ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            if (outboundBuffer == null) {
                return;
            }
            outboundBuffer.addFlush();
            this.flush0();
        }
        
        protected void flush0() {
            if (this.inFlush0) {
                return;
            }
            final ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            if (outboundBuffer == null || outboundBuffer.isEmpty()) {
                return;
            }
            this.inFlush0 = true;
            if (!AbstractChannel.this.isActive()) {
                try {
                    if (AbstractChannel.this.isOpen()) {
                        outboundBuffer.failFlushed(AbstractChannel.NOT_YET_CONNECTED_EXCEPTION);
                    }
                    else {
                        outboundBuffer.failFlushed(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
                    }
                }
                finally {
                    this.inFlush0 = false;
                }
                return;
            }
            try {
                AbstractChannel.this.doWrite(outboundBuffer);
            }
            catch (Throwable t) {
                outboundBuffer.failFlushed(t);
                if (t instanceof IOException && AbstractChannel.this.config().isAutoClose()) {
                    this.close(this.voidPromise());
                }
            }
            finally {
                this.inFlush0 = false;
            }
        }
        
        @Override
        public final ChannelPromise voidPromise() {
            return AbstractChannel.this.unsafeVoidPromise;
        }
        
        protected final boolean ensureOpen(final ChannelPromise promise) {
            if (AbstractChannel.this.isOpen()) {
                return true;
            }
            this.safeSetFailure(promise, AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
            return false;
        }
        
        protected final void safeSetSuccess(final ChannelPromise promise) {
            if (!(promise instanceof VoidChannelPromise) && !promise.trySuccess()) {
                AbstractChannel.logger.warn("Failed to mark a promise as success because it is done already: {}", promise);
            }
        }
        
        protected final void safeSetFailure(final ChannelPromise promise, final Throwable cause) {
            if (!(promise instanceof VoidChannelPromise) && !promise.tryFailure(cause)) {
                AbstractChannel.logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause);
            }
        }
        
        protected final void closeIfClosed() {
            if (AbstractChannel.this.isOpen()) {
                return;
            }
            this.close(this.voidPromise());
        }
        
        private void invokeLater(final Runnable task) {
            try {
                AbstractChannel.this.eventLoop().execute(task);
            }
            catch (RejectedExecutionException e) {
                AbstractChannel.logger.warn("Can't invoke task later as EventLoop rejected it", e);
            }
        }
    }
    
    static final class CloseFuture extends DefaultChannelPromise
    {
        CloseFuture(final AbstractChannel ch) {
            super(ch);
        }
        
        @Override
        public ChannelPromise setSuccess() {
            throw new IllegalStateException();
        }
        
        @Override
        public ChannelPromise setFailure(final Throwable cause) {
            throw new IllegalStateException();
        }
        
        @Override
        public boolean trySuccess() {
            throw new IllegalStateException();
        }
        
        @Override
        public boolean tryFailure(final Throwable cause) {
            throw new IllegalStateException();
        }
        
        boolean setClosed() {
            return super.trySuccess();
        }
    }
}
