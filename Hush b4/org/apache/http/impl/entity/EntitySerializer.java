// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.entity;

import org.apache.http.HttpEntity;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.impl.io.ContentLengthOutputStream;
import org.apache.http.impl.io.IdentityOutputStream;
import org.apache.http.impl.io.ChunkedOutputStream;
import java.io.OutputStream;
import org.apache.http.HttpMessage;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.annotation.Immutable;

@Deprecated
@Immutable
public class EntitySerializer
{
    private final ContentLengthStrategy lenStrategy;
    
    public EntitySerializer(final ContentLengthStrategy lenStrategy) {
        this.lenStrategy = Args.notNull(lenStrategy, "Content length strategy");
    }
    
    protected OutputStream doSerialize(final SessionOutputBuffer outbuffer, final HttpMessage message) throws HttpException, IOException {
        final long len = this.lenStrategy.determineLength(message);
        if (len == -2L) {
            return new ChunkedOutputStream(outbuffer);
        }
        if (len == -1L) {
            return new IdentityOutputStream(outbuffer);
        }
        return new ContentLengthOutputStream(outbuffer, len);
    }
    
    public void serialize(final SessionOutputBuffer outbuffer, final HttpMessage message, final HttpEntity entity) throws HttpException, IOException {
        Args.notNull(outbuffer, "Session output buffer");
        Args.notNull(message, "HTTP message");
        Args.notNull(entity, "HTTP entity");
        final OutputStream outstream = this.doSerialize(outbuffer, message);
        entity.writeTo(outstream);
        outstream.close();
    }
}
