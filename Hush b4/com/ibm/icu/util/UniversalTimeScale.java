// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import com.ibm.icu.math.BigDecimal;

public final class UniversalTimeScale
{
    public static final int JAVA_TIME = 0;
    public static final int UNIX_TIME = 1;
    public static final int ICU4C_TIME = 2;
    public static final int WINDOWS_FILE_TIME = 3;
    public static final int DOTNET_DATE_TIME = 4;
    public static final int MAC_OLD_TIME = 5;
    public static final int MAC_TIME = 6;
    public static final int EXCEL_TIME = 7;
    public static final int DB2_TIME = 8;
    public static final int UNIX_MICROSECONDS_TIME = 9;
    public static final int MAX_SCALE = 10;
    public static final int UNITS_VALUE = 0;
    public static final int EPOCH_OFFSET_VALUE = 1;
    public static final int FROM_MIN_VALUE = 2;
    public static final int FROM_MAX_VALUE = 3;
    public static final int TO_MIN_VALUE = 4;
    public static final int TO_MAX_VALUE = 5;
    public static final int EPOCH_OFFSET_PLUS_1_VALUE = 6;
    @Deprecated
    public static final int EPOCH_OFFSET_MINUS_1_VALUE = 7;
    @Deprecated
    public static final int UNITS_ROUND_VALUE = 8;
    @Deprecated
    public static final int MIN_ROUND_VALUE = 9;
    @Deprecated
    public static final int MAX_ROUND_VALUE = 10;
    @Deprecated
    public static final int MAX_SCALE_VALUE = 11;
    private static final long ticks = 1L;
    private static final long microseconds = 10L;
    private static final long milliseconds = 10000L;
    private static final long seconds = 10000000L;
    private static final long minutes = 600000000L;
    private static final long hours = 36000000000L;
    private static final long days = 864000000000L;
    private static final TimeScaleData[] timeScaleTable;
    
    private UniversalTimeScale() {
    }
    
    public static long from(final long otherTime, final int timeScale) {
        final TimeScaleData data = fromRangeCheck(otherTime, timeScale);
        return (otherTime + data.epochOffset) * data.units;
    }
    
    public static BigDecimal bigDecimalFrom(final double otherTime, final int timeScale) {
        final TimeScaleData data = getTimeScaleData(timeScale);
        final BigDecimal other = new BigDecimal(String.valueOf(otherTime));
        final BigDecimal units = new BigDecimal(data.units);
        final BigDecimal epochOffset = new BigDecimal(data.epochOffset);
        return other.add(epochOffset).multiply(units);
    }
    
    public static BigDecimal bigDecimalFrom(final long otherTime, final int timeScale) {
        final TimeScaleData data = getTimeScaleData(timeScale);
        final BigDecimal other = new BigDecimal(otherTime);
        final BigDecimal units = new BigDecimal(data.units);
        final BigDecimal epochOffset = new BigDecimal(data.epochOffset);
        return other.add(epochOffset).multiply(units);
    }
    
    public static BigDecimal bigDecimalFrom(final BigDecimal otherTime, final int timeScale) {
        final TimeScaleData data = getTimeScaleData(timeScale);
        final BigDecimal units = new BigDecimal(data.units);
        final BigDecimal epochOffset = new BigDecimal(data.epochOffset);
        return otherTime.add(epochOffset).multiply(units);
    }
    
    public static long toLong(final long universalTime, final int timeScale) {
        final TimeScaleData data = toRangeCheck(universalTime, timeScale);
        if (universalTime < 0L) {
            if (universalTime < data.minRound) {
                return (universalTime + data.unitsRound) / data.units - data.epochOffsetP1;
            }
            return (universalTime - data.unitsRound) / data.units - data.epochOffset;
        }
        else {
            if (universalTime > data.maxRound) {
                return (universalTime - data.unitsRound) / data.units - data.epochOffsetM1;
            }
            return (universalTime + data.unitsRound) / data.units - data.epochOffset;
        }
    }
    
    public static BigDecimal toBigDecimal(final long universalTime, final int timeScale) {
        final TimeScaleData data = getTimeScaleData(timeScale);
        final BigDecimal universal = new BigDecimal(universalTime);
        final BigDecimal units = new BigDecimal(data.units);
        final BigDecimal epochOffset = new BigDecimal(data.epochOffset);
        return universal.divide(units, 4).subtract(epochOffset);
    }
    
    public static BigDecimal toBigDecimal(final BigDecimal universalTime, final int timeScale) {
        final TimeScaleData data = getTimeScaleData(timeScale);
        final BigDecimal units = new BigDecimal(data.units);
        final BigDecimal epochOffset = new BigDecimal(data.epochOffset);
        return universalTime.divide(units, 4).subtract(epochOffset);
    }
    
