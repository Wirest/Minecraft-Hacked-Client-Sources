// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.io.Serializable;

public final class DateInterval implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final long fromDate;
    private final long toDate;
    
    public DateInterval(final long from, final long to) {
        this.fromDate = from;
        this.toDate = to;
    }
    
    public long getFromDate() {
        return this.fromDate;
    }
    
    public long getToDate() {
        return this.toDate;
    }
    
    @Override
    public boolean equals(final Object a) {
        if (a instanceof DateInterval) {
            final DateInterval di = (DateInterval)a;
            return this.fromDate == di.fromDate && this.toDate == di.toDate;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int)(this.fromDate + this.toDate);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.fromDate) + " " + String.valueOf(this.toDate);
    }
}
