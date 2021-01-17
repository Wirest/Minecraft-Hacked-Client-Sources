// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;
import com.ibm.icu.impl.CalendarCache;
import com.ibm.icu.impl.CalendarAstronomer;

public class IslamicCalendar extends Calendar
{
    private static final long serialVersionUID = -6253365474073869325L;
    public static final int MUHARRAM = 0;
    public static final int SAFAR = 1;
    public static final int RABI_1 = 2;
    public static final int RABI_2 = 3;
    public static final int JUMADA_1 = 4;
    public static final int JUMADA_2 = 5;
    public static final int RAJAB = 6;
    public static final int SHABAN = 7;
    public static final int RAMADAN = 8;
    public static final int SHAWWAL = 9;
    public static final int DHU_AL_QIDAH = 10;
    public static final int DHU_AL_HIJJAH = 11;
    private static final long HIJRA_MILLIS = -42521587200000L;
    private static final int[][] LIMITS;
    private static CalendarAstronomer astro;
    private static CalendarCache cache;
    private boolean civil;
    
    public IslamicCalendar() {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public IslamicCalendar(final TimeZone zone) {
        this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public IslamicCalendar(final Locale aLocale) {
        this(TimeZone.getDefault(), aLocale);
    }
    
    public IslamicCalendar(final ULocale locale) {
        this(TimeZone.getDefault(), locale);
    }
    
    public IslamicCalendar(final TimeZone zone, final Locale aLocale) {
        super(zone, aLocale);
        this.civil = true;
        this.setTimeInMillis(System.currentTimeMillis());
    }
    
    public IslamicCalendar(final TimeZone zone, final ULocale locale) {
        super(zone, locale);
        this.civil = true;
        this.setTimeInMillis(System.currentTimeMillis());
    }
    
    public IslamicCalendar(final Date date) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.civil = true;
        this.setTime(date);
    }
    
    public IslamicCalendar(final int year, final int month, final int date) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.civil = true;
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
    }
    
    public IslamicCalendar(final int year, final int month, final int date, final int hour, final int minute, final int second) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.civil = true;
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
        this.set(13, second);
    }
    
    public void setCivil(final boolean beCivil) {
        if (this.civil != beCivil) {
            final long m = this.getTimeInMillis();
            this.civil = beCivil;
            this.clear();
            this.setTimeInMillis(m);
        }
    }
    
    public boolean isCivil() {
        return this.civil;
    }
    
    @Override
    protected int handleGetLimit(final int field, final int limitType) {
        return IslamicCalendar.LIMITS[field][limitType];
    }
    
    private static final boolean civilLeapYear(final int year) {
        return (14 + 11 * year) % 30 < 11;
    }
    
    private long yearStart(final int year) {
        if (this.civil) {
            return (year - 1) * 354 + (long)Math.floor((3 + 11 * year) / 30.0);
        }
        return trueMonthStart(12 * (year - 1));
    }
    
    private long monthStart(final int year, final int month) {
        final int realYear = year + month / 12;
        final int realMonth = month % 12;
        if (this.civil) {
            return (long)Math.ceil(29.5 * realMonth) + (realYear - 1) * 354 + (long)Math.floor((3 + 11 * realYear) / 30.0);
        }
        return trueMonthStart(12 * (realYear - 1) + realMonth);
    }
    
    private static final long trueMonthStart(final long month) {
        long start = IslamicCalendar.cache.get(month);
        if (start == CalendarCache.EMPTY) {
            long origin = -42521587200000L + (long)Math.floor(month * 29.530588853) * 86400000L;
            double age = moonAge(origin);
            if (moonAge(origin) >= 0.0) {
                do {
                    origin -= 86400000L;
                    age = moonAge(origin);
                } while (age >= 0.0);
            }
            else {
                do {
                    origin += 86400000L;
                    age = moonAge(origin);
                } while (age < 0.0);
            }
            start = (origin + 42521587200000L) / 86400000L + 1L;
            IslamicCalendar.cache.put(month, start);
        }
        return start;
    }
    
    static final double moonAge(final long time) {
        double age = 0.0;
        synchronized (IslamicCalendar.astro) {
            IslamicCalendar.astro.setTime(time);
            age = IslamicCalendar.astro.getMoonAge();
        }
        age = age * 180.0 / 3.141592653589793;
        if (age > 180.0) {
            age -= 360.0;
        }
        return age;
    }
    
    @Override
    protected int handleGetMonthLength(final int extendedYear, int month) {
        int length = 0;
        if (this.civil) {
            length = 29 + (month + 1) % 2;
            if (month == 11 && civilLeapYear(extendedYear)) {
                ++length;
            }
        }
        else {
            month += 12 * (extendedYear - 1);
            length = (int)(trueMonthStart(month + 1) - trueMonthStart(month));
        }
        return length;
    }
    
    @Override
    protected int handleGetYearLength(final int extendedYear) {
        if (this.civil) {
            return 354 + (civilLeapYear(extendedYear) ? 1 : 0);
        }
        final int month = 12 * (extendedYear - 1);
        return (int)(trueMonthStart(month + 12) - trueMonthStart(month));
    }
    
    @Override
    protected int handleComputeMonthStart(final int eyear, final int month, final boolean useMonth) {
        return (int)this.monthStart(eyear, month) + 1948439;
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
    protected void handleComputeFields(final int julianDay) {
        final long days = julianDay - 1948440;
        int year;
        int month;
        if (this.civil) {
            year = (int)Math.floor((30L * days + 10646L) / 10631.0);
            month = (int)Math.ceil((days - 29L - this.yearStart(year)) / 29.5);
            month = Math.min(month, 11);
        }
        else {
            int months = (int)Math.floor(days / 29.530588853);
            long monthStart = (long)Math.floor(months * 29.530588853 - 1.0);
            if (days - monthStart >= 25L && moonAge(this.internalGetTimeInMillis()) > 0.0) {
                ++months;
            }
            while ((monthStart = trueMonthStart(months)) > days) {
                --months;
            }
            year = months / 12 + 1;
            month = months % 12;
        }
        final int dayOfMonth = (int)(days - this.monthStart(year, month)) + 1;
        final int dayOfYear = (int)(days - this.monthStart(year, 0) + 1L);
        this.internalSet(0, 0);
        this.internalSet(1, year);
        this.internalSet(19, year);
        this.internalSet(2, month);
        this.internalSet(5, dayOfMonth);
        this.internalSet(6, dayOfYear);
    }
    
    @Override
    public String getType() {
        if (this.civil) {
            return "islamic-civil";
        }
        return "islamic";
    }
    
    static {
        LIMITS = new int[][] { { 0, 0, 0, 0 }, { 1, 1, 5000000, 5000000 }, { 0, 0, 11, 11 }, { 1, 1, 50, 51 }, new int[0], { 1, 1, 29, 30 }, { 1, 1, 354, 355 }, new int[0], { -1, -1, 5, 5 }, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { 1, 1, 5000000, 5000000 }, new int[0], { 1, 1, 5000000, 5000000 }, new int[0], new int[0] };
        IslamicCalendar.astro = new CalendarAstronomer();
        IslamicCalendar.cache = new CalendarCache();
    }
}
