// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.io.DataInputStream;
import java.util.Arrays;
import com.ibm.icu.text.UTF16;
import java.io.IOException;
import java.io.InputStream;

public class IntTrie extends Trie
{
    private int m_initialValue_;
    private int[] m_data_;
    
    public IntTrie(final InputStream inputStream, final DataManipulate dataManipulate) throws IOException {
        super(inputStream, dataManipulate);
        if (!this.isIntTrie()) {
            throw new IllegalArgumentException("Data given does not belong to a int trie.");
        }
    }
    
    public IntTrie(final int initialValue, final int leadUnitValue, final DataManipulate dataManipulate) {
        super(new char[2080], 512, dataManipulate);
        int dataLength;
        final int latin1Length = dataLength = 256;
        if (leadUnitValue != initialValue) {
            dataLength += 32;
        }
        this.m_data_ = new int[dataLength];
        this.m_dataLength_ = dataLength;
        this.m_initialValue_ = initialValue;
        for (int i = 0; i < latin1Length; ++i) {
            this.m_data_[i] = initialValue;
        }
        if (leadUnitValue != initialValue) {
            final char block = (char)(latin1Length >> 2);
            for (int i = 1728, limit = 1760; i < limit; ++i) {
                this.m_index_[i] = block;
            }
            for (int limit = latin1Length + 32, i = latin1Length; i < limit; ++i) {
                this.m_data_[i] = leadUnitValue;
            }
        }
    }
    
    public final int getCodePointValue(final int ch) {
        if (0 <= ch && ch < 55296) {
            final int offset = (this.m_index_[ch >> 5] << 2) + (ch & 0x1F);
            return this.m_data_[offset];
        }
        final int offset = this.getCodePointOffset(ch);
        return (offset >= 0) ? this.m_data_[offset] : this.m_initialValue_;
    }
    
    public final int getLeadValue(final char ch) {
        return this.m_data_[this.getLeadOffset(ch)];
    }
    
    public final int getBMPValue(final char ch) {
        return this.m_data_[this.getBMPOffset(ch)];
    }
    
    public final int getSurrogateValue(final char lead, final char trail) {
        if (!UTF16.isLeadSurrogate(lead) || !UTF16.isTrailSurrogate(trail)) {
            throw new IllegalArgumentException("Argument characters do not form a supplementary character");
        }
        final int offset = this.getSurrogateOffset(lead, trail);
        if (offset > 0) {
            return this.m_data_[offset];
        }
        return this.m_initialValue_;
    }
    
    public final int getTrailValue(final int leadvalue, final char trail) {
        if (this.m_dataManipulate_ == null) {
            throw new NullPointerException("The field DataManipulate in this Trie is null");
        }
        final int offset = this.m_dataManipulate_.getFoldingOffset(leadvalue);
        if (offset > 0) {
            return this.m_data_[this.getRawOffset(offset, (char)(trail & '\u03ff'))];
        }
        return this.m_initialValue_;
    }
    
    public final int getLatin1LinearValue(final char ch) {
        return this.m_data_[' ' + ch];
    }
    
    @Override
    public boolean equals(final Object other) {
        final boolean result = super.equals(other);
        if (result && other instanceof IntTrie) {
            final IntTrie othertrie = (IntTrie)other;
            return this.m_initialValue_ == othertrie.m_initialValue_ && Arrays.equals(this.m_data_, othertrie.m_data_);
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
        super.unserialize(inputStream);
        this.m_data_ = new int[this.m_dataLength_];
        final DataInputStream input = new DataInputStream(inputStream);
        for (int i = 0; i < this.m_dataLength_; ++i) {
            this.m_data_[i] = input.readInt();
        }
        this.m_initialValue_ = this.m_data_[0];
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
    
    IntTrie(final char[] index, final int[] data, final int initialvalue, final int options, final DataManipulate datamanipulate) {
        super(index, options, datamanipulate);
        this.m_data_ = data;
        this.m_dataLength_ = this.m_data_.length;
        this.m_initialValue_ = initialvalue;
    }
}
