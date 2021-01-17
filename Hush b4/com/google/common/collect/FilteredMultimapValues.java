// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Predicates;
import java.util.Collection;
import com.google.common.base.Predicate;
import com.google.common.base.Objects;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import java.util.AbstractCollection;

@GwtCompatible
final class FilteredMultimapValues<K, V> extends AbstractCollection<V>
{
    private final FilteredMultimap<K, V> multimap;
    
    FilteredMultimapValues(final FilteredMultimap<K, V> multimap) {
        this.multimap = Preconditions.checkNotNull(multimap);
    }
    
    @Override
    public Iterator<V> iterator() {
        return Maps.valueIterator((Iterator<Map.Entry<Object, V>>)this.multimap.entries().iterator());
    }
    
    @Override
    public boolean contains(@Nullable final Object o) {
        return this.multimap.containsValue(o);
    }
    
    @Override
    public int size() {
        return this.multimap.size();
    }
    
    @Override
    public boolean remove(@Nullable final Object o) {
        final Predicate<? super Map.Entry<K, V>> entryPredicate = this.multimap.entryPredicate();
        final Iterator<Map.Entry<K, V>> unfilteredItr = this.multimap.unfiltered().entries().iterator();
        while (unfilteredItr.hasNext()) {
            final Map.Entry<K, V> entry = unfilteredItr.next();
            if (entryPredicate.apply(entry) && Objects.equal(entry.getValue(), o)) {
                unfilteredItr.remove();
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), (Predicate<? super Map.Entry<K, V>>)Maps.valuePredicateOnEntries(Predicates.in(c))));
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), (Predicate<? super Map.Entry<K, V>>)Maps.valuePredicateOnEntries(Predicates.not(Predicates.in((Collection<? extends V>)c)))));
    }
    
    @Override
    public void clear() {
        this.multimap.clear();
    }
}
