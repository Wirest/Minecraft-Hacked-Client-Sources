// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.List;
import java.io.Serializable;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import com.google.common.base.Preconditions;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public final class MapConstraints
{
    private MapConstraints() {
    }
    
    public static MapConstraint<Object, Object> notNull() {
        return NotNullMapConstraint.INSTANCE;
    }
    
    public static <K, V> Map<K, V> constrainedMap(final Map<K, V> map, final MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedMap<K, V>(map, constraint);
    }
    
    public static <K, V> Multimap<K, V> constrainedMultimap(final Multimap<K, V> multimap, final MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedMultimap<K, V>(multimap, constraint);
    }
    
    public static <K, V> ListMultimap<K, V> constrainedListMultimap(final ListMultimap<K, V> multimap, final MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedListMultimap<K, V>(multimap, constraint);
    }
    
    public static <K, V> SetMultimap<K, V> constrainedSetMultimap(final SetMultimap<K, V> multimap, final MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedSetMultimap<K, V>(multimap, constraint);
    }
    
    public static <K, V> SortedSetMultimap<K, V> constrainedSortedSetMultimap(final SortedSetMultimap<K, V> multimap, final MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedSortedSetMultimap<K, V>(multimap, constraint);
    }
    
    private static <K, V> Map.Entry<K, V> constrainedEntry(final Map.Entry<K, V> entry, final MapConstraint<? super K, ? super V> constraint) {
        Preconditions.checkNotNull(entry);
        Preconditions.checkNotNull(constraint);
        return new ForwardingMapEntry<K, V>() {
            @Override
            protected Map.Entry<K, V> delegate() {
                return entry;
            }
            
            @Override
            public V setValue(final V value) {
                constraint.checkKeyValue(((ForwardingMapEntry<Object, V>)this).getKey(), value);
                return entry.setValue(value);
            }
        };
    }
    
    private static <K, V> Map.Entry<K, Collection<V>> constrainedAsMapEntry(final Map.Entry<K, Collection<V>> entry, final MapConstraint<? super K, ? super V> constraint) {
        Preconditions.checkNotNull(entry);
        Preconditions.checkNotNull(constraint);
        return new ForwardingMapEntry<K, Collection<V>>() {
            @Override
            protected Map.Entry<K, Collection<V>> delegate() {
                return entry;
            }
            
            @Override
            public Collection<V> getValue() {
                return Constraints.constrainedTypePreservingCollection(entry.getValue(), new Constraint<V>() {
                    @Override
                    public V checkElement(final V value) {
                        constraint.checkKeyValue(((ForwardingMapEntry<Object, V>)ForwardingMapEntry.this).getKey(), value);
                        return value;
                    }
                });
            }
        };
    }
    
    private static <K, V> Set<Map.Entry<K, Collection<V>>> constrainedAsMapEntries(final Set<Map.Entry<K, Collection<V>>> entries, final MapConstraint<? super K, ? super V> constraint) {
        return (Set<Map.Entry<K, Collection<V>>>)new ConstrainedAsMapEntries((Set<Map.Entry<Object, Collection<Object>>>)entries, (MapConstraint<? super Object, ? super Object>)constraint);
    }
    
    private static <K, V> Collection<Map.Entry<K, V>> constrainedEntries(final Collection<Map.Entry<K, V>> entries, final MapConstraint<? super K, ? super V> constraint) {
        if (entries instanceof Set) {
            return (Collection<Map.Entry<K, V>>)constrainedEntrySet((Set<Map.Entry<Object, Object>>)(Set)entries, (MapConstraint<? super Object, ? super Object>)constraint);
        }
        return (Collection<Map.Entry<K, V>>)new ConstrainedEntries((Collection<Map.Entry<Object, Object>>)entries, (MapConstraint<? super Object, ? super Object>)constraint);
    }
    
    private static <K, V> Set<Map.Entry<K, V>> constrainedEntrySet(final Set<Map.Entry<K, V>> entries, final MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedEntrySet<K, V>(entries, constraint);
    }
    
    public static <K, V> BiMap<K, V> constrainedBiMap(final BiMap<K, V> map, final MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedBiMap<K, V>(map, null, constraint);
    }
    
    private static <K, V> Collection<V> checkValues(final K key, final Iterable<? extends V> values, final MapConstraint<? super K, ? super V> constraint) {
        final Collection<V> copy = (Collection<V>)Lists.newArrayList((Iterable<?>)values);
        for (final V value : copy) {
            constraint.checkKeyValue((Object)key, (Object)value);
        }
        return copy;
    }
    
    private static <K, V> Map<K, V> checkMap(final Map<? extends K, ? extends V> map, final MapConstraint<? super K, ? super V> constraint) {
        final Map<K, V> copy = new LinkedHashMap<K, V>(map);
        for (final Map.Entry<K, V> entry : copy.entrySet()) {
            constraint.checkKeyValue((Object)entry.getKey(), (Object)entry.getValue());
        }
        return copy;
    }
    
    private enum NotNullMapConstraint implements MapConstraint<Object, Object>
    {
        INSTANCE;
        
        @Override
        public void checkKeyValue(final Object key, final Object value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
        }
        
        @Override
        public String toString() {
            return "Not null";
        }
    }
    
    static class ConstrainedMap<K, V> extends ForwardingMap<K, V>
    {
        private final Map<K, V> delegate;
        final MapConstraint<? super K, ? super V> constraint;
        private transient Set<Map.Entry<K, V>> entrySet;
        
        ConstrainedMap(final Map<K, V> delegate, final MapConstraint<? super K, ? super V> constraint) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.constraint = Preconditions.checkNotNull(constraint);
        }
        
        @Override
        protected Map<K, V> delegate() {
            return this.delegate;
        }
        
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> result = this.entrySet;
            if (result == null) {
                result = (this.entrySet = (Set<Map.Entry<K, V>>)constrainedEntrySet((Set<Map.Entry<Object, Object>>)this.delegate.entrySet(), this.constraint));
            }
            return result;
        }
        
        @Override
        public V put(final K key, final V value) {
            this.constraint.checkKeyValue((Object)key, (Object)value);
            return this.delegate.put(key, value);
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends V> map) {
            this.delegate.putAll(checkMap((Map<?, ?>)map, (MapConstraint<? super Object, ? super Object>)this.constraint));
        }
    }
    
    private static class ConstrainedBiMap<K, V> extends ConstrainedMap<K, V> implements BiMap<K, V>
    {
        volatile BiMap<V, K> inverse;
        
        ConstrainedBiMap(final BiMap<K, V> delegate, @Nullable final BiMap<V, K> inverse, final MapConstraint<? super K, ? super V> constraint) {
            super(delegate, constraint);
            this.inverse = inverse;
        }
        
        @Override
        protected BiMap<K, V> delegate() {
            return (BiMap<K, V>)(BiMap)super.delegate();
        }
        
        @Override
        public V forcePut(final K key, final V value) {
            this.constraint.checkKeyValue((Object)key, (Object)value);
            return this.delegate().forcePut(key, value);
        }
        
        @Override
        public BiMap<V, K> inverse() {
            if (this.inverse == null) {
                this.inverse = (BiMap<V, K>)new ConstrainedBiMap((BiMap<Object, Object>)this.delegate().inverse(), (BiMap<Object, Object>)this, new InverseConstraint<Object, Object>((MapConstraint<? super Object, ? super Object>)this.constraint));
            }
            return this.inverse;
        }
        
        @Override
        public Set<V> values() {
            return this.delegate().values();
        }
    }
    
    private static class InverseConstraint<K, V> implements MapConstraint<K, V>
    {
        final MapConstraint<? super V, ? super K> constraint;
        
        public InverseConstraint(final MapConstraint<? super V, ? super K> constraint) {
            this.constraint = Preconditions.checkNotNull(constraint);
        }
        
        @Override
        public void checkKeyValue(final K key, final V value) {
            this.constraint.checkKeyValue((Object)value, (Object)key);
        }
    }
    
    private static class ConstrainedMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable
    {
        final MapConstraint<? super K, ? super V> constraint;
        final Multimap<K, V> delegate;
        transient Collection<Map.Entry<K, V>> entries;
        transient Map<K, Collection<V>> asMap;
        
        public ConstrainedMultimap(final Multimap<K, V> delegate, final MapConstraint<? super K, ? super V> constraint) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.constraint = Preconditions.checkNotNull(constraint);
        }
        
        @Override
        protected Multimap<K, V> delegate() {
            return this.delegate;
        }
        
        @Override
        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> result = this.asMap;
            if (result == null) {
                final Map<K, Collection<V>> asMapDelegate = this.delegate.asMap();
                result = (this.asMap = new ForwardingMap<K, Collection<V>>() {
                    Set<Map.Entry<K, Collection<V>>> entrySet;
                    Collection<Collection<V>> values;
                    
                    @Override
                    protected Map<K, Collection<V>> delegate() {
                        return asMapDelegate;
                    }
                    
                    @Override
                    public Set<Map.Entry<K, Collection<V>>> entrySet() {
                        Set<Map.Entry<K, Collection<V>>> result = this.entrySet;
                        if (result == null) {
                            result = (this.entrySet = (Set<Map.Entry<K, Collection<V>>>)constrainedAsMapEntries((Set<Map.Entry<Object, Collection<Object>>>)asMapDelegate.entrySet(), ConstrainedMultimap.this.constraint));
                        }
                        return result;
                    }
                    
                    @Override
                    public Collection<V> get(final Object key) {
                        try {
                            final Collection<V> collection = ConstrainedMultimap.this.get(key);
                            return collection.isEmpty() ? null : collection;
                        }
                        catch (ClassCastException e) {
                            return null;
                        }
                    }
                    
                    @Override
                    public Collection<Collection<V>> values() {
                        Collection<Collection<V>> result = this.values;
                        if (result == null) {
                            result = (this.values = (Collection<Collection<V>>)new ConstrainedAsMapValues((Collection<Collection<Object>>)this.delegate().values(), this.entrySet()));
                        }
                        return result;
                    }
                    
                    @Override
                    public boolean containsValue(final Object o) {
                        return this.values().contains(o);
                    }
                });
            }
            return result;
        }
        
        @Override
        public Collection<Map.Entry<K, V>> entries() {
            Collection<Map.Entry<K, V>> result = this.entries;
            if (result == null) {
                result = (this.entries = (Collection<Map.Entry<K, V>>)constrainedEntries((Collection<Map.Entry<Object, Object>>)this.delegate.entries(), this.constraint));
            }
            return result;
        }
        
        @Override
        public Collection<V> get(final K key) {
            return Constraints.constrainedTypePreservingCollection(this.delegate.get(key), new Constraint<V>() {
                @Override
                public V checkElement(final V value) {
                    ConstrainedMultimap.this.constraint.checkKeyValue((Object)key, (Object)value);
                    return value;
                }
            });
        }
        
        @Override
        public boolean put(final K key, final V value) {
            this.constraint.checkKeyValue((Object)key, (Object)value);
            return this.delegate.put(key, value);
        }
        
        @Override
        public boolean putAll(final K key, final Iterable<? extends V> values) {
            return this.delegate.putAll(key, checkValues(key, (Iterable<?>)values, (MapConstraint<? super Object, ? super Object>)this.constraint));
        }
        
        @Override
        public boolean putAll(final Multimap<? extends K, ? extends V> multimap) {
            boolean changed = false;
            for (final Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
                changed |= this.put(entry.getKey(), entry.getValue());
            }
            return changed;
        }
        
        @Override
        public Collection<V> replaceValues(final K key, final Iterable<? extends V> values) {
            return this.delegate.replaceValues(key, checkValues(key, (Iterable<?>)values, (MapConstraint<? super Object, ? super Object>)this.constraint));
        }
    }
    
    private static class ConstrainedAsMapValues<K, V> extends ForwardingCollection<Collection<V>>
    {
        final Collection<Collection<V>> delegate;
        final Set<Map.Entry<K, Collection<V>>> entrySet;
        
        ConstrainedAsMapValues(final Collection<Collection<V>> delegate, final Set<Map.Entry<K, Collection<V>>> entrySet) {
            this.delegate = delegate;
            this.entrySet = entrySet;
        }
        
        @Override
        protected Collection<Collection<V>> delegate() {
            return this.delegate;
        }
        
        @Override
        public Iterator<Collection<V>> iterator() {
            final Iterator<Map.Entry<K, Collection<V>>> iterator = this.entrySet.iterator();
            return new Iterator<Collection<V>>() {
                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }
                
                @Override
                public Collection<V> next() {
                    return iterator.next().getValue();
                }
                
                @Override
                public void remove() {
                    iterator.remove();
                }
            };
        }
        
        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.standardToArray(array);
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.standardContains(o);
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            return this.standardContainsAll(c);
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.standardRemove(o);
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            return this.standardRemoveAll(c);
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            return this.standardRetainAll(c);
        }
    }
    
    private static class ConstrainedEntries<K, V> extends ForwardingCollection<Map.Entry<K, V>>
    {
        final MapConstraint<? super K, ? super V> constraint;
        final Collection<Map.Entry<K, V>> entries;
        
        ConstrainedEntries(final Collection<Map.Entry<K, V>> entries, final MapConstraint<? super K, ? super V> constraint) {
            this.entries = entries;
            this.constraint = constraint;
        }
        
        @Override
        protected Collection<Map.Entry<K, V>> delegate() {
            return this.entries;
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            final Iterator<Map.Entry<K, V>> iterator = this.entries.iterator();
            return new ForwardingIterator<Map.Entry<K, V>>() {
                @Override
                public Map.Entry<K, V> next() {
                    return (Map.Entry<K, V>)constrainedEntry((Map.Entry<Object, Object>)iterator.next(), ConstrainedEntries.this.constraint);
                }
                
                @Override
                protected Iterator<Map.Entry<K, V>> delegate() {
                    return iterator;
                }
            };
        }
        
        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.standardToArray(array);
        }
        
        @Override
        public boolean contains(final Object o) {
            return Maps.containsEntryImpl(this.delegate(), o);
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            return this.standardContainsAll(c);
        }
        
        @Override
        public boolean remove(final Object o) {
            return Maps.removeEntryImpl(this.delegate(), o);
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            return this.standardRemoveAll(c);
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            return this.standardRetainAll(c);
        }
    }
    
    static class ConstrainedEntrySet<K, V> extends ConstrainedEntries<K, V> implements Set<Map.Entry<K, V>>
    {
        ConstrainedEntrySet(final Set<Map.Entry<K, V>> entries, final MapConstraint<? super K, ? super V> constraint) {
            super(entries, constraint);
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            return Sets.equalsImpl(this, object);
        }
        
        @Override
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }
    
    static class ConstrainedAsMapEntries<K, V> extends ForwardingSet<Map.Entry<K, Collection<V>>>
    {
        private final MapConstraint<? super K, ? super V> constraint;
        private final Set<Map.Entry<K, Collection<V>>> entries;
        
        ConstrainedAsMapEntries(final Set<Map.Entry<K, Collection<V>>> entries, final MapConstraint<? super K, ? super V> constraint) {
            this.entries = entries;
            this.constraint = constraint;
        }
        
        @Override
        protected Set<Map.Entry<K, Collection<V>>> delegate() {
            return this.entries;
        }
        
        @Override
        public Iterator<Map.Entry<K, Collection<V>>> iterator() {
            final Iterator<Map.Entry<K, Collection<V>>> iterator = this.entries.iterator();
            return new ForwardingIterator<Map.Entry<K, Collection<V>>>() {
                @Override
                public Map.Entry<K, Collection<V>> next() {
                    return (Map.Entry<K, Collection<V>>)constrainedAsMapEntry((Map.Entry<Object, Collection<Object>>)iterator.next(), ConstrainedAsMapEntries.this.constraint);
                }
                
                @Override
                protected Iterator<Map.Entry<K, Collection<V>>> delegate() {
                    return iterator;
                }
            };
        }
        
        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.standardToArray(array);
        }
        
        @Override
        public boolean contains(final Object o) {
            return Maps.containsEntryImpl(this.delegate(), o);
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            return this.standardContainsAll(c);
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            return this.standardEquals(object);
        }
        
        @Override
        public int hashCode() {
            return this.standardHashCode();
        }
        
        @Override
        public boolean remove(final Object o) {
            return Maps.removeEntryImpl(this.delegate(), o);
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            return this.standardRemoveAll(c);
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            return this.standardRetainAll(c);
        }
    }
    
    private static class ConstrainedListMultimap<K, V> extends ConstrainedMultimap<K, V> implements ListMultimap<K, V>
    {
        ConstrainedListMultimap(final ListMultimap<K, V> delegate, final MapConstraint<? super K, ? super V> constraint) {
            super(delegate, constraint);
        }
        
        @Override
        public List<V> get(final K key) {
            return (List<V>)(List)super.get(key);
        }
        
        @Override
        public List<V> removeAll(final Object key) {
            return (List<V>)(List)super.removeAll(key);
        }
        
        @Override
        public List<V> replaceValues(final K key, final Iterable<? extends V> values) {
            return (List<V>)(List)super.replaceValues(key, values);
        }
    }
    
    private static class ConstrainedSetMultimap<K, V> extends ConstrainedMultimap<K, V> implements SetMultimap<K, V>
    {
        ConstrainedSetMultimap(final SetMultimap<K, V> delegate, final MapConstraint<? super K, ? super V> constraint) {
            super(delegate, constraint);
        }
        
        @Override
        public Set<V> get(final K key) {
            return (Set<V>)(Set)super.get(key);
        }
        
        @Override
        public Set<Map.Entry<K, V>> entries() {
            return (Set<Map.Entry<K, V>>)(Set)super.entries();
        }
        
        @Override
        public Set<V> removeAll(final Object key) {
            return (Set<V>)(Set)super.removeAll(key);
        }
        
        @Override
        public Set<V> replaceValues(final K key, final Iterable<? extends V> values) {
            return (Set<V>)(Set)super.replaceValues(key, values);
        }
    }
    
    private static class ConstrainedSortedSetMultimap<K, V> extends ConstrainedSetMultimap<K, V> implements SortedSetMultimap<K, V>
    {
        ConstrainedSortedSetMultimap(final SortedSetMultimap<K, V> delegate, final MapConstraint<? super K, ? super V> constraint) {
            super(delegate, constraint);
        }
        
        @Override
        public SortedSet<V> get(final K key) {
            return (SortedSet<V>)(SortedSet)super.get(key);
        }
        
        @Override
        public SortedSet<V> removeAll(final Object key) {
            return (SortedSet<V>)(SortedSet)super.removeAll(key);
        }
        
        @Override
        public SortedSet<V> replaceValues(final K key, final Iterable<? extends V> values) {
            return (SortedSet<V>)(SortedSet)super.replaceValues(key, values);
        }
        
        @Override
        public Comparator<? super V> valueComparator() {
            return (Comparator<? super V>)((SortedSetMultimap)this.delegate()).valueComparator();
        }
    }
}
