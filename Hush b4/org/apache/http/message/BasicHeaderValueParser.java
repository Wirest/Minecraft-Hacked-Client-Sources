// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.message;

import org.apache.http.protocol.HTTP;
import org.apache.http.NameValuePair;
import java.util.List;
import java.util.ArrayList;
import org.apache.http.ParseException;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.Args;
import org.apache.http.HeaderElement;
import org.apache.http.annotation.Immutable;

@Immutable
public class BasicHeaderValueParser implements HeaderValueParser
{
    @Deprecated
    public static final BasicHeaderValueParser DEFAULT;
    public static final BasicHeaderValueParser INSTANCE;
    private static final char PARAM_DELIMITER = ';';
    private static final char ELEM_DELIMITER = ',';
    private static final char[] ALL_DELIMITERS;
    
    public static HeaderElement[] parseElements(final String value, final HeaderValueParser parser) throws ParseException {
        Args.notNull(value, "Value");
        final CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        final ParserCursor cursor = new ParserCursor(0, value.length());
        return ((parser != null) ? parser : BasicHeaderValueParser.INSTANCE).parseElements(buffer, cursor);
    }
    
    public HeaderElement[] parseElements(final CharArrayBuffer buffer, final ParserCursor cursor) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        final List<HeaderElement> elements = new ArrayList<HeaderElement>();
        while (!cursor.atEnd()) {
            final HeaderElement element = this.parseHeaderElement(buffer, cursor);
            if (element.getName().length() != 0 || element.getValue() != null) {
                elements.add(element);
            }
        }
        return elements.toArray(new HeaderElement[elements.size()]);
    }
    
    public static HeaderElement parseHeaderElement(final String value, final HeaderValueParser parser) throws ParseException {
        Args.notNull(value, "Value");
        final CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        final ParserCursor cursor = new ParserCursor(0, value.length());
        return ((parser != null) ? parser : BasicHeaderValueParser.INSTANCE).parseHeaderElement(buffer, cursor);
    }
    
    public HeaderElement parseHeaderElement(final CharArrayBuffer buffer, final ParserCursor cursor) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        final NameValuePair nvp = this.parseNameValuePair(buffer, cursor);
        NameValuePair[] params = null;
        if (!cursor.atEnd()) {
            final char ch = buffer.charAt(cursor.getPos() - 1);
            if (ch != ',') {
                params = this.parseParameters(buffer, cursor);
            }
        }
        return this.createHeaderElement(nvp.getName(), nvp.getValue(), params);
    }
    
    protected HeaderElement createHeaderElement(final String name, final String value, final NameValuePair[] params) {
        return new BasicHeaderElement(name, value, params);
    }
    
    public static NameValuePair[] parseParameters(final String value, final HeaderValueParser parser) throws ParseException {
        Args.notNull(value, "Value");
        final CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        final ParserCursor cursor = new ParserCursor(0, value.length());
        return ((parser != null) ? parser : BasicHeaderValueParser.INSTANCE).parseParameters(buffer, cursor);
    }
    
    public NameValuePair[] parseParameters(final CharArrayBuffer buffer, final ParserCursor cursor) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        int pos = cursor.getPos();
        for (int indexTo = cursor.getUpperBound(); pos < indexTo; ++pos) {
            final char ch = buffer.charAt(pos);
            if (!HTTP.isWhitespace(ch)) {
                break;
            }
        }
        cursor.updatePos(pos);
        if (cursor.atEnd()) {
            return new NameValuePair[0];
        }
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        while (!cursor.atEnd()) {
            final NameValuePair param = this.parseNameValuePair(buffer, cursor);
            params.add(param);
            final char ch2 = buffer.charAt(cursor.getPos() - 1);
            if (ch2 == ',') {
                break;
            }
        }
        return params.toArray(new NameValuePair[params.size()]);
    }
    
    public static NameValuePair parseNameValuePair(final String value, final HeaderValueParser parser) throws ParseException {
        Args.notNull(value, "Value");
        final CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        final ParserCursor cursor = new ParserCursor(0, value.length());
        return ((parser != null) ? parser : BasicHeaderValueParser.INSTANCE).parseNameValuePair(buffer, cursor);
    }
    
    public NameValuePair parseNameValuePair(final CharArrayBuffer buffer, final ParserCursor cursor) {
        return this.parseNameValuePair(buffer, cursor, BasicHeaderValueParser.ALL_DELIMITERS);
    }
    
    private static boolean isOneOf(final char ch, final char[] chs) {
        if (chs != null) {
            for (final char ch2 : chs) {
                if (ch == ch2) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public NameValuePair parseNameValuePair(final CharArrayBuffer buffer, final ParserCursor cursor, final char[] delimiters) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        boolean terminated = false;
        int pos = cursor.getPos();
        final int indexFrom = cursor.getPos();
        int indexTo;
        for (indexTo = cursor.getUpperBound(); pos < indexTo; ++pos) {
            final char ch = buffer.charAt(pos);
            if (ch == '=') {
                break;
            }
            if (isOneOf(ch, delimiters)) {
                terminated = true;
                break;
            }
        }
        String name;
        if (pos == indexTo) {
            terminated = true;
            name = buffer.substringTrimmed(indexFrom, indexTo);
        }
        else {
            name = buffer.substringTrimmed(indexFrom, pos);
            ++pos;
        }
        if (terminated) {
            cursor.updatePos(pos);
            return this.createNameValuePair(name, null);
        }
        int i1 = pos;
        boolean qouted = false;
        boolean escaped = false;
        while (pos < indexTo) {
            final char ch2 = buffer.charAt(pos);
            if (ch2 == '\"' && !escaped) {
                qouted = !qouted;
            }
            if (!qouted && !escaped && isOneOf(ch2, delimiters)) {
                terminated = true;
                break;
            }
            escaped = (!escaped && qouted && ch2 == '\\');
            ++pos;
        }
        int i2;
        for (i2 = pos; i1 < i2 && HTTP.isWhitespace(buffer.charAt(i1)); ++i1) {}
        while (i2 > i1 && HTTP.isWhitespace(buffer.charAt(i2 - 1))) {
            --i2;
        }
        if (i2 - i1 >= 2 && buffer.charAt(i1) == '\"' && buffer.charAt(i2 - 1) == '\"') {
            ++i1;
            --i2;
        }
        final String value = buffer.substring(i1, i2);
        if (terminated) {
            ++pos;
        }
        cursor.updatePos(pos);
        return this.createNameValuePair(name, value);
    }
    
    protected NameValuePair createNameValuePair(final String name, final String value) {
        return new BasicNameValuePair(name, value);
    }
    
    static {
        DEFAULT = new BasicHeaderValueParser();
        INSTANCE = new BasicHeaderValueParser();
        ALL_DELIMITERS = new char[] { ';', ',' };
    }
}
