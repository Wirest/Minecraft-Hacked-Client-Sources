// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Set;
import com.google.common.annotations.GwtIncompatible;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.primitives.Ints;
import java.util.Collection;
import java.util.Arrays;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableMultiset<E> extends ImmutableCollection<E> implements Multiset<E>
{
    private static final ImmutableMultiset<Object> EMPTY;
    private transient ImmutableSet<Entry<E>> entrySet;
    
    public static <E> ImmutableMultiset<E> of() {
        return (ImmutableMultiset<E>)ImmutableMultiset.EMPTY;
    }
    
    public static <E> ImmutableMultiset<E> of(final E element) {
        return copyOfInternal(element);
    }
    
    public static <E> ImmutableMultiset<E> of(final E e1, final E e2) {
        return copyOfInternal(e1, e2);
    }
    
    public static <E> ImmutableMultiset<E> of(final E e1, final E e2, final E e3) {
        return copyOfInternal(e1, e2, e3);
    }
    
    public static <E> ImmutableMultiset<E> of(final E e1, final E e2, final E e3, final E e4) {
        return copyOfInternal(e1, e2, e3, e4);
    }
    
    public static <E> ImmutableMultiset<E> of(final E e1, final E e2, final E e3, final E e4, final E e5) {
        return copyOfInternal(e1, e2, e3, e4, e5);
    }
    
    public static <E> ImmutableMultiset<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E... others) {
        return new Builder<E>().add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).add(others).build();
    }
    
    public static <E> ImmutableMultiset<E> copyOf(final E[] elements) {
        return copyOf((Iterable<? extends E>)Arrays.asList(elements));
    }
    
    public static <E> ImmutableMultiset<E> copyOf(final Iterable<? extends E> elements) {
        if (elements instanceof ImmutableMultiset) {
            final ImmutableMultiset<E> result = (ImmutableMultiset<E>)(ImmutableMultiset)elements;
            if (!result.isPartialView()) {
                return result;
            }
        }
        final Multiset<? extends E> multiset = (Multiset<? extends E>)((elements instanceof Multiset) ? Multisets.cast(elements) : LinkedHashMultiset.create((Iterable<?>)elements));
        return copyOfInternal(multiset);
    }
    
    private static <E> ImmutableMultiset<E> copyOfInternal(final E... elements) {
        return copyOf((Iterable<? extends E>)Arrays.asList(elements));
    }
    
    private static <E> ImmutableMultiset<E> copyOfInternal(final Multiset<? extends E> multiset) {
        return copyFromEntries((Collection<? extends Entry<? extends E>>)multiset.entrySet());
    }
    
    static <E> ImmutableMultiset<E> copyFromEntries(final Collection<? extends Entry<? extends E>> entries) {
        long size = 0L;
        final ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();
        for (final Entry<? extends E> entry : entries) {
            final int count = entry.getCount();
            if (count > 0) {
                builder.put((E)entry.getElement(), count);
                size += count;
            }
        }
        if (size == 0L) {
            return of();
        }
        return new RegularImmutableMultiset<E>(builder.build(), Ints.saturatedCast(size));
    }
    
    public static <E> ImmutableMultiset<E> copyOf(final Iterator<? extends E> elements) {
        final Multiset<E> multiset = (Multiset<E>)LinkedHashMultiset.create();
        Iterators.addAll(multiset, elements);
        return copyOfInternal((Multiset<? extends E>)multiset);
    }
    
    ImmutableMultiset() {
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        final Iterator<Entry<E>> entryIterator = this.entrySet().iterator();
        return new UnmodifiableIterator<E>() {
            int remaining;
            E element;
            
            @Override
            public boolean hasNext() {
                return this.remaining > 0 || entryIterator.hasNext();
            }
            
            @Override
            public E next() {
                if (this.remaining <= 0) {
                    final Entry<E> entry = entryIterator.next();
                    this.element = entry.getElement();
                    this.remaining = entry.getCount();
                }
                --this.remaining;
                return this.element;
            }
        };
    }
    
    @Override
    public boolean contains(@Nullable final Object object) {
        return this.count(object) > 0;
    }
    
    @Override
    public boolean containsAll(final Collection<?> targets) {
        return this.elementSet().containsAll(targets);
    }
    
    @Deprecated
    @Override
    public final int add(final E element, final int occurrences) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final int remove(final Object element, final int occurrences) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final int setCount(final E element, final int count) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final boolean setCount(final E element, final int oldCount, final int newCount) {
        throw new UnsupportedOperationException();
    }
    
    @GwtIncompatible("not present in emulated superclass")
    @Override
    int copyIntoArray(final Object[] dst, int offset) {
        for (final Entry<E> entry : this.entrySet()) {
            Arrays.fill(dst, offset, offset + entry.getCount(), entry.getElement());
            offset += entry.getCount();
        }
        return offset;
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        return Multisets.equalsImpl(this, object);
    }
    
    @Override
    public int hashCode() {
        return Sets.hashCodeImpl(this.entrySet());
    }
    
    @Override
    public String toString() {
        return this.entrySet().toString();
    }
    
    @Override
    public ImmutableSet<Entry<E>> entrySet() {
        final ImmutableSet<Entry<E>> es = this.entrySet;
        return (es == null) ? (this.entrySet = this.createEntrySet()) : es;
    }
    
    private final ImmutableSet<Entry<E>> createEntrySet() {
        return this.isEmpty() ? ImmutableSet.of() : new EntrySet();
    }
    
    abstract Entry<E> getEntry(final int p0);
    
    @Override
    Object writeReplace() {
        return new SerializedForm(this);
    }
    
    public static <E> Builder<E> builder() {
        return new Builder<E>();
    }
    
    static {
        EMPTY = new RegularImmutableMultiset<Object>(ImmutableMap.of(), 0);
    }
    
    private final class EntrySet extends ImmutableSet<Entry<E>>
    {
        private static final long serialVersionUID = 0L;
        
        @Override
        boolean isPartialView() {
            return ImmutableMultiset.this.isPartialView();
        }
        
        @Override
        public UnmodifiableIterator<Entry<E>> iterator() {
            return this.asList().iterator();
        }
        
        @Override
        ImmutableList<Entry<E>> createAsList() {
            return new ImmutableAsList<Entry<E>>() {
                @Override
                public Entry<E> get(final int index) {
                    return ImmutableMultiset.this.getEntry(index);
                }
                
                @Override
                ImmutableCollection<Entry<E>> delegateCollection() {
                    return EntrySet.this;
                }
            };
        }
        
        @Override
        public int size() {
            return ImmutableMultiset.this.elementSet().size();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            final Entry<?> entry = (Entry<?>)o;
            if (entry.getCount() <= 0) {
                return false;
            }
            final int count = ImmutableMultiset.this.count(entry.getElement());
            return count == entry.getCount();
        }
        
        @Override
        public int hashCode() {
            return ImmutableMultiset.this.hashCode();
        }
        
        @Override
        Object writeReplace() {
            return new EntrySetSerializedForm(ImmutableMultiset.this);
        }
    }
    
    static class EntrySetSerializedForm<E> implements Serializable
    {
        final ImmutableMultiset<E> multiset;
        
        EntrySetSerializedForm(final ImmutableMultiset<E> multiset) {
            this.multiset = multiset;
        }
        
        Object readResolve() {
            return this.multiset.entrySet();
        }
    }
    
    private static class SerializedForm implements Serializable
    {
        final Object[] elements;
        final int[] counts;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final Multiset<?> multiset) {
            final int distinct = multiset.entrySet().size();
            this.elements = new Object[distinct];
            this.counts = new int[distinct];
            int i = 0;
            for (final Entry<?> entry : multiset.entrySet()) {
                this.elements[i] = entry.getElement();
                this.counts[i] = entry.getCount();
                ++i;
            }
        }
        
        Object readResolve() {
            final LinkedHashMultiset<Object> multiset = LinkedHashMultiset.create(this.elements.length);
            for (int i = 0; i < this.elements.length; ++i) {
                multiset.add(this.elements[i], this.counts[i]);
            }
            return ImmutableMultiset.copyOf((Iterable<?>)multiset);
        }
    }
    
    public static class Builder<E> extends ImmutableCollection.Builder<E>
    {
        final Multiset<E> contents;
        
        public Builder() {
            this(LinkedHashMultiset.create());
        }
        
        Builder(final Multiset<E> contents) {
            this.contents = contents;
        }
        
        @Override
        public Builder<E> add(final E element) {
            this.contents.add(Preconditions.checkNotNull(element));
            return this;
        }
        
        public Builder<E> addCopies(final E element, final int occurrences) {
            this.contents.add(Preconditions.checkNotNull(element), occurrences);
            return this;
        }
        
        public Builder<E> setCount(final E element, final int count) {
            this.contents.setCount(Preconditions.checkNotNull(element), count);
            return this;
        }
        
        @Override
        public Builder<E> add(final E... elements) {
            super.add(elements);
            return this;
        }
        
        @Override
        public Builder<E> addAll(final Iterable<? extends E> elements) {
            if (elements instanceof Multiset) {
                final Multiset<? extends E> multiset = Multisets.cast(elements);
                for (final Entry<? extends E> entry : multiset.entrySet()) {
                    this.addCopies(entry.getElement(), entry.getCount());
                }
            }
            else {
                super.addAll(elements);
            }
            return this;
        }
        
        @Override
        public Builder<E> addAll(final Iterator<? extends E> elements) {
            super.addAll(elements);
            return this;
        }
        
        @Override
        public ImmutableMultiset<E> build() {
            return ImmutableMultiset.copyOf((Iterable<? extends E>)this.contents);
        }
    }
}
