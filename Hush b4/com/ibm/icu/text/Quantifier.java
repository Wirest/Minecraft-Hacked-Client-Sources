// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;

class Quantifier implements UnicodeMatcher
{
    private UnicodeMatcher matcher;
    private int minCount;
    private int maxCount;
    public static final int MAX = Integer.MAX_VALUE;
    
    public Quantifier(final UnicodeMatcher theMatcher, final int theMinCount, final int theMaxCount) {
        if (theMatcher == null || theMinCount < 0 || theMaxCount < 0 || theMinCount > theMaxCount) {
            throw new IllegalArgumentException();
        }
        this.matcher = theMatcher;
        this.minCount = theMinCount;
        this.maxCount = theMaxCount;
    }
    
    public int matches(final Replaceable text, final int[] offset, final int limit, final boolean incremental) {
        final int start = offset[0];
        int count = 0;
        while (count < this.maxCount) {
            final int pos = offset[0];
            final int m = this.matcher.matches(text, offset, limit, incremental);
            if (m == 2) {
                ++count;
                if (pos == offset[0]) {
                    break;
                }
                continue;
            }
            else {
                if (incremental && m == 1) {
                    return 1;
                }
                break;
            }
        }
        if (incremental && offset[0] == limit) {
            return 1;
        }
        if (count >= this.minCount) {
            return 2;
        }
        offset[0] = start;
        return 0;
    }
    
    public String toPattern(final boolean escapeUnprintable) {
        final StringBuilder result = new StringBuilder();
        result.append(this.matcher.toPattern(escapeUnprintable));
        if (this.minCount == 0) {
            if (this.maxCount == 1) {
                return result.append('?').toString();
            }
            if (this.maxCount == Integer.MAX_VALUE) {
                return result.append('*').toString();
            }
        }
        else if (this.minCount == 1 && this.maxCount == Integer.MAX_VALUE) {
            return result.append('+').toString();
        }
        result.append('{');
        result.append(Utility.hex(this.minCount, 1));
        result.append(',');
        if (this.maxCount != Integer.MAX_VALUE) {
            result.append(Utility.hex(this.maxCount, 1));
        }
        result.append('}');
        return result.toString();
    }
    
    public boolean matchesIndexValue(final int v) {
        return this.minCount == 0 || this.matcher.matchesIndexValue(v);
    }
    
    public void addMatchSetTo(final UnicodeSet toUnionTo) {
        if (this.maxCount > 0) {
            this.matcher.addMatchSetTo(toUnionTo);
        }
    }
}
