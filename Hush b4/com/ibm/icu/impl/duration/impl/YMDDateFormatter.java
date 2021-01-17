// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration.impl;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import com.ibm.icu.impl.duration.DateFormatter;

public class YMDDateFormatter implements DateFormatter
{
    private String requestedFields;
    private String localeName;
    private TimeZone timeZone;
    private SimpleDateFormat df;
    
    public YMDDateFormatter(final String requestedFields) {
        this(requestedFields, Locale.getDefault().toString(), TimeZone.getDefault());
    }
    
    public YMDDateFormatter(final String requestedFields, final String localeName, final TimeZone timeZone) {
        this.requestedFields = requestedFields;
        this.localeName = localeName;
        this.timeZone = timeZone;
        final Locale locale = Utils.localeFromString(localeName);
        (this.df = new SimpleDateFormat("yyyy/mm/dd", locale)).setTimeZone(timeZone);
    }
    
    public String format(final long date) {
        return this.format(new Date(date));
    }
    
    public String format(final Date date) {
        return this.df.format(date);
    }
    
    public DateFormatter withLocale(final String locName) {
        if (!locName.equals(this.localeName)) {
            return new YMDDateFormatter(this.requestedFields, locName, this.timeZone);
        }
        return this;
    }
    
    public DateFormatter withTimeZone(final TimeZone tz) {
        if (!tz.equals(this.timeZone)) {
            return new YMDDateFormatter(this.requestedFields, this.localeName, tz);
        }
        return this;
    }
}
