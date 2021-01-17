// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Objects;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.annotations.GwtCompatible;
import java.util.Collection;

@GwtCompatible
public abstract class ForwardingCollection<E> extends ForwardingObject implements Collection<E>
{
    protected ForwardingCollection() {
    }
    
    @Override
    protected abstract Collection<E> delegate();
    
    @Override
    public Iterator<E> iterator() {
        return this.delegate().iterator();
    }
    
    @Override
    public int size() {
        return this.delegate().size();
    }
    
    @Override
    public boolean removeAll(final Collection<?> collection) {
        return this.delegate().removeAll(collection);
    }
    
    @Override
    public boolean isEmpty() {
        return this.delegate().isEmpty();
    }
    
    @Override
    public boolean contains(final Object object) {
        return this.delegate().contains(object);
    }
    
    @Override
    public boolean add(final E element) {
        return this.delegate().add(element);
    }
    
    @Override
    public boolean remove(final Object object) {
        return this.delegate().remove(object);
    }
    
    @Override
    public boolean containsAll(final Collection<?> collection) {
        return this.delegate().containsAll(collection);
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        return this.delegate().addAll(collection);
    }
    
    @Override
    public boolean retainAll(final Collection<?> collection) {
        return this.delegate().retainAll(collection);
    }
    
    @Override
    public void clear() {
        this.delegate().clear();
    }
    
    @Override
    public Object[] toArray() {
        return this.delegate().toArray();
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        return this.delegate().toArray(array);
    }
    
    protected boolean standardContains(@Nullable final Object object) {
        return Iterators.contains(this.iterator(), object);
    }
    
    protected boolean standardContainsAll(final Collection<?> collection) {
        return Collections2.containsAllImpl(this, collection);
    }
    
    protected boolean standardAddAll(final Collection<? extends E> collection) {
        return Iterators.addAll((Collection<Object>)this, collection.iterator());
    }
    
    protected boolean standardRemove(@Nullable final Object object) {
        final Iterator<E> iterator = this.iterator();
        while (iterator.hasNext()) {
            if (Objects.equal(iterator.next(), object)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    protected boolean standardRemoveAll(final Collection<?> collection) {
        return Iterators.removeAll(this.iterator(), collection);
    }
    
    protected boolean standardRetainAll(final Collection<?> collection) {
        return Iterators.retainAll(this.iterator(), collection);
    }
    
    protected void standardClear() {
        Iterators.clear(this.iterator());
    }
    
    protected boolean standardIsEmpty() {
        return !this.iterator().hasNext();
    }
    
    protected String standardToString() {
        return Collections2.toStringImpl(this);
    }
    
    protected Object[] standardToArray() {
        final Object[] newArray = new Object[this.size()];
        return this.toArray(newArray);
    }
    
    protected <T> T[] standardToArray(final T[] array) {
        return ObjectArrays.toArrayImpl(this, array);
    }
}
