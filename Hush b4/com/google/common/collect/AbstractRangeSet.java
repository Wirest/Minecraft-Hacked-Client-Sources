// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.Nullable;
import java.util.Iterator;

abstract class AbstractRangeSet<C extends Comparable> implements RangeSet<C>
{
    @Override
    public boolean contains(final C value) {
        return this.rangeContaining(value) != null;
    }
    
    @Override
    public abstract Range<C> rangeContaining(final C p0);
    
    @Override
    public boolean isEmpty() {
        return this.asRanges().isEmpty();
    }
    
    @Override
    public void add(final Range<C> range) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void remove(final Range<C> range) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        this.remove(Range.all());
    }
    
    @Override
    public boolean enclosesAll(final RangeSet<C> other) {
        for (final Range<C> range : other.asRanges()) {
            if (!this.encloses(range)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void addAll(final RangeSet<C> other) {
        for (final Range<C> range : other.asRanges()) {
            this.add(range);
        }
    }
    
    @Override
    public void removeAll(final RangeSet<C> other) {
        for (final Range<C> range : other.asRanges()) {
            this.remove(range);
        }
    }
    
    @Override
    public abstract boolean encloses(final Range<C> p0);
    
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof RangeSet) {
            final RangeSet<?> other = (RangeSet<?>)obj;
            return this.asRanges().equals(other.asRanges());
        }
        return false;
    }
    
    @Override
    public final int hashCode() {
        return this.asRanges().hashCode();
    }
    
    @Override
    public final String toString() {
        return this.asRanges().toString();
    }
}
