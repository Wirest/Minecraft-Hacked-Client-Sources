// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.utils;

import java.util.Locale;
import java.util.HashMap;
import java.util.Map;
import java.lang.ref.SoftReference;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import org.apache.http.util.Args;
import java.util.TimeZone;
import java.util.Date;
import org.apache.http.annotation.Immutable;

@Immutable
public final class DateUtils
{
    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";
    public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
    private static final String[] DEFAULT_PATTERNS;
    private static final Date DEFAULT_TWO_DIGIT_YEAR_START;
    public static final TimeZone GMT;
    
    public static Date parseDate(final String dateValue) {
        return parseDate(dateValue, null, null);
    }
    
    public static Date parseDate(final String dateValue, final String[] dateFormats) {
        return parseDate(dateValue, dateFormats, null);
    }
    
    public static Date parseDate(final String dateValue, final String[] dateFormats, final Date startDate) {
        Args.notNull(dateValue, "Date value");
        final String[] localDateFormats = (dateFormats != null) ? dateFormats : DateUtils.DEFAULT_PATTERNS;
        final Date localStartDate = (startDate != null) ? startDate : DateUtils.DEFAULT_TWO_DIGIT_YEAR_START;
        String v = dateValue;
        if (v.length() > 1 && v.startsWith("'") && v.endsWith("'")) {
            v = v.substring(1, v.length() - 1);
        }
        for (final String dateFormat : localDateFormats) {
            final SimpleDateFormat dateParser = DateFormatHolder.formatFor(dateFormat);
            dateParser.set2DigitYearStart(localStartDate);
            final ParsePosition pos = new ParsePosition(0);
            final Date result = dateParser.parse(v, pos);
            if (pos.getIndex() != 0) {
                return result;
            }
        }
        return null;
    }
    
    public static String formatDate(final Date date) {
        return formatDate(date, "EEE, dd MMM yyyy HH:mm:ss zzz");
    }
    
    public static String formatDate(final Date date, final String pattern) {
        Args.notNull(date, "Date");
        Args.notNull(pattern, "Pattern");
        final SimpleDateFormat formatter = DateFormatHolder.formatFor(pattern);
        return formatter.format(date);
    }
    
    public static void clearThreadLocal() {
        DateFormatHolder.clearThreadLocal();
    }
    
    private DateUtils() {
    }
    
    static {
        DEFAULT_PATTERNS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy" };
        GMT = TimeZone.getTimeZone("GMT");
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(DateUtils.GMT);
        calendar.set(2000, 0, 1, 0, 0, 0);
        calendar.set(14, 0);
        DEFAULT_TWO_DIGIT_YEAR_START = calendar.getTime();
    }
    
    static final class DateFormatHolder
    {
        private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS;
        
        public static SimpleDateFormat formatFor(final String pattern) {
            final SoftReference<Map<String, SimpleDateFormat>> ref = DateFormatHolder.THREADLOCAL_FORMATS.get();
            Map<String, SimpleDateFormat> formats = ref.get();
            if (formats == null) {
                formats = new HashMap<String, SimpleDateFormat>();
                DateFormatHolder.THREADLOCAL_FORMATS.set(new SoftReference<Map<String, SimpleDateFormat>>(formats));
            }
            SimpleDateFormat format = formats.get(pattern);
            if (format == null) {
                format = new SimpleDateFormat(pattern, Locale.US);
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                formats.put(pattern, format);
            }
            return format;
        }
        
        public static void clearThreadLocal() {
            DateFormatHolder.THREADLOCAL_FORMATS.remove();
        }
        
        static {
            THREADLOCAL_FORMATS = new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>() {
                @Override
                protected SoftReference<Map<String, SimpleDateFormat>> initialValue() {
                    return new SoftReference<Map<String, SimpleDateFormat>>(new HashMap<String, SimpleDateFormat>());
                }
            };
        }
    }
}
