// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import com.google.common.primitives.UnsignedInts;
import java.io.Serializable;
import java.security.MessageDigest;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.annotations.Beta;

@Beta
public abstract class HashCode
{
    private static final char[] hexDigits;
    
    HashCode() {
    }
    
    public abstract int bits();
    
    public abstract int asInt();
    
    public abstract long asLong();
    
    public abstract long padToLong();
    
    public abstract byte[] asBytes();
    
    public int writeBytesTo(final byte[] dest, final int offset, int maxLength) {
        maxLength = Ints.min(maxLength, this.bits() / 8);
        Preconditions.checkPositionIndexes(offset, offset + maxLength, dest.length);
        this.writeBytesToImpl(dest, offset, maxLength);
        return maxLength;
    }
    
    abstract void writeBytesToImpl(final byte[] p0, final int p1, final int p2);
    
    byte[] getBytesInternal() {
        return this.asBytes();
    }
    
    public static HashCode fromInt(final int hash) {
        return new IntHashCode(hash);
    }
    
    public static HashCode fromLong(final long hash) {
        return new LongHashCode(hash);
    }
    
    public static HashCode fromBytes(final byte[] bytes) {
        Preconditions.checkArgument(bytes.length >= 1, (Object)"A HashCode must contain at least 1 byte.");
        return fromBytesNoCopy(bytes.clone());
    }
    
    static HashCode fromBytesNoCopy(final byte[] bytes) {
        return new BytesHashCode(bytes);
    }
    
    public static HashCode fromString(final String string) {
        Preconditions.checkArgument(string.length() >= 2, "input string (%s) must have at least 2 characters", string);
        Preconditions.checkArgument(string.length() % 2 == 0, "input string (%s) must have an even number of characters", string);
        final byte[] bytes = new byte[string.length() / 2];
        for (int i = 0; i < string.length(); i += 2) {
            final int ch1 = decode(string.charAt(i)) << 4;
            final int ch2 = decode(string.charAt(i + 1));
            bytes[i / 2] = (byte)(ch1 + ch2);
        }
        return fromBytesNoCopy(bytes);
    }
    
    private static int decode(final char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - '0';
        }
        if (ch >= 'a' && ch <= 'f') {
            return ch - 'a' + 10;
        }
        throw new IllegalArgumentException("Illegal hexadecimal character: " + ch);
    }
    
    @Override
    public final boolean equals(@Nullable final Object object) {
        if (object instanceof HashCode) {
            final HashCode that = (HashCode)object;
            return MessageDigest.isEqual(this.asBytes(), that.asBytes());
        }
        return false;
    }
    
    @Override
    public final int hashCode() {
        if (this.bits() >= 32) {
            return this.asInt();
        }
        final byte[] bytes = this.asBytes();
        int val = bytes[0] & 0xFF;
        for (int i = 1; i < bytes.length; ++i) {
            val |= (bytes[i] & 0xFF) << i * 8;
        }
        return val;
    }
    
    @Override
    public final String toString() {
        final byte[] bytes = this.asBytes();
        final StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (final byte b : bytes) {
            sb.append(HashCode.hexDigits[b >> 4 & 0xF]).append(HashCode.hexDigits[b & 0xF]);
        }
        return sb.toString();
    }
    
    static {
        hexDigits = "0123456789abcdef".toCharArray();
    }
    
    private static final class IntHashCode extends HashCode implements Serializable
    {
        final int hash;
        private static final long serialVersionUID = 0L;
        
        IntHashCode(final int hash) {
            this.hash = hash;
        }
        
        @Override
        public int bits() {
            return 32;
        }
        
        @Override
        public byte[] asBytes() {
            return new byte[] { (byte)this.hash, (byte)(this.hash >> 8), (byte)(this.hash >> 16), (byte)(this.hash >> 24) };
        }
        
        @Override
        public int asInt() {
            return this.hash;
        }
        
        @Override
        public long asLong() {
            throw new IllegalStateException("this HashCode only has 32 bits; cannot create a long");
        }
        
        @Override
        public long padToLong() {
            return UnsignedInts.toLong(this.hash);
        }
        
        @Override
        void writeBytesToImpl(final byte[] dest, final int offset, final int maxLength) {
            for (int i = 0; i < maxLength; ++i) {
                dest[offset + i] = (byte)(this.hash >> i * 8);
            }
        }
    }
    
    private static final class LongHashCode extends HashCode implements Serializable
    {
        final long hash;
        private static final long serialVersionUID = 0L;
        
        LongHashCode(final long hash) {
            this.hash = hash;
        }
        
        @Override
        public int bits() {
            return 64;
        }
        
        @Override
        public byte[] asBytes() {
            return new byte[] { (byte)this.hash, (byte)(this.hash >> 8), (byte)(this.hash >> 16), (byte)(this.hash >> 24), (byte)(this.hash >> 32), (byte)(this.hash >> 40), (byte)(this.hash >> 48), (byte)(this.hash >> 56) };
        }
        
        @Override
        public int asInt() {
            return (int)this.hash;
        }
        
        @Override
        public long asLong() {
            return this.hash;
        }
        
        @Override
        public long padToLong() {
            return this.hash;
        }
        
        @Override
        void writeBytesToImpl(final byte[] dest, final int offset, final int maxLength) {
            for (int i = 0; i < maxLength; ++i) {
                dest[offset + i] = (byte)(this.hash >> i * 8);
            }
        }
    }
    
    private static final class BytesHashCode extends HashCode implements Serializable
    {
        final byte[] bytes;
        private static final long serialVersionUID = 0L;
        
        BytesHashCode(final byte[] bytes) {
            this.bytes = Preconditions.checkNotNull(bytes);
        }
        
        @Override
        public int bits() {
            return this.bytes.length * 8;
        }
        
        @Override
        public byte[] asBytes() {
            return this.bytes.clone();
        }
        
        @Override
        public int asInt() {
            Preconditions.checkState(this.bytes.length >= 4, "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", this.bytes.length);
            return (this.bytes[0] & 0xFF) | (this.bytes[1] & 0xFF) << 8 | (this.bytes[2] & 0xFF) << 16 | (this.bytes[3] & 0xFF) << 24;
        }
        
        @Override
        public long asLong() {
            Preconditions.checkState(this.bytes.length >= 8, "HashCode#asLong() requires >= 8 bytes (it only has %s bytes).", this.bytes.length);
            return this.padToLong();
        }
        
        @Override
        public long padToLong() {
            long retVal = this.bytes[0] & 0xFF;
            for (int i = 1; i < Math.min(this.bytes.length, 8); ++i) {
                retVal |= ((long)this.bytes[i] & 0xFFL) << i * 8;
            }
            return retVal;
        }
        
        @Override
        void writeBytesToImpl(final byte[] dest, final int offset, final int maxLength) {
            System.arraycopy(this.bytes, 0, dest, offset, maxLength);
        }
        
        @Override
        byte[] getBytesInternal() {
            return this.bytes;
        }
    }
}
