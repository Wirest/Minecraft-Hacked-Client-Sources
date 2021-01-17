// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;

public class SimpleHoliday extends Holiday
{
    public static final SimpleHoliday NEW_YEARS_DAY;
    public static final SimpleHoliday EPIPHANY;
    public static final SimpleHoliday MAY_DAY;
    public static final SimpleHoliday ASSUMPTION;
    public static final SimpleHoliday ALL_SAINTS_DAY;
    public static final SimpleHoliday ALL_SOULS_DAY;
    public static final SimpleHoliday IMMACULATE_CONCEPTION;
    public static final SimpleHoliday CHRISTMAS_EVE;
    public static final SimpleHoliday CHRISTMAS;
    public static final SimpleHoliday BOXING_DAY;
    public static final SimpleHoliday ST_STEPHENS_DAY;
    public static final SimpleHoliday NEW_YEARS_EVE;
    
    public SimpleHoliday(final int month, final int dayOfMonth, final String name) {
        super(name, new SimpleDateRule(month, dayOfMonth));
    }
    
    public SimpleHoliday(final int month, final int dayOfMonth, final String name, final int startYear) {
        super(name, rangeRule(startYear, 0, new SimpleDateRule(month, dayOfMonth)));
    }
    
    public SimpleHoliday(final int month, final int dayOfMonth, final String name, final int startYear, final int endYear) {
        super(name, rangeRule(startYear, endYear, new SimpleDateRule(month, dayOfMonth)));
    }
    
    public SimpleHoliday(final int month, final int dayOfMonth, final int dayOfWeek, final String name) {
        super(name, new SimpleDateRule(month, dayOfMonth, (dayOfWeek > 0) ? dayOfWeek : (-dayOfWeek), dayOfWeek > 0));
    }
    
    public SimpleHoliday(final int month, final int dayOfMonth, final int dayOfWeek, final String name, final int startYear) {
        super(name, rangeRule(startYear, 0, new SimpleDateRule(month, dayOfMonth, (dayOfWeek > 0) ? dayOfWeek : (-dayOfWeek), dayOfWeek > 0)));
    }
    
    public SimpleHoliday(final int month, final int dayOfMonth, final int dayOfWeek, final String name, final int startYear, final int endYear) {
        super(name, rangeRule(startYear, endYear, new SimpleDateRule(month, dayOfMonth, (dayOfWeek > 0) ? dayOfWeek : (-dayOfWeek), dayOfWeek > 0)));
    }
    
    private static DateRule rangeRule(final int startYear, final int endYear, final DateRule rule) {
        if (startYear == 0 && endYear == 0) {
            return rule;
        }
        final RangeDateRule rangeRule = new RangeDateRule();
        if (startYear != 0) {
            final Calendar start = new GregorianCalendar(startYear, 0, 1);
            rangeRule.add(start.getTime(), rule);
        }
        else {
            rangeRule.add(rule);
        }
        if (endYear != 0) {
            final Date end = new GregorianCalendar(endYear, 11, 31).getTime();
            rangeRule.add(end, null);
        }
        return rangeRule;
    }
    
    static {
        NEW_YEARS_DAY = new SimpleHoliday(0, 1, "New Year's Day");
        EPIPHANY = new SimpleHoliday(0, 6, "Epiphany");
        MAY_DAY = new SimpleHoliday(4, 1, "May Day");
        ASSUMPTION = new SimpleHoliday(7, 15, "Assumption");
        ALL_SAINTS_DAY = new SimpleHoliday(10, 1, "All Saints' Day");
        ALL_SOULS_DAY = new SimpleHoliday(10, 2, "All Souls' Day");
        IMMACULATE_CONCEPTION = new SimpleHoliday(11, 8, "Immaculate Conception");
        CHRISTMAS_EVE = new SimpleHoliday(11, 24, "Christmas Eve");
        CHRISTMAS = new SimpleHoliday(11, 25, "Christmas");
        BOXING_DAY = new SimpleHoliday(11, 26, "Boxing Day");
        ST_STEPHENS_DAY = new SimpleHoliday(11, 26, "St. Stephen's Day");
        NEW_YEARS_EVE = new SimpleHoliday(11, 31, "New Year's Eve");
    }
}
