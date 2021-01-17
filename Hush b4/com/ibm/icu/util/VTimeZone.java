// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.MissingResourceException;
import com.ibm.icu.impl.Grego;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.IOException;
import java.util.Iterator;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.Reader;
import java.util.Date;
import java.util.List;

public class VTimeZone extends BasicTimeZone
{
    private static final long serialVersionUID = -6851467294127795902L;
    private BasicTimeZone tz;
    private List<String> vtzlines;
    private String olsonzid;
    private String tzurl;
    private Date lastmod;
    private static String ICU_TZVERSION;
    private static final String ICU_TZINFO_PROP = "X-TZINFO";
    private static final int DEF_DSTSAVINGS = 3600000;
    private static final long DEF_TZSTARTTIME = 0L;
    private static final long MIN_TIME = Long.MIN_VALUE;
    private static final long MAX_TIME = Long.MAX_VALUE;
    private static final String COLON = ":";
    private static final String SEMICOLON = ";";
    private static final String EQUALS_SIGN = "=";
    private static final String COMMA = ",";
    private static final String NEWLINE = "\r\n";
    private static final String ICAL_BEGIN_VTIMEZONE = "BEGIN:VTIMEZONE";
    private static final String ICAL_END_VTIMEZONE = "END:VTIMEZONE";
    private static final String ICAL_BEGIN = "BEGIN";
    private static final String ICAL_END = "END";
    private static final String ICAL_VTIMEZONE = "VTIMEZONE";
    private static final String ICAL_TZID = "TZID";
    private static final String ICAL_STANDARD = "STANDARD";
    private static final String ICAL_DAYLIGHT = "DAYLIGHT";
    private static final String ICAL_DTSTART = "DTSTART";
    private static final String ICAL_TZOFFSETFROM = "TZOFFSETFROM";
    private static final String ICAL_TZOFFSETTO = "TZOFFSETTO";
    private static final String ICAL_RDATE = "RDATE";
    private static final String ICAL_RRULE = "RRULE";
    private static final String ICAL_TZNAME = "TZNAME";
    private static final String ICAL_TZURL = "TZURL";
    private static final String ICAL_LASTMOD = "LAST-MODIFIED";
    private static final String ICAL_FREQ = "FREQ";
    private static final String ICAL_UNTIL = "UNTIL";
    private static final String ICAL_YEARLY = "YEARLY";
    private static final String ICAL_BYMONTH = "BYMONTH";
    private static final String ICAL_BYDAY = "BYDAY";
    private static final String ICAL_BYMONTHDAY = "BYMONTHDAY";
    private static final String[] ICAL_DOW_NAMES;
    private static final int[] MONTHLENGTH;
    private static final int INI = 0;
    private static final int VTZ = 1;
    private static final int TZI = 2;
    private static final int ERR = 3;
    private transient boolean isFrozen;
    
    public static VTimeZone create(final String tzid) {
        final VTimeZone vtz = new VTimeZone(tzid);
        vtz.tz = (BasicTimeZone)TimeZone.getTimeZone(tzid, 0);
        vtz.olsonzid = vtz.tz.getID();
        return vtz;
    }
    
    public static VTimeZone create(final Reader reader) {
        final VTimeZone vtz = new VTimeZone();
        if (vtz.load(reader)) {
            return vtz;
        }
        return null;
    }
    
    @Override
    public int getOffset(final int era, final int year, final int month, final int day, final int dayOfWeek, final int milliseconds) {
        return this.tz.getOffset(era, year, month, day, dayOfWeek, milliseconds);
    }
    
    @Override
    public void getOffset(final long date, final boolean local, final int[] offsets) {
        this.tz.getOffset(date, local, offsets);
    }
    
