// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.lang;

import com.ibm.icu.text.UTF16;

public final class UScriptRun
{
    private char[] emptyCharArray;
    private char[] text;
    private int textIndex;
    private int textStart;
    private int textLimit;
    private int scriptStart;
    private int scriptLimit;
    private int scriptCode;
    private static int PAREN_STACK_DEPTH;
    private static ParenStackEntry[] parenStack;
    private int parenSP;
    private int pushCount;
    private int fixupCount;
    private static int[] pairedChars;
    private static int pairedCharPower;
    private static int pairedCharExtra;
    
    @Deprecated
    public UScriptRun() {
        this.emptyCharArray = new char[0];
        this.parenSP = -1;
        this.pushCount = 0;
        this.fixupCount = 0;
        final char[] nullChars = null;
        this.reset(nullChars, 0, 0);
    }
    
    @Deprecated
    public UScriptRun(final String text) {
        this.emptyCharArray = new char[0];
        this.parenSP = -1;
        this.pushCount = 0;
        this.fixupCount = 0;
        this.reset(text);
    }
    
    @Deprecated
    public UScriptRun(final String text, final int start, final int count) {
        this.emptyCharArray = new char[0];
        this.parenSP = -1;
        this.pushCount = 0;
        this.fixupCount = 0;
        this.reset(text, start, count);
    }
    
    @Deprecated
    public UScriptRun(final char[] chars) {
        this.emptyCharArray = new char[0];
        this.parenSP = -1;
        this.pushCount = 0;
        this.fixupCount = 0;
        this.reset(chars);
    }
    
    @Deprecated
    public UScriptRun(final char[] chars, final int start, final int count) {
        this.emptyCharArray = new char[0];
        this.parenSP = -1;
        this.pushCount = 0;
        this.fixupCount = 0;
        this.reset(chars, start, count);
    }
    
    @Deprecated
    public final void reset() {
        while (this.stackIsNotEmpty()) {
            this.pop();
        }
        this.scriptStart = this.textStart;
        this.scriptLimit = this.textStart;
        this.scriptCode = -1;
        this.parenSP = -1;
        this.pushCount = 0;
        this.fixupCount = 0;
        this.textIndex = this.textStart;
    }
    
    @Deprecated
    public final void reset(final int start, final int count) throws IllegalArgumentException {
        int len = 0;
        if (this.text != null) {
            len = this.text.length;
        }
        if (start < 0 || count < 0 || start > len - count) {
            throw new IllegalArgumentException();
        }
        this.textStart = start;
        this.textLimit = start + count;
        this.reset();
    }
    
    @Deprecated
    public final void reset(char[] chars, final int start, final int count) {
        if (chars == null) {
            chars = this.emptyCharArray;
        }
        this.text = chars;
        this.reset(start, count);
    }
    
    @Deprecated
    public final void reset(final char[] chars) {
        int length = 0;
        if (chars != null) {
            length = chars.length;
        }
        this.reset(chars, 0, length);
    }
    
    @Deprecated
    public final void reset(final String str, final int start, final int count) {
        char[] chars = null;
        if (str != null) {
            chars = str.toCharArray();
        }
        this.reset(chars, start, count);
    }
    
    @Deprecated
    public final void reset(final String str) {
        int length = 0;
        if (str != null) {
            length = str.length();
        }
        this.reset(str, 0, length);
    }
    
    @Deprecated
    public final int getScriptStart() {
        return this.scriptStart;
    }
    
    @Deprecated
    public final int getScriptLimit() {
        return this.scriptLimit;
    }
    
    @Deprecated
    public final int getScriptCode() {
        return this.scriptCode;
    }
    
    @Deprecated
    public final boolean next() {
        if (this.scriptLimit >= this.textLimit) {
            return false;
        }
        this.scriptCode = 0;
        this.scriptStart = this.scriptLimit;
        this.syncFixup();
        while (this.textIndex < this.textLimit) {
            final int ch = UTF16.charAt(this.text, this.textStart, this.textLimit, this.textIndex - this.textStart);
            final int codePointCount = UTF16.getCharCount(ch);
            int sc = UScript.getScript(ch);
            final int pairIndex = getPairIndex(ch);
            this.textIndex += codePointCount;
            if (pairIndex >= 0) {
                if ((pairIndex & 0x1) == 0x0) {
                    this.push(pairIndex, this.scriptCode);
                }
                else {
                    final int pi = pairIndex & 0xFFFFFFFE;
                    while (this.stackIsNotEmpty() && this.top().pairIndex != pi) {
                        this.pop();
                    }
                    if (this.stackIsNotEmpty()) {
                        sc = this.top().scriptCode;
                    }
                }
            }
            if (!sameScript(this.scriptCode, sc)) {
                this.textIndex -= codePointCount;
                break;
            }
            if (this.scriptCode <= 1 && sc > 1) {
                this.fixup(this.scriptCode = sc);
            }
            if (pairIndex < 0 || (pairIndex & 0x1) == 0x0) {
                continue;
            }
            this.pop();
        }
        this.scriptLimit = this.textIndex;
        return true;
    }
    
