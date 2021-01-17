// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import com.ibm.icu.text.DateFormat;
import java.util.Locale;
import java.util.Date;
import com.ibm.icu.impl.CalendarCache;
import com.ibm.icu.impl.CalendarAstronomer;

public class ChineseCalendar extends Calendar
{
    private static final long serialVersionUID = 7312110751940929420L;
    private int epochYear;
    private TimeZone zoneAstro;
    private transient CalendarAstronomer astro;
    private transient CalendarCache winterSolsticeCache;
    private transient CalendarCache newYearCache;
    private transient boolean isLeapYear;
    private static final int[][] LIMITS;
    static final int[][][] CHINESE_DATE_PRECEDENCE;
    private static final int CHINESE_EPOCH_YEAR = -2636;
    private static final TimeZone CHINA_ZONE;
    private static final int SYNODIC_GAP = 25;
    
    public ChineseCalendar() {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT), -2636, ChineseCalendar.CHINA_ZONE);
    }
    
    public ChineseCalendar(final Date date) {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT), -2636, ChineseCalendar.CHINA_ZONE);
        this.setTime(date);
    }
    
    public ChineseCalendar(final int year, final int month, final int isLeapMonth, final int date) {
        this(year, month, isLeapMonth, date, 0, 0, 0);
    }
    
    public ChineseCalendar(final int year, final int month, final int isLeapMonth, final int date, final int hour, final int minute, final int second) {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT), -2636, ChineseCalendar.CHINA_ZONE);
        this.set(14, 0);
        this.set(1, year);
        this.set(2, month);
        this.set(22, isLeapMonth);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
        this.set(13, second);
    }
    
    public ChineseCalendar(final int era, final int year, final int month, final int isLeapMonth, final int date) {
        this(era, year, month, isLeapMonth, 0, 0, 0);
    }
    
    public ChineseCalendar(final int era, final int year, final int month, final int isLeapMonth, final int date, final int hour, final int minute, final int second) {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT), -2636, ChineseCalendar.CHINA_ZONE);
        this.set(14, 0);
        this.set(0, era);
        this.set(1, year);
        this.set(2, month);
        this.set(22, isLeapMonth);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
        this.set(13, second);
    }
    
    public ChineseCalendar(final Locale aLocale) {
        this(TimeZone.getDefault(), ULocale.forLocale(aLocale), -2636, ChineseCalendar.CHINA_ZONE);
    }
    
    public ChineseCalendar(final TimeZone zone) {
        this(zone, ULocale.getDefault(ULocale.Category.FORMAT), -2636, ChineseCalendar.CHINA_ZONE);
    }
    
    public ChineseCalendar(final TimeZone zone, final Locale aLocale) {
        this(zone, ULocale.forLocale(aLocale), -2636, ChineseCalendar.CHINA_ZONE);
    }
    
    public ChineseCalendar(final ULocale locale) {
        this(TimeZone.getDefault(), locale, -2636, ChineseCalendar.CHINA_ZONE);
    }
    
    public ChineseCalendar(final TimeZone zone, final ULocale locale) {
        this(zone, locale, -2636, ChineseCalendar.CHINA_ZONE);
    }
    
    @Deprecated
    protected ChineseCalendar(final TimeZone zone, final ULocale locale, final int epochYear, final TimeZone zoneAstroCalc) {
        super(zone, locale);
        this.astro = new CalendarAstronomer();
        this.winterSolsticeCache = new CalendarCache();
        this.newYearCache = new CalendarCache();
        this.epochYear = epochYear;
        this.zoneAstro = zoneAstroCalc;
        this.setTimeInMillis(System.currentTimeMillis());
    }
    
    @Override
    protected int handleGetLimit(final int field, final int limitType) {
        return ChineseCalendar.LIMITS[field][limitType];
    }
    
    @Override
    protected int handleGetExtendedYear() {
        int year;
        if (this.newestStamp(0, 1, 0) <= this.getStamp(19)) {
            year = this.internalGet(19, 1);
        }
        else {
            final int cycle = this.internalGet(0, 1) - 1;
            year = cycle * 60 + this.internalGet(1, 1) - (this.epochYear + 2636);
        }
        return year;
    }
    
    @Override
    protected int handleGetMonthLength(final int extendedYear, final int month) {
        final int thisStart = this.handleComputeMonthStart(extendedYear, month, true) - 2440588 + 1;
        final int nextStart = this.newMoonNear(thisStart + 25, true);
        return nextStart - thisStart;
    }
    
    @Override
    protected DateFormat handleGetDateFormat(final String pattern, final String override, final ULocale locale) {
        return super.handleGetDateFormat(pattern, override, locale);
    }
    
    @Override
    protected int[][][] getFieldResolutionTable() {
        return ChineseCalendar.CHINESE_DATE_PRECEDENCE;
    }
    
    private void offsetMonth(int newMoon, final int dom, final int delta) {
        newMoon += (int)(29.530588853 * (delta - 0.5));
        newMoon = this.newMoonNear(newMoon, true);
        final int jd = newMoon + 2440588 - 1 + dom;
        if (dom > 29) {
            this.set(20, jd - 1);
            this.complete();
            if (this.getActualMaximum(5) >= dom) {
                this.set(20, jd);
            }
        }
        else {
            this.set(20, jd);
        }
    }
    
    @Override
    public void add(final int field, final int amount) {
        switch (field) {
            case 2: {
                if (amount != 0) {
                    final int dom = this.get(5);
                    final int day = this.get(20) - 2440588;
                    final int moon = day - dom + 1;
                    this.offsetMonth(moon, dom, amount);
                    break;
                }
                break;
            }
            default: {
                super.add(field, amount);
                break;
            }
        }
    }
    
    @Override
    public void roll(final int field, final int amount) {
        switch (field) {
            case 2: {
                if (amount != 0) {
                    final int dom = this.get(5);
                    final int day = this.get(20) - 2440588;
                    final int moon = day - dom + 1;
                    int m = this.get(2);
                    if (this.isLeapYear) {
                        if (this.get(22) == 1) {
                            ++m;
                        }
                        else {
                            int moon2 = moon - (int)(29.530588853 * (m - 0.5));
                            moon2 = this.newMoonNear(moon2, true);
                            if (this.isLeapMonthBetween(moon2, moon)) {
                                ++m;
                            }
                        }
                    }
                    final int n = this.isLeapYear ? 13 : 12;
                    int newM = (m + amount) % n;
                    if (newM < 0) {
                        newM += n;
                    }
                    if (newM != m) {
                        this.offsetMonth(moon, dom, newM - m);
                    }
                    break;
                }
                break;
            }
            default: {
                super.roll(field, amount);
                break;
            }
        }
    }
    
    private final long daysToMillis(final int days) {
        final long millis = days * 86400000L;
        return millis - this.zoneAstro.getOffset(millis);
    }
    
    private final int millisToDays(final long millis) {
        return (int)Calendar.floorDivide(millis + this.zoneAstro.getOffset(millis), 86400000L);
    }
    
    private int winterSolstice(final int gyear) {
        long cacheValue = this.winterSolsticeCache.get(gyear);
        if (cacheValue == CalendarCache.EMPTY) {
            final long ms = this.daysToMillis(this.computeGregorianMonthStart(gyear, 11) + 1 - 2440588);
            this.astro.setTime(ms);
            final long solarLong = this.astro.getSunTime(CalendarAstronomer.WINTER_SOLSTICE, true);
            cacheValue = this.millisToDays(solarLong);
            this.winterSolsticeCache.put(gyear, cacheValue);
        }
        return (int)cacheValue;
    }
    
    private int newMoonNear(final int days, final boolean after) {
        this.astro.setTime(this.daysToMillis(days));
        final long newMoon = this.astro.getMoonTime(CalendarAstronomer.NEW_MOON, after);
        return this.millisToDays(newMoon);
    }
    
    private int synodicMonthsBetween(final int day1, final int day2) {
        return (int)Math.round((day2 - day1) / 29.530588853);
    }
    
    private int majorSolarTerm(final int days) {
        this.astro.setTime(this.daysToMillis(days));
        int term = ((int)Math.floor(6.0 * this.astro.getSunLongitude() / 3.141592653589793) + 2) % 12;
        if (term < 1) {
            term += 12;
        }
        return term;
    }
    
    private boolean hasNoMajorSolarTerm(final int newMoon) {
        final int mst = this.majorSolarTerm(newMoon);
        final int nmn = this.newMoonNear(newMoon + 25, true);
        final int mstt = this.majorSolarTerm(nmn);
        return mst == mstt;
    }
    
    private boolean isLeapMonthBetween(final int newMoon1, final int newMoon2) {
        if (this.synodicMonthsBetween(newMoon1, newMoon2) >= 50) {
            throw new IllegalArgumentException("isLeapMonthBetween(" + newMoon1 + ", " + newMoon2 + "): Invalid parameters");
        }
        return newMoon2 >= newMoon1 && (this.isLeapMonthBetween(newMoon1, this.newMoonNear(newMoon2 - 25, false)) || this.hasNoMajorSolarTerm(newMoon2));
    }
    
    @Override
    protected void handleComputeFields(final int julianDay) {
        this.computeChineseFields(julianDay - 2440588, this.getGregorianYear(), this.getGregorianMonth(), true);
    }
    
    private void computeChineseFields(final int days, final int gyear, final int gmonth, final boolean setAllFields) {
        int solsticeAfter = this.winterSolstice(gyear);
        int solsticeBefore;
        if (days < solsticeAfter) {
            solsticeBefore = this.winterSolstice(gyear - 1);
        }
        else {
            solsticeBefore = solsticeAfter;
            solsticeAfter = this.winterSolstice(gyear + 1);
        }
        final int firstMoon = this.newMoonNear(solsticeBefore + 1, true);
        final int lastMoon = this.newMoonNear(solsticeAfter + 1, false);
        final int thisMoon = this.newMoonNear(days + 1, false);
        this.isLeapYear = (this.synodicMonthsBetween(firstMoon, lastMoon) == 12);
        int month = this.synodicMonthsBetween(firstMoon, thisMoon);
        if (this.isLeapYear && this.isLeapMonthBetween(firstMoon, thisMoon)) {
            --month;
        }
        if (month < 1) {
            month += 12;
        }
        final boolean isLeapMonth = this.isLeapYear && this.hasNoMajorSolarTerm(thisMoon) && !this.isLeapMonthBetween(firstMoon, this.newMoonNear(thisMoon - 25, false));
        this.internalSet(2, month - 1);
        this.internalSet(22, isLeapMonth ? 1 : 0);
        if (setAllFields) {
            int extended_year = gyear - this.epochYear;
            int cycle_year = gyear + 2636;
            if (month < 11 || gmonth >= 6) {
                ++extended_year;
                ++cycle_year;
            }
            final int dayOfMonth = days - thisMoon + 1;
            this.internalSet(19, extended_year);
            final int[] yearOfCycle = { 0 };
            final int cycle = Calendar.floorDivide(cycle_year - 1, 60, yearOfCycle);
            this.internalSet(0, cycle + 1);
            this.internalSet(1, yearOfCycle[0] + 1);
            this.internalSet(5, dayOfMonth);
            int newYear = this.newYear(gyear);
            if (days < newYear) {
                newYear = this.newYear(gyear - 1);
            }
            this.internalSet(6, days - newYear + 1);
        }
    }
    
    private int newYear(final int gyear) {
        long cacheValue = this.newYearCache.get(gyear);
        if (cacheValue == CalendarCache.EMPTY) {
            final int solsticeBefore = this.winterSolstice(gyear - 1);
            final int solsticeAfter = this.winterSolstice(gyear);
            final int newMoon1 = this.newMoonNear(solsticeBefore + 1, true);
            final int newMoon2 = this.newMoonNear(newMoon1 + 25, true);
            final int newMoon3 = this.newMoonNear(solsticeAfter + 1, false);
            if (this.synodicMonthsBetween(newMoon1, newMoon3) == 12 && (this.hasNoMajorSolarTerm(newMoon1) || this.hasNoMajorSolarTerm(newMoon2))) {
                cacheValue = this.newMoonNear(newMoon2 + 25, true);
            }
            else {
                cacheValue = newMoon2;
            }
            this.newYearCache.put(gyear, cacheValue);
        }
        return (int)cacheValue;
    }
    
    @Override
    protected int handleComputeMonthStart(int eyear, int month, final boolean useMonth) {
        if (month < 0 || month > 11) {
            final int[] rem = { 0 };
            eyear += Calendar.floorDivide(month, 12, rem);
            month = rem[0];
        }
        final int gyear = eyear + this.epochYear - 1;
        final int newYear = this.newYear(gyear);
        int newMoon = this.newMoonNear(newYear + month * 29, true);
        int julianDay = newMoon + 2440588;
        final int saveMonth = this.internalGet(2);
        final int saveIsLeapMonth = this.internalGet(22);
        final int isLeapMonth = useMonth ? saveIsLeapMonth : 0;
        this.computeGregorianFields(julianDay);
        this.computeChineseFields(newMoon, this.getGregorianYear(), this.getGregorianMonth(), false);
        if (month != this.internalGet(2) || isLeapMonth != this.internalGet(22)) {
            newMoon = this.newMoonNear(newMoon + 25, true);
            julianDay = newMoon + 2440588;
        }
        this.internalSet(2, saveMonth);
        this.internalSet(22, saveIsLeapMonth);
        return julianDay - 1;
    }
    
    @Override
    public String getType() {
        return "chinese";
    }
    
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.epochYear = -2636;
        this.zoneAstro = ChineseCalendar.CHINA_ZONE;
        stream.defaultReadObject();
        this.astro = new CalendarAstronomer();
        this.winterSolsticeCache = new CalendarCache();
        this.newYearCache = new CalendarCache();
    }
    
    static {
        LIMITS = new int[][] { { 1, 1, 83333, 83333 }, { 1, 1, 60, 60 }, { 0, 0, 11, 11 }, { 1, 1, 50, 55 }, new int[0], { 1, 1, 29, 30 }, { 1, 1, 353, 385 }, new int[0], { -1, -1, 5, 5 }, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], new int[0], { 0, 0, 1, 1 } };
        CHINESE_DATE_PRECEDENCE = new int[][][] { { { 5 }, { 3, 7 }, { 4, 7 }, { 8, 7 }, { 3, 18 }, { 4, 18 }, { 8, 18 }, { 6 }, { 37, 22 } }, { { 3 }, { 4 }, { 8 }, { 40, 7 }, { 40, 18 } } };
        CHINA_ZONE = new SimpleTimeZone(28800000, "CHINA_ZONE").freeze();
    }
}
