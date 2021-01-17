// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.ObjectInputStream;

final class Serialization
{
    private Serialization() {
    }
    
    static int readCount(final ObjectInputStream stream) throws IOException {
        return stream.readInt();
    }
    
    static <K, V> void writeMap(final Map<K, V> map, final ObjectOutputStream stream) throws IOException {
        stream.writeInt(map.size());
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            stream.writeObject(entry.getKey());
            stream.writeObject(entry.getValue());
        }
    }
    
    static <K, V> void populateMap(final Map<K, V> map, final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        final int size = stream.readInt();
        populateMap(map, stream, size);
    }
    
    static <K, V> void populateMap(final Map<K, V> map, final ObjectInputStream stream, final int size) throws IOException, ClassNotFoundException {
        for (int i = 0; i < size; ++i) {
            final K key = (K)stream.readObject();
            final V value = (V)stream.readObject();
            map.put(key, value);
        }
    }
    
    static <E> void writeMultiset(final Multiset<E> multiset, final ObjectOutputStream stream) throws IOException {
        final int entryCount = multiset.entrySet().size();
        stream.writeInt(entryCount);
        for (final Multiset.Entry<E> entry : multiset.entrySet()) {
            stream.writeObject(entry.getElement());
            stream.writeInt(entry.getCount());
        }
    }
    
    static <E> void populateMultiset(final Multiset<E> multiset, final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        final int distinctElements = stream.readInt();
        populateMultiset(multiset, stream, distinctElements);
    }
    
    static <E> void populateMultiset(final Multiset<E> multiset, final ObjectInputStream stream, final int distinctElements) throws IOException, ClassNotFoundException {
        for (int i = 0; i < distinctElements; ++i) {
            final E element = (E)stream.readObject();
            final int count = stream.readInt();
            multiset.add(element, count);
        }
    }
    
    static <K, V> void writeMultimap(final Multimap<K, V> multimap, final ObjectOutputStream stream) throws IOException {
        stream.writeInt(multimap.asMap().size());
        for (final Map.Entry<K, Collection<V>> entry : multimap.asMap().entrySet()) {
            stream.writeObject(entry.getKey());
            stream.writeInt(entry.getValue().size());
            for (final V value : entry.getValue()) {
                stream.writeObject(value);
            }
        }
    }
    
    static <K, V> void populateMultimap(final Multimap<K, V> multimap, final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        final int distinctKeys = stream.readInt();
        populateMultimap(multimap, stream, distinctKeys);
    }
    
    static <K, V> void populateMultimap(final Multimap<K, V> multimap, final ObjectInputStream stream, final int distinctKeys) throws IOException, ClassNotFoundException {
        for (int i = 0; i < distinctKeys; ++i) {
            final K key = (K)stream.readObject();
            final Collection<V> values = multimap.get(key);
            for (int valueCount = stream.readInt(), j = 0; j < valueCount; ++j) {
                final V value = (V)stream.readObject();
                values.add(value);
            }
        }
    }
    
    static <T> FieldSetter<T> getFieldSetter(final Class<T> clazz, final String fieldName) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            return new FieldSetter<T>(field);
        }
        catch (NoSuchFieldException e) {
            throw new AssertionError((Object)e);
        }
    }
    
    static final class FieldSetter<T>
    {
        private final Field field;
        
        private FieldSetter(final Field field) {
            (this.field = field).setAccessible(true);
        }
        
        void set(final T instance, final Object value) {
            try {
                this.field.set(instance, value);
            }
            catch (IllegalAccessException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        void set(final T instance, final int value) {
            try {
                this.field.set(instance, value);
            }
            catch (IllegalAccessException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
    }
}
