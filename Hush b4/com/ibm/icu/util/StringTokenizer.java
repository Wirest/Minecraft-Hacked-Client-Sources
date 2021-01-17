// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import com.ibm.icu.text.UTF16;
import java.util.NoSuchElementException;
import com.ibm.icu.text.UnicodeSet;
import java.util.Enumeration;

public final class StringTokenizer implements Enumeration<Object>
{
    private int m_tokenOffset_;
    private int m_tokenSize_;
    private int[] m_tokenStart_;
    private int[] m_tokenLimit_;
    private UnicodeSet m_delimiters_;
    private String m_source_;
    private int m_length_;
    private int m_nextOffset_;
    private boolean m_returnDelimiters_;
    private boolean m_coalesceDelimiters_;
    private static final UnicodeSet DEFAULT_DELIMITERS_;
    private static final int TOKEN_SIZE_ = 100;
    private static final UnicodeSet EMPTY_DELIMITER_;
    private boolean[] delims;
    
    public StringTokenizer(final String str, final UnicodeSet delim, final boolean returndelims) {
        this(str, delim, returndelims, false);
    }
    
    @Deprecated
    public StringTokenizer(final String str, final UnicodeSet delim, final boolean returndelims, final boolean coalescedelims) {
        this.m_source_ = str;
        this.m_length_ = str.length();
        if (delim == null) {
            this.m_delimiters_ = StringTokenizer.EMPTY_DELIMITER_;
        }
        else {
            this.m_delimiters_ = delim;
        }
        this.m_returnDelimiters_ = returndelims;
        this.m_coalesceDelimiters_ = coalescedelims;
        this.m_tokenOffset_ = -1;
        this.m_tokenSize_ = -1;
        if (this.m_length_ == 0) {
            this.m_nextOffset_ = -1;
        }
        else {
            this.m_nextOffset_ = 0;
            if (!returndelims) {
                this.m_nextOffset_ = this.getNextNonDelimiter(0);
            }
        }
    }
    
    public StringTokenizer(final String str, final UnicodeSet delim) {
        this(str, delim, false, false);
    }
    
    public StringTokenizer(final String str, final String delim, final boolean returndelims) {
        this(str, delim, returndelims, false);
    }
    
    @Deprecated
    public StringTokenizer(final String str, final String delim, final boolean returndelims, final boolean coalescedelims) {
        this.m_delimiters_ = StringTokenizer.EMPTY_DELIMITER_;
        if (delim != null && delim.length() > 0) {
            (this.m_delimiters_ = new UnicodeSet()).addAll(delim);
            this.checkDelimiters();
        }
        this.m_coalesceDelimiters_ = coalescedelims;
        this.m_source_ = str;
        this.m_length_ = str.length();
        this.m_returnDelimiters_ = returndelims;
        this.m_tokenOffset_ = -1;
        this.m_tokenSize_ = -1;
        if (this.m_length_ == 0) {
            this.m_nextOffset_ = -1;
        }
        else {
            this.m_nextOffset_ = 0;
            if (!returndelims) {
                this.m_nextOffset_ = this.getNextNonDelimiter(0);
            }
        }
    }
    
    public StringTokenizer(final String str, final String delim) {
        this(str, delim, false, false);
    }
    
    public StringTokenizer(final String str) {
        this(str, StringTokenizer.DEFAULT_DELIMITERS_, false, false);
    }
    
    public boolean hasMoreTokens() {
        return this.m_nextOffset_ >= 0;
    }
    
    public String nextToken() {
        if (this.m_tokenOffset_ < 0) {
            if (this.m_nextOffset_ < 0) {
                throw new NoSuchElementException("No more tokens in String");
            }
            if (this.m_returnDelimiters_) {
                int tokenlimit = 0;
                final int c = UTF16.charAt(this.m_source_, this.m_nextOffset_);
                final boolean contains = (this.delims == null) ? this.m_delimiters_.contains(c) : (c < this.delims.length && this.delims[c]);
                if (contains) {
                    if (this.m_coalesceDelimiters_) {
                        tokenlimit = this.getNextNonDelimiter(this.m_nextOffset_);
                    }
                    else {
                        tokenlimit = this.m_nextOffset_ + UTF16.getCharCount(c);
                        if (tokenlimit == this.m_length_) {
                            tokenlimit = -1;
                        }
                    }
                }
                else {
                    tokenlimit = this.getNextDelimiter(this.m_nextOffset_);
                }
                String result;
                if (tokenlimit < 0) {
                    result = this.m_source_.substring(this.m_nextOffset_);
                }
                else {
                    result = this.m_source_.substring(this.m_nextOffset_, tokenlimit);
                }
                this.m_nextOffset_ = tokenlimit;
                return result;
            }
            int tokenlimit = this.getNextDelimiter(this.m_nextOffset_);
            String result2;
            if (tokenlimit < 0) {
                result2 = this.m_source_.substring(this.m_nextOffset_);
                this.m_nextOffset_ = tokenlimit;
            }
            else {
                result2 = this.m_source_.substring(this.m_nextOffset_, tokenlimit);
                this.m_nextOffset_ = this.getNextNonDelimiter(tokenlimit);
            }
            return result2;
        }
        else {
            if (this.m_tokenOffset_ >= this.m_tokenSize_) {
                throw new NoSuchElementException("No more tokens in String");
            }
            String result3;
            if (this.m_tokenLimit_[this.m_tokenOffset_] >= 0) {
                result3 = this.m_source_.substring(this.m_tokenStart_[this.m_tokenOffset_], this.m_tokenLimit_[this.m_tokenOffset_]);
            }
            else {
                result3 = this.m_source_.substring(this.m_tokenStart_[this.m_tokenOffset_]);
            }
            ++this.m_tokenOffset_;
            this.m_nextOffset_ = -1;
            if (this.m_tokenOffset_ < this.m_tokenSize_) {
                this.m_nextOffset_ = this.m_tokenStart_[this.m_tokenOffset_];
            }
            return result3;
        }
    }
    
