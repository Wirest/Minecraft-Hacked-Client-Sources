// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.BitSet;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.base.Optional;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible(emulated = true)
public abstract class BinaryTreeTraverser<T> extends TreeTraverser<T>
{
    public abstract Optional<T> leftChild(final T p0);
    
    public abstract Optional<T> rightChild(final T p0);
    
    @Override
    public final Iterable<T> children(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new AbstractIterator<T>() {
                    boolean doneLeft;
                    boolean doneRight;
                    
                    @Override
                    protected T computeNext() {
                        if (!this.doneLeft) {
                            this.doneLeft = true;
                            final Optional<T> left = (Optional<T>)BinaryTreeTraverser.this.leftChild(root);
                            if (left.isPresent()) {
                                return left.get();
                            }
                        }
                        if (!this.doneRight) {
                            this.doneRight = true;
                            final Optional<T> right = (Optional<T>)BinaryTreeTraverser.this.rightChild(root);
                            if (right.isPresent()) {
                                return right.get();
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
        };
    }
    
    @Override
    UnmodifiableIterator<T> preOrderIterator(final T root) {
        return new PreOrderIterator(root);
    }
    
    @Override
    UnmodifiableIterator<T> postOrderIterator(final T root) {
        return new PostOrderIterator(root);
    }
    
    public final FluentIterable<T> inOrderTraversal(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>() {
            @Override
            public UnmodifiableIterator<T> iterator() {
                return new InOrderIterator(root);
            }
        };
    }
    
    private static <T> void pushIfPresent(final Deque<T> stack, final Optional<T> node) {
        if (node.isPresent()) {
            stack.addLast(node.get());
        }
    }
    
    private final class PreOrderIterator extends UnmodifiableIterator<T> implements PeekingIterator<T>
    {
        private final Deque<T> stack;
        
        PreOrderIterator(final T root) {
            (this.stack = new ArrayDeque<T>()).addLast(root);
        }
        
        @Override
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }
        
        @Override
        public T next() {
            final T result = this.stack.removeLast();
            pushIfPresent(this.stack, (Optional<Object>)BinaryTreeTraverser.this.rightChild(result));
            pushIfPresent(this.stack, (Optional<Object>)BinaryTreeTraverser.this.leftChild(result));
            return result;
        }
        
        @Override
        public T peek() {
            return this.stack.getLast();
        }
    }
    
    private final class PostOrderIterator extends UnmodifiableIterator<T>
    {
        private final Deque<T> stack;
        private final BitSet hasExpanded;
        
        PostOrderIterator(final T root) {
            (this.stack = new ArrayDeque<T>()).addLast(root);
            this.hasExpanded = new BitSet();
        }
        
        @Override
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }
        
        @Override
        public T next() {
            T node;
            while (true) {
                node = this.stack.getLast();
                final boolean expandedNode = this.hasExpanded.get(this.stack.size() - 1);
                if (expandedNode) {
                    break;
                }
                this.hasExpanded.set(this.stack.size() - 1);
                pushIfPresent(this.stack, (Optional<Object>)BinaryTreeTraverser.this.rightChild(node));
                pushIfPresent(this.stack, (Optional<Object>)BinaryTreeTraverser.this.leftChild(node));
            }
            this.stack.removeLast();
            this.hasExpanded.clear(this.stack.size());
            return node;
        }
    }
    
    private final class InOrderIterator extends AbstractIterator<T>
    {
        private final Deque<T> stack;
        private final BitSet hasExpandedLeft;
        
        InOrderIterator(final T root) {
            this.stack = new ArrayDeque<T>();
            this.hasExpandedLeft = new BitSet();
            this.stack.addLast(root);
        }
        
        @Override
        protected T computeNext() {
            while (!this.stack.isEmpty()) {
                final T node = this.stack.getLast();
                if (this.hasExpandedLeft.get(this.stack.size() - 1)) {
                    this.stack.removeLast();
                    this.hasExpandedLeft.clear(this.stack.size());
                    pushIfPresent(this.stack, (Optional<Object>)BinaryTreeTraverser.this.rightChild(node));
                    return node;
                }
                this.hasExpandedLeft.set(this.stack.size() - 1);
                pushIfPresent(this.stack, (Optional<Object>)BinaryTreeTraverser.this.leftChild(node));
            }
            return this.endOfData();
        }
    }
}
