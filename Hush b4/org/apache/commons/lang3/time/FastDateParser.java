// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.time;

import java.text.DateFormatSymbols;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.text.ParsePosition;
import java.text.ParseException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import java.util.TimeZone;
import java.util.Locale;
import java.io.Serializable;

public class FastDateParser implements DateParser, Serializable
{
    private static final long serialVersionUID = 2L;
    static final Locale JAPANESE_IMPERIAL;
    private final String pattern;
    private final TimeZone timeZone;
    private final Locale locale;
    private final int century;
    private final int startYear;
    private transient Pattern parsePattern;
    private transient Strategy[] strategies;
    private transient String currentFormatField;
    private transient Strategy nextStrategy;
    private static final Pattern formatPattern;
    private static final ConcurrentMap<Locale, Strategy>[] caches;
    private static final Strategy ABBREVIATED_YEAR_STRATEGY;
    private static final Strategy NUMBER_MONTH_STRATEGY;
    private static final Strategy LITERAL_YEAR_STRATEGY;
    private static final Strategy WEEK_OF_YEAR_STRATEGY;
    private static final Strategy WEEK_OF_MONTH_STRATEGY;
    private static final Strategy DAY_OF_YEAR_STRATEGY;
    private static final Strategy DAY_OF_MONTH_STRATEGY;
    private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY;
    private static final Strategy HOUR_OF_DAY_STRATEGY;
    private static final Strategy MODULO_HOUR_OF_DAY_STRATEGY;
    private static final Strategy MODULO_HOUR_STRATEGY;
    private static final Strategy HOUR_STRATEGY;
    private static final Strategy MINUTE_STRATEGY;
    private static final Strategy SECOND_STRATEGY;
    private static final Strategy MILLISECOND_STRATEGY;
    
    protected FastDateParser(final String pattern, final TimeZone timeZone, final Locale locale) {
        this(pattern, timeZone, locale, null);
    }
    
    protected FastDateParser(final String pattern, final TimeZone timeZone, final Locale locale, final Date centuryStart) {
        this.pattern = pattern;
        this.timeZone = timeZone;
        this.locale = locale;
        final Calendar definingCalendar = Calendar.getInstance(timeZone, locale);
        int centuryStartYear;
        if (centuryStart != null) {
            definingCalendar.setTime(centuryStart);
            centuryStartYear = definingCalendar.get(1);
        }
        else if (locale.equals(FastDateParser.JAPANESE_IMPERIAL)) {
            centuryStartYear = 0;
        }
        else {
            definingCalendar.setTime(new Date());
            centuryStartYear = definingCalendar.get(1) - 80;
        }
        this.century = centuryStartYear / 100 * 100;
        this.startYear = centuryStartYear - this.century;
        this.init(definingCalendar);
    }
    
