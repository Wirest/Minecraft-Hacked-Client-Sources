// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.ConcurrentMap;
import com.google.common.annotations.Beta;

@Beta
public final class Interners
{
    private Interners() {
    }
    
    public static <E> Interner<E> newStrongInterner() {
        final ConcurrentMap<E, E> map = new MapMaker().makeMap();
        return new Interner<E>() {
            @Override
            public E intern(final E sample) {
                final E canonical = map.putIfAbsent(Preconditions.checkNotNull(sample), sample);
                return (canonical == null) ? sample : canonical;
            }
        };
    }
    
    @GwtIncompatible("java.lang.ref.WeakReference")
    public static <E> Interner<E> newWeakInterner() {
        return new WeakInterner<E>();
    }
    
    public static <E> Function<E, E> asFunction(final Interner<E> interner) {
        return new InternerFunction<E>(Preconditions.checkNotNull(interner));
    }
    
    private static class WeakInterner<E> implements Interner<E>
    {
        private final MapMakerInternalMap<E, Dummy> map;
        
        private WeakInterner() {
            this.map = new MapMaker().weakKeys().keyEquivalence(Equivalence.equals()).makeCustomMap();
        }
        
        @Override
        public E intern(final E sample) {
            while (true) {
                final MapMakerInternalMap.ReferenceEntry<E, Dummy> entry = this.map.getEntry(sample);
                if (entry != null) {
                    final E canonical = entry.getKey();
                    if (canonical != null) {
                        return canonical;
                    }
                }
                final Dummy sneaky = this.map.putIfAbsent(sample, Dummy.VALUE);
                if (sneaky == null) {
                    return sample;
                }
            }
        }
        
        private enum Dummy
        {
            VALUE;
        }
    }
    
    private static class InternerFunction<E> implements Function<E, E>
    {
        private final Interner<E> interner;
        
        public InternerFunction(final Interner<E> interner) {
            this.interner = interner;
        }
        
        @Override
        public E apply(final E input) {
            return this.interner.intern(input);
        }
        
        @Override
        public int hashCode() {
            return this.interner.hashCode();
        }
        
        @Override
        public boolean equals(final Object other) {
            if (other instanceof InternerFunction) {
                final InternerFunction<?> that = (InternerFunction<?>)other;
                return this.interner.equals(that.interner);
            }
            return false;
        }
    }
}
