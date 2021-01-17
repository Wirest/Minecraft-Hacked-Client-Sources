// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.CalendarData;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ChineseCalendar;
import java.util.Locale;
import com.ibm.icu.util.ULocale;

public class ChineseDateFormatSymbols extends DateFormatSymbols
{
    static final long serialVersionUID = 6827816119783952890L;
    String[] isLeapMonth;
    
    @Deprecated
    public ChineseDateFormatSymbols() {
        this(ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    @Deprecated
    public ChineseDateFormatSymbols(final Locale locale) {
        super(ChineseCalendar.class, ULocale.forLocale(locale));
    }
    
    @Deprecated
    public ChineseDateFormatSymbols(final ULocale locale) {
        super(ChineseCalendar.class, locale);
    }
    
    @Deprecated
    public ChineseDateFormatSymbols(final Calendar cal, final Locale locale) {
        super((cal == null) ? null : cal.getClass(), locale);
    }
    
    @Deprecated
    public ChineseDateFormatSymbols(final Calendar cal, final ULocale locale) {
        super((cal == null) ? null : cal.getClass(), locale);
    }
    
    @Deprecated
    public String getLeapMonth(final int leap) {
        return this.isLeapMonth[leap];
    }
    
    @Override
    @Deprecated
    protected void initializeData(final ULocale loc, final CalendarData calData) {
        super.initializeData(loc, calData);
        this.initializeIsLeapMonth();
    }
    
    @Override
    void initializeData(final DateFormatSymbols dfs) {
        super.initializeData(dfs);
        if (dfs instanceof ChineseDateFormatSymbols) {
            this.isLeapMonth = ((ChineseDateFormatSymbols)dfs).isLeapMonth;
        }
        else {
            this.initializeIsLeapMonth();
        }
    }
    
    private void initializeIsLeapMonth() {
        (this.isLeapMonth = new String[2])[0] = "";
        this.isLeapMonth[1] = ((this.leapMonthPatterns != null) ? this.leapMonthPatterns[0].replace("{0}", "") : "");
    }
}
