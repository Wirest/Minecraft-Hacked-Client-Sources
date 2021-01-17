// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.Calendar;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Filter;
import java.util.TimeZone;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "TimeFilter", category = "Core", elementType = "filter", printObject = true)
public final class TimeFilter extends AbstractFilter
{
    private static final long HOUR_MS = 3600000L;
    private static final long MINUTE_MS = 60000L;
    private static final long SECOND_MS = 1000L;
    private final long start;
    private final long end;
    private final TimeZone timezone;
    
    private TimeFilter(final long start, final long end, final TimeZone tz, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.start = start;
        this.end = end;
        this.timezone = tz;
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        final Calendar calendar = Calendar.getInstance(this.timezone);
        calendar.setTimeInMillis(event.getMillis());
        final long apparentOffset = calendar.get(11) * 3600000L + calendar.get(12) * 60000L + calendar.get(13) * 1000L + calendar.get(14);
        return (apparentOffset >= this.start && apparentOffset < this.end) ? this.onMatch : this.onMismatch;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("start=").append(this.start);
        sb.append(", end=").append(this.end);
        sb.append(", timezone=").append(this.timezone.toString());
        return sb.toString();
    }
    
    @PluginFactory
    public static TimeFilter createFilter(@PluginAttribute("start") final String start, @PluginAttribute("end") final String end, @PluginAttribute("timezone") final String tz, @PluginAttribute("onMatch") final String match, @PluginAttribute("onMismatch") final String mismatch) {
        final SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        long s = 0L;
        if (start != null) {
            stf.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                s = stf.parse(start).getTime();
            }
            catch (ParseException ex) {
                TimeFilter.LOGGER.warn("Error parsing start value " + start, ex);
            }
        }
        long e = Long.MAX_VALUE;
        if (end != null) {
            stf.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                e = stf.parse(end).getTime();
            }
            catch (ParseException ex2) {
                TimeFilter.LOGGER.warn("Error parsing start value " + end, ex2);
            }
        }
        final TimeZone timezone = (tz == null) ? TimeZone.getDefault() : TimeZone.getTimeZone(tz);
        final Filter.Result onMatch = Filter.Result.toResult(match, Filter.Result.NEUTRAL);
        final Filter.Result onMismatch = Filter.Result.toResult(mismatch, Filter.Result.DENY);
        return new TimeFilter(s, e, timezone, onMatch, onMismatch);
    }
}
