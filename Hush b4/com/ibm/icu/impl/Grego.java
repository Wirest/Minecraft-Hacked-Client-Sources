// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

public class Grego
{
    public static final long MIN_MILLIS = -184303902528000000L;
    public static final long MAX_MILLIS = 183882168921600000L;
    public static final int MILLIS_PER_SECOND = 1000;
    public static final int MILLIS_PER_MINUTE = 60000;
    public static final int MILLIS_PER_HOUR = 3600000;
    public static final int MILLIS_PER_DAY = 86400000;
    private static final int JULIAN_1_CE = 1721426;
    private static final int JULIAN_1970_CE = 2440588;
    private static final int[] MONTH_LENGTH;
    private static final int[] DAYS_BEFORE;
    
    public static final boolean isLeapYear(final int year) {
        return (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
    }
    
    public static final int monthLength(final int year, final int month) {
        return Grego.MONTH_LENGTH[month + (isLeapYear(year) ? 12 : 0)];
    }
    
    public static final int previousMonthLength(final int year, final int month) {
        return (month > 0) ? monthLength(year, month - 1) : 31;
    }
    
    public static long fieldsToDay(final int year, final int month, final int dom) {
        final int y = year - 1;
        final long julian = 365 * y + floorDivide(y, 4L) + 1721423L + floorDivide(y, 400L) - floorDivide(y, 100L) + 2L + Grego.DAYS_BEFORE[month + (isLeapYear(year) ? 12 : 0)] + dom;
        return julian - 2440588L;
    }
    
    public static int dayOfWeek(final long day) {
        final long[] remainder = { 0L };
        floorDivide(day + 5L, 7L, remainder);
        int dayOfWeek = (int)remainder[0];
        dayOfWeek = ((dayOfWeek == 0) ? 7 : dayOfWeek);
        return dayOfWeek;
    }
    
    public static int[] dayToFields(long day, int[] fields) {
        if (fields == null || fields.length < 5) {
            fields = new int[5];
        }
        day += 719162L;
        final long[] rem = { 0L };
        final long n400 = floorDivide(day, 146097L, rem);
        final long n401 = floorDivide(rem[0], 36524L, rem);
        final long n402 = floorDivide(rem[0], 1461L, rem);
        final long n403 = floorDivide(rem[0], 365L, rem);
        int year = (int)(400L * n400 + 100L * n401 + 4L * n402 + n403);
        int dayOfYear = (int)rem[0];
        if (n401 == 4L || n403 == 4L) {
            dayOfYear = 365;
        }
        else {
            ++year;
        }
        final boolean isLeap = isLeapYear(year);
        int correction = 0;
        final int march1 = isLeap ? 60 : 59;
        if (dayOfYear >= march1) {
            correction = (isLeap ? 1 : 2);
        }
        final int month = (12 * (dayOfYear + correction) + 6) / 367;
        final int dayOfMonth = dayOfYear - Grego.DAYS_BEFORE[isLeap ? (month + 12) : month] + 1;
        int dayOfWeek = (int)((day + 2L) % 7L);
        if (dayOfWeek < 1) {
            dayOfWeek += 7;
        }
        ++dayOfYear;
        fields[0] = year;
        fields[1] = month;
        fields[2] = dayOfMonth;
        fields[3] = dayOfWeek;
        fields[4] = dayOfYear;
        return fields;
    }
    
    public static int[] timeToFields(final long time, int[] fields) {
        if (fields == null || fields.length < 6) {
            fields = new int[6];
        }
        final long[] remainder = { 0L };
        final long day = floorDivide(time, 86400000L, remainder);
        dayToFields(day, fields);
        fields[5] = (int)remainder[0];
        return fields;
    }
    
    public static long floorDivide(final long numerator, final long denominator) {
        return (numerator >= 0L) ? (numerator / denominator) : ((numerator + 1L) / denominator - 1L);
    }
    
    private static long floorDivide(final long numerator, final long denominator, final long[] remainder) {
        if (numerator >= 0L) {
            remainder[0] = numerator % denominator;
            return numerator / denominator;
        }
        final long quotient = (numerator + 1L) / denominator - 1L;
        remainder[0] = numerator - quotient * denominator;
        return quotient;
    }
    
    public static int getDayOfWeekInMonth(final int year, final int month, final int dayOfMonth) {
        int weekInMonth = (dayOfMonth + 6) / 7;
        if (weekInMonth == 4) {
            if (dayOfMonth + 7 > monthLength(year, month)) {
                weekInMonth = -1;
            }
        }
        else if (weekInMonth == 5) {
            weekInMonth = -1;
        }
        return weekInMonth;
    }
    
    static {
        MONTH_LENGTH = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        DAYS_BEFORE = new int[] { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335 };
    }
}
