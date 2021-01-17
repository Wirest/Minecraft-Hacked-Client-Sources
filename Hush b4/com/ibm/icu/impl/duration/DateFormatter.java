// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

import java.util.TimeZone;
import java.util.Date;

public interface DateFormatter
{
    String format(final Date p0);
    
    String format(final long p0);
    
    DateFormatter withLocale(final String p0);
    
    DateFormatter withTimeZone(final TimeZone p0);
}
