// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.EnumSet;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedList;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;
import com.google.common.base.Supplier;
import java.util.EnumMap;
import java.util.TreeMap;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public abstract class MultimapBuilder<K0, V0>
{
    private static final int DEFAULT_EXPECTED_KEYS = 8;
    
    private MultimapBuilder() {
    }
    
    public static MultimapBuilderWithKeys<Object> hashKeys() {
        return hashKeys(8);
    }
    
    public static MultimapBuilderWithKeys<Object> hashKeys(final int expectedKeys) {
        CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
        return new MultimapBuilderWithKeys<Object>() {
            @Override
             <K, V> Map<K, Collection<V>> createMap() {
                return new HashMap<K, Collection<V>>(expectedKeys);
            }
        };
    }
    
    public static MultimapBuilderWithKeys<Object> linkedHashKeys() {
        return linkedHashKeys(8);
    }
    
    public static MultimapBuilderWithKeys<Object> linkedHashKeys(final int expectedKeys) {
        CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
        return new MultimapBuilderWithKeys<Object>() {
            @Override
             <K, V> Map<K, Collection<V>> createMap() {
                return new LinkedHashMap<K, Collection<V>>(expectedKeys);
            }
        };
    }
    
    public static MultimapBuilderWithKeys<Comparable> treeKeys() {
        return (MultimapBuilderWithKeys<Comparable>)treeKeys(Ordering.natural());
    }
    
    public static <K0> MultimapBuilderWithKeys<K0> treeKeys(final Comparator<K0> comparator) {
        Preconditions.checkNotNull(comparator);
        return new MultimapBuilderWithKeys<K0>() {
            @Override
             <K extends K0, V> Map<K, Collection<V>> createMap() {
                return new TreeMap<K, Collection<V>>(comparator);
            }
        };
    }
    
    public static <K0 extends Enum<K0>> MultimapBuilderWithKeys<K0> enumKeys(final Class<K0> keyClass) {
        Preconditions.checkNotNull(keyClass);
        return new MultimapBuilderWithKeys<K0>() {
            @Override
             <K extends K0, V> Map<K, Collection<V>> createMap() {
                return new EnumMap<K, Collection<V>>(keyClass);
            }
        };
    }
    
    public abstract <K extends K0, V extends V0> Multimap<K, V> build();
    
    public <K extends K0, V extends V0> Multimap<K, V> build(final Multimap<? extends K, ? extends V> multimap) {
        final Multimap<K, V> result = this.build();
        result.putAll(multimap);
        return result;
    }
    
    private static final class ArrayListSupplier<V> implements Supplier<List<V>>, Serializable
    {
        private final int expectedValuesPerKey;
        
        ArrayListSupplier(final int expectedValuesPerKey) {
            this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
        }
        
        @Override
        public List<V> get() {
            return new ArrayList<V>(this.expectedValuesPerKey);
        }
    }
    
    private enum LinkedListSupplier implements Supplier<List<Object>>
    {
        INSTANCE;
        
        public static <V> Supplier<List<V>> instance() {
            final Supplier<List<V>> result = (Supplier<List<V>>)LinkedListSupplier.INSTANCE;
            return result;
        }
        
        @Override
        public List<Object> get() {
            return new LinkedList<Object>();
        }
    }
    
    private static final class HashSetSupplier<V> implements Supplier<Set<V>>, Serializable
    {
        private final int expectedValuesPerKey;
        
        HashSetSupplier(final int expectedValuesPerKey) {
            this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
        }
        
        @Override
        public Set<V> get() {
            return new HashSet<V>(this.expectedValuesPerKey);
        }
    }
    
    private static final class LinkedHashSetSupplier<V> implements Supplier<Set<V>>, Serializable
    {
        private final int expectedValuesPerKey;
        
        LinkedHashSetSupplier(final int expectedValuesPerKey) {
            this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
        }
        
        @Override
        public Set<V> get() {
            return new LinkedHashSet<V>(this.expectedValuesPerKey);
        }
    }
    
    private static final class TreeSetSupplier<V> implements Supplier<SortedSet<V>>, Serializable
    {
        private final Comparator<? super V> comparator;
        
        TreeSetSupplier(final Comparator<? super V> comparator) {
            this.comparator = Preconditions.checkNotNull(comparator);
        }
        
        @Override
        public SortedSet<V> get() {
            return new TreeSet<V>(this.comparator);
        }
    }
    
    private static final class EnumSetSupplier<V extends Enum<V>> implements Supplier<Set<V>>, Serializable
    {
        private final Class<V> clazz;
        
        EnumSetSupplier(final Class<V> clazz) {
            this.clazz = Preconditions.checkNotNull(clazz);
        }
        
        @Override
        public Set<V> get() {
            return EnumSet.noneOf(this.clazz);
        }
    }
    
    public abstract static class MultimapBuilderWithKeys<K0>
    {
        private static final int DEFAULT_EXPECTED_VALUES_PER_KEY = 2;
        
        MultimapBuilderWithKeys() {
        }
        
        abstract <K extends K0, V> Map<K, Collection<V>> createMap();
        
        public ListMultimapBuilder<K0, Object> arrayListValues() {
            return this.arrayListValues(2);
        }
        
        public ListMultimapBuilder<K0, Object> arrayListValues(final int expectedValuesPerKey) {
            CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
            return new ListMultimapBuilder<K0, Object>() {
                @Override
                public <K extends K0, V> ListMultimap<K, V> build() {
                    return Multimaps.newListMultimap(MultimapBuilderWithKeys.this.createMap(), (Supplier<? extends List<V>>)new ArrayListSupplier(expectedValuesPerKey));
                }
            };
        }
        
        public ListMultimapBuilder<K0, Object> linkedListValues() {
            return new ListMultimapBuilder<K0, Object>() {
                @Override
                public <K extends K0, V> ListMultimap<K, V> build() {
                    return Multimaps.newListMultimap(MultimapBuilderWithKeys.this.createMap(), (Supplier<? extends List<V>>)LinkedListSupplier.instance());
                }
            };
        }
        
        public SetMultimapBuilder<K0, Object> hashSetValues() {
            return this.hashSetValues(2);
        }
        
        public SetMultimapBuilder<K0, Object> hashSetValues(final int expectedValuesPerKey) {
            CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
            return new SetMultimapBuilder<K0, Object>() {
                @Override
                public <K extends K0, V> SetMultimap<K, V> build() {
                    return Multimaps.newSetMultimap(MultimapBuilderWithKeys.this.createMap(), (Supplier<? extends Set<V>>)new HashSetSupplier(expectedValuesPerKey));
                }
            };
        }
        
        public SetMultimapBuilder<K0, Object> linkedHashSetValues() {
            return this.linkedHashSetValues(2);
        }
        
        public SetMultimapBuilder<K0, Object> linkedHashSetValues(final int expectedValuesPerKey) {
            CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
            return new SetMultimapBuilder<K0, Object>() {
                @Override
                public <K extends K0, V> SetMultimap<K, V> build() {
                    return Multimaps.newSetMultimap(MultimapBuilderWithKeys.this.createMap(), (Supplier<? extends Set<V>>)new LinkedHashSetSupplier(expectedValuesPerKey));
                }
            };
        }
        
        public SortedSetMultimapBuilder<K0, Comparable> treeSetValues() {
            return (SortedSetMultimapBuilder<K0, Comparable>)this.treeSetValues(Ordering.natural());
        }
        
        public <V0> SortedSetMultimapBuilder<K0, V0> treeSetValues(final Comparator<V0> comparator) {
            Preconditions.checkNotNull(comparator, (Object)"comparator");
            return new SortedSetMultimapBuilder<K0, V0>() {
                @Override
                public <K extends K0, V extends V0> SortedSetMultimap<K, V> build() {
                    return Multimaps.newSortedSetMultimap(MultimapBuilderWithKeys.this.createMap(), (Supplier<? extends SortedSet<V>>)new TreeSetSupplier(comparator));
                }
            };
        }
        
        public <V0 extends Enum<V0>> SetMultimapBuilder<K0, V0> enumSetValues(final Class<V0> valueClass) {
            Preconditions.checkNotNull(valueClass, (Object)"valueClass");
            return new SetMultimapBuilder<K0, V0>() {
                @Override
                public <K extends K0, V extends V0> SetMultimap<K, V> build() {
                    final Supplier<Set<V>> factory = new EnumSetSupplier<V>(valueClass);
                    return Multimaps.newSetMultimap(MultimapBuilderWithKeys.this.createMap(), factory);
                }
            };
        }
    }
    
    public abstract static class ListMultimapBuilder<K0, V0> extends MultimapBuilder<K0, V0>
    {
        ListMultimapBuilder() {
            super(null);
        }
        
        @Override
        public abstract <K extends K0, V extends V0> ListMultimap<K, V> build();
        
        @Override
        public <K extends K0, V extends V0> ListMultimap<K, V> build(final Multimap<? extends K, ? extends V> multimap) {
            return (ListMultimap<K, V>)(ListMultimap)super.build(multimap);
        }
    }
    
    public abstract static class SetMultimapBuilder<K0, V0> extends MultimapBuilder<K0, V0>
    {
        SetMultimapBuilder() {
            super(null);
        }
        
        @Override
        public abstract <K extends K0, V extends V0> SetMultimap<K, V> build();
        
        @Override
        public <K extends K0, V extends V0> SetMultimap<K, V> build(final Multimap<? extends K, ? extends V> multimap) {
            return (SetMultimap<K, V>)(SetMultimap)super.build(multimap);
        }
    }
    
    public abstract static class SortedSetMultimapBuilder<K0, V0> extends SetMultimapBuilder<K0, V0>
    {
        SortedSetMultimapBuilder() {
        }
        
        @Override
        public abstract <K extends K0, V extends V0> SortedSetMultimap<K, V> build();
        
        @Override
        public <K extends K0, V extends V0> SortedSetMultimap<K, V> build(final Multimap<? extends K, ? extends V> multimap) {
            return (SortedSetMultimap<K, V>)(SortedSetMultimap)super.build(multimap);
        }
    }
}
