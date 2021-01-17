// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import java.util.Arrays;

public class TimeArrayTimeZoneRule extends TimeZoneRule
{
    private static final long serialVersionUID = -1117109130077415245L;
    private final long[] startTimes;
    private final int timeType;
    
    public TimeArrayTimeZoneRule(final String name, final int rawOffset, final int dstSavings, final long[] startTimes, final int timeType) {
        super(name, rawOffset, dstSavings);
        if (startTimes == null || startTimes.length == 0) {
            throw new IllegalArgumentException("No start times are specified.");
        }
        Arrays.sort(this.startTimes = startTimes.clone());
        this.timeType = timeType;
    }
    
    public long[] getStartTimes() {
        return this.startTimes.clone();
    }
    
    public int getTimeType() {
        return this.timeType;
    }
    
    @Override
    public Date getFirstStart(final int prevRawOffset, final int prevDSTSavings) {
        return new Date(this.getUTC(this.startTimes[0], prevRawOffset, prevDSTSavings));
    }
    
    @Override
    public Date getFinalStart(final int prevRawOffset, final int prevDSTSavings) {
        return new Date(this.getUTC(this.startTimes[this.startTimes.length - 1], prevRawOffset, prevDSTSavings));
    }
    
    @Override
    public Date getNextStart(final long base, final int prevOffset, final int prevDSTSavings, final boolean inclusive) {
        int i;
        for (i = this.startTimes.length - 1; i >= 0; --i) {
            final long time = this.getUTC(this.startTimes[i], prevOffset, prevDSTSavings);
            if (time < base) {
                break;
            }
            if (!inclusive && time == base) {
                break;
            }
        }
        if (i == this.startTimes.length - 1) {
            return null;
        }
        return new Date(this.getUTC(this.startTimes[i + 1], prevOffset, prevDSTSavings));
    }
    
    @Override
    public Date getPreviousStart(final long base, final int prevOffset, final int prevDSTSavings, final boolean inclusive) {
        for (int i = this.startTimes.length - 1; i >= 0; --i) {
            final long time = this.getUTC(this.startTimes[i], prevOffset, prevDSTSavings);
            if (time < base || (inclusive && time == base)) {
                return new Date(time);
            }
        }
        return null;
    }
    
    @Override
    public boolean isEquivalentTo(final TimeZoneRule other) {
        return other instanceof TimeArrayTimeZoneRule && (this.timeType == ((TimeArrayTimeZoneRule)other).timeType && Arrays.equals(this.startTimes, ((TimeArrayTimeZoneRule)other).startTimes)) && super.isEquivalentTo(other);
    }
    
    @Override
    public boolean isTransitionRule() {
        return true;
    }
    
    private long getUTC(long time, final int raw, final int dst) {
        if (this.timeType != 2) {
            time -= raw;
        }
        if (this.timeType == 0) {
            time -= dst;
        }
        return time;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(super.toString());
        buf.append(", timeType=");
        buf.append(this.timeType);
        buf.append(", startTimes=[");
        for (int i = 0; i < this.startTimes.length; ++i) {
            if (i != 0) {
                buf.append(", ");
            }
            buf.append(Long.toString(this.startTimes[i]));
        }
        buf.append("]");
        return buf.toString();
    }
}
