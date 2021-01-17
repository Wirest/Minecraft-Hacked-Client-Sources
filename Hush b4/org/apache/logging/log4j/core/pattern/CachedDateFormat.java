// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.TimeZone;
import java.text.FieldPosition;
import java.util.Date;
import java.text.DateFormat;

final class CachedDateFormat extends DateFormat
{
    public static final int NO_MILLISECONDS = -2;
    public static final int UNRECOGNIZED_MILLISECONDS = -1;
    private static final long serialVersionUID = -1253877934598423628L;
    private static final String DIGITS = "0123456789";
    private static final int MAGIC1 = 654;
    private static final String MAGICSTRING1 = "654";
    private static final int MAGIC2 = 987;
    private static final String MAGICSTRING2 = "987";
    private static final String ZERO_STRING = "000";
    private static final int BUF_SIZE = 50;
    private static final int DEFAULT_VALIDITY = 1000;
    private static final int THREE_DIGITS = 100;
    private static final int TWO_DIGITS = 10;
    private static final long SLOTS = 1000L;
    private final DateFormat formatter;
    private int millisecondStart;
    private long slotBegin;
    private final StringBuffer cache;
    private final int expiration;
    private long previousTime;
    private final Date tmpDate;
    
    public CachedDateFormat(final DateFormat dateFormat, final int expiration) {
        this.cache = new StringBuffer(50);
        this.tmpDate = new Date(0L);
        if (dateFormat == null) {
            throw new IllegalArgumentException("dateFormat cannot be null");
        }
        if (expiration < 0) {
            throw new IllegalArgumentException("expiration must be non-negative");
        }
        this.formatter = dateFormat;
        this.expiration = expiration;
        this.millisecondStart = 0;
        this.previousTime = Long.MIN_VALUE;
        this.slotBegin = Long.MIN_VALUE;
    }
    
    public static int findMillisecondStart(final long time, final String formatted, final DateFormat formatter) {
        long slotBegin = time / 1000L * 1000L;
        if (slotBegin > time) {
            slotBegin -= 1000L;
        }
        final int millis = (int)(time - slotBegin);
        int magic = 654;
        String magicString = "654";
        if (millis == 654) {
            magic = 987;
            magicString = "987";
        }
        final String plusMagic = formatter.format(new Date(slotBegin + magic));
        if (plusMagic.length() != formatted.length()) {
            return -1;
        }
        int i = 0;
        while (i < formatted.length()) {
            if (formatted.charAt(i) != plusMagic.charAt(i)) {
                final StringBuffer formattedMillis = new StringBuffer("ABC");
                millisecondFormat(millis, formattedMillis, 0);
                final String plusZero = formatter.format(new Date(slotBegin));
                if (plusZero.length() == formatted.length() && magicString.regionMatches(0, plusMagic, i, magicString.length()) && formattedMillis.toString().regionMatches(0, formatted, i, magicString.length()) && "000".regionMatches(0, plusZero, i, "000".length())) {
                    return i;
                }
                return -1;
            }
            else {
                ++i;
            }
        }
        return -2;
    }
    
    @Override
    public StringBuffer format(final Date date, final StringBuffer sbuf, final FieldPosition fieldPosition) {
        this.format(date.getTime(), sbuf);
        return sbuf;
    }
    
    public StringBuffer format(final long now, final StringBuffer buf) {
        if (now == this.previousTime) {
            buf.append(this.cache);
            return buf;
        }
        if (this.millisecondStart != -1 && now < this.slotBegin + this.expiration && now >= this.slotBegin && now < this.slotBegin + 1000L) {
            if (this.millisecondStart >= 0) {
                millisecondFormat((int)(now - this.slotBegin), this.cache, this.millisecondStart);
            }
            this.previousTime = now;
            buf.append(this.cache);
            return buf;
        }
        this.cache.setLength(0);
        this.tmpDate.setTime(now);
        this.cache.append(this.formatter.format(this.tmpDate));
        buf.append(this.cache);
        this.previousTime = now;
        this.slotBegin = this.previousTime / 1000L * 1000L;
        if (this.slotBegin > this.previousTime) {
            this.slotBegin -= 1000L;
        }
        if (this.millisecondStart >= 0) {
            this.millisecondStart = findMillisecondStart(now, this.cache.toString(), this.formatter);
        }
        return buf;
    }
    
    private static void millisecondFormat(final int millis, final StringBuffer buf, final int offset) {
        buf.setCharAt(offset, "0123456789".charAt(millis / 100));
        buf.setCharAt(offset + 1, "0123456789".charAt(millis / 10 % 10));
        buf.setCharAt(offset + 2, "0123456789".charAt(millis % 10));
    }
    
    @Override
    public void setTimeZone(final TimeZone timeZone) {
        this.formatter.setTimeZone(timeZone);
        this.previousTime = Long.MIN_VALUE;
        this.slotBegin = Long.MIN_VALUE;
    }
    
    @Override
    public Date parse(final String s, final ParsePosition pos) {
        return this.formatter.parse(s, pos);
    }
    
    @Override
    public NumberFormat getNumberFormat() {
        return this.formatter.getNumberFormat();
    }
    
    public static int getMaximumCacheValidity(final String pattern) {
        final int firstS = pattern.indexOf(83);
        if (firstS >= 0 && firstS != pattern.lastIndexOf("SSS")) {
            return 1;
        }
        return 1000;
    }
}
