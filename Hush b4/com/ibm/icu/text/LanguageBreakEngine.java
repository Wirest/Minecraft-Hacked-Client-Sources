// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Stack;
import java.text.CharacterIterator;

interface LanguageBreakEngine
{
    boolean handles(final int p0, final int p1);
    
    int findBreaks(final CharacterIterator p0, final int p1, final int p2, final boolean p3, final int p4, final Stack<Integer> p5);
}
