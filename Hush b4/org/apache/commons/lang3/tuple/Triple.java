// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.tuple;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import java.io.Serializable;

public abstract class Triple<L, M, R> implements Comparable<Triple<L, M, R>>, Serializable
{
    private static final long serialVersionUID = 1L;
    
    public static <L, M, R> Triple<L, M, R> of(final L left, final M middle, final R right) {
        return new ImmutableTriple<L, M, R>(left, middle, right);
    }
    
    public abstract L getLeft();
    
    public abstract M getMiddle();
    
    public abstract R getRight();
    
    @Override
    public int compareTo(final Triple<L, M, R> other) {
        return new CompareToBuilder().append(this.getLeft(), other.getLeft()).append(this.getMiddle(), other.getMiddle()).append(this.getRight(), other.getRight()).toComparison();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Triple) {
            final Triple<?, ?, ?> other = (Triple<?, ?, ?>)obj;
            return ObjectUtils.equals(this.getLeft(), other.getLeft()) && ObjectUtils.equals(this.getMiddle(), other.getMiddle()) && ObjectUtils.equals(this.getRight(), other.getRight());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return ((this.getLeft() == null) ? 0 : this.getLeft().hashCode()) ^ ((this.getMiddle() == null) ? 0 : this.getMiddle().hashCode()) ^ ((this.getRight() == null) ? 0 : this.getRight().hashCode());
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append('(').append(this.getLeft()).append(',').append(this.getMiddle()).append(',').append(this.getRight()).append(')').toString();
    }
    
    public String toString(final String format) {
        return String.format(format, this.getLeft(), this.getMiddle(), this.getRight());
    }
}
