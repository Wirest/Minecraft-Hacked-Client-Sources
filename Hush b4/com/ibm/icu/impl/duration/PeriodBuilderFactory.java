// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

import java.util.TimeZone;

public interface PeriodBuilderFactory
{
    PeriodBuilderFactory setAvailableUnitRange(final TimeUnit p0, final TimeUnit p1);
    
    PeriodBuilderFactory setUnitIsAvailable(final TimeUnit p0, final boolean p1);
    
    PeriodBuilderFactory setMaxLimit(final float p0);
    
    PeriodBuilderFactory setMinLimit(final float p0);
    
    PeriodBuilderFactory setAllowZero(final boolean p0);
    
    PeriodBuilderFactory setWeeksAloneOnly(final boolean p0);
    
    PeriodBuilderFactory setAllowMilliseconds(final boolean p0);
    
    PeriodBuilderFactory setLocale(final String p0);
    
    PeriodBuilderFactory setTimeZone(final TimeZone p0);
    
    PeriodBuilder getFixedUnitBuilder(final TimeUnit p0);
    
    PeriodBuilder getSingleUnitBuilder();
    
    PeriodBuilder getOneOrTwoUnitBuilder();
    
    PeriodBuilder getMultiUnitBuilder(final int p0);
}
