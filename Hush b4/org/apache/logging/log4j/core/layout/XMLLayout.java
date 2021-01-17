// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.message.Message;
import java.util.Map;
import org.apache.logging.log4j.core.helpers.Throwables;
import org.apache.logging.log4j.message.MultiformatMessage;
import org.apache.logging.log4j.core.helpers.Transform;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.helpers.Strings;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "XMLLayout", category = "Core", elementType = "layout", printObject = true)
public class XMLLayout extends AbstractStringLayout
{
    private static final String XML_NAMESPACE = "http://logging.apache.org/log4j/2.0/events";
    private static final String ROOT_TAG = "Events";
    private static final int DEFAULT_SIZE = 256;
    private static final String DEFAULT_EOL = "\r\n";
    private static final String COMPACT_EOL = "";
    private static final String DEFAULT_INDENT = "  ";
    private static final String COMPACT_INDENT = "";
    private static final String DEFAULT_NS_PREFIX = "log4j";
    private static final String[] FORMATS;
    private final boolean locationInfo;
    private final boolean properties;
    private final boolean complete;
    private final String namespacePrefix;
    private final String eol;
    private final String indent1;
    private final String indent2;
    private final String indent3;
    
    protected XMLLayout(final boolean locationInfo, final boolean properties, final boolean complete, final boolean compact, final String nsPrefix, final Charset charset) {
        super(charset);
        this.locationInfo = locationInfo;
        this.properties = properties;
        this.complete = complete;
        this.eol = (compact ? "" : "\r\n");
        this.indent1 = (compact ? "" : "  ");
        this.indent2 = this.indent1 + this.indent1;
        this.indent3 = this.indent2 + this.indent1;
        this.namespacePrefix = (Strings.isEmpty(nsPrefix) ? "log4j" : nsPrefix) + ":";
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilder buf = new StringBuilder(256);
        buf.append(this.indent1);
        buf.append('<');
        if (!this.complete) {
            buf.append(this.namespacePrefix);
        }
        buf.append("Event logger=\"");
        String name = event.getLoggerName();
        if (name.isEmpty()) {
            name = "root";
        }
        buf.append(Transform.escapeHtmlTags(name));
        buf.append("\" timestamp=\"");
        buf.append(event.getMillis());
        buf.append("\" level=\"");
        buf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
        buf.append("\" thread=\"");
        buf.append(Transform.escapeHtmlTags(event.getThreadName()));
        buf.append("\">");
        buf.append(this.eol);
        final Message msg = event.getMessage();
        if (msg != null) {
            boolean xmlSupported = false;
            if (msg instanceof MultiformatMessage) {
                final String[] arr$;
                final String[] formats = arr$ = ((MultiformatMessage)msg).getFormats();
                for (final String format : arr$) {
                    if (format.equalsIgnoreCase("XML")) {
                        xmlSupported = true;
                        break;
                    }
                }
            }
            buf.append(this.indent2);
            buf.append('<');
            if (!this.complete) {
                buf.append(this.namespacePrefix);
            }
            buf.append("Message>");
            if (xmlSupported) {
                buf.append(((MultiformatMessage)msg).getFormattedMessage(XMLLayout.FORMATS));
            }
            else {
                buf.append("<![CDATA[");
                Transform.appendEscapingCDATA(buf, event.getMessage().getFormattedMessage());
                buf.append("]]>");
            }
            buf.append("</");
            if (!this.complete) {
                buf.append(this.namespacePrefix);
            }
            buf.append("Message>");
            buf.append(this.eol);
        }
        if (event.getContextStack().getDepth() > 0) {
            buf.append(this.indent2);
            buf.append('<');
            if (!this.complete) {
                buf.append(this.namespacePrefix);
            }
            buf.append("NDC><![CDATA[");
            Transform.appendEscapingCDATA(buf, event.getContextStack().toString());
            buf.append("]]></");
            if (!this.complete) {
                buf.append(this.namespacePrefix);
            }
            buf.append("NDC>");
            buf.append(this.eol);
        }
        final Throwable throwable = event.getThrown();
        if (throwable != null) {
            final List<String> s = Throwables.toStringList(throwable);
            buf.append(this.indent2);
            buf.append('<');
            if (!this.complete) {
                buf.append(this.namespacePrefix);
            }
            buf.append("Throwable><![CDATA[");
            for (final String str : s) {
                Transform.appendEscapingCDATA(buf, str);
                buf.append(this.eol);
            }
            buf.append("]]></");
            if (!this.complete) {
                buf.append(this.namespacePrefix);
            }
            buf.append("Throwable>");
            buf.append(this.eol);
        }
        if (this.locationInfo) {
            final StackTraceElement element = event.getSource();
            buf.append(this.indent2);
            buf.append('<');
            if (!this.complete) {
                buf.append(this.namespacePrefix);
            }
            buf.append("LocationInfo class=\"");
            buf.append(Transform.escapeHtmlTags(element.getClassName()));
            buf.append("\" method=\"");
            buf.append(Transform.escapeHtmlTags(element.getMethodName()));
            buf.append("\" file=\"");
            buf.append(Transform.escapeHtmlTags(element.getFileName()));
            buf.append("\" line=\"");
            buf.append(element.getLineNumber());
            buf.append("\"/>");
            buf.append(this.eol);
        }
        if (this.properties && event.getContextMap().size() > 0) {
            buf.append(this.indent2);
            buf.append('<');
            if (!this.complete) {
                buf.append(this.namespacePrefix);
            }
            buf.append("Properties>");
            buf.append(this.eol);
            for (final Map.Entry<String, String> entry : event.getContextMap().entrySet()) {
                buf.append(this.indent3);
                buf.append('<');
                if (!this.complete) {
                    buf.append(this.namespacePrefix);
                }
                buf.append("Data name=\"");
                buf.append(Transform.escapeHtmlTags(entry.getKey()));
                buf.append("\" value=\"");
                buf.append(Transform.escapeHtmlTags(String.valueOf(entry.getValue())));
                buf.append("\"/>");
                buf.append(this.eol);
            }
            buf.append(this.indent2);
            buf.append("</");
            if (!this.complete) {
                buf.append(this.namespacePrefix);
            }
            buf.append("Properties>");
            buf.append(this.eol);
        }
        buf.append(this.indent1);
        buf.append("</");
        if (!this.complete) {
            buf.append(this.namespacePrefix);
        }
        buf.append("Event>");
        buf.append(this.eol);
        return buf.toString();
    }
    
