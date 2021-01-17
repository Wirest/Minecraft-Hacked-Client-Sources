// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.Date;
import java.util.Iterator;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.io.Reader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.helpers.Transform;
import org.apache.logging.log4j.core.helpers.Constants;
import org.apache.logging.log4j.core.LogEvent;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "HTMLLayout", category = "Core", elementType = "layout", printObject = true)
public final class HTMLLayout extends AbstractStringLayout
{
    private static final int BUF_SIZE = 256;
    private static final String TRACE_PREFIX = "<br />&nbsp;&nbsp;&nbsp;&nbsp;";
    private static final String REGEXP;
    private static final String DEFAULT_TITLE = "Log4j Log Messages";
    private static final String DEFAULT_CONTENT_TYPE = "text/html";
    private final long jvmStartTime;
    private final boolean locationInfo;
    private final String title;
    private final String contentType;
    private final String font;
    private final String fontSize;
    private final String headerSize;
    
    private HTMLLayout(final boolean locationInfo, final String title, final String contentType, final Charset charset, final String font, final String fontSize, final String headerSize) {
        super(charset);
        this.jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        this.locationInfo = locationInfo;
        this.title = title;
        this.contentType = contentType;
        this.font = font;
        this.fontSize = fontSize;
        this.headerSize = headerSize;
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilder sbuf = new StringBuilder(256);
        sbuf.append(Constants.LINE_SEP).append("<tr>").append(Constants.LINE_SEP);
        sbuf.append("<td>");
        sbuf.append(event.getMillis() - this.jvmStartTime);
        sbuf.append("</td>").append(Constants.LINE_SEP);
        final String escapedThread = Transform.escapeHtmlTags(event.getThreadName());
        sbuf.append("<td title=\"").append(escapedThread).append(" thread\">");
        sbuf.append(escapedThread);
        sbuf.append("</td>").append(Constants.LINE_SEP);
        sbuf.append("<td title=\"Level\">");
        if (event.getLevel().equals(Level.DEBUG)) {
            sbuf.append("<font color=\"#339933\">");
            sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
            sbuf.append("</font>");
        }
        else if (event.getLevel().isAtLeastAsSpecificAs(Level.WARN)) {
            sbuf.append("<font color=\"#993300\"><strong>");
            sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
            sbuf.append("</strong></font>");
        }
        else {
            sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
        }
        sbuf.append("</td>").append(Constants.LINE_SEP);
        String escapedLogger = Transform.escapeHtmlTags(event.getLoggerName());
        if (escapedLogger.isEmpty()) {
            escapedLogger = "root";
        }
        sbuf.append("<td title=\"").append(escapedLogger).append(" logger\">");
        sbuf.append(escapedLogger);
        sbuf.append("</td>").append(Constants.LINE_SEP);
        if (this.locationInfo) {
            final StackTraceElement element = event.getSource();
            sbuf.append("<td>");
            sbuf.append(Transform.escapeHtmlTags(element.getFileName()));
            sbuf.append(':');
            sbuf.append(element.getLineNumber());
            sbuf.append("</td>").append(Constants.LINE_SEP);
        }
        sbuf.append("<td title=\"Message\">");
        sbuf.append(Transform.escapeHtmlTags(event.getMessage().getFormattedMessage()).replaceAll(HTMLLayout.REGEXP, "<br />"));
        sbuf.append("</td>").append(Constants.LINE_SEP);
        sbuf.append("</tr>").append(Constants.LINE_SEP);
        if (event.getContextStack().getDepth() > 0) {
            sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
            sbuf.append(";\" colspan=\"6\" ");
            sbuf.append("title=\"Nested Diagnostic Context\">");
            sbuf.append("NDC: ").append(Transform.escapeHtmlTags(event.getContextStack().toString()));
            sbuf.append("</td></tr>").append(Constants.LINE_SEP);
        }
        if (event.getContextMap().size() > 0) {
            sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
            sbuf.append(";\" colspan=\"6\" ");
            sbuf.append("title=\"Mapped Diagnostic Context\">");
            sbuf.append("MDC: ").append(Transform.escapeHtmlTags(event.getContextMap().toString()));
            sbuf.append("</td></tr>").append(Constants.LINE_SEP);
        }
        final Throwable throwable = event.getThrown();
        if (throwable != null) {
            sbuf.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : ").append(this.fontSize);
            sbuf.append(";\" colspan=\"6\">");
            this.appendThrowableAsHTML(throwable, sbuf);
            sbuf.append("</td></tr>").append(Constants.LINE_SEP);
        }
        return sbuf.toString();
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        return new HashMap<String, String>();
    }
    
    @Override
    public String getContentType() {
        return this.contentType;
    }
    
