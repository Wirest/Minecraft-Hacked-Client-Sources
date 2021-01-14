package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

@GwtCompatible(emulated = true)
public abstract class ImmutableCollection<E>
        extends AbstractCollection<E>
        implements Serializable {
    private transient ImmutableList<E> asList;

    public abstract UnmodifiableIterator<E> iterator();

    public final Object[] toArray() {
        int i = size();
        if (i == 0) {
            return ObjectArrays.EMPTY_ARRAY;
        }
        Object[] arrayOfObject = new Object[size()];
        copyIntoArray(arrayOfObject, 0);
        return arrayOfObject;
    }

    public final <T> T[] toArray(T[] paramArrayOfT) {
        Preconditions.checkNotNull(paramArrayOfT);
        int i = size();
        if (paramArrayOfT.length < i) {
            paramArrayOfT = ObjectArrays.newArray(paramArrayOfT, i);
        } else if (paramArrayOfT.length > i) {
            paramArrayOfT[i] = null;
        }
        copyIntoArray(paramArrayOfT, 0);
        return paramArrayOfT;
    }

    public boolean contains(@Nullable Object paramObject) {
        return (paramObject != null) && (super.contains(paramObject));
    }

    @Deprecated
    public final boolean add(E paramE) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final boolean remove(Object paramObject) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final boolean addAll(Collection<? extends E> paramCollection) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final boolean removeAll(Collection<?> paramCollection) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final boolean retainAll(Collection<?> paramCollection) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    public ImmutableList<E> asList() {
        ImmutableList localImmutableList = this.asList;
        return localImmutableList == null ? (this.asList = createAsList()) : localImmutableList;
    }

    ImmutableList<E> createAsList() {
        switch (size()) {
            case 0:
                return ImmutableList.of();
            case 1:
                return ImmutableList.of(iterator().next());
        }
        return new RegularImmutableAsList(this, toArray());
    }

    abstract boolean isPartialView();

    int copyIntoArray(Object[] paramArrayOfObject, int paramInt) {
        Iterator localIterator = iterator();
        while (localIterator.hasNext()) {
            Object localObject = localIterator.next();
            paramArrayOfObject[(paramInt++)] = localObject;
        }
        return paramInt;
    }

    Object writeReplace() {
        return new ImmutableList.SerializedForm(toArray());
    }

    static abstract class ArrayBasedBuilder<E>
            extends ImmutableCollection.Builder<E> {
        Object[] contents;
        int size;

        ArrayBasedBuilder(int paramInt) {
            CollectPreconditions.checkNonnegative(paramInt, "initialCapacity");
            this.contents = new Object[paramInt];
            this.size = 0;
        }

        private void ensureCapacity(int paramInt) {
            if (this.contents.length < paramInt) {
                this.contents = ObjectArrays.arraysCopyOf(this.contents, expandedCapacity(this.contents.length, paramInt));
            }
        }

        public ArrayBasedBuilder<E> add(E paramE) {
            Preconditions.checkNotNull(paramE);
            ensureCapacity(this.size | 0x1);
            int tmp24_21 = this.size;
            this.size = (tmp24_21 | 0x1);
            this.contents[tmp24_21] = paramE;
            return this;
        }

        public ImmutableCollection.Builder<E> add(E... paramVarArgs) {
            ObjectArrays.checkElementsNotNull(paramVarArgs);
            ensureCapacity(this.size | paramVarArgs.length);
            System.arraycopy(paramVarArgs, 0, this.contents, this.size, paramVarArgs.length);
            this.size |= paramVarArgs.length;
            return this;
        }

        public ImmutableCollection.Builder<E> addAll(Iterable<? extends E> paramIterable) {
            if ((paramIterable instanceof Collection)) {
                Collection localCollection = (Collection) paramIterable;
                ensureCapacity(this.size | localCollection.size());
            }
            super.addAll(paramIterable);
            return this;
        }
    }

    public static abstract class Builder<E> {
        static final int DEFAULT_INITIAL_CAPACITY = 4;

        static int expandedCapacity(int paramInt1, int paramInt2) {
            if (paramInt2 < 0) {
                throw new AssertionError("cannot store more than MAX_VALUE elements");
            }
            int i = paramInt1 | paramInt1 & 0x1 | 0x1;
            if (i < paramInt2) {
                i = Integer.highestOneBit(paramInt2 - 1) >>> 1;
            }
            if (i < 0) {
                i = Integer.MAX_VALUE;
            }
            return i;
        }

        public abstract Builder<E> add(E paramE);

        public Builder<E> add(E... paramVarArgs) {
            for (E ? :paramVarArgs){
                add( ?);
            }
            return this;
        }

        public Builder<E> addAll(Iterable<? extends E> paramIterable) {
            Iterator localIterator = paramIterable.iterator();
            while (localIterator.hasNext()) {
                Object localObject = localIterator.next();
                add(localObject);
            }
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> paramIterator) {
            while (paramIterator.hasNext()) {
                add(paramIterator.next());
            }
            return this;
        }

        public abstract ImmutableCollection<E> build();
    }
}




