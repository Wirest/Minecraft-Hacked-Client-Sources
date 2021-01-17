// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import javax.annotation.Nullable;
import com.google.common.annotations.VisibleForTesting;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.zip.Checksum;
import com.google.common.base.Supplier;
import com.google.common.annotations.Beta;

@Beta
public final class Hashing
{
    private static final int GOOD_FAST_HASH_SEED;
    
    public static HashFunction goodFastHash(final int minimumBits) {
        final int bits = checkPositiveAndMakeMultipleOf32(minimumBits);
        if (bits == 32) {
            return Murmur3_32Holder.GOOD_FAST_HASH_FUNCTION_32;
        }
        if (bits <= 128) {
            return Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
        }
        final int hashFunctionsNeeded = (bits + 127) / 128;
        final HashFunction[] hashFunctions = new HashFunction[hashFunctionsNeeded];
        hashFunctions[0] = Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
        int seed = Hashing.GOOD_FAST_HASH_SEED;
        for (int i = 1; i < hashFunctionsNeeded; ++i) {
            seed += 1500450271;
            hashFunctions[i] = murmur3_128(seed);
        }
        return new ConcatenatedHashFunction(hashFunctions);
    }
    
    public static HashFunction murmur3_32(final int seed) {
        return new Murmur3_32HashFunction(seed);
    }
    
    public static HashFunction murmur3_32() {
        return Murmur3_32Holder.MURMUR3_32;
    }
    
    public static HashFunction murmur3_128(final int seed) {
        return new Murmur3_128HashFunction(seed);
    }
    
    public static HashFunction murmur3_128() {
        return Murmur3_128Holder.MURMUR3_128;
    }
    
    public static HashFunction sipHash24() {
        return SipHash24Holder.SIP_HASH_24;
    }
    
    public static HashFunction sipHash24(final long k0, final long k1) {
        return new SipHashFunction(2, 4, k0, k1);
    }
    
    public static HashFunction md5() {
        return Md5Holder.MD5;
    }
    
    public static HashFunction sha1() {
        return Sha1Holder.SHA_1;
    }
    
    public static HashFunction sha256() {
        return Sha256Holder.SHA_256;
    }
    
    public static HashFunction sha512() {
        return Sha512Holder.SHA_512;
    }
    
    public static HashFunction crc32() {
        return Crc32Holder.CRC_32;
    }
    
    public static HashFunction adler32() {
        return Adler32Holder.ADLER_32;
    }
    
    private static HashFunction checksumHashFunction(final ChecksumType type, final String toString) {
        return new ChecksumHashFunction(type, type.bits, toString);
    }
    
    public static int consistentHash(final HashCode hashCode, final int buckets) {
        return consistentHash(hashCode.padToLong(), buckets);
    }
    
    public static int consistentHash(final long input, final int buckets) {
        Preconditions.checkArgument(buckets > 0, "buckets must be positive: %s", buckets);
        final LinearCongruentialGenerator generator = new LinearCongruentialGenerator(input);
        int candidate = 0;
        while (true) {
            final int next = (int)((candidate + 1) / generator.nextDouble());
            if (next < 0 || next >= buckets) {
                break;
            }
            candidate = next;
        }
        return candidate;
    }
    
    public static HashCode combineOrdered(final Iterable<HashCode> hashCodes) {
        final Iterator<HashCode> iterator = hashCodes.iterator();
        Preconditions.checkArgument(iterator.hasNext(), (Object)"Must be at least 1 hash code to combine.");
        final int bits = iterator.next().bits();
        final byte[] resultBytes = new byte[bits / 8];
        for (final HashCode hashCode : hashCodes) {
            final byte[] nextBytes = hashCode.asBytes();
            Preconditions.checkArgument(nextBytes.length == resultBytes.length, (Object)"All hashcodes must have the same bit length.");
            for (int i = 0; i < nextBytes.length; ++i) {
                resultBytes[i] = (byte)(resultBytes[i] * 37 ^ nextBytes[i]);
            }
        }
        return HashCode.fromBytesNoCopy(resultBytes);
    }
    
    public static HashCode combineUnordered(final Iterable<HashCode> hashCodes) {
        final Iterator<HashCode> iterator = hashCodes.iterator();
        Preconditions.checkArgument(iterator.hasNext(), (Object)"Must be at least 1 hash code to combine.");
        final byte[] resultBytes = new byte[iterator.next().bits() / 8];
        for (final HashCode hashCode : hashCodes) {
            final byte[] nextBytes = hashCode.asBytes();
            Preconditions.checkArgument(nextBytes.length == resultBytes.length, (Object)"All hashcodes must have the same bit length.");
            for (int i = 0; i < nextBytes.length; ++i) {
                final byte[] array = resultBytes;
                final int n = i;
                array[n] += nextBytes[i];
            }
        }
        return HashCode.fromBytesNoCopy(resultBytes);
    }
    
