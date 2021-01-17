// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.CharacterIterator;

abstract class DictionaryMatcher
{
    public abstract int matches(final CharacterIterator p0, final int p1, final int[] p2, final int[] p3, final int p4, final int[] p5);
    
    public int matches(final CharacterIterator text, final int maxLength, final int[] lengths, final int[] count, final int limit) {
        return this.matches(text, maxLength, lengths, count, limit, null);
    }
    
    public abstract int getType();
}
