// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import org.apache.http.protocol.HTTP;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ParseException;
import java.util.List;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.NameValuePair;
import java.util.ArrayList;
import org.apache.http.util.Args;
import org.apache.http.HeaderElement;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.annotation.Immutable;

@Immutable
public class NetscapeDraftHeaderParser
{
    public static final NetscapeDraftHeaderParser DEFAULT;
    
    public HeaderElement parseHeader(final CharArrayBuffer buffer, final ParserCursor cursor) throws ParseException {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        final NameValuePair nvp = this.parseNameValuePair(buffer, cursor);
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        while (!cursor.atEnd()) {
            final NameValuePair param = this.parseNameValuePair(buffer, cursor);
            params.add(param);
        }
        return new BasicHeaderElement(nvp.getName(), nvp.getValue(), params.toArray(new NameValuePair[params.size()]));
    }
    
    private NameValuePair parseNameValuePair(final CharArrayBuffer buffer, final ParserCursor cursor) {
        boolean terminated = false;
        int pos = cursor.getPos();
        final int indexFrom = cursor.getPos();
        final int indexTo = cursor.getUpperBound();
        String name = null;
        while (pos < indexTo) {
            final char ch = buffer.charAt(pos);
            if (ch == '=') {
                break;
            }
            if (ch == ';') {
                terminated = true;
                break;
            }
            ++pos;
        }
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
            return new BasicNameValuePair(name, null);
        }
        String value = null;
        int i1 = pos;
        while (pos < indexTo) {
            final char ch2 = buffer.charAt(pos);
            if (ch2 == ';') {
                terminated = true;
                break;
            }
            ++pos;
        }
        int i2;
        for (i2 = pos; i1 < i2 && HTTP.isWhitespace(buffer.charAt(i1)); ++i1) {}
        while (i2 > i1 && HTTP.isWhitespace(buffer.charAt(i2 - 1))) {
            --i2;
        }
        value = buffer.substring(i1, i2);
        if (terminated) {
            ++pos;
        }
        cursor.updatePos(pos);
        return new BasicNameValuePair(name, value);
    }
    
    static {
        DEFAULT = new NetscapeDraftHeaderParser();
    }
}
