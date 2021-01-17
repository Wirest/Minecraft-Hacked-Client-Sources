// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.SimpleCache;
import java.io.IOException;
import java.io.ObjectInputStream;
import com.ibm.icu.impl.CalendarData;
import java.util.HashMap;
import java.util.Collections;
import java.text.ParsePosition;
import com.ibm.icu.util.DateInterval;
import java.text.FieldPosition;
import java.util.Locale;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.Calendar;
import java.util.Map;
import com.ibm.icu.impl.ICUCache;

public class DateIntervalFormat extends UFormat
{
    private static final long serialVersionUID = 1L;
    private static ICUCache<String, Map<String, DateIntervalInfo.PatternInfo>> LOCAL_PATTERN_CACHE;
    private DateIntervalInfo fInfo;
    private SimpleDateFormat fDateFormat;
    private Calendar fFromCalendar;
    private Calendar fToCalendar;
    private String fSkeleton;
    private boolean isDateIntervalInfoDefault;
    private transient Map<String, DateIntervalInfo.PatternInfo> fIntervalPatterns;
    
    private DateIntervalFormat() {
        this.fSkeleton = null;
        this.fIntervalPatterns = null;
    }
    
    @Deprecated
    public DateIntervalFormat(final String skeleton, final DateIntervalInfo dtItvInfo, final SimpleDateFormat simpleDateFormat) {
        this.fSkeleton = null;
        this.fIntervalPatterns = null;
        this.fDateFormat = simpleDateFormat;
        dtItvInfo.freeze();
        this.fSkeleton = skeleton;
        this.fInfo = dtItvInfo;
        this.isDateIntervalInfoDefault = false;
        this.fFromCalendar = (Calendar)this.fDateFormat.getCalendar().clone();
        this.fToCalendar = (Calendar)this.fDateFormat.getCalendar().clone();
        this.initializePattern(null);
    }
    
    private DateIntervalFormat(final String skeleton, final ULocale locale, final SimpleDateFormat simpleDateFormat) {
        this.fSkeleton = null;
        this.fIntervalPatterns = null;
        this.fDateFormat = simpleDateFormat;
        this.fSkeleton = skeleton;
        this.fInfo = new DateIntervalInfo(locale).freeze();
        this.isDateIntervalInfoDefault = true;
        this.fFromCalendar = (Calendar)this.fDateFormat.getCalendar().clone();
        this.fToCalendar = (Calendar)this.fDateFormat.getCalendar().clone();
        this.initializePattern(DateIntervalFormat.LOCAL_PATTERN_CACHE);
    }
    
