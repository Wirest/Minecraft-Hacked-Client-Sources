// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.lang;

import com.ibm.icu.impl.UCharacterName;
import com.ibm.icu.util.ValueIterator;

class UCharacterNameIterator implements ValueIterator
{
    private UCharacterName m_name_;
    private int m_choice_;
    private int m_start_;
    private int m_limit_;
    private int m_current_;
    private int m_groupIndex_;
    private int m_algorithmIndex_;
    private static char[] GROUP_OFFSETS_;
    private static char[] GROUP_LENGTHS_;
    
    public boolean next(final Element element) {
        if (this.m_current_ >= this.m_limit_) {
            return false;
        }
        if (this.m_choice_ == 0 || this.m_choice_ == 2) {
            final int length = this.m_name_.getAlgorithmLength();
            if (this.m_algorithmIndex_ < length) {
                while (this.m_algorithmIndex_ < length && (this.m_algorithmIndex_ < 0 || this.m_name_.getAlgorithmEnd(this.m_algorithmIndex_) < this.m_current_)) {
                    ++this.m_algorithmIndex_;
                }
                if (this.m_algorithmIndex_ < length) {
                    final int start = this.m_name_.getAlgorithmStart(this.m_algorithmIndex_);
                    if (this.m_current_ < start) {
                        int end;
                        if (this.m_limit_ <= (end = start)) {
                            end = this.m_limit_;
                        }
                        if (!this.iterateGroup(element, end)) {
                            ++this.m_current_;
                            return true;
                        }
                    }
                    if (this.m_current_ >= this.m_limit_) {
                        return false;
                    }
                    element.integer = this.m_current_;
                    element.value = this.m_name_.getAlgorithmName(this.m_algorithmIndex_, this.m_current_);
                    this.m_groupIndex_ = -1;
                    ++this.m_current_;
                    return true;
                }
            }
        }
        if (!this.iterateGroup(element, this.m_limit_)) {
            ++this.m_current_;
            return true;
        }
        if (this.m_choice_ == 2 && !this.iterateExtended(element, this.m_limit_)) {
            ++this.m_current_;
            return true;
        }
        return false;
    }
    
    public void reset() {
        this.m_current_ = this.m_start_;
        this.m_groupIndex_ = -1;
        this.m_algorithmIndex_ = -1;
    }
    
    public void setRange(final int start, final int limit) {
        if (start >= limit) {
            throw new IllegalArgumentException("start or limit has to be valid Unicode codepoints and start < limit");
        }
        if (start < 0) {
            this.m_start_ = 0;
        }
        else {
            this.m_start_ = start;
        }
        if (limit > 1114112) {
            this.m_limit_ = 1114112;
        }
        else {
            this.m_limit_ = limit;
        }
        this.m_current_ = this.m_start_;
    }
    
    protected UCharacterNameIterator(final UCharacterName name, final int choice) {
        this.m_groupIndex_ = -1;
        this.m_algorithmIndex_ = -1;
        if (name == null) {
            throw new IllegalArgumentException("UCharacterName name argument cannot be null. Missing unames.icu?");
        }
        this.m_name_ = name;
        this.m_choice_ = choice;
        this.m_start_ = 0;
        this.m_limit_ = 1114112;
        this.m_current_ = this.m_start_;
    }
    
    private boolean iterateSingleGroup(final Element result, final int limit) {
        synchronized (UCharacterNameIterator.GROUP_OFFSETS_) {
            synchronized (UCharacterNameIterator.GROUP_LENGTHS_) {
                final int index = this.m_name_.getGroupLengths(this.m_groupIndex_, UCharacterNameIterator.GROUP_OFFSETS_, UCharacterNameIterator.GROUP_LENGTHS_);
                while (this.m_current_ < limit) {
                    final int offset = UCharacterName.getGroupOffset(this.m_current_);
                    String name = this.m_name_.getGroupName(index + UCharacterNameIterator.GROUP_OFFSETS_[offset], UCharacterNameIterator.GROUP_LENGTHS_[offset], this.m_choice_);
                    if ((name == null || name.length() == 0) && this.m_choice_ == 2) {
                        name = this.m_name_.getExtendedName(this.m_current_);
                    }
                    if (name != null && name.length() > 0) {
                        result.integer = this.m_current_;
                        result.value = name;
                        return false;
                    }
                    ++this.m_current_;
                }
            }
        }
        return true;
    }
    
    private boolean iterateGroup(final Element result, final int limit) {
        if (this.m_groupIndex_ < 0) {
            this.m_groupIndex_ = this.m_name_.getGroup(this.m_current_);
        }
        while (this.m_groupIndex_ < this.m_name_.m_groupcount_ && this.m_current_ < limit) {
            final int startMSB = UCharacterName.getCodepointMSB(this.m_current_);
            final int gMSB = this.m_name_.getGroupMSB(this.m_groupIndex_);
            if (startMSB == gMSB) {
                if (startMSB == UCharacterName.getCodepointMSB(limit - 1)) {
                    return this.iterateSingleGroup(result, limit);
                }
                if (!this.iterateSingleGroup(result, UCharacterName.getGroupLimit(gMSB))) {
                    return false;
                }
                ++this.m_groupIndex_;
            }
            else if (startMSB > gMSB) {
                ++this.m_groupIndex_;
            }
            else {
                int gMIN = UCharacterName.getGroupMin(gMSB);
                if (gMIN > limit) {
                    gMIN = limit;
                }
                if (this.m_choice_ == 2 && !this.iterateExtended(result, gMIN)) {
                    return false;
                }
                this.m_current_ = gMIN;
            }
        }
        return true;
    }
    
    private boolean iterateExtended(final Element result, final int limit) {
        while (this.m_current_ < limit) {
            final String name = this.m_name_.getExtendedOr10Name(this.m_current_);
            if (name != null && name.length() > 0) {
                result.integer = this.m_current_;
                result.value = name;
                return false;
            }
            ++this.m_current_;
        }
        return true;
    }
    
    static {
        UCharacterNameIterator.GROUP_OFFSETS_ = new char[33];
        UCharacterNameIterator.GROUP_LENGTHS_ = new char[33];
    }
}
