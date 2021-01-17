// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Predicates;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import com.google.common.base.Objects;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.Collection;
import com.google.common.base.Preconditions;
import java.util.Map;
import com.google.common.base.Predicate;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
class FilteredEntryMultimap<K, V> extends AbstractMultimap<K, V> implements FilteredMultimap<K, V>
{
    final Multimap<K, V> unfiltered;
    final Predicate<? super Map.Entry<K, V>> predicate;
    
    FilteredEntryMultimap(final Multimap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> predicate) {
        this.unfiltered = Preconditions.checkNotNull(unfiltered);
        this.predicate = Preconditions.checkNotNull(predicate);
    }
    
    @Override
    public Multimap<K, V> unfiltered() {
        return this.unfiltered;
    }
    
    @Override
    public Predicate<? super Map.Entry<K, V>> entryPredicate() {
        return this.predicate;
    }
    
    @Override
    public int size() {
        return this.entries().size();
    }
    
    private boolean satisfies(final K key, final V value) {
        return this.predicate.apply(Maps.immutableEntry(key, value));
    }
    
    static <E> Collection<E> filterCollection(final Collection<E> collection, final Predicate<? super E> predicate) {
        if (collection instanceof Set) {
            return (Collection<E>)Sets.filter((Set<Object>)(Set)collection, (Predicate<? super Object>)predicate);
        }
        return Collections2.filter(collection, predicate);
    }
    
    @Override
    public boolean containsKey(@Nullable final Object key) {
        return this.asMap().get(key) != null;
    }
    
    @Override
    public Collection<V> removeAll(@Nullable final Object key) {
        return Objects.firstNonNull(this.asMap().remove(key), this.unmodifiableEmptyCollection());
    }
    
    Collection<V> unmodifiableEmptyCollection() {
        return (Collection<V>)((this.unfiltered instanceof SetMultimap) ? Collections.emptySet() : Collections.emptyList());
    }
    
    @Override
    public void clear() {
        this.entries().clear();
    }
    
    @Override
    public Collection<V> get(final K key) {
        return filterCollection(this.unfiltered.get(key), new ValuePredicate(key));
    }
    
    @Override
    Collection<Map.Entry<K, V>> createEntries() {
        return filterCollection(this.unfiltered.entries(), this.predicate);
    }
    
    @Override
    Collection<V> createValues() {
        return (Collection<V>)new FilteredMultimapValues((FilteredMultimap<Object, Object>)this);
    }
    
    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    Map<K, Collection<V>> createAsMap() {
        return new AsMap();
    }
    
    @Override
    public Set<K> keySet() {
        return this.asMap().keySet();
    }
    
    boolean removeEntriesIf(final Predicate<? super Map.Entry<K, Collection<V>>> predicate) {
        final Iterator<Map.Entry<K, Collection<V>>> entryIterator = this.unfiltered.asMap().entrySet().iterator();
        boolean changed = false;
        while (entryIterator.hasNext()) {
            final Map.Entry<K, Collection<V>> entry = entryIterator.next();
            final K key = entry.getKey();
            final Collection<V> collection = filterCollection(entry.getValue(), new ValuePredicate(key));
            if (!collection.isEmpty() && predicate.apply(Maps.immutableEntry(key, collection))) {
                if (collection.size() == entry.getValue().size()) {
                    entryIterator.remove();
                }
                else {
                    collection.clear();
                }
                changed = true;
            }
        }
        return changed;
    }
    
    @Override
    Multiset<K> createKeys() {
        return (Multiset<K>)new Keys();
    }
    
    final class ValuePredicate implements Predicate<V>
    {
        private final K key;
        
        ValuePredicate(final K key) {
            this.key = key;
        }
        
        @Override
        public boolean apply(@Nullable final V value) {
            return FilteredEntryMultimap.this.satisfies(this.key, value);
        }
    }
    
    class AsMap extends Maps.ImprovedAbstractMap<K, Collection<V>>
    {
        @Override
        public boolean containsKey(@Nullable final Object key) {
            return this.get(key) != null;
        }
        
        @Override
        public void clear() {
            FilteredEntryMultimap.this.clear();
        }
        
        @Override
        public Collection<V> get(@Nullable final Object key) {
            Collection<V> result = FilteredEntryMultimap.this.unfiltered.asMap().get(key);
            if (result == null) {
                return null;
            }
            final K k = (K)key;
            result = FilteredEntryMultimap.filterCollection(result, new ValuePredicate(k));
            return result.isEmpty() ? null : result;
        }
        
        @Override
        public Collection<V> remove(@Nullable final Object key) {
            final Collection<V> collection = FilteredEntryMultimap.this.unfiltered.asMap().get(key);
            if (collection == null) {
                return null;
            }
            final K k = (K)key;
            final List<V> result = (List<V>)Lists.newArrayList();
            final Iterator<V> itr = collection.iterator();
            while (itr.hasNext()) {
                final V v = itr.next();
                if (FilteredEntryMultimap.this.satisfies(k, v)) {
                    itr.remove();
                    result.add(v);
                }
            }
            if (result.isEmpty()) {
                return null;
            }
            if (FilteredEntryMultimap.this.unfiltered instanceof SetMultimap) {
                return (Collection<V>)Collections.unmodifiableSet((Set<?>)Sets.newLinkedHashSet((Iterable<?>)result));
            }
            return (Collection<V>)Collections.unmodifiableList((List<?>)result);
        }
        
