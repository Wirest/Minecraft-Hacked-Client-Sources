// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.entity;

import java.io.IOException;
import org.apache.http.message.BasicHeader;
import org.apache.http.Header;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpEntity;

@NotThreadSafe
public abstract class AbstractHttpEntity implements HttpEntity
{
    protected static final int OUTPUT_BUFFER_SIZE = 4096;
    protected Header contentType;
    protected Header contentEncoding;
    protected boolean chunked;
    
    protected AbstractHttpEntity() {
    }
    
    public Header getContentType() {
        return this.contentType;
    }
    
    public Header getContentEncoding() {
        return this.contentEncoding;
    }
    
    public boolean isChunked() {
        return this.chunked;
    }
    
    public void setContentType(final Header contentType) {
        this.contentType = contentType;
    }
    
    public void setContentType(final String ctString) {
        Header h = null;
        if (ctString != null) {
            h = new BasicHeader("Content-Type", ctString);
        }
        this.setContentType(h);
    }
    
    public void setContentEncoding(final Header contentEncoding) {
        this.contentEncoding = contentEncoding;
    }
    
    public void setContentEncoding(final String ceString) {
        Header h = null;
        if (ceString != null) {
            h = new BasicHeader("Content-Encoding", ceString);
        }
        this.setContentEncoding(h);
    }
    
    public void setChunked(final boolean b) {
        this.chunked = b;
    }
    
    @Deprecated
    public void consumeContent() throws IOException {
    }
}
