// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import java.util.Stack;
import java.text.CharacterIterator;

final class UnhandledBreakEngine implements LanguageBreakEngine
{
    private final UnicodeSet[] fHandled;
    
    public UnhandledBreakEngine() {
        this.fHandled = new UnicodeSet[5];
        for (int i = 0; i < this.fHandled.length; ++i) {
            this.fHandled[i] = new UnicodeSet();
        }
    }
    
    public boolean handles(final int c, final int breakType) {
        return breakType >= 0 && breakType < this.fHandled.length && this.fHandled[breakType].contains(c);
    }
    
    public int findBreaks(final CharacterIterator text, final int startPos, final int endPos, final boolean reverse, final int breakType, final Stack<Integer> foundBreaks) {
        text.setIndex(endPos);
        return 0;
    }
    
    public synchronized void handleChar(final int c, final int breakType) {
        if (breakType >= 0 && breakType < this.fHandled.length && c != Integer.MAX_VALUE && !this.fHandled[breakType].contains(c)) {
            final int script = UCharacter.getIntPropertyValue(c, 4106);
            this.fHandled[breakType].applyIntPropertyValue(4106, script);
        }
    }
}
