// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import com.ibm.icu.impl.Grego;
import java.util.Date;

public class AnnualTimeZoneRule extends TimeZoneRule
{
    private static final long serialVersionUID = -8870666707791230688L;
    public static final int MAX_YEAR = Integer.MAX_VALUE;
    private final DateTimeRule dateTimeRule;
    private final int startYear;
    private final int endYear;
    
    public AnnualTimeZoneRule(final String name, final int rawOffset, final int dstSavings, final DateTimeRule dateTimeRule, final int startYear, final int endYear) {
        super(name, rawOffset, dstSavings);
        this.dateTimeRule = dateTimeRule;
        this.startYear = startYear;
        this.endYear = ((endYear > Integer.MAX_VALUE) ? Integer.MAX_VALUE : endYear);
    }
    
    public DateTimeRule getRule() {
        return this.dateTimeRule;
    }
    
    public int getStartYear() {
        return this.startYear;
    }
    
    public int getEndYear() {
        return this.endYear;
    }
    
    public Date getStartInYear(final int year, final int prevRawOffset, final int prevDSTSavings) {
        if (year < this.startYear || year > this.endYear) {
            return null;
        }
        final int type = this.dateTimeRule.getDateRuleType();
        long ruleDay;
        if (type == 0) {
            ruleDay = Grego.fieldsToDay(year, this.dateTimeRule.getRuleMonth(), this.dateTimeRule.getRuleDayOfMonth());
        }
        else {
            boolean after = true;
            if (type == 1) {
                final int weeks = this.dateTimeRule.getRuleWeekInMonth();
                if (weeks > 0) {
                    ruleDay = Grego.fieldsToDay(year, this.dateTimeRule.getRuleMonth(), 1);
                    ruleDay += 7 * (weeks - 1);
                }
                else {
                    after = false;
                    ruleDay = Grego.fieldsToDay(year, this.dateTimeRule.getRuleMonth(), Grego.monthLength(year, this.dateTimeRule.getRuleMonth()));
                    ruleDay += 7 * (weeks + 1);
                }
            }
            else {
                final int month = this.dateTimeRule.getRuleMonth();
                int dom = this.dateTimeRule.getRuleDayOfMonth();
                if (type == 3) {
                    after = false;
                    if (month == 1 && dom == 29 && !Grego.isLeapYear(year)) {
                        --dom;
                    }
                }
                ruleDay = Grego.fieldsToDay(year, month, dom);
            }
            final int dow = Grego.dayOfWeek(ruleDay);
            int delta = this.dateTimeRule.getRuleDayOfWeek() - dow;
            if (after) {
                delta = ((delta < 0) ? (delta + 7) : delta);
            }
            else {
                delta = ((delta > 0) ? (delta - 7) : delta);
            }
            ruleDay += delta;
        }
        long ruleTime = ruleDay * 86400000L + this.dateTimeRule.getRuleMillisInDay();
        if (this.dateTimeRule.getTimeRuleType() != 2) {
            ruleTime -= prevRawOffset;
        }
        if (this.dateTimeRule.getTimeRuleType() == 0) {
            ruleTime -= prevDSTSavings;
        }
        return new Date(ruleTime);
    }
    
    @Override
    public Date getFirstStart(final int prevRawOffset, final int prevDSTSavings) {
        return this.getStartInYear(this.startYear, prevRawOffset, prevDSTSavings);
    }
    
    @Override
    public Date getFinalStart(final int prevRawOffset, final int prevDSTSavings) {
        if (this.endYear == Integer.MAX_VALUE) {
            return null;
        }
        return this.getStartInYear(this.endYear, prevRawOffset, prevDSTSavings);
    }
    
    @Override
    public Date getNextStart(final long base, final int prevRawOffset, final int prevDSTSavings, final boolean inclusive) {
        final int[] fields = Grego.timeToFields(base, null);
        final int year = fields[0];
        if (year < this.startYear) {
            return this.getFirstStart(prevRawOffset, prevDSTSavings);
        }
        Date d = this.getStartInYear(year, prevRawOffset, prevDSTSavings);
        if (d != null && (d.getTime() < base || (!inclusive && d.getTime() == base))) {
            d = this.getStartInYear(year + 1, prevRawOffset, prevDSTSavings);
        }
        return d;
    }
    
    @Override
    public Date getPreviousStart(final long base, final int prevRawOffset, final int prevDSTSavings, final boolean inclusive) {
        final int[] fields = Grego.timeToFields(base, null);
        final int year = fields[0];
        if (year > this.endYear) {
            return this.getFinalStart(prevRawOffset, prevDSTSavings);
        }
        Date d = this.getStartInYear(year, prevRawOffset, prevDSTSavings);
        if (d != null && (d.getTime() > base || (!inclusive && d.getTime() == base))) {
            d = this.getStartInYear(year - 1, prevRawOffset, prevDSTSavings);
        }
        return d;
    }
    
    @Override
    public boolean isEquivalentTo(final TimeZoneRule other) {
        if (!(other instanceof AnnualTimeZoneRule)) {
            return false;
        }
        final AnnualTimeZoneRule otherRule = (AnnualTimeZoneRule)other;
        return this.startYear == otherRule.startYear && this.endYear == otherRule.endYear && this.dateTimeRule.equals(otherRule.dateTimeRule) && super.isEquivalentTo(other);
    }
    
    @Override
    public boolean isTransitionRule() {
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(super.toString());
        buf.append(", rule={" + this.dateTimeRule + "}");
        buf.append(", startYear=" + this.startYear);
        buf.append(", endYear=");
        if (this.endYear == Integer.MAX_VALUE) {
            buf.append("max");
        }
        else {
            buf.append(this.endYear);
        }
        return buf.toString();
    }
}
