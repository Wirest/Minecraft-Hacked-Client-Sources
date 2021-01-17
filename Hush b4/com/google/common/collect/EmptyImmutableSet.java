// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
final class EmptyImmutableSet extends ImmutableSet<Object>
{
    static final EmptyImmutableSet INSTANCE;
    private static final long serialVersionUID = 0L;
    
    private EmptyImmutableSet() {
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public boolean contains(@Nullable final Object target) {
        return false;
    }
    
    @Override
    public boolean containsAll(final Collection<?> targets) {
        return targets.isEmpty();
    }
    
    @Override
    public UnmodifiableIterator<Object> iterator() {
        return Iterators.emptyIterator();
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        return offset;
    }
    
    @Override
    public ImmutableList<Object> asList() {
        return ImmutableList.of();
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object instanceof Set) {
            final Set<?> that = (Set<?>)object;
            return that.isEmpty();
        }
        return false;
    }
    
    @Override
    public final int hashCode() {
        return 0;
    }
    
    @Override
    boolean isHashCodeFast() {
        return true;
    }
    
    @Override
    public String toString() {
        return "[]";
    }
    
    Object readResolve() {
        return EmptyImmutableSet.INSTANCE;
    }
    
    static {
        INSTANCE = new EmptyImmutableSet();
    }
}
