// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

import java.util.Collection;

public interface PeriodFormatterService
{
    DurationFormatterFactory newDurationFormatterFactory();
    
    PeriodFormatterFactory newPeriodFormatterFactory();
    
    PeriodBuilderFactory newPeriodBuilderFactory();
    
    Collection<String> getAvailableLocaleNames();
}
