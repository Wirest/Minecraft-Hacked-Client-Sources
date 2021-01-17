// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import com.google.common.base.Preconditions;

abstract class AbstractNonStreamingHashFunction implements HashFunction
{
    @Override
    public Hasher newHasher() {
        return new BufferingHasher(32);
    }
    
    @Override
    public Hasher newHasher(final int expectedInputSize) {
        Preconditions.checkArgument(expectedInputSize >= 0);
        return new BufferingHasher(expectedInputSize);
    }
    
    @Override
    public <T> HashCode hashObject(final T instance, final Funnel<? super T> funnel) {
        return this.newHasher().putObject(instance, funnel).hash();
    }
    
    @Override
    public HashCode hashUnencodedChars(final CharSequence input) {
        final int len = input.length();
        final Hasher hasher = this.newHasher(len * 2);
        for (int i = 0; i < len; ++i) {
            hasher.putChar(input.charAt(i));
        }
        return hasher.hash();
    }
    
    @Override
    public HashCode hashString(final CharSequence input, final Charset charset) {
        return this.hashBytes(input.toString().getBytes(charset));
    }
    
    @Override
    public HashCode hashInt(final int input) {
        return this.newHasher(4).putInt(input).hash();
    }
    
    @Override
    public HashCode hashLong(final long input) {
        return this.newHasher(8).putLong(input).hash();
    }
    
    @Override
    public HashCode hashBytes(final byte[] input) {
        return this.hashBytes(input, 0, input.length);
    }
    
    private final class BufferingHasher extends AbstractHasher
    {
        final ExposedByteArrayOutputStream stream;
        static final int BOTTOM_BYTE = 255;
        
        BufferingHasher(final int expectedInputSize) {
            this.stream = new ExposedByteArrayOutputStream(expectedInputSize);
        }
        
        @Override
        public Hasher putByte(final byte b) {
            this.stream.write(b);
            return this;
        }
        
        @Override
        public Hasher putBytes(final byte[] bytes) {
            try {
                this.stream.write(bytes);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this;
        }
        
        @Override
        public Hasher putBytes(final byte[] bytes, final int off, final int len) {
            this.stream.write(bytes, off, len);
            return this;
        }
        
        @Override
        public Hasher putShort(final short s) {
            this.stream.write(s & 0xFF);
            this.stream.write(s >>> 8 & 0xFF);
            return this;
        }
        
        @Override
        public Hasher putInt(final int i) {
            this.stream.write(i & 0xFF);
            this.stream.write(i >>> 8 & 0xFF);
            this.stream.write(i >>> 16 & 0xFF);
            this.stream.write(i >>> 24 & 0xFF);
            return this;
        }
        
        @Override
        public Hasher putLong(final long l) {
            for (int i = 0; i < 64; i += 8) {
                this.stream.write((byte)(l >>> i & 0xFFL));
            }
            return this;
        }
        
        @Override
        public Hasher putChar(final char c) {
            this.stream.write(c & '\u00ff');
            this.stream.write(c >>> 8 & 0xFF);
            return this;
        }
        
        @Override
        public <T> Hasher putObject(final T instance, final Funnel<? super T> funnel) {
            funnel.funnel((Object)instance, (PrimitiveSink)this);
            return this;
        }
        
        @Override
        public HashCode hash() {
            return AbstractNonStreamingHashFunction.this.hashBytes(this.stream.byteArray(), 0, this.stream.length());
        }
    }
    
    private static final class ExposedByteArrayOutputStream extends ByteArrayOutputStream
    {
        ExposedByteArrayOutputStream(final int expectedInputSize) {
            super(expectedInputSize);
        }
        
        byte[] byteArray() {
            return this.buf;
        }
        
        int length() {
            return this.count;
        }
    }
}
