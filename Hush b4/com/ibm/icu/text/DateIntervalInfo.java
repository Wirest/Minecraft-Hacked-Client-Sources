// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.impl.SimpleCache;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.util.Calendar;
import java.util.HashSet;
import com.ibm.icu.util.ULocale;
import java.util.HashMap;
import java.util.Map;
import com.ibm.icu.impl.ICUCache;
import java.io.Serializable;
import com.ibm.icu.util.Freezable;

public class DateIntervalInfo implements Cloneable, Freezable<DateIntervalInfo>, Serializable
{
    static final int currentSerialVersion = 1;
    static final String[] CALENDAR_FIELD_TO_PATTERN_LETTER;
    private static final long serialVersionUID = 1L;
    private static final int MINIMUM_SUPPORTED_CALENDAR_FIELD = 12;
    private static String FALLBACK_STRING;
    private static String LATEST_FIRST_PREFIX;
    private static String EARLIEST_FIRST_PREFIX;
    private static final ICUCache<String, DateIntervalInfo> DIICACHE;
    private String fFallbackIntervalPattern;
    private boolean fFirstDateInPtnIsLaterDate;
    private Map<String, Map<String, PatternInfo>> fIntervalPatterns;
    private transient boolean frozen;
    private transient boolean fIntervalPatternsReadOnly;
    
    @Deprecated
    public DateIntervalInfo() {
        this.fFirstDateInPtnIsLaterDate = false;
        this.fIntervalPatterns = null;
        this.frozen = false;
        this.fIntervalPatternsReadOnly = false;
        this.fIntervalPatterns = new HashMap<String, Map<String, PatternInfo>>();
        this.fFallbackIntervalPattern = "{0} \u2013 {1}";
    }
    
    public DateIntervalInfo(final ULocale locale) {
        this.fFirstDateInPtnIsLaterDate = false;
        this.fIntervalPatterns = null;
        this.frozen = false;
        this.fIntervalPatternsReadOnly = false;
        this.initializeData(locale);
    }
    
    private void initializeData(final ULocale locale) {
        final String key = locale.toString();
        final DateIntervalInfo dii = DateIntervalInfo.DIICACHE.get(key);
        if (dii == null) {
            this.setup(locale);
            this.fIntervalPatternsReadOnly = true;
            DateIntervalInfo.DIICACHE.put(key, ((DateIntervalInfo)this.clone()).freeze());
        }
        else {
            this.initializeFromReadOnlyPatterns(dii);
        }
    }
    
    private void initializeFromReadOnlyPatterns(final DateIntervalInfo dii) {
        this.fFallbackIntervalPattern = dii.fFallbackIntervalPattern;
        this.fFirstDateInPtnIsLaterDate = dii.fFirstDateInPtnIsLaterDate;
        this.fIntervalPatterns = dii.fIntervalPatterns;
        this.fIntervalPatternsReadOnly = true;
    }
    
