// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import io.netty.util.internal.PlatformDependent;
import java.util.NoSuchElementException;
import java.util.concurrent.Future;
import io.netty.util.internal.StringUtil;
import java.util.IdentityHashMap;
import java.util.HashMap;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import java.util.Map;
import java.util.WeakHashMap;
import io.netty.util.internal.logging.InternalLogger;

final class DefaultChannelPipeline implements ChannelPipeline
{
    static final InternalLogger logger;
    private static final WeakHashMap<Class<?>, String>[] nameCaches;
    final AbstractChannel channel;
    final AbstractChannelHandlerContext head;
    final AbstractChannelHandlerContext tail;
    private final Map<String, AbstractChannelHandlerContext> name2ctx;
    final Map<EventExecutorGroup, EventExecutor> childExecutors;
    
    public DefaultChannelPipeline(final AbstractChannel channel) {
        this.name2ctx = new HashMap<String, AbstractChannelHandlerContext>(4);
        this.childExecutors = new IdentityHashMap<EventExecutorGroup, EventExecutor>();
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        this.channel = channel;
        this.tail = new TailContext(this);
        this.head = new HeadContext(this);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }
    
    @Override
    public Channel channel() {
        return this.channel;
    }
    
    @Override
    public ChannelPipeline addFirst(final String name, final ChannelHandler handler) {
        return this.addFirst(null, name, handler);
    }
    
    @Override
    public ChannelPipeline addFirst(final EventExecutorGroup group, final String name, final ChannelHandler handler) {
        synchronized (this) {
            this.checkDuplicateName(name);
            final AbstractChannelHandlerContext newCtx = new DefaultChannelHandlerContext(this, group, name, handler);
            this.addFirst0(name, newCtx);
        }
        return this;
    }
    
    private void addFirst0(final String name, final AbstractChannelHandlerContext newCtx) {
        checkMultiplicity(newCtx);
        final AbstractChannelHandlerContext nextCtx = this.head.next;
        newCtx.prev = this.head;
        newCtx.next = nextCtx;
        this.head.next = newCtx;
        nextCtx.prev = newCtx;
        this.name2ctx.put(name, newCtx);
        this.callHandlerAdded(newCtx);
    }
    
    @Override
    public ChannelPipeline addLast(final String name, final ChannelHandler handler) {
        return this.addLast(null, name, handler);
    }
    
    @Override
    public ChannelPipeline addLast(final EventExecutorGroup group, final String name, final ChannelHandler handler) {
        synchronized (this) {
            this.checkDuplicateName(name);
            final AbstractChannelHandlerContext newCtx = new DefaultChannelHandlerContext(this, group, name, handler);
            this.addLast0(name, newCtx);
        }
        return this;
    }
    
    private void addLast0(final String name, final AbstractChannelHandlerContext newCtx) {
        checkMultiplicity(newCtx);
        final AbstractChannelHandlerContext prev = this.tail.prev;
        newCtx.prev = prev;
        newCtx.next = this.tail;
        prev.next = newCtx;
        this.tail.prev = newCtx;
        this.name2ctx.put(name, newCtx);
        this.callHandlerAdded(newCtx);
    }
    
    @Override
    public ChannelPipeline addBefore(final String baseName, final String name, final ChannelHandler handler) {
        return this.addBefore(null, baseName, name, handler);
    }
    
    @Override
    public ChannelPipeline addBefore(final EventExecutorGroup group, final String baseName, final String name, final ChannelHandler handler) {
        synchronized (this) {
            final AbstractChannelHandlerContext ctx = this.getContextOrDie(baseName);
            this.checkDuplicateName(name);
            final AbstractChannelHandlerContext newCtx = new DefaultChannelHandlerContext(this, group, name, handler);
            this.addBefore0(name, ctx, newCtx);
        }
        return this;
    }
    
    private void addBefore0(final String name, final AbstractChannelHandlerContext ctx, final AbstractChannelHandlerContext newCtx) {
        checkMultiplicity(newCtx);
        newCtx.prev = ctx.prev;
        newCtx.next = ctx;
        ctx.prev.next = newCtx;
        ctx.prev = newCtx;
        this.name2ctx.put(name, newCtx);
        this.callHandlerAdded(newCtx);
    }
    