    private void appendThrowableAsHTML(final Throwable throwable, final StringBuilder sbuf) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
        }
        catch (RuntimeException ex2) {}
        pw.flush();
        final LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
        final ArrayList<String> lines = new ArrayList<String>();
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                lines.add(line);
            }
        }
        catch (IOException ex) {
            if (ex instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            lines.add(ex.toString());
        }
        boolean first = true;
        for (final String line2 : lines) {
            if (!first) {
                sbuf.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;");
            }
            else {
                first = false;
            }
            sbuf.append(Transform.escapeHtmlTags(line2));
            sbuf.append(Constants.LINE_SEP);
        }
    }
    
    @Override
    public byte[] getHeader() {
        final StringBuilder sbuf = new StringBuilder();
        sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" ");
        sbuf.append("\"http://www.w3.org/TR/html4/loose.dtd\">");
        sbuf.append(Constants.LINE_SEP);
        sbuf.append("<html>").append(Constants.LINE_SEP);
        sbuf.append("<head>").append(Constants.LINE_SEP);
        sbuf.append("<meta charset=\"").append(this.getCharset()).append("\"/>").append(Constants.LINE_SEP);
        sbuf.append("<title>").append(this.title).append("</title>").append(Constants.LINE_SEP);
        sbuf.append("<style type=\"text/css\">").append(Constants.LINE_SEP);
        sbuf.append("<!--").append(Constants.LINE_SEP);
        sbuf.append("body, table {font-family:").append(this.font).append("; font-size: ");
        sbuf.append(this.headerSize).append(";}").append(Constants.LINE_SEP);
        sbuf.append("th {background: #336699; color: #FFFFFF; text-align: left;}").append(Constants.LINE_SEP);
        sbuf.append("-->").append(Constants.LINE_SEP);
        sbuf.append("</style>").append(Constants.LINE_SEP);
        sbuf.append("</head>").append(Constants.LINE_SEP);
        sbuf.append("<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">").append(Constants.LINE_SEP);
        sbuf.append("<hr size=\"1\" noshade>").append(Constants.LINE_SEP);
        sbuf.append("Log session start time " + new Date() + "<br>").append(Constants.LINE_SEP);
        sbuf.append("<br>").append(Constants.LINE_SEP);
        sbuf.append("<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">");
        sbuf.append(Constants.LINE_SEP);
        sbuf.append("<tr>").append(Constants.LINE_SEP);
        sbuf.append("<th>Time</th>").append(Constants.LINE_SEP);
        sbuf.append("<th>Thread</th>").append(Constants.LINE_SEP);
        sbuf.append("<th>Level</th>").append(Constants.LINE_SEP);
        sbuf.append("<th>Logger</th>").append(Constants.LINE_SEP);
        if (this.locationInfo) {
            sbuf.append("<th>File:Line</th>").append(Constants.LINE_SEP);
        }
        sbuf.append("<th>Message</th>").append(Constants.LINE_SEP);
        sbuf.append("</tr>").append(Constants.LINE_SEP);
        return sbuf.toString().getBytes(this.getCharset());
    }
    
    @Override
    public byte[] getFooter() {
        final StringBuilder sbuf = new StringBuilder();
        sbuf.append("</table>").append(Constants.LINE_SEP);
        sbuf.append("<br>").append(Constants.LINE_SEP);
        sbuf.append("</body></html>");
        return sbuf.toString().getBytes(this.getCharset());
    }
    
    @PluginFactory
    public static HTMLLayout createLayout(@PluginAttribute("locationInfo") final String locationInfo, @PluginAttribute("title") String title, @PluginAttribute("contentType") String contentType, @PluginAttribute("charset") final String charsetName, @PluginAttribute("fontSize") String fontSize, @PluginAttribute("fontName") String font) {
        final Charset charset = Charsets.getSupportedCharset(charsetName, Charsets.UTF_8);
        if (font == null) {
            font = "arial,sans-serif";
        }
        final FontSize fs = FontSize.getFontSize(fontSize);
        fontSize = fs.getFontSize();
        final String headerSize = fs.larger().getFontSize();
        final boolean info = Boolean.parseBoolean(locationInfo);
        if (title == null) {
            title = "Log4j Log Messages";
        }
        if (contentType == null) {
            contentType = "text/html; charset=" + charset;
        }
        return new HTMLLayout(info, title, contentType, charset, font, fontSize, headerSize);
    }
    
    static {
        REGEXP = (Constants.LINE_SEP.equals("\n") ? "\n" : (Constants.LINE_SEP + "|\n"));
    }
    
    private enum FontSize
    {
        SMALLER("smaller"), 
        XXSMALL("xx-small"), 
        XSMALL("x-small"), 
        SMALL("small"), 
        MEDIUM("medium"), 
        LARGE("large"), 
        XLARGE("x-large"), 
        XXLARGE("xx-large"), 
        LARGER("larger");
        
        private final String size;
        
        private FontSize(final String size) {
            this.size = size;
        }
        
        public String getFontSize() {
            return this.size;
        }
        
        public static FontSize getFontSize(final String size) {
            for (final FontSize fontSize : values()) {
                if (fontSize.size.equals(size)) {
                    return fontSize;
                }
            }
            return FontSize.SMALL;
        }
        
        public FontSize larger() {
            return (this.ordinal() < FontSize.XXLARGE.ordinal()) ? values()[this.ordinal() + 1] : this;
        }
    }
}
