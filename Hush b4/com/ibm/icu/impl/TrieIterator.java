// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import com.ibm.icu.util.RangeValueIterator;

public class TrieIterator implements RangeValueIterator
{
    private static final int BMP_INDEX_LENGTH_ = 2048;
    private static final int LEAD_SURROGATE_MIN_VALUE_ = 55296;
    private static final int TRAIL_SURROGATE_MIN_VALUE_ = 56320;
    private static final int TRAIL_SURROGATE_COUNT_ = 1024;
    private static final int TRAIL_SURROGATE_INDEX_BLOCK_LENGTH_ = 32;
    private static final int DATA_BLOCK_LENGTH_ = 32;
    private Trie m_trie_;
    private int m_initialValue_;
    private int m_currentCodepoint_;
    private int m_nextCodepoint_;
    private int m_nextValue_;
    private int m_nextIndex_;
    private int m_nextBlock_;
    private int m_nextBlockIndex_;
    private int m_nextTrailIndexOffset_;
    
    public TrieIterator(final Trie trie) {
        if (trie == null) {
            throw new IllegalArgumentException("Argument trie cannot be null");
        }
        this.m_trie_ = trie;
        this.m_initialValue_ = this.extract(this.m_trie_.getInitialValue());
        this.reset();
    }
    
    public final boolean next(final Element element) {
        if (this.m_nextCodepoint_ > 1114111) {
            return false;
        }
        if (this.m_nextCodepoint_ < 65536 && this.calculateNextBMPElement(element)) {
            return true;
        }
        this.calculateNextSupplementaryElement(element);
        return true;
    }
    
    public final void reset() {
        this.m_currentCodepoint_ = 0;
        this.m_nextCodepoint_ = 0;
        this.m_nextIndex_ = 0;
        this.m_nextBlock_ = this.m_trie_.m_index_[0] << 2;
        if (this.m_nextBlock_ == this.m_trie_.m_dataOffset_) {
            this.m_nextValue_ = this.m_initialValue_;
        }
        else {
            this.m_nextValue_ = this.extract(this.m_trie_.getValue(this.m_nextBlock_));
        }
        this.m_nextBlockIndex_ = 0;
        this.m_nextTrailIndexOffset_ = 32;
    }
    
    protected int extract(final int value) {
        return value;
    }
    
    private final void setResult(final Element element, final int start, final int limit, final int value) {
        element.start = start;
        element.limit = limit;
        element.value = value;
    }
    
    private final boolean calculateNextBMPElement(final Element element) {
        final int currentValue = this.m_nextValue_;
        this.m_currentCodepoint_ = this.m_nextCodepoint_;
        ++this.m_nextCodepoint_;
        ++this.m_nextBlockIndex_;
        if (!this.checkBlockDetail(currentValue)) {
            this.setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
            return true;
        }
        while (this.m_nextCodepoint_ < 65536) {
            if (this.m_nextCodepoint_ == 55296) {
                this.m_nextIndex_ = 2048;
            }
            else if (this.m_nextCodepoint_ == 56320) {
                this.m_nextIndex_ = this.m_nextCodepoint_ >> 5;
            }
            else {
                ++this.m_nextIndex_;
            }
            this.m_nextBlockIndex_ = 0;
            if (!this.checkBlock(currentValue)) {
                this.setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
                return true;
            }
        }
        --this.m_nextCodepoint_;
        --this.m_nextBlockIndex_;
        return false;
    }
    
