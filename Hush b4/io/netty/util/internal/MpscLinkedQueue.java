// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Queue;

final class MpscLinkedQueue<E> extends MpscLinkedQueueTailRef<E> implements Queue<E>
{
    private static final long serialVersionUID = -1878402552271506449L;
    long p00;
    long p01;
    long p02;
    long p03;
    long p04;
    long p05;
    long p06;
    long p07;
    long p30;
    long p31;
    long p32;
    long p33;
    long p34;
    long p35;
    long p36;
    long p37;
    
    MpscLinkedQueue() {
        final MpscLinkedQueueNode<E> tombstone = new DefaultNode<E>(null);
        this.setHeadRef(tombstone);
        this.setTailRef(tombstone);
    }
    
    private MpscLinkedQueueNode<E> peekNode() {
        while (true) {
            final MpscLinkedQueueNode<E> head = this.headRef();
            final MpscLinkedQueueNode<E> next = head.next();
            if (next != null) {
                return next;
            }
            if (head == this.tailRef()) {
                return null;
            }
        }
    }
    
    @Override
    public boolean offer(final E value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        MpscLinkedQueueNode<E> newTail;
        if (value instanceof MpscLinkedQueueNode) {
            newTail = (MpscLinkedQueueNode<E>)value;
            newTail.setNext(null);
        }
        else {
            newTail = new DefaultNode<E>(value);
        }
        final MpscLinkedQueueNode<E> oldTail = this.getAndSetTailRef(newTail);
        oldTail.setNext(newTail);
        return true;
    }
    
    @Override
    public E poll() {
        final MpscLinkedQueueNode<E> next = this.peekNode();
        if (next == null) {
            return null;
        }
        final MpscLinkedQueueNode<E> oldHead = this.headRef();
        this.lazySetHeadRef(next);
        oldHead.unlink();
        return next.clearMaybe();
    }
    
    @Override
    public E peek() {
        final MpscLinkedQueueNode<E> next = this.peekNode();
        if (next == null) {
            return null;
        }
        return next.value();
    }
    
    @Override
    public int size() {
        int count = 0;
        for (MpscLinkedQueueNode<E> n = this.peekNode(); n != null; n = n.next()) {
            ++count;
        }
        return count;
    }
    
    @Override
    public boolean isEmpty() {
        return this.peekNode() == null;
    }
    
    @Override
    public boolean contains(final Object o) {
        for (MpscLinkedQueueNode<E> n = this.peekNode(); n != null; n = n.next()) {
            if (n.value() == o) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private MpscLinkedQueueNode<E> node = MpscLinkedQueue.this.peekNode();
            
            @Override
            public boolean hasNext() {
                return this.node != null;
            }
            
            @Override
            public E next() {
                final MpscLinkedQueueNode<E> node = this.node;
                if (node == null) {
                    throw new NoSuchElementException();
                }
                final E value = node.value();
                this.node = node.next();
                return value;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    @Override
    public boolean add(final E e) {
        if (this.offer(e)) {
            return true;
        }
        throw new IllegalStateException("queue full");
    }
    
    @Override
    public E remove() {
        final E e = this.poll();
        if (e != null) {
            return e;
        }
        throw new NoSuchElementException();
    }
    
    @Override
    public E element() {
        final E e = this.peek();
        if (e != null) {
            return e;
        }
        throw new NoSuchElementException();
    }
    
    @Override
    public Object[] toArray() {
        final Object[] array = new Object[this.size()];
        final Iterator<E> it = this.iterator();
        for (int i = 0; i < array.length; ++i) {
            if (!it.hasNext()) {
                return Arrays.copyOf(array, i);
            }
            array[i] = it.next();
        }
        return array;
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        final int size = this.size();
        T[] array;
        if (a.length >= size) {
            array = a;
        }
        else {
            array = (T[])Array.newInstance(a.getClass().getComponentType(), size);
        }
        final Iterator<E> it = this.iterator();
        int i = 0;
        while (i < array.length) {
            if (it.hasNext()) {
                array[i] = (T)it.next();
                ++i;
            }
            else {
                if (a == array) {
                    array[i] = null;
                    return array;
                }
                if (a.length < i) {
                    return Arrays.copyOf(array, i);
                }
                System.arraycopy(array, 0, a, 0, i);
                if (a.length > i) {
                    a[i] = null;
                }
                return a;
            }
        }
        return array;
    }
    
    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        for (final Object e : c) {
            if (!this.contains(e)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("c");
        }
        if (c == this) {
            throw new IllegalArgumentException("c == this");
        }
        boolean modified = false;
        for (final E e : c) {
            this.add(e);
            modified = true;
        }
        return modified;
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        while (this.poll() != null) {}
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        for (final E e : this) {
            out.writeObject(e);
        }
        out.writeObject(null);
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final MpscLinkedQueueNode<E> tombstone = new DefaultNode<E>(null);
        this.setHeadRef(tombstone);
        this.setTailRef(tombstone);
        while (true) {
            final E e = (E)in.readObject();
            if (e == null) {
                break;
            }
            this.add(e);
        }
    }
    
    private static final class DefaultNode<T> extends MpscLinkedQueueNode<T>
    {
        private T value;
        
        DefaultNode(final T value) {
            this.value = value;
        }
        
        @Override
        public T value() {
            return this.value;
        }
        
        @Override
        protected T clearMaybe() {
            final T value = this.value;
            this.value = null;
            return value;
        }
    }
}
