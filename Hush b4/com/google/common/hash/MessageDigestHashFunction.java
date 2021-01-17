// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.util.Arrays;
import java.security.NoSuchAlgorithmException;
import com.google.common.base.Preconditions;
import java.security.MessageDigest;
import java.io.Serializable;

final class MessageDigestHashFunction extends AbstractStreamingHashFunction implements Serializable
{
    private final MessageDigest prototype;
    private final int bytes;
    private final boolean supportsClone;
    private final String toString;
    
    MessageDigestHashFunction(final String algorithmName, final String toString) {
        this.prototype = getMessageDigest(algorithmName);
        this.bytes = this.prototype.getDigestLength();
        this.toString = Preconditions.checkNotNull(toString);
        this.supportsClone = this.supportsClone();
    }
    
    MessageDigestHashFunction(final String algorithmName, final int bytes, final String toString) {
        this.toString = Preconditions.checkNotNull(toString);
        this.prototype = getMessageDigest(algorithmName);
        final int maxLength = this.prototype.getDigestLength();
        Preconditions.checkArgument(bytes >= 4 && bytes <= maxLength, "bytes (%s) must be >= 4 and < %s", bytes, maxLength);
        this.bytes = bytes;
        this.supportsClone = this.supportsClone();
    }
    
    private boolean supportsClone() {
        try {
            this.prototype.clone();
            return true;
        }
        catch (CloneNotSupportedException e) {
            return false;
        }
    }
    
    @Override
    public int bits() {
        return this.bytes * 8;
    }
    
    @Override
    public String toString() {
        return this.toString;
    }
    
    private static MessageDigest getMessageDigest(final String algorithmName) {
        try {
            return MessageDigest.getInstance(algorithmName);
        }
        catch (NoSuchAlgorithmException e) {
            throw new AssertionError((Object)e);
        }
    }
    
    @Override
    public Hasher newHasher() {
        if (this.supportsClone) {
            try {
                return new MessageDigestHasher((MessageDigest)this.prototype.clone(), this.bytes);
            }
            catch (CloneNotSupportedException ex) {}
        }
        return new MessageDigestHasher(getMessageDigest(this.prototype.getAlgorithm()), this.bytes);
    }
    
    Object writeReplace() {
        return new SerializedForm(this.prototype.getAlgorithm(), this.bytes, this.toString);
    }
    
    private static final class SerializedForm implements Serializable
    {
        private final String algorithmName;
        private final int bytes;
        private final String toString;
        private static final long serialVersionUID = 0L;
        
        private SerializedForm(final String algorithmName, final int bytes, final String toString) {
            this.algorithmName = algorithmName;
            this.bytes = bytes;
            this.toString = toString;
        }
        
        private Object readResolve() {
            return new MessageDigestHashFunction(this.algorithmName, this.bytes, this.toString);
        }
    }
    
    private static final class MessageDigestHasher extends AbstractByteHasher
    {
        private final MessageDigest digest;
        private final int bytes;
        private boolean done;
        
        private MessageDigestHasher(final MessageDigest digest, final int bytes) {
            this.digest = digest;
            this.bytes = bytes;
        }
        
        @Override
        protected void update(final byte b) {
            this.checkNotDone();
            this.digest.update(b);
        }
        
        @Override
        protected void update(final byte[] b) {
            this.checkNotDone();
            this.digest.update(b);
        }
        
        @Override
        protected void update(final byte[] b, final int off, final int len) {
            this.checkNotDone();
            this.digest.update(b, off, len);
        }
        
        private void checkNotDone() {
            Preconditions.checkState(!this.done, (Object)"Cannot re-use a Hasher after calling hash() on it");
        }
        
        @Override
        public HashCode hash() {
            this.checkNotDone();
            this.done = true;
            return (this.bytes == this.digest.getDigestLength()) ? HashCode.fromBytesNoCopy(this.digest.digest()) : HashCode.fromBytesNoCopy(Arrays.copyOf(this.digest.digest(), this.bytes));
        }
    }
}
