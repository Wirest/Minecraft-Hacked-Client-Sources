// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;

public class SimpleDateRule implements DateRule
{
    private static GregorianCalendar gCalendar;
    private Calendar calendar;
    private int month;
    private int dayOfMonth;
    private int dayOfWeek;
    
    public SimpleDateRule(final int month, final int dayOfMonth) {
        this.calendar = SimpleDateRule.gCalendar;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = 0;
    }
    
    SimpleDateRule(final int month, final int dayOfMonth, final Calendar cal) {
        this.calendar = SimpleDateRule.gCalendar;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = 0;
        this.calendar = cal;
    }
    
    public SimpleDateRule(final int month, final int dayOfMonth, final int dayOfWeek, final boolean after) {
        this.calendar = SimpleDateRule.gCalendar;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = (after ? dayOfWeek : (-dayOfWeek));
    }
    
    public Date firstAfter(final Date start) {
        return this.doFirstBetween(start, null);
    }
    
    public Date firstBetween(final Date start, final Date end) {
        return this.doFirstBetween(start, end);
    }
    
    public boolean isOn(final Date date) {
        final Calendar c = this.calendar;
        synchronized (c) {
            c.setTime(date);
            final int dayOfYear = c.get(6);
            c.setTime(this.computeInYear(c.get(1), c));
            return c.get(6) == dayOfYear;
        }
    }
    
    public boolean isBetween(final Date start, final Date end) {
        return this.firstBetween(start, end) != null;
    }
    
    private Date doFirstBetween(final Date start, final Date end) {
        final Calendar c = this.calendar;
        synchronized (c) {
            c.setTime(start);
            int year = c.get(1);
            final int mon = c.get(2);
            if (mon > this.month) {
                ++year;
            }
            Date result = this.computeInYear(year, c);
            if (mon == this.month && result.before(start)) {
                result = this.computeInYear(year + 1, c);
            }
            if (end != null && result.after(end)) {
                return null;
            }
            return result;
        }
    }
    
    private Date computeInYear(final int year, final Calendar c) {
        synchronized (c) {
            c.clear();
            c.set(0, c.getMaximum(0));
            c.set(1, year);
            c.set(2, this.month);
            c.set(5, this.dayOfMonth);
            if (this.dayOfWeek != 0) {
                c.setTime(c.getTime());
                final int weekday = c.get(7);
                int delta = 0;
                if (this.dayOfWeek > 0) {
                    delta = (this.dayOfWeek - weekday + 7) % 7;
                }
                else {
                    delta = -((this.dayOfWeek + weekday + 7) % 7);
                }
                c.add(5, delta);
            }
            return c.getTime();
        }
    }
    
    static {
        SimpleDateRule.gCalendar = new GregorianCalendar();
    }
}
