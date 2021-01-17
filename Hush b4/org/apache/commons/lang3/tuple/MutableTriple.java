// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.tuple;

public class MutableTriple<L, M, R> extends Triple<L, M, R>
{
    private static final long serialVersionUID = 1L;
    public L left;
    public M middle;
    public R right;
    
    public static <L, M, R> MutableTriple<L, M, R> of(final L left, final M middle, final R right) {
        return new MutableTriple<L, M, R>(left, middle, right);
    }
    
    public MutableTriple() {
    }
    
    public MutableTriple(final L left, final M middle, final R right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }
    
    @Override
    public L getLeft() {
        return this.left;
    }
    
    public void setLeft(final L left) {
        this.left = left;
    }
    
    @Override
    public M getMiddle() {
        return this.middle;
    }
    
    public void setMiddle(final M middle) {
        this.middle = middle;
    }
    
    @Override
    public R getRight() {
        return this.right;
    }
    
    public void setRight(final R right) {
        this.right = right;
    }
}