    @Override
    public ChannelPipeline addAfter(final String baseName, final String name, final ChannelHandler handler) {
        return this.addAfter(null, baseName, name, handler);
    }
    
    @Override
    public ChannelPipeline addAfter(final EventExecutorGroup group, final String baseName, final String name, final ChannelHandler handler) {
        synchronized (this) {
            final AbstractChannelHandlerContext ctx = this.getContextOrDie(baseName);
            this.checkDuplicateName(name);
            final AbstractChannelHandlerContext newCtx = new DefaultChannelHandlerContext(this, group, name, handler);
            this.addAfter0(name, ctx, newCtx);
        }
        return this;
    }
    
    private void addAfter0(final String name, final AbstractChannelHandlerContext ctx, final AbstractChannelHandlerContext newCtx) {
        this.checkDuplicateName(name);
        checkMultiplicity(newCtx);
        newCtx.prev = ctx;
        newCtx.next = ctx.next;
        ctx.next.prev = newCtx;
        ctx.next = newCtx;
        this.name2ctx.put(name, newCtx);
        this.callHandlerAdded(newCtx);
    }
    
    @Override
    public ChannelPipeline addFirst(final ChannelHandler... handlers) {
        return this.addFirst((EventExecutorGroup)null, handlers);
    }
    
    @Override
    public ChannelPipeline addFirst(final EventExecutorGroup executor, final ChannelHandler... handlers) {
        if (handlers == null) {
            throw new NullPointerException("handlers");
        }
        if (handlers.length == 0 || handlers[0] == null) {
            return this;
        }
        int size;
        for (size = 1; size < handlers.length && handlers[size] != null; ++size) {}
        for (int i = size - 1; i >= 0; --i) {
            final ChannelHandler h = handlers[i];
            this.addFirst(executor, this.generateName(h), h);
        }
        return this;
    }
    
    @Override
    public ChannelPipeline addLast(final ChannelHandler... handlers) {
        return this.addLast((EventExecutorGroup)null, handlers);
    }
    
    @Override
    public ChannelPipeline addLast(final EventExecutorGroup executor, final ChannelHandler... handlers) {
        if (handlers == null) {
            throw new NullPointerException("handlers");
        }
        for (final ChannelHandler h : handlers) {
            if (h == null) {
                break;
            }
            this.addLast(executor, this.generateName(h), h);
        }
        return this;
    }
    
    private String generateName(final ChannelHandler handler) {
        final WeakHashMap<Class<?>, String> cache = DefaultChannelPipeline.nameCaches[(int)(Thread.currentThread().getId() % DefaultChannelPipeline.nameCaches.length)];
        final Class<?> handlerType = handler.getClass();
        String name;
        synchronized (cache) {
            name = cache.get(handlerType);
            if (name == null) {
                name = generateName0(handlerType);
                cache.put(handlerType, name);
            }
        }
        synchronized (this) {
            if (this.name2ctx.containsKey(name)) {
                final String baseName = name.substring(0, name.length() - 1);
                int i = 1;
                String newName;
                while (true) {
                    newName = baseName + i;
                    if (!this.name2ctx.containsKey(newName)) {
                        break;
                    }
                    ++i;
                }
                name = newName;
            }
        }
        return name;
    }
    
    private static String generateName0(final Class<?> handlerType) {
        return StringUtil.simpleClassName(handlerType) + "#0";
    }
    
    @Override
    public ChannelPipeline remove(final ChannelHandler handler) {
        this.remove(this.getContextOrDie(handler));
        return this;
    }
    
    @Override
    public ChannelHandler remove(final String name) {
        return this.remove(this.getContextOrDie(name)).handler();
    }
    
    @Override
    public <T extends ChannelHandler> T remove(final Class<T> handlerType) {
        return (T)this.remove(this.getContextOrDie(handlerType)).handler();
    }
    
