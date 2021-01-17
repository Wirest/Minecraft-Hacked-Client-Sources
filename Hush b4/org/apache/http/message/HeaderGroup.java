// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.message;

import org.apache.http.HeaderIterator;
import java.util.Locale;
import org.apache.http.util.CharArrayBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import org.apache.http.Header;
import java.util.List;
import org.apache.http.annotation.NotThreadSafe;
import java.io.Serializable;

@NotThreadSafe
public class HeaderGroup implements Cloneable, Serializable
{
    private static final long serialVersionUID = 2608834160639271617L;
    private final List<Header> headers;
    
    public HeaderGroup() {
        this.headers = new ArrayList<Header>(16);
    }
    
    public void clear() {
        this.headers.clear();
    }
    
    public void addHeader(final Header header) {
        if (header == null) {
            return;
        }
        this.headers.add(header);
    }
    
    public void removeHeader(final Header header) {
        if (header == null) {
            return;
        }
        this.headers.remove(header);
    }
    
    public void updateHeader(final Header header) {
        if (header == null) {
            return;
        }
        for (int i = 0; i < this.headers.size(); ++i) {
            final Header current = this.headers.get(i);
            if (current.getName().equalsIgnoreCase(header.getName())) {
                this.headers.set(i, header);
                return;
            }
        }
        this.headers.add(header);
    }
    
    public void setHeaders(final Header[] headers) {
        this.clear();
        if (headers == null) {
            return;
        }
        Collections.addAll(this.headers, headers);
    }
    
    public Header getCondensedHeader(final String name) {
        final Header[] hdrs = this.getHeaders(name);
        if (hdrs.length == 0) {
            return null;
        }
        if (hdrs.length == 1) {
            return hdrs[0];
        }
        final CharArrayBuffer valueBuffer = new CharArrayBuffer(128);
        valueBuffer.append(hdrs[0].getValue());
        for (int i = 1; i < hdrs.length; ++i) {
            valueBuffer.append(", ");
            valueBuffer.append(hdrs[i].getValue());
        }
        return new BasicHeader(name.toLowerCase(Locale.ENGLISH), valueBuffer.toString());
    }
    
    public Header[] getHeaders(final String name) {
        final List<Header> headersFound = new ArrayList<Header>();
        for (int i = 0; i < this.headers.size(); ++i) {
            final Header header = this.headers.get(i);
            if (header.getName().equalsIgnoreCase(name)) {
                headersFound.add(header);
            }
        }
        return headersFound.toArray(new Header[headersFound.size()]);
    }
    
    public Header getFirstHeader(final String name) {
        for (int i = 0; i < this.headers.size(); ++i) {
            final Header header = this.headers.get(i);
            if (header.getName().equalsIgnoreCase(name)) {
                return header;
            }
        }
        return null;
    }
    
    public Header getLastHeader(final String name) {
        for (int i = this.headers.size() - 1; i >= 0; --i) {
            final Header header = this.headers.get(i);
            if (header.getName().equalsIgnoreCase(name)) {
                return header;
            }
        }
        return null;
    }
    
    public Header[] getAllHeaders() {
        return this.headers.toArray(new Header[this.headers.size()]);
    }
    
    public boolean containsHeader(final String name) {
        for (int i = 0; i < this.headers.size(); ++i) {
            final Header header = this.headers.get(i);
            if (header.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    public HeaderIterator iterator() {
        return new BasicListHeaderIterator(this.headers, null);
    }
    
    public HeaderIterator iterator(final String name) {
        return new BasicListHeaderIterator(this.headers, name);
    }
    
    public HeaderGroup copy() {
        final HeaderGroup clone = new HeaderGroup();
        clone.headers.addAll(this.headers);
        return clone;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    @Override
    public String toString() {
        return this.headers.toString();
    }
}