    private void setup(final ULocale locale) {
        final int DEFAULT_HASH_SIZE = 19;
        this.fIntervalPatterns = new HashMap<String, Map<String, PatternInfo>>(DEFAULT_HASH_SIZE);
        this.fFallbackIntervalPattern = "{0} \u2013 {1}";
        final HashSet<String> skeletonSet = new HashSet<String>();
        try {
            ULocale currentLocale = locale;
            String calendarTypeToUse = locale.getKeywordValue("calendar");
            if (calendarTypeToUse == null) {
                final String[] preferredCalendarTypes = Calendar.getKeywordValuesForLocale("calendar", locale, true);
                calendarTypeToUse = preferredCalendarTypes[0];
            }
            if (calendarTypeToUse == null) {
                calendarTypeToUse = "gregorian";
            }
            do {
                final String name = currentLocale.getName();
                if (name.length() == 0) {
                    break;
                }
                final ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", currentLocale);
                final ICUResourceBundle itvDtPtnResource = rb.getWithFallback("calendar/" + calendarTypeToUse + "/intervalFormats");
                final String fallback = itvDtPtnResource.getStringWithFallback(DateIntervalInfo.FALLBACK_STRING);
                this.setFallbackIntervalPattern(fallback);
                for (int size = itvDtPtnResource.getSize(), index = 0; index < size; ++index) {
                    final String skeleton = itvDtPtnResource.get(index).getKey();
                    if (!skeletonSet.contains(skeleton)) {
                        skeletonSet.add(skeleton);
                        if (skeleton.compareTo(DateIntervalInfo.FALLBACK_STRING) != 0) {
                            final ICUResourceBundle intervalPatterns = (ICUResourceBundle)itvDtPtnResource.get(skeleton);
                            for (int ptnNum = intervalPatterns.getSize(), ptnIndex = 0; ptnIndex < ptnNum; ++ptnIndex) {
                                final String key = intervalPatterns.get(ptnIndex).getKey();
                                final String pattern = intervalPatterns.get(ptnIndex).getString();
                                int calendarField = -1;
                                if (key.compareTo(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[1]) == 0) {
                                    calendarField = 1;
                                }
                                else if (key.compareTo(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[2]) == 0) {
                                    calendarField = 2;
                                }
                                else if (key.compareTo(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[5]) == 0) {
                                    calendarField = 5;
                                }
                                else if (key.compareTo(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[9]) == 0) {
                                    calendarField = 9;
                                }
                                else if (key.compareTo(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[10]) == 0) {
                                    calendarField = 10;
                                }
                                else if (key.compareTo(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[12]) == 0) {
                                    calendarField = 12;
                                }
                                if (calendarField != -1) {
                                    this.setIntervalPatternInternally(skeleton, key, pattern);
                                }
                            }
                        }
                    }
                }
                try {
                    final UResourceBundle parentNameBundle = rb.get("%%Parent");
                    currentLocale = new ULocale(parentNameBundle.getString());
                }
                catch (MissingResourceException e) {
                    currentLocale = currentLocale.getFallback();
                }
            } while (currentLocale != null && !currentLocale.getBaseName().equals("root"));
        }
        catch (MissingResourceException ex) {}
    }
    
