// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.NoSuchElementException;
import com.google.common.primitives.Booleans;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible
abstract class Cut<C extends Comparable> implements Comparable<Cut<C>>, Serializable
{
    final C endpoint;
    private static final long serialVersionUID = 0L;
    
    Cut(@Nullable final C endpoint) {
        this.endpoint = endpoint;
    }
    
    abstract boolean isLessThan(final C p0);
    
    abstract BoundType typeAsLowerBound();
    
    abstract BoundType typeAsUpperBound();
    
    abstract Cut<C> withLowerBoundType(final BoundType p0, final DiscreteDomain<C> p1);
    
    abstract Cut<C> withUpperBoundType(final BoundType p0, final DiscreteDomain<C> p1);
    
    abstract void describeAsLowerBound(final StringBuilder p0);
    
    abstract void describeAsUpperBound(final StringBuilder p0);
    
    abstract C leastValueAbove(final DiscreteDomain<C> p0);
    
    abstract C greatestValueBelow(final DiscreteDomain<C> p0);
    
    Cut<C> canonical(final DiscreteDomain<C> domain) {
        return this;
    }
    
    @Override
    public int compareTo(final Cut<C> that) {
        if (that == belowAll()) {
            return 1;
        }
        if (that == aboveAll()) {
            return -1;
        }
        final int result = Range.compareOrThrow(this.endpoint, that.endpoint);
        if (result != 0) {
            return result;
        }
        return Booleans.compare(this instanceof AboveValue, that instanceof AboveValue);
    }
    
