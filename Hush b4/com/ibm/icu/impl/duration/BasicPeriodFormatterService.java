// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

import java.util.Collection;
import com.ibm.icu.impl.duration.impl.ResourceBasedPeriodFormatterDataService;
import com.ibm.icu.impl.duration.impl.PeriodFormatterDataService;

public class BasicPeriodFormatterService implements PeriodFormatterService
{
    private static BasicPeriodFormatterService instance;
    private PeriodFormatterDataService ds;
    
    public static BasicPeriodFormatterService getInstance() {
        if (BasicPeriodFormatterService.instance == null) {
            final PeriodFormatterDataService ds = ResourceBasedPeriodFormatterDataService.getInstance();
            BasicPeriodFormatterService.instance = new BasicPeriodFormatterService(ds);
        }
        return BasicPeriodFormatterService.instance;
    }
    
    public BasicPeriodFormatterService(final PeriodFormatterDataService ds) {
        this.ds = ds;
    }
    
    public DurationFormatterFactory newDurationFormatterFactory() {
        return new BasicDurationFormatterFactory(this);
    }
    
    public PeriodFormatterFactory newPeriodFormatterFactory() {
        return new BasicPeriodFormatterFactory(this.ds);
    }
    
    public PeriodBuilderFactory newPeriodBuilderFactory() {
        return new BasicPeriodBuilderFactory(this.ds);
    }
    
    public Collection<String> getAvailableLocaleNames() {
        return this.ds.getAvailableLocales();
    }
}
