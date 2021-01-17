// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

class MultiUnitBuilder extends PeriodBuilderImpl
{
    private int nPeriods;
    
    MultiUnitBuilder(final int nPeriods, final BasicPeriodBuilderFactory.Settings settings) {
        super(settings);
        this.nPeriods = nPeriods;
    }
    
    public static MultiUnitBuilder get(final int nPeriods, final BasicPeriodBuilderFactory.Settings settings) {
        if (nPeriods > 0 && settings != null) {
            return new MultiUnitBuilder(nPeriods, settings);
        }
        return null;
    }
    
    @Override
    protected PeriodBuilder withSettings(final BasicPeriodBuilderFactory.Settings settingsToUse) {
        return get(this.nPeriods, settingsToUse);
    }
    
    @Override
    protected Period handleCreate(long duration, final long referenceDate, final boolean inPast) {
        Period period = null;
        int n = 0;
        final short uset = this.settings.effectiveSet();
        for (int i = 0; i < TimeUnit.units.length; ++i) {
            if (0x0 != (uset & 1 << i)) {
                final TimeUnit unit = TimeUnit.units[i];
                if (n == this.nPeriods) {
                    break;
                }
                final long unitDuration = this.approximateDurationOf(unit);
                if (duration >= unitDuration || n > 0) {
                    ++n;
                    double count = duration / (double)unitDuration;
                    if (n < this.nPeriods) {
                        count = Math.floor(count);
                        duration -= (long)(count * unitDuration);
                    }
                    if (period == null) {
                        period = Period.at((float)count, unit).inPast(inPast);
                    }
                    else {
                        period = period.and((float)count, unit);
                    }
                }
            }
        }
        return period;
    }
}
