// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.util.Arrays;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.math.LongMath;
import java.math.RoundingMode;
import com.google.common.primitives.Longs;

enum BloomFilterStrategies implements BloomFilter.Strategy
{
    MURMUR128_MITZ_32 {
        @Override
        public <T> boolean put(final T object, final Funnel<? super T> funnel, final int numHashFunctions, final BitArray bits) {
            final long bitSize = bits.bitSize();
            final long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
            final int hash65 = (int)hash64;
            final int hash66 = (int)(hash64 >>> 32);
            boolean bitsChanged = false;
            for (int i = 1; i <= numHashFunctions; ++i) {
                int combinedHash = hash65 + i * hash66;
                if (combinedHash < 0) {
                    combinedHash ^= -1;
                }
                bitsChanged |= bits.set(combinedHash % bitSize);
            }
            return bitsChanged;
        }
        
        @Override
        public <T> boolean mightContain(final T object, final Funnel<? super T> funnel, final int numHashFunctions, final BitArray bits) {
            final long bitSize = bits.bitSize();
            final long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
            final int hash65 = (int)hash64;
            final int hash66 = (int)(hash64 >>> 32);
            for (int i = 1; i <= numHashFunctions; ++i) {
                int combinedHash = hash65 + i * hash66;
                if (combinedHash < 0) {
                    combinedHash ^= -1;
                }
                if (!bits.get(combinedHash % bitSize)) {
                    return false;
                }
            }
            return true;
        }
    }, 
    MURMUR128_MITZ_64 {
        @Override
        public <T> boolean put(final T object, final Funnel<? super T> funnel, final int numHashFunctions, final BitArray bits) {
            final long bitSize = bits.bitSize();
            final byte[] bytes = Hashing.murmur3_128().hashObject(object, funnel).getBytesInternal();
            final long hash1 = this.lowerEight(bytes);
            final long hash2 = this.upperEight(bytes);
            boolean bitsChanged = false;
            long combinedHash = hash1;
            for (int i = 0; i < numHashFunctions; ++i) {
                bitsChanged |= bits.set((combinedHash & Long.MAX_VALUE) % bitSize);
                combinedHash += hash2;
            }
            return bitsChanged;
        }
        
        @Override
        public <T> boolean mightContain(final T object, final Funnel<? super T> funnel, final int numHashFunctions, final BitArray bits) {
            final long bitSize = bits.bitSize();
            final byte[] bytes = Hashing.murmur3_128().hashObject(object, funnel).getBytesInternal();
            final long hash1 = this.lowerEight(bytes);
            final long hash2 = this.upperEight(bytes);
            long combinedHash = hash1;
            for (int i = 0; i < numHashFunctions; ++i) {
                if (!bits.get((combinedHash & Long.MAX_VALUE) % bitSize)) {
                    return false;
                }
                combinedHash += hash2;
            }
            return true;
        }
        
        private long lowerEight(final byte[] bytes) {
            return Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
        }
        
        private long upperEight(final byte[] bytes) {
            return Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
        }
    };
    
    static final class BitArray
    {
        final long[] data;
        long bitCount;
        
        BitArray(final long bits) {
            this(new long[Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING))]);
        }
        
        BitArray(final long[] data) {
            Preconditions.checkArgument(data.length > 0, (Object)"data length is zero!");
            this.data = data;
            long bitCount = 0L;
            for (final long value : data) {
                bitCount += Long.bitCount(value);
            }
            this.bitCount = bitCount;
        }
        
        boolean set(final long index) {
            if (!this.get(index)) {
                final long[] data = this.data;
                final int n = (int)(index >>> 6);
                data[n] |= 1L << (int)index;
                ++this.bitCount;
                return true;
            }
            return false;
        }
        
        boolean get(final long index) {
            return (this.data[(int)(index >>> 6)] & 1L << (int)index) != 0x0L;
        }
        
        long bitSize() {
            return this.data.length * 64L;
        }
        
        long bitCount() {
            return this.bitCount;
        }
        
        BitArray copy() {
            return new BitArray(this.data.clone());
        }
        
        void putAll(final BitArray array) {
            Preconditions.checkArgument(this.data.length == array.data.length, "BitArrays must be of equal length (%s != %s)", this.data.length, array.data.length);
            this.bitCount = 0L;
            for (int i = 0; i < this.data.length; ++i) {
                final long[] data = this.data;
                final int n = i;
                data[n] |= array.data[i];
                this.bitCount += Long.bitCount(this.data[i]);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o instanceof BitArray) {
                final BitArray bitArray = (BitArray)o;
                return Arrays.equals(this.data, bitArray.data);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Arrays.hashCode(this.data);
        }
    }
}
