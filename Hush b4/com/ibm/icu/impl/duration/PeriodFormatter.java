// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

public interface PeriodFormatter
{
    String format(final Period p0);
    
    PeriodFormatter withLocale(final String p0);
}
