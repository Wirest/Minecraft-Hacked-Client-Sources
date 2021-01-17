// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

public class TimeZoneTransition
{
    private final TimeZoneRule from;
    private final TimeZoneRule to;
    private final long time;
    
    public TimeZoneTransition(final long time, final TimeZoneRule from, final TimeZoneRule to) {
        this.time = time;
        this.from = from;
        this.to = to;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public TimeZoneRule getTo() {
        return this.to;
    }
    
    public TimeZoneRule getFrom() {
        return this.from;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("time=" + this.time);
        buf.append(", from={" + this.from + "}");
        buf.append(", to={" + this.to + "}");
        return buf.toString();
    }
}
