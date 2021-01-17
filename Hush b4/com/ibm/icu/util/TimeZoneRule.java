// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import java.io.Serializable;

public abstract class TimeZoneRule implements Serializable
{
    private static final long serialVersionUID = 6374143828553768100L;
    private final String name;
    private final int rawOffset;
    private final int dstSavings;
    
    public TimeZoneRule(final String name, final int rawOffset, final int dstSavings) {
        this.name = name;
        this.rawOffset = rawOffset;
        this.dstSavings = dstSavings;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getRawOffset() {
        return this.rawOffset;
    }
    
    public int getDSTSavings() {
        return this.dstSavings;
    }
    
    public boolean isEquivalentTo(final TimeZoneRule other) {
        return this.rawOffset == other.rawOffset && this.dstSavings == other.dstSavings;
    }
    
    public abstract Date getFirstStart(final int p0, final int p1);
    
    public abstract Date getFinalStart(final int p0, final int p1);
    
    public abstract Date getNextStart(final long p0, final int p1, final int p2, final boolean p3);
    
    public abstract Date getPreviousStart(final long p0, final int p1, final int p2, final boolean p3);
    
    public abstract boolean isTransitionRule();
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("name=" + this.name);
        buf.append(", stdOffset=" + this.rawOffset);
        buf.append(", dstSaving=" + this.dstSavings);
        return buf.toString();
    }
}
