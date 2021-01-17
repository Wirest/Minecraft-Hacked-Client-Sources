// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.helpers.Strings;
import java.io.Serializable;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Integers;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.logging.log4j.LoggingException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.core.net.Priority;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.TLSSyslogFrame;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import java.util.Iterator;
import java.util.HashMap;
import org.apache.logging.log4j.core.pattern.PatternParser;
import java.util.ArrayList;
import org.apache.logging.log4j.core.helpers.NetUtils;
import java.util.regex.Matcher;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.Configuration;
import java.util.Map;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import java.util.List;
import org.apache.logging.log4j.message.StructuredDataId;
import org.apache.logging.log4j.core.net.Facility;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "RFC5424Layout", category = "Core", elementType = "layout", printObject = true)
public class RFC5424Layout extends AbstractStringLayout
{
    private static final String LF = "\n";
    public static final int DEFAULT_ENTERPRISE_NUMBER = 18060;
    public static final String DEFAULT_ID = "Audit";
    public static final Pattern NEWLINE_PATTERN;
    public static final Pattern PARAM_VALUE_ESCAPE_PATTERN;
    protected static final String DEFAULT_MDCID = "mdc";
    private static final int TWO_DIGITS = 10;
    private static final int THREE_DIGITS = 100;
    private static final int MILLIS_PER_MINUTE = 60000;
    private static final int MINUTES_PER_HOUR = 60;
    private static final String COMPONENT_KEY = "RFC5424-Converter";
    private final Facility facility;
    private final String defaultId;
    private final int enterpriseNumber;
    private final boolean includeMDC;
    private final String mdcId;
    private final StructuredDataId mdcSDID;
    private final String localHostName;
    private final String appName;
    private final String messageId;
    private final String configName;
    private final String mdcPrefix;
    private final String eventPrefix;
    private final List<String> mdcExcludes;
    private final List<String> mdcIncludes;
    private final List<String> mdcRequired;
    private final ListChecker checker;
    private final ListChecker noopChecker;
    private final boolean includeNewLine;
    private final String escapeNewLine;
    private final boolean useTLSMessageFormat;
    private long lastTimestamp;
    private String timestamppStr;
    private final List<PatternFormatter> exceptionFormatters;
    private final Map<String, FieldFormatter> fieldFormatters;
    
    private RFC5424Layout(final Configuration config, final Facility facility, final String id, final int ein, final boolean includeMDC, final boolean includeNL, final String escapeNL, final String mdcId, final String mdcPrefix, final String eventPrefix, final String appName, final String messageId, final String excludes, final String includes, final String required, final Charset charset, final String exceptionPattern, final boolean useTLSMessageFormat, final LoggerFields[] loggerFields) {
        super(charset);
        this.noopChecker = new NoopChecker();
        this.lastTimestamp = -1L;
        final PatternParser exceptionParser = createPatternParser(config, ThrowablePatternConverter.class);
        this.exceptionFormatters = ((exceptionPattern == null) ? null : exceptionParser.parse(exceptionPattern, false));
        this.facility = facility;
        this.defaultId = ((id == null) ? "Audit" : id);
        this.enterpriseNumber = ein;
        this.includeMDC = includeMDC;
        this.includeNewLine = includeNL;
        this.escapeNewLine = ((escapeNL == null) ? null : Matcher.quoteReplacement(escapeNL));
        this.mdcId = mdcId;
        this.mdcSDID = new StructuredDataId(mdcId, this.enterpriseNumber, null, null);
        this.mdcPrefix = mdcPrefix;
        this.eventPrefix = eventPrefix;
        this.appName = appName;
        this.messageId = messageId;
        this.useTLSMessageFormat = useTLSMessageFormat;
        this.localHostName = NetUtils.getLocalHostname();
        ListChecker c = null;
        if (excludes != null) {
            final String[] array = excludes.split(",");
            if (array.length > 0) {
                c = new ExcludeChecker();
                this.mdcExcludes = new ArrayList<String>(array.length);
                for (final String str : array) {
                    this.mdcExcludes.add(str.trim());
                }
            }
            else {
                this.mdcExcludes = null;
            }
        }
        else {
            this.mdcExcludes = null;
        }
        if (includes != null) {
            final String[] array = includes.split(",");
            if (array.length > 0) {
                c = new IncludeChecker();
                this.mdcIncludes = new ArrayList<String>(array.length);
                for (final String str : array) {
                    this.mdcIncludes.add(str.trim());
                }
            }
            else {
                this.mdcIncludes = null;
            }
        }
        else {
            this.mdcIncludes = null;
        }
        if (required != null) {
            final String[] array = required.split(",");
            if (array.length > 0) {
                this.mdcRequired = new ArrayList<String>(array.length);
                for (final String str : array) {
                    this.mdcRequired.add(str.trim());
                }
            }
            else {
                this.mdcRequired = null;
            }
        }
        else {
            this.mdcRequired = null;
        }
        this.checker = ((c != null) ? c : this.noopChecker);
        final String name = (config == null) ? null : config.getName();
        this.configName = ((name != null && name.length() > 0) ? name : null);
        this.fieldFormatters = this.createFieldFormatters(loggerFields, config);
    }
    
