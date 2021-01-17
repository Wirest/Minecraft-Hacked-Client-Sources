// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.text.CharacterIterator;
import com.ibm.icu.text.UCharacterIterator;

public class CharacterIteratorWrapper extends UCharacterIterator
{
    private CharacterIterator iterator;
    
    public CharacterIteratorWrapper(final CharacterIterator iter) {
        if (iter == null) {
            throw new IllegalArgumentException();
        }
        this.iterator = iter;
    }
    
    @Override
    public int current() {
        final int c = this.iterator.current();
        if (c == 65535) {
            return -1;
        }
        return c;
    }
    
    @Override
    public int getLength() {
        return this.iterator.getEndIndex() - this.iterator.getBeginIndex();
    }
    
    @Override
    public int getIndex() {
        return this.iterator.getIndex();
    }
    
    @Override
    public int next() {
        final int i = this.iterator.current();
        this.iterator.next();
        if (i == 65535) {
            return -1;
        }
        return i;
    }
    
    @Override
    public int previous() {
        final int i = this.iterator.previous();
        if (i == 65535) {
            return -1;
        }
        return i;
    }
    
    @Override
    public void setIndex(final int index) {
        try {
            this.iterator.setIndex(index);
        }
        catch (IllegalArgumentException e) {
            throw new IndexOutOfBoundsException();
        }
    }
    
    @Override
    public void setToLimit() {
        this.iterator.setIndex(this.iterator.getEndIndex());
    }
    
    @Override
    public int getText(final char[] fillIn, int offset) {
        final int length = this.iterator.getEndIndex() - this.iterator.getBeginIndex();
        final int currentIndex = this.iterator.getIndex();
        if (offset < 0 || offset + length > fillIn.length) {
            throw new IndexOutOfBoundsException(Integer.toString(length));
        }
        for (char ch = this.iterator.first(); ch != '\uffff'; ch = this.iterator.next()) {
            fillIn[offset++] = ch;
        }
        this.iterator.setIndex(currentIndex);
        return length;
    }
    
    @Override
    public Object clone() {
        try {
            final CharacterIteratorWrapper result = (CharacterIteratorWrapper)super.clone();
            result.iterator = (CharacterIterator)this.iterator.clone();
            return result;
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }
    
    @Override
    public int moveIndex(final int delta) {
        final int length = this.iterator.getEndIndex() - this.iterator.getBeginIndex();
        int idx = this.iterator.getIndex() + delta;
        if (idx < 0) {
            idx = 0;
        }
        else if (idx > length) {
            idx = length;
        }
        return this.iterator.setIndex(idx);
    }
    
    @Override
    public CharacterIterator getCharacterIterator() {
        return (CharacterIterator)this.iterator.clone();
    }
}
