// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

import java.util.TimeZone;

abstract class PeriodBuilderImpl implements PeriodBuilder
{
    protected BasicPeriodBuilderFactory.Settings settings;
    
    public Period create(final long duration) {
        return this.createWithReferenceDate(duration, System.currentTimeMillis());
    }
    
    public long approximateDurationOf(final TimeUnit unit) {
        return BasicPeriodBuilderFactory.approximateDurationOf(unit);
    }
    
    public Period createWithReferenceDate(long duration, final long referenceDate) {
        final boolean inPast = duration < 0L;
        if (inPast) {
            duration = -duration;
        }
        Period ts = this.settings.createLimited(duration, inPast);
        if (ts == null) {
            ts = this.handleCreate(duration, referenceDate, inPast);
            if (ts == null) {
                ts = Period.lessThan(1.0f, this.settings.effectiveMinUnit()).inPast(inPast);
            }
        }
        return ts;
    }
    
    public PeriodBuilder withTimeZone(final TimeZone timeZone) {
        return this;
    }
    
    public PeriodBuilder withLocale(final String localeName) {
        final BasicPeriodBuilderFactory.Settings newSettings = this.settings.setLocale(localeName);
        if (newSettings != this.settings) {
            return this.withSettings(newSettings);
        }
        return this;
    }
    
    protected abstract PeriodBuilder withSettings(final BasicPeriodBuilderFactory.Settings p0);
    
    protected abstract Period handleCreate(final long p0, final long p1, final boolean p2);
    
    protected PeriodBuilderImpl(final BasicPeriodBuilderFactory.Settings settings) {
        this.settings = settings;
    }
}
