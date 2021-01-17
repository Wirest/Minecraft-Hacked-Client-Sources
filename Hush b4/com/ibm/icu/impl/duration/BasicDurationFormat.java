// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import java.util.Date;
import java.text.FieldPosition;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.text.DurationFormat;

public class BasicDurationFormat extends DurationFormat
{
    private static final long serialVersionUID = -3146984141909457700L;
    transient DurationFormatter formatter;
    transient PeriodFormatter pformatter;
    transient PeriodFormatterService pfs;
    private static boolean checkXMLDuration;
    
    public static BasicDurationFormat getInstance(final ULocale locale) {
        return new BasicDurationFormat(locale);
    }
    
    @Override
    public StringBuffer format(final Object object, final StringBuffer toAppend, final FieldPosition pos) {
        if (object instanceof Long) {
            final String res = this.formatDurationFromNow((long)object);
            return toAppend.append(res);
        }
        if (object instanceof Date) {
            final String res = this.formatDurationFromNowTo((Date)object);
            return toAppend.append(res);
        }
        if (BasicDurationFormat.checkXMLDuration) {
            try {
                if (object instanceof Duration) {
                    final String res = this.formatDuration(object);
                    return toAppend.append(res);
                }
            }
            catch (NoClassDefFoundError ncdfe) {
                System.err.println("Skipping XML capability");
                BasicDurationFormat.checkXMLDuration = false;
            }
        }
        throw new IllegalArgumentException("Cannot format given Object as a Duration");
    }
    
    public BasicDurationFormat() {
        this.pfs = null;
        this.pfs = BasicPeriodFormatterService.getInstance();
        this.formatter = this.pfs.newDurationFormatterFactory().getFormatter();
        this.pformatter = this.pfs.newPeriodFormatterFactory().setDisplayPastFuture(false).getFormatter();
    }
    
    public BasicDurationFormat(final ULocale locale) {
        super(locale);
        this.pfs = null;
        this.pfs = BasicPeriodFormatterService.getInstance();
        this.formatter = this.pfs.newDurationFormatterFactory().setLocale(locale.getName()).getFormatter();
        this.pformatter = this.pfs.newPeriodFormatterFactory().setDisplayPastFuture(false).setLocale(locale.getName()).getFormatter();
    }
    
    @Override
    public String formatDurationFrom(final long duration, final long referenceDate) {
        return this.formatter.formatDurationFrom(duration, referenceDate);
    }
    
    @Override
    public String formatDurationFromNow(final long duration) {
        return this.formatter.formatDurationFromNow(duration);
    }
    
    @Override
    public String formatDurationFromNowTo(final Date targetDate) {
        return this.formatter.formatDurationFromNowTo(targetDate);
    }
    
    public String formatDuration(final Object obj) {
        final DatatypeConstants.Field[] inFields = { DatatypeConstants.YEARS, DatatypeConstants.MONTHS, DatatypeConstants.DAYS, DatatypeConstants.HOURS, DatatypeConstants.MINUTES, DatatypeConstants.SECONDS };
        final TimeUnit[] outFields = { TimeUnit.YEAR, TimeUnit.MONTH, TimeUnit.DAY, TimeUnit.HOUR, TimeUnit.MINUTE, TimeUnit.SECOND };
        final Duration inDuration = (Duration)obj;
        Period p = null;
        Duration duration = inDuration;
        boolean inPast = false;
        if (inDuration.getSign() < 0) {
            duration = inDuration.negate();
            inPast = true;
        }
        boolean sawNonZero = false;
        for (int i = 0; i < inFields.length; ++i) {
            if (duration.isSet(inFields[i])) {
                final Number n = duration.getField(inFields[i]);
                if (n.intValue() != 0 || sawNonZero) {
                    sawNonZero = true;
                    float floatVal = n.floatValue();
                    TimeUnit alternateUnit = null;
                    float alternateVal = 0.0f;
                    if (outFields[i] == TimeUnit.SECOND) {
                        final double fullSeconds = floatVal;
                        final double intSeconds = Math.floor(floatVal);
                        final double millis = (fullSeconds - intSeconds) * 1000.0;
                        if (millis > 0.0) {
                            alternateUnit = TimeUnit.MILLISECOND;
                            alternateVal = (float)millis;
                            floatVal = (float)intSeconds;
                        }
                    }
                    if (p == null) {
                        p = Period.at(floatVal, outFields[i]);
                    }
                    else {
                        p = p.and(floatVal, outFields[i]);
                    }
                    if (alternateUnit != null) {
                        p = p.and(alternateVal, alternateUnit);
                    }
                }
            }
        }
        if (p == null) {
            return this.formatDurationFromNow(0L);
        }
        if (inPast) {
            p = p.inPast();
        }
        else {
            p = p.inFuture();
        }
        return this.pformatter.format(p);
    }
    
    static {
        BasicDurationFormat.checkXMLDuration = true;
    }
}
