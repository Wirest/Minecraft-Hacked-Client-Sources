// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Stack;
import java.text.CharacterIterator;

abstract class DictionaryBreakEngine implements LanguageBreakEngine
{
    protected UnicodeSet fSet;
    private final int fTypes;
    
    public DictionaryBreakEngine(final int breakTypes) {
        this.fSet = new UnicodeSet();
        this.fTypes = breakTypes;
    }
    
    public boolean handles(final int c, final int breakType) {
        return breakType >= 0 && breakType < 32 && (1 << breakType & this.fTypes) != 0x0 && this.fSet.contains(c);
    }
    
    public int findBreaks(final CharacterIterator text_, final int startPos, final int endPos, final boolean reverse, final int breakType, final Stack<Integer> foundBreaks) {
        if (breakType < 0 || breakType >= 32 || (1 << breakType & this.fTypes) == 0x0) {
            return 0;
        }
        int result = 0;
        final UCharacterIterator text = UCharacterIterator.getInstance(text_);
        final int start = text.getIndex();
        int c = text.current();
        int current;
        int rangeStart;
        int rangeEnd;
        if (reverse) {
            for (boolean isDict = this.fSet.contains(c); (current = text.getIndex()) > startPos && isDict; isDict = this.fSet.contains(c)) {
                c = text.previous();
            }
            boolean isDict;
            rangeStart = ((current < startPos) ? startPos : (current + (isDict ? 0 : 1)));
            rangeEnd = start + 1;
        }
        else {
            while ((current = text.getIndex()) < endPos && this.fSet.contains(c)) {
                c = text.next();
            }
            rangeStart = start;
            rangeEnd = current;
        }
        result = this.divideUpDictionaryRange(text, rangeStart, rangeEnd, foundBreaks);
        text.setIndex(current);
        return result;
    }
    
    protected abstract int divideUpDictionaryRange(final UCharacterIterator p0, final int p1, final int p2, final Stack<Integer> p3);
}
