// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.nio.ByteOrder;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;
import java.io.Serializable;

final class Murmur3_128HashFunction extends AbstractStreamingHashFunction implements Serializable
{
    private final int seed;
    private static final long serialVersionUID = 0L;
    
    Murmur3_128HashFunction(final int seed) {
        this.seed = seed;
    }
    
    @Override
    public int bits() {
        return 128;
    }
    
    @Override
    public Hasher newHasher() {
        return new Murmur3_128Hasher(this.seed);
    }
    
    @Override
    public String toString() {
        return "Hashing.murmur3_128(" + this.seed + ")";
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object instanceof Murmur3_128HashFunction) {
            final Murmur3_128HashFunction other = (Murmur3_128HashFunction)object;
            return this.seed == other.seed;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.getClass().hashCode() ^ this.seed;
    }
    
    private static final class Murmur3_128Hasher extends AbstractStreamingHasher
    {
        private static final int CHUNK_SIZE = 16;
        private static final long C1 = -8663945395140668459L;
        private static final long C2 = 5545529020109919103L;
        private long h1;
        private long h2;
        private int length;
        
        Murmur3_128Hasher(final int seed) {
            super(16);
            this.h1 = seed;
            this.h2 = seed;
            this.length = 0;
        }
        
        @Override
        protected void process(final ByteBuffer bb) {
            final long k1 = bb.getLong();
            final long k2 = bb.getLong();
            this.bmix64(k1, k2);
            this.length += 16;
        }
        
        private void bmix64(final long k1, final long k2) {
            this.h1 ^= mixK1(k1);
            this.h1 = Long.rotateLeft(this.h1, 27);
            this.h1 += this.h2;
            this.h1 = this.h1 * 5L + 1390208809L;
            this.h2 ^= mixK2(k2);
            this.h2 = Long.rotateLeft(this.h2, 31);
            this.h2 += this.h1;
            this.h2 = this.h2 * 5L + 944331445L;
        }
        
        @Override
        protected void processRemaining(final ByteBuffer bb) {
            long k1 = 0L;
            long k2 = 0L;
            this.length += bb.remaining();
            switch (bb.remaining()) {
                case 15: {
                    k2 ^= (long)UnsignedBytes.toInt(bb.get(14)) << 48;
                }
                case 14: {
                    k2 ^= (long)UnsignedBytes.toInt(bb.get(13)) << 40;
                }
                case 13: {
                    k2 ^= (long)UnsignedBytes.toInt(bb.get(12)) << 32;
                }
                case 12: {
                    k2 ^= (long)UnsignedBytes.toInt(bb.get(11)) << 24;
                }
                case 11: {
                    k2 ^= (long)UnsignedBytes.toInt(bb.get(10)) << 16;
                }
                case 10: {
                    k2 ^= (long)UnsignedBytes.toInt(bb.get(9)) << 8;
                }
                case 9: {
                    k2 ^= UnsignedBytes.toInt(bb.get(8));
                }
                case 8: {
                    k1 ^= bb.getLong();
                    break;
                }
                case 7: {
                    k1 ^= (long)UnsignedBytes.toInt(bb.get(6)) << 48;
                }
                case 6: {
                    k1 ^= (long)UnsignedBytes.toInt(bb.get(5)) << 40;
                }
                case 5: {
                    k1 ^= (long)UnsignedBytes.toInt(bb.get(4)) << 32;
                }
                case 4: {
                    k1 ^= (long)UnsignedBytes.toInt(bb.get(3)) << 24;
                }
                case 3: {
                    k1 ^= (long)UnsignedBytes.toInt(bb.get(2)) << 16;
                }
                case 2: {
                    k1 ^= (long)UnsignedBytes.toInt(bb.get(1)) << 8;
                }
                case 1: {
                    k1 ^= UnsignedBytes.toInt(bb.get(0));
                    break;
                }
                default: {
                    throw new AssertionError((Object)"Should never get here.");
                }
            }
            this.h1 ^= mixK1(k1);
            this.h2 ^= mixK2(k2);
        }
        
        public HashCode makeHash() {
            this.h1 ^= this.length;
            this.h2 ^= this.length;
            this.h1 += this.h2;
            this.h2 += this.h1;
            this.h1 = fmix64(this.h1);
            this.h2 = fmix64(this.h2);
            this.h1 += this.h2;
            this.h2 += this.h1;
            return HashCode.fromBytesNoCopy(ByteBuffer.wrap(new byte[16]).order(ByteOrder.LITTLE_ENDIAN).putLong(this.h1).putLong(this.h2).array());
        }
        
        private static long fmix64(long k) {
            k ^= k >>> 33;
            k *= -49064778989728563L;
            k ^= k >>> 33;
            k *= -4265267296055464877L;
            k ^= k >>> 33;
            return k;
        }
        
        private static long mixK1(long k1) {
            k1 *= -8663945395140668459L;
            k1 = Long.rotateLeft(k1, 31);
            k1 *= 5545529020109919103L;
            return k1;
        }
        
        private static long mixK2(long k2) {
            k2 *= 5545529020109919103L;
            k2 = Long.rotateLeft(k2, 33);
            k2 *= -8663945395140668459L;
            return k2;
        }
    }
}
