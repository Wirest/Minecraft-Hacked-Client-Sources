// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.nio.CharBuffer;

public final class CharsTrieBuilder extends StringTrieBuilder
{
    private final char[] intUnits;
    private char[] chars;
    private int charsLength;
    
    public CharsTrieBuilder() {
        this.intUnits = new char[3];
    }
    
    public CharsTrieBuilder add(final CharSequence s, final int value) {
        this.addImpl(s, value);
        return this;
    }
    
    public CharsTrie build(final Option buildOption) {
        return new CharsTrie(this.buildCharSequence(buildOption), 0);
    }
    
    public CharSequence buildCharSequence(final Option buildOption) {
        this.buildChars(buildOption);
        return CharBuffer.wrap(this.chars, this.chars.length - this.charsLength, this.charsLength);
    }
    
    private void buildChars(final Option buildOption) {
        if (this.chars == null) {
            this.chars = new char[1024];
        }
        this.buildImpl(buildOption);
    }
    
    public CharsTrieBuilder clear() {
        this.clearImpl();
        this.chars = null;
        this.charsLength = 0;
        return this;
    }
    
    @Override
    @Deprecated
    protected boolean matchNodesCanHaveValues() {
        return true;
    }
    
    @Override
    @Deprecated
    protected int getMaxBranchLinearSubNodeLength() {
        return 5;
    }
    
    @Override
    @Deprecated
    protected int getMinLinearMatch() {
        return 48;
    }
    
    @Override
    @Deprecated
    protected int getMaxLinearMatchLength() {
        return 16;
    }
    
    private void ensureCapacity(final int length) {
        if (length > this.chars.length) {
            int newCapacity = this.chars.length;
            do {
                newCapacity *= 2;
            } while (newCapacity <= length);
            final char[] newChars = new char[newCapacity];
            System.arraycopy(this.chars, this.chars.length - this.charsLength, newChars, newChars.length - this.charsLength, this.charsLength);
            this.chars = newChars;
        }
    }
    
    @Override
    @Deprecated
    protected int write(final int unit) {
        final int newLength = this.charsLength + 1;
        this.ensureCapacity(newLength);
        this.charsLength = newLength;
        this.chars[this.chars.length - this.charsLength] = (char)unit;
        return this.charsLength;
    }
    
    @Override
    @Deprecated
    protected int write(int offset, int length) {
        final int newLength = this.charsLength + length;
        this.ensureCapacity(newLength);
        this.charsLength = newLength;
        int charsOffset = this.chars.length - this.charsLength;
        while (length > 0) {
            this.chars[charsOffset++] = this.strings.charAt(offset++);
            --length;
        }
        return this.charsLength;
    }
    
    private int write(final char[] s, final int length) {
        final int newLength = this.charsLength + length;
        this.ensureCapacity(newLength);
        this.charsLength = newLength;
        System.arraycopy(s, 0, this.chars, this.chars.length - this.charsLength, length);
        return this.charsLength;
    }
    
    @Override
    @Deprecated
    protected int writeValueAndFinal(final int i, final boolean isFinal) {
        if (0 <= i && i <= 16383) {
            return this.write(i | (isFinal ? 32768 : 0));
        }
        int length;
        if (i < 0 || i > 1073676287) {
            this.intUnits[0] = '\u7fff';
            this.intUnits[1] = (char)(i >> 16);
            this.intUnits[2] = (char)i;
            length = 3;
        }
        else {
            this.intUnits[0] = (char)(16384 + (i >> 16));
            this.intUnits[1] = (char)i;
            length = 2;
        }
        this.intUnits[0] |= (isFinal ? '\u8000' : '\0');
        return this.write(this.intUnits, length);
    }
    
    @Override
    @Deprecated
    protected int writeValueAndType(final boolean hasValue, final int value, final int node) {
        if (!hasValue) {
            return this.write(node);
        }
        int length;
        if (value < 0 || value > 16646143) {
            this.intUnits[0] = '\u7fc0';
            this.intUnits[1] = (char)(value >> 16);
            this.intUnits[2] = (char)value;
            length = 3;
        }
        else if (value <= 255) {
            this.intUnits[0] = (char)(value + 1 << 6);
            length = 1;
        }
        else {
            this.intUnits[0] = (char)(16448 + (value >> 10 & 0x7FC0));
            this.intUnits[1] = (char)value;
            length = 2;
        }
        final char[] intUnits = this.intUnits;
        final int n = 0;
        intUnits[n] |= (char)node;
        return this.write(this.intUnits, length);
    }
    
    @Override
    @Deprecated
    protected int writeDeltaTo(final int jumpTarget) {
        final int i = this.charsLength - jumpTarget;
        assert i >= 0;
        if (i <= 64511) {
            return this.write(i);
        }
        int length;
        if (i <= 67043327) {
            this.intUnits[0] = (char)(64512 + (i >> 16));
            length = 1;
        }
        else {
            this.intUnits[0] = '\uffff';
            this.intUnits[1] = (char)(i >> 16);
            length = 2;
        }
        this.intUnits[length++] = (char)i;
        return this.write(this.intUnits, length);
    }
}
