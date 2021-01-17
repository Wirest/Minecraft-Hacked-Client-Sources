// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.text.UCharacterIterator;

public final class StringUCharacterIterator extends UCharacterIterator
{
    private String m_text_;
    private int m_currentIndex_;
    
    public StringUCharacterIterator(final String str) {
        if (str == null) {
            throw new IllegalArgumentException();
        }
        this.m_text_ = str;
        this.m_currentIndex_ = 0;
    }
    
    public StringUCharacterIterator() {
        this.m_text_ = "";
        this.m_currentIndex_ = 0;
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
    
    @Override
    public int current() {
        if (this.m_currentIndex_ < this.m_text_.length()) {
            return this.m_text_.charAt(this.m_currentIndex_);
        }
        return -1;
    }
    
    @Override
    public int getLength() {
        return this.m_text_.length();
    }
    
    @Override
    public int getIndex() {
        return this.m_currentIndex_;
    }
    
    @Override
    public int next() {
        if (this.m_currentIndex_ < this.m_text_.length()) {
            return this.m_text_.charAt(this.m_currentIndex_++);
        }
        return -1;
    }
    
    @Override
    public int previous() {
        if (this.m_currentIndex_ > 0) {
            final String text_ = this.m_text_;
            final int n = this.m_currentIndex_ - 1;
            this.m_currentIndex_ = n;
            return text_.charAt(n);
        }
        return -1;
    }
    
    @Override
    public void setIndex(final int currentIndex) throws IndexOutOfBoundsException {
        if (currentIndex < 0 || currentIndex > this.m_text_.length()) {
            throw new IndexOutOfBoundsException();
        }
        this.m_currentIndex_ = currentIndex;
    }
    
    @Override
    public int getText(final char[] fillIn, final int offset) {
        final int length = this.m_text_.length();
        if (offset < 0 || offset + length > fillIn.length) {
            throw new IndexOutOfBoundsException(Integer.toString(length));
        }
        this.m_text_.getChars(0, length, fillIn, offset);
        return length;
    }
    
    @Override
    public String getText() {
        return this.m_text_;
    }
    
    public void setText(final String text) {
        if (text == null) {
            throw new NullPointerException();
        }
        this.m_text_ = text;
        this.m_currentIndex_ = 0;
    }
}
