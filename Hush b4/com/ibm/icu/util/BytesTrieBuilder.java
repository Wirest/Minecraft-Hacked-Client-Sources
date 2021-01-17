// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.nio.ByteBuffer;

public final class BytesTrieBuilder extends StringTrieBuilder
{
    private final byte[] intBytes;
    private byte[] bytes;
    private int bytesLength;
    
    public BytesTrieBuilder() {
        this.intBytes = new byte[5];
    }
    
    public BytesTrieBuilder add(final byte[] sequence, final int length, final int value) {
        this.addImpl(new BytesAsCharSequence(sequence, length), value);
        return this;
    }
    
    public BytesTrie build(final Option buildOption) {
        this.buildBytes(buildOption);
        return new BytesTrie(this.bytes, this.bytes.length - this.bytesLength);
    }
    
    public ByteBuffer buildByteBuffer(final Option buildOption) {
        this.buildBytes(buildOption);
        return ByteBuffer.wrap(this.bytes, this.bytes.length - this.bytesLength, this.bytesLength);
    }
    
    private void buildBytes(final Option buildOption) {
        if (this.bytes == null) {
            this.bytes = new byte[1024];
        }
        this.buildImpl(buildOption);
    }
    
    public BytesTrieBuilder clear() {
        this.clearImpl();
        this.bytes = null;
        this.bytesLength = 0;
        return this;
    }
    
    @Override
    @Deprecated
    protected boolean matchNodesCanHaveValues() {
        return false;
    }
    
    @Override
    @Deprecated
    protected int getMaxBranchLinearSubNodeLength() {
        return 5;
    }
    
    @Override
    @Deprecated
    protected int getMinLinearMatch() {
        return 16;
    }
    
    @Override
    @Deprecated
    protected int getMaxLinearMatchLength() {
        return 16;
    }
    
    private void ensureCapacity(final int length) {
        if (length > this.bytes.length) {
            int newCapacity = this.bytes.length;
            do {
                newCapacity *= 2;
            } while (newCapacity <= length);
            final byte[] newBytes = new byte[newCapacity];
            System.arraycopy(this.bytes, this.bytes.length - this.bytesLength, newBytes, newBytes.length - this.bytesLength, this.bytesLength);
            this.bytes = newBytes;
        }
    }
    
    @Override
    @Deprecated
    protected int write(final int b) {
        final int newLength = this.bytesLength + 1;
        this.ensureCapacity(newLength);
        this.bytesLength = newLength;
        this.bytes[this.bytes.length - this.bytesLength] = (byte)b;
        return this.bytesLength;
    }
    
    @Override
    @Deprecated
    protected int write(int offset, int length) {
        final int newLength = this.bytesLength + length;
        this.ensureCapacity(newLength);
        this.bytesLength = newLength;
        int bytesOffset = this.bytes.length - this.bytesLength;
        while (length > 0) {
            this.bytes[bytesOffset++] = (byte)this.strings.charAt(offset++);
            --length;
        }
        return this.bytesLength;
    }
    
    private int write(final byte[] b, final int length) {
        final int newLength = this.bytesLength + length;
        this.ensureCapacity(newLength);
        this.bytesLength = newLength;
        System.arraycopy(b, 0, this.bytes, this.bytes.length - this.bytesLength, length);
        return this.bytesLength;
    }
    
    @Override
    @Deprecated
    protected int writeValueAndFinal(final int i, final boolean isFinal) {
        if (0 <= i && i <= 64) {
            return this.write(16 + i << 1 | (isFinal ? 1 : 0));
        }
        int length = 1;
        if (i < 0 || i > 16777215) {
            this.intBytes[0] = 127;
            this.intBytes[1] = (byte)(i >> 24);
            this.intBytes[2] = (byte)(i >> 16);
            this.intBytes[3] = (byte)(i >> 8);
            this.intBytes[4] = (byte)i;
            length = 5;
        }
        else {
            if (i <= 6911) {
                this.intBytes[0] = (byte)(81 + (i >> 8));
            }
            else {
                if (i <= 1179647) {
                    this.intBytes[0] = (byte)(108 + (i >> 16));
                }
                else {
                    this.intBytes[0] = 126;
                    this.intBytes[1] = (byte)(i >> 16);
                    length = 2;
                }
                this.intBytes[length++] = (byte)(i >> 8);
            }
            this.intBytes[length++] = (byte)i;
        }
        this.intBytes[0] = (byte)(this.intBytes[0] << 1 | (isFinal ? 1 : 0));
        return this.write(this.intBytes, length);
    }
    
    @Override
    @Deprecated
    protected int writeValueAndType(final boolean hasValue, final int value, final int node) {
        int offset = this.write(node);
        if (hasValue) {
            offset = this.writeValueAndFinal(value, false);
        }
        return offset;
    }
    
    @Override
    @Deprecated
    protected int writeDeltaTo(final int jumpTarget) {
        final int i = this.bytesLength - jumpTarget;
        assert i >= 0;
        if (i <= 191) {
            return this.write(i);
        }
        int length;
        if (i <= 12287) {
            this.intBytes[0] = (byte)(192 + (i >> 8));
            length = 1;
        }
        else {
            if (i <= 917503) {
                this.intBytes[0] = (byte)(240 + (i >> 16));
                length = 2;
            }
            else {
                if (i <= 16777215) {
                    this.intBytes[0] = -2;
                    length = 3;
                }
                else {
                    this.intBytes[0] = -1;
                    this.intBytes[1] = (byte)(i >> 24);
                    length = 4;
                }
                this.intBytes[1] = (byte)(i >> 16);
            }
            this.intBytes[1] = (byte)(i >> 8);
        }
        this.intBytes[length++] = (byte)i;
        return this.write(this.intBytes, length);
    }
    
    private static final class BytesAsCharSequence implements CharSequence
    {
        private byte[] s;
        private int len;
        
        public BytesAsCharSequence(final byte[] sequence, final int length) {
            this.s = sequence;
            this.len = length;
        }
        
        public char charAt(final int i) {
            return (char)(this.s[i] & 0xFF);
        }
        
        public int length() {
            return this.len;
        }
        
        public CharSequence subSequence(final int start, final int end) {
            return null;
        }
    }
}
