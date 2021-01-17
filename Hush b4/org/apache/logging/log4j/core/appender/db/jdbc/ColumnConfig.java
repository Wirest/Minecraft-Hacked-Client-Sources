// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Column", category = "Core", printObject = true)
public final class ColumnConfig
{
    private static final Logger LOGGER;
    private final String columnName;
    private final PatternLayout layout;
    private final String literalValue;
    private final boolean eventTimestamp;
    private final boolean unicode;
    private final boolean clob;
    
    private ColumnConfig(final String columnName, final PatternLayout layout, final String literalValue, final boolean eventDate, final boolean unicode, final boolean clob) {
        this.columnName = columnName;
        this.layout = layout;
        this.literalValue = literalValue;
        this.eventTimestamp = eventDate;
        this.unicode = unicode;
        this.clob = clob;
    }
    
    public String getColumnName() {
        return this.columnName;
    }
    
    public PatternLayout getLayout() {
        return this.layout;
    }
    
    public String getLiteralValue() {
        return this.literalValue;
    }
    
    public boolean isEventTimestamp() {
        return this.eventTimestamp;
    }
    
    public boolean isUnicode() {
        return this.unicode;
    }
    
    public boolean isClob() {
        return this.clob;
    }
    
    @Override
    public String toString() {
        return "{ name=" + this.columnName + ", layout=" + this.layout + ", literal=" + this.literalValue + ", timestamp=" + this.eventTimestamp + " }";
    }
    
    @PluginFactory
    public static ColumnConfig createColumnConfig(@PluginConfiguration final Configuration config, @PluginAttribute("name") final String name, @PluginAttribute("pattern") final String pattern, @PluginAttribute("literal") final String literalValue, @PluginAttribute("isEventTimestamp") final String eventTimestamp, @PluginAttribute("isUnicode") final String unicode, @PluginAttribute("isClob") final String clob) {
        if (Strings.isEmpty(name)) {
            ColumnConfig.LOGGER.error("The column config is not valid because it does not contain a column name.");
            return null;
        }
        final boolean isPattern = Strings.isNotEmpty(pattern);
        final boolean isLiteralValue = Strings.isNotEmpty(literalValue);
        final boolean isEventTimestamp = Boolean.parseBoolean(eventTimestamp);
        final boolean isUnicode = Booleans.parseBoolean(unicode, true);
        final boolean isClob = Boolean.parseBoolean(clob);
        if ((isPattern && isLiteralValue) || (isPattern && isEventTimestamp) || (isLiteralValue && isEventTimestamp)) {
            ColumnConfig.LOGGER.error("The pattern, literal, and isEventTimestamp attributes are mutually exclusive.");
            return null;
        }
        if (isEventTimestamp) {
            return new ColumnConfig(name, null, null, true, false, false);
        }
        if (isLiteralValue) {
            return new ColumnConfig(name, null, literalValue, false, false, false);
        }
        if (isPattern) {
            return new ColumnConfig(name, PatternLayout.createLayout(pattern, config, null, null, "false"), null, false, isUnicode, isClob);
        }
        ColumnConfig.LOGGER.error("To configure a column you must specify a pattern or literal or set isEventDate to true.");
        return null;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
