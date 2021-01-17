// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;
import java.util.ArrayList;
import com.ibm.icu.impl.PatternTokenizer;
import java.util.Arrays;
import com.ibm.icu.impl.SimpleCache;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.BitSet;
import java.util.List;
import java.util.MissingResourceException;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.util.ULocale;
import java.util.HashSet;
import java.util.Set;
import com.ibm.icu.impl.ICUCache;
import java.util.TreeMap;
import com.ibm.icu.util.Freezable;

public class DateTimePatternGenerator implements Freezable<DateTimePatternGenerator>, Cloneable
{
    private static final boolean DEBUG = false;
    public static final int ERA = 0;
    public static final int YEAR = 1;
    public static final int QUARTER = 2;
    public static final int MONTH = 3;
    public static final int WEEK_OF_YEAR = 4;
    public static final int WEEK_OF_MONTH = 5;
    public static final int WEEKDAY = 6;
    public static final int DAY = 7;
    public static final int DAY_OF_YEAR = 8;
    public static final int DAY_OF_WEEK_IN_MONTH = 9;
    public static final int DAYPERIOD = 10;
    public static final int HOUR = 11;
    public static final int MINUTE = 12;
    public static final int SECOND = 13;
    public static final int FRACTIONAL_SECOND = 14;
    public static final int ZONE = 15;
    public static final int TYPE_LIMIT = 16;
    public static final int MATCH_NO_OPTIONS = 0;
    public static final int MATCH_HOUR_FIELD_LENGTH = 2048;
    @Deprecated
    public static final int MATCH_MINUTE_FIELD_LENGTH = 4096;
    @Deprecated
    public static final int MATCH_SECOND_FIELD_LENGTH = 8192;
    public static final int MATCH_ALL_FIELDS_LENGTH = 65535;
    private TreeMap<DateTimeMatcher, PatternWithSkeletonFlag> skeleton2pattern;
    private TreeMap<String, PatternWithSkeletonFlag> basePattern_pattern;
    private String decimal;
    private String dateTimeFormat;
    private String[] appendItemFormats;
    private String[] appendItemNames;
    private char defaultHourFormatChar;
    private boolean frozen;
    private transient DateTimeMatcher current;
    private transient FormatParser fp;
    private transient DistanceInfo _distanceInfo;
    private static final int FRACTIONAL_MASK = 16384;
    private static final int SECOND_AND_FRACTIONAL_MASK = 24576;
    private static ICUCache<String, DateTimePatternGenerator> DTPNG_CACHE;
    private static final String[] CLDR_FIELD_APPEND;
    private static final String[] CLDR_FIELD_NAME;
    private static final String[] FIELD_NAME;
    private static final String[] CANONICAL_ITEMS;
    private static final Set<String> CANONICAL_SET;
    private Set<String> cldrAvailableFormatKeys;
    private static final int DATE_MASK = 1023;
    private static final int TIME_MASK = 64512;
    private static final int DELTA = 16;
    private static final int NUMERIC = 256;
    private static final int NONE = 0;
    private static final int NARROW = -257;
    private static final int SHORT = -258;
    private static final int LONG = -259;
    private static final int EXTRA_FIELD = 65536;
    private static final int MISSING_FIELD = 4096;
    private static final int[][] types;
    
    public static DateTimePatternGenerator getEmptyInstance() {
        return new DateTimePatternGenerator();
    }
    
    protected DateTimePatternGenerator() {
        this.skeleton2pattern = new TreeMap<DateTimeMatcher, PatternWithSkeletonFlag>();
        this.basePattern_pattern = new TreeMap<String, PatternWithSkeletonFlag>();
        this.decimal = "?";
        this.dateTimeFormat = "{1} {0}";
        this.appendItemFormats = new String[16];
        this.appendItemNames = new String[16];
        for (int i = 0; i < 16; ++i) {
            this.appendItemFormats[i] = "{0} \u251c{2}: {1}\u2524";
            this.appendItemNames[i] = "F" + i;
        }
        this.defaultHourFormatChar = 'H';
        this.frozen = false;
        this.current = new DateTimeMatcher();
        this.fp = new FormatParser();
        this._distanceInfo = new DistanceInfo();
        this.complete();
        this.cldrAvailableFormatKeys = new HashSet<String>(20);
    }
    
