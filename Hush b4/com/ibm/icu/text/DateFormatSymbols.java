// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.SimpleCache;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ResourceBundle;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.HashMap;
import com.ibm.icu.util.UResourceBundle;
import java.util.MissingResourceException;
import com.ibm.icu.impl.CalendarData;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.CalendarUtil;
import java.util.Locale;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.impl.ICUCache;
import java.util.Map;
import java.io.Serializable;

public class DateFormatSymbols implements Serializable, Cloneable
{
    public static final int FORMAT = 0;
    public static final int STANDALONE = 1;
    @Deprecated
    public static final int DT_CONTEXT_COUNT = 2;
    public static final int ABBREVIATED = 0;
    public static final int WIDE = 1;
    public static final int NARROW = 2;
    public static final int SHORT = 3;
    @Deprecated
    public static final int DT_WIDTH_COUNT = 4;
    static final int DT_LEAP_MONTH_PATTERN_FORMAT_WIDE = 0;
    static final int DT_LEAP_MONTH_PATTERN_FORMAT_ABBREV = 1;
    static final int DT_LEAP_MONTH_PATTERN_FORMAT_NARROW = 2;
    static final int DT_LEAP_MONTH_PATTERN_STANDALONE_WIDE = 3;
    static final int DT_LEAP_MONTH_PATTERN_STANDALONE_ABBREV = 4;
    static final int DT_LEAP_MONTH_PATTERN_STANDALONE_NARROW = 5;
    static final int DT_LEAP_MONTH_PATTERN_NUMERIC = 6;
    static final int DT_MONTH_PATTERN_COUNT = 7;
    String[] eras;
    String[] eraNames;
    String[] narrowEras;
    String[] months;
    String[] shortMonths;
    String[] narrowMonths;
    String[] standaloneMonths;
    String[] standaloneShortMonths;
    String[] standaloneNarrowMonths;
    String[] weekdays;
    String[] shortWeekdays;
    String[] shorterWeekdays;
    String[] narrowWeekdays;
    String[] standaloneWeekdays;
    String[] standaloneShortWeekdays;
    String[] standaloneShorterWeekdays;
    String[] standaloneNarrowWeekdays;
    String[] ampms;
    String[] shortQuarters;
    String[] quarters;
    String[] standaloneShortQuarters;
    String[] standaloneQuarters;
    String[] leapMonthPatterns;
    String[] shortYearNames;
    private String[][] zoneStrings;
    static final String patternChars = "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXx";
    String localPatternChars;
    private static final long serialVersionUID = -5987973545549424702L;
    private static final String[][] CALENDAR_CLASSES;
    private static final Map<String, CapitalizationContextUsage> contextUsageTypeMap;
    Map<CapitalizationContextUsage, boolean[]> capitalization;
    static final int millisPerHour = 3600000;
    private static ICUCache<String, DateFormatSymbols> DFSCACHE;
    private ULocale requestedLocale;
    private ULocale validLocale;
    private ULocale actualLocale;
    
