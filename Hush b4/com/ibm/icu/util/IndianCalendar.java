// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public class IndianCalendar extends Calendar
{
    private static final long serialVersionUID = 3617859668165014834L;
    public static final int CHAITRA = 0;
    public static final int VAISAKHA = 1;
    public static final int JYAISTHA = 2;
    public static final int ASADHA = 3;
    public static final int SRAVANA = 4;
    public static final int BHADRA = 5;
    public static final int ASVINA = 6;
    public static final int KARTIKA = 7;
    public static final int AGRAHAYANA = 8;
    public static final int PAUSA = 9;
    public static final int MAGHA = 10;
    public static final int PHALGUNA = 11;
    public static final int IE = 0;
    private static final int INDIAN_ERA_START = 78;
    private static final int INDIAN_YEAR_START = 80;
    private static final int[][] LIMITS;
    
    public IndianCalendar() {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public IndianCalendar(final TimeZone zone) {
        this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public IndianCalendar(final Locale aLocale) {
        this(TimeZone.getDefault(), aLocale);
    }
    
    public IndianCalendar(final ULocale locale) {
        this(TimeZone.getDefault(), locale);
    }
    
    public IndianCalendar(final TimeZone zone, final Locale aLocale) {
        super(zone, aLocale);
        this.setTimeInMillis(System.currentTimeMillis());
    }
    
    public IndianCalendar(final TimeZone zone, final ULocale locale) {
        super(zone, locale);
        this.setTimeInMillis(System.currentTimeMillis());
    }
    
    public IndianCalendar(final Date date) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.setTime(date);
    }
    
    public IndianCalendar(final int year, final int month, final int date) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
    }
    
    public IndianCalendar(final int year, final int month, final int date, final int hour, final int minute, final int second) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
        this.set(13, second);
    }
    
    @Override
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
    protected int handleGetYearLength(final int extendedYear) {
        return super.handleGetYearLength(extendedYear);
    }
    
    @Override
    protected int handleGetMonthLength(int extendedYear, int month) {
        if (month < 0 || month > 11) {
            final int[] remainder = { 0 };
            extendedYear += Calendar.floorDivide(month, 12, remainder);
            month = remainder[0];
        }
        if (isGregorianLeap(extendedYear + 78) && month == 0) {
            return 31;
        }
        if (month >= 1 && month <= 5) {
            return 31;
        }
        return 30;
    }
    
    @Override
    protected void handleComputeFields(final int julianDay) {
        final int[] gregorianDay = jdToGregorian(julianDay);
        int IndianYear = gregorianDay[0] - 78;
        final double jdAtStartOfGregYear = gregorianToJD(gregorianDay[0], 1, 1);
        int yday = (int)(julianDay - jdAtStartOfGregYear);
        int leapMonth;
        if (yday < 80) {
            --IndianYear;
            leapMonth = (isGregorianLeap(gregorianDay[0] - 1) ? 31 : 30);
            yday += leapMonth + 155 + 90 + 10;
        }
        else {
            leapMonth = (isGregorianLeap(gregorianDay[0]) ? 31 : 30);
            yday -= 80;
        }
        int IndianMonth;
        int IndianDayOfMonth;
        if (yday < leapMonth) {
            IndianMonth = 0;
            IndianDayOfMonth = yday + 1;
        }
        else {
            int mday = yday - leapMonth;
            if (mday < 155) {
                IndianMonth = mday / 31 + 1;
                IndianDayOfMonth = mday % 31 + 1;
            }
            else {
                mday -= 155;
                IndianMonth = mday / 30 + 6;
                IndianDayOfMonth = mday % 30 + 1;
            }
        }
        this.internalSet(0, 0);
        this.internalSet(19, IndianYear);
        this.internalSet(1, IndianYear);
        this.internalSet(2, IndianMonth);
        this.internalSet(5, IndianDayOfMonth);
        this.internalSet(6, yday + 1);
    }
    
    @Override
    protected int handleGetLimit(final int field, final int limitType) {
        return IndianCalendar.LIMITS[field][limitType];
    }
    
    @Override
    protected int handleComputeMonthStart(int year, int month, final boolean useMonth) {
        if (month < 0 || month > 11) {
            year += month / 12;
            month %= 12;
        }
        int imonth;
        if (month == 12) {
            imonth = 1;
        }
        else {
            imonth = month + 1;
        }
        final double jd = IndianToJD(year, imonth, 1);
        return (int)jd;
    }
    
    private static double IndianToJD(final int year, final int month, final int date) {
        final int gyear = year + 78;
        int leapMonth;
        double start;
        if (isGregorianLeap(gyear)) {
            leapMonth = 31;
            start = gregorianToJD(gyear, 3, 21);
        }
        else {
            leapMonth = 30;
            start = gregorianToJD(gyear, 3, 22);
        }
        double jd;
        if (month == 1) {
            jd = start + (date - 1);
        }
        else {
            jd = start + leapMonth;
            int m = month - 2;
            m = Math.min(m, 5);
            jd += m * 31;
            if (month >= 8) {
                m = month - 7;
                jd += m * 30;
            }
            jd += date - 1;
        }
        return jd;
    }
    
    private static double gregorianToJD(final int year, final int month, final int date) {
        final double JULIAN_EPOCH = 1721425.5;
        final int y = year - 1;
        final int result = 365 * y + y / 4 - y / 100 + y / 400 + (367 * month - 362) / 12 + ((month <= 2) ? 0 : (isGregorianLeap(year) ? -1 : -2)) + date;
        return result - 1 + JULIAN_EPOCH;
    }
    
    private static int[] jdToGregorian(final double jd) {
        final double JULIAN_EPOCH = 1721425.5;
        final double wjd = Math.floor(jd - 0.5) + 0.5;
        final double depoch = wjd - JULIAN_EPOCH;
        final double quadricent = Math.floor(depoch / 146097.0);
        final double dqc = depoch % 146097.0;
        final double cent = Math.floor(dqc / 36524.0);
        final double dcent = dqc % 36524.0;
        final double quad = Math.floor(dcent / 1461.0);
        final double dquad = dcent % 1461.0;
        final double yindex = Math.floor(dquad / 365.0);
        int year = (int)(quadricent * 400.0 + cent * 100.0 + quad * 4.0 + yindex);
        if (cent != 4.0 && yindex != 4.0) {
            ++year;
        }
        final double yearday = wjd - gregorianToJD(year, 1, 1);
        final double leapadj = (wjd < gregorianToJD(year, 3, 1)) ? 0 : (isGregorianLeap(year) ? 1 : 2);
        final int month = (int)Math.floor(((yearday + leapadj) * 12.0 + 373.0) / 367.0);
        final int day = (int)(wjd - gregorianToJD(year, month, 1)) + 1;
        final int[] julianDate = { year, month, day };
        return julianDate;
    }
    
    private static boolean isGregorianLeap(final int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }
    
    @Override
    public String getType() {
        return "indian";
    }
    
    static {
        LIMITS = new int[][] { { 0, 0, 0, 0 }, { -5000000, -5000000, 5000000, 5000000 }, { 0, 0, 11, 11 }, { 1, 1, 52, 53 }, new int[0], { 1, 1, 30, 31 }, { 1, 1, 365, 366 }, new int[0], { -1, -1, 5, 5 }, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], new int[0] };
    }
}
