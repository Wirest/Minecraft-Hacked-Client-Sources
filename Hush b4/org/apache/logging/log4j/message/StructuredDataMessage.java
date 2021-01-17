// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.EnglishEnums;
import java.util.Map;

public class StructuredDataMessage extends MapMessage
{
    private static final long serialVersionUID = 1703221292892071920L;
    private static final int MAX_LENGTH = 32;
    private static final int HASHVAL = 31;
    private StructuredDataId id;
    private String message;
    private String type;
    
    public StructuredDataMessage(final String id, final String msg, final String type) {
        this.id = new StructuredDataId(id, null, null);
        this.message = msg;
        this.type = type;
    }
    
    public StructuredDataMessage(final String id, final String msg, final String type, final Map<String, String> data) {
        super(data);
        this.id = new StructuredDataId(id, null, null);
        this.message = msg;
        this.type = type;
    }
    
    public StructuredDataMessage(final StructuredDataId id, final String msg, final String type) {
        this.id = id;
        this.message = msg;
        this.type = type;
    }
    
    public StructuredDataMessage(final StructuredDataId id, final String msg, final String type, final Map<String, String> data) {
        super(data);
        this.id = id;
        this.message = msg;
        this.type = type;
    }
    
    private StructuredDataMessage(final StructuredDataMessage msg, final Map<String, String> map) {
        super(map);
        this.id = msg.id;
        this.message = msg.message;
        this.type = msg.type;
    }
    
    protected StructuredDataMessage() {
    }
    
    @Override
    public String[] getFormats() {
        final String[] formats = new String[Format.values().length];
        int i = 0;
        for (final Format format : Format.values()) {
            formats[i++] = format.name();
        }
        return formats;
    }
    
    public StructuredDataId getId() {
        return this.id;
    }
    
    protected void setId(final String id) {
        this.id = new StructuredDataId(id, null, null);
    }
    
    protected void setId(final StructuredDataId id) {
        this.id = id;
    }
    
    public String getType() {
        return this.type;
    }
    
    protected void setType(final String type) {
        if (type.length() > 32) {
            throw new IllegalArgumentException("structured data type exceeds maximum length of 32 characters: " + type);
        }
        this.type = type;
    }
    
    @Override
    public String getFormat() {
        return this.message;
    }
    
    protected void setMessageFormat(final String msg) {
        this.message = msg;
    }
    
    @Override
    protected void validate(final String key, final String value) {
        this.validateKey(key);
    }
    
    private void validateKey(final String key) {
        if (key.length() > 32) {
            throw new IllegalArgumentException("Structured data keys are limited to 32 characters. key: " + key);
        }
        final char[] arr$;
        final char[] chars = arr$ = key.toCharArray();
        for (final char c : arr$) {
            if (c < '!' || c > '~' || c == '=' || c == ']' || c == '\"') {
                throw new IllegalArgumentException("Structured data keys must contain printable US ASCII charactersand may not contain a space, =, ], or \"");
            }
        }
    }
    
    @Override
    public String asString() {
        return this.asString(Format.FULL, null);
    }
    
    @Override
    public String asString(final String format) {
        try {
            return this.asString(EnglishEnums.valueOf(Format.class, format), null);
        }
        catch (IllegalArgumentException ex) {
            return this.asString();
        }
    }
    
    public final String asString(final Format format, final StructuredDataId structuredDataId) {
        final StringBuilder sb = new StringBuilder();
        final boolean full = Format.FULL.equals(format);
        if (full) {
            final String type = this.getType();
            if (type == null) {
                return sb.toString();
            }
            sb.append(this.getType()).append(" ");
        }
        StructuredDataId id = this.getId();
        if (id != null) {
            id = id.makeId(structuredDataId);
        }
        else {
            id = structuredDataId;
        }
        if (id == null || id.getName() == null) {
            return sb.toString();
        }
        sb.append("[");
        sb.append(id);
        sb.append(" ");
        this.appendMap(sb);
        sb.append("]");
        if (full) {
            final String msg = this.getFormat();
            if (msg != null) {
                sb.append(" ").append(msg);
            }
        }
        return sb.toString();
    }
    
    @Override
    public String getFormattedMessage() {
        return this.asString(Format.FULL, null);
    }
    
    @Override
    public String getFormattedMessage(final String[] formats) {
        if (formats != null && formats.length > 0) {
            for (final String format : formats) {
                if (Format.XML.name().equalsIgnoreCase(format)) {
                    return this.asXML();
                }
                if (Format.FULL.name().equalsIgnoreCase(format)) {
                    return this.asString(Format.FULL, null);
                }
            }
            return this.asString(null, null);
        }
        return this.asString(Format.FULL, null);
    }
    
    private String asXML() {
        final StringBuilder sb = new StringBuilder();
        final StructuredDataId id = this.getId();
        if (id == null || id.getName() == null || this.type == null) {
            return sb.toString();
        }
        sb.append("<StructuredData>\n");
        sb.append("<type>").append(this.type).append("</type>\n");
        sb.append("<id>").append(id).append("</id>\n");
        super.asXML(sb);
        sb.append("</StructuredData>\n");
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.asString(null, null);
    }
    
    @Override
    public MapMessage newInstance(final Map<String, String> map) {
        return new StructuredDataMessage(this, map);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final StructuredDataMessage that = (StructuredDataMessage)o;
        if (!super.equals(o)) {
            return false;
        }
        Label_0072: {
            if (this.type != null) {
                if (this.type.equals(that.type)) {
                    break Label_0072;
                }
            }
            else if (that.type == null) {
                break Label_0072;
            }
            return false;
        }
        Label_0105: {
            if (this.id != null) {
                if (this.id.equals(that.id)) {
                    break Label_0105;
                }
            }
            else if (that.id == null) {
                break Label_0105;
            }
            return false;
        }
        if (this.message != null) {
            if (this.message.equals(that.message)) {
                return true;
            }
        }
        else if (that.message == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ((this.type != null) ? this.type.hashCode() : 0);
        result = 31 * result + ((this.id != null) ? this.id.hashCode() : 0);
        result = 31 * result + ((this.message != null) ? this.message.hashCode() : 0);
        return result;
    }
    
    public enum Format
    {
        XML, 
        FULL;
    }
}
