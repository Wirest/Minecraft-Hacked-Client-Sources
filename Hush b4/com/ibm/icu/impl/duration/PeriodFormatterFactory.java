// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

public interface PeriodFormatterFactory
{
    PeriodFormatterFactory setLocale(final String p0);
    
    PeriodFormatterFactory setDisplayLimit(final boolean p0);
    
    PeriodFormatterFactory setDisplayPastFuture(final boolean p0);
    
    PeriodFormatterFactory setSeparatorVariant(final int p0);
    
    PeriodFormatterFactory setUnitVariant(final int p0);
    
    PeriodFormatterFactory setCountVariant(final int p0);
    
    PeriodFormatter getFormatter();
}