    public static DateTimePatternGenerator getInstance() {
        return getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public static DateTimePatternGenerator getInstance(final ULocale uLocale) {
        return getFrozenInstance(uLocale).cloneAsThawed();
    }
    
    @Deprecated
    public static DateTimePatternGenerator getFrozenInstance(final ULocale uLocale) {
        final String localeKey = uLocale.toString();
        DateTimePatternGenerator result = DateTimePatternGenerator.DTPNG_CACHE.get(localeKey);
        if (result != null) {
            return result;
        }
        result = new DateTimePatternGenerator();
        final PatternInfo returnInfo = new PatternInfo();
        String shortTimePattern = null;
        for (int i = 0; i <= 3; ++i) {
            SimpleDateFormat df = (SimpleDateFormat)DateFormat.getDateInstance(i, uLocale);
            result.addPattern(df.toPattern(), false, returnInfo);
            df = (SimpleDateFormat)DateFormat.getTimeInstance(i, uLocale);
            result.addPattern(df.toPattern(), false, returnInfo);
            if (i == 3) {
                shortTimePattern = df.toPattern();
                final FormatParser fp = new FormatParser();
                fp.set(shortTimePattern);
                final List<Object> items = fp.getItems();
                for (int idx = 0; idx < items.size(); ++idx) {
                    final Object item = items.get(idx);
                    if (item instanceof VariableField) {
                        final VariableField fld = (VariableField)item;
                        if (fld.getType() == 11) {
                            result.defaultHourFormatChar = fld.toString().charAt(0);
                            break;
                        }
                    }
                }
            }
        }
        final ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", uLocale);
        String calendarTypeToUse = uLocale.getKeywordValue("calendar");
        if (calendarTypeToUse == null) {
            final String[] preferredCalendarTypes = Calendar.getKeywordValuesForLocale("calendar", uLocale, true);
            calendarTypeToUse = preferredCalendarTypes[0];
        }
        if (calendarTypeToUse == null) {
            calendarTypeToUse = "gregorian";
        }
        try {
            final ICUResourceBundle itemBundle = rb.getWithFallback("calendar/" + calendarTypeToUse + "/appendItems");
            for (int j = 0; j < itemBundle.getSize(); ++j) {
                final ICUResourceBundle formatBundle = (ICUResourceBundle)itemBundle.get(j);
                final String formatName = itemBundle.get(j).getKey();
                final String value = formatBundle.getString();
                result.setAppendItemFormat(getAppendFormatNumber(formatName), value);
            }
        }
        catch (MissingResourceException ex) {}
        try {
            final ICUResourceBundle itemBundle = rb.getWithFallback("fields");
            for (int k = 0; k < 16; ++k) {
                if (isCLDRFieldName(k)) {
                    final ICUResourceBundle fieldBundle = itemBundle.getWithFallback(DateTimePatternGenerator.CLDR_FIELD_NAME[k]);
                    final ICUResourceBundle dnBundle = fieldBundle.getWithFallback("dn");
                    final String value = dnBundle.getString();
                    result.setAppendItemName(k, value);
                }
            }
        }
        catch (MissingResourceException ex2) {}
        ICUResourceBundle availFormatsBundle = null;
        try {
            availFormatsBundle = rb.getWithFallback("calendar/" + calendarTypeToUse + "/availableFormats");
        }
        catch (MissingResourceException ex3) {}
        boolean override = true;
        while (availFormatsBundle != null) {
            for (int l = 0; l < availFormatsBundle.getSize(); ++l) {
                final String formatKey = availFormatsBundle.get(l).getKey();
                if (!result.isAvailableFormatSet(formatKey)) {
                    result.setAvailableFormat(formatKey);
                    final String formatValue = availFormatsBundle.get(l).getString();
                    result.addPatternWithSkeleton(formatValue, formatKey, override, returnInfo);
                }
            }
            final ICUResourceBundle pbundle = (ICUResourceBundle)availFormatsBundle.getParent();
            if (pbundle == null) {
                break;
            }
            try {
                availFormatsBundle = pbundle.getWithFallback("calendar/" + calendarTypeToUse + "/availableFormats");
            }
            catch (MissingResourceException e) {
                availFormatsBundle = null;
            }
            if (availFormatsBundle == null || !pbundle.getULocale().getBaseName().equals("root")) {
                continue;
            }
            override = false;
        }
        if (shortTimePattern != null) {
            hackTimes(result, returnInfo, shortTimePattern);
        }
        result.setDateTimeFormat(Calendar.getDateTimePattern(Calendar.getInstance(uLocale), uLocale, 2));
        final DecimalFormatSymbols dfs = new DecimalFormatSymbols(uLocale);
        result.setDecimal(String.valueOf(dfs.getDecimalSeparator()));
        result.freeze();
        DateTimePatternGenerator.DTPNG_CACHE.put(localeKey, result);
        return result;
    }
    
    @Deprecated
    public char getDefaultHourFormatChar() {
        return this.defaultHourFormatChar;
    }
    
    @Deprecated
    public void setDefaultHourFormatChar(final char defaultHourFormatChar) {
        this.defaultHourFormatChar = defaultHourFormatChar;
    }
    
    private static void hackTimes(final DateTimePatternGenerator result, final PatternInfo returnInfo, final String hackPattern) {
        result.fp.set(hackPattern);
        final StringBuilder mmss = new StringBuilder();
        boolean gotMm = false;
        for (int i = 0; i < result.fp.items.size(); ++i) {
            final Object item = result.fp.items.get(i);
            if (item instanceof String) {
                if (gotMm) {
                    mmss.append(result.fp.quoteLiteral(item.toString()));
                }
            }
            else {
                final char ch = item.toString().charAt(0);
                if (ch == 'm') {
                    gotMm = true;
                    mmss.append(item);
                }
                else if (ch == 's') {
                    if (!gotMm) {
                        break;
                    }
                    mmss.append(item);
                    result.addPattern(mmss.toString(), false, returnInfo);
                    break;
                }
                else {
                    if (gotMm || ch == 'z' || ch == 'Z' || ch == 'v') {
                        break;
                    }
                    if (ch == 'V') {
                        break;
                    }
                }
            }
        }
        final BitSet variables = new BitSet();
        final BitSet nuke = new BitSet();
        for (int j = 0; j < result.fp.items.size(); ++j) {
            final Object item2 = result.fp.items.get(j);
            if (item2 instanceof VariableField) {
                variables.set(j);
                final char ch2 = item2.toString().charAt(0);
                if (ch2 == 's' || ch2 == 'S') {
                    nuke.set(j);
                    for (int k = j - 1; k >= 0; ++k) {
                        if (variables.get(k)) {
                            break;
                        }
                        nuke.set(j);
                    }
                }
            }
        }
        final String hhmm = getFilteredPattern(result.fp, nuke);
        result.addPattern(hhmm, false, returnInfo);
    }
    
    private static String getFilteredPattern(final FormatParser fp, final BitSet nuke) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < fp.items.size(); ++i) {
            if (!nuke.get(i)) {
                final Object item = fp.items.get(i);
                if (item instanceof String) {
                    result.append(fp.quoteLiteral(item.toString()));
                }
                else {
                    result.append(item.toString());
                }
            }
        }
        return result.toString();
    }
    
    @Deprecated
    public static int getAppendFormatNumber(final String string) {
        for (int i = 0; i < DateTimePatternGenerator.CLDR_FIELD_APPEND.length; ++i) {
            if (DateTimePatternGenerator.CLDR_FIELD_APPEND[i].equals(string)) {
                return i;
            }
        }
        return -1;
    }
    
    private static boolean isCLDRFieldName(final int index) {
        return (index >= 0 || index < 16) && DateTimePatternGenerator.CLDR_FIELD_NAME[index].charAt(0) != '*';
    }
    
    public String getBestPattern(final String skeleton) {
        return this.getBestPattern(skeleton, null, 0);
    }
    
    public String getBestPattern(final String skeleton, final int options) {
        return this.getBestPattern(skeleton, null, options);
    }
    
    private String getBestPattern(String skeleton, final DateTimeMatcher skipMatcher, final int options) {
        skeleton = skeleton.replaceAll("j", String.valueOf(this.defaultHourFormatChar));
        final String datePattern;
        final String timePattern;
        synchronized (this) {
            this.current.set(skeleton, this.fp, false);
            final PatternWithMatcher bestWithMatcher = this.getBestRaw(this.current, -1, this._distanceInfo, skipMatcher);
            if (this._distanceInfo.missingFieldMask == 0 && this._distanceInfo.extraFieldMask == 0) {
                return this.adjustFieldTypes(bestWithMatcher, this.current, false, options);
            }
            final int neededFields = this.current.getFieldMask();
            datePattern = this.getBestAppending(this.current, neededFields & 0x3FF, this._distanceInfo, skipMatcher, options);
            timePattern = this.getBestAppending(this.current, neededFields & 0xFC00, this._distanceInfo, skipMatcher, options);
        }
        if (datePattern == null) {
            return (timePattern == null) ? "" : timePattern;
        }
        if (timePattern == null) {
            return datePattern;
        }
        return MessageFormat.format(this.getDateTimeFormat(), timePattern, datePattern);
    }
    
    public DateTimePatternGenerator addPattern(final String pattern, final boolean override, final PatternInfo returnInfo) {
        return this.addPatternWithSkeleton(pattern, null, override, returnInfo);
    }
    
    @Deprecated
    public DateTimePatternGenerator addPatternWithSkeleton(final String pattern, final String skeletonToUse, final boolean override, final PatternInfo returnInfo) {
        this.checkFrozen();
        DateTimeMatcher matcher;
        if (skeletonToUse == null) {
            matcher = new DateTimeMatcher().set(pattern, this.fp, false);
        }
        else {
            matcher = new DateTimeMatcher().set(skeletonToUse, this.fp, false);
        }
        final String basePattern = matcher.getBasePattern();
        final PatternWithSkeletonFlag previousPatternWithSameBase = this.basePattern_pattern.get(basePattern);
        if (previousPatternWithSameBase != null && (!previousPatternWithSameBase.skeletonWasSpecified || (skeletonToUse != null && !override))) {
            returnInfo.status = 1;
            returnInfo.conflictingPattern = previousPatternWithSameBase.pattern;
            if (!override) {
                return this;
            }
        }
        final PatternWithSkeletonFlag previousValue = this.skeleton2pattern.get(matcher);
        if (previousValue != null) {
            returnInfo.status = 2;
            returnInfo.conflictingPattern = previousValue.pattern;
            if (!override || (skeletonToUse != null && previousValue.skeletonWasSpecified)) {
                return this;
            }
        }
        returnInfo.status = 0;
        returnInfo.conflictingPattern = "";
        final PatternWithSkeletonFlag patWithSkelFlag = new PatternWithSkeletonFlag(pattern, skeletonToUse != null);
        this.skeleton2pattern.put(matcher, patWithSkelFlag);
        this.basePattern_pattern.put(basePattern, patWithSkelFlag);
        return this;
    }
    
    public String getSkeleton(final String pattern) {
        synchronized (this) {
            this.current.set(pattern, this.fp, false);
            return this.current.toString();
        }
    }
    
    @Deprecated
    public String getSkeletonAllowingDuplicates(final String pattern) {
        synchronized (this) {
            this.current.set(pattern, this.fp, true);
            return this.current.toString();
        }
    }
    
    @Deprecated
    public String getCanonicalSkeletonAllowingDuplicates(final String pattern) {
        synchronized (this) {
            this.current.set(pattern, this.fp, true);
            return this.current.toCanonicalString();
        }
    }
    
    public String getBaseSkeleton(final String pattern) {
        synchronized (this) {
            this.current.set(pattern, this.fp, false);
            return this.current.getBasePattern();
        }
    }
    
    public Map<String, String> getSkeletons(Map<String, String> result) {
        if (result == null) {
            result = new LinkedHashMap<String, String>();
        }
        for (final DateTimeMatcher item : this.skeleton2pattern.keySet()) {
            final PatternWithSkeletonFlag patternWithSkelFlag = this.skeleton2pattern.get(item);
            final String pattern = patternWithSkelFlag.pattern;
            if (DateTimePatternGenerator.CANONICAL_SET.contains(pattern)) {
                continue;
            }
            result.put(item.toString(), pattern);
        }
        return result;
    }
    
    public Set<String> getBaseSkeletons(Set<String> result) {
        if (result == null) {
            result = new HashSet<String>();
        }
        result.addAll(this.basePattern_pattern.keySet());
        return result;
    }
    
    public String replaceFieldTypes(final String pattern, final String skeleton) {
        return this.replaceFieldTypes(pattern, skeleton, 0);
    }
    
    public String replaceFieldTypes(final String pattern, final String skeleton, final int options) {
        synchronized (this) {
            final PatternWithMatcher patternNoMatcher = new PatternWithMatcher(pattern, null);
            return this.adjustFieldTypes(patternNoMatcher, this.current.set(skeleton, this.fp, false), false, options);
        }
    }
    
    public void setDateTimeFormat(final String dateTimeFormat) {
        this.checkFrozen();
        this.dateTimeFormat = dateTimeFormat;
    }
    
    public String getDateTimeFormat() {
        return this.dateTimeFormat;
    }
    
    public void setDecimal(final String decimal) {
        this.checkFrozen();
        this.decimal = decimal;
    }
    
    public String getDecimal() {
        return this.decimal;
    }
    
    @Deprecated
    public Collection<String> getRedundants(Collection<String> output) {
        synchronized (this) {
            if (output == null) {
                output = new LinkedHashSet<String>();
            }
            for (final DateTimeMatcher cur : this.skeleton2pattern.keySet()) {
                final PatternWithSkeletonFlag patternWithSkelFlag = this.skeleton2pattern.get(cur);
                final String pattern = patternWithSkelFlag.pattern;
                if (DateTimePatternGenerator.CANONICAL_SET.contains(pattern)) {
                    continue;
                }
                final String trial = this.getBestPattern(cur.toString(), cur, 0);
                if (!trial.equals(pattern)) {
                    continue;
                }
                output.add(pattern);
            }
            return output;
        }
    }
    
    public void setAppendItemFormat(final int field, final String value) {
        this.checkFrozen();
        this.appendItemFormats[field] = value;
    }
    
    public String getAppendItemFormat(final int field) {
        return this.appendItemFormats[field];
    }
    
    public void setAppendItemName(final int field, final String value) {
        this.checkFrozen();
        this.appendItemNames[field] = value;
    }
    
    public String getAppendItemName(final int field) {
        return this.appendItemNames[field];
    }
    
    @Deprecated
    public static boolean isSingleField(final String skeleton) {
        final char first = skeleton.charAt(0);
        for (int i = 1; i < skeleton.length(); ++i) {
            if (skeleton.charAt(i) != first) {
                return false;
            }
        }
        return true;
    }
    
    private void setAvailableFormat(final String key) {
        this.checkFrozen();
        this.cldrAvailableFormatKeys.add(key);
    }
    
    private boolean isAvailableFormatSet(final String key) {
        return this.cldrAvailableFormatKeys.contains(key);
    }
    
    public boolean isFrozen() {
        return this.frozen;
    }
    
    public DateTimePatternGenerator freeze() {
        this.frozen = true;
        return this;
    }
    
    public DateTimePatternGenerator cloneAsThawed() {
        final DateTimePatternGenerator result = (DateTimePatternGenerator)this.clone();
        this.frozen = false;
        return result;
    }
    
    public Object clone() {
        try {
            final DateTimePatternGenerator result = (DateTimePatternGenerator)super.clone();
            result.skeleton2pattern = (TreeMap<DateTimeMatcher, PatternWithSkeletonFlag>)this.skeleton2pattern.clone();
            result.basePattern_pattern = (TreeMap<String, PatternWithSkeletonFlag>)this.basePattern_pattern.clone();
            result.appendItemFormats = this.appendItemFormats.clone();
            result.appendItemNames = this.appendItemNames.clone();
            result.current = new DateTimeMatcher();
            result.fp = new FormatParser();
            result._distanceInfo = new DistanceInfo();
            result.frozen = false;
            return result;
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Internal Error");
        }
    }
    
    @Deprecated
    public boolean skeletonsAreSimilar(final String id, final String skeleton) {
        if (id.equals(skeleton)) {
            return true;
        }
        final TreeSet<String> parser1 = this.getSet(id);
        final TreeSet<String> parser2 = this.getSet(skeleton);
        if (parser1.size() != parser2.size()) {
            return false;
        }
        final Iterator<String> it2 = parser2.iterator();
        for (final String item : parser1) {
            final int index1 = getCanonicalIndex(item, false);
            final String item2 = it2.next();
            final int index2 = getCanonicalIndex(item2, false);
            if (DateTimePatternGenerator.types[index1][1] != DateTimePatternGenerator.types[index2][1]) {
                return false;
            }
        }
        return true;
    }
    
    private TreeSet<String> getSet(final String id) {
        final List<Object> items = this.fp.set(id).getItems();
        final TreeSet<String> result = new TreeSet<String>();
        for (final Object obj : items) {
            final String item = obj.toString();
            if (!item.startsWith("G")) {
                if (item.startsWith("a")) {
                    continue;
                }
                result.add(item);
            }
        }
        return result;
    }
    
    private void checkFrozen() {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify frozen object");
        }
    }
    
    private String getBestAppending(final DateTimeMatcher source, final int missingFields, final DistanceInfo distInfo, final DateTimeMatcher skipMatcher, final int options) {
        String resultPattern = null;
        if (missingFields != 0) {
            final PatternWithMatcher resultPatternWithMatcher = this.getBestRaw(source, missingFields, distInfo, skipMatcher);
            resultPattern = this.adjustFieldTypes(resultPatternWithMatcher, source, false, options);
            while (distInfo.missingFieldMask != 0) {
                if ((distInfo.missingFieldMask & 0x6000) == 0x4000 && (missingFields & 0x6000) == 0x6000) {
                    resultPatternWithMatcher.pattern = resultPattern;
                    resultPattern = this.adjustFieldTypes(resultPatternWithMatcher, source, true, options);
                    distInfo.missingFieldMask &= 0xFFFFBFFF;
                }
                else {
                    final int startingMask = distInfo.missingFieldMask;
                    final PatternWithMatcher tempWithMatcher = this.getBestRaw(source, distInfo.missingFieldMask, distInfo, skipMatcher);
                    final String temp = this.adjustFieldTypes(tempWithMatcher, source, false, options);
                    final int foundMask = startingMask & ~distInfo.missingFieldMask;
                    final int topField = this.getTopBitNumber(foundMask);
                    resultPattern = MessageFormat.format(this.getAppendFormat(topField), resultPattern, temp, this.getAppendName(topField));
                }
            }
        }
        return resultPattern;
    }
    
    private String getAppendName(final int foundMask) {
        return "'" + this.appendItemNames[foundMask] + "'";
    }
    
    private String getAppendFormat(final int foundMask) {
        return this.appendItemFormats[foundMask];
    }
    
    private int getTopBitNumber(int foundMask) {
        int i;
        for (i = 0; foundMask != 0; foundMask >>>= 1, ++i) {}
        return i - 1;
    }
    
    private void complete() {
        final PatternInfo patternInfo = new PatternInfo();
        for (int i = 0; i < DateTimePatternGenerator.CANONICAL_ITEMS.length; ++i) {
            this.addPattern(String.valueOf(DateTimePatternGenerator.CANONICAL_ITEMS[i]), false, patternInfo);
        }
    }
    
    private PatternWithMatcher getBestRaw(final DateTimeMatcher source, final int includeMask, final DistanceInfo missingFields, final DateTimeMatcher skipMatcher) {
        int bestDistance = Integer.MAX_VALUE;
        final PatternWithMatcher bestPatternWithMatcher = new PatternWithMatcher("", null);
        final DistanceInfo tempInfo = new DistanceInfo();
        for (final DateTimeMatcher trial : this.skeleton2pattern.keySet()) {
            if (trial.equals(skipMatcher)) {
                continue;
            }
            final int distance = source.getDistance(trial, includeMask, tempInfo);
            if (distance >= bestDistance) {
                continue;
            }
            bestDistance = distance;
            final PatternWithSkeletonFlag patternWithSkelFlag = this.skeleton2pattern.get(trial);
            bestPatternWithMatcher.pattern = patternWithSkelFlag.pattern;
            if (patternWithSkelFlag.skeletonWasSpecified) {
                bestPatternWithMatcher.matcherWithSkeleton = trial;
            }
            else {
                bestPatternWithMatcher.matcherWithSkeleton = null;
            }
            missingFields.setTo(tempInfo);
            if (distance == 0) {
                break;
            }
        }
        return bestPatternWithMatcher;
    }
    
    private String adjustFieldTypes(final PatternWithMatcher patternWithMatcher, final DateTimeMatcher inputRequest, final boolean fixFractionalSeconds, final int options) {
        this.fp.set(patternWithMatcher.pattern);
        final StringBuilder newPattern = new StringBuilder();
        for (final Object item : this.fp.getItems()) {
            if (item instanceof String) {
                newPattern.append(this.fp.quoteLiteral((String)item));
            }
            else {
                final VariableField variableField = (VariableField)item;
                StringBuilder fieldBuilder = new StringBuilder(variableField.toString());
                final int type = variableField.getType();
                if (fixFractionalSeconds && type == 13) {
                    final String newField = inputRequest.original[14];
                    fieldBuilder.append(this.decimal);
                    fieldBuilder.append(newField);
                }
                else if (inputRequest.type[type] != 0) {
                    final String reqField = inputRequest.original[type];
                    int reqFieldLen = reqField.length();
                    if (reqField.charAt(0) == 'E' && reqFieldLen < 3) {
                        reqFieldLen = 3;
                    }
                    int adjFieldLen = reqFieldLen;
                    final DateTimeMatcher matcherWithSkeleton = patternWithMatcher.matcherWithSkeleton;
                    if ((type == 11 && (options & 0x800) == 0x0) || (type == 12 && (options & 0x1000) == 0x0) || (type == 13 && (options & 0x2000) == 0x0)) {
                        adjFieldLen = fieldBuilder.length();
                    }
                    else if (matcherWithSkeleton != null) {
                        final String skelField = matcherWithSkeleton.origStringForField(type);
                        final int skelFieldLen = skelField.length();
                        final boolean patFieldIsNumeric = variableField.isNumeric();
                        final boolean skelFieldIsNumeric = matcherWithSkeleton.fieldIsNumeric(type);
                        if (skelFieldLen == reqFieldLen || (patFieldIsNumeric && !skelFieldIsNumeric) || (skelFieldIsNumeric && !patFieldIsNumeric)) {
                            adjFieldLen = fieldBuilder.length();
                        }
                    }
                    final char c = (type != 11 && type != 3 && type != 6 && type != 1) ? reqField.charAt(0) : fieldBuilder.charAt(0);
                    fieldBuilder = new StringBuilder();
                    for (int i = adjFieldLen; i > 0; --i) {
                        fieldBuilder.append(c);
                    }
                }
                newPattern.append((CharSequence)fieldBuilder);
            }
        }
        return newPattern.toString();
    }
    
    @Deprecated
    public String getFields(final String pattern) {
        this.fp.set(pattern);
        final StringBuilder newPattern = new StringBuilder();
        for (final Object item : this.fp.getItems()) {
            if (item instanceof String) {
                newPattern.append(this.fp.quoteLiteral((String)item));
            }
            else {
                newPattern.append("{" + getName(item.toString()) + "}");
            }
        }
        return newPattern.toString();
    }
    
    private static String showMask(final int mask) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < 16; ++i) {
            if ((mask & 1 << i) != 0x0) {
                if (result.length() != 0) {
                    result.append(" | ");
                }
                result.append(DateTimePatternGenerator.FIELD_NAME[i]);
                result.append(" ");
            }
        }
        return result.toString();
    }
    
    private static String getName(final String s) {
        final int i = getCanonicalIndex(s, true);
        String name = DateTimePatternGenerator.FIELD_NAME[DateTimePatternGenerator.types[i][1]];
        int subtype = DateTimePatternGenerator.types[i][2];
        final boolean string = subtype < 0;
        if (string) {
            subtype = -subtype;
        }
        if (subtype < 0) {
            name += ":S";
        }
        else {
            name += ":N";
        }
        return name;
    }
    
    private static int getCanonicalIndex(final String s, final boolean strict) {
        final int len = s.length();
        if (len == 0) {
            return -1;
        }
        final int ch = s.charAt(0);
        for (int i = 1; i < len; ++i) {
            if (s.charAt(i) != ch) {
                return -1;
            }
        }
        int bestRow = -1;
        for (int j = 0; j < DateTimePatternGenerator.types.length; ++j) {
            final int[] row = DateTimePatternGenerator.types[j];
            if (row[0] == ch) {
                bestRow = j;
                if (row[3] <= len) {
                    if (row[row.length - 1] >= len) {
                        return j;
                    }
                }
            }
        }
        return strict ? -1 : bestRow;
    }
    
    static {
        DateTimePatternGenerator.DTPNG_CACHE = new SimpleCache<String, DateTimePatternGenerator>();
        CLDR_FIELD_APPEND = new String[] { "Era", "Year", "Quarter", "Month", "Week", "*", "Day-Of-Week", "Day", "*", "*", "*", "Hour", "Minute", "Second", "*", "Timezone" };
        CLDR_FIELD_NAME = new String[] { "era", "year", "*", "month", "week", "*", "weekday", "day", "*", "*", "dayperiod", "hour", "minute", "second", "*", "zone" };
        FIELD_NAME = new String[] { "Era", "Year", "Quarter", "Month", "Week_in_Year", "Week_in_Month", "Weekday", "Day", "Day_Of_Year", "Day_of_Week_in_Month", "Dayperiod", "Hour", "Minute", "Second", "Fractional_Second", "Zone" };
        CANONICAL_ITEMS = new String[] { "G", "y", "Q", "M", "w", "W", "E", "d", "D", "F", "H", "m", "s", "S", "v" };
        CANONICAL_SET = new HashSet<String>(Arrays.asList(DateTimePatternGenerator.CANONICAL_ITEMS));
        types = new int[][] { { 71, 0, -258, 1, 3 }, { 71, 0, -259, 4 }, { 121, 1, 256, 1, 20 }, { 89, 1, 272, 1, 20 }, { 117, 1, 288, 1, 20 }, { 85, 1, -258, 1, 3 }, { 85, 1, -259, 4 }, { 85, 1, -257, 5 }, { 81, 2, 256, 1, 2 }, { 81, 2, -258, 3 }, { 81, 2, -259, 4 }, { 113, 2, 272, 1, 2 }, { 113, 2, -242, 3 }, { 113, 2, -243, 4 }, { 77, 3, 256, 1, 2 }, { 77, 3, -258, 3 }, { 77, 3, -259, 4 }, { 77, 3, -257, 5 }, { 76, 3, 272, 1, 2 }, { 76, 3, -274, 3 }, { 76, 3, -275, 4 }, { 76, 3, -273, 5 }, { 108, 3, 272, 1, 1 }, { 119, 4, 256, 1, 2 }, { 87, 5, 272, 1 }, { 69, 6, -258, 1, 3 }, { 69, 6, -259, 4 }, { 69, 6, -257, 5 }, { 99, 6, 288, 1, 2 }, { 99, 6, -290, 3 }, { 99, 6, -291, 4 }, { 99, 6, -289, 5 }, { 101, 6, 272, 1, 2 }, { 101, 6, -274, 3 }, { 101, 6, -275, 4 }, { 101, 6, -273, 5 }, { 100, 7, 256, 1, 2 }, { 68, 8, 272, 1, 3 }, { 70, 9, 288, 1 }, { 103, 7, 304, 1, 20 }, { 97, 10, -258, 1 }, { 72, 11, 416, 1, 2 }, { 107, 11, 432, 1, 2 }, { 104, 11, 256, 1, 2 }, { 75, 11, 272, 1, 2 }, { 109, 12, 256, 1, 2 }, { 115, 13, 256, 1, 2 }, { 83, 14, 272, 1, 1000 }, { 65, 13, 288, 1, 1000 }, { 118, 15, -290, 1 }, { 118, 15, -291, 4 }, { 122, 15, -258, 1, 3 }, { 122, 15, -259, 4 }, { 90, 15, -274, 1, 3 }, { 90, 15, -275, 4 }, { 86, 15, -274, 1, 3 }, { 86, 15, -275, 4 } };
    }
    
    public static final class PatternInfo
    {
        public static final int OK = 0;
        public static final int BASE_CONFLICT = 1;
        public static final int CONFLICT = 2;
        public int status;
        public String conflictingPattern;
    }
    
    public static class VariableField
    {
        private final String string;
        private final int canonicalIndex;
        
        @Deprecated
        public VariableField(final String string) {
            this(string, false);
        }
        
        @Deprecated
        public VariableField(final String string, final boolean strict) {
            this.canonicalIndex = getCanonicalIndex(string, strict);
            if (this.canonicalIndex < 0) {
                throw new IllegalArgumentException("Illegal datetime field:\t" + string);
            }
            this.string = string;
        }
        
        @Deprecated
        public int getType() {
            return DateTimePatternGenerator.types[this.canonicalIndex][1];
        }
        
        @Deprecated
        public static String getCanonicalCode(final int type) {
            try {
                return DateTimePatternGenerator.CANONICAL_ITEMS[type];
            }
            catch (Exception e) {
                return String.valueOf(type);
            }
        }
        
        @Deprecated
        public boolean isNumeric() {
            return DateTimePatternGenerator.types[this.canonicalIndex][2] > 0;
        }
        
        private int getCanonicalIndex() {
            return this.canonicalIndex;
        }
        
        @Override
        @Deprecated
        public String toString() {
            return this.string;
        }
    }
    
    public static class FormatParser
    {
        private transient PatternTokenizer tokenizer;
        private List<Object> items;
        
        @Deprecated
        public FormatParser() {
            this.tokenizer = new PatternTokenizer().setSyntaxCharacters(new UnicodeSet("[a-zA-Z]")).setExtraQuotingCharacters(new UnicodeSet("[[[:script=Latn:][:script=Cyrl:]]&[[:L:][:M:]]]")).setUsingQuote(true);
            this.items = new ArrayList<Object>();
        }
        
        @Deprecated
        public final FormatParser set(final String string) {
            return this.set(string, false);
        }
        
        @Deprecated
        public FormatParser set(final String string, final boolean strict) {
            this.items.clear();
            if (string.length() == 0) {
                return this;
            }
            this.tokenizer.setPattern(string);
            final StringBuffer buffer = new StringBuffer();
            final StringBuffer variable = new StringBuffer();
            while (true) {
                buffer.setLength(0);
                final int status = this.tokenizer.next(buffer);
                if (status == 0) {
                    break;
                }
                if (status == 1) {
                    if (variable.length() != 0 && buffer.charAt(0) != variable.charAt(0)) {
                        this.addVariable(variable, false);
                    }
                    variable.append(buffer);
                }
                else {
                    this.addVariable(variable, false);
                    this.items.add(buffer.toString());
                }
            }
            this.addVariable(variable, false);
            return this;
        }
        
        private void addVariable(final StringBuffer variable, final boolean strict) {
            if (variable.length() != 0) {
                this.items.add(new VariableField(variable.toString(), strict));
                variable.setLength(0);
            }
        }
        
        @Deprecated
        public List<Object> getItems() {
            return this.items;
        }
        
        @Override
        @Deprecated
        public String toString() {
            return this.toString(0, this.items.size());
        }
        
        @Deprecated
        public String toString(final int start, final int limit) {
            final StringBuilder result = new StringBuilder();
            for (int i = start; i < limit; ++i) {
                final Object item = this.items.get(i);
                if (item instanceof String) {
                    final String itemString = (String)item;
                    result.append(this.tokenizer.quoteLiteral(itemString));
                }
                else {
                    result.append(this.items.get(i).toString());
                }
            }
            return result.toString();
        }
        
        @Deprecated
        public boolean hasDateAndTimeFields() {
            int foundMask = 0;
            for (final Object item : this.items) {
                if (item instanceof VariableField) {
                    final int type = ((VariableField)item).getType();
                    foundMask |= 1 << type;
                }
            }
            final boolean isDate = (foundMask & 0x3FF) != 0x0;
            final boolean isTime = (foundMask & 0xFC00) != 0x0;
            return isDate && isTime;
        }
        
        @Deprecated
        public Object quoteLiteral(final String string) {
            return this.tokenizer.quoteLiteral(string);
        }
    }
    
    private static class PatternWithMatcher
    {
        public String pattern;
        public DateTimeMatcher matcherWithSkeleton;
        
        public PatternWithMatcher(final String pat, final DateTimeMatcher matcher) {
            this.pattern = pat;
            this.matcherWithSkeleton = matcher;
        }
    }
    
    private static class PatternWithSkeletonFlag
    {
        public String pattern;
        public boolean skeletonWasSpecified;
        
        public PatternWithSkeletonFlag(final String pat, final boolean skelSpecified) {
            this.pattern = pat;
            this.skeletonWasSpecified = skelSpecified;
        }
        
        @Override
        public String toString() {
            return this.pattern + "," + this.skeletonWasSpecified;
        }
    }
    
    private static class DateTimeMatcher implements Comparable<DateTimeMatcher>
    {
        private int[] type;
        private String[] original;
        private String[] baseOriginal;
        
        private DateTimeMatcher() {
            this.type = new int[16];
            this.original = new String[16];
            this.baseOriginal = new String[16];
        }
        
        public String origStringForField(final int field) {
            return this.original[field];
        }
        
        public boolean fieldIsNumeric(final int field) {
            return this.type[field] > 0;
        }
        
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            for (int i = 0; i < 16; ++i) {
                if (this.original[i].length() != 0) {
                    result.append(this.original[i]);
                }
            }
            return result.toString();
        }
        
        public String toCanonicalString() {
            final StringBuilder result = new StringBuilder();
            for (int i = 0; i < 16; ++i) {
                if (this.original[i].length() != 0) {
                    for (int j = 0; j < DateTimePatternGenerator.types.length; ++j) {
                        final int[] row = DateTimePatternGenerator.types[j];
                        if (row[1] == i) {
                            final char originalChar = this.original[i].charAt(0);
                            final char repeatChar = (originalChar == 'h' || originalChar == 'K') ? 'h' : ((char)row[0]);
                            result.append(Utility.repeat(String.valueOf(repeatChar), this.original[i].length()));
                            break;
                        }
                    }
                }
            }
            return result.toString();
        }
        
        String getBasePattern() {
            final StringBuilder result = new StringBuilder();
            for (int i = 0; i < 16; ++i) {
                if (this.baseOriginal[i].length() != 0) {
                    result.append(this.baseOriginal[i]);
                }
            }
            return result.toString();
        }
        
        DateTimeMatcher set(final String pattern, final FormatParser fp, final boolean allowDuplicateFields) {
            for (int i = 0; i < 16; ++i) {
                this.type[i] = 0;
                this.original[i] = "";
                this.baseOriginal[i] = "";
            }
            fp.set(pattern);
            for (final Object obj : fp.getItems()) {
                if (!(obj instanceof VariableField)) {
                    continue;
                }
                final VariableField item = (VariableField)obj;
                final String field = item.toString();
                if (field.charAt(0) == 'a') {
                    continue;
                }
                final int canonicalIndex = item.getCanonicalIndex();
                final int[] row = DateTimePatternGenerator.types[canonicalIndex];
                final int typeValue = row[1];
                if (this.original[typeValue].length() != 0) {
                    if (allowDuplicateFields) {
                        continue;
                    }
                    throw new IllegalArgumentException("Conflicting fields:\t" + this.original[typeValue] + ", " + field + "\t in " + pattern);
                }
                else {
                    this.original[typeValue] = field;
                    final char repeatChar = (char)row[0];
                    int repeatCount = row[3];
                    if ("GEzvQ".indexOf(repeatChar) >= 0) {
                        repeatCount = 1;
                    }
                    this.baseOriginal[typeValue] = Utility.repeat(String.valueOf(repeatChar), repeatCount);
                    int subTypeValue = row[2];
                    if (subTypeValue > 0) {
                        subTypeValue += field.length();
                    }
                    this.type[typeValue] = subTypeValue;
                }
            }
            return this;
        }
        
        int getFieldMask() {
            int result = 0;
            for (int i = 0; i < this.type.length; ++i) {
                if (this.type[i] != 0) {
                    result |= 1 << i;
                }
            }
            return result;
        }
        
        void extractFrom(final DateTimeMatcher source, final int fieldMask) {
            for (int i = 0; i < this.type.length; ++i) {
                if ((fieldMask & 1 << i) != 0x0) {
                    this.type[i] = source.type[i];
                    this.original[i] = source.original[i];
                }
                else {
                    this.type[i] = 0;
                    this.original[i] = "";
                }
            }
        }
        
        int getDistance(final DateTimeMatcher other, final int includeMask, final DistanceInfo distanceInfo) {
            int result = 0;
            distanceInfo.clear();
            for (int i = 0; i < this.type.length; ++i) {
                final int myType = ((includeMask & 1 << i) == 0x0) ? 0 : this.type[i];
                final int otherType = other.type[i];
                if (myType != otherType) {
                    if (myType == 0) {
                        result += 65536;
                        distanceInfo.addExtra(i);
                    }
                    else if (otherType == 0) {
                        result += 4096;
                        distanceInfo.addMissing(i);
                    }
                    else {
                        result += Math.abs(myType - otherType);
                    }
                }
            }
            return result;
        }
        
        public int compareTo(final DateTimeMatcher that) {
            for (int i = 0; i < this.original.length; ++i) {
                final int comp = this.original[i].compareTo(that.original[i]);
                if (comp != 0) {
                    return -comp;
                }
            }
            return 0;
        }
        
        @Override
        public boolean equals(final Object other) {
            if (!(other instanceof DateTimeMatcher)) {
                return false;
            }
            final DateTimeMatcher that = (DateTimeMatcher)other;
            for (int i = 0; i < this.original.length; ++i) {
                if (!this.original[i].equals(that.original[i])) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            int result = 0;
            for (int i = 0; i < this.original.length; ++i) {
                result ^= this.original[i].hashCode();
            }
            return result;
        }
    }
    
    private static class DistanceInfo
    {
        int missingFieldMask;
        int extraFieldMask;
        
        void clear() {
            final int n = 0;
            this.extraFieldMask = n;
            this.missingFieldMask = n;
        }
        
        void setTo(final DistanceInfo other) {
            this.missingFieldMask = other.missingFieldMask;
            this.extraFieldMask = other.extraFieldMask;
        }
        
        void addMissing(final int field) {
            this.missingFieldMask |= 1 << field;
        }
        
        void addExtra(final int field) {
            this.extraFieldMask |= 1 << field;
        }
        
        @Override
        public String toString() {
            return "missingFieldMask: " + showMask(this.missingFieldMask) + ", extraFieldMask: " + showMask(this.extraFieldMask);
        }
    }
}