        @Override
        Set<K> createKeySet() {
            return (Set<K>)new Maps.KeySet<K, Collection<V>>(this) {
                @Override
                public boolean removeAll(final Collection<?> c) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.in(c)));
                }
                
                @Override
                public boolean retainAll(final Collection<?> c) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in((Collection<? extends K>)c))));
                }
                
                @Override
                public boolean remove(@Nullable final Object o) {
                    return AsMap.this.remove(o) != null;
                }
            };
        }
        
        @Override
        Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            return (Set<Map.Entry<K, Collection<V>>>)new Maps.EntrySet<K, Collection<V>>() {
                @Override
                Map<K, Collection<V>> map() {
                    return AsMap.this;
                }
                
                @Override
                public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                    return new AbstractIterator<Map.Entry<K, Collection<V>>>() {
                        final Iterator<Map.Entry<K, Collection<V>>> backingIterator = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();
                        
                        @Override
                        protected Map.Entry<K, Collection<V>> computeNext() {
                            while (this.backingIterator.hasNext()) {
                                final Map.Entry<K, Collection<V>> entry = this.backingIterator.next();
                                final K key = entry.getKey();
                                final Collection<V> collection = FilteredEntryMultimap.filterCollection(entry.getValue(), new ValuePredicate(key));
                                if (!collection.isEmpty()) {
                                    return Maps.immutableEntry(key, collection);
                                }
                            }
                            return this.endOfData();
                        }
                    };
                }
                
                @Override
                public boolean removeAll(final Collection<?> c) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Predicates.in((Collection<? extends Map.Entry<K, Collection<V>>>)c));
                }
                
                @Override
                public boolean retainAll(final Collection<?> c) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Predicates.not(Predicates.in((Collection<? extends Map.Entry<K, Collection<V>>>)c)));
                }
                
                @Override
                public int size() {
                    return Iterators.size(this.iterator());
                }
            };
        }
        
        @Override
        Collection<Collection<V>> createValues() {
            return (Collection<Collection<V>>)new Maps.Values<K, Collection<V>>(this) {
                @Override
                public boolean remove(@Nullable final Object o) {
                    if (o instanceof Collection) {
                        final Collection<?> c = (Collection<?>)o;
                        final Iterator<Map.Entry<K, Collection<V>>> entryIterator = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();
                        while (entryIterator.hasNext()) {
                            final Map.Entry<K, Collection<V>> entry = entryIterator.next();
                            final K key = entry.getKey();
                            final Collection<V> collection = FilteredEntryMultimap.filterCollection(entry.getValue(), new ValuePredicate(key));
                            if (!collection.isEmpty() && c.equals(collection)) {
                                if (collection.size() == entry.getValue().size()) {
                                    entryIterator.remove();
                                }
                                else {
                                    collection.clear();
                                }
                                return true;
                            }
                        }
                    }
                    return false;
                }
                
                @Override
                public boolean removeAll(final Collection<?> c) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.in(c)));
                }
                
                @Override
                public boolean retainAll(final Collection<?> c) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in((Collection<? extends V>)c))));
                }
            };
        }
    }
    
    class Keys extends Multimaps.Keys<K, V>
    {
        Keys() {
            super(FilteredEntryMultimap.this);
        }
        
        @Override
        public int remove(@Nullable final Object key, final int occurrences) {
            CollectPreconditions.checkNonnegative(occurrences, "occurrences");
            if (occurrences == 0) {
                return this.count(key);
            }
            final Collection<V> collection = FilteredEntryMultimap.this.unfiltered.asMap().get(key);
            if (collection == null) {
                return 0;
            }
            final K k = (K)key;
            int oldCount = 0;
            final Iterator<V> itr = collection.iterator();
            while (itr.hasNext()) {
                final V v = itr.next();
                if (FilteredEntryMultimap.this.satisfies(k, v) && ++oldCount <= occurrences) {
                    itr.remove();
                }
            }
            return oldCount;
        }
        
        @Override
        public Set<Multiset.Entry<K>> entrySet() {
            return (Set<Multiset.Entry<K>>)new Multisets.EntrySet<K>() {
                @Override
                Multiset<K> multiset() {
                    return (Multiset<K>)Keys.this;
                }
                
                @Override
                public Iterator<Multiset.Entry<K>> iterator() {
                    return ((Multimaps.Keys<K, V>)Keys.this).entryIterator();
                }
                
                @Override
                public int size() {
                    return FilteredEntryMultimap.this.keySet().size();
                }
                
                private boolean removeEntriesIf(final Predicate<? super Multiset.Entry<K>> predicate) {
                    return FilteredEntryMultimap.this.removeEntriesIf(new Predicate<Map.Entry<K, Collection<V>>>() {
                        @Override
                        public boolean apply(final Map.Entry<K, Collection<V>> entry) {
                            return predicate.apply(Multisets.immutableEntry(entry.getKey(), entry.getValue().size()));
                        }
                    });
                }
                
                @Override
                public boolean removeAll(final Collection<?> c) {
                    return this.removeEntriesIf(Predicates.in((Collection<? extends Multiset.Entry<K>>)c));
                }
                
                @Override
                public boolean retainAll(final Collection<?> c) {
                    return this.removeEntriesIf(Predicates.not(Predicates.in((Collection<? extends Multiset.Entry<K>>)c)));
                }
            };
        }
    }
}
