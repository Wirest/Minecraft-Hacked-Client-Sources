// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;
import com.google.common.math.IntMath;
import java.util.List;
import java.util.Iterator;
import com.google.common.primitives.Ints;
import java.util.Map;
import javax.annotation.Nullable;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.annotations.Beta;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentMap;
import java.io.Serializable;

public final class ConcurrentHashMultiset<E> extends AbstractMultiset<E> implements Serializable
{
    private final transient ConcurrentMap<E, AtomicInteger> countMap;
    private transient EntrySet entrySet;
    private static final long serialVersionUID = 1L;
    
    public static <E> ConcurrentHashMultiset<E> create() {
        return new ConcurrentHashMultiset<E>(new ConcurrentHashMap<E, AtomicInteger>());
    }
    
    public static <E> ConcurrentHashMultiset<E> create(final Iterable<? extends E> elements) {
        final ConcurrentHashMultiset<E> multiset = create();
        Iterables.addAll(multiset, elements);
        return multiset;
    }
    
    @Beta
    public static <E> ConcurrentHashMultiset<E> create(final MapMaker mapMaker) {
        return new ConcurrentHashMultiset<E>(mapMaker.makeMap());
    }
    
    @VisibleForTesting
    ConcurrentHashMultiset(final ConcurrentMap<E, AtomicInteger> countMap) {
        Preconditions.checkArgument(countMap.isEmpty());
        this.countMap = countMap;
    }
    
    @Override
    public int count(@Nullable final Object element) {
        final AtomicInteger existingCounter = Maps.safeGet(this.countMap, element);
        return (existingCounter == null) ? 0 : existingCounter.get();
    }
    
    @Override
    public int size() {
        long sum = 0L;
        for (final AtomicInteger value : this.countMap.values()) {
            sum += value.get();
        }
        return Ints.saturatedCast(sum);
    }
    
    @Override
    public Object[] toArray() {
        return this.snapshot().toArray();
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        return this.snapshot().toArray(array);
    }
    
    private List<E> snapshot() {
        final List<E> list = (List<E>)Lists.newArrayListWithExpectedSize(this.size());
        for (final Multiset.Entry<E> entry : this.entrySet()) {
            final E element = entry.getElement();
            for (int i = entry.getCount(); i > 0; --i) {
                list.add(element);
            }
        }
        return list;
    }
    
    @Override
    public int add(final E element, final int occurrences) {
        Preconditions.checkNotNull(element);
        if (occurrences == 0) {
            return this.count(element);
        }
        Preconditions.checkArgument(occurrences > 0, "Invalid occurrences: %s", occurrences);
        while (true) {
            AtomicInteger existingCounter = Maps.safeGet(this.countMap, element);
            if (existingCounter == null) {
                existingCounter = this.countMap.putIfAbsent(element, new AtomicInteger(occurrences));
                if (existingCounter == null) {
                    return 0;
                }
            }
            while (true) {
                final int oldValue = existingCounter.get();
                if (oldValue != 0) {
                    try {
                        final int newValue = IntMath.checkedAdd(oldValue, occurrences);
                        if (existingCounter.compareAndSet(oldValue, newValue)) {
                            return oldValue;
                        }
                        continue;
                    }
                    catch (ArithmeticException overflow) {
                        throw new IllegalArgumentException("Overflow adding " + occurrences + " occurrences to a count of " + oldValue);
                    }
                    break;
                }
                break;
            }
            final AtomicInteger newCounter = new AtomicInteger(occurrences);
            if (this.countMap.putIfAbsent(element, newCounter) == null || this.countMap.replace(element, existingCounter, newCounter)) {
                return 0;
            }
        }
    }
    
    @Override
    public int remove(@Nullable final Object element, final int occurrences) {
        if (occurrences == 0) {
            return this.count(element);
        }
        Preconditions.checkArgument(occurrences > 0, "Invalid occurrences: %s", occurrences);
        final AtomicInteger existingCounter = Maps.safeGet(this.countMap, element);
        if (existingCounter == null) {
            return 0;
        }
        while (true) {
            final int oldValue = existingCounter.get();
            if (oldValue == 0) {
                return 0;
            }
            final int newValue = Math.max(0, oldValue - occurrences);
            if (existingCounter.compareAndSet(oldValue, newValue)) {
                if (newValue == 0) {
                    this.countMap.remove(element, existingCounter);
                }
                return oldValue;
            }
        }
    }
    
    public boolean removeExactly(@Nullable final Object element, final int occurrences) {
        if (occurrences == 0) {
            return true;
        }
        Preconditions.checkArgument(occurrences > 0, "Invalid occurrences: %s", occurrences);
        final AtomicInteger existingCounter = Maps.safeGet(this.countMap, element);
        if (existingCounter == null) {
            return false;
        }
        while (true) {
            final int oldValue = existingCounter.get();
            if (oldValue < occurrences) {
                return false;
            }
            final int newValue = oldValue - occurrences;
            if (existingCounter.compareAndSet(oldValue, newValue)) {
                if (newValue == 0) {
                    this.countMap.remove(element, existingCounter);
                }
                return true;
            }
        }
    }
    
