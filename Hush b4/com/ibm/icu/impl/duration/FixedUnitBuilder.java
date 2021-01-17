// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

class FixedUnitBuilder extends PeriodBuilderImpl
{
    private TimeUnit unit;
    
    public static FixedUnitBuilder get(final TimeUnit unit, final BasicPeriodBuilderFactory.Settings settingsToUse) {
        if (settingsToUse != null && (settingsToUse.effectiveSet() & 1 << unit.ordinal) != 0x0) {
            return new FixedUnitBuilder(unit, settingsToUse);
        }
        return null;
    }
    
    FixedUnitBuilder(final TimeUnit unit, final BasicPeriodBuilderFactory.Settings settings) {
        super(settings);
        this.unit = unit;
    }
    
    @Override
    protected PeriodBuilder withSettings(final BasicPeriodBuilderFactory.Settings settingsToUse) {
        return get(this.unit, settingsToUse);
    }
    
    @Override
    protected Period handleCreate(final long duration, final long referenceDate, final boolean inPast) {
        if (this.unit == null) {
            return null;
        }
        final long unitDuration = this.approximateDurationOf(this.unit);
        return Period.at((float)(duration / (double)unitDuration), this.unit).inPast(inPast);
    }
}
