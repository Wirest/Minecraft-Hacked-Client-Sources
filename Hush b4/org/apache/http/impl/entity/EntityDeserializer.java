// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.entity;

import org.apache.http.HttpEntity;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.Header;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.IdentityInputStream;
import java.io.InputStream;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.annotation.Immutable;

@Deprecated
@Immutable
public class EntityDeserializer
{
    private final ContentLengthStrategy lenStrategy;
    
    public EntityDeserializer(final ContentLengthStrategy lenStrategy) {
        this.lenStrategy = Args.notNull(lenStrategy, "Content length strategy");
    }
    
    protected BasicHttpEntity doDeserialize(final SessionInputBuffer inbuffer, final HttpMessage message) throws HttpException, IOException {
        final BasicHttpEntity entity = new BasicHttpEntity();
        final long len = this.lenStrategy.determineLength(message);
        if (len == -2L) {
            entity.setChunked(true);
            entity.setContentLength(-1L);
            entity.setContent(new ChunkedInputStream(inbuffer));
        }
        else if (len == -1L) {
            entity.setChunked(false);
            entity.setContentLength(-1L);
            entity.setContent(new IdentityInputStream(inbuffer));
        }
        else {
            entity.setChunked(false);
            entity.setContentLength(len);
            entity.setContent(new ContentLengthInputStream(inbuffer, len));
        }
        final Header contentTypeHeader = message.getFirstHeader("Content-Type");
        if (contentTypeHeader != null) {
            entity.setContentType(contentTypeHeader);
        }
        final Header contentEncodingHeader = message.getFirstHeader("Content-Encoding");
        if (contentEncodingHeader != null) {
            entity.setContentEncoding(contentEncodingHeader);
        }
        return entity;
    }
    
    public HttpEntity deserialize(final SessionInputBuffer inbuffer, final HttpMessage message) throws HttpException, IOException {
        Args.notNull(inbuffer, "Session input buffer");
        Args.notNull(message, "HTTP message");
        return this.doDeserialize(inbuffer, message);
    }
}
