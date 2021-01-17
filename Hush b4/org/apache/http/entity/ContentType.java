// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.entity;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.HeaderElement;
import java.util.Locale;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.Args;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.http.util.TextUtils;
import org.apache.http.NameValuePair;
import java.nio.charset.Charset;
import org.apache.http.annotation.Immutable;
import java.io.Serializable;

@Immutable
public final class ContentType implements Serializable
{
    private static final long serialVersionUID = -7768694718232371896L;
    public static final ContentType APPLICATION_ATOM_XML;
    public static final ContentType APPLICATION_FORM_URLENCODED;
    public static final ContentType APPLICATION_JSON;
    public static final ContentType APPLICATION_OCTET_STREAM;
    public static final ContentType APPLICATION_SVG_XML;
    public static final ContentType APPLICATION_XHTML_XML;
    public static final ContentType APPLICATION_XML;
    public static final ContentType MULTIPART_FORM_DATA;
    public static final ContentType TEXT_HTML;
    public static final ContentType TEXT_PLAIN;
    public static final ContentType TEXT_XML;
    public static final ContentType WILDCARD;
    public static final ContentType DEFAULT_TEXT;
    public static final ContentType DEFAULT_BINARY;
    private final String mimeType;
    private final Charset charset;
    private final NameValuePair[] params;
    
    ContentType(final String mimeType, final Charset charset) {
        this.mimeType = mimeType;
        this.charset = charset;
        this.params = null;
    }
    
    ContentType(final String mimeType, final NameValuePair[] params) throws UnsupportedCharsetException {
        this.mimeType = mimeType;
        this.params = params;
        final String s = this.getParameter("charset");
        this.charset = (TextUtils.isBlank(s) ? null : Charset.forName(s));
    }
    
    public String getMimeType() {
        return this.mimeType;
    }
    
    public Charset getCharset() {
        return this.charset;
    }
    
    public String getParameter(final String name) {
        Args.notEmpty(name, "Parameter name");
        if (this.params == null) {
            return null;
        }
        for (final NameValuePair param : this.params) {
            if (param.getName().equalsIgnoreCase(name)) {
                return param.getValue();
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        final CharArrayBuffer buf = new CharArrayBuffer(64);
        buf.append(this.mimeType);
        if (this.params != null) {
            buf.append("; ");
            BasicHeaderValueFormatter.INSTANCE.formatParameters(buf, this.params, false);
        }
        else if (this.charset != null) {
            buf.append("; charset=");
            buf.append(this.charset.name());
        }
        return buf.toString();
    }
    
    private static boolean valid(final String s) {
        for (int i = 0; i < s.length(); ++i) {
            final char ch = s.charAt(i);
            if (ch == '\"' || ch == ',' || ch == ';') {
                return false;
            }
        }
        return true;
    }
    
    public static ContentType create(final String mimeType, final Charset charset) {
        final String type = Args.notBlank(mimeType, "MIME type").toLowerCase(Locale.US);
        Args.check(valid(type), "MIME type may not contain reserved characters");
        return new ContentType(type, charset);
    }
    
    public static ContentType create(final String mimeType) {
        return new ContentType(mimeType, (Charset)null);
    }
    
    public static ContentType create(final String mimeType, final String charset) throws UnsupportedCharsetException {
        return create(mimeType, TextUtils.isBlank(charset) ? null : Charset.forName(charset));
    }
    
    private static ContentType create(final HeaderElement helem) {
        final String mimeType = helem.getName();
        final NameValuePair[] params = helem.getParameters();
        return new ContentType(mimeType, (NameValuePair[])((params != null && params.length > 0) ? params : null));
    }
    
    public static ContentType parse(final String s) throws ParseException, UnsupportedCharsetException {
        Args.notNull(s, "Content type");
        final CharArrayBuffer buf = new CharArrayBuffer(s.length());
        buf.append(s);
        final ParserCursor cursor = new ParserCursor(0, s.length());
        final HeaderElement[] elements = BasicHeaderValueParser.INSTANCE.parseElements(buf, cursor);
        if (elements.length > 0) {
            return create(elements[0]);
        }
        throw new ParseException("Invalid content type: " + s);
    }
    
    public static ContentType get(final HttpEntity entity) throws ParseException, UnsupportedCharsetException {
        if (entity == null) {
            return null;
        }
        final Header header = entity.getContentType();
        if (header != null) {
            final HeaderElement[] elements = header.getElements();
            if (elements.length > 0) {
                return create(elements[0]);
            }
        }
        return null;
    }
    
    public static ContentType getOrDefault(final HttpEntity entity) throws ParseException, UnsupportedCharsetException {
        final ContentType contentType = get(entity);
        return (contentType != null) ? contentType : ContentType.DEFAULT_TEXT;
    }
    
    public ContentType withCharset(final Charset charset) {
        return create(this.getMimeType(), charset);
    }
    
    public ContentType withCharset(final String charset) {
        return create(this.getMimeType(), charset);
    }
    
    static {
        APPLICATION_ATOM_XML = create("application/atom+xml", Consts.ISO_8859_1);
        APPLICATION_FORM_URLENCODED = create("application/x-www-form-urlencoded", Consts.ISO_8859_1);
        APPLICATION_JSON = create("application/json", Consts.UTF_8);
        APPLICATION_OCTET_STREAM = create("application/octet-stream", (Charset)null);
        APPLICATION_SVG_XML = create("application/svg+xml", Consts.ISO_8859_1);
        APPLICATION_XHTML_XML = create("application/xhtml+xml", Consts.ISO_8859_1);
        APPLICATION_XML = create("application/xml", Consts.ISO_8859_1);
        MULTIPART_FORM_DATA = create("multipart/form-data", Consts.ISO_8859_1);
        TEXT_HTML = create("text/html", Consts.ISO_8859_1);
        TEXT_PLAIN = create("text/plain", Consts.ISO_8859_1);
        TEXT_XML = create("text/xml", Consts.ISO_8859_1);
        WILDCARD = create("*/*", (Charset)null);
        DEFAULT_TEXT = ContentType.TEXT_PLAIN;
        DEFAULT_BINARY = ContentType.APPLICATION_OCTET_STREAM;
    }
}
