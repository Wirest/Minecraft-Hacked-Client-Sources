// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.Nullable;
import java.util.Set;
import com.google.common.annotations.Beta;

@Beta
public interface RangeSet<C extends Comparable>
{
    boolean contains(final C p0);
    
    Range<C> rangeContaining(final C p0);
    
    boolean encloses(final Range<C> p0);
    
    boolean enclosesAll(final RangeSet<C> p0);
    
    boolean isEmpty();
    
    Range<C> span();
    
    Set<Range<C>> asRanges();
    
    RangeSet<C> complement();
    
    RangeSet<C> subRangeSet(final Range<C> p0);
    
    void add(final Range<C> p0);
    
    void remove(final Range<C> p0);
    
    void clear();
    
    void addAll(final RangeSet<C> p0);
    
    void removeAll(final RangeSet<C> p0);
    
    boolean equals(@Nullable final Object p0);
    
    int hashCode();
    
    String toString();
}
