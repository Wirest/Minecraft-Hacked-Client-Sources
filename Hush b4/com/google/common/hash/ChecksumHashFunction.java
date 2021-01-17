// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.util.zip.Checksum;
import com.google.common.base.Supplier;
import java.io.Serializable;

final class ChecksumHashFunction extends AbstractStreamingHashFunction implements Serializable
{
    private final Supplier<? extends Checksum> checksumSupplier;
    private final int bits;
    private final String toString;
    private static final long serialVersionUID = 0L;
    
    ChecksumHashFunction(final Supplier<? extends Checksum> checksumSupplier, final int bits, final String toString) {
        this.checksumSupplier = Preconditions.checkNotNull(checksumSupplier);
        Preconditions.checkArgument(bits == 32 || bits == 64, "bits (%s) must be either 32 or 64", bits);
        this.bits = bits;
        this.toString = Preconditions.checkNotNull(toString);
    }
    
    @Override
    public int bits() {
        return this.bits;
    }
    
    @Override
    public Hasher newHasher() {
        return new ChecksumHasher((Checksum)this.checksumSupplier.get());
    }
    
    @Override
    public String toString() {
        return this.toString;
    }
    
    private final class ChecksumHasher extends AbstractByteHasher
    {
        private final Checksum checksum;
        
        private ChecksumHasher(final Checksum checksum) {
            this.checksum = Preconditions.checkNotNull(checksum);
        }
        
        @Override
        protected void update(final byte b) {
            this.checksum.update(b);
        }
        
        @Override
        protected void update(final byte[] bytes, final int off, final int len) {
            this.checksum.update(bytes, off, len);
        }
        
        @Override
        public HashCode hash() {
            final long value = this.checksum.getValue();
            if (ChecksumHashFunction.this.bits == 32) {
                return HashCode.fromInt((int)value);
            }
            return HashCode.fromLong(value);
        }
    }
}
