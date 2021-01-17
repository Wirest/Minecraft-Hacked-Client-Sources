// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;

class EasterRule implements DateRule
{
    private static GregorianCalendar gregorian;
    private static GregorianCalendar orthodox;
    private int daysAfterEaster;
    private GregorianCalendar calendar;
    
    public EasterRule(final int daysAfterEaster, final boolean isOrthodox) {
        this.calendar = EasterRule.gregorian;
        this.daysAfterEaster = daysAfterEaster;
        if (isOrthodox) {
            EasterRule.orthodox.setGregorianChange(new Date(Long.MAX_VALUE));
            this.calendar = EasterRule.orthodox;
        }
    }
    
    public Date firstAfter(final Date start) {
        return this.doFirstBetween(start, null);
    }
    
    public Date firstBetween(final Date start, final Date end) {
        return this.doFirstBetween(start, end);
    }
    
    public boolean isOn(final Date date) {
        synchronized (this.calendar) {
            this.calendar.setTime(date);
            final int dayOfYear = this.calendar.get(6);
            this.calendar.setTime(this.computeInYear(this.calendar.getTime(), this.calendar));
            return this.calendar.get(6) == dayOfYear;
        }
    }
    
    public boolean isBetween(final Date start, final Date end) {
        return this.firstBetween(start, end) != null;
    }
    
    private Date doFirstBetween(final Date start, final Date end) {
        synchronized (this.calendar) {
            Date result = this.computeInYear(start, this.calendar);
            if (result.before(start)) {
                this.calendar.setTime(start);
                this.calendar.get(1);
                this.calendar.add(1, 1);
                result = this.computeInYear(this.calendar.getTime(), this.calendar);
            }
            if (end != null && result.after(end)) {
                return null;
            }
            return result;
        }
    }
    
    private Date computeInYear(final Date date, GregorianCalendar cal) {
        if (cal == null) {
            cal = this.calendar;
        }
        synchronized (cal) {
            cal.setTime(date);
            final int year = cal.get(1);
            final int g = year % 19;
            int i = 0;
            int j = 0;
            if (cal.getTime().after(cal.getGregorianChange())) {
                final int c = year / 100;
                final int h = (c - c / 4 - (8 * c + 13) / 25 + 19 * g + 15) % 30;
                i = h - h / 28 * (1 - h / 28 * (29 / (h + 1)) * ((21 - g) / 11));
                j = (year + year / 4 + i + 2 - c + c / 4) % 7;
            }
            else {
                i = (19 * g + 15) % 30;
                j = (year + year / 4 + i) % 7;
            }
            final int l = i - j;
            final int m = 3 + (l + 40) / 44;
            final int d = l + 28 - 31 * (m / 4);
            cal.clear();
            cal.set(0, 1);
            cal.set(1, year);
            cal.set(2, m - 1);
            cal.set(5, d);
            cal.getTime();
            cal.add(5, this.daysAfterEaster);
            return cal.getTime();
        }
    }
    
    static {
        EasterRule.gregorian = new GregorianCalendar();
        EasterRule.orthodox = new GregorianCalendar();
    }
}