    public static final DateIntervalFormat getInstance(final String skeleton) {
        return getInstance(skeleton, ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public static final DateIntervalFormat getInstance(final String skeleton, final Locale locale) {
        return getInstance(skeleton, ULocale.forLocale(locale));
    }
    
    public static final DateIntervalFormat getInstance(final String skeleton, final ULocale locale) {
        final DateTimePatternGenerator generator = DateTimePatternGenerator.getInstance(locale);
        return new DateIntervalFormat(skeleton, locale, new SimpleDateFormat(generator.getBestPattern(skeleton), locale));
    }
    
    public static final DateIntervalFormat getInstance(final String skeleton, final DateIntervalInfo dtitvinf) {
        return getInstance(skeleton, ULocale.getDefault(ULocale.Category.FORMAT), dtitvinf);
    }
    
    public static final DateIntervalFormat getInstance(final String skeleton, final Locale locale, final DateIntervalInfo dtitvinf) {
        return getInstance(skeleton, ULocale.forLocale(locale), dtitvinf);
    }
    
    public static final DateIntervalFormat getInstance(final String skeleton, final ULocale locale, DateIntervalInfo dtitvinf) {
        dtitvinf = (DateIntervalInfo)dtitvinf.clone();
        final DateTimePatternGenerator generator = DateTimePatternGenerator.getInstance(locale);
        return new DateIntervalFormat(skeleton, dtitvinf, new SimpleDateFormat(generator.getBestPattern(skeleton), locale));
    }
    
    @Override
    public Object clone() {
        final DateIntervalFormat other = (DateIntervalFormat)super.clone();
        other.fDateFormat = (SimpleDateFormat)this.fDateFormat.clone();
        other.fInfo = (DateIntervalInfo)this.fInfo.clone();
        other.fFromCalendar = (Calendar)this.fFromCalendar.clone();
        other.fToCalendar = (Calendar)this.fToCalendar.clone();
        return other;
    }
    
    @Override
    public final StringBuffer format(final Object obj, final StringBuffer appendTo, final FieldPosition fieldPosition) {
        if (obj instanceof DateInterval) {
            return this.format((DateInterval)obj, appendTo, fieldPosition);
        }
        throw new IllegalArgumentException("Cannot format given Object (" + obj.getClass().getName() + ") as a DateInterval");
    }
    
    public final StringBuffer format(final DateInterval dtInterval, final StringBuffer appendTo, final FieldPosition fieldPosition) {
        this.fFromCalendar.setTimeInMillis(dtInterval.getFromDate());
        this.fToCalendar.setTimeInMillis(dtInterval.getToDate());
        return this.format(this.fFromCalendar, this.fToCalendar, appendTo, fieldPosition);
    }
    
    public final StringBuffer format(final Calendar fromCalendar, final Calendar toCalendar, final StringBuffer appendTo, final FieldPosition pos) {
        if (!fromCalendar.isEquivalentTo(toCalendar)) {
            throw new IllegalArgumentException("can not format on two different calendars");
        }
        int field = -1;
        if (fromCalendar.get(0) != toCalendar.get(0)) {
            field = 0;
        }
        else if (fromCalendar.get(1) != toCalendar.get(1)) {
            field = 1;
        }
        else if (fromCalendar.get(2) != toCalendar.get(2)) {
            field = 2;
        }
        else if (fromCalendar.get(5) != toCalendar.get(5)) {
            field = 5;
        }
        else if (fromCalendar.get(9) != toCalendar.get(9)) {
            field = 9;
        }
        else if (fromCalendar.get(10) != toCalendar.get(10)) {
            field = 10;
        }
        else {
            if (fromCalendar.get(12) == toCalendar.get(12)) {
                return this.fDateFormat.format(fromCalendar, appendTo, pos);
            }
            field = 12;
        }
        final DateIntervalInfo.PatternInfo intervalPattern = this.fIntervalPatterns.get(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field]);
        if (intervalPattern == null) {
            if (this.fDateFormat.isFieldUnitIgnored(field)) {
                return this.fDateFormat.format(fromCalendar, appendTo, pos);
            }
            return this.fallbackFormat(fromCalendar, toCalendar, appendTo, pos);
        }
        else {
            if (intervalPattern.getFirstPart() == null) {
                return this.fallbackFormat(fromCalendar, toCalendar, appendTo, pos, intervalPattern.getSecondPart());
            }
            Calendar firstCal;
            Calendar secondCal;
            if (intervalPattern.firstDateInPtnIsLaterDate()) {
                firstCal = toCalendar;
                secondCal = fromCalendar;
            }
            else {
                firstCal = fromCalendar;
                secondCal = toCalendar;
            }
            final String originalPattern = this.fDateFormat.toPattern();
            this.fDateFormat.applyPattern(intervalPattern.getFirstPart());
            this.fDateFormat.format(firstCal, appendTo, pos);
            if (intervalPattern.getSecondPart() != null) {
                this.fDateFormat.applyPattern(intervalPattern.getSecondPart());
                this.fDateFormat.format(secondCal, appendTo, pos);
            }
            this.fDateFormat.applyPattern(originalPattern);
            return appendTo;
        }
    }
    
    private final StringBuffer fallbackFormat(final Calendar fromCalendar, final Calendar toCalendar, final StringBuffer appendTo, final FieldPosition pos) {
        StringBuffer earlierDate = new StringBuffer(64);
        earlierDate = this.fDateFormat.format(fromCalendar, earlierDate, pos);
        StringBuffer laterDate = new StringBuffer(64);
        laterDate = this.fDateFormat.format(toCalendar, laterDate, pos);
        final String fallbackPattern = this.fInfo.getFallbackIntervalPattern();
        final String fallback = MessageFormat.format(fallbackPattern, earlierDate.toString(), laterDate.toString());
        appendTo.append(fallback);
        return appendTo;
    }
    
    private final StringBuffer fallbackFormat(final Calendar fromCalendar, final Calendar toCalendar, final StringBuffer appendTo, final FieldPosition pos, final String fullPattern) {
        final String originalPattern = this.fDateFormat.toPattern();
        this.fDateFormat.applyPattern(fullPattern);
        this.fallbackFormat(fromCalendar, toCalendar, appendTo, pos);
        this.fDateFormat.applyPattern(originalPattern);
        return appendTo;
    }
    
    @Override
    @Deprecated
    public Object parseObject(final String source, final ParsePosition parse_pos) {
        throw new UnsupportedOperationException("parsing is not supported");
    }
    
    public DateIntervalInfo getDateIntervalInfo() {
        return (DateIntervalInfo)this.fInfo.clone();
    }
    
    public void setDateIntervalInfo(final DateIntervalInfo newItvPattern) {
        this.fInfo = (DateIntervalInfo)newItvPattern.clone();
        this.isDateIntervalInfoDefault = false;
        this.fInfo.freeze();
        if (this.fDateFormat != null) {
            this.initializePattern(null);
        }
    }
    
    public DateFormat getDateFormat() {
        return (DateFormat)this.fDateFormat.clone();
    }
    
    private void initializePattern(final ICUCache<String, Map<String, DateIntervalInfo.PatternInfo>> cache) {
        final String fullPattern = this.fDateFormat.toPattern();
        final ULocale locale = this.fDateFormat.getLocale();
        String key = null;
        Map<String, DateIntervalInfo.PatternInfo> patterns = null;
        if (cache != null) {
            if (this.fSkeleton != null) {
                key = locale.toString() + "+" + fullPattern + "+" + this.fSkeleton;
            }
            else {
                key = locale.toString() + "+" + fullPattern;
            }
            patterns = cache.get(key);
        }
        if (patterns == null) {
            final Map<String, DateIntervalInfo.PatternInfo> intervalPatterns = this.initializeIntervalPattern(fullPattern, locale);
            patterns = Collections.unmodifiableMap((Map<? extends String, ? extends DateIntervalInfo.PatternInfo>)intervalPatterns);
            if (cache != null) {
                cache.put(key, patterns);
            }
        }
        this.fIntervalPatterns = patterns;
    }
    
    private Map<String, DateIntervalInfo.PatternInfo> initializeIntervalPattern(final String fullPattern, final ULocale locale) {
        final DateTimePatternGenerator dtpng = DateTimePatternGenerator.getInstance(locale);
        if (this.fSkeleton == null) {
            this.fSkeleton = dtpng.getSkeleton(fullPattern);
        }
        String skeleton = this.fSkeleton;
        final HashMap<String, DateIntervalInfo.PatternInfo> intervalPatterns = new HashMap<String, DateIntervalInfo.PatternInfo>();
        final StringBuilder date = new StringBuilder(skeleton.length());
        final StringBuilder normalizedDate = new StringBuilder(skeleton.length());
        final StringBuilder time = new StringBuilder(skeleton.length());
        final StringBuilder normalizedTime = new StringBuilder(skeleton.length());
        getDateTimeSkeleton(skeleton, date, normalizedDate, time, normalizedTime);
        final String dateSkeleton = date.toString();
        String timeSkeleton = time.toString();
        final String normalizedDateSkeleton = normalizedDate.toString();
        final String normalizedTimeSkeleton = normalizedTime.toString();
        final boolean found = this.genSeparateDateTimePtn(normalizedDateSkeleton, normalizedTimeSkeleton, intervalPatterns);
        if (!found) {
            if (time.length() != 0 && date.length() == 0) {
                timeSkeleton = "yMd" + timeSkeleton;
                final String pattern = dtpng.getBestPattern(timeSkeleton);
                final DateIntervalInfo.PatternInfo ptn = new DateIntervalInfo.PatternInfo(null, pattern, this.fInfo.getDefaultOrder());
                intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[5], ptn);
                intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[2], ptn);
                intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[1], ptn);
            }
            return intervalPatterns;
        }
        if (time.length() != 0) {
            if (date.length() == 0) {
                timeSkeleton = "yMd" + timeSkeleton;
                final String pattern = dtpng.getBestPattern(timeSkeleton);
                final DateIntervalInfo.PatternInfo ptn = new DateIntervalInfo.PatternInfo(null, pattern, this.fInfo.getDefaultOrder());
                intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[5], ptn);
                intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[2], ptn);
                intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[1], ptn);
            }
            else {
                if (!fieldExistsInSkeleton(5, dateSkeleton)) {
                    skeleton = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[5] + skeleton;
                    this.genFallbackPattern(5, skeleton, intervalPatterns, dtpng);
                }
                if (!fieldExistsInSkeleton(2, dateSkeleton)) {
                    skeleton = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[2] + skeleton;
                    this.genFallbackPattern(2, skeleton, intervalPatterns, dtpng);
                }
                if (!fieldExistsInSkeleton(1, dateSkeleton)) {
                    skeleton = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[1] + skeleton;
                    this.genFallbackPattern(1, skeleton, intervalPatterns, dtpng);
                }
                final CalendarData calData = new CalendarData(locale, null);
                final String[] patterns = calData.getDateTimePatterns();
                final String datePattern = dtpng.getBestPattern(dateSkeleton);
                this.concatSingleDate2TimeInterval(patterns[8], datePattern, 9, intervalPatterns);
                this.concatSingleDate2TimeInterval(patterns[8], datePattern, 10, intervalPatterns);
                this.concatSingleDate2TimeInterval(patterns[8], datePattern, 12, intervalPatterns);
            }
        }
        return intervalPatterns;
    }
    
    private void genFallbackPattern(final int field, final String skeleton, final Map<String, DateIntervalInfo.PatternInfo> intervalPatterns, final DateTimePatternGenerator dtpng) {
        final String pattern = dtpng.getBestPattern(skeleton);
        final DateIntervalInfo.PatternInfo ptn = new DateIntervalInfo.PatternInfo(null, pattern, this.fInfo.getDefaultOrder());
        intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field], ptn);
    }
    
    private static void getDateTimeSkeleton(final String skeleton, final StringBuilder dateSkeleton, final StringBuilder normalizedDateSkeleton, final StringBuilder timeSkeleton, final StringBuilder normalizedTimeSkeleton) {
        int ECount = 0;
        int dCount = 0;
        int MCount = 0;
        int yCount = 0;
        int hCount = 0;
        int HCount = 0;
        int mCount = 0;
        int vCount = 0;
        int zCount = 0;
        for (int i = 0; i < skeleton.length(); ++i) {
            final char ch = skeleton.charAt(i);
            switch (ch) {
                case 'E': {
                    dateSkeleton.append(ch);
                    ++ECount;
                    break;
                }
                case 'd': {
                    dateSkeleton.append(ch);
                    ++dCount;
                    break;
                }
                case 'M': {
                    dateSkeleton.append(ch);
                    ++MCount;
                    break;
                }
                case 'y': {
                    dateSkeleton.append(ch);
                    ++yCount;
                    break;
                }
                case 'D':
                case 'F':
                case 'G':
                case 'L':
                case 'Q':
                case 'W':
                case 'Y':
                case 'c':
                case 'e':
                case 'g':
                case 'l':
                case 'q':
                case 'u':
                case 'w': {
                    normalizedDateSkeleton.append(ch);
                    dateSkeleton.append(ch);
                    break;
                }
                case 'a': {
                    timeSkeleton.append(ch);
                    break;
                }
                case 'h': {
                    timeSkeleton.append(ch);
                    ++hCount;
                    break;
                }
                case 'H': {
                    timeSkeleton.append(ch);
                    ++HCount;
                    break;
                }
                case 'm': {
                    timeSkeleton.append(ch);
                    ++mCount;
                    break;
                }
                case 'z': {
                    ++zCount;
                    timeSkeleton.append(ch);
                    break;
                }
                case 'v': {
                    ++vCount;
                    timeSkeleton.append(ch);
                    break;
                }
                case 'A':
                case 'K':
                case 'S':
                case 'V':
                case 'Z':
                case 'j':
                case 'k':
                case 's': {
                    timeSkeleton.append(ch);
                    normalizedTimeSkeleton.append(ch);
                    break;
                }
            }
        }
        if (yCount != 0) {
            normalizedDateSkeleton.append('y');
        }
        if (MCount != 0) {
            if (MCount < 3) {
                normalizedDateSkeleton.append('M');
            }
            else {
                for (int i = 0; i < MCount && i < 5; ++i) {
                    normalizedDateSkeleton.append('M');
                }
            }
        }
        if (ECount != 0) {
            if (ECount <= 3) {
                normalizedDateSkeleton.append('E');
            }
            else {
                for (int i = 0; i < ECount && i < 5; ++i) {
                    normalizedDateSkeleton.append('E');
                }
            }
        }
        if (dCount != 0) {
            normalizedDateSkeleton.append('d');
        }
        if (HCount != 0) {
            normalizedTimeSkeleton.append('H');
        }
        else if (hCount != 0) {
            normalizedTimeSkeleton.append('h');
        }
        if (mCount != 0) {
            normalizedTimeSkeleton.append('m');
        }
        if (zCount != 0) {
            normalizedTimeSkeleton.append('z');
        }
        if (vCount != 0) {
            normalizedTimeSkeleton.append('v');
        }
    }
    
    private boolean genSeparateDateTimePtn(final String dateSkeleton, final String timeSkeleton, final Map<String, DateIntervalInfo.PatternInfo> intervalPatterns) {
        String skeleton;
        if (timeSkeleton.length() != 0) {
            skeleton = timeSkeleton;
        }
        else {
            skeleton = dateSkeleton;
        }
        final BestMatchInfo retValue = this.fInfo.getBestSkeleton(skeleton);
        String bestSkeleton = retValue.bestMatchSkeleton;
        final int differenceInfo = retValue.bestMatchDistanceInfo;
        if (differenceInfo == -1) {
            return false;
        }
        if (timeSkeleton.length() == 0) {
            this.genIntervalPattern(5, skeleton, bestSkeleton, differenceInfo, intervalPatterns);
            final SkeletonAndItsBestMatch skeletons = this.genIntervalPattern(2, skeleton, bestSkeleton, differenceInfo, intervalPatterns);
            if (skeletons != null) {
                bestSkeleton = skeletons.skeleton;
                skeleton = skeletons.bestMatchSkeleton;
            }
            this.genIntervalPattern(1, skeleton, bestSkeleton, differenceInfo, intervalPatterns);
        }
        else {
            this.genIntervalPattern(12, skeleton, bestSkeleton, differenceInfo, intervalPatterns);
            this.genIntervalPattern(10, skeleton, bestSkeleton, differenceInfo, intervalPatterns);
            this.genIntervalPattern(9, skeleton, bestSkeleton, differenceInfo, intervalPatterns);
        }
        return true;
    }
    
    private SkeletonAndItsBestMatch genIntervalPattern(final int field, String skeleton, String bestSkeleton, int differenceInfo, final Map<String, DateIntervalInfo.PatternInfo> intervalPatterns) {
        SkeletonAndItsBestMatch retValue = null;
        DateIntervalInfo.PatternInfo pattern = this.fInfo.getIntervalPattern(bestSkeleton, field);
        if (pattern == null) {
            if (SimpleDateFormat.isFieldUnitIgnored(bestSkeleton, field)) {
                final DateIntervalInfo.PatternInfo ptnInfo = new DateIntervalInfo.PatternInfo(this.fDateFormat.toPattern(), null, this.fInfo.getDefaultOrder());
                intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field], ptnInfo);
                return null;
            }
            if (field == 9) {
                pattern = this.fInfo.getIntervalPattern(bestSkeleton, 10);
                if (pattern != null) {
                    intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field], pattern);
                }
                return null;
            }
            final String fieldLetter = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field];
            bestSkeleton = fieldLetter + bestSkeleton;
            skeleton = fieldLetter + skeleton;
            pattern = this.fInfo.getIntervalPattern(bestSkeleton, field);
            if (pattern == null && differenceInfo == 0) {
                final BestMatchInfo tmpRetValue = this.fInfo.getBestSkeleton(skeleton);
                final String tmpBestSkeleton = tmpRetValue.bestMatchSkeleton;
                differenceInfo = tmpRetValue.bestMatchDistanceInfo;
                if (tmpBestSkeleton.length() != 0 && differenceInfo != -1) {
                    pattern = this.fInfo.getIntervalPattern(tmpBestSkeleton, field);
                    bestSkeleton = tmpBestSkeleton;
                }
            }
            if (pattern != null) {
                retValue = new SkeletonAndItsBestMatch(skeleton, bestSkeleton);
            }
        }
        if (pattern != null) {
            if (differenceInfo != 0) {
                final String part1 = adjustFieldWidth(skeleton, bestSkeleton, pattern.getFirstPart(), differenceInfo);
                final String part2 = adjustFieldWidth(skeleton, bestSkeleton, pattern.getSecondPart(), differenceInfo);
                pattern = new DateIntervalInfo.PatternInfo(part1, part2, pattern.firstDateInPtnIsLaterDate());
            }
            intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field], pattern);
        }
        return retValue;
    }
    
    private static String adjustFieldWidth(final String inputSkeleton, final String bestMatchSkeleton, String bestMatchIntervalPattern, final int differenceInfo) {
        if (bestMatchIntervalPattern == null) {
            return null;
        }
        final int[] inputSkeletonFieldWidth = new int[58];
        final int[] bestMatchSkeletonFieldWidth = new int[58];
        DateIntervalInfo.parseSkeleton(inputSkeleton, inputSkeletonFieldWidth);
        DateIntervalInfo.parseSkeleton(bestMatchSkeleton, bestMatchSkeletonFieldWidth);
        if (differenceInfo == 2) {
            bestMatchIntervalPattern = bestMatchIntervalPattern.replace('v', 'z');
        }
        final StringBuilder adjustedPtn = new StringBuilder(bestMatchIntervalPattern);
        boolean inQuote = false;
        char prevCh = '\0';
        int count = 0;
        final int PATTERN_CHAR_BASE = 65;
        for (int adjustedPtnLength = adjustedPtn.length(), i = 0; i < adjustedPtnLength; ++i) {
            final char ch = adjustedPtn.charAt(i);
            if (ch != prevCh && count > 0) {
                char skeletonChar = prevCh;
                if (skeletonChar == 'L') {
                    skeletonChar = 'M';
                }
                final int fieldCount = bestMatchSkeletonFieldWidth[skeletonChar - PATTERN_CHAR_BASE];
                final int inputFieldCount = inputSkeletonFieldWidth[skeletonChar - PATTERN_CHAR_BASE];
                if (fieldCount == count && inputFieldCount > fieldCount) {
                    count = inputFieldCount - fieldCount;
                    for (int j = 0; j < count; ++j) {
                        adjustedPtn.insert(i, prevCh);
                    }
                    i += count;
                    adjustedPtnLength += count;
                }
                count = 0;
            }
            if (ch == '\'') {
                if (i + 1 < adjustedPtn.length() && adjustedPtn.charAt(i + 1) == '\'') {
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
            char skeletonChar2 = prevCh;
            if (skeletonChar2 == 'L') {
                skeletonChar2 = 'M';
            }
            final int fieldCount2 = bestMatchSkeletonFieldWidth[skeletonChar2 - PATTERN_CHAR_BASE];
            final int inputFieldCount2 = inputSkeletonFieldWidth[skeletonChar2 - PATTERN_CHAR_BASE];
            if (fieldCount2 == count && inputFieldCount2 > fieldCount2) {
                count = inputFieldCount2 - fieldCount2;
                for (int k = 0; k < count; ++k) {
                    adjustedPtn.append(prevCh);
                }
            }
        }
        return adjustedPtn.toString();
    }
    
    private void concatSingleDate2TimeInterval(final String dtfmt, final String datePattern, final int field, final Map<String, DateIntervalInfo.PatternInfo> intervalPatterns) {
        DateIntervalInfo.PatternInfo timeItvPtnInfo = intervalPatterns.get(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field]);
        if (timeItvPtnInfo != null) {
            final String timeIntervalPattern = timeItvPtnInfo.getFirstPart() + timeItvPtnInfo.getSecondPart();
            final String pattern = MessageFormat.format(dtfmt, timeIntervalPattern, datePattern);
            timeItvPtnInfo = DateIntervalInfo.genPatternInfo(pattern, timeItvPtnInfo.firstDateInPtnIsLaterDate());
            intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field], timeItvPtnInfo);
        }
    }
    
    private static boolean fieldExistsInSkeleton(final int field, final String skeleton) {
        final String fieldChar = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field];
        return skeleton.indexOf(fieldChar) != -1;
    }
    
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.initializePattern(this.isDateIntervalInfoDefault ? DateIntervalFormat.LOCAL_PATTERN_CACHE : null);
    }
    
    static {
        DateIntervalFormat.LOCAL_PATTERN_CACHE = new SimpleCache<String, Map<String, DateIntervalInfo.PatternInfo>>();
    }
    
    static final class BestMatchInfo
    {
        final String bestMatchSkeleton;
        final int bestMatchDistanceInfo;
        
        BestMatchInfo(final String bestSkeleton, final int difference) {
            this.bestMatchSkeleton = bestSkeleton;
            this.bestMatchDistanceInfo = difference;
        }
    }
    
    private static final class SkeletonAndItsBestMatch
    {
        final String skeleton;
        final String bestMatchSkeleton;
        
        SkeletonAndItsBestMatch(final String skeleton, final String bestMatch) {
            this.skeleton = skeleton;
            this.bestMatchSkeleton = bestMatch;
        }
    }
}
