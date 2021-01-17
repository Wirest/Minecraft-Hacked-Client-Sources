// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.impl.PeriodFormatterData;

class BasicPeriodFormatter implements PeriodFormatter
{
    private BasicPeriodFormatterFactory factory;
    private String localeName;
    private PeriodFormatterData data;
    private BasicPeriodFormatterFactory.Customizations customs;
    
    BasicPeriodFormatter(final BasicPeriodFormatterFactory factory, final String localeName, final PeriodFormatterData data, final BasicPeriodFormatterFactory.Customizations customs) {
        this.factory = factory;
        this.localeName = localeName;
        this.data = data;
        this.customs = customs;
    }
    
    public String format(final Period period) {
        if (!period.isSet()) {
            throw new IllegalArgumentException("period is not set");
        }
        return this.format(period.timeLimit, period.inFuture, period.counts);
    }
    
    public PeriodFormatter withLocale(final String locName) {
        if (!this.localeName.equals(locName)) {
            final PeriodFormatterData newData = this.factory.getData(locName);
            return new BasicPeriodFormatter(this.factory, locName, newData, this.customs);
        }
        return this;
    }
    
    private String format(int tl, final boolean inFuture, final int[] counts) {
        int mask = 0;
        for (int i = 0; i < counts.length; ++i) {
            if (counts[i] > 0) {
                mask |= 1 << i;
            }
        }
        if (!this.data.allowZero()) {
            for (int i = 0, m = 1; i < counts.length; ++i, m <<= 1) {
                if ((mask & m) != 0x0 && counts[i] == 1) {
                    mask &= ~m;
                }
            }
            if (mask == 0) {
                return null;
            }
        }
        boolean forceD3Seconds = false;
        if (this.data.useMilliseconds() != 0 && (mask & 1 << TimeUnit.MILLISECOND.ordinal) != 0x0) {
            final int sx = TimeUnit.SECOND.ordinal;
            final int mx = TimeUnit.MILLISECOND.ordinal;
            final int sf = 1 << sx;
            final int mf = 1 << mx;
            switch (this.data.useMilliseconds()) {
                case 2: {
                    if ((mask & sf) != 0x0) {
                        final int n = sx;
                        counts[n] += (counts[mx] - 1) / 1000;
                        mask &= ~mf;
                        forceD3Seconds = true;
                        break;
                    }
                    break;
                }
                case 1: {
                    if ((mask & sf) == 0x0) {
                        mask |= sf;
                        counts[sx] = 1;
                    }
                    final int n2 = sx;
                    counts[n2] += (counts[mx] - 1) / 1000;
                    mask &= ~mf;
                    forceD3Seconds = true;
                    break;
                }
            }
        }
        int first = 0;
        int last = counts.length - 1;
        while (first < counts.length && (mask & 1 << first) == 0x0) {
            ++first;
        }
        while (last > first && (mask & 1 << last) == 0x0) {
            --last;
        }
        boolean isZero = true;
        for (int j = first; j <= last; ++j) {
            if ((mask & 1 << j) != 0x0 && counts[j] > 1) {
                isZero = false;
                break;
            }
        }
        final StringBuffer sb = new StringBuffer();
        if (!this.customs.displayLimit || isZero) {
            tl = 0;
        }
        int td;
        if (!this.customs.displayDirection || isZero) {
            td = 0;
        }
        else {
            td = (inFuture ? 2 : 1);
        }
        boolean useDigitPrefix = this.data.appendPrefix(tl, td, sb);
        final boolean multiple = first != last;
        boolean wasSkipped = true;
        boolean skipped = false;
        final boolean countSep = this.customs.separatorVariant != 0;
        int l;
        for (int k = l = first; k <= last; k = l) {
            if (skipped) {
                this.data.appendSkippedUnit(sb);
                skipped = false;
                wasSkipped = true;
            }
            while (++l < last && (mask & 1 << l) == 0x0) {
                skipped = true;
            }
            final TimeUnit unit = TimeUnit.units[k];
            final int count = counts[k] - 1;
            int cv = this.customs.countVariant;
            if (k == last) {
                if (forceD3Seconds) {
                    cv = 5;
                }
            }
            else {
                cv = 0;
            }
            final boolean isLast = k == last;
            final boolean mustSkip = this.data.appendUnit(unit, count, cv, this.customs.unitVariant, countSep, useDigitPrefix, multiple, isLast, wasSkipped, sb);
            skipped |= mustSkip;
            wasSkipped = false;
            if (this.customs.separatorVariant != 0 && l <= last) {
                final boolean afterFirst = k == first;
                final boolean beforeLast = l == last;
                final boolean fullSep = this.customs.separatorVariant == 2;
                useDigitPrefix = this.data.appendUnitSeparator(unit, fullSep, afterFirst, beforeLast, sb);
            }
            else {
                useDigitPrefix = false;
            }
        }
        this.data.appendSuffix(tl, td, sb);
        return sb.toString();
    }
}
