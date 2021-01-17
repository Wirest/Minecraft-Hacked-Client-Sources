// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.BitSet;
import java.util.Date;
import com.ibm.icu.impl.Grego;
import java.util.ArrayList;
import java.util.List;

public class RuleBasedTimeZone extends BasicTimeZone
{
    private static final long serialVersionUID = 7580833058949327935L;
    private final InitialTimeZoneRule initialRule;
    private List<TimeZoneRule> historicRules;
    private AnnualTimeZoneRule[] finalRules;
    private transient List<TimeZoneTransition> historicTransitions;
    private transient boolean upToDate;
    private transient boolean isFrozen;
    
    public RuleBasedTimeZone(final String id, final InitialTimeZoneRule initialRule) {
        super(id);
        this.isFrozen = false;
        this.initialRule = initialRule;
    }
    
    public void addTransitionRule(final TimeZoneRule rule) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen RuleBasedTimeZone instance.");
        }
        if (!rule.isTransitionRule()) {
            throw new IllegalArgumentException("Rule must be a transition rule");
        }
        if (rule instanceof AnnualTimeZoneRule && ((AnnualTimeZoneRule)rule).getEndYear() == Integer.MAX_VALUE) {
            if (this.finalRules == null) {
                (this.finalRules = new AnnualTimeZoneRule[2])[0] = (AnnualTimeZoneRule)rule;
            }
            else {
                if (this.finalRules[1] != null) {
                    throw new IllegalStateException("Too many final rules");
                }
                this.finalRules[1] = (AnnualTimeZoneRule)rule;
            }
        }
        else {
            if (this.historicRules == null) {
                this.historicRules = new ArrayList<TimeZoneRule>();
            }
            this.historicRules.add(rule);
        }
        this.upToDate = false;
    }
    
    @Override
    public int getOffset(final int era, int year, final int month, final int day, final int dayOfWeek, final int milliseconds) {
        if (era == 0) {
            year = 1 - year;
        }
        final long time = Grego.fieldsToDay(year, month, day) * 86400000L + milliseconds;
        final int[] offsets = new int[2];
        this.getOffset(time, true, 3, 1, offsets);
        return offsets[0] + offsets[1];
    }
    
    @Override
    public void getOffset(final long time, final boolean local, final int[] offsets) {
        this.getOffset(time, local, 4, 12, offsets);
    }
    
    @Override
    @Deprecated
    public void getOffsetFromLocal(final long date, final int nonExistingTimeOpt, final int duplicatedTimeOpt, final int[] offsets) {
        this.getOffset(date, true, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
    }
    
    @Override
    public int getRawOffset() {
        final long now = System.currentTimeMillis();
        final int[] offsets = new int[2];
        this.getOffset(now, false, offsets);
        return offsets[0];
    }
    
    @Override
    public boolean inDaylightTime(final Date date) {
        final int[] offsets = new int[2];
        this.getOffset(date.getTime(), false, offsets);
        return offsets[1] != 0;
    }
    
    @Override
    public void setRawOffset(final int offsetMillis) {
        throw new UnsupportedOperationException("setRawOffset in RuleBasedTimeZone is not supported.");
    }
    
    @Override
    public boolean useDaylightTime() {
        final long now = System.currentTimeMillis();
        final int[] offsets = new int[2];
        this.getOffset(now, false, offsets);
        if (offsets[1] != 0) {
            return true;
        }
        final TimeZoneTransition tt = this.getNextTransition(now, false);
        return tt != null && tt.getTo().getDSTSavings() != 0;
    }
    
    @Override
    public boolean observesDaylightTime() {
        long time = System.currentTimeMillis();
        final int[] offsets = new int[2];
        this.getOffset(time, false, offsets);
        if (offsets[1] != 0) {
            return true;
        }
        final BitSet checkFinals = (this.finalRules == null) ? null : new BitSet(this.finalRules.length);
        while (true) {
            final TimeZoneTransition tt = this.getNextTransition(time, false);
            if (tt == null) {
                break;
            }
            final TimeZoneRule toRule = tt.getTo();
            if (toRule.getDSTSavings() != 0) {
                return true;
            }
            if (checkFinals != null) {
                for (int i = 0; i < this.finalRules.length; ++i) {
                    if (this.finalRules[i].equals(toRule)) {
                        checkFinals.set(i);
                    }
                }
                if (checkFinals.cardinality() == this.finalRules.length) {
                    break;
                }
            }
            time = tt.getTime();
        }
        return false;
    }
    
    @Override
    public boolean hasSameRules(final TimeZone other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RuleBasedTimeZone)) {
            return false;
        }
        final RuleBasedTimeZone otherRBTZ = (RuleBasedTimeZone)other;
        if (!this.initialRule.isEquivalentTo(otherRBTZ.initialRule)) {
            return false;
        }
        if (this.finalRules != null && otherRBTZ.finalRules != null) {
            for (int i = 0; i < this.finalRules.length; ++i) {
                if (this.finalRules[i] != null || otherRBTZ.finalRules[i] != null) {
                    if (this.finalRules[i] == null || otherRBTZ.finalRules[i] == null || !this.finalRules[i].isEquivalentTo(otherRBTZ.finalRules[i])) {
                        return false;
                    }
                }
            }
        }
        else if (this.finalRules != null || otherRBTZ.finalRules != null) {
            return false;
        }
        if (this.historicRules != null && otherRBTZ.historicRules != null) {
            if (this.historicRules.size() != otherRBTZ.historicRules.size()) {
                return false;
            }
            for (final TimeZoneRule rule : this.historicRules) {
                boolean foundSameRule = false;
                for (final TimeZoneRule orule : otherRBTZ.historicRules) {
                    if (rule.isEquivalentTo(orule)) {
                        foundSameRule = true;
                        break;
                    }
                }
                if (!foundSameRule) {
                    return false;
                }
            }
        }
        else if (this.historicRules != null || otherRBTZ.historicRules != null) {
            return false;
        }
        return true;
    }
    
    @Override
    public TimeZoneRule[] getTimeZoneRules() {
        int size = 1;
        if (this.historicRules != null) {
            size += this.historicRules.size();
        }
        if (this.finalRules != null) {
            if (this.finalRules[1] != null) {
                size += 2;
            }
            else {
                ++size;
            }
        }
        final TimeZoneRule[] rules = new TimeZoneRule[size];
        rules[0] = this.initialRule;
        int idx = 1;
        if (this.historicRules != null) {
            while (idx < this.historicRules.size() + 1) {
                rules[idx] = this.historicRules.get(idx - 1);
                ++idx;
            }
        }
        if (this.finalRules != null) {
            rules[idx++] = this.finalRules[0];
            if (this.finalRules[1] != null) {
                rules[idx] = this.finalRules[1];
            }
        }
        return rules;
    }
    
    @Override
    public TimeZoneTransition getNextTransition(final long base, final boolean inclusive) {
        this.complete();
        if (this.historicTransitions == null) {
            return null;
        }
        boolean isFinal = false;
        TimeZoneTransition result = null;
        TimeZoneTransition tzt = this.historicTransitions.get(0);
        long tt = tzt.getTime();
        if (tt > base || (inclusive && tt == base)) {
            result = tzt;
        }
        else {
            int idx = this.historicTransitions.size() - 1;
            tzt = this.historicTransitions.get(idx);
            tt = tzt.getTime();
            if (inclusive && tt == base) {
                result = tzt;
            }
            else if (tt <= base) {
                if (this.finalRules == null) {
                    return null;
                }
                final Date start0 = this.finalRules[0].getNextStart(base, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), inclusive);
                final Date start2 = this.finalRules[1].getNextStart(base, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), inclusive);
                if (start2.after(start0)) {
                    tzt = new TimeZoneTransition(start0.getTime(), this.finalRules[1], this.finalRules[0]);
                }
                else {
                    tzt = new TimeZoneTransition(start2.getTime(), this.finalRules[0], this.finalRules[1]);
                }
                result = tzt;
                isFinal = true;
            }
            else {
                --idx;
                TimeZoneTransition prev = tzt;
                while (idx > 0) {
                    tzt = this.historicTransitions.get(idx);
                    tt = tzt.getTime();
                    if (tt < base) {
                        break;
                    }
                    if (!inclusive && tt == base) {
                        break;
                    }
                    --idx;
                    prev = tzt;
                }
                result = prev;
            }
        }
        if (result != null) {
            final TimeZoneRule from = result.getFrom();
            final TimeZoneRule to = result.getTo();
            if (from.getRawOffset() == to.getRawOffset() && from.getDSTSavings() == to.getDSTSavings()) {
                if (isFinal) {
                    return null;
                }
                result = this.getNextTransition(result.getTime(), false);
            }
        }
        return result;
    }
    
    @Override
    public TimeZoneTransition getPreviousTransition(final long base, final boolean inclusive) {
        this.complete();
        if (this.historicTransitions == null) {
            return null;
        }
        TimeZoneTransition result = null;
        TimeZoneTransition tzt = this.historicTransitions.get(0);
        long tt = tzt.getTime();
        if (inclusive && tt == base) {
            result = tzt;
        }
        else {
            if (tt >= base) {
                return null;
            }
            int idx = this.historicTransitions.size() - 1;
            tzt = this.historicTransitions.get(idx);
            tt = tzt.getTime();
            if (inclusive && tt == base) {
                result = tzt;
            }
            else if (tt < base) {
                if (this.finalRules != null) {
                    final Date start0 = this.finalRules[0].getPreviousStart(base, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), inclusive);
                    final Date start2 = this.finalRules[1].getPreviousStart(base, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), inclusive);
                    if (start2.before(start0)) {
                        tzt = new TimeZoneTransition(start0.getTime(), this.finalRules[1], this.finalRules[0]);
                    }
                    else {
                        tzt = new TimeZoneTransition(start2.getTime(), this.finalRules[0], this.finalRules[1]);
                    }
                }
                result = tzt;
            }
            else {
                --idx;
                while (idx >= 0) {
                    tzt = this.historicTransitions.get(idx);
                    tt = tzt.getTime();
                    if (tt < base) {
                        break;
                    }
                    if (inclusive && tt == base) {
                        break;
                    }
                    --idx;
                }
                result = tzt;
            }
        }
        if (result != null) {
            final TimeZoneRule from = result.getFrom();
            final TimeZoneRule to = result.getTo();
            if (from.getRawOffset() == to.getRawOffset() && from.getDSTSavings() == to.getDSTSavings()) {
                result = this.getPreviousTransition(result.getTime(), false);
            }
        }
        return result;
    }
    
    @Override
    public Object clone() {
        if (this.isFrozen()) {
            return this;
        }
        return this.cloneAsThawed();
    }
    
    private void complete() {
        if (this.upToDate) {
            return;
        }
        if (this.finalRules != null && this.finalRules[1] == null) {
            throw new IllegalStateException("Incomplete final rules");
        }
        if (this.historicRules != null || this.finalRules != null) {
            TimeZoneRule curRule = this.initialRule;
            long lastTransitionTime = -184303902528000000L;
            if (this.historicRules != null) {
                final BitSet done = new BitSet(this.historicRules.size());
                while (true) {
                    final int curStdOffset = curRule.getRawOffset();
                    final int curDstSavings = curRule.getDSTSavings();
                    long nextTransitionTime = 183882168921600000L;
                    TimeZoneRule nextRule = null;
                    for (int i = 0; i < this.historicRules.size(); ++i) {
                        if (!done.get(i)) {
                            final TimeZoneRule r = this.historicRules.get(i);
                            final Date d = r.getNextStart(lastTransitionTime, curStdOffset, curDstSavings, false);
                            if (d == null) {
                                done.set(i);
                            }
                            else if (r != curRule) {
                                if (!r.getName().equals(curRule.getName()) || r.getRawOffset() != curRule.getRawOffset() || r.getDSTSavings() != curRule.getDSTSavings()) {
                                    final long tt = d.getTime();
                                    if (tt < nextTransitionTime) {
                                        nextTransitionTime = tt;
                                        nextRule = r;
                                    }
                                }
                            }
                        }
                    }
                    if (nextRule == null) {
                        boolean bDoneAll = true;
                        for (int j = 0; j < this.historicRules.size(); ++j) {
                            if (!done.get(j)) {
                                bDoneAll = false;
                                break;
                            }
                        }
                        if (bDoneAll) {
                            break;
                        }
                    }
                    if (this.finalRules != null) {
                        for (int i = 0; i < 2; ++i) {
                            if (this.finalRules[i] != curRule) {
                                final Date d = this.finalRules[i].getNextStart(lastTransitionTime, curStdOffset, curDstSavings, false);
                                if (d != null) {
                                    final long tt = d.getTime();
                                    if (tt < nextTransitionTime) {
                                        nextTransitionTime = tt;
                                        nextRule = this.finalRules[i];
                                    }
                                }
                            }
                        }
                    }
                    if (nextRule == null) {
                        break;
                    }
                    if (this.historicTransitions == null) {
                        this.historicTransitions = new ArrayList<TimeZoneTransition>();
                    }
                    this.historicTransitions.add(new TimeZoneTransition(nextTransitionTime, curRule, nextRule));
                    lastTransitionTime = nextTransitionTime;
                    curRule = nextRule;
                }
            }
            if (this.finalRules != null) {
                if (this.historicTransitions == null) {
                    this.historicTransitions = new ArrayList<TimeZoneTransition>();
                }
                Date d2 = this.finalRules[0].getNextStart(lastTransitionTime, curRule.getRawOffset(), curRule.getDSTSavings(), false);
                Date d3 = this.finalRules[1].getNextStart(lastTransitionTime, curRule.getRawOffset(), curRule.getDSTSavings(), false);
                if (d3.after(d2)) {
                    this.historicTransitions.add(new TimeZoneTransition(d2.getTime(), curRule, this.finalRules[0]));
                    d3 = this.finalRules[1].getNextStart(d2.getTime(), this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), false);
                    this.historicTransitions.add(new TimeZoneTransition(d3.getTime(), this.finalRules[0], this.finalRules[1]));
                }
                else {
                    this.historicTransitions.add(new TimeZoneTransition(d3.getTime(), curRule, this.finalRules[1]));
                    d2 = this.finalRules[0].getNextStart(d3.getTime(), this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), false);
                    this.historicTransitions.add(new TimeZoneTransition(d2.getTime(), this.finalRules[1], this.finalRules[0]));
                }
            }
        }
        this.upToDate = true;
    }
    
    private void getOffset(final long time, final boolean local, final int NonExistingTimeOpt, final int DuplicatedTimeOpt, final int[] offsets) {
        this.complete();
        TimeZoneRule rule = null;
        if (this.historicTransitions == null) {
            rule = this.initialRule;
        }
        else {
            final long tstart = getTransitionTime(this.historicTransitions.get(0), local, NonExistingTimeOpt, DuplicatedTimeOpt);
            if (time < tstart) {
                rule = this.initialRule;
            }
            else {
                int idx = this.historicTransitions.size() - 1;
                final long tend = getTransitionTime(this.historicTransitions.get(idx), local, NonExistingTimeOpt, DuplicatedTimeOpt);
                if (time > tend) {
                    if (this.finalRules != null) {
                        rule = this.findRuleInFinal(time, local, NonExistingTimeOpt, DuplicatedTimeOpt);
                    }
                    if (rule == null) {
                        rule = this.historicTransitions.get(idx).getTo();
                    }
                }
                else {
                    while (idx >= 0 && time < getTransitionTime(this.historicTransitions.get(idx), local, NonExistingTimeOpt, DuplicatedTimeOpt)) {
                        --idx;
                    }
                    rule = this.historicTransitions.get(idx).getTo();
                }
            }
        }
        offsets[0] = rule.getRawOffset();
        offsets[1] = rule.getDSTSavings();
    }
    
    private TimeZoneRule findRuleInFinal(final long time, final boolean local, final int NonExistingTimeOpt, final int DuplicatedTimeOpt) {
        if (this.finalRules == null || this.finalRules.length != 2 || this.finalRules[0] == null || this.finalRules[1] == null) {
            return null;
        }
        long base = time;
        if (local) {
            final int localDelta = getLocalDelta(this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), NonExistingTimeOpt, DuplicatedTimeOpt);
            base -= localDelta;
        }
        final Date start0 = this.finalRules[0].getPreviousStart(base, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), true);
        base = time;
        if (local) {
            final int localDelta = getLocalDelta(this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), NonExistingTimeOpt, DuplicatedTimeOpt);
            base -= localDelta;
        }
        final Date start2 = this.finalRules[1].getPreviousStart(base, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), true);
        if (start0 != null && start2 != null) {
            return start0.after(start2) ? this.finalRules[0] : this.finalRules[1];
        }
        if (start0 != null) {
            return this.finalRules[0];
        }
        if (start2 != null) {
            return this.finalRules[1];
        }
        return null;
    }
    
    private static long getTransitionTime(final TimeZoneTransition tzt, final boolean local, final int NonExistingTimeOpt, final int DuplicatedTimeOpt) {
        long time = tzt.getTime();
        if (local) {
            time += getLocalDelta(tzt.getFrom().getRawOffset(), tzt.getFrom().getDSTSavings(), tzt.getTo().getRawOffset(), tzt.getTo().getDSTSavings(), NonExistingTimeOpt, DuplicatedTimeOpt);
        }
        return time;
    }
    
    private static int getLocalDelta(final int rawBefore, final int dstBefore, final int rawAfter, final int dstAfter, final int NonExistingTimeOpt, final int DuplicatedTimeOpt) {
        int delta = 0;
        final int offsetBefore = rawBefore + dstBefore;
        final int offsetAfter = rawAfter + dstAfter;
        final boolean dstToStd = dstBefore != 0 && dstAfter == 0;
        final boolean stdToDst = dstBefore == 0 && dstAfter != 0;
        if (offsetAfter - offsetBefore >= 0) {
            if (((NonExistingTimeOpt & 0x3) == 0x1 && dstToStd) || ((NonExistingTimeOpt & 0x3) == 0x3 && stdToDst)) {
                delta = offsetBefore;
            }
            else if (((NonExistingTimeOpt & 0x3) == 0x1 && stdToDst) || ((NonExistingTimeOpt & 0x3) == 0x3 && dstToStd)) {
                delta = offsetAfter;
            }
            else if ((NonExistingTimeOpt & 0xC) == 0xC) {
                delta = offsetBefore;
            }
            else {
                delta = offsetAfter;
            }
        }
        else if (((DuplicatedTimeOpt & 0x3) == 0x1 && dstToStd) || ((DuplicatedTimeOpt & 0x3) == 0x3 && stdToDst)) {
            delta = offsetAfter;
        }
        else if (((DuplicatedTimeOpt & 0x3) == 0x1 && stdToDst) || ((DuplicatedTimeOpt & 0x3) == 0x3 && dstToStd)) {
            delta = offsetBefore;
        }
        else if ((DuplicatedTimeOpt & 0xC) == 0x4) {
            delta = offsetBefore;
        }
        else {
            delta = offsetAfter;
        }
        return delta;
    }
    
    @Override
    public boolean isFrozen() {
        return this.isFrozen;
    }
    
    @Override
    public TimeZone freeze() {
        this.complete();
        this.isFrozen = true;
        return this;
    }
    
    @Override
    public TimeZone cloneAsThawed() {
        final RuleBasedTimeZone tz = (RuleBasedTimeZone)super.cloneAsThawed();
        if (this.historicRules != null) {
            tz.historicRules = new ArrayList<TimeZoneRule>(this.historicRules);
        }
        if (this.finalRules != null) {
            tz.finalRules = this.finalRules.clone();
        }
        tz.isFrozen = false;
        return tz;
    }
}
