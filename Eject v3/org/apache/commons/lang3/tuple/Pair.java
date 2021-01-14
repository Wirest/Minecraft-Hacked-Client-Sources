package org.apache.commons.lang3.tuple;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.io.Serializable;
import java.util.Map.Entry;

public abstract class Pair<L, R>
        implements Map.Entry<L, R>, Comparable<Pair<L, R>>, Serializable {
    private static final long serialVersionUID = 4954918890077093841L;

    public static <L, R> Pair<L, R> of(L paramL, R paramR) {
        return new ImmutablePair(paramL, paramR);
    }

    public abstract L getLeft();

    public abstract R getRight();

    public final L getKey() {
        return (L) getLeft();
    }

    public R getValue() {
        return (R) getRight();
    }

    public int compareTo(Pair<L, R> paramPair) {
        return new CompareToBuilder().append(getLeft(), paramPair.getLeft()).append(getRight(), paramPair.getRight()).toComparison();
    }

    public boolean equals(Object paramObject) {
        if (paramObject == this) {
            return true;
        }
        if ((paramObject instanceof Map.Entry)) {
            Map.Entry localEntry = (Map.Entry) paramObject;
            return (ObjectUtils.equals(getKey(), localEntry.getKey())) && (ObjectUtils.equals(getValue(), localEntry.getValue()));
        }
        return false;
    }

    public int hashCode() {
        return (getKey() == null ? 0 : getKey().hashCode()) + (getValue() == null ? 0 : getValue().hashCode());
    }

    public String toString() {
        return '(' + getLeft() + ',' + getRight() + ')';
    }

    public String toString(String paramString) {
        return String.format(paramString, new Object[]{getLeft(), getRight()});
    }
}




