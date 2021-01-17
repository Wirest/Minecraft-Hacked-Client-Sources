// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

class SingleUnitBuilder extends PeriodBuilderImpl
{
    SingleUnitBuilder(final BasicPeriodBuilderFactory.Settings settings) {
        super(settings);
    }
    
    public static SingleUnitBuilder get(final BasicPeriodBuilderFactory.Settings settings) {
        if (settings == null) {
            return null;
        }
        return new SingleUnitBuilder(settings);
    }
    
    @Override
    protected PeriodBuilder withSettings(final BasicPeriodBuilderFactory.Settings settingsToUse) {
        return get(settingsToUse);
    }
    
    @Override
    protected Period handleCreate(final long duration, final long referenceDate, final boolean inPast) {
        final short uset = this.settings.effectiveSet();
        for (int i = 0; i < TimeUnit.units.length; ++i) {
            if (0x0 != (uset & 1 << i)) {
                final TimeUnit unit = TimeUnit.units[i];
                final long unitDuration = this.approximateDurationOf(unit);
                if (duration >= unitDuration) {
                    return Period.at((float)(duration / (double)unitDuration), unit).inPast(inPast);
                }
            }
        }
        return null;
    }
}
