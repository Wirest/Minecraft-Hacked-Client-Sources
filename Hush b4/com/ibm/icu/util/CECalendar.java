// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

abstract class CECalendar extends Calendar
{
    private static final long serialVersionUID = -999547623066414271L;
    private static final int[][] LIMITS;
    
    protected CECalendar() {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    protected CECalendar(final TimeZone zone) {
        this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    protected CECalendar(final Locale aLocale) {
        this(TimeZone.getDefault(), aLocale);
    }
    
    protected CECalendar(final ULocale locale) {
        this(TimeZone.getDefault(), locale);
    }
    
    protected CECalendar(final TimeZone zone, final Locale aLocale) {
        super(zone, aLocale);
        this.setTimeInMillis(System.currentTimeMillis());
    }
    
    protected CECalendar(final TimeZone zone, final ULocale locale) {
        super(zone, locale);
        this.setTimeInMillis(System.currentTimeMillis());
    }
    
    protected CECalendar(final int year, final int month, final int date) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.set(year, month, date);
    }
    
    protected CECalendar(final Date date) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.setTime(date);
    }
    
    protected CECalendar(final int year, final int month, final int date, final int hour, final int minute, final int second) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.set(year, month, date, hour, minute, second);
    }
    
    protected abstract int getJDEpochOffset();
    
    @Override
    protected int handleComputeMonthStart(final int eyear, final int emonth, final boolean useMonth) {
        return ceToJD(eyear, emonth, 0, this.getJDEpochOffset());
    }
    
    @Override
    protected int handleGetLimit(final int field, final int limitType) {
        return CECalendar.LIMITS[field][limitType];
    }
    
    @Override
    protected int handleGetMonthLength(final int extendedYear, final int month) {
        if ((month + 1) % 13 != 0) {
            return 30;
        }
        return extendedYear % 4 / 3 + 5;
    }
    
    public static int ceToJD(long year, int month, final int day, final int jdEpochOffset) {
        if (month >= 0) {
            year += month / 13;
            month %= 13;
        }
        else {
            ++month;
            year += month / 13 - 1;
            month = month % 13 + 12;
        }
        return (int)(jdEpochOffset + 365L * year + Calendar.floorDivide(year, 4L) + 30 * month + day - 1L);
    }
    
    public static void jdToCE(final int julianDay, final int jdEpochOffset, final int[] fields) {
        final int[] r4 = { 0 };
        final int c4 = Calendar.floorDivide(julianDay - jdEpochOffset, 1461, r4);
        fields[0] = 4 * c4 + (r4[0] / 365 - r4[0] / 1460);
        final int doy = (r4[0] == 1460) ? 365 : (r4[0] % 365);
        fields[1] = doy / 30;
        fields[2] = doy % 30 + 1;
    }
    
    static {
        LIMITS = new int[][] { { 0, 0, 1, 1 }, { 1, 1, 5000000, 5000000 }, { 0, 0, 12, 12 }, { 1, 1, 52, 53 }, new int[0], { 1, 1, 5, 30 }, { 1, 1, 365, 366 }, new int[0], { -1, -1, 1, 5 }, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], new int[0] };
    }
}
