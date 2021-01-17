// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Date;
import java.text.ParsePosition;
import java.text.FieldPosition;
import com.ibm.icu.impl.duration.BasicDurationFormat;
import com.ibm.icu.util.ULocale;

public abstract class DurationFormat extends UFormat
{
    private static final long serialVersionUID = -2076961954727774282L;
    
    public static DurationFormat getInstance(final ULocale locale) {
        return BasicDurationFormat.getInstance(locale);
    }
    
    @Deprecated
    protected DurationFormat() {
    }
    
    @Deprecated
    protected DurationFormat(final ULocale locale) {
        this.setLocale(locale, locale);
    }
    
    @Override
    public abstract StringBuffer format(final Object p0, final StringBuffer p1, final FieldPosition p2);
    
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        throw new UnsupportedOperationException();
    }
    
    public abstract String formatDurationFromNowTo(final Date p0);
    
    public abstract String formatDurationFromNow(final long p0);
    
    public abstract String formatDurationFrom(final long p0, final long p1);
}