    @Override
    public byte[] getHeader() {
        if (!this.complete) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        buf.append("<?xml version=\"1.0\" encoding=\"");
        buf.append(this.getCharset().name());
        buf.append("\"?>");
        buf.append(this.eol);
        buf.append('<');
        buf.append("Events");
        buf.append(" xmlns=\"http://logging.apache.org/log4j/2.0/events\">");
        buf.append(this.eol);
        return buf.toString().getBytes(this.getCharset());
    }
    
    @Override
    public byte[] getFooter() {
        if (!this.complete) {
            return null;
        }
        return ("</Events>" + this.eol).getBytes(this.getCharset());
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put("xsd", "log4j-events.xsd");
        result.put("version", "2.0");
        return result;
    }
    
    @Override
    public String getContentType() {
        return "text/xml; charset=" + this.getCharset();
    }
    
    @PluginFactory
    public static XMLLayout createLayout(@PluginAttribute("locationInfo") final String locationInfo, @PluginAttribute("properties") final String properties, @PluginAttribute("complete") final String completeStr, @PluginAttribute("compact") final String compactStr, @PluginAttribute("namespacePrefix") final String namespacePrefix, @PluginAttribute("charset") final String charsetName) {
        final Charset charset = Charsets.getSupportedCharset(charsetName, Charsets.UTF_8);
        final boolean info = Boolean.parseBoolean(locationInfo);
        final boolean props = Boolean.parseBoolean(properties);
        final boolean complete = Boolean.parseBoolean(completeStr);
        final boolean compact = Boolean.parseBoolean(compactStr);
        return new XMLLayout(info, props, complete, compact, namespacePrefix, charset);
    }
    
    static {
        FORMATS = new String[] { "xml" };
    }
}
