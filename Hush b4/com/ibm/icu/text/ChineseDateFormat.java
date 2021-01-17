// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.io.InvalidObjectException;
import java.text.FieldPosition;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ChineseCalendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import java.util.Locale;

public class ChineseDateFormat extends SimpleDateFormat
{
    static final long serialVersionUID = -4610300753104099899L;
    
    @Deprecated
    public ChineseDateFormat(final String pattern, final Locale locale) {
        this(pattern, ULocale.forLocale(locale));
    }
    
    @Deprecated
    public ChineseDateFormat(final String pattern, final ULocale locale) {
        this(pattern, (String)null, locale);
    }
    
    @Deprecated
    public ChineseDateFormat(final String pattern, final String override, final ULocale locale) {
        super(pattern, new ChineseDateFormatSymbols(locale), new ChineseCalendar(TimeZone.getDefault(), locale), locale, true, override);
    }
    
    @Override
    @Deprecated
    protected void subFormat(final StringBuffer buf, final char ch, final int count, final int beginOffset, final int fieldNum, final DisplayContext capitalizationContext, final FieldPosition pos, final Calendar cal) {
        super.subFormat(buf, ch, count, beginOffset, fieldNum, capitalizationContext, pos, cal);
    }
    
    @Override
    @Deprecated
    protected int subParse(final String text, final int start, final char ch, final int count, final boolean obeyCount, final boolean allowNegative, final boolean[] ambiguousYear, final Calendar cal) {
        return super.subParse(text, start, ch, count, obeyCount, allowNegative, ambiguousYear, cal);
    }
    
    @Override
    @Deprecated
    protected DateFormat.Field patternCharToDateFormatField(final char ch) {
        return super.patternCharToDateFormatField(ch);
    }
    
    public static class Field extends DateFormat.Field
    {
        private static final long serialVersionUID = -5102130532751400330L;
        @Deprecated
        public static final Field IS_LEAP_MONTH;
        
        @Deprecated
        protected Field(final String name, final int calendarField) {
            super(name, calendarField);
        }
        
        @Deprecated
        public static DateFormat.Field ofCalendarField(final int calendarField) {
            if (calendarField == 22) {
                return Field.IS_LEAP_MONTH;
            }
            return DateFormat.Field.ofCalendarField(calendarField);
        }
        
        @Override
        @Deprecated
        protected Object readResolve() throws InvalidObjectException {
            if (this.getClass() != Field.class) {
                throw new InvalidObjectException("A subclass of ChineseDateFormat.Field must implement readResolve.");
            }
            if (this.getName().equals(Field.IS_LEAP_MONTH.getName())) {
                return Field.IS_LEAP_MONTH;
            }
            throw new InvalidObjectException("Unknown attribute name.");
        }
        
        static {
            IS_LEAP_MONTH = new Field("is leap month", 22);
        }
    }
}
