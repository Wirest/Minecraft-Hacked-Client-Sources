// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "replace", category = "Core", printObject = true)
public final class RegexReplacement
{
    private static final Logger LOGGER;
    private final Pattern pattern;
    private final String substitution;
    
    private RegexReplacement(final Pattern pattern, final String substitution) {
        this.pattern = pattern;
        this.substitution = substitution;
    }
    
    public String format(final String msg) {
        return this.pattern.matcher(msg).replaceAll(this.substitution);
    }
    
    @Override
    public String toString() {
        return "replace(regex=" + this.pattern.pattern() + ", replacement=" + this.substitution + ")";
    }
    
    @PluginFactory
    public static RegexReplacement createRegexReplacement(@PluginAttribute("regex") final String regex, @PluginAttribute("replacement") final String replacement) {
        if (regex == null) {
            RegexReplacement.LOGGER.error("A regular expression is required for replacement");
            return null;
        }
        if (replacement == null) {
            RegexReplacement.LOGGER.error("A replacement string is required to perform replacement");
        }
        final Pattern p = Pattern.compile(regex);
        return new RegexReplacement(p, replacement);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
