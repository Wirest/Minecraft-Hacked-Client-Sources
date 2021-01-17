// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.sevenz;

import java.util.LinkedList;

class Folder
{
    Coder[] coders;
    long totalInputStreams;
    long totalOutputStreams;
    BindPair[] bindPairs;
    long[] packedStreams;
    long[] unpackSizes;
    boolean hasCrc;
    long crc;
    int numUnpackSubStreams;
    
    Iterable<Coder> getOrderedCoders() {
        final LinkedList<Coder> l = new LinkedList<Coder>();
        int pair;
        for (int current = (int)this.packedStreams[0]; current != -1; current = ((pair != -1) ? ((int)this.bindPairs[pair].inIndex) : -1)) {
            l.addLast(this.coders[current]);
            pair = this.findBindPairForOutStream(current);
        }
        return l;
    }
    
    int findBindPairForInStream(final int index) {
        for (int i = 0; i < this.bindPairs.length; ++i) {
            if (this.bindPairs[i].inIndex == index) {
                return i;
            }
        }
        return -1;
    }
    
    int findBindPairForOutStream(final int index) {
        for (int i = 0; i < this.bindPairs.length; ++i) {
            if (this.bindPairs[i].outIndex == index) {
                return i;
            }
        }
        return -1;
    }
    
    long getUnpackSize() {
        if (this.totalOutputStreams == 0L) {
            return 0L;
        }
        for (int i = (int)this.totalOutputStreams - 1; i >= 0; --i) {
            if (this.findBindPairForOutStream(i) < 0) {
                return this.unpackSizes[i];
            }
        }
        return 0L;
    }
}
