// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.MissingResourceException;
import java.util.Date;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;
import com.ibm.icu.util.UResourceBundle;
import java.text.ParsePosition;
import java.text.FieldPosition;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.DateFormat;

public class RelativeDateFormat extends DateFormat
{
    private static final long serialVersionUID = 1131984966440549435L;
    private DateFormat fDateFormat;
    private DateFormat fTimeFormat;
    private MessageFormat fCombinedFormat;
    private SimpleDateFormat fDateTimeFormat;
    private String fDatePattern;
    private String fTimePattern;
    int fDateStyle;
    int fTimeStyle;
    ULocale fLocale;
    private transient URelativeString[] fDates;
    
    public RelativeDateFormat(final int timeStyle, final int dateStyle, final ULocale locale) {
        this.fDateTimeFormat = null;
        this.fDatePattern = null;
        this.fTimePattern = null;
        this.fDates = null;
        this.fLocale = locale;
        this.fTimeStyle = timeStyle;
        this.fDateStyle = dateStyle;
        if (this.fDateStyle != -1) {
            int newStyle = this.fDateStyle & 0xFFFFFF7F;
            DateFormat df = DateFormat.getDateInstance(newStyle, locale);
            if (!(df instanceof SimpleDateFormat)) {
                throw new IllegalArgumentException("Can't create SimpleDateFormat for date style");
            }
            this.fDateTimeFormat = (SimpleDateFormat)df;
            this.fDatePattern = this.fDateTimeFormat.toPattern();
            if (this.fTimeStyle != -1) {
                newStyle = (this.fTimeStyle & 0xFFFFFF7F);
                df = DateFormat.getTimeInstance(newStyle, locale);
                if (df instanceof SimpleDateFormat) {
                    this.fTimePattern = ((SimpleDateFormat)df).toPattern();
                }
            }
        }
        else {
            final int newStyle = this.fTimeStyle & 0xFFFFFF7F;
            final DateFormat df = DateFormat.getTimeInstance(newStyle, locale);
            if (!(df instanceof SimpleDateFormat)) {
                throw new IllegalArgumentException("Can't create SimpleDateFormat for time style");
            }
            this.fDateTimeFormat = (SimpleDateFormat)df;
            this.fTimePattern = this.fDateTimeFormat.toPattern();
        }
        this.initializeCalendar(null, this.fLocale);
        this.loadDates();
        this.initializeCombinedFormat(this.calendar, this.fLocale);
    }
    
    @Override
    public StringBuffer format(final Calendar cal, final StringBuffer toAppendTo, final FieldPosition fieldPosition) {
        String relativeDayString = null;
        if (this.fDateStyle != -1) {
            final int dayDiff = dayDifference(cal);
            relativeDayString = this.getStringForDay(dayDiff);
        }
        if (this.fDateTimeFormat != null && (this.fDatePattern != null || this.fTimePattern != null)) {
            if (this.fDatePattern == null) {
                this.fDateTimeFormat.applyPattern(this.fTimePattern);
                this.fDateTimeFormat.format(cal, toAppendTo, fieldPosition);
            }
            else if (this.fTimePattern == null) {
                if (relativeDayString != null) {
                    toAppendTo.append(relativeDayString);
                }
                else {
                    this.fDateTimeFormat.applyPattern(this.fDatePattern);
                    this.fDateTimeFormat.format(cal, toAppendTo, fieldPosition);
                }
            }
            else {
                String datePattern = this.fDatePattern;
                if (relativeDayString != null) {
                    datePattern = "'" + relativeDayString.replace("'", "''") + "'";
                }
                final StringBuffer combinedPattern = new StringBuffer("");
                this.fCombinedFormat.format(new Object[] { this.fTimePattern, datePattern }, combinedPattern, new FieldPosition(0));
                this.fDateTimeFormat.applyPattern(combinedPattern.toString());
                this.fDateTimeFormat.format(cal, toAppendTo, fieldPosition);
            }
        }
        else if (this.fDateFormat != null) {
            if (relativeDayString != null) {
                toAppendTo.append(relativeDayString);
            }
            else {
                this.fDateFormat.format(cal, toAppendTo, fieldPosition);
            }
        }
        return toAppendTo;
    }
    
    @Override
    public void parse(final String text, final Calendar cal, final ParsePosition pos) {
        throw new UnsupportedOperationException("Relative Date parse is not implemented yet");
    }
    
    private String getStringForDay(final int day) {
        if (this.fDates == null) {
            this.loadDates();
        }
        for (int i = 0; i < this.fDates.length; ++i) {
            if (this.fDates[i].offset == day) {
                return this.fDates[i].string;
            }
        }
        return null;
    }
    
    private synchronized void loadDates() {
        final ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", this.fLocale);
        final ICUResourceBundle rdb = rb.getWithFallback("fields/day/relative");
        final Set<URelativeString> datesSet = new TreeSet<URelativeString>(new Comparator<URelativeString>() {
            public int compare(final URelativeString r1, final URelativeString r2) {
                if (r1.offset == r2.offset) {
                    return 0;
                }
                if (r1.offset < r2.offset) {
                    return -1;
                }
                return 1;
            }
        });
        final UResourceBundleIterator i = rdb.getIterator();
        while (i.hasNext()) {
            final UResourceBundle line = i.next();
            final String k = line.getKey();
            final String v = line.getString();
            final URelativeString rs = new URelativeString(k, v);
            datesSet.add(rs);
        }
        this.fDates = datesSet.toArray(new URelativeString[0]);
    }
    
    private static int dayDifference(final Calendar until) {
        final Calendar nowCal = (Calendar)until.clone();
        final Date nowDate = new Date(System.currentTimeMillis());
        nowCal.clear();
        nowCal.setTime(nowDate);
        final int dayDiff = until.get(20) - nowCal.get(20);
        return dayDiff;
    }
    
    private Calendar initializeCalendar(final TimeZone zone, final ULocale locale) {
        if (this.calendar == null) {
            if (zone == null) {
                this.calendar = Calendar.getInstance(locale);
            }
            else {
                this.calendar = Calendar.getInstance(zone, locale);
            }
        }
        return this.calendar;
    }
    
    private MessageFormat initializeCombinedFormat(final Calendar cal, final ULocale locale) {
        String pattern = "{1} {0}";
        try {
            final CalendarData calData = new CalendarData(locale, cal.getType());
            final String[] patterns = calData.getDateTimePatterns();
            if (patterns != null && patterns.length >= 9) {
                int glueIndex = 8;
                if (patterns.length >= 13) {
                    switch (this.fDateStyle) {
                        case 0:
                        case 128: {
                            ++glueIndex;
                            break;
                        }
                        case 1:
                        case 129: {
                            glueIndex += 2;
                            break;
                        }
                        case 2:
                        case 130: {
                            glueIndex += 3;
                            break;
                        }
                        case 3:
                        case 131: {
                            glueIndex += 4;
                            break;
                        }
                    }
                }
                pattern = patterns[glueIndex];
            }
        }
        catch (MissingResourceException ex) {}
        return this.fCombinedFormat = new MessageFormat(pattern, locale);
    }
    
    public static class URelativeString
    {
        public int offset;
        public String string;
        
        URelativeString(final int offset, final String string) {
            this.offset = offset;
            this.string = string;
        }
        
        URelativeString(final String offset, final String string) {
            this.offset = Integer.parseInt(offset);
            this.string = string;
        }
    }
}
