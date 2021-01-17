// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.annotation.NotThreadSafe;
import java.io.Serializable;
import org.apache.http.FormattedHeader;

@NotThreadSafe
public class BufferedHeader implements FormattedHeader, Cloneable, Serializable
{
    private static final long serialVersionUID = -2768352615787625448L;
    private final String name;
    private final CharArrayBuffer buffer;
    private final int valuePos;
    
    public BufferedHeader(final CharArrayBuffer buffer) throws ParseException {
        Args.notNull(buffer, "Char array buffer");
        final int colon = buffer.indexOf(58);
        if (colon == -1) {
            throw new ParseException("Invalid header: " + buffer.toString());
        }
        final String s = buffer.substringTrimmed(0, colon);
        if (s.length() == 0) {
            throw new ParseException("Invalid header: " + buffer.toString());
        }
        this.buffer = buffer;
        this.name = s;
        this.valuePos = colon + 1;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getValue() {
        return this.buffer.substringTrimmed(this.valuePos, this.buffer.length());
    }
    
    public HeaderElement[] getElements() throws ParseException {
        final ParserCursor cursor = new ParserCursor(0, this.buffer.length());
        cursor.updatePos(this.valuePos);
        return BasicHeaderValueParser.INSTANCE.parseElements(this.buffer, cursor);
    }
    
    public int getValuePos() {
        return this.valuePos;
    }
    
    public CharArrayBuffer getBuffer() {
        return this.buffer;
    }
    
    @Override
    public String toString() {
        return this.buffer.toString();
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