    private Map<String, FieldFormatter> createFieldFormatters(final LoggerFields[] loggerFields, final Configuration config) {
        final Map<String, FieldFormatter> sdIdMap = new HashMap<String, FieldFormatter>();
        if (loggerFields != null) {
            for (final LoggerFields lField : loggerFields) {
                final StructuredDataId key = (lField.getSdId() == null) ? this.mdcSDID : lField.getSdId();
                final Map<String, List<PatternFormatter>> sdParams = new HashMap<String, List<PatternFormatter>>();
                final Map<String, String> fields = lField.getMap();
                if (!fields.isEmpty()) {
                    final PatternParser fieldParser = createPatternParser(config, null);
                    for (final Map.Entry<String, String> entry : fields.entrySet()) {
                        final List<PatternFormatter> formatters = fieldParser.parse(entry.getValue(), false);
                        sdParams.put(entry.getKey(), formatters);
                    }
                    final FieldFormatter fieldFormatter = new FieldFormatter(sdParams, lField.getDiscardIfAllFieldsAreEmpty());
                    sdIdMap.put(key.toString(), fieldFormatter);
                }
            }
        }
        return (sdIdMap.size() > 0) ? sdIdMap : null;
    }
    
    private static PatternParser createPatternParser(final Configuration config, final Class<? extends PatternConverter> filterClass) {
        if (config == null) {
            return new PatternParser(config, "Converter", LogEventPatternConverter.class, filterClass);
        }
        PatternParser parser = config.getComponent("RFC5424-Converter");
        if (parser == null) {
            parser = new PatternParser(config, "Converter", ThrowablePatternConverter.class);
            config.addComponent("RFC5424-Converter", parser);
            parser = config.getComponent("RFC5424-Converter");
        }
        return parser;
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put("structured", "true");
        result.put("formatType", "RFC5424");
        return result;
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilder buf = new StringBuilder();
        this.appendPriority(buf, event.getLevel());
        this.appendTimestamp(buf, event.getMillis());
        this.appendSpace(buf);
        this.appendHostName(buf);
        this.appendSpace(buf);
        this.appendAppName(buf);
        this.appendSpace(buf);
        this.appendProcessId(buf);
        this.appendSpace(buf);
        this.appendMessageId(buf, event.getMessage());
        this.appendSpace(buf);
        this.appendStructuredElements(buf, event);
        this.appendMessage(buf, event);
        if (this.useTLSMessageFormat) {
            return new TLSSyslogFrame(buf.toString()).toString();
        }
        return buf.toString();
    }
    
    private void appendPriority(final StringBuilder buffer, final Level logLevel) {
        buffer.append("<");
        buffer.append(Priority.getPriority(this.facility, logLevel));
        buffer.append(">1 ");
    }
    
    private void appendTimestamp(final StringBuilder buffer, final long milliseconds) {
        buffer.append(this.computeTimeStampString(milliseconds));
    }
    
    private void appendSpace(final StringBuilder buffer) {
        buffer.append(" ");
    }
    
    private void appendHostName(final StringBuilder buffer) {
        buffer.append(this.localHostName);
    }
    
    private void appendAppName(final StringBuilder buffer) {
        if (this.appName != null) {
            buffer.append(this.appName);
        }
        else if (this.configName != null) {
            buffer.append(this.configName);
        }
        else {
            buffer.append("-");
        }
    }
    
