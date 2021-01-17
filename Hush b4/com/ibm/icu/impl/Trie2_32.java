// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Trie2_32 extends Trie2
{
    Trie2_32() {
    }
    
    public static Trie2_32 createFromSerialized(final InputStream is) throws IOException {
        return (Trie2_32)Trie2.createFromSerialized(is);
    }
    
    @Override
    public final int get(final int codePoint) {
        if (codePoint >= 0) {
            if (codePoint < 55296 || (codePoint > 56319 && codePoint <= 65535)) {
                int ix = this.index[codePoint >> 5];
                ix = (ix << 2) + (codePoint & 0x1F);
                final int value = this.data32[ix];
                return value;
            }
            if (codePoint <= 65535) {
                int ix = this.index[2048 + (codePoint - 55296 >> 5)];
                ix = (ix << 2) + (codePoint & 0x1F);
                final int value = this.data32[ix];
                return value;
            }
            if (codePoint < this.highStart) {
                int ix = 2080 + (codePoint >> 11);
                ix = this.index[ix];
                ix += (codePoint >> 5 & 0x3F);
                ix = this.index[ix];
                ix = (ix << 2) + (codePoint & 0x1F);
                final int value = this.data32[ix];
                return value;
            }
            if (codePoint <= 1114111) {
                final int value = this.data32[this.highValueIndex];
                return value;
            }
        }
        return this.errorValue;
    }
    
    @Override
    public int getFromU16SingleLead(final char codeUnit) {
        int ix = this.index[codeUnit >> 5];
        ix = (ix << 2) + (codeUnit & '\u001f');
        final int value = this.data32[ix];
        return value;
    }
    
    public int serialize(final OutputStream os) throws IOException {
        final DataOutputStream dos = new DataOutputStream(os);
        int bytesWritten = 0;
        bytesWritten += this.serializeHeader(dos);
        for (int i = 0; i < this.dataLength; ++i) {
            dos.writeInt(this.data32[i]);
        }
        bytesWritten += this.dataLength * 4;
        return bytesWritten;
    }
    
    public int getSerializedLength() {
        return 16 + this.header.indexLength * 2 + this.dataLength * 4;
    }
    
    @Override
    int rangeEnd(final int startingCP, final int limit, final int value) {
        int cp = startingCP;
        int block = 0;
        int index2Block = 0;
    Label_0288:
        while (true) {
            while (cp < limit) {
                if (cp < 55296 || (cp > 56319 && cp <= 65535)) {
                    index2Block = 0;
                    block = this.index[cp >> 5] << 2;
                }
                else if (cp < 65535) {
                    index2Block = 2048;
                    block = this.index[index2Block + (cp - 55296 >> 5)] << 2;
                }
                else if (cp < this.highStart) {
                    final int ix = 2080 + (cp >> 11);
                    index2Block = this.index[ix];
                    block = this.index[index2Block + (cp >> 5 & 0x3F)] << 2;
                }
                else {
                    if (value == this.data32[this.highValueIndex]) {
                        cp = limit;
                        break;
                    }
                    break;
                }
                if (index2Block == this.index2NullOffset) {
                    if (value == this.initialValue) {
                        cp += 2048;
                        continue;
                    }
                }
                else {
                    if (block != this.dataNullOffset) {
                        final int startIx = block + (cp & 0x1F);
                        final int limitIx = block + 32;
                        for (int ix2 = startIx; ix2 < limitIx; ++ix2) {
                            if (this.data32[ix2] != value) {
                                cp += ix2 - startIx;
                                break Label_0288;
                            }
                        }
                        cp += limitIx - startIx;
                        continue;
                    }
                    if (value == this.initialValue) {
                        cp += 32;
                        continue;
                    }
                }
                if (cp > limit) {
                    cp = limit;
                }
                return cp - 1;
            }
            continue Label_0288;
        }
    }
}
