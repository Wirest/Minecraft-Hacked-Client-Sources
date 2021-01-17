// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Set;
import com.ibm.icu.impl.SimpleCache;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.ibm.icu.impl.CalendarData;
import java.text.StringCharacterIterator;
import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.text.DateFormatSymbols;
import com.ibm.icu.text.DateFormat;
import java.util.ArrayList;
import java.util.MissingResourceException;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.CalendarUtil;
import java.util.Locale;
import com.ibm.icu.impl.ICUCache;
import java.util.Date;
import java.io.Serializable;

public abstract class Calendar implements Serializable, Cloneable, Comparable<Calendar>
{
    public static final int ERA = 0;
    public static final int YEAR = 1;
    public static final int MONTH = 2;
    public static final int WEEK_OF_YEAR = 3;
    public static final int WEEK_OF_MONTH = 4;
    public static final int DATE = 5;
    public static final int DAY_OF_MONTH = 5;
    public static final int DAY_OF_YEAR = 6;
    public static final int DAY_OF_WEEK = 7;
    public static final int DAY_OF_WEEK_IN_MONTH = 8;
    public static final int AM_PM = 9;
    public static final int HOUR = 10;
    public static final int HOUR_OF_DAY = 11;
    public static final int MINUTE = 12;
    public static final int SECOND = 13;
    public static final int MILLISECOND = 14;
    public static final int ZONE_OFFSET = 15;
    public static final int DST_OFFSET = 16;
    public static final int YEAR_WOY = 17;
    public static final int DOW_LOCAL = 18;
    public static final int EXTENDED_YEAR = 19;
    public static final int JULIAN_DAY = 20;
    public static final int MILLISECONDS_IN_DAY = 21;
    public static final int IS_LEAP_MONTH = 22;
    protected static final int BASE_FIELD_COUNT = 23;
    protected static final int MAX_FIELD_COUNT = 32;
    public static final int SUNDAY = 1;
    public static final int MONDAY = 2;
    public static final int TUESDAY = 3;
    public static final int WEDNESDAY = 4;
    public static final int THURSDAY = 5;
    public static final int FRIDAY = 6;
    public static final int SATURDAY = 7;
    public static final int JANUARY = 0;
    public static final int FEBRUARY = 1;
    public static final int MARCH = 2;
    public static final int APRIL = 3;
    public static final int MAY = 4;
    public static final int JUNE = 5;
    public static final int JULY = 6;
    public static final int AUGUST = 7;
    public static final int SEPTEMBER = 8;
    public static final int OCTOBER = 9;
    public static final int NOVEMBER = 10;
    public static final int DECEMBER = 11;
    public static final int UNDECIMBER = 12;
    public static final int AM = 0;
    public static final int PM = 1;
    public static final int WEEKDAY = 0;
    public static final int WEEKEND = 1;
    public static final int WEEKEND_ONSET = 2;
    public static final int WEEKEND_CEASE = 3;
    public static final int WALLTIME_LAST = 0;
    public static final int WALLTIME_FIRST = 1;
    public static final int WALLTIME_NEXT_VALID = 2;
    protected static final int ONE_SECOND = 1000;
    protected static final int ONE_MINUTE = 60000;
    protected static final int ONE_HOUR = 3600000;
    protected static final long ONE_DAY = 86400000L;
    protected static final long ONE_WEEK = 604800000L;
    protected static final int JAN_1_1_JULIAN_DAY = 1721426;
    protected static final int EPOCH_JULIAN_DAY = 2440588;
    protected static final int MIN_JULIAN = -2130706432;
    protected static final long MIN_MILLIS = -184303902528000000L;
    protected static final Date MIN_DATE;
    protected static final int MAX_JULIAN = 2130706432;
    protected static final long MAX_MILLIS = 183882168921600000L;
    protected static final Date MAX_DATE;
    private transient int[] fields;
    private transient int[] stamp;
    private long time;
    private transient boolean isTimeSet;
    private transient boolean areFieldsSet;
    private transient boolean areAllFieldsSet;
    private transient boolean areFieldsVirtuallySet;
    private boolean lenient;
    private TimeZone zone;
    private int firstDayOfWeek;
    private int minimalDaysInFirstWeek;
    private int weekendOnset;
    private int weekendOnsetMillis;
    private int weekendCease;
    private int weekendCeaseMillis;
    private int repeatedWallTime;
    private int skippedWallTime;
    private static ICUCache<ULocale, WeekData> cachedLocaleData;
    protected static final int UNSET = 0;
    protected static final int INTERNALLY_SET = 1;
    protected static final int MINIMUM_USER_STAMP = 2;
    private transient int nextStamp;
    private static final long serialVersionUID = 6222646104888790989L;
    private transient int internalSetMask;
    private transient int gregorianYear;
    private transient int gregorianMonth;
    private transient int gregorianDayOfYear;
    private transient int gregorianDayOfMonth;
    private static int STAMP_MAX;
    private static final String[] calTypes;
    private static final int CALTYPE_GREGORIAN = 0;
    private static final int CALTYPE_JAPANESE = 1;
    private static final int CALTYPE_BUDDHIST = 2;
    private static final int CALTYPE_ROC = 3;
    private static final int CALTYPE_PERSIAN = 4;
    private static final int CALTYPE_ISLAMIC_CIVIL = 5;
    private static final int CALTYPE_ISLAMIC = 6;
    private static final int CALTYPE_HEBREW = 7;
    private static final int CALTYPE_CHINESE = 8;
    private static final int CALTYPE_INDIAN = 9;
    private static final int CALTYPE_COPTIC = 10;
    private static final int CALTYPE_ETHIOPIC = 11;
    private static final int CALTYPE_ETHIOPIC_AMETE_ALEM = 12;
    private static final int CALTYPE_ISO8601 = 13;
    private static final int CALTYPE_DANGI = 14;
    private static final int CALTYPE_UNKNOWN = -1;
    private static CalendarShim shim;
    private static final ICUCache<String, PatternData> PATTERN_CACHE;
    private static final String[] DEFAULT_PATTERNS;
    private static final char QUOTE = '\'';
    private static final int FIELD_DIFF_MAX_INT = Integer.MAX_VALUE;
    private static final int[][] LIMITS;
    protected static final int MINIMUM = 0;
    protected static final int GREATEST_MINIMUM = 1;
    protected static final int LEAST_MAXIMUM = 2;
    protected static final int MAXIMUM = 3;
    protected static final int RESOLVE_REMAP = 32;
    static final int[][][] DATE_PRECEDENCE;
    static final int[][][] DOW_PRECEDENCE;
    private static final int[] FIND_ZONE_TRANSITION_TIME_UNITS;
    private static final int[][] GREGORIAN_MONTH_COUNT;
    private static final String[] FIELD_NAME;
    private ULocale validLocale;
    private ULocale actualLocale;
    