    private void appendProcessId(final StringBuilder buffer) {
        buffer.append(this.getProcId());
    }
    
    private void appendMessageId(final StringBuilder buffer, final Message message) {
        final boolean isStructured = message instanceof StructuredDataMessage;
        final String type = isStructured ? ((StructuredDataMessage)message).getType() : null;
        if (type != null) {
            buffer.append(type);
        }
        else if (this.messageId != null) {
            buffer.append(this.messageId);
        }
        else {
            buffer.append("-");
        }
    }
    
    private void appendMessage(final StringBuilder buffer, final LogEvent event) {
        final Message message = event.getMessage();
        final String text = message.getFormat();
        if (text != null && text.length() > 0) {
            buffer.append(" ").append(this.escapeNewlines(text, this.escapeNewLine));
        }
        if (this.exceptionFormatters != null && event.getThrown() != null) {
            final StringBuilder exception = new StringBuilder("\n");
            for (final PatternFormatter formatter : this.exceptionFormatters) {
                formatter.format(event, exception);
            }
            buffer.append(this.escapeNewlines(exception.toString(), this.escapeNewLine));
        }
        if (this.includeNewLine) {
            buffer.append("\n");
        }
    }
    
    private void appendStructuredElements(final StringBuilder buffer, final LogEvent event) {
        final Message message = event.getMessage();
        final boolean isStructured = message instanceof StructuredDataMessage;
        if (!isStructured && this.fieldFormatters != null && this.fieldFormatters.size() == 0 && !this.includeMDC) {
            buffer.append("-");
            return;
        }
        final Map<String, StructuredDataElement> sdElements = new HashMap<String, StructuredDataElement>();
        final Map<String, String> contextMap = event.getContextMap();
        if (this.mdcRequired != null) {
            this.checkRequired(contextMap);
        }
        if (this.fieldFormatters != null) {
            for (final Map.Entry<String, FieldFormatter> sdElement : this.fieldFormatters.entrySet()) {
                final String sdId = sdElement.getKey();
                final StructuredDataElement elem = sdElement.getValue().format(event);
                sdElements.put(sdId, elem);
            }
        }
        if (this.includeMDC && contextMap.size() > 0) {
            if (sdElements.containsKey(this.mdcSDID.toString())) {
                final StructuredDataElement union = sdElements.get(this.mdcSDID.toString());
                union.union(contextMap);
                sdElements.put(this.mdcSDID.toString(), union);
            }
            else {
                final StructuredDataElement formattedContextMap = new StructuredDataElement(contextMap, false);
                sdElements.put(this.mdcSDID.toString(), formattedContextMap);
            }
        }
        if (isStructured) {
            final StructuredDataMessage data = (StructuredDataMessage)message;
            final Map<String, String> map = data.getData();
            final StructuredDataId id = data.getId();
            if (sdElements.containsKey(id.toString())) {
                final StructuredDataElement union2 = sdElements.get(id.toString());
                union2.union(map);
                sdElements.put(id.toString(), union2);
            }
            else {
                final StructuredDataElement formattedData = new StructuredDataElement(map, false);
                sdElements.put(id.toString(), formattedData);
            }
        }
        if (sdElements.size() == 0) {
            buffer.append("-");
            return;
        }
        for (final Map.Entry<String, StructuredDataElement> entry : sdElements.entrySet()) {
            this.formatStructuredElement(entry.getKey(), this.mdcPrefix, entry.getValue(), buffer, this.checker);
        }
    }
    
    private String escapeNewlines(final String text, final String escapeNewLine) {
        if (null == escapeNewLine) {
            return text;
        }
        return RFC5424Layout.NEWLINE_PATTERN.matcher(text).replaceAll(escapeNewLine);
    }
    
    protected String getProcId() {
        return "-";
    }
    
    protected List<String> getMdcExcludes() {
        return this.mdcExcludes;
    }
    
    protected List<String> getMdcIncludes() {
        return this.mdcIncludes;
    }
    
