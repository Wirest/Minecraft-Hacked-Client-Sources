// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;

class BinaryTree
{
    private static final int UNDEFINED = -1;
    private static final int NODE = -2;
    private final int[] tree;
    
    public BinaryTree(final int depth) {
        Arrays.fill(this.tree = new int[(1 << depth + 1) - 1], -1);
    }
    
    public void addLeaf(final int node, final int path, final int depth, final int value) {
        if (depth == 0) {
            if (this.tree[node] != -1) {
                throw new IllegalArgumentException("Tree value at index " + node + " has already been assigned (" + this.tree[node] + ")");
            }
            this.tree[node] = value;
        }
        else {
            this.tree[node] = -2;
            final int nextChild = 2 * node + 1 + (path & 0x1);
            this.addLeaf(nextChild, path >>> 1, depth - 1, value);
        }
    }
    
    public int read(final BitStream stream) throws IOException {
        int currentIndex = 0;
        while (true) {
            final int bit = stream.nextBit();
            if (bit == -1) {
                return -1;
            }
            final int childIndex = 2 * currentIndex + 1 + bit;
            final int value = this.tree[childIndex];
            if (value == -2) {
                currentIndex = childIndex;
            }
            else {
                if (value != -1) {
                    return value;
                }
                throw new IOException("The child " + bit + " of node at index " + currentIndex + " is not defined");
            }
        }
    }
    
    static BinaryTree decode(final InputStream in, final int totalNumberOfValues) throws IOException {
        final int size = in.read() + 1;
        if (size == 0) {
            throw new IOException("Cannot read the size of the encoded tree, unexpected end of stream");
        }
        final byte[] encodedTree = new byte[size];
        new DataInputStream(in).readFully(encodedTree);
        int maxLength = 0;
        final int[] originalBitLengths = new int[totalNumberOfValues];
        int pos = 0;
        for (final byte b : encodedTree) {
            final int numberOfValues = ((b & 0xF0) >> 4) + 1;
            final int bitLength = (b & 0xF) + 1;
            for (int j = 0; j < numberOfValues; ++j) {
                originalBitLengths[pos++] = bitLength;
            }
            maxLength = Math.max(maxLength, bitLength);
        }
        final int[] permutation = new int[originalBitLengths.length];
        for (int k = 0; k < permutation.length; ++k) {
            permutation[k] = k;
        }
        int c = 0;
        final int[] sortedBitLengths = new int[originalBitLengths.length];
        for (int i = 0; i < originalBitLengths.length; ++i) {
            for (int l = 0; l < originalBitLengths.length; ++l) {
                if (originalBitLengths[l] == i) {
                    sortedBitLengths[c] = i;
                    permutation[c] = l;
                    ++c;
                }
            }
        }
        int code = 0;
        int codeIncrement = 0;
        int lastBitLength = 0;
        final int[] codes = new int[totalNumberOfValues];
        for (int m = totalNumberOfValues - 1; m >= 0; --m) {
            code += codeIncrement;
            if (sortedBitLengths[m] != lastBitLength) {
                lastBitLength = sortedBitLengths[m];
                codeIncrement = 1 << 16 - lastBitLength;
            }
            codes[permutation[m]] = code;
        }
        final BinaryTree tree = new BinaryTree(maxLength);
        for (int k2 = 0; k2 < codes.length; ++k2) {
            final int bitLength2 = originalBitLengths[k2];
            if (bitLength2 > 0) {
                tree.addLeaf(0, Integer.reverse(codes[k2] << 16), bitLength2, k2);
            }
        }
        return tree;
    }
}