    private static int splitPatternInto2Part(final String intervalPattern) {
        boolean inQuote = false;
        char prevCh = '\0';
        int count = 0;
        final int[] patternRepeated = new int[58];
        final int PATTERN_CHAR_BASE = 65;
        boolean foundRepetition = false;
        int i;
        for (i = 0; i < intervalPattern.length(); ++i) {
            final char ch = intervalPattern.charAt(i);
            if (ch != prevCh && count > 0) {
                final int repeated = patternRepeated[prevCh - PATTERN_CHAR_BASE];
                if (repeated != 0) {
                    foundRepetition = true;
                    break;
                }
                patternRepeated[prevCh - PATTERN_CHAR_BASE] = 1;
                count = 0;
            }
            if (ch == '\'') {
                if (i + 1 < intervalPattern.length() && intervalPattern.charAt(i + 1) == '\'') {
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
        if (count > 0 && !foundRepetition && patternRepeated[prevCh - PATTERN_CHAR_BASE] == 0) {
            count = 0;
        }
        return i - count;
    }
    
    public void setIntervalPattern(final String skeleton, final int lrgDiffCalUnit, final String intervalPattern) {
        if (this.frozen) {
            throw new UnsupportedOperationException("no modification is allowed after DII is frozen");
        }
        if (lrgDiffCalUnit > 12) {
            throw new IllegalArgumentException("calendar field is larger than MINIMUM_SUPPORTED_CALENDAR_FIELD");
        }
        if (this.fIntervalPatternsReadOnly) {
            this.fIntervalPatterns = cloneIntervalPatterns(this.fIntervalPatterns);
            this.fIntervalPatternsReadOnly = false;
        }
        final PatternInfo ptnInfo = this.setIntervalPatternInternally(skeleton, DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[lrgDiffCalUnit], intervalPattern);
        if (lrgDiffCalUnit == 11) {
            this.setIntervalPattern(skeleton, DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[9], ptnInfo);
            this.setIntervalPattern(skeleton, DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[10], ptnInfo);
        }
        else if (lrgDiffCalUnit == 5 || lrgDiffCalUnit == 7) {
            this.setIntervalPattern(skeleton, DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[5], ptnInfo);
        }
    }
    
    private PatternInfo setIntervalPatternInternally(final String skeleton, final String lrgDiffCalUnit, String intervalPattern) {
        Map<String, PatternInfo> patternsOfOneSkeleton = this.fIntervalPatterns.get(skeleton);
        boolean emptyHash = false;
        if (patternsOfOneSkeleton == null) {
            patternsOfOneSkeleton = new HashMap<String, PatternInfo>();
            emptyHash = true;
        }
        boolean order = this.fFirstDateInPtnIsLaterDate;
        if (intervalPattern.startsWith(DateIntervalInfo.LATEST_FIRST_PREFIX)) {
            order = true;
            final int prefixLength = DateIntervalInfo.LATEST_FIRST_PREFIX.length();
            intervalPattern = intervalPattern.substring(prefixLength, intervalPattern.length());
        }
        else if (intervalPattern.startsWith(DateIntervalInfo.EARLIEST_FIRST_PREFIX)) {
            order = false;
            final int earliestFirstLength = DateIntervalInfo.EARLIEST_FIRST_PREFIX.length();
            intervalPattern = intervalPattern.substring(earliestFirstLength, intervalPattern.length());
        }
        final PatternInfo itvPtnInfo = genPatternInfo(intervalPattern, order);
        patternsOfOneSkeleton.put(lrgDiffCalUnit, itvPtnInfo);
        if (emptyHash) {
            this.fIntervalPatterns.put(skeleton, patternsOfOneSkeleton);
        }
        return itvPtnInfo;
    }
    
    private void setIntervalPattern(final String skeleton, final String lrgDiffCalUnit, final PatternInfo ptnInfo) {
        final Map<String, PatternInfo> patternsOfOneSkeleton = this.fIntervalPatterns.get(skeleton);
        patternsOfOneSkeleton.put(lrgDiffCalUnit, ptnInfo);
    }
    
    static PatternInfo genPatternInfo(final String intervalPattern, final boolean laterDateFirst) {
        final int splitPoint = splitPatternInto2Part(intervalPattern);
        final String firstPart = intervalPattern.substring(0, splitPoint);
        String secondPart = null;
        if (splitPoint < intervalPattern.length()) {
            secondPart = intervalPattern.substring(splitPoint, intervalPattern.length());
        }
        return new PatternInfo(firstPart, secondPart, laterDateFirst);
    }
    
    public PatternInfo getIntervalPattern(final String skeleton, final int field) {
        if (field > 12) {
            throw new IllegalArgumentException("no support for field less than MINUTE");
        }
        final Map<String, PatternInfo> patternsOfOneSkeleton = this.fIntervalPatterns.get(skeleton);
        if (patternsOfOneSkeleton != null) {
            final PatternInfo intervalPattern = patternsOfOneSkeleton.get(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field]);
            if (intervalPattern != null) {
                return intervalPattern;
            }
        }
        return null;
    }
    
    public String getFallbackIntervalPattern() {
        return this.fFallbackIntervalPattern;
    }
    
    public void setFallbackIntervalPattern(final String fallbackPattern) {
        if (this.frozen) {
            throw new UnsupportedOperationException("no modification is allowed after DII is frozen");
        }
        final int firstPatternIndex = fallbackPattern.indexOf("{0}");
        final int secondPatternIndex = fallbackPattern.indexOf("{1}");
        if (firstPatternIndex == -1 || secondPatternIndex == -1) {
            throw new IllegalArgumentException("no pattern {0} or pattern {1} in fallbackPattern");
        }
        if (firstPatternIndex > secondPatternIndex) {
            this.fFirstDateInPtnIsLaterDate = true;
        }
        this.fFallbackIntervalPattern = fallbackPattern;
    }
    
    public boolean getDefaultOrder() {
        return this.fFirstDateInPtnIsLaterDate;
    }
    
    public Object clone() {
        if (this.frozen) {
            return this;
        }
        return this.cloneUnfrozenDII();
    }
    
    private Object cloneUnfrozenDII() {
        try {
            final DateIntervalInfo other = (DateIntervalInfo)super.clone();
            other.fFallbackIntervalPattern = this.fFallbackIntervalPattern;
            other.fFirstDateInPtnIsLaterDate = this.fFirstDateInPtnIsLaterDate;
            if (this.fIntervalPatternsReadOnly) {
                other.fIntervalPatterns = this.fIntervalPatterns;
                other.fIntervalPatternsReadOnly = true;
            }
            else {
                other.fIntervalPatterns = cloneIntervalPatterns(this.fIntervalPatterns);
                other.fIntervalPatternsReadOnly = false;
            }
            other.frozen = false;
            return other;
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException("clone is not supported");
        }
    }
    
    private static Map<String, Map<String, PatternInfo>> cloneIntervalPatterns(final Map<String, Map<String, PatternInfo>> patterns) {
        final Map<String, Map<String, PatternInfo>> result = new HashMap<String, Map<String, PatternInfo>>();
        for (final Map.Entry<String, Map<String, PatternInfo>> skeletonEntry : patterns.entrySet()) {
            final String skeleton = skeletonEntry.getKey();
            final Map<String, PatternInfo> patternsOfOneSkeleton = skeletonEntry.getValue();
            final Map<String, PatternInfo> oneSetPtn = new HashMap<String, PatternInfo>();
            for (final Map.Entry<String, PatternInfo> calEntry : patternsOfOneSkeleton.entrySet()) {
                final String calField = calEntry.getKey();
                final PatternInfo value = calEntry.getValue();
                oneSetPtn.put(calField, value);
            }
            result.put(skeleton, oneSetPtn);
        }
        return result;
    }
    
    public boolean isFrozen() {
        return this.frozen;
    }
    
    public DateIntervalInfo freeze() {
        this.frozen = true;
        this.fIntervalPatternsReadOnly = true;
        return this;
    }
    
    public DateIntervalInfo cloneAsThawed() {
        final DateIntervalInfo result = (DateIntervalInfo)this.cloneUnfrozenDII();
        return result;
    }
    
    static void parseSkeleton(final String skeleton, final int[] skeletonFieldWidth) {
        final int PATTERN_CHAR_BASE = 65;
        for (int i = 0; i < skeleton.length(); ++i) {
            final int n = skeleton.charAt(i) - PATTERN_CHAR_BASE;
            ++skeletonFieldWidth[n];
        }
    }
    
    private static boolean stringNumeric(final int fieldWidth, final int anotherFieldWidth, final char patternLetter) {
        return patternLetter == 'M' && ((fieldWidth <= 2 && anotherFieldWidth > 2) || (fieldWidth > 2 && anotherFieldWidth <= 2));
    }
    
    DateIntervalFormat.BestMatchInfo getBestSkeleton(String inputSkeleton) {
        String bestSkeleton = inputSkeleton;
        final int[] inputSkeletonFieldWidth = new int[58];
        final int[] skeletonFieldWidth = new int[58];
        final int DIFFERENT_FIELD = 4096;
        final int STRING_NUMERIC_DIFFERENCE = 256;
        final int BASE = 65;
        boolean replaceZWithV = false;
        if (inputSkeleton.indexOf(122) != -1) {
            inputSkeleton = inputSkeleton.replace('z', 'v');
            replaceZWithV = true;
        }
        parseSkeleton(inputSkeleton, inputSkeletonFieldWidth);
        int bestDistance = Integer.MAX_VALUE;
        int bestFieldDifference = 0;
        for (final String skeleton : this.fIntervalPatterns.keySet()) {
            for (int i = 0; i < skeletonFieldWidth.length; ++i) {
                skeletonFieldWidth[i] = 0;
            }
            parseSkeleton(skeleton, skeletonFieldWidth);
            int distance = 0;
            int fieldDifference = 1;
            for (int j = 0; j < inputSkeletonFieldWidth.length; ++j) {
                final int inputFieldWidth = inputSkeletonFieldWidth[j];
                final int fieldWidth = skeletonFieldWidth[j];
                if (inputFieldWidth != fieldWidth) {
                    if (inputFieldWidth == 0) {
                        fieldDifference = -1;
                        distance += 4096;
                    }
                    else if (fieldWidth == 0) {
                        fieldDifference = -1;
                        distance += 4096;
                    }
                    else if (stringNumeric(inputFieldWidth, fieldWidth, (char)(j + 65))) {
                        distance += 256;
                    }
                    else {
                        distance += Math.abs(inputFieldWidth - fieldWidth);
                    }
                }
            }
            if (distance < bestDistance) {
                bestSkeleton = skeleton;
                bestDistance = distance;
                bestFieldDifference = fieldDifference;
            }
            if (distance == 0) {
                bestFieldDifference = 0;
                break;
            }
        }
        if (replaceZWithV && bestFieldDifference != -1) {
            bestFieldDifference = 2;
        }
        return new DateIntervalFormat.BestMatchInfo(bestSkeleton, bestFieldDifference);
    }
    
    @Override
    public boolean equals(final Object a) {
        if (a instanceof DateIntervalInfo) {
            final DateIntervalInfo dtInfo = (DateIntervalInfo)a;
            return this.fIntervalPatterns.equals(dtInfo.fIntervalPatterns);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.fIntervalPatterns.hashCode();
    }
    
    @Deprecated
    public Map<String, Set<String>> getPatterns() {
        final LinkedHashMap<String, Set<String>> result = new LinkedHashMap<String, Set<String>>();
        for (final Map.Entry<String, Map<String, PatternInfo>> entry : this.fIntervalPatterns.entrySet()) {
            result.put(entry.getKey(), new LinkedHashSet<String>(entry.getValue().keySet()));
        }
        return result;
    }
    
    static {
        CALENDAR_FIELD_TO_PATTERN_LETTER = new String[] { "G", "y", "M", "w", "W", "d", "D", "E", "F", "a", "h", "H", "m" };
        DateIntervalInfo.FALLBACK_STRING = "fallback";
        DateIntervalInfo.LATEST_FIRST_PREFIX = "latestFirst:";
        DateIntervalInfo.EARLIEST_FIRST_PREFIX = "earliestFirst:";
        DIICACHE = new SimpleCache<String, DateIntervalInfo>();
    }
    
    public static final class PatternInfo implements Cloneable, Serializable
    {
        static final int currentSerialVersion = 1;
        private static final long serialVersionUID = 1L;
        private final String fIntervalPatternFirstPart;
        private final String fIntervalPatternSecondPart;
        private final boolean fFirstDateInPtnIsLaterDate;
        
        public PatternInfo(final String firstPart, final String secondPart, final boolean firstDateInPtnIsLaterDate) {
            this.fIntervalPatternFirstPart = firstPart;
            this.fIntervalPatternSecondPart = secondPart;
            this.fFirstDateInPtnIsLaterDate = firstDateInPtnIsLaterDate;
        }
        
        public String getFirstPart() {
            return this.fIntervalPatternFirstPart;
        }
        
        public String getSecondPart() {
            return this.fIntervalPatternSecondPart;
        }
        
        public boolean firstDateInPtnIsLaterDate() {
            return this.fFirstDateInPtnIsLaterDate;
        }
        
        @Override
        public boolean equals(final Object a) {
            if (a instanceof PatternInfo) {
                final PatternInfo patternInfo = (PatternInfo)a;
                return Utility.objectEquals(this.fIntervalPatternFirstPart, patternInfo.fIntervalPatternFirstPart) && Utility.objectEquals(this.fIntervalPatternSecondPart, this.fIntervalPatternSecondPart) && this.fFirstDateInPtnIsLaterDate == patternInfo.fFirstDateInPtnIsLaterDate;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int hash = (this.fIntervalPatternFirstPart != null) ? this.fIntervalPatternFirstPart.hashCode() : 0;
            if (this.fIntervalPatternSecondPart != null) {
                hash ^= this.fIntervalPatternSecondPart.hashCode();
            }
            if (this.fFirstDateInPtnIsLaterDate) {
                hash ^= -1;
            }
            return hash;
        }
    }
}