    private AbstractChannelHandlerContext remove(final AbstractChannelHandlerContext ctx) {
        assert ctx != this.head && ctx != this.tail;
        final Future<?> future;
        final AbstractChannelHandlerContext context;
        synchronized (this) {
            if (!ctx.channel().isRegistered() || ctx.executor().inEventLoop()) {
                this.remove0(ctx);
                return ctx;
            }
            future = ctx.executor().submit((Runnable)new Runnable() {
                @Override
                public void run() {
                    synchronized (DefaultChannelPipeline.this) {
                        DefaultChannelPipeline.this.remove0(ctx);
                    }
                }
            });
            context = ctx;
        }
        waitForFuture(future);
        return context;
    }
    
    void remove0(final AbstractChannelHandlerContext ctx) {
        final AbstractChannelHandlerContext prev = ctx.prev;
        final AbstractChannelHandlerContext next = ctx.next;
        prev.next = next;
        next.prev = prev;
        this.name2ctx.remove(ctx.name());
        this.callHandlerRemoved(ctx);
    }
    
    @Override
    public ChannelHandler removeFirst() {
        if (this.head.next == this.tail) {
            throw new NoSuchElementException();
        }
        return this.remove(this.head.next).handler();
    }
    
    @Override
    public ChannelHandler removeLast() {
        if (this.head.next == this.tail) {
            throw new NoSuchElementException();
        }
        return this.remove(this.tail.prev).handler();
    }
    
    @Override
    public ChannelPipeline replace(final ChannelHandler oldHandler, final String newName, final ChannelHandler newHandler) {
        this.replace(this.getContextOrDie(oldHandler), newName, newHandler);
        return this;
    }
    
    @Override
    public ChannelHandler replace(final String oldName, final String newName, final ChannelHandler newHandler) {
        return this.replace(this.getContextOrDie(oldName), newName, newHandler);
    }
    
    @Override
    public <T extends ChannelHandler> T replace(final Class<T> oldHandlerType, final String newName, final ChannelHandler newHandler) {
        return (T)this.replace(this.getContextOrDie(oldHandlerType), newName, newHandler);
    }
    
    private ChannelHandler replace(final AbstractChannelHandlerContext ctx, final String newName, final ChannelHandler newHandler) {
        assert ctx != this.head && ctx != this.tail;
        final Future<?> future;
        synchronized (this) {
            final boolean sameName = ctx.name().equals(newName);
            if (!sameName) {
                this.checkDuplicateName(newName);
            }
            final AbstractChannelHandlerContext newCtx = new DefaultChannelHandlerContext(this, ctx.executor, newName, newHandler);
            if (!newCtx.channel().isRegistered() || newCtx.executor().inEventLoop()) {
                this.replace0(ctx, newName, newCtx);
                return ctx.handler();
            }
            future = newCtx.executor().submit((Runnable)new Runnable() {
                @Override
                public void run() {
                    synchronized (DefaultChannelPipeline.this) {
                        DefaultChannelPipeline.this.replace0(ctx, newName, newCtx);
                    }
                }
            });
        }
        waitForFuture(future);
        return ctx.handler();
    }
    
    private void replace0(final AbstractChannelHandlerContext oldCtx, final String newName, final AbstractChannelHandlerContext newCtx) {
        checkMultiplicity(newCtx);
        final AbstractChannelHandlerContext prev = oldCtx.prev;
        final AbstractChannelHandlerContext next = oldCtx.next;
        newCtx.prev = prev;
        newCtx.next = next;
        prev.next = newCtx;
        next.prev = newCtx;
        if (!oldCtx.name().equals(newName)) {
            this.name2ctx.remove(oldCtx.name());
        }
        this.name2ctx.put(newName, newCtx);
        oldCtx.prev = newCtx;
        this.callHandlerAdded(oldCtx.next = newCtx);
        this.callHandlerRemoved(oldCtx);
    }
    
    private static void checkMultiplicity(final ChannelHandlerContext ctx) {
        final ChannelHandler handler = ctx.handler();
        if (handler instanceof ChannelHandlerAdapter) {
            final ChannelHandlerAdapter h = (ChannelHandlerAdapter)handler;
            if (!h.isSharable() && h.added) {
                throw new ChannelPipelineException(h.getClass().getName() + " is not a @Sharable handler, so can't be added or removed multiple times.");
            }
            h.added = true;
        }
    }
    
