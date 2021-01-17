// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.impl.PeriodFormatterData;
import java.util.TimeZone;
import com.ibm.icu.impl.duration.impl.PeriodFormatterDataService;

class BasicPeriodBuilderFactory implements PeriodBuilderFactory
{
    private PeriodFormatterDataService ds;
    private Settings settings;
    private static final short allBits = 255;
    
    BasicPeriodBuilderFactory(final PeriodFormatterDataService ds) {
        this.ds = ds;
        this.settings = new Settings();
    }
    
    static long approximateDurationOf(final TimeUnit unit) {
        return TimeUnit.approxDurations[unit.ordinal];
    }
    
    public PeriodBuilderFactory setAvailableUnitRange(final TimeUnit minUnit, final TimeUnit maxUnit) {
        int uset = 0;
        for (int i = maxUnit.ordinal; i <= minUnit.ordinal; ++i) {
            uset |= 1 << i;
        }
        if (uset == 0) {
            throw new IllegalArgumentException("range " + minUnit + " to " + maxUnit + " is empty");
        }
        this.settings = this.settings.setUnits(uset);
        return this;
    }
    
    public PeriodBuilderFactory setUnitIsAvailable(final TimeUnit unit, final boolean available) {
        int uset = this.settings.uset;
        if (available) {
            uset |= 1 << unit.ordinal;
        }
        else {
            uset &= ~(1 << unit.ordinal);
        }
        this.settings = this.settings.setUnits(uset);
        return this;
    }
    
    public PeriodBuilderFactory setMaxLimit(final float maxLimit) {
        this.settings = this.settings.setMaxLimit(maxLimit);
        return this;
    }
    
    public PeriodBuilderFactory setMinLimit(final float minLimit) {
        this.settings = this.settings.setMinLimit(minLimit);
        return this;
    }
    
    public PeriodBuilderFactory setAllowZero(final boolean allow) {
        this.settings = this.settings.setAllowZero(allow);
        return this;
    }
    
    public PeriodBuilderFactory setWeeksAloneOnly(final boolean aloneOnly) {
        this.settings = this.settings.setWeeksAloneOnly(aloneOnly);
        return this;
    }
    
    public PeriodBuilderFactory setAllowMilliseconds(final boolean allow) {
        this.settings = this.settings.setAllowMilliseconds(allow);
        return this;
    }
    
    public PeriodBuilderFactory setLocale(final String localeName) {
        this.settings = this.settings.setLocale(localeName);
        return this;
    }
    
    public PeriodBuilderFactory setTimeZone(final TimeZone timeZone) {
        return this;
    }
    
    private Settings getSettings() {
        if (this.settings.effectiveSet() == 0) {
            return null;
        }
        return this.settings.setInUse();
    }
    
    public PeriodBuilder getFixedUnitBuilder(final TimeUnit unit) {
        return FixedUnitBuilder.get(unit, this.getSettings());
    }
    
    public PeriodBuilder getSingleUnitBuilder() {
        return SingleUnitBuilder.get(this.getSettings());
    }
    
    public PeriodBuilder getOneOrTwoUnitBuilder() {
        return OneOrTwoUnitBuilder.get(this.getSettings());
    }
    
    public PeriodBuilder getMultiUnitBuilder(final int periodCount) {
        return MultiUnitBuilder.get(periodCount, this.getSettings());
    }
    
    class Settings
    {
        boolean inUse;
        short uset;
        TimeUnit maxUnit;
        TimeUnit minUnit;
        int maxLimit;
        int minLimit;
        boolean allowZero;
        boolean weeksAloneOnly;
        boolean allowMillis;
        
        Settings() {
            this.uset = 255;
            this.maxUnit = TimeUnit.YEAR;
            this.minUnit = TimeUnit.MILLISECOND;
            this.allowZero = true;
            this.allowMillis = true;
        }
        
        Settings setUnits(final int uset) {
            if (this.uset == uset) {
                return this;
            }
            final Settings result = this.inUse ? this.copy() : this;
            result.uset = (short)uset;
            if ((uset & 0xFF) == 0xFF) {
                result.uset = 255;
                result.maxUnit = TimeUnit.YEAR;
                result.minUnit = TimeUnit.MILLISECOND;
            }
            else {
                int lastUnit = -1;
                for (int i = 0; i < TimeUnit.units.length; ++i) {
                    if (0x0 != (uset & 1 << i)) {
                        if (lastUnit == -1) {
                            result.maxUnit = TimeUnit.units[i];
                        }
                        lastUnit = i;
                    }
                }
                if (lastUnit == -1) {
                    final Settings settings = result;
                    final Settings settings2 = result;
                    final TimeUnit timeUnit = null;
                    settings2.maxUnit = timeUnit;
                    settings.minUnit = timeUnit;
                }
                else {
                    result.minUnit = TimeUnit.units[lastUnit];
                }
            }
            return result;
        }
        
