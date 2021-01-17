// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.text.UCharacterIterator;

public final class UCharArrayIterator extends UCharacterIterator
{
    private final char[] text;
    private final int start;
    private final int limit;
    private int pos;
    
    public UCharArrayIterator(final char[] text, final int start, final int limit) {
        if (start < 0 || limit > text.length || start > limit) {
            throw new IllegalArgumentException("start: " + start + " or limit: " + limit + " out of range [0, " + text.length + ")");
        }
        this.text = text;
        this.start = start;
        this.limit = limit;
        this.pos = start;
    }
    
    @Override
    public int current() {
        return (this.pos < this.limit) ? this.text[this.pos] : -1;
    }
    
    @Override
    public int getLength() {
        return this.limit - this.start;
    }
    
    @Override
    public int getIndex() {
        return this.pos - this.start;
    }
    
    @Override
    public int next() {
        return (this.pos < this.limit) ? this.text[this.pos++] : -1;
    }
    
    @Override
    public int previous() {
        int n;
        if (this.pos > this.start) {
            final char[] text = this.text;
            final int pos = this.pos - 1;
            this.pos = pos;
            n = text[pos];
        }
        else {
            n = -1;
        }
        return n;
    }
    
    @Override
    public void setIndex(final int index) {
        if (index < 0 || index > this.limit - this.start) {
            throw new IndexOutOfBoundsException("index: " + index + " out of range [0, " + (this.limit - this.start) + ")");
        }
        this.pos = this.start + index;
    }
    
    @Override
    public int getText(final char[] fillIn, final int offset) {
        final int len = this.limit - this.start;
        System.arraycopy(this.text, this.start, fillIn, offset, len);
        return len;
    }
    
    @Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
