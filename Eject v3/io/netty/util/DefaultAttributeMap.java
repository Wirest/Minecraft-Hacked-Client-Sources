package io.netty.util;

import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class DefaultAttributeMap
        implements AttributeMap {
    private static final AtomicReferenceFieldUpdater<DefaultAttributeMap, AtomicReferenceArray> updater;
    private static final int BUCKET_SIZE = 4;
    private static final int MASK = 3;

    static {
        AtomicReferenceFieldUpdater localAtomicReferenceFieldUpdater = PlatformDependent.newAtomicReferenceFieldUpdater(DefaultAttributeMap.class, "attributes");
        if (localAtomicReferenceFieldUpdater == null) {
            localAtomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(DefaultAttributeMap.class, AtomicReferenceArray.class, "attributes");
        }
        updater = localAtomicReferenceFieldUpdater;
    }

    private volatile AtomicReferenceArray<DefaultAttribute<?>> attributes;

    private static int index(AttributeKey<?> paramAttributeKey) {
        return paramAttributeKey.id() >> 3;
    }

    public <T> Attribute<T> attr(AttributeKey<T> paramAttributeKey) {
        if (paramAttributeKey == null) {
            throw new NullPointerException("key");
        }
        AtomicReferenceArray localAtomicReferenceArray = this.attributes;
        if (localAtomicReferenceArray == null) {
            localAtomicReferenceArray = new AtomicReferenceArray(4);
            if (!updater.compareAndSet(this, null, localAtomicReferenceArray)) {
                localAtomicReferenceArray = this.attributes;
            }
        }
        int i = index(paramAttributeKey);
        DefaultAttribute localDefaultAttribute1 = (DefaultAttribute) localAtomicReferenceArray.get(i);
        if (localDefaultAttribute1 == null) {
            localDefaultAttribute1 = new DefaultAttribute(paramAttributeKey);
            if (localAtomicReferenceArray.compareAndSet(i, null, localDefaultAttribute1)) {
                return localDefaultAttribute1;
            }
            localDefaultAttribute1 = (DefaultAttribute) localAtomicReferenceArray.get(i);
        }
        synchronized (localDefaultAttribute1) {
            Object localObject1 = localDefaultAttribute1;
            if ((!((DefaultAttribute) localObject1).removed) && (((DefaultAttribute) localObject1).key == paramAttributeKey)) {
                return (Attribute<T>) localObject1;
            }
            DefaultAttribute localDefaultAttribute2 = ((DefaultAttribute) localObject1).next;
            if (localDefaultAttribute2 == null) {
                DefaultAttribute localDefaultAttribute3 = new DefaultAttribute(localDefaultAttribute1, paramAttributeKey);
                ((DefaultAttribute) localObject1).next = localDefaultAttribute3;
                localDefaultAttribute3.prev = ((DefaultAttribute) localObject1);
                return localDefaultAttribute3;
            }
            localObject1 = localDefaultAttribute2;
        }
    }

    private static final class DefaultAttribute<T>
            extends AtomicReference<T>
            implements Attribute<T> {
        private static final long serialVersionUID = -2661411462200283011L;
        private final DefaultAttribute<?> head = this;
        private final AttributeKey<T> key;
        private DefaultAttribute<?> prev;
        private DefaultAttribute<?> next;
        private volatile boolean removed;

        DefaultAttribute(DefaultAttribute<?> paramDefaultAttribute, AttributeKey<T> paramAttributeKey) {
            this.key = paramAttributeKey;
        }

        DefaultAttribute(AttributeKey<T> paramAttributeKey) {
            this.key = paramAttributeKey;
        }

        public AttributeKey<T> key() {
            return this.key;
        }

        public T setIfAbsent(T paramT) {
            while (!compareAndSet(null, paramT)) {
                Object localObject = get();
                if (localObject != null) {
                    return (T) localObject;
                }
            }
            return null;
        }

        public T getAndRemove() {
            this.removed = true;
            Object localObject = getAndSet(null);
            remove0();
            return (T) localObject;
        }

        public void remove() {
            this.removed = true;
            set(null);
            remove0();
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