    public DateFormatSymbols() {
        this(ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public DateFormatSymbols(final Locale locale) {
        this(ULocale.forLocale(locale));
    }
    
    public DateFormatSymbols(final ULocale locale) {
        this.eras = null;
        this.eraNames = null;
        this.narrowEras = null;
        this.months = null;
        this.shortMonths = null;
        this.narrowMonths = null;
        this.standaloneMonths = null;
        this.standaloneShortMonths = null;
        this.standaloneNarrowMonths = null;
        this.weekdays = null;
        this.shortWeekdays = null;
        this.shorterWeekdays = null;
        this.narrowWeekdays = null;
        this.standaloneWeekdays = null;
        this.standaloneShortWeekdays = null;
        this.standaloneShorterWeekdays = null;
        this.standaloneNarrowWeekdays = null;
        this.ampms = null;
        this.shortQuarters = null;
        this.quarters = null;
        this.standaloneShortQuarters = null;
        this.standaloneQuarters = null;
        this.leapMonthPatterns = null;
        this.shortYearNames = null;
        this.zoneStrings = null;
        this.localPatternChars = null;
        this.capitalization = null;
        this.initializeData(locale, CalendarUtil.getCalendarType(locale));
    }
    
    public static DateFormatSymbols getInstance() {
        return new DateFormatSymbols();
    }
    
    public static DateFormatSymbols getInstance(final Locale locale) {
        return new DateFormatSymbols(locale);
    }
    
    public static DateFormatSymbols getInstance(final ULocale locale) {
        return new DateFormatSymbols(locale);
    }
    
    public static Locale[] getAvailableLocales() {
        return ICUResourceBundle.getAvailableLocales();
    }
    
    public static ULocale[] getAvailableULocales() {
        return ICUResourceBundle.getAvailableULocales();
    }
    
    public String[] getEras() {
        return this.duplicate(this.eras);
    }
    
    public void setEras(final String[] newEras) {
        this.eras = this.duplicate(newEras);
    }
    
    public String[] getEraNames() {
        return this.duplicate(this.eraNames);
    }
    
    public void setEraNames(final String[] newEraNames) {
        this.eraNames = this.duplicate(newEraNames);
    }
    
    public String[] getMonths() {
        return this.duplicate(this.months);
    }
    
    public String[] getMonths(final int context, final int width) {
        String[] returnValue = null;
        Label_0137: {
            switch (context) {
                case 0: {
                    switch (width) {
                        case 1: {
                            returnValue = this.months;
                            break;
                        }
                        case 0:
                        case 3: {
                            returnValue = this.shortMonths;
                            break;
                        }
                        case 2: {
                            returnValue = this.narrowMonths;
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (width) {
                        case 1: {
                            returnValue = this.standaloneMonths;
                            break Label_0137;
                        }
                        case 0:
                        case 3: {
                            returnValue = this.standaloneShortMonths;
                            break Label_0137;
                        }
                        case 2: {
                            returnValue = this.standaloneNarrowMonths;
                            break Label_0137;
                        }
                    }
                    break;
                }
            }
        }
        return this.duplicate(returnValue);
    }
    
    public void setMonths(final String[] newMonths) {
        this.months = this.duplicate(newMonths);
    }
    
    public void setMonths(final String[] newMonths, final int context, final int width) {
        Label_0160: {
            switch (context) {
                case 0: {
                    switch (width) {
                        case 1: {
                            this.months = this.duplicate(newMonths);
                            break Label_0160;
                        }
                        case 0: {
                            this.shortMonths = this.duplicate(newMonths);
                            break Label_0160;
                        }
                        case 2: {
                            this.narrowMonths = this.duplicate(newMonths);
                            break Label_0160;
                        }
                        default: {
                            break Label_0160;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (width) {
                        case 1: {
                            this.standaloneMonths = this.duplicate(newMonths);
                            break Label_0160;
                        }
                        case 0: {
                            this.standaloneShortMonths = this.duplicate(newMonths);
                            break Label_0160;
                        }
                        case 2: {
                            this.standaloneNarrowMonths = this.duplicate(newMonths);
                            break Label_0160;
                        }
                    }
                    break;
                }
            }
        }
    }
    
    public String[] getShortMonths() {
        return this.duplicate(this.shortMonths);
    }
    
    public void setShortMonths(final String[] newShortMonths) {
        this.shortMonths = this.duplicate(newShortMonths);
    }
    
    public String[] getWeekdays() {
        return this.duplicate(this.weekdays);
    }
    
    public String[] getWeekdays(final int context, final int width) {
        String[] returnValue = null;
        Label_0179: {
            switch (context) {
                case 0: {
                    switch (width) {
                        case 1: {
                            returnValue = this.weekdays;
                            break;
                        }
                        case 0: {
                            returnValue = this.shortWeekdays;
                            break;
                        }
                        case 3: {
                            returnValue = ((this.shorterWeekdays != null) ? this.shorterWeekdays : this.shortWeekdays);
                            break;
                        }
                        case 2: {
                            returnValue = this.narrowWeekdays;
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (width) {
                        case 1: {
                            returnValue = this.standaloneWeekdays;
                            break Label_0179;
                        }
                        case 0: {
                            returnValue = this.standaloneShortWeekdays;
                            break Label_0179;
                        }
                        case 3: {
                            returnValue = ((this.standaloneShorterWeekdays != null) ? this.standaloneShorterWeekdays : this.standaloneShortWeekdays);
                            break Label_0179;
                        }
                        case 2: {
                            returnValue = this.standaloneNarrowWeekdays;
                            break Label_0179;
                        }
                    }
                    break;
                }
            }
        }
        return this.duplicate(returnValue);
    }
    
    public void setWeekdays(final String[] newWeekdays, final int context, final int width) {
        Label_0185: {
            switch (context) {
                case 0: {
                    switch (width) {
                        case 1: {
                            this.weekdays = this.duplicate(newWeekdays);
                            break;
                        }
                        case 0: {
                            this.shortWeekdays = this.duplicate(newWeekdays);
                            break;
                        }
                        case 3: {
                            this.shorterWeekdays = this.duplicate(newWeekdays);
                            break;
                        }
                        case 2: {
                            this.narrowWeekdays = this.duplicate(newWeekdays);
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (width) {
                        case 1: {
                            this.standaloneWeekdays = this.duplicate(newWeekdays);
                            break Label_0185;
                        }
                        case 0: {
                            this.standaloneShortWeekdays = this.duplicate(newWeekdays);
                            break Label_0185;
                        }
                        case 3: {
                            this.standaloneShorterWeekdays = this.duplicate(newWeekdays);
                            break Label_0185;
                        }
                        case 2: {
                            this.standaloneNarrowWeekdays = this.duplicate(newWeekdays);
                            break Label_0185;
                        }
                    }
                    break;
                }
            }
        }
    }
    
    public void setWeekdays(final String[] newWeekdays) {
        this.weekdays = this.duplicate(newWeekdays);
    }
    
    public String[] getShortWeekdays() {
        return this.duplicate(this.shortWeekdays);
    }
    
    public void setShortWeekdays(final String[] newAbbrevWeekdays) {
        this.shortWeekdays = this.duplicate(newAbbrevWeekdays);
    }
    
    public String[] getQuarters(final int context, final int width) {
        String[] returnValue = null;
        Label_0130: {
            switch (context) {
                case 0: {
                    switch (width) {
                        case 1: {
                            returnValue = this.quarters;
                            break;
                        }
                        case 0:
                        case 3: {
                            returnValue = this.shortQuarters;
                            break;
                        }
                        case 2: {
                            returnValue = null;
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (width) {
                        case 1: {
                            returnValue = this.standaloneQuarters;
                            break Label_0130;
                        }
                        case 0:
                        case 3: {
                            returnValue = this.standaloneShortQuarters;
                            break Label_0130;
                        }
                        case 2: {
                            returnValue = null;
                            break Label_0130;
                        }
                    }
                    break;
                }
            }
        }
        return this.duplicate(returnValue);
    }
    
    public void setQuarters(final String[] newQuarters, final int context, final int width) {
        Label_0139: {
            switch (context) {
                case 0: {
                    switch (width) {
                        case 1: {
                            this.quarters = this.duplicate(newQuarters);
                            break Label_0139;
                        }
                        case 0: {
                            this.shortQuarters = this.duplicate(newQuarters);
                            break Label_0139;
                        }
                        case 2: {
                            break Label_0139;
                        }
                        default: {
                            break Label_0139;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (width) {
                        case 1: {
                            this.standaloneQuarters = this.duplicate(newQuarters);
                            break Label_0139;
                        }
                        case 0: {
                            this.standaloneShortQuarters = this.duplicate(newQuarters);
                        }
                        case 2: {
                            break Label_0139;
                        }
                    }
                    break;
                }
            }
        }
    }
    
    public String[] getAmPmStrings() {
        return this.duplicate(this.ampms);
    }
    
    public void setAmPmStrings(final String[] newAmpms) {
        this.ampms = this.duplicate(newAmpms);
    }
    
    public String[][] getZoneStrings() {
        if (this.zoneStrings != null) {
            return this.duplicate(this.zoneStrings);
        }
        final String[] tzIDs = TimeZone.getAvailableIDs();
        final TimeZoneNames tznames = TimeZoneNames.getInstance(this.validLocale);
        final long now = System.currentTimeMillis();
        final String[][] array = new String[tzIDs.length][5];
        for (int i = 0; i < tzIDs.length; ++i) {
            String canonicalID = TimeZone.getCanonicalID(tzIDs[i]);
            if (canonicalID == null) {
                canonicalID = tzIDs[i];
            }
            array[i][0] = tzIDs[i];
            array[i][1] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.LONG_STANDARD, now);
            array[i][2] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.SHORT_STANDARD, now);
            array[i][3] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.LONG_DAYLIGHT, now);
            array[i][4] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.SHORT_DAYLIGHT, now);
        }
        return this.zoneStrings = array;
    }
    
    public void setZoneStrings(final String[][] newZoneStrings) {
        this.zoneStrings = this.duplicate(newZoneStrings);
    }
    
    public String getLocalPatternChars() {
        return this.localPatternChars;
    }
    
    public void setLocalPatternChars(final String newLocalPatternChars) {
        this.localPatternChars = newLocalPatternChars;
    }
    
    public Object clone() {
        try {
            final DateFormatSymbols other = (DateFormatSymbols)super.clone();
            return other;
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }
    
    @Override
    public int hashCode() {
        return this.requestedLocale.toString().hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final DateFormatSymbols that = (DateFormatSymbols)obj;
        return Utility.arrayEquals(this.eras, that.eras) && Utility.arrayEquals(this.eraNames, that.eraNames) && Utility.arrayEquals(this.months, that.months) && Utility.arrayEquals(this.shortMonths, that.shortMonths) && Utility.arrayEquals(this.narrowMonths, that.narrowMonths) && Utility.arrayEquals(this.standaloneMonths, that.standaloneMonths) && Utility.arrayEquals(this.standaloneShortMonths, that.standaloneShortMonths) && Utility.arrayEquals(this.standaloneNarrowMonths, that.standaloneNarrowMonths) && Utility.arrayEquals(this.weekdays, that.weekdays) && Utility.arrayEquals(this.shortWeekdays, that.shortWeekdays) && Utility.arrayEquals(this.shorterWeekdays, that.shorterWeekdays) && Utility.arrayEquals(this.narrowWeekdays, that.narrowWeekdays) && Utility.arrayEquals(this.standaloneWeekdays, that.standaloneWeekdays) && Utility.arrayEquals(this.standaloneShortWeekdays, that.standaloneShortWeekdays) && Utility.arrayEquals(this.standaloneShorterWeekdays, that.standaloneShorterWeekdays) && Utility.arrayEquals(this.standaloneNarrowWeekdays, that.standaloneNarrowWeekdays) && Utility.arrayEquals(this.ampms, that.ampms) && arrayOfArrayEquals(this.zoneStrings, that.zoneStrings) && this.requestedLocale.getDisplayName().equals(that.requestedLocale.getDisplayName()) && Utility.arrayEquals(this.localPatternChars, that.localPatternChars);
    }
    
    protected void initializeData(final ULocale desiredLocale, final String type) {
        final String key = desiredLocale.getBaseName() + "+" + type;
        DateFormatSymbols dfs = DateFormatSymbols.DFSCACHE.get(key);
        if (dfs == null) {
            final CalendarData calData = new CalendarData(desiredLocale, type);
            this.initializeData(desiredLocale, calData);
            if (this.getClass().getName().equals("com.ibm.icu.text.DateFormatSymbols")) {
                dfs = (DateFormatSymbols)this.clone();
                DateFormatSymbols.DFSCACHE.put(key, dfs);
            }
        }
        else {
            this.initializeData(dfs);
        }
    }
    
    void initializeData(final DateFormatSymbols dfs) {
        this.eras = dfs.eras;
        this.eraNames = dfs.eraNames;
        this.narrowEras = dfs.narrowEras;
        this.months = dfs.months;
        this.shortMonths = dfs.shortMonths;
        this.narrowMonths = dfs.narrowMonths;
        this.standaloneMonths = dfs.standaloneMonths;
        this.standaloneShortMonths = dfs.standaloneShortMonths;
        this.standaloneNarrowMonths = dfs.standaloneNarrowMonths;
        this.weekdays = dfs.weekdays;
        this.shortWeekdays = dfs.shortWeekdays;
        this.shorterWeekdays = dfs.shorterWeekdays;
        this.narrowWeekdays = dfs.narrowWeekdays;
        this.standaloneWeekdays = dfs.standaloneWeekdays;
        this.standaloneShortWeekdays = dfs.standaloneShortWeekdays;
        this.standaloneShorterWeekdays = dfs.standaloneShorterWeekdays;
        this.standaloneNarrowWeekdays = dfs.standaloneNarrowWeekdays;
        this.ampms = dfs.ampms;
        this.shortQuarters = dfs.shortQuarters;
        this.quarters = dfs.quarters;
        this.standaloneShortQuarters = dfs.standaloneShortQuarters;
        this.standaloneQuarters = dfs.standaloneQuarters;
        this.leapMonthPatterns = dfs.leapMonthPatterns;
        this.shortYearNames = dfs.shortYearNames;
        this.zoneStrings = dfs.zoneStrings;
        this.localPatternChars = dfs.localPatternChars;
        this.capitalization = dfs.capitalization;
        this.actualLocale = dfs.actualLocale;
        this.validLocale = dfs.validLocale;
        this.requestedLocale = dfs.requestedLocale;
    }
    
    @Deprecated
    protected void initializeData(final ULocale desiredLocale, final CalendarData calData) {
        this.eras = calData.getEras("abbreviated");
        this.eraNames = calData.getEras("wide");
        this.narrowEras = calData.getEras("narrow");
        this.months = calData.getStringArray("monthNames", "wide");
        this.shortMonths = calData.getStringArray("monthNames", "abbreviated");
        this.narrowMonths = calData.getStringArray("monthNames", "narrow");
        this.standaloneMonths = calData.getStringArray("monthNames", "stand-alone", "wide");
        this.standaloneShortMonths = calData.getStringArray("monthNames", "stand-alone", "abbreviated");
        this.standaloneNarrowMonths = calData.getStringArray("monthNames", "stand-alone", "narrow");
        final String[] lWeekdays = calData.getStringArray("dayNames", "wide");
        (this.weekdays = new String[8])[0] = "";
        System.arraycopy(lWeekdays, 0, this.weekdays, 1, lWeekdays.length);
        final String[] aWeekdays = calData.getStringArray("dayNames", "abbreviated");
        (this.shortWeekdays = new String[8])[0] = "";
        System.arraycopy(aWeekdays, 0, this.shortWeekdays, 1, aWeekdays.length);
        final String[] sWeekdays = calData.getStringArray("dayNames", "short");
        (this.shorterWeekdays = new String[8])[0] = "";
        System.arraycopy(sWeekdays, 0, this.shorterWeekdays, 1, sWeekdays.length);
        String[] nWeekdays = null;
        try {
            nWeekdays = calData.getStringArray("dayNames", "narrow");
        }
        catch (MissingResourceException e) {
            try {
                nWeekdays = calData.getStringArray("dayNames", "stand-alone", "narrow");
            }
            catch (MissingResourceException e2) {
                nWeekdays = calData.getStringArray("dayNames", "abbreviated");
            }
        }
        (this.narrowWeekdays = new String[8])[0] = "";
        System.arraycopy(nWeekdays, 0, this.narrowWeekdays, 1, nWeekdays.length);
        String[] swWeekdays = null;
        swWeekdays = calData.getStringArray("dayNames", "stand-alone", "wide");
        (this.standaloneWeekdays = new String[8])[0] = "";
        System.arraycopy(swWeekdays, 0, this.standaloneWeekdays, 1, swWeekdays.length);
        String[] saWeekdays = null;
        saWeekdays = calData.getStringArray("dayNames", "stand-alone", "abbreviated");
        (this.standaloneShortWeekdays = new String[8])[0] = "";
        System.arraycopy(saWeekdays, 0, this.standaloneShortWeekdays, 1, saWeekdays.length);
        String[] ssWeekdays = null;
        ssWeekdays = calData.getStringArray("dayNames", "stand-alone", "short");
        (this.standaloneShorterWeekdays = new String[8])[0] = "";
        System.arraycopy(ssWeekdays, 0, this.standaloneShorterWeekdays, 1, ssWeekdays.length);
        String[] snWeekdays = null;
        snWeekdays = calData.getStringArray("dayNames", "stand-alone", "narrow");
        (this.standaloneNarrowWeekdays = new String[8])[0] = "";
        System.arraycopy(snWeekdays, 0, this.standaloneNarrowWeekdays, 1, snWeekdays.length);
        this.ampms = calData.getStringArray("AmPmMarkers");
        this.quarters = calData.getStringArray("quarters", "wide");
        this.shortQuarters = calData.getStringArray("quarters", "abbreviated");
        this.standaloneQuarters = calData.getStringArray("quarters", "stand-alone", "wide");
        this.standaloneShortQuarters = calData.getStringArray("quarters", "stand-alone", "abbreviated");
        ICUResourceBundle monthPatternsBundle = null;
        try {
            monthPatternsBundle = calData.get("monthPatterns");
        }
        catch (MissingResourceException e3) {
            monthPatternsBundle = null;
        }
        if (monthPatternsBundle != null) {
            (this.leapMonthPatterns = new String[7])[0] = calData.get("monthPatterns", "wide").get("leap").getString();
            this.leapMonthPatterns[1] = calData.get("monthPatterns", "abbreviated").get("leap").getString();
            this.leapMonthPatterns[2] = calData.get("monthPatterns", "narrow").get("leap").getString();
            this.leapMonthPatterns[3] = calData.get("monthPatterns", "stand-alone", "wide").get("leap").getString();
            this.leapMonthPatterns[4] = calData.get("monthPatterns", "stand-alone", "abbreviated").get("leap").getString();
            this.leapMonthPatterns[5] = calData.get("monthPatterns", "stand-alone", "narrow").get("leap").getString();
            this.leapMonthPatterns[6] = calData.get("monthPatterns", "numeric", "all").get("leap").getString();
        }
        ICUResourceBundle cyclicNameSetsBundle = null;
        try {
            cyclicNameSetsBundle = calData.get("cyclicNameSets");
        }
        catch (MissingResourceException e4) {
            cyclicNameSetsBundle = null;
        }
        if (cyclicNameSetsBundle != null) {
            this.shortYearNames = calData.get("cyclicNameSets", "years", "format", "abbreviated").getStringArray();
        }
        this.requestedLocale = desiredLocale;
        final ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", desiredLocale);
        this.localPatternChars = "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXx";
        final ULocale uloc = rb.getULocale();
        this.setLocale(uloc, uloc);
        this.capitalization = new HashMap<CapitalizationContextUsage, boolean[]>();
        final boolean[] noTransforms = { false, false };
        final CapitalizationContextUsage[] arr$;
        final CapitalizationContextUsage[] allUsages = arr$ = CapitalizationContextUsage.values();
        for (final CapitalizationContextUsage usage : arr$) {
            this.capitalization.put(usage, noTransforms);
        }
        UResourceBundle contextTransformsBundle = null;
        try {
            contextTransformsBundle = rb.getWithFallback("contextTransforms");
        }
        catch (MissingResourceException e5) {
            contextTransformsBundle = null;
        }
        if (contextTransformsBundle != null) {
            final UResourceBundleIterator ctIterator = contextTransformsBundle.getIterator();
            while (ctIterator.hasNext()) {
                final UResourceBundle contextTransformUsage = ctIterator.next();
                final int[] intVector = contextTransformUsage.getIntVector();
                if (intVector.length >= 2) {
                    final String usageKey = contextTransformUsage.getKey();
                    final CapitalizationContextUsage usage2 = DateFormatSymbols.contextUsageTypeMap.get(usageKey);
                    if (usage2 == null) {
                        continue;
                    }
                    final boolean[] transforms = { intVector[0] != 0, intVector[1] != 0 };
                    this.capitalization.put(usage2, transforms);
                }
            }
        }
    }
    
    private static final boolean arrayOfArrayEquals(final Object[][] aa1, final Object[][] aa2) {
        if (aa1 == aa2) {
            return true;
        }
        if (aa1 == null || aa2 == null) {
            return false;
        }
        if (aa1.length != aa2.length) {
            return false;
        }
        boolean equal = true;
        for (int i = 0; i < aa1.length; ++i) {
            equal = Utility.arrayEquals(aa1[i], aa2[i]);
            if (!equal) {
                break;
            }
        }
        return equal;
    }
    
    private final String[] duplicate(final String[] srcArray) {
        return srcArray.clone();
    }
    
    private final String[][] duplicate(final String[][] srcArray) {
        final String[][] aCopy = new String[srcArray.length][];
        for (int i = 0; i < srcArray.length; ++i) {
            aCopy[i] = this.duplicate(srcArray[i]);
        }
        return aCopy;
    }
    
    public DateFormatSymbols(final Calendar cal, final Locale locale) {
        this.eras = null;
        this.eraNames = null;
        this.narrowEras = null;
        this.months = null;
        this.shortMonths = null;
        this.narrowMonths = null;
        this.standaloneMonths = null;
        this.standaloneShortMonths = null;
        this.standaloneNarrowMonths = null;
        this.weekdays = null;
        this.shortWeekdays = null;
        this.shorterWeekdays = null;
        this.narrowWeekdays = null;
        this.standaloneWeekdays = null;
        this.standaloneShortWeekdays = null;
        this.standaloneShorterWeekdays = null;
        this.standaloneNarrowWeekdays = null;
        this.ampms = null;
        this.shortQuarters = null;
        this.quarters = null;
        this.standaloneShortQuarters = null;
        this.standaloneQuarters = null;
        this.leapMonthPatterns = null;
        this.shortYearNames = null;
        this.zoneStrings = null;
        this.localPatternChars = null;
        this.capitalization = null;
        this.initializeData(ULocale.forLocale(locale), cal.getType());
    }
    
    public DateFormatSymbols(final Calendar cal, final ULocale locale) {
        this.eras = null;
        this.eraNames = null;
        this.narrowEras = null;
        this.months = null;
        this.shortMonths = null;
        this.narrowMonths = null;
        this.standaloneMonths = null;
        this.standaloneShortMonths = null;
        this.standaloneNarrowMonths = null;
        this.weekdays = null;
        this.shortWeekdays = null;
        this.shorterWeekdays = null;
        this.narrowWeekdays = null;
        this.standaloneWeekdays = null;
        this.standaloneShortWeekdays = null;
        this.standaloneShorterWeekdays = null;
        this.standaloneNarrowWeekdays = null;
        this.ampms = null;
        this.shortQuarters = null;
        this.quarters = null;
        this.standaloneShortQuarters = null;
        this.standaloneQuarters = null;
        this.leapMonthPatterns = null;
        this.shortYearNames = null;
        this.zoneStrings = null;
        this.localPatternChars = null;
        this.capitalization = null;
        this.initializeData(locale, cal.getType());
    }
    
    public DateFormatSymbols(final Class<? extends Calendar> calendarClass, final Locale locale) {
        this(calendarClass, ULocale.forLocale(locale));
    }
    
    public DateFormatSymbols(final Class<? extends Calendar> calendarClass, final ULocale locale) {
        this.eras = null;
        this.eraNames = null;
        this.narrowEras = null;
        this.months = null;
        this.shortMonths = null;
        this.narrowMonths = null;
        this.standaloneMonths = null;
        this.standaloneShortMonths = null;
        this.standaloneNarrowMonths = null;
        this.weekdays = null;
        this.shortWeekdays = null;
        this.shorterWeekdays = null;
        this.narrowWeekdays = null;
        this.standaloneWeekdays = null;
        this.standaloneShortWeekdays = null;
        this.standaloneShorterWeekdays = null;
        this.standaloneNarrowWeekdays = null;
        this.ampms = null;
        this.shortQuarters = null;
        this.quarters = null;
        this.standaloneShortQuarters = null;
        this.standaloneQuarters = null;
        this.leapMonthPatterns = null;
        this.shortYearNames = null;
        this.zoneStrings = null;
        this.localPatternChars = null;
        this.capitalization = null;
        final String fullName = calendarClass.getName();
        final int lastDot = fullName.lastIndexOf(46);
        final String className = fullName.substring(lastDot + 1);
        String calType = null;
        for (final String[] calClassInfo : DateFormatSymbols.CALENDAR_CLASSES) {
            if (calClassInfo[0].equals(className)) {
                calType = calClassInfo[1];
                break;
            }
        }
        if (calType == null) {
            calType = className.replaceAll("Calendar", "").toLowerCase(Locale.ENGLISH);
        }
        this.initializeData(locale, calType);
    }
    
    public DateFormatSymbols(final ResourceBundle bundle, final Locale locale) {
        this(bundle, ULocale.forLocale(locale));
    }
    
    public DateFormatSymbols(final ResourceBundle bundle, final ULocale locale) {
        this.eras = null;
        this.eraNames = null;
        this.narrowEras = null;
        this.months = null;
        this.shortMonths = null;
        this.narrowMonths = null;
        this.standaloneMonths = null;
        this.standaloneShortMonths = null;
        this.standaloneNarrowMonths = null;
        this.weekdays = null;
        this.shortWeekdays = null;
        this.shorterWeekdays = null;
        this.narrowWeekdays = null;
        this.standaloneWeekdays = null;
        this.standaloneShortWeekdays = null;
        this.standaloneShorterWeekdays = null;
        this.standaloneNarrowWeekdays = null;
        this.ampms = null;
        this.shortQuarters = null;
        this.quarters = null;
        this.standaloneShortQuarters = null;
        this.standaloneQuarters = null;
        this.leapMonthPatterns = null;
        this.shortYearNames = null;
        this.zoneStrings = null;
        this.localPatternChars = null;
        this.capitalization = null;
        this.initializeData(locale, new CalendarData((ICUResourceBundle)bundle, CalendarUtil.getCalendarType(locale)));
    }
    
    @Deprecated
    public static ResourceBundle getDateFormatBundle(final Class<? extends Calendar> calendarClass, final Locale locale) throws MissingResourceException {
        return null;
    }
    
    @Deprecated
    public static ResourceBundle getDateFormatBundle(final Class<? extends Calendar> calendarClass, final ULocale locale) throws MissingResourceException {
        return null;
    }
    
    @Deprecated
    public static ResourceBundle getDateFormatBundle(final Calendar cal, final Locale locale) throws MissingResourceException {
        return null;
    }
    
    @Deprecated
    public static ResourceBundle getDateFormatBundle(final Calendar cal, final ULocale locale) throws MissingResourceException {
        return null;
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
    
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
    
    static {
        CALENDAR_CLASSES = new String[][] { { "GregorianCalendar", "gregorian" }, { "JapaneseCalendar", "japanese" }, { "BuddhistCalendar", "buddhist" }, { "TaiwanCalendar", "roc" }, { "PersianCalendar", "persian" }, { "IslamicCalendar", "islamic" }, { "HebrewCalendar", "hebrew" }, { "ChineseCalendar", "chinese" }, { "IndianCalendar", "indian" }, { "CopticCalendar", "coptic" }, { "EthiopicCalendar", "ethiopic" } };
        (contextUsageTypeMap = new HashMap<String, CapitalizationContextUsage>()).put("month-format-except-narrow", CapitalizationContextUsage.MONTH_FORMAT);
        DateFormatSymbols.contextUsageTypeMap.put("month-standalone-except-narrow", CapitalizationContextUsage.MONTH_STANDALONE);
        DateFormatSymbols.contextUsageTypeMap.put("month-narrow", CapitalizationContextUsage.MONTH_NARROW);
        DateFormatSymbols.contextUsageTypeMap.put("day-format-except-narrow", CapitalizationContextUsage.DAY_FORMAT);
        DateFormatSymbols.contextUsageTypeMap.put("day-standalone-except-narrow", CapitalizationContextUsage.DAY_STANDALONE);
        DateFormatSymbols.contextUsageTypeMap.put("day-narrow", CapitalizationContextUsage.DAY_NARROW);
        DateFormatSymbols.contextUsageTypeMap.put("era-name", CapitalizationContextUsage.ERA_WIDE);
        DateFormatSymbols.contextUsageTypeMap.put("era-abbr", CapitalizationContextUsage.ERA_ABBREV);
        DateFormatSymbols.contextUsageTypeMap.put("era-narrow", CapitalizationContextUsage.ERA_NARROW);
        DateFormatSymbols.contextUsageTypeMap.put("zone-long", CapitalizationContextUsage.ZONE_LONG);
        DateFormatSymbols.contextUsageTypeMap.put("zone-short", CapitalizationContextUsage.ZONE_SHORT);
        DateFormatSymbols.contextUsageTypeMap.put("metazone-long", CapitalizationContextUsage.METAZONE_LONG);
        DateFormatSymbols.contextUsageTypeMap.put("metazone-short", CapitalizationContextUsage.METAZONE_SHORT);
        DateFormatSymbols.DFSCACHE = new SimpleCache<String, DateFormatSymbols>();
    }
    
    enum CapitalizationContextUsage
    {
        OTHER, 
        MONTH_FORMAT, 
        MONTH_STANDALONE, 
        MONTH_NARROW, 
        DAY_FORMAT, 
        DAY_STANDALONE, 
        DAY_NARROW, 
        ERA_WIDE, 
        ERA_ABBREV, 
        ERA_NARROW, 
        ZONE_LONG, 
        ZONE_SHORT, 
        METAZONE_LONG, 
        METAZONE_SHORT;
    }
}