    private void init(final Calendar definingCalendar) {
        final StringBuilder regex = new StringBuilder();
        final List<Strategy> collector = new ArrayList<Strategy>();
        final Matcher patternMatcher = FastDateParser.formatPattern.matcher(this.pattern);
        if (!patternMatcher.lookingAt()) {
            throw new IllegalArgumentException("Illegal pattern character '" + this.pattern.charAt(patternMatcher.regionStart()) + "'");
        }
        this.currentFormatField = patternMatcher.group();
        Strategy currentStrategy = this.getStrategy(this.currentFormatField, definingCalendar);
        while (true) {
            patternMatcher.region(patternMatcher.end(), patternMatcher.regionEnd());
            if (!patternMatcher.lookingAt()) {
                break;
            }
            final String nextFormatField = patternMatcher.group();
            this.nextStrategy = this.getStrategy(nextFormatField, definingCalendar);
            if (currentStrategy.addRegex(this, regex)) {
                collector.add(currentStrategy);
            }
            this.currentFormatField = nextFormatField;
            currentStrategy = this.nextStrategy;
        }
        this.nextStrategy = null;
        if (patternMatcher.regionStart() != patternMatcher.regionEnd()) {
            throw new IllegalArgumentException("Failed to parse \"" + this.pattern + "\" ; gave up at index " + patternMatcher.regionStart());
        }
        if (currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
        this.currentFormatField = null;
        this.strategies = collector.toArray(new Strategy[collector.size()]);
        this.parsePattern = Pattern.compile(regex.toString());
    }
    
    @Override
    public String getPattern() {
        return this.pattern;
    }
    
    @Override
    public TimeZone getTimeZone() {
        return this.timeZone;
    }
    
    @Override
    public Locale getLocale() {
        return this.locale;
    }
    
    Pattern getParsePattern() {
        return this.parsePattern;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof FastDateParser)) {
            return false;
        }
        final FastDateParser other = (FastDateParser)obj;
        return this.pattern.equals(other.pattern) && this.timeZone.equals(other.timeZone) && this.locale.equals(other.locale);
    }
    
    @Override
    public int hashCode() {
        return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
    }
    
    @Override
    public String toString() {
        return "FastDateParser[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final Calendar definingCalendar = Calendar.getInstance(this.timeZone, this.locale);
        this.init(definingCalendar);
    }
    
    @Override
    public Object parseObject(final String source) throws ParseException {
        return this.parse(source);
    }
    
    @Override
    public Date parse(final String source) throws ParseException {
        final Date date = this.parse(source, new ParsePosition(0));
        if (date != null) {
            return date;
        }
        if (this.locale.equals(FastDateParser.JAPANESE_IMPERIAL)) {
            throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\n" + "Unparseable date: \"" + source + "\" does not match " + this.parsePattern.pattern(), 0);
        }
        throw new ParseException("Unparseable date: \"" + source + "\" does not match " + this.parsePattern.pattern(), 0);
    }
    
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        return this.parse(source, pos);
    }
    
    @Override
    public Date parse(final String source, final ParsePosition pos) {
        final int offset = pos.getIndex();
        final Matcher matcher = this.parsePattern.matcher(source.substring(offset));
        if (!matcher.lookingAt()) {
            return null;
        }
        final Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
        cal.clear();
        int i = 0;
        while (i < this.strategies.length) {
            final Strategy strategy = this.strategies[i++];
            strategy.setCalendar(this, cal, matcher.group(i));
        }
        pos.setIndex(offset + matcher.end());
        return cal.getTime();
    }
    
    private static StringBuilder escapeRegex(final StringBuilder regex, final String value, final boolean unquote) {
        regex.append("\\Q");
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            switch (c) {
                case '\'': {
                    if (!unquote) {
                        break;
                    }
                    if (++i == value.length()) {
                        return regex;
                    }
                    c = value.charAt(i);
                    break;
                }
                case '\\': {
                    if (++i == value.length()) {
                        break;
                    }
                    regex.append(c);
                    c = value.charAt(i);
                    if (c == 'E') {
                        regex.append("E\\\\E\\");
                        c = 'Q';
                        break;
                    }
                    break;
                }
            }
            regex.append(c);
        }
        regex.append("\\E");
        return regex;
    }
    
    private static Map<String, Integer> getDisplayNames(final int field, final Calendar definingCalendar, final Locale locale) {
        return definingCalendar.getDisplayNames(field, 0, locale);
    }
    
    private int adjustYear(final int twoDigitYear) {
        final int trial = this.century + twoDigitYear;
        return (twoDigitYear >= this.startYear) ? trial : (trial + 100);
    }
    
    boolean isNextNumber() {
        return this.nextStrategy != null && this.nextStrategy.isNumber();
    }
    
    int getFieldWidth() {
        return this.currentFormatField.length();
    }
    
    private Strategy getStrategy(final String formatField, final Calendar definingCalendar) {
        switch (formatField.charAt(0)) {
            case '\'': {
                if (formatField.length() > 2) {
                    return new CopyQuotedStrategy(formatField.substring(1, formatField.length() - 1));
                }
                break;
            }
            case 'D': {
                return FastDateParser.DAY_OF_YEAR_STRATEGY;
            }
            case 'E': {
                return this.getLocaleSpecificStrategy(7, definingCalendar);
            }
            case 'F': {
                return FastDateParser.DAY_OF_WEEK_IN_MONTH_STRATEGY;
            }
            case 'G': {
                return this.getLocaleSpecificStrategy(0, definingCalendar);
            }
            case 'H': {
                return FastDateParser.MODULO_HOUR_OF_DAY_STRATEGY;
            }
            case 'K': {
                return FastDateParser.HOUR_STRATEGY;
            }
            case 'M': {
                return (formatField.length() >= 3) ? this.getLocaleSpecificStrategy(2, definingCalendar) : FastDateParser.NUMBER_MONTH_STRATEGY;
            }
            case 'S': {
                return FastDateParser.MILLISECOND_STRATEGY;
            }
            case 'W': {
                return FastDateParser.WEEK_OF_MONTH_STRATEGY;
            }
            case 'a': {
                return this.getLocaleSpecificStrategy(9, definingCalendar);
            }
            case 'd': {
                return FastDateParser.DAY_OF_MONTH_STRATEGY;
            }
            case 'h': {
                return FastDateParser.MODULO_HOUR_STRATEGY;
            }
            case 'k': {
                return FastDateParser.HOUR_OF_DAY_STRATEGY;
            }
            case 'm': {
                return FastDateParser.MINUTE_STRATEGY;
            }
            case 's': {
                return FastDateParser.SECOND_STRATEGY;
            }
            case 'w': {
                return FastDateParser.WEEK_OF_YEAR_STRATEGY;
            }
            case 'y': {
                return (formatField.length() > 2) ? FastDateParser.LITERAL_YEAR_STRATEGY : FastDateParser.ABBREVIATED_YEAR_STRATEGY;
            }
            case 'Z':
            case 'z': {
                return this.getLocaleSpecificStrategy(15, definingCalendar);
            }
        }
        return new CopyQuotedStrategy(formatField);
    }
    
    private static ConcurrentMap<Locale, Strategy> getCache(final int field) {
        synchronized (FastDateParser.caches) {
            if (FastDateParser.caches[field] == null) {
                FastDateParser.caches[field] = new ConcurrentHashMap<Locale, Strategy>(3);
            }
            return FastDateParser.caches[field];
        }
    }
    
    private Strategy getLocaleSpecificStrategy(final int field, final Calendar definingCalendar) {
        final ConcurrentMap<Locale, Strategy> cache = getCache(field);
        Strategy strategy = cache.get(this.locale);
        if (strategy == null) {
            strategy = ((field == 15) ? new TimeZoneStrategy(this.locale) : new TextStrategy(field, definingCalendar, this.locale));
            final Strategy inCache = cache.putIfAbsent(this.locale, strategy);
            if (inCache != null) {
                return inCache;
            }
        }
        return strategy;
    }
    
    static {
        JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
        formatPattern = Pattern.compile("D+|E+|F+|G+|H+|K+|M+|S+|W+|Z+|a+|d+|h+|k+|m+|s+|w+|y+|z+|''|'[^']++(''[^']*+)*+'|[^'A-Za-z]++");
        caches = new ConcurrentMap[17];
        ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1) {
            @Override
            void setCalendar(final FastDateParser parser, final Calendar cal, final String value) {
                int iValue = Integer.parseInt(value);
                if (iValue < 100) {
                    iValue = parser.adjustYear(iValue);
                }
                cal.set(1, iValue);
            }
        };
        NUMBER_MONTH_STRATEGY = new NumberStrategy(2) {
            @Override
            int modify(final int iValue) {
                return iValue - 1;
            }
        };
        LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
        WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
        WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
        DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
        DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
        DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
        HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
        MODULO_HOUR_OF_DAY_STRATEGY = new NumberStrategy(11) {
            @Override
            int modify(final int iValue) {
                return iValue % 24;
            }
        };
        MODULO_HOUR_STRATEGY = new NumberStrategy(10) {
            @Override
            int modify(final int iValue) {
                return iValue % 12;
            }
        };
        HOUR_STRATEGY = new NumberStrategy(10);
        MINUTE_STRATEGY = new NumberStrategy(12);
        SECOND_STRATEGY = new NumberStrategy(13);
        MILLISECOND_STRATEGY = new NumberStrategy(14);
    }
    
    private abstract static class Strategy
    {
        boolean isNumber() {
            return false;
        }
        
        void setCalendar(final FastDateParser parser, final Calendar cal, final String value) {
        }
        
        abstract boolean addRegex(final FastDateParser p0, final StringBuilder p1);
    }
    
    private static class CopyQuotedStrategy extends Strategy
    {
        private final String formatField;
        
        CopyQuotedStrategy(final String formatField) {
            this.formatField = formatField;
        }
        
        @Override
        boolean isNumber() {
            char c = this.formatField.charAt(0);
            if (c == '\'') {
                c = this.formatField.charAt(1);
            }
            return Character.isDigit(c);
        }
        
        @Override
        boolean addRegex(final FastDateParser parser, final StringBuilder regex) {
            escapeRegex(regex, this.formatField, true);
            return false;
        }
    }
    
    private static class TextStrategy extends Strategy
    {
        private final int field;
        private final Map<String, Integer> keyValues;
        
        TextStrategy(final int field, final Calendar definingCalendar, final Locale locale) {
            this.field = field;
            this.keyValues = getDisplayNames(field, definingCalendar, locale);
        }
        
        @Override
        boolean addRegex(final FastDateParser parser, final StringBuilder regex) {
            regex.append('(');
            for (final String textKeyValue : this.keyValues.keySet()) {
                escapeRegex(regex, textKeyValue, false).append('|');
            }
            regex.setCharAt(regex.length() - 1, ')');
            return true;
        }
        
        @Override
        void setCalendar(final FastDateParser parser, final Calendar cal, final String value) {
            final Integer iVal = this.keyValues.get(value);
            if (iVal == null) {
                final StringBuilder sb = new StringBuilder(value);
                sb.append(" not in (");
                for (final String textKeyValue : this.keyValues.keySet()) {
                    sb.append(textKeyValue).append(' ');
                }
                sb.setCharAt(sb.length() - 1, ')');
                throw new IllegalArgumentException(sb.toString());
            }
            cal.set(this.field, iVal);
        }
    }
    
    private static class NumberStrategy extends Strategy
    {
        private final int field;
        
        NumberStrategy(final int field) {
            this.field = field;
        }
        
        @Override
        boolean isNumber() {
            return true;
        }
        
        @Override
        boolean addRegex(final FastDateParser parser, final StringBuilder regex) {
            if (parser.isNextNumber()) {
                regex.append("(\\p{Nd}{").append(parser.getFieldWidth()).append("}+)");
            }
            else {
                regex.append("(\\p{Nd}++)");
            }
            return true;
        }
        
        @Override
        void setCalendar(final FastDateParser parser, final Calendar cal, final String value) {
            cal.set(this.field, this.modify(Integer.parseInt(value)));
        }
        
        int modify(final int iValue) {
            return iValue;
        }
    }
    
    private static class TimeZoneStrategy extends Strategy
    {
        private final String validTimeZoneChars;
        private final SortedMap<String, TimeZone> tzNames;
        private static final int ID = 0;
        private static final int LONG_STD = 1;
        private static final int SHORT_STD = 2;
        private static final int LONG_DST = 3;
        private static final int SHORT_DST = 4;
        
        TimeZoneStrategy(final Locale locale) {
            this.tzNames = new TreeMap<String, TimeZone>(String.CASE_INSENSITIVE_ORDER);
            final String[][] arr$;
            final String[][] zones = arr$ = DateFormatSymbols.getInstance(locale).getZoneStrings();
            for (final String[] zone : arr$) {
                if (!zone[0].startsWith("GMT")) {
                    final TimeZone tz = TimeZone.getTimeZone(zone[0]);
                    if (!this.tzNames.containsKey(zone[1])) {
                        this.tzNames.put(zone[1], tz);
                    }
                    if (!this.tzNames.containsKey(zone[2])) {
                        this.tzNames.put(zone[2], tz);
                    }
                    if (tz.useDaylightTime()) {
                        if (!this.tzNames.containsKey(zone[3])) {
                            this.tzNames.put(zone[3], tz);
                        }
                        if (!this.tzNames.containsKey(zone[4])) {
                            this.tzNames.put(zone[4], tz);
                        }
                    }
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("(GMT[+\\-]\\d{0,1}\\d{2}|[+\\-]\\d{2}:?\\d{2}|");
            for (final String id : this.tzNames.keySet()) {
                escapeRegex(sb, id, false).append('|');
            }
            sb.setCharAt(sb.length() - 1, ')');
            this.validTimeZoneChars = sb.toString();
        }
        
        @Override
        boolean addRegex(final FastDateParser parser, final StringBuilder regex) {
            regex.append(this.validTimeZoneChars);
            return true;
        }
        
        @Override
        void setCalendar(final FastDateParser parser, final Calendar cal, final String value) {
            TimeZone tz;
            if (value.charAt(0) == '+' || value.charAt(0) == '-') {
                tz = TimeZone.getTimeZone("GMT" + value);
            }
            else if (value.startsWith("GMT")) {
                tz = TimeZone.getTimeZone(value);
            }
            else {
                tz = this.tzNames.get(value);
                if (tz == null) {
                    throw new IllegalArgumentException(value + " is not a supported timezone name");
                }
            }
            cal.setTimeZone(tz);
        }
    }
}
