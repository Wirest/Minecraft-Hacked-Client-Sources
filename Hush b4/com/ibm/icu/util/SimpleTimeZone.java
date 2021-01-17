// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import com.ibm.icu.impl.Grego;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SimpleTimeZone extends BasicTimeZone
{
    private static final long serialVersionUID = -7034676239311322769L;
    public static final int WALL_TIME = 0;
    public static final int STANDARD_TIME = 1;
    public static final int UTC_TIME = 2;
    private static final byte[] staticMonthLength;
    private static final int DOM_MODE = 1;
    private static final int DOW_IN_MONTH_MODE = 2;
    private static final int DOW_GE_DOM_MODE = 3;
    private static final int DOW_LE_DOM_MODE = 4;
    private int raw;
    private int dst;
    private STZInfo xinfo;
    private int startMonth;
    private int startDay;
    private int startDayOfWeek;
    private int startTime;
    private int startTimeMode;
    private int endTimeMode;
    private int endMonth;
    private int endDay;
    private int endDayOfWeek;
    private int endTime;
    private int startYear;
    private boolean useDaylight;
    private int startMode;
    private int endMode;
    private transient boolean transitionRulesInitialized;
    private transient InitialTimeZoneRule initialRule;
    private transient TimeZoneTransition firstTransition;
    private transient AnnualTimeZoneRule stdRule;
    private transient AnnualTimeZoneRule dstRule;
    private transient boolean isFrozen;
    
    public SimpleTimeZone(final int rawOffset, final String ID) {
        super(ID);
        this.dst = 3600000;
        this.xinfo = null;
        this.isFrozen = false;
        this.construct(rawOffset, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3600000);
    }
    
    public SimpleTimeZone(final int rawOffset, final String ID, final int startMonth, final int startDay, final int startDayOfWeek, final int startTime, final int endMonth, final int endDay, final int endDayOfWeek, final int endTime) {
        super(ID);
        this.dst = 3600000;
        this.xinfo = null;
        this.isFrozen = false;
        this.construct(rawOffset, startMonth, startDay, startDayOfWeek, startTime, 0, endMonth, endDay, endDayOfWeek, endTime, 0, 3600000);
    }
    
    public SimpleTimeZone(final int rawOffset, final String ID, final int startMonth, final int startDay, final int startDayOfWeek, final int startTime, final int startTimeMode, final int endMonth, final int endDay, final int endDayOfWeek, final int endTime, final int endTimeMode, final int dstSavings) {
        super(ID);
        this.dst = 3600000;
        this.xinfo = null;
        this.isFrozen = false;
        this.construct(rawOffset, startMonth, startDay, startDayOfWeek, startTime, startTimeMode, endMonth, endDay, endDayOfWeek, endTime, endTimeMode, dstSavings);
    }
    
    public SimpleTimeZone(final int rawOffset, final String ID, final int startMonth, final int startDay, final int startDayOfWeek, final int startTime, final int endMonth, final int endDay, final int endDayOfWeek, final int endTime, final int dstSavings) {
        super(ID);
        this.dst = 3600000;
        this.xinfo = null;
        this.isFrozen = false;
        this.construct(rawOffset, startMonth, startDay, startDayOfWeek, startTime, 0, endMonth, endDay, endDayOfWeek, endTime, 0, dstSavings);
    }
    
    @Override
    public void setID(final String ID) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
        }
        super.setID(ID);
        this.transitionRulesInitialized = false;
    }
    
    @Override
    public void setRawOffset(final int offsetMillis) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
        }
        this.raw = offsetMillis;
        this.transitionRulesInitialized = false;
    }
    
    @Override
    public int getRawOffset() {
        return this.raw;
    }
    
    public void setStartYear(final int year) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
        }
        this.getSTZInfo().sy = year;
        this.startYear = year;
        this.transitionRulesInitialized = false;
    }
    
    public void setStartRule(final int month, final int dayOfWeekInMonth, final int dayOfWeek, final int time) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
        }
        this.getSTZInfo().setStart(month, dayOfWeekInMonth, dayOfWeek, time, -1, false);
        this.setStartRule(month, dayOfWeekInMonth, dayOfWeek, time, 0);
    }
    
    private void setStartRule(final int month, final int dayOfWeekInMonth, final int dayOfWeek, final int time, final int mode) {
        assert !this.isFrozen();
        this.startMonth = month;
        this.startDay = dayOfWeekInMonth;
        this.startDayOfWeek = dayOfWeek;
        this.startTime = time;
        this.startTimeMode = mode;
        this.decodeStartRule();
        this.transitionRulesInitialized = false;
    }
    
    public void setStartRule(final int month, final int dayOfMonth, final int time) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
        }
        this.getSTZInfo().setStart(month, -1, -1, time, dayOfMonth, false);
        this.setStartRule(month, dayOfMonth, 0, time, 0);
    }
    
    public void setStartRule(final int month, final int dayOfMonth, final int dayOfWeek, final int time, final boolean after) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
        }
        this.getSTZInfo().setStart(month, -1, dayOfWeek, time, dayOfMonth, after);
        this.setStartRule(month, after ? dayOfMonth : (-dayOfMonth), -dayOfWeek, time, 0);
    }
    
    public void setEndRule(final int month, final int dayOfWeekInMonth, final int dayOfWeek, final int time) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
        }
        this.getSTZInfo().setEnd(month, dayOfWeekInMonth, dayOfWeek, time, -1, false);
        this.setEndRule(month, dayOfWeekInMonth, dayOfWeek, time, 0);
    }
    
    public void setEndRule(final int month, final int dayOfMonth, final int time) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
        }
        this.getSTZInfo().setEnd(month, -1, -1, time, dayOfMonth, false);
        this.setEndRule(month, dayOfMonth, 0, time);
    }
    
    public void setEndRule(final int month, final int dayOfMonth, final int dayOfWeek, final int time, final boolean after) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
        }
        this.getSTZInfo().setEnd(month, -1, dayOfWeek, time, dayOfMonth, after);
        this.setEndRule(month, dayOfMonth, dayOfWeek, time, 0, after);
    }
    
    private void setEndRule(final int month, final int dayOfMonth, final int dayOfWeek, final int time, final int mode, final boolean after) {
        assert !this.isFrozen();
        this.setEndRule(month, after ? dayOfMonth : (-dayOfMonth), -dayOfWeek, time, mode);
    }
    
    private void setEndRule(final int month, final int dayOfWeekInMonth, final int dayOfWeek, final int time, final int mode) {
        assert !this.isFrozen();
        this.endMonth = month;
        this.endDay = dayOfWeekInMonth;
        this.endDayOfWeek = dayOfWeek;
        this.endTime = time;
        this.endTimeMode = mode;
        this.decodeEndRule();
        this.transitionRulesInitialized = false;
    }
    
    public void setDSTSavings(final int millisSavedDuringDST) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
        }
        if (millisSavedDuringDST <= 0) {
            throw new IllegalArgumentException();
        }
        this.dst = millisSavedDuringDST;
        this.transitionRulesInitialized = false;
    }
    
    @Override
    public int getDSTSavings() {
        return this.dst;
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (this.xinfo != null) {
            this.xinfo.applyTo(this);
        }
    }
    
    @Override
    public String toString() {
        return "SimpleTimeZone: " + this.getID();
    }
    
    private STZInfo getSTZInfo() {
        if (this.xinfo == null) {
            this.xinfo = new STZInfo();
        }
        return this.xinfo;
    }
    
    @Override
    public int getOffset(final int era, final int year, final int month, final int day, final int dayOfWeek, final int millis) {
        if (month < 0 || month > 11) {
            throw new IllegalArgumentException();
        }
        return this.getOffset(era, year, month, day, dayOfWeek, millis, Grego.monthLength(year, month));
    }
    
    @Deprecated
    public int getOffset(final int era, final int year, final int month, final int day, final int dayOfWeek, final int millis, final int monthLength) {
        if (month < 0 || month > 11) {
            throw new IllegalArgumentException();
        }
        return this.getOffset(era, year, month, day, dayOfWeek, millis, Grego.monthLength(year, month), Grego.previousMonthLength(year, month));
    }
    
    private int getOffset(final int era, final int year, final int month, final int day, final int dayOfWeek, final int millis, final int monthLength, final int prevMonthLength) {
        if ((era != 1 && era != 0) || month < 0 || month > 11 || day < 1 || day > monthLength || dayOfWeek < 1 || dayOfWeek > 7 || millis < 0 || millis >= 86400000 || monthLength < 28 || monthLength > 31 || prevMonthLength < 28 || prevMonthLength > 31) {
            throw new IllegalArgumentException();
        }
        int result = this.raw;
        if (!this.useDaylight || year < this.startYear || era != 1) {
            return result;
        }
        final boolean southern = this.startMonth > this.endMonth;
        final int startCompare = this.compareToRule(month, monthLength, prevMonthLength, day, dayOfWeek, millis, (this.startTimeMode == 2) ? (-this.raw) : 0, this.startMode, this.startMonth, this.startDayOfWeek, this.startDay, this.startTime);
        int endCompare = 0;
        if (southern != startCompare >= 0) {
            endCompare = this.compareToRule(month, monthLength, prevMonthLength, day, dayOfWeek, millis, (this.endTimeMode == 0) ? this.dst : ((this.endTimeMode == 2) ? (-this.raw) : 0), this.endMode, this.endMonth, this.endDayOfWeek, this.endDay, this.endTime);
        }
        if ((!southern && startCompare >= 0 && endCompare < 0) || (southern && (startCompare >= 0 || endCompare < 0))) {
            result += this.dst;
        }
        return result;
    }
    
    @Override
    @Deprecated
    public void getOffsetFromLocal(long date, final int nonExistingTimeOpt, final int duplicatedTimeOpt, final int[] offsets) {
        offsets[0] = this.getRawOffset();
        final int[] fields = new int[6];
        Grego.timeToFields(date, fields);
        offsets[1] = this.getOffset(1, fields[0], fields[1], fields[2], fields[3], fields[5]) - offsets[0];
        boolean recalc = false;
        if (offsets[1] > 0) {
            if ((nonExistingTimeOpt & 0x3) == 0x1 || ((nonExistingTimeOpt & 0x3) != 0x3 && (nonExistingTimeOpt & 0xC) != 0xC)) {
                date -= this.getDSTSavings();
                recalc = true;
            }
        }
        else if ((duplicatedTimeOpt & 0x3) == 0x3 || ((duplicatedTimeOpt & 0x3) != 0x1 && (duplicatedTimeOpt & 0xC) == 0x4)) {
            date -= this.getDSTSavings();
            recalc = true;
        }
        if (recalc) {
            Grego.timeToFields(date, fields);
            offsets[1] = this.getOffset(1, fields[0], fields[1], fields[2], fields[3], fields[5]) - offsets[0];
        }
    }
    
    private int compareToRule(int month, final int monthLen, final int prevMonthLen, int dayOfMonth, int dayOfWeek, int millis, final int millisDelta, final int ruleMode, final int ruleMonth, final int ruleDayOfWeek, int ruleDay, final int ruleMillis) {
        millis += millisDelta;
        while (millis >= 86400000) {
            millis -= 86400000;
            ++dayOfMonth;
            dayOfWeek = 1 + dayOfWeek % 7;
            if (dayOfMonth > monthLen) {
                dayOfMonth = 1;
                ++month;
            }
        }
        while (millis < 0) {
            --dayOfMonth;
            dayOfWeek = 1 + (dayOfWeek + 5) % 7;
            if (dayOfMonth < 1) {
                dayOfMonth = prevMonthLen;
                --month;
            }
            millis += 86400000;
        }
        if (month < ruleMonth) {
            return -1;
        }
        if (month > ruleMonth) {
            return 1;
        }
        int ruleDayOfMonth = 0;
        if (ruleDay > monthLen) {
            ruleDay = monthLen;
        }
        switch (ruleMode) {
            case 1: {
                ruleDayOfMonth = ruleDay;
                break;
            }
            case 2: {
                if (ruleDay > 0) {
                    ruleDayOfMonth = 1 + (ruleDay - 1) * 7 + (7 + ruleDayOfWeek - (dayOfWeek - dayOfMonth + 1)) % 7;
                    break;
                }
                ruleDayOfMonth = monthLen + (ruleDay + 1) * 7 - (7 + (dayOfWeek + monthLen - dayOfMonth) - ruleDayOfWeek) % 7;
                break;
            }
            case 3: {
                ruleDayOfMonth = ruleDay + (49 + ruleDayOfWeek - ruleDay - dayOfWeek + dayOfMonth) % 7;
                break;
            }
            case 4: {
                ruleDayOfMonth = ruleDay - (49 - ruleDayOfWeek + ruleDay + dayOfWeek - dayOfMonth) % 7;
                break;
            }
        }
        if (dayOfMonth < ruleDayOfMonth) {
            return -1;
        }
        if (dayOfMonth > ruleDayOfMonth) {
            return 1;
        }
        if (millis < ruleMillis) {
            return -1;
        }
        if (millis > ruleMillis) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public boolean useDaylightTime() {
        return this.useDaylight;
    }
    
    @Override
    public boolean observesDaylightTime() {
        return this.useDaylight;
    }
    
    @Override
    public boolean inDaylightTime(final Date date) {
        final GregorianCalendar gc = new GregorianCalendar(this);
        gc.setTime(date);
        return gc.inDaylightTime();
    }
    
    private void construct(final int _raw, final int _startMonth, final int _startDay, final int _startDayOfWeek, final int _startTime, final int _startTimeMode, final int _endMonth, final int _endDay, final int _endDayOfWeek, final int _endTime, final int _endTimeMode, final int _dst) {
        this.raw = _raw;
        this.startMonth = _startMonth;
        this.startDay = _startDay;
        this.startDayOfWeek = _startDayOfWeek;
        this.startTime = _startTime;
        this.startTimeMode = _startTimeMode;
        this.endMonth = _endMonth;
        this.endDay = _endDay;
        this.endDayOfWeek = _endDayOfWeek;
        this.endTime = _endTime;
        this.endTimeMode = _endTimeMode;
        this.dst = _dst;
        this.startYear = 0;
        this.startMode = 1;
        this.endMode = 1;
        this.decodeRules();
        if (_dst <= 0) {
            throw new IllegalArgumentException();
        }
    }
    
    private void decodeRules() {
        this.decodeStartRule();
        this.decodeEndRule();
    }
    
    private void decodeStartRule() {
        this.useDaylight = (this.startDay != 0 && this.endDay != 0);
        if (this.useDaylight && this.dst == 0) {
            this.dst = 86400000;
        }
        if (this.startDay != 0) {
            if (this.startMonth < 0 || this.startMonth > 11) {
                throw new IllegalArgumentException();
            }
            if (this.startTime < 0 || this.startTime > 86400000 || this.startTimeMode < 0 || this.startTimeMode > 2) {
                throw new IllegalArgumentException();
            }
            if (this.startDayOfWeek == 0) {
                this.startMode = 1;
            }
            else {
                if (this.startDayOfWeek > 0) {
                    this.startMode = 2;
                }
                else {
                    this.startDayOfWeek = -this.startDayOfWeek;
                    if (this.startDay > 0) {
                        this.startMode = 3;
                    }
                    else {
                        this.startDay = -this.startDay;
                        this.startMode = 4;
                    }
                }
                if (this.startDayOfWeek > 7) {
                    throw new IllegalArgumentException();
                }
            }
            if (this.startMode == 2) {
                if (this.startDay < -5 || this.startDay > 5) {
                    throw new IllegalArgumentException();
                }
            }
            else if (this.startDay < 1 || this.startDay > SimpleTimeZone.staticMonthLength[this.startMonth]) {
                throw new IllegalArgumentException();
            }
        }
    }
    
    private void decodeEndRule() {
        this.useDaylight = (this.startDay != 0 && this.endDay != 0);
        if (this.useDaylight && this.dst == 0) {
            this.dst = 86400000;
        }
        if (this.endDay != 0) {
            if (this.endMonth < 0 || this.endMonth > 11) {
                throw new IllegalArgumentException();
            }
            if (this.endTime < 0 || this.endTime > 86400000 || this.endTimeMode < 0 || this.endTimeMode > 2) {
                throw new IllegalArgumentException();
            }
            if (this.endDayOfWeek == 0) {
                this.endMode = 1;
            }
            else {
                if (this.endDayOfWeek > 0) {
                    this.endMode = 2;
                }
                else {
                    this.endDayOfWeek = -this.endDayOfWeek;
                    if (this.endDay > 0) {
                        this.endMode = 3;
                    }
                    else {
                        this.endDay = -this.endDay;
                        this.endMode = 4;
                    }
                }
                if (this.endDayOfWeek > 7) {
                    throw new IllegalArgumentException();
                }
            }
            if (this.endMode == 2) {
                if (this.endDay < -5 || this.endDay > 5) {
                    throw new IllegalArgumentException();
                }
            }
            else if (this.endDay < 1 || this.endDay > SimpleTimeZone.staticMonthLength[this.endMonth]) {
                throw new IllegalArgumentException();
            }
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final SimpleTimeZone that = (SimpleTimeZone)obj;
        return this.raw == that.raw && this.useDaylight == that.useDaylight && this.idEquals(this.getID(), that.getID()) && (!this.useDaylight || (this.dst == that.dst && this.startMode == that.startMode && this.startMonth == that.startMonth && this.startDay == that.startDay && this.startDayOfWeek == that.startDayOfWeek && this.startTime == that.startTime && this.startTimeMode == that.startTimeMode && this.endMode == that.endMode && this.endMonth == that.endMonth && this.endDay == that.endDay && this.endDayOfWeek == that.endDayOfWeek && this.endTime == that.endTime && this.endTimeMode == that.endTimeMode && this.startYear == that.startYear));
    }
    
    private boolean idEquals(final String id1, final String id2) {
        return (id1 == null && id2 == null) || (id1 != null && id2 != null && id1.equals(id2));
    }
    
    @Override
    public int hashCode() {
        int ret = super.hashCode() + this.raw ^ (this.raw >>> 8) + (this.useDaylight ? 0 : 1);
        if (!this.useDaylight) {
            ret += (this.dst ^ (this.dst >>> 10) + this.startMode ^ (this.startMode >>> 11) + this.startMonth ^ (this.startMonth >>> 12) + this.startDay ^ (this.startDay >>> 13) + this.startDayOfWeek ^ (this.startDayOfWeek >>> 14) + this.startTime ^ (this.startTime >>> 15) + this.startTimeMode ^ (this.startTimeMode >>> 16) + this.endMode ^ (this.endMode >>> 17) + this.endMonth ^ (this.endMonth >>> 18) + this.endDay ^ (this.endDay >>> 19) + this.endDayOfWeek ^ (this.endDayOfWeek >>> 20) + this.endTime ^ (this.endTime >>> 21) + this.endTimeMode ^ (this.endTimeMode >>> 22) + this.startYear ^ this.startYear >>> 23);
        }
        return ret;
    }
    
    @Override
    public Object clone() {
        if (this.isFrozen()) {
            return this;
        }
        return this.cloneAsThawed();
    }
    
    @Override
    public boolean hasSameRules(final TimeZone othr) {
        if (this == othr) {
            return true;
        }
        if (!(othr instanceof SimpleTimeZone)) {
            return false;
        }
        final SimpleTimeZone other = (SimpleTimeZone)othr;
        return other != null && this.raw == other.raw && this.useDaylight == other.useDaylight && (!this.useDaylight || (this.dst == other.dst && this.startMode == other.startMode && this.startMonth == other.startMonth && this.startDay == other.startDay && this.startDayOfWeek == other.startDayOfWeek && this.startTime == other.startTime && this.startTimeMode == other.startTimeMode && this.endMode == other.endMode && this.endMonth == other.endMonth && this.endDay == other.endDay && this.endDayOfWeek == other.endDayOfWeek && this.endTime == other.endTime && this.endTimeMode == other.endTimeMode && this.startYear == other.startYear));
    }
    
    @Override
    public TimeZoneTransition getNextTransition(final long base, final boolean inclusive) {
        if (!this.useDaylight) {
            return null;
        }
        this.initTransitionRules();
        final long firstTransitionTime = this.firstTransition.getTime();
        if (base < firstTransitionTime || (inclusive && base == firstTransitionTime)) {
            return this.firstTransition;
        }
        final Date stdDate = this.stdRule.getNextStart(base, this.dstRule.getRawOffset(), this.dstRule.getDSTSavings(), inclusive);
        final Date dstDate = this.dstRule.getNextStart(base, this.stdRule.getRawOffset(), this.stdRule.getDSTSavings(), inclusive);
        if (stdDate != null && (dstDate == null || stdDate.before(dstDate))) {
            return new TimeZoneTransition(stdDate.getTime(), this.dstRule, this.stdRule);
        }
        if (dstDate != null && (stdDate == null || dstDate.before(stdDate))) {
            return new TimeZoneTransition(dstDate.getTime(), this.stdRule, this.dstRule);
        }
        return null;
    }
    
    @Override
    public TimeZoneTransition getPreviousTransition(final long base, final boolean inclusive) {
        if (!this.useDaylight) {
            return null;
        }
        this.initTransitionRules();
        final long firstTransitionTime = this.firstTransition.getTime();
        if (base < firstTransitionTime || (!inclusive && base == firstTransitionTime)) {
            return null;
        }
        final Date stdDate = this.stdRule.getPreviousStart(base, this.dstRule.getRawOffset(), this.dstRule.getDSTSavings(), inclusive);
        final Date dstDate = this.dstRule.getPreviousStart(base, this.stdRule.getRawOffset(), this.stdRule.getDSTSavings(), inclusive);
        if (stdDate != null && (dstDate == null || stdDate.after(dstDate))) {
            return new TimeZoneTransition(stdDate.getTime(), this.dstRule, this.stdRule);
        }
        if (dstDate != null && (stdDate == null || dstDate.after(stdDate))) {
            return new TimeZoneTransition(dstDate.getTime(), this.stdRule, this.dstRule);
        }
        return null;
    }
    
    @Override
    public TimeZoneRule[] getTimeZoneRules() {
        this.initTransitionRules();
        final int size = this.useDaylight ? 3 : 1;
        final TimeZoneRule[] rules = new TimeZoneRule[size];
        rules[0] = this.initialRule;
        if (this.useDaylight) {
            rules[1] = this.stdRule;
            rules[2] = this.dstRule;
        }
        return rules;
    }
    
    private synchronized void initTransitionRules() {
        if (this.transitionRulesInitialized) {
            return;
        }
        if (this.useDaylight) {
            DateTimeRule dtRule = null;
            int timeRuleType = (this.startTimeMode == 1) ? 1 : ((this.startTimeMode == 2) ? 2 : 0);
            switch (this.startMode) {
                case 1: {
                    dtRule = new DateTimeRule(this.startMonth, this.startDay, this.startTime, timeRuleType);
                    break;
                }
                case 2: {
                    dtRule = new DateTimeRule(this.startMonth, this.startDay, this.startDayOfWeek, this.startTime, timeRuleType);
                    break;
                }
                case 3: {
                    dtRule = new DateTimeRule(this.startMonth, this.startDay, this.startDayOfWeek, true, this.startTime, timeRuleType);
                    break;
                }
                case 4: {
                    dtRule = new DateTimeRule(this.startMonth, this.startDay, this.startDayOfWeek, false, this.startTime, timeRuleType);
                    break;
                }
            }
            this.dstRule = new AnnualTimeZoneRule(this.getID() + "(DST)", this.getRawOffset(), this.getDSTSavings(), dtRule, this.startYear, Integer.MAX_VALUE);
            final long firstDstStart = this.dstRule.getFirstStart(this.getRawOffset(), 0).getTime();
            timeRuleType = ((this.endTimeMode == 1) ? 1 : ((this.endTimeMode == 2) ? 2 : 0));
            switch (this.endMode) {
                case 1: {
                    dtRule = new DateTimeRule(this.endMonth, this.endDay, this.endTime, timeRuleType);
                    break;
                }
                case 2: {
                    dtRule = new DateTimeRule(this.endMonth, this.endDay, this.endDayOfWeek, this.endTime, timeRuleType);
                    break;
                }
                case 3: {
                    dtRule = new DateTimeRule(this.endMonth, this.endDay, this.endDayOfWeek, true, this.endTime, timeRuleType);
                    break;
                }
                case 4: {
                    dtRule = new DateTimeRule(this.endMonth, this.endDay, this.endDayOfWeek, false, this.endTime, timeRuleType);
                    break;
                }
            }
            this.stdRule = new AnnualTimeZoneRule(this.getID() + "(STD)", this.getRawOffset(), 0, dtRule, this.startYear, Integer.MAX_VALUE);
            final long firstStdStart = this.stdRule.getFirstStart(this.getRawOffset(), this.dstRule.getDSTSavings()).getTime();
            if (firstStdStart < firstDstStart) {
                this.initialRule = new InitialTimeZoneRule(this.getID() + "(DST)", this.getRawOffset(), this.dstRule.getDSTSavings());
                this.firstTransition = new TimeZoneTransition(firstStdStart, this.initialRule, this.stdRule);
            }
            else {
                this.initialRule = new InitialTimeZoneRule(this.getID() + "(STD)", this.getRawOffset(), 0);
                this.firstTransition = new TimeZoneTransition(firstDstStart, this.initialRule, this.dstRule);
            }
        }
        else {
            this.initialRule = new InitialTimeZoneRule(this.getID(), this.getRawOffset(), 0);
        }
        this.transitionRulesInitialized = true;
    }
    
    @Override
    public boolean isFrozen() {
        return this.isFrozen;
    }
    
    @Override
    public TimeZone freeze() {
        this.isFrozen = true;
        return this;
    }
    
    @Override
    public TimeZone cloneAsThawed() {
        final SimpleTimeZone tz = (SimpleTimeZone)super.cloneAsThawed();
        tz.isFrozen = false;
        return tz;
    }
    
    static {
        staticMonthLength = new byte[] { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    }
}
