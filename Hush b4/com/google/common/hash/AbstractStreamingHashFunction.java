// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import com.google.common.base.Preconditions;
import java.nio.charset.Charset;

abstract class AbstractStreamingHashFunction implements HashFunction
{
    @Override
    public <T> HashCode hashObject(final T instance, final Funnel<? super T> funnel) {
        return this.newHasher().putObject(instance, funnel).hash();
    }
    
    @Override
    public HashCode hashUnencodedChars(final CharSequence input) {
        return this.newHasher().putUnencodedChars(input).hash();
    }
    
    @Override
    public HashCode hashString(final CharSequence input, final Charset charset) {
        return this.newHasher().putString(input, charset).hash();
    }
    
    @Override
    public HashCode hashInt(final int input) {
        return this.newHasher().putInt(input).hash();
    }
    
    @Override
    public HashCode hashLong(final long input) {
        return this.newHasher().putLong(input).hash();
    }
    
    @Override
    public HashCode hashBytes(final byte[] input) {
        return this.newHasher().putBytes(input).hash();
    }
    
    @Override
    public HashCode hashBytes(final byte[] input, final int off, final int len) {
        return this.newHasher().putBytes(input, off, len).hash();
    }
    
    @Override
    public Hasher newHasher(final int expectedInputSize) {
        Preconditions.checkArgument(expectedInputSize >= 0);
        return this.newHasher();
    }
    
    protected abstract static class AbstractStreamingHasher extends AbstractHasher
    {
        private final ByteBuffer buffer;
        private final int bufferSize;
        private final int chunkSize;
        
        protected AbstractStreamingHasher(final int chunkSize) {
            this(chunkSize, chunkSize);
        }
        
        protected AbstractStreamingHasher(final int chunkSize, final int bufferSize) {
            Preconditions.checkArgument(bufferSize % chunkSize == 0);
            this.buffer = ByteBuffer.allocate(bufferSize + 7).order(ByteOrder.LITTLE_ENDIAN);
            this.bufferSize = bufferSize;
            this.chunkSize = chunkSize;
        }
        
        protected abstract void process(final ByteBuffer p0);
        
        protected void processRemaining(final ByteBuffer bb) {
            bb.position(bb.limit());
            bb.limit(this.chunkSize + 7);
            while (bb.position() < this.chunkSize) {
                bb.putLong(0L);
            }
            bb.limit(this.chunkSize);
            bb.flip();
            this.process(bb);
        }
        
        @Override
        public final Hasher putBytes(final byte[] bytes) {
            return this.putBytes(bytes, 0, bytes.length);
        }
        
        @Override
        public final Hasher putBytes(final byte[] bytes, final int off, final int len) {
            return this.putBytes(ByteBuffer.wrap(bytes, off, len).order(ByteOrder.LITTLE_ENDIAN));
        }
        
        private Hasher putBytes(final ByteBuffer readBuffer) {
            if (readBuffer.remaining() <= this.buffer.remaining()) {
                this.buffer.put(readBuffer);
                this.munchIfFull();
                return this;
            }
            for (int bytesToCopy = this.bufferSize - this.buffer.position(), i = 0; i < bytesToCopy; ++i) {
                this.buffer.put(readBuffer.get());
            }
            this.munch();
            while (readBuffer.remaining() >= this.chunkSize) {
                this.process(readBuffer);
            }
            this.buffer.put(readBuffer);
            return this;
        }
        
        @Override
        public final Hasher putUnencodedChars(final CharSequence charSequence) {
            for (int i = 0; i < charSequence.length(); ++i) {
                this.putChar(charSequence.charAt(i));
            }
            return this;
        }
        
        @Override
        public final Hasher putByte(final byte b) {
            this.buffer.put(b);
            this.munchIfFull();
            return this;
        }
        
        @Override
        public final Hasher putShort(final short s) {
            this.buffer.putShort(s);
            this.munchIfFull();
            return this;
        }
        
        @Override
        public final Hasher putChar(final char c) {
            this.buffer.putChar(c);
            this.munchIfFull();
            return this;
        }
        
        @Override
        public final Hasher putInt(final int i) {
            this.buffer.putInt(i);
            this.munchIfFull();
            return this;
        }
        
        @Override
        public final Hasher putLong(final long l) {
            this.buffer.putLong(l);
            this.munchIfFull();
            return this;
        }
        
        @Override
        public final <T> Hasher putObject(final T instance, final Funnel<? super T> funnel) {
            funnel.funnel((Object)instance, (PrimitiveSink)this);
            return this;
        }
        
        @Override
        public final HashCode hash() {
            this.munch();
            this.buffer.flip();
            if (this.buffer.remaining() > 0) {
                this.processRemaining(this.buffer);
            }
            return this.makeHash();
        }
        
        abstract HashCode makeHash();
        
        private void munchIfFull() {
            if (this.buffer.remaining() < 8) {
                this.munch();
            }
        }
        
        private void munch() {
            this.buffer.flip();
            while (this.buffer.remaining() >= this.chunkSize) {
                this.process(this.buffer);
            }
            this.buffer.compact();
        }
    }
}
