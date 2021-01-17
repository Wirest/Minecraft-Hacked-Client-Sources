// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.io.Serializable;
import com.google.common.annotations.Beta;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract class Equivalence<T>
{
    protected Equivalence() {
    }
    
    public final boolean equivalent(@Nullable final T a, @Nullable final T b) {
        return a == b || (a != null && b != null && this.doEquivalent(a, b));
    }
    
    protected abstract boolean doEquivalent(final T p0, final T p1);
    
    public final int hash(@Nullable final T t) {
        if (t == null) {
            return 0;
        }
        return this.doHash(t);
    }
    
    protected abstract int doHash(final T p0);
    
    public final <F> Equivalence<F> onResultOf(final Function<F, ? extends T> function) {
        return (Equivalence<F>)new FunctionalEquivalence((Function<Object, ?>)function, (Equivalence<Object>)this);
    }
    
    public final <S extends T> Wrapper<S> wrap(@Nullable final S reference) {
        return new Wrapper<S>(this, (Object)reference);
    }
    
    @GwtCompatible(serializable = true)
    public final <S extends T> Equivalence<Iterable<S>> pairwise() {
        return (Equivalence<Iterable<S>>)new PairwiseEquivalence((Equivalence<? super Object>)this);
    }
    
    @Beta
    public final Predicate<T> equivalentTo(@Nullable final T target) {
        return new EquivalentToPredicate<T>(this, target);
    }
    
    public static Equivalence<Object> equals() {
        return Equals.INSTANCE;
    }
    
    public static Equivalence<Object> identity() {
        return Identity.INSTANCE;
    }
    
    public static final class Wrapper<T> implements Serializable
    {
        private final Equivalence<? super T> equivalence;
        @Nullable
        private final T reference;
        private static final long serialVersionUID = 0L;
        
        private Wrapper(final Equivalence<? super T> equivalence, @Nullable final T reference) {
            this.equivalence = Preconditions.checkNotNull(equivalence);
            this.reference = reference;
        }
        
        @Nullable
        public T get() {
            return this.reference;
        }
        
        @Override
        public boolean equals(@Nullable final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Wrapper) {
                final Wrapper<?> that = (Wrapper<?>)obj;
                if (this.equivalence.equals(that.equivalence)) {
                    final Equivalence<Object> equivalence = (Equivalence<Object>)this.equivalence;
                    return equivalence.equivalent(this.reference, that.reference);
                }
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.equivalence.hash((Object)this.reference);
        }
        
        @Override
        public String toString() {
            return this.equivalence + ".wrap(" + this.reference + ")";
        }
    }
    
    private static final class EquivalentToPredicate<T> implements Predicate<T>, Serializable
    {
        private final Equivalence<T> equivalence;
        @Nullable
        private final T target;
        private static final long serialVersionUID = 0L;
        
        EquivalentToPredicate(final Equivalence<T> equivalence, @Nullable final T target) {
            this.equivalence = Preconditions.checkNotNull(equivalence);
            this.target = target;
        }
        
        @Override
        public boolean apply(@Nullable final T input) {
            return this.equivalence.equivalent(input, this.target);
        }
        
        @Override
        public boolean equals(@Nullable final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof EquivalentToPredicate) {
                final EquivalentToPredicate<?> that = (EquivalentToPredicate<?>)obj;
                return this.equivalence.equals(that.equivalence) && Objects.equal(this.target, that.target);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.equivalence, this.target);
        }
        
        @Override
        public String toString() {
            return this.equivalence + ".equivalentTo(" + this.target + ")";
        }
    }
    
    static final class Equals extends Equivalence<Object> implements Serializable
    {
        static final Equals INSTANCE;
        private static final long serialVersionUID = 1L;
        
        @Override
        protected boolean doEquivalent(final Object a, final Object b) {
            return a.equals(b);
        }
        
        public int doHash(final Object o) {
            return o.hashCode();
        }
        
        private Object readResolve() {
            return Equals.INSTANCE;
        }
        
        static {
            INSTANCE = new Equals();
        }
    }
    
    static final class Identity extends Equivalence<Object> implements Serializable
    {
        static final Identity INSTANCE;
        private static final long serialVersionUID = 1L;
        
        @Override
        protected boolean doEquivalent(final Object a, final Object b) {
            return false;
        }
        
        @Override
        protected int doHash(final Object o) {
            return System.identityHashCode(o);
        }
        
        private Object readResolve() {
            return Identity.INSTANCE;
        }
        
        static {
            INSTANCE = new Identity();
        }
    }
}