    private static boolean sameScript(final int scriptOne, final int scriptTwo) {
        return scriptOne <= 1 || scriptTwo <= 1 || scriptOne == scriptTwo;
    }
    
    private static final int mod(final int sp) {
        return sp % UScriptRun.PAREN_STACK_DEPTH;
    }
    
    private static final int inc(final int sp, final int count) {
        return mod(sp + count);
    }
    
    private static final int inc(final int sp) {
        return inc(sp, 1);
    }
    
    private static final int dec(final int sp, final int count) {
        return mod(sp + UScriptRun.PAREN_STACK_DEPTH - count);
    }
    
    private static final int dec(final int sp) {
        return dec(sp, 1);
    }
    
    private static final int limitInc(int count) {
        if (count < UScriptRun.PAREN_STACK_DEPTH) {
            ++count;
        }
        return count;
    }
    
    private final boolean stackIsEmpty() {
        return this.pushCount <= 0;
    }
    
    private final boolean stackIsNotEmpty() {
        return !this.stackIsEmpty();
    }
    
    private final void push(final int pairIndex, final int scrptCode) {
        this.pushCount = limitInc(this.pushCount);
        this.fixupCount = limitInc(this.fixupCount);
        this.parenSP = inc(this.parenSP);
        UScriptRun.parenStack[this.parenSP] = new ParenStackEntry(pairIndex, scrptCode);
    }
    
    private final void pop() {
        if (this.stackIsEmpty()) {
            return;
        }
        UScriptRun.parenStack[this.parenSP] = null;
        if (this.fixupCount > 0) {
            --this.fixupCount;
        }
        --this.pushCount;
        this.parenSP = dec(this.parenSP);
        if (this.stackIsEmpty()) {
            this.parenSP = -1;
        }
    }
    
    private final ParenStackEntry top() {
        return UScriptRun.parenStack[this.parenSP];
    }
    
    private final void syncFixup() {
        this.fixupCount = 0;
    }
    
    private final void fixup(final int scrptCode) {
        int fixupSP = dec(this.parenSP, this.fixupCount);
        while (this.fixupCount-- > 0) {
            fixupSP = inc(fixupSP);
            UScriptRun.parenStack[fixupSP].scriptCode = scrptCode;
        }
    }
    
    private static final byte highBit(int n) {
        if (n <= 0) {
            return -32;
        }
        byte bit = 0;
        if (n >= 65536) {
            n >>= 16;
            bit += 16;
        }
        if (n >= 256) {
            n >>= 8;
            bit += 8;
        }
        if (n >= 16) {
            n >>= 4;
            bit += 4;
        }
        if (n >= 4) {
            n >>= 2;
            bit += 2;
        }
        if (n >= 2) {
            n >>= 1;
            ++bit;
        }
        return bit;
    }
    
    private static int getPairIndex(final int ch) {
        int probe = UScriptRun.pairedCharPower;
        int index = 0;
        if (ch >= UScriptRun.pairedChars[UScriptRun.pairedCharExtra]) {
            index = UScriptRun.pairedCharExtra;
        }
        while (probe > 1) {
            probe >>= 1;
            if (ch >= UScriptRun.pairedChars[index + probe]) {
                index += probe;
            }
        }
        if (UScriptRun.pairedChars[index] != ch) {
            index = -1;
        }
        return index;
    }
    
    static {
        UScriptRun.PAREN_STACK_DEPTH = 32;
        UScriptRun.parenStack = new ParenStackEntry[UScriptRun.PAREN_STACK_DEPTH];
        UScriptRun.pairedChars = new int[] { 40, 41, 60, 62, 91, 93, 123, 125, 171, 187, 8216, 8217, 8220, 8221, 8249, 8250, 12296, 12297, 12298, 12299, 12300, 12301, 12302, 12303, 12304, 12305, 12308, 12309, 12310, 12311, 12312, 12313, 12314, 12315 };
        UScriptRun.pairedCharPower = 1 << highBit(UScriptRun.pairedChars.length);
        UScriptRun.pairedCharExtra = UScriptRun.pairedChars.length - UScriptRun.pairedCharPower;
    }
    
    private static final class ParenStackEntry
    {
        int pairIndex;
        int scriptCode;
        
        public ParenStackEntry(final int thePairIndex, final int theScriptCode) {
            this.pairIndex = thePairIndex;
            this.scriptCode = theScriptCode;
        }
    }
}
