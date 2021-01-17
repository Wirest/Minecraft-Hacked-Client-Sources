// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

class OneOrTwoUnitBuilder extends PeriodBuilderImpl
{
    OneOrTwoUnitBuilder(final BasicPeriodBuilderFactory.Settings settings) {
        super(settings);
    }
    
    public static OneOrTwoUnitBuilder get(final BasicPeriodBuilderFactory.Settings settings) {
        if (settings == null) {
            return null;
        }
        return new OneOrTwoUnitBuilder(settings);
    }
    
    @Override
    protected PeriodBuilder withSettings(final BasicPeriodBuilderFactory.Settings settingsToUse) {
        return get(settingsToUse);
    }
    
    @Override
    protected Period handleCreate(long duration, final long referenceDate, final boolean inPast) {
        Period period = null;
        final short uset = this.settings.effectiveSet();
        for (int i = 0; i < TimeUnit.units.length; ++i) {
            if (0x0 != (uset & 1 << i)) {
                final TimeUnit unit = TimeUnit.units[i];
                final long unitDuration = this.approximateDurationOf(unit);
                if (duration >= unitDuration || period != null) {
                    final double count = duration / (double)unitDuration;
                    if (period == null) {
                        if (count >= 2.0) {
                            period = Period.at((float)count, unit);
                            break;
                        }
                        period = Period.at(1.0f, unit).inPast(inPast);
                        duration -= unitDuration;
                    }
                    else {
                        if (count >= 1.0) {
                            period = period.and((float)count, unit);
                            break;
                        }
                        break;
                    }
                }
            }
        }
        return period;
    }
}