    private void callHandlerAdded(final ChannelHandlerContext ctx) {
        if (ctx.channel().isRegistered() && !ctx.executor().inEventLoop()) {
            ctx.executor().execute(new Runnable() {
                @Override
                public void run() {
                    DefaultChannelPipeline.this.callHandlerAdded0(ctx);
                }
            });
            return;
        }
        this.callHandlerAdded0(ctx);
    }
    
    private void callHandlerAdded0(final ChannelHandlerContext ctx) {
        try {
            ctx.handler().handlerAdded(ctx);
        }
        catch (Throwable t3) {
            boolean removed = false;
            try {
                this.remove((AbstractChannelHandlerContext)ctx);
                removed = true;
            }
            catch (Throwable t2) {
                if (DefaultChannelPipeline.logger.isWarnEnabled()) {
                    DefaultChannelPipeline.logger.warn("Failed to remove a handler: " + ctx.name(), t2);
                }
            }
            if (removed) {
                this.fireExceptionCaught(new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerAdded() has thrown an exception; removed.", t3));
            }
            else {
                this.fireExceptionCaught(new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerAdded() has thrown an exception; also failed to remove.", t3));
            }
        }
    }
    
    private void callHandlerRemoved(final AbstractChannelHandlerContext ctx) {
        if (ctx.channel().isRegistered() && !ctx.executor().inEventLoop()) {
            ctx.executor().execute(new Runnable() {
                @Override
                public void run() {
                    DefaultChannelPipeline.this.callHandlerRemoved0(ctx);
                }
            });
            return;
        }
        this.callHandlerRemoved0(ctx);
    }
    
    private void callHandlerRemoved0(final AbstractChannelHandlerContext ctx) {
        try {
            ctx.handler().handlerRemoved(ctx);
            ctx.setRemoved();
        }
        catch (Throwable t) {
            this.fireExceptionCaught(new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerRemoved() has thrown an exception.", t));
        }
    }
    
    private static void waitForFuture(final Future<?> future) {
        try {
            future.get();
        }
        catch (ExecutionException ex) {
            PlatformDependent.throwException(ex.getCause());
        }
        catch (InterruptedException ex2) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Override
    public ChannelHandler first() {
        final ChannelHandlerContext first = this.firstContext();
        if (first == null) {
            return null;
        }
        return first.handler();
    }
    
    @Override
    public ChannelHandlerContext firstContext() {
        final AbstractChannelHandlerContext first = this.head.next;
        if (first == this.tail) {
            return null;
        }
        return this.head.next;
    }
    
    @Override
    public ChannelHandler last() {
        final AbstractChannelHandlerContext last = this.tail.prev;
        if (last == this.head) {
            return null;
        }
        return last.handler();
    }
    
    @Override
    public ChannelHandlerContext lastContext() {
        final AbstractChannelHandlerContext last = this.tail.prev;
        if (last == this.head) {
            return null;
        }
        return last;
    }
    
    @Override
    public ChannelHandler get(final String name) {
        final ChannelHandlerContext ctx = this.context(name);
        if (ctx == null) {
            return null;
        }
        return ctx.handler();
    }
    
    @Override
    public <T extends ChannelHandler> T get(final Class<T> handlerType) {
        final ChannelHandlerContext ctx = this.context(handlerType);
        if (ctx == null) {
            return null;
        }
        return (T)ctx.handler();
    }
    
    @Override
    public ChannelHandlerContext context(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        synchronized (this) {
            return this.name2ctx.get(name);
        }
    }
    
    @Override
    public ChannelHandlerContext context(final ChannelHandler handler) {
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        for (AbstractChannelHandlerContext ctx = this.head.next; ctx != null; ctx = ctx.next) {
            if (ctx.handler() == handler) {
                return ctx;
            }
        }
        return null;
    }
    
    @Override
    public ChannelHandlerContext context(final Class<? extends ChannelHandler> handlerType) {
        if (handlerType == null) {
            throw new NullPointerException("handlerType");
        }
        for (AbstractChannelHandlerContext ctx = this.head.next; ctx != null; ctx = ctx.next) {
            if (handlerType.isAssignableFrom(ctx.handler().getClass())) {
                return ctx;
            }
        }
        return null;
    }
    
    @Override
    public List<String> names() {
        final List<String> list = new ArrayList<String>();
        for (AbstractChannelHandlerContext ctx = this.head.next; ctx != null; ctx = ctx.next) {
            list.add(ctx.name());
        }
        return list;
    }
    
    @Override
    public Map<String, ChannelHandler> toMap() {
        final Map<String, ChannelHandler> map = new LinkedHashMap<String, ChannelHandler>();
        for (AbstractChannelHandlerContext ctx = this.head.next; ctx != this.tail; ctx = ctx.next) {
            map.put(ctx.name(), ctx.handler());
        }
        return map;
    }
    
    @Override
    public Iterator<Map.Entry<String, ChannelHandler>> iterator() {
        return this.toMap().entrySet().iterator();
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.simpleClassName(this));
        buf.append('{');
        AbstractChannelHandlerContext ctx = this.head.next;
        while (true) {
            while (ctx != this.tail) {
                buf.append('(');
                buf.append(ctx.name());
                buf.append(" = ");
                buf.append(ctx.handler().getClass().getName());
                buf.append(')');
                ctx = ctx.next;
                if (ctx == this.tail) {
                    buf.append('}');
                    return buf.toString();
                }
                buf.append(", ");
            }
            continue;
        }
    }
    
    @Override
    public ChannelPipeline fireChannelRegistered() {
        this.head.fireChannelRegistered();
        return this;
    }
    
    @Override
    public ChannelPipeline fireChannelUnregistered() {
        this.head.fireChannelUnregistered();
        if (!this.channel.isOpen()) {
            this.teardownAll();
        }
        return this;
    }
    
    private void teardownAll() {
        this.tail.prev.teardown();
    }
    
    @Override
    public ChannelPipeline fireChannelActive() {
        this.head.fireChannelActive();
        if (this.channel.config().isAutoRead()) {
            this.channel.read();
        }
        return this;
    }
    
    @Override
    public ChannelPipeline fireChannelInactive() {
        this.head.fireChannelInactive();
        return this;
    }
    
    @Override
    public ChannelPipeline fireExceptionCaught(final Throwable cause) {
        this.head.fireExceptionCaught(cause);
        return this;
    }
    
    @Override
    public ChannelPipeline fireUserEventTriggered(final Object event) {
        this.head.fireUserEventTriggered(event);
        return this;
    }
    
    @Override
    public ChannelPipeline fireChannelRead(final Object msg) {
        this.head.fireChannelRead(msg);
        return this;
    }
    
    @Override
    public ChannelPipeline fireChannelReadComplete() {
        this.head.fireChannelReadComplete();
        if (this.channel.config().isAutoRead()) {
            this.read();
        }
        return this;
    }
    
    @Override
    public ChannelPipeline fireChannelWritabilityChanged() {
        this.head.fireChannelWritabilityChanged();
        return this;
    }
    
    @Override
    public ChannelFuture bind(final SocketAddress localAddress) {
        return this.tail.bind(localAddress);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress) {
        return this.tail.connect(remoteAddress);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        return this.tail.connect(remoteAddress, localAddress);
    }
    
    @Override
    public ChannelFuture disconnect() {
        return this.tail.disconnect();
    }
    
    @Override
    public ChannelFuture close() {
        return this.tail.close();
    }
    
    @Override
    public ChannelFuture deregister() {
        return this.tail.deregister();
    }
    
    @Override
    public ChannelPipeline flush() {
        this.tail.flush();
        return this;
    }
    
    @Override
    public ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise) {
        return this.tail.bind(localAddress, promise);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final ChannelPromise promise) {
        return this.tail.connect(remoteAddress, promise);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        return this.tail.connect(remoteAddress, localAddress, promise);
    }
    
    @Override
    public ChannelFuture disconnect(final ChannelPromise promise) {
        return this.tail.disconnect(promise);
    }
    
    @Override
    public ChannelFuture close(final ChannelPromise promise) {
        return this.tail.close(promise);
    }
    
    @Override
    public ChannelFuture deregister(final ChannelPromise promise) {
        return this.tail.deregister(promise);
    }
    
    @Override
    public ChannelPipeline read() {
        this.tail.read();
        return this;
    }
    
    @Override
    public ChannelFuture write(final Object msg) {
        return this.tail.write(msg);
    }
    
    @Override
    public ChannelFuture write(final Object msg, final ChannelPromise promise) {
        return this.tail.write(msg, promise);
    }
    
    @Override
    public ChannelFuture writeAndFlush(final Object msg, final ChannelPromise promise) {
        return this.tail.writeAndFlush(msg, promise);
    }
    
    @Override
    public ChannelFuture writeAndFlush(final Object msg) {
        return this.tail.writeAndFlush(msg);
    }
    
    private void checkDuplicateName(final String name) {
        if (this.name2ctx.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate handler name: " + name);
        }
    }
    
    private AbstractChannelHandlerContext getContextOrDie(final String name) {
        final AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)this.context(name);
        if (ctx == null) {
            throw new NoSuchElementException(name);
        }
        return ctx;
    }
    
    private AbstractChannelHandlerContext getContextOrDie(final ChannelHandler handler) {
        final AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)this.context(handler);
        if (ctx == null) {
            throw new NoSuchElementException(handler.getClass().getName());
        }
        return ctx;
    }
    
