// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.time;

import java.util.Locale;
import java.util.TimeZone;
import java.text.ParsePosition;
import java.text.ParseException;
import java.util.Date;

public interface DateParser
{
    Date parse(final String p0) throws ParseException;
    
    Date parse(final String p0, final ParsePosition p1);
    
    String getPattern();
    
    TimeZone getTimeZone();
    
    Locale getLocale();
    
    Object parseObject(final String p0) throws ParseException;
    
    Object parseObject(final String p0, final ParsePosition p1);
}
