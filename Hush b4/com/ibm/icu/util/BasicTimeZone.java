// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import java.util.List;
import com.ibm.icu.impl.Grego;
import java.util.LinkedList;
import java.util.BitSet;

public abstract class BasicTimeZone extends TimeZone
{
    private static final long serialVersionUID = -3204278532246180932L;
    private static final long MILLIS_PER_YEAR = 31536000000L;
    @Deprecated
    public static final int LOCAL_STD = 1;
    @Deprecated
    public static final int LOCAL_DST = 3;
    @Deprecated
    public static final int LOCAL_FORMER = 4;
    @Deprecated
    public static final int LOCAL_LATTER = 12;
    @Deprecated
    protected static final int STD_DST_MASK = 3;
    @Deprecated
    protected static final int FORMER_LATTER_MASK = 12;
    
    public abstract TimeZoneTransition getNextTransition(final long p0, final boolean p1);
    
    public abstract TimeZoneTransition getPreviousTransition(final long p0, final boolean p1);
    
    public boolean hasEquivalentTransitions(final TimeZone tz, final long start, final long end) {
        return this.hasEquivalentTransitions(tz, start, end, false);
    }
    
    public boolean hasEquivalentTransitions(final TimeZone tz, final long start, final long end, final boolean ignoreDstAmount) {
        if (this == tz) {
            return true;
        }
        if (!(tz instanceof BasicTimeZone)) {
            return false;
        }
        final int[] offsets1 = new int[2];
        final int[] offsets2 = new int[2];
        this.getOffset(start, false, offsets1);
        tz.getOffset(start, false, offsets2);
        if (ignoreDstAmount) {
            if (offsets1[0] + offsets1[1] != offsets2[0] + offsets2[1] || (offsets1[1] != 0 && offsets2[1] == 0) || (offsets1[1] == 0 && offsets2[1] != 0)) {
                return false;
            }
        }
        else if (offsets1[0] != offsets2[0] || offsets1[1] != offsets2[1]) {
            return false;
        }
        long time = start;
        while (true) {
            TimeZoneTransition tr1 = this.getNextTransition(time, false);
            TimeZoneTransition tr2 = ((BasicTimeZone)tz).getNextTransition(time, false);
            if (ignoreDstAmount) {
                while (tr1 != null && tr1.getTime() <= end && tr1.getFrom().getRawOffset() + tr1.getFrom().getDSTSavings() == tr1.getTo().getRawOffset() + tr1.getTo().getDSTSavings() && tr1.getFrom().getDSTSavings() != 0 && tr1.getTo().getDSTSavings() != 0) {
                    tr1 = this.getNextTransition(tr1.getTime(), false);
                }
                while (tr2 != null && tr2.getTime() <= end && tr2.getFrom().getRawOffset() + tr2.getFrom().getDSTSavings() == tr2.getTo().getRawOffset() + tr2.getTo().getDSTSavings() && tr2.getFrom().getDSTSavings() != 0 && tr2.getTo().getDSTSavings() != 0) {
                    tr2 = ((BasicTimeZone)tz).getNextTransition(tr2.getTime(), false);
                }
            }
            boolean inRange1 = false;
            boolean inRange2 = false;
            if (tr1 != null && tr1.getTime() <= end) {
                inRange1 = true;
            }
            if (tr2 != null && tr2.getTime() <= end) {
                inRange2 = true;
            }
            if (!inRange1 && !inRange2) {
                return true;
            }
            if (!inRange1 || !inRange2) {
                return false;
            }
            if (tr1.getTime() != tr2.getTime()) {
                return false;
            }
            if (ignoreDstAmount) {
                if (tr1.getTo().getRawOffset() + tr1.getTo().getDSTSavings() != tr2.getTo().getRawOffset() + tr2.getTo().getDSTSavings() || (tr1.getTo().getDSTSavings() != 0 && tr2.getTo().getDSTSavings() == 0) || (tr1.getTo().getDSTSavings() == 0 && tr2.getTo().getDSTSavings() != 0)) {
                    return false;
                }
            }
            else if (tr1.getTo().getRawOffset() != tr2.getTo().getRawOffset() || tr1.getTo().getDSTSavings() != tr2.getTo().getDSTSavings()) {
                return false;
            }
            time = tr1.getTime();
        }
    }
    
    public abstract TimeZoneRule[] getTimeZoneRules();
    