    private String computeTimeStampString(final long now) {
        final long last;
        synchronized (this) {
            last = this.lastTimestamp;
            if (now == this.lastTimestamp) {
                return this.timestamppStr;
            }
        }
        final StringBuilder buffer = new StringBuilder();
        final Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(now);
        buffer.append(Integer.toString(cal.get(1)));
        buffer.append("-");
        this.pad(cal.get(2) + 1, 10, buffer);
        buffer.append("-");
        this.pad(cal.get(5), 10, buffer);
        buffer.append("T");
        this.pad(cal.get(11), 10, buffer);
        buffer.append(":");
        this.pad(cal.get(12), 10, buffer);
        buffer.append(":");
        this.pad(cal.get(13), 10, buffer);
        final int millis = cal.get(14);
        if (millis != 0) {
            buffer.append('.');
            this.pad(millis, 100, buffer);
        }
        int tzmin = (cal.get(15) + cal.get(16)) / 60000;
        if (tzmin == 0) {
            buffer.append("Z");
        }
        else {
            if (tzmin < 0) {
                tzmin = -tzmin;
                buffer.append("-");
            }
            else {
                buffer.append("+");
            }
            final int tzhour = tzmin / 60;
            tzmin -= tzhour * 60;
            this.pad(tzhour, 10, buffer);
            buffer.append(":");
            this.pad(tzmin, 10, buffer);
        }
        synchronized (this) {
            if (last == this.lastTimestamp) {
                this.lastTimestamp = now;
                this.timestamppStr = buffer.toString();
            }
        }
        return buffer.toString();
    }
    
    private void pad(final int val, int max, final StringBuilder buf) {
        while (max > 1) {
            if (val < max) {
                buf.append("0");
            }
            max /= 10;
        }
        buf.append(Integer.toString(val));
    }
    
    private void formatStructuredElement(final String id, final String prefix, final StructuredDataElement data, final StringBuilder sb, final ListChecker checker) {
        if ((id == null && this.defaultId == null) || data.discard()) {
            return;
        }
        sb.append("[");
        sb.append(id);
        if (!this.mdcSDID.toString().equals(id)) {
            this.appendMap(prefix, data.getFields(), sb, this.noopChecker);
        }
        else {
            this.appendMap(prefix, data.getFields(), sb, checker);
        }
        sb.append("]");
    }
    
    private String getId(final StructuredDataId id) {
        final StringBuilder sb = new StringBuilder();
        if (id == null || id.getName() == null) {
            sb.append(this.defaultId);
        }
        else {
            sb.append(id.getName());
        }
        int ein = (id != null) ? id.getEnterpriseNumber() : this.enterpriseNumber;
        if (ein < 0) {
            ein = this.enterpriseNumber;
        }
        if (ein >= 0) {
            sb.append("@").append(ein);
        }
        return sb.toString();
    }
    
    private void checkRequired(final Map<String, String> map) {
        for (final String key : this.mdcRequired) {
            final String value = map.get(key);
            if (value == null) {
                throw new LoggingException("Required key " + key + " is missing from the " + this.mdcId);
            }
        }
    }
    
    private void appendMap(final String prefix, final Map<String, String> map, final StringBuilder sb, final ListChecker checker) {
        final SortedMap<String, String> sorted = new TreeMap<String, String>(map);
        for (final Map.Entry<String, String> entry : sorted.entrySet()) {
            if (checker.check(entry.getKey()) && entry.getValue() != null) {
                sb.append(" ");
                if (prefix != null) {
                    sb.append(prefix);
                }
                sb.append(this.escapeNewlines(this.escapeSDParams(entry.getKey()), this.escapeNewLine)).append("=\"").append(this.escapeNewlines(this.escapeSDParams(entry.getValue()), this.escapeNewLine)).append("\"");
            }
        }
    }
    
    private String escapeSDParams(final String value) {
        return RFC5424Layout.PARAM_VALUE_ESCAPE_PATTERN.matcher(value).replaceAll("\\\\$0");
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("facility=").append(this.facility.name());
        sb.append(" appName=").append(this.appName);
        sb.append(" defaultId=").append(this.defaultId);
        sb.append(" enterpriseNumber=").append(this.enterpriseNumber);
        sb.append(" newLine=").append(this.includeNewLine);
        sb.append(" includeMDC=").append(this.includeMDC);
        sb.append(" messageId=").append(this.messageId);
        return sb.toString();
    }
    
