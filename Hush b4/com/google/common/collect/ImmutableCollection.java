// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Collection;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.AbstractCollection;

@GwtCompatible(emulated = true)
public abstract class ImmutableCollection<E> extends AbstractCollection<E> implements Serializable
{
    private transient ImmutableList<E> asList;
    
    ImmutableCollection() {
    }
    
    @Override
    public abstract UnmodifiableIterator<E> iterator();
    
    @Override
    public final Object[] toArray() {
        final int size = this.size();
        if (size == 0) {
            return ObjectArrays.EMPTY_ARRAY;
        }
        final Object[] result = new Object[this.size()];
        this.copyIntoArray(result, 0);
        return result;
    }
    
    @Override
    public final <T> T[] toArray(T[] other) {
        Preconditions.checkNotNull(other);
        final int size = this.size();
        if (other.length < size) {
            other = ObjectArrays.newArray(other, size);
        }
        else if (other.length > size) {
            other[size] = null;
        }
        this.copyIntoArray(other, 0);
        return other;
    }
    
    @Override
    public boolean contains(@Nullable final Object object) {
        return object != null && super.contains(object);
    }
    
    @Deprecated
    @Override
    public final boolean add(final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final boolean remove(final Object object) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final boolean addAll(final Collection<? extends E> newElements) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final boolean removeAll(final Collection<?> oldElements) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final boolean retainAll(final Collection<?> elementsToKeep) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }
    
    public ImmutableList<E> asList() {
        final ImmutableList<E> list = this.asList;
        return (list == null) ? (this.asList = this.createAsList()) : list;
    }
    
    ImmutableList<E> createAsList() {
        switch (this.size()) {
            case 0: {
                return ImmutableList.of();
            }
            case 1: {
                return ImmutableList.of(this.iterator().next());
            }
            default: {
                return new RegularImmutableAsList<E>(this, this.toArray());
            }
        }
    }
    
    abstract boolean isPartialView();
    
    int copyIntoArray(final Object[] dst, int offset) {
        for (final E e : this) {
            dst[offset++] = e;
        }
        return offset;
    }
    
    Object writeReplace() {
        return new ImmutableList.SerializedForm(this.toArray());
    }
    
    public abstract static class Builder<E>
    {
        static final int DEFAULT_INITIAL_CAPACITY = 4;
        
        static int expandedCapacity(final int oldCapacity, final int minCapacity) {
            if (minCapacity < 0) {
                throw new AssertionError((Object)"cannot store more than MAX_VALUE elements");
            }
            int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
            if (newCapacity < minCapacity) {
                newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
            }
            if (newCapacity < 0) {
                newCapacity = Integer.MAX_VALUE;
            }
            return newCapacity;
        }
        
        Builder() {
        }
        
        public abstract Builder<E> add(final E p0);
        
        public Builder<E> add(final E... elements) {
            for (final E element : elements) {
                this.add(element);
            }
            return this;
        }
        
        public Builder<E> addAll(final Iterable<? extends E> elements) {
            for (final E element : elements) {
                this.add(element);
            }
            return this;
        }
        
        public Builder<E> addAll(final Iterator<? extends E> elements) {
            while (elements.hasNext()) {
                this.add(elements.next());
            }
            return this;
        }
        
        public abstract ImmutableCollection<E> build();
    }
    
    abstract static class ArrayBasedBuilder<E> extends Builder<E>
    {
        Object[] contents;
        int size;
        
        ArrayBasedBuilder(final int initialCapacity) {
            CollectPreconditions.checkNonnegative(initialCapacity, "initialCapacity");
            this.contents = new Object[initialCapacity];
            this.size = 0;
        }
        
        private void ensureCapacity(final int minCapacity) {
            if (this.contents.length < minCapacity) {
                this.contents = ObjectArrays.arraysCopyOf(this.contents, Builder.expandedCapacity(this.contents.length, minCapacity));
            }
        }
        
        @Override
        public ArrayBasedBuilder<E> add(final E element) {
            Preconditions.checkNotNull(element);
            this.ensureCapacity(this.size + 1);
            this.contents[this.size++] = element;
            return this;
        }
        
        @Override
        public Builder<E> add(final E... elements) {
            ObjectArrays.checkElementsNotNull((Object[])elements);
            this.ensureCapacity(this.size + elements.length);
            System.arraycopy(elements, 0, this.contents, this.size, elements.length);
            this.size += elements.length;
            return this;
        }
        
        @Override
        public Builder<E> addAll(final Iterable<? extends E> elements) {
            if (elements instanceof Collection) {
                final Collection<?> collection = (Collection<?>)(Collection)elements;
                this.ensureCapacity(this.size + collection.size());
            }
            super.addAll(elements);
            return this;
        }
    }
}
