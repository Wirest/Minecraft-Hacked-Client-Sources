// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Objects;
import java.util.ConcurrentModificationException;
import java.util.Set;
import java.util.NavigableSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.util.Collection;
import javax.annotation.Nullable;
import java.util.Comparator;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(emulated = true)
public final class TreeMultiset<E> extends AbstractSortedMultiset<E> implements Serializable
{
    private final transient Reference<AvlNode<E>> rootReference;
    private final transient GeneralRange<E> range;
    private final transient AvlNode<E> header;
    @GwtIncompatible("not needed in emulated source")
    private static final long serialVersionUID = 1L;
    
    public static <E extends Comparable> TreeMultiset<E> create() {
        return new TreeMultiset<E>(Ordering.natural());
    }
    
    public static <E> TreeMultiset<E> create(@Nullable final Comparator<? super E> comparator) {
        return (comparator == null) ? new TreeMultiset<E>((Comparator<? super E>)Ordering.natural()) : new TreeMultiset<E>(comparator);
    }
    
    public static <E extends Comparable> TreeMultiset<E> create(final Iterable<? extends E> elements) {
        final TreeMultiset<E> multiset = create();
        Iterables.addAll(multiset, elements);
        return multiset;
    }
    
    TreeMultiset(final Reference<AvlNode<E>> rootReference, final GeneralRange<E> range, final AvlNode<E> endLink) {
        super(range.comparator());
        this.rootReference = rootReference;
        this.range = range;
        this.header = endLink;
    }
    
    TreeMultiset(final Comparator<? super E> comparator) {
        super(comparator);
        this.range = GeneralRange.all(comparator);
        successor(this.header = new AvlNode<E>(null, 1), this.header);
        this.rootReference = new Reference<AvlNode<E>>();
    }
    
    private long aggregateForEntries(final Aggregate aggr) {
        final AvlNode<E> root = this.rootReference.get();
        long total = aggr.treeAggregate(root);
        if (this.range.hasLowerBound()) {
            total -= this.aggregateBelowRange(aggr, root);
        }
        if (this.range.hasUpperBound()) {
            total -= this.aggregateAboveRange(aggr, root);
        }
        return total;
    }
    