    protected Calendar() {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    protected Calendar(final TimeZone zone, final Locale aLocale) {
        this(zone, ULocale.forLocale(aLocale));
    }
    
    protected Calendar(final TimeZone zone, final ULocale locale) {
        this.lenient = true;
        this.repeatedWallTime = 0;
        this.skippedWallTime = 0;
        this.nextStamp = 2;
        this.zone = zone;
        this.setWeekData(locale);
        this.initInternal();
    }
    
    private void recalculateStamp() {
        this.nextStamp = 1;
        for (int j = 0; j < this.stamp.length; ++j) {
            int currentValue = Calendar.STAMP_MAX;
            int index = -1;
            for (int i = 0; i < this.stamp.length; ++i) {
                if (this.stamp[i] > this.nextStamp && this.stamp[i] < currentValue) {
                    currentValue = this.stamp[i];
                    index = i;
                }
            }
            if (index < 0) {
                break;
            }
            this.stamp[index] = ++this.nextStamp;
        }
        ++this.nextStamp;
    }
    
    private void initInternal() {
        this.fields = this.handleCreateFields();
        if (this.fields == null || this.fields.length < 23 || this.fields.length > 32) {
            throw new IllegalStateException("Invalid fields[]");
        }
        this.stamp = new int[this.fields.length];
        int mask = 4718695;
        for (int i = 23; i < this.fields.length; ++i) {
            mask |= 1 << i;
        }
        this.internalSetMask = mask;
    }
    
    public static synchronized Calendar getInstance() {
        return getInstanceInternal(null, null);
    }
    
    public static synchronized Calendar getInstance(final TimeZone zone) {
        return getInstanceInternal(zone, null);
    }
    
    public static synchronized Calendar getInstance(final Locale aLocale) {
        return getInstanceInternal(null, ULocale.forLocale(aLocale));
    }
    
    public static synchronized Calendar getInstance(final ULocale locale) {
        return getInstanceInternal(null, locale);
    }
    
    public static synchronized Calendar getInstance(final TimeZone zone, final Locale aLocale) {
        return getInstanceInternal(zone, ULocale.forLocale(aLocale));
    }
    
    public static synchronized Calendar getInstance(final TimeZone zone, final ULocale locale) {
        return getInstanceInternal(zone, locale);
    }
    
    private static Calendar getInstanceInternal(TimeZone tz, ULocale locale) {
        if (locale == null) {
            locale = ULocale.getDefault(ULocale.Category.FORMAT);
        }
        if (tz == null) {
            tz = TimeZone.getDefault();
        }
        final Calendar cal = getShim().createInstance(locale);
        cal.setTimeZone(tz);
        cal.setTimeInMillis(System.currentTimeMillis());
        return cal;
    }
    
    private static int getCalendarTypeForLocale(final ULocale l) {
        String s = CalendarUtil.getCalendarType(l);
        if (s != null) {
            s = s.toLowerCase(Locale.ENGLISH);
            for (int i = 0; i < Calendar.calTypes.length; ++i) {
                if (s.equals(Calendar.calTypes[i])) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static Locale[] getAvailableLocales() {
        if (Calendar.shim == null) {
            return ICUResourceBundle.getAvailableLocales();
        }
        return getShim().getAvailableLocales();
    }
    
    public static ULocale[] getAvailableULocales() {
        if (Calendar.shim == null) {
            return ICUResourceBundle.getAvailableULocales();
        }
        return getShim().getAvailableULocales();
    }
    
    private static CalendarShim getShim() {
        if (Calendar.shim == null) {
            try {
                final Class<?> cls = Class.forName("com.ibm.icu.util.CalendarServiceShim");
                Calendar.shim = (CalendarShim)cls.newInstance();
            }
            catch (MissingResourceException e) {
                throw e;
            }
            catch (Exception e2) {
                throw new RuntimeException(e2.getMessage());
            }
        }
        return Calendar.shim;
    }
    
    static Calendar createInstance(final ULocale locale) {
        Calendar cal = null;
        final TimeZone zone = TimeZone.getDefault();
        int calType = getCalendarTypeForLocale(locale);
        if (calType == -1) {
            calType = 0;
        }
        switch (calType) {
            case 0: {
                cal = new GregorianCalendar(zone, locale);
                break;
            }
            case 1: {
                cal = new JapaneseCalendar(zone, locale);
                break;
            }
            case 2: {
                cal = new BuddhistCalendar(zone, locale);
                break;
            }
            case 3: {
                cal = new TaiwanCalendar(zone, locale);
                break;
            }
            case 4: {
                cal = new PersianCalendar(zone, locale);
                break;
            }
            case 5: {
                cal = new IslamicCalendar(zone, locale);
                break;
            }
            case 6: {
                cal = new IslamicCalendar(zone, locale);
                ((IslamicCalendar)cal).setCivil(false);
                break;
            }
            case 7: {
                cal = new HebrewCalendar(zone, locale);
                break;
            }
            case 8: {
                cal = new ChineseCalendar(zone, locale);
                break;
            }
            case 9: {
                cal = new IndianCalendar(zone, locale);
                break;
            }
            case 10: {
                cal = new CopticCalendar(zone, locale);
                break;
            }
            case 11: {
                cal = new EthiopicCalendar(zone, locale);
                break;
            }
            case 12: {
                cal = new EthiopicCalendar(zone, locale);
                ((EthiopicCalendar)cal).setAmeteAlemEra(true);
                break;
            }
            case 14: {
                cal = new DangiCalendar(zone, locale);
                break;
            }
            case 13: {
                cal = new GregorianCalendar(zone, locale);
                cal.setFirstDayOfWeek(2);
                cal.setMinimalDaysInFirstWeek(4);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown calendar type");
            }
        }
        return cal;
    }
    
    static Object registerFactory(final CalendarFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("factory must not be null");
        }
        return getShim().registerFactory(factory);
    }
    
    static boolean unregister(final Object registryKey) {
        if (registryKey == null) {
            throw new IllegalArgumentException("registryKey must not be null");
        }
        return Calendar.shim != null && Calendar.shim.unregister(registryKey);
    }
    
    public static final String[] getKeywordValuesForLocale(final String key, final ULocale locale, final boolean commonlyUsed) {
        String prefRegion = locale.getCountry();
        if (prefRegion.length() == 0) {
            final ULocale loc = ULocale.addLikelySubtags(locale);
            prefRegion = loc.getCountry();
        }
        final ArrayList<String> values = new ArrayList<String>();
        final UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        final UResourceBundle calPref = rb.get("calendarPreferenceData");
        UResourceBundle order = null;
        try {
            order = calPref.get(prefRegion);
        }
        catch (MissingResourceException mre) {
            order = calPref.get("001");
        }
        final String[] caltypes = order.getStringArray();
        if (commonlyUsed) {
            return caltypes;
        }
        for (int i = 0; i < caltypes.length; ++i) {
            values.add(caltypes[i]);
        }
        for (int i = 0; i < Calendar.calTypes.length; ++i) {
            if (!values.contains(Calendar.calTypes[i])) {
                values.add(Calendar.calTypes[i]);
            }
        }
        return values.toArray(new String[values.size()]);
    }
    
    public final Date getTime() {
        return new Date(this.getTimeInMillis());
    }
    
    public final void setTime(final Date date) {
        this.setTimeInMillis(date.getTime());
    }
    
    public long getTimeInMillis() {
        if (!this.isTimeSet) {
            this.updateTime();
        }
        return this.time;
    }
    
    public void setTimeInMillis(long millis) {
        if (millis > 183882168921600000L) {
            if (!this.isLenient()) {
                throw new IllegalArgumentException("millis value greater than upper bounds for a Calendar : " + millis);
            }
            millis = 183882168921600000L;
        }
        else if (millis < -184303902528000000L) {
            if (!this.isLenient()) {
                throw new IllegalArgumentException("millis value less than lower bounds for a Calendar : " + millis);
            }
            millis = -184303902528000000L;
        }
        this.time = millis;
        final boolean b = false;
        this.areAllFieldsSet = b;
        this.areFieldsSet = b;
        final boolean b2 = true;
        this.areFieldsVirtuallySet = b2;
        this.isTimeSet = b2;
        for (int i = 0; i < this.fields.length; ++i) {
            this.fields[i] = (this.stamp[i] = 0);
        }
    }
    
    public final int get(final int field) {
        this.complete();
        return this.fields[field];
    }
    
    protected final int internalGet(final int field) {
        return this.fields[field];
    }
    
    protected final int internalGet(final int field, final int defaultValue) {
        return (this.stamp[field] > 0) ? this.fields[field] : defaultValue;
    }
    
    public final void set(final int field, final int value) {
        if (this.areFieldsVirtuallySet) {
            this.computeFields();
        }
        this.fields[field] = value;
        if (this.nextStamp == Calendar.STAMP_MAX) {
            this.recalculateStamp();
        }
        this.stamp[field] = this.nextStamp++;
        final boolean isTimeSet = false;
        this.areFieldsVirtuallySet = isTimeSet;
        this.areFieldsSet = isTimeSet;
        this.isTimeSet = isTimeSet;
    }
    
    public final void set(final int year, final int month, final int date) {
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
    }
    
    public final void set(final int year, final int month, final int date, final int hour, final int minute) {
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
    }
    
    public final void set(final int year, final int month, final int date, final int hour, final int minute, final int second) {
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
        this.set(13, second);
    }
    
    public final void clear() {
        for (int i = 0; i < this.fields.length; ++i) {
            this.fields[i] = (this.stamp[i] = 0);
        }
        final boolean b = false;
        this.areFieldsVirtuallySet = b;
        this.areAllFieldsSet = b;
        this.areFieldsSet = b;
        this.isTimeSet = b;
    }
    
    public final void clear(final int field) {
        if (this.areFieldsVirtuallySet) {
            this.computeFields();
        }
        this.fields[field] = 0;
        this.stamp[field] = 0;
        final boolean b = false;
        this.areFieldsVirtuallySet = b;
        this.areAllFieldsSet = b;
        this.areFieldsSet = b;
        this.isTimeSet = b;
    }
    
    public final boolean isSet(final int field) {
        return this.areFieldsVirtuallySet || this.stamp[field] != 0;
    }
    
    protected void complete() {
        if (!this.isTimeSet) {
            this.updateTime();
        }
        if (!this.areFieldsSet) {
            this.computeFields();
            this.areFieldsSet = true;
            this.areAllFieldsSet = true;
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Calendar that = (Calendar)obj;
        return this.isEquivalentTo(that) && this.getTimeInMillis() == that.getTime().getTime();
    }
    
    public boolean isEquivalentTo(final Calendar other) {
        return this.getClass() == other.getClass() && this.isLenient() == other.isLenient() && this.getFirstDayOfWeek() == other.getFirstDayOfWeek() && this.getMinimalDaysInFirstWeek() == other.getMinimalDaysInFirstWeek() && this.getTimeZone().equals(other.getTimeZone()) && this.getRepeatedWallTimeOption() == other.getRepeatedWallTimeOption() && this.getSkippedWallTimeOption() == other.getSkippedWallTimeOption();
    }
    
    @Override
    public int hashCode() {
        return (this.lenient ? 1 : 0) | this.firstDayOfWeek << 1 | this.minimalDaysInFirstWeek << 4 | this.repeatedWallTime << 7 | this.skippedWallTime << 9 | this.zone.hashCode() << 11;
    }
    
    private long compare(final Object that) {
        long thatMs;
        if (that instanceof Calendar) {
            thatMs = ((Calendar)that).getTimeInMillis();
        }
        else {
            if (!(that instanceof Date)) {
                throw new IllegalArgumentException(that + "is not a Calendar or Date");
            }
            thatMs = ((Date)that).getTime();
        }
        return this.getTimeInMillis() - thatMs;
    }
    
    public boolean before(final Object when) {
        return this.compare(when) < 0L;
    }
    
    public boolean after(final Object when) {
        return this.compare(when) > 0L;
    }
    
    public int getActualMaximum(final int field) {
        int result = 0;
        switch (field) {
            case 5: {
                final Calendar cal = (Calendar)this.clone();
                cal.setLenient(true);
                cal.prepareGetActual(field, false);
                result = this.handleGetMonthLength(cal.get(19), cal.get(2));
                break;
            }
            case 6: {
                final Calendar cal = (Calendar)this.clone();
                cal.setLenient(true);
                cal.prepareGetActual(field, false);
                result = this.handleGetYearLength(cal.get(19));
                break;
            }
            case 0:
            case 7:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 20:
            case 21: {
                result = this.getMaximum(field);
                break;
            }
            default: {
                result = this.getActualHelper(field, this.getLeastMaximum(field), this.getMaximum(field));
                break;
            }
        }
        return result;
    }
    
    public int getActualMinimum(final int field) {
        int result = 0;
        switch (field) {
            case 7:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 20:
            case 21: {
                result = this.getMinimum(field);
                break;
            }
            default: {
                result = this.getActualHelper(field, this.getGreatestMinimum(field), this.getMinimum(field));
                break;
            }
        }
        return result;
    }
    
    protected void prepareGetActual(final int field, final boolean isMinimum) {
        this.set(21, 0);
        switch (field) {
            case 1:
            case 19: {
                this.set(6, this.getGreatestMinimum(6));
                break;
            }
            case 17: {
                this.set(3, this.getGreatestMinimum(3));
                break;
            }
            case 2: {
                this.set(5, this.getGreatestMinimum(5));
                break;
            }
            case 8: {
                this.set(5, 1);
                this.set(7, this.get(7));
                break;
            }
            case 3:
            case 4: {
                int dow = this.firstDayOfWeek;
                if (isMinimum) {
                    dow = (dow + 6) % 7;
                    if (dow < 1) {
                        dow += 7;
                    }
                }
                this.set(7, dow);
                break;
            }
        }
        this.set(field, this.getGreatestMinimum(field));
    }
    
    private int getActualHelper(final int field, int startValue, final int endValue) {
        if (startValue == endValue) {
            return startValue;
        }
        final int delta = (endValue > startValue) ? 1 : -1;
        final Calendar work = (Calendar)this.clone();
        work.complete();
        work.setLenient(true);
        work.prepareGetActual(field, delta < 0);
        work.set(field, startValue);
        if (work.get(field) != startValue && field != 4 && delta > 0) {
            return startValue;
        }
        int result = startValue;
        do {
            startValue += delta;
            work.add(field, delta);
            if (work.get(field) != startValue) {
                break;
            }
        } while ((result = startValue) != endValue);
        return result;
    }
    
    public final void roll(final int field, final boolean up) {
        this.roll(field, up ? 1 : -1);
    }
    
    public void roll(final int field, int amount) {
        if (amount == 0) {
            return;
        }
        this.complete();
        switch (field) {
            case 0:
            case 5:
            case 9:
            case 12:
            case 13:
            case 14:
            case 21: {
                final int min = this.getActualMinimum(field);
                final int max = this.getActualMaximum(field);
                final int gap = max - min + 1;
                int value = this.internalGet(field) + amount;
                value = (value - min) % gap;
                if (value < 0) {
                    value += gap;
                }
                value += min;
                this.set(field, value);
            }
            case 10:
            case 11: {
                final long start = this.getTimeInMillis();
                final int oldHour = this.internalGet(field);
                final int max2 = this.getMaximum(field);
                int newHour = (oldHour + amount) % (max2 + 1);
                if (newHour < 0) {
                    newHour += max2 + 1;
                }
                this.setTimeInMillis(start + 3600000L * (newHour - (long)oldHour));
            }
            case 2: {
                final int max3 = this.getActualMaximum(2);
                int mon = (this.internalGet(2) + amount) % (max3 + 1);
                if (mon < 0) {
                    mon += max3 + 1;
                }
                this.set(2, mon);
                this.pinField(5);
            }
            case 1:
            case 17: {
                boolean era0WithYearsThatGoBackwards = false;
                final int era = this.get(0);
                if (era == 0) {
                    final String calType = this.getType();
                    if (calType.equals("gregorian") || calType.equals("roc") || calType.equals("coptic")) {
                        amount = -amount;
                        era0WithYearsThatGoBackwards = true;
                    }
                }
                int newYear = this.internalGet(field) + amount;
                if (era > 0 || newYear >= 1) {
                    final int maxYear = this.getActualMaximum(field);
                    if (maxYear < 32768) {
                        if (newYear < 1) {
                            newYear = maxYear - -newYear % maxYear;
                        }
                        else if (newYear > maxYear) {
                            newYear = (newYear - 1) % maxYear + 1;
                        }
                    }
                    else if (newYear < 1) {
                        newYear = 1;
                    }
                }
                else if (era0WithYearsThatGoBackwards) {
                    newYear = 1;
                }
                this.set(field, newYear);
                this.pinField(2);
                this.pinField(5);
            }
            case 19: {
                this.set(field, this.internalGet(field) + amount);
                this.pinField(2);
                this.pinField(5);
            }
            case 4: {
                int dow = this.internalGet(7) - this.getFirstDayOfWeek();
                if (dow < 0) {
                    dow += 7;
                }
                int fdm = (dow - this.internalGet(5) + 1) % 7;
                if (fdm < 0) {
                    fdm += 7;
                }
                int start2;
                if (7 - fdm < this.getMinimalDaysInFirstWeek()) {
                    start2 = 8 - fdm;
                }
                else {
                    start2 = 1 - fdm;
                }
                final int monthLen = this.getActualMaximum(5);
                final int ldm = (monthLen - this.internalGet(5) + dow) % 7;
                final int limit = monthLen + 7 - ldm;
                final int gap2 = limit - start2;
                int day_of_month = (this.internalGet(5) + amount * 7 - start2) % gap2;
                if (day_of_month < 0) {
                    day_of_month += gap2;
                }
                day_of_month += start2;
                if (day_of_month < 1) {
                    day_of_month = 1;
                }
                if (day_of_month > monthLen) {
                    day_of_month = monthLen;
                }
                this.set(5, day_of_month);
            }
            case 3: {
                int dow = this.internalGet(7) - this.getFirstDayOfWeek();
                if (dow < 0) {
                    dow += 7;
                }
                int fdy = (dow - this.internalGet(6) + 1) % 7;
                if (fdy < 0) {
                    fdy += 7;
                }
                int start2;
                if (7 - fdy < this.getMinimalDaysInFirstWeek()) {
                    start2 = 8 - fdy;
                }
                else {
                    start2 = 1 - fdy;
                }
                final int yearLen = this.getActualMaximum(6);
                final int ldy = (yearLen - this.internalGet(6) + dow) % 7;
                final int limit = yearLen + 7 - ldy;
                final int gap2 = limit - start2;
                int day_of_year = (this.internalGet(6) + amount * 7 - start2) % gap2;
                if (day_of_year < 0) {
                    day_of_year += gap2;
                }
                day_of_year += start2;
                if (day_of_year < 1) {
                    day_of_year = 1;
                }
                if (day_of_year > yearLen) {
                    day_of_year = yearLen;
                }
                this.set(6, day_of_year);
                this.clear(2);
            }
            case 6: {
                final long delta = amount * 86400000L;
                final long min2 = this.time - (this.internalGet(6) - 1) * 86400000L;
                final int yearLength = this.getActualMaximum(6);
                this.time = (this.time + delta - min2) % (yearLength * 86400000L);
                if (this.time < 0L) {
                    this.time += yearLength * 86400000L;
                }
                this.setTimeInMillis(this.time + min2);
            }
            case 7:
            case 18: {
                final long delta = amount * 86400000L;
                int leadDays = this.internalGet(field);
                leadDays -= ((field == 7) ? this.getFirstDayOfWeek() : 1);
                if (leadDays < 0) {
                    leadDays += 7;
                }
                final long min3 = this.time - leadDays * 86400000L;
                this.time = (this.time + delta - min3) % 604800000L;
                if (this.time < 0L) {
                    this.time += 604800000L;
                }
                this.setTimeInMillis(this.time + min3);
            }
            case 8: {
                final long delta = amount * 604800000L;
                final int preWeeks = (this.internalGet(5) - 1) / 7;
                final int postWeeks = (this.getActualMaximum(5) - this.internalGet(5)) / 7;
                final long min4 = this.time - preWeeks * 604800000L;
                final long gap3 = 604800000L * (preWeeks + postWeeks + 1);
                this.time = (this.time + delta - min4) % gap3;
                if (this.time < 0L) {
                    this.time += gap3;
                }
                this.setTimeInMillis(this.time + min4);
            }
            case 20: {
                this.set(field, this.internalGet(field) + amount);
            }
            default: {
                throw new IllegalArgumentException("Calendar.roll(" + this.fieldName(field) + ") not supported");
            }
        }
    }
    
    public void add(final int field, int amount) {
        if (amount == 0) {
            return;
        }
        long delta = amount;
        boolean keepHourInvariant = true;
        Label_0185: {
            switch (field) {
                case 0: {
                    this.set(field, this.get(field) + amount);
                    this.pinField(0);
                    return;
                }
                case 1:
                case 17: {
                    final int era = this.get(0);
                    if (era != 0) {
                        break Label_0185;
                    }
                    final String calType = this.getType();
                    if (calType.equals("gregorian") || calType.equals("roc") || calType.equals("coptic")) {
                        amount = -amount;
                    }
                    break Label_0185;
                }
                case 2:
                case 19: {
                    final boolean oldLenient = this.isLenient();
                    this.setLenient(true);
                    this.set(field, this.get(field) + amount);
                    this.pinField(5);
                    if (!oldLenient) {
                        this.complete();
                        this.setLenient(oldLenient);
                    }
                    return;
                }
                case 3:
                case 4:
                case 8: {
                    delta *= 604800000L;
                    break;
                }
                case 9: {
                    delta *= 43200000L;
                    break;
                }
                case 5:
                case 6:
                case 7:
                case 18:
                case 20: {
                    delta *= 86400000L;
                    break;
                }
                case 10:
                case 11: {
                    delta *= 3600000L;
                    keepHourInvariant = false;
                    break;
                }
                case 12: {
                    delta *= 60000L;
                    keepHourInvariant = false;
                    break;
                }
                case 13: {
                    delta *= 1000L;
                    keepHourInvariant = false;
                    break;
                }
                case 14:
                case 21: {
                    keepHourInvariant = false;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Calendar.add(" + this.fieldName(field) + ") not supported");
                }
            }
        }
        int prevOffset = 0;
        int hour = 0;
        if (keepHourInvariant) {
            prevOffset = this.get(16) + this.get(15);
            hour = this.internalGet(11);
        }
        this.setTimeInMillis(this.getTimeInMillis() + delta);
        if (keepHourInvariant) {
            final int newOffset = this.get(16) + this.get(15);
            if (newOffset != prevOffset) {
                final long adjAmount = (prevOffset - newOffset) % 86400000L;
                if (adjAmount != 0L) {
                    final long t = this.time;
                    this.setTimeInMillis(this.time + adjAmount);
                    if (this.get(11) != hour) {
                        this.setTimeInMillis(t);
                    }
                }
            }
        }
    }
    
    public String getDisplayName(final Locale loc) {
        return this.getClass().getName();
    }
    
    public String getDisplayName(final ULocale loc) {
        return this.getClass().getName();
    }
    
    public int compareTo(final Calendar that) {
        final long v = this.getTimeInMillis() - that.getTimeInMillis();
        return (v < 0L) ? -1 : ((v > 0L) ? 1 : 0);
    }
    
    public DateFormat getDateTimeFormat(final int dateStyle, final int timeStyle, final Locale loc) {
        return formatHelper(this, ULocale.forLocale(loc), dateStyle, timeStyle);
    }
    
    public DateFormat getDateTimeFormat(final int dateStyle, final int timeStyle, final ULocale loc) {
        return formatHelper(this, loc, dateStyle, timeStyle);
    }
    
    protected DateFormat handleGetDateFormat(final String pattern, final Locale locale) {
        return this.handleGetDateFormat(pattern, null, ULocale.forLocale(locale));
    }
    
    protected DateFormat handleGetDateFormat(final String pattern, final String override, final Locale locale) {
        return this.handleGetDateFormat(pattern, override, ULocale.forLocale(locale));
    }
    
    protected DateFormat handleGetDateFormat(final String pattern, final ULocale locale) {
        return this.handleGetDateFormat(pattern, null, locale);
    }
    
    protected DateFormat handleGetDateFormat(final String pattern, final String override, final ULocale locale) {
        final FormatConfiguration fmtConfig = new FormatConfiguration();
        fmtConfig.pattern = pattern;
        fmtConfig.override = override;
        fmtConfig.formatData = new DateFormatSymbols(this, locale);
        fmtConfig.loc = locale;
        fmtConfig.cal = this;
        return SimpleDateFormat.getInstance(fmtConfig);
    }
    
    private static DateFormat formatHelper(final Calendar cal, final ULocale loc, final int dateStyle, final int timeStyle) {
        final PatternData patternData = make(cal, loc);
        String override = null;
        String pattern = null;
        if (timeStyle >= 0 && dateStyle >= 0) {
            pattern = MessageFormat.format(patternData.getDateTimePattern(dateStyle), patternData.patterns[timeStyle], patternData.patterns[dateStyle + 4]);
            if (patternData.overrides != null) {
                final String dateOverride = patternData.overrides[dateStyle + 4];
                final String timeOverride = patternData.overrides[timeStyle];
                override = mergeOverrideStrings(patternData.patterns[dateStyle + 4], patternData.patterns[timeStyle], dateOverride, timeOverride);
            }
        }
        else if (timeStyle >= 0) {
            pattern = patternData.patterns[timeStyle];
            if (patternData.overrides != null) {
                override = patternData.overrides[timeStyle];
            }
        }
        else {
            if (dateStyle < 0) {
                throw new IllegalArgumentException("No date or time style specified");
            }
            pattern = patternData.patterns[dateStyle + 4];
            if (patternData.overrides != null) {
                override = patternData.overrides[dateStyle + 4];
            }
        }
        final DateFormat result = cal.handleGetDateFormat(pattern, override, loc);
        result.setCalendar(cal);
        return result;
    }
    
    @Deprecated
    public static String getDateTimePattern(final Calendar cal, final ULocale uLocale, final int dateStyle) {
        final PatternData patternData = make(cal, uLocale);
        return patternData.getDateTimePattern(dateStyle);
    }
    
    private static String mergeOverrideStrings(final String datePattern, final String timePattern, final String dateOverride, final String timeOverride) {
        if (dateOverride == null && timeOverride == null) {
            return null;
        }
        if (dateOverride == null) {
            return expandOverride(timePattern, timeOverride);
        }
        if (timeOverride == null) {
            return expandOverride(datePattern, dateOverride);
        }
        if (dateOverride.equals(timeOverride)) {
            return dateOverride;
        }
        return expandOverride(datePattern, dateOverride) + ";" + expandOverride(timePattern, timeOverride);
    }
    
    private static String expandOverride(final String pattern, final String override) {
        if (override.indexOf(61) >= 0) {
            return override;
        }
        boolean inQuotes = false;
        char prevChar = ' ';
        final StringBuilder result = new StringBuilder();
        final StringCharacterIterator it = new StringCharacterIterator(pattern);
        for (char c = it.first(); c != '\uffff'; c = it.next()) {
            if (c == '\'') {
                inQuotes = !inQuotes;
                prevChar = c;
            }
            else {
                if (!inQuotes && c != prevChar) {
                    if (result.length() > 0) {
                        result.append(";");
                    }
                    result.append(c);
                    result.append("=");
                    result.append(override);
                }
                prevChar = c;
            }
        }
        return result.toString();
    }
    
    protected void pinField(final int field) {
        final int max = this.getActualMaximum(field);
        final int min = this.getActualMinimum(field);
        if (this.fields[field] > max) {
            this.set(field, max);
        }
        else if (this.fields[field] < min) {
            this.set(field, min);
        }
    }
    
    protected int weekNumber(final int desiredDay, final int dayOfPeriod, final int dayOfWeek) {
        int periodStartDayOfWeek = (dayOfWeek - this.getFirstDayOfWeek() - dayOfPeriod + 1) % 7;
        if (periodStartDayOfWeek < 0) {
            periodStartDayOfWeek += 7;
        }
        int weekNo = (desiredDay + periodStartDayOfWeek - 1) / 7;
        if (7 - periodStartDayOfWeek >= this.getMinimalDaysInFirstWeek()) {
            ++weekNo;
        }
        return weekNo;
    }
    
    protected final int weekNumber(final int dayOfPeriod, final int dayOfWeek) {
        return this.weekNumber(dayOfPeriod, dayOfPeriod, dayOfWeek);
    }
    
    public int fieldDifference(final Date when, final int field) {
        int min = 0;
        final long startMs = this.getTimeInMillis();
        final long targetMs = when.getTime();
        if (startMs < targetMs) {
            int max = 1;
            while (true) {
                this.setTimeInMillis(startMs);
                this.add(field, max);
                final long ms = this.getTimeInMillis();
                if (ms == targetMs) {
                    return max;
                }
                if (ms > targetMs) {
                    while (max - min > 1) {
                        final int t = min + (max - min) / 2;
                        this.setTimeInMillis(startMs);
                        this.add(field, t);
                        final long ms2 = this.getTimeInMillis();
                        if (ms2 == targetMs) {
                            return t;
                        }
                        if (ms2 > targetMs) {
                            max = t;
                        }
                        else {
                            min = t;
                        }
                    }
                    break;
                }
                if (max >= Integer.MAX_VALUE) {
                    throw new RuntimeException();
                }
                min = max;
                max <<= 1;
                if (max >= 0) {
                    continue;
                }
                max = Integer.MAX_VALUE;
            }
        }
        else if (startMs > targetMs) {
            int max = -1;
            while (true) {
                this.setTimeInMillis(startMs);
                this.add(field, max);
                final long ms = this.getTimeInMillis();
                if (ms == targetMs) {
                    return max;
                }
                if (ms < targetMs) {
                    while (min - max > 1) {
                        final int t = min + (max - min) / 2;
                        this.setTimeInMillis(startMs);
                        this.add(field, t);
                        final long ms2 = this.getTimeInMillis();
                        if (ms2 == targetMs) {
                            return t;
                        }
                        if (ms2 < targetMs) {
                            max = t;
                        }
                        else {
                            min = t;
                        }
                    }
                    break;
                }
                min = max;
                max <<= 1;
                if (max == 0) {
                    throw new RuntimeException();
                }
            }
        }
        this.setTimeInMillis(startMs);
        this.add(field, min);
        return min;
    }
    
    public void setTimeZone(final TimeZone value) {
        this.zone = value;
        this.areFieldsSet = false;
    }
    
    public TimeZone getTimeZone() {
        return this.zone;
    }
    
    public void setLenient(final boolean lenient) {
        this.lenient = lenient;
    }
    
    public boolean isLenient() {
        return this.lenient;
    }
    
    public void setRepeatedWallTimeOption(final int option) {
        if (option != 0 && option != 1) {
            throw new IllegalArgumentException("Illegal repeated wall time option - " + option);
        }
        this.repeatedWallTime = option;
    }
    
    public int getRepeatedWallTimeOption() {
        return this.repeatedWallTime;
    }
    
    public void setSkippedWallTimeOption(final int option) {
        if (option != 0 && option != 1 && option != 2) {
            throw new IllegalArgumentException("Illegal skipped wall time option - " + option);
        }
        this.skippedWallTime = option;
    }
    
    public int getSkippedWallTimeOption() {
        return this.skippedWallTime;
    }
    
    public void setFirstDayOfWeek(final int value) {
        if (this.firstDayOfWeek != value) {
            if (value < 1 || value > 7) {
                throw new IllegalArgumentException("Invalid day of week");
            }
            this.firstDayOfWeek = value;
            this.areFieldsSet = false;
        }
    }
    
    public int getFirstDayOfWeek() {
        return this.firstDayOfWeek;
    }
    
    public void setMinimalDaysInFirstWeek(int value) {
        if (value < 1) {
            value = 1;
        }
        else if (value > 7) {
            value = 7;
        }
        if (this.minimalDaysInFirstWeek != value) {
            this.minimalDaysInFirstWeek = value;
            this.areFieldsSet = false;
        }
    }
    
    public int getMinimalDaysInFirstWeek() {
        return this.minimalDaysInFirstWeek;
    }
    
    protected abstract int handleGetLimit(final int p0, final int p1);
    
    protected int getLimit(final int field, final int limitType) {
        switch (field) {
            case 7:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 20:
            case 21:
            case 22: {
                return Calendar.LIMITS[field][limitType];
            }
            case 4: {
                int limit;
                if (limitType == 0) {
                    limit = ((this.getMinimalDaysInFirstWeek() == 1) ? 1 : 0);
                }
                else if (limitType == 1) {
                    limit = 1;
                }
                else {
                    final int minDaysInFirst = this.getMinimalDaysInFirstWeek();
                    final int daysInMonth = this.handleGetLimit(5, limitType);
                    if (limitType == 2) {
                        limit = (daysInMonth + (7 - minDaysInFirst)) / 7;
                    }
                    else {
                        limit = (daysInMonth + 6 + (7 - minDaysInFirst)) / 7;
                    }
                }
                return limit;
            }
            default: {
                return this.handleGetLimit(field, limitType);
            }
        }
    }
    
    public final int getMinimum(final int field) {
        return this.getLimit(field, 0);
    }
    
    public final int getMaximum(final int field) {
        return this.getLimit(field, 3);
    }
    
    public final int getGreatestMinimum(final int field) {
        return this.getLimit(field, 1);
    }
    
    public final int getLeastMaximum(final int field) {
        return this.getLimit(field, 2);
    }
    
    public int getDayOfWeekType(final int dayOfWeek) {
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new IllegalArgumentException("Invalid day of week");
        }
        if (this.weekendOnset < this.weekendCease) {
            if (dayOfWeek < this.weekendOnset || dayOfWeek > this.weekendCease) {
                return 0;
            }
        }
        else if (dayOfWeek > this.weekendCease && dayOfWeek < this.weekendOnset) {
            return 0;
        }
        if (dayOfWeek == this.weekendOnset) {
            return (this.weekendOnsetMillis == 0) ? 1 : 2;
        }
        if (dayOfWeek == this.weekendCease) {
            return (this.weekendCeaseMillis == 0) ? 0 : 3;
        }
        return 1;
    }
    
    public int getWeekendTransition(final int dayOfWeek) {
        if (dayOfWeek == this.weekendOnset) {
            return this.weekendOnsetMillis;
        }
        if (dayOfWeek == this.weekendCease) {
            return this.weekendCeaseMillis;
        }
        throw new IllegalArgumentException("Not weekend transition day");
    }
    
    public boolean isWeekend(final Date date) {
        this.setTime(date);
        return this.isWeekend();
    }
    
    public boolean isWeekend() {
        final int dow = this.get(7);
        final int dowt = this.getDayOfWeekType(dow);
        switch (dowt) {
            case 0: {
                return false;
            }
            case 1: {
                return true;
            }
            default: {
                final int millisInDay = this.internalGet(14) + 1000 * (this.internalGet(13) + 60 * (this.internalGet(12) + 60 * this.internalGet(11)));
                final int transition = this.getWeekendTransition(dow);
                return (dowt == 2) ? (millisInDay >= transition) : (millisInDay < transition);
            }
        }
    }
    
    public Object clone() {
        try {
            final Calendar other = (Calendar)super.clone();
            other.fields = new int[this.fields.length];
            other.stamp = new int[this.fields.length];
            System.arraycopy(this.fields, 0, other.fields, 0, this.fields.length);
            System.arraycopy(this.stamp, 0, other.stamp, 0, this.fields.length);
            other.zone = (TimeZone)this.zone.clone();
            return other;
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(this.getClass().getName());
        buffer.append("[time=");
        buffer.append(this.isTimeSet ? String.valueOf(this.time) : "?");
        buffer.append(",areFieldsSet=");
        buffer.append(this.areFieldsSet);
        buffer.append(",areAllFieldsSet=");
        buffer.append(this.areAllFieldsSet);
        buffer.append(",lenient=");
        buffer.append(this.lenient);
        buffer.append(",zone=");
        buffer.append(this.zone);
        buffer.append(",firstDayOfWeek=");
        buffer.append(this.firstDayOfWeek);
        buffer.append(",minimalDaysInFirstWeek=");
        buffer.append(this.minimalDaysInFirstWeek);
        buffer.append(",repeatedWallTime=");
        buffer.append(this.repeatedWallTime);
        buffer.append(",skippedWallTime=");
        buffer.append(this.skippedWallTime);
        for (int i = 0; i < this.fields.length; ++i) {
            buffer.append(',').append(this.fieldName(i)).append('=');
            buffer.append(this.isSet(i) ? String.valueOf(this.fields[i]) : "?");
        }
        buffer.append(']');
        return buffer.toString();
    }
    
    private void setWeekData(final ULocale locale) {
        WeekData data = Calendar.cachedLocaleData.get(locale);
        if (data == null) {
            final CalendarData calData = new CalendarData(locale, this.getType());
            final ULocale min = ULocale.minimizeSubtags(calData.getULocale());
            ULocale useLocale;
            if (min.getCountry().length() > 0) {
                useLocale = min;
            }
            else {
                final ULocale max = ULocale.addLikelySubtags(min);
                final StringBuilder buf = new StringBuilder();
                buf.append(min.getLanguage());
                if (min.getScript().length() > 0) {
                    buf.append("_" + min.getScript());
                }
                if (max.getCountry().length() > 0) {
                    buf.append("_" + max.getCountry());
                }
                if (min.getVariant().length() > 0) {
                    buf.append("_" + min.getVariant());
                }
                useLocale = new ULocale(buf.toString());
            }
            final UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            final UResourceBundle weekDataInfo = rb.get("weekData");
            UResourceBundle weekDataBundle = null;
            try {
                weekDataBundle = weekDataInfo.get(useLocale.getCountry());
            }
            catch (MissingResourceException mre) {
                weekDataBundle = weekDataInfo.get("001");
            }
            final int[] wdi = weekDataBundle.getIntVector();
            data = new WeekData(wdi[0], wdi[1], wdi[2], wdi[3], wdi[4], wdi[5], calData.getULocale());
            Calendar.cachedLocaleData.put(locale, data);
        }
        this.setFirstDayOfWeek(data.firstDayOfWeek);
        this.setMinimalDaysInFirstWeek(data.minimalDaysInFirstWeek);
        this.weekendOnset = data.weekendOnset;
        this.weekendOnsetMillis = data.weekendOnsetMillis;
        this.weekendCease = data.weekendCease;
        this.weekendCeaseMillis = data.weekendCeaseMillis;
        final ULocale uloc = data.actualLocale;
        this.setLocale(uloc, uloc);
    }
    
    private void updateTime() {
        this.computeTime();
        if (this.isLenient() || !this.areAllFieldsSet) {
            this.areFieldsSet = false;
        }
        this.isTimeSet = true;
        this.areFieldsVirtuallySet = false;
    }
    
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        if (!this.isTimeSet) {
            try {
                this.updateTime();
            }
            catch (IllegalArgumentException ex) {}
        }
        stream.defaultWriteObject();
    }
    
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.initInternal();
        this.isTimeSet = true;
        final boolean b = false;
        this.areAllFieldsSet = b;
        this.areFieldsSet = b;
        this.areFieldsVirtuallySet = true;
        this.nextStamp = 2;
    }
    
    protected void computeFields() {
        final int[] offsets = new int[2];
        this.getTimeZone().getOffset(this.time, false, offsets);
        final long localMillis = this.time + offsets[0] + offsets[1];
        int mask = this.internalSetMask;
        for (int i = 0; i < this.fields.length; ++i) {
            if ((mask & 0x1) == 0x0) {
                this.stamp[i] = 1;
            }
            else {
                this.stamp[i] = 0;
            }
            mask >>= 1;
        }
        final long days = floorDivide(localMillis, 86400000L);
        this.computeGregorianAndDOWFields(this.fields[20] = (int)days + 2440588);
        this.handleComputeFields(this.fields[20]);
        this.computeWeekFields();
        int millisInDay = (int)(localMillis - days * 86400000L);
        this.fields[21] = millisInDay;
        this.fields[14] = millisInDay % 1000;
        millisInDay /= 1000;
        this.fields[13] = millisInDay % 60;
        millisInDay /= 60;
        this.fields[12] = millisInDay % 60;
        millisInDay /= 60;
        this.fields[11] = millisInDay;
        this.fields[9] = millisInDay / 12;
        this.fields[10] = millisInDay % 12;
        this.fields[15] = offsets[0];
        this.fields[16] = offsets[1];
    }
    
    private final void computeGregorianAndDOWFields(final int julianDay) {
        this.computeGregorianFields(julianDay);
        final int[] fields = this.fields;
        final int n = 7;
        final int julianDayToDayOfWeek = julianDayToDayOfWeek(julianDay);
        fields[n] = julianDayToDayOfWeek;
        final int dow = julianDayToDayOfWeek;
        int dowLocal = dow - this.getFirstDayOfWeek() + 1;
        if (dowLocal < 1) {
            dowLocal += 7;
        }
        this.fields[18] = dowLocal;
    }
    
    protected final void computeGregorianFields(final int julianDay) {
        final long gregorianEpochDay = julianDay - 1721426;
        final int[] rem = { 0 };
        final int n400 = floorDivide(gregorianEpochDay, 146097, rem);
        final int n401 = floorDivide(rem[0], 36524, rem);
        final int n402 = floorDivide(rem[0], 1461, rem);
        final int n403 = floorDivide(rem[0], 365, rem);
        int year = 400 * n400 + 100 * n401 + 4 * n402 + n403;
        int dayOfYear = rem[0];
        if (n401 == 4 || n403 == 4) {
            dayOfYear = 365;
        }
        else {
            ++year;
        }
        final boolean isLeap = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
        int correction = 0;
        final int march1 = isLeap ? 60 : 59;
        if (dayOfYear >= march1) {
            correction = (isLeap ? 1 : 2);
        }
        final int month = (12 * (dayOfYear + correction) + 6) / 367;
        final int dayOfMonth = dayOfYear - Calendar.GREGORIAN_MONTH_COUNT[month][isLeap ? 3 : 2] + 1;
        this.gregorianYear = year;
        this.gregorianMonth = month;
        this.gregorianDayOfMonth = dayOfMonth;
        this.gregorianDayOfYear = dayOfYear + 1;
    }
    
    private final void computeWeekFields() {
        final int eyear = this.fields[19];
        final int dayOfWeek = this.fields[7];
        final int dayOfYear = this.fields[6];
        int yearOfWeekOfYear = eyear;
        final int relDow = (dayOfWeek + 7 - this.getFirstDayOfWeek()) % 7;
        final int relDowJan1 = (dayOfWeek - dayOfYear + 7001 - this.getFirstDayOfWeek()) % 7;
        int woy = (dayOfYear - 1 + relDowJan1) / 7;
        if (7 - relDowJan1 >= this.getMinimalDaysInFirstWeek()) {
            ++woy;
        }
        if (woy == 0) {
            final int prevDoy = dayOfYear + this.handleGetYearLength(eyear - 1);
            woy = this.weekNumber(prevDoy, dayOfWeek);
            --yearOfWeekOfYear;
        }
        else {
            final int lastDoy = this.handleGetYearLength(eyear);
            if (dayOfYear >= lastDoy - 5) {
                int lastRelDow = (relDow + lastDoy - dayOfYear) % 7;
                if (lastRelDow < 0) {
                    lastRelDow += 7;
                }
                if (6 - lastRelDow >= this.getMinimalDaysInFirstWeek() && dayOfYear + 7 - relDow > lastDoy) {
                    woy = 1;
                    ++yearOfWeekOfYear;
                }
            }
        }
        this.fields[3] = woy;
        this.fields[17] = yearOfWeekOfYear;
        final int dayOfMonth = this.fields[5];
        this.fields[4] = this.weekNumber(dayOfMonth, dayOfWeek);
        this.fields[8] = (dayOfMonth - 1) / 7 + 1;
    }
    
    protected int resolveFields(final int[][][] precedenceTable) {
        int bestField = -1;
        for (int g = 0; g < precedenceTable.length && bestField < 0; ++g) {
            final int[][] group = precedenceTable[g];
            int bestStamp = 0;
            int l = 0;
        Label_0028:
            while (l < group.length) {
                final int[] line = group[l];
                int lineStamp = 0;
                while (true) {
                    for (int i = (line[0] >= 32) ? 1 : 0; i < line.length; ++i) {
                        final int s = this.stamp[line[i]];
                        if (s == 0) {
                            ++l;
                            continue Label_0028;
                        }
                        lineStamp = Math.max(lineStamp, s);
                    }
                    if (lineStamp <= bestStamp) {
                        continue;
                    }
                    int tempBestField = line[0];
                    if (tempBestField >= 32) {
                        tempBestField &= 0x1F;
                        if (tempBestField != 5 || this.stamp[4] < this.stamp[tempBestField]) {
                            bestField = tempBestField;
                        }
                    }
                    else {
                        bestField = tempBestField;
                    }
                    if (bestField == tempBestField) {
                        bestStamp = lineStamp;
                    }
                    continue;
                }
            }
        }
        return (bestField >= 32) ? (bestField & 0x1F) : bestField;
    }
    
    protected int newestStamp(final int first, final int last, final int bestStampSoFar) {
        int bestStamp = bestStampSoFar;
        for (int i = first; i <= last; ++i) {
            if (this.stamp[i] > bestStamp) {
                bestStamp = this.stamp[i];
            }
        }
        return bestStamp;
    }
    
    protected final int getStamp(final int field) {
        return this.stamp[field];
    }
    
    protected int newerField(final int defaultField, final int alternateField) {
        if (this.stamp[alternateField] > this.stamp[defaultField]) {
            return alternateField;
        }
        return defaultField;
    }
    
    protected void validateFields() {
        for (int field = 0; field < this.fields.length; ++field) {
            if (this.stamp[field] >= 2) {
                this.validateField(field);
            }
        }
    }
    
    protected void validateField(final int field) {
        switch (field) {
            case 5: {
                final int y = this.handleGetExtendedYear();
                this.validateField(field, 1, this.handleGetMonthLength(y, this.internalGet(2)));
                break;
            }
            case 6: {
                final int y = this.handleGetExtendedYear();
                this.validateField(field, 1, this.handleGetYearLength(y));
                break;
            }
            case 8: {
                if (this.internalGet(field) == 0) {
                    throw new IllegalArgumentException("DAY_OF_WEEK_IN_MONTH cannot be zero");
                }
                this.validateField(field, this.getMinimum(field), this.getMaximum(field));
                break;
            }
            default: {
                this.validateField(field, this.getMinimum(field), this.getMaximum(field));
                break;
            }
        }
    }
    
    protected final void validateField(final int field, final int min, final int max) {
        final int value = this.fields[field];
        if (value < min || value > max) {
            throw new IllegalArgumentException(this.fieldName(field) + '=' + value + ", valid range=" + min + ".." + max);
        }
    }
    
    protected void computeTime() {
        if (!this.isLenient()) {
            this.validateFields();
        }
        final int julianDay = this.computeJulianDay();
        final long millis = julianDayToMillis(julianDay);
        int millisInDay;
        if (this.stamp[21] >= 2 && this.newestStamp(9, 14, 0) <= this.stamp[21]) {
            millisInDay = this.internalGet(21);
        }
        else {
            millisInDay = this.computeMillisInDay();
        }
        if (this.stamp[15] >= 2 || this.stamp[16] >= 2) {
            this.time = millis + millisInDay - (this.internalGet(15) + this.internalGet(16));
        }
        else if (!this.lenient || this.skippedWallTime == 2) {
            final int zoneOffset = this.computeZoneOffset(millis, millisInDay);
            final long tmpTime = millis + millisInDay - zoneOffset;
            final int zoneOffset2 = this.zone.getOffset(tmpTime);
            if (zoneOffset != zoneOffset2) {
                if (!this.lenient) {
                    throw new IllegalArgumentException("The specified wall time does not exist due to time zone offset transition.");
                }
                assert this.skippedWallTime == 2 : this.skippedWallTime;
                if (this.zone instanceof BasicTimeZone) {
                    final TimeZoneTransition transition = ((BasicTimeZone)this.zone).getPreviousTransition(tmpTime, true);
                    if (transition == null) {
                        throw new RuntimeException("Could not locate previous zone transition");
                    }
                    this.time = transition.getTime();
                }
                else {
                    Long transitionT = this.getPreviousZoneTransitionTime(this.zone, tmpTime, 7200000L);
                    if (transitionT == null) {
                        transitionT = this.getPreviousZoneTransitionTime(this.zone, tmpTime, 108000000L);
                        if (transitionT == null) {
                            throw new RuntimeException("Could not locate previous zone transition within 30 hours from " + tmpTime);
                        }
                    }
                    this.time = transitionT;
                }
            }
            else {
                this.time = tmpTime;
            }
        }
        else {
            this.time = millis + millisInDay - this.computeZoneOffset(millis, millisInDay);
        }
    }
    
    private Long getPreviousZoneTransitionTime(final TimeZone tz, final long base, final long duration) {
        assert duration > 0L;
        final long upper = base;
        final long lower = base - duration - 1L;
        final int offsetU = tz.getOffset(upper);
        final int offsetL = tz.getOffset(lower);
        if (offsetU == offsetL) {
            return null;
        }
        return this.findPreviousZoneTransitionTime(tz, offsetU, upper, lower);
    }
    
    private Long findPreviousZoneTransitionTime(final TimeZone tz, final int upperOffset, long upper, final long lower) {
        boolean onUnitTime = false;
        long mid = 0L;
        for (final int unit : Calendar.FIND_ZONE_TRANSITION_TIME_UNITS) {
            final long lunits = lower / unit;
            final long uunits = upper / unit;
            if (uunits > lunits) {
                mid = (lunits + uunits + 1L >>> 1) * unit;
                onUnitTime = true;
                break;
            }
        }
        if (!onUnitTime) {
            mid = upper + lower >>> 1;
        }
        if (onUnitTime) {
            if (mid != upper) {
                final int midOffset = tz.getOffset(mid);
                if (midOffset != upperOffset) {
                    return this.findPreviousZoneTransitionTime(tz, upperOffset, upper, mid);
                }
                upper = mid;
            }
            --mid;
        }
        else {
            mid = upper + lower >>> 1;
        }
        if (mid == lower) {
            return upper;
        }
        final int midOffset = tz.getOffset(mid);
        if (midOffset == upperOffset) {
            return this.findPreviousZoneTransitionTime(tz, upperOffset, mid, lower);
        }
        if (onUnitTime) {
            return upper;
        }
        return this.findPreviousZoneTransitionTime(tz, upperOffset, upper, mid);
    }
    
    protected int computeMillisInDay() {
        int millisInDay = 0;
        final int hourOfDayStamp = this.stamp[11];
        final int hourStamp = Math.max(this.stamp[10], this.stamp[9]);
        final int bestStamp = (hourStamp > hourOfDayStamp) ? hourStamp : hourOfDayStamp;
        if (bestStamp != 0) {
            if (bestStamp == hourOfDayStamp) {
                millisInDay += this.internalGet(11);
            }
            else {
                millisInDay += this.internalGet(10);
                millisInDay += 12 * this.internalGet(9);
            }
        }
        millisInDay *= 60;
        millisInDay += this.internalGet(12);
        millisInDay *= 60;
        millisInDay += this.internalGet(13);
        millisInDay *= 1000;
        millisInDay += this.internalGet(14);
        return millisInDay;
    }
    
    protected int computeZoneOffset(final long millis, final int millisInDay) {
        final int[] offsets = new int[2];
        final long wall = millis + millisInDay;
        if (this.zone instanceof BasicTimeZone) {
            final int duplicatedTimeOpt = (this.repeatedWallTime == 1) ? 4 : 12;
            final int nonExistingTimeOpt = (this.skippedWallTime == 1) ? 12 : 4;
            ((BasicTimeZone)this.zone).getOffsetFromLocal(wall, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
        }
        else {
            this.zone.getOffset(wall, true, offsets);
            boolean sawRecentNegativeShift = false;
            if (this.repeatedWallTime == 1) {
                final long tgmt = wall - (offsets[0] + offsets[1]);
                final int offsetBefore6 = this.zone.getOffset(tgmt - 21600000L);
                final int offsetDelta = offsets[0] + offsets[1] - offsetBefore6;
                assert offsetDelta < -21600000 : offsetDelta;
                if (offsetDelta < 0) {
                    sawRecentNegativeShift = true;
                    this.zone.getOffset(wall + offsetDelta, true, offsets);
                }
            }
            if (!sawRecentNegativeShift && this.skippedWallTime == 1) {
                final long tgmt = wall - (offsets[0] + offsets[1]);
                this.zone.getOffset(tgmt, false, offsets);
            }
        }
        return offsets[0] + offsets[1];
    }
    
    protected int computeJulianDay() {
        if (this.stamp[20] >= 2) {
            int bestStamp = this.newestStamp(0, 8, 0);
            bestStamp = this.newestStamp(17, 19, bestStamp);
            if (bestStamp <= this.stamp[20]) {
                return this.internalGet(20);
            }
        }
        int bestField = this.resolveFields(this.getFieldResolutionTable());
        if (bestField < 0) {
            bestField = 5;
        }
        return this.handleComputeJulianDay(bestField);
    }
    
    protected int[][][] getFieldResolutionTable() {
        return Calendar.DATE_PRECEDENCE;
    }
    
    protected abstract int handleComputeMonthStart(final int p0, final int p1, final boolean p2);
    
    protected abstract int handleGetExtendedYear();
    
    protected int handleGetMonthLength(final int extendedYear, final int month) {
        return this.handleComputeMonthStart(extendedYear, month + 1, true) - this.handleComputeMonthStart(extendedYear, month, true);
    }
    
    protected int handleGetYearLength(final int eyear) {
        return this.handleComputeMonthStart(eyear + 1, 0, false) - this.handleComputeMonthStart(eyear, 0, false);
    }
    
    protected int[] handleCreateFields() {
        return new int[23];
    }
    
    protected int getDefaultMonthInYear(final int extendedYear) {
        return 0;
    }
    
    protected int getDefaultDayInMonth(final int extendedYear, final int month) {
        return 1;
    }
    
    protected int handleComputeJulianDay(final int bestField) {
        final boolean useMonth = bestField == 5 || bestField == 4 || bestField == 8;
        int year;
        if (bestField == 3) {
            year = this.internalGet(17, this.handleGetExtendedYear());
        }
        else {
            year = this.handleGetExtendedYear();
        }
        this.internalSet(19, year);
        final int month = useMonth ? this.internalGet(2, this.getDefaultMonthInYear(year)) : 0;
        final int julianDay = this.handleComputeMonthStart(year, month, useMonth);
        if (bestField == 5) {
            if (this.isSet(5)) {
                return julianDay + this.internalGet(5, this.getDefaultDayInMonth(year, month));
            }
            return julianDay + this.getDefaultDayInMonth(year, month);
        }
        else {
            if (bestField == 6) {
                return julianDay + this.internalGet(6);
            }
            final int firstDOW = this.getFirstDayOfWeek();
            int first = julianDayToDayOfWeek(julianDay + 1) - firstDOW;
            if (first < 0) {
                first += 7;
            }
            int dowLocal = 0;
            switch (this.resolveFields(Calendar.DOW_PRECEDENCE)) {
                case 7: {
                    dowLocal = this.internalGet(7) - firstDOW;
                    break;
                }
                case 18: {
                    dowLocal = this.internalGet(18) - 1;
                    break;
                }
            }
            dowLocal %= 7;
            if (dowLocal < 0) {
                dowLocal += 7;
            }
            int date = 1 - first + dowLocal;
            if (bestField == 8) {
                if (date < 1) {
                    date += 7;
                }
                final int dim = this.internalGet(8, 1);
                if (dim >= 0) {
                    date += 7 * (dim - 1);
                }
                else {
                    final int m = this.internalGet(2, 0);
                    final int monthLength = this.handleGetMonthLength(year, m);
                    date += ((monthLength - date) / 7 + dim + 1) * 7;
                }
            }
            else {
                if (7 - first < this.getMinimalDaysInFirstWeek()) {
                    date += 7;
                }
                date += 7 * (this.internalGet(bestField) - 1);
            }
            return julianDay + date;
        }
    }
    
    protected int computeGregorianMonthStart(int year, int month) {
        if (month < 0 || month > 11) {
            final int[] rem = { 0 };
            year += floorDivide(month, 12, rem);
            month = rem[0];
        }
        final boolean isLeap = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
        final int y = year - 1;
        int julianDay = 365 * y + floorDivide(y, 4) - floorDivide(y, 100) + floorDivide(y, 400) + 1721426 - 1;
        if (month != 0) {
            julianDay += Calendar.GREGORIAN_MONTH_COUNT[month][isLeap ? 3 : 2];
        }
        return julianDay;
    }
    
    protected void handleComputeFields(final int julianDay) {
        this.internalSet(2, this.getGregorianMonth());
        this.internalSet(5, this.getGregorianDayOfMonth());
        this.internalSet(6, this.getGregorianDayOfYear());
        int eyear = this.getGregorianYear();
        this.internalSet(19, eyear);
        int era = 1;
        if (eyear < 1) {
            era = 0;
            eyear = 1 - eyear;
        }
        this.internalSet(0, era);
        this.internalSet(1, eyear);
    }
    
    protected final int getGregorianYear() {
        return this.gregorianYear;
    }
    
    protected final int getGregorianMonth() {
        return this.gregorianMonth;
    }
    
    protected final int getGregorianDayOfYear() {
        return this.gregorianDayOfYear;
    }
    
    protected final int getGregorianDayOfMonth() {
        return this.gregorianDayOfMonth;
    }
    
    public final int getFieldCount() {
        return this.fields.length;
    }
    
    protected final void internalSet(final int field, final int value) {
        if ((1 << field & this.internalSetMask) == 0x0) {
            throw new IllegalStateException("Subclass cannot set " + this.fieldName(field));
        }
        this.fields[field] = value;
        this.stamp[field] = 1;
    }
    
    protected static final boolean isGregorianLeapYear(final int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }
    
    protected static final int gregorianMonthLength(final int y, final int m) {
        return Calendar.GREGORIAN_MONTH_COUNT[m][isGregorianLeapYear(y)];
    }
    
    protected static final int gregorianPreviousMonthLength(final int y, final int m) {
        return (m > 0) ? gregorianMonthLength(y, m - 1) : 31;
    }
    
    protected static final long floorDivide(final long numerator, final long denominator) {
        return (numerator >= 0L) ? (numerator / denominator) : ((numerator + 1L) / denominator - 1L);
    }
    
    protected static final int floorDivide(final int numerator, final int denominator) {
        return (numerator >= 0) ? (numerator / denominator) : ((numerator + 1) / denominator - 1);
    }
    
    protected static final int floorDivide(final int numerator, final int denominator, final int[] remainder) {
        if (numerator >= 0) {
            remainder[0] = numerator % denominator;
            return numerator / denominator;
        }
        final int quotient = (numerator + 1) / denominator - 1;
        remainder[0] = numerator - quotient * denominator;
        return quotient;
    }
    
    protected static final int floorDivide(final long numerator, final int denominator, final int[] remainder) {
        if (numerator >= 0L) {
            remainder[0] = (int)(numerator % denominator);
            return (int)(numerator / denominator);
        }
        final int quotient = (int)((numerator + 1L) / denominator - 1L);
        remainder[0] = (int)(numerator - quotient * (long)denominator);
        return quotient;
    }
    
    protected String fieldName(final int field) {
        try {
            return Calendar.FIELD_NAME[field];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return "Field " + field;
        }
    }
    
    protected static final int millisToJulianDay(final long millis) {
        return (int)(2440588L + floorDivide(millis, 86400000L));
    }
    
    protected static final long julianDayToMillis(final int julian) {
        return (julian - 2440588) * 86400000L;
    }
    
    protected static final int julianDayToDayOfWeek(final int julian) {
        int dayOfWeek = (julian + 2) % 7;
        if (dayOfWeek < 1) {
            dayOfWeek += 7;
        }
        return dayOfWeek;
    }
    
    protected final long internalGetTimeInMillis() {
        return this.time;
    }
    
    public String getType() {
        return "unknown";
    }
    
    public final ULocale getLocale(final ULocale.Type type) {
        return (type == ULocale.ACTUAL_LOCALE) ? this.actualLocale : this.validLocale;
    }
    
    final void setLocale(final ULocale valid, final ULocale actual) {
        if (valid == null != (actual == null)) {
            throw new IllegalArgumentException();
        }
        this.validLocale = valid;
        this.actualLocale = actual;
    }
    
    static {
        MIN_DATE = new Date(-184303902528000000L);
        MAX_DATE = new Date(183882168921600000L);
        Calendar.cachedLocaleData = new SimpleCache<ULocale, WeekData>();
        Calendar.STAMP_MAX = 10000;
        calTypes = new String[] { "gregorian", "japanese", "buddhist", "roc", "persian", "islamic-civil", "islamic", "hebrew", "chinese", "indian", "coptic", "ethiopic", "ethiopic-amete-alem", "iso8601", "dangi" };
        PATTERN_CACHE = new SimpleCache<String, PatternData>();
        DEFAULT_PATTERNS = new String[] { "HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm", "EEEE, yyyy MMMM dd", "yyyy MMMM d", "yyyy MMM d", "yy/MM/dd", "{1} {0}", "{1} {0}", "{1} {0}", "{1} {0}", "{1} {0}" };
        LIMITS = new int[][] { new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { 1, 1, 7, 7 }, new int[0], { 0, 0, 1, 1 }, { 0, 0, 11, 11 }, { 0, 0, 23, 23 }, { 0, 0, 59, 59 }, { 0, 0, 59, 59 }, { 0, 0, 999, 999 }, { -43200000, -43200000, 43200000, 43200000 }, { 0, 0, 3600000, 3600000 }, new int[0], { 1, 1, 7, 7 }, new int[0], { -2130706432, -2130706432, 2130706432, 2130706432 }, { 0, 0, 86399999, 86399999 }, { 0, 0, 1, 1 } };
        DATE_PRECEDENCE = new int[][][] { { { 5 }, { 3, 7 }, { 4, 7 }, { 8, 7 }, { 3, 18 }, { 4, 18 }, { 8, 18 }, { 6 }, { 37, 1 }, { 35, 17 } }, { { 3 }, { 4 }, { 8 }, { 40, 7 }, { 40, 18 } } };
        DOW_PRECEDENCE = new int[][][] { { { 7 }, { 18 } } };
        FIND_ZONE_TRANSITION_TIME_UNITS = new int[] { 3600000, 1800000, 60000, 1000 };
        GREGORIAN_MONTH_COUNT = new int[][] { { 31, 31, 0, 0 }, { 28, 29, 31, 31 }, { 31, 31, 59, 60 }, { 30, 30, 90, 91 }, { 31, 31, 120, 121 }, { 30, 30, 151, 152 }, { 31, 31, 181, 182 }, { 31, 31, 212, 213 }, { 30, 30, 243, 244 }, { 31, 31, 273, 274 }, { 30, 30, 304, 305 }, { 31, 31, 334, 335 } };
        FIELD_NAME = new String[] { "ERA", "YEAR", "MONTH", "WEEK_OF_YEAR", "WEEK_OF_MONTH", "DAY_OF_MONTH", "DAY_OF_YEAR", "DAY_OF_WEEK", "DAY_OF_WEEK_IN_MONTH", "AM_PM", "HOUR", "HOUR_OF_DAY", "MINUTE", "SECOND", "MILLISECOND", "ZONE_OFFSET", "DST_OFFSET", "YEAR_WOY", "DOW_LOCAL", "EXTENDED_YEAR", "JULIAN_DAY", "MILLISECONDS_IN_DAY" };
    }
    
    abstract static class CalendarFactory
    {
        public boolean visible() {
            return true;
        }
        
        public abstract Set<String> getSupportedLocaleNames();
        
        public Calendar createCalendar(final ULocale loc) {
            return null;
        }
        
        protected CalendarFactory() {
        }
    }
    
    abstract static class CalendarShim
    {
        abstract Locale[] getAvailableLocales();
        
        abstract ULocale[] getAvailableULocales();
        
        abstract Object registerFactory(final CalendarFactory p0);
        
        abstract boolean unregister(final Object p0);
        
        abstract Calendar createInstance(final ULocale p0);
    }
    
    static class PatternData
    {
        private String[] patterns;
        private String[] overrides;
        
        public PatternData(final String[] patterns, final String[] overrides) {
            this.patterns = patterns;
            this.overrides = overrides;
        }
        
        private String getDateTimePattern(final int dateStyle) {
            int glueIndex = 8;
            if (this.patterns.length >= 13) {
                glueIndex += dateStyle + 1;
            }
            final String dateTimePattern = this.patterns[glueIndex];
            return dateTimePattern;
        }
        
        private static PatternData make(final Calendar cal, final ULocale loc) {
            final String calType = cal.getType();
            final String key = loc.getBaseName() + "+" + calType;
            PatternData patternData = Calendar.PATTERN_CACHE.get(key);
            if (patternData == null) {
                try {
                    final CalendarData calData = new CalendarData(loc, calType);
                    patternData = new PatternData(calData.getDateTimePatterns(), calData.getOverrides());
                }
                catch (MissingResourceException e) {
                    patternData = new PatternData(Calendar.DEFAULT_PATTERNS, null);
                }
                Calendar.PATTERN_CACHE.put(key, patternData);
            }
            return patternData;
        }
    }
    
    public static class FormatConfiguration
    {
        private String pattern;
        private String override;
        private DateFormatSymbols formatData;
        private Calendar cal;
        private ULocale loc;
        
        private FormatConfiguration() {
        }
        
        @Deprecated
        public String getPatternString() {
            return this.pattern;
        }
        
        @Deprecated
        public String getOverrideString() {
            return this.override;
        }
        
        @Deprecated
        public Calendar getCalendar() {
            return this.cal;
        }
        
        @Deprecated
        public ULocale getLocale() {
            return this.loc;
        }
        
        @Deprecated
        public DateFormatSymbols getDateFormatSymbols() {
            return this.formatData;
        }
    }
    
    private static class WeekData
    {
        public int firstDayOfWeek;
        public int minimalDaysInFirstWeek;
        public int weekendOnset;
        public int weekendOnsetMillis;
        public int weekendCease;
        public int weekendCeaseMillis;
        public ULocale actualLocale;
        
        public WeekData(final int fdow, final int mdifw, final int weekendOnset, final int weekendOnsetMillis, final int weekendCease, final int weekendCeaseMillis, final ULocale actualLoc) {
            this.firstDayOfWeek = fdow;
            this.minimalDaysInFirstWeek = mdifw;
            this.actualLocale = actualLoc;
            this.weekendOnset = weekendOnset;
            this.weekendOnsetMillis = weekendOnsetMillis;
            this.weekendCease = weekendCease;
            this.weekendCeaseMillis = weekendCeaseMillis;
        }
    }
}