    public TimeZoneRule[] getTimeZoneRules(final long start) {
        final TimeZoneRule[] all = this.getTimeZoneRules();
        TimeZoneTransition tzt = this.getPreviousTransition(start, true);
        if (tzt == null) {
            return all;
        }
        final BitSet isProcessed = new BitSet(all.length);
        final List<TimeZoneRule> filteredRules = new LinkedList<TimeZoneRule>();
        final TimeZoneRule initial = new InitialTimeZoneRule(tzt.getTo().getName(), tzt.getTo().getRawOffset(), tzt.getTo().getDSTSavings());
        filteredRules.add(initial);
        isProcessed.set(0);
        for (int i = 1; i < all.length; ++i) {
            final Date d = all[i].getNextStart(start, initial.getRawOffset(), initial.getDSTSavings(), false);
            if (d == null) {
                isProcessed.set(i);
            }
        }
        long time = start;
        boolean bFinalStd = false;
        boolean bFinalDst = false;
        while (!bFinalStd || !bFinalDst) {
            tzt = this.getNextTransition(time, false);
            if (tzt == null) {
                break;
            }
            time = tzt.getTime();
            TimeZoneRule toRule;
            int ruleIdx;
            for (toRule = tzt.getTo(), ruleIdx = 1; ruleIdx < all.length && !all[ruleIdx].equals(toRule); ++ruleIdx) {}
            if (ruleIdx >= all.length) {
                throw new IllegalStateException("The rule was not found");
            }
            if (isProcessed.get(ruleIdx)) {
                continue;
            }
            if (toRule instanceof TimeArrayTimeZoneRule) {
                final TimeArrayTimeZoneRule tar = (TimeArrayTimeZoneRule)toRule;
                long t = start;
                while (true) {
                    tzt = this.getNextTransition(t, false);
                    if (tzt == null) {
                        break;
                    }
                    if (tzt.getTo().equals(tar)) {
                        break;
                    }
                    t = tzt.getTime();
                }
                if (tzt != null) {
                    final Date firstStart = tar.getFirstStart(tzt.getFrom().getRawOffset(), tzt.getFrom().getDSTSavings());
                    if (firstStart.getTime() > start) {
                        filteredRules.add(tar);
                    }
                    else {
                        final long[] times = tar.getStartTimes();
                        final int timeType = tar.getTimeType();
                        int idx;
                        for (idx = 0; idx < times.length; ++idx) {
                            t = times[idx];
                            if (timeType == 1) {
                                t -= tzt.getFrom().getRawOffset();
                            }
                            if (timeType == 0) {
                                t -= tzt.getFrom().getDSTSavings();
                            }
                            if (t > start) {
                                break;
                            }
                        }
                        final int asize = times.length - idx;
                        if (asize > 0) {
                            final long[] newtimes = new long[asize];
                            System.arraycopy(times, idx, newtimes, 0, asize);
                            final TimeArrayTimeZoneRule newtar = new TimeArrayTimeZoneRule(tar.getName(), tar.getRawOffset(), tar.getDSTSavings(), newtimes, tar.getTimeType());
                            filteredRules.add(newtar);
                        }
                    }
                }
            }
            else if (toRule instanceof AnnualTimeZoneRule) {
                final AnnualTimeZoneRule ar = (AnnualTimeZoneRule)toRule;
                final Date firstStart2 = ar.getFirstStart(tzt.getFrom().getRawOffset(), tzt.getFrom().getDSTSavings());
                if (firstStart2.getTime() == tzt.getTime()) {
                    filteredRules.add(ar);
                }
                else {
                    final int[] dfields = new int[6];
                    Grego.timeToFields(tzt.getTime(), dfields);
                    final AnnualTimeZoneRule newar = new AnnualTimeZoneRule(ar.getName(), ar.getRawOffset(), ar.getDSTSavings(), ar.getRule(), dfields[0], ar.getEndYear());
                    filteredRules.add(newar);
                }
                if (ar.getEndYear() == Integer.MAX_VALUE) {
                    if (ar.getDSTSavings() == 0) {
                        bFinalStd = true;
                    }
                    else {
                        bFinalDst = true;
                    }
                }
            }
            isProcessed.set(ruleIdx);
        }
        final TimeZoneRule[] rules = filteredRules.toArray(new TimeZoneRule[filteredRules.size()]);
        return rules;
    }
    
