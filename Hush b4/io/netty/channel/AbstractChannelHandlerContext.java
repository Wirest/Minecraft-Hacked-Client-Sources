// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.Recycler;
import io.netty.util.internal.RecyclableMpscLinkedQueueNode;
import io.netty.util.internal.StringUtil;
import io.netty.util.ReferenceCountUtil;
import java.net.SocketAddress;
import io.netty.util.internal.OneTimeTask;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.DefaultAttributeMap;

abstract class AbstractChannelHandlerContext extends DefaultAttributeMap implements ChannelHandlerContext
{
    volatile AbstractChannelHandlerContext next;
    volatile AbstractChannelHandlerContext prev;
    private final boolean inbound;
    private final boolean outbound;
    private final AbstractChannel channel;
    private final DefaultChannelPipeline pipeline;
    private final String name;
    private boolean removed;
    final EventExecutor executor;
    private ChannelFuture succeededFuture;
    private volatile Runnable invokeChannelReadCompleteTask;
    private volatile Runnable invokeReadTask;
    private volatile Runnable invokeChannelWritableStateChangedTask;
    private volatile Runnable invokeFlushTask;
    
    AbstractChannelHandlerContext(final DefaultChannelPipeline pipeline, final EventExecutorGroup group, final String name, final boolean inbound, final boolean outbound) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.channel = pipeline.channel;
        this.pipeline = pipeline;
        this.name = name;
        if (group != null) {
            EventExecutor childExecutor = pipeline.childExecutors.get(group);
            if (childExecutor == null) {
                childExecutor = group.next();
                pipeline.childExecutors.put(group, childExecutor);
            }
            this.executor = childExecutor;
        }
        else {
            this.executor = null;
        }
        this.inbound = inbound;
        this.outbound = outbound;
    }
    
    void teardown() {
        final EventExecutor executor = this.executor();
        if (executor.inEventLoop()) {
            this.teardown0();
        }
        else {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.teardown0();
                }
            });
        }
    }
    
    private void teardown0() {
        final AbstractChannelHandlerContext prev = this.prev;
        if (prev != null) {
            synchronized (this.pipeline) {
                this.pipeline.remove0(this);
            }
            prev.teardown();
        }
    }
    
    @Override
    public Channel channel() {
        return this.channel;
    }
    
    @Override
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.channel().config().getAllocator();
    }
    
    @Override
    public EventExecutor executor() {
        if (this.executor == null) {
            return this.channel().eventLoop();
        }
        return this.executor;
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    @Override
    public ChannelHandlerContext fireChannelRegistered() {
        final AbstractChannelHandlerContext next = this.findContextInbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelRegistered();
        }
        else {
            executor.execute(new OneTimeTask() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelRegistered();
                }
            });
        }
        return this;
    }
    
    private void invokeChannelRegistered() {
        try {
            ((ChannelInboundHandler)this.handler()).channelRegistered(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelUnregistered() {
        final AbstractChannelHandlerContext next = this.findContextInbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelUnregistered();
        }
        else {
            executor.execute(new OneTimeTask() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelUnregistered();
                }
            });
        }
        return this;
    }
    
    private void invokeChannelUnregistered() {
        try {
            ((ChannelInboundHandler)this.handler()).channelUnregistered(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelActive() {
        final AbstractChannelHandlerContext next = this.findContextInbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelActive();
        }
        else {
            executor.execute(new OneTimeTask() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelActive();
                }
            });
        }
        return this;
    }
    
    private void invokeChannelActive() {
        try {
            ((ChannelInboundHandler)this.handler()).channelActive(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelInactive() {
        final AbstractChannelHandlerContext next = this.findContextInbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelInactive();
        }
        else {
            executor.execute(new OneTimeTask() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelInactive();
                }
            });
        }
        return this;
    }
    
    private void invokeChannelInactive() {
        try {
            ((ChannelInboundHandler)this.handler()).channelInactive(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }
    
    @Override
    public ChannelHandlerContext fireExceptionCaught(final Throwable cause) {
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        final AbstractChannelHandlerContext next = this.next;
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeExceptionCaught(cause);
        }
        else {
            try {
                executor.execute(new OneTimeTask() {
                    @Override
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeExceptionCaught(cause);
                    }
                });
            }
            catch (Throwable t) {
                if (DefaultChannelPipeline.logger.isWarnEnabled()) {
                    DefaultChannelPipeline.logger.warn("Failed to submit an exceptionCaught() event.", t);
                    DefaultChannelPipeline.logger.warn("The exceptionCaught() event that was failed to submit was:", cause);
                }
            }
        }
        return this;
    }
    
    private void invokeExceptionCaught(final Throwable cause) {
        try {
            this.handler().exceptionCaught(this, cause);
        }
        catch (Throwable t) {
            if (DefaultChannelPipeline.logger.isWarnEnabled()) {
                DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler's exceptionCaught() method while handling the following exception:", cause);
            }
        }
    }
    
    @Override
    public ChannelHandlerContext fireUserEventTriggered(final Object event) {
        if (event == null) {
            throw new NullPointerException("event");
        }
        final AbstractChannelHandlerContext next = this.findContextInbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeUserEventTriggered(event);
        }
        else {
            executor.execute(new OneTimeTask() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeUserEventTriggered(event);
                }
            });
        }
        return this;
    }
    
    private void invokeUserEventTriggered(final Object event) {
        try {
            ((ChannelInboundHandler)this.handler()).userEventTriggered(this, event);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelRead(final Object msg) {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        final AbstractChannelHandlerContext next = this.findContextInbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelRead(msg);
        }
        else {
            executor.execute(new OneTimeTask() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelRead(msg);
                }
            });
        }
        return this;
    }
    
    private void invokeChannelRead(final Object msg) {
        try {
            ((ChannelInboundHandler)this.handler()).channelRead(this, msg);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelReadComplete() {
        final AbstractChannelHandlerContext next = this.findContextInbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelReadComplete();
        }
        else {
            Runnable task = next.invokeChannelReadCompleteTask;
            if (task == null) {
                task = (next.invokeChannelReadCompleteTask = new Runnable() {
                    @Override
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeChannelReadComplete();
                    }
                });
            }
            executor.execute(task);
        }
        return this;
    }
    
    private void invokeChannelReadComplete() {
        try {
            ((ChannelInboundHandler)this.handler()).channelReadComplete(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelWritabilityChanged() {
        final AbstractChannelHandlerContext next = this.findContextInbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelWritabilityChanged();
        }
        else {
            Runnable task = next.invokeChannelWritableStateChangedTask;
            if (task == null) {
                task = (next.invokeChannelWritableStateChangedTask = new Runnable() {
                    @Override
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeChannelWritabilityChanged();
                    }
                });
            }
            executor.execute(task);
        }
        return this;
    }
    
    private void invokeChannelWritabilityChanged() {
        try {
            ((ChannelInboundHandler)this.handler()).channelWritabilityChanged(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }
    
    @Override
    public ChannelFuture bind(final SocketAddress localAddress) {
        return this.bind(localAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress) {
        return this.connect(remoteAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        return this.connect(remoteAddress, localAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture disconnect() {
        return this.disconnect(this.newPromise());
    }
    
    @Override
    public ChannelFuture close() {
        return this.close(this.newPromise());
    }
    
    @Override
    public ChannelFuture deregister() {
        return this.deregister(this.newPromise());
    }
    
    @Override
    public ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise) {
        if (localAddress == null) {
            throw new NullPointerException("localAddress");
        }
        if (!this.validatePromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeBind(localAddress, promise);
        }
        else {
            safeExecute(executor, new OneTimeTask() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeBind(localAddress, promise);
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeBind(final SocketAddress localAddress, final ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler()).bind(this, localAddress, promise);
        }
        catch (Throwable t) {
            notifyOutboundHandlerException(t, promise);
        }
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final ChannelPromise promise) {
        return this.connect(remoteAddress, null, promise);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        if (remoteAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        if (!this.validatePromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeConnect(remoteAddress, localAddress, promise);
        }
        else {
            safeExecute(executor, new OneTimeTask() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeConnect(remoteAddress, localAddress, promise);
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeConnect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler()).connect(this, remoteAddress, localAddress, promise);
        }
        catch (Throwable t) {
            notifyOutboundHandlerException(t, promise);
        }
    }
    
    @Override
    public ChannelFuture disconnect(final ChannelPromise promise) {
        if (!this.validatePromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            if (!this.channel().metadata().hasDisconnect()) {
                next.invokeClose(promise);
            }
            else {
                next.invokeDisconnect(promise);
            }
        }
        else {
            safeExecute(executor, new OneTimeTask() {
                @Override
                public void run() {
                    if (!AbstractChannelHandlerContext.this.channel().metadata().hasDisconnect()) {
                        AbstractChannelHandlerContext.this.invokeClose(promise);
                    }
                    else {
                        AbstractChannelHandlerContext.this.invokeDisconnect(promise);
                    }
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeDisconnect(final ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler()).disconnect(this, promise);
        }
        catch (Throwable t) {
            notifyOutboundHandlerException(t, promise);
        }
    }
    
    @Override
    public ChannelFuture close(final ChannelPromise promise) {
        if (!this.validatePromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeClose(promise);
        }
        else {
            safeExecute(executor, new OneTimeTask() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeClose(promise);
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeClose(final ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler()).close(this, promise);
        }
        catch (Throwable t) {
            notifyOutboundHandlerException(t, promise);
        }
    }
    
    @Override
    public ChannelFuture deregister(final ChannelPromise promise) {
        if (!this.validatePromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeDeregister(promise);
        }
        else {
            safeExecute(executor, new OneTimeTask() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeDeregister(promise);
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeDeregister(final ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler()).deregister(this, promise);
        }
        catch (Throwable t) {
            notifyOutboundHandlerException(t, promise);
        }
    }
    
    @Override
    public ChannelHandlerContext read() {
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeRead();
        }
        else {
            Runnable task = next.invokeReadTask;
            if (task == null) {
                task = (next.invokeReadTask = new Runnable() {
                    @Override
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeRead();
                    }
                });
            }
            executor.execute(task);
        }
        return this;
    }
    
    private void invokeRead() {
        try {
            ((ChannelOutboundHandler)this.handler()).read(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }
    
    @Override
    public ChannelFuture write(final Object msg) {
        return this.write(msg, this.newPromise());
    }
    
    @Override
    public ChannelFuture write(final Object msg, final ChannelPromise promise) {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        if (!this.validatePromise(promise, true)) {
            ReferenceCountUtil.release(msg);
            return promise;
        }
        this.write(msg, false, promise);
        return promise;
    }
    
    private void invokeWrite(final Object msg, final ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler()).write(this, msg, promise);
        }
        catch (Throwable t) {
            notifyOutboundHandlerException(t, promise);
        }
    }
    
    @Override
    public ChannelHandlerContext flush() {
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeFlush();
        }
        else {
            Runnable task = next.invokeFlushTask;
            if (task == null) {
                task = (next.invokeFlushTask = new Runnable() {
                    @Override
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeFlush();
                    }
                });
            }
            safeExecute(executor, task, this.channel.voidPromise(), null);
        }
        return this;
    }
    
    private void invokeFlush() {
        try {
            ((ChannelOutboundHandler)this.handler()).flush(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }
    
    @Override
    public ChannelFuture writeAndFlush(final Object msg, final ChannelPromise promise) {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        if (!this.validatePromise(promise, true)) {
            ReferenceCountUtil.release(msg);
            return promise;
        }
        this.write(msg, true, promise);
        return promise;
    }
    
    private void write(final Object msg, final boolean flush, final ChannelPromise promise) {
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeWrite(msg, promise);
            if (flush) {
                next.invokeFlush();
            }
        }
        else {
            final int size = this.channel.estimatorHandle().size(msg);
            if (size > 0) {
                final ChannelOutboundBuffer buffer = this.channel.unsafe().outboundBuffer();
                if (buffer != null) {
                    buffer.incrementPendingOutboundBytes(size);
                }
            }
            Runnable task;
            if (flush) {
                task = newInstance(next, msg, size, promise);
            }
            else {
                task = newInstance(next, msg, size, promise);
            }
            safeExecute(executor, task, promise, msg);
        }
    }
    
    @Override
    public ChannelFuture writeAndFlush(final Object msg) {
        return this.writeAndFlush(msg, this.newPromise());
    }
    
    private static void notifyOutboundHandlerException(final Throwable cause, final ChannelPromise promise) {
        if (promise instanceof VoidChannelPromise) {
            return;
        }
        if (!promise.tryFailure(cause) && DefaultChannelPipeline.logger.isWarnEnabled()) {
            DefaultChannelPipeline.logger.warn("Failed to fail the promise because it's done already: {}", promise, cause);
        }
    }
    
    private void notifyHandlerException(final Throwable cause) {
        if (inExceptionCaught(cause)) {
            if (DefaultChannelPipeline.logger.isWarnEnabled()) {
                DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler while handling an exceptionCaught event", cause);
            }
            return;
        }
        this.invokeExceptionCaught(cause);
    }
    
    private static boolean inExceptionCaught(Throwable cause) {
        do {
            final StackTraceElement[] trace = cause.getStackTrace();
            if (trace != null) {
                for (final StackTraceElement t : trace) {
                    if (t == null) {
                        break;
                    }
                    if ("exceptionCaught".equals(t.getMethodName())) {
                        return true;
                    }
                }
            }
            cause = cause.getCause();
        } while (cause != null);
        return false;
    }
    
    @Override
    public ChannelPromise newPromise() {
        return new DefaultChannelPromise(this.channel(), this.executor());
    }
    
    @Override
    public ChannelProgressivePromise newProgressivePromise() {
        return new DefaultChannelProgressivePromise(this.channel(), this.executor());
    }
    
    @Override
    public ChannelFuture newSucceededFuture() {
        ChannelFuture succeededFuture = this.succeededFuture;
        if (succeededFuture == null) {
            succeededFuture = (this.succeededFuture = new SucceededChannelFuture(this.channel(), this.executor()));
        }
        return succeededFuture;
    }
    
    @Override
    public ChannelFuture newFailedFuture(final Throwable cause) {
        return new FailedChannelFuture(this.channel(), this.executor(), cause);
    }
    
    private boolean validatePromise(final ChannelPromise promise, final boolean allowVoidPromise) {
        if (promise == null) {
            throw new NullPointerException("promise");
        }
        if (promise.isDone()) {
            if (promise.isCancelled()) {
                return false;
            }
            throw new IllegalArgumentException("promise already done: " + promise);
        }
        else {
            if (promise.channel() != this.channel()) {
                throw new IllegalArgumentException(String.format("promise.channel does not match: %s (expected: %s)", promise.channel(), this.channel()));
            }
            if (promise.getClass() == DefaultChannelPromise.class) {
                return true;
            }
            if (!allowVoidPromise && promise instanceof VoidChannelPromise) {
                throw new IllegalArgumentException(StringUtil.simpleClassName(VoidChannelPromise.class) + " not allowed for this operation");
            }
            if (promise instanceof AbstractChannel.CloseFuture) {
                throw new IllegalArgumentException(StringUtil.simpleClassName(AbstractChannel.CloseFuture.class) + " not allowed in a pipeline");
            }
            return true;
        }
    }
    
    private AbstractChannelHandlerContext findContextInbound() {
        AbstractChannelHandlerContext ctx = this;
        do {
            ctx = ctx.next;
        } while (!ctx.inbound);
        return ctx;
    }
    
    private AbstractChannelHandlerContext findContextOutbound() {
        AbstractChannelHandlerContext ctx = this;
        do {
            ctx = ctx.prev;
        } while (!ctx.outbound);
        return ctx;
    }
    
    @Override
    public ChannelPromise voidPromise() {
        return this.channel.voidPromise();
    }
    
    void setRemoved() {
        this.removed = true;
    }
    
    @Override
    public boolean isRemoved() {
        return this.removed;
    }
    
    private static void safeExecute(final EventExecutor executor, final Runnable runnable, final ChannelPromise promise, final Object msg) {
        try {
            executor.execute(runnable);
        }
        catch (Throwable cause) {
            try {
                promise.setFailure(cause);
            }
            finally {
                if (msg != null) {
                    ReferenceCountUtil.release(msg);
                }
            }
        }
    }
    
    abstract static class AbstractWriteTask extends RecyclableMpscLinkedQueueNode<Runnable> implements Runnable
    {
        private AbstractChannelHandlerContext ctx;
        private Object msg;
        private ChannelPromise promise;
        private int size;
        
        private AbstractWriteTask(final Recycler.Handle handle) {
            super(handle);
        }
        
        protected static void init(final AbstractWriteTask task, final AbstractChannelHandlerContext ctx, final Object msg, final int size, final ChannelPromise promise) {
            task.ctx = ctx;
            task.msg = msg;
            task.promise = promise;
            task.size = size;
        }
        
        @Override
        public final void run() {
            try {
                if (this.size > 0) {
                    final ChannelOutboundBuffer buffer = this.ctx.channel.unsafe().outboundBuffer();
                    if (buffer != null) {
                        buffer.decrementPendingOutboundBytes(this.size);
                    }
                }
                this.write(this.ctx, this.msg, this.promise);
            }
            finally {
                this.ctx = null;
                this.msg = null;
                this.promise = null;
            }
        }
        
        @Override
        public Runnable value() {
            return this;
        }
        
        protected void write(final AbstractChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
            ctx.invokeWrite(msg, promise);
        }
    }
    
    static final class WriteTask extends AbstractWriteTask implements SingleThreadEventLoop.NonWakeupRunnable
    {
        private static final Recycler<WriteTask> RECYCLER;
        
        private static WriteTask newInstance(final AbstractChannelHandlerContext ctx, final Object msg, final int size, final ChannelPromise promise) {
            final WriteTask task = WriteTask.RECYCLER.get();
            AbstractWriteTask.init(task, ctx, msg, size, promise);
            return task;
        }
        
        private WriteTask(final Recycler.Handle handle) {
            super(handle);
        }
        
        @Override
        protected void recycle(final Recycler.Handle handle) {
            WriteTask.RECYCLER.recycle(this, handle);
        }
        
        static {
            RECYCLER = new Recycler<WriteTask>() {
                @Override
                protected WriteTask newObject(final Handle handle) {
                    return new WriteTask(handle);
                }
            };
        }
    }
    
    static final class WriteAndFlushTask extends AbstractWriteTask
    {
        private static final Recycler<WriteAndFlushTask> RECYCLER;
        
        private static WriteAndFlushTask newInstance(final AbstractChannelHandlerContext ctx, final Object msg, final int size, final ChannelPromise promise) {
            final WriteAndFlushTask task = WriteAndFlushTask.RECYCLER.get();
            AbstractWriteTask.init(task, ctx, msg, size, promise);
            return task;
        }
        
        private WriteAndFlushTask(final Recycler.Handle handle) {
            super(handle);
        }
        
        public void write(final AbstractChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
            super.write(ctx, msg, promise);
            ctx.invokeFlush();
        }
        
        @Override
        protected void recycle(final Recycler.Handle handle) {
            WriteAndFlushTask.RECYCLER.recycle(this, handle);
        }
        
        static {
            RECYCLER = new Recycler<WriteAndFlushTask>() {
                @Override
                protected WriteAndFlushTask newObject(final Handle handle) {
                    return new WriteAndFlushTask(handle);
                }
            };
        }
    }
}
