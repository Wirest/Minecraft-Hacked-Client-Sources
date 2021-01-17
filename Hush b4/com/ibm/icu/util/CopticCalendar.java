// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public final class CopticCalendar extends CECalendar
{
    private static final long serialVersionUID = 5903818751846742911L;
    public static final int TOUT = 0;
    public static final int BABA = 1;
    public static final int HATOR = 2;
    public static final int KIAHK = 3;
    public static final int TOBA = 4;
    public static final int AMSHIR = 5;
    public static final int BARAMHAT = 6;
    public static final int BARAMOUDA = 7;
    public static final int BASHANS = 8;
    public static final int PAONA = 9;
    public static final int EPEP = 10;
    public static final int MESRA = 11;
    public static final int NASIE = 12;
    private static final int JD_EPOCH_OFFSET = 1824665;
    private static final int BCE = 0;
    private static final int CE = 1;
    
    public CopticCalendar() {
    }
    
    public CopticCalendar(final TimeZone zone) {
        super(zone);
    }
    
    public CopticCalendar(final Locale aLocale) {
        super(aLocale);
    }
    
    public CopticCalendar(final ULocale locale) {
        super(locale);
    }
    
    public CopticCalendar(final TimeZone zone, final Locale aLocale) {
        super(zone, aLocale);
    }
    
    public CopticCalendar(final TimeZone zone, final ULocale locale) {
        super(zone, locale);
    }
    
    public CopticCalendar(final int year, final int month, final int date) {
        super(year, month, date);
    }
    
    public CopticCalendar(final Date date) {
        super(date);
    }
    
    public CopticCalendar(final int year, final int month, final int date, final int hour, final int minute, final int second) {
        super(year, month, date, hour, minute, second);
    }
    
    @Override
    public String getType() {
        return "coptic";
    }
    
    @Override
    @Deprecated
    protected int handleGetExtendedYear() {
        int eyear;
        if (this.newerField(19, 1) == 19) {
            eyear = this.internalGet(19, 1);
        }
        else {
            final int era = this.internalGet(0, 1);
            if (era == 0) {
                eyear = 1 - this.internalGet(1, 1);
            }
            else {
                eyear = this.internalGet(1, 1);
            }
        }
        return eyear;
    }
    
    @Override
    @Deprecated
    protected void handleComputeFields(final int julianDay) {
        final int[] fields = new int[3];
        CECalendar.jdToCE(julianDay, this.getJDEpochOffset(), fields);
        int era;
        int year;
        if (fields[0] <= 0) {
            era = 0;
            year = 1 - fields[0];
        }
        else {
            era = 1;
            year = fields[0];
        }
        this.internalSet(19, fields[0]);
        this.internalSet(0, era);
        this.internalSet(1, year);
        this.internalSet(2, fields[1]);
        this.internalSet(5, fields[2]);
        this.internalSet(6, 30 * fields[1] + fields[2]);
    }
    
    @Override
    @Deprecated
    protected int getJDEpochOffset() {
        return 1824665;
    }
    
    public static int copticToJD(final long year, final int month, final int date) {
        return CECalendar.ceToJD(year, month, date, 1824665);
    }
}
