// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.SimpleCache;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.TimeZoneTransition;
import com.ibm.icu.util.BasicTimeZone;
import com.ibm.icu.impl.PatternProps;
import java.text.ParsePosition;
import java.util.ArrayList;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.HebrewCalendar;
import java.text.Format;
import com.ibm.icu.util.TimeZone;
import java.util.List;
import java.text.FieldPosition;
import java.util.MissingResourceException;
import com.ibm.icu.impl.CalendarData;
import com.ibm.icu.impl.DateNumberFormat;
import java.util.Locale;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.impl.ICUCache;
import java.util.Date;
import com.ibm.icu.util.ULocale;
import java.util.HashMap;

public class SimpleDateFormat extends DateFormat
{
    private static final long serialVersionUID = 4774881970558875024L;
    static final int currentSerialVersion = 2;
    static boolean DelayedHebrewMonthCheck;
    private static final int[] CALENDAR_FIELD_TO_LEVEL;
    private static final int[] PATTERN_CHAR_TO_LEVEL;
    private static final int HEBREW_CAL_CUR_MILLENIUM_START_YEAR = 5000;
    private static final int HEBREW_CAL_CUR_MILLENIUM_END_YEAR = 6000;
    private int serialVersionOnStream;
    private String pattern;
    private String override;
    private HashMap<String, NumberFormat> numberFormatters;
    private HashMap<Character, String> overrideMap;
    private DateFormatSymbols formatData;
    private transient ULocale locale;
    private Date defaultCenturyStart;
    private transient int defaultCenturyStartYear;
    private transient long defaultCenturyBase;
    private transient TimeZoneFormat.TimeType tztype;
    private static final int millisPerHour = 3600000;
    private static final int ISOSpecialEra = -32000;
    private static final String SUPPRESS_NEGATIVE_PREFIX = "\uab00";
    private transient boolean useFastFormat;
    private volatile TimeZoneFormat tzFormat;
    private transient DisplayContext capitalizationSetting;
    private static ULocale cachedDefaultLocale;
    private static String cachedDefaultPattern;
    private static final String FALLBACKPATTERN = "yy/MM/dd HH:mm";
    private static final int PATTERN_CHAR_BASE = 64;
    private static final int[] PATTERN_CHAR_TO_INDEX;
    private static final int[] PATTERN_INDEX_TO_CALENDAR_FIELD;
    private static final int[] PATTERN_INDEX_TO_DATE_FORMAT_FIELD;
    private static final Field[] PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE;
    private static ICUCache<String, Object[]> PARSED_PATTERN_CACHE;
    private transient Object[] patternItems;
    private transient boolean useLocalZeroPaddingNumberFormat;
    private transient char[] decDigits;
    private transient char[] decimalBuf;
    private static final String NUMERIC_FORMAT_CHARS = "MYyudehHmsSDFwWkK";
    static final UnicodeSet DATE_PATTERN_TYPE;
    
    public SimpleDateFormat() {
        this(getDefaultPattern(), null, null, null, null, true, null);
    }
    
    public SimpleDateFormat(final String pattern) {
        this(pattern, null, null, null, null, true, null);
    }
    
    public SimpleDateFormat(final String pattern, final Locale loc) {
        this(pattern, null, null, null, ULocale.forLocale(loc), true, null);
    }
    
    public SimpleDateFormat(final String pattern, final ULocale loc) {
        this(pattern, null, null, null, loc, true, null);
    }
    
    public SimpleDateFormat(final String pattern, final String override, final ULocale loc) {
        this(pattern, null, null, null, loc, false, override);
    }
    
    public SimpleDateFormat(final String pattern, final DateFormatSymbols formatData) {
        this(pattern, (DateFormatSymbols)formatData.clone(), null, null, null, true, null);
    }
    
    @Deprecated
    public SimpleDateFormat(final String pattern, final DateFormatSymbols formatData, final ULocale loc) {
        this(pattern, (DateFormatSymbols)formatData.clone(), null, null, loc, true, null);
    }
    
    SimpleDateFormat(final String pattern, final DateFormatSymbols formatData, final Calendar calendar, final ULocale locale, final boolean useFastFormat, final String override) {
        this(pattern, (DateFormatSymbols)formatData.clone(), (Calendar)calendar.clone(), null, locale, useFastFormat, override);
    }
    
    private SimpleDateFormat(final String pattern, final DateFormatSymbols formatData, final Calendar calendar, final NumberFormat numberFormat, final ULocale locale, final boolean useFastFormat, final String override) {
        this.serialVersionOnStream = 2;
        this.tztype = TimeZoneFormat.TimeType.UNKNOWN;
        this.pattern = pattern;
        this.formatData = formatData;
        this.calendar = calendar;
        this.numberFormat = numberFormat;
        this.locale = locale;
        this.useFastFormat = useFastFormat;
        this.override = override;
        this.initialize();
    }
    
    @Deprecated
    public static SimpleDateFormat getInstance(final Calendar.FormatConfiguration formatConfig) {
        final String ostr = formatConfig.getOverrideString();
        final boolean useFast = ostr != null && ostr.length() > 0;
        return new SimpleDateFormat(formatConfig.getPatternString(), formatConfig.getDateFormatSymbols(), formatConfig.getCalendar(), null, formatConfig.getLocale(), useFast, formatConfig.getOverrideString());
    }
    
    private void initialize() {
        if (this.locale == null) {
            this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
        }
        if (this.formatData == null) {
            this.formatData = new DateFormatSymbols(this.locale);
        }
        if (this.calendar == null) {
            this.calendar = Calendar.getInstance(this.locale);
        }
        if (this.numberFormat == null) {
            final NumberingSystem ns = NumberingSystem.getInstance(this.locale);
            if (ns.isAlgorithmic()) {
                this.numberFormat = NumberFormat.getInstance(this.locale);
            }
            else {
                final String digitString = ns.getDescription();
                final String nsName = ns.getName();
                this.numberFormat = new DateNumberFormat(this.locale, digitString, nsName);
            }
        }
        this.defaultCenturyBase = System.currentTimeMillis();
        this.setLocale(this.calendar.getLocale(ULocale.VALID_LOCALE), this.calendar.getLocale(ULocale.ACTUAL_LOCALE));
        this.initLocalZeroPaddingNumberFormat();
        if (this.override != null) {
            this.initNumberFormatters(this.locale);
        }
        this.capitalizationSetting = DisplayContext.CAPITALIZATION_NONE;
    }
    
    private synchronized void initializeTimeZoneFormat(final boolean bForceUpdate) {
        if (bForceUpdate || this.tzFormat == null) {
            this.tzFormat = TimeZoneFormat.getInstance(this.locale);
            String digits = null;
            if (this.numberFormat instanceof DecimalFormat) {
                final DecimalFormatSymbols decsym = ((DecimalFormat)this.numberFormat).getDecimalFormatSymbols();
                digits = new String(decsym.getDigits());
            }
            else if (this.numberFormat instanceof DateNumberFormat) {
                digits = new String(((DateNumberFormat)this.numberFormat).getDigits());
            }
            if (digits != null && !this.tzFormat.getGMTOffsetDigits().equals(digits)) {
                if (this.tzFormat.isFrozen()) {
                    this.tzFormat = this.tzFormat.cloneAsThawed();
                }
                this.tzFormat.setGMTOffsetDigits(digits);
            }
        }
    }
    
    private TimeZoneFormat tzFormat() {
        if (this.tzFormat == null) {
            this.initializeTimeZoneFormat(false);
        }
        return this.tzFormat;
    }
    
    private static synchronized String getDefaultPattern() {
        final ULocale defaultLocale = ULocale.getDefault(ULocale.Category.FORMAT);
        if (!defaultLocale.equals(SimpleDateFormat.cachedDefaultLocale)) {
            SimpleDateFormat.cachedDefaultLocale = defaultLocale;
            final Calendar cal = Calendar.getInstance(SimpleDateFormat.cachedDefaultLocale);
            try {
                final CalendarData calData = new CalendarData(SimpleDateFormat.cachedDefaultLocale, cal.getType());
                final String[] dateTimePatterns = calData.getDateTimePatterns();
                int glueIndex = 8;
                if (dateTimePatterns.length >= 13) {
                    glueIndex += 4;
                }
                SimpleDateFormat.cachedDefaultPattern = MessageFormat.format(dateTimePatterns[glueIndex], dateTimePatterns[3], dateTimePatterns[7]);
            }
            catch (MissingResourceException e) {
                SimpleDateFormat.cachedDefaultPattern = "yy/MM/dd HH:mm";
            }
        }
        return SimpleDateFormat.cachedDefaultPattern;
    }
    
    private void parseAmbiguousDatesAsAfter(final Date startDate) {
        this.defaultCenturyStart = startDate;
        this.calendar.setTime(startDate);
        this.defaultCenturyStartYear = this.calendar.get(1);
    }
    
    private void initializeDefaultCenturyStart(final long baseTime) {
        this.defaultCenturyBase = baseTime;
        final Calendar tmpCal = (Calendar)this.calendar.clone();
        tmpCal.setTimeInMillis(baseTime);
        tmpCal.add(1, -80);
        this.defaultCenturyStart = tmpCal.getTime();
        this.defaultCenturyStartYear = tmpCal.get(1);
    }
    
    private Date getDefaultCenturyStart() {
        if (this.defaultCenturyStart == null) {
            this.initializeDefaultCenturyStart(this.defaultCenturyBase);
        }
        return this.defaultCenturyStart;
    }
    
    private int getDefaultCenturyStartYear() {
        if (this.defaultCenturyStart == null) {
            this.initializeDefaultCenturyStart(this.defaultCenturyBase);
        }
        return this.defaultCenturyStartYear;
    }
    
    public void set2DigitYearStart(final Date startDate) {
        this.parseAmbiguousDatesAsAfter(startDate);
    }
    
