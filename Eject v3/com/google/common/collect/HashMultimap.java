package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Set;

@GwtCompatible(serializable = true, emulated = true)
public final class HashMultimap<K, V>
        extends AbstractSetMultimap<K, V> {
    private static final int DEFAULT_VALUES_PER_KEY = 2;
    @GwtIncompatible("Not needed in emulated source")
    private static final long serialVersionUID = 0L;
    @VisibleForTesting
    transient int expectedValuesPerKey = 2;

    private HashMultimap() {
        super(new HashMap());
    }

    private HashMultimap(int paramInt1, int paramInt2) {
        super(Maps.newHashMapWithExpectedSize(paramInt1));
        Preconditions.checkArgument(paramInt2 >= 0);
        this.expectedValuesPerKey = paramInt2;
    }

    private HashMultimap(Multimap<? extends K, ? extends V> paramMultimap) {
        super(Maps.newHashMapWithExpectedSize(paramMultimap.keySet().size()));
        putAll(paramMultimap);
    }

    public static <K, V> HashMultimap<K, V> create() {
        return new HashMultimap();
    }

    public static <K, V> HashMultimap<K, V> create(int paramInt1, int paramInt2) {
        return new HashMultimap(paramInt1, paramInt2);
    }

    public static <K, V> HashMultimap<K, V> create(Multimap<? extends K, ? extends V> paramMultimap) {
        return new HashMultimap(paramMultimap);
    }

    Set<V> createCollection() {
        return Sets.newHashSetWithExpectedSize(this.expectedValuesPerKey);
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream paramObjectOutputStream)
            throws IOException {
        paramObjectOutputStream.defaultWriteObject();
        paramObjectOutputStream.writeInt(this.expectedValuesPerKey);
        Serialization.writeMultimap(this, paramObjectOutputStream);
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream paramObjectInputStream)
            throws IOException, ClassNotFoundException {
        paramObjectInputStream.defaultReadObject();
        this.expectedValuesPerKey = paramObjectInputStream.readInt();
        int i = Serialization.readCount(paramObjectInputStream);
        HashMap localHashMap = Maps.newHashMapWithExpectedSize(i);
        setMap(localHashMap);
        Serialization.populateMultimap(this, paramObjectInputStream, i);
    }
}




