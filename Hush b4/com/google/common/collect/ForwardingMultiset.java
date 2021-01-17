// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.Set;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract class ForwardingMultiset<E> extends ForwardingCollection<E> implements Multiset<E>
{
    protected ForwardingMultiset() {
    }
    
    @Override
    protected abstract Multiset<E> delegate();
    
    @Override
    public int count(final Object element) {
        return this.delegate().count(element);
    }
    
    @Override
    public int add(final E element, final int occurrences) {
        return this.delegate().add(element, occurrences);
    }
    
    @Override
    public int remove(final Object element, final int occurrences) {
        return this.delegate().remove(element, occurrences);
    }
    
    @Override
    public Set<E> elementSet() {
        return this.delegate().elementSet();
    }
    
    @Override
    public Set<Entry<E>> entrySet() {
        return this.delegate().entrySet();
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        return object == this || this.delegate().equals(object);
    }
    
    @Override
    public int hashCode() {
        return this.delegate().hashCode();
    }
    
    @Override
    public int setCount(final E element, final int count) {
        return this.delegate().setCount(element, count);
    }
    
    @Override
    public boolean setCount(final E element, final int oldCount, final int newCount) {
        return this.delegate().setCount(element, oldCount, newCount);
    }
    
    @Override
    protected boolean standardContains(@Nullable final Object object) {
        return this.count(object) > 0;
    }
    
    @Override
    protected void standardClear() {
        Iterators.clear(this.entrySet().iterator());
    }
    
    @Beta
    protected int standardCount(@Nullable final Object object) {
        for (final Entry<?> entry : this.entrySet()) {
            if (Objects.equal(entry.getElement(), object)) {
                return entry.getCount();
            }
        }
        return 0;
    }
    
    protected boolean standardAdd(final E element) {
        this.add(element, 1);
        return true;
    }
    
    @Beta
    @Override
    protected boolean standardAddAll(final Collection<? extends E> elementsToAdd) {
        return Multisets.addAllImpl((Multiset<Object>)this, elementsToAdd);
    }
    
    @Override
    protected boolean standardRemove(final Object element) {
        return this.remove(element, 1) > 0;
    }
    
    @Override
    protected boolean standardRemoveAll(final Collection<?> elementsToRemove) {
        return Multisets.removeAllImpl(this, elementsToRemove);
    }
    
    @Override
    protected boolean standardRetainAll(final Collection<?> elementsToRetain) {
        return Multisets.retainAllImpl(this, elementsToRetain);
    }
    
    protected int standardSetCount(final E element, final int count) {
        return Multisets.setCountImpl(this, element, count);
    }
    
    protected boolean standardSetCount(final E element, final int oldCount, final int newCount) {
        return Multisets.setCountImpl(this, element, oldCount, newCount);
    }
    
    protected Iterator<E> standardIterator() {
        return Multisets.iteratorImpl((Multiset<E>)this);
    }
    
    protected int standardSize() {
        return Multisets.sizeImpl(this);
    }
    
    protected boolean standardEquals(@Nullable final Object object) {
        return Multisets.equalsImpl(this, object);
    }
    
    protected int standardHashCode() {
        return this.entrySet().hashCode();
    }
    
    @Override
    protected String standardToString() {
        return this.entrySet().toString();
    }
    
    @Beta
    protected class StandardElementSet extends Multisets.ElementSet<E>
    {
        public StandardElementSet() {
        }
        
        @Override
        Multiset<E> multiset() {
            return (Multiset<E>)ForwardingMultiset.this;
        }
    }
}
