// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util;

import java.util.Arrays;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Map;
import io.netty.util.concurrent.FastThreadLocal;
import java.util.concurrent.atomic.AtomicInteger;
import io.netty.util.internal.logging.InternalLogger;

public abstract class Recycler<T>
{
    private static final InternalLogger logger;
    private static final AtomicInteger ID_GENERATOR;
    private static final int OWN_THREAD_ID;
    private static final int DEFAULT_MAX_CAPACITY;
    private static final int INITIAL_CAPACITY;
    private final int maxCapacity;
    private final FastThreadLocal<Stack<T>> threadLocal;
    private static final FastThreadLocal<Map<Stack<?>, WeakOrderQueue>> DELAYED_RECYCLED;
    
    protected Recycler() {
        this(Recycler.DEFAULT_MAX_CAPACITY);
    }
    
    protected Recycler(final int maxCapacity) {
        this.threadLocal = new FastThreadLocal<Stack<T>>() {
            @Override
            protected Stack<T> initialValue() {
                return new Stack<T>(Recycler.this, Thread.currentThread(), Recycler.this.maxCapacity);
            }
        };
        this.maxCapacity = Math.max(0, maxCapacity);
    }
    
    public final T get() {
        final Stack<T> stack = this.threadLocal.get();
        DefaultHandle handle = stack.pop();
        if (handle == null) {
            handle = stack.newHandle();
            handle.value = this.newObject(handle);
        }
        return (T)handle.value;
    }
    
    public final boolean recycle(final T o, final Handle handle) {
        final DefaultHandle h = (DefaultHandle)handle;
        if (h.stack.parent != this) {
            return false;
        }
        if (o != h.value) {
            throw new IllegalArgumentException("o does not belong to handle");
        }
        h.recycle();
        return true;
    }
    
    protected abstract T newObject(final Handle p0);
    
    static {
        logger = InternalLoggerFactory.getInstance(Recycler.class);
        ID_GENERATOR = new AtomicInteger(Integer.MIN_VALUE);
        OWN_THREAD_ID = Recycler.ID_GENERATOR.getAndIncrement();
        int maxCapacity = SystemPropertyUtil.getInt("io.netty.recycler.maxCapacity.default", 0);
        if (maxCapacity <= 0) {
            maxCapacity = 262144;
        }
        DEFAULT_MAX_CAPACITY = maxCapacity;
        if (Recycler.logger.isDebugEnabled()) {
            Recycler.logger.debug("-Dio.netty.recycler.maxCapacity.default: {}", (Object)Recycler.DEFAULT_MAX_CAPACITY);
        }
        INITIAL_CAPACITY = Math.min(Recycler.DEFAULT_MAX_CAPACITY, 256);
        DELAYED_RECYCLED = new FastThreadLocal<Map<Stack<?>, WeakOrderQueue>>() {
            @Override
            protected Map<Stack<?>, WeakOrderQueue> initialValue() {
                return new WeakHashMap<Stack<?>, WeakOrderQueue>();
            }
        };
    }
    
    static final class DefaultHandle implements Handle
    {
        private int lastRecycledId;
        private int recycleId;
        private Stack<?> stack;
        private Object value;
        
        DefaultHandle(final Stack<?> stack) {
            this.stack = stack;
        }
        
        public void recycle() {
            final Thread thread = Thread.currentThread();
            if (thread == this.stack.thread) {
                this.stack.push(this);
                return;
            }
            final Map<Stack<?>, WeakOrderQueue> delayedRecycled = Recycler.DELAYED_RECYCLED.get();
            WeakOrderQueue queue = delayedRecycled.get(this.stack);
            if (queue == null) {
                delayedRecycled.put(this.stack, queue = new WeakOrderQueue(this.stack, thread));
            }
            queue.add(this);
        }
    }
    
    private static final class WeakOrderQueue
    {
        private static final int LINK_CAPACITY = 16;
        private Link head;
        private Link tail;
        private WeakOrderQueue next;
        private final WeakReference<Thread> owner;
        private final int id;
        
        WeakOrderQueue(final Stack<?> stack, final Thread thread) {
            this.id = Recycler.ID_GENERATOR.getAndIncrement();
            final Link link = new Link();
            this.tail = link;
            this.head = link;
            this.owner = new WeakReference<Thread>(thread);
            synchronized (stack) {
                this.next = ((Stack<Object>)stack).head;
                ((Stack<Object>)stack).head = this;
            }
        }
        