    @Override
    @Deprecated
    public void getOffsetFromLocal(final long date, final int nonExistingTimeOpt, final int duplicatedTimeOpt, final int[] offsets) {
        this.tz.getOffsetFromLocal(date, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
    }
    
    @Override
    public int getRawOffset() {
        return this.tz.getRawOffset();
    }
    
    @Override
    public boolean inDaylightTime(final Date date) {
        return this.tz.inDaylightTime(date);
    }
    
    @Override
    public void setRawOffset(final int offsetMillis) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen VTimeZone instance.");
        }
        this.tz.setRawOffset(offsetMillis);
    }
    
    @Override
    public boolean useDaylightTime() {
        return this.tz.useDaylightTime();
    }
    
    @Override
    public boolean observesDaylightTime() {
        return this.tz.observesDaylightTime();
    }
    
    @Override
    public boolean hasSameRules(final TimeZone other) {
        if (this == other) {
            return true;
        }
        if (other instanceof VTimeZone) {
            return this.tz.hasSameRules(((VTimeZone)other).tz);
        }
        return this.tz.hasSameRules(other);
    }
    
    public String getTZURL() {
        return this.tzurl;
    }
    
    public void setTZURL(final String url) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen VTimeZone instance.");
        }
        this.tzurl = url;
    }
    
    public Date getLastModified() {
        return this.lastmod;
    }
    
    public void setLastModified(final Date date) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen VTimeZone instance.");
        }
        this.lastmod = date;
    }
    
    public void write(final Writer writer) throws IOException {
        final BufferedWriter bw = new BufferedWriter(writer);
        if (this.vtzlines != null) {
            for (final String line : this.vtzlines) {
                if (line.startsWith("TZURL:")) {
                    if (this.tzurl == null) {
                        continue;
                    }
                    bw.write("TZURL");
                    bw.write(":");
                    bw.write(this.tzurl);
                    bw.write("\r\n");
                }
                else if (line.startsWith("LAST-MODIFIED:")) {
                    if (this.lastmod == null) {
                        continue;
                    }
                    bw.write("LAST-MODIFIED");
                    bw.write(":");
                    bw.write(getUTCDateTimeString(this.lastmod.getTime()));
                    bw.write("\r\n");
                }
                else {
                    bw.write(line);
                    bw.write("\r\n");
                }
            }
            bw.flush();
        }
        else {
            String[] customProperties = null;
            if (this.olsonzid != null && VTimeZone.ICU_TZVERSION != null) {
                customProperties = new String[] { "X-TZINFO:" + this.olsonzid + "[" + VTimeZone.ICU_TZVERSION + "]" };
            }
            this.writeZone(writer, this.tz, customProperties);
        }
    }
    
    public void write(final Writer writer, final long start) throws IOException {
        final TimeZoneRule[] rules = this.tz.getTimeZoneRules(start);
        final RuleBasedTimeZone rbtz = new RuleBasedTimeZone(this.tz.getID(), (InitialTimeZoneRule)rules[0]);
        for (int i = 1; i < rules.length; ++i) {
            rbtz.addTransitionRule(rules[i]);
        }
        String[] customProperties = null;
        if (this.olsonzid != null && VTimeZone.ICU_TZVERSION != null) {
            customProperties = new String[] { "X-TZINFO:" + this.olsonzid + "[" + VTimeZone.ICU_TZVERSION + "/Partial@" + start + "]" };
        }
        this.writeZone(writer, rbtz, customProperties);
    }
    
    public void writeSimple(final Writer writer, final long time) throws IOException {
        final TimeZoneRule[] rules = this.tz.getSimpleTimeZoneRulesNear(time);
        final RuleBasedTimeZone rbtz = new RuleBasedTimeZone(this.tz.getID(), (InitialTimeZoneRule)rules[0]);
        for (int i = 1; i < rules.length; ++i) {
            rbtz.addTransitionRule(rules[i]);
        }
        String[] customProperties = null;
        if (this.olsonzid != null && VTimeZone.ICU_TZVERSION != null) {
            customProperties = new String[] { "X-TZINFO:" + this.olsonzid + "[" + VTimeZone.ICU_TZVERSION + "/Simple@" + time + "]" };
        }
        this.writeZone(writer, rbtz, customProperties);
    }
    
    @Override
    public TimeZoneTransition getNextTransition(final long base, final boolean inclusive) {
        return this.tz.getNextTransition(base, inclusive);
    }
    
    @Override
    public TimeZoneTransition getPreviousTransition(final long base, final boolean inclusive) {
        return this.tz.getPreviousTransition(base, inclusive);
    }
    
    @Override
    public boolean hasEquivalentTransitions(final TimeZone other, final long start, final long end) {
        return this == other || this.tz.hasEquivalentTransitions(other, start, end);
    }
    
    @Override
    public TimeZoneRule[] getTimeZoneRules() {
        return this.tz.getTimeZoneRules();
    }
    
    @Override
    public TimeZoneRule[] getTimeZoneRules(final long start) {
        return this.tz.getTimeZoneRules(start);
    }
    
    @Override
    public Object clone() {
        if (this.isFrozen()) {
            return this;
        }
        return this.cloneAsThawed();
    }
    
    private VTimeZone() {
        this.olsonzid = null;
        this.tzurl = null;
        this.lastmod = null;
        this.isFrozen = false;
    }
    
    private VTimeZone(final String tzid) {
        super(tzid);
        this.olsonzid = null;
        this.tzurl = null;
        this.lastmod = null;
        this.isFrozen = false;
    }
    
    private boolean load(final Reader reader) {
        try {
            this.vtzlines = new LinkedList<String>();
            boolean eol = false;
            boolean start = false;
            boolean success = false;
            final StringBuilder line = new StringBuilder();
            while (true) {
                final int ch = reader.read();
                if (ch == -1) {
                    if (start && line.toString().startsWith("END:VTIMEZONE")) {
                        this.vtzlines.add(line.toString());
                        success = true;
                        break;
                    }
                    break;
                }
                else {
                    if (ch == 13) {
                        continue;
                    }
                    if (eol) {
                        if (ch != 9 && ch != 32) {
                            if (start && line.length() > 0) {
                                this.vtzlines.add(line.toString());
                            }
                            line.setLength(0);
                            if (ch != 10) {
                                line.append((char)ch);
                            }
                        }
                        eol = false;
                    }
                    else if (ch == 10) {
                        eol = true;
                        if (start) {
                            if (line.toString().startsWith("END:VTIMEZONE")) {
                                this.vtzlines.add(line.toString());
                                success = true;
                                break;
                            }
                            continue;
                        }
                        else {
                            if (!line.toString().startsWith("BEGIN:VTIMEZONE")) {
                                continue;
                            }
                            this.vtzlines.add(line.toString());
                            line.setLength(0);
                            start = true;
                            eol = false;
                        }
                    }
                    else {
                        line.append((char)ch);
                    }
                }
            }
            if (!success) {
                return false;
            }
        }
        catch (IOException ioe) {
            return false;
        }
        return this.parse();
    }
    
    private boolean parse() {
        if (this.vtzlines == null || this.vtzlines.size() == 0) {
            return false;
        }
        String tzid = null;
        int state = 0;
        boolean dst = false;
        String from = null;
        String to = null;
        String tzname = null;
        String dtstart = null;
        boolean isRRULE = false;
        List<String> dates = null;
        final List<TimeZoneRule> rules = new ArrayList<TimeZoneRule>();
        int initialRawOffset = 0;
        int initialDSTSavings = 0;
        long firstStart = Long.MAX_VALUE;
        for (final String line : this.vtzlines) {
            final int valueSep = line.indexOf(":");
            if (valueSep < 0) {
                continue;
            }
            final String name = line.substring(0, valueSep);
            final String value = line.substring(valueSep + 1);
            switch (state) {
                case 0: {
                    if (name.equals("BEGIN") && value.equals("VTIMEZONE")) {
                        state = 1;
                        break;
                    }
                    break;
                }
                case 1: {
                    if (name.equals("TZID")) {
                        tzid = value;
                        break;
                    }
                    if (name.equals("TZURL")) {
                        this.tzurl = value;
                        break;
                    }
                    if (name.equals("LAST-MODIFIED")) {
                        this.lastmod = new Date(parseDateTimeString(value, 0));
                        break;
                    }
                    if (name.equals("BEGIN")) {
                        final boolean isDST = value.equals("DAYLIGHT");
                        if (!value.equals("STANDARD") && !isDST) {
                            state = 3;
                            break;
                        }
                        if (tzid == null) {
                            state = 3;
                            break;
                        }
                        dates = null;
                        isRRULE = false;
                        from = null;
                        to = null;
                        tzname = null;
                        dst = isDST;
                        state = 2;
                        break;
                    }
                    else {
                        if (name.equals("END")) {
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 2: {
                    if (name.equals("DTSTART")) {
                        dtstart = value;
                        break;
                    }
                    if (name.equals("TZNAME")) {
                        tzname = value;
                        break;
                    }
                    if (name.equals("TZOFFSETFROM")) {
                        from = value;
                        break;
                    }
                    if (name.equals("TZOFFSETTO")) {
                        to = value;
                        break;
                    }
                    if (name.equals("RDATE")) {
                        if (isRRULE) {
                            state = 3;
                            break;
                        }
                        if (dates == null) {
                            dates = new LinkedList<String>();
                        }
                        final StringTokenizer st = new StringTokenizer(value, ",");
                        while (st.hasMoreTokens()) {
                            final String date = st.nextToken();
                            dates.add(date);
                        }
                        break;
                    }
                    else if (name.equals("RRULE")) {
                        if (!isRRULE && dates != null) {
                            state = 3;
                            break;
                        }
                        if (dates == null) {
                            dates = new LinkedList<String>();
                        }
                        isRRULE = true;
                        dates.add(value);
                        break;
                    }
                    else {
                        if (!name.equals("END")) {
                            break;
                        }
                        if (dtstart == null || from == null || to == null) {
                            state = 3;
                            break;
                        }
                        if (tzname == null) {
                            tzname = getDefaultTZName(tzid, dst);
                        }
                        TimeZoneRule rule = null;
                        int fromOffset = 0;
                        int toOffset = 0;
                        int rawOffset = 0;
                        int dstSavings = 0;
                        long start = 0L;
                        try {
                            fromOffset = offsetStrToMillis(from);
                            toOffset = offsetStrToMillis(to);
                            if (dst) {
                                if (toOffset - fromOffset > 0) {
                                    rawOffset = fromOffset;
                                    dstSavings = toOffset - fromOffset;
                                }
                                else {
                                    rawOffset = toOffset - 3600000;
                                    dstSavings = 3600000;
                                }
                            }
                            else {
                                rawOffset = toOffset;
                                dstSavings = 0;
                            }
                            start = parseDateTimeString(dtstart, fromOffset);
                            Date actualStart = null;
                            if (isRRULE) {
                                rule = createRuleByRRULE(tzname, rawOffset, dstSavings, start, dates, fromOffset);
                            }
                            else {
                                rule = createRuleByRDATE(tzname, rawOffset, dstSavings, start, dates, fromOffset);
                            }
                            if (rule != null) {
                                actualStart = rule.getFirstStart(fromOffset, 0);
                                if (actualStart.getTime() < firstStart) {
                                    firstStart = actualStart.getTime();
                                    if (dstSavings > 0) {
                                        initialRawOffset = fromOffset;
                                        initialDSTSavings = 0;
                                    }
                                    else if (fromOffset - toOffset == 3600000) {
                                        initialRawOffset = fromOffset - 3600000;
                                        initialDSTSavings = 3600000;
                                    }
                                    else {
                                        initialRawOffset = fromOffset;
                                        initialDSTSavings = 0;
                                    }
                                }
                            }
                        }
                        catch (IllegalArgumentException ex) {}
                        if (rule == null) {
                            state = 3;
                            break;
                        }
                        rules.add(rule);
                        state = 1;
                        break;
                    }
                    break;
                }
            }
            if (state == 3) {
                this.vtzlines = null;
                return false;
            }
        }
        if (rules.size() == 0) {
            return false;
        }
        final InitialTimeZoneRule initialRule = new InitialTimeZoneRule(getDefaultTZName(tzid, false), initialRawOffset, initialDSTSavings);
        final RuleBasedTimeZone rbtz = new RuleBasedTimeZone(tzid, initialRule);
        int finalRuleIdx = -1;
        int finalRuleCount = 0;
        for (int i = 0; i < rules.size(); ++i) {
            final TimeZoneRule r = rules.get(i);
            if (r instanceof AnnualTimeZoneRule && ((AnnualTimeZoneRule)r).getEndYear() == Integer.MAX_VALUE) {
                ++finalRuleCount;
                finalRuleIdx = i;
            }
        }
        if (finalRuleCount > 2) {
            return false;
        }
        if (finalRuleCount == 1) {
            if (rules.size() == 1) {
                rules.clear();
            }
            else {
                final AnnualTimeZoneRule finalRule = rules.get(finalRuleIdx);
                final int tmpRaw = finalRule.getRawOffset();
                final int tmpDST = finalRule.getDSTSavings();
                Date start2;
                final Date finalStart = start2 = finalRule.getFirstStart(initialRawOffset, initialDSTSavings);
                for (int j = 0; j < rules.size(); ++j) {
                    if (finalRuleIdx != j) {
                        final TimeZoneRule r2 = rules.get(j);
                        final Date lastStart = r2.getFinalStart(tmpRaw, tmpDST);
                        if (lastStart.after(start2)) {
                            start2 = finalRule.getNextStart(lastStart.getTime(), r2.getRawOffset(), r2.getDSTSavings(), false);
                        }
                    }
                }
                TimeZoneRule newRule;
                if (start2 == finalStart) {
                    newRule = new TimeArrayTimeZoneRule(finalRule.getName(), finalRule.getRawOffset(), finalRule.getDSTSavings(), new long[] { finalStart.getTime() }, 2);
                }
                else {
                    final int[] fields = Grego.timeToFields(start2.getTime(), null);
                    newRule = new AnnualTimeZoneRule(finalRule.getName(), finalRule.getRawOffset(), finalRule.getDSTSavings(), finalRule.getRule(), finalRule.getStartYear(), fields[0]);
                }
                rules.set(finalRuleIdx, newRule);
            }
        }
        final Iterator i$2 = rules.iterator();
        while (i$2.hasNext()) {
            final TimeZoneRule r = i$2.next();
            rbtz.addTransitionRule(r);
        }
        this.tz = rbtz;
        this.setID(tzid);
        return true;
    }
    
    private static String getDefaultTZName(final String tzid, final boolean isDST) {
        if (isDST) {
            return tzid + "(DST)";
        }
        return tzid + "(STD)";
    }
    
    private static TimeZoneRule createRuleByRRULE(final String tzname, final int rawOffset, final int dstSavings, final long start, final List<String> dates, final int fromOffset) {
        if (dates == null || dates.size() == 0) {
            return null;
        }
        String rrule = dates.get(0);
        long[] until = { 0L };
        final int[] ruleFields = parseRRULE(rrule, until);
        if (ruleFields == null) {
            return null;
        }
        int month = ruleFields[0];
        final int dayOfWeek = ruleFields[1];
        final int nthDayOfWeek = ruleFields[2];
        int dayOfMonth = ruleFields[3];
        if (dates.size() == 1) {
            if (ruleFields.length > 4) {
                if (ruleFields.length != 10 || month == -1 || dayOfWeek == 0) {
                    return null;
                }
                int firstDay = 31;
                final int[] days = new int[7];
                for (int i = 0; i < 7; ++i) {
                    days[i] = ruleFields[3 + i];
                    days[i] = ((days[i] > 0) ? days[i] : (VTimeZone.MONTHLENGTH[month] + days[i] + 1));
                    firstDay = ((days[i] < firstDay) ? days[i] : firstDay);
                }
                for (int i = 1; i < 7; ++i) {
                    boolean found = false;
                    for (int j = 0; j < 7; ++j) {
                        if (days[j] == firstDay + i) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        return null;
                    }
                }
                dayOfMonth = firstDay;
            }
        }
        else {
            if (month == -1 || dayOfWeek == 0 || dayOfMonth == 0) {
                return null;
            }
            if (dates.size() > 7) {
                return null;
            }
            int earliestMonth = month;
            int daysCount = ruleFields.length - 3;
            int earliestDay = 31;
            for (int k = 0; k < daysCount; ++k) {
                int dom = ruleFields[3 + k];
                dom = ((dom > 0) ? dom : (VTimeZone.MONTHLENGTH[month] + dom + 1));
                earliestDay = ((dom < earliestDay) ? dom : earliestDay);
            }
            int anotherMonth = -1;
            for (int l = 1; l < dates.size(); ++l) {
                rrule = dates.get(l);
                final long[] unt = { 0L };
                final int[] fields = parseRRULE(rrule, unt);
                if (unt[0] > until[0]) {
                    until = unt;
                }
                if (fields[0] == -1 || fields[1] == 0 || fields[3] == 0) {
                    return null;
                }
                final int count = fields.length - 3;
                if (daysCount + count > 7) {
                    return null;
                }
                if (fields[1] != dayOfWeek) {
                    return null;
                }
                if (fields[0] != month) {
                    if (anotherMonth == -1) {
                        final int diff = fields[0] - month;
                        if (diff == -11 || diff == -1) {
                            anotherMonth = (earliestMonth = fields[0]);
                            earliestDay = 31;
                        }
                        else {
                            if (diff != 11 && diff != 1) {
                                return null;
                            }
                            anotherMonth = fields[0];
                        }
                    }
                    else if (fields[0] != month && fields[0] != anotherMonth) {
                        return null;
                    }
                }
                if (fields[0] == earliestMonth) {
                    for (int m = 0; m < count; ++m) {
                        int dom2 = fields[3 + m];
                        dom2 = ((dom2 > 0) ? dom2 : (VTimeZone.MONTHLENGTH[fields[0]] + dom2 + 1));
                        earliestDay = ((dom2 < earliestDay) ? dom2 : earliestDay);
                    }
                }
                daysCount += count;
            }
            if (daysCount != 7) {
                return null;
            }
            month = earliestMonth;
            dayOfMonth = earliestDay;
        }
        final int[] dfields = Grego.timeToFields(start + fromOffset, null);
        final int startYear = dfields[0];
        if (month == -1) {
            month = dfields[1];
        }
        if (dayOfWeek == 0 && nthDayOfWeek == 0 && dayOfMonth == 0) {
            dayOfMonth = dfields[2];
        }
        final int timeInDay = dfields[5];
        int endYear = Integer.MAX_VALUE;
        if (until[0] != Long.MIN_VALUE) {
            Grego.timeToFields(until[0], dfields);
            endYear = dfields[0];
        }
        DateTimeRule adtr = null;
        if (dayOfWeek == 0 && nthDayOfWeek == 0 && dayOfMonth != 0) {
            adtr = new DateTimeRule(month, dayOfMonth, timeInDay, 0);
        }
        else if (dayOfWeek != 0 && nthDayOfWeek != 0 && dayOfMonth == 0) {
            adtr = new DateTimeRule(month, nthDayOfWeek, dayOfWeek, timeInDay, 0);
        }
        else {
            if (dayOfWeek == 0 || nthDayOfWeek != 0 || dayOfMonth == 0) {
                return null;
            }
            adtr = new DateTimeRule(month, dayOfMonth, dayOfWeek, true, timeInDay, 0);
        }
        return new AnnualTimeZoneRule(tzname, rawOffset, dstSavings, adtr, startYear, endYear);
    }
    
    private static int[] parseRRULE(final String rrule, final long[] until) {
        int month = -1;
        int dayOfWeek = 0;
        int nthDayOfWeek = 0;
        int[] dayOfMonth = null;
        long untilTime = Long.MIN_VALUE;
        boolean yearly = false;
        boolean parseError = false;
        final StringTokenizer st = new StringTokenizer(rrule, ";");
        while (st.hasMoreTokens()) {
            final String prop = st.nextToken();
            final int sep = prop.indexOf("=");
            if (sep == -1) {
                parseError = true;
                break;
            }
            final String attr = prop.substring(0, sep);
            String value = prop.substring(sep + 1);
            if (attr.equals("FREQ")) {
                if (!value.equals("YEARLY")) {
                    parseError = true;
                    break;
                }
                yearly = true;
            }
            else {
                if (attr.equals("UNTIL")) {
                    try {
                        untilTime = parseDateTimeString(value, 0);
                        continue;
                    }
                    catch (IllegalArgumentException iae) {
                        parseError = true;
                        break;
                    }
                }
                if (attr.equals("BYMONTH")) {
                    if (value.length() > 2) {
                        parseError = true;
                        break;
                    }
                    try {
                        month = Integer.parseInt(value) - 1;
                        if (month < 0 || month >= 12) {
                            parseError = true;
                            break;
                        }
                        continue;
                    }
                    catch (NumberFormatException nfe) {
                        parseError = true;
                        break;
                    }
                }
                if (attr.equals("BYDAY")) {
                    final int length = value.length();
                    if (length < 2 || length > 4) {
                        parseError = true;
                        break;
                    }
                    if (length > 2) {
                        int sign = 1;
                        if (value.charAt(0) == '+') {
                            sign = 1;
                        }
                        else if (value.charAt(0) == '-') {
                            sign = -1;
                        }
                        else if (length == 4) {
                            parseError = true;
                            break;
                        }
                        try {
                            final int n = Integer.parseInt(value.substring(length - 3, length - 2));
                            if (n == 0 || n > 4) {
                                parseError = true;
                                break;
                            }
                            nthDayOfWeek = n * sign;
                        }
                        catch (NumberFormatException nfe2) {
                            parseError = true;
                            break;
                        }
                        value = value.substring(length - 2);
                    }
                    int wday;
                    for (wday = 0; wday < VTimeZone.ICAL_DOW_NAMES.length && !value.equals(VTimeZone.ICAL_DOW_NAMES[wday]); ++wday) {}
                    if (wday >= VTimeZone.ICAL_DOW_NAMES.length) {
                        parseError = true;
                        break;
                    }
                    dayOfWeek = wday + 1;
                }
                else {
                    if (!attr.equals("BYMONTHDAY")) {
                        continue;
                    }
                    final StringTokenizer days = new StringTokenizer(value, ",");
                    final int count = days.countTokens();
                    dayOfMonth = new int[count];
                    int index = 0;
                    while (days.hasMoreTokens()) {
                        try {
                            dayOfMonth[index++] = Integer.parseInt(days.nextToken());
                            continue;
                        }
                        catch (NumberFormatException nfe3) {
                            parseError = true;
                        }
                        break;
                    }
                }
            }
        }
        if (parseError) {
            return null;
        }
        if (!yearly) {
            return null;
        }
        until[0] = untilTime;
        int[] results;
        if (dayOfMonth == null) {
            results = new int[] { 0, 0, 0, 0 };
        }
        else {
            results = new int[3 + dayOfMonth.length];
            for (int i = 0; i < dayOfMonth.length; ++i) {
                results[3 + i] = dayOfMonth[i];
            }
        }
        results[0] = month;
        results[1] = dayOfWeek;
        results[2] = nthDayOfWeek;
        return results;
    }
    
    private static TimeZoneRule createRuleByRDATE(final String tzname, final int rawOffset, final int dstSavings, final long start, final List<String> dates, final int fromOffset) {
        long[] times;
        if (dates == null || dates.size() == 0) {
            times = new long[] { start };
        }
        else {
            times = new long[dates.size()];
            int idx = 0;
            try {
                for (final String date : dates) {
                    times[idx++] = parseDateTimeString(date, fromOffset);
                }
            }
            catch (IllegalArgumentException iae) {
                return null;
            }
        }
        return new TimeArrayTimeZoneRule(tzname, rawOffset, dstSavings, times, 2);
    }
    
    private void writeZone(final Writer w, final BasicTimeZone basictz, final String[] customProperties) throws IOException {
        this.writeHeader(w);
        if (customProperties != null && customProperties.length > 0) {
            for (int i = 0; i < customProperties.length; ++i) {
                if (customProperties[i] != null) {
                    w.write(customProperties[i]);
                    w.write("\r\n");
                }
            }
        }
        long t = Long.MIN_VALUE;
        String dstName = null;
        int dstFromOffset = 0;
        int dstFromDSTSavings = 0;
        int dstToOffset = 0;
        int dstStartYear = 0;
        int dstMonth = 0;
        int dstDayOfWeek = 0;
        int dstWeekInMonth = 0;
        int dstMillisInDay = 0;
        long dstStartTime = 0L;
        long dstUntilTime = 0L;
        int dstCount = 0;
        AnnualTimeZoneRule finalDstRule = null;
        String stdName = null;
        int stdFromOffset = 0;
        int stdFromDSTSavings = 0;
        int stdToOffset = 0;
        int stdStartYear = 0;
        int stdMonth = 0;
        int stdDayOfWeek = 0;
        int stdWeekInMonth = 0;
        int stdMillisInDay = 0;
        long stdStartTime = 0L;
        long stdUntilTime = 0L;
        int stdCount = 0;
        AnnualTimeZoneRule finalStdRule = null;
        final int[] dtfields = new int[6];
        boolean hasTransitions = false;
        while (true) {
            final TimeZoneTransition tzt = basictz.getNextTransition(t, false);
            if (tzt == null) {
                break;
            }
            hasTransitions = true;
            t = tzt.getTime();
            final String name = tzt.getTo().getName();
            final boolean isDst = tzt.getTo().getDSTSavings() != 0;
            final int fromOffset = tzt.getFrom().getRawOffset() + tzt.getFrom().getDSTSavings();
            final int fromDSTSavings = tzt.getFrom().getDSTSavings();
            final int toOffset = tzt.getTo().getRawOffset() + tzt.getTo().getDSTSavings();
            Grego.timeToFields(tzt.getTime() + fromOffset, dtfields);
            final int weekInMonth = Grego.getDayOfWeekInMonth(dtfields[0], dtfields[1], dtfields[2]);
            final int year = dtfields[0];
            boolean sameRule = false;
            if (isDst) {
                if (finalDstRule == null && tzt.getTo() instanceof AnnualTimeZoneRule && ((AnnualTimeZoneRule)tzt.getTo()).getEndYear() == Integer.MAX_VALUE) {
                    finalDstRule = (AnnualTimeZoneRule)tzt.getTo();
                }
                if (dstCount > 0) {
                    if (year == dstStartYear + dstCount && name.equals(dstName) && dstFromOffset == fromOffset && dstToOffset == toOffset && dstMonth == dtfields[1] && dstDayOfWeek == dtfields[3] && dstWeekInMonth == weekInMonth && dstMillisInDay == dtfields[5]) {
                        dstUntilTime = t;
                        ++dstCount;
                        sameRule = true;
                    }
                    if (!sameRule) {
                        if (dstCount == 1) {
                            writeZonePropsByTime(w, true, dstName, dstFromOffset, dstToOffset, dstStartTime, true);
                        }
                        else {
                            writeZonePropsByDOW(w, true, dstName, dstFromOffset, dstToOffset, dstMonth, dstWeekInMonth, dstDayOfWeek, dstStartTime, dstUntilTime);
                        }
                    }
                }
                if (!sameRule) {
                    dstName = name;
                    dstFromOffset = fromOffset;
                    dstFromDSTSavings = fromDSTSavings;
                    dstToOffset = toOffset;
                    dstStartYear = year;
                    dstMonth = dtfields[1];
                    dstDayOfWeek = dtfields[3];
                    dstWeekInMonth = weekInMonth;
                    dstMillisInDay = dtfields[5];
                    dstUntilTime = (dstStartTime = t);
                    dstCount = 1;
                }
                if (finalStdRule != null && finalDstRule != null) {
                    break;
                }
                continue;
            }
            else {
                if (finalStdRule == null && tzt.getTo() instanceof AnnualTimeZoneRule && ((AnnualTimeZoneRule)tzt.getTo()).getEndYear() == Integer.MAX_VALUE) {
                    finalStdRule = (AnnualTimeZoneRule)tzt.getTo();
                }
                if (stdCount > 0) {
                    if (year == stdStartYear + stdCount && name.equals(stdName) && stdFromOffset == fromOffset && stdToOffset == toOffset && stdMonth == dtfields[1] && stdDayOfWeek == dtfields[3] && stdWeekInMonth == weekInMonth && stdMillisInDay == dtfields[5]) {
                        stdUntilTime = t;
                        ++stdCount;
                        sameRule = true;
                    }
                    if (!sameRule) {
                        if (stdCount == 1) {
                            writeZonePropsByTime(w, false, stdName, stdFromOffset, stdToOffset, stdStartTime, true);
                        }
                        else {
                            writeZonePropsByDOW(w, false, stdName, stdFromOffset, stdToOffset, stdMonth, stdWeekInMonth, stdDayOfWeek, stdStartTime, stdUntilTime);
                        }
                    }
                }
                if (!sameRule) {
                    stdName = name;
                    stdFromOffset = fromOffset;
                    stdFromDSTSavings = fromDSTSavings;
                    stdToOffset = toOffset;
                    stdStartYear = year;
                    stdMonth = dtfields[1];
                    stdDayOfWeek = dtfields[3];
                    stdWeekInMonth = weekInMonth;
                    stdMillisInDay = dtfields[5];
                    stdUntilTime = (stdStartTime = t);
                    stdCount = 1;
                }
                if (finalStdRule != null && finalDstRule != null) {
                    break;
                }
                continue;
            }
        }
        if (!hasTransitions) {
            final int offset = basictz.getOffset(0L);
            final boolean isDst2 = offset != basictz.getRawOffset();
            writeZonePropsByTime(w, isDst2, getDefaultTZName(basictz.getID(), isDst2), offset, offset, 0L - offset, false);
        }
        else {
            if (dstCount > 0) {
                if (finalDstRule == null) {
                    if (dstCount == 1) {
                        writeZonePropsByTime(w, true, dstName, dstFromOffset, dstToOffset, dstStartTime, true);
                    }
                    else {
                        writeZonePropsByDOW(w, true, dstName, dstFromOffset, dstToOffset, dstMonth, dstWeekInMonth, dstDayOfWeek, dstStartTime, dstUntilTime);
                    }
                }
                else if (dstCount == 1) {
                    writeFinalRule(w, true, finalDstRule, dstFromOffset - dstFromDSTSavings, dstFromDSTSavings, dstStartTime);
                }
                else if (isEquivalentDateRule(dstMonth, dstWeekInMonth, dstDayOfWeek, finalDstRule.getRule())) {
                    writeZonePropsByDOW(w, true, dstName, dstFromOffset, dstToOffset, dstMonth, dstWeekInMonth, dstDayOfWeek, dstStartTime, Long.MAX_VALUE);
                }
                else {
                    writeZonePropsByDOW(w, true, dstName, dstFromOffset, dstToOffset, dstMonth, dstWeekInMonth, dstDayOfWeek, dstStartTime, dstUntilTime);
                    writeFinalRule(w, true, finalDstRule, dstFromOffset - dstFromDSTSavings, dstFromDSTSavings, dstStartTime);
                }
            }
            if (stdCount > 0) {
                if (finalStdRule == null) {
                    if (stdCount == 1) {
                        writeZonePropsByTime(w, false, stdName, stdFromOffset, stdToOffset, stdStartTime, true);
                    }
                    else {
                        writeZonePropsByDOW(w, false, stdName, stdFromOffset, stdToOffset, stdMonth, stdWeekInMonth, stdDayOfWeek, stdStartTime, stdUntilTime);
                    }
                }
                else if (stdCount == 1) {
                    writeFinalRule(w, false, finalStdRule, stdFromOffset - stdFromDSTSavings, stdFromDSTSavings, stdStartTime);
                }
                else if (isEquivalentDateRule(stdMonth, stdWeekInMonth, stdDayOfWeek, finalStdRule.getRule())) {
                    writeZonePropsByDOW(w, false, stdName, stdFromOffset, stdToOffset, stdMonth, stdWeekInMonth, stdDayOfWeek, stdStartTime, Long.MAX_VALUE);
                }
                else {
                    writeZonePropsByDOW(w, false, stdName, stdFromOffset, stdToOffset, stdMonth, stdWeekInMonth, stdDayOfWeek, stdStartTime, stdUntilTime);
                    writeFinalRule(w, false, finalStdRule, stdFromOffset - stdFromDSTSavings, stdFromDSTSavings, stdStartTime);
                }
            }
        }
        writeFooter(w);
    }
    
    private static boolean isEquivalentDateRule(final int month, final int weekInMonth, final int dayOfWeek, final DateTimeRule dtrule) {
        if (month != dtrule.getRuleMonth() || dayOfWeek != dtrule.getRuleDayOfWeek()) {
            return false;
        }
        if (dtrule.getTimeRuleType() != 0) {
            return false;
        }
        if (dtrule.getDateRuleType() == 1 && dtrule.getRuleWeekInMonth() == weekInMonth) {
            return true;
        }
        final int ruleDOM = dtrule.getRuleDayOfMonth();
        if (dtrule.getDateRuleType() == 2) {
            if (ruleDOM % 7 == 1 && (ruleDOM + 6) / 7 == weekInMonth) {
                return true;
            }
            if (month != 1 && (VTimeZone.MONTHLENGTH[month] - ruleDOM) % 7 == 6 && weekInMonth == -1 * ((VTimeZone.MONTHLENGTH[month] - ruleDOM + 1) / 7)) {
                return true;
            }
        }
        if (dtrule.getDateRuleType() == 3) {
            if (ruleDOM % 7 == 0 && ruleDOM / 7 == weekInMonth) {
                return true;
            }
            if (month != 1 && (VTimeZone.MONTHLENGTH[month] - ruleDOM) % 7 == 0 && weekInMonth == -1 * ((VTimeZone.MONTHLENGTH[month] - ruleDOM) / 7 + 1)) {
                return true;
            }
        }
        return false;
    }
    
    private static void writeZonePropsByTime(final Writer writer, final boolean isDst, final String tzname, final int fromOffset, final int toOffset, final long time, final boolean withRDATE) throws IOException {
        beginZoneProps(writer, isDst, tzname, fromOffset, toOffset, time);
        if (withRDATE) {
            writer.write("RDATE");
            writer.write(":");
            writer.write(getDateTimeString(time + fromOffset));
            writer.write("\r\n");
        }
        endZoneProps(writer, isDst);
    }
    
    private static void writeZonePropsByDOM(final Writer writer, final boolean isDst, final String tzname, final int fromOffset, final int toOffset, final int month, final int dayOfMonth, final long startTime, final long untilTime) throws IOException {
        beginZoneProps(writer, isDst, tzname, fromOffset, toOffset, startTime);
        beginRRULE(writer, month);
        writer.write("BYMONTHDAY");
        writer.write("=");
        writer.write(Integer.toString(dayOfMonth));
        if (untilTime != Long.MAX_VALUE) {
            appendUNTIL(writer, getDateTimeString(untilTime + fromOffset));
        }
        writer.write("\r\n");
        endZoneProps(writer, isDst);
    }
    
    private static void writeZonePropsByDOW(final Writer writer, final boolean isDst, final String tzname, final int fromOffset, final int toOffset, final int month, final int weekInMonth, final int dayOfWeek, final long startTime, final long untilTime) throws IOException {
        beginZoneProps(writer, isDst, tzname, fromOffset, toOffset, startTime);
        beginRRULE(writer, month);
        writer.write("BYDAY");
        writer.write("=");
        writer.write(Integer.toString(weekInMonth));
        writer.write(VTimeZone.ICAL_DOW_NAMES[dayOfWeek - 1]);
        if (untilTime != Long.MAX_VALUE) {
            appendUNTIL(writer, getDateTimeString(untilTime + fromOffset));
        }
        writer.write("\r\n");
        endZoneProps(writer, isDst);
    }
    
    private static void writeZonePropsByDOW_GEQ_DOM(final Writer writer, final boolean isDst, final String tzname, final int fromOffset, final int toOffset, final int month, final int dayOfMonth, final int dayOfWeek, final long startTime, final long untilTime) throws IOException {
        if (dayOfMonth % 7 == 1) {
            writeZonePropsByDOW(writer, isDst, tzname, fromOffset, toOffset, month, (dayOfMonth + 6) / 7, dayOfWeek, startTime, untilTime);
        }
        else if (month != 1 && (VTimeZone.MONTHLENGTH[month] - dayOfMonth) % 7 == 6) {
            writeZonePropsByDOW(writer, isDst, tzname, fromOffset, toOffset, month, -1 * ((VTimeZone.MONTHLENGTH[month] - dayOfMonth + 1) / 7), dayOfWeek, startTime, untilTime);
        }
        else {
            beginZoneProps(writer, isDst, tzname, fromOffset, toOffset, startTime);
            int startDay = dayOfMonth;
            int currentMonthDays = 7;
            if (dayOfMonth <= 0) {
                final int prevMonthDays = 1 - dayOfMonth;
                currentMonthDays -= prevMonthDays;
                final int prevMonth = (month - 1 < 0) ? 11 : (month - 1);
                writeZonePropsByDOW_GEQ_DOM_sub(writer, prevMonth, -prevMonthDays, dayOfWeek, prevMonthDays, Long.MAX_VALUE, fromOffset);
                startDay = 1;
            }
            else if (dayOfMonth + 6 > VTimeZone.MONTHLENGTH[month]) {
                final int nextMonthDays = dayOfMonth + 6 - VTimeZone.MONTHLENGTH[month];
                currentMonthDays -= nextMonthDays;
                final int nextMonth = (month + 1 > 11) ? 0 : (month + 1);
                writeZonePropsByDOW_GEQ_DOM_sub(writer, nextMonth, 1, dayOfWeek, nextMonthDays, Long.MAX_VALUE, fromOffset);
            }
            writeZonePropsByDOW_GEQ_DOM_sub(writer, month, startDay, dayOfWeek, currentMonthDays, untilTime, fromOffset);
            endZoneProps(writer, isDst);
        }
    }
    
    private static void writeZonePropsByDOW_GEQ_DOM_sub(final Writer writer, final int month, final int dayOfMonth, final int dayOfWeek, final int numDays, final long untilTime, final int fromOffset) throws IOException {
        int startDayNum = dayOfMonth;
        final boolean isFeb = month == 1;
        if (dayOfMonth < 0 && !isFeb) {
            startDayNum = VTimeZone.MONTHLENGTH[month] + dayOfMonth + 1;
        }
        beginRRULE(writer, month);
        writer.write("BYDAY");
        writer.write("=");
        writer.write(VTimeZone.ICAL_DOW_NAMES[dayOfWeek - 1]);
        writer.write(";");
        writer.write("BYMONTHDAY");
        writer.write("=");
        writer.write(Integer.toString(startDayNum));
        for (int i = 1; i < numDays; ++i) {
            writer.write(",");
            writer.write(Integer.toString(startDayNum + i));
        }
        if (untilTime != Long.MAX_VALUE) {
            appendUNTIL(writer, getDateTimeString(untilTime + fromOffset));
        }
        writer.write("\r\n");
    }
    
    private static void writeZonePropsByDOW_LEQ_DOM(final Writer writer, final boolean isDst, final String tzname, final int fromOffset, final int toOffset, final int month, final int dayOfMonth, final int dayOfWeek, final long startTime, final long untilTime) throws IOException {
        if (dayOfMonth % 7 == 0) {
            writeZonePropsByDOW(writer, isDst, tzname, fromOffset, toOffset, month, dayOfMonth / 7, dayOfWeek, startTime, untilTime);
        }
        else if (month != 1 && (VTimeZone.MONTHLENGTH[month] - dayOfMonth) % 7 == 0) {
            writeZonePropsByDOW(writer, isDst, tzname, fromOffset, toOffset, month, -1 * ((VTimeZone.MONTHLENGTH[month] - dayOfMonth) / 7 + 1), dayOfWeek, startTime, untilTime);
        }
        else if (month == 1 && dayOfMonth == 29) {
            writeZonePropsByDOW(writer, isDst, tzname, fromOffset, toOffset, 1, -1, dayOfWeek, startTime, untilTime);
        }
        else {
            writeZonePropsByDOW_GEQ_DOM(writer, isDst, tzname, fromOffset, toOffset, month, dayOfMonth - 6, dayOfWeek, startTime, untilTime);
        }
    }
    
    private static void writeFinalRule(final Writer writer, final boolean isDst, final AnnualTimeZoneRule rule, final int fromRawOffset, final int fromDSTSavings, long startTime) throws IOException {
        final DateTimeRule dtrule = toWallTimeRule(rule.getRule(), fromRawOffset, fromDSTSavings);
        final int timeInDay = dtrule.getRuleMillisInDay();
        if (timeInDay < 0) {
            startTime += 0 - timeInDay;
        }
        else if (timeInDay >= 86400000) {
            startTime -= timeInDay - 86399999;
        }
        final int toOffset = rule.getRawOffset() + rule.getDSTSavings();
        switch (dtrule.getDateRuleType()) {
            case 0: {
                writeZonePropsByDOM(writer, isDst, rule.getName(), fromRawOffset + fromDSTSavings, toOffset, dtrule.getRuleMonth(), dtrule.getRuleDayOfMonth(), startTime, Long.MAX_VALUE);
                break;
            }
            case 1: {
                writeZonePropsByDOW(writer, isDst, rule.getName(), fromRawOffset + fromDSTSavings, toOffset, dtrule.getRuleMonth(), dtrule.getRuleWeekInMonth(), dtrule.getRuleDayOfWeek(), startTime, Long.MAX_VALUE);
                break;
            }
            case 2: {
                writeZonePropsByDOW_GEQ_DOM(writer, isDst, rule.getName(), fromRawOffset + fromDSTSavings, toOffset, dtrule.getRuleMonth(), dtrule.getRuleDayOfMonth(), dtrule.getRuleDayOfWeek(), startTime, Long.MAX_VALUE);
                break;
            }
            case 3: {
                writeZonePropsByDOW_LEQ_DOM(writer, isDst, rule.getName(), fromRawOffset + fromDSTSavings, toOffset, dtrule.getRuleMonth(), dtrule.getRuleDayOfMonth(), dtrule.getRuleDayOfWeek(), startTime, Long.MAX_VALUE);
                break;
            }
        }
    }
    
    private static DateTimeRule toWallTimeRule(final DateTimeRule rule, final int rawOffset, final int dstSavings) {
        if (rule.getTimeRuleType() == 0) {
            return rule;
        }
        int wallt = rule.getRuleMillisInDay();
        if (rule.getTimeRuleType() == 2) {
            wallt += rawOffset + dstSavings;
        }
        else if (rule.getTimeRuleType() == 1) {
            wallt += dstSavings;
        }
        int month = -1;
        int dom = 0;
        int dow = 0;
        int dtype = -1;
        int dshift = 0;
        if (wallt < 0) {
            dshift = -1;
            wallt += 86400000;
        }
        else if (wallt >= 86400000) {
            dshift = 1;
            wallt -= 86400000;
        }
        month = rule.getRuleMonth();
        dom = rule.getRuleDayOfMonth();
        dow = rule.getRuleDayOfWeek();
        dtype = rule.getDateRuleType();
        if (dshift != 0) {
            if (dtype == 1) {
                final int wim = rule.getRuleWeekInMonth();
                if (wim > 0) {
                    dtype = 2;
                    dom = 7 * (wim - 1) + 1;
                }
                else {
                    dtype = 3;
                    dom = VTimeZone.MONTHLENGTH[month] + 7 * (wim + 1);
                }
            }
            dom += dshift;
            if (dom == 0) {
                month = ((--month < 0) ? 11 : month);
                dom = VTimeZone.MONTHLENGTH[month];
            }
            else if (dom > VTimeZone.MONTHLENGTH[month]) {
                month = ((++month > 11) ? 0 : month);
                dom = 1;
            }
            if (dtype != 0) {
                dow += dshift;
                if (dow < 1) {
                    dow = 7;
                }
                else if (dow > 7) {
                    dow = 1;
                }
            }
        }
        DateTimeRule modifiedRule;
        if (dtype == 0) {
            modifiedRule = new DateTimeRule(month, dom, wallt, 0);
        }
        else {
            modifiedRule = new DateTimeRule(month, dom, dow, dtype == 2, wallt, 0);
        }
        return modifiedRule;
    }
    
    private static void beginZoneProps(final Writer writer, final boolean isDst, final String tzname, final int fromOffset, final int toOffset, final long startTime) throws IOException {
        writer.write("BEGIN");
        writer.write(":");
        if (isDst) {
            writer.write("DAYLIGHT");
        }
        else {
            writer.write("STANDARD");
        }
        writer.write("\r\n");
        writer.write("TZOFFSETTO");
        writer.write(":");
        writer.write(millisToOffset(toOffset));
        writer.write("\r\n");
        writer.write("TZOFFSETFROM");
        writer.write(":");
        writer.write(millisToOffset(fromOffset));
        writer.write("\r\n");
        writer.write("TZNAME");
        writer.write(":");
        writer.write(tzname);
        writer.write("\r\n");
        writer.write("DTSTART");
        writer.write(":");
        writer.write(getDateTimeString(startTime + fromOffset));
        writer.write("\r\n");
    }
    
    private static void endZoneProps(final Writer writer, final boolean isDst) throws IOException {
        writer.write("END");
        writer.write(":");
        if (isDst) {
            writer.write("DAYLIGHT");
        }
        else {
            writer.write("STANDARD");
        }
        writer.write("\r\n");
    }
    
    private static void beginRRULE(final Writer writer, final int month) throws IOException {
        writer.write("RRULE");
        writer.write(":");
        writer.write("FREQ");
        writer.write("=");
        writer.write("YEARLY");
        writer.write(";");
        writer.write("BYMONTH");
        writer.write("=");
        writer.write(Integer.toString(month + 1));
        writer.write(";");
    }
    
    private static void appendUNTIL(final Writer writer, final String until) throws IOException {
        if (until != null) {
            writer.write(";");
            writer.write("UNTIL");
            writer.write("=");
            writer.write(until);
        }
    }
    
    private void writeHeader(final Writer writer) throws IOException {
        writer.write("BEGIN");
        writer.write(":");
        writer.write("VTIMEZONE");
        writer.write("\r\n");
        writer.write("TZID");
        writer.write(":");
        writer.write(this.tz.getID());
        writer.write("\r\n");
        if (this.tzurl != null) {
            writer.write("TZURL");
            writer.write(":");
            writer.write(this.tzurl);
            writer.write("\r\n");
        }
        if (this.lastmod != null) {
            writer.write("LAST-MODIFIED");
            writer.write(":");
            writer.write(getUTCDateTimeString(this.lastmod.getTime()));
            writer.write("\r\n");
        }
    }
    
    private static void writeFooter(final Writer writer) throws IOException {
        writer.write("END");
        writer.write(":");
        writer.write("VTIMEZONE");
        writer.write("\r\n");
    }
    
    private static String getDateTimeString(final long time) {
        final int[] fields = Grego.timeToFields(time, null);
        final StringBuilder sb = new StringBuilder(15);
        sb.append(numToString(fields[0], 4));
        sb.append(numToString(fields[1] + 1, 2));
        sb.append(numToString(fields[2], 2));
        sb.append('T');
        int t = fields[5];
        final int hour = t / 3600000;
        t %= 3600000;
        final int min = t / 60000;
        t %= 60000;
        final int sec = t / 1000;
        sb.append(numToString(hour, 2));
        sb.append(numToString(min, 2));
        sb.append(numToString(sec, 2));
        return sb.toString();
    }
    
    private static String getUTCDateTimeString(final long time) {
        return getDateTimeString(time) + "Z";
    }
    
    private static long parseDateTimeString(final String str, final int offset) {
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int min = 0;
        int sec = 0;
        boolean isUTC = false;
        boolean isValid = false;
        Label_0249: {
            if (str != null) {
                final int length = str.length();
                if (length == 15 || length == 16) {
                    if (str.charAt(8) == 'T') {
                        if (length == 16) {
                            if (str.charAt(15) != 'Z') {
                                break Label_0249;
                            }
                            isUTC = true;
                        }
                        try {
                            year = Integer.parseInt(str.substring(0, 4));
                            month = Integer.parseInt(str.substring(4, 6)) - 1;
                            day = Integer.parseInt(str.substring(6, 8));
                            hour = Integer.parseInt(str.substring(9, 11));
                            min = Integer.parseInt(str.substring(11, 13));
                            sec = Integer.parseInt(str.substring(13, 15));
                        }
                        catch (NumberFormatException nfe) {
                            break Label_0249;
                        }
                        final int maxDayOfMonth = Grego.monthLength(year, month);
                        if (year >= 0 && month >= 0 && month <= 11 && day >= 1 && day <= maxDayOfMonth && hour >= 0 && hour < 24 && min >= 0 && min < 60 && sec >= 0) {
                            if (sec < 60) {
                                isValid = true;
                            }
                        }
                    }
                }
            }
        }
        if (!isValid) {
            throw new IllegalArgumentException("Invalid date time string format");
        }
        long time = Grego.fieldsToDay(year, month, day) * 86400000L;
        time += hour * 3600000 + min * 60000 + sec * 1000;
        if (!isUTC) {
            time -= offset;
        }
        return time;
    }
    
    private static int offsetStrToMillis(final String str) {
        boolean isValid = false;
        int sign = 0;
        int hour = 0;
        int min = 0;
        int sec = 0;
        Label_0119: {
            if (str != null) {
                final int length = str.length();
                if (length == 5 || length == 7) {
                    final char s = str.charAt(0);
                    if (s == '+') {
                        sign = 1;
                    }
                    else {
                        if (s != '-') {
                            break Label_0119;
                        }
                        sign = -1;
                    }
                    try {
                        hour = Integer.parseInt(str.substring(1, 3));
                        min = Integer.parseInt(str.substring(3, 5));
                        if (length == 7) {
                            sec = Integer.parseInt(str.substring(5, 7));
                        }
                    }
                    catch (NumberFormatException nfe) {
                        break Label_0119;
                    }
                    isValid = true;
                }
            }
        }
        if (!isValid) {
            throw new IllegalArgumentException("Bad offset string");
        }
        final int millis = sign * ((hour * 60 + min) * 60 + sec) * 1000;
        return millis;
    }
    
    private static String millisToOffset(int millis) {
        final StringBuilder sb = new StringBuilder(7);
        if (millis >= 0) {
            sb.append('+');
        }
        else {
            sb.append('-');
            millis = -millis;
        }
        int t = millis / 1000;
        final int sec = t % 60;
        t = (t - sec) / 60;
        final int min = t % 60;
        final int hour = t / 60;
        sb.append(numToString(hour, 2));
        sb.append(numToString(min, 2));
        sb.append(numToString(sec, 2));
        return sb.toString();
    }
    
    private static String numToString(final int num, final int width) {
        final String str = Integer.toString(num);
        final int len = str.length();
        if (len >= width) {
            return str.substring(len - width, len);
        }
        final StringBuilder sb = new StringBuilder(width);
        for (int i = len; i < width; ++i) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
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
        final VTimeZone vtz = (VTimeZone)super.cloneAsThawed();
        vtz.tz = (BasicTimeZone)this.tz.cloneAsThawed();
        vtz.isFrozen = false;
        return vtz;
    }
    
    static {
        ICAL_DOW_NAMES = new String[] { "SU", "MO", "TU", "WE", "TH", "FR", "SA" };
        MONTHLENGTH = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        try {
            VTimeZone.ICU_TZVERSION = TimeZone.getTZDataVersion();
        }
        catch (MissingResourceException e) {
            VTimeZone.ICU_TZVERSION = null;
        }
    }
}