        short effectiveSet() {
            if (this.allowMillis) {
                return this.uset;
            }
            return (short)(this.uset & ~(1 << TimeUnit.MILLISECOND.ordinal));
        }
        
        TimeUnit effectiveMinUnit() {
            if (this.allowMillis || this.minUnit != TimeUnit.MILLISECOND) {
                return this.minUnit;
            }
            int i = TimeUnit.units.length - 1;
            while (--i >= 0) {
                if (0x0 != (this.uset & 1 << i)) {
                    return TimeUnit.units[i];
                }
            }
            return TimeUnit.SECOND;
        }
        
        Settings setMaxLimit(final float maxLimit) {
            final int val = (maxLimit <= 0.0f) ? 0 : ((int)(maxLimit * 1000.0f));
            if (maxLimit == val) {
                return this;
            }
            final Settings result = this.inUse ? this.copy() : this;
            result.maxLimit = val;
            return result;
        }
        
        Settings setMinLimit(final float minLimit) {
            final int val = (minLimit <= 0.0f) ? 0 : ((int)(minLimit * 1000.0f));
            if (minLimit == val) {
                return this;
            }
            final Settings result = this.inUse ? this.copy() : this;
            result.minLimit = val;
            return result;
        }
        
        Settings setAllowZero(final boolean allow) {
            if (this.allowZero == allow) {
                return this;
            }
            final Settings result = this.inUse ? this.copy() : this;
            result.allowZero = allow;
            return result;
        }
        
        Settings setWeeksAloneOnly(final boolean weeksAlone) {
            if (this.weeksAloneOnly == weeksAlone) {
                return this;
            }
            final Settings result = this.inUse ? this.copy() : this;
            result.weeksAloneOnly = weeksAlone;
            return result;
        }
        
        Settings setAllowMilliseconds(final boolean allowMillis) {
            if (this.allowMillis == allowMillis) {
                return this;
            }
            final Settings result = this.inUse ? this.copy() : this;
            result.allowMillis = allowMillis;
            return result;
        }
        
        Settings setLocale(final String localeName) {
            final PeriodFormatterData data = BasicPeriodBuilderFactory.this.ds.get(localeName);
            return this.setAllowZero(data.allowZero()).setWeeksAloneOnly(data.weeksAloneOnly()).setAllowMilliseconds(data.useMilliseconds() != 1);
        }
        
        Settings setInUse() {
            this.inUse = true;
            return this;
        }
        
        Period createLimited(final long duration, final boolean inPast) {
            if (this.maxLimit > 0) {
                final long maxUnitDuration = BasicPeriodBuilderFactory.approximateDurationOf(this.maxUnit);
                if (duration * 1000L > this.maxLimit * maxUnitDuration) {
                    return Period.moreThan(this.maxLimit / 1000.0f, this.maxUnit).inPast(inPast);
                }
            }
            if (this.minLimit > 0) {
                final TimeUnit emu = this.effectiveMinUnit();
                final long emud = BasicPeriodBuilderFactory.approximateDurationOf(emu);
                final long eml = (emu == this.minUnit) ? this.minLimit : Math.max(1000L, BasicPeriodBuilderFactory.approximateDurationOf(this.minUnit) * this.minLimit / emud);
                if (duration * 1000L < eml * emud) {
                    return Period.lessThan(eml / 1000.0f, emu).inPast(inPast);
                }
            }
            return null;
        }
        
        public Settings copy() {
            final Settings result = new Settings();
            result.inUse = this.inUse;
            result.uset = this.uset;
            result.maxUnit = this.maxUnit;
            result.minUnit = this.minUnit;
            result.maxLimit = this.maxLimit;
            result.minLimit = this.minLimit;
            result.allowZero = this.allowZero;
            result.weeksAloneOnly = this.weeksAloneOnly;
            result.allowMillis = this.allowMillis;
            return result;
        }
    }
}
