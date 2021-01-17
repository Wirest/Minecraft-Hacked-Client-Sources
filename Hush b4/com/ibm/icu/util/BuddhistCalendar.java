// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public class BuddhistCalendar extends GregorianCalendar
{
    private static final long serialVersionUID = 2583005278132380631L;
    public static final int BE = 0;
    private static final int BUDDHIST_ERA_START = -543;
    private static final int GREGORIAN_EPOCH = 1970;
    
    public BuddhistCalendar() {
    }
    
    public BuddhistCalendar(final TimeZone zone) {
        super(zone);
    }
    
    public BuddhistCalendar(final Locale aLocale) {
        super(aLocale);
    }
    
    public BuddhistCalendar(final ULocale locale) {
        super(locale);
    }
    
    public BuddhistCalendar(final TimeZone zone, final Locale aLocale) {
        super(zone, aLocale);
    }
    
    public BuddhistCalendar(final TimeZone zone, final ULocale locale) {
        super(zone, locale);
    }
    
    public BuddhistCalendar(final Date date) {
        this();
        this.setTime(date);
    }
    
    public BuddhistCalendar(final int year, final int month, final int date) {
        super(year, month, date);
    }
    
    public BuddhistCalendar(final int year, final int month, final int date, final int hour, final int minute, final int second) {
        super(year, month, date, hour, minute, second);
    }
    
    @Override
    protected int handleGetExtendedYear() {
        int year;
        if (this.newerField(19, 1) == 19) {
            year = this.internalGet(19, 1970);
        }
        else {
            year = this.internalGet(1, 2513) - 543;
        }
        return year;
    }
    
    @Override
    protected int handleComputeMonthStart(final int eyear, final int month, final boolean useMonth) {
        return super.handleComputeMonthStart(eyear, month, useMonth);
    }
    
    @Override
    protected void handleComputeFields(final int julianDay) {
        super.handleComputeFields(julianDay);
        final int y = this.internalGet(19) + 543;
        this.internalSet(0, 0);
        this.internalSet(1, y);
    }
    
    @Override
    protected int handleGetLimit(final int field, final int limitType) {
        if (field == 0) {
            return 0;
        }
        return super.handleGetLimit(field, limitType);
    }
    
    @Override
    public String getType() {
        return "buddhist";
    }
}
