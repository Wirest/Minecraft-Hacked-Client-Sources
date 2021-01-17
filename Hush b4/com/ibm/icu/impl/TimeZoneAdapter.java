// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.Date;
import java.util.TimeZone;

public class TimeZoneAdapter extends TimeZone
{
    static final long serialVersionUID = -2040072218820018557L;
    private com.ibm.icu.util.TimeZone zone;
    
    public static TimeZone wrap(final com.ibm.icu.util.TimeZone tz) {
        return new TimeZoneAdapter(tz);
    }
    
    public com.ibm.icu.util.TimeZone unwrap() {
        return this.zone;
    }
    
    public TimeZoneAdapter(final com.ibm.icu.util.TimeZone zone) {
        this.zone = zone;
        super.setID(zone.getID());
    }
    
    @Override
    public void setID(final String ID) {
        super.setID(ID);
        this.zone.setID(ID);
    }
    
    @Override
    public boolean hasSameRules(final TimeZone other) {
        return other instanceof TimeZoneAdapter && this.zone.hasSameRules(((TimeZoneAdapter)other).zone);
    }
    
    @Override
    public int getOffset(final int era, final int year, final int month, final int day, final int dayOfWeek, final int millis) {
        return this.zone.getOffset(era, year, month, day, dayOfWeek, millis);
    }
    
    @Override
    public int getRawOffset() {
        return this.zone.getRawOffset();
    }
    
    @Override
    public void setRawOffset(final int offsetMillis) {
        this.zone.setRawOffset(offsetMillis);
    }
    
    @Override
    public boolean useDaylightTime() {
        return this.zone.useDaylightTime();
    }
    
    @Override
    public boolean inDaylightTime(final Date date) {
        return this.zone.inDaylightTime(date);
    }
    
    @Override
    public Object clone() {
        return new TimeZoneAdapter((com.ibm.icu.util.TimeZone)this.zone.clone());
    }
    
    @Override
    public synchronized int hashCode() {
        return this.zone.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeZoneAdapter) {
            obj = ((TimeZoneAdapter)obj).zone;
        }
        return this.zone.equals(obj);
    }
    
    @Override
    public String toString() {
        return "TimeZoneAdapter: " + this.zone.toString();
    }
}