    C endpoint() {
        return this.endpoint;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Cut) {
            final Cut<C> that = (Cut<C>)obj;
            try {
                final int compareResult = this.compareTo(that);
                return compareResult == 0;
            }
            catch (ClassCastException ex) {}
        }
        return false;
    }
    
    static <C extends Comparable> Cut<C> belowAll() {
        return (Cut<C>)BelowAll.INSTANCE;
    }
    
    static <C extends Comparable> Cut<C> aboveAll() {
        return (Cut<C>)AboveAll.INSTANCE;
    }
    
    static <C extends Comparable> Cut<C> belowValue(final C endpoint) {
        return new BelowValue<C>(endpoint);
    }
    
    static <C extends Comparable> Cut<C> aboveValue(final C endpoint) {
        return new AboveValue<C>(endpoint);
    }
    
    private static final class BelowAll extends Cut<Comparable<?>>
    {
        private static final BelowAll INSTANCE;
        private static final long serialVersionUID = 0L;
        
        private BelowAll() {
            super(null);
        }
        
        @Override
        Comparable<?> endpoint() {
            throw new IllegalStateException("range unbounded on this side");
        }
        
        @Override
        boolean isLessThan(final Comparable<?> value) {
            return true;
        }
        
        @Override
        BoundType typeAsLowerBound() {
            throw new IllegalStateException();
        }
        
        @Override
        BoundType typeAsUpperBound() {
            throw new AssertionError((Object)"this statement should be unreachable");
        }
        
        @Override
        Cut<Comparable<?>> withLowerBoundType(final BoundType boundType, final DiscreteDomain<Comparable<?>> domain) {
            throw new IllegalStateException();
        }
        
        @Override
        Cut<Comparable<?>> withUpperBoundType(final BoundType boundType, final DiscreteDomain<Comparable<?>> domain) {
            throw new AssertionError((Object)"this statement should be unreachable");
        }
        
        @Override
        void describeAsLowerBound(final StringBuilder sb) {
            sb.append("(-\u221e");
        }
        
        @Override
        void describeAsUpperBound(final StringBuilder sb) {
            throw new AssertionError();
        }
        
        @Override
        Comparable<?> leastValueAbove(final DiscreteDomain<Comparable<?>> domain) {
            return domain.minValue();
        }
        
        @Override
        Comparable<?> greatestValueBelow(final DiscreteDomain<Comparable<?>> domain) {
            throw new AssertionError();
        }
        
        @Override
        Cut<Comparable<?>> canonical(final DiscreteDomain<Comparable<?>> domain) {
            try {
                return Cut.belowValue(domain.minValue());
            }
            catch (NoSuchElementException e) {
                return this;
            }
        }
        
        @Override
        public int compareTo(final Cut<Comparable<?>> o) {
            return (o == this) ? 0 : -1;
        }
        
        @Override
        public String toString() {
            return "-\u221e";
        }
        
        private Object readResolve() {
            return BelowAll.INSTANCE;
        }
        
        static {
            INSTANCE = new BelowAll();
        }
    }
    
    private static final class AboveAll extends Cut<Comparable<?>>
    {
        private static final AboveAll INSTANCE;
        private static final long serialVersionUID = 0L;
        
        private AboveAll() {
            super(null);
        }
        
        @Override
        Comparable<?> endpoint() {
            throw new IllegalStateException("range unbounded on this side");
        }
        
        @Override
        boolean isLessThan(final Comparable<?> value) {
            return false;
        }
        
        @Override
        BoundType typeAsLowerBound() {
            throw new AssertionError((Object)"this statement should be unreachable");
        }
        
        @Override
        BoundType typeAsUpperBound() {
            throw new IllegalStateException();
        }
        
        @Override
        Cut<Comparable<?>> withLowerBoundType(final BoundType boundType, final DiscreteDomain<Comparable<?>> domain) {
            throw new AssertionError((Object)"this statement should be unreachable");
        }
        
        @Override
        Cut<Comparable<?>> withUpperBoundType(final BoundType boundType, final DiscreteDomain<Comparable<?>> domain) {
            throw new IllegalStateException();
        }
        
        @Override
        void describeAsLowerBound(final StringBuilder sb) {
            throw new AssertionError();
        }
        
        @Override
        void describeAsUpperBound(final StringBuilder sb) {
            sb.append("+\u221e)");
        }
        
        @Override
        Comparable<?> leastValueAbove(final DiscreteDomain<Comparable<?>> domain) {
            throw new AssertionError();
        }
        
        @Override
        Comparable<?> greatestValueBelow(final DiscreteDomain<Comparable<?>> domain) {
            return domain.maxValue();
        }
        
        @Override
        public int compareTo(final Cut<Comparable<?>> o) {
            return (o != this) ? 1 : 0;
        }
        
        @Override
        public String toString() {
            return "+\u221e";
        }
        
        private Object readResolve() {
            return AboveAll.INSTANCE;
        }
        
        static {
            INSTANCE = new AboveAll();
        }
    }
    
    private static final class BelowValue<C extends Comparable> extends Cut<C>
    {
        private static final long serialVersionUID = 0L;
        
        BelowValue(final C endpoint) {
            super(Preconditions.checkNotNull((C)endpoint));
        }
        
        @Override
        boolean isLessThan(final C value) {
            return Range.compareOrThrow(this.endpoint, value) <= 0;
        }
        
        @Override
        BoundType typeAsLowerBound() {
            return BoundType.CLOSED;
        }
        
        @Override
        BoundType typeAsUpperBound() {
            return BoundType.OPEN;
        }
        
        @Override
        Cut<C> withLowerBoundType(final BoundType boundType, final DiscreteDomain<C> domain) {
            switch (boundType) {
                case CLOSED: {
                    return this;
                }
                case OPEN: {
                    final C previous = domain.previous(this.endpoint);
                    return (previous == null) ? Cut.belowAll() : new AboveValue<C>(previous);
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        
        @Override
        Cut<C> withUpperBoundType(final BoundType boundType, final DiscreteDomain<C> domain) {
            switch (boundType) {
                case CLOSED: {
                    final C previous = domain.previous(this.endpoint);
                    return (previous == null) ? Cut.aboveAll() : new AboveValue<C>(previous);
                }
                case OPEN: {
                    return this;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        
        @Override
        void describeAsLowerBound(final StringBuilder sb) {
            sb.append('[').append(this.endpoint);
        }
        
        @Override
        void describeAsUpperBound(final StringBuilder sb) {
            sb.append(this.endpoint).append(')');
        }
        
        @Override
        C leastValueAbove(final DiscreteDomain<C> domain) {
            return this.endpoint;
        }
        
        @Override
        C greatestValueBelow(final DiscreteDomain<C> domain) {
            return domain.previous(this.endpoint);
        }
        
        @Override
        public int hashCode() {
            return this.endpoint.hashCode();
        }
        
        @Override
        public String toString() {
            return "\\" + this.endpoint + "/";
        }
    }
    
    private static final class AboveValue<C extends Comparable> extends Cut<C>
    {
        private static final long serialVersionUID = 0L;
        
        AboveValue(final C endpoint) {
            super(Preconditions.checkNotNull((C)endpoint));
        }
        
        @Override
        boolean isLessThan(final C value) {
            return Range.compareOrThrow(this.endpoint, value) < 0;
        }
        
        @Override
        BoundType typeAsLowerBound() {
            return BoundType.OPEN;
        }
        
        @Override
        BoundType typeAsUpperBound() {
            return BoundType.CLOSED;
        }
        
        @Override
        Cut<C> withLowerBoundType(final BoundType boundType, final DiscreteDomain<C> domain) {
            switch (boundType) {
                case OPEN: {
                    return this;
                }
                case CLOSED: {
                    final C next = domain.next(this.endpoint);
                    return (next == null) ? Cut.belowAll() : Cut.belowValue(next);
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        
        @Override
        Cut<C> withUpperBoundType(final BoundType boundType, final DiscreteDomain<C> domain) {
            switch (boundType) {
                case OPEN: {
                    final C next = domain.next(this.endpoint);
                    return (next == null) ? Cut.aboveAll() : Cut.belowValue(next);
                }
                case CLOSED: {
                    return this;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        
        @Override
        void describeAsLowerBound(final StringBuilder sb) {
            sb.append('(').append(this.endpoint);
        }
        
        @Override
        void describeAsUpperBound(final StringBuilder sb) {
            sb.append(this.endpoint).append(']');
        }
        
        @Override
        C leastValueAbove(final DiscreteDomain<C> domain) {
            return domain.next(this.endpoint);
        }
        
        @Override
        C greatestValueBelow(final DiscreteDomain<C> domain) {
            return this.endpoint;
        }
        
        @Override
        Cut<C> canonical(final DiscreteDomain<C> domain) {
            final C next = this.leastValueAbove(domain);
            return (next != null) ? Cut.belowValue(next) : Cut.aboveAll();
        }
        
        @Override
        public int hashCode() {
            return ~this.endpoint.hashCode();
        }
        
        @Override
        public String toString() {
            return "/" + this.endpoint + "\\";
        }
    }
}
