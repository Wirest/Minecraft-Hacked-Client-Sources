// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.regex.Matcher;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "RegexFilter", category = "Core", elementType = "filter", printObject = true)
public final class RegexFilter extends AbstractFilter
{
    private final Pattern pattern;
    private final boolean useRawMessage;
    
    private RegexFilter(final boolean raw, final Pattern pattern, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.pattern = pattern;
        this.useRawMessage = raw;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return this.filter(msg);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        if (msg == null) {
            return this.onMismatch;
        }
        return this.filter(msg.toString());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        if (msg == null) {
            return this.onMismatch;
        }
        final String text = this.useRawMessage ? msg.getFormat() : msg.getFormattedMessage();
        return this.filter(text);
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        final String text = this.useRawMessage ? event.getMessage().getFormat() : event.getMessage().getFormattedMessage();
        return this.filter(text);
    }
    
    private Filter.Result filter(final String msg) {
        if (msg == null) {
            return this.onMismatch;
        }
        final Matcher m = this.pattern.matcher(msg);
        return m.matches() ? this.onMatch : this.onMismatch;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("useRaw=").append(this.useRawMessage);
        sb.append(", pattern=").append(this.pattern.toString());
        return sb.toString();
    }
    
    @PluginFactory
    public static RegexFilter createFilter(@PluginAttribute("regex") final String regex, @PluginAttribute("useRawMsg") final String useRawMsg, @PluginAttribute("onMatch") final String match, @PluginAttribute("onMismatch") final String mismatch) {
        if (regex == null) {
            RegexFilter.LOGGER.error("A regular expression must be provided for RegexFilter");
            return null;
        }
        final boolean raw = Boolean.parseBoolean(useRawMsg);
        Pattern pattern;
        try {
            pattern = Pattern.compile(regex);
        }
        catch (Exception ex) {
            RegexFilter.LOGGER.error("RegexFilter caught exception compiling pattern: " + regex + " cause: " + ex.getMessage());
            return null;
        }
        final Filter.Result onMatch = Filter.Result.toResult(match);
        final Filter.Result onMismatch = Filter.Result.toResult(mismatch);
        return new RegexFilter(raw, pattern, onMatch, onMismatch);
    }
}
