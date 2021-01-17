// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;
import java.io.Serializable;

final class Murmur3_32HashFunction extends AbstractStreamingHashFunction implements Serializable
{
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    private final int seed;
    private static final long serialVersionUID = 0L;
    
    Murmur3_32HashFunction(final int seed) {
        this.seed = seed;
    }
    
    @Override
    public int bits() {
        return 32;
    }
    
    @Override
    public Hasher newHasher() {
        return new Murmur3_32Hasher(this.seed);
    }
    
    @Override
    public String toString() {
        return "Hashing.murmur3_32(" + this.seed + ")";
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object instanceof Murmur3_32HashFunction) {
            final Murmur3_32HashFunction other = (Murmur3_32HashFunction)object;
            return this.seed == other.seed;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.getClass().hashCode() ^ this.seed;
    }
    
    @Override
    public HashCode hashInt(final int input) {
        final int k1 = mixK1(input);
        final int h1 = mixH1(this.seed, k1);
        return fmix(h1, 4);
    }
    
    @Override
    public HashCode hashLong(final long input) {
        final int low = (int)input;
        final int high = (int)(input >>> 32);
        int k1 = mixK1(low);
        int h1 = mixH1(this.seed, k1);
        k1 = mixK1(high);
        h1 = mixH1(h1, k1);
        return fmix(h1, 8);
    }
    
    @Override
    public HashCode hashUnencodedChars(final CharSequence input) {
        int h1 = this.seed;
        for (int i = 1; i < input.length(); i += 2) {
            int k1 = input.charAt(i - 1) | input.charAt(i) << 16;
            k1 = mixK1(k1);
            h1 = mixH1(h1, k1);
        }
        if ((input.length() & 0x1) == 0x1) {
            int k2 = input.charAt(input.length() - 1);
            k2 = mixK1(k2);
            h1 ^= k2;
        }
        return fmix(h1, 2 * input.length());
    }
    
    private static int mixK1(int k1) {
        k1 *= -862048943;
        k1 = Integer.rotateLeft(k1, 15);
        k1 *= 461845907;
        return k1;
    }
    
    private static int mixH1(int h1, final int k1) {
        h1 ^= k1;
        h1 = Integer.rotateLeft(h1, 13);
        h1 = h1 * 5 - 430675100;
        return h1;
    }
    
    private static HashCode fmix(int h1, final int length) {
        h1 ^= length;
        h1 ^= h1 >>> 16;
        h1 *= -2048144789;
        h1 ^= h1 >>> 13;
        h1 *= -1028477387;
        h1 ^= h1 >>> 16;
        return HashCode.fromInt(h1);
    }
    
    private static final class Murmur3_32Hasher extends AbstractStreamingHasher
    {
        private static final int CHUNK_SIZE = 4;
        private int h1;
        private int length;
        
        Murmur3_32Hasher(final int seed) {
            super(4);
            this.h1 = seed;
            this.length = 0;
        }
        
        @Override
        protected void process(final ByteBuffer bb) {
            final int k1 = mixK1(bb.getInt());
            this.h1 = mixH1(this.h1, k1);
            this.length += 4;
        }
        
        @Override
        protected void processRemaining(final ByteBuffer bb) {
            this.length += bb.remaining();
            int k1 = 0;
            int i = 0;
            while (bb.hasRemaining()) {
                k1 ^= UnsignedBytes.toInt(bb.get()) << i;
                i += 8;
            }
            this.h1 ^= mixK1(k1);
        }
        
        public HashCode makeHash() {
            return fmix(this.h1, this.length);
        }
    }
}
