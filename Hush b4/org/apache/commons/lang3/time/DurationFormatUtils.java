// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.time;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;

public class DurationFormatUtils
{
    public static final String ISO_EXTENDED_FORMAT_PATTERN = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
    static final Object y;
    static final Object M;
    static final Object d;
    static final Object H;
    static final Object m;
    static final Object s;
    static final Object S;
    
    public static String formatDurationHMS(final long durationMillis) {
        return formatDuration(durationMillis, "H:mm:ss.SSS");
    }
    
    public static String formatDurationISO(final long durationMillis) {
        return formatDuration(durationMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", false);
    }
    
    public static String formatDuration(final long durationMillis, final String format) {
        return formatDuration(durationMillis, format, true);
    }
    
    public static String formatDuration(final long durationMillis, final String format, final boolean padWithZeros) {
        final Token[] tokens = lexx(format);
        long days = 0L;
        long hours = 0L;
        long minutes = 0L;
        long seconds = 0L;
        long milliseconds = durationMillis;
        if (Token.containsTokenWithValue(tokens, DurationFormatUtils.d)) {
            days = milliseconds / 86400000L;
            milliseconds -= days * 86400000L;
        }
        if (Token.containsTokenWithValue(tokens, DurationFormatUtils.H)) {
            hours = milliseconds / 3600000L;
            milliseconds -= hours * 3600000L;
        }
        if (Token.containsTokenWithValue(tokens, DurationFormatUtils.m)) {
            minutes = milliseconds / 60000L;
            milliseconds -= minutes * 60000L;
        }
        if (Token.containsTokenWithValue(tokens, DurationFormatUtils.s)) {
            seconds = milliseconds / 1000L;
            milliseconds -= seconds * 1000L;
        }
        return format(tokens, 0L, 0L, days, hours, minutes, seconds, milliseconds, padWithZeros);
    }
    
    public static String formatDurationWords(final long durationMillis, final boolean suppressLeadingZeroElements, final boolean suppressTrailingZeroElements) {
        String duration = formatDuration(durationMillis, "d' days 'H' hours 'm' minutes 's' seconds'");
        if (suppressLeadingZeroElements) {
            duration = " " + duration;
            String tmp = StringUtils.replaceOnce(duration, " 0 days", "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, " 0 hours", "");
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = (duration = StringUtils.replaceOnce(duration, " 0 minutes", ""));
                    if (tmp.length() != duration.length()) {
                        duration = StringUtils.replaceOnce(tmp, " 0 seconds", "");
                    }
                }
            }
            if (duration.length() != 0) {
                duration = duration.substring(1);
            }
        }
        if (suppressTrailingZeroElements) {
            String tmp = StringUtils.replaceOnce(duration, " 0 seconds", "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, " 0 minutes", "");
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce(duration, " 0 hours", "");
                    if (tmp.length() != duration.length()) {
                        duration = StringUtils.replaceOnce(tmp, " 0 days", "");
                    }
                }
            }
        }
        duration = " " + duration;
        duration = StringUtils.replaceOnce(duration, " 1 seconds", " 1 second");
        duration = StringUtils.replaceOnce(duration, " 1 minutes", " 1 minute");
        duration = StringUtils.replaceOnce(duration, " 1 hours", " 1 hour");
        duration = StringUtils.replaceOnce(duration, " 1 days", " 1 day");
        return duration.trim();
    }
    
    public static String formatPeriodISO(final long startMillis, final long endMillis) {
        return formatPeriod(startMillis, endMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", false, TimeZone.getDefault());
    }
    
    public static String formatPeriod(final long startMillis, final long endMillis, final String format) {
        return formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
    }
    
    public static String formatPeriod(final long startMillis, final long endMillis, final String format, final boolean padWithZeros, final TimeZone timezone) {
        final Token[] tokens = lexx(format);
        final Calendar start = Calendar.getInstance(timezone);
        start.setTime(new Date(startMillis));
        final Calendar end = Calendar.getInstance(timezone);
        end.setTime(new Date(endMillis));
        int milliseconds = end.get(14) - start.get(14);
        int seconds = end.get(13) - start.get(13);
        int minutes = end.get(12) - start.get(12);
        int hours = end.get(11) - start.get(11);
        int days = end.get(5) - start.get(5);
        int months = end.get(2) - start.get(2);
        int years = end.get(1) - start.get(1);
        while (milliseconds < 0) {
            milliseconds += 1000;
            --seconds;
        }
        while (seconds < 0) {
            seconds += 60;
            --minutes;
        }
        while (minutes < 0) {
            minutes += 60;
            --hours;
        }
        while (hours < 0) {
            hours += 24;
            --days;
        }
        if (Token.containsTokenWithValue(tokens, DurationFormatUtils.M)) {
            while (days < 0) {
                days += start.getActualMaximum(5);
                --months;
                start.add(2, 1);
            }
            while (months < 0) {
                months += 12;
                --years;
            }
            if (!Token.containsTokenWithValue(tokens, DurationFormatUtils.y) && years != 0) {
                while (years != 0) {
                    months += 12 * years;
                    years = 0;
                }
            }
        }
        else {
            if (!Token.containsTokenWithValue(tokens, DurationFormatUtils.y)) {
                int target = end.get(1);
                if (months < 0) {
                    --target;
                }
                while (start.get(1) != target) {
                    days += start.getActualMaximum(6) - start.get(6);
                    if (start instanceof GregorianCalendar && start.get(2) == 1 && start.get(5) == 29) {
                        ++days;
                    }
                    start.add(1, 1);
                    days += start.get(6);
                }
                years = 0;
            }
            while (start.get(2) != end.get(2)) {
                days += start.getActualMaximum(5);
                start.add(2, 1);
            }
            months = 0;
            while (days < 0) {
                days += start.getActualMaximum(5);
                --months;
                start.add(2, 1);
            }
        }
        if (!Token.containsTokenWithValue(tokens, DurationFormatUtils.d)) {
            hours += 24 * days;
            days = 0;
        }
        if (!Token.containsTokenWithValue(tokens, DurationFormatUtils.H)) {
            minutes += 60 * hours;
            hours = 0;
        }
        if (!Token.containsTokenWithValue(tokens, DurationFormatUtils.m)) {
            seconds += 60 * minutes;
            minutes = 0;
        }
        if (!Token.containsTokenWithValue(tokens, DurationFormatUtils.s)) {
            milliseconds += 1000 * seconds;
            seconds = 0;
        }
        return format(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
    }
    
    static String format(final Token[] tokens, final long years, final long months, final long days, final long hours, final long minutes, final long seconds, final long milliseconds, final boolean padWithZeros) {
        final StringBuilder buffer = new StringBuilder();
        boolean lastOutputSeconds = false;
        for (final Token token : tokens) {
            final Object value = token.getValue();
            final int count = token.getCount();
            if (value instanceof StringBuilder) {
                buffer.append(value.toString());
            }
            else if (value == DurationFormatUtils.y) {
                buffer.append(paddedValue(years, padWithZeros, count));
                lastOutputSeconds = false;
            }
            else if (value == DurationFormatUtils.M) {
                buffer.append(paddedValue(months, padWithZeros, count));
                lastOutputSeconds = false;
            }
            else if (value == DurationFormatUtils.d) {
                buffer.append(paddedValue(days, padWithZeros, count));
                lastOutputSeconds = false;
            }
            else if (value == DurationFormatUtils.H) {
                buffer.append(paddedValue(hours, padWithZeros, count));
                lastOutputSeconds = false;
            }
            else if (value == DurationFormatUtils.m) {
                buffer.append(paddedValue(minutes, padWithZeros, count));
                lastOutputSeconds = false;
            }
            else if (value == DurationFormatUtils.s) {
                buffer.append(paddedValue(seconds, padWithZeros, count));
                lastOutputSeconds = true;
            }
            else if (value == DurationFormatUtils.S) {
                if (lastOutputSeconds) {
                    final int width = padWithZeros ? Math.max(3, count) : 3;
                    buffer.append(paddedValue(milliseconds, true, width));
                }
                else {
                    buffer.append(paddedValue(milliseconds, padWithZeros, count));
                }
                lastOutputSeconds = false;
            }
        }
        return buffer.toString();
    }
    
    private static String paddedValue(final long value, final boolean padWithZeros, final int count) {
        final String longString = Long.toString(value);
        return padWithZeros ? StringUtils.leftPad(longString, count, '0') : longString;
    }
    
    static Token[] lexx(final String format) {
        final ArrayList<Token> list = new ArrayList<Token>(format.length());
        boolean inLiteral = false;
        StringBuilder buffer = null;
        Token previous = null;
        for (int i = 0; i < format.length(); ++i) {
            final char ch = format.charAt(i);
            if (inLiteral && ch != '\'') {
                buffer.append(ch);
            }
            else {
                Object value = null;
                switch (ch) {
                    case '\'': {
                        if (inLiteral) {
                            buffer = null;
                            inLiteral = false;
                            break;
                        }
                        buffer = new StringBuilder();
                        list.add(new Token(buffer));
                        inLiteral = true;
                        break;
                    }
                    case 'y': {
                        value = DurationFormatUtils.y;
                        break;
                    }
                    case 'M': {
                        value = DurationFormatUtils.M;
                        break;
                    }
                    case 'd': {
                        value = DurationFormatUtils.d;
                        break;
                    }
                    case 'H': {
                        value = DurationFormatUtils.H;
                        break;
                    }
                    case 'm': {
                        value = DurationFormatUtils.m;
                        break;
                    }
                    case 's': {
                        value = DurationFormatUtils.s;
                        break;
                    }
                    case 'S': {
                        value = DurationFormatUtils.S;
                        break;
                    }
                    default: {
                        if (buffer == null) {
                            buffer = new StringBuilder();
                            list.add(new Token(buffer));
                        }
                        buffer.append(ch);
                        break;
                    }
                }
                if (value != null) {
                    if (previous != null && previous.getValue() == value) {
                        previous.increment();
                    }
                    else {
                        final Token token = new Token(value);
                        list.add(token);
                        previous = token;
                    }
                    buffer = null;
                }
            }
        }
        if (inLiteral) {
            throw new IllegalArgumentException("Unmatched quote in format: " + format);
        }
        return list.toArray(new Token[list.size()]);
    }
    
    static {
        y = "y";
        M = "M";
        d = "d";
        H = "H";
        m = "m";
        s = "s";
        S = "S";
    }
    
    static class Token
    {
        private final Object value;
        private int count;
        
        static boolean containsTokenWithValue(final Token[] tokens, final Object value) {
            for (int sz = tokens.length, i = 0; i < sz; ++i) {
                if (tokens[i].getValue() == value) {
                    return true;
                }
            }
            return false;
        }
        
        Token(final Object value) {
            this.value = value;
            this.count = 1;
        }
        
        Token(final Object value, final int count) {
            this.value = value;
            this.count = count;
        }
        
        void increment() {
            ++this.count;
        }
        
        int getCount() {
            return this.count;
        }
        
        Object getValue() {
            return this.value;
        }
        
        @Override
        public boolean equals(final Object obj2) {
            if (!(obj2 instanceof Token)) {
                return false;
            }
            final Token tok2 = (Token)obj2;
            if (this.value.getClass() != tok2.value.getClass()) {
                return false;
            }
            if (this.count != tok2.count) {
                return false;
            }
            if (this.value instanceof StringBuilder) {
                return this.value.toString().equals(tok2.value.toString());
            }
            if (this.value instanceof Number) {
                return this.value.equals(tok2.value);
            }
            return this.value == tok2.value;
        }
        
        @Override
        public int hashCode() {
            return this.value.hashCode();
        }
        
        @Override
        public String toString() {
            return StringUtils.repeat(this.value.toString(), this.count);
        }
    }
}
