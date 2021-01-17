// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.CharacterIterator;

public abstract class SearchIterator
{
    public static final int DONE = -1;
    protected BreakIterator breakIterator;
    protected CharacterIterator targetText;
    protected int matchLength;
    private boolean m_isForwardSearching_;
    private boolean m_isOverlap_;
    private boolean m_reset_;
    private int m_setOffset_;
    private int m_lastMatchStart_;
    
    public void setIndex(final int position) {
        if (position < this.targetText.getBeginIndex() || position > this.targetText.getEndIndex()) {
            throw new IndexOutOfBoundsException("setIndex(int) expected position to be between " + this.targetText.getBeginIndex() + " and " + this.targetText.getEndIndex());
        }
        this.m_setOffset_ = position;
        this.m_reset_ = false;
        this.matchLength = 0;
    }
    
    public void setOverlapping(final boolean allowOverlap) {
        this.m_isOverlap_ = allowOverlap;
    }
    
    public void setBreakIterator(final BreakIterator breakiter) {
        this.breakIterator = breakiter;
        if (this.breakIterator != null) {
            this.breakIterator.setText(this.targetText);
        }
    }
    
    public void setTarget(final CharacterIterator text) {
        if (text == null || text.getEndIndex() == text.getIndex()) {
            throw new IllegalArgumentException("Illegal null or empty text");
        }
        (this.targetText = text).setIndex(this.targetText.getBeginIndex());
        this.matchLength = 0;
        this.m_reset_ = true;
        this.m_isForwardSearching_ = true;
        if (this.breakIterator != null) {
            this.breakIterator.setText(this.targetText);
        }
    }
    
    public int getMatchStart() {
        return this.m_lastMatchStart_;
    }
    
    public abstract int getIndex();
    
    public int getMatchLength() {
        return this.matchLength;
    }
    
    public BreakIterator getBreakIterator() {
        return this.breakIterator;
    }
    
    public CharacterIterator getTarget() {
        return this.targetText;
    }
    
    public String getMatchedText() {
        if (this.matchLength > 0) {
            final int limit = this.m_lastMatchStart_ + this.matchLength;
            final StringBuilder result = new StringBuilder(this.matchLength);
            result.append(this.targetText.current());
            this.targetText.next();
            while (this.targetText.getIndex() < limit) {
                result.append(this.targetText.current());
                this.targetText.next();
            }
            this.targetText.setIndex(this.m_lastMatchStart_);
            return result.toString();
        }
        return null;
    }
    
    public int next() {
        int start = this.targetText.getIndex();
        if (this.m_setOffset_ != -1) {
            start = this.m_setOffset_;
            this.m_setOffset_ = -1;
        }
        if (this.m_isForwardSearching_) {
            if (!this.m_reset_ && start + this.matchLength >= this.targetText.getEndIndex()) {
                this.matchLength = 0;
                this.targetText.setIndex(this.targetText.getEndIndex());
                return this.m_lastMatchStart_ = -1;
            }
            this.m_reset_ = false;
        }
        else {
            this.m_isForwardSearching_ = true;
            if (start != -1) {
                return start;
            }
        }
        if (start == -1) {
            start = this.targetText.getBeginIndex();
        }
        if (this.matchLength > 0) {
            if (this.m_isOverlap_) {
                ++start;
            }
            else {
                start += this.matchLength;
            }
        }
        return this.m_lastMatchStart_ = this.handleNext(start);
    }
    
    public int previous() {
        int start = this.targetText.getIndex();
        if (this.m_setOffset_ != -1) {
            start = this.m_setOffset_;
            this.m_setOffset_ = -1;
        }
        if (this.m_reset_) {
            this.m_isForwardSearching_ = false;
            this.m_reset_ = false;
            start = this.targetText.getEndIndex();
        }
        if (this.m_isForwardSearching_) {
            this.m_isForwardSearching_ = false;
            if (start != this.targetText.getEndIndex()) {
                return start;
            }
        }
        else if (start == this.targetText.getBeginIndex()) {
            this.matchLength = 0;
            this.targetText.setIndex(this.targetText.getBeginIndex());
            return this.m_lastMatchStart_ = -1;
        }
        return this.m_lastMatchStart_ = this.handlePrevious(start);
    }
    
    public boolean isOverlapping() {
        return this.m_isOverlap_;
    }
    
    public void reset() {
        this.matchLength = 0;
        this.setIndex(this.targetText.getBeginIndex());
        this.m_isOverlap_ = false;
        this.m_isForwardSearching_ = true;
        this.m_reset_ = true;
        this.m_setOffset_ = -1;
    }
    
    public final int first() {
        this.m_isForwardSearching_ = true;
        this.setIndex(this.targetText.getBeginIndex());
        return this.next();
    }
    
    public final int following(final int position) {
        this.m_isForwardSearching_ = true;
        this.setIndex(position);
        return this.next();
    }
    
    public final int last() {
        this.m_isForwardSearching_ = false;
        this.setIndex(this.targetText.getEndIndex());
        return this.previous();
    }
    
    public final int preceding(final int position) {
        this.m_isForwardSearching_ = false;
        this.setIndex(position);
        return this.previous();
    }
    
    protected SearchIterator(final CharacterIterator target, final BreakIterator breaker) {
        if (target == null || target.getEndIndex() - target.getBeginIndex() == 0) {
            throw new IllegalArgumentException("Illegal argument target.  Argument can not be null or of length 0");
        }
        this.targetText = target;
        this.breakIterator = breaker;
        if (this.breakIterator != null) {
            this.breakIterator.setText(target);
        }
        this.matchLength = 0;
        this.m_lastMatchStart_ = -1;
        this.m_isOverlap_ = false;
        this.m_isForwardSearching_ = true;
        this.m_reset_ = true;
        this.m_setOffset_ = -1;
    }
    
    protected void setMatchLength(final int length) {
        this.matchLength = length;
    }
    
    protected abstract int handleNext(final int p0);
    
    protected abstract int handlePrevious(final int p0);
}
