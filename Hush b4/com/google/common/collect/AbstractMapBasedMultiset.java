// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.ConcurrentModificationException;
import java.io.ObjectStreamException;
import java.io.InvalidObjectException;
import javax.annotation.Nullable;
import com.google.common.primitives.Ints;
import java.util.Iterator;
import java.util.Set;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(emulated = true)
abstract class AbstractMapBasedMultiset<E> extends AbstractMultiset<E> implements Serializable
{
    private transient Map<E, Count> backingMap;
    private transient long size;
    @GwtIncompatible("not needed in emulated source.")
    private static final long serialVersionUID = -2250766705698539974L;
    
    protected AbstractMapBasedMultiset(final Map<E, Count> backingMap) {
        this.backingMap = Preconditions.checkNotNull(backingMap);
        this.size = super.size();
    }
    
    void setBackingMap(final Map<E, Count> backingMap) {
        this.backingMap = backingMap;
    }
    
    @Override
    public Set<Multiset.Entry<E>> entrySet() {
        return super.entrySet();
    }
    
    @Override
    Iterator<Multiset.Entry<E>> entryIterator() {
        final Iterator<Map.Entry<E, Count>> backingEntries = this.backingMap.entrySet().iterator();
        return new Iterator<Multiset.Entry<E>>() {
            Map.Entry<E, Count> toRemove;
            
            @Override
            public boolean hasNext() {
                return backingEntries.hasNext();
            }
            
            @Override
            public Multiset.Entry<E> next() {
                final Map.Entry<E, Count> mapEntry = backingEntries.next();
                this.toRemove = mapEntry;
                return new Multisets.AbstractEntry<E>() {
                    @Override
                    public E getElement() {
                        return mapEntry.getKey();
                    }
                    
                    @Override
                    public int getCount() {
                        final Count count = mapEntry.getValue();
                        if (count == null || count.get() == 0) {
                            final Count frequency = AbstractMapBasedMultiset.this.backingMap.get(this.getElement());
                            if (frequency != null) {
                                return frequency.get();
                            }
                        }
                        return (count == null) ? 0 : count.get();
                    }
                };
            }
            
            @Override
            public void remove() {
                CollectPreconditions.checkRemove(this.toRemove != null);
                AbstractMapBasedMultiset.this.size -= this.toRemove.getValue().getAndSet(0);
                backingEntries.remove();
                this.toRemove = null;
            }
        };
    }
    
    @Override
    public void clear() {
        for (final Count frequency : this.backingMap.values()) {
            frequency.set(0);
        }
        this.backingMap.clear();
        this.size = 0L;
    }
    
    @Override
    int distinctElements() {
        return this.backingMap.size();
    }
    
    @Override
    public int size() {
        return Ints.saturatedCast(this.size);
    }
    
    @Override
    public Iterator<E> iterator() {
        return new MapBasedMultisetIterator();
    }
    
    @Override
    public int count(@Nullable final Object element) {
        final Count frequency = Maps.safeGet(this.backingMap, element);
        return (frequency == null) ? 0 : frequency.get();
    }
    
    @Override
    public int add(@Nullable final E element, final int occurrences) {
        if (occurrences == 0) {
            return this.count(element);
        }
        Preconditions.checkArgument(occurrences > 0, "occurrences cannot be negative: %s", occurrences);
        final Count frequency = this.backingMap.get(element);
        int oldCount;
        if (frequency == null) {
            oldCount = 0;
            this.backingMap.put(element, new Count(occurrences));
        }
        else {
            oldCount = frequency.get();
            final long newCount = oldCount + (long)occurrences;
            Preconditions.checkArgument(newCount <= 2147483647L, "too many occurrences: %s", newCount);
            frequency.getAndAdd(occurrences);
        }
        this.size += occurrences;
        return oldCount;
    }
    
    @Override
    public int remove(@Nullable final Object element, final int occurrences) {
        if (occurrences == 0) {
            return this.count(element);
        }
        Preconditions.checkArgument(occurrences > 0, "occurrences cannot be negative: %s", occurrences);
        final Count frequency = this.backingMap.get(element);
        if (frequency == null) {
            return 0;
        }
        final int oldCount = frequency.get();
        int numberRemoved;
        if (oldCount > occurrences) {
            numberRemoved = occurrences;
        }
        else {
            numberRemoved = oldCount;
            this.backingMap.remove(element);
        }
        frequency.addAndGet(-numberRemoved);
        this.size -= numberRemoved;
        return oldCount;
    }
    
    @Override
    public int setCount(@Nullable final E element, final int count) {
        CollectPreconditions.checkNonnegative(count, "count");
        int oldCount;
        if (count == 0) {
            final Count existingCounter = this.backingMap.remove(element);
            oldCount = getAndSet(existingCounter, count);
        }
        else {
            final Count existingCounter = this.backingMap.get(element);
            oldCount = getAndSet(existingCounter, count);
            if (existingCounter == null) {
                this.backingMap.put(element, new Count(count));
            }
        }
        this.size += count - oldCount;
        return oldCount;
    }
    
    private static int getAndSet(final Count i, final int count) {
        if (i == null) {
            return 0;
        }
        return i.getAndSet(count);
    }
    
    @GwtIncompatible("java.io.ObjectStreamException")
    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("Stream data required");
    }
    
    private class MapBasedMultisetIterator implements Iterator<E>
    {
        final Iterator<Map.Entry<E, Count>> entryIterator;
        Map.Entry<E, Count> currentEntry;
        int occurrencesLeft;
        boolean canRemove;
        
        MapBasedMultisetIterator() {
            this.entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
        }
        
        @Override
        public boolean hasNext() {
            return this.occurrencesLeft > 0 || this.entryIterator.hasNext();
        }
        
        @Override
        public E next() {
            if (this.occurrencesLeft == 0) {
                this.currentEntry = this.entryIterator.next();
                this.occurrencesLeft = this.currentEntry.getValue().get();
            }
            --this.occurrencesLeft;
            this.canRemove = true;
            return this.currentEntry.getKey();
        }
        
        @Override
        public void remove() {
            CollectPreconditions.checkRemove(this.canRemove);
            final int frequency = this.currentEntry.getValue().get();
            if (frequency <= 0) {
                throw new ConcurrentModificationException();
            }
            if (this.currentEntry.getValue().addAndGet(-1) == 0) {
                this.entryIterator.remove();
            }
            AbstractMapBasedMultiset.this.size--;
            this.canRemove = false;
        }
    }
}
