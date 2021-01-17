// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.time;

import org.apache.commons.lang3.Validate;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.text.DateFormatSymbols;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.Locale;
import java.util.TimeZone;
import java.io.Serializable;

public class FastDatePrinter implements DatePrinter, Serializable
{
    private static final long serialVersionUID = 1L;
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    private final String mPattern;
    private final TimeZone mTimeZone;
    private final Locale mLocale;
    private transient Rule[] mRules;
    private transient int mMaxLengthEstimate;
    private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache;
    
    protected FastDatePrinter(final String pattern, final TimeZone timeZone, final Locale locale) {
        this.mPattern = pattern;
        this.mTimeZone = timeZone;
        this.mLocale = locale;
        this.init();
    }
    
    private void init() {
        final List<Rule> rulesList = this.parsePattern();
        this.mRules = rulesList.toArray(new Rule[rulesList.size()]);
        int len = 0;
        int i = this.mRules.length;
        while (--i >= 0) {
            len += this.mRules[i].estimateLength();
        }
        this.mMaxLengthEstimate = len;
    }
    
    protected List<Rule> parsePattern() {
        final DateFormatSymbols symbols = new DateFormatSymbols(this.mLocale);
        final List<Rule> rules = new ArrayList<Rule>();
        final String[] ERAs = symbols.getEras();
        final String[] months = symbols.getMonths();
        final String[] shortMonths = symbols.getShortMonths();
        final String[] weekdays = symbols.getWeekdays();
        final String[] shortWeekdays = symbols.getShortWeekdays();
        final String[] AmPmStrings = symbols.getAmPmStrings();
        final int length = this.mPattern.length();
        final int[] indexRef = { 0 };
        for (int i = 0; i < length; ++i) {
            indexRef[0] = i;
            final String token = this.parseToken(this.mPattern, indexRef);
            i = indexRef[0];
            final int tokenLen = token.length();
            if (tokenLen == 0) {
                break;
            }
            final char c = token.charAt(0);
            Rule rule = null;
            switch (c) {
                case 'G': {
                    rule = new TextField(0, ERAs);
                    break;
                }
                case 'y': {
                    if (tokenLen == 2) {
                        rule = TwoDigitYearField.INSTANCE;
                        break;
                    }
                    rule = this.selectNumberRule(1, (tokenLen < 4) ? 4 : tokenLen);
                    break;
                }
                case 'M': {
                    if (tokenLen >= 4) {
                        rule = new TextField(2, months);
                        break;
                    }
                    if (tokenLen == 3) {
                        rule = new TextField(2, shortMonths);
                        break;
                    }
                    if (tokenLen == 2) {
                        rule = TwoDigitMonthField.INSTANCE;
                        break;
                    }
                    rule = UnpaddedMonthField.INSTANCE;
                    break;
                }
                case 'd': {
                    rule = this.selectNumberRule(5, tokenLen);
                    break;
                }
                case 'h': {
                    rule = new TwelveHourField(this.selectNumberRule(10, tokenLen));
                    break;
                }
                case 'H': {
                    rule = this.selectNumberRule(11, tokenLen);
                    break;
                }
                case 'm': {
                    rule = this.selectNumberRule(12, tokenLen);
                    break;
                }
                case 's': {
                    rule = this.selectNumberRule(13, tokenLen);
                    break;
                }
                case 'S': {
                    rule = this.selectNumberRule(14, tokenLen);
                    break;
                }
                case 'E': {
                    rule = new TextField(7, (tokenLen < 4) ? shortWeekdays : weekdays);
                    break;
                }
                case 'D': {
                    rule = this.selectNumberRule(6, tokenLen);
                    break;
                }
                case 'F': {
                    rule = this.selectNumberRule(8, tokenLen);
                    break;
                }
                case 'w': {
                    rule = this.selectNumberRule(3, tokenLen);
                    break;
                }
                case 'W': {
                    rule = this.selectNumberRule(4, tokenLen);
                    break;
                }
                case 'a': {
                    rule = new TextField(9, AmPmStrings);
                    break;
                }
                case 'k': {
                    rule = new TwentyFourHourField(this.selectNumberRule(11, tokenLen));
                    break;
                }
                case 'K': {
                    rule = this.selectNumberRule(10, tokenLen);
                    break;
                }
                case 'z': {
                    if (tokenLen >= 4) {
                        rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 1);
                        break;
                    }
                    rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
                    break;
                }
                case 'Z': {
                    if (tokenLen == 1) {
                        rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
                        break;
                    }
                    rule = TimeZoneNumberRule.INSTANCE_COLON;
                    break;
                }
                case '\'': {
                    final String sub = token.substring(1);
                    if (sub.length() == 1) {
                        rule = new CharacterLiteral(sub.charAt(0));
                        break;
                    }
                    rule = new StringLiteral(sub);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Illegal pattern component: " + token);
                }
            }
            rules.add(rule);
        }
        return rules;
    }
    
    protected String parseToken(final String pattern, final int[] indexRef) {
        final StringBuilder buf = new StringBuilder();
        int i = indexRef[0];
        final int length = pattern.length();
        char c = pattern.charAt(i);
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
            buf.append(c);
            while (i + 1 < length) {
                final char peek = pattern.charAt(i + 1);
                if (peek != c) {
                    break;
                }
                buf.append(c);
                ++i;
            }
        }
        else {
            buf.append('\'');
            boolean inLiteral = false;
            while (i < length) {
                c = pattern.charAt(i);
                if (c == '\'') {
                    if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
                        ++i;
                        buf.append(c);
                    }
                    else {
                        inLiteral = !inLiteral;
                    }
                }
                else {
                    if (!inLiteral && ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
                        --i;
                        break;
                    }
                    buf.append(c);
                }
                ++i;
            }
        }
        indexRef[0] = i;
        return buf.toString();
    }
    
    protected NumberRule selectNumberRule(final int field, final int padding) {
        switch (padding) {
            case 1: {
                return new UnpaddedNumberField(field);
            }
            case 2: {
                return new TwoDigitNumberField(field);
            }
            default: {
                return new PaddedNumberField(field, padding);
            }
        }
    }
    
    @Override
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
        if (obj instanceof Date) {
            return this.format((Date)obj, toAppendTo);
        }
        if (obj instanceof Calendar) {
            return this.format((Calendar)obj, toAppendTo);
        }
        if (obj instanceof Long) {
            return this.format((long)obj, toAppendTo);
        }
        throw new IllegalArgumentException("Unknown class: " + ((obj == null) ? "<null>" : obj.getClass().getName()));
    }
    
    @Override
    public String format(final long millis) {
        final Calendar c = this.newCalendar();
        c.setTimeInMillis(millis);
        return this.applyRulesToString(c);
    }
    
    private String applyRulesToString(final Calendar c) {
        return this.applyRules(c, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }
    
    private GregorianCalendar newCalendar() {
        return new GregorianCalendar(this.mTimeZone, this.mLocale);
    }
    
    @Override
    public String format(final Date date) {
        final Calendar c = this.newCalendar();
        c.setTime(date);
        return this.applyRulesToString(c);
    }
    
    @Override
    public String format(final Calendar calendar) {
        return this.format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }
    
    @Override
    public StringBuffer format(final long millis, final StringBuffer buf) {
        return this.format(new Date(millis), buf);
    }
    
    @Override
    public StringBuffer format(final Date date, final StringBuffer buf) {
        final Calendar c = this.newCalendar();
        c.setTime(date);
        return this.applyRules(c, buf);
    }
    
    @Override
    public StringBuffer format(final Calendar calendar, final StringBuffer buf) {
        return this.applyRules(calendar, buf);
    }
    
    protected StringBuffer applyRules(final Calendar calendar, final StringBuffer buf) {
        for (final Rule rule : this.mRules) {
            rule.appendTo(buf, calendar);
        }
        return buf;
    }
    
    @Override
    public String getPattern() {
        return this.mPattern;
    }
    
    @Override
    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }
    
    @Override
    public Locale getLocale() {
        return this.mLocale;
    }
    
    public int getMaxLengthEstimate() {
        return this.mMaxLengthEstimate;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof FastDatePrinter)) {
            return false;
        }
        final FastDatePrinter other = (FastDatePrinter)obj;
        return this.mPattern.equals(other.mPattern) && this.mTimeZone.equals(other.mTimeZone) && this.mLocale.equals(other.mLocale);
    }
    
    @Override
    public int hashCode() {
        return this.mPattern.hashCode() + 13 * (this.mTimeZone.hashCode() + 13 * this.mLocale.hashCode());
    }
    
    @Override
    public String toString() {
        return "FastDatePrinter[" + this.mPattern + "," + this.mLocale + "," + this.mTimeZone.getID() + "]";
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.init();
    }
    
    static String getTimeZoneDisplay(final TimeZone tz, final boolean daylight, final int style, final Locale locale) {
        final TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
        String value = FastDatePrinter.cTimeZoneDisplayCache.get(key);
        if (value == null) {
            value = tz.getDisplayName(daylight, style, locale);
            final String prior = FastDatePrinter.cTimeZoneDisplayCache.putIfAbsent(key, value);
            if (prior != null) {
                value = prior;
            }
        }
        return value;
    }
    
    static {
        cTimeZoneDisplayCache = new ConcurrentHashMap<TimeZoneDisplayKey, String>(7);
    }
    
    private static class CharacterLiteral implements Rule
    {
        private final char mValue;
        
        CharacterLiteral(final char value) {
            this.mValue = value;
        }
        
        @Override
        public int estimateLength() {
            return 1;
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final Calendar calendar) {
            buffer.append(this.mValue);
        }
    }
    
    private static class StringLiteral implements Rule
    {
        private final String mValue;
        
        StringLiteral(final String value) {
            this.mValue = value;
        }
        
        @Override
        public int estimateLength() {
            return this.mValue.length();
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final Calendar calendar) {
            buffer.append(this.mValue);
        }
    }
    
    private static class TextField implements Rule
    {
        private final int mField;
        private final String[] mValues;
        
        TextField(final int field, final String[] values) {
            this.mField = field;
            this.mValues = values;
        }
        
        @Override
        public int estimateLength() {
            int max = 0;
            int i = this.mValues.length;
            while (--i >= 0) {
                final int len = this.mValues[i].length();
                if (len > max) {
                    max = len;
                }
            }
            return max;
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final Calendar calendar) {
            buffer.append(this.mValues[calendar.get(this.mField)]);
        }
    }
    
    private static class UnpaddedNumberField implements NumberRule
    {
        private final int mField;
        
        UnpaddedNumberField(final int field) {
            this.mField = field;
        }
        
        @Override
        public int estimateLength() {
            return 4;
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final Calendar calendar) {
            this.appendTo(buffer, calendar.get(this.mField));
        }
        
        @Override
        public final void appendTo(final StringBuffer buffer, final int value) {
            if (value < 10) {
                buffer.append((char)(value + 48));
            }
            else if (value < 100) {
                buffer.append((char)(value / 10 + 48));
                buffer.append((char)(value % 10 + 48));
            }
            else {
                buffer.append(Integer.toString(value));
            }
        }
    }
    
    private static class UnpaddedMonthField implements NumberRule
    {
        static final UnpaddedMonthField INSTANCE;
        
        UnpaddedMonthField() {
        }
        
        @Override
        public int estimateLength() {
            return 2;
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final Calendar calendar) {
            this.appendTo(buffer, calendar.get(2) + 1);
        }
        
        @Override
        public final void appendTo(final StringBuffer buffer, final int value) {
            if (value < 10) {
                buffer.append((char)(value + 48));
            }
            else {
                buffer.append((char)(value / 10 + 48));
                buffer.append((char)(value % 10 + 48));
            }
        }
        
        static {
            INSTANCE = new UnpaddedMonthField();
        }
    }
    
    private static class PaddedNumberField implements NumberRule
    {
        private final int mField;
        private final int mSize;
        
        PaddedNumberField(final int field, final int size) {
            if (size < 3) {
                throw new IllegalArgumentException();
            }
            this.mField = field;
            this.mSize = size;
        }
        
        @Override
        public int estimateLength() {
            return 4;
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final Calendar calendar) {
            this.appendTo(buffer, calendar.get(this.mField));
        }
        
        @Override
        public final void appendTo(final StringBuffer buffer, final int value) {
            if (value < 100) {
                int i = this.mSize;
                while (--i >= 2) {
                    buffer.append('0');
                }
                buffer.append((char)(value / 10 + 48));
                buffer.append((char)(value % 10 + 48));
            }
            else {
                int digits;
                if (value < 1000) {
                    digits = 3;
                }
                else {
                    Validate.isTrue(value > -1, "Negative values should not be possible", value);
                    digits = Integer.toString(value).length();
                }
                int j = this.mSize;
                while (--j >= digits) {
                    buffer.append('0');
                }
                buffer.append(Integer.toString(value));
            }
        }
    }
    
    private static class TwoDigitNumberField implements NumberRule
    {
        private final int mField;
        
        TwoDigitNumberField(final int field) {
            this.mField = field;
        }
        
        @Override
        public int estimateLength() {
            return 2;
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final Calendar calendar) {
            this.appendTo(buffer, calendar.get(this.mField));
        }
        
        @Override
        public final void appendTo(final StringBuffer buffer, final int value) {
            if (value < 100) {
                buffer.append((char)(value / 10 + 48));
                buffer.append((char)(value % 10 + 48));
            }
            else {
                buffer.append(Integer.toString(value));
            }
        }
    }
    
    private static class TwoDigitYearField implements NumberRule
    {
        static final TwoDigitYearField INSTANCE;
        
        TwoDigitYearField() {
        }
        
        @Override
        public int estimateLength() {
            return 2;
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final Calendar calendar) {
            this.appendTo(buffer, calendar.get(1) % 100);
        }
        
        @Override
        public final void appendTo(final StringBuffer buffer, final int value) {
            buffer.append((char)(value / 10 + 48));
            buffer.append((char)(value % 10 + 48));
        }
        
        static {
            INSTANCE = new TwoDigitYearField();
        }
    }
    
    private static class TwoDigitMonthField implements NumberRule
    {
        static final TwoDigitMonthField INSTANCE;
        
        TwoDigitMonthField() {
        }
        
        @Override
        public int estimateLength() {
            return 2;
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final Calendar calendar) {
            this.appendTo(buffer, calendar.get(2) + 1);
        }
        
        @Override
        public final void appendTo(final StringBuffer buffer, final int value) {
            buffer.append((char)(value / 10 + 48));
            buffer.append((char)(value % 10 + 48));
        }
        
        static {
            INSTANCE = new TwoDigitMonthField();
        }
    }
    
    private static class TwelveHourField implements NumberRule
    {
        private final NumberRule mRule;
        
        TwelveHourField(final NumberRule rule) {
            this.mRule = rule;
        }
        
        @Override
        public int estimateLength() {
            return this.mRule.estimateLength();
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final Calendar calendar) {
            int value = calendar.get(10);
            if (value == 0) {
                value = calendar.getLeastMaximum(10) + 1;
            }
            this.mRule.appendTo(buffer, value);
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final int value) {
            this.mRule.appendTo(buffer, value);
        }
    }
    
    private static class TwentyFourHourField implements NumberRule
    {
        private final NumberRule mRule;
        
        TwentyFourHourField(final NumberRule rule) {
            this.mRule = rule;
        }
        
        @Override
        public int estimateLength() {
            return this.mRule.estimateLength();
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final Calendar calendar) {
            int value = calendar.get(11);
            if (value == 0) {
                value = calendar.getMaximum(11) + 1;
            }
            this.mRule.appendTo(buffer, value);
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final int value) {
            this.mRule.appendTo(buffer, value);
        }
    }
    
    private static class TimeZoneNameRule implements Rule
    {
        private final Locale mLocale;
        private final int mStyle;
        private final String mStandard;
        private final String mDaylight;
        
        TimeZoneNameRule(final TimeZone timeZone, final Locale locale, final int style) {
            this.mLocale = locale;
            this.mStyle = style;
            this.mStandard = FastDatePrinter.getTimeZoneDisplay(timeZone, false, style, locale);
            this.mDaylight = FastDatePrinter.getTimeZoneDisplay(timeZone, true, style, locale);
        }
        
        @Override
        public int estimateLength() {
            return Math.max(this.mStandard.length(), this.mDaylight.length());
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final Calendar calendar) {
            final TimeZone zone = calendar.getTimeZone();
            if (zone.useDaylightTime() && calendar.get(16) != 0) {
                buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, true, this.mStyle, this.mLocale));
            }
            else {
                buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, false, this.mStyle, this.mLocale));
            }
        }
    }
    
    private static class TimeZoneNumberRule implements Rule
    {
        static final TimeZoneNumberRule INSTANCE_COLON;
        static final TimeZoneNumberRule INSTANCE_NO_COLON;
        final boolean mColon;
        
        TimeZoneNumberRule(final boolean colon) {
            this.mColon = colon;
        }
        
        @Override
        public int estimateLength() {
            return 5;
        }
        
        @Override
        public void appendTo(final StringBuffer buffer, final Calendar calendar) {
            int offset = calendar.get(15) + calendar.get(16);
            if (offset < 0) {
                buffer.append('-');
                offset = -offset;
            }
            else {
                buffer.append('+');
            }
            final int hours = offset / 3600000;
            buffer.append((char)(hours / 10 + 48));
            buffer.append((char)(hours % 10 + 48));
            if (this.mColon) {
                buffer.append(':');
            }
            final int minutes = offset / 60000 - 60 * hours;
            buffer.append((char)(minutes / 10 + 48));
            buffer.append((char)(minutes % 10 + 48));
        }
        
        static {
            INSTANCE_COLON = new TimeZoneNumberRule(true);
            INSTANCE_NO_COLON = new TimeZoneNumberRule(false);
        }
    }
    
    private static class TimeZoneDisplayKey
    {
        private final TimeZone mTimeZone;
        private final int mStyle;
        private final Locale mLocale;
        
        TimeZoneDisplayKey(final TimeZone timeZone, final boolean daylight, final int style, final Locale locale) {
            this.mTimeZone = timeZone;
            if (daylight) {
                this.mStyle = (style | Integer.MIN_VALUE);
            }
            else {
                this.mStyle = style;
            }
            this.mLocale = locale;
        }
        
        @Override
        public int hashCode() {
            return (this.mStyle * 31 + this.mLocale.hashCode()) * 31 + this.mTimeZone.hashCode();
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof TimeZoneDisplayKey) {
                final TimeZoneDisplayKey other = (TimeZoneDisplayKey)obj;
                return this.mTimeZone.equals(other.mTimeZone) && this.mStyle == other.mStyle && this.mLocale.equals(other.mLocale);
            }
            return false;
        }
    }
    
    private interface Rule
    {
        int estimateLength();
        
        void appendTo(final StringBuffer p0, final Calendar p1);
    }
    
    private interface NumberRule extends Rule
    {
        void appendTo(final StringBuffer p0, final int p1);
    }
}
