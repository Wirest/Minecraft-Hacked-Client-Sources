// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class RangeDateRule implements DateRule
{
    List<Range> ranges;
    
    public RangeDateRule() {
        this.ranges = new ArrayList<Range>(2);
    }
    
    public void add(final DateRule rule) {
        this.add(new Date(Long.MIN_VALUE), rule);
    }
    
    public void add(final Date start, final DateRule rule) {
        this.ranges.add(new Range(start, rule));
    }
    
    public Date firstAfter(final Date start) {
        int index = this.startIndex(start);
        if (index == this.ranges.size()) {
            index = 0;
        }
        Date result = null;
        final Range r = this.rangeAt(index);
        final Range e = this.rangeAt(index + 1);
        if (r != null && r.rule != null) {
            if (e != null) {
                result = r.rule.firstBetween(start, e.start);
            }
            else {
                result = r.rule.firstAfter(start);
            }
        }
        return result;
    }
    
    public Date firstBetween(final Date start, final Date end) {
        if (end == null) {
            return this.firstAfter(start);
        }
        final int index = this.startIndex(start);
        Date result = null;
        Range r;
        Date e = null;
        for (Range next = this.rangeAt(index); result == null && next != null && !next.start.after(end); result = r.rule.firstBetween(start, e)) {
            r = next;
            next = this.rangeAt(index + 1);
            if (r.rule != null) {
                e = ((next != null && !next.start.after(end)) ? next.start : end);
            }
        }
        return result;
    }
    
    public boolean isOn(final Date date) {
        final Range r = this.rangeAt(this.startIndex(date));
        return r != null && r.rule != null && r.rule.isOn(date);
    }
    
    public boolean isBetween(final Date start, final Date end) {
        return this.firstBetween(start, end) == null;
    }
    
    private int startIndex(final Date start) {
        int lastIndex = this.ranges.size();
        for (int i = 0; i < this.ranges.size(); ++i) {
            final Range r = this.ranges.get(i);
            if (start.before(r.start)) {
                break;
            }
            lastIndex = i;
        }
        return lastIndex;
    }
    
    private Range rangeAt(final int index) {
        return (index < this.ranges.size()) ? this.ranges.get(index) : null;
    }
}
