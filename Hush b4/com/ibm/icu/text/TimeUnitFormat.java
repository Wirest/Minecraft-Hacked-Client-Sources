// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.MissingResourceException;
import java.util.TreeMap;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.impl.ICUResourceBundle;
import java.util.Set;
import java.util.HashMap;
import java.text.ParsePosition;
import com.ibm.icu.util.TimeUnitAmount;
import java.text.FieldPosition;
import java.util.Iterator;
import java.text.Format;
import java.util.Locale;
import com.ibm.icu.util.TimeUnit;
import java.util.Map;
import com.ibm.icu.util.ULocale;

public class TimeUnitFormat extends MeasureFormat
{
    public static final int FULL_NAME = 0;
    public static final int ABBREVIATED_NAME = 1;
    private static final int TOTAL_STYLES = 2;
    private static final long serialVersionUID = -3707773153184971529L;
    private static final String DEFAULT_PATTERN_FOR_SECOND = "{0} s";
    private static final String DEFAULT_PATTERN_FOR_MINUTE = "{0} min";
    private static final String DEFAULT_PATTERN_FOR_HOUR = "{0} h";
    private static final String DEFAULT_PATTERN_FOR_DAY = "{0} d";
    private static final String DEFAULT_PATTERN_FOR_WEEK = "{0} w";
    private static final String DEFAULT_PATTERN_FOR_MONTH = "{0} m";
    private static final String DEFAULT_PATTERN_FOR_YEAR = "{0} y";
    private NumberFormat format;
    private ULocale locale;
    private transient Map<TimeUnit, Map<String, Object[]>> timeUnitToCountToPatterns;
    private transient PluralRules pluralRules;
    private transient boolean isReady;
    private int style;
    
    public TimeUnitFormat() {
        this.isReady = false;
        this.style = 0;
    }
    
    public TimeUnitFormat(final ULocale locale) {
        this(locale, 0);
    }
    
    public TimeUnitFormat(final Locale locale) {
        this(locale, 0);
    }
    
    public TimeUnitFormat(final ULocale locale, final int style) {
        if (style < 0 || style >= 2) {
            throw new IllegalArgumentException("style should be either FULL_NAME or ABBREVIATED_NAME style");
        }
        this.style = style;
        this.locale = locale;
        this.isReady = false;
    }
    
    public TimeUnitFormat(final Locale locale, final int style) {
        this(ULocale.forLocale(locale), style);
    }
    
    public TimeUnitFormat setLocale(final ULocale locale) {
        if (locale != this.locale) {
            this.locale = locale;
            this.isReady = false;
        }
        return this;
    }
    
    public TimeUnitFormat setLocale(final Locale locale) {
        return this.setLocale(ULocale.forLocale(locale));
    }
    
    public TimeUnitFormat setNumberFormat(final NumberFormat format) {
        if (format == this.format) {
            return this;
        }
        if (format == null) {
            if (this.locale == null) {
                this.isReady = false;
                return this;
            }
            this.format = NumberFormat.getNumberInstance(this.locale);
        }
        else {
            this.format = format;
        }
        if (!this.isReady) {
            return this;
        }
        for (final Map<String, Object[]> countToPattern : this.timeUnitToCountToPatterns.values()) {
            for (final Object[] pair : countToPattern.values()) {
                MessageFormat pattern = (MessageFormat)pair[0];
                pattern.setFormatByArgumentIndex(0, format);
                pattern = (MessageFormat)pair[1];
                pattern.setFormatByArgumentIndex(0, format);
            }
        }
        return this;
    }
    
