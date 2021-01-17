// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

import java.util.Locale;
import java.util.TimeZone;

class BasicDurationFormatterFactory implements DurationFormatterFactory
{
    private BasicPeriodFormatterService ps;
    private PeriodFormatter formatter;
    private PeriodBuilder builder;
    private DateFormatter fallback;
    private long fallbackLimit;
    private String localeName;
    private TimeZone timeZone;
    private BasicDurationFormatter f;
    
    BasicDurationFormatterFactory(final BasicPeriodFormatterService ps) {
        this.ps = ps;
        this.localeName = Locale.getDefault().toString();
        this.timeZone = TimeZone.getDefault();
    }
    
    public DurationFormatterFactory setPeriodFormatter(final PeriodFormatter formatter) {
        if (formatter != this.formatter) {
            this.formatter = formatter;
            this.reset();
        }
        return this;
    }
    
    public DurationFormatterFactory setPeriodBuilder(final PeriodBuilder builder) {
        if (builder != this.builder) {
            this.builder = builder;
            this.reset();
        }
        return this;
    }
    
    public DurationFormatterFactory setFallback(final DateFormatter fallback) {
        final boolean doReset = (fallback == null) ? (this.fallback != null) : (!fallback.equals(this.fallback));
        if (doReset) {
            this.fallback = fallback;
            this.reset();
        }
        return this;
    }
    
    public DurationFormatterFactory setFallbackLimit(long fallbackLimit) {
        if (fallbackLimit < 0L) {
            fallbackLimit = 0L;
        }
        if (fallbackLimit != this.fallbackLimit) {
            this.fallbackLimit = fallbackLimit;
            this.reset();
        }
        return this;
    }
    
    public DurationFormatterFactory setLocale(final String localeName) {
        if (!localeName.equals(this.localeName)) {
            this.localeName = localeName;
            if (this.builder != null) {
                this.builder = this.builder.withLocale(localeName);
            }
            if (this.formatter != null) {
                this.formatter = this.formatter.withLocale(localeName);
            }
            this.reset();
        }
        return this;
    }
    
    public DurationFormatterFactory setTimeZone(final TimeZone timeZone) {
        if (!timeZone.equals(this.timeZone)) {
            this.timeZone = timeZone;
            if (this.builder != null) {
                this.builder = this.builder.withTimeZone(timeZone);
            }
            this.reset();
        }
        return this;
    }
    
    public DurationFormatter getFormatter() {
        if (this.f == null) {
            if (this.fallback != null) {
                this.fallback = this.fallback.withLocale(this.localeName).withTimeZone(this.timeZone);
            }
            this.formatter = this.getPeriodFormatter();
            this.builder = this.getPeriodBuilder();
            this.f = this.createFormatter();
        }
        return this.f;
    }
    
    public PeriodFormatter getPeriodFormatter() {
        if (this.formatter == null) {
            this.formatter = this.ps.newPeriodFormatterFactory().setLocale(this.localeName).getFormatter();
        }
        return this.formatter;
    }
    
    public PeriodBuilder getPeriodBuilder() {
        if (this.builder == null) {
            this.builder = this.ps.newPeriodBuilderFactory().setLocale(this.localeName).setTimeZone(this.timeZone).getSingleUnitBuilder();
        }
        return this.builder;
    }
    
    public DateFormatter getFallback() {
        return this.fallback;
    }
    
    public long getFallbackLimit() {
        return (this.fallback == null) ? 0L : this.fallbackLimit;
    }
    
    public String getLocaleName() {
        return this.localeName;
    }
    
    public TimeZone getTimeZone() {
        return this.timeZone;
    }
    
    protected BasicDurationFormatter createFormatter() {
        return new BasicDurationFormatter(this.formatter, this.builder, this.fallback, this.fallbackLimit, this.localeName, this.timeZone);
    }
    
    protected void reset() {
        this.f = null;
    }
}
