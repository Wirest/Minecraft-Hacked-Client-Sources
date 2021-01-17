// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.message;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.HeaderElement;
import org.apache.http.util.CharArrayBuffer;

public interface HeaderValueParser
{
    HeaderElement[] parseElements(final CharArrayBuffer p0, final ParserCursor p1) throws ParseException;
    
    HeaderElement parseHeaderElement(final CharArrayBuffer p0, final ParserCursor p1) throws ParseException;
    
    NameValuePair[] parseParameters(final CharArrayBuffer p0, final ParserCursor p1) throws ParseException;
    
    NameValuePair parseNameValuePair(final CharArrayBuffer p0, final ParserCursor p1) throws ParseException;
}
