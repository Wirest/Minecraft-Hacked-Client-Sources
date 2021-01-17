// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public class PersianCalendar extends Calendar
{
    private static final long serialVersionUID = -6727306982975111643L;
    private static final int[][] MONTH_COUNT;
    private static final int PERSIAN_EPOCH = 1948320;
    private static final int[][] LIMITS;
    
    @Deprecated
    public PersianCalendar() {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    @Deprecated
    public PersianCalendar(final TimeZone zone) {
        this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    @Deprecated
    public PersianCalendar(final Locale aLocale) {
        this(TimeZone.getDefault(), aLocale);
    }
    
    @Deprecated
    public PersianCalendar(final ULocale locale) {
        this(TimeZone.getDefault(), locale);
    }
    
    @Deprecated
    public PersianCalendar(final TimeZone zone, final Locale aLocale) {
        super(zone, aLocale);
        this.setTimeInMillis(System.currentTimeMillis());
    }
    
    @Deprecated
    public PersianCalendar(final TimeZone zone, final ULocale locale) {
        super(zone, locale);
        this.setTimeInMillis(System.currentTimeMillis());
    }
    
    @Deprecated
    public PersianCalendar(final Date date) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.setTime(date);
    }
    
    @Deprecated
    public PersianCalendar(final int year, final int month, final int date) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
    }
    
    @Deprecated
    public PersianCalendar(final int year, final int month, final int date, final int hour, final int minute, final int second) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
        this.set(13, second);
    }
    
    @Override
    @Deprecated
    protected int handleGetLimit(final int field, final int limitType) {
        return PersianCalendar.LIMITS[field][limitType];
    }
    
    private static final boolean isLeapYear(final int year) {
        final int[] remainder = { 0 };
        Calendar.floorDivide(25 * year + 11, 33, remainder);
        return remainder[0] < 8;
    }
    
    @Override
    @Deprecated
    protected int handleGetMonthLength(int extendedYear, int month) {
        if (month < 0 || month > 11) {
            final int[] rem = { 0 };
            extendedYear += Calendar.floorDivide(month, 12, rem);
            month = rem[0];
        }
        return PersianCalendar.MONTH_COUNT[month][isLeapYear(extendedYear)];
    }
    
    @Override
    @Deprecated
    protected int handleGetYearLength(final int extendedYear) {
        return isLeapYear(extendedYear) ? 366 : 365;
    }
    
    @Override
    @Deprecated
    protected int handleComputeMonthStart(int eyear, int month, final boolean useMonth) {
        if (month < 0 || month > 11) {
            final int[] rem = { 0 };
            eyear += Calendar.floorDivide(month, 12, rem);
            month = rem[0];
        }
        int julianDay = 1948319 + 365 * (eyear - 1) + Calendar.floorDivide(8 * eyear + 21, 33);
        if (month != 0) {
            julianDay += PersianCalendar.MONTH_COUNT[month][2];
        }
        return julianDay;
    }
    
    @Override
    @Deprecated
    protected int handleGetExtendedYear() {
        int year;
        if (this.newerField(19, 1) == 19) {
            year = this.internalGet(19, 1);
        }
        else {
            year = this.internalGet(1, 1);
        }
        return year;
    }
    
    @Override
    @Deprecated
    protected void handleComputeFields(final int julianDay) {
        final long daysSinceEpoch = julianDay - 1948320;
        final int year = 1 + (int)Calendar.floorDivide(33L * daysSinceEpoch + 3L, 12053L);
        final long farvardin1 = 365 * (year - 1) + Calendar.floorDivide(8 * year + 21, 33);
        int dayOfYear = (int)(daysSinceEpoch - farvardin1);
        int month;
        if (dayOfYear < 216) {
            month = dayOfYear / 31;
        }
        else {
            month = (dayOfYear - 6) / 30;
        }
        final int dayOfMonth = dayOfYear - PersianCalendar.MONTH_COUNT[month][2] + 1;
        ++dayOfYear;
        this.internalSet(0, 0);
        this.internalSet(1, year);
        this.internalSet(19, year);
        this.internalSet(2, month);
        this.internalSet(5, dayOfMonth);
        this.internalSet(6, dayOfYear);
    }
    
    @Override
    @Deprecated
    public String getType() {
        return "persian";
    }
    
    static {
        MONTH_COUNT = new int[][] { { 31, 31, 0 }, { 31, 31, 31 }, { 31, 31, 62 }, { 31, 31, 93 }, { 31, 31, 124 }, { 31, 31, 155 }, { 30, 30, 186 }, { 30, 30, 216 }, { 30, 30, 246 }, { 30, 30, 276 }, { 30, 30, 306 }, { 29, 30, 336 } };
        LIMITS = new int[][] { { 0, 0, 0, 0 }, { -5000000, -5000000, 5000000, 5000000 }, { 0, 0, 11, 11 }, { 1, 1, 52, 53 }, new int[0], { 1, 1, 29, 31 }, { 1, 1, 365, 366 }, new int[0], { -1, -1, 5, 5 }, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], new int[0] };
    }
}
