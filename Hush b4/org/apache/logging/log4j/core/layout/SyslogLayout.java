// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.net.UnknownHostException;
import java.net.InetAddress;
import org.apache.logging.log4j.core.net.Priority;
import org.apache.logging.log4j.core.LogEvent;
import java.util.regex.Matcher;
import java.util.Locale;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.core.net.Facility;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "SyslogLayout", category = "Core", elementType = "layout", printObject = true)
public class SyslogLayout extends AbstractStringLayout
{
    public static final Pattern NEWLINE_PATTERN;
    private final Facility facility;
    private final boolean includeNewLine;
    private final String escapeNewLine;
    private final SimpleDateFormat dateFormat;
    private final String localHostname;
    
    protected SyslogLayout(final Facility facility, final boolean includeNL, final String escapeNL, final Charset charset) {
        super(charset);
        this.dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss ", Locale.ENGLISH);
        this.localHostname = this.getLocalHostname();
        this.facility = facility;
        this.includeNewLine = includeNL;
        this.escapeNewLine = ((escapeNL == null) ? null : Matcher.quoteReplacement(escapeNL));
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilder buf = new StringBuilder();
        buf.append("<");
        buf.append(Priority.getPriority(this.facility, event.getLevel()));
        buf.append(">");
        this.addDate(event.getMillis(), buf);
        buf.append(" ");
        buf.append(this.localHostname);
        buf.append(" ");
        String message = event.getMessage().getFormattedMessage();
        if (null != this.escapeNewLine) {
            message = SyslogLayout.NEWLINE_PATTERN.matcher(message).replaceAll(this.escapeNewLine);
        }
        buf.append(message);
        if (this.includeNewLine) {
            buf.append("\n");
        }
        return buf.toString();
    }
    
    private String getLocalHostname() {
        try {
            final InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        }
        catch (UnknownHostException uhe) {
            SyslogLayout.LOGGER.error("Could not determine local host name", uhe);
            return "UNKNOWN_LOCALHOST";
        }
    }
    
    private synchronized void addDate(final long timestamp, final StringBuilder buf) {
        final int index = buf.length() + 4;
        buf.append(this.dateFormat.format(new Date(timestamp)));
        if (buf.charAt(index) == '0') {
            buf.setCharAt(index, ' ');
        }
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put("structured", "false");
        result.put("formatType", "logfilepatternreceiver");
        result.put("dateFormat", this.dateFormat.toPattern());
        result.put("format", "<LEVEL>TIMESTAMP PROP(HOSTNAME) MESSAGE");
        return result;
    }
    
    @PluginFactory
    public static SyslogLayout createLayout(@PluginAttribute("facility") final String facility, @PluginAttribute("newLine") final String includeNL, @PluginAttribute("newLineEscape") final String escapeNL, @PluginAttribute("charset") final String charsetName) {
        final Charset charset = Charsets.getSupportedCharset(charsetName);
        final boolean includeNewLine = Boolean.parseBoolean(includeNL);
        final Facility f = Facility.toFacility(facility, Facility.LOCAL0);
        return new SyslogLayout(f, includeNewLine, escapeNL, charset);
    }
    
    static {
        NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
    }
}
