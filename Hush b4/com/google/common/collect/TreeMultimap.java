// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.Map;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NavigableSet;
import java.util.NavigableMap;
import java.util.Collection;
import javax.annotation.Nullable;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.SortedMap;
import java.util.TreeMap;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
public class TreeMultimap<K, V> extends AbstractSortedKeySortedSetMultimap<K, V>
{
    private transient Comparator<? super K> keyComparator;
    private transient Comparator<? super V> valueComparator;
    @GwtIncompatible("not needed in emulated source")
    private static final long serialVersionUID = 0L;
    
    public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create() {
        return new TreeMultimap<K, V>(Ordering.natural(), Ordering.natural());
    }
    
    public static <K, V> TreeMultimap<K, V> create(final Comparator<? super K> keyComparator, final Comparator<? super V> valueComparator) {
        return new TreeMultimap<K, V>(Preconditions.checkNotNull(keyComparator), Preconditions.checkNotNull(valueComparator));
    }
    
    public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create(final Multimap<? extends K, ? extends V> multimap) {
        return new TreeMultimap<K, V>(Ordering.natural(), Ordering.natural(), multimap);
    }
    
    TreeMultimap(final Comparator<? super K> keyComparator, final Comparator<? super V> valueComparator) {
        super(new TreeMap(keyComparator));
        this.keyComparator = keyComparator;
        this.valueComparator = valueComparator;
    }
    
    private TreeMultimap(final Comparator<? super K> keyComparator, final Comparator<? super V> valueComparator, final Multimap<? extends K, ? extends V> multimap) {
        this(keyComparator, valueComparator);
        this.putAll(multimap);
    }
    
    @Override
    SortedSet<V> createCollection() {
        return new TreeSet<V>(this.valueComparator);
    }
    
    @Override
    Collection<V> createCollection(@Nullable final K key) {
        if (key == null) {
            this.keyComparator().compare((Object)key, (Object)key);
        }
        return super.createCollection(key);
    }
    
    public Comparator<? super K> keyComparator() {
        return this.keyComparator;
    }
    
    @Override
    public Comparator<? super V> valueComparator() {
        return this.valueComparator;
    }
    
    @GwtIncompatible("NavigableMap")
    @Override
    NavigableMap<K, Collection<V>> backingMap() {
        return (NavigableMap<K, Collection<V>>)(NavigableMap)super.backingMap();
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    public NavigableSet<V> get(@Nullable final K key) {
        return (NavigableSet<V>)(NavigableSet)super.get(key);
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    Collection<V> unmodifiableCollectionSubclass(final Collection<V> collection) {
        return (Collection<V>)Sets.unmodifiableNavigableSet((NavigableSet<Object>)(NavigableSet)collection);
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    Collection<V> wrapCollection(final K key, final Collection<V> collection) {
        return (Collection<V>)new WrappedNavigableSet((K)key, (NavigableSet)collection, null);
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    public NavigableSet<K> keySet() {
        return (NavigableSet<K>)(NavigableSet)super.keySet();
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    NavigableSet<K> createKeySet() {
        return (NavigableSet<K>)new NavigableKeySet(this.backingMap());
    }
    
    @GwtIncompatible("NavigableMap")
    @Override
    public NavigableMap<K, Collection<V>> asMap() {
        return (NavigableMap<K, Collection<V>>)(NavigableMap)super.asMap();
    }
    
    @GwtIncompatible("NavigableMap")
    @Override
    NavigableMap<K, Collection<V>> createAsMap() {
        return (NavigableMap<K, Collection<V>>)new NavigableAsMap(this.backingMap());
    }
    
    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.keyComparator());
        stream.writeObject(this.valueComparator());
        Serialization.writeMultimap((Multimap<Object, Object>)this, stream);
    }
    
    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.keyComparator = Preconditions.checkNotNull(stream.readObject());
        this.valueComparator = Preconditions.checkNotNull(stream.readObject());
        this.setMap(new TreeMap<K, Collection<V>>(this.keyComparator));
        Serialization.populateMultimap((Multimap<Object, Object>)this, stream);
    }
}
