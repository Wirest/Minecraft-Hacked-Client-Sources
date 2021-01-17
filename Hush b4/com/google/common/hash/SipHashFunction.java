// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.nio.ByteBuffer;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import java.io.Serializable;

final class SipHashFunction extends AbstractStreamingHashFunction implements Serializable
{
    private final int c;
    private final int d;
    private final long k0;
    private final long k1;
    private static final long serialVersionUID = 0L;
    
    SipHashFunction(final int c, final int d, final long k0, final long k1) {
        Preconditions.checkArgument(c > 0, "The number of SipRound iterations (c=%s) during Compression must be positive.", c);
        Preconditions.checkArgument(d > 0, "The number of SipRound iterations (d=%s) during Finalization must be positive.", d);
        this.c = c;
        this.d = d;
        this.k0 = k0;
        this.k1 = k1;
    }
    
    @Override
    public int bits() {
        return 64;
    }
    
    @Override
    public Hasher newHasher() {
        return new SipHasher(this.c, this.d, this.k0, this.k1);
    }
    
    @Override
    public String toString() {
        return "Hashing.sipHash" + this.c + "" + this.d + "(" + this.k0 + ", " + this.k1 + ")";
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object instanceof SipHashFunction) {
            final SipHashFunction other = (SipHashFunction)object;
            return this.c == other.c && this.d == other.d && this.k0 == other.k0 && this.k1 == other.k1;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int)((long)(this.getClass().hashCode() ^ this.c ^ this.d) ^ this.k0 ^ this.k1);
    }
    
    private static final class SipHasher extends AbstractStreamingHasher
    {
        private static final int CHUNK_SIZE = 8;
        private final int c;
        private final int d;
        private long v0;
        private long v1;
        private long v2;
        private long v3;
        private long b;
        private long finalM;
        
        SipHasher(final int c, final int d, final long k0, final long k1) {
            super(8);
            this.v0 = 8317987319222330741L;
            this.v1 = 7237128888997146477L;
            this.v2 = 7816392313619706465L;
            this.v3 = 8387220255154660723L;
            this.b = 0L;
            this.finalM = 0L;
            this.c = c;
            this.d = d;
            this.v0 ^= k0;
            this.v1 ^= k1;
            this.v2 ^= k0;
            this.v3 ^= k1;
        }
        
        @Override
        protected void process(final ByteBuffer buffer) {
            this.b += 8L;
            this.processM(buffer.getLong());
        }
        
        @Override
        protected void processRemaining(final ByteBuffer buffer) {
            this.b += buffer.remaining();
            int i = 0;
            while (buffer.hasRemaining()) {
                this.finalM ^= ((long)buffer.get() & 0xFFL) << i;
                i += 8;
            }
        }
        
        public HashCode makeHash() {
            this.processM(this.finalM ^= this.b << 56);
            this.v2 ^= 0xFFL;
            this.sipRound(this.d);
            return HashCode.fromLong(this.v0 ^ this.v1 ^ this.v2 ^ this.v3);
        }
        
        private void processM(final long m) {
            this.v3 ^= m;
            this.sipRound(this.c);
            this.v0 ^= m;
        }
        
        private void sipRound(final int iterations) {
            for (int i = 0; i < iterations; ++i) {
                this.v0 += this.v1;
                this.v2 += this.v3;
                this.v1 = Long.rotateLeft(this.v1, 13);
                this.v3 = Long.rotateLeft(this.v3, 16);
                this.v1 ^= this.v0;
                this.v3 ^= this.v2;
                this.v0 = Long.rotateLeft(this.v0, 32);
                this.v2 += this.v1;
                this.v0 += this.v3;
                this.v1 = Long.rotateLeft(this.v1, 17);
                this.v3 = Long.rotateLeft(this.v3, 21);
                this.v1 ^= this.v2;
                this.v3 ^= this.v0;
                this.v2 = Long.rotateLeft(this.v2, 32);
            }
        }
    }
}