    public TimeZoneRule[] getSimpleTimeZoneRulesNear(final long date) {
        AnnualTimeZoneRule[] annualRules = null;
        TimeZoneRule initialRule = null;
        TimeZoneTransition tr = this.getNextTransition(date, false);
        if (tr != null) {
            String initialName = tr.getFrom().getName();
            int initialRaw = tr.getFrom().getRawOffset();
            int initialDst = tr.getFrom().getDSTSavings();
            final long nextTransitionTime = tr.getTime();
            if (((tr.getFrom().getDSTSavings() == 0 && tr.getTo().getDSTSavings() != 0) || (tr.getFrom().getDSTSavings() != 0 && tr.getTo().getDSTSavings() == 0)) && date + 31536000000L > nextTransitionTime) {
                annualRules = new AnnualTimeZoneRule[2];
                int[] dtfields = Grego.timeToFields(nextTransitionTime + tr.getFrom().getRawOffset() + tr.getFrom().getDSTSavings(), null);
                int weekInMonth = Grego.getDayOfWeekInMonth(dtfields[0], dtfields[1], dtfields[2]);
                DateTimeRule dtr = new DateTimeRule(dtfields[1], weekInMonth, dtfields[3], dtfields[5], 0);
                AnnualTimeZoneRule secondRule = null;
                annualRules[0] = new AnnualTimeZoneRule(tr.getTo().getName(), initialRaw, tr.getTo().getDSTSavings(), dtr, dtfields[0], Integer.MAX_VALUE);
                if (tr.getTo().getRawOffset() == initialRaw) {
                    tr = this.getNextTransition(nextTransitionTime, false);
                    if (tr != null && ((tr.getFrom().getDSTSavings() == 0 && tr.getTo().getDSTSavings() != 0) || (tr.getFrom().getDSTSavings() != 0 && tr.getTo().getDSTSavings() == 0)) && nextTransitionTime + 31536000000L > tr.getTime()) {
                        dtfields = Grego.timeToFields(tr.getTime() + tr.getFrom().getRawOffset() + tr.getFrom().getDSTSavings(), dtfields);
                        weekInMonth = Grego.getDayOfWeekInMonth(dtfields[0], dtfields[1], dtfields[2]);
                        dtr = new DateTimeRule(dtfields[1], weekInMonth, dtfields[3], dtfields[5], 0);
                        secondRule = new AnnualTimeZoneRule(tr.getTo().getName(), tr.getTo().getRawOffset(), tr.getTo().getDSTSavings(), dtr, dtfields[0] - 1, Integer.MAX_VALUE);
                        final Date d = secondRule.getPreviousStart(date, tr.getFrom().getRawOffset(), tr.getFrom().getDSTSavings(), true);
                        if (d != null && d.getTime() <= date && initialRaw == tr.getTo().getRawOffset() && initialDst == tr.getTo().getDSTSavings()) {
                            annualRules[1] = secondRule;
                        }
                    }
                }
                if (annualRules[1] == null) {
                    tr = this.getPreviousTransition(date, true);
                    if (tr != null && ((tr.getFrom().getDSTSavings() == 0 && tr.getTo().getDSTSavings() != 0) || (tr.getFrom().getDSTSavings() != 0 && tr.getTo().getDSTSavings() == 0))) {
                        dtfields = Grego.timeToFields(tr.getTime() + tr.getFrom().getRawOffset() + tr.getFrom().getDSTSavings(), dtfields);
                        weekInMonth = Grego.getDayOfWeekInMonth(dtfields[0], dtfields[1], dtfields[2]);
                        dtr = new DateTimeRule(dtfields[1], weekInMonth, dtfields[3], dtfields[5], 0);
                        secondRule = new AnnualTimeZoneRule(tr.getTo().getName(), initialRaw, initialDst, dtr, annualRules[0].getStartYear() - 1, Integer.MAX_VALUE);
                        final Date d = secondRule.getNextStart(date, tr.getFrom().getRawOffset(), tr.getFrom().getDSTSavings(), false);
                        if (d.getTime() > nextTransitionTime) {
                            annualRules[1] = secondRule;
                        }
                    }
                }
                if (annualRules[1] == null) {
                    annualRules = null;
                }
                else {
                    initialName = annualRules[0].getName();
                    initialRaw = annualRules[0].getRawOffset();
                    initialDst = annualRules[0].getDSTSavings();
                }
            }
            initialRule = new InitialTimeZoneRule(initialName, initialRaw, initialDst);
        }
        else {
            tr = this.getPreviousTransition(date, true);
            if (tr != null) {
                initialRule = new InitialTimeZoneRule(tr.getTo().getName(), tr.getTo().getRawOffset(), tr.getTo().getDSTSavings());
            }
            else {
                final int[] offsets = new int[2];
                this.getOffset(date, false, offsets);
                initialRule = new InitialTimeZoneRule(this.getID(), offsets[0], offsets[1]);
            }
        }
        TimeZoneRule[] result = null;
        if (annualRules == null) {
            result = new TimeZoneRule[] { initialRule };
        }
        else {
            result = new TimeZoneRule[] { initialRule, annualRules[0], annualRules[1] };
        }
        return result;
    }
    
    @Deprecated
    public void getOffsetFromLocal(final long date, final int nonExistingTimeOpt, final int duplicatedTimeOpt, final int[] offsets) {
        throw new IllegalStateException("Not implemented");
    }
    
    protected BasicTimeZone() {
    }
    
    @Deprecated
    protected BasicTimeZone(final String ID) {
        super(ID);
    }
}
