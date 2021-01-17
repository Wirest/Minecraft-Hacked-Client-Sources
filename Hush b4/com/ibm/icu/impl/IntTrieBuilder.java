// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class IntTrieBuilder extends TrieBuilder
{
    protected int[] m_data_;
    protected int m_initialValue_;
    private int m_leadUnitValue_;
    
    public IntTrieBuilder(final IntTrieBuilder table) {
        super(table);
        this.m_data_ = new int[this.m_dataCapacity_];
        System.arraycopy(table.m_data_, 0, this.m_data_, 0, this.m_dataLength_);
        this.m_initialValue_ = table.m_initialValue_;
        this.m_leadUnitValue_ = table.m_leadUnitValue_;
    }
    
    public IntTrieBuilder(final int[] aliasdata, final int maxdatalength, final int initialvalue, final int leadunitvalue, final boolean latin1linear) {
        if (maxdatalength < 32 || (latin1linear && maxdatalength < 1024)) {
            throw new IllegalArgumentException("Argument maxdatalength is too small");
        }
        if (aliasdata != null) {
            this.m_data_ = aliasdata;
        }
        else {
            this.m_data_ = new int[maxdatalength];
        }
        int j = 32;
        if (latin1linear) {
            int i = 0;
            do {
                this.m_index_[i++] = j;
                j += 32;
            } while (i < 8);
        }
        this.m_dataLength_ = j;
        Arrays.fill(this.m_data_, 0, this.m_dataLength_, initialvalue);
        this.m_initialValue_ = initialvalue;
        this.m_leadUnitValue_ = leadunitvalue;
        this.m_dataCapacity_ = maxdatalength;
        this.m_isLatin1Linear_ = latin1linear;
        this.m_isCompacted_ = false;
    }
    
    public int getValue(final int ch) {
        if (this.m_isCompacted_ || ch > 1114111 || ch < 0) {
            return 0;
        }
        final int block = this.m_index_[ch >> 5];
        return this.m_data_[Math.abs(block) + (ch & 0x1F)];
    }
    
    public int getValue(final int ch, final boolean[] inBlockZero) {
        if (this.m_isCompacted_ || ch > 1114111 || ch < 0) {
            if (inBlockZero != null) {
                inBlockZero[0] = true;
            }
            return 0;
        }
        final int block = this.m_index_[ch >> 5];
        if (inBlockZero != null) {
            inBlockZero[0] = (block == 0);
        }
        return this.m_data_[Math.abs(block) + (ch & 0x1F)];
    }
    
    public boolean setValue(final int ch, final int value) {
        if (this.m_isCompacted_ || ch > 1114111 || ch < 0) {
            return false;
        }
        final int block = this.getDataBlock(ch);
        if (block < 0) {
            return false;
        }
        this.m_data_[block + (ch & 0x1F)] = value;
        return true;
    }
    
    public IntTrie serialize(final DataManipulate datamanipulate, final Trie.DataManipulate triedatamanipulate) {
        if (datamanipulate == null) {
            throw new IllegalArgumentException("Parameters can not be null");
        }
        if (!this.m_isCompacted_) {
            this.compact(false);
            this.fold(datamanipulate);
            this.compact(true);
            this.m_isCompacted_ = true;
        }
        if (this.m_dataLength_ >= 262144) {
            throw new ArrayIndexOutOfBoundsException("Data length too small");
        }
        final char[] index = new char[this.m_indexLength_];
        final int[] data = new int[this.m_dataLength_];
        for (int i = 0; i < this.m_indexLength_; ++i) {
            index[i] = (char)(this.m_index_[i] >>> 2);
        }
        System.arraycopy(this.m_data_, 0, data, 0, this.m_dataLength_);
        int options = 37;
        options |= 0x100;
        if (this.m_isLatin1Linear_) {
            options |= 0x200;
        }
        return new IntTrie(index, data, this.m_initialValue_, options, triedatamanipulate);
    }
    
    public int serialize(final OutputStream os, final boolean reduceTo16Bits, final DataManipulate datamanipulate) throws IOException {
        if (datamanipulate == null) {
            throw new IllegalArgumentException("Parameters can not be null");
        }
        if (!this.m_isCompacted_) {
            this.compact(false);
            this.fold(datamanipulate);
            this.compact(true);
            this.m_isCompacted_ = true;
        }
        int length;
        if (reduceTo16Bits) {
            length = this.m_dataLength_ + this.m_indexLength_;
        }
        else {
            length = this.m_dataLength_;
        }
        if (length >= 262144) {
            throw new ArrayIndexOutOfBoundsException("Data length too small");
        }
        length = 16 + 2 * this.m_indexLength_;
        if (reduceTo16Bits) {
            length += 2 * this.m_dataLength_;
        }
        else {
            length += 4 * this.m_dataLength_;
        }
        if (os == null) {
            return length;
        }
        final DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(1416784229);
        int options = 37;
        if (!reduceTo16Bits) {
            options |= 0x100;
        }
        if (this.m_isLatin1Linear_) {
            options |= 0x200;
        }
        dos.writeInt(options);
        dos.writeInt(this.m_indexLength_);
        dos.writeInt(this.m_dataLength_);
        if (reduceTo16Bits) {
            for (int i = 0; i < this.m_indexLength_; ++i) {
                final int v = this.m_index_[i] + this.m_indexLength_ >>> 2;
                dos.writeChar(v);
            }
            for (int i = 0; i < this.m_dataLength_; ++i) {
                final int v = this.m_data_[i] & 0xFFFF;
                dos.writeChar(v);
            }
        }
        else {
            for (int i = 0; i < this.m_indexLength_; ++i) {
                final int v = this.m_index_[i] >>> 2;
                dos.writeChar(v);
            }
            for (int i = 0; i < this.m_dataLength_; ++i) {
                dos.writeInt(this.m_data_[i]);
            }
        }
        return length;
    }
    
    public boolean setRange(int start, int limit, final int value, final boolean overwrite) {
        if (this.m_isCompacted_ || start < 0 || start > 1114111 || limit < 0 || limit > 1114112 || start > limit) {
            return false;
        }
        if (start == limit) {
            return true;
        }
        if ((start & 0x1F) != 0x0) {
            final int block = this.getDataBlock(start);
            if (block < 0) {
                return false;
            }
            final int nextStart = start + 32 & 0xFFFFFFE0;
            if (nextStart > limit) {
                this.fillBlock(block, start & 0x1F, limit & 0x1F, value, overwrite);
                return true;
            }
            this.fillBlock(block, start & 0x1F, 32, value, overwrite);
            start = nextStart;
        }
        final int rest = limit & 0x1F;
        limit &= 0xFFFFFFE0;
        int repeatBlock = 0;
        if (value != this.m_initialValue_) {
            repeatBlock = -1;
        }
        while (start < limit) {
            final int block2 = this.m_index_[start >> 5];
            if (block2 > 0) {
                this.fillBlock(block2, 0, 32, value, overwrite);
            }
            else if (this.m_data_[-block2] != value && (block2 == 0 || overwrite)) {
                if (repeatBlock >= 0) {
                    this.m_index_[start >> 5] = -repeatBlock;
                }
                else {
                    repeatBlock = this.getDataBlock(start);
                    if (repeatBlock < 0) {
                        return false;
                    }
                    this.m_index_[start >> 5] = -repeatBlock;
                    this.fillBlock(repeatBlock, 0, 32, value, true);
                }
            }
            start += 32;
        }
        if (rest > 0) {
            final int block2 = this.getDataBlock(start);
            if (block2 < 0) {
                return false;
            }
            this.fillBlock(block2, 0, rest, value, overwrite);
        }
        return true;
    }
    
    private int allocDataBlock() {
        final int newBlock = this.m_dataLength_;
        final int newTop = newBlock + 32;
        if (newTop > this.m_dataCapacity_) {
            return -1;
        }
        this.m_dataLength_ = newTop;
        return newBlock;
    }
    
    private int getDataBlock(int ch) {
        ch >>= 5;
        final int indexValue = this.m_index_[ch];
        if (indexValue > 0) {
            return indexValue;
        }
        final int newBlock = this.allocDataBlock();
        if (newBlock < 0) {
            return -1;
        }
        this.m_index_[ch] = newBlock;
        System.arraycopy(this.m_data_, Math.abs(indexValue), this.m_data_, newBlock, 128);
        return newBlock;
    }
    
    private void compact(final boolean overlap) {
        if (this.m_isCompacted_) {
            return;
        }
        this.findUnusedBlocks();
        int overlapStart = 32;
        if (this.m_isLatin1Linear_) {
            overlapStart += 256;
        }
        int start;
        int newStart = start = 32;
        while (start < this.m_dataLength_) {
            if (this.m_map_[start >>> 5] < 0) {
                start += 32;
            }
            else {
                if (start >= overlapStart) {
                    final int i = findSameDataBlock(this.m_data_, newStart, start, overlap ? 4 : 32);
                    if (i >= 0) {
                        this.m_map_[start >>> 5] = i;
                        start += 32;
                        continue;
                    }
                }
                int i;
                if (overlap && start >= overlapStart) {
                    for (int i = 28; i > 0 && !TrieBuilder.equal_int(this.m_data_, newStart - i, start, i); i -= 4) {}
                }
                else {
                    i = 0;
                }
                if (i > 0) {
                    this.m_map_[start >>> 5] = newStart - i;
                    start += i;
                    for (i = 32 - i; i > 0; --i) {
                        this.m_data_[newStart++] = this.m_data_[start++];
                    }
                }
                else if (newStart < start) {
                    this.m_map_[start >>> 5] = newStart;
                    for (int i = 32; i > 0; --i) {
                        this.m_data_[newStart++] = this.m_data_[start++];
                    }
                }
                else {
                    this.m_map_[start >>> 5] = start;
                    newStart += 32;
                    start = newStart;
                }
            }
        }
        for (int i = 0; i < this.m_indexLength_; ++i) {
            this.m_index_[i] = this.m_map_[Math.abs(this.m_index_[i]) >>> 5];
        }
        this.m_dataLength_ = newStart;
    }
    
    private static final int findSameDataBlock(final int[] data, int dataLength, final int otherBlock, final int step) {
        dataLength -= 32;
        for (int block = 0; block <= dataLength; block += step) {
            if (TrieBuilder.equal_int(data, block, otherBlock, 32)) {
                return block;
            }
        }
        return -1;
    }
    
    private final void fold(final DataManipulate manipulate) {
        final int[] leadIndexes = new int[32];
        final int[] index = this.m_index_;
        System.arraycopy(index, 1728, leadIndexes, 0, 32);
        int block = 0;
        if (this.m_leadUnitValue_ != this.m_initialValue_) {
            block = this.allocDataBlock();
            if (block < 0) {
                throw new IllegalStateException("Internal error: Out of memory space");
            }
            this.fillBlock(block, 0, 32, this.m_leadUnitValue_, true);
            block = -block;
        }
        for (int c = 1728; c < 1760; ++c) {
            this.m_index_[c] = block;
        }
        int indexLength = 2048;
        int c2 = 65536;
        while (c2 < 1114112) {
            if (index[c2 >> 5] != 0) {
                c2 &= 0xFFFFFC00;
                block = TrieBuilder.findSameIndexBlock(index, indexLength, c2 >> 5);
                final int value = manipulate.getFoldedValue(c2, block + 32);
                if (value != this.getValue(UTF16.getLeadSurrogate(c2))) {
                    if (!this.setValue(UTF16.getLeadSurrogate(c2), value)) {
                        throw new ArrayIndexOutOfBoundsException("Data table overflow");
                    }
                    if (block == indexLength) {
                        System.arraycopy(index, c2 >> 5, index, indexLength, 32);
                        indexLength += 32;
                    }
                }
                c2 += 1024;
            }
            else {
                c2 += 32;
            }
        }
        if (indexLength >= 34816) {
            throw new ArrayIndexOutOfBoundsException("Index table overflow");
        }
        System.arraycopy(index, 2048, index, 2080, indexLength - 2048);
        System.arraycopy(leadIndexes, 0, index, 2048, 32);
        indexLength += 32;
        this.m_indexLength_ = indexLength;
    }
    
    private void fillBlock(int block, final int start, int limit, final int value, final boolean overwrite) {
        limit += block;
        block += start;
        if (overwrite) {
            while (block < limit) {
                this.m_data_[block++] = value;
            }
        }
        else {
            while (block < limit) {
                if (this.m_data_[block] == this.m_initialValue_) {
                    this.m_data_[block] = value;
                }
                ++block;
            }
        }
    }
}