    static int checkPositiveAndMakeMultipleOf32(final int bits) {
        Preconditions.checkArgument(bits > 0, (Object)"Number of bits must be positive");
        return bits + 31 & 0xFFFFFFE0;
    }
    
    private Hashing() {
    }
    
    static {
        GOOD_FAST_HASH_SEED = (int)System.currentTimeMillis();
    }
    
    private static class Murmur3_32Holder
    {
        static final HashFunction MURMUR3_32;
        static final HashFunction GOOD_FAST_HASH_FUNCTION_32;
        
        static {
            MURMUR3_32 = new Murmur3_32HashFunction(0);
            GOOD_FAST_HASH_FUNCTION_32 = Hashing.murmur3_32(Hashing.GOOD_FAST_HASH_SEED);
        }
    }
    
    private static class Murmur3_128Holder
    {
        static final HashFunction MURMUR3_128;
        static final HashFunction GOOD_FAST_HASH_FUNCTION_128;
        
        static {
            MURMUR3_128 = new Murmur3_128HashFunction(0);
            GOOD_FAST_HASH_FUNCTION_128 = Hashing.murmur3_128(Hashing.GOOD_FAST_HASH_SEED);
        }
    }
    
    private static class SipHash24Holder
    {
        static final HashFunction SIP_HASH_24;
        
        static {
            SIP_HASH_24 = new SipHashFunction(2, 4, 506097522914230528L, 1084818905618843912L);
        }
    }
    
    private static class Md5Holder
    {
        static final HashFunction MD5;
        
        static {
            MD5 = new MessageDigestHashFunction("MD5", "Hashing.md5()");
        }
    }
    
    private static class Sha1Holder
    {
        static final HashFunction SHA_1;
        
        static {
            SHA_1 = new MessageDigestHashFunction("SHA-1", "Hashing.sha1()");
        }
    }
    
    private static class Sha256Holder
    {
        static final HashFunction SHA_256;
        
        static {
            SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");
        }
    }
    
    private static class Sha512Holder
    {
        static final HashFunction SHA_512;
        
        static {
            SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");
        }
    }
    
    private static class Crc32Holder
    {
        static final HashFunction CRC_32;
        
        static {
            CRC_32 = checksumHashFunction(ChecksumType.CRC_32, "Hashing.crc32()");
        }
    }
    
    private static class Adler32Holder
    {
        static final HashFunction ADLER_32;
        
        static {
            ADLER_32 = checksumHashFunction(ChecksumType.ADLER_32, "Hashing.adler32()");
        }
    }
    
    enum ChecksumType implements Supplier<Checksum>
    {
        CRC_32(32) {
            @Override
            public Checksum get() {
                return new CRC32();
            }
        }, 
        ADLER_32(32) {
            @Override
            public Checksum get() {
                return new Adler32();
            }
        };
        
        private final int bits;
        
        private ChecksumType(final int bits) {
            this.bits = bits;
        }
        
        @Override
        public abstract Checksum get();
    }
    
    @VisibleForTesting
    static final class ConcatenatedHashFunction extends AbstractCompositeHashFunction
    {
        private final int bits;
        
        ConcatenatedHashFunction(final HashFunction... functions) {
            super(functions);
            int bitSum = 0;
            for (final HashFunction function : functions) {
                bitSum += function.bits();
            }
            this.bits = bitSum;
        }
        
        @Override
        HashCode makeHash(final Hasher[] hashers) {
            final byte[] bytes = new byte[this.bits / 8];
            int i = 0;
            for (final Hasher hasher : hashers) {
                final HashCode newHash = hasher.hash();
                i += newHash.writeBytesTo(bytes, i, newHash.bits() / 8);
            }
            return HashCode.fromBytesNoCopy(bytes);
        }
        
        @Override
        public int bits() {
            return this.bits;
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            if (!(object instanceof ConcatenatedHashFunction)) {
                return false;
            }
            final ConcatenatedHashFunction other = (ConcatenatedHashFunction)object;
            if (this.bits != other.bits || this.functions.length != other.functions.length) {
                return false;
            }
            for (int i = 0; i < this.functions.length; ++i) {
                if (!this.functions[i].equals(other.functions[i])) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            int hash = this.bits;
            for (final HashFunction function : this.functions) {
                hash ^= function.hashCode();
            }
            return hash;
        }
    }
    
    private static final class LinearCongruentialGenerator
    {
        private long state;
        
        public LinearCongruentialGenerator(final long seed) {
            this.state = seed;
        }
        
        public double nextDouble() {
            this.state = 2862933555777941757L * this.state + 1L;
            return ((int)(this.state >>> 33) + 1) / 2.147483648E9;
        }
    }
}
