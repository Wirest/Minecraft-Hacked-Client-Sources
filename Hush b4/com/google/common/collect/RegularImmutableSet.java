// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableSet<E> extends ImmutableSet<E>
{
    private final Object[] elements;
    @VisibleForTesting
    final transient Object[] table;
    private final transient int mask;
    private final transient int hashCode;
    
    RegularImmutableSet(final Object[] elements, final int hashCode, final Object[] table, final int mask) {
        this.elements = elements;
        this.table = table;
        this.mask = mask;
        this.hashCode = hashCode;
    }
    
    @Override
    public boolean contains(final Object target) {
        if (target == null) {
            return false;
        }
        int i = Hashing.smear(target.hashCode());
        while (true) {
            final Object candidate = this.table[i & this.mask];
            if (candidate == null) {
                return false;
            }
            if (candidate.equals(target)) {
                return true;
            }
            ++i;
        }
    }
    
    @Override
    public int size() {
        return this.elements.length;
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.forArray((E[])this.elements);
    }
    
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        System.arraycopy(this.elements, 0, dst, offset, this.elements.length);
        return offset + this.elements.length;
    }
    
    @Override
    ImmutableList<E> createAsList() {
        return new RegularImmutableAsList<E>(this, this.elements);
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.hashCode;
    }
    
    @Override
    boolean isHashCodeFast() {
        return true;
    }
}
