// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;
import java.util.Arrays;
import com.ibm.icu.util.TimeZone;
import java.util.Date;
import com.ibm.icu.util.DateTimeRule;
import com.ibm.icu.util.TimeZoneRule;
import com.ibm.icu.util.AnnualTimeZoneRule;
import com.ibm.icu.util.TimeArrayTimeZoneRule;
import com.ibm.icu.util.TimeZoneTransition;
import com.ibm.icu.util.InitialTimeZoneRule;
import com.ibm.icu.util.SimpleTimeZone;
import com.ibm.icu.util.BasicTimeZone;

public class OlsonTimeZone extends BasicTimeZone
{
    static final long serialVersionUID = -6281977362477515376L;
    private int transitionCount;
    private int typeCount;
    private long[] transitionTimes64;
    private int[] typeOffsets;
    private byte[] typeMapData;
    private int finalStartYear;
    private double finalStartMillis;
    private SimpleTimeZone finalZone;
    private volatile String canonicalID;
    private static final String ZONEINFORES = "zoneinfo64";
    private static final boolean DEBUG;
    private static final int SECONDS_PER_DAY = 86400;
    private transient InitialTimeZoneRule initialRule;
    private transient TimeZoneTransition firstTZTransition;
    private transient int firstTZTransitionIdx;
    private transient TimeZoneTransition firstFinalTZTransition;
    private transient TimeArrayTimeZoneRule[] historicRules;
    private transient SimpleTimeZone finalZoneWithStartYear;
    private transient boolean transitionRulesInitialized;
    private static final int currentSerialVersion = 1;
    private int serialVersionOnStream;
    private transient boolean isFrozen;
    
    @Override
    public int getOffset(final int era, final int year, final int month, final int day, final int dayOfWeek, final int milliseconds) {
        if (month < 0 || month > 11) {
            throw new IllegalArgumentException("Month is not in the legal range: " + month);
        }
        return this.getOffset(era, year, month, day, dayOfWeek, milliseconds, Grego.monthLength(year, month));
    }
    
    public int getOffset(final int era, int year, final int month, final int dom, final int dow, final int millis, final int monthLength) {
        if ((era != 1 && era != 0) || month < 0 || month > 11 || dom < 1 || dom > monthLength || dow < 1 || dow > 7 || millis < 0 || millis >= 86400000 || monthLength < 28 || monthLength > 31) {
            throw new IllegalArgumentException();
        }
        if (era == 0) {
            year = -year;
        }
        if (this.finalZone != null && year >= this.finalStartYear) {
            return this.finalZone.getOffset(era, year, month, dom, dow, millis);
        }
        final long time = Grego.fieldsToDay(year, month, dom) * 86400000L + millis;
        final int[] offsets = new int[2];
        this.getHistoricalOffset(time, true, 3, 1, offsets);
        return offsets[0] + offsets[1];
    }
    
