// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.message;

import org.apache.http.NameValuePair;
import org.apache.http.HeaderElement;
import org.apache.http.util.CharArrayBuffer;

public interface HeaderValueFormatter
{
    CharArrayBuffer formatElements(final CharArrayBuffer p0, final HeaderElement[] p1, final boolean p2);
    
    CharArrayBuffer formatHeaderElement(final CharArrayBuffer p0, final HeaderElement p1, final boolean p2);
    
    CharArrayBuffer formatParameters(final CharArrayBuffer p0, final NameValuePair[] p1, final boolean p2);
    
    CharArrayBuffer formatNameValuePair(final CharArrayBuffer p0, final NameValuePair p1, final boolean p2);
}
