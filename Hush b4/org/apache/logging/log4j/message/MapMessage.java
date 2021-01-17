// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import java.util.Iterator;
import org.apache.logging.log4j.util.EnglishEnums;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.SortedMap;

public class MapMessage implements MultiformatMessage
{
    private static final long serialVersionUID = -5031471831131487120L;
    private final SortedMap<String, String> data;
    
    public MapMessage() {
        this.data = new TreeMap<String, String>();
    }
    
    public MapMessage(final Map<String, String> map) {
        this.data = ((map instanceof SortedMap) ? ((SortedMap)map) : new TreeMap<String, String>(map));
    }
    
    @Override
    public String[] getFormats() {
        final String[] formats = new String[MapFormat.values().length];
        int i = 0;
        for (final MapFormat format : MapFormat.values()) {
            formats[i++] = format.name();
        }
        return formats;
    }
    
    @Override
    public Object[] getParameters() {
        return this.data.values().toArray();
    }
    
    @Override
    public String getFormat() {
        return "";
    }
    
    public Map<String, String> getData() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends String>)this.data);
    }
    
    public void clear() {
        this.data.clear();
    }
    
    public void put(final String key, final String value) {
        if (value == null) {
            throw new IllegalArgumentException("No value provided for key " + key);
        }
        this.validate(key, value);
        this.data.put(key, value);
    }
    
    protected void validate(final String key, final String value) {
    }
    
    public void putAll(final Map<String, String> map) {
        this.data.putAll((Map<?, ?>)map);
    }
    
    public String get(final String key) {
        return this.data.get(key);
    }
    
    public String remove(final String key) {
        return this.data.remove(key);
    }
    
    public String asString() {
        return this.asString((MapFormat)null);
    }
    
    public String asString(final String format) {
        try {
            return this.asString(EnglishEnums.valueOf(MapFormat.class, format));
        }
        catch (IllegalArgumentException ex) {
            return this.asString();
        }
    }
    
    private String asString(final MapFormat format) {
        final StringBuilder sb = new StringBuilder();
        if (format == null) {
            this.appendMap(sb);
        }
        else {
            switch (format) {
                case XML: {
                    this.asXML(sb);
                    break;
                }
                case JSON: {
                    this.asJSON(sb);
                    break;
                }
                case JAVA: {
                    this.asJava(sb);
                    break;
                }
                default: {
                    this.appendMap(sb);
                    break;
                }
            }
        }
        return sb.toString();
    }
    
    public void asXML(final StringBuilder sb) {
        sb.append("<Map>\n");
        for (final Map.Entry<String, String> entry : this.data.entrySet()) {
            sb.append("  <Entry key=\"").append(entry.getKey()).append("\">").append(entry.getValue()).append("</Entry>\n");
        }
        sb.append("</Map>");
    }
    
    @Override
    public String getFormattedMessage() {
        return this.asString();
    }
    
    @Override
    public String getFormattedMessage(final String[] formats) {
        if (formats == null || formats.length == 0) {
            return this.asString();
        }
        for (final String format : formats) {
            for (final MapFormat mapFormat : MapFormat.values()) {
                if (mapFormat.name().equalsIgnoreCase(format)) {
                    return this.asString(mapFormat);
                }
            }
        }
        return this.asString();
    }
    
    protected void appendMap(final StringBuilder sb) {
        boolean first = true;
        for (final Map.Entry<String, String> entry : this.data.entrySet()) {
            if (!first) {
                sb.append(" ");
            }
            first = false;
            sb.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
        }
    }
    
    protected void asJSON(final StringBuilder sb) {
        boolean first = true;
        sb.append("{");
        for (final Map.Entry<String, String> entry : this.data.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append("\"").append(entry.getKey()).append("\":");
            sb.append("\"").append(entry.getValue()).append("\"");
        }
        sb.append("}");
    }
    
    protected void asJava(final StringBuilder sb) {
        boolean first = true;
        sb.append("{");
        for (final Map.Entry<String, String> entry : this.data.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
        }
        sb.append("}");
    }
    
    public MapMessage newInstance(final Map<String, String> map) {
        return new MapMessage(map);
    }
    
    @Override
    public String toString() {
        return this.asString();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MapMessage that = (MapMessage)o;
        return this.data.equals(that.data);
    }
    
    @Override
    public int hashCode() {
        return this.data.hashCode();
    }
    
    @Override
    public Throwable getThrowable() {
        return null;
    }
    
    public enum MapFormat
    {
        XML, 
        JSON, 
        JAVA;
    }
}