    @Override
    public void setRawOffset(final int offsetMillis) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen OlsonTimeZone instance.");
        }
        if (this.getRawOffset() == offsetMillis) {
            return;
        }
        final long current = System.currentTimeMillis();
        if (current < this.finalStartMillis) {
            final SimpleTimeZone stz = new SimpleTimeZone(offsetMillis, this.getID());
            final boolean bDst = this.useDaylightTime();
            if (bDst) {
                TimeZoneRule[] currentRules = this.getSimpleTimeZoneRulesNear(current);
                if (currentRules.length != 3) {
                    final TimeZoneTransition tzt = this.getPreviousTransition(current, false);
                    if (tzt != null) {
                        currentRules = this.getSimpleTimeZoneRulesNear(tzt.getTime() - 1L);
                    }
                }
                if (currentRules.length == 3 && currentRules[1] instanceof AnnualTimeZoneRule && currentRules[2] instanceof AnnualTimeZoneRule) {
                    final AnnualTimeZoneRule r1 = (AnnualTimeZoneRule)currentRules[1];
                    final AnnualTimeZoneRule r2 = (AnnualTimeZoneRule)currentRules[2];
                    final int offset1 = r1.getRawOffset() + r1.getDSTSavings();
                    final int offset2 = r2.getRawOffset() + r2.getDSTSavings();
                    DateTimeRule start;
                    DateTimeRule end;
                    int sav;
                    if (offset1 > offset2) {
                        start = r1.getRule();
                        end = r2.getRule();
                        sav = offset1 - offset2;
                    }
                    else {
                        start = r2.getRule();
                        end = r1.getRule();
                        sav = offset2 - offset1;
                    }
                    stz.setStartRule(start.getRuleMonth(), start.getRuleWeekInMonth(), start.getRuleDayOfWeek(), start.getRuleMillisInDay());
                    stz.setEndRule(end.getRuleMonth(), end.getRuleWeekInMonth(), end.getRuleDayOfWeek(), end.getRuleMillisInDay());
                    stz.setDSTSavings(sav);
                }
                else {
                    stz.setStartRule(0, 1, 0);
                    stz.setEndRule(11, 31, 86399999);
                }
            }
            final int[] fields = Grego.timeToFields(current, null);
            this.finalStartYear = fields[0];
            this.finalStartMillis = (double)Grego.fieldsToDay(fields[0], 0, 1);
            if (bDst) {
                stz.setStartYear(this.finalStartYear);
            }
            this.finalZone = stz;
        }
        else {
            this.finalZone.setRawOffset(offsetMillis);
        }
        this.transitionRulesInitialized = false;
    }
    
    @Override
    public Object clone() {
        if (this.isFrozen()) {
            return this;
        }
        return this.cloneAsThawed();
    }
    
    @Override
    public void getOffset(final long date, final boolean local, final int[] offsets) {
        if (this.finalZone != null && date >= this.finalStartMillis) {
            this.finalZone.getOffset(date, local, offsets);
        }
        else {
            this.getHistoricalOffset(date, local, 4, 12, offsets);
        }
    }
    
    @Override
    @Deprecated
    public void getOffsetFromLocal(final long date, final int nonExistingTimeOpt, final int duplicatedTimeOpt, final int[] offsets) {
        if (this.finalZone != null && date >= this.finalStartMillis) {
            this.finalZone.getOffsetFromLocal(date, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
        }
        else {
            this.getHistoricalOffset(date, true, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
        }
    }
    
    @Override
    public int getRawOffset() {
        final int[] ret = new int[2];
        this.getOffset(System.currentTimeMillis(), false, ret);
        return ret[0];
    }
    
    @Override
    public boolean useDaylightTime() {
        final long current = System.currentTimeMillis();
        if (this.finalZone != null && current >= this.finalStartMillis) {
            return this.finalZone != null && this.finalZone.useDaylightTime();
        }
        final int[] fields = Grego.timeToFields(current, null);
        final long start = Grego.fieldsToDay(fields[0], 0, 1) * 86400L;
        final long limit = Grego.fieldsToDay(fields[0] + 1, 0, 1) * 86400L;
        for (int i = 0; i < this.transitionCount && this.transitionTimes64[i] < limit; ++i) {
            if ((this.transitionTimes64[i] >= start && this.dstOffsetAt(i) != 0) || (this.transitionTimes64[i] > start && i > 0 && this.dstOffsetAt(i - 1) != 0)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean observesDaylightTime() {
        final long current = System.currentTimeMillis();
        if (this.finalZone != null) {
            if (this.finalZone.useDaylightTime()) {
                return true;
            }
            if (current >= this.finalStartMillis) {
                return false;
            }
        }
        final long currentSec = Grego.floorDivide(current, 1000L);
        final int trsIdx = this.transitionCount - 1;
        if (this.dstOffsetAt(trsIdx) != 0) {
            return true;
        }
        while (trsIdx >= 0 && this.transitionTimes64[trsIdx] > currentSec) {
            if (this.dstOffsetAt(trsIdx - 1) != 0) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int getDSTSavings() {
        if (this.finalZone != null) {
            return this.finalZone.getDSTSavings();
        }
        return super.getDSTSavings();
    }
    
    @Override
    public boolean inDaylightTime(final Date date) {
        final int[] temp = new int[2];
        this.getOffset(date.getTime(), false, temp);
        return temp[1] != 0;
    }
    
    @Override
    public boolean hasSameRules(final TimeZone other) {
        if (this == other) {
            return true;
        }
        if (!super.hasSameRules(other)) {
            return false;
        }
        if (!(other instanceof OlsonTimeZone)) {
            return false;
        }
        final OlsonTimeZone o = (OlsonTimeZone)other;
        if (this.finalZone == null) {
            if (o.finalZone != null) {
                return false;
            }
        }
        else if (o.finalZone == null || this.finalStartYear != o.finalStartYear || !this.finalZone.hasSameRules(o.finalZone)) {
            return false;
        }
        return this.transitionCount == o.transitionCount && Arrays.equals(this.transitionTimes64, o.transitionTimes64) && this.typeCount == o.typeCount && Arrays.equals(this.typeMapData, o.typeMapData) && Arrays.equals(this.typeOffsets, o.typeOffsets);
    }
    
    public String getCanonicalID() {
        if (this.canonicalID == null) {
            synchronized (this) {
                if (this.canonicalID == null) {
                    this.canonicalID = TimeZone.getCanonicalID(this.getID());
                    assert this.canonicalID != null;
                    if (this.canonicalID == null) {
                        this.canonicalID = this.getID();
                    }
                }
            }
        }
        return this.canonicalID;
    }
    
    private void constructEmpty() {
        this.transitionCount = 0;
        this.transitionTimes64 = null;
        this.typeMapData = null;
        this.typeCount = 1;
        this.typeOffsets = new int[] { 0, 0 };
        this.finalZone = null;
        this.finalStartYear = Integer.MAX_VALUE;
        this.finalStartMillis = Double.MAX_VALUE;
        this.transitionRulesInitialized = false;
    }
    
    public OlsonTimeZone(final UResourceBundle top, final UResourceBundle res, final String id) {
        super(id);
        this.finalStartYear = Integer.MAX_VALUE;
        this.finalStartMillis = Double.MAX_VALUE;
        this.finalZone = null;
        this.canonicalID = null;
        this.serialVersionOnStream = 1;
        this.isFrozen = false;
        this.construct(top, res);
    }
    
    private void construct(final UResourceBundle top, final UResourceBundle res) {
        if (top == null || res == null) {
            throw new IllegalArgumentException();
        }
        if (OlsonTimeZone.DEBUG) {
            System.out.println("OlsonTimeZone(" + res.getKey() + ")");
        }
        int[] transPost32;
        int[] transPre32;
        int[] trans32 = transPre32 = (transPost32 = null);
        this.transitionCount = 0;
        try {
            final UResourceBundle r = res.get("transPre32");
            transPre32 = r.getIntVector();
            if (transPre32.length % 2 != 0) {
                throw new IllegalArgumentException("Invalid Format");
            }
            this.transitionCount += transPre32.length / 2;
        }
        catch (MissingResourceException ex) {}
        try {
            final UResourceBundle r = res.get("trans");
            trans32 = r.getIntVector();
            this.transitionCount += trans32.length;
        }
        catch (MissingResourceException ex2) {}
        try {
            final UResourceBundle r = res.get("transPost32");
            transPost32 = r.getIntVector();
            if (transPost32.length % 2 != 0) {
                throw new IllegalArgumentException("Invalid Format");
            }
            this.transitionCount += transPost32.length / 2;
        }
        catch (MissingResourceException ex3) {}
        if (this.transitionCount > 0) {
            this.transitionTimes64 = new long[this.transitionCount];
            int idx = 0;
            if (transPre32 != null) {
                for (int i = 0; i < transPre32.length / 2; ++i, ++idx) {
                    this.transitionTimes64[idx] = (((long)transPre32[i * 2] & 0xFFFFFFFFL) << 32 | ((long)transPre32[i * 2 + 1] & 0xFFFFFFFFL));
                }
            }
            if (trans32 != null) {
                for (int i = 0; i < trans32.length; ++i, ++idx) {
                    this.transitionTimes64[idx] = trans32[i];
                }
            }
            if (transPost32 != null) {
                for (int i = 0; i < transPost32.length / 2; ++i, ++idx) {
                    this.transitionTimes64[idx] = (((long)transPost32[i * 2] & 0xFFFFFFFFL) << 32 | ((long)transPost32[i * 2 + 1] & 0xFFFFFFFFL));
                }
            }
        }
        else {
            this.transitionTimes64 = null;
        }
        UResourceBundle r = res.get("typeOffsets");
        this.typeOffsets = r.getIntVector();
        if (this.typeOffsets.length < 2 || this.typeOffsets.length > 32766 || this.typeOffsets.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid Format");
        }
        this.typeCount = this.typeOffsets.length / 2;
        if (this.transitionCount > 0) {
            r = res.get("typeMap");
            this.typeMapData = r.getBinary(null);
            if (this.typeMapData.length != this.transitionCount) {
                throw new IllegalArgumentException("Invalid Format");
            }
        }
        else {
            this.typeMapData = null;
        }
        this.finalZone = null;
        this.finalStartYear = Integer.MAX_VALUE;
        this.finalStartMillis = Double.MAX_VALUE;
        String ruleID = null;
        try {
            ruleID = res.getString("finalRule");
            r = res.get("finalRaw");
            final int ruleRaw = r.getInt() * 1000;
            r = loadRule(top, ruleID);
            final int[] ruleData = r.getIntVector();
            if (ruleData == null || ruleData.length != 11) {
                throw new IllegalArgumentException("Invalid Format");
            }
            this.finalZone = new SimpleTimeZone(ruleRaw, "", ruleData[0], ruleData[1], ruleData[2], ruleData[3] * 1000, ruleData[4], ruleData[5], ruleData[6], ruleData[7], ruleData[8] * 1000, ruleData[9], ruleData[10] * 1000);
            r = res.get("finalYear");
            this.finalStartYear = r.getInt();
            this.finalStartMillis = (double)(Grego.fieldsToDay(this.finalStartYear, 0, 1) * 86400000L);
        }
        catch (MissingResourceException e) {
            if (ruleID != null) {
                throw new IllegalArgumentException("Invalid Format");
            }
        }
    }
    
    public OlsonTimeZone(final String id) {
        super(id);
        this.finalStartYear = Integer.MAX_VALUE;
        this.finalStartMillis = Double.MAX_VALUE;
        this.finalZone = null;
        this.canonicalID = null;
        this.serialVersionOnStream = 1;
        this.isFrozen = false;
        final UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        final UResourceBundle res = ZoneMeta.openOlsonResource(top, id);
        this.construct(top, res);
        if (this.finalZone != null) {
            this.finalZone.setID(id);
        }
    }
    
    @Override
    public void setID(final String id) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen OlsonTimeZone instance.");
        }
        if (this.canonicalID == null) {
            this.canonicalID = TimeZone.getCanonicalID(this.getID());
            assert this.canonicalID != null;
            if (this.canonicalID == null) {
                this.canonicalID = this.getID();
            }
        }
        if (this.finalZone != null) {
            this.finalZone.setID(id);
        }
        super.setID(id);
        this.transitionRulesInitialized = false;
    }
    
    private void getHistoricalOffset(final long date, final boolean local, final int NonExistingTimeOpt, final int DuplicatedTimeOpt, final int[] offsets) {
        if (this.transitionCount != 0) {
            final long sec = Grego.floorDivide(date, 1000L);
            if (!local && sec < this.transitionTimes64[0]) {
                offsets[0] = this.initialRawOffset() * 1000;
                offsets[1] = this.initialDstOffset() * 1000;
            }
            else {
                int transIdx;
                for (transIdx = this.transitionCount - 1; transIdx >= 0; --transIdx) {
                    long transition = this.transitionTimes64[transIdx];
                    if (local) {
                        final int offsetBefore = this.zoneOffsetAt(transIdx - 1);
                        final boolean dstBefore = this.dstOffsetAt(transIdx - 1) != 0;
                        final int offsetAfter = this.zoneOffsetAt(transIdx);
                        final boolean dstAfter = this.dstOffsetAt(transIdx) != 0;
                        final boolean dstToStd = dstBefore && !dstAfter;
                        final boolean stdToDst = !dstBefore && dstAfter;
                        if (offsetAfter - offsetBefore >= 0) {
                            if (((NonExistingTimeOpt & 0x3) == 0x1 && dstToStd) || ((NonExistingTimeOpt & 0x3) == 0x3 && stdToDst)) {
                                transition += offsetBefore;
                            }
                            else if (((NonExistingTimeOpt & 0x3) == 0x1 && stdToDst) || ((NonExistingTimeOpt & 0x3) == 0x3 && dstToStd)) {
                                transition += offsetAfter;
                            }
                            else if ((NonExistingTimeOpt & 0xC) == 0xC) {
                                transition += offsetBefore;
                            }
                            else {
                                transition += offsetAfter;
                            }
                        }
                        else if (((DuplicatedTimeOpt & 0x3) == 0x1 && dstToStd) || ((DuplicatedTimeOpt & 0x3) == 0x3 && stdToDst)) {
                            transition += offsetAfter;
                        }
                        else if (((DuplicatedTimeOpt & 0x3) == 0x1 && stdToDst) || ((DuplicatedTimeOpt & 0x3) == 0x3 && dstToStd)) {
                            transition += offsetBefore;
                        }
                        else if ((DuplicatedTimeOpt & 0xC) == 0x4) {
                            transition += offsetBefore;
                        }
                        else {
                            transition += offsetAfter;
                        }
                    }
                    if (sec >= transition) {
                        break;
                    }
                }
                offsets[0] = this.rawOffsetAt(transIdx) * 1000;
                offsets[1] = this.dstOffsetAt(transIdx) * 1000;
            }
        }
        else {
            offsets[0] = this.initialRawOffset() * 1000;
            offsets[1] = this.initialDstOffset() * 1000;
        }
    }
    
    private int getInt(final byte val) {
        return val & 0xFF;
    }
    
    private int zoneOffsetAt(final int transIdx) {
        final int typeIdx = (transIdx >= 0) ? (this.getInt(this.typeMapData[transIdx]) * 2) : 0;
        return this.typeOffsets[typeIdx] + this.typeOffsets[typeIdx + 1];
    }
    
    private int rawOffsetAt(final int transIdx) {
        final int typeIdx = (transIdx >= 0) ? (this.getInt(this.typeMapData[transIdx]) * 2) : 0;
        return this.typeOffsets[typeIdx];
    }
    
    private int dstOffsetAt(final int transIdx) {
        final int typeIdx = (transIdx >= 0) ? (this.getInt(this.typeMapData[transIdx]) * 2) : 0;
        return this.typeOffsets[typeIdx + 1];
    }
    
    private int initialRawOffset() {
        return this.typeOffsets[0];
    }
    
    private int initialDstOffset() {
        return this.typeOffsets[1];
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(super.toString());
        buf.append('[');
        buf.append("transitionCount=" + this.transitionCount);
        buf.append(",typeCount=" + this.typeCount);
        buf.append(",transitionTimes=");
        if (this.transitionTimes64 != null) {
            buf.append('[');
            for (int i = 0; i < this.transitionTimes64.length; ++i) {
                if (i > 0) {
                    buf.append(',');
                }
                buf.append(Long.toString(this.transitionTimes64[i]));
            }
            buf.append(']');
        }
        else {
            buf.append("null");
        }
        buf.append(",typeOffsets=");
        if (this.typeOffsets != null) {
            buf.append('[');
            for (int i = 0; i < this.typeOffsets.length; ++i) {
                if (i > 0) {
                    buf.append(',');
                }
                buf.append(Integer.toString(this.typeOffsets[i]));
            }
            buf.append(']');
        }
        else {
            buf.append("null");
        }
        buf.append(",typeMapData=");
        if (this.typeMapData != null) {
            buf.append('[');
            for (int i = 0; i < this.typeMapData.length; ++i) {
                if (i > 0) {
                    buf.append(',');
                }
                buf.append(Byte.toString(this.typeMapData[i]));
            }
        }
        else {
            buf.append("null");
        }
        buf.append(",finalStartYear=" + this.finalStartYear);
        buf.append(",finalStartMillis=" + this.finalStartMillis);
        buf.append(",finalZone=" + this.finalZone);
        buf.append(']');
        return buf.toString();
    }
    
    private static UResourceBundle loadRule(final UResourceBundle top, final String ruleid) {
        UResourceBundle r = top.get("Rules");
        r = r.get(ruleid);
        return r;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        final OlsonTimeZone z = (OlsonTimeZone)obj;
        return Utility.arrayEquals(this.typeMapData, z.typeMapData) || (this.finalStartYear == z.finalStartYear && ((this.finalZone == null && z.finalZone == null) || (this.finalZone != null && z.finalZone != null && this.finalZone.equals(z.finalZone) && this.transitionCount == z.transitionCount && this.typeCount == z.typeCount && Utility.arrayEquals(this.transitionTimes64, z.transitionTimes64) && Utility.arrayEquals(this.typeOffsets, z.typeOffsets) && Utility.arrayEquals(this.typeMapData, z.typeMapData))));
    }
    
    @Override
    public int hashCode() {
        int ret = (int)((long)(this.finalStartYear ^ (this.finalStartYear >>> 4) + this.transitionCount ^ (this.transitionCount >>> 6) + this.typeCount) ^ (this.typeCount >>> 8) + Double.doubleToLongBits(this.finalStartMillis) + ((this.finalZone == null) ? 0 : this.finalZone.hashCode()) + super.hashCode());
        if (this.transitionTimes64 != null) {
            for (int i = 0; i < this.transitionTimes64.length; ++i) {
                ret += (int)(this.transitionTimes64[i] ^ this.transitionTimes64[i] >>> 8);
            }
        }
        for (int i = 0; i < this.typeOffsets.length; ++i) {
            ret += (this.typeOffsets[i] ^ this.typeOffsets[i] >>> 8);
        }
        if (this.typeMapData != null) {
            for (int i = 0; i < this.typeMapData.length; ++i) {
                ret += (this.typeMapData[i] & 0xFF);
            }
        }
        return ret;
    }
    
    @Override
    public TimeZoneTransition getNextTransition(final long base, final boolean inclusive) {
        this.initTransitionRules();
        if (this.finalZone != null) {
            if (inclusive && base == this.firstFinalTZTransition.getTime()) {
                return this.firstFinalTZTransition;
            }
            if (base >= this.firstFinalTZTransition.getTime()) {
                if (this.finalZone.useDaylightTime()) {
                    return this.finalZoneWithStartYear.getNextTransition(base, inclusive);
                }
                return null;
            }
        }
        if (this.historicRules == null) {
            return null;
        }
        int ttidx;
        for (ttidx = this.transitionCount - 1; ttidx >= this.firstTZTransitionIdx; --ttidx) {
            final long t = this.transitionTimes64[ttidx] * 1000L;
            if (base > t) {
                break;
            }
            if (!inclusive && base == t) {
                break;
            }
        }
        if (ttidx == this.transitionCount - 1) {
            return this.firstFinalTZTransition;
        }
        if (ttidx < this.firstTZTransitionIdx) {
            return this.firstTZTransition;
        }
        final TimeZoneRule to = this.historicRules[this.getInt(this.typeMapData[ttidx + 1])];
        final TimeZoneRule from = this.historicRules[this.getInt(this.typeMapData[ttidx])];
        final long startTime = this.transitionTimes64[ttidx + 1] * 1000L;
        if (from.getName().equals(to.getName()) && from.getRawOffset() == to.getRawOffset() && from.getDSTSavings() == to.getDSTSavings()) {
            return this.getNextTransition(startTime, false);
        }
        return new TimeZoneTransition(startTime, from, to);
    }
    
    @Override
    public TimeZoneTransition getPreviousTransition(final long base, final boolean inclusive) {
        this.initTransitionRules();
        if (this.finalZone != null) {
            if (inclusive && base == this.firstFinalTZTransition.getTime()) {
                return this.firstFinalTZTransition;
            }
            if (base > this.firstFinalTZTransition.getTime()) {
                if (this.finalZone.useDaylightTime()) {
                    return this.finalZoneWithStartYear.getPreviousTransition(base, inclusive);
                }
                return this.firstFinalTZTransition;
            }
        }
        if (this.historicRules == null) {
            return null;
        }
        int ttidx;
        for (ttidx = this.transitionCount - 1; ttidx >= this.firstTZTransitionIdx; --ttidx) {
            final long t = this.transitionTimes64[ttidx] * 1000L;
            if (base > t) {
                break;
            }
            if (inclusive && base == t) {
                break;
            }
        }
        if (ttidx < this.firstTZTransitionIdx) {
            return null;
        }
        if (ttidx == this.firstTZTransitionIdx) {
            return this.firstTZTransition;
        }
        final TimeZoneRule to = this.historicRules[this.getInt(this.typeMapData[ttidx])];
        final TimeZoneRule from = this.historicRules[this.getInt(this.typeMapData[ttidx - 1])];
        final long startTime = this.transitionTimes64[ttidx] * 1000L;
        if (from.getName().equals(to.getName()) && from.getRawOffset() == to.getRawOffset() && from.getDSTSavings() == to.getDSTSavings()) {
            return this.getPreviousTransition(startTime, false);
        }
        return new TimeZoneTransition(startTime, from, to);
    }
    
    @Override
    public TimeZoneRule[] getTimeZoneRules() {
        this.initTransitionRules();
        int size = 1;
        if (this.historicRules != null) {
            for (int i = 0; i < this.historicRules.length; ++i) {
                if (this.historicRules[i] != null) {
                    ++size;
                }
            }
        }
        if (this.finalZone != null) {
            if (this.finalZone.useDaylightTime()) {
                size += 2;
            }
            else {
                ++size;
            }
        }
        final TimeZoneRule[] rules = new TimeZoneRule[size];
        int idx = 0;
        rules[idx++] = this.initialRule;
        if (this.historicRules != null) {
            for (int j = 0; j < this.historicRules.length; ++j) {
                if (this.historicRules[j] != null) {
                    rules[idx++] = this.historicRules[j];
                }
            }
        }
        if (this.finalZone != null) {
            if (this.finalZone.useDaylightTime()) {
                final TimeZoneRule[] stzr = this.finalZoneWithStartYear.getTimeZoneRules();
                rules[idx++] = stzr[1];
                rules[idx++] = stzr[2];
            }
            else {
                rules[idx++] = new TimeArrayTimeZoneRule(this.getID() + "(STD)", this.finalZone.getRawOffset(), 0, new long[] { (long)this.finalStartMillis }, 2);
            }
        }
        return rules;
    }
    
    private synchronized void initTransitionRules() {
        if (this.transitionRulesInitialized) {
            return;
        }
        this.initialRule = null;
        this.firstTZTransition = null;
        this.firstFinalTZTransition = null;
        this.historicRules = null;
        this.firstTZTransitionIdx = 0;
        this.finalZoneWithStartYear = null;
        final String stdName = this.getID() + "(STD)";
        final String dstName = this.getID() + "(DST)";
        int raw = this.initialRawOffset() * 1000;
        int dst = this.initialDstOffset() * 1000;
        this.initialRule = new InitialTimeZoneRule((dst == 0) ? stdName : dstName, raw, dst);
        if (this.transitionCount > 0) {
            int transitionIdx;
            for (transitionIdx = 0; transitionIdx < this.transitionCount && this.getInt(this.typeMapData[transitionIdx]) == 0; ++transitionIdx) {
                ++this.firstTZTransitionIdx;
            }
            if (transitionIdx != this.transitionCount) {
                final long[] times = new long[this.transitionCount];
                for (int typeIdx = 0; typeIdx < this.typeCount; ++typeIdx) {
                    int nTimes = 0;
                    for (transitionIdx = this.firstTZTransitionIdx; transitionIdx < this.transitionCount; ++transitionIdx) {
                        if (typeIdx == this.getInt(this.typeMapData[transitionIdx])) {
                            final long tt = this.transitionTimes64[transitionIdx] * 1000L;
                            if (tt < this.finalStartMillis) {
                                times[nTimes++] = tt;
                            }
                        }
                    }
                    if (nTimes > 0) {
                        final long[] startTimes = new long[nTimes];
                        System.arraycopy(times, 0, startTimes, 0, nTimes);
                        raw = this.typeOffsets[typeIdx * 2] * 1000;
                        dst = this.typeOffsets[typeIdx * 2 + 1] * 1000;
                        if (this.historicRules == null) {
                            this.historicRules = new TimeArrayTimeZoneRule[this.typeCount];
                        }
                        this.historicRules[typeIdx] = new TimeArrayTimeZoneRule((dst == 0) ? stdName : dstName, raw, dst, startTimes, 2);
                    }
                }
                int typeIdx = this.getInt(this.typeMapData[this.firstTZTransitionIdx]);
                this.firstTZTransition = new TimeZoneTransition(this.transitionTimes64[this.firstTZTransitionIdx] * 1000L, this.initialRule, this.historicRules[typeIdx]);
            }
        }
        if (this.finalZone != null) {
            long startTime = (long)this.finalStartMillis;
            TimeZoneRule firstFinalRule;
            if (this.finalZone.useDaylightTime()) {
                (this.finalZoneWithStartYear = (SimpleTimeZone)this.finalZone.clone()).setStartYear(this.finalStartYear);
                final TimeZoneTransition tzt = this.finalZoneWithStartYear.getNextTransition(startTime, false);
                firstFinalRule = tzt.getTo();
                startTime = tzt.getTime();
            }
            else {
                this.finalZoneWithStartYear = this.finalZone;
                firstFinalRule = new TimeArrayTimeZoneRule(this.finalZone.getID(), this.finalZone.getRawOffset(), 0, new long[] { startTime }, 2);
            }
            TimeZoneRule prevRule = null;
            if (this.transitionCount > 0) {
                prevRule = this.historicRules[this.getInt(this.typeMapData[this.transitionCount - 1])];
            }
            if (prevRule == null) {
                prevRule = this.initialRule;
            }
            this.firstFinalTZTransition = new TimeZoneTransition(startTime, prevRule, firstFinalRule);
        }
        this.transitionRulesInitialized = true;
    }
    
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        if (this.serialVersionOnStream < 1) {
            boolean initialized = false;
            final String tzid = this.getID();
            if (tzid != null) {
                try {
                    final UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
                    final UResourceBundle res = ZoneMeta.openOlsonResource(top, tzid);
                    this.construct(top, res);
                    if (this.finalZone != null) {
                        this.finalZone.setID(tzid);
                    }
                    initialized = true;
                }
                catch (Exception ex) {}
            }
            if (!initialized) {
                this.constructEmpty();
            }
        }
        this.transitionRulesInitialized = false;
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
        final OlsonTimeZone tz = (OlsonTimeZone)super.cloneAsThawed();
        if (this.finalZone != null) {
            this.finalZone.setID(this.getID());
            tz.finalZone = (SimpleTimeZone)this.finalZone.clone();
        }
        tz.isFrozen = false;
        return tz;
    }
    
    static {
        DEBUG = ICUDebug.enabled("olson");
    }
}
