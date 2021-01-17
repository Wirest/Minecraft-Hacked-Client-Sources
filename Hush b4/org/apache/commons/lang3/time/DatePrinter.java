// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.time;

import java.text.FieldPosition;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;

public interface DatePrinter
{
    String format(final long p0);
    
    String format(final Date p0);
    
    String format(final Calendar p0);
    
    StringBuffer format(final long p0, final StringBuffer p1);
    
    StringBuffer format(final Date p0, final StringBuffer p1);
    
    StringBuffer format(final Calendar p0, final StringBuffer p1);
    
    String getPattern();
    
    TimeZone getTimeZone();
    
    Locale getLocale();
    
    StringBuffer format(final Object p0, final StringBuffer p1, final FieldPosition p2);
}
