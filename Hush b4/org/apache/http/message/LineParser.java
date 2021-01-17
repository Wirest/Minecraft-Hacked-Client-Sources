// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.message;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.RequestLine;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.util.CharArrayBuffer;

public interface LineParser
{
    ProtocolVersion parseProtocolVersion(final CharArrayBuffer p0, final ParserCursor p1) throws ParseException;
    
    boolean hasProtocolVersion(final CharArrayBuffer p0, final ParserCursor p1);
    
    RequestLine parseRequestLine(final CharArrayBuffer p0, final ParserCursor p1) throws ParseException;
    
    StatusLine parseStatusLine(final CharArrayBuffer p0, final ParserCursor p1) throws ParseException;
    
    Header parseHeader(final CharArrayBuffer p0) throws ParseException;
}
