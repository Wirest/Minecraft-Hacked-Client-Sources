// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util;

import java.util.concurrent.atomic.AtomicReference;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class DefaultAttributeMap implements AttributeMap
{
    private static final AtomicReferenceFieldUpdater<DefaultAttributeMap, AtomicReferenceArray> updater;
    private static final int BUCKET_SIZE = 4;
    private static final int MASK = 3;
    private volatile AtomicReferenceArray<DefaultAttribute<?>> attributes;
    
    @Override
    public <T> Attribute<T> attr(final AttributeKey<T> key) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        AtomicReferenceArray<DefaultAttribute<?>> attributes = this.attributes;
        if (attributes == null) {
            attributes = new AtomicReferenceArray<DefaultAttribute<?>>(4);
            if (!DefaultAttributeMap.updater.compareAndSet(this, null, attributes)) {
                attributes = this.attributes;
            }
        }
        final int i = index(key);
        DefaultAttribute<?> head = attributes.get(i);
        if (head == null) {
            head = new DefaultAttribute<Object>(key);
            if (attributes.compareAndSet(i, null, head)) {
                return (Attribute<T>)head;
            }
            head = attributes.get(i);
        }
        synchronized (head) {
            DefaultAttribute<?> curr = head;
            while (((DefaultAttribute<Object>)head).removed || ((DefaultAttribute<Object>)head).key != key) {
                final DefaultAttribute<?> next = ((DefaultAttribute<Object>)head).next;
                if (next == null) {
                    final DefaultAttribute<T> attr = new DefaultAttribute<T>(head, key);
                    ((DefaultAttribute<Object>)head).next = attr;
                    ((DefaultAttribute<Object>)attr).prev = head;
                    return attr;
                }
                curr = next;
            }
            return (Attribute<T>)head;
        }
    }
    
    private static int index(final AttributeKey<?> key) {
        return key.id() & 0x3;
    }
    
    static {
        AtomicReferenceFieldUpdater<DefaultAttributeMap, AtomicReferenceArray> referenceFieldUpdater = (AtomicReferenceFieldUpdater<DefaultAttributeMap, AtomicReferenceArray>)PlatformDependent.newAtomicReferenceFieldUpdater(DefaultAttributeMap.class, "attributes");
        if (referenceFieldUpdater == null) {
            referenceFieldUpdater = (AtomicReferenceFieldUpdater<DefaultAttributeMap, AtomicReferenceArray>)AtomicReferenceFieldUpdater.newUpdater(DefaultAttributeMap.class, AtomicReferenceArray.class, "attributes");
        }
        updater = referenceFieldUpdater;
    }
    
    private static final class DefaultAttribute<T> extends AtomicReference<T> implements Attribute<T>
    {
        private static final long serialVersionUID = -2661411462200283011L;
        private final DefaultAttribute<?> head;
        private final AttributeKey<T> key;
        private DefaultAttribute<?> prev;
        private DefaultAttribute<?> next;
        private volatile boolean removed;
        
        DefaultAttribute(final DefaultAttribute<?> head, final AttributeKey<T> key) {
            this.head = head;
            this.key = key;
        }
        
        DefaultAttribute(final AttributeKey<T> key) {
            this.head = this;
            this.key = key;
        }
        
        @Override
        public AttributeKey<T> key() {
            return this.key;
        }
        
        @Override
        public T setIfAbsent(final T value) {
            while (!this.compareAndSet(null, value)) {
                final T old = this.get();
                if (old != null) {
                    return old;
                }
            }
            return null;
        }
        
        @Override
        public T getAndRemove() {
            this.removed = true;
            final T oldValue = this.getAndSet(null);
            this.remove0();
            return oldValue;
        }
        
        @Override
        public void remove() {
            this.removed = true;
            this.set(null);
            this.remove0();
        }
        
        private void remove0() {
            synchronized (this.head) {
                if (this.prev != null) {
                    this.prev.next = this.next;
                    if (this.next != null) {
                        this.next.prev = this.prev;
                    }
                }
            }
        }
    }
}