    private long aggregateBelowRange(final Aggregate aggr, @Nullable final AvlNode<E> node) {
        if (node == null) {
            return 0L;
        }
        final int cmp = this.comparator().compare(this.range.getLowerEndpoint(), ((AvlNode<Object>)node).elem);
        if (cmp < 0) {
            return this.aggregateBelowRange(aggr, ((AvlNode<Object>)node).left);
        }
        if (cmp != 0) {
            return aggr.treeAggregate(((AvlNode<Object>)node).left) + aggr.nodeAggregate(node) + this.aggregateBelowRange(aggr, ((AvlNode<Object>)node).right);
        }
        switch (this.range.getLowerBoundType()) {
            case OPEN: {
                return aggr.nodeAggregate(node) + aggr.treeAggregate(((AvlNode<Object>)node).left);
            }
            case CLOSED: {
                return aggr.treeAggregate(((AvlNode<Object>)node).left);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    private long aggregateAboveRange(final Aggregate aggr, @Nullable final AvlNode<E> node) {
        if (node == null) {
            return 0L;
        }
        final int cmp = this.comparator().compare(this.range.getUpperEndpoint(), ((AvlNode<Object>)node).elem);
        if (cmp > 0) {
            return this.aggregateAboveRange(aggr, ((AvlNode<Object>)node).right);
        }
        if (cmp != 0) {
            return aggr.treeAggregate(((AvlNode<Object>)node).right) + aggr.nodeAggregate(node) + this.aggregateAboveRange(aggr, ((AvlNode<Object>)node).left);
        }
        switch (this.range.getUpperBoundType()) {
            case OPEN: {
                return aggr.nodeAggregate(node) + aggr.treeAggregate(((AvlNode<Object>)node).right);
            }
            case CLOSED: {
                return aggr.treeAggregate(((AvlNode<Object>)node).right);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    @Override
    public int size() {
        return Ints.saturatedCast(this.aggregateForEntries(Aggregate.SIZE));
    }
    
    @Override
    int distinctElements() {
        return Ints.saturatedCast(this.aggregateForEntries(Aggregate.DISTINCT));
    }
    
    @Override
    public int count(@Nullable final Object element) {
        try {
            final E e = (E)element;
            final AvlNode<E> root = this.rootReference.get();
            if (!this.range.contains(e) || root == null) {
                return 0;
            }
            return root.count(this.comparator(), e);
        }
        catch (ClassCastException e2) {
            return 0;
        }
        catch (NullPointerException e3) {
            return 0;
        }
    }
    
    @Override
    public int add(@Nullable final E element, final int occurrences) {
        CollectPreconditions.checkNonnegative(occurrences, "occurrences");
        if (occurrences == 0) {
            return this.count(element);
        }
        Preconditions.checkArgument(this.range.contains(element));
        final AvlNode<E> root = this.rootReference.get();
        if (root == null) {
            this.comparator().compare(element, element);
            final AvlNode<E> newRoot = new AvlNode<E>(element, occurrences);
            successor(this.header, newRoot, this.header);
            this.rootReference.checkAndSet(root, newRoot);
            return 0;
        }
        final int[] result = { 0 };
        final AvlNode<E> newRoot2 = root.add(this.comparator(), element, occurrences, result);
        this.rootReference.checkAndSet(root, newRoot2);
        return result[0];
    }
    
    @Override
    public int remove(@Nullable final Object element, final int occurrences) {
        CollectPreconditions.checkNonnegative(occurrences, "occurrences");
        if (occurrences == 0) {
            return this.count(element);
        }
        final AvlNode<E> root = this.rootReference.get();
        final int[] result = { 0 };
        AvlNode<E> newRoot;
        try {
            final E e = (E)element;
            if (!this.range.contains(e) || root == null) {
                return 0;
            }
            newRoot = root.remove(this.comparator(), e, occurrences, result);
        }
        catch (ClassCastException e2) {
            return 0;
        }
        catch (NullPointerException e3) {
            return 0;
        }
        this.rootReference.checkAndSet(root, newRoot);
        return result[0];
    }
    
    @Override
    public int setCount(@Nullable final E element, final int count) {
        CollectPreconditions.checkNonnegative(count, "count");
        if (!this.range.contains(element)) {
            Preconditions.checkArgument(count == 0);
            return 0;
        }
        final AvlNode<E> root = this.rootReference.get();
        if (root == null) {
            if (count > 0) {
                this.add(element, count);
            }
            return 0;
        }
        final int[] result = { 0 };
        final AvlNode<E> newRoot = root.setCount(this.comparator(), element, count, result);
        this.rootReference.checkAndSet(root, newRoot);
        return result[0];
    }
    
    @Override
    public boolean setCount(@Nullable final E element, final int oldCount, final int newCount) {
        CollectPreconditions.checkNonnegative(newCount, "newCount");
        CollectPreconditions.checkNonnegative(oldCount, "oldCount");
        Preconditions.checkArgument(this.range.contains(element));
        final AvlNode<E> root = this.rootReference.get();
        if (root != null) {
            final int[] result = { 0 };
            final AvlNode<E> newRoot = root.setCount(this.comparator(), element, oldCount, newCount, result);
            this.rootReference.checkAndSet(root, newRoot);
            return result[0] == oldCount;
        }
        if (oldCount == 0) {
            if (newCount > 0) {
                this.add(element, newCount);
            }
            return true;
        }
        return false;
    }
    
    private Multiset.Entry<E> wrapEntry(final AvlNode<E> baseEntry) {
        return new Multisets.AbstractEntry<E>() {
            @Override
            public E getElement() {
                return baseEntry.getElement();
            }
            
            @Override
            public int getCount() {
                final int result = baseEntry.getCount();
                if (result == 0) {
                    return TreeMultiset.this.count(this.getElement());
                }
                return result;
            }
        };
    }
    
    @Nullable
    private AvlNode<E> firstNode() {
        final AvlNode<E> root = this.rootReference.get();
        if (root == null) {
            return null;
        }
        AvlNode<E> node;
        if (this.range.hasLowerBound()) {
            final E endpoint = this.range.getLowerEndpoint();
            node = (AvlNode<E>)((AvlNode<Object>)this.rootReference.get()).ceiling(this.comparator(), endpoint);
            if (node == null) {
                return null;
            }
            if (this.range.getLowerBoundType() == BoundType.OPEN && this.comparator().compare(endpoint, node.getElement()) == 0) {
                node = (AvlNode<E>)((AvlNode<Object>)node).succ;
            }
        }
        else {
            node = (AvlNode<E>)((AvlNode<Object>)this.header).succ;
        }
        return (node == this.header || !this.range.contains(node.getElement())) ? null : node;
    }
    
    @Nullable
    private AvlNode<E> lastNode() {
        final AvlNode<E> root = this.rootReference.get();
        if (root == null) {
            return null;
        }
        AvlNode<E> node;
        if (this.range.hasUpperBound()) {
            final E endpoint = this.range.getUpperEndpoint();
            node = (AvlNode<E>)((AvlNode<Object>)this.rootReference.get()).floor(this.comparator(), endpoint);
            if (node == null) {
                return null;
            }
            if (this.range.getUpperBoundType() == BoundType.OPEN && this.comparator().compare(endpoint, node.getElement()) == 0) {
                node = (AvlNode<E>)((AvlNode<Object>)node).pred;
            }
        }
        else {
            node = (AvlNode<E>)((AvlNode<Object>)this.header).pred;
        }
        return (node == this.header || !this.range.contains(node.getElement())) ? null : node;
    }
    
    @Override
    Iterator<Multiset.Entry<E>> entryIterator() {
        return new Iterator<Multiset.Entry<E>>() {
            AvlNode<E> current = TreeMultiset.this.firstNode();
            Multiset.Entry<E> prevEntry;
            
            @Override
            public boolean hasNext() {
                if (this.current == null) {
                    return false;
                }
                if (TreeMultiset.this.range.tooHigh(this.current.getElement())) {
                    this.current = null;
                    return false;
                }
                return true;
            }
            
            @Override
            public Multiset.Entry<E> next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final Multiset.Entry<E> result = (Multiset.Entry<E>)TreeMultiset.this.wrapEntry(this.current);
                this.prevEntry = result;
                if (((AvlNode<Object>)this.current).succ == TreeMultiset.this.header) {
                    this.current = null;
                }
                else {
                    this.current = (AvlNode<E>)((AvlNode<Object>)this.current).succ;
                }
                return result;
            }
            
            @Override
            public void remove() {
                CollectPreconditions.checkRemove(this.prevEntry != null);
                TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
                this.prevEntry = null;
            }
        };
    }
    
    @Override
    Iterator<Multiset.Entry<E>> descendingEntryIterator() {
        return new Iterator<Multiset.Entry<E>>() {
            AvlNode<E> current = TreeMultiset.this.lastNode();
            Multiset.Entry<E> prevEntry = null;
            
            @Override
            public boolean hasNext() {
                if (this.current == null) {
                    return false;
                }
                if (TreeMultiset.this.range.tooLow(this.current.getElement())) {
                    this.current = null;
                    return false;
                }
                return true;
            }
            
            @Override
            public Multiset.Entry<E> next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final Multiset.Entry<E> result = (Multiset.Entry<E>)TreeMultiset.this.wrapEntry(this.current);
                this.prevEntry = result;
                if (((AvlNode<Object>)this.current).pred == TreeMultiset.this.header) {
                    this.current = null;
                }
                else {
                    this.current = (AvlNode<E>)((AvlNode<Object>)this.current).pred;
                }
                return result;
            }
            
            @Override
            public void remove() {
                CollectPreconditions.checkRemove(this.prevEntry != null);
                TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
                this.prevEntry = null;
            }
        };
    }
    
    @Override
    public SortedMultiset<E> headMultiset(@Nullable final E upperBound, final BoundType boundType) {
        return new TreeMultiset((Reference<AvlNode<Object>>)this.rootReference, (GeneralRange<Object>)this.range.intersect(GeneralRange.upTo(this.comparator(), upperBound, boundType)), (AvlNode<Object>)this.header);
    }
    
    @Override
    public SortedMultiset<E> tailMultiset(@Nullable final E lowerBound, final BoundType boundType) {
        return new TreeMultiset((Reference<AvlNode<Object>>)this.rootReference, (GeneralRange<Object>)this.range.intersect(GeneralRange.downTo(this.comparator(), lowerBound, boundType)), (AvlNode<Object>)this.header);
    }
    
    static int distinctElements(@Nullable final AvlNode<?> node) {
        return (node == null) ? 0 : ((AvlNode<Object>)node).distinctElements;
    }
    
    private static <T> void successor(final AvlNode<T> a, final AvlNode<T> b) {
        ((AvlNode<Object>)a).succ = (AvlNode<Object>)b;
        ((AvlNode<Object>)b).pred = (AvlNode<Object>)a;
    }
    
    private static <T> void successor(final AvlNode<T> a, final AvlNode<T> b, final AvlNode<T> c) {
        successor(a, b);
        successor(b, c);
    }
    
    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.elementSet().comparator());
        Serialization.writeMultiset((Multiset<Object>)this, stream);
    }
    
    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final Comparator<? super E> comparator = (Comparator<? super E>)stream.readObject();
        Serialization.getFieldSetter((Class<TreeMultiset>)AbstractSortedMultiset.class, "comparator").set(this, comparator);
        Serialization.getFieldSetter(TreeMultiset.class, "range").set(this, GeneralRange.all((Comparator<? super Object>)comparator));
        Serialization.getFieldSetter(TreeMultiset.class, "rootReference").set(this, new Reference());
        final AvlNode<E> header = new AvlNode<E>(null, 1);
        Serialization.getFieldSetter(TreeMultiset.class, "header").set(this, header);
        successor(header, header);
        Serialization.populateMultiset((Multiset<Object>)this, stream);
    }
    
    private enum Aggregate
    {
        SIZE {
            @Override
            int nodeAggregate(final AvlNode<?> node) {
                return ((AvlNode<Object>)node).elemCount;
            }
            
            @Override
            long treeAggregate(@Nullable final AvlNode<?> root) {
                return (root == null) ? 0L : ((AvlNode<Object>)root).totalCount;
            }
        }, 
        DISTINCT {
            @Override
            int nodeAggregate(final AvlNode<?> node) {
                return 1;
            }
            
            @Override
            long treeAggregate(@Nullable final AvlNode<?> root) {
                return (root == null) ? 0L : ((AvlNode<Object>)root).distinctElements;
            }
        };
        
        abstract int nodeAggregate(final AvlNode<?> p0);
        
        abstract long treeAggregate(@Nullable final AvlNode<?> p0);
    }
    
    private static final class Reference<T>
    {
        @Nullable
        private T value;
        
        @Nullable
        public T get() {
            return this.value;
        }
        
        public void checkAndSet(@Nullable final T expected, final T newValue) {
            if (this.value != expected) {
                throw new ConcurrentModificationException();
            }
            this.value = newValue;
        }
    }
    
    private static final class AvlNode<E> extends Multisets.AbstractEntry<E>
    {
        @Nullable
        private final E elem;
        private int elemCount;
        private int distinctElements;
        private long totalCount;
        private int height;
        private AvlNode<E> left;
        private AvlNode<E> right;
        private AvlNode<E> pred;
        private AvlNode<E> succ;
        
        AvlNode(@Nullable final E elem, final int elemCount) {
            Preconditions.checkArgument(elemCount > 0);
            this.elem = elem;
            this.elemCount = elemCount;
            this.totalCount = elemCount;
            this.distinctElements = 1;
            this.height = 1;
            this.left = null;
            this.right = null;
        }
        
        public int count(final Comparator<? super E> comparator, final E e) {
            final int cmp = comparator.compare((Object)e, (Object)this.elem);
            if (cmp < 0) {
                return (this.left == null) ? 0 : this.left.count(comparator, e);
            }
            if (cmp > 0) {
                return (this.right == null) ? 0 : this.right.count(comparator, e);
            }
            return this.elemCount;
        }
        
        private AvlNode<E> addRightChild(final E e, final int count) {
            successor(this, this.right = new AvlNode<E>(e, count), (AvlNode<Object>)this.succ);
            this.height = Math.max(2, this.height);
            ++this.distinctElements;
            this.totalCount += count;
            return this;
        }
        
        private AvlNode<E> addLeftChild(final E e, final int count) {
            this.left = new AvlNode<E>(e, count);
            successor(this.pred, this.left, (AvlNode<Object>)this);
            this.height = Math.max(2, this.height);
            ++this.distinctElements;
            this.totalCount += count;
            return this;
        }
        
        AvlNode<E> add(final Comparator<? super E> comparator, @Nullable final E e, final int count, final int[] result) {
            final int cmp = comparator.compare((Object)e, (Object)this.elem);
            if (cmp < 0) {
                final AvlNode<E> initLeft = this.left;
                if (initLeft == null) {
                    result[0] = 0;
                    return this.addLeftChild(e, count);
                }
                final int initHeight = initLeft.height;
                this.left = initLeft.add(comparator, e, count, result);
                if (result[0] == 0) {
                    ++this.distinctElements;
                }
                this.totalCount += count;
                return (this.left.height == initHeight) ? this : this.rebalance();
            }
            else {
                if (cmp <= 0) {
                    result[0] = this.elemCount;
                    final long resultCount = this.elemCount + (long)count;
                    Preconditions.checkArgument(resultCount <= 2147483647L);
                    this.elemCount += count;
                    this.totalCount += count;
                    return this;
                }
                final AvlNode<E> initRight = this.right;
                if (initRight == null) {
                    result[0] = 0;
                    return this.addRightChild(e, count);
                }
                final int initHeight = initRight.height;
                this.right = initRight.add(comparator, e, count, result);
                if (result[0] == 0) {
                    ++this.distinctElements;
                }
                this.totalCount += count;
                return (this.right.height == initHeight) ? this : this.rebalance();
            }
        }
        
        AvlNode<E> remove(final Comparator<? super E> comparator, @Nullable final E e, final int count, final int[] result) {
            final int cmp = comparator.compare((Object)e, (Object)this.elem);
            if (cmp < 0) {
                final AvlNode<E> initLeft = this.left;
                if (initLeft == null) {
                    result[0] = 0;
                    return this;
                }
                this.left = initLeft.remove(comparator, e, count, result);
                if (result[0] > 0) {
                    if (count >= result[0]) {
                        --this.distinctElements;
                        this.totalCount -= result[0];
                    }
                    else {
                        this.totalCount -= count;
                    }
                }
                return (result[0] == 0) ? this : this.rebalance();
            }
            else if (cmp > 0) {
                final AvlNode<E> initRight = this.right;
                if (initRight == null) {
                    result[0] = 0;
                    return this;
                }
                this.right = initRight.remove(comparator, e, count, result);
                if (result[0] > 0) {
                    if (count >= result[0]) {
                        --this.distinctElements;
                        this.totalCount -= result[0];
                    }
                    else {
                        this.totalCount -= count;
                    }
                }
                return this.rebalance();
            }
            else {
                result[0] = this.elemCount;
                if (count >= this.elemCount) {
                    return this.deleteMe();
                }
                this.elemCount -= count;
                this.totalCount -= count;
                return this;
            }
        }
        
        AvlNode<E> setCount(final Comparator<? super E> comparator, @Nullable final E e, final int count, final int[] result) {
            final int cmp = comparator.compare((Object)e, (Object)this.elem);
            if (cmp < 0) {
                final AvlNode<E> initLeft = this.left;
                if (initLeft == null) {
                    result[0] = 0;
                    return (count > 0) ? this.addLeftChild(e, count) : this;
                }
                this.left = initLeft.setCount(comparator, e, count, result);
                if (count == 0 && result[0] != 0) {
                    --this.distinctElements;
                }
                else if (count > 0 && result[0] == 0) {
                    ++this.distinctElements;
                }
                this.totalCount += count - result[0];
                return this.rebalance();
            }
            else if (cmp > 0) {
                final AvlNode<E> initRight = this.right;
                if (initRight == null) {
                    result[0] = 0;
                    return (count > 0) ? this.addRightChild(e, count) : this;
                }
                this.right = initRight.setCount(comparator, e, count, result);
                if (count == 0 && result[0] != 0) {
                    --this.distinctElements;
                }
                else if (count > 0 && result[0] == 0) {
                    ++this.distinctElements;
                }
                this.totalCount += count - result[0];
                return this.rebalance();
            }
            else {
                result[0] = this.elemCount;
                if (count == 0) {
                    return this.deleteMe();
                }
                this.totalCount += count - this.elemCount;
                this.elemCount = count;
                return this;
            }
        }
        
        AvlNode<E> setCount(final Comparator<? super E> comparator, @Nullable final E e, final int expectedCount, final int newCount, final int[] result) {
            final int cmp = comparator.compare((Object)e, (Object)this.elem);
            if (cmp < 0) {
                final AvlNode<E> initLeft = this.left;
                if (initLeft != null) {
                    this.left = initLeft.setCount(comparator, e, expectedCount, newCount, result);
                    if (result[0] == expectedCount) {
                        if (newCount == 0 && result[0] != 0) {
                            --this.distinctElements;
                        }
                        else if (newCount > 0 && result[0] == 0) {
                            ++this.distinctElements;
                        }
                        this.totalCount += newCount - result[0];
                    }
                    return this.rebalance();
                }
                result[0] = 0;
                if (expectedCount == 0 && newCount > 0) {
                    return this.addLeftChild(e, newCount);
                }
                return this;
            }
            else {
                if (cmp <= 0) {
                    result[0] = this.elemCount;
                    if (expectedCount == this.elemCount) {
                        if (newCount == 0) {
                            return this.deleteMe();
                        }
                        this.totalCount += newCount - this.elemCount;
                        this.elemCount = newCount;
                    }
                    return this;
                }
                final AvlNode<E> initRight = this.right;
                if (initRight != null) {
                    this.right = initRight.setCount(comparator, e, expectedCount, newCount, result);
                    if (result[0] == expectedCount) {
                        if (newCount == 0 && result[0] != 0) {
                            --this.distinctElements;
                        }
                        else if (newCount > 0 && result[0] == 0) {
                            ++this.distinctElements;
                        }
                        this.totalCount += newCount - result[0];
                    }
                    return this.rebalance();
                }
                result[0] = 0;
                if (expectedCount == 0 && newCount > 0) {
                    return this.addRightChild(e, newCount);
                }
                return this;
            }
        }
        
        private AvlNode<E> deleteMe() {
            final int oldElemCount = this.elemCount;
            this.elemCount = 0;
            successor(this.pred, (AvlNode<Object>)this.succ);
            if (this.left == null) {
                return this.right;
            }
            if (this.right == null) {
                return this.left;
            }
            if (this.left.height >= this.right.height) {
                final AvlNode<E> newTop = this.pred;
                newTop.left = this.left.removeMax(newTop);
                newTop.right = this.right;
                newTop.distinctElements = this.distinctElements - 1;
                newTop.totalCount = this.totalCount - oldElemCount;
                return newTop.rebalance();
            }
            final AvlNode<E> newTop = this.succ;
            newTop.right = this.right.removeMin(newTop);
            newTop.left = this.left;
            newTop.distinctElements = this.distinctElements - 1;
            newTop.totalCount = this.totalCount - oldElemCount;
            return newTop.rebalance();
        }
        
        private AvlNode<E> removeMin(final AvlNode<E> node) {
            if (this.left == null) {
                return this.right;
            }
            this.left = this.left.removeMin(node);
            --this.distinctElements;
            this.totalCount -= node.elemCount;
            return this.rebalance();
        }
        
        private AvlNode<E> removeMax(final AvlNode<E> node) {
            if (this.right == null) {
                return this.left;
            }
            this.right = this.right.removeMax(node);
            --this.distinctElements;
            this.totalCount -= node.elemCount;
            return this.rebalance();
        }
        
        private void recomputeMultiset() {
            this.distinctElements = 1 + TreeMultiset.distinctElements(this.left) + TreeMultiset.distinctElements(this.right);
            this.totalCount = this.elemCount + totalCount(this.left) + totalCount(this.right);
        }
        
        private void recomputeHeight() {
            this.height = 1 + Math.max(height(this.left), height(this.right));
        }
        
        private void recompute() {
            this.recomputeMultiset();
            this.recomputeHeight();
        }
        
        private AvlNode<E> rebalance() {
            switch (this.balanceFactor()) {
                case -2: {
                    if (this.right.balanceFactor() > 0) {
                        this.right = this.right.rotateRight();
                    }
                    return this.rotateLeft();
                }
                case 2: {
                    if (this.left.balanceFactor() < 0) {
                        this.left = this.left.rotateLeft();
                    }
                    return this.rotateRight();
                }
                default: {
                    this.recomputeHeight();
                    return this;
                }
            }
        }
        
        private int balanceFactor() {
            return height(this.left) - height(this.right);
        }
        
        private AvlNode<E> rotateLeft() {
            Preconditions.checkState(this.right != null);
            final AvlNode<E> newTop = this.right;
            this.right = newTop.left;
            newTop.left = this;
            newTop.totalCount = this.totalCount;
            newTop.distinctElements = this.distinctElements;
            this.recompute();
            newTop.recomputeHeight();
            return newTop;
        }
        
        private AvlNode<E> rotateRight() {
            Preconditions.checkState(this.left != null);
            final AvlNode<E> newTop = this.left;
            this.left = newTop.right;
            newTop.right = this;
            newTop.totalCount = this.totalCount;
            newTop.distinctElements = this.distinctElements;
            this.recompute();
            newTop.recomputeHeight();
            return newTop;
        }
        
        private static long totalCount(@Nullable final AvlNode<?> node) {
            return (node == null) ? 0L : node.totalCount;
        }
        
        private static int height(@Nullable final AvlNode<?> node) {
            return (node == null) ? 0 : node.height;
        }
        
        @Nullable
        private AvlNode<E> ceiling(final Comparator<? super E> comparator, final E e) {
            final int cmp = comparator.compare((Object)e, (Object)this.elem);
            if (cmp < 0) {
                return (this.left == null) ? this : Objects.firstNonNull(this.left.ceiling(comparator, e), this);
            }
            if (cmp == 0) {
                return this;
            }
            return (this.right == null) ? null : this.right.ceiling(comparator, e);
        }
        
        @Nullable
        private AvlNode<E> floor(final Comparator<? super E> comparator, final E e) {
            final int cmp = comparator.compare((Object)e, (Object)this.elem);
            if (cmp > 0) {
                return (this.right == null) ? this : Objects.firstNonNull(this.right.floor(comparator, e), this);
            }
            if (cmp == 0) {
                return this;
            }
            return (this.left == null) ? null : this.left.floor(comparator, e);
        }
        
        @Override
        public E getElement() {
            return this.elem;
        }
        
        @Override
        public int getCount() {
            return this.elemCount;
        }
        
        @Override
        public String toString() {
            return Multisets.immutableEntry(this.getElement(), this.getCount()).toString();
        }
    }
}