    @Override
    public int setCount(final E element, final int count) {
        Preconditions.checkNotNull(element);
        CollectPreconditions.checkNonnegative(count, "count");
        while (true) {
            AtomicInteger existingCounter = Maps.safeGet(this.countMap, element);
            if (existingCounter == null) {
                if (count == 0) {
                    return 0;
                }
                existingCounter = this.countMap.putIfAbsent(element, new AtomicInteger(count));
                if (existingCounter == null) {
                    return 0;
                }
            }
            while (true) {
                final int oldValue = existingCounter.get();
                if (oldValue == 0) {
                    if (count == 0) {
                        return 0;
                    }
                    final AtomicInteger newCounter = new AtomicInteger(count);
                    if (this.countMap.putIfAbsent(element, newCounter) == null || this.countMap.replace(element, existingCounter, newCounter)) {
                        return 0;
                    }
                    break;
                }
                else {
                    if (existingCounter.compareAndSet(oldValue, count)) {
                        if (count == 0) {
                            this.countMap.remove(element, existingCounter);
                        }
                        return oldValue;
                    }
                    continue;
                }
            }
        }
    }
    
    @Override
    public boolean setCount(final E element, final int expectedOldCount, final int newCount) {
        Preconditions.checkNotNull(element);
        CollectPreconditions.checkNonnegative(expectedOldCount, "oldCount");
        CollectPreconditions.checkNonnegative(newCount, "newCount");
        final AtomicInteger existingCounter = Maps.safeGet(this.countMap, element);
        if (existingCounter == null) {
            return expectedOldCount == 0 && (newCount == 0 || this.countMap.putIfAbsent(element, new AtomicInteger(newCount)) == null);
        }
        final int oldValue = existingCounter.get();
        if (oldValue == expectedOldCount) {
            if (oldValue == 0) {
                if (newCount == 0) {
                    this.countMap.remove(element, existingCounter);
                    return true;
                }
                final AtomicInteger newCounter = new AtomicInteger(newCount);
                return this.countMap.putIfAbsent(element, newCounter) == null || this.countMap.replace(element, existingCounter, newCounter);
            }
            else if (existingCounter.compareAndSet(oldValue, newCount)) {
                if (newCount == 0) {
                    this.countMap.remove(element, existingCounter);
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    Set<E> createElementSet() {
        final Set<E> delegate = this.countMap.keySet();
        return new ForwardingSet<E>() {
            @Override
            protected Set<E> delegate() {
                return delegate;
            }
            
            @Override
            public boolean contains(@Nullable final Object object) {
                return object != null && Collections2.safeContains(delegate, object);
            }
            
            @Override
            public boolean containsAll(final Collection<?> collection) {
                return this.standardContainsAll(collection);
            }
            
            @Override
            public boolean remove(final Object object) {
                return object != null && Collections2.safeRemove(delegate, object);
            }
            
            @Override
            public boolean removeAll(final Collection<?> c) {
                return this.standardRemoveAll(c);
            }
        };
    }
    
    @Override
    public Set<Multiset.Entry<E>> entrySet() {
        EntrySet result = this.entrySet;
        if (result == null) {
            result = (this.entrySet = new EntrySet());
        }
        return (Set<Multiset.Entry<E>>)result;
    }
    
    @Override
    int distinctElements() {
        return this.countMap.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.countMap.isEmpty();
    }
    
    @Override
    Iterator<Multiset.Entry<E>> entryIterator() {
        final Iterator<Multiset.Entry<E>> readOnlyIterator = new AbstractIterator<Multiset.Entry<E>>() {
            private Iterator<Map.Entry<E, AtomicInteger>> mapEntries = ConcurrentHashMultiset.this.countMap.entrySet().iterator();
            
            @Override
            protected Multiset.Entry<E> computeNext() {
                while (this.mapEntries.hasNext()) {
                    final Map.Entry<E, AtomicInteger> mapEntry = this.mapEntries.next();
                    final int count = mapEntry.getValue().get();
                    if (count != 0) {
                        return Multisets.immutableEntry(mapEntry.getKey(), count);
                    }
                }
                return this.endOfData();
            }
        };
        return new ForwardingIterator<Multiset.Entry<E>>() {
            private Multiset.Entry<E> last;
            
            @Override
            protected Iterator<Multiset.Entry<E>> delegate() {
                return readOnlyIterator;
            }
            
            @Override
            public Multiset.Entry<E> next() {
                return this.last = super.next();
            }
            
            @Override
            public void remove() {
                CollectPreconditions.checkRemove(this.last != null);
                ConcurrentHashMultiset.this.setCount(this.last.getElement(), 0);
                this.last = null;
            }
        };
    }
    
    @Override
    public void clear() {
        this.countMap.clear();
    }
    
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.countMap);
    }
    
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final ConcurrentMap<E, Integer> deserializedCountMap = (ConcurrentMap<E, Integer>)stream.readObject();
        FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set(this, deserializedCountMap);
    }
    
    private static class FieldSettersHolder
    {
        static final Serialization.FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER;
        
        static {
            COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");
        }
    }
    
    private class EntrySet extends AbstractMultiset.EntrySet
    {
        @Override
        ConcurrentHashMultiset<E> multiset() {
            return ConcurrentHashMultiset.this;
        }
        
        @Override
        public Object[] toArray() {
            return this.snapshot().toArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.snapshot().toArray(array);
        }
        
        private List<Multiset.Entry<E>> snapshot() {
            final List<Multiset.Entry<E>> list = (List<Multiset.Entry<E>>)Lists.newArrayListWithExpectedSize(this.size());
            Iterators.addAll(list, (Iterator<? extends Multiset.Entry<E>>)this.iterator());
            return list;
        }
    }
}
