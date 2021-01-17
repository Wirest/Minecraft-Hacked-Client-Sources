// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.CharacterIterator;
import java.io.IOException;
import java.io.DataInputStream;
import com.ibm.icu.impl.ICUBinary;
import java.io.InputStream;

class BreakCTDictionary
{
    private CompactTrieHeader fData;
    private CompactTrieNodes[] nodes;
    private static final byte[] DATA_FORMAT_ID;
    
    private CompactTrieNodes getCompactTrieNode(final int node) {
        return this.nodes[node];
    }
    
    public BreakCTDictionary(final InputStream is) throws IOException {
        ICUBinary.readHeader(is, BreakCTDictionary.DATA_FORMAT_ID, null);
        final DataInputStream in = new DataInputStream(is);
        this.fData = new CompactTrieHeader();
        this.fData.size = in.readInt();
        this.fData.magic = in.readInt();
        this.fData.nodeCount = in.readShort();
        this.fData.root = in.readShort();
        this.loadBreakCTDictionary(in);
    }
    
    private void loadBreakCTDictionary(final DataInputStream in) throws IOException {
        for (int i = 0; i < this.fData.nodeCount; ++i) {
            in.readInt();
        }
        (this.nodes = new CompactTrieNodes[this.fData.nodeCount])[0] = new CompactTrieNodes();
        for (int j = 1; j < this.fData.nodeCount; ++j) {
            this.nodes[j] = new CompactTrieNodes();
            this.nodes[j].flagscount = in.readShort();
            final int count = this.nodes[j].flagscount & 0xFFF;
            if (count != 0) {
                final boolean isVerticalNode = (this.nodes[j].flagscount & 0x1000) != 0x0;
                if (isVerticalNode) {
                    this.nodes[j].vnode = new CompactTrieVerticalNode();
                    this.nodes[j].vnode.equal = in.readShort();
                    this.nodes[j].vnode.chars = new char[count];
                    for (int l = 0; l < count; ++l) {
                        this.nodes[j].vnode.chars[l] = in.readChar();
                    }
                }
                else {
                    this.nodes[j].hnode = new CompactTrieHorizontalNode[count];
                    for (int n = 0; n < count; ++n) {
                        this.nodes[j].hnode[n] = new CompactTrieHorizontalNode(in.readChar(), in.readShort());
                    }
                }
            }
        }
    }
    
    public int matches(final CharacterIterator text, final int maxLength, final int[] lengths, final int[] count, int limit) {
        CompactTrieNodes node = this.getCompactTrieNode(this.fData.root);
        int mycount = 0;
        char uc = text.current();
        int i = 0;
        boolean exitFlag = false;
        while (node != null) {
            if (limit > 0 && (node.flagscount & 0x2000) != 0x0) {
                lengths[mycount++] = i;
                --limit;
            }
            if (i >= maxLength) {
                break;
            }
            final int nodeCount = node.flagscount & 0xFFF;
            if (nodeCount == 0) {
                break;
            }
            if ((node.flagscount & 0x1000) != 0x0) {
                final CompactTrieVerticalNode vnode = node.vnode;
                for (int j = 0; j < nodeCount && i < maxLength; ++i, ++j) {
                    if (uc != vnode.chars[j]) {
                        exitFlag = true;
                        break;
                    }
                    text.next();
                    uc = text.current();
                }
                if (exitFlag) {
                    break;
                }
                node = this.getCompactTrieNode(vnode.equal);
            }
            else {
                final CompactTrieHorizontalNode[] hnode = node.hnode;
                int low = 0;
                int high = nodeCount - 1;
                node = null;
                while (high >= low) {
                    final int middle = high + low >>> 1;
                    if (uc == hnode[middle].ch) {
                        node = this.getCompactTrieNode(hnode[middle].equal);
                        text.next();
                        uc = text.current();
                        ++i;
                        break;
                    }
                    if (uc < hnode[middle].ch) {
                        high = middle - 1;
                    }
                    else {
                        low = middle + 1;
                    }
                }
            }
        }
        count[0] = mycount;
        return i;
    }
    
    static {
        DATA_FORMAT_ID = new byte[] { 84, 114, 68, 99 };
    }
    
    static class CompactTrieHeader
    {
        int size;
        int magic;
        int nodeCount;
        int root;
        int[] offset;
        
        CompactTrieHeader() {
            this.size = 0;
            this.magic = 0;
            this.nodeCount = 0;
            this.root = 0;
            this.offset = null;
        }
    }
    
    static final class CompactTrieNodeFlags
    {
        static final int kVerticalNode = 4096;
        static final int kParentEndsWord = 8192;
        static final int kReservedFlag1 = 16384;
        static final int kReservedFlag2 = 32768;
        static final int kCountMask = 4095;
        static final int kFlagMask = 61440;
    }
    
    static class CompactTrieHorizontalNode
    {
        char ch;
        int equal;
        
        CompactTrieHorizontalNode(final char newCh, final int newEqual) {
            this.ch = newCh;
            this.equal = newEqual;
        }
    }
    
    static class CompactTrieVerticalNode
    {
        int equal;
        char[] chars;
        
        CompactTrieVerticalNode() {
            this.equal = 0;
            this.chars = null;
        }
    }
    
    static class CompactTrieNodes
    {
        short flagscount;
        CompactTrieHorizontalNode[] hnode;
        CompactTrieVerticalNode vnode;
        
        CompactTrieNodes() {
            this.flagscount = 0;
            this.hnode = null;
            this.vnode = null;
        }
    }
}
