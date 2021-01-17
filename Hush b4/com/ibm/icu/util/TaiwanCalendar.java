// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public class TaiwanCalendar extends GregorianCalendar
{
    private static final long serialVersionUID = 2583005278132380631L;
    public static final int BEFORE_MINGUO = 0;
    public static final int MINGUO = 1;
    private static final int Taiwan_ERA_START = 1911;
    private static final int GREGORIAN_EPOCH = 1970;
    
    public TaiwanCalendar() {
    }
    
    public TaiwanCalendar(final TimeZone zone) {
        super(zone);
    }
    
    public TaiwanCalendar(final Locale aLocale) {
        super(aLocale);
    }
    
    public TaiwanCalendar(final ULocale locale) {
        super(locale);
    }
    
    public TaiwanCalendar(final TimeZone zone, final Locale aLocale) {
        super(zone, aLocale);
    }
    
    public TaiwanCalendar(final TimeZone zone, final ULocale locale) {
        super(zone, locale);
    }
    
    public TaiwanCalendar(final Date date) {
        this();
        this.setTime(date);
    }
    
    public TaiwanCalendar(final int year, final int month, final int date) {
        super(year, month, date);
    }
    
    public TaiwanCalendar(final int year, final int month, final int date, final int hour, final int minute, final int second) {
        super(year, month, date, hour, minute, second);
    }
    
    @Override
    protected int handleGetExtendedYear() {
        int year = 1970;
        if (this.newerField(19, 1) == 19 && this.newerField(19, 0) == 19) {
            year = this.internalGet(19, 1970);
        }
        else {
            final int era = this.internalGet(0, 1);
            if (era == 1) {
                year = this.internalGet(1, 1) + 1911;
            }
            else {
                year = 1 - this.internalGet(1, 1) + 1911;
            }
        }
        return year;
    }
    
    @Override
    protected void handleComputeFields(final int julianDay) {
        super.handleComputeFields(julianDay);
        final int y = this.internalGet(19) - 1911;
        if (y > 0) {
            this.internalSet(0, 1);
            this.internalSet(1, y);
        }
        else {
            this.internalSet(0, 0);
            this.internalSet(1, 1 - y);
        }
    }
    
    @Override
    protected int handleGetLimit(final int field, final int limitType) {
        if (field != 0) {
            return super.handleGetLimit(field, limitType);
        }
        if (limitType == 0 || limitType == 1) {
            return 0;
        }
        return 1;
    }
    
    @Override
    public String getType() {
        return "roc";
    }
}
