// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.io.IOException;

public class FilteredNormalizer2 extends Normalizer2
{
    private Normalizer2 norm2;
    private UnicodeSet set;
    
    public FilteredNormalizer2(final Normalizer2 n2, final UnicodeSet filterSet) {
        this.norm2 = n2;
        this.set = filterSet;
    }
    
    @Override
    public StringBuilder normalize(final CharSequence src, final StringBuilder dest) {
        if (dest == src) {
            throw new IllegalArgumentException();
        }
        dest.setLength(0);
        this.normalize(src, dest, UnicodeSet.SpanCondition.SIMPLE);
        return dest;
    }
    
    @Override
    public Appendable normalize(final CharSequence src, final Appendable dest) {
        if (dest == src) {
            throw new IllegalArgumentException();
        }
        return this.normalize(src, dest, UnicodeSet.SpanCondition.SIMPLE);
    }
    
    @Override
    public StringBuilder normalizeSecondAndAppend(final StringBuilder first, final CharSequence second) {
        return this.normalizeSecondAndAppend(first, second, true);
    }
    
    @Override
    public StringBuilder append(final StringBuilder first, final CharSequence second) {
        return this.normalizeSecondAndAppend(first, second, false);
    }
    
    @Override
    public String getDecomposition(final int c) {
        return this.set.contains(c) ? this.norm2.getDecomposition(c) : null;
    }
    
    @Override
    public String getRawDecomposition(final int c) {
        return this.set.contains(c) ? this.norm2.getRawDecomposition(c) : null;
    }
    
    @Override
    public int composePair(final int a, final int b) {
        return (this.set.contains(a) && this.set.contains(b)) ? this.norm2.composePair(a, b) : -1;
    }
    
    @Override
    public int getCombiningClass(final int c) {
        return this.set.contains(c) ? this.norm2.getCombiningClass(c) : 0;
    }
    
    @Override
    public boolean isNormalized(final CharSequence s) {
        UnicodeSet.SpanCondition spanCondition = UnicodeSet.SpanCondition.SIMPLE;
        int spanLimit;
        for (int prevSpanLimit = 0; prevSpanLimit < s.length(); prevSpanLimit = spanLimit) {
            spanLimit = this.set.span(s, prevSpanLimit, spanCondition);
            if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
                spanCondition = UnicodeSet.SpanCondition.SIMPLE;
            }
            else {
                if (!this.norm2.isNormalized(s.subSequence(prevSpanLimit, spanLimit))) {
                    return false;
                }
                spanCondition = UnicodeSet.SpanCondition.NOT_CONTAINED;
            }
        }
        return true;
    }
    
    @Override
    public Normalizer.QuickCheckResult quickCheck(final CharSequence s) {
        Normalizer.QuickCheckResult result = Normalizer.YES;
        UnicodeSet.SpanCondition spanCondition = UnicodeSet.SpanCondition.SIMPLE;
        int spanLimit;
        for (int prevSpanLimit = 0; prevSpanLimit < s.length(); prevSpanLimit = spanLimit) {
            spanLimit = this.set.span(s, prevSpanLimit, spanCondition);
            if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
                spanCondition = UnicodeSet.SpanCondition.SIMPLE;
            }
            else {
                final Normalizer.QuickCheckResult qcResult = this.norm2.quickCheck(s.subSequence(prevSpanLimit, spanLimit));
                if (qcResult == Normalizer.NO) {
                    return qcResult;
                }
                if (qcResult == Normalizer.MAYBE) {
                    result = qcResult;
                }
                spanCondition = UnicodeSet.SpanCondition.NOT_CONTAINED;
            }
        }
        return result;
    }
    
    @Override
    public int spanQuickCheckYes(final CharSequence s) {
        UnicodeSet.SpanCondition spanCondition = UnicodeSet.SpanCondition.SIMPLE;
        int spanLimit;
        for (int prevSpanLimit = 0; prevSpanLimit < s.length(); prevSpanLimit = spanLimit) {
            spanLimit = this.set.span(s, prevSpanLimit, spanCondition);
            if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
                spanCondition = UnicodeSet.SpanCondition.SIMPLE;
            }
            else {
                final int yesLimit = prevSpanLimit + this.norm2.spanQuickCheckYes(s.subSequence(prevSpanLimit, spanLimit));
                if (yesLimit < spanLimit) {
                    return yesLimit;
                }
                spanCondition = UnicodeSet.SpanCondition.NOT_CONTAINED;
            }
        }
        return s.length();
    }
    
    @Override
    public boolean hasBoundaryBefore(final int c) {
        return !this.set.contains(c) || this.norm2.hasBoundaryBefore(c);
    }
    
    @Override
    public boolean hasBoundaryAfter(final int c) {
        return !this.set.contains(c) || this.norm2.hasBoundaryAfter(c);
    }
    
    @Override
    public boolean isInert(final int c) {
        return !this.set.contains(c) || this.norm2.isInert(c);
    }
    
    private Appendable normalize(final CharSequence src, final Appendable dest, UnicodeSet.SpanCondition spanCondition) {
        final StringBuilder tempDest = new StringBuilder();
        try {
            int spanLimit;
            for (int prevSpanLimit = 0; prevSpanLimit < src.length(); prevSpanLimit = spanLimit) {
                spanLimit = this.set.span(src, prevSpanLimit, spanCondition);
                final int spanLength = spanLimit - prevSpanLimit;
                if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
                    if (spanLength != 0) {
                        dest.append(src, prevSpanLimit, spanLimit);
                    }
                    spanCondition = UnicodeSet.SpanCondition.SIMPLE;
                }
                else {
                    if (spanLength != 0) {
                        dest.append(this.norm2.normalize(src.subSequence(prevSpanLimit, spanLimit), tempDest));
                    }
                    spanCondition = UnicodeSet.SpanCondition.NOT_CONTAINED;
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dest;
    }
    
    private StringBuilder normalizeSecondAndAppend(final StringBuilder first, final CharSequence second, final boolean doNormalize) {
        if (first == second) {
            throw new IllegalArgumentException();
        }
        if (first.length() != 0) {
            final int prefixLimit = this.set.span(second, 0, UnicodeSet.SpanCondition.SIMPLE);
            if (prefixLimit != 0) {
                final CharSequence prefix = second.subSequence(0, prefixLimit);
                final int suffixStart = this.set.spanBack(first, Integer.MAX_VALUE, UnicodeSet.SpanCondition.SIMPLE);
                if (suffixStart == 0) {
                    if (doNormalize) {
                        this.norm2.normalizeSecondAndAppend(first, prefix);
                    }
                    else {
                        this.norm2.append(first, prefix);
                    }
                }
                else {
                    final StringBuilder middle = new StringBuilder(first.subSequence(suffixStart, first.length()));
                    if (doNormalize) {
                        this.norm2.normalizeSecondAndAppend(middle, prefix);
                    }
                    else {
                        this.norm2.append(middle, prefix);
                    }
                    first.delete(suffixStart, Integer.MAX_VALUE).append((CharSequence)middle);
                }
            }
            if (prefixLimit < second.length()) {
                final CharSequence rest = second.subSequence(prefixLimit, second.length());
                if (doNormalize) {
                    this.normalize(rest, first, UnicodeSet.SpanCondition.NOT_CONTAINED);
                }
                else {
                    first.append(rest);
                }
            }
            return first;
        }
        if (doNormalize) {
            return this.normalize(second, first);
        }
        return first.append(second);
    }
}