    public String nextToken(final String delim) {
        this.m_delimiters_ = StringTokenizer.EMPTY_DELIMITER_;
        if (delim != null && delim.length() > 0) {
            (this.m_delimiters_ = new UnicodeSet()).addAll(delim);
        }
        return this.nextToken(this.m_delimiters_);
    }
    
    public String nextToken(final UnicodeSet delim) {
        this.m_delimiters_ = delim;
        this.checkDelimiters();
        this.m_tokenOffset_ = -1;
        this.m_tokenSize_ = -1;
        if (!this.m_returnDelimiters_) {
            this.m_nextOffset_ = this.getNextNonDelimiter(this.m_nextOffset_);
        }
        return this.nextToken();
    }
    
    public boolean hasMoreElements() {
        return this.hasMoreTokens();
    }
    
    public Object nextElement() {
        return this.nextToken();
    }
    
    public int countTokens() {
        int result = 0;
        if (this.hasMoreTokens()) {
            if (this.m_tokenOffset_ >= 0) {
                return this.m_tokenSize_ - this.m_tokenOffset_;
            }
            if (this.m_tokenStart_ == null) {
                this.m_tokenStart_ = new int[100];
                this.m_tokenLimit_ = new int[100];
            }
            do {
                if (this.m_tokenStart_.length == result) {
                    final int[] temptokenindex = this.m_tokenStart_;
                    final int[] temptokensize = this.m_tokenLimit_;
                    final int originalsize = temptokenindex.length;
                    final int newsize = originalsize + 100;
                    this.m_tokenStart_ = new int[newsize];
                    this.m_tokenLimit_ = new int[newsize];
                    System.arraycopy(temptokenindex, 0, this.m_tokenStart_, 0, originalsize);
                    System.arraycopy(temptokensize, 0, this.m_tokenLimit_, 0, originalsize);
                }
                this.m_tokenStart_[result] = this.m_nextOffset_;
                if (this.m_returnDelimiters_) {
                    final int c = UTF16.charAt(this.m_source_, this.m_nextOffset_);
                    final boolean contains = (this.delims == null) ? this.m_delimiters_.contains(c) : (c < this.delims.length && this.delims[c]);
                    if (contains) {
                        if (this.m_coalesceDelimiters_) {
                            this.m_tokenLimit_[result] = this.getNextNonDelimiter(this.m_nextOffset_);
                        }
                        else {
                            int p = this.m_nextOffset_ + 1;
                            if (p == this.m_length_) {
                                p = -1;
                            }
                            this.m_tokenLimit_[result] = p;
                        }
                    }
                    else {
                        this.m_tokenLimit_[result] = this.getNextDelimiter(this.m_nextOffset_);
                    }
                    this.m_nextOffset_ = this.m_tokenLimit_[result];
                }
                else {
                    this.m_tokenLimit_[result] = this.getNextDelimiter(this.m_nextOffset_);
                    this.m_nextOffset_ = this.getNextNonDelimiter(this.m_tokenLimit_[result]);
                }
                ++result;
            } while (this.m_nextOffset_ >= 0);
            this.m_tokenOffset_ = 0;
            this.m_tokenSize_ = result;
            this.m_nextOffset_ = this.m_tokenStart_[0];
        }
        return result;
    }
    
    private int getNextDelimiter(final int offset) {
        if (offset >= 0) {
            int result = offset;
            int c = 0;
            if (this.delims == null) {
                do {
                    c = UTF16.charAt(this.m_source_, result);
                    if (this.m_delimiters_.contains(c)) {
                        break;
                    }
                } while (++result < this.m_length_);
            }
            else {
                do {
                    c = UTF16.charAt(this.m_source_, result);
                    if (c < this.delims.length && this.delims[c]) {
                        break;
                    }
                } while (++result < this.m_length_);
            }
            if (result < this.m_length_) {
                return result;
            }
        }
        return -1 - this.m_length_;
    }
    
    private int getNextNonDelimiter(final int offset) {
        if (offset >= 0) {
            int result = offset;
            int c = 0;
            if (this.delims == null) {
                do {
                    c = UTF16.charAt(this.m_source_, result);
                    if (!this.m_delimiters_.contains(c)) {
                        break;
                    }
                } while (++result < this.m_length_);
            }
            else {
                do {
                    c = UTF16.charAt(this.m_source_, result);
                    if (c >= this.delims.length) {
                        break;
                    }
                    if (!this.delims[c]) {
                        break;
                    }
                } while (++result < this.m_length_);
            }
            if (result < this.m_length_) {
                return result;
            }
        }
        return -1 - this.m_length_;
    }
    
    void checkDelimiters() {
        if (this.m_delimiters_ == null || this.m_delimiters_.size() == 0) {
            this.delims = new boolean[0];
        }
        else {
            final int maxChar = this.m_delimiters_.getRangeEnd(this.m_delimiters_.getRangeCount() - 1);
            if (maxChar < 127) {
                this.delims = new boolean[maxChar + 1];
                int ch;
                for (int i = 0; -1 != (ch = this.m_delimiters_.charAt(i)); ++i) {
                    this.delims[ch] = true;
                }
            }
            else {
                this.delims = null;
            }
        }
    }
    
    static {
        DEFAULT_DELIMITERS_ = new UnicodeSet(new int[] { 9, 10, 12, 13, 32, 32 });
        EMPTY_DELIMITER_ = UnicodeSet.EMPTY;
    }
}
