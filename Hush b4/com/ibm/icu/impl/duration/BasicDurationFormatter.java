// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

import java.util.Date;
import java.util.TimeZone;

class BasicDurationFormatter implements DurationFormatter
{
    private PeriodFormatter formatter;
    private PeriodBuilder builder;
    private DateFormatter fallback;
    private long fallbackLimit;
    private String localeName;
    private TimeZone timeZone;
    
    public BasicDurationFormatter(final PeriodFormatter formatter, final PeriodBuilder builder, final DateFormatter fallback, final long fallbackLimit) {
        this.formatter = formatter;
        this.builder = builder;
        this.fallback = fallback;
        this.fallbackLimit = ((fallbackLimit < 0L) ? 0L : fallbackLimit);
    }
    
    protected BasicDurationFormatter(final PeriodFormatter formatter, final PeriodBuilder builder, final DateFormatter fallback, final long fallbackLimit, final String localeName, final TimeZone timeZone) {
        this.formatter = formatter;
        this.builder = builder;
        this.fallback = fallback;
        this.fallbackLimit = fallbackLimit;
        this.localeName = localeName;
        this.timeZone = timeZone;
    }
    
    public String formatDurationFromNowTo(final Date targetDate) {
        final long now = System.currentTimeMillis();
        final long duration = now - targetDate.getTime();
        return this.formatDurationFrom(duration, now);
    }
    
    public String formatDurationFromNow(final long duration) {
        return this.formatDurationFrom(duration, System.currentTimeMillis());
    }
    
    public String formatDurationFrom(final long duration, final long referenceDate) {
        String s = this.doFallback(duration, referenceDate);
        if (s == null) {
            final Period p = this.doBuild(duration, referenceDate);
            s = this.doFormat(p);
        }
        return s;
    }
    
    public DurationFormatter withLocale(final String locName) {
        if (!locName.equals(this.localeName)) {
            final PeriodFormatter newFormatter = this.formatter.withLocale(locName);
            final PeriodBuilder newBuilder = this.builder.withLocale(locName);
            final DateFormatter newFallback = (this.fallback == null) ? null : this.fallback.withLocale(locName);
            return new BasicDurationFormatter(newFormatter, newBuilder, newFallback, this.fallbackLimit, locName, this.timeZone);
        }
        return this;
    }
    
    public DurationFormatter withTimeZone(final TimeZone tz) {
        if (!tz.equals(this.timeZone)) {
            final PeriodBuilder newBuilder = this.builder.withTimeZone(tz);
            final DateFormatter newFallback = (this.fallback == null) ? null : this.fallback.withTimeZone(tz);
            return new BasicDurationFormatter(this.formatter, newBuilder, newFallback, this.fallbackLimit, this.localeName, tz);
        }
        return this;
    }
    
    protected String doFallback(final long duration, final long referenceDate) {
        if (this.fallback != null && this.fallbackLimit > 0L && Math.abs(duration) >= this.fallbackLimit) {
            return this.fallback.format(referenceDate + duration);
        }
        return null;
    }
    
    protected Period doBuild(final long duration, final long referenceDate) {
        return this.builder.createWithReferenceDate(duration, referenceDate);
    }
    
    protected String doFormat(final Period period) {
        if (!period.isSet()) {
            throw new IllegalArgumentException("period is not set");
        }
        return this.formatter.format(period);
    }
}
