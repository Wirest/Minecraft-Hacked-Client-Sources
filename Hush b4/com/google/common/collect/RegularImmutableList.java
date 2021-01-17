// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.ListIterator;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
class RegularImmutableList<E> extends ImmutableList<E>
{
    private final transient int offset;
    private final transient int size;
    private final transient Object[] array;
    
    RegularImmutableList(final Object[] array, final int offset, final int size) {
        this.offset = offset;
        this.size = size;
        this.array = array;
    }
    
    RegularImmutableList(final Object[] array) {
        this(array, 0, array.length);
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    boolean isPartialView() {
        return this.size != this.array.length;
    }
    
    @Override
    int copyIntoArray(final Object[] dst, final int dstOff) {
        System.arraycopy(this.array, this.offset, dst, dstOff, this.size);
        return dstOff + this.size;
    }
    
    @Override
    public E get(final int index) {
        Preconditions.checkElementIndex(index, this.size);
        return (E)this.array[index + this.offset];
    }
    
    @Override
    public int indexOf(@Nullable final Object object) {
        if (object == null) {
            return -1;
        }
        for (int i = 0; i < this.size; ++i) {
            if (this.array[this.offset + i].equals(object)) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(@Nullable final Object object) {
        if (object == null) {
            return -1;
        }
        for (int i = this.size - 1; i >= 0; --i) {
            if (this.array[this.offset + i].equals(object)) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    ImmutableList<E> subListUnchecked(final int fromIndex, final int toIndex) {
        return new RegularImmutableList(this.array, this.offset + fromIndex, toIndex - fromIndex);
    }
    
    @Override
    public UnmodifiableListIterator<E> listIterator(final int index) {
        return Iterators.forArray(this.array, this.offset, this.size, index);
    }
}