    public Date get2DigitYearStart() {
        return this.getDefaultCenturyStart();
    }
    
    @Override
    public StringBuffer format(Calendar cal, final StringBuffer toAppendTo, final FieldPosition pos) {
        TimeZone backupTZ = null;
        if (cal != this.calendar && !cal.getType().equals(this.calendar.getType())) {
            this.calendar.setTimeInMillis(cal.getTimeInMillis());
            backupTZ = this.calendar.getTimeZone();
            this.calendar.setTimeZone(cal.getTimeZone());
            cal = this.calendar;
        }
        final StringBuffer result = this.format(cal, this.capitalizationSetting, toAppendTo, pos, null);
        if (backupTZ != null) {
            this.calendar.setTimeZone(backupTZ);
        }
        return result;
    }
    
    private StringBuffer format(final Calendar cal, final DisplayContext capitalizationContext, final StringBuffer toAppendTo, final FieldPosition pos, final List<FieldPosition> attributes) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        final Object[] items = this.getPatternItems();
        for (int i = 0; i < items.length; ++i) {
            if (items[i] instanceof String) {
                toAppendTo.append((String)items[i]);
            }
            else {
                final PatternItem item = (PatternItem)items[i];
                int start = 0;
                if (attributes != null) {
                    start = toAppendTo.length();
                }
                if (this.useFastFormat) {
                    this.subFormat(toAppendTo, item.type, item.length, toAppendTo.length(), i, capitalizationContext, pos, cal);
                }
                else {
                    toAppendTo.append(this.subFormat(item.type, item.length, toAppendTo.length(), i, capitalizationContext, pos, cal));
                }
                if (attributes != null) {
                    final int end = toAppendTo.length();
                    if (end - start > 0) {
                        final Field attr = this.patternCharToDateFormatField(item.type);
                        final FieldPosition fp = new FieldPosition(attr);
                        fp.setBeginIndex(start);
                        fp.setEndIndex(end);
                        attributes.add(fp);
                    }
                }
            }
        }
        return toAppendTo;
    }
    
    protected Field patternCharToDateFormatField(final char ch) {
        int patternCharIndex = -1;
        if ('A' <= ch && ch <= 'z') {
            patternCharIndex = SimpleDateFormat.PATTERN_CHAR_TO_INDEX[ch - '@'];
        }
        if (patternCharIndex != -1) {
            return SimpleDateFormat.PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE[patternCharIndex];
        }
        return null;
    }
    
    protected String subFormat(final char ch, final int count, final int beginOffset, final FieldPosition pos, final DateFormatSymbols fmtData, final Calendar cal) throws IllegalArgumentException {
        return this.subFormat(ch, count, beginOffset, 0, DisplayContext.CAPITALIZATION_NONE, pos, cal);
    }
    
    @Deprecated
    protected String subFormat(final char ch, final int count, final int beginOffset, final int fieldNum, final DisplayContext capitalizationContext, final FieldPosition pos, final Calendar cal) {
        final StringBuffer buf = new StringBuffer();
        this.subFormat(buf, ch, count, beginOffset, fieldNum, capitalizationContext, pos, cal);
        return buf.toString();
    }
    
    @Deprecated
    protected void subFormat(final StringBuffer buf, final char ch, final int count, final int beginOffset, final int fieldNum, final DisplayContext capitalizationContext, final FieldPosition pos, final Calendar cal) {
        final int maxIntCount = Integer.MAX_VALUE;
        final int bufstart = buf.length();
        final TimeZone tz = cal.getTimeZone();
        final long date = cal.getTimeInMillis();
        String result = null;
        int patternCharIndex = -1;
        if ('A' <= ch && ch <= 'z') {
            patternCharIndex = SimpleDateFormat.PATTERN_CHAR_TO_INDEX[ch - '@'];
        }
        if (patternCharIndex != -1) {
            final int field = SimpleDateFormat.PATTERN_INDEX_TO_CALENDAR_FIELD[patternCharIndex];
            int value = cal.get(field);
            final NumberFormat currentNumberFormat = this.getNumberFormat(ch);
            DateFormatSymbols.CapitalizationContextUsage capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.OTHER;
            switch (patternCharIndex) {
                case 0: {
                    if (cal.getType().equals("chinese")) {
                        this.zeroPaddingNumber(currentNumberFormat, buf, value, 1, 9);
                        break;
                    }
                    if (count == 5) {
                        safeAppend(this.formatData.narrowEras, value, buf);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.ERA_NARROW;
                        break;
                    }
                    if (count == 4) {
                        safeAppend(this.formatData.eraNames, value, buf);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.ERA_WIDE;
                        break;
                    }
                    safeAppend(this.formatData.eras, value, buf);
                    capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.ERA_ABBREV;
                    break;
                }
                case 30: {
                    if (this.formatData.shortYearNames != null && value <= this.formatData.shortYearNames.length) {
                        safeAppend(this.formatData.shortYearNames, value - 1, buf);
                        break;
                    }
                }
                case 1:
                case 18: {
                    if (this.override != null && (this.override.compareTo("hebr") == 0 || this.override.indexOf("y=hebr") >= 0) && value > 5000 && value < 6000) {
                        value -= 5000;
                    }
                    if (count == 2) {
                        this.zeroPaddingNumber(currentNumberFormat, buf, value, 2, 2);
                        break;
                    }
                    this.zeroPaddingNumber(currentNumberFormat, buf, value, count, Integer.MAX_VALUE);
                    break;
                }
                case 2:
                case 26: {
                    if (cal.getType().equals("hebrew")) {
                        final boolean isLeap = HebrewCalendar.isLeapYear(cal.get(1));
                        if (isLeap && value == 6 && count >= 3) {
                            value = 13;
                        }
                        if (!isLeap && value >= 6 && count < 3) {
                            --value;
                        }
                    }
                    final int isLeapMonth = (this.formatData.leapMonthPatterns != null && this.formatData.leapMonthPatterns.length >= 7) ? cal.get(22) : 0;
                    if (count == 5) {
                        if (patternCharIndex == 2) {
                            safeAppendWithMonthPattern(this.formatData.narrowMonths, value, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[2] : null);
                        }
                        else {
                            safeAppendWithMonthPattern(this.formatData.standaloneNarrowMonths, value, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[5] : null);
                        }
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.MONTH_NARROW;
                        break;
                    }
                    if (count == 4) {
                        if (patternCharIndex == 2) {
                            safeAppendWithMonthPattern(this.formatData.months, value, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[0] : null);
                            capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.MONTH_FORMAT;
                            break;
                        }
                        safeAppendWithMonthPattern(this.formatData.standaloneMonths, value, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[3] : null);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.MONTH_STANDALONE;
                        break;
                    }
                    else {
                        if (count != 3) {
                            final StringBuffer monthNumber = new StringBuffer();
                            this.zeroPaddingNumber(currentNumberFormat, monthNumber, value + 1, count, Integer.MAX_VALUE);
                            final String[] monthNumberStrings = { monthNumber.toString() };
                            safeAppendWithMonthPattern(monthNumberStrings, 0, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[6] : null);
                            break;
                        }
                        if (patternCharIndex == 2) {
                            safeAppendWithMonthPattern(this.formatData.shortMonths, value, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[1] : null);
                            capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.MONTH_FORMAT;
                            break;
                        }
                        safeAppendWithMonthPattern(this.formatData.standaloneShortMonths, value, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[4] : null);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.MONTH_STANDALONE;
                        break;
                    }
                    break;
                }
                case 4: {
                    if (value == 0) {
                        this.zeroPaddingNumber(currentNumberFormat, buf, cal.getMaximum(11) + 1, count, Integer.MAX_VALUE);
                        break;
                    }
                    this.zeroPaddingNumber(currentNumberFormat, buf, value, count, Integer.MAX_VALUE);
                    break;
                }
                case 8: {
                    this.numberFormat.setMinimumIntegerDigits(Math.min(3, count));
                    this.numberFormat.setMaximumIntegerDigits(Integer.MAX_VALUE);
                    if (count == 1) {
                        value /= 100;
                    }
                    else if (count == 2) {
                        value /= 10;
                    }
                    final FieldPosition p = new FieldPosition(-1);
                    this.numberFormat.format(value, buf, p);
                    if (count > 3) {
                        this.numberFormat.setMinimumIntegerDigits(count - 3);
                        this.numberFormat.format(0L, buf, p);
                    }
                    break;
                }
                case 19: {
                    if (count < 3) {
                        this.zeroPaddingNumber(currentNumberFormat, buf, value, count, Integer.MAX_VALUE);
                        break;
                    }
                    value = cal.get(7);
                }
                case 9: {
                    if (count == 5) {
                        safeAppend(this.formatData.narrowWeekdays, value, buf);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_NARROW;
                        break;
                    }
                    if (count == 4) {
                        safeAppend(this.formatData.weekdays, value, buf);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_FORMAT;
                        break;
                    }
                    if (count == 6 && this.formatData.shorterWeekdays != null) {
                        safeAppend(this.formatData.shorterWeekdays, value, buf);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_FORMAT;
                        break;
                    }
                    safeAppend(this.formatData.shortWeekdays, value, buf);
                    capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_FORMAT;
                    break;
                }
                case 14: {
                    safeAppend(this.formatData.ampms, value, buf);
                    break;
                }
                case 15: {
                    if (value == 0) {
                        this.zeroPaddingNumber(currentNumberFormat, buf, cal.getLeastMaximum(10) + 1, count, Integer.MAX_VALUE);
                        break;
                    }
                    this.zeroPaddingNumber(currentNumberFormat, buf, value, count, Integer.MAX_VALUE);
                    break;
                }
                case 17: {
                    if (count < 4) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.SPECIFIC_SHORT, tz, date);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.METAZONE_SHORT;
                    }
                    else {
                        result = this.tzFormat().format(TimeZoneFormat.Style.SPECIFIC_LONG, tz, date);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.METAZONE_LONG;
                    }
                    buf.append(result);
                    break;
                }
                case 23: {
                    if (count < 4) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_LOCAL_FULL, tz, date);
                    }
                    else if (count == 5) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_FULL, tz, date);
                    }
                    else {
                        result = this.tzFormat().format(TimeZoneFormat.Style.LOCALIZED_GMT, tz, date);
                    }
                    buf.append(result);
                    break;
                }
                case 24: {
                    if (count == 1) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.GENERIC_SHORT, tz, date);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.METAZONE_SHORT;
                    }
                    else if (count == 4) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.GENERIC_LONG, tz, date);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.METAZONE_LONG;
                    }
                    buf.append(result);
                    break;
                }
                case 29: {
                    if (count == 1) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ZONE_ID_SHORT, tz, date);
                    }
                    else if (count == 2) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ZONE_ID, tz, date);
                    }
                    else if (count == 3) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.EXEMPLAR_LOCATION, tz, date);
                    }
                    else if (count == 4) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.GENERIC_LOCATION, tz, date);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.ZONE_LONG;
                    }
                    buf.append(result);
                    break;
                }
                case 31: {
                    if (count == 1) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.LOCALIZED_GMT_SHORT, tz, date);
                    }
                    else if (count == 4) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.LOCALIZED_GMT, tz, date);
                    }
                    buf.append(result);
                    break;
                }
                case 32: {
                    if (count == 1) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_SHORT, tz, date);
                    }
                    else if (count == 2) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_FIXED, tz, date);
                    }
                    else if (count == 3) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_FIXED, tz, date);
                    }
                    else if (count == 4) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_FULL, tz, date);
                    }
                    else if (count == 5) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_FULL, tz, date);
                    }
                    buf.append(result);
                    break;
                }
                case 33: {
                    if (count == 1) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_LOCAL_SHORT, tz, date);
                    }
                    else if (count == 2) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_LOCAL_FIXED, tz, date);
                    }
                    else if (count == 3) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_LOCAL_FIXED, tz, date);
                    }
                    else if (count == 4) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_LOCAL_FULL, tz, date);
                    }
                    else if (count == 5) {
                        result = this.tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_LOCAL_FULL, tz, date);
                    }
                    buf.append(result);
                    break;
                }
                case 25: {
                    if (count < 3) {
                        this.zeroPaddingNumber(currentNumberFormat, buf, value, 1, Integer.MAX_VALUE);
                        break;
                    }
                    value = cal.get(7);
                    if (count == 5) {
                        safeAppend(this.formatData.standaloneNarrowWeekdays, value, buf);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_NARROW;
                        break;
                    }
                    if (count == 4) {
                        safeAppend(this.formatData.standaloneWeekdays, value, buf);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_STANDALONE;
                        break;
                    }
                    if (count == 6 && this.formatData.standaloneShorterWeekdays != null) {
                        safeAppend(this.formatData.standaloneShorterWeekdays, value, buf);
                        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_STANDALONE;
                        break;
                    }
                    safeAppend(this.formatData.standaloneShortWeekdays, value, buf);
                    capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_STANDALONE;
                    break;
                }
                case 27: {
                    if (count >= 4) {
                        safeAppend(this.formatData.quarters, value / 3, buf);
                        break;
                    }
                    if (count == 3) {
                        safeAppend(this.formatData.shortQuarters, value / 3, buf);
                        break;
                    }
                    this.zeroPaddingNumber(currentNumberFormat, buf, value / 3 + 1, count, Integer.MAX_VALUE);
                    break;
                }
                case 28: {
                    if (count >= 4) {
                        safeAppend(this.formatData.standaloneQuarters, value / 3, buf);
                        break;
                    }
                    if (count == 3) {
                        safeAppend(this.formatData.standaloneShortQuarters, value / 3, buf);
                        break;
                    }
                    this.zeroPaddingNumber(currentNumberFormat, buf, value / 3 + 1, count, Integer.MAX_VALUE);
                    break;
                }
                default: {
                    this.zeroPaddingNumber(currentNumberFormat, buf, value, count, Integer.MAX_VALUE);
                    break;
                }
            }
            if (fieldNum == 0) {
                boolean titlecase = false;
                if (capitalizationContext != null) {
                    switch (capitalizationContext) {
                        case CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE: {
                            titlecase = true;
                            break;
                        }
                        case CAPITALIZATION_FOR_UI_LIST_OR_MENU:
                        case CAPITALIZATION_FOR_STANDALONE: {
                            if (this.formatData.capitalization != null) {
                                final boolean[] transforms = this.formatData.capitalization.get(capContextUsageType);
                                titlecase = ((capitalizationContext == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU) ? transforms[0] : transforms[1]);
                                break;
                            }
                            break;
                        }
                    }
                }
                if (titlecase) {
                    final String firstField = buf.substring(bufstart);
                    final String firstFieldTitleCase = UCharacter.toTitleCase(this.locale, firstField, null, 768);
                    buf.replace(bufstart, buf.length(), firstFieldTitleCase);
                }
            }
            if (pos.getBeginIndex() == pos.getEndIndex()) {
                if (pos.getField() == SimpleDateFormat.PATTERN_INDEX_TO_DATE_FORMAT_FIELD[patternCharIndex]) {
                    pos.setBeginIndex(beginOffset);
                    pos.setEndIndex(beginOffset + buf.length() - bufstart);
                }
                else if (pos.getFieldAttribute() == SimpleDateFormat.PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE[patternCharIndex]) {
                    pos.setBeginIndex(beginOffset);
                    pos.setEndIndex(beginOffset + buf.length() - bufstart);
                }
            }
            return;
        }
        if (ch == 'l') {
            return;
        }
        throw new IllegalArgumentException("Illegal pattern character '" + ch + "' in \"" + this.pattern + '\"');
    }
    
    private static void safeAppend(final String[] array, final int value, final StringBuffer appendTo) {
        if (array != null && value >= 0 && value < array.length) {
            appendTo.append(array[value]);
        }
    }
    
    private static void safeAppendWithMonthPattern(final String[] array, final int value, final StringBuffer appendTo, final String monthPattern) {
        if (array != null && value >= 0 && value < array.length) {
            if (monthPattern == null) {
                appendTo.append(array[value]);
            }
            else {
                appendTo.append(MessageFormat.format(monthPattern, array[value]));
            }
        }
    }
    
    private Object[] getPatternItems() {
        if (this.patternItems != null) {
            return this.patternItems;
        }
        this.patternItems = SimpleDateFormat.PARSED_PATTERN_CACHE.get(this.pattern);
        if (this.patternItems != null) {
            return this.patternItems;
        }
        boolean isPrevQuote = false;
        boolean inQuote = false;
        final StringBuilder text = new StringBuilder();
        char itemType = '\0';
        int itemLength = 1;
        final List<Object> items = new ArrayList<Object>();
        for (int i = 0; i < this.pattern.length(); ++i) {
            final char ch = this.pattern.charAt(i);
            if (ch == '\'') {
                if (isPrevQuote) {
                    text.append('\'');
                    isPrevQuote = false;
                }
                else {
                    isPrevQuote = true;
                    if (itemType != '\0') {
                        items.add(new PatternItem(itemType, itemLength));
                        itemType = '\0';
                    }
                }
                inQuote = !inQuote;
            }
            else {
                isPrevQuote = false;
                if (inQuote) {
                    text.append(ch);
                }
                else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                    if (ch == itemType) {
                        ++itemLength;
                    }
                    else {
                        if (itemType == '\0') {
                            if (text.length() > 0) {
                                items.add(text.toString());
                                text.setLength(0);
                            }
                        }
                        else {
                            items.add(new PatternItem(itemType, itemLength));
                        }
                        itemType = ch;
                        itemLength = 1;
                    }
                }
                else {
                    if (itemType != '\0') {
                        items.add(new PatternItem(itemType, itemLength));
                        itemType = '\0';
                    }
                    text.append(ch);
                }
            }
        }
        if (itemType == '\0') {
            if (text.length() > 0) {
                items.add(text.toString());
                text.setLength(0);
            }
        }
        else {
            items.add(new PatternItem(itemType, itemLength));
        }
        this.patternItems = items.toArray(new Object[items.size()]);
        SimpleDateFormat.PARSED_PATTERN_CACHE.put(this.pattern, this.patternItems);
        return this.patternItems;
    }
    
    @Deprecated
    protected void zeroPaddingNumber(final NumberFormat nf, final StringBuffer buf, final int value, final int minDigits, final int maxDigits) {
        if (this.useLocalZeroPaddingNumberFormat && value >= 0) {
            this.fastZeroPaddingNumber(buf, value, minDigits, maxDigits);
        }
        else {
            nf.setMinimumIntegerDigits(minDigits);
            nf.setMaximumIntegerDigits(maxDigits);
            nf.format(value, buf, new FieldPosition(-1));
        }
    }
    
    @Override
    public void setNumberFormat(final NumberFormat newNumberFormat) {
        super.setNumberFormat(newNumberFormat);
        this.initLocalZeroPaddingNumberFormat();
        this.initializeTimeZoneFormat(true);
    }
    
    private void initLocalZeroPaddingNumberFormat() {
        if (this.numberFormat instanceof DecimalFormat) {
            this.decDigits = ((DecimalFormat)this.numberFormat).getDecimalFormatSymbols().getDigits();
            this.useLocalZeroPaddingNumberFormat = true;
        }
        else if (this.numberFormat instanceof DateNumberFormat) {
            this.decDigits = ((DateNumberFormat)this.numberFormat).getDigits();
            this.useLocalZeroPaddingNumberFormat = true;
        }
        else {
            this.useLocalZeroPaddingNumberFormat = false;
        }
        if (this.useLocalZeroPaddingNumberFormat) {
            this.decimalBuf = new char[10];
        }
    }
    
    private void fastZeroPaddingNumber(final StringBuffer buf, int value, final int minDigits, final int maxDigits) {
        final int limit = (this.decimalBuf.length < maxDigits) ? this.decimalBuf.length : maxDigits;
        int index = limit - 1;
        while (true) {
            this.decimalBuf[index] = this.decDigits[value % 10];
            value /= 10;
            if (index == 0 || value == 0) {
                break;
            }
            --index;
        }
        int padding;
        for (padding = minDigits - (limit - index); padding > 0 && index > 0; this.decimalBuf[--index] = this.decDigits[0], --padding) {}
        while (padding > 0) {
            buf.append(this.decDigits[0]);
            --padding;
        }
        buf.append(this.decimalBuf, index, limit - index);
    }
    
    protected String zeroPaddingNumber(final long value, final int minDigits, final int maxDigits) {
        this.numberFormat.setMinimumIntegerDigits(minDigits);
        this.numberFormat.setMaximumIntegerDigits(maxDigits);
        return this.numberFormat.format(value);
    }
    
    private static final boolean isNumeric(final char formatChar, final int count) {
        final int i = "MYyudehHmsSDFwWkK".indexOf(formatChar);
        return i > 0 || (i == 0 && count < 3);
    }
    
    @Override
    public void parse(final String text, Calendar cal, final ParsePosition parsePos) {
        TimeZone backupTZ = null;
        Calendar resultCal = null;
        if (cal != this.calendar && !cal.getType().equals(this.calendar.getType())) {
            this.calendar.setTimeInMillis(cal.getTimeInMillis());
            backupTZ = this.calendar.getTimeZone();
            this.calendar.setTimeZone(cal.getTimeZone());
            resultCal = cal;
            cal = this.calendar;
        }
        final int start;
        int pos = start = parsePos.getIndex();
        this.tztype = TimeZoneFormat.TimeType.UNKNOWN;
        final boolean[] ambiguousYear = { false };
        int numericFieldStart = -1;
        int numericFieldLength = 0;
        int numericStartPos = 0;
        MessageFormat numericLeapMonthFormatter = null;
        if (this.formatData.leapMonthPatterns != null && this.formatData.leapMonthPatterns.length >= 7) {
            numericLeapMonthFormatter = new MessageFormat(this.formatData.leapMonthPatterns[6], this.locale);
        }
        final Object[] items = this.getPatternItems();
        int i = 0;
        while (i < items.length) {
            if (items[i] instanceof PatternItem) {
                final PatternItem field = (PatternItem)items[i];
                if (field.isNumeric && numericFieldStart == -1 && i + 1 < items.length && items[i + 1] instanceof PatternItem && ((PatternItem)items[i + 1]).isNumeric) {
                    numericFieldStart = i;
                    numericFieldLength = field.length;
                    numericStartPos = pos;
                }
                if (numericFieldStart != -1) {
                    int len = field.length;
                    if (numericFieldStart == i) {
                        len = numericFieldLength;
                    }
                    pos = this.subParse(text, pos, field.type, len, true, false, ambiguousYear, cal, numericLeapMonthFormatter);
                    if (pos < 0) {
                        if (--numericFieldLength == 0) {
                            parsePos.setIndex(start);
                            parsePos.setErrorIndex(pos);
                            if (backupTZ != null) {
                                this.calendar.setTimeZone(backupTZ);
                            }
                            return;
                        }
                        i = numericFieldStart;
                        pos = numericStartPos;
                        continue;
                    }
                }
                else if (field.type != 'l') {
                    numericFieldStart = -1;
                    final int s = pos;
                    pos = this.subParse(text, pos, field.type, field.length, false, true, ambiguousYear, cal, numericLeapMonthFormatter);
                    if (pos < 0) {
                        if (pos != -32000) {
                            parsePos.setIndex(start);
                            parsePos.setErrorIndex(s);
                            if (backupTZ != null) {
                                this.calendar.setTimeZone(backupTZ);
                            }
                            return;
                        }
                        pos = s;
                        if (i + 1 < items.length) {
                            String patl = null;
                            try {
                                patl = (String)items[i + 1];
                            }
                            catch (ClassCastException cce) {
                                parsePos.setIndex(start);
                                parsePos.setErrorIndex(s);
                                if (backupTZ != null) {
                                    this.calendar.setTimeZone(backupTZ);
                                }
                                return;
                            }
                            if (patl == null) {
                                patl = (String)items[i + 1];
                            }
                            int plen;
                            int idx;
                            for (plen = patl.length(), idx = 0; idx < plen; ++idx) {
                                final char pch = patl.charAt(idx);
                                if (!PatternProps.isWhiteSpace(pch)) {
                                    break;
                                }
                            }
                            if (idx == plen) {
                                ++i;
                            }
                        }
                    }
                }
            }
            else {
                numericFieldStart = -1;
                final boolean[] complete = { false };
                pos = this.matchLiteral(text, pos, items, i, complete);
                if (!complete[0]) {
                    parsePos.setIndex(start);
                    parsePos.setErrorIndex(pos);
                    if (backupTZ != null) {
                        this.calendar.setTimeZone(backupTZ);
                    }
                    return;
                }
            }
            ++i;
        }
        if (pos < text.length()) {
            final char extra = text.charAt(pos);
            if (extra == '.' && this.isLenient() && items.length != 0) {
                final Object lastItem = items[items.length - 1];
                if (lastItem instanceof PatternItem && !((PatternItem)lastItem).isNumeric) {
                    ++pos;
                }
            }
        }
        parsePos.setIndex(pos);
        try {
            if (ambiguousYear[0] || this.tztype != TimeZoneFormat.TimeType.UNKNOWN) {
                if (ambiguousYear[0]) {
                    final Calendar copy = (Calendar)cal.clone();
                    final Date parsedDate = copy.getTime();
                    if (parsedDate.before(this.getDefaultCenturyStart())) {
                        cal.set(1, this.getDefaultCenturyStartYear() + 100);
                    }
                }
                if (this.tztype != TimeZoneFormat.TimeType.UNKNOWN) {
                    final Calendar copy = (Calendar)cal.clone();
                    final TimeZone tz = copy.getTimeZone();
                    BasicTimeZone btz = null;
                    if (tz instanceof BasicTimeZone) {
                        btz = (BasicTimeZone)tz;
                    }
                    copy.set(15, 0);
                    copy.set(16, 0);
                    final long localMillis = copy.getTimeInMillis();
                    final int[] offsets = new int[2];
                    if (btz != null) {
                        if (this.tztype == TimeZoneFormat.TimeType.STANDARD) {
                            btz.getOffsetFromLocal(localMillis, 1, 1, offsets);
                        }
                        else {
                            btz.getOffsetFromLocal(localMillis, 3, 3, offsets);
                        }
                    }
                    else {
                        tz.getOffset(localMillis, true, offsets);
                        if ((this.tztype == TimeZoneFormat.TimeType.STANDARD && offsets[1] != 0) || (this.tztype == TimeZoneFormat.TimeType.DAYLIGHT && offsets[1] == 0)) {
                            tz.getOffset(localMillis - 86400000L, true, offsets);
                        }
                    }
                    int resolvedSavings = offsets[1];
                    if (this.tztype == TimeZoneFormat.TimeType.STANDARD) {
                        if (offsets[1] != 0) {
                            resolvedSavings = 0;
                        }
                    }
                    else if (offsets[1] == 0) {
                        if (btz != null) {
                            long beforeT;
                            long afterT;
                            final long time = afterT = (beforeT = localMillis + offsets[0]);
                            int beforeSav = 0;
                            int afterSav = 0;
                            TimeZoneTransition beforeTrs;
                            do {
                                beforeTrs = btz.getPreviousTransition(beforeT, true);
                                if (beforeTrs == null) {
                                    break;
                                }
                                beforeT = beforeTrs.getTime() - 1L;
                                beforeSav = beforeTrs.getFrom().getDSTSavings();
                            } while (beforeSav == 0);
                            TimeZoneTransition afterTrs;
                            do {
                                afterTrs = btz.getNextTransition(afterT, false);
                                if (afterTrs == null) {
                                    break;
                                }
                                afterT = afterTrs.getTime();
                                afterSav = afterTrs.getTo().getDSTSavings();
                            } while (afterSav == 0);
                            if (beforeTrs != null && afterTrs != null) {
                                if (time - beforeT > afterT - time) {
                                    resolvedSavings = afterSav;
                                }
                                else {
                                    resolvedSavings = beforeSav;
                                }
                            }
                            else if (beforeTrs != null && beforeSav != 0) {
                                resolvedSavings = beforeSav;
                            }
                            else if (afterTrs != null && afterSav != 0) {
                                resolvedSavings = afterSav;
                            }
                            else {
                                resolvedSavings = btz.getDSTSavings();
                            }
                        }
                        else {
                            resolvedSavings = tz.getDSTSavings();
                        }
                        if (resolvedSavings == 0) {
                            resolvedSavings = 3600000;
                        }
                    }
                    cal.set(15, offsets[0]);
                    cal.set(16, resolvedSavings);
                }
            }
        }
        catch (IllegalArgumentException e) {
            parsePos.setErrorIndex(pos);
            parsePos.setIndex(start);
            if (backupTZ != null) {
                this.calendar.setTimeZone(backupTZ);
            }
            return;
        }
        if (resultCal != null) {
            resultCal.setTimeZone(cal.getTimeZone());
            resultCal.setTimeInMillis(cal.getTimeInMillis());
        }
        if (backupTZ != null) {
            this.calendar.setTimeZone(backupTZ);
        }
    }
    
    private int matchLiteral(final String text, int pos, final Object[] items, final int itemIndex, final boolean[] complete) {
        final int originalPos = pos;
        final String patternLiteral = (String)items[itemIndex];
        final int plen = patternLiteral.length();
        final int tlen = text.length();
        int idx = 0;
        while (idx < plen && pos < tlen) {
            final char pch = patternLiteral.charAt(idx);
            final char ich = text.charAt(pos);
            if (PatternProps.isWhiteSpace(pch) && PatternProps.isWhiteSpace(ich)) {
                while (idx + 1 < plen && PatternProps.isWhiteSpace(patternLiteral.charAt(idx + 1))) {
                    ++idx;
                }
                while (pos + 1 < tlen && PatternProps.isWhiteSpace(text.charAt(pos + 1))) {
                    ++pos;
                }
            }
            else if (pch != ich) {
                if (ich == '.' && pos == originalPos && 0 < itemIndex && this.isLenient()) {
                    final Object before = items[itemIndex - 1];
                    if (before instanceof PatternItem) {
                        final boolean isNumeric = ((PatternItem)before).isNumeric;
                        if (!isNumeric) {
                            ++pos;
                            continue;
                        }
                    }
                    break;
                }
                break;
            }
            ++idx;
            ++pos;
        }
        complete[0] = (idx == plen);
        if (!complete[0] && this.isLenient() && 0 < itemIndex && itemIndex < items.length - 1 && originalPos < tlen) {
            final Object before2 = items[itemIndex - 1];
            final Object after = items[itemIndex + 1];
            if (before2 instanceof PatternItem && after instanceof PatternItem) {
                final char beforeType = ((PatternItem)before2).type;
                final char afterType = ((PatternItem)after).type;
                if (SimpleDateFormat.DATE_PATTERN_TYPE.contains(beforeType) != SimpleDateFormat.DATE_PATTERN_TYPE.contains(afterType)) {
                    int newPos = originalPos;
                    while (true) {
                        final char ich2 = text.charAt(newPos);
                        if (!PatternProps.isWhiteSpace(ich2)) {
                            break;
                        }
                        ++newPos;
                    }
                    complete[0] = (newPos > originalPos);
                    pos = newPos;
                }
            }
        }
        return pos;
    }
    
    protected int matchString(final String text, final int start, final int field, final String[] data, final Calendar cal) {
        return this.matchString(text, start, field, data, null, cal);
    }
    
    protected int matchString(final String text, final int start, final int field, final String[] data, final String monthPattern, final Calendar cal) {
        int i = 0;
        final int count = data.length;
        if (field == 7) {
            i = 1;
        }
        int bestMatchLength = 0;
        int bestMatch = -1;
        int isLeapMonth = 0;
        int matchLength = 0;
        while (i < count) {
            int length = data[i].length();
            if (length > bestMatchLength && (matchLength = this.regionMatchesWithOptionalDot(text, start, data[i], length)) >= 0) {
                bestMatch = i;
                bestMatchLength = matchLength;
                isLeapMonth = 0;
            }
            if (monthPattern != null) {
                final String leapMonthName = MessageFormat.format(monthPattern, data[i]);
                length = leapMonthName.length();
                if (length > bestMatchLength && (matchLength = this.regionMatchesWithOptionalDot(text, start, leapMonthName, length)) >= 0) {
                    bestMatch = i;
                    bestMatchLength = matchLength;
                    isLeapMonth = 1;
                }
            }
            ++i;
        }
        if (bestMatch >= 0) {
            if (field == 1) {
                ++bestMatch;
            }
            cal.set(field, bestMatch);
            if (monthPattern != null) {
                cal.set(22, isLeapMonth);
            }
            return start + bestMatchLength;
        }
        return -start;
    }
    
    private int regionMatchesWithOptionalDot(final String text, final int start, final String data, final int length) {
        final boolean matches = text.regionMatches(true, start, data, 0, length);
        if (matches) {
            return length;
        }
        if (data.length() > 0 && data.charAt(data.length() - 1) == '.' && text.regionMatches(true, start, data, 0, length - 1)) {
            return length - 1;
        }
        return -1;
    }
    
    protected int matchQuarterString(final String text, final int start, final int field, final String[] data, final Calendar cal) {
        int i = 0;
        final int count = data.length;
        int bestMatchLength = 0;
        int bestMatch = -1;
        int matchLength = 0;
        while (i < count) {
            final int length = data[i].length();
            if (length > bestMatchLength && (matchLength = this.regionMatchesWithOptionalDot(text, start, data[i], length)) >= 0) {
                bestMatch = i;
                bestMatchLength = matchLength;
            }
            ++i;
        }
        if (bestMatch >= 0) {
            cal.set(field, bestMatch * 3);
            return start + bestMatchLength;
        }
        return -start;
    }
    
    protected int subParse(final String text, final int start, final char ch, final int count, final boolean obeyCount, final boolean allowNegative, final boolean[] ambiguousYear, final Calendar cal) {
        return this.subParse(text, start, ch, count, obeyCount, allowNegative, ambiguousYear, cal, null);
    }
    
    protected int subParse(final String text, int start, final char ch, final int count, final boolean obeyCount, final boolean allowNegative, final boolean[] ambiguousYear, final Calendar cal, final MessageFormat numericLeapMonthFormatter) {
        Number number = null;
        NumberFormat currentNumberFormat = null;
        int value = 0;
        final ParsePosition pos = new ParsePosition(0);
        final boolean lenient = this.isLenient();
        int patternCharIndex = -1;
        if ('A' <= ch && ch <= 'z') {
            patternCharIndex = SimpleDateFormat.PATTERN_CHAR_TO_INDEX[ch - '@'];
        }
        if (patternCharIndex == -1) {
            return -start;
        }
        currentNumberFormat = this.getNumberFormat(ch);
        final int field = SimpleDateFormat.PATTERN_INDEX_TO_CALENDAR_FIELD[patternCharIndex];
        if (numericLeapMonthFormatter != null) {
            numericLeapMonthFormatter.setFormatByArgumentIndex(0, currentNumberFormat);
        }
        while (start < text.length()) {
            final int c = UTF16.charAt(text, start);
            if (UCharacter.isUWhiteSpace(c) && PatternProps.isWhiteSpace(c)) {
                start += UTF16.getCharCount(c);
            }
            else {
                pos.setIndex(start);
                if (patternCharIndex == 4 || patternCharIndex == 15 || (patternCharIndex == 2 && count <= 2) || (patternCharIndex == 26 && count <= 2) || patternCharIndex == 1 || patternCharIndex == 18 || patternCharIndex == 30 || (patternCharIndex == 0 && cal.getType().equals("chinese")) || patternCharIndex == 8) {
                    boolean parsedNumericLeapMonth = false;
                    if (numericLeapMonthFormatter != null && (patternCharIndex == 2 || patternCharIndex == 26)) {
                        final Object[] args = numericLeapMonthFormatter.parse(text, pos);
                        if (args != null && pos.getIndex() > start && args[0] instanceof Number) {
                            parsedNumericLeapMonth = true;
                            number = (Number)args[0];
                            cal.set(22, 1);
                        }
                        else {
                            pos.setIndex(start);
                            cal.set(22, 0);
                        }
                    }
                    if (!parsedNumericLeapMonth) {
                        if (obeyCount) {
                            if (start + count > text.length()) {
                                return -start;
                            }
                            number = this.parseInt(text, count, pos, allowNegative, currentNumberFormat);
                        }
                        else {
                            number = this.parseInt(text, pos, allowNegative, currentNumberFormat);
                        }
                        if (number == null && patternCharIndex != 30) {
                            return -start;
                        }
                    }
                    if (number != null) {
                        value = number.intValue();
                    }
                }
                switch (patternCharIndex) {
                    case 0: {
                        if (cal.getType().equals("chinese")) {
                            cal.set(0, value);
                            return pos.getIndex();
                        }
                        int ps = 0;
                        if (count == 5) {
                            ps = this.matchString(text, start, 0, this.formatData.narrowEras, null, cal);
                        }
                        else if (count == 4) {
                            ps = this.matchString(text, start, 0, this.formatData.eraNames, null, cal);
                        }
                        else {
                            ps = this.matchString(text, start, 0, this.formatData.eras, null, cal);
                        }
                        if (ps == -start) {
                            ps = -32000;
                        }
                        return ps;
                    }
                    case 1:
                    case 18: {
                        if (this.override != null && (this.override.compareTo("hebr") == 0 || this.override.indexOf("y=hebr") >= 0) && value < 1000) {
                            value += 5000;
                        }
                        else if (count == 2 && pos.getIndex() - start == 2 && !cal.getType().equals("chinese") && UCharacter.isDigit(text.charAt(start)) && UCharacter.isDigit(text.charAt(start + 1))) {
                            final int ambiguousTwoDigitYear = this.getDefaultCenturyStartYear() % 100;
                            ambiguousYear[0] = (value == ambiguousTwoDigitYear);
                            value += this.getDefaultCenturyStartYear() / 100 * 100 + ((value < ambiguousTwoDigitYear) ? 100 : 0);
                        }
                        cal.set(field, value);
                        if (SimpleDateFormat.DelayedHebrewMonthCheck) {
                            if (!HebrewCalendar.isLeapYear(value)) {
                                cal.add(2, 1);
                            }
                            SimpleDateFormat.DelayedHebrewMonthCheck = false;
                        }
                        return pos.getIndex();
                    }
                    case 30: {
                        if (this.formatData.shortYearNames != null) {
                            final int newStart = this.matchString(text, start, 1, this.formatData.shortYearNames, null, cal);
                            if (newStart > 0) {
                                return newStart;
                            }
                        }
                        if (number != null && (lenient || this.formatData.shortYearNames == null || value > this.formatData.shortYearNames.length)) {
                            cal.set(1, value);
                            return pos.getIndex();
                        }
                        return -start;
                    }
                    case 2:
                    case 26: {
                        if (count <= 2) {
                            cal.set(2, value - 1);
                            if (cal.getType().equals("hebrew") && value >= 6) {
                                if (cal.isSet(1)) {
                                    if (!HebrewCalendar.isLeapYear(cal.get(1))) {
                                        cal.set(2, value);
                                    }
                                }
                                else {
                                    SimpleDateFormat.DelayedHebrewMonthCheck = true;
                                }
                            }
                            return pos.getIndex();
                        }
                        final boolean haveMonthPat = this.formatData.leapMonthPatterns != null && this.formatData.leapMonthPatterns.length >= 7;
                        final int newStart2 = (patternCharIndex == 2) ? this.matchString(text, start, 2, this.formatData.months, haveMonthPat ? this.formatData.leapMonthPatterns[0] : null, cal) : this.matchString(text, start, 2, this.formatData.standaloneMonths, haveMonthPat ? this.formatData.leapMonthPatterns[3] : null, cal);
                        if (newStart2 > 0) {
                            return newStart2;
                        }
                        return (patternCharIndex == 2) ? this.matchString(text, start, 2, this.formatData.shortMonths, haveMonthPat ? this.formatData.leapMonthPatterns[1] : null, cal) : this.matchString(text, start, 2, this.formatData.standaloneShortMonths, haveMonthPat ? this.formatData.leapMonthPatterns[4] : null, cal);
                    }
                    case 4: {
                        if (value == cal.getMaximum(11) + 1) {
                            value = 0;
                        }
                        cal.set(11, value);
                        return pos.getIndex();
                    }
                    case 8: {
                        int i = pos.getIndex() - start;
                        if (i < 3) {
                            while (i < 3) {
                                value *= 10;
                                ++i;
                            }
                        }
                        else {
                            int a = 1;
                            while (i > 3) {
                                a *= 10;
                                --i;
                            }
                            value /= a;
                        }
                        cal.set(14, value);
                        return pos.getIndex();
                    }
                    case 9: {
                        int newStart = this.matchString(text, start, 7, this.formatData.weekdays, null, cal);
                        if (newStart > 0) {
                            return newStart;
                        }
                        if ((newStart = this.matchString(text, start, 7, this.formatData.shortWeekdays, null, cal)) > 0) {
                            return newStart;
                        }
                        if (this.formatData.shorterWeekdays != null) {
                            return this.matchString(text, start, 7, this.formatData.shorterWeekdays, null, cal);
                        }
                        return newStart;
                    }
                    case 25: {
                        int newStart = this.matchString(text, start, 7, this.formatData.standaloneWeekdays, null, cal);
                        if (newStart > 0) {
                            return newStart;
                        }
                        if ((newStart = this.matchString(text, start, 7, this.formatData.standaloneShortWeekdays, null, cal)) > 0) {
                            return newStart;
                        }
                        if (this.formatData.standaloneShorterWeekdays != null) {
                            return this.matchString(text, start, 7, this.formatData.standaloneShorterWeekdays, null, cal);
                        }
                        return newStart;
                    }
                    case 14: {
                        return this.matchString(text, start, 9, this.formatData.ampms, null, cal);
                    }
                    case 15: {
                        if (value == cal.getLeastMaximum(10) + 1) {
                            value = 0;
                        }
                        cal.set(10, value);
                        return pos.getIndex();
                    }
                    case 17: {
                        final Output<TimeZoneFormat.TimeType> tzTimeType = new Output<TimeZoneFormat.TimeType>();
                        final TimeZoneFormat.Style style = (count < 4) ? TimeZoneFormat.Style.SPECIFIC_SHORT : TimeZoneFormat.Style.SPECIFIC_LONG;
                        final TimeZone tz = this.tzFormat().parse(style, text, pos, tzTimeType);
                        if (tz != null) {
                            this.tztype = tzTimeType.value;
                            cal.setTimeZone(tz);
                            return pos.getIndex();
                        }
                        return -start;
                    }
                    case 23: {
                        final Output<TimeZoneFormat.TimeType> tzTimeType = new Output<TimeZoneFormat.TimeType>();
                        final TimeZoneFormat.Style style = (count < 4) ? TimeZoneFormat.Style.ISO_BASIC_LOCAL_FULL : ((count == 5) ? TimeZoneFormat.Style.ISO_EXTENDED_FULL : TimeZoneFormat.Style.LOCALIZED_GMT);
                        final TimeZone tz = this.tzFormat().parse(style, text, pos, tzTimeType);
                        if (tz != null) {
                            this.tztype = tzTimeType.value;
                            cal.setTimeZone(tz);
                            return pos.getIndex();
                        }
                        return -start;
                    }
                    case 24: {
                        final Output<TimeZoneFormat.TimeType> tzTimeType = new Output<TimeZoneFormat.TimeType>();
                        final TimeZoneFormat.Style style = (count < 4) ? TimeZoneFormat.Style.GENERIC_SHORT : TimeZoneFormat.Style.GENERIC_LONG;
                        final TimeZone tz = this.tzFormat().parse(style, text, pos, tzTimeType);
                        if (tz != null) {
                            this.tztype = tzTimeType.value;
                            cal.setTimeZone(tz);
                            return pos.getIndex();
                        }
                        return -start;
                    }
                    case 29: {
                        final Output<TimeZoneFormat.TimeType> tzTimeType = new Output<TimeZoneFormat.TimeType>();
                        TimeZoneFormat.Style style = null;
                        switch (count) {
                            case 1: {
                                style = TimeZoneFormat.Style.ZONE_ID_SHORT;
                                break;
                            }
                            case 2: {
                                style = TimeZoneFormat.Style.ZONE_ID;
                                break;
                            }
                            case 3: {
                                style = TimeZoneFormat.Style.EXEMPLAR_LOCATION;
                                break;
                            }
                            default: {
                                style = TimeZoneFormat.Style.GENERIC_LOCATION;
                                break;
                            }
                        }
                        final TimeZone tz = this.tzFormat().parse(style, text, pos, tzTimeType);
                        if (tz != null) {
                            this.tztype = tzTimeType.value;
                            cal.setTimeZone(tz);
                            return pos.getIndex();
                        }
                        return -start;
                    }
                    case 31: {
                        final Output<TimeZoneFormat.TimeType> tzTimeType = new Output<TimeZoneFormat.TimeType>();
                        final TimeZoneFormat.Style style = (count < 4) ? TimeZoneFormat.Style.LOCALIZED_GMT_SHORT : TimeZoneFormat.Style.LOCALIZED_GMT;
                        final TimeZone tz = this.tzFormat().parse(style, text, pos, tzTimeType);
                        if (tz != null) {
                            this.tztype = tzTimeType.value;
                            cal.setTimeZone(tz);
                            return pos.getIndex();
                        }
                        return -start;
                    }
                    case 32: {
                        final Output<TimeZoneFormat.TimeType> tzTimeType = new Output<TimeZoneFormat.TimeType>();
                        TimeZoneFormat.Style style = null;
                        switch (count) {
                            case 1: {
                                style = TimeZoneFormat.Style.ISO_BASIC_SHORT;
                                break;
                            }
                            case 2: {
                                style = TimeZoneFormat.Style.ISO_BASIC_FIXED;
                                break;
                            }
                            case 3: {
                                style = TimeZoneFormat.Style.ISO_EXTENDED_FIXED;
                                break;
                            }
                            case 4: {
                                style = TimeZoneFormat.Style.ISO_BASIC_FULL;
                                break;
                            }
                            default: {
                                style = TimeZoneFormat.Style.ISO_EXTENDED_FULL;
                                break;
                            }
                        }
                        final TimeZone tz = this.tzFormat().parse(style, text, pos, tzTimeType);
                        if (tz != null) {
                            this.tztype = tzTimeType.value;
                            cal.setTimeZone(tz);
                            return pos.getIndex();
                        }
                        return -start;
                    }
                    case 33: {
                        final Output<TimeZoneFormat.TimeType> tzTimeType = new Output<TimeZoneFormat.TimeType>();
                        TimeZoneFormat.Style style = null;
                        switch (count) {
                            case 1: {
                                style = TimeZoneFormat.Style.ISO_BASIC_LOCAL_SHORT;
                                break;
                            }
                            case 2: {
                                style = TimeZoneFormat.Style.ISO_BASIC_LOCAL_FIXED;
                                break;
                            }
                            case 3: {
                                style = TimeZoneFormat.Style.ISO_EXTENDED_LOCAL_FIXED;
                                break;
                            }
                            case 4: {
                                style = TimeZoneFormat.Style.ISO_BASIC_LOCAL_FULL;
                                break;
                            }
                            default: {
                                style = TimeZoneFormat.Style.ISO_EXTENDED_LOCAL_FULL;
                                break;
                            }
                        }
                        final TimeZone tz = this.tzFormat().parse(style, text, pos, tzTimeType);
                        if (tz != null) {
                            this.tztype = tzTimeType.value;
                            cal.setTimeZone(tz);
                            return pos.getIndex();
                        }
                        return -start;
                    }
                    case 27: {
                        if (count <= 2) {
                            cal.set(2, (value - 1) * 3);
                            return pos.getIndex();
                        }
                        final int newStart = this.matchQuarterString(text, start, 2, this.formatData.quarters, cal);
                        if (newStart > 0) {
                            return newStart;
                        }
                        return this.matchQuarterString(text, start, 2, this.formatData.shortQuarters, cal);
                    }
                    case 28: {
                        if (count <= 2) {
                            cal.set(2, (value - 1) * 3);
                            return pos.getIndex();
                        }
                        final int newStart = this.matchQuarterString(text, start, 2, this.formatData.standaloneQuarters, cal);
                        if (newStart > 0) {
                            return newStart;
                        }
                        return this.matchQuarterString(text, start, 2, this.formatData.standaloneShortQuarters, cal);
                    }
                    default: {
                        if (obeyCount) {
                            if (start + count > text.length()) {
                                return -start;
                            }
                            number = this.parseInt(text, count, pos, allowNegative, currentNumberFormat);
                        }
                        else {
                            number = this.parseInt(text, pos, allowNegative, currentNumberFormat);
                        }
                        if (number != null) {
                            cal.set(field, number.intValue());
                            return pos.getIndex();
                        }
                        return -start;
                    }
                }
            }
        }
        return -start;
    }
    
    private Number parseInt(final String text, final ParsePosition pos, final boolean allowNegative, final NumberFormat fmt) {
        return this.parseInt(text, -1, pos, allowNegative, fmt);
    }
    
    private Number parseInt(final String text, final int maxDigits, final ParsePosition pos, final boolean allowNegative, final NumberFormat fmt) {
        final int oldPos = pos.getIndex();
        Number number;
        if (allowNegative) {
            number = fmt.parse(text, pos);
        }
        else if (fmt instanceof DecimalFormat) {
            final String oldPrefix = ((DecimalFormat)fmt).getNegativePrefix();
            ((DecimalFormat)fmt).setNegativePrefix("\uab00");
            number = fmt.parse(text, pos);
            ((DecimalFormat)fmt).setNegativePrefix(oldPrefix);
        }
        else {
            final boolean dateNumberFormat = fmt instanceof DateNumberFormat;
            if (dateNumberFormat) {
                ((DateNumberFormat)fmt).setParsePositiveOnly(true);
            }
            number = fmt.parse(text, pos);
            if (dateNumberFormat) {
                ((DateNumberFormat)fmt).setParsePositiveOnly(false);
            }
        }
        if (maxDigits > 0) {
            int nDigits = pos.getIndex() - oldPos;
            if (nDigits > maxDigits) {
                double val = number.doubleValue();
                for (nDigits -= maxDigits; nDigits > 0; --nDigits) {
                    val /= 10.0;
                }
                pos.setIndex(oldPos + maxDigits);
                number = (int)val;
            }
        }
        return number;
    }
    
    private String translatePattern(final String pat, final String from, final String to) {
        final StringBuilder result = new StringBuilder();
        boolean inQuote = false;
        for (int i = 0; i < pat.length(); ++i) {
            char c = pat.charAt(i);
            if (inQuote) {
                if (c == '\'') {
                    inQuote = false;
                }
            }
            else if (c == '\'') {
                inQuote = true;
            }
            else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                final int ci = from.indexOf(c);
                if (ci != -1) {
                    c = to.charAt(ci);
                }
            }
            result.append(c);
        }
        if (inQuote) {
            throw new IllegalArgumentException("Unfinished quote in pattern");
        }
        return result.toString();
    }
    
    public String toPattern() {
        return this.pattern;
    }
    
    public String toLocalizedPattern() {
        return this.translatePattern(this.pattern, "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXx", this.formatData.localPatternChars);
    }
    
    public void applyPattern(final String pat) {
        this.pattern = pat;
        this.setLocale(null, null);
        this.patternItems = null;
    }
    
    public void applyLocalizedPattern(final String pat) {
        this.pattern = this.translatePattern(pat, this.formatData.localPatternChars, "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXx");
        this.setLocale(null, null);
    }
    
    public DateFormatSymbols getDateFormatSymbols() {
        return (DateFormatSymbols)this.formatData.clone();
    }
    
    public void setDateFormatSymbols(final DateFormatSymbols newFormatSymbols) {
        this.formatData = (DateFormatSymbols)newFormatSymbols.clone();
    }
    
    protected DateFormatSymbols getSymbols() {
        return this.formatData;
    }
    
    public TimeZoneFormat getTimeZoneFormat() {
        return this.tzFormat().freeze();
    }
    
    public void setTimeZoneFormat(final TimeZoneFormat tzfmt) {
        if (tzfmt.isFrozen()) {
            this.tzFormat = tzfmt;
        }
        else {
            this.tzFormat = tzfmt.cloneAsThawed().freeze();
        }
    }
    
    public void setContext(final DisplayContext context) {
        if (context.type() == DisplayContext.Type.CAPITALIZATION) {
            this.capitalizationSetting = context;
        }
    }
    
    public DisplayContext getContext(final DisplayContext.Type type) {
        return (type == DisplayContext.Type.CAPITALIZATION && this.capitalizationSetting != null) ? this.capitalizationSetting : DisplayContext.CAPITALIZATION_NONE;
    }
    
    @Override
    public Object clone() {
        final SimpleDateFormat other = (SimpleDateFormat)super.clone();
        other.formatData = (DateFormatSymbols)this.formatData.clone();
        return other;
    }
    
    @Override
    public int hashCode() {
        return this.pattern.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        final SimpleDateFormat that = (SimpleDateFormat)obj;
        return this.pattern.equals(that.pattern) && this.formatData.equals(that.formatData);
    }
    
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        if (this.defaultCenturyStart == null) {
            this.initializeDefaultCenturyStart(this.defaultCenturyBase);
        }
        this.initializeTimeZoneFormat(false);
        stream.defaultWriteObject();
        stream.writeInt(this.capitalizationSetting.value());
    }
    
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final int capitalizationSettingValue = (this.serialVersionOnStream > 1) ? stream.readInt() : -1;
        if (this.serialVersionOnStream < 1) {
            this.defaultCenturyBase = System.currentTimeMillis();
        }
        else {
            this.parseAmbiguousDatesAsAfter(this.defaultCenturyStart);
        }
        this.serialVersionOnStream = 2;
        this.locale = this.getLocale(ULocale.VALID_LOCALE);
        if (this.locale == null) {
            this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
        }
        this.initLocalZeroPaddingNumberFormat();
        this.capitalizationSetting = DisplayContext.CAPITALIZATION_NONE;
        if (capitalizationSettingValue >= 0) {
            for (final DisplayContext context : DisplayContext.values()) {
                if (context.value() == capitalizationSettingValue) {
                    this.capitalizationSetting = context;
                    break;
                }
            }
        }
    }
    
    @Override
    public AttributedCharacterIterator formatToCharacterIterator(final Object obj) {
        Calendar cal = this.calendar;
        if (obj instanceof Calendar) {
            cal = (Calendar)obj;
        }
        else if (obj instanceof Date) {
            this.calendar.setTime((Date)obj);
        }
        else {
            if (!(obj instanceof Number)) {
                throw new IllegalArgumentException("Cannot format given Object as a Date");
            }
            this.calendar.setTimeInMillis(((Number)obj).longValue());
        }
        final StringBuffer toAppendTo = new StringBuffer();
        final FieldPosition pos = new FieldPosition(0);
        final List<FieldPosition> attributes = new ArrayList<FieldPosition>();
        this.format(cal, this.capitalizationSetting, toAppendTo, pos, attributes);
        final AttributedString as = new AttributedString(toAppendTo.toString());
        for (int i = 0; i < attributes.size(); ++i) {
            final FieldPosition fp = attributes.get(i);
            final Format.Field attribute = fp.getFieldAttribute();
            as.addAttribute(attribute, attribute, fp.getBeginIndex(), fp.getEndIndex());
        }
        return as.getIterator();
    }
    
    ULocale getLocale() {
        return this.locale;
    }
    
    boolean isFieldUnitIgnored(final int field) {
        return isFieldUnitIgnored(this.pattern, field);
    }
    
    static boolean isFieldUnitIgnored(final String pattern, final int field) {
        final int fieldLevel = SimpleDateFormat.CALENDAR_FIELD_TO_LEVEL[field];
        boolean inQuote = false;
        char prevCh = '\0';
        int count = 0;
        for (int i = 0; i < pattern.length(); ++i) {
            final char ch = pattern.charAt(i);
            if (ch != prevCh && count > 0) {
                final int level = SimpleDateFormat.PATTERN_CHAR_TO_LEVEL[prevCh - '@'];
                if (fieldLevel <= level) {
                    return false;
                }
                count = 0;
            }
            if (ch == '\'') {
                if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '\'') {
                    ++i;
                }
                else {
                    inQuote = !inQuote;
                }
            }
            else if (!inQuote && ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))) {
                prevCh = ch;
                ++count;
            }
        }
        if (count > 0) {
            final int level = SimpleDateFormat.PATTERN_CHAR_TO_LEVEL[prevCh - '@'];
            if (fieldLevel <= level) {
                return false;
            }
        }
        return true;
    }
    
    @Deprecated
    public final StringBuffer intervalFormatByAlgorithm(final Calendar fromCalendar, final Calendar toCalendar, final StringBuffer appendTo, final FieldPosition pos) throws IllegalArgumentException {
        if (!fromCalendar.isEquivalentTo(toCalendar)) {
            throw new IllegalArgumentException("can not format on two different calendars");
        }
        final Object[] items = this.getPatternItems();
        int diffBegin = -1;
        int diffEnd = -1;
        try {
            for (int i = 0; i < items.length; ++i) {
                if (this.diffCalFieldValue(fromCalendar, toCalendar, items, i)) {
                    diffBegin = i;
                    break;
                }
            }
            if (diffBegin == -1) {
                return this.format(fromCalendar, appendTo, pos);
            }
            for (int i = items.length - 1; i >= diffBegin; --i) {
                if (this.diffCalFieldValue(fromCalendar, toCalendar, items, i)) {
                    diffEnd = i;
                    break;
                }
            }
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.toString());
        }
        if (diffBegin == 0 && diffEnd == items.length - 1) {
            this.format(fromCalendar, appendTo, pos);
            appendTo.append(" \u2013 ");
            this.format(toCalendar, appendTo, pos);
            return appendTo;
        }
        int highestLevel = 1000;
        for (int j = diffBegin; j <= diffEnd; ++j) {
            if (!(items[j] instanceof String)) {
                final PatternItem item = (PatternItem)items[j];
                final char ch = item.type;
                int patternCharIndex = -1;
                if ('A' <= ch && ch <= 'z') {
                    patternCharIndex = SimpleDateFormat.PATTERN_CHAR_TO_LEVEL[ch - '@'];
                }
                if (patternCharIndex == -1) {
                    throw new IllegalArgumentException("Illegal pattern character '" + ch + "' in \"" + this.pattern + '\"');
                }
                if (patternCharIndex < highestLevel) {
                    highestLevel = patternCharIndex;
                }
            }
        }
        try {
            for (int j = 0; j < diffBegin; ++j) {
                if (this.lowerLevel(items, j, highestLevel)) {
                    diffBegin = j;
                    break;
                }
            }
            for (int j = items.length - 1; j > diffEnd; --j) {
                if (this.lowerLevel(items, j, highestLevel)) {
                    diffEnd = j;
                    break;
                }
            }
        }
        catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException(e2.toString());
        }
        if (diffBegin == 0 && diffEnd == items.length - 1) {
            this.format(fromCalendar, appendTo, pos);
            appendTo.append(" \u2013 ");
            this.format(toCalendar, appendTo, pos);
            return appendTo;
        }
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        for (int j = 0; j <= diffEnd; ++j) {
            if (items[j] instanceof String) {
                appendTo.append((String)items[j]);
            }
            else {
                final PatternItem item = (PatternItem)items[j];
                if (this.useFastFormat) {
                    this.subFormat(appendTo, item.type, item.length, appendTo.length(), j, this.capitalizationSetting, pos, fromCalendar);
                }
                else {
                    appendTo.append(this.subFormat(item.type, item.length, appendTo.length(), j, this.capitalizationSetting, pos, fromCalendar));
                }
            }
        }
        appendTo.append(" \u2013 ");
        for (int j = diffBegin; j < items.length; ++j) {
            if (items[j] instanceof String) {
                appendTo.append((String)items[j]);
            }
            else {
                final PatternItem item = (PatternItem)items[j];
                if (this.useFastFormat) {
                    this.subFormat(appendTo, item.type, item.length, appendTo.length(), j, this.capitalizationSetting, pos, toCalendar);
                }
                else {
                    appendTo.append(this.subFormat(item.type, item.length, appendTo.length(), j, this.capitalizationSetting, pos, toCalendar));
                }
            }
        }
        return appendTo;
    }
    
    private boolean diffCalFieldValue(final Calendar fromCalendar, final Calendar toCalendar, final Object[] items, final int i) throws IllegalArgumentException {
        if (items[i] instanceof String) {
            return false;
        }
        final PatternItem item = (PatternItem)items[i];
        final char ch = item.type;
        int patternCharIndex = -1;
        if ('A' <= ch && ch <= 'z') {
            patternCharIndex = SimpleDateFormat.PATTERN_CHAR_TO_INDEX[ch - '@'];
        }
        if (patternCharIndex == -1) {
            throw new IllegalArgumentException("Illegal pattern character '" + ch + "' in \"" + this.pattern + '\"');
        }
        final int field = SimpleDateFormat.PATTERN_INDEX_TO_CALENDAR_FIELD[patternCharIndex];
        final int value = fromCalendar.get(field);
        final int value_2 = toCalendar.get(field);
        return value != value_2;
    }
    
    private boolean lowerLevel(final Object[] items, final int i, final int level) throws IllegalArgumentException {
        if (items[i] instanceof String) {
            return false;
        }
        final PatternItem item = (PatternItem)items[i];
        final char ch = item.type;
        int patternCharIndex = -1;
        if ('A' <= ch && ch <= 'z') {
            patternCharIndex = SimpleDateFormat.PATTERN_CHAR_TO_LEVEL[ch - '@'];
        }
        if (patternCharIndex == -1) {
            throw new IllegalArgumentException("Illegal pattern character '" + ch + "' in \"" + this.pattern + '\"');
        }
        return patternCharIndex >= level;
    }
    
    @Deprecated
    protected NumberFormat getNumberFormat(final char ch) {
        final Character ovrField = ch;
        if (this.overrideMap != null && this.overrideMap.containsKey(ovrField)) {
            final String nsName = this.overrideMap.get(ovrField).toString();
            final NumberFormat nf = this.numberFormatters.get(nsName);
            return nf;
        }
        return this.numberFormat;
    }
    
    private void initNumberFormatters(final ULocale loc) {
        this.numberFormatters = new HashMap<String, NumberFormat>();
        this.overrideMap = new HashMap<Character, String>();
        this.processOverrideString(loc, this.override);
    }
    
    private void processOverrideString(final ULocale loc, final String str) {
        if (str == null || str.length() == 0) {
            return;
        }
        int start = 0;
        boolean moreToProcess = true;
        while (moreToProcess) {
            final int delimiterPosition = str.indexOf(";", start);
            int end;
            if (delimiterPosition == -1) {
                moreToProcess = false;
                end = str.length();
            }
            else {
                end = delimiterPosition;
            }
            final String currentString = str.substring(start, end);
            final int equalSignPosition = currentString.indexOf("=");
            String nsName;
            boolean fullOverride;
            if (equalSignPosition == -1) {
                nsName = currentString;
                fullOverride = true;
            }
            else {
                nsName = currentString.substring(equalSignPosition + 1);
                final Character ovrField = currentString.charAt(0);
                this.overrideMap.put(ovrField, nsName);
                fullOverride = false;
            }
            final ULocale ovrLoc = new ULocale(loc.getBaseName() + "@numbers=" + nsName);
            final NumberFormat nf = NumberFormat.createInstance(ovrLoc, 0);
            nf.setGroupingUsed(false);
            if (fullOverride) {
                this.setNumberFormat(nf);
            }
            else {
                this.useLocalZeroPaddingNumberFormat = false;
            }
            if (!this.numberFormatters.containsKey(nsName)) {
                this.numberFormatters.put(nsName, nf);
            }
            start = delimiterPosition + 1;
        }
    }
    
    static {
        SimpleDateFormat.DelayedHebrewMonthCheck = false;
        CALENDAR_FIELD_TO_LEVEL = new int[] { 0, 10, 20, 20, 30, 30, 20, 30, 30, 40, 50, 50, 60, 70, 80, 0, 0, 10, 30, 10, 0, 40 };
        PATTERN_CHAR_TO_LEVEL = new int[] { -1, 40, -1, -1, 20, 30, 30, 0, 50, -1, -1, 50, 20, 20, -1, 0, -1, 20, -1, 80, -1, 10, 0, 30, 0, 10, 0, -1, -1, -1, -1, -1, -1, 40, -1, 30, 30, 30, -1, 0, 50, -1, -1, 50, -1, 60, -1, -1, -1, 20, -1, 70, -1, 10, 0, 20, 0, 10, 0, -1, -1, -1, -1, -1 };
        SimpleDateFormat.cachedDefaultLocale = null;
        SimpleDateFormat.cachedDefaultPattern = null;
        PATTERN_CHAR_TO_INDEX = new int[] { -1, 22, -1, -1, 10, 9, 11, 0, 5, -1, -1, 16, 26, 2, -1, 31, -1, 27, -1, 8, -1, 30, 29, 13, 32, 18, 23, -1, -1, -1, -1, -1, -1, 14, -1, 25, 3, 19, -1, 21, 15, -1, -1, 4, -1, 6, -1, -1, -1, 28, -1, 7, -1, 20, 24, 12, 33, 1, 17, -1, -1, -1, -1, -1 };
        PATTERN_INDEX_TO_CALENDAR_FIELD = new int[] { 0, 1, 2, 5, 11, 11, 12, 13, 14, 7, 6, 8, 3, 4, 9, 10, 10, 15, 17, 18, 19, 20, 21, 15, 15, 18, 2, 2, 2, 15, 1, 15, 15, 15 };
        PATTERN_INDEX_TO_DATE_FORMAT_FIELD = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33 };
        PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE = new Field[] { Field.ERA, Field.YEAR, Field.MONTH, Field.DAY_OF_MONTH, Field.HOUR_OF_DAY1, Field.HOUR_OF_DAY0, Field.MINUTE, Field.SECOND, Field.MILLISECOND, Field.DAY_OF_WEEK, Field.DAY_OF_YEAR, Field.DAY_OF_WEEK_IN_MONTH, Field.WEEK_OF_YEAR, Field.WEEK_OF_MONTH, Field.AM_PM, Field.HOUR1, Field.HOUR0, Field.TIME_ZONE, Field.YEAR_WOY, Field.DOW_LOCAL, Field.EXTENDED_YEAR, Field.JULIAN_DAY, Field.MILLISECONDS_IN_DAY, Field.TIME_ZONE, Field.TIME_ZONE, Field.DAY_OF_WEEK, Field.MONTH, Field.QUARTER, Field.QUARTER, Field.TIME_ZONE, Field.YEAR, Field.TIME_ZONE, Field.TIME_ZONE, Field.TIME_ZONE };
        SimpleDateFormat.PARSED_PATTERN_CACHE = new SimpleCache<String, Object[]>();
        DATE_PATTERN_TYPE = new UnicodeSet("[GyYuUQqMLlwWd]").freeze();
    }
    
    private enum ContextValue
    {
        UNKNOWN, 
        CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE, 
        CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE, 
        CAPITALIZATION_FOR_UI_LIST_OR_MENU, 
        CAPITALIZATION_FOR_STANDALONE;
    }
    
    private static class PatternItem
    {
        final char type;
        final int length;
        final boolean isNumeric;
        
        PatternItem(final char type, final int length) {
            this.type = type;
            this.length = length;
            this.isNumeric = isNumeric(type, length);
        }
    }
}
