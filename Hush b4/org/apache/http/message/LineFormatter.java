// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.message;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.RequestLine;
import org.apache.http.ProtocolVersion;
import org.apache.http.util.CharArrayBuffer;

public interface LineFormatter
{
    CharArrayBuffer appendProtocolVersion(final CharArrayBuffer p0, final ProtocolVersion p1);
    
    CharArrayBuffer formatRequestLine(final CharArrayBuffer p0, final RequestLine p1);
    
    CharArrayBuffer formatStatusLine(final CharArrayBuffer p0, final StatusLine p1);
    
    CharArrayBuffer formatHeader(final CharArrayBuffer p0, final Header p1);
}
