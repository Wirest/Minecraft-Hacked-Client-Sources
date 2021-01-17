// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Arrays;

public abstract class Trie
{
    protected static final int LEAD_INDEX_OFFSET_ = 320;
    protected static final int INDEX_STAGE_1_SHIFT_ = 5;
    protected static final int INDEX_STAGE_2_SHIFT_ = 2;
    protected static final int DATA_BLOCK_LENGTH = 32;
    protected static final int INDEX_STAGE_3_MASK_ = 31;
    protected static final int SURROGATE_BLOCK_BITS = 5;
    protected static final int SURROGATE_BLOCK_COUNT = 32;
    protected static final int BMP_INDEX_LENGTH = 2048;
    protected static final int SURROGATE_MASK_ = 1023;
    protected char[] m_index_;
    protected DataManipulate m_dataManipulate_;
    protected int m_dataOffset_;
    protected int m_dataLength_;
    protected static final int HEADER_LENGTH_ = 16;
    protected static final int HEADER_OPTIONS_LATIN1_IS_LINEAR_MASK_ = 512;
    protected static final int HEADER_SIGNATURE_ = 1416784229;
    private static final int HEADER_OPTIONS_SHIFT_MASK_ = 15;
    protected static final int HEADER_OPTIONS_INDEX_SHIFT_ = 4;
    protected static final int HEADER_OPTIONS_DATA_IS_32_BIT_ = 256;
    private boolean m_isLatin1Linear_;
    private int m_options_;
    
    public final boolean isLatin1Linear() {
        return this.m_isLatin1Linear_;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Trie)) {
            return false;
        }
        final Trie othertrie = (Trie)other;
        return this.m_isLatin1Linear_ == othertrie.m_isLatin1Linear_ && this.m_options_ == othertrie.m_options_ && this.m_dataLength_ == othertrie.m_dataLength_ && Arrays.equals(this.m_index_, othertrie.m_index_);
    }
    
    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42;
    }
    
    public int getSerializedDataSize() {
        int result = 16;
        result += this.m_dataOffset_ << 1;
        if (this.isCharTrie()) {
            result += this.m_dataLength_ << 1;
        }
        else if (this.isIntTrie()) {
            result += this.m_dataLength_ << 2;
        }
        return result;
    }
    
    protected Trie(final InputStream inputStream, final DataManipulate dataManipulate) throws IOException {
        final DataInputStream input = new DataInputStream(inputStream);
        final int signature = input.readInt();
        this.m_options_ = input.readInt();
        if (!this.checkHeader(signature)) {
            throw new IllegalArgumentException("ICU data file error: Trie header authentication failed, please check if you have the most updated ICU data file");
        }
        if (dataManipulate != null) {
            this.m_dataManipulate_ = dataManipulate;
        }
        else {
            this.m_dataManipulate_ = new DefaultGetFoldingOffset();
        }
        this.m_isLatin1Linear_ = ((this.m_options_ & 0x200) != 0x0);
        this.m_dataOffset_ = input.readInt();
        this.m_dataLength_ = input.readInt();
        this.unserialize(inputStream);
    }
    
    protected Trie(final char[] index, final int options, final DataManipulate dataManipulate) {
        this.m_options_ = options;
        if (dataManipulate != null) {
            this.m_dataManipulate_ = dataManipulate;
        }
        else {
            this.m_dataManipulate_ = new DefaultGetFoldingOffset();
        }
        this.m_isLatin1Linear_ = ((this.m_options_ & 0x200) != 0x0);
        this.m_index_ = index;
        this.m_dataOffset_ = this.m_index_.length;
    }
    
    protected abstract int getSurrogateOffset(final char p0, final char p1);
    
    protected abstract int getValue(final int p0);
    
    protected abstract int getInitialValue();
    
    protected final int getRawOffset(final int offset, final char ch) {
        return (this.m_index_[offset + (ch >> 5)] << 2) + (ch & '\u001f');
    }
    
    protected final int getBMPOffset(final char ch) {
        return (ch >= '\ud800' && ch <= '\udbff') ? this.getRawOffset(320, ch) : this.getRawOffset(0, ch);
    }
    
    protected final int getLeadOffset(final char ch) {
        return this.getRawOffset(0, ch);
    }
    
    protected final int getCodePointOffset(final int ch) {
        if (ch < 0) {
            return -1;
        }
        if (ch < 55296) {
            return this.getRawOffset(0, (char)ch);
        }
        if (ch < 65536) {
            return this.getBMPOffset((char)ch);
        }
        if (ch <= 1114111) {
            return this.getSurrogateOffset(UTF16.getLeadSurrogate(ch), (char)(ch & 0x3FF));
        }
        return -1;
    }
    
    protected void unserialize(final InputStream inputStream) throws IOException {
        this.m_index_ = new char[this.m_dataOffset_];
        final DataInputStream input = new DataInputStream(inputStream);
        for (int i = 0; i < this.m_dataOffset_; ++i) {
            this.m_index_[i] = input.readChar();
        }
    }
    
    protected final boolean isIntTrie() {
        return (this.m_options_ & 0x100) != 0x0;
    }
    
    protected final boolean isCharTrie() {
        return (this.m_options_ & 0x100) == 0x0;
    }
    
    private final boolean checkHeader(final int signature) {
        return signature == 1416784229 && (this.m_options_ & 0xF) == 0x5 && (this.m_options_ >> 4 & 0xF) == 0x2;
    }
    
    private static class DefaultGetFoldingOffset implements DataManipulate
    {
        public int getFoldingOffset(final int value) {
            return value;
        }
    }
    
    public interface DataManipulate
    {
        int getFoldingOffset(final int p0);
    }
}