    private AbstractChannelHandlerContext getContextOrDie(final Class<? extends ChannelHandler> handlerType) {
        final AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)this.context(handlerType);
        if (ctx == null) {
            throw new NoSuchElementException(handlerType.getName());
        }
        return ctx;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DefaultChannelPipeline.class);
        nameCaches = new WeakHashMap[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < DefaultChannelPipeline.nameCaches.length; ++i) {
            DefaultChannelPipeline.nameCaches[i] = new WeakHashMap<Class<?>, String>();
        }
    }
    
    static final class TailContext extends AbstractChannelHandlerContext implements ChannelInboundHandler
    {
        private static final String TAIL_NAME;
        
        TailContext(final DefaultChannelPipeline pipeline) {
            super(pipeline, null, TailContext.TAIL_NAME, true, false);
        }
        
        @Override
        public ChannelHandler handler() {
            return this;
        }
        
        @Override
        public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        }
        
        @Override
        public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        }
        
        @Override
        public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        }
        
        @Override
        public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        }
        
        @Override
        public void channelWritabilityChanged(final ChannelHandlerContext ctx) throws Exception {
        }
        
        @Override
        public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        }
        
        @Override
        public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        }
        
        @Override
        public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        }
        
        @Override
        public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
            DefaultChannelPipeline.logger.warn("An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.", cause);
        }
        
        @Override
        public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
            try {
                DefaultChannelPipeline.logger.debug("Discarded inbound message {} that reached at the tail of the pipeline. Please check your pipeline configuration.", msg);
            }
            finally {
                ReferenceCountUtil.release(msg);
            }
        }
        
        @Override
        public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        }
        
        static {
            TAIL_NAME = generateName0(TailContext.class);
        }
    }
    
    static final class HeadContext extends AbstractChannelHandlerContext implements ChannelOutboundHandler
    {
        private static final String HEAD_NAME;
        protected final Channel.Unsafe unsafe;
        
        HeadContext(final DefaultChannelPipeline pipeline) {
            super(pipeline, null, HeadContext.HEAD_NAME, false, true);
            this.unsafe = pipeline.channel().unsafe();
        }
        
        @Override
        public ChannelHandler handler() {
            return this;
        }
        
        @Override
        public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        }
        
        @Override
        public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        }
        
        @Override
        public void bind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
            this.unsafe.bind(localAddress, promise);
        }
        
        @Override
        public void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
            this.unsafe.connect(remoteAddress, localAddress, promise);
        }
        
        @Override
        public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
            this.unsafe.disconnect(promise);
        }
        
        @Override
        public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
            this.unsafe.close(promise);
        }
        
        @Override
        public void deregister(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
            this.unsafe.deregister(promise);
        }
        
        @Override
        public void read(final ChannelHandlerContext ctx) {
            this.unsafe.beginRead();
        }
        
        @Override
        public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
            this.unsafe.write(msg, promise);
        }
        
        @Override
        public void flush(final ChannelHandlerContext ctx) throws Exception {
            this.unsafe.flush();
        }
        
        @Override
        public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
            ctx.fireExceptionCaught(cause);
        }
        
        static {
            HEAD_NAME = generateName0(HeadContext.class);
        }
    }
}
