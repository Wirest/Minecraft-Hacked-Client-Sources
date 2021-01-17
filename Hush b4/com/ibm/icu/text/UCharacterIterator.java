// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.impl.UCharacterIteratorWrapper;
import com.ibm.icu.impl.CharacterIteratorWrapper;
import java.text.CharacterIterator;
import com.ibm.icu.impl.UCharArrayIterator;
import com.ibm.icu.impl.ReplaceableUCharacterIterator;

public abstract class UCharacterIterator implements Cloneable, UForwardCharacterIterator
{
    protected UCharacterIterator() {
    }
    
    public static final UCharacterIterator getInstance(final Replaceable source) {
        return new ReplaceableUCharacterIterator(source);
    }
    
    public static final UCharacterIterator getInstance(final String source) {
        return new ReplaceableUCharacterIterator(source);
    }
    
    public static final UCharacterIterator getInstance(final char[] source) {
        return getInstance(source, 0, source.length);
    }
    
    public static final UCharacterIterator getInstance(final char[] source, final int start, final int limit) {
        return new UCharArrayIterator(source, start, limit);
    }
    
    public static final UCharacterIterator getInstance(final StringBuffer source) {
        return new ReplaceableUCharacterIterator(source);
    }
    
    public static final UCharacterIterator getInstance(final CharacterIterator source) {
        return new CharacterIteratorWrapper(source);
    }
    
    public CharacterIterator getCharacterIterator() {
        return new UCharacterIteratorWrapper(this);
    }
    
    public abstract int current();
    
    public int currentCodePoint() {
        final int ch = this.current();
        if (UTF16.isLeadSurrogate((char)ch)) {
            this.next();
            final int ch2 = this.current();
            this.previous();
            if (UTF16.isTrailSurrogate((char)ch2)) {
                return UCharacterProperty.getRawSupplementary((char)ch, (char)ch2);
            }
        }
        return ch;
    }
    
    public abstract int getLength();
    
    public abstract int getIndex();
    
    public abstract int next();
    
    public int nextCodePoint() {
        final int ch1 = this.next();
        if (UTF16.isLeadSurrogate((char)ch1)) {
            final int ch2 = this.next();
            if (UTF16.isTrailSurrogate((char)ch2)) {
                return UCharacterProperty.getRawSupplementary((char)ch1, (char)ch2);
            }
            if (ch2 != -1) {
                this.previous();
            }
        }
        return ch1;
    }
    
    public abstract int previous();
    
    public int previousCodePoint() {
        final int ch1 = this.previous();
        if (UTF16.isTrailSurrogate((char)ch1)) {
            final int ch2 = this.previous();
            if (UTF16.isLeadSurrogate((char)ch2)) {
                return UCharacterProperty.getRawSupplementary((char)ch2, (char)ch1);
            }
            if (ch2 != -1) {
                this.next();
            }
        }
        return ch1;
    }
    
    public abstract void setIndex(final int p0);
    
    public void setToLimit() {
        this.setIndex(this.getLength());
    }
    
    public void setToStart() {
        this.setIndex(0);
    }
    
    public abstract int getText(final char[] p0, final int p1);
    
    public final int getText(final char[] fillIn) {
        return this.getText(fillIn, 0);
    }
    
    public String getText() {
        final char[] text = new char[this.getLength()];
        this.getText(text);
        return new String(text);
    }
    
    public int moveIndex(final int delta) {
        final int x = Math.max(0, Math.min(this.getIndex() + delta, this.getLength()));
        this.setIndex(x);
        return x;
    }
    
    public int moveCodePointIndex(int delta) {
        if (delta > 0) {
            while (delta > 0 && this.nextCodePoint() != -1) {
                --delta;
            }
        }
        else {
            while (delta < 0 && this.previousCodePoint() != -1) {
                ++delta;
            }
        }
        if (delta != 0) {
            throw new IndexOutOfBoundsException();
        }
        return this.getIndex();
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
