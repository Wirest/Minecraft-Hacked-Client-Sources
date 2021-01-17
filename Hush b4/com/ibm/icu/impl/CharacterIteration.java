// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import java.text.CharacterIterator;

public final class CharacterIteration
{
    public static final int DONE32 = Integer.MAX_VALUE;
    
    private CharacterIteration() {
    }
    
    public static int next32(final CharacterIterator ci) {
        int c = ci.current();
        if (c >= 55296 && c <= 56319) {
            c = ci.next();
            if (c < 56320 || c > 57343) {
                c = ci.previous();
            }
        }
        c = ci.next();
        if (c >= 55296) {
            c = nextTrail32(ci, c);
        }
        if (c >= 65536 && c != Integer.MAX_VALUE) {
            ci.previous();
        }
        return c;
    }
    
    public static int nextTrail32(final CharacterIterator ci, final int lead) {
        if (lead == 65535 && ci.getIndex() >= ci.getEndIndex()) {
            return Integer.MAX_VALUE;
        }
        int retVal;
        if ((retVal = lead) <= 56319) {
            final char cTrail = ci.next();
            if (UTF16.isTrailSurrogate(cTrail)) {
                retVal = (lead - 55296 << 10) + (cTrail - '\udc00') + 65536;
            }
            else {
                ci.previous();
            }
        }
        return retVal;
    }
    
    public static int previous32(final CharacterIterator ci) {
        if (ci.getIndex() <= ci.getBeginIndex()) {
            return Integer.MAX_VALUE;
        }
        int retVal;
        final char trail = (char)(retVal = ci.previous());
        if (UTF16.isTrailSurrogate(trail) && ci.getIndex() > ci.getBeginIndex()) {
            final char lead = ci.previous();
            if (UTF16.isLeadSurrogate(lead)) {
                retVal = (lead - '\ud800' << 10) + (trail - '\udc00') + 65536;
            }
            else {
                ci.next();
            }
        }
        return retVal;
    }
    
    public static int current32(final CharacterIterator ci) {
        int retVal;
        final char lead = (char)(retVal = ci.current());
        if (retVal < 55296) {
            return retVal;
        }
        if (UTF16.isLeadSurrogate(lead)) {
            final int trail = ci.next();
            ci.previous();
            if (UTF16.isTrailSurrogate((char)trail)) {
                retVal = (lead - '\ud800' << 10) + (trail - 56320) + 65536;
            }
        }
        else if (lead == '\uffff' && ci.getIndex() >= ci.getEndIndex()) {
            retVal = Integer.MAX_VALUE;
        }
        return retVal;
    }
}
