// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.text.UCharacterIterator;
import java.text.CharacterIterator;

public class UCharacterIteratorWrapper implements CharacterIterator
{
    private UCharacterIterator iterator;
    
    public UCharacterIteratorWrapper(final UCharacterIterator iter) {
        this.iterator = iter;
    }
    
    public char first() {
        this.iterator.setToStart();
        return (char)this.iterator.current();
    }
    
    public char last() {
        this.iterator.setToLimit();
        return (char)this.iterator.previous();
    }
    
    public char current() {
        return (char)this.iterator.current();
    }
    
    public char next() {
        this.iterator.next();
        return (char)this.iterator.current();
    }
    
    public char previous() {
        return (char)this.iterator.previous();
    }
    
    public char setIndex(final int position) {
        this.iterator.setIndex(position);
        return (char)this.iterator.current();
    }
    
    public int getBeginIndex() {
        return 0;
    }
    
    public int getEndIndex() {
        return this.iterator.getLength();
    }
    
    public int getIndex() {
        return this.iterator.getIndex();
    }
    
    public Object clone() {
        try {
            final UCharacterIteratorWrapper result = (UCharacterIteratorWrapper)super.clone();
            result.iterator = (UCharacterIterator)this.iterator.clone();
            return result;
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