    private final void calculateNextSupplementaryElement(final Element element) {
        final int currentValue = this.m_nextValue_;
        ++this.m_nextCodepoint_;
        ++this.m_nextBlockIndex_;
        if (UTF16.getTrailSurrogate(this.m_nextCodepoint_) != '\udc00') {
            if (!this.checkNullNextTrailIndex() && !this.checkBlockDetail(currentValue)) {
                this.setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
                this.m_currentCodepoint_ = this.m_nextCodepoint_;
                return;
            }
            ++this.m_nextIndex_;
            ++this.m_nextTrailIndexOffset_;
            if (!this.checkTrailBlock(currentValue)) {
                this.setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
                this.m_currentCodepoint_ = this.m_nextCodepoint_;
                return;
            }
        }
        int nextLead = UTF16.getLeadSurrogate(this.m_nextCodepoint_);
        while (nextLead < 56320) {
            final int leadBlock = this.m_trie_.m_index_[nextLead >> 5] << 2;
            if (leadBlock == this.m_trie_.m_dataOffset_) {
                if (currentValue != this.m_initialValue_) {
                    this.m_nextValue_ = this.m_initialValue_;
                    this.m_nextBlock_ = leadBlock;
                    this.m_nextBlockIndex_ = 0;
                    this.setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
                    this.m_currentCodepoint_ = this.m_nextCodepoint_;
                    return;
                }
                nextLead += 32;
                this.m_nextCodepoint_ = UCharacterProperty.getRawSupplementary((char)nextLead, '\udc00');
            }
            else {
                if (this.m_trie_.m_dataManipulate_ == null) {
                    throw new NullPointerException("The field DataManipulate in this Trie is null");
                }
                this.m_nextIndex_ = this.m_trie_.m_dataManipulate_.getFoldingOffset(this.m_trie_.getValue(leadBlock + (nextLead & 0x1F)));
                if (this.m_nextIndex_ <= 0) {
                    if (currentValue != this.m_initialValue_) {
                        this.m_nextValue_ = this.m_initialValue_;
                        this.m_nextBlock_ = this.m_trie_.m_dataOffset_;
                        this.m_nextBlockIndex_ = 0;
                        this.setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
                        this.m_currentCodepoint_ = this.m_nextCodepoint_;
                        return;
                    }
                    this.m_nextCodepoint_ += 1024;
                }
                else {
                    this.m_nextTrailIndexOffset_ = 0;
                    if (!this.checkTrailBlock(currentValue)) {
                        this.setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
                        this.m_currentCodepoint_ = this.m_nextCodepoint_;
                        return;
                    }
                }
                ++nextLead;
            }
        }
        this.setResult(element, this.m_currentCodepoint_, 1114112, currentValue);
    }
    
    private final boolean checkBlockDetail(final int currentValue) {
        while (this.m_nextBlockIndex_ < 32) {
            this.m_nextValue_ = this.extract(this.m_trie_.getValue(this.m_nextBlock_ + this.m_nextBlockIndex_));
            if (this.m_nextValue_ != currentValue) {
                return false;
            }
            ++this.m_nextBlockIndex_;
            ++this.m_nextCodepoint_;
        }
        return true;
    }
    
    private final boolean checkBlock(final int currentValue) {
        final int currentBlock = this.m_nextBlock_;
        this.m_nextBlock_ = this.m_trie_.m_index_[this.m_nextIndex_] << 2;
        if (this.m_nextBlock_ == currentBlock && this.m_nextCodepoint_ - this.m_currentCodepoint_ >= 32) {
            this.m_nextCodepoint_ += 32;
        }
        else if (this.m_nextBlock_ == this.m_trie_.m_dataOffset_) {
            if (currentValue != this.m_initialValue_) {
                this.m_nextValue_ = this.m_initialValue_;
                this.m_nextBlockIndex_ = 0;
                return false;
            }
            this.m_nextCodepoint_ += 32;
        }
        else if (!this.checkBlockDetail(currentValue)) {
            return false;
        }
        return true;
    }
    
    private final boolean checkTrailBlock(final int currentValue) {
        while (this.m_nextTrailIndexOffset_ < 32) {
            this.m_nextBlockIndex_ = 0;
            if (!this.checkBlock(currentValue)) {
                return false;
            }
            ++this.m_nextTrailIndexOffset_;
            ++this.m_nextIndex_;
        }
        return true;
    }
    
    private final boolean checkNullNextTrailIndex() {
        if (this.m_nextIndex_ > 0) {
            return false;
        }
        this.m_nextCodepoint_ += 1023;
        final int nextLead = UTF16.getLeadSurrogate(this.m_nextCodepoint_);
        final int leadBlock = this.m_trie_.m_index_[nextLead >> 5] << 2;
        if (this.m_trie_.m_dataManipulate_ == null) {
            throw new NullPointerException("The field DataManipulate in this Trie is null");
        }
        this.m_nextIndex_ = this.m_trie_.m_dataManipulate_.getFoldingOffset(this.m_trie_.getValue(leadBlock + (nextLead & 0x1F)));
        --this.m_nextIndex_;
        this.m_nextBlockIndex_ = 32;
        return true;
    }
}