    @PluginFactory
    public static RFC5424Layout createLayout(@PluginAttribute("facility") final String facility, @PluginAttribute("id") final String id, @PluginAttribute("enterpriseNumber") final String ein, @PluginAttribute("includeMDC") final String includeMDC, @PluginAttribute("mdcId") String mdcId, @PluginAttribute("mdcPrefix") final String mdcPrefix, @PluginAttribute("eventPrefix") final String eventPrefix, @PluginAttribute("newLine") final String includeNL, @PluginAttribute("newLineEscape") final String escapeNL, @PluginAttribute("appName") final String appName, @PluginAttribute("messageId") final String msgId, @PluginAttribute("mdcExcludes") final String excludes, @PluginAttribute("mdcIncludes") String includes, @PluginAttribute("mdcRequired") final String required, @PluginAttribute("exceptionPattern") final String exceptionPattern, @PluginAttribute("useTLSMessageFormat") final String useTLSMessageFormat, @PluginElement("LoggerFields") final LoggerFields[] loggerFields, @PluginConfiguration final Configuration config) {
        final Charset charset = Charsets.UTF_8;
        if (includes != null && excludes != null) {
            RFC5424Layout.LOGGER.error("mdcIncludes and mdcExcludes are mutually exclusive. Includes wil be ignored");
            includes = null;
        }
        final Facility f = Facility.toFacility(facility, Facility.LOCAL0);
        final int enterpriseNumber = Integers.parseInt(ein, 18060);
        final boolean isMdc = Booleans.parseBoolean(includeMDC, true);
        final boolean includeNewLine = Boolean.parseBoolean(includeNL);
        final boolean useTlsMessageFormat = Booleans.parseBoolean(useTLSMessageFormat, false);
        if (mdcId == null) {
            mdcId = "mdc";
        }
        return new RFC5424Layout(config, f, id, enterpriseNumber, isMdc, includeNewLine, escapeNL, mdcId, mdcPrefix, eventPrefix, appName, msgId, excludes, includes, required, charset, exceptionPattern, useTlsMessageFormat, loggerFields);
    }
    
    static {
        NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
        PARAM_VALUE_ESCAPE_PATTERN = Pattern.compile("[\\\"\\]\\\\]");
    }
    
    private class IncludeChecker implements ListChecker
    {
        @Override
        public boolean check(final String key) {
            return RFC5424Layout.this.mdcIncludes.contains(key);
        }
    }
    
    private class ExcludeChecker implements ListChecker
    {
        @Override
        public boolean check(final String key) {
            return !RFC5424Layout.this.mdcExcludes.contains(key);
        }
    }
    
    private class NoopChecker implements ListChecker
    {
        @Override
        public boolean check(final String key) {
            return true;
        }
    }
    
    private class FieldFormatter
    {
        private final Map<String, List<PatternFormatter>> delegateMap;
        private final boolean discardIfEmpty;
        
        public FieldFormatter(final Map<String, List<PatternFormatter>> fieldMap, final boolean discardIfEmpty) {
            this.discardIfEmpty = discardIfEmpty;
            this.delegateMap = fieldMap;
        }
        
        public StructuredDataElement format(final LogEvent event) {
            final Map<String, String> map = new HashMap<String, String>();
            for (final Map.Entry<String, List<PatternFormatter>> entry : this.delegateMap.entrySet()) {
                final StringBuilder buffer = new StringBuilder();
                for (final PatternFormatter formatter : entry.getValue()) {
                    formatter.format(event, buffer);
                }
                map.put(entry.getKey(), buffer.toString());
            }
            return new StructuredDataElement(map, this.discardIfEmpty);
        }
    }
    
    private class StructuredDataElement
    {
        private final Map<String, String> fields;
        private final boolean discardIfEmpty;
        
        public StructuredDataElement(final Map<String, String> fields, final boolean discardIfEmpty) {
            this.discardIfEmpty = discardIfEmpty;
            this.fields = fields;
        }
        
        boolean discard() {
            if (!this.discardIfEmpty) {
                return false;
            }
            boolean foundNotEmptyValue = false;
            for (final Map.Entry<String, String> entry : this.fields.entrySet()) {
                if (Strings.isNotEmpty(entry.getValue())) {
                    foundNotEmptyValue = true;
                    break;
                }
            }
            return !foundNotEmptyValue;
        }
        
        void union(final Map<String, String> fields) {
            this.fields.putAll(fields);
        }
        
        Map<String, String> getFields() {
            return this.fields;
        }
    }
    
    private interface ListChecker
    {
        boolean check(final String p0);
    }
}