        void add(final DefaultHandle handle) {
            handle.lastRecycledId = this.id;
            Link tail = this.tail;
            int writeIndex;
            if ((writeIndex = tail.get()) == 16) {
                tail = (this.tail = (tail.next = new Link()));
                writeIndex = tail.get();
            }
            (tail.elements[writeIndex] = handle).stack = null;
            tail.lazySet(writeIndex + 1);
        }
        
        boolean hasFinalData() {
            return this.tail.readIndex != this.tail.get();
        }
        
        boolean transfer(final Stack<?> to) {
            Link head = this.head;
            if (head == null) {
                return false;
            }
            if (head.readIndex == 16) {
                if (head.next == null) {
                    return false;
                }
                head = (this.head = head.next);
            }
            int start = head.readIndex;
            final int end = head.get();
            if (start == end) {
                return false;
            }
            final int count = end - start;
            if (((Stack<Object>)to).size + count > ((Stack<Object>)to).elements.length) {
                ((Stack<Object>)to).elements = Arrays.copyOf(((Stack<Object>)to).elements, (((Stack<Object>)to).size + count) * 2);
            }
            final DefaultHandle[] src = head.elements;
            final DefaultHandle[] trg = ((Stack<Object>)to).elements;
            int size = ((Stack<Object>)to).size;
            while (start < end) {
                final DefaultHandle element = src[start];
                if (element.recycleId == 0) {
                    element.recycleId = element.lastRecycledId;
                }
                else if (element.recycleId != element.lastRecycledId) {
                    throw new IllegalStateException("recycled already");
                }
                element.stack = to;
                trg[size++] = element;
                src[start++] = null;
            }
            ((Stack<Object>)to).size = size;
            if (end == 16 && head.next != null) {
                this.head = head.next;
            }
            head.readIndex = end;
            return true;
        }
        
        private static final class Link extends AtomicInteger
        {
            private final DefaultHandle[] elements;
            private int readIndex;
            private Link next;
            
            private Link() {
                this.elements = new DefaultHandle[16];
            }
        }
    }
    
    static final class Stack<T>
    {
        final Recycler<T> parent;
        final Thread thread;
        private DefaultHandle[] elements;
        private final int maxCapacity;
        private int size;
        private volatile WeakOrderQueue head;
        private WeakOrderQueue cursor;
        private WeakOrderQueue prev;
        
        Stack(final Recycler<T> parent, final Thread thread, final int maxCapacity) {
            this.parent = parent;
            this.thread = thread;
            this.maxCapacity = maxCapacity;
            this.elements = new DefaultHandle[Recycler.INITIAL_CAPACITY];
        }
        
        DefaultHandle pop() {
            int size = this.size;
            if (size == 0) {
                if (!this.scavenge()) {
                    return null;
                }
                size = this.size;
            }
            --size;
            final DefaultHandle ret = this.elements[size];
            if (ret.lastRecycledId != ret.recycleId) {
                throw new IllegalStateException("recycled multiple times");
            }
            ret.recycleId = 0;
            ret.lastRecycledId = 0;
            this.size = size;
            return ret;
        }
        
        boolean scavenge() {
            if (this.scavengeSome()) {
                return true;
            }
            this.prev = null;
            this.cursor = this.head;
            return false;
        }
        
        boolean scavengeSome() {
            boolean success = false;
            WeakOrderQueue cursor = this.cursor;
            WeakOrderQueue prev = this.prev;
            while (cursor != null) {
                if (cursor.transfer(this)) {
                    success = true;
                    break;
                }
                final WeakOrderQueue next = cursor.next;
                if (cursor.owner.get() == null) {
                    if (cursor.hasFinalData()) {
                        while (cursor.transfer(this)) {}
                    }
                    if (prev != null) {
                        prev.next = next;
                    }
                }
                else {
                    prev = cursor;
                }
                cursor = next;
            }
            this.prev = prev;
            this.cursor = cursor;
            return success;
        }
        
        void push(final DefaultHandle item) {
            if ((item.recycleId | item.lastRecycledId) != 0x0) {
                throw new IllegalStateException("recycled already");
            }
            item.recycleId = (item.lastRecycledId = Recycler.OWN_THREAD_ID);
            final int size = this.size;
            if (size == this.elements.length) {
                if (size == this.maxCapacity) {
                    return;
                }
                this.elements = Arrays.copyOf(this.elements, size << 1);
            }
            this.elements[size] = item;
            this.size = size + 1;
        }
        
        DefaultHandle newHandle() {
            return new DefaultHandle(this);
        }
    }
    
    public interface Handle
    {
    }
}
