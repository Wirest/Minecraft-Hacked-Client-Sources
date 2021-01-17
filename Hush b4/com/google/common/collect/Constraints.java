// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.List;
import java.util.SortedSet;
import java.util.Set;
import java.util.Collection;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class Constraints
{
    private Constraints() {
    }
    
    public static <E> Collection<E> constrainedCollection(final Collection<E> collection, final Constraint<? super E> constraint) {
        return new ConstrainedCollection<E>(collection, constraint);
    }
    
    public static <E> Set<E> constrainedSet(final Set<E> set, final Constraint<? super E> constraint) {
        return new ConstrainedSet<E>(set, constraint);
    }
    
    public static <E> SortedSet<E> constrainedSortedSet(final SortedSet<E> sortedSet, final Constraint<? super E> constraint) {
        return new ConstrainedSortedSet<E>(sortedSet, constraint);
    }
    
    public static <E> List<E> constrainedList(final List<E> list, final Constraint<? super E> constraint) {
        return (list instanceof RandomAccess) ? new ConstrainedRandomAccessList<E>(list, constraint) : new ConstrainedList<E>(list, constraint);
    }
    
    private static <E> ListIterator<E> constrainedListIterator(final ListIterator<E> listIterator, final Constraint<? super E> constraint) {
        return new ConstrainedListIterator<E>(listIterator, constraint);
    }
    
    static <E> Collection<E> constrainedTypePreservingCollection(final Collection<E> collection, final Constraint<E> constraint) {
        if (collection instanceof SortedSet) {
            return (Collection<E>)constrainedSortedSet((SortedSet<Object>)(SortedSet)collection, (Constraint<? super Object>)constraint);
        }
        if (collection instanceof Set) {
            return (Collection<E>)constrainedSet((Set<Object>)(Set)collection, (Constraint<? super Object>)constraint);
        }
        if (collection instanceof List) {
            return (Collection<E>)constrainedList((List<Object>)(List)collection, (Constraint<? super Object>)constraint);
        }
        return (Collection<E>)constrainedCollection((Collection<Object>)collection, (Constraint<? super Object>)constraint);
    }
    
    private static <E> Collection<E> checkElements(final Collection<E> elements, final Constraint<? super E> constraint) {
        final Collection<E> copy = (Collection<E>)Lists.newArrayList((Iterable<?>)elements);
        for (final E element : copy) {
            constraint.checkElement((Object)element);
        }
        return copy;
    }
    
    static class ConstrainedCollection<E> extends ForwardingCollection<E>
    {
        private final Collection<E> delegate;
        private final Constraint<? super E> constraint;
        
        public ConstrainedCollection(final Collection<E> delegate, final Constraint<? super E> constraint) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.constraint = Preconditions.checkNotNull(constraint);
        }
        
        @Override
        protected Collection<E> delegate() {
            return this.delegate;
        }
        
        @Override
        public boolean add(final E element) {
            this.constraint.checkElement((Object)element);
            return this.delegate.add(element);
        }
        
        @Override
        public boolean addAll(final Collection<? extends E> elements) {
            return this.delegate.addAll(checkElements((Collection<Object>)elements, this.constraint));
        }
    }
    
    static class ConstrainedSet<E> extends ForwardingSet<E>
    {
        private final Set<E> delegate;
        private final Constraint<? super E> constraint;
        
        public ConstrainedSet(final Set<E> delegate, final Constraint<? super E> constraint) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.constraint = Preconditions.checkNotNull(constraint);
        }
        
        @Override
        protected Set<E> delegate() {
            return this.delegate;
        }
        
        @Override
        public boolean add(final E element) {
            this.constraint.checkElement((Object)element);
            return this.delegate.add(element);
        }
        
        @Override
        public boolean addAll(final Collection<? extends E> elements) {
            return this.delegate.addAll(checkElements((Collection<Object>)elements, this.constraint));
        }
    }
    
    private static class ConstrainedSortedSet<E> extends ForwardingSortedSet<E>
    {
        final SortedSet<E> delegate;
        final Constraint<? super E> constraint;
        
        ConstrainedSortedSet(final SortedSet<E> delegate, final Constraint<? super E> constraint) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.constraint = Preconditions.checkNotNull(constraint);
        }
        
        @Override
        protected SortedSet<E> delegate() {
            return this.delegate;
        }
        
        @Override
        public SortedSet<E> headSet(final E toElement) {
            return Constraints.constrainedSortedSet(this.delegate.headSet(toElement), this.constraint);
        }
        
        @Override
        public SortedSet<E> subSet(final E fromElement, final E toElement) {
            return Constraints.constrainedSortedSet(this.delegate.subSet(fromElement, toElement), this.constraint);
        }
        
        @Override
        public SortedSet<E> tailSet(final E fromElement) {
            return Constraints.constrainedSortedSet(this.delegate.tailSet(fromElement), this.constraint);
        }
        
        @Override
        public boolean add(final E element) {
            this.constraint.checkElement((Object)element);
            return this.delegate.add(element);
        }
        
        @Override
        public boolean addAll(final Collection<? extends E> elements) {
            return this.delegate.addAll((Collection<?>)checkElements((Collection<Object>)elements, this.constraint));
        }
    }
    
    @GwtCompatible
    private static class ConstrainedList<E> extends ForwardingList<E>
    {
        final List<E> delegate;
        final Constraint<? super E> constraint;
        
        ConstrainedList(final List<E> delegate, final Constraint<? super E> constraint) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.constraint = Preconditions.checkNotNull(constraint);
        }
        
        @Override
        protected List<E> delegate() {
            return this.delegate;
        }
        
        @Override
        public boolean add(final E element) {
            this.constraint.checkElement((Object)element);
            return this.delegate.add(element);
        }
        
        @Override
        public void add(final int index, final E element) {
            this.constraint.checkElement((Object)element);
            this.delegate.add(index, element);
        }
        
        @Override
        public boolean addAll(final Collection<? extends E> elements) {
            return this.delegate.addAll(checkElements((Collection<Object>)elements, this.constraint));
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends E> elements) {
            return this.delegate.addAll(index, checkElements((Collection<Object>)elements, this.constraint));
        }
        
        @Override
        public ListIterator<E> listIterator() {
            return (ListIterator<E>)constrainedListIterator((ListIterator<Object>)this.delegate.listIterator(), this.constraint);
        }
        
        @Override
        public ListIterator<E> listIterator(final int index) {
            return (ListIterator<E>)constrainedListIterator((ListIterator<Object>)this.delegate.listIterator(index), this.constraint);
        }
        
        @Override
        public E set(final int index, final E element) {
            this.constraint.checkElement((Object)element);
            return this.delegate.set(index, element);
        }
        
        @Override
        public List<E> subList(final int fromIndex, final int toIndex) {
            return Constraints.constrainedList(this.delegate.subList(fromIndex, toIndex), this.constraint);
        }
    }
    
    static class ConstrainedRandomAccessList<E> extends ConstrainedList<E> implements RandomAccess
    {
        ConstrainedRandomAccessList(final List<E> delegate, final Constraint<? super E> constraint) {
            super(delegate, constraint);
        }
    }
    
    static class ConstrainedListIterator<E> extends ForwardingListIterator<E>
    {
        private final ListIterator<E> delegate;
        private final Constraint<? super E> constraint;
        
        public ConstrainedListIterator(final ListIterator<E> delegate, final Constraint<? super E> constraint) {
            this.delegate = delegate;
            this.constraint = constraint;
        }
        
        @Override
        protected ListIterator<E> delegate() {
            return this.delegate;
        }
        
        @Override
        public void add(final E element) {
            this.constraint.checkElement((Object)element);
            this.delegate.add(element);
        }
        
        @Override
        public void set(final E element) {
            this.constraint.checkElement((Object)element);
            this.delegate.set(element);
        }
    }
}