    private static TimeScaleData getTimeScaleData(final int scale) {
        if (scale < 0 || scale >= 10) {
            throw new IllegalArgumentException("scale out of range: " + scale);
        }
        return UniversalTimeScale.timeScaleTable[scale];
    }
    
    public static long getTimeScaleValue(final int scale, final int value) {
        final TimeScaleData data = getTimeScaleData(scale);
        switch (value) {
            case 0: {
                return data.units;
            }
            case 1: {
                return data.epochOffset;
            }
            case 2: {
                return data.fromMin;
            }
            case 3: {
                return data.fromMax;
            }
            case 4: {
                return data.toMin;
            }
            case 5: {
                return data.toMax;
            }
            case 6: {
                return data.epochOffsetP1;
            }
            case 7: {
                return data.epochOffsetM1;
            }
            case 8: {
                return data.unitsRound;
            }
            case 9: {
                return data.minRound;
            }
            case 10: {
                return data.maxRound;
            }
            default: {
                throw new IllegalArgumentException("value out of range: " + value);
            }
        }
    }
    
    private static TimeScaleData toRangeCheck(final long universalTime, final int scale) {
        final TimeScaleData data = getTimeScaleData(scale);
        if (universalTime >= data.toMin && universalTime <= data.toMax) {
            return data;
        }
        throw new IllegalArgumentException("universalTime out of range:" + universalTime);
    }
    
    private static TimeScaleData fromRangeCheck(final long otherTime, final int scale) {
        final TimeScaleData data = getTimeScaleData(scale);
        if (otherTime >= data.fromMin && otherTime <= data.fromMax) {
            return data;
        }
        throw new IllegalArgumentException("otherTime out of range:" + otherTime);
    }
    
    @Deprecated
    public static BigDecimal toBigDecimalTrunc(final BigDecimal universalTime, final int timeScale) {
        final TimeScaleData data = getTimeScaleData(timeScale);
        final BigDecimal units = new BigDecimal(data.units);
        final BigDecimal epochOffset = new BigDecimal(data.epochOffset);
        return universalTime.divide(units, 1).subtract(epochOffset);
    }
    
    static {
        timeScaleTable = new TimeScaleData[] { new TimeScaleData(10000L, 621355968000000000L, -9223372036854774999L, 9223372036854774999L, -984472800485477L, 860201606885477L), new TimeScaleData(10000000L, 621355968000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -984472800485L, 860201606885L), new TimeScaleData(10000L, 621355968000000000L, -9223372036854774999L, 9223372036854774999L, -984472800485477L, 860201606885477L), new TimeScaleData(1L, 504911232000000000L, -8718460804854775808L, Long.MAX_VALUE, Long.MIN_VALUE, 8718460804854775807L), new TimeScaleData(1L, 0L, Long.MIN_VALUE, Long.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE), new TimeScaleData(10000000L, 600527520000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -982389955685L, 862284451685L), new TimeScaleData(10000000L, 631139040000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -985451107685L, 859223299685L), new TimeScaleData(864000000000L, 599265216000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -11368793L, 9981605L), new TimeScaleData(864000000000L, 599265216000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -11368793L, 9981605L), new TimeScaleData(10L, 621355968000000000L, -9223372036854775804L, 9223372036854775804L, -984472800485477580L, 860201606885477580L) };
    }
    
    private static final class TimeScaleData
    {
        long units;
        long epochOffset;
        long fromMin;
        long fromMax;
        long toMin;
        long toMax;
        long epochOffsetP1;
        long epochOffsetM1;
        long unitsRound;
        long minRound;
        long maxRound;
        
        TimeScaleData(final long theUnits, final long theEpochOffset, final long theToMin, final long theToMax, final long theFromMin, final long theFromMax) {
            this.units = theUnits;
            this.unitsRound = theUnits / 2L;
            this.minRound = Long.MIN_VALUE + this.unitsRound;
            this.maxRound = Long.MAX_VALUE - this.unitsRound;
            this.epochOffset = theEpochOffset / theUnits;
            if (theUnits == 1L) {
                final long epochOffset = this.epochOffset;
                this.epochOffsetM1 = epochOffset;
                this.epochOffsetP1 = epochOffset;
            }
            else {
                this.epochOffsetP1 = this.epochOffset + 1L;
                this.epochOffsetM1 = this.epochOffset - 1L;
            }
            this.toMin = theToMin;
            this.toMax = theToMax;
            this.fromMin = theFromMin;
            this.fromMax = theFromMax;
        }
    }
}
