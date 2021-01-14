package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;

import javax.annotation.Nullable;
import java.util.Map.Entry;

@GwtCompatible
abstract class AbstractMapEntry<K, V>
        implements Map.Entry<K, V> {
    public abstract K getKey();

    public abstract V getValue();

    public V setValue(V paramV) {
        throw new UnsupportedOperationException();
    }

    public boolean equals(@Nullable Object paramObject) {
        if ((paramObject instanceof Map.Entry)) {
            Map.Entry localEntry = (Map.Entry) paramObject;
            return (Objects.equal(getKey(), localEntry.getKey())) && (Objects.equal(getValue(), localEntry.getValue()));
        }
        return false;
    }

    public int hashCode() {
        Object localObject1 = getKey();
        Object localObject2 = getValue();
        return (localObject1 == null ? 0 : localObject1.hashCode()) + (localObject2 == null ? 0 : localObject2.hashCode());
    }

    public String toString() {
        return getKey() + "=" + getValue();
    }
}




