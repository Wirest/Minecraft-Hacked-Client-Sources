// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CharTrie extends Trie
{
    private char m_initialValue_;
    private char[] m_data_;
    
    public CharTrie(final InputStream inputStream, final DataManipulate dataManipulate) throws IOException {
        super(inputStream, dataManipulate);
        if (!this.isCharTrie()) {
            throw new IllegalArgumentException("Data given does not belong to a char trie.");
        }
    }
    
    public CharTrie(final int initialValue, final int leadUnitValue, final DataManipulate dataManipulate) {
        super(new char[2080], 512, dataManipulate);
        int dataLength;
        final int latin1Length = dataLength = 256;
        if (leadUnitValue != initialValue) {
            dataLength += 32;
        }
        this.m_data_ = new char[dataLength];
        this.m_dataLength_ = dataLength;
        this.m_initialValue_ = (char)initialValue;
        for (int i = 0; i < latin1Length; ++i) {
            this.m_data_[i] = (char)initialValue;
        }
        if (leadUnitValue != initialValue) {
            final char block = (char)(latin1Length >> 2);
            for (int i = 1728, limit = 1760; i < limit; ++i) {
                this.m_index_[i] = block;
            }
            for (int limit = latin1Length + 32, i = latin1Length; i < limit; ++i) {
                this.m_data_[i] = (char)leadUnitValue;
            }
        }
    }
    
    public final char getCodePointValue(final int ch) {
        if (0 <= ch && ch < 55296) {
            final int offset = (this.m_index_[ch >> 5] << 2) + (ch & 0x1F);
            return this.m_data_[offset];
        }
        final int offset = this.getCodePointOffset(ch);
        return (offset >= 0) ? this.m_data_[offset] : this.m_initialValue_;
    }
    
    public final char getLeadValue(final char ch) {
        return this.m_data_[this.getLeadOffset(ch)];
    }
    
    public final char getBMPValue(final char ch) {
        return this.m_data_[this.getBMPOffset(ch)];
    }
    
    public final char getSurrogateValue(final char lead, final char trail) {
        final int offset = this.getSurrogateOffset(lead, trail);
        if (offset > 0) {
            return this.m_data_[offset];
        }
        return this.m_initialValue_;
    }
    
    public final char getTrailValue(final int leadvalue, final char trail) {
        if (this.m_dataManipulate_ == null) {
            throw new NullPointerException("The field DataManipulate in this Trie is null");
        }
        final int offset = this.m_dataManipulate_.getFoldingOffset(leadvalue);
        if (offset > 0) {
            return this.m_data_[this.getRawOffset(offset, (char)(trail & '\u03ff'))];
        }
        return this.m_initialValue_;
    }
    
    public final char getLatin1LinearValue(final char ch) {
        return this.m_data_[32 + this.m_dataOffset_ + ch];
    }
    
    @Override
    public boolean equals(final Object other) {
        final boolean result = super.equals(other);
        if (result && other instanceof CharTrie) {
            final CharTrie othertrie = (CharTrie)other;
            return this.m_initialValue_ == othertrie.m_initialValue_;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42;
    }
    
    @Override
    protected final void unserialize(final InputStream inputStream) throws IOException {
        final DataInputStream input = new DataInputStream(inputStream);
        final int indexDataLength = this.m_dataOffset_ + this.m_dataLength_;
        this.m_index_ = new char[indexDataLength];
        for (int i = 0; i < indexDataLength; ++i) {
            this.m_index_[i] = input.readChar();
        }
        this.m_data_ = this.m_index_;
        this.m_initialValue_ = this.m_data_[this.m_dataOffset_];
    }
    
    @Override
    protected final int getSurrogateOffset(final char lead, final char trail) {
        if (this.m_dataManipulate_ == null) {
            throw new NullPointerException("The field DataManipulate in this Trie is null");
        }
        final int offset = this.m_dataManipulate_.getFoldingOffset(this.getLeadValue(lead));
        if (offset > 0) {
            return this.getRawOffset(offset, (char)(trail & '\u03ff'));
        }
        return -1;
    }
    
    @Override
    protected final int getValue(final int index) {
        return this.m_data_[index];
    }
    
    @Override
    protected final int getInitialValue() {
        return this.m_initialValue_;
    }
}
