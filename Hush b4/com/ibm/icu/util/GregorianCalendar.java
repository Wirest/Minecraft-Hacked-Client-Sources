// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public class GregorianCalendar extends Calendar
{
    private static final long serialVersionUID = 9199388694351062137L;
    public static final int BC = 0;
    public static final int AD = 1;
    private static final int EPOCH_YEAR = 1970;
    private static final int[][] MONTH_COUNT;
    private static final int[][] LIMITS;
    private long gregorianCutover;
    private transient int cutoverJulianDay;
    private transient int gregorianCutoverYear;
    protected transient boolean isGregorian;
    protected transient boolean invertGregorian;
    
    @Override
    protected int handleGetLimit(final int field, final int limitType) {
        return GregorianCalendar.LIMITS[field][limitType];
    }
    
    public GregorianCalendar() {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public GregorianCalendar(final TimeZone zone) {
        this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public GregorianCalendar(final Locale aLocale) {
        this(TimeZone.getDefault(), aLocale);
    }
    
    public GregorianCalendar(final ULocale locale) {
        this(TimeZone.getDefault(), locale);
    }
    
    public GregorianCalendar(final TimeZone zone, final Locale aLocale) {
        super(zone, aLocale);
        this.gregorianCutover = -12219292800000L;
        this.cutoverJulianDay = 2299161;
        this.gregorianCutoverYear = 1582;
        this.setTimeInMillis(System.currentTimeMillis());
    }
    
    public GregorianCalendar(final TimeZone zone, final ULocale locale) {
        super(zone, locale);
        this.gregorianCutover = -12219292800000L;
        this.cutoverJulianDay = 2299161;
        this.gregorianCutoverYear = 1582;
        this.setTimeInMillis(System.currentTimeMillis());
    }
    
    public GregorianCalendar(final int year, final int month, final int date) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.gregorianCutover = -12219292800000L;
        this.cutoverJulianDay = 2299161;
        this.gregorianCutoverYear = 1582;
        this.set(0, 1);
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
    }
    
    public GregorianCalendar(final int year, final int month, final int date, final int hour, final int minute) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.gregorianCutover = -12219292800000L;
        this.cutoverJulianDay = 2299161;
        this.gregorianCutoverYear = 1582;
        this.set(0, 1);
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
    }
    
    public GregorianCalendar(final int year, final int month, final int date, final int hour, final int minute, final int second) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.gregorianCutover = -12219292800000L;
        this.cutoverJulianDay = 2299161;
        this.gregorianCutoverYear = 1582;
        this.set(0, 1);
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
        this.set(13, second);
    }
    
    public void setGregorianChange(final Date date) {
        this.gregorianCutover = date.getTime();
        if (this.gregorianCutover <= -184303902528000000L) {
            final int n = Integer.MIN_VALUE;
            this.cutoverJulianDay = n;
            this.gregorianCutoverYear = n;
        }
        else if (this.gregorianCutover >= 183882168921600000L) {
            final int n2 = Integer.MAX_VALUE;
            this.cutoverJulianDay = n2;
            this.gregorianCutoverYear = n2;
        }
        else {
            this.cutoverJulianDay = (int)Calendar.floorDivide(this.gregorianCutover, 86400000L);
            final GregorianCalendar cal = new GregorianCalendar(this.getTimeZone());
            cal.setTime(date);
            this.gregorianCutoverYear = cal.get(19);
        }
    }
    
    public final Date getGregorianChange() {
        return new Date(this.gregorianCutover);
    }
    
    public boolean isLeapYear(final int year) {
        return (year >= this.gregorianCutoverYear) ? (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) : (year % 4 == 0);
    }
    
    @Override
    public boolean isEquivalentTo(final Calendar other) {
        return super.isEquivalentTo(other) && this.gregorianCutover == ((GregorianCalendar)other).gregorianCutover;
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ (int)this.gregorianCutover;
    }
    
    @Override
    public void roll(final int field, final int amount) {
        switch (field) {
            case 3: {
                int woy = this.get(3);
                final int isoYear = this.get(17);
                int isoDoy = this.internalGet(6);
                if (this.internalGet(2) == 0) {
                    if (woy >= 52) {
                        isoDoy += this.handleGetYearLength(isoYear);
                    }
                }
                else if (woy == 1) {
                    isoDoy -= this.handleGetYearLength(isoYear - 1);
                }
                woy += amount;
                if (woy < 1 || woy > 52) {
                    int lastDoy = this.handleGetYearLength(isoYear);
                    int lastRelDow = (lastDoy - isoDoy + this.internalGet(7) - this.getFirstDayOfWeek()) % 7;
                    if (lastRelDow < 0) {
                        lastRelDow += 7;
                    }
                    if (6 - lastRelDow >= this.getMinimalDaysInFirstWeek()) {
                        lastDoy -= 7;
                    }
                    final int lastWoy = this.weekNumber(lastDoy, lastRelDow + 1);
                    woy = (woy + lastWoy - 1) % lastWoy + 1;
                }
                this.set(3, woy);
                this.set(1, isoYear);
            }
            default: {
                super.roll(field, amount);
            }
        }
    }
    
    @Override
    public int getActualMinimum(final int field) {
        return this.getMinimum(field);
    }
    
    @Override
    public int getActualMaximum(final int field) {
        switch (field) {
            case 1: {
                final Calendar cal = (Calendar)this.clone();
                cal.setLenient(true);
                final int era = cal.get(0);
                final Date d = cal.getTime();
                int lowGood = GregorianCalendar.LIMITS[1][1];
                int highBad = GregorianCalendar.LIMITS[1][2] + 1;
                while (lowGood + 1 < highBad) {
                    final int y = (lowGood + highBad) / 2;
                    cal.set(1, y);
                    if (cal.get(1) == y && cal.get(0) == era) {
                        lowGood = y;
                    }
                    else {
                        highBad = y;
                        cal.setTime(d);
                    }
                }
                return lowGood;
            }
            default: {
                return super.getActualMaximum(field);
            }
        }
    }
    
    boolean inDaylightTime() {
        if (!this.getTimeZone().useDaylightTime()) {
            return false;
        }
        this.complete();
        return this.internalGet(16) != 0;
    }
    
    @Override
    protected int handleGetMonthLength(int extendedYear, int month) {
        if (month < 0 || month > 11) {
            final int[] rem = { 0 };
            extendedYear += Calendar.floorDivide(month, 12, rem);
            month = rem[0];
        }
        return GregorianCalendar.MONTH_COUNT[month][this.isLeapYear(extendedYear)];
    }
    
    @Override
    protected int handleGetYearLength(final int eyear) {
        return this.isLeapYear(eyear) ? 366 : 365;
    }
    
    @Override
    protected void handleComputeFields(final int julianDay) {
        int month;
        int dayOfMonth;
        int dayOfYear;
        int eyear;
        if (julianDay >= this.cutoverJulianDay) {
            month = this.getGregorianMonth();
            dayOfMonth = this.getGregorianDayOfMonth();
            dayOfYear = this.getGregorianDayOfYear();
            eyear = this.getGregorianYear();
        }
        else {
            final long julianEpochDay = julianDay - 1721424;
            eyear = (int)Calendar.floorDivide(4L * julianEpochDay + 1464L, 1461L);
            final long january1 = 365 * (eyear - 1) + Calendar.floorDivide(eyear - 1, 4);
            dayOfYear = (int)(julianEpochDay - january1);
            final boolean isLeap = (eyear & 0x3) == 0x0;
            int correction = 0;
            final int march1 = isLeap ? 60 : 59;
            if (dayOfYear >= march1) {
                correction = (isLeap ? 1 : 2);
            }
            month = (12 * (dayOfYear + correction) + 6) / 367;
            dayOfMonth = dayOfYear - GregorianCalendar.MONTH_COUNT[month][isLeap ? 3 : 2] + 1;
            ++dayOfYear;
        }
        this.internalSet(2, month);
        this.internalSet(5, dayOfMonth);
        this.internalSet(6, dayOfYear);
        this.internalSet(19, eyear);
        int era = 1;
        if (eyear < 1) {
            era = 0;
            eyear = 1 - eyear;
        }
        this.internalSet(0, era);
        this.internalSet(1, eyear);
    }
    
    @Override
    protected int handleGetExtendedYear() {
        int year;
        if (this.newerField(19, 1) == 19) {
            year = this.internalGet(19, 1970);
        }
        else {
            final int era = this.internalGet(0, 1);
            if (era == 0) {
                year = 1 - this.internalGet(1, 1);
            }
            else {
                year = this.internalGet(1, 1970);
            }
        }
        return year;
    }
    
    @Override
    protected int handleComputeJulianDay(final int bestField) {
        this.invertGregorian = false;
        int jd = super.handleComputeJulianDay(bestField);
        if (this.isGregorian != jd >= this.cutoverJulianDay) {
            this.invertGregorian = true;
            jd = super.handleComputeJulianDay(bestField);
        }
        return jd;
    }
    
    @Override
    protected int handleComputeMonthStart(int eyear, int month, final boolean useMonth) {
        if (month < 0 || month > 11) {
            final int[] rem = { 0 };
            eyear += Calendar.floorDivide(month, 12, rem);
            month = rem[0];
        }
        boolean isLeap = eyear % 4 == 0;
        final int y = eyear - 1;
        int julianDay = 365 * y + Calendar.floorDivide(y, 4) + 1721423;
        this.isGregorian = (eyear >= this.gregorianCutoverYear);
        if (this.invertGregorian) {
            this.isGregorian = !this.isGregorian;
        }
        if (this.isGregorian) {
            isLeap = (isLeap && (eyear % 100 != 0 || eyear % 400 == 0));
            julianDay += Calendar.floorDivide(y, 400) - Calendar.floorDivide(y, 100) + 2;
        }
        if (month != 0) {
            julianDay += GregorianCalendar.MONTH_COUNT[month][isLeap ? 3 : 2];
        }
        return julianDay;
    }
    
    @Override
    public String getType() {
        return "gregorian";
    }
    
    static {
        MONTH_COUNT = new int[][] { { 31, 31, 0, 0 }, { 28, 29, 31, 31 }, { 31, 31, 59, 60 }, { 30, 30, 90, 91 }, { 31, 31, 120, 121 }, { 30, 30, 151, 152 }, { 31, 31, 181, 182 }, { 31, 31, 212, 213 }, { 30, 30, 243, 244 }, { 31, 31, 273, 274 }, { 30, 30, 304, 305 }, { 31, 31, 334, 335 } };
        LIMITS = new int[][] { { 0, 0, 1, 1 }, { 1, 1, 5828963, 5838270 }, { 0, 0, 11, 11 }, { 1, 1, 52, 53 }, new int[0], { 1, 1, 28, 31 }, { 1, 1, 365, 366 }, new int[0], { -1, -1, 4, 5 }, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { -5838270, -5838270, 5828964, 5838271 }, new int[0], { -5838269, -5838269, 5828963, 5838270 }, new int[0], new int[0] };
    }
}
