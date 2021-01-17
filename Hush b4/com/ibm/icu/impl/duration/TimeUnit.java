// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

public final class TimeUnit
{
    final String name;
    final byte ordinal;
    public static final TimeUnit YEAR;
    public static final TimeUnit MONTH;
    public static final TimeUnit WEEK;
    public static final TimeUnit DAY;
    public static final TimeUnit HOUR;
    public static final TimeUnit MINUTE;
    public static final TimeUnit SECOND;
    public static final TimeUnit MILLISECOND;
    static final TimeUnit[] units;
    static final long[] approxDurations;
    
    private TimeUnit(final String name, final int ordinal) {
        this.name = name;
        this.ordinal = (byte)ordinal;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public TimeUnit larger() {
        return (this.ordinal == 0) ? null : TimeUnit.units[this.ordinal - 1];
    }
    
    public TimeUnit smaller() {
        return (this.ordinal == TimeUnit.units.length - 1) ? null : TimeUnit.units[this.ordinal + 1];
    }
    
    public int ordinal() {
        return this.ordinal;
    }
    
    static {
        YEAR = new TimeUnit("year", 0);
        MONTH = new TimeUnit("month", 1);
        WEEK = new TimeUnit("week", 2);
        DAY = new TimeUnit("day", 3);
        HOUR = new TimeUnit("hour", 4);
        MINUTE = new TimeUnit("minute", 5);
        SECOND = new TimeUnit("second", 6);
        MILLISECOND = new TimeUnit("millisecond", 7);
        units = new TimeUnit[] { TimeUnit.YEAR, TimeUnit.MONTH, TimeUnit.WEEK, TimeUnit.DAY, TimeUnit.HOUR, TimeUnit.MINUTE, TimeUnit.SECOND, TimeUnit.MILLISECOND };
        approxDurations = new long[] { 31557600000L, 2630880000L, 604800000L, 86400000L, 3600000L, 60000L, 1000L, 1L };
    }
}