    @Override
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
        if (!(obj instanceof TimeUnitAmount)) {
            throw new IllegalArgumentException("can not format non TimeUnitAmount object");
        }
        if (!this.isReady) {
            this.setup();
        }
        final TimeUnitAmount amount = (TimeUnitAmount)obj;
        final Map<String, Object[]> countToPattern = this.timeUnitToCountToPatterns.get(amount.getTimeUnit());
        final double number = amount.getNumber().doubleValue();
        final String count = this.pluralRules.select(number);
        final MessageFormat pattern = (MessageFormat)countToPattern.get(count)[this.style];
        return pattern.format(new Object[] { amount.getNumber() }, toAppendTo, pos);
    }
    
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        if (!this.isReady) {
            this.setup();
        }
        Number resultNumber = null;
        TimeUnit resultTimeUnit = null;
        final int oldPos = pos.getIndex();
        int newPos = -1;
        int longestParseDistance = 0;
        String countOfLongestMatch = null;
        for (final TimeUnit timeUnit : this.timeUnitToCountToPatterns.keySet()) {
            final Map<String, Object[]> countToPattern = this.timeUnitToCountToPatterns.get(timeUnit);
            for (final Map.Entry<String, Object[]> patternEntry : countToPattern.entrySet()) {
                final String count = patternEntry.getKey();
                for (int styl = 0; styl < 2; ++styl) {
                    final MessageFormat pattern = (MessageFormat)patternEntry.getValue()[styl];
                    pos.setErrorIndex(-1);
                    pos.setIndex(oldPos);
                    final Object parsed = pattern.parseObject(source, pos);
                    if (pos.getErrorIndex() == -1) {
                        if (pos.getIndex() != oldPos) {
                            Number temp = null;
                            if (((Object[])parsed).length != 0) {
                                temp = (Number)((Object[])parsed)[0];
                                final String select = this.pluralRules.select(temp.doubleValue());
                                if (!count.equals(select)) {
                                    continue;
                                }
                            }
                            final int parseDistance = pos.getIndex() - oldPos;
                            if (parseDistance > longestParseDistance) {
                                resultNumber = temp;
                                resultTimeUnit = timeUnit;
                                newPos = pos.getIndex();
                                longestParseDistance = parseDistance;
                                countOfLongestMatch = count;
                            }
                        }
                    }
                }
            }
        }
        if (resultNumber == null && longestParseDistance != 0) {
            if (countOfLongestMatch.equals("zero")) {
                resultNumber = 0;
            }
            else if (countOfLongestMatch.equals("one")) {
                resultNumber = 1;
            }
            else if (countOfLongestMatch.equals("two")) {
                resultNumber = 2;
            }
            else {
                resultNumber = 3;
            }
        }
        if (longestParseDistance == 0) {
            pos.setIndex(oldPos);
            pos.setErrorIndex(0);
            return null;
        }
        pos.setIndex(newPos);
        pos.setErrorIndex(-1);
        return new TimeUnitAmount(resultNumber, resultTimeUnit);
    }
    
    private void setup() {
        if (this.locale == null) {
            if (this.format != null) {
                this.locale = this.format.getLocale(null);
            }
            else {
                this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
            }
        }
        if (this.format == null) {
            this.format = NumberFormat.getNumberInstance(this.locale);
        }
        this.pluralRules = PluralRules.forLocale(this.locale);
        this.timeUnitToCountToPatterns = new HashMap<TimeUnit, Map<String, Object[]>>();
        final Set<String> pluralKeywords = this.pluralRules.getKeywords();
        this.setup("units", this.timeUnitToCountToPatterns, 0, pluralKeywords);
        this.setup("unitsShort", this.timeUnitToCountToPatterns, 1, pluralKeywords);
        this.isReady = true;
    }
    
    private void setup(final String resourceKey, final Map<TimeUnit, Map<String, Object[]>> timeUnitToCountToPatterns, final int style, final Set<String> pluralKeywords) {
        try {
            final ICUResourceBundle resource = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", this.locale);
            final ICUResourceBundle unitsRes = resource.getWithFallback(resourceKey);
            for (int size = unitsRes.getSize(), index = 0; index < size; ++index) {
                final String timeUnitName = unitsRes.get(index).getKey();
                TimeUnit timeUnit = null;
                if (timeUnitName.equals("year")) {
                    timeUnit = TimeUnit.YEAR;
                }
                else if (timeUnitName.equals("month")) {
                    timeUnit = TimeUnit.MONTH;
                }
                else if (timeUnitName.equals("day")) {
                    timeUnit = TimeUnit.DAY;
                }
                else if (timeUnitName.equals("hour")) {
                    timeUnit = TimeUnit.HOUR;
                }
                else if (timeUnitName.equals("minute")) {
                    timeUnit = TimeUnit.MINUTE;
                }
                else if (timeUnitName.equals("second")) {
                    timeUnit = TimeUnit.SECOND;
                }
                else {
                    if (!timeUnitName.equals("week")) {
                        continue;
                    }
                    timeUnit = TimeUnit.WEEK;
                }
                final ICUResourceBundle oneUnitRes = unitsRes.getWithFallback(timeUnitName);
                final int count = oneUnitRes.getSize();
                Map<String, Object[]> countToPatterns = timeUnitToCountToPatterns.get(timeUnit);
                if (countToPatterns == null) {
                    countToPatterns = new TreeMap<String, Object[]>();
                    timeUnitToCountToPatterns.put(timeUnit, countToPatterns);
                }
                for (int pluralIndex = 0; pluralIndex < count; ++pluralIndex) {
                    final String pluralCount = oneUnitRes.get(pluralIndex).getKey();
                    if (pluralKeywords.contains(pluralCount)) {
                        final String pattern = oneUnitRes.get(pluralIndex).getString();
                        final MessageFormat messageFormat = new MessageFormat(pattern, this.locale);
                        if (this.format != null) {
                            messageFormat.setFormatByArgumentIndex(0, this.format);
                        }
                        Object[] pair = countToPatterns.get(pluralCount);
                        if (pair == null) {
                            pair = new Object[2];
                            countToPatterns.put(pluralCount, pair);
                        }
                        pair[style] = messageFormat;
                    }
                }
            }
        }
        catch (MissingResourceException ex) {}
        final TimeUnit[] timeUnits = TimeUnit.values();
        final Set<String> keywords = this.pluralRules.getKeywords();
        for (int i = 0; i < timeUnits.length; ++i) {
            final TimeUnit timeUnit2 = timeUnits[i];
            Map<String, Object[]> countToPatterns2 = timeUnitToCountToPatterns.get(timeUnit2);
            if (countToPatterns2 == null) {
                countToPatterns2 = new TreeMap<String, Object[]>();
                timeUnitToCountToPatterns.put(timeUnit2, countToPatterns2);
            }
            for (final String pluralCount2 : keywords) {
                if (countToPatterns2.get(pluralCount2) == null || countToPatterns2.get(pluralCount2)[style] == null) {
                    this.searchInTree(resourceKey, style, timeUnit2, pluralCount2, pluralCount2, countToPatterns2);
                }
            }
        }
    }
    
    private void searchInTree(final String resourceKey, final int styl, final TimeUnit timeUnit, final String srcPluralCount, final String searchPluralCount, final Map<String, Object[]> countToPatterns) {
        ULocale parentLocale = this.locale;
        final String srcTimeUnitName = timeUnit.toString();
        while (parentLocale != null) {
            try {
                ICUResourceBundle unitsRes = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", parentLocale);
                unitsRes = unitsRes.getWithFallback(resourceKey);
                final ICUResourceBundle oneUnitRes = unitsRes.getWithFallback(srcTimeUnitName);
                final String pattern = oneUnitRes.getStringWithFallback(searchPluralCount);
                final MessageFormat messageFormat = new MessageFormat(pattern, this.locale);
                if (this.format != null) {
                    messageFormat.setFormatByArgumentIndex(0, this.format);
                }
                Object[] pair = countToPatterns.get(srcPluralCount);
                if (pair == null) {
                    pair = new Object[2];
                    countToPatterns.put(srcPluralCount, pair);
                }
                pair[styl] = messageFormat;
                return;
            }
            catch (MissingResourceException e) {
                parentLocale = parentLocale.getFallback();
                continue;
            }
            break;
        }
        if (parentLocale == null && resourceKey.equals("unitsShort")) {
            this.searchInTree("units", styl, timeUnit, srcPluralCount, searchPluralCount, countToPatterns);
            if (countToPatterns != null && countToPatterns.get(srcPluralCount) != null && countToPatterns.get(srcPluralCount)[styl] != null) {
                return;
            }
        }
        if (searchPluralCount.equals("other")) {
            MessageFormat messageFormat2 = null;
            if (timeUnit == TimeUnit.SECOND) {
                messageFormat2 = new MessageFormat("{0} s", this.locale);
            }
            else if (timeUnit == TimeUnit.MINUTE) {
                messageFormat2 = new MessageFormat("{0} min", this.locale);
            }
            else if (timeUnit == TimeUnit.HOUR) {
                messageFormat2 = new MessageFormat("{0} h", this.locale);
            }
            else if (timeUnit == TimeUnit.WEEK) {
                messageFormat2 = new MessageFormat("{0} w", this.locale);
            }
            else if (timeUnit == TimeUnit.DAY) {
                messageFormat2 = new MessageFormat("{0} d", this.locale);
            }
            else if (timeUnit == TimeUnit.MONTH) {
                messageFormat2 = new MessageFormat("{0} m", this.locale);
            }
            else if (timeUnit == TimeUnit.YEAR) {
                messageFormat2 = new MessageFormat("{0} y", this.locale);
            }
            if (this.format != null && messageFormat2 != null) {
                messageFormat2.setFormatByArgumentIndex(0, this.format);
            }
            Object[] pair2 = countToPatterns.get(srcPluralCount);
            if (pair2 == null) {
                pair2 = new Object[2];
                countToPatterns.put(srcPluralCount, pair2);
            }
            pair2[styl] = messageFormat2;
        }
        else {
            this.searchInTree(resourceKey, styl, timeUnit, srcPluralCount, "other", countToPatterns);
        }
    }
}
